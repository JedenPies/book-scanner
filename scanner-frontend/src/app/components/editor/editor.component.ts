import { Component, ElementRef, inject, input, signal, ViewChild } from '@angular/core';
import {
  ScanCreatedSseEvent,
  ScanDeletedSseEvent,
  ScanDto,
  ScanUpdatedSseEvent,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { LowerCasePipe } from '@angular/common';
import { ToastService } from '../../services/toast.service';

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
  scans = signal<ScanDto[]>([]);
  scansToDelete = signal<ScanToDelete[]>([]);
  showScrollDown = signal<boolean>(false);
  showScrollUp = signal<boolean>(false);

  backendService = inject(ScannerBackendService);
  toastService = inject(ToastService);

  private eventSource?: EventSource;

  ngOnInit() {
    this.loadScans();
    this.initSseStream();
  }

  ngOnDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

  deleteScan(scanId: string) {
    if (this.isIntendedToDelete(scanId)) return;
    let timeoutHandler = setTimeout(() => this.confirmDelete(scanId), 10000);
    let scanToDelete: ScanToDelete = { scanId, timeoutHandler };
    this.scansToDelete.update((current) => [...current, scanToDelete]);
  }

  isIntendedToDelete(scanId: string) {
    return this.scansToDelete().some(scanToDelete => scanToDelete.scanId === scanId);
  }

  cancelDelete(scanId: string) {
    if (!this.isIntendedToDelete(scanId)) return;
    this.clearTimeoutAndRemoveFromDeleteList(scanId);
  }

  private confirmDelete(scanId: string) {
    this.clearTimeoutAndRemoveFromDeleteList(scanId);
    this.backendService.deleteScan(this.sessionId(), scanId).subscribe({
      error: (err) => {
        this.toastService.show("Cannot delete scan.", "error", 10000);
        console.error("Cannot delete scan. ", err);
      }
    });
  }

  private clearTimeoutAndRemoveFromDeleteList(scanId: string) {
    const foundScanToDelete = this.scansToDelete().find(element => element.scanId === scanId);
    if (foundScanToDelete && foundScanToDelete.timeoutHandler) {
      clearTimeout(foundScanToDelete.timeoutHandler);
    }
    this.scansToDelete.update(
      (current) => current.filter((scanToDelete) => scanToDelete.scanId !== scanId));
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
      this.scans.update((currentScans) => currentScans.filter((scan) => scan.id !== updatedScan.id));
      this.toastService.show(
        `Deleted ${updatedScan.bookDetails?.title || 'ISBN: ' + updatedScan.isbn}`,
        'info',
      );
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
