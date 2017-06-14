package com.moex.enums;

/**
 * Перечисление возможных направлений заявок (купить или продать)
 */
public enum Direction {
    BUY ("b"),
    SELL ("s");

    private String name;

    Direction(String name) {
        this.name = name;
    }

    public static Direction findByName(String name) {
        for (Direction direction: values()) {
            if (direction.name.equals(name)) {
                return direction;
            }
        }
        throw new IllegalArgumentException(String.format("Некорректное направление заявки %s", name));
    }
}
