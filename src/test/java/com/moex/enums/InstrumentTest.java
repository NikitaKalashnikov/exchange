package com.moex.enums;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class InstrumentTest {
    @Test
    public void findByName() throws Exception {
        Assert.assertEquals(Instrument.findByName("A"), Instrument.A);
        Assert.assertEquals(Instrument.findByName("B"), Instrument.B);
        Assert.assertEquals(Instrument.findByName("C"), Instrument.C);
        Assert.assertEquals(Instrument.findByName("D"), Instrument.D);
        Assert.assertEquals(Instrument.findByName("$"), Instrument.DOLLAR);
        try {
            Instrument.findByName("E");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Не найден инструмент с именем E", ex.getMessage());
            return;
        }
        fail();
    }

}