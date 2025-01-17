package com.moneymanager.repos;

import com.moneymanager.core.Account;
import java.util.List;
import java.util.Map;

public interface AccountRepo {
    List<Account> getAllAccounts();
    void addAccount(Account account);
    Map<String, Account> getAccountMap();
}
