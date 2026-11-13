package com.intelidoc.backend.service;

import com.intelidoc.backend.dto.AnalysisResultDTO;
import com.intelidoc.backend.dto.UploadResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private ChatClient.Builder chatClientBuilder; // Mockeamos el Builder que pide tu constructor

    @Mock
    private ChatClient chatClient; // Mockeamos el cliente final

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        // Antes de cada test, configuramos el Builder para que devuelva nuestro chatClient mockeado al hacer .build()
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Inicializamos manualmente el servicio inyectándole el Builder entrenado
        documentService = new DocumentServiceImpl(chatClientBuilder, kafkaTemplate, objectMapper);
    }

    @Test
    @DisplayName("1. Debería lanzar excepción si el archivo es nulo")
    void upload_ShouldThrowException_WhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            documentService.upload(null);
        });
    }

    @Test
    @DisplayName("2. Debería lanzar excepción si el archivo está vacío")
    void upload_ShouldThrowException_WhenFileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            documentService.upload(emptyFile);
        });
        assertEquals("El archivo está vacío", exception.getMessage());
    }

    @Test
    @DisplayName("3. Debería lanzar excepción si el Content-Type no está permitido")
    void upload_ShouldThrowException_WhenInvalidContentType() {
        MultipartFile invalidFile = new MockMultipartFile("file", "test.exe", "application/x-msdownload", "contenido".getBytes());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            documentService.upload(invalidFile);
        });
        assertTrue(exception.getMessage().contains("Tipo de archivo no permitido"));
    }

    @Test
    @DisplayName("4. Debería procesar el archivo, llamar a la IA y publicar evento en Kafka")
    void upload_ShouldReturnAnalysisAndPublishEvent_WhenFileIsValid() throws Exception {
        // Arrange
        String mockContent = "PROYECTO: Test. TECNOLOGÍAS: Java, Angular.";
        MultipartFile validFile = new MockMultipartFile("file", "test.txt", "text/plain", mockContent.getBytes());

        ChatClient.ChatClientRequestSpec mockRequestSpec = mock(ChatClient.ChatClientRequestSpec.class, Answers.RETURNS_DEEP_STUBS);
        String fakeRawJson = "{\"summary\":\"Resumen de prueba\"}";

        AnalysisResultDTO expectedAiResult = new AnalysisResultDTO(
                "Resumen de prueba",
                List.of("Java", "Angular"),
                "Microservicios",
                List.of("Ninguno"),
                "Seguir así"
        );

        // Entrenamos a ChatClient para que devuelva el String crudo en .content()
        when(chatClient.prompt()).thenReturn(mockRequestSpec);
        when(mockRequestSpec.system(anyString()).user(anyString()).call().content()).thenReturn(fakeRawJson);

        // Entrenamos a ObjectMapper para deserializar el JSON de la IA y serializar el evento de Kafka
        when(objectMapper.readValue(fakeRawJson, AnalysisResultDTO.class)).thenReturn(expectedAiResult);
        when(objectMapper.writeValueAsString(any(UploadResponseDTO.class))).thenReturn("{\"filename\":\"test.txt\"}");

        // Act
        UploadResponseDTO response = documentService.upload(validFile);

        // Assert
        assertNotNull(response);
        assertEquals("test.txt", response.filename());
        assertEquals("Resumen de prueba", response.analysis().summary());

        // Verificamos que realmente se envió el mensaje al tópico de Kafka una vez
        verify(kafkaTemplate, times(1)).send(eq("document-analysis-completed"), eq("test.txt"), anyString());
    }

}
