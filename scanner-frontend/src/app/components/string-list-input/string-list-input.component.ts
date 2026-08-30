import { Component, model, input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-string-list-input',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './string-list-input.component.html',
  styleUrl: './string-list-input.component.scss',
})
export class StringListInputComponent {
  // Dwukierunkowy model danych oparty na sygnałach
  items = model<string[]>([]);

  // Opcjonalny placeholder dla pola tekstowego
  placeholder = input<string>('Type and press Enter to add. Backspace to remove.');

  // Aktualny tekst w inpucie
  currentInput = signal<string>('');

  onKeyDown(event: KeyboardEvent) {
    const text = this.currentInput().trim();

    if (event.key === 'Enter' || event.key === ',') {
      event.preventDefault();
      if (text) {
        this.addItem(text);
      }
    } else if (event.key === 'Backspace' && !this.currentInput()) {
      // Usunięcie ostatniego elementu przy pustym inpucie
      const currentItems = this.items();
      if (currentItems.length > 0) {
        this.removeItem(currentItems.length - 1);
      }
    }
  }

  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    this.currentInput.set(input.value);
  }

  addItem(value: string) {
    const sanitized = value.replace(/^,|,$/g, '').trim();
    if (sanitized && !this.items().includes(sanitized)) {
      this.items.update((list) => [...list, sanitized]);
      this.currentInput.set('');
    }
  }

  removeItem(index: number) {
    this.items.update((list) => list.filter((_, i) => i !== index));
  }
}
