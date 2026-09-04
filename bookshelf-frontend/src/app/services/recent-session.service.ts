import { Injectable, signal } from '@angular/core';

export interface RecentSession {
  id: string;
  name?: string;
  draftCount?: number;
  createdAt: string; // ISO string
}

@Injectable({
  providedIn: 'root',
})
export class RecentSessionsService {

  private readonly STORAGE_KEY = 'recent_catalog_sessions';
  private readonly MAX_SESSIONS = 5;

  recentSessions = signal<RecentSession[]>(this.loadFromStorage());

  updateSession(sessionId: string, draftCount?: number, sessionName?: string): void {
    console.log("update session count", draftCount);
    const updated = this.recentSessions().map((s) => {
      if (s.id === sessionId) {
        return {
          ...s,
          draftCount: draftCount || s.draftCount,
          name: sessionName || s.name,
        };
      }
      return s;
    });
    this.saveToStorage(updated);
  }

  addSession(sessionId: string, draftCount?: number, sessionName?: string): void {
    const current = this.recentSessions().filter((s) => s.id !== sessionId);

    const newEntry: RecentSession = {
      id: sessionId,
      name: sessionName || `Sesja ${sessionId.substring(0, 8)}`,
      draftCount: draftCount || 0,
      createdAt: this.formatCurrentDateTime(),
    };

    // Dodajemy na początek i obcinamy do 5 elementów
    const updated = [newEntry, ...current].slice(0, this.MAX_SESSIONS);

    this.recentSessions.set(updated);
    this.saveToStorage(updated);
  }

  removeSession(sessionId: string): void {
    const updated = this.recentSessions().filter((s) => s.id !== sessionId);
    this.recentSessions.set(updated);
    this.saveToStorage(updated);
  }

  private loadFromStorage(): RecentSession[] {
    try {
      const data = localStorage.getItem(this.STORAGE_KEY);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }

  private saveToStorage(sessions: RecentSession[]): void {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(sessions));
    } catch (e) {
      console.error('Nie udało się zapisać historii sesji w localStorage', e);
    }
  }

  private formatCurrentDateTime(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
  }
}
