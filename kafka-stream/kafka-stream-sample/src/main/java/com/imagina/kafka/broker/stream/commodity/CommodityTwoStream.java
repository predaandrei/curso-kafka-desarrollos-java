package com.imagina.kafka.broker.stream.commodity;

import com.imagina.kafka.broker.message.OrderMessage;
import com.imagina.kafka.broker.message.OrderPatternMessage;
import com.imagina.kafka.broker.message.OrderRewardMessage;
import com.imagina.kafka.util.CommodityStreamUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;

import org.springframework.stereotype.Component;

@Component
public class CommodityTwoStream {

        @Autowired
        void kstreamCommodityTrading(StreamsBuilder builder) {
                var orderSerde = new JsonSerde<>(OrderMessage.class);
                var orderPatternSerde = new JsonSerde<>(OrderPatternMessage.class);
                var orderRewardSerde = new JsonSerde<>(OrderRewardMessage.class);
                var stringSerde = Serdes.String();

                var maskedCreditCardStream = builder
                                .stream("t-commodity-order", Consumed.with(Serdes.String(), orderSerde))
                                .mapValues(CommodityStreamUtil::maskCreditCardNumber);

                maskedCreditCardStream.mapValues(CommodityStreamUtil::convertToOrderPatternMessage)
                                .split()
                                .branch(CommodityStreamUtil.isPlastic(),
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-two-plastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))))
                                .defaultBranch(
                                                Branched.<String, OrderPatternMessage>withConsumer(
                                                                ks -> ks.to("t-commodity-pattern-two-notplastic",
                                                                                Produced.with(stringSerde,
                                                                                                orderPatternSerde))));

                maskedCreditCardStream.filter(CommodityStreamUtil.isLargeQuantity())
                                .filterNot(CommodityStreamUtil.isCheap())
                                .mapValues(CommodityStreamUtil::convertToOrderRewardMessage)
                                .to("t-commodity-reward-two", Produced.with(Serdes.String(), orderRewardSerde));

                maskedCreditCardStream
                                .selectKey(CommodityStreamUtil.generateStorageKey())
                                .to("t-commodity-storage-two", Produced.with(Serdes.String(), orderSerde));
        }

}
