import { computed, inject, Injectable, signal } from '@angular/core';
import { BackendService } from './backend.service';
import { ToastService } from './toast.service';
import { ExportDto, ExportFormat } from '../models/backend.model';
import { ExportState } from '../components/cataloging-session/cataloging-session.model';

@Injectable({
  providedIn: 'root',
})
export class ExportService {

  private backendService = inject(BackendService);
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
          text: `Preparing ${exp.format}...`,
          cssClass: 'status-pending',
          showSpinner: true,
          isClickable: false,
        };
      case 'SUCCEED':
        return {
          text: `Download your ${exp.format}`,
          cssClass: 'status-found',
          showSpinner: false,
          isClickable: true,
        };
      case 'FAILED':
        return {
          text: `Error exporting ${exp.format}. Please try again.`,
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
    this.backendService.loadExportForSession(sessionId).subscribe({
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
        this.toastService.show('Export requested. Please wait.');
      },
      error: () => {
        this.toastService.show('Error during export request.', 'error');
      },
    });
  }

  handleSseComplete(exportDto: ExportDto) {
    this.currentExport.set(exportDto);
    if (this.exportInvalidated()) {
      this.toastService.show('Your export is ready, but already outdated. Generate again.', 'warning');
    } else {
      this.toastService.show('Your export is ready!', 'success');
    }
  }

  download() {
    const exp = this.currentExport();
    const id = exp?.id;
    if (exp && exp.status === 'SUCCEED' && id) {
      window.open(`/api/exports/${id}/data`);
    }
  }
}
