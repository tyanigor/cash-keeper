package com.cash.keeper.service;

import com.cash.keeper.domain.Account;
import com.cash.keeper.domain.Operation;
import com.cash.keeper.domain.OperationType;
import com.cash.keeper.repository.AccountRepository;
import com.cash.keeper.repository.OperationRepository;
import com.cash.keeper.validation.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис по операциям со счетами
 */
@Service
@Transactional
public class OperationServiceImpl implements IOperationService {

    private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);

    @Autowired
    OperationRepository operationRepository;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Приход денег на счет
     *
     * @param accountId Идентификатор счета операции
     * @param amount Сумма пополнения
     */
    @Override
    public void income(long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId);

        // Валидация
        Assert.notNull(account, "Счет #" + accountId + " не найден");
        Validation.assertAboveZero(amount, "Сумма не может быть меньше или равная нулю");

        // Изменение баланса
        account.setBalances(account.getBalance().add(amount));

        // Добавление новой операции к счету
        Operation operation = new Operation(null, account, amount, OperationType.INCOME);
        operationRepository.save(operation);
        logger.info("Операция пополнения #{}", operation.getId());
    }

    /**
     * Списание денег со счета
     *
     * @param accountId Идентификатор счета операции
     * @param amount Сумма снятия
     */
    @Override
    public void expense(long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId);

        // Валидация
        Assert.notNull(account, "Счет #" + accountId + " не найден");
        Validation.assertAboveZero(amount, "Сумма не может быть меньше или равная нулю");
        Validation.assertNegativeBalance(account.getBalance(), amount, "Сумма снятия привышает баланс");

        // Изменение баланса
        account.setBalances(account.getBalance().subtract(amount));

        // Добавление новой операции к счету
        Operation operation = new Operation(account, null, amount, OperationType.EXPENSE);
        operationRepository.save(operation);
        logger.info("Операция списания #{}", operation.getId());
    }

    /**
     * Перевод денег с одного счета, на другой
     *
     * @param accountIdFrom Счет с которого списывают
     * @param accountIdTo Счет на который зачисляют
     * @param amount Сумма перевода
     */
    @Override
    public void transfer(long accountIdFrom, long accountIdTo, BigDecimal amount) {
        Validation.assertAboveZero(amount, "Сумма не может быть меньше или равная нулю");
        if (accountIdFrom == accountIdTo) {
            throw new IllegalArgumentException("Одинаковые счета перевода");
        }

        Account accountFrom;
        Account accountTo;

        // Выстраиваем порядок блокировки, чтобы избежать взаимной блокировки
        if (accountIdFrom > accountIdTo) {
            accountTo = accountRepository.findById(accountIdTo);
            accountFrom = accountRepository.findById(accountIdFrom);
        } else {
            accountFrom = accountRepository.findById(accountIdFrom);
            accountTo = accountRepository.findById(accountIdTo);
        }

        // Валидация
        Assert.notNull(accountFrom, "Счет #" + accountIdFrom + " не найден");
        Assert.notNull(accountTo, "Счет #" + accountIdTo + " не найден");
        Validation.assertNegativeBalance(accountFrom.getBalance(), amount, "Сумма перевода привышает баланс счета #" + accountIdFrom);
        Validation.assertEqualsCurrency(accountFrom.getCurrency(), accountTo.getCurrency(), "Валюта счетов не совпадает");

        // Изменение баланса
        accountFrom.setBalances(accountFrom.getBalance().subtract(amount));
        accountTo.setBalances(accountTo.getBalance().add(amount));
        Operation operation = new Operation(accountFrom, accountTo, amount, OperationType.TRANSFER);

        // Добавление новой операции к счету
        operationRepository.save(operation);
        logger.info("Операция перевода #{}", operation.getId());
    }

    /**
     * Получение списка операций по счету
     *
     * @param accountId Идентификатор счета
     * @return Список операций
     */
    @Override
    public List<Operation> getOperationList(long accountId) {
        Account account = accountRepository.findById(accountId);
        return operationRepository.findAllByAccountFromOrAccountTo(account, account);
    }
}
