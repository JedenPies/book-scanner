import { computed, inject, Injectable, signal } from '@angular/core';
import { ScannerBackendService } from './scanner-backend.service';
import { ToastService } from './toast.service';
import { ExportDto, ExportFormat } from '../models/backend.model';
import { ExportState } from '../components/editor/editor.model';

@Injectable({
  providedIn: 'root',
})
export class ExportService {

  private backendService = inject(ScannerBackendService);
  private toastService = inject(ToastService);

  currentExport = signal<ExportDto | null>(null);
  exportInvalidated = signal<boolean>(false);

  isProcessing = computed(() => {
    const status = this.currentExport()?.status;
    return status === 'REQUESTED' || status === 'PROCESSING';
  });

  exportState = computed<ExportState | null>(() => {
    const exp = this.currentExport();
    if (!exp) return null;

    switch (exp.status) {
      case 'REQUESTED':
      case 'PROCESSING':
        return {
          icon: '',
          text: `Przygotowuję plik ${exp.format}...`,
          cssClass: 'status-pending',
          showSpinner: true,
          isClickable: false,
        };
      case 'SUCCEED':
        return {
          icon: '📥',
          text: `Pobierz gotowy plik ${exp.format}`,
          cssClass: 'status-found',
          showSpinner: false,
          isClickable: true,
        };
      case 'FAILED':
        return {
          icon: '❌',
          text: `Błąd eksportu ${exp.format}. Spróbuj ponownie.`,
          cssClass: 'status-failed',
          showSpinner: false,
          isClickable: false,
        };
      default:
        return null;
    }
  });

  invalidateExport() {
    this.exportInvalidated.set(true);
  }

  loadExport(sessionId: string) {
    if (!sessionId) return;
    this.backendService.loadExport(sessionId).subscribe({
      next: (result) => this.currentExport.set(result),
      error: () => this.currentExport.set(null),
    });
  }

  requestExport(sessionId: string, format: ExportFormat) {
    this.exportInvalidated.set(false);
    this.currentExport.set(null);
    this.backendService.requestExport(sessionId, format).subscribe({
      next: (result) => {
        this.currentExport.set(result);
        this.toastService.show('Zlecenie eksportu wysłane. Proszę czekać.');
      },
      error: () => {
        this.toastService.show('Błąd podczas zlecenia eksportu', 'error');
      },
    });
  }

  handleSseComplete(exportDto: ExportDto) {
    this.currentExport.set(exportDto);
    if (this.exportInvalidated()) {
      this.toastService.show('Eksport gotowy, ale nieaktualny. Wygeneruj ponownie.', 'warning');
    } else {
      this.toastService.show('Eksport gotowy!', 'success');
    }
  }

  download(sessionId: string) {
    const exp = this.currentExport();
    if (exp && exp.status === 'SUCCEED') {
      window.open(`/api/sessions/${sessionId}/export/data`);
    }
  }
}
