package com.intelidoc.backend.event;

import com.intelidoc.backend.dto.UploadResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class DocumentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DocumentEventConsumer.class);
    private final ObjectMapper objectMapper;

    public DocumentEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "document-analysis-completed", groupId = "intelidoc-audit-group")
    public void consumeAnalysisEvent(String rawJsonEvent) {
        try {
            UploadResponseDTO event = objectMapper.readValue(rawJsonEvent, UploadResponseDTO.class);
            log.info("📩 [KAFKA CONSUMER] Evento recibido del tópico 'document-analysis-completed'");
            log.info("📄 Archivo auditado: {} | Arquitectura detectada: {}",
                    event.filename(),
                    event.analysis() != null ? event.analysis().architectureType() : "N/A");
            log.info("📧 Simulando envío de informe técnico por correo electrónico al usuario...");
        } catch (Exception e) {
            log.error("❌ Error al deserializar el evento de Kafka", e);
        }
    }
}
