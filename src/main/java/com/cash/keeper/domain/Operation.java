package com.cash.keeper.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Операция
 */
@Entity
@Table(name = "t_operation")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор операции
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Дата операции
     */
    @Column(nullable = false)
    private Date date = new Date();

    /**
     * Счет с которого снимают/переводят
     */
    @ManyToOne
    private Account accountFrom;

    /**
     * Счет на который пополняют/переводят
     */
    @ManyToOne
    private Account accountTo;

    /**
     * Сумма операции
     */
    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * Тип операции
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OperationType operationType;

    public Operation() {
    }

    public Operation(Account accountFrom, Account accountTo, BigDecimal amount, OperationType operationType) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
