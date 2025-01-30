package com.imagina.kafka.broker.stream.client;

import com.imagina.kafka.broker.message.ClientInfoAggregateMessage;
import com.imagina.kafka.broker.message.ClientInfoShoppingCartMessage;
import org.apache.kafka.streams.kstream.Aggregator;

public class ClientInfoShoppingCartAggregator
        implements Aggregator<String, ClientInfoShoppingCartMessage, ClientInfoAggregateMessage> {

    @Override
    public ClientInfoAggregateMessage apply(String key, ClientInfoShoppingCartMessage value,
                                            ClientInfoAggregateMessage aggregate) {
        aggregate.putShoppingCartItem(value.getItemName(), value.getCartDatetime());

        return aggregate;
    }

}
