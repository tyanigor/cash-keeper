package com.cash.keeper.repository;

import com.cash.keeper.domain.Account;
import com.cash.keeper.domain.Operation;

import java.util.List;

/**
 * Репозиторий для работы с операциями
 */
public interface OperationRepository extends BaseRepository<Operation, Long> {

    /**
     * Поиск операций по счетам
     *
     * @param accountFrom Счет с которого списывали
     * @param accountTo Счет на который переводили
     * @return Список операций
     */
    List<Operation> findAllByAccountFromOrAccountTo(Account accountFrom, Account accountTo);
}
