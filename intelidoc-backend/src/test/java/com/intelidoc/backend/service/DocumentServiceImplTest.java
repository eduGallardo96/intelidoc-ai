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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private ChatClient.Builder chatClientBuilder; // Mockeamos el Builder que pide tu constructor

    @Mock
    private ChatClient chatClient; // Mockeamos el cliente final

    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        // Antes de cada test, configuramos el Builder para que devuelva nuestro chatClient mockeado al hacer .build()
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Inicializamos manualmente el servicio inyectándole el Builder entrenado
        documentService = new DocumentServiceImpl(chatClientBuilder);
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
    @DisplayName("4. Debería procesar el archivo e invocar a la IA correctamente cuando el formato es válido")
    void upload_ShouldReturnAnalysis_WhenFileIsValid() {
        // Arrange (Preparación)
        String mockContent = "PROYECTO: Test. TECNOLOGÍAS: Java, Angular.";
        MultipartFile validFile = new MockMultipartFile("file", "test.txt", "text/plain", mockContent.getBytes());

        // RETURNS_DEEP_STUBS simula toda la cadena fluida (.prompt().system().user().call().entity())
        ChatClient.ChatClientRequestSpec mockRequestSpec = mock(ChatClient.ChatClientRequestSpec.class, Answers.RETURNS_DEEP_STUBS);

        AnalysisResultDTO expectedAiResult = new AnalysisResultDTO(
                "Resumen de prueba",
                List.of("Java", "Angular"),
                "Microservicios",
                List.of("Ninguno"),
                "Seguir así"
        );

        when(chatClient.prompt()).thenReturn(mockRequestSpec);
        when(mockRequestSpec.system(anyString()).user(anyString()).call().entity(AnalysisResultDTO.class))
                .thenReturn(expectedAiResult);

        // Act (Ejecución)
        UploadResponseDTO response = documentService.upload(validFile);

        // Assert (Verificación)
        assertNotNull(response);
        assertEquals("test.txt", response.filename());
        assertEquals("Análisis completado con éxito por la IA local", response.message());
        assertNotNull(response.analysis());
        assertEquals("Resumen de prueba", response.analysis().summary());
        assertEquals("Microservicios", response.analysis().architectureType());
    }
}
