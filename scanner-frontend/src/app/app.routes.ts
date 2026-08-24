import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/entry/entry.component').then((m) => m.EntryComponent),
  },
  {
    path: 'scanner/:sessionId',
    loadComponent: () => import('./components/scanner/scanner.component').then((m) => m.ScannerComponent),
  },
  {
    path: 'editor/:sessionId',
    loadComponent: () => import('./components/editor/editor.component').then((m) => m.EditorComponent),
  }
];
