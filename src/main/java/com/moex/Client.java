package com.moex;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс описывает клиента и его активы
 */
public class Client {
    /**
     * Имя клиента
     */
    private String name;
    /**
     * Мапа с активами. Ключ - актив, значение - количество актива
     */
    private Map<Actives, BigDecimal> actives = new HashMap<>();

    public Client(String name) {
        this.name = name;
    }

    /**
     * Метод добавляет клиенту базовый актив. Если у клиента не было указанного актива - значит у него 0.
     * @param active - добавляемый актив
     * @param amount - количество актива
     */
    public void addActive(Actives active, BigDecimal amount) {
        if (amount == null) {
            return;
        }
        BigDecimal initional = actives.computeIfAbsent(active, actives -> BigDecimal.ZERO);
        initional.add(amount);
    }

    public String getName() {
        return name;
    }

    public Map<Actives, BigDecimal> getActives() {
        return actives;
    }
}
