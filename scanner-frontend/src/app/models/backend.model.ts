export type ScanStatus =
  'PENDING' | 'FETCHING' | 'FOUND' | 'NOT_FOUND' | 'FAILED' | 'MODIFIED' | 'DUPLICATE';

export type ExportFormat = 'CSV' | 'XLSX';

export type ExportStatus = 'REQUESTED' | 'PROCESSING' | 'SUCCEED' | 'FAILED';

export interface SessionDto {
  id: string;
}

export interface CreateScanRequestDto {
  isbn: string;
}

export interface BookDetailsDto {
  sources: string[];
  title: string;
  authors: string[];
  publisher: string;
  publicationYear: string;
  publicationPlace: string;
  language: string;
}

export interface ScanDto {
  id: string;
  isbn: string;
  status: ScanStatus;
  bookDetails: BookDetailsDto | null;
  createdAt: string;
}

export interface ShareCodeDto {
  sessionId: string;
  code: string;
}

export interface ScanCreatedSseEvent {
  scan: ScanDto;
}

export interface ScanUpdatedSseEvent {
  scan: ScanDto;
}

export interface ScanDeletedSseEvent {
  scan: ScanDto;
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

export interface ExportRequestDto {
  format: ExportFormat;
}
