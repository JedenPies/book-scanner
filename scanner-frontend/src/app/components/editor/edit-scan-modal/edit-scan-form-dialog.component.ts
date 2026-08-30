import { Component, effect, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EditScanCommandDto, ScanDto } from '../../../models/backend.model';
import { StringListInputComponent } from '../../string-list-input/string-list-input.component';

@Component({
  selector: 'app-edit-scan-form-dialog',
  standalone: true,
  imports: [FormsModule, StringListInputComponent],
  templateUrl: './edit-scan-form-dialog.component.html',
  styleUrl: './edit-scan-form-dialog.component.scss',
})
export class EditScanFormDialogComponent {
  scan = input.required<ScanDto>();

  saved = output<EditScanCommandDto>();
  closed = output<void>();

  formData = signal<EditScanCommandDto>({});

  editableAuthors = signal<string[]>([]);
  editableGenres = signal<string[]>([]);

  constructor() {
    effect(() => {
      const currentScan = this.scan();
      if (currentScan) {
        const authorsList = currentScan.bookDetails?.authors || [];
        const genresList = currentScan.bookDetails?.genres || [];
        this.editableAuthors.set(authorsList);
        this.editableGenres.set(genresList);
        this.formData.set({
          title: currentScan.bookDetails?.title || '',
          publisher: currentScan.bookDetails?.publisher || '',
          publicationPlace: currentScan.bookDetails?.publicationPlace || '',
          publicationYear: currentScan.bookDetails?.publicationYear || '',
          language: currentScan.bookDetails?.language || '',
          authors: authorsList,
          genres: genresList,
        });
      }
    });
  }

  submit() {
    const command: EditScanCommandDto = {
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
