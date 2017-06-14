package com.moex.parsers;

import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class ClientParserTest {

    @Before
    public void setUp() {
        Clients.clearClients();
    }

    @Test
    public void parseClientsException() throws Exception {
        try {
            ClientParser.parseClients("NoNamesdf");
        } catch (FileNotFoundException ex) {
            return;
        }
        fail();
    }

    @Test
    public void parseClients() throws Exception {

        File file = new File("testFile1");
        boolean b = file.createNewFile();
        Assert.assertTrue(b);
        file.deleteOnExit();

        PrintWriter pw = new PrintWriter(file);
        pw.println("C1\t1000\t130\t240\t760\t320");
        pw.println("C2\t4350\t370\t120\t950\t560");
        pw.flush();
        pw.close();

        ClientParser.parseClients(file.getAbsolutePath());

        Assert.assertEquals(2, Clients.getClients().size());
        Client client = Clients.getClient("C1");
        Assert.assertEquals(new Long(1000), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(130), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(240), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(760), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(320), client.getInstruments().get(Instrument.D));
    }

    @Test
    public void parseClientsWithError() throws Exception {

        File file = new File("testFile2");
        boolean b = file.createNewFile();
        Assert.assertTrue(b);
        file.deleteOnExit();

        PrintWriter pw = new PrintWriter(file);
        pw.println("C3\t1000\t240\t760\t320");
        pw.println("C4\t4350\t370\t120\t950\t560");
        pw.flush();
        pw.close();

        ClientParser.parseClients(file.getAbsolutePath());

        Assert.assertEquals(1, Clients.getClients().size());
        Client client = Clients.getClient("C3");
        Assert.assertNull(client);
        client = Clients.getClient("C4");
        Assert.assertEquals(new Long(4350), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(370), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(120), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(950), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(560), client.getInstruments().get(Instrument.D));
    }

}