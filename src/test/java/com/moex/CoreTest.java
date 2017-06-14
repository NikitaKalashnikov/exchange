package com.moex;

import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CoreTest {

    @Before
    public void setUp() {
        Clients.clearClients();
    }

    @Test
    public void mainTest() throws IOException {
        File file = new File("clients_1.tmp");
        boolean b = file.createNewFile();
        Assert.assertTrue(b);
        file.deleteOnExit();

        PrintWriter pw = new PrintWriter(file);
        pw.print("C1\t1000\t130\t240\t760\t320\n"
                + "C2\t4350\t370\t120\t950\t560\n"
                + "C3\t2760\t0\t0\t0\t0\n"
                + "C4\t560\t450\t540\t480\t950\n"
                + "C5\t1500\t0\t0\t400\t100\n"
                + "C6\t1300\t890\t320\t100\t0\n"
                + "C7\t750\t20\t0\t790\t0\n"
                + "C8\t7000\t90\t190\t0\t0\n"
                + "C9\t7250\t190\t190\t0\t280");
        pw.flush();
        pw.close();

        file = new File("orders_1.tmp");
        b = file.createNewFile();
        Assert.assertTrue(b);
        file.deleteOnExit();

        pw = new PrintWriter(file);
        pw.print("C2\ts\tC\t14\t5\n"
                        + "C8\tb\tC\t15\t4\n"
                        + "C2\ts\tC\t13\t2\n"
                        + "C9\tb\tB\t6\t4\n"
                        + "C4\tb\tD\t5\t4\n"
                        + "C8\tb\tD\t4\t5\n"
                        + "C8\tb\tA\t11\t1\n"
                        + "C6\tb\tC\t15\t1\n"
                        + "C9\tb\tB\t7\t3\n"
                        + "C6\ts\tA\t9\t1\n"
                        + "C4\ts\tD\t3\t2\n"
                        + "C9\tb\tD\t5\t5\n"
                        + "C8\tb\tB\t6\t5\n"
                        + "C6\ts\tB\t5\t2\n"
                        + "C5\tb\tB\t5\t3\n"
                        + "C9\tb\tC\t14\t5\n"
                        + "C6\ts\tA\t9\t1\n"
                        + "C2\tb\tD\t4\t3\n"
                        + "C2\ts\tC\t14\t2\n"
                        + "C1\ts\tC\t15\t3");
        pw.flush();
        pw.close();

        Core.main(new String[] {"clients_1.tmp", "orders_1.tmp", "result_1.tmp"});

        file = new File("result_1.tmp");
        Assert.assertTrue(file.delete());

        Client client = Clients.getClient("C2");
        Assert.assertEquals(new Long(4474), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(370), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(120), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(941), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(560), client.getInstruments().get(Instrument.D));

        client = Clients.getClient("C1");
        Assert.assertEquals(new Long(1000), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(130), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(240), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(760), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(320), client.getInstruments().get(Instrument.D));

        client = Clients.getClient("C4");
        // Изменений нет, т.к. сделка заключена с собой
        Assert.assertEquals(new Long(560), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(450), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(540), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(480), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(950), client.getInstruments().get(Instrument.D));

        client = Clients.getClient("C6");
        // Изменений нет, т.к. сделка заключена с собой
        Assert.assertEquals(new Long(1312), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(889), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(318), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(101), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(0), client.getInstruments().get(Instrument.D));

        client = Clients.getClient("C8");
        Assert.assertEquals(new Long(6933), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(91), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(190), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(4), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(0), client.getInstruments().get(Instrument.D));

        client = Clients.getClient("C9");
        Assert.assertEquals(new Long(7181), client.getInstruments().get(Instrument.DOLLAR));
        Assert.assertEquals(new Long(190), client.getInstruments().get(Instrument.A));
        Assert.assertEquals(new Long(192), client.getInstruments().get(Instrument.B));
        Assert.assertEquals(new Long(4), client.getInstruments().get(Instrument.C));
        Assert.assertEquals(new Long(280), client.getInstruments().get(Instrument.D));
    }
}