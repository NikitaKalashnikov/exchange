package com.moex.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientsTest {

    @Before
    public void setUp() {
        Clients.clearClients();
    }

    @Test
    public void clientTest() throws Exception {
        int size = Clients.getClients().size();
        Assert.assertEquals(0, size);
        Clients.addClient(new Client("name"));
        size = Clients.getClients().size();
        Assert.assertEquals(1, size);
        Client client = Clients.getClient("noName");
        Assert.assertNull(client);
        client = Clients.getClient("name");
        Assert.assertNotNull(client);
        Assert.assertEquals("name", client.getName());
    }

}