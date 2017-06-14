package com.moex.enums;

/**
 * Перечисление активов
 */
public enum Instrument {
    DOLLAR("$"),
    A ("A"),
    B ("B"),
    C ("C"),
    D ("D");

    private String name;

    Instrument(String name) {
        this.name = name;
    }

    public static Instrument findByName(String name) {
        for (Instrument instrument : values()) {
            if (instrument.name.equals(name)) {
                return instrument;
            }
        }
        throw new IllegalArgumentException(String.format("Не найден инструмент с именем %s", name));
    }
}
