package com.moneymanager.repos;

import com.moneymanager.core.Account;
import com.moneymanager.ui.view.AccountTableView;

import java.util.List;
import java.util.Map;

public interface AccountRepo {
    
    List<Account> getAllAccounts();
    void addAccount(Account account);
    Map<String, Account> getAccountMap();
    Account getAccountById(String id);
    void updateAccountBalance(Account account);
    public void updateAccount(AccountTableView.AccountModel account);
}
