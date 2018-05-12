package com.cash.keeper.repository;

import com.cash.keeper.domain.Account;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 * Репозиторий для работы со счетами
 */
public interface AccountRepository extends BaseRepository<Account, Long> {

    /**
     * Выборка счета по идентификатору с пессимистической блокировкой
     * select for update (нужно при обновлении баланса)
     *
     * @param id Идентификатор счета
     * @return Заблокированный счет
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account findById(Long id);
}
