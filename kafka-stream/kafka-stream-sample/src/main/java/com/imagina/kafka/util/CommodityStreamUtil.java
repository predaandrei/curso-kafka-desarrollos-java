package com.imagina.kafka.util;

import java.time.OffsetDateTime;
import java.util.Base64;

import com.imagina.kafka.broker.message.OrderMessage;
import com.imagina.kafka.broker.message.OrderPatternMessage;
import com.imagina.kafka.broker.message.OrderRewardMessage;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;

public class CommodityStreamUtil {

    public static OrderMessage maskCreditCardNumber(OrderMessage orderMessage) {
        String creditCardNumber = orderMessage.getCreditCardNumber();
        String maskedCreditCardNumber = "****-****-****-" + creditCardNumber.substring(creditCardNumber.length() - 4);

        return new OrderMessage(
                orderMessage.getOrderLocation(),
                orderMessage.getOrderNumber(),
                maskedCreditCardNumber,
                orderMessage.getOrderDateTime(),
                orderMessage.getItemName(),
                orderMessage.getPrice(),
                orderMessage.getQuantity());
    }

    public static OrderPatternMessage convertToOrderPatternMessage(OrderMessage orderMessage) {
        String itemName = orderMessage.getItemName();
        long totalItemAmount = orderMessage.getPrice() * orderMessage.getQuantity();
        OffsetDateTime orderDateTime = orderMessage.getOrderDateTime();
        String orderLocation = orderMessage.getOrderLocation();
        String orderNumber = orderMessage.getOrderNumber();

        return new OrderPatternMessage(itemName, totalItemAmount, orderDateTime, orderLocation, orderNumber);
    }

    public static OrderRewardMessage convertToOrderRewardMessage(OrderMessage orderMessage) {
        String orderLocation = orderMessage.getOrderLocation();
        String orderNumber = orderMessage.getOrderNumber();
        OffsetDateTime orderDateTime = orderMessage.getOrderDateTime();
        String itemName = orderMessage.getItemName();
        int price = orderMessage.getPrice();
        int quantity = orderMessage.getQuantity();

        return new OrderRewardMessage(orderLocation, orderNumber, orderDateTime, itemName, price, quantity);
    }

    public static Predicate<String, OrderMessage> isLargeQuantity() {
        return (key, orderMessage) -> orderMessage.getQuantity() > 200;
    }

    public static Predicate<String, OrderPatternMessage> isPlastic() {
        return (key, orderPatternMessage) -> orderPatternMessage.getItemName().toUpperCase().startsWith("PLASTIC");
    }

    public static Predicate<String, OrderMessage> isCheap() {
        return (key, orderMessage) -> orderMessage.getPrice() < 100;
    }

    public static KeyValueMapper<String, OrderMessage, String> generateStorageKey() {
        return (key, orderMessage) -> Base64.getEncoder().encodeToString(orderMessage.getOrderNumber().getBytes());
    }

    public static KeyValueMapper<String, OrderMessage, KeyValue<String, OrderRewardMessage>> mapToOrderRewardChangeKey() {
        return (key, orderMessage) -> KeyValue.pair(orderMessage.getOrderLocation(),
                convertToOrderRewardMessage(orderMessage));
    }

}