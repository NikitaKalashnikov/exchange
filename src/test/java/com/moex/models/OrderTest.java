package com.moex.models;

import com.moex.enums.Direction;
import com.moex.enums.Instrument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {

    private Order order;

    @Test
    public void orderTest() throws Exception {
        Order order = new Order();
        Assert.assertNull(order.getClient());
        Assert.assertNull(order.getDirection());
        Assert.assertNull(order.getInstrument());
        Assert.assertEquals(0, order.getAmount());
        Assert.assertEquals(0, order.getPrice());

        Client client = new Client("name");
        order.setClient(client);
        order.setDirection(Direction.BUY);
        order.setInstrument(Instrument.A);
        order.setAmount(33L);
        order.setPrice(21);

        Assert.assertEquals(client, order.getClient());
        Assert.assertEquals("name", order.getClient().getName());
        Assert.assertEquals(Direction.BUY, order.getDirection());
        Assert.assertEquals(Instrument.A, order.getInstrument());
        Assert.assertEquals(33, order.getAmount());
        Assert.assertEquals(21, order.getPrice());

        Assert.assertEquals("Order{client=name, direction=BUY, instrument=A, amount=33, price=21}",
                order.toString());
    }


}