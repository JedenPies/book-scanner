import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info' | 'warning';

export interface Toast {
  id: string;
  message: string;
  type: ToastType;
  action?: ToastAction;
}

export interface ToastAction {
  label: string;
  run: () => void;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  toasts = signal<Toast[]>([]);

  show(
    message: string,
    type: ToastType = 'info',
    action?: ToastAction,
    customId?: string,
    duration: number = 10000,
  ): string {
    const id = customId || Math.random().toString(36).substring(2, 9);
    const newToast: Toast = { id, message, type, action };

    if (duration > 0) {
      setTimeout(() => this.remove(id), duration);
    }
    this.toasts.update((current) => {
      const exists = current.some((t) => t.id === id);
      if (exists) {
        return current.map((t) => (t.id === id ? newToast : t));
      }
      return [...current, newToast];
    });
    return id;
  }

  remove(id: string) {
    this.toasts.update((current) => current.filter((t) => t.id !== id));
  }
}
