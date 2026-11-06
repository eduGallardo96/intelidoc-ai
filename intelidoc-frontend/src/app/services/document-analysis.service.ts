import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import { UploadResponse } from '../models/document.model';
import { RequestStatus } from '../models/request-status';

@Injectable({
  providedIn: 'root',
})
export class DocumentAnalysisService {
  private readonly http = inject(HttpClient);
  private readonly uploadUrl = 'http://localhost:8080/api/v1/documents/upload';

  private readonly statusSignal = signal<RequestStatus>(RequestStatus.Idle);
  private readonly analysisResultSignal = signal<UploadResponse | null>(null);
  private readonly errorMessageSignal = signal<string | null>(null);

  // Exponemos las señales como propiedades de solo lectura para que la vista pueda suscribirse a ellas
  readonly status = this.statusSignal.asReadonly();
  readonly analysisResult = this.analysisResultSignal.asReadonly();
  readonly errorMessage = this.errorMessageSignal.asReadonly();

  readonly isIdle = computed(() => this.statusSignal() === RequestStatus.Idle);
  readonly isLoading = computed(() => this.statusSignal() === RequestStatus.Loading);
  readonly isSuccess = computed(() => this.statusSignal() === RequestStatus.Success);
  readonly isError = computed(() => this.statusSignal() === RequestStatus.Error);

  async uploadDocument(file: File): Promise<void> {
    const formData = new FormData();
    formData.append('file', file);

    this.analysisResultSignal.set(null);
    this.errorMessageSignal.set(null);
    this.statusSignal.set(RequestStatus.Loading);

    try {
      const response = await firstValueFrom(
        this.http.post<UploadResponse>(this.uploadUrl, formData),
      );
      this.analysisResultSignal.set(response);
      this.statusSignal.set(RequestStatus.Success);
    } catch (error) {
      this.errorMessageSignal.set(this.toErrorMessage(error));
      this.statusSignal.set(RequestStatus.Error);
    }
  }

  reset(): void {
    this.statusSignal.set(RequestStatus.Idle);
    this.analysisResultSignal.set(null);
    this.errorMessageSignal.set(null);
  }

  private toErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const detail = this.readProblemDetail(error.error);
      return detail || error.message || 'No se pudo subir el documento.';
    }

    if (error instanceof Error) {
      return error.message;
    }

    return 'No se pudo subir el documento.';
  }

  private readProblemDetail(payload: unknown): string | null {
    if (typeof payload === 'object' && payload !== null && 'detail' in payload) {
      const detail = payload.detail;
      return typeof detail === 'string' && detail.length > 0 ? detail : null;
    }

    return null;
  }
}
