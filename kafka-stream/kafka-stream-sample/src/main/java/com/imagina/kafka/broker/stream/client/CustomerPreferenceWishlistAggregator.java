package com.imagina.kafka.broker.stream.client;

import com.imagina.kafka.broker.message.ClientInfoAggregateMessage;
import com.imagina.kafka.broker.message.ClientInfoWishlistMessage;
import org.apache.kafka.streams.kstream.Aggregator;

public class CustomerPreferenceWishlistAggregator
        implements Aggregator<String, ClientInfoWishlistMessage, ClientInfoAggregateMessage> {

    @Override
    public ClientInfoAggregateMessage apply(String key, ClientInfoWishlistMessage value,
                                                    ClientInfoAggregateMessage aggregate) {
        aggregate.putWishlistItem(value.getItemName(), value.getWishlistDatetime());
        return aggregate;
    }

}
