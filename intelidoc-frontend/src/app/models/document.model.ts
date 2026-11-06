export interface AnalysisResult {
  summary: string;
  detectedTechnologies: string[];
  architectureType: string;
  potentialRisks: string[];
  recommendations: string;
}

export interface UploadResponse {
  filename: string;
  size: number;
  contentType: string;
  message: string;
  analysis: AnalysisResult | null; // Mapeamos el informe de la IA
}
