import { Component, inject, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-scanner',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './entry.component.html',
  styleUrl: './entry.component.scss',
})
export class EntryComponent {
  private scannerService = inject(ScannerBackendService);

  router = inject(Router);

  shareCode = signal<string>('');

  showScanView() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
      navigator.userAgent,
    );
  }

  startSession() {
    this.scannerService.createSession().subscribe({
      next: (response) => {
        // // Zapisujemy ID sesji w localStorage zgodnie z planem
        // localStorage.setItem('sessionId', response.id);
        this.router.navigate(['/scanner', response.id]);
      },
      error: (err) => {
        console.error('Nie udało się utworzyć sesji', err);
      },
    });
  }

  joinByShareCode() {
    const shareCode = this.shareCode();
    if (shareCode) {
      this.scannerService.retrieveSessionIdByShareCode(shareCode).subscribe({
        next: (response) => {
          this.router.navigate(['/editor', response.sessionId]);
        },
        error: (err) => {
          console.error('Nie udało się pobrać numeru sesji', err);
        },
      });
    }
  }
}
