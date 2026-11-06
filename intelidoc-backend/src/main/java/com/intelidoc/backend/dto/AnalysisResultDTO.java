package com.intelidoc.backend.dto;

import java.util.List;

// Uso de Record en Java25 (genera getters, equals, toString de forma nativa)
public record AnalysisResultDTO(
        String summary,
        List<String> detectedTechnologies,
        String architectureType,
        List<String> potentialRisks,
        String recommendations
) {}
