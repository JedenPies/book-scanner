import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info' | 'warning';

export interface Toast {
  id: string;
  message: string;
  type: ToastType;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {

  toasts = signal<Toast[]>([]);

  show(message: string, type: ToastType = 'info', duration: number = 10000) {
    const id = Math.random().toString(36).substring(2, 9);
    const newToast: Toast = { id, message, type };
    this.toasts.update((current) => [...current, newToast]);
    if (duration > 0) {
      setTimeout(() => this.remove(id), duration);
    }
  }

  remove(id: string) {
    this.toasts.update((current) => current.filter(toast => toast.id !== id));
  }

}
