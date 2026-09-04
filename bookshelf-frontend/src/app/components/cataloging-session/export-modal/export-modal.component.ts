import { Component, inject, input, output } from '@angular/core';
import { ExportService } from '../../../services/export.service';
import { ExportFormat } from '../../../models/backend.model';

@Component({
  selector: 'app-export-modal',
  standalone: true,
  templateUrl: './export-modal.component.html',
  styleUrl: './export-modal.component.scss',
})
export class ExportModalComponent {

  sessionId = input.required<string>();

  closed = output<void>();

  private exportService = inject(ExportService);

  selectFormat(format: ExportFormat) {
    this.exportService.requestExport(this.sessionId(), format);
    this.closed.emit();
  }

  close() {
    this.closed.emit();
  }
}
