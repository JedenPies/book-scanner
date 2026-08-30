import { Component, effect, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EditScanCommandDto, ScanDto } from '../../../models/backend.model';

@Component({
  selector: 'app-edit-scan-form-dialog',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './edit-scan-form-dialog.component.html',
  styleUrl: './edit-scan-form-dialog.component.scss',
})
export class EditScanFormDialogComponent {

  scan = input.required<ScanDto>();

  saved = output<EditScanCommandDto>();
  closed = output<void>();

  formData = signal<EditScanCommandDto>({});

  constructor() {
    effect(() => {
      const currentScan = this.scan();
      if (currentScan) {
        this.formData.set({
          title: currentScan.bookDetails?.title || '',
          publisher: currentScan.bookDetails?.publisher || '',
          publicationPlace: currentScan.bookDetails?.publicationPlace || '',
          publicationYear: currentScan.bookDetails?.publicationYear || '',
          language: currentScan.bookDetails?.language || '',
          authors: currentScan.bookDetails?.authors || [],
          genres: currentScan.bookDetails?.genres || [],
        });
      }
    });
  }

  submit() {
    this.saved.emit(this.formData());
    this.close();
  }

  close() {
    this.closed.emit();
  }
}
