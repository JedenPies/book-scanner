import { Component, inject, input, output } from '@angular/core';
import { ExportState } from '../editor.model';
import { ClipboardService } from '../../../services/clipboard.service';

@Component({
  selector: 'app-editor-header',
  standalone: true,
  templateUrl: './editor-header.component.html',
  styleUrl: './editor-header.component.scss',
})
export class EditorHeaderComponent {

  clipboardService = inject(ClipboardService);

  sessionId = input.required<string>();
  exportState = input<ExportState | null>(null);
  isExportProcessing = input<boolean>(false);

  openExport = output<void>();
  openManualIsbn = output<void>();
  downloadExport = output<void>();

  copyUrlToClipboard() {
    const currentUrl = window.location.href;
    this.clipboardService.copyToClipboard(currentUrl, true, 'URL');
  }
}
