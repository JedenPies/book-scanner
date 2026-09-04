import { Component, ElementRef, inject, input, output, signal, ViewChild } from '@angular/core';
import { LowerCasePipe } from '@angular/common';
import { ClipboardService } from '../../../services/clipboard.service';
import { DraftBookDto } from '../../../models/backend.model';

@Component({
  selector: 'app-draft-books-table',
  standalone: true,
  imports: [LowerCasePipe],
  templateUrl: './draft-books-table.component.html',
  styleUrl: './draft-books-table.component.scss',
})
export class DraftBooksTableComponent {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;

  clipboardService = inject(ClipboardService);

  draftBooks = input.required<DraftBookDto[]>();

  // Wyjścia (zdarzenia)
  retry = output<string>();
  delete = output<DraftBookDto>();
  edit = output<DraftBookDto>();

  // Wewnętrzny stan tabeli
  expandedDraftBookId = signal<string | null>(null);
  showScrollDown = signal<boolean>(false);
  showScrollUp = signal<boolean>(false);

  toggleExpand(draftBookId: string) {
    this.expandedDraftBookId.update((current) => (current === draftBookId ? null : draftBookId));
  }

  isExpandable(draftBook: DraftBookDto): boolean {
    return (
      draftBook.status !== 'NOT_FOUND' &&
      draftBook.status !== 'FAILED' &&
      draftBook.status !== 'PENDING' &&
      draftBook.status !== 'FETCHING'
    );
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
