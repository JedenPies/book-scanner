import { Component, inject, input, signal } from '@angular/core';
import {
  BookScanRequestedEvent,
  ScanDto, ScanUpdatedEventDto,
} from '../../models/backend.model';
import { ScannerBackendService } from '../../services/scanner-backend.service';

@Component({
  selector: 'app-scanner',
  imports: [],
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
          this.scans.set(result);
        },
      });
    }
  }

  private initSseStream() {
    const sessionId = this.sessionId();
    if (!sessionId) return;
    this.eventSource = new EventSource(`/api/sessions/${sessionId}/events-stream`);
    this.eventSource.addEventListener('SCAN_REQUESTED', (event: MessageEvent) => {
      const eventDto: BookScanRequestedEvent = JSON.parse(event.data);
      const newScan: ScanDto = {
        id: eventDto.scanId,
        isbn: eventDto.isbn,
        status: 'PENDING',
        bookDetails: null,
      };
      this.scans.update((currentScans) => [...currentScans, newScan]);
    });
    this.eventSource.addEventListener('SCAN_UPDATED', (event: MessageEvent) => {
      const eventDto: ScanUpdatedEventDto = JSON.parse(event.data);
      const updatedScan = eventDto.scan;
      this.scans.update(currentScans =>
        currentScans.map(scan => scan.id === updatedScan.id ? updatedScan : scan)
      );
    })
    this.eventSource.onerror = (error) => {
      console.error('EventSource failed:', error);
    }
  }
}
