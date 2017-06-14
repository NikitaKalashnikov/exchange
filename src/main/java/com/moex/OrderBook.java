package com.moex;

import com.moex.enums.Direction;
import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import com.moex.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * Класс стакана заявок. Матчит заявки и обновляет активы клиентов
 */
public class OrderBook {

    private static final Logger log = LoggerFactory.getLogger(OrderBook.class);
    // Счетчик. Проставляется для каждой заявки. Нужен для различия заявок с одной ценой
    private long counter = 0;

    private class OrderKey {
        long price;
        long index = ++counter;

        OrderKey(long price) {
            this.price = price;
        }
    }

    // Стакан с заявками на покупку. В нем ищем контрагента для заявки на продажу.
    // Должен быть отсортирован по уменьшению
    private TreeMap<OrderKey, Order> orderBookBuy;
    // Стакан с заявками на продажу. В нем ищем контрагента для заявки на покупку.
    // Должен быть отсортирован по возрастанию
    private TreeMap<OrderKey, Order> orderBookSell;

    public OrderBook() {
        // У компараторов сначала сравниваем по цене, затем по индексу - меньше индекс - больше приоритет
        orderBookBuy = new TreeMap<>((o1, o2) -> {
            long result = o2.price - o1.price;
            return result > 0 ? 1 : result == 0 ? compareLong(o2.index - o1.index) : -1;
        });

        orderBookSell = new TreeMap<>((o1, o2) -> {
            long result = o1.price - o2.price;
            return result > 0 ? 1 : result == compareLong(o1.index - o2.index) ? 0 : -1;
        });
    }

    private int compareLong(long comp) {
        return comp > 0 ? 1 : comp == 0 ? 0 : -1;
    }

    /**
     * Метод матчит заявки и/или добавляет заявку в стакан
     *
     * @param order - заявка для матчинга
     */
    public void match(Order order) {
        log.trace("Обработка заявки {}", order);
        Direction direction = order.getDirection();
        // Прилетела заявка на покупку. Пытаемся сматчит с заявками из стакана на продажу
        if (direction == Direction.BUY) {
            match(orderBookSell, orderBookBuy, order, 1);
        } else { // Заявка на продажу
            match(orderBookBuy, orderBookSell, order, -1);
        }
    }

    // Метод матчит заявки на покупку или продажу. Параметр buy может быть 1(buy) или -1(sell), нужен для добавления
    // или вычитания актива
    private void match(TreeMap<OrderKey, Order> book1, TreeMap<OrderKey, Order> book2, Order order, int buy) {
        while (order.getAmount() > 0) {
            try {
                // Ищем заявку на продажу с минимальной ценой
                OrderKey key = book1.firstKey();
                if (buy * key.price > buy * order.getPrice()) {
                    // Цена заявки на покупку меньше минимальной заявки на продажу. Ставим всю заявку на покупку в
                    // стакан для покупок
                    book2.put(new OrderKey(order.getPrice()), order);
                    log.trace("В стакан для покупки добавлена заявка {}", order);
                    break;
                } else {
                    // Матчим заявку по цене, которая в стакане
                    Order matchOrder = book1.get(key);
                    // Максимальное количество для матчинга
                    long maxAmount = Math.min(matchOrder.getAmount(), order.getAmount());
                    matchOrder.setAmount(matchOrder.getAmount() - maxAmount);
                    order.setAmount(order.getAmount() - maxAmount);
                    // Клиент, выставивший завку на покупку получает актив и отдает доллары
                    Client client = Clients.getClient(order.getClient().getName());
                    client.addInstrument(order.getInstrument(), buy * maxAmount);
                    client.addInstrument(Instrument.DOLLAR, -1 * buy * maxAmount * matchOrder.getPrice());
                    // Контрагент лишается актива, но получает деньги
                    client = Clients.getClient(matchOrder.getClient().getName());
                    client.addInstrument(order.getInstrument(), -1 * buy * maxAmount);
                    client.addInstrument(Instrument.DOLLAR, buy * maxAmount * matchOrder.getPrice());

                    log.trace("Матчинг заявки с заявкой {} по цене {} в количестве {}", matchOrder, matchOrder.getPrice(),
                            maxAmount);

                    // Заявка закончилась - удаляем ее
                    if (matchOrder.getAmount() == 0) {
                        book1.remove(key);
                        log.trace("Удаление заявки из стакана", matchOrder);
                    }
                }
            } catch (NoSuchElementException ex) {
                // нет заявок на продажу, добавляем заявку в стакан на покупку
                book2.put(new OrderKey(order.getPrice()), order);
                log.trace("В стакан для покупки добавлена заявка {}", order);
                break;
            }
        }
    }

}
