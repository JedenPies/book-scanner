import { Component, inject, input, signal } from '@angular/core';
import {
  ScanCreatedSseEvent,
  ScanDto, ScanUpdatedSseEvent,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';
import { LowerCasePipe } from '@angular/common';

@Component({
  selector: 'app-scanner',
  imports: [LowerCasePipe],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.scss',
})
export class EditorComponent {
  sessionId = input.required<string>();
  scans = signal<ScanDto[]>([]);

  backendService = inject(ScannerBackendService);

  private eventSource?: EventSource;

  ngOnInit() {
    this.loadScans();
    this.initSseStream();
  }

  ngOnDestroy() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  }

  private loadScans() {
    const sessionId = this.sessionId();
    if (sessionId) {
      this.backendService.retrieveAllScans(sessionId).subscribe({
        next: (result) => {
          const sortedScans = result.sort(
            (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
          );
          this.scans.set(sortedScans);
        },
      });
    }
  }

  retryScan(scanId: string) {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.backendService.retryScan(sessionId, scanId).subscribe({
      error: (err) => {
        console.error('Błąd podczas ponawiania skanowania:', err);
      },
    });
  }

  private initSseStream() {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.eventSource = new EventSource(`/api/sessions/${sessionId}/events-stream`);
    this.eventSource.addEventListener('SCAN_CREATED', (event: MessageEvent) => {
      console.log('scan created:');
      const eventDto: ScanCreatedSseEvent = JSON.parse(event.data);
      const newScan = eventDto.scan;
      this.scans.update((currentScans) => [newScan, ...currentScans]);
    });
    this.eventSource.addEventListener('SCAN_UPDATED', (event: MessageEvent) => {
      const eventDto: ScanUpdatedSseEvent = JSON.parse(event.data);
      const updatedScan = eventDto.scan;
      console.log('scan updated:', updatedScan);
      this.scans.update((currentScans) =>
        currentScans.map((scan) => (scan.id === updatedScan.id ? updatedScan : scan)),
      );
    });
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
    };
  }
}
