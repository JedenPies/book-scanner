import { Component, inject, input, output } from '@angular/core';
import { ClipboardService } from '../../../services/clipboard.service';
import { ExportService } from '../../../services/export.service';

@Component({
  selector: 'app-editor-header',
  standalone: true,
  templateUrl: './editor-header.component.html',
  styleUrl: './editor-header.component.scss',
})
export class EditorHeaderComponent {
  exportService = inject(ExportService);
  clipboardService = inject(ClipboardService);

  sessionId = input.required<string>();

  openExport = output<void>();
  openManualIsbn = output<void>();

  copyUrlToClipboard() {
    const currentUrl = window.location.href;
    this.clipboardService.copyToClipboard(currentUrl, true, 'URL');
  }
}
