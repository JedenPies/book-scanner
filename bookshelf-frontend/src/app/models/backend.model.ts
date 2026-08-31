export type DraftBookStatus =
  'PENDING' | 'FETCHING' | 'FOUND' | 'NOT_FOUND' | 'FAILED' | 'MODIFIED' | 'DUPLICATE';

export type ExportFormat = 'CSV' | 'XLSX';

export type ExportStatus = 'REQUESTED' | 'PROCESSING' | 'SUCCEED' | 'FAILED';

export interface SessionDto {
  id: string;
  exportId: string | null;
}

export interface BookDetailsDto {
  sources: string[];
  title: string;
  authors: string[];
  publisher: string;
  publicationYear: string;
  publicationPlace: string;
  language: string;
  genres: string[];
}

export interface DraftBookDto {
  id: string;
  isbn: string;
  status: DraftBookStatus;
  bookDetails: BookDetailsDto | null;
  createdAt: string;
}

export interface ShareCodeDto {
  sessionId: string;
  code: string;
}

export interface DraftBookCreatedSseEvent {
  draftBook: DraftBookDto;
}

export interface DraftBookUpdatedSseEvent {
  draftBook: DraftBookDto;
}

export interface DraftBookDeletedSseEvent {
  draftBooks: DraftBookDto[];
  count: number;
}

export interface ExportCompleteSseEvent {
  export: ExportDto;
}

export interface ExportDto {
  id: string;
  sessionId: string;
  format: ExportFormat;
  status: ExportStatus;
  createdAt: string;
}

export interface EditDraftBookCommandDto {
  title?: string;
  publisher?: string;
  publicationYear?: string;
  publicationPlace?: string;
  language?: string;
  authors?: string[];
  genres?: string[];
}
