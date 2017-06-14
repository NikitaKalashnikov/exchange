package com.moex.models;

import com.moex.enums.Instrument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

    private Client client;

    @Before
    public void setUp() {
        client = new Client("name");
    }

    @Test
    public void addInstrument() throws Exception {

        Long amountA = client.getInstruments().get(Instrument.A);
        Assert.assertEquals(null, amountA);

        client.addInstrument(Instrument.A, null);
        Assert.assertEquals(null, amountA);
        client.addInstrument(Instrument.A, 55L);
        amountA = client.getInstruments().get(Instrument.A);
        Assert.assertEquals(new Long(55), amountA);

        Long amountB = client.getInstruments().get(Instrument.B);
        Assert.assertEquals(null, amountB);
        Long amountDollar = client.getInstruments().get(Instrument.DOLLAR);
        Assert.assertEquals(null, amountDollar);

        client.addInstrument(Instrument.A, 12L);
        amountA = client.getInstruments().get(Instrument.A);
        Assert.assertEquals(new Long(67), amountA);

        client.addInstrument(Instrument.A, -8L);
        amountA = client.getInstruments().get(Instrument.A);
        Assert.assertEquals(new Long(59), amountA);
    }

    @Test
    public void getName() throws Exception {
        Assert.assertTrue("name".equals(client.getName()));
    }

    @Test
    public void getInstruments() throws Exception {
        Assert.assertEquals(0, client.getInstruments().size());
        client.addInstrument(Instrument.A, 55L);
        Assert.assertEquals(1, client.getInstruments().size());
        client.addInstrument(Instrument.A, 55L);
        Assert.assertEquals(1, client.getInstruments().size());
        client.addInstrument(Instrument.B, 55L);
        Assert.assertEquals(2, client.getInstruments().size());
        client.addInstrument(Instrument.C, 55L);
        Assert.assertEquals(3, client.getInstruments().size());
        client.addInstrument(Instrument.D, 55L);
        Assert.assertEquals(4, client.getInstruments().size());
        client.addInstrument(Instrument.DOLLAR, 55L);
        Assert.assertEquals(5, client.getInstruments().size());
        client.addInstrument(Instrument.A, 55L);
        Assert.assertEquals(5, client.getInstruments().size());
    }

}