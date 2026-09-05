import { Component, computed, effect, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EditDraftBookCommandDto, DraftBookDto } from '../../../models/backend.model';
import { StringListInputComponent } from '../../string-list-input/string-list-input.component';

@Component({
  selector: 'app-edit-draft-book-form-dialog',
  standalone: true,
  imports: [FormsModule, StringListInputComponent],
  templateUrl: './edit-draft-book-form-dialog.component.html',
  styleUrl: './edit-draft-book-form-dialog.component.scss',
})
export class EditDraftBookFormDialogComponent {

  draftBook = input.required<DraftBookDto>();

  saved = output<EditDraftBookCommandDto>();
  closed = output<void>();

  // Sygnały dla poszczególnych pól i list
  title = signal<string>('');
  publisher = signal<string>('');
  publicationYear = signal<string>('');
  publicationPlace = signal<string>('');
  language = signal<string>('');

  editableAuthors = signal<string[]>([]);
  editableGenres = signal<string[]>([]);

  constructor() {
    effect(() => {
      const currentDraftBook = this.draftBook();
      if (currentDraftBook) {
        const details = currentDraftBook.bookDetails;
        this.title.set(details?.title || '');
        this.publisher.set(details?.publisher || '');
        this.publicationYear.set(details?.publicationYear ? String(details.publicationYear) : '');
        this.publicationPlace.set(details?.publicationPlace || '');
        this.language.set(details?.language || '');
        this.editableAuthors.set(details?.authors ? [...details.authors] : []);
        this.editableGenres.set(details?.genres ? [...details.genres] : []);
      }
    });
  }

  private isNullOrNotBlank(val?: string | null): boolean {
    if (val === undefined || val === null || val === '') {
      return true;
    }
    return val.trim().length > 0;
  }

  // Wymagane: @NotBlank oraz @Size(max = 500)
  isValidTitle = computed(() => {
    const val = this.title();
    if (!val) return false;
    const trimmed = val.trim();
    return trimmed.length > 0 && trimmed.length <= 500;
  });

  // Opcjonalne: @NullOrNotBlank oraz @Size(max = 200)
  isValidPublisher = computed(() => {
    const val = this.publisher();
    return this.isNullOrNotBlank(val) && val.length <= 200;
  });

  // Opcjonalne: @NullOrNotBlank oraz regex 4 cyfry
  isValidPublicationYear = computed(() => {
    const val = this.publicationYear();
    if (!val || val.trim().length === 0) {
      return true;
    }
    return /^[0-9]{4}$/.test(val.trim());
  });

  // Opcjonalne: @NullOrNotBlank oraz @Size(max = 200)
  isValidPublicationPlace = computed(() => {
    const val = this.publicationPlace();
    return this.isNullOrNotBlank(val) && val.length <= 200;
  });

  // Opcjonalne: @NullOrNotBlank oraz @Size(max = 50)
  isValidLanguage = computed(() => {
    const val = this.language();
    return this.isNullOrNotBlank(val) && val.length <= 50;
  });

  // Elementy list: @NotBlank
  isValidAuthors = computed(() => {
    return this.editableAuthors().every((a) => a && a.trim().length > 0);
  });

  isValidGenres = computed(() => {
    return this.editableGenres().every((g) => g && g.trim().length > 0);
  });

  isFormValid = computed(() => {
    return (
      this.isValidTitle() &&
      this.isValidPublisher() &&
      this.isValidPublicationYear() &&
      this.isValidPublicationPlace() &&
      this.isValidLanguage() &&
      this.isValidAuthors() &&
      this.isValidGenres()
    );
  });

  private sanitizeOptionalField(value?: string | null): string | undefined {
    if (!value) return undefined;
    const trimmed = value.trim();
    return trimmed.length > 0 ? trimmed : undefined;
  }

  onYearBeforeInput(event: InputEvent) {
    if (event.data && !/^\d+$/.test(event.data)) {
      event.preventDefault();
    }
  }

  submit() {
    if (!this.isFormValid()) {
      return;
    }

    const command: EditDraftBookCommandDto = {
      title: this.title().trim(),
      publisher: this.sanitizeOptionalField(this.publisher()),
      publicationYear: this.sanitizeOptionalField(this.publicationYear()),
      publicationPlace: this.sanitizeOptionalField(this.publicationPlace()),
      language: this.sanitizeOptionalField(this.language()),
      authors: this.editableAuthors()
        .map((a) => a.trim())
        .filter((a) => a.length > 0),
      genres: this.editableGenres()
        .map((g) => g.trim())
        .filter((g) => g.length > 0),
    };

    this.saved.emit(command);
    this.close();
  }

  close() {
    this.closed.emit();
  }
}
