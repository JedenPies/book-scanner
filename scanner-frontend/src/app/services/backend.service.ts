import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  EditDraftBookCommandDto,
  ExportDto,
  ExportFormat,
  DraftBookDto,
  SessionDto,
  ShareCodeDto,
} from '../models/backend.model';

@Service()
export class BackendService {

  private apiCatalogingSessionUrl = '/api/sessions';
  private apiShareCodeUrl = '/api/share-codes';
  private http = inject(HttpClient);

  createSession() {
    return this.http.post<SessionDto>(this.apiCatalogingSessionUrl, {});
  }

  generateShareCode(sessionId: string) {
    return this.http.post<ShareCodeDto>(`${this.apiShareCodeUrl}`, { sessionId: sessionId });
  }

  retrieveSessionIdByShareCode(shareCode: string) {
    return this.http.get<ShareCodeDto>(`${this.apiShareCodeUrl}/${shareCode}`);
  }

  notifyDraftBookResult(sessionId: string, scanResult: string) {
    return this.http.post<DraftBookDto>(`${this.apiCatalogingSessionUrl}/${sessionId}/draft-books`, {
      isbn: scanResult
    });
  }

  retrieveAllDraftBooks(sessionId: string) {
    return this.http.get<DraftBookDto[]>(`${this.apiCatalogingSessionUrl}/${sessionId}/draft-books`);
  }

  retryFetch(sessionId: string, scanId: string) {
    return this.http.post<void>(
      `${this.apiCatalogingSessionUrl}/${sessionId}/draft-books/${scanId}/retry`,
      {},
    );
  }

  addDraftBook(sessionId: string, isbn: string) {
    return this.http.post<DraftBookDto>(`${this.apiCatalogingSessionUrl}/${sessionId}/draft-books`, {
      isbn: isbn,
    });
  }

  deleteDraftBooks(sessionId: string, draftBooks: DraftBookDto[]) {
    const draftBooksIds = draftBooks.map((draftBook) => draftBook.id);
    console.log('Deleting draft books:', draftBooksIds);
    return this.http.post<void>(`${this.apiCatalogingSessionUrl}/${sessionId}/draft-books/delete-requests`, {
      draftBooksIds: draftBooksIds,
    });
  }

  loadExport(sessionId: string) {
    return this.http.get<ExportDto>(`${this.apiCatalogingSessionUrl}/${sessionId}/export`);
  }

  requestExport(sessionId: string, format: ExportFormat) {
    return this.http.put<ExportDto>(`${this.apiCatalogingSessionUrl}/${sessionId}/export-request`, {
      format
    });
  }

  modifyDraftBook(sessionId: string, draftBookId: string, command: EditDraftBookCommandDto) {
    return this.http.patch<DraftBookDto>(`/api/sessions/${sessionId}/draft-books/${draftBookId}`, command);
  }
}
