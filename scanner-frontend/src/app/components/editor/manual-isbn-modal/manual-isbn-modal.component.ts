import { Component, computed, ElementRef, output, signal, ViewChild } from '@angular/core';

@Component({
  selector: 'app-manual-isbn-modal',
  standalone: true,
  templateUrl: './manual-isbn-modal.component.html',
  styleUrl: './manual-isbn-modal.component.scss',
})
export class ManualIsbnModalComponent {

  @ViewChild('isbnInput') set inputRef(ref: ElementRef<HTMLInputElement> | undefined) {
    if (ref) {
      setTimeout(() => ref.nativeElement.focus(), 50);
    }
  }

  isbnSubmitted = output<string>();
  closed = output<void>();

  manualIsbn = signal<string>('');
  private submitTimeout?: number;

  isValidIsbn = computed(() => this.isIsbnValid(this.manualIsbn()));

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const digitsOnly = input.value.replace(/\D/g, '');
    input.value = digitsOnly;
    this.handleDataEntered(digitsOnly);
  }

  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pastedText = event.clipboardData?.getData('text') || '';
    const digitsOnly = pastedText.replace(/\D/g, '').substring(0, 13);
    if (!digitsOnly) return;
    this.handleDataEntered(digitsOnly);
  }

  private handleDataEntered(digitsOnly: string) {
    this.manualIsbn.set(digitsOnly);
    if (this.submitTimeout) {
      clearTimeout(this.submitTimeout);
    }

    const is13 = digitsOnly.length === 13;
    const is10 =
      digitsOnly.length === 10 && !digitsOnly.startsWith('978') && !digitsOnly.startsWith('979');

    if ((is13 || is10) && this.isIsbnValid(digitsOnly)) {
      this.submitTimeout = window.setTimeout(() => this.submit(), 500);
    }
  }

  submit() {
    if (this.submitTimeout) {
      clearTimeout(this.submitTimeout);
    }

    const isbn = this.manualIsbn().trim();
    if (!isbn || !this.isIsbnValid(isbn)) return;

    this.isbnSubmitted.emit(isbn);
    this.manualIsbn.set('');
  }

  close() {
    if (this.submitTimeout) {
      clearTimeout(this.submitTimeout);
    }
    this.manualIsbn.set('');
    this.closed.emit();
  }

  private isIsbnValid(rawIsbn: string): boolean {
    if (!rawIsbn) return false;
    const cleanIsbn = rawIsbn.replace(/[-\s]/g, '').toUpperCase();

    if (cleanIsbn.length === 10) return this.isValidIsbn10(cleanIsbn);
    if (cleanIsbn.length === 13) return this.isValidIsbn13(cleanIsbn);
    return false;
  }

  private isValidIsbn10(isbn: string): boolean {
    if (!/^\d{9}[\dX]$/.test(isbn)) return false;
    let sum = 0;
    for (let i = 0; i < 9; i++) {
      sum += Number(isbn[i]) * (10 - i);
    }
    const checkDigit = isbn[9] === 'X' ? 10 : Number(isbn[9]);
    sum += checkDigit;
    return sum % 11 === 0;
  }

  private isValidIsbn13(isbn: string): boolean {
    if (!/^\d{13}$/.test(isbn)) return false;
    let sum = 0;
    for (let i = 0; i < 13; i++) {
      const digit = Number(isbn[i]);
      const weight = i % 2 === 0 ? 1 : 3;
      sum += digit * weight;
    }
    return sum % 10 === 0;
  }
}
