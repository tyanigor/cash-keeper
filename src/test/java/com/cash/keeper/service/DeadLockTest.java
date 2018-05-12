package com.cash.keeper.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Тест на dead lock
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeadLockTest {

    @Autowired
    private IOperationService operationService;

    @Autowired
    private IAccountService accountService;

    private final static int threadsCount = 400;

    private List<Callable<Object>> tasks = new ArrayList<>();

    /**
     * Предварительная инициализация
     */
    @Before
    public void init() {
        boolean b = false;
        // Перевод с первого счета на второй 1 USD
        Callable<Object> callableFromFirstToSecond = () -> {
            operationService.transfer(1, 2, BigDecimal.ONE);
            return null;
        };
        // Перевод со второго счета на первый 1 USD
        Callable<Object> callableFromSecondToFirst = () -> {
            operationService.transfer(2, 1, BigDecimal.ONE);
            return null;
        };

        // Создание двух счетов
        accountService.createAccount("first", "USD");
        accountService.createAccount("second", "USD");

        // Пополним первый счет и второй по 200 USD
        operationService.income(1L, BigDecimal.valueOf(200));
        operationService.income(2L, BigDecimal.valueOf(200));

        // Создание тасков 200 с переводом с первого на второй, и 200 со второго на первый счет
        for (int i = 0; i < threadsCount; i++) {
            b = !b;
            tasks.add(b ? callableFromFirstToSecond : callableFromSecondToFirst);
        }
    }

    /**
     * Тест на взаимную блокировку
     *
     * @throws InterruptedException
     */
    @Test
    public void deadLockTest() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadsCount * 2);
        // Запуск всех потоков на выполнение
        pool.invokeAll(tasks);
        pool.shutdown();

        // Если выполнится assertTrue значит не зависли на взаимной блокировке и счета сошлись
        Assert.assertTrue(accountService.getAccountList().get(0).getBalance().compareTo(BigDecimal.valueOf(200)) == 0);
        Assert.assertTrue(accountService.getAccountList().get(1).getBalance().compareTo(BigDecimal.valueOf(200)) == 0);
    }
}
