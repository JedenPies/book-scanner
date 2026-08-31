import { Component, computed, HostListener, inject, input, signal } from '@angular/core';
import {
  EditScanCommandDto,
  ExportCompleteSseEvent,
  ScanCreatedSseEvent,
  ScanDto,
  ScansDeletedSseEvent,
  ScanUpdatedSseEvent,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { ToastService } from '../../services/toast.service';
import { EditorHeaderComponent } from './header/editor-header.component';
import { ExportService } from '../../services/export.service';
import { ManualIsbnModalComponent } from './manual-isbn-modal/manual-isbn-modal.component';
import { ExportModalComponent } from './export-modal/export-modal.component';
import { ScanTableComponent } from './scan-table/scan-table.component';
import { EditScanFormDialogComponent } from './edit-scan-modal/edit-scan-form-dialog.component';

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
    EditScanFormDialogComponent,
  ],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.scss',
  standalone: true,
})
export class EditorComponent {

  private readonly DELETE_TOAST_ID = 'batch-delete-toast';

  exportService = inject(ExportService);
  backendService = inject(ScannerBackendService);
  toastService = inject(ToastService);

  isModalOpen = computed<boolean>(() => {
    return this.isManualIsbnModalOpen() || this.isExportModalOpen() || this.editingScan() !== null;
  });
  scansToShow = computed<ScanDto[]>(() => {
    const deleIds = new Set(this.pendingDeletionScans().map((item) => item));
    return this.scans().filter((scan) => !deleIds.has(scan));
  });

  sessionId = input.required<string>();
  isExportModalOpen = signal<boolean>(false);
  isManualIsbnModalOpen = signal<boolean>(false);

  scans = signal<ScanDto[]>([]);
  scansToDelete = signal<ScanToDelete[]>([]);
  pendingDeletionScans = signal<ScanDto[]>([]);
  editingScan = signal<ScanDto | null>(null);

  private deleteTimeoutId?: any;

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

  handleUpdateScan(command: EditScanCommandDto) {
    const scan = this.editingScan();
    if (!scan) return;

    this.backendService.modifyScan(this.sessionId(), scan.id, command).subscribe({
      next: (updatedScan) => {
        this.scans.update((current) =>
          current.map((s) => (s.id === updatedScan.id ? updatedScan : s)),
        );
        this.toastService.show('Book details updated', 'success');
        this.editingScan.set(null);
      },
      error: () => this.toastService.show('Failed to update book', 'error'),
    });
  }

  @HostListener('window:keydown.control.space', ['$event'])
  handleCtrlSpace(event: Event) {
    if (this.isModalOpen()) return;
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

  retryScan(scanId: string) {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.backendService.retryScan(sessionId, scanId).subscribe({
      error: (err) => console.error('Błąd podczas ponawiania skanowania:', err),
    });
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
    this.eventSource.addEventListener('SCANS_DELETED', (event: MessageEvent) => {
      const eventDto: ScansDeletedSseEvent = JSON.parse(event.data);
      this.exportService.invalidateExport();
      this.toastService.show(`${eventDto.count} scans permanently deleted`, 'info');
    });
    this.eventSource.addEventListener('EXPORT_COMPLETE', (event: MessageEvent) => {
      const eventDto: ExportCompleteSseEvent = JSON.parse(event.data);
      this.exportService.handleSseComplete(eventDto.export);
    });
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
    };
  }

  deleteScan(scan: ScanDto) {
    this.pendingDeletionScans.update((list) => [...list, scan]);
    if (this.deleteTimeoutId) {
      clearTimeout(this.deleteTimeoutId);
    }

    const count = this.pendingDeletionScans().length;
    const message = count === 1 ? '1 scan deleted' : `${count} elements deleted`;
    this.toastService.show(
      message,
      'warning',
      {
        label: 'Cancel',
        run: () => this.cancelBatchDelete(),
      },
      this.DELETE_TOAST_ID,
      10000
    );

    this.deleteTimeoutId = setTimeout(() => {
      this.commitBatchDelete();
    }, 5000);
  }

  cancelBatchDelete() {
    if (this.deleteTimeoutId) {
      clearTimeout(this.deleteTimeoutId);
      this.deleteTimeoutId = undefined;
    }
    this.pendingDeletionScans.set([]);
    this.toastService.remove(this.DELETE_TOAST_ID);
  }

  private commitBatchDelete() {
    const scansToDelete = this.pendingDeletionScans();
    if (scansToDelete.length === 0) return;
    this.toastService.remove(this.DELETE_TOAST_ID);
    this.pendingDeletionScans.set([]);
    const ids = scansToDelete.map((s) => s.id);
    this.scans.update((current) => current.filter((s) => !ids.includes(s.id)));
    this.backendService.deleteScans(this.sessionId(), scansToDelete).subscribe({});
  }
}
