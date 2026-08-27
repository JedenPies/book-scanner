import { Component, computed, ElementRef, inject, input, signal, ViewChild } from '@angular/core';
import {
  ExportCompleteSseEvent,
  ExportDto,
  ExportFormat,
  ScanCreatedSseEvent,
  ScanDeletedSseEvent,
  ScanDto,
  ScanUpdatedSseEvent,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { LowerCasePipe } from '@angular/common';
import { ToastService } from '../../services/toast.service';
import { ClipboardService } from '../../services/clipboard.service';

export interface ScanToDelete {
  scanId: string;
  timeoutHandler: number;
}

@Component({
  selector: 'app-scanner',
  imports: [LowerCasePipe],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.scss',
})
export class EditorComponent {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;

  sessionId = input.required<string>();

  currentExport = signal<ExportDto | null>(null);
  scans = signal<ScanDto[]>([]);
  scansToDelete = signal<ScanToDelete[]>([]);
  showScrollDown = signal<boolean>(false);
  showScrollUp = signal<boolean>(false);
  isExportModalOpen = signal<boolean>(false);
  expandedScanId = signal<string | null>(null);

  exportState = computed(() => { return this.computedExportState() });
  isExportProcessing = computed(() => {
    const status = this.currentExport()?.status;
    return status === 'REQUESTED' || status === 'PROCESSING';
  });

  backendService = inject(ScannerBackendService);
  toastService = inject(ToastService);
  clipboardService = inject(ClipboardService);

  private eventSource?: EventSource;

  ngOnInit() {
    this.loadScansAndExport();
    this.initSseStream();
  }

  ngOnDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

  toggleExpand(scanId: string) {
    this.expandedScanId.update(current => current === scanId ? null : scanId);
  }

  copyUrlToClipboard() {
    const currentUrl = window.location.href;
    this.clipboardService.copyToClipboard(currentUrl);
  }

  private computedExportState() {
    const exp = this.currentExport();

    // Brak eksportu
    if (!exp) return null;

    // W zależności od statusu zwracamy odpowiedni widok
    switch (exp.status) {
      case 'REQUESTED':
      case 'PROCESSING':
        return {
          icon: '', // Spinner załatwimy klasą CSS (jak w tabeli)
          text: `Przygotowuję plik ${exp.format}...`,
          cssClass: 'status-pending', // Klasy wzięte z Twoich odznak
          showSpinner: true,
          isClickable: false
        };
      case 'SUCCEED':
        return {
          icon: '📥',
          text: `Pobierz gotowy plik ${exp.format}`,
          cssClass: 'status-found', // Użyjemy zielonej klasy dla sukcesu
          showSpinner: false,
          isClickable: true
        };
      case 'FAILED':
        return {
          icon: '❌',
          text: `Błąd eksportu ${exp.format}. Spróbuj ponownie.`,
          cssClass: 'status-failed', // Czerwona klasa
          showSpinner: false,
          isClickable: false
        };
      default:
        return null;
    }
  }

  openExportModal() {
    this.isExportModalOpen.set(true);
  }

  closeExportModal() {
    this.isExportModalOpen.set(false);
  }

  requestExport(exportFormat: string) {
    const format = exportFormat as ExportFormat;
    this.currentExport.set(null);
    this.backendService.requestExport(this.sessionId(), format).subscribe({
      next: (result) => {
        this.currentExport.set(result);
      },
    });
    this.closeExportModal();
    this.toastService.show(`Export request sent. Please wait.`)
  }

  downloadExport() {
    const exp = this.currentExport();
    if (exp && exp.status === 'SUCCEED') {
      window.open(`/api/sessions/${this.sessionId()}/export/data`);
    }
  }

  deleteScan(scanId: string) {
    if (this.isIntendedToDelete(scanId)) return;
    let timeoutHandler = setTimeout(() => this.confirmDelete(scanId), 10000);
    let scanToDelete: ScanToDelete = { scanId, timeoutHandler };
    this.scansToDelete.update((current) => [...current, scanToDelete]);
  }

  isIntendedToDelete(scanId: string) {
    return this.scansToDelete().some((scanToDelete) => scanToDelete.scanId === scanId);
  }

  cancelDelete(scanId: string) {
    if (!this.isIntendedToDelete(scanId)) return;
    this.clearTimeoutAndRemoveFromDeleteList(scanId);
  }

  private confirmDelete(scanId: string) {
    this.clearTimeoutAndRemoveFromDeleteList(scanId);
    this.backendService.deleteScan(this.sessionId(), scanId).subscribe({
      error: (err) => {
        this.toastService.show('Cannot delete scan.', 'error', 10000);
        console.error('Cannot delete scan. ', err);
      },
    });
  }

  private clearTimeoutAndRemoveFromDeleteList(scanId: string) {
    const foundScanToDelete = this.scansToDelete().find((element) => element.scanId === scanId);
    if (foundScanToDelete && foundScanToDelete.timeoutHandler) {
      clearTimeout(foundScanToDelete.timeoutHandler);
    }
    this.scansToDelete.update((current) =>
      current.filter((scanToDelete) => scanToDelete.scanId !== scanId),
    );
  }

  private loadScansAndExport() {
    const sessionId = this.sessionId();
    if (sessionId) {
      this.backendService.retrieveAllScans(sessionId).subscribe({
        next: (result) => {
          const sortedScans = result.sort(
            (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
          );
          this.scans.set(sortedScans);
        },
      });
      this.backendService.loadExport(sessionId).subscribe(({
        next: (result) => {
          this.currentExport.set(result);
        }
      }))
    }
  }

  retryScan(scanId: string) {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.backendService.retryScan(sessionId, scanId).subscribe({
      error: (err) => {
        console.error('Błąd podczas ponawiania skanowania:', err);
      },
    });
  }

  private initSseStream() {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.eventSource = new EventSource(`/api/sessions/${sessionId}/events-stream`);
    this.eventSource.addEventListener('SCAN_CREATED', (event: MessageEvent) => {
      const eventDto: ScanCreatedSseEvent = JSON.parse(event.data);
      const newScan = eventDto.scan;
      this.scans.update((currentScans) => [newScan, ...currentScans]);
    });
    this.eventSource.addEventListener('SCAN_UPDATED', (event: MessageEvent) => {
      const eventDto: ScanUpdatedSseEvent = JSON.parse(event.data);
      const updatedScan = eventDto.scan;
      this.scans.update((currentScans) =>
        currentScans.map((scan) => (scan.id === updatedScan.id ? updatedScan : scan)),
      );
    });
    this.eventSource.addEventListener('SCAN_DELETED', (event: MessageEvent) => {
      const eventDto: ScanDeletedSseEvent = JSON.parse(event.data);
      const updatedScan = eventDto.scan;
      this.scans.update((currentScans) =>
        currentScans.filter((scan) => scan.id !== updatedScan.id),
      );
      this.toastService.show(
        `Deleted ${updatedScan.bookDetails?.title || 'ISBN: ' + updatedScan.isbn}`,
        'info',
      );
    });
    this.eventSource.addEventListener('EXPORT_COMPLETE', (event: MessageEvent) => {
      const eventDto: ExportCompleteSseEvent = JSON.parse(event.data);
      this.currentExport.set(eventDto.export);
      this.toastService.show('Export ready!', 'success');

    });
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
    };
  }

  onScroll() {
    this.checkScroll();
  }

  checkScroll() {
    if (!this.scrollContainer) return;
    const { scrollTop, scrollHeight, clientHeight } = this.scrollContainer.nativeElement;
    const hasMoreToScroll =
      scrollHeight > clientHeight && scrollHeight - scrollTop - clientHeight > 10;
    this.showScrollDown.set(hasMoreToScroll);
    this.showScrollUp.set(scrollTop > 20);
  }

  scrollToBottom() {
    if (!this.scrollContainer) return;
    this.scrollContainer.nativeElement.scrollTo({
      top: this.scrollContainer.nativeElement.scrollHeight,
      behavior: 'smooth',
    });
  }

  scrollToTop() {
    if (!this.scrollContainer) return;
    this.scrollContainer.nativeElement.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }
}
