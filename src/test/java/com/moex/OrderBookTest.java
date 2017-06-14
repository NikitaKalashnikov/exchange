package com.moex;

import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import com.moex.models.Order;
import com.moex.parsers.OrderParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OrderBookTest {

    Map<Instrument, OrderBook> books = new HashMap<>();

    @Before
    public void setUp() {
        Clients.clearClients();

        // Стаканы для инструментов
        for (Instrument instrument: Instrument.values()) {
            books.put(instrument, new OrderBook());
        }

    }

    @Test
    public void match() throws Exception {
        createClients();

        OrderParser parser = new OrderParser();
        Order order = parser.parseOrder("C2\tb\tD\t4\t3");
        books.get(order.getInstrument()).match(order);
        order = parser.parseOrder("C1\ts\tD\t4\t3");
        books.get(order.getInstrument()).match(order);

        long dAmount = Clients.getClient("C1").getInstruments().get(Instrument.D);
        Assert.assertEquals(317, dAmount);
        dAmount = Clients.getClient("C2").getInstruments().get(Instrument.D);
        Assert.assertEquals(563, dAmount);
        long dollar = Clients.getClient("C1").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(1012, dollar);
        dollar = Clients.getClient("C2").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(4338, dollar);

    }

    @Test
    public void match2() throws Exception {
        createClients();

        OrderParser parser = new OrderParser();
        Order order = parser.parseOrder("C1\tb\tD\t4\t3");
        books.get(order.getInstrument()).match(order);
        order = parser.parseOrder("C2\tb\tD\t4\t3");
        books.get(order.getInstrument()).match(order);
        // Продажа будет осуществлена по цене, которая в стакане, т.е. 4
        order = parser.parseOrder("C3\ts\tD\t3\t10");
        books.get(order.getInstrument()).match(order);

        long dAmount = Clients.getClient("C1").getInstruments().get(Instrument.D);
        Assert.assertEquals(323, dAmount);
        dAmount = Clients.getClient("C2").getInstruments().get(Instrument.D);
        Assert.assertEquals(563, dAmount);
        dAmount = Clients.getClient("C3").getInstruments().get(Instrument.D);
        Assert.assertEquals(94, dAmount);
        long dollar = Clients.getClient("C1").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(988, dollar);
        dollar = Clients.getClient("C2").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(4338, dollar);
        dollar = Clients.getClient("C3").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(824, dollar);

        // А эта заявка выполнится по 3
        order = parser.parseOrder("C1\tb\tD\t5\t3");
        books.get(order.getInstrument()).match(order);


        dAmount = Clients.getClient("C1").getInstruments().get(Instrument.D);
        Assert.assertEquals(326, dAmount);
        dAmount = Clients.getClient("C3").getInstruments().get(Instrument.D);
        Assert.assertEquals(91, dAmount);
        dollar = Clients.getClient("C1").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(979, dollar);
        dollar = Clients.getClient("C3").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(833, dollar);
        // В стакане одна заявка на продажу по 3$ в количестве 1

        // Купит 1 единицу по цене 3, 10 единиц в стакан
        order = parser.parseOrder("C1\tb\tD\t4\t11");
        books.get(order.getInstrument()).match(order);
        order = parser.parseOrder("C2\tb\tD\t5\t4");
        books.get(order.getInstrument()).match(order);
        // Не будет выполнена, попадет в стакан. Цена продажи высока
        order = parser.parseOrder("C3\ts\tD\t6\t10");
        books.get(order.getInstrument()).match(order);
        // 4 продажи по цене 5, 1 в стакан на продажу
        order = parser.parseOrder("C3\ts\tD\t5\t5");
        books.get(order.getInstrument()).match(order);
        // 6 продаж по 4
        order = parser.parseOrder("C3\ts\tD\t4\t6");
        books.get(order.getInstrument()).match(order);
        // 4 продаж по 4 - кончились заявки на покупку
        order = parser.parseOrder("C3\ts\tD\t4\t6");
        books.get(order.getInstrument()).match(order);

        dAmount = Clients.getClient("C1").getInstruments().get(Instrument.D);
        Assert.assertEquals(337, dAmount);
        dAmount = Clients.getClient("C2").getInstruments().get(Instrument.D);
        Assert.assertEquals(567, dAmount);
        dAmount = Clients.getClient("C3").getInstruments().get(Instrument.D);
        Assert.assertEquals(76, dAmount);
        dollar = Clients.getClient("C1").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(936, dollar);
        dollar = Clients.getClient("C2").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(4318, dollar);
        dollar = Clients.getClient("C3").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(896, dollar);

        // Заявка летит в стакан и ничего не меняет
        order = parser.parseOrder("C2\tb\tD\t2\t400");
        books.get(order.getInstrument()).match(order);

        dAmount = Clients.getClient("C1").getInstruments().get(Instrument.D);
        Assert.assertEquals(337, dAmount);
        dAmount = Clients.getClient("C2").getInstruments().get(Instrument.D);
        Assert.assertEquals(567, dAmount);
        dAmount = Clients.getClient("C3").getInstruments().get(Instrument.D);
        Assert.assertEquals(76, dAmount);
        dollar = Clients.getClient("C1").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(936, dollar);
        dollar = Clients.getClient("C2").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(4318, dollar);
        dollar = Clients.getClient("C3").getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(896, dollar);


    }

    private void createClients() {
        Clients.addClient(createClient("C1", 1000, 130, 240, 760, 320));
        Clients.addClient(createClient("C2", 4350, 370, 120, 950, 560));
        Clients.addClient(createClient("C3", 800, 100, 100, 100, 100));
    }

    private Client createClient(String name, long dollar, long a, long b, long c, long d) {
        Client client = new Client(name);
        client.addInstrument(Instrument.DOLLAR, dollar);
        client.addInstrument(Instrument.A, a);
        client.addInstrument(Instrument.B, b);
        client.addInstrument(Instrument.C, c);
        client.addInstrument(Instrument.D, d);
        return client;
    }

}