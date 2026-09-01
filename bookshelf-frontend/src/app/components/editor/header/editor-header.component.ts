import { Component, computed, inject, input, output } from '@angular/core';
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
  shareCode = input.required<string>();
  shareCodeFormatted = computed<string>(() => this.shareCode().replace(/(.{3})(.{3})/g, '$1-$2'));

  openExport = output<void>();
  openManualIsbn = output<void>();
  openAttachScanner = output<void>();

  copyShareCodeToClipboard() {
    const currentShareCode = this.shareCode();
    this.clipboardService.copyToClipboard(currentShareCode, true, 'Share Code');
  }

  copyUrlToClipboard() {
    const currentUrl = window.location.href;
    this.clipboardService.copyToClipboard(currentUrl, true, 'URL');
  }
}
