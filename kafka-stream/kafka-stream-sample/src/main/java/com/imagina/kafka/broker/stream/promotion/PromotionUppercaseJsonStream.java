package com.imagina.kafka.broker.stream.promotion;

import com.imagina.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class PromotionUppercaseJsonStream {

    //@Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    void kstreamPromotionUppercase(StreamsBuilder builder) {
        var sourceStream = builder.stream("t-commodity-promotion", Consumed.with(Serdes.String(), Serdes.String()));
        var uppercaseStream = sourceStream.mapValues(this::uppercasePromotionCode);

        uppercaseStream.to("t-commodity-promotion-uppercase");

        sourceStream.print(Printed.<String, String>toSysOut().withLabel("JSON Original Stream"));
        uppercaseStream.print(Printed.<String, String>toSysOut().withLabel("JSON Uppercase Stream"));
    }

    private String uppercasePromotionCode(String jsonString) {
        try {
            var promotion = objectMapper.readValue(jsonString, PromotionMessage.class);
            promotion.setPromotionCode(promotion.getPromotionCode().toUpperCase());
            return objectMapper.writeValueAsString(promotion);
        } catch (Exception e) {
            log.warn("Unable to process JSON", e);
            return "";
        }
    }

}
