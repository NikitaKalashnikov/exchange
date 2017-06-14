package com.moex.parsers;

import com.moex.enums.Direction;
import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import com.moex.models.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderParserTest {

    @Before
    public void setUp() {
        Clients.clearClients();
    }

    @Test
    public void parseOrder() throws Exception {
        OrderParser parser = new OrderParser();
        Clients.addClient(new Client("C2"));
        Order order = parser.parseOrder("C2\tb\tD\t4\t3");
        Assert.assertEquals(Direction.BUY, order.getDirection());
        Assert.assertEquals(Instrument.D, order.getInstrument());
        Assert.assertEquals(4, order.getPrice());
        Assert.assertEquals(3, order.getAmount());

        boolean b = false;
        try {
            parser.parseOrder("C2\tb\tDt4\t3");
        } catch (IllegalArgumentException ex) {
            b = true;
            Assert.assertEquals("Неверное количество лексем в строке. Исходная строка: C2\tb\tDt4\t3", ex.getMessage());
        }
        Assert.assertTrue(b);

        try {
            parser.parseOrder("C0\tb\tD\t4\t3");
        } catch(IllegalStateException ex) {
            Assert.assertEquals("Получена заявка от неизвестного клиента: C0", ex.getMessage());
            b = false;
        }
        Assert.assertFalse(b);

    }

}