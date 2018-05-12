package com.cash.keeper.controller;

import com.cash.keeper.domain.Account;
import com.cash.keeper.domain.Operation;
import com.cash.keeper.dto.ErrorDTO;
import com.cash.keeper.service.IAccountService;
import com.cash.keeper.service.IOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Базовый контроллер REST API
 */
@RestController
@RequestMapping("/api")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    IAccountService accountService;

    @Autowired
    IOperationService operationService;

    /**
     * Получение списка счетов
     *
     * @return Список счетов
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/accounts"}, method = RequestMethod.GET)
    public List<Account> getAccountList() {
        return accountService.getAccountList();
    }

    /**
     * Получение списка операций по счету
     *
     * @param accountId Идентификатор счета
     * @return Список операций
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/operations/{accountId}"}, method = RequestMethod.GET)
    public List<Operation> getAccountList(@PathVariable long accountId) {
        return operationService.getOperationList(accountId);
    }

    /**
     * Создание счета
     *
     * @param fio ФИО
     * @param currency Валюта счета
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = {"/account"}, method = RequestMethod.POST)
    public void createAccount(@RequestParam String fio, @RequestParam String currency) {
        accountService.createAccount(fio, currency);
    }

    /**
     * Операция пополнения счета
     *
     * @param accountId Идентификатор счета пополнения
     * @param amount Сумма пополнения
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/income"}, method = RequestMethod.POST)
    public void income(@RequestParam long accountId, @RequestParam BigDecimal amount) {
        operationService.income(accountId, amount);
    }

    /**
     * Операция снятия денег со счета
     *
     * @param accountId Идентификатор счета списания
     * @param amount Сумма списания
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/expense"}, method = RequestMethod.POST)
    public void expense(@RequestParam long accountId, @RequestParam BigDecimal amount) {
        operationService.expense(accountId, amount);
    }

    /**
     * Операция перевода денег с одного счета, на другой
     *
     * @param accountIdFrom Идентификатор счета с которого переводим деньги
     * @param accountIdTo Идентификатор счета на который переводим деньги
     * @param amount Сумма перевода
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/transfer"}, method = RequestMethod.POST)
    public void transfer(
            @RequestParam long accountIdFrom,
            @RequestParam long accountIdTo,
            @RequestParam BigDecimal amount) {

        operationService.transfer(accountIdFrom, accountIdTo, amount);
    }

    /**
     * Обработчик ошибок, формирует JSON с типом исключения и описанием ошибки
     *
     * @param e Исключение
     * @return Ошибка
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView errorHandler(Exception e) {
        logger.warn("Ошибка", e);
        return new ErrorDTO(e.getMessage(), e.getClass().getSimpleName()).asModelAndView();
    }
}
