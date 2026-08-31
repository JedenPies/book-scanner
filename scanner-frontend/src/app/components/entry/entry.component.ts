import { Component, ElementRef, inject, QueryList, signal, ViewChildren } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-scanner',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './entry.component.html',
  styleUrl: './entry.component.scss',
})
export class EntryComponent {
  @ViewChildren('codeInput') inputs!: QueryList<ElementRef<HTMLInputElement>>;

  private scannerService = inject(BackendService);

  router = inject(Router);
  toasts = inject(ToastService);

  shareCodeDigits = signal<string[]>(['', '', '', '', '', '']);

  onInput(event: Event, index: number) {
    const input = event.target as HTMLInputElement;
    let value = input.value.toUpperCase().replace(/[^A-Z0-9]/g, '');

    if (value.length > 1) {
      value = value.slice(-1);
    }

    this.shareCodeDigits.update((digits) => {
      digits[index] = value;
      return [...digits];
    });

    input.value = value;

    if (value && index < 5) {
      this.focusInput(index + 1);
    } else if (index === 5) {
      this.joinByShareCode();
    }
  }

  onKeyDown(event: KeyboardEvent, index: number) {
    if (event.key === 'Backspace') {
      if (!this.shareCodeDigits()[index] && index > 0) {
        this.focusInput(index - 1);
      }
    }
  }

  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    const pasteData =
      event.clipboardData
        ?.getData('text')
        .toUpperCase()
        .replace(/[^A-Z0-9]/g, '') || '';
    if (pasteData) {
      this.shareCodeDigits.update((digits) => {
        for (let i = 0; i < 6; i++) {
          digits[i] = pasteData[i] || '';
        }
        return [...digits];
      });
      const nextIndex = Math.min(pasteData.length, 5);
      if (nextIndex < 5) {
        this.focusInput(nextIndex);
      } else {
        this.joinByShareCode();
      }
    }
  }

  focusInput(index: number) {
    const inputsArray = this.inputs.toArray();
    if (inputsArray[index]) {
      inputsArray[index].nativeElement.focus();
    }
  }

  startSession() {
    this.scannerService.createSession().subscribe({
      next: (response) => {
        this.router.navigate(['/scanner', response.id]);
      },
      error: (err) => {
        this.toasts.show('Nie udało się utworzyć sesji skanowania: ' + err);
      },
    });
  }

  joinByShareCode() {
    const shareCode = this.shareCodeDigits().join('');
    if (shareCode) {
      this.scannerService.retrieveSessionIdByShareCode(shareCode).subscribe({
        next: (response) => {
          this.router.navigate(['/editor', response.sessionId]);
        },
        error: () => {
          this.toasts.show('Nie udało się pobrać numeru sesji', 'error');
        },
      });
    }
  }
}
