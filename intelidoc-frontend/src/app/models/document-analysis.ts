export interface AnalysisEntity {
  type: string;
  value: string;
  confidence: number;
}

export interface DocumentAnalysis {
  id: string;
  fileName: string;
  mimeType: string;
  summary: string;
  extractedText: string;
  entities: AnalysisEntity[];
  confidence: number;
  analyzedAt: string;
}

export interface DocumentAnalysisError {
  message: string;
  statusCode?: number;
}
