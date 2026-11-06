package com.intelidoc.backend.service;

import com.intelidoc.backend.dto.AnalysisResultDTO;
import com.intelidoc.backend.dto.UploadResponseDTO;
import org.springframework.ai.chat.client.ChatClient; // Importante para Spring AI
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentServiceImpl implements DocumentService {

    // Lee de forma transparente la configuración del modelo desde el application.yml
    private final ChatClient chatClient;

    // Inyección de dependencias moderna por constructor para Spring Boot 4
    public DocumentServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public UploadResponseDTO upload(MultipartFile file) {
        return switch (file) {
            case null -> throw new IllegalArgumentException("El archivo es obligatorio");
            case MultipartFile f when f.isEmpty() ->
                    throw new IllegalArgumentException("El archivo está vacío");
            case MultipartFile f when !isAllowedContentType(f.getContentType()) ->
                    throw new IllegalArgumentException(
                            "Tipo de archivo no permitido. Solo se aceptan text/plain o application/pdf");
            case MultipartFile f -> {
                try {
                    // 1. Extraemos el texto plano del archivo recibido
                    String documentContent = new String(f.getBytes(), StandardCharsets.UTF_8);

                    // 2. Invocamos a Ollama (Llama3) forzando un JSON estricto con bloques de texto Java
                    AnalysisResultDTO aiAnalysis = chatClient.prompt()
                            .system("""
                                Actúas como un Arquitecto de Software Senior y Tech Lead.
                                Analiza el documento técnico provisto y extrae la información.
                                DEBES responder ÚNICAMENTE con un objeto JSON válido que tenga exactamente estos campos:
                                {
                                  "summary": "un texto corto con el resumen",
                                  "detectedTechnologies": ["lista", "de", "tecnologias"],
                                  "architectureType": "el tipo de arquitectura",
                                  "potentialRisks": ["lista", "de", "riesgos"],
                                  "recommendations": "recomendaciones de arquitectura"
                                }
                                No añadas ninguna explicación, ni introducciones, ni texto fuera del JSON.
                                """)
                            .user(documentContent)
                            .call()
                            .entity(AnalysisResultDTO.class); // Mapeo automático de Spring AI al Record Java

                    // 3. Devolvemos el DTO con el análisis de la IA incluido (Yield para expresiones switch)
                    yield new UploadResponseDTO(
                            f.getOriginalFilename(),
                            f.getSize(),
                            f.getContentType(),
                            "Análisis completado con éxito por la IA local",
                            aiAnalysis
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error al leer el contenido del archivo técnico", e);
                }
            }
        };
    }

    private static boolean isAllowedContentType(String contentType) {
        return switch (contentType) {
            case null -> false;
            case String type when type.isBlank() -> false;
            case String type -> {
                try {
                    var mediaType = MediaType.parseMediaType(type);
                    yield mediaType.isCompatibleWith(MediaType.TEXT_PLAIN)
                            || mediaType.isCompatibleWith(MediaType.APPLICATION_PDF);
                } catch (InvalidMediaTypeException _) {
                    yield false;
                }
            }
        };
    }
}
