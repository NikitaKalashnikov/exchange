package com.moex.enums;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {
    @Test
    public void findByName() throws Exception {
        Assert.assertEquals(Direction.findByName("s"), Direction.SELL);
        Assert.assertEquals(Direction.findByName("b"), Direction.BUY);
        try {
            Direction.findByName("c");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Некорректное направление заявки c", ex.getMessage());
            return;
        }
        fail();
    }

}