import { Component, effect, ElementRef, input, output, viewChild } from '@angular/core';
import { BrowserQRCodeSvgWriter } from '@zxing/browser';

@Component({
  selector: 'app-qr-code-modal',
  standalone: true,
  templateUrl: './qr-code-modal.component.html',
  styleUrl: './qr-code-modal.component.scss',
})
export class QrCodeModalComponent {

  title = input<string>('Scan QR Code');
  description = input<string>('Scan QR Code to follow');
  value = input.required<string>();

  size = input<number>(240);

  closed = output<void>();

  // Referencja do kontenera na wygenerowane SVG
  qrContainerRef = viewChild.required<ElementRef<HTMLDivElement>>('qrContainer');

  constructor() {
    effect(() => {
      const textToEncode = this.value();
      const qrSize = this.size();

      if (!textToEncode) return;

      const writer = new BrowserQRCodeSvgWriter();
      const svgElement = writer.write(textToEncode, qrSize, qrSize);

      // Czyścimy kontener i wstawiamy nowe drzewo SVG
      this.qrContainerRef().nativeElement.replaceChildren(svgElement);
    });
  }

  close() {
    this.closed.emit();
  }
}
