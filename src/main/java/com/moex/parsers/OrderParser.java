package com.moex.parsers;

import com.moex.enums.Direction;
import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import com.moex.models.Order;

/**
 * Класс для парсинга заявок
 */
public class OrderParser {

    // Константа для количества лексем в строке.
    private static final int AMOUNT_ITEM_IN_STRING = 5;

    public Order parseOrder(String source) {
        String[] items = source.split("\t");
        if (items.length != AMOUNT_ITEM_IN_STRING) {
            throw new IllegalArgumentException(String.format("Неверное количество лексем в строке. " +
                    "Исходная строка: %s", source));
        }
        Order order = new Order();
        String clientName = items[0].trim();
        Client client = Clients.getClient(clientName);
        if (client == null) {
            throw new IllegalStateException("Получена заявка от неизвестного клиента: " + clientName);
        }
        order.setClient(client);
        order.setDirection(Direction.findByName(items[1].trim()));
        order.setInstrument(Instrument.findByName(items[2].trim()));
        order.setPrice(Long.parseLong(items[3].trim()));
        order.setAmount(Long.parseLong(items[4].trim()));
        return order;
    }
}
