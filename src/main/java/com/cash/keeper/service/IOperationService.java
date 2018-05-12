package com.cash.keeper.service;

import com.cash.keeper.domain.Operation;

import java.math.BigDecimal;
import java.util.List;

public interface IOperationService {

    void income(long accountId, BigDecimal amount);

    void expense(long accountId, BigDecimal amount);

    void transfer(long accountIdFrom, long accountIdTo, BigDecimal amount);

    List<Operation> getOperationList(long accountId);
}
