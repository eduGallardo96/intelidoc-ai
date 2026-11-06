import { Component } from '@angular/core';
import { UploadDocumentComponent } from './features/upload-document/upload-document.component'; // Importamos nuestro componente

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [UploadDocumentComponent], // Lo declaramos aquí
  template: `
    <main style="padding: 2rem; font-family: sans-serif; max-width: 800px; margin: 0 auto;">
      <h1 style="color: #333; text-align: center;">Intelidoc AI</h1>
      <p style="text-align: center; color: #666;">Análisis Inteligente de Documentos Técnicos</p>

      <!-- Renderizamos el componente de subida -->
      <app-upload-document></app-upload-document>
    </main>
  `
})
export class AppComponent {}
