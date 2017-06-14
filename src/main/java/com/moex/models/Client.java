package com.moex.models;

import com.moex.enums.Instrument;

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
    private Map<Instrument, Long> instruments = new HashMap<>();

    public Client(String name) {
        this.name = name;
    }

    /**
     * Метод добавляет клиенту базовый актив. Если у клиента не было указанного актива - значит у него 0.
     * @param instrument - добавляемый актив
     * @param amount - количество актива
     */
    public void addInstrument(Instrument instrument, Long amount) {
        if (amount == null) {
            return;
        }
        Long initional = instruments.computeIfAbsent(instrument, instrument1 -> 0L);
        instruments.put(instrument, initional + amount);
    }

    public String getName() {
        return name;
    }

    public Map<Instrument, Long> getInstruments() {
        return instruments;
    }
}
