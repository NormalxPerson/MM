package com.moneymanager.service;

import com.moneymanager.core.Account;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.SQLiteAccountRepo;

import java.util.*;

// TODO: Create an AccountServiceInterface
public class AccountService {
    private AccountRepo accountRepo;


    public AccountService(AccountRepo accountRepo) {
        
        this.accountRepo = accountRepo;
    }
    
    public void updateBalance(String accountId, double amount) {
        Account account = accountRepo.getAccountById(accountId);
        System.out.println(account.toString());
        double oldBalance = account.getBalance();
        
        account.setBalance(oldBalance + amount);
        System.out.println(account.toString());
        
        accountRepo.updateAccountBalance(account);
    }
    
    public Account createAccount(String accountName, String bankName, String accountType) {
        // Validate inputs
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be empty");
        }
        if (bankName == null || bankName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be empty");
        }
        if (accountType == null || (!accountType.equals("DEBT") && !accountType.equals("CREDIT"))) {
            throw new IllegalArgumentException("Account type must be DEBT or CREDIT");
        }
        
        Account newAccount = new Account(accountName, bankName, accountType);
        
        accountRepo.addAccount(newAccount);
        return newAccount;
    }
    
    public Account getAccountByAccountId(String accountId) {
        return accountRepo.getAccountById(accountId);
    }
    
    public Map<String, Account> getAccountMap() {return accountRepo.getAccountMap();}
    
    public List<Account> getAccountList() {
	    return new ArrayList<>(accountRepo.getAllAccounts());
    }


}

