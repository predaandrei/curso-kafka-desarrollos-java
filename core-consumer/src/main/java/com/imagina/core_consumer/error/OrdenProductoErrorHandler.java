package com.imagina.core_consumer.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "ordenProductoErrorHandler")
public class OrdenProductoErrorHandler implements ConsumerAwareListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        log.warn("Error en el procesado de una orden de producto, enviando el error a ElasticSearch : {}, " +
                "causa : {}", message.getPayload(), exception.getMessage());

        if (exception.getCause() instanceof RuntimeException) {
            throw exception;
        }
        return null;
    }
}
