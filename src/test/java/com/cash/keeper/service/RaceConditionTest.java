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
 * Тест на race condition
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaceConditionTest {

    @Autowired
    private IOperationService operationService;

    @Autowired
    private IAccountService accountService;

    private final static int threadsCount = 200;

    private List<Callable<Object>> tasks = new ArrayList<>();

    /**
     * Предварительная инициализация
     */
    @Before
    public void init() {
        // Создание двух счетов
        accountService.createAccount("first", "USD");
        accountService.createAccount("second", "USD");

        // Пополним первый счет 200 USD
        operationService.income(1L, BigDecimal.valueOf(200));

        // Создание тасков
        for (int i = 0; i < threadsCount; i++) {
            tasks.add(() -> {
                // Каждый поток переводит с первого счета на второй 1 USD
                operationService.transfer(1, 2, BigDecimal.ONE);
                return null;
            });
        }
    }

    /**
     * Тест перевода денег при состоянии гонки
     *
     * @throws InterruptedException
     */
    @Test
    public void raceConditionTest() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);
        // Запуск всех потоков на выполнение
        pool.invokeAll(tasks);
        pool.shutdown();

        // Проверяем, все ли деньги перешли с первого счета на второй
        Assert.assertTrue(accountService.getAccountList().get(0).getBalance().compareTo(BigDecimal.ZERO) == 0);
        Assert.assertTrue(accountService.getAccountList().get(1).getBalance().compareTo(BigDecimal.valueOf(200)) == 0);
    }
}
