package com.imagina.kafka.broker.stream.client;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientPurchaseOneStream {

    @Autowired
    void kstreamClientPurchase(StreamsBuilder builder) {
        var stringSerde = Serdes.String();

        //var clientPurchaseAppStream = builder.stream("t-client-purchase-app", Consumed.with(stringSerde, stringSerde));
        //var clientPurchaseWebStream = builder.stream("t-client-purchase-web", Consumed.with(stringSerde, stringSerde));

        var topics = List.of("t-client-purchase-app", "t-client-purchase-web");

        builder.stream(topics, Consumed.with(stringSerde, stringSerde)).to("t-client-purchase-all");

        //clientPurchaseAppStream.merge(clientPurchaseWebStream).to("t-client-purchase-all");
    }
}
