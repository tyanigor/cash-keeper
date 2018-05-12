package com.cash.keeper.domain;

/**
 * Тип операции
 */
public enum OperationType {

    /**
     * Приход
     */
    INCOME,

    /**
     * Расход
     */
    EXPENSE,

    /**
     * Перевод с одного счета на другой
     */
    TRANSFER;

    public static boolean contains(String value) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
