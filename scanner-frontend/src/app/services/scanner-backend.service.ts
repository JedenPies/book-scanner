import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ScanDto, SessionDto, ShareCodeDto } from '../models/backend.model';

@Service()
export class ScannerBackendService {

  private apiScannerUrl = '/api/sessions';
  private apiShareCodeUrl = '/api/share-codes';
  private http = inject(HttpClient);

  createSession() {
    return this.http.post<SessionDto>(this.apiScannerUrl, {});
  }

  generateShareCode(sessionId: string) {
    return this.http.post<ShareCodeDto>(`${this.apiShareCodeUrl}`, { sessionId: sessionId });
  }

  retrieveSessionIdByShareCode(shareCode: string) {
    return this.http.get<ShareCodeDto>(`${this.apiShareCodeUrl}/${shareCode}`);
  }

  notifyScanResult(sessionId: string, scanResult: string) {
    return this.http.post<ScanDto>(`${this.apiScannerUrl}/${sessionId}/scans`, { isbn: scanResult});
  }

  retrieveAllScans(sessionId: string) {
    return this.http.get<ScanDto[]>(`${this.apiScannerUrl}/${sessionId}/scans`);
  }

  retryScan(sessionId: string, scanId: string) {
    return this.http.post<void>(`${this.apiScannerUrl}/${sessionId}/scans/${scanId}/retry`, {});
  }
}
