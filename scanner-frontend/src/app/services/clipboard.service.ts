import { inject, Service } from '@angular/core';
import { ToastService } from './toast.service';
import { Clipboard } from '@angular/cdk/clipboard';

@Service()
export class ClipboardService {
  private clipboard = inject(Clipboard);
  private toastService = inject(ToastService);

  copyToClipboard(textToCopy: string, displayWhat: boolean = false, what: string | null = null) {
    if (!textToCopy) return;
    const success = this.clipboard.copy(textToCopy);
    if (success) {
      if (displayWhat) {
        if (!what) what = textToCopy;
        what = what.charAt(0).toUpperCase() + what.slice(1);
        this.toastService.show(`${what} copied to clipboard`);
      } else {
        this.toastService.show('Copied to clipboard');
      }
    } else {
      this.toastService.show('Couldn\'t copy to clipboard', 'error');
    }
  }
}
