import { Component, ElementRef, inject, input, output, signal, ViewChild } from '@angular/core';
import { LowerCasePipe, NgOptimizedImage } from '@angular/common';
import { ClipboardService } from '../../../services/clipboard.service';
import { ScanDto } from '../../../models/backend.model';
import { ScanToDelete } from '../editor.component';

@Component({
  selector: 'app-scan-table',
  standalone: true,
  imports: [LowerCasePipe, NgOptimizedImage],
  templateUrl: './scan-table.component.html',
  styleUrl: './scan-table.component.scss',
})
export class ScanTableComponent {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;

  clipboardService = inject(ClipboardService);

  // Wejścia z rodzica
  scans = input.required<ScanDto[]>();
  scansToDelete = input.required<ScanToDelete[]>();

  // Wyjścia (zdarzenia)
  retry = output<string>();
  delete = output<string>();
  cancelDelete = output<string>();

  // Wewnętrzny stan tabeli
  expandedScanId = signal<string | null>(null);
  showScrollDown = signal<boolean>(false);
  showScrollUp = signal<boolean>(false);

  toggleExpand(scanId: string) {
    this.expandedScanId.update((current) => (current === scanId ? null : scanId));
  }

  isIntendedToDelete(scanId: string): boolean {
    return this.scansToDelete().some((item) => item.scanId === scanId);
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
