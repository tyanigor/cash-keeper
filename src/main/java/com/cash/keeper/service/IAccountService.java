package com.cash.keeper.service;

import com.cash.keeper.domain.Account;

import java.util.List;

public interface IAccountService {

    List<Account> getAccountList();

    void createAccount(String fio, String currency);
}
