package com.cash.keeper.domain;

/**
 * Валюта
 */
public enum Currency {

    /**
     * Рубли
     */
    RUB,

    /**
     * Доллары
     */
    USD,

    /**
     * Евро
     */
    EUR;

    public static boolean contains(String value) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
