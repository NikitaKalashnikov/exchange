package com.moex.models;

import com.moex.enums.Instrument;
import com.moex.enums.Direction;

/**
 * Класс заявки
 */
public class Order {
    // Клиент
    private Client client;
    // Направление заявки
    private Direction direction;
    // Инструмент
    private Instrument instrument;
    // Количество актива
    private long amount;
    // цена за единицу
    private long price;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override public String toString() {
        return "Order{" +
                "client=" + client.getName() +
                ", direction=" + direction +
                ", instrument=" + instrument +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
