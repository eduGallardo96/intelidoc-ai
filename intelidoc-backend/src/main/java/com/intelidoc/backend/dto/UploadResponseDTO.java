package com.intelidoc.backend.dto;

// Uso de Record en Java25 (genera getters, equals, toString de forma nativa)
public record UploadResponseDTO(
        String filename,
        long size,
        String contentType,
        String message,
        AnalysisResultDTO analysis
) {
}
