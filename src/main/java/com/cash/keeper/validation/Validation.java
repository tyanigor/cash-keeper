package com.cash.keeper.validation;

import com.cash.keeper.domain.Currency;

import java.math.BigDecimal;

public final class Validation {

    private Validation() {
        throw new RuntimeException("Класс валидации не может быть инстанциирован");
    }

    /**
     * Проверка баланс счета минус сумма больше единицы или нет
     *
     * @param accountBalance Баланс счета
     * @param amount Сумма
     * @param message Сообщение об ошибке
     */
    public static void assertNegativeBalance(BigDecimal accountBalance, BigDecimal amount, String message) {
        if (accountBalance == null || amount == null || !(accountBalance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Проверка сумма меньше нуля
     *
     * @param amount Сумма
     * @param message Сообщение об ошибке
     */
    public static void assertAboveZero(BigDecimal amount, String message) {
        if (amount == null || !(amount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Сравнение эквивалентности валют
     *
     * @param firstCurrency Первая валюта для сравнения
     * @param secondCurrency Вторая валюта для сравнения
     * @param message Сообщение об ошибке в случае неравенства
     */
    public static void assertEqualsCurrency(Currency firstCurrency, Currency secondCurrency, String message) {
        if (firstCurrency == null || secondCurrency == null || !firstCurrency.equals(secondCurrency)) {
            throw new IllegalArgumentException(message);
        }
    }
}
