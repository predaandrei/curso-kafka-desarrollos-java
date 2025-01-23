package com.imagina.kafka.broker.stream.commodity;

import com.imagina.kafka.broker.message.OrderMessage;
import com.imagina.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

//@Component
public class MaskOrderStream {

    //@Autowired
    void kstreamCommodityMask(StreamsBuilder builder) {
        var orderSerde = new JsonSerde<>(OrderMessage.class);
        var maskedCreditCardStream = builder.stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

        maskedCreditCardStream.to("t-commodity-order-masked", Produced.with(Serdes.String(), orderSerde));

        maskedCreditCardStream.print(Printed.<String, OrderMessage>toSysOut().withLabel("Masked Order Stream"));
    }

}
