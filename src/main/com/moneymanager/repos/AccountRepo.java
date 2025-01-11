package com.moneymanager.repos;

import com.moneymanager.core.Account;
import java.util.List;

public interface AccountRepo {
    List<Account> getAllAccounts();
    void addAccount(Account account);
}
