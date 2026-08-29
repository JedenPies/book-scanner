import { Component, HostListener, inject, input, signal } from '@angular/core';
import {
  EditScanCommandDto,
  ExportCompleteSseEvent,
  ScanCreatedSseEvent,
  ScanDeletedSseEvent,
  ScanDto,
  ScanUpdatedSseEvent,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { ToastService } from '../../services/toast.service';
import { EditorHeaderComponent } from './header/editor-header.component';
import { ExportService } from '../../services/export.service';
import { ManualIsbnModalComponent } from './manual-isbn-modal/manual-isbn-modal.component';
import { ExportModalComponent } from './export-modal/export-modal.component';
import { ScanTableComponent } from './scan-table/scan-table.component';

export interface ScanToDelete {
  scanId: string;
  timeoutHandler: number;
}

@Component({
  selector: 'app-scanner',
  imports: [
    EditorHeaderComponent,
    ScanTableComponent,
    ManualIsbnModalComponent,
    ExportModalComponent,
  ],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.scss',
})
export class EditorComponent {
  exportService = inject(ExportService);
  backendService = inject(ScannerBackendService);
  toastService = inject(ToastService);

  sessionId = input.required<string>();
  isExportModalOpen = signal<boolean>(false);
  isManualIsbnModalOpen = signal<boolean>(false);

  scans = signal<ScanDto[]>([]);
  scansToDelete = signal<ScanToDelete[]>([]);

  private eventSource?: EventSource;

  ngOnInit() {
    this.loadScans();
    this.exportService.loadExport(this.sessionId());
    this.initSseStream();
  }

  ngOnDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

  handleUpdateScan(event: { scanId: string; command: EditScanCommandDto }) {
    this.backendService.modifyScan(this.sessionId(), event.scanId, event.command).subscribe({
      next: (updatedScan) => {
        this.scans.update((current) =>
          current.map((s) => (s.id === updatedScan.id ? updatedScan : s)),
        );
        this.exportService.invalidateExport();
        this.toastService.show('Book details updated', 'success');
      },
      error: () => this.toastService.show('Failed to update book', 'error'),
    });
  }

  @HostListener('window:keydown.control.space', ['$event'])
  handleCtrlSpace(event: Event) {
    if (this.isManualIsbnModalOpen() || this.isExportModalOpen()) {
      return;
    }

    event.preventDefault(); // Zapobiega domyślnemu scrollowaniu strony spacją
    this.openManualIsbnModal();
  }

  openManualIsbnModal() {
    this.isManualIsbnModalOpen.set(true);
  }

  addManualIsbn(isbn: string) {
    this.backendService.addScan(this.sessionId(), isbn).subscribe({
      error: () => this.toastService.show('Błąd dodawania ISBN', 'error'),
    });
  }

  deleteScan(scanId: string) {
    if (this.isIntendedToDelete(scanId)) return;
    const timeoutHandler = window.setTimeout(() => this.confirmDelete(scanId), 10000);
    const scanToDelete: ScanToDelete = { scanId, timeoutHandler };
    this.scansToDelete.update((current) => [...current, scanToDelete]);
  }

  isIntendedToDelete(scanId: string): boolean {
    return this.scansToDelete().some((scanToDelete) => scanToDelete.scanId === scanId);
  }

  cancelDelete(scanId: string) {
    if (!this.isIntendedToDelete(scanId)) return;
    this.clearTimeoutAndRemoveFromDeleteList(scanId);
  }

  retryScan(scanId: string) {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.backendService.retryScan(sessionId, scanId).subscribe({
      error: (err) => console.error('Błąd podczas ponawiania skanowania:', err),
    });
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
    if (foundScanToDelete?.timeoutHandler) {
      clearTimeout(foundScanToDelete.timeoutHandler);
    }
    this.scansToDelete.update((current) =>
      current.filter((scanToDelete) => scanToDelete.scanId !== scanId),
    );
  }

  private loadScans() {
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
    }
  }

  private initSseStream() {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.eventSource = new EventSource(`/api/sessions/${sessionId}/events-stream`);
    this.eventSource.addEventListener('SCAN_CREATED', (event: MessageEvent) => {
      const eventDto: ScanCreatedSseEvent = JSON.parse(event.data);
      this.scans.update((currentScans) => [eventDto.scan, ...currentScans]);
      this.exportService.invalidateExport();
    });
    this.eventSource.addEventListener('SCAN_UPDATED', (event: MessageEvent) => {
      const eventDto: ScanUpdatedSseEvent = JSON.parse(event.data);
      this.scans.update((currentScans) =>
        currentScans.map((scan) => (scan.id === eventDto.scan.id ? eventDto.scan : scan)),
      );
      this.exportService.invalidateExport();
    });
    this.eventSource.addEventListener('SCAN_DELETED', (event: MessageEvent) => {
      const eventDto: ScanDeletedSseEvent = JSON.parse(event.data);
      this.scans.update((currentScans) =>
        currentScans.filter((scan) => scan.id !== eventDto.scan.id),
      );
      this.exportService.invalidateExport();
      this.toastService.show(
        `Deleted ${eventDto.scan.bookDetails?.title || 'ISBN: ' + eventDto.scan.isbn}`,
        'info',
      );
    });
    this.eventSource.addEventListener('EXPORT_COMPLETE', (event: MessageEvent) => {
      const eventDto: ExportCompleteSseEvent = JSON.parse(event.data);
      this.exportService.handleSseComplete(eventDto.export);
    });
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
    };
  }
}

