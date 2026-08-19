import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { BarcodeFormat } from '@zxing/library';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ZXingScannerModule],
  styles: [`
    .scanner-container {
      max-width: 500px;
      margin: 0 auto;
      text-align: center;
      font-family: sans-serif;
    }
    .camera-select {
      margin: 15px 0;
      padding: 10px;
      width: 90%;
      font-size: 16px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }


    .results {
      margin-top: 20px;
      padding: 10px;
      background: #f4f4f4;
      border-radius: 8px;
    }
    .isbn-badge {
      display: inline-block;
      background: #4caf50;
      color: white;
      padding: 8px 16px;
      margin: 5px;
      border-radius: 20px;
      font-weight: bold;
    }
  `],
  template: `
    <div class="scanner-container">
      <h2>Skaner ISBN 📚</h2>

      <select class="camera-select"
              (change)="onDeviceSelectChange($event)"
              *ngIf="hasDevices">
        <option value="" [selected]="!currentDevice">Domyślna kamera</option>
        <option *ngFor="let device of availableDevices"
                [value]="device.deviceId"
                [selected]="currentDevice && device.deviceId === currentDevice.deviceId">
          {{ device.label || 'Kamera bez nazwy' }}
        </option>
      </select>

      <div *ngIf="!hasDevices && hasPermission === false" style="color: red; margin: 15px;">
        Brak uprawnień do kamery lub brak dostępnych urządzeń.
      </div>

      <zxing-scanner
        [formats]="allowedFormats"
        [device]="currentDevice"
        (camerasFound)="onCamerasFound($event)"
        (permissionResponse)="onHasPermission($event)"
        (scanSuccess)="onCodeResult($event)">
      </zxing-scanner>

      <div class="results" *ngIf="scannedCodes.length > 0">
        <h3>Zeskanowane:</h3>
        <span class="isbn-badge" *ngFor="let code of scannedCodes">
          {{ code }}
        </span>
      </div>
    </div>
  `
})
export class AppComponent {
  allowedFormats = [
    BarcodeFormat.EAN_13,
    BarcodeFormat.EAN_8,
    BarcodeFormat.CODE_128,
    BarcodeFormat.UPC_A
  ];

  availableDevices: MediaDeviceInfo[] = [];
  currentDevice: MediaDeviceInfo | undefined = undefined;
  hasDevices: boolean = false;
  hasPermission: boolean | null = null;

  scannedCodes: string[] = [];
  lastScannedCode: string = '';

  // Wywoływane, gdy Zxing po starcie wykryje dostępne obiektywy
  onCamerasFound(devices: MediaDeviceInfo[]): void {
    this.availableDevices = devices;
    this.hasDevices = Boolean(devices && devices.length > 0);

    // Opcjonalnie: Jeśli chcemy wymusić domyślnie ostatnią kamerę z tyłu
    // (czasami główny obiektyw ładuje się na końcu listy)
    if (this.hasDevices) {
      // this.currentDevice = devices[devices.length - 1];
    }
  }

  // Odpowiedź na to, czy user zezwolił na kamerę w przeglądarce
  onHasPermission(has: boolean) {
    this.hasPermission = has;
  }

  // Kiedy użytkownik zmieni kamerę w SelectBoxie
  onDeviceSelectChange(event: any) {
    const deviceId = event.target.value;
    if (deviceId === "") {
      this.currentDevice = undefined;
    } else {
      this.currentDevice = this.availableDevices.find(d => d.deviceId === deviceId);
    }
  }

  onCodeResult(resultString: string) {
    if (resultString !== this.lastScannedCode) {
      this.lastScannedCode = resultString;
      this.scannedCodes.unshift(resultString);

      this.provideFeedback();

      setTimeout(() => {
        this.lastScannedCode = '';
      }, 2000);
    }
  }

  private provideFeedback() {
    if (window.navigator.vibrate) {
      window.navigator.vibrate(50);
    }

    try {
      const context = new (window.AudioContext || (window as any).webkitAudioContext)();
      const oscillator = context.createOscillator();
      const gainNode = context.createGain();

      oscillator.type = 'sine';
      oscillator.frequency.setValueAtTime(800, context.currentTime);

      gainNode.gain.setValueAtTime(0.1, context.currentTime);

      oscillator.connect(gainNode);
      gainNode.connect(context.destination);

      oscillator.start();
      setTimeout(() => { oscillator.stop(); }, 100);
    } catch (e) {
      console.log('Audio API not supported');
    }
  }
}
