package com.moex.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс содержит список
 */
public class Clients {

    private static Map<String, Client> clients;

    private static synchronized Map<String, Client> getInstance() {
        if (clients == null) {
            clients = new HashMap<>();
        }
        return clients;
    }

    public static void addClient(Client client) {
        getInstance().put(client.getName(), client);
    }

    public static Client getClient(String clientName) {
        return getInstance().get(clientName);
    }

    public static Collection<Client> getClients() {
        return getInstance().values();
    }

    // Класс нужен только для тестов
    public static void clearClients() {
        getInstance().clear();
    }
}
