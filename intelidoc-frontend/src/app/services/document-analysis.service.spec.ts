import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { DocumentAnalysisService } from './document-analysis.service';
import { UploadResponse } from '../models/document.model';
import { RequestStatus } from '../models/request-status';

describe('DocumentAnalysisService', () => {
  let service: DocumentAnalysisService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    // TestBed crea un mini-módulo de Angular falso y aislado
    TestBed.configureTestingModule({
      providers: [
        DocumentAnalysisService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(DocumentAnalysisService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Verifica y comprueba que no se haya quedado ninguna petición HTTP
  afterEach(() => {
    httpMock.verify();
  });

  it('1. Debería inicializarse con estado IDLE y resultados nulos', () => {
    expect(service.status()).toBe(RequestStatus.Idle);
    expect(service.analysisResult()).toBeNull();
    expect(service.errorMessage()).toBeNull();
  });

  it('2. Debería mutar las Signals a LOADING y luego a SUCCESS al recibir respuesta del Backend', async () => {
    const mockFile = new File(['contenido de prueba'], 'requisitos.txt', { type: 'text/plain' });
    const mockResponse: UploadResponse = {
      filename: 'requisitos.txt',
      size: 1024,
      contentType: 'text/plain',
      message: 'Éxito',
      analysis: {
        summary: 'Resumen IA',
        detectedTechnologies: ['Java'],
        architectureType: 'Monorrepo',
        potentialRisks: ['Ninguno'],
        recommendations: 'Ok'
      }
    };

    const uploadPromise = service.uploadDocument(mockFile);

    // Vitest usa .toBe(true) en lugar de .toBeTrue()
    expect(service.isLoading()).toBe(true);

    const req = httpMock.expectOne('http://localhost:8080/api/v1/documents/upload');
    expect(req.request.method).toBe('POST');

    req.flush(mockResponse);

    await uploadPromise;

    expect(service.isSuccess()).toBe(true);
    expect(service.analysisResult()).toEqual(mockResponse);
  });
});
