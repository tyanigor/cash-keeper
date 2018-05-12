package com.cash.keeper.service;

import com.cash.keeper.domain.Account;
import com.cash.keeper.domain.Currency;
import com.cash.keeper.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис по работе со счетами
 */
@Service
@Transactional
public class AccountServiceImpl implements IAccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountRepository accountRepository;

    /**
     * Получение списка счетов
     *
     * @return Список счетов
     */
    @Override
    public List<Account> getAccountList() {
        return accountRepository.findAll();
    }

    /**
     * Создание счета
     *
     * @param fio ФИО
     * @param currency Валюта
     */
    @Override
    public void createAccount(String fio, String currency) {
        // Валидация
        if (fio == null || fio.length() < 1 || fio.length() > 128) {
            throw new IllegalArgumentException("Недопустимое значение ФИО");
        }
        if (!Currency.contains(currency)) {
            throw new IllegalArgumentException("Не указана валюта");
        }

        // Создание счета
        Account account = new Account(fio, Currency.valueOf(currency));
        accountRepository.save(account);
        logger.info("Создан счет #{}", account.getId());
    }
}
