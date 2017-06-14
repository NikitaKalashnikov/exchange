package com.moex;

import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import com.moex.models.Order;
import com.moex.parsers.ClientParser;
import com.moex.parsers.OrderParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Точка входа
 */
public class Core {

    private static final int ERROR_CODE = 17;
    private static final Logger log = LoggerFactory.getLogger(Core.class);

    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            log.error("Должно быть передано три аргумента: имя файла с клиентами, имя файла с заявками,"
                    + "имя файла для вывода результатов");
            System.exit(ERROR_CODE);
        }
        log.info("Файл с клиентами {}, файл с заявками {}, файла с результатами {}", (Object[]) args);
        try {
            ClientParser.parseClients(args[0]);
        } catch (FileNotFoundException e) {
            log.error("Не найден файл с клиентами: {}", args[0]);
        }

        OrderParser parser = new OrderParser();
        // Стаканы для инструментов
        Map<Instrument, OrderBook> books = new HashMap<>();
        for (Instrument instrument: Instrument.values()) {
            books.put(instrument, new OrderBook());
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(args[1]));
            while (scanner.hasNextLine()) {
                String orderSource = scanner.nextLine();
                try {
                    Order order = parser.parseOrder(orderSource);
                    OrderBook book = books.get(order.getInstrument());
                    book.match(order);
                } catch (Exception ex) {
                    log.error("Ошибка парсинга заявки: {}", orderSource);
                }
            }
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(args[2].trim());
            Collection<Client> clients = Clients.getClients();
            @SuppressWarnings("unchecked")
            ArrayList<Client> clientList = new ArrayList(clients);
            clientList.sort(Comparator.comparing(Client::getName));
            for (Client client : clientList) {
                pw.println(String.format("%s\t%d\t%d\t%d\t%d\t%d", client.getName(),
                        client.getInstruments().get(Instrument.DOLLAR), client.getInstruments().get(Instrument.A),
                        client.getInstruments().get(Instrument.B), client.getInstruments().get(Instrument.C),
                        client.getInstruments().get(Instrument.D)));
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }

    }
}
