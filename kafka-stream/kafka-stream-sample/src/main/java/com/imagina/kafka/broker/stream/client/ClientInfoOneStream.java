package com.imagina.kafka.broker.stream.client;

import com.imagina.kafka.broker.message.ClientInfoAggregateMessage;
import com.imagina.kafka.broker.message.ClientInfoShoppingCartMessage;
import com.imagina.kafka.broker.message.ClientInfoWishlistMessage;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class ClientInfoOneStream {

    private static final ClientInfoShoppingCartAggregator SHOPPING_CART_AGGREGATOR = new ClientInfoShoppingCartAggregator();
    private static final CustomerPreferenceWishlistAggregator WISHLIST_AGGREGATOR = new CustomerPreferenceWishlistAggregator();

    @Autowired
    void kstreamClientInfo(StreamsBuilder builder) {
        var stringSerde = Serdes.String();
        var shoppingCartSerde = new JsonSerde<>(ClientInfoShoppingCartMessage.class);
        var wishlistSerde = new JsonSerde<>(ClientInfoWishlistMessage.class);
        var aggregateSerde = new JsonSerde<>(ClientInfoAggregateMessage.class);

        var groupedShoppingCartStream = builder.stream("t-commodity-customer-preference-shopping-cart",
                Consumed.with(stringSerde, shoppingCartSerde)).groupByKey();

        var groupedWishlistStream = builder.stream("t-commodity-customer-preference-wishlist",
                Consumed.with(stringSerde, wishlistSerde)).groupByKey();

        var customerPreferenceStream = groupedShoppingCartStream
                .cogroup(SHOPPING_CART_AGGREGATOR)
                .cogroup(groupedWishlistStream, WISHLIST_AGGREGATOR)
                .aggregate(
                        ClientInfoAggregateMessage::new,
                        Materialized.with(stringSerde, aggregateSerde))
                .toStream();

        customerPreferenceStream.to("t-commodity-customer-preference-all",
                Produced.with(stringSerde, aggregateSerde));
    }
}