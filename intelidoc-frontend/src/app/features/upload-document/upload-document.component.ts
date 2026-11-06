import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentAnalysisService } from '../../services/document-analysis.service';

@Component({
  selector: 'app-upload-document',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload-document.component.html',
  styleUrls: ['./upload-document.component.scss']
})
export class UploadDocumentComponent {
  private readonly documentService = inject(DocumentAnalysisService);

  // Exponemos las propiedades limpias del servicio a la vista
  readonly isLoading = this.documentService.isLoading;
  readonly isSuccess = this.documentService.isSuccess;
  readonly isError = this.documentService.isError;
  readonly analysisResult = this.documentService.analysisResult;
  readonly errorMessage = this.documentService.errorMessage;

  // Señal local para los estilos visuales del dragover
  readonly isDragOver = signal<boolean>(false);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.documentService.uploadDocument(input.files[0]);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(false);
    if (event.dataTransfer?.files.length) {
      this.documentService.uploadDocument(event.dataTransfer.files[0]);
    }
  }
}
