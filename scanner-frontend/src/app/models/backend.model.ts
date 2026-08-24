export type ScanStatus = 'PENDING' | 'FETCHING' | 'COMPLETED' | 'FAILED' | 'MODIFIED' | 'DUPLICATE';


export interface SessionDto {
  id: string;
}

export interface CreateScanRequestDto {
  isbn: string;
}

export interface BookDetailsDto {
  source: string;
  title: string;
  subtitle: string;
  authors: string[];
}

export interface ScanDto {
  id: string;
  isbn: string;
  status: ScanStatus;
  bookDetails: BookDetailsDto | null;
  // private final Instant createdAt;
}

export interface ShareCodeDto {
  sessionId: string;
  code: string;
}

export interface BookScanRequestedEvent {
  scanId: string;
  isbn: string;
}

export interface ScanUpdatedEventDto {
  scan: ScanDto;
}
