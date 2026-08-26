import { inject, Service } from '@angular/core';
import { ToastService } from './toast.service';
import { Clipboard } from '@angular/cdk/clipboard';

@Service()
export class ClipboardService {

  private clipboard = inject(Clipboard);
  private toastService = inject(ToastService);

  copyToClipboard(textToCopy: string) {
    if (!textToCopy) return;
    const success = this.clipboard.copy(textToCopy)
    if (success) {
      this.toastService.show('Skopiowano do schowka');
    } else {
      this.toastService.show('Nie udało się skopiować do schowka', 'error');
    }
  }
}
