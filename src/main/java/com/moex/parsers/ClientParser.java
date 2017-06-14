package com.moex.parsers;

import com.moex.enums.Instrument;
import com.moex.models.Client;
import com.moex.models.Clients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс парсит файл с начальными позициями клиентов и заполняет список клиентов <see>Clients</see>
 */
public class ClientParser {

    private static final Logger log = LoggerFactory.getLogger(ClientParser.class);

    /*
     * Метод парсит строку с начальными позициями клиента
     * @param source - исходная строка с позициями клиента
     * @return - класс клиента
     */
    private static Client parseClient(String source) {
        String[] items = source.split("\t");
        // Количество лексем в строке равно количеству активов плюс имя клиента
        if (items.length != Instrument.values().length + 1) {
            throw new IllegalStateException(String.format("Неверное количество лексем в строке. " +
                    "Исходная строка: %s", source));
        }
        Client client = new Client(items[0].trim());
        final int[] i = {1};
        Arrays.stream(Instrument.values()).forEach(instrument -> client.addInstrument(instrument, Long.parseLong(items[i[0]++].trim())));
        return client;
    }

    /**
     * Метод парсит файл с начальными позициями клиента
     * @param fileName - имя файла с клиентами
     */
    public static void parseClients(String fileName) throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                try {
                    Client client = parseClient(scanner.nextLine());
                    Clients.addClient(client);
                } catch (Exception ex) {
                    log.error("Ошибка парсинга клиента {}", ex.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Не найден файл с клиентами: {}", e);
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
