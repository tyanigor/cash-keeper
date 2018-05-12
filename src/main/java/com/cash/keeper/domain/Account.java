package com.cash.keeper.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Счет
 */
@Entity
@Table(name = "t_account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор счета
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * ФИО
     */
    @Column(nullable = false, length = 128)
    private String fio;

    /**
     * Баланс изменяется с добавлением операции, нужен чтобы постоянно не пересчитывать его при запросе
     */
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Валюта счета
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Currency currency = Currency.USD;

    public Account() {
    }

    public Account(String fio, Currency currency) {
        this.fio = fio;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalances(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
