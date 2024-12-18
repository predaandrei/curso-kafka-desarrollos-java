package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.imagina.core_consumer.model.SolicitudCompra;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SolicitudCompraConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("cacheSolicitudCompra")
    private Cache<UUID, Boolean> cache;

    private boolean isInCache(UUID solicitudCompraId) {
        return Optional.ofNullable(cache.getIfPresent(solicitudCompraId)).orElse(false);
    }

    @KafkaListener(topics = "t-solicitud-compra")
    public void consume(String message) throws JsonMappingException, JsonProcessingException {
        var solicitudCompra = objectMapper.readValue(message, SolicitudCompra.class);
        var processed = isInCache(solicitudCompra.getSolicitudId());
        if (processed) {
            return;
        }
        log.info("Procesando solicitud de compra {}", solicitudCompra);
        cache.put(solicitudCompra.getSolicitudId(), true);
    }
}
