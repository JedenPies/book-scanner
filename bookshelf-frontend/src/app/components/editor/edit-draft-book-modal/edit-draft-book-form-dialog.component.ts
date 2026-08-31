import { Component, effect, input, output, signal } from '@angular/core';
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

  formData = signal<EditDraftBookCommandDto>({});

  editableAuthors = signal<string[]>([]);
  editableGenres = signal<string[]>([]);

  constructor() {
    effect(() => {
      const currentDraftBook = this.draftBook();
      if (currentDraftBook) {
        const authorsList = currentDraftBook.bookDetails?.authors || [];
        const genresList = currentDraftBook.bookDetails?.genres || [];
        this.editableAuthors.set(authorsList);
        this.editableGenres.set(genresList);
        this.formData.set({
          title: currentDraftBook.bookDetails?.title || '',
          publisher: currentDraftBook.bookDetails?.publisher || '',
          publicationPlace: currentDraftBook.bookDetails?.publicationPlace || '',
          publicationYear: currentDraftBook.bookDetails?.publicationYear || '',
          language: currentDraftBook.bookDetails?.language || '',
          authors: authorsList,
          genres: genresList,
        });
      }
    });
  }

  submit() {
    const command: EditDraftBookCommandDto = {
      ...this.formData(),
      authors: this.editableAuthors(),
      genres: this.editableGenres(),
    };
    this.saved.emit(command);
    this.close();
  }

  close() {
    this.closed.emit();
  }
}
