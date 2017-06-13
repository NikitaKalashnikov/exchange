package com.moex;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Класс парсит файл с начальными позициями клиентов
 */
public class ClientParser {

    /**
     * Метод парсит строку с начальными позициями клиента
     * @param source - исходная строка с позициями клиента
     * @return - класс клиента
     */
    private static Client parseClient(String source) {
        String[] items = source.split("\t");
        // Количество лексем в строке равно количеству активов плюс имя клиента
        if (items.length != Actives.values().length + 1) {
            throw new IllegalStateException(String.format("Неверное количество лексем в строке. " +
                    "Исходная строка: %s", source));
        }
        Client client = new Client(items[0].trim());
        final int[] i = {1};
        Arrays.stream(Actives.values()).forEach(active -> client.addActive(active, new BigDecimal(items[i[0]++].trim())));
        return client;
    }

    public static Collection<Client> parseClients(String fileName) {
        Collection<Client> clients = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                try {
                    Client client = parseClient(scanner.nextLine());
                    clients.add(client);
                } catch (Exception ex) {
                    // TODO: пропускаем клиента, пишем в лог
                }
            }
            return clients;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return clients;
        }
    }
}
