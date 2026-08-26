export type ScanStatus =
  'PENDING' | 'FETCHING' | 'FOUND' | 'NOT_FOUND' | 'FAILED' | 'MODIFIED' | 'DUPLICATE';


export interface SessionDto {
  id: string;
}

export interface CreateScanRequestDto {
  isbn: string;
}

export interface BookDetailsDto {
  source: string;
  title: string;
  authors: string[];
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
