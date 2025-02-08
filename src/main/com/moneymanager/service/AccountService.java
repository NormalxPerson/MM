package com.moneymanager.service;

import com.moneymanager.core.Account;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

// TODO: Create an AccountServiceInterface
public class AccountService {
    private AccountRepo accountRepo;
    private HashMap<String, Account> theSourceHashMapOfAccounts;
    private ObservableList<AccountTableView.AccountModel> accountModelObservableList;

    public AccountService(AccountRepo accountRepo) {
        
        this.accountRepo = accountRepo;
        this.accountModelObservableList = FXCollections.observableArrayList();
        loadAccountModelsObservableList();
    }
    
    public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() {
        loadAccountModelsObservableList();
        return accountModelObservableList;
    }
    
    private void loadAccountModelsObservableList() {
        updateTheSourceAccountMap();
        accountModelObservableList.clear();
        
        for (Account account : theSourceHashMapOfAccounts.values()) {
            AccountTableView.AccountModel accountModel = new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType(), account.getBalance(), account.getAccountId());
            accountModelObservableList.add(accountModel);
        }
    }
    
    public void updateBalance(String accountId, double amount) {
        Account account = theSourceHashMapOfAccounts.get(accountId);
        System.out.println("Updating account balance: AccountService.updateBalance: Old Balance: " + account.getBalance() + ", Transaction Amount: " + amount);
        account.setBalance(account.getBalance() + amount);
        System.out.println("Updated account balance: AccountService.updateBalance: New Balance: " + account.getBalance());
        
        
        accountModelObservableList.get(Integer.parseInt(accountId)-1).setAccountBalance(account.getBalance());
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
        loadAccountModelsObservableList();
        return newAccount;
    }
    
    public void addModelToAccountTableView(AccountTableView.AccountModel accountModel) {
        accountModelObservableList.add(accountModel);
    }
    
    public Account getAccountByAccountId(String accountId) {
        return theSourceHashMapOfAccounts.get(accountId);
    }
    
    public String getAccountNameByAccountId(String accountId) {
        return theSourceHashMapOfAccounts.get(accountId).getAccountName();
    }
    
    public HashMap<String, Account> getAccountMap() {return theSourceHashMapOfAccounts;}
    
    private void updateTheSourceAccountMap() {this.theSourceHashMapOfAccounts = (HashMap<String, Account>) accountRepo.getAccountMap();}
    
    public List<Account> getAccountList() {
	    return new ArrayList<>(accountRepo.getAllAccounts());
    }



}

