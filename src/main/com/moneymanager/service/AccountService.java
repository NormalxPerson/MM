package com.moneymanager.service;

import com.moneymanager.core.Account;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

// TODO: Create an AccountServiceInterface
public class AccountService {
    private AccountRepo accountRepo;
    private HashMap<String, Account> theSourceHashMapOfAccounts;
    private ObservableMap<String, AccountTableView.AccountModel> accountModelMap;
    private ObservableList<AccountTableView.AccountModel> accountModelObservableList;

    public AccountService(AccountRepo accountRepo) {
        
        this.accountRepo = accountRepo;
        this.accountModelMap = FXCollections.observableHashMap();
        this.accountModelObservableList = FXCollections.observableArrayList(accountModelMap.values());
        
        accountModelMap.addListener((MapChangeListener.Change<? extends String, ? extends AccountTableView.AccountModel> change) -> {
            accountModelObservableList.setAll(accountModelMap.values());
        });
        
        loadAccountModelsObservableList();
    }
    
    public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() {
        loadAccountModelsObservableList();
        return accountModelObservableList;
    }
    
    public void loadAccountModelsObservableList() {
        updateTheSourceAccountMap();
        accountModelMap.clear();
        
        for (Account account : theSourceHashMapOfAccounts.values()) {
            AccountTableView.AccountModel accountModel = new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType().name(), account.getBalance(), account.getAccountId());
            accountModelMap.put(accountModel.getAccountId(),accountModel);
        }
    }
    public void updateBalance(String accountId, double amount) {
        Account account = theSourceHashMapOfAccounts.get(accountId);
        System.out.println("Updating account balance: AccountService.updateBalance: Old Balance: " + account.getBalance() + ", Transaction Amount: " + amount);
        account.setBalance(account.getBalance() + amount);
        System.out.println("Updated account balance: AccountService.updateBalance: New Balance: " + account.getBalance());
        
        
        accountModelMap.get(account.getAccountId()).setAccountBalance(account.getBalance());
        accountRepo.updateAccountBalance(account);
        
    }

    
    public AccountTableView.AccountModel createAndAddAccount(String accountName, String bankName, String accountType, Double balance) {
        Account newAccount = new Account(accountName, bankName, accountType, balance);
        accountRepo.saveNewAccount(newAccount);
        theSourceHashMapOfAccounts.put(newAccount.getAccountId(),newAccount);
        
        AccountTableView.AccountModel newAccountModel = createModelFromAccount(newAccount);
        accountModelMap.put(newAccountModel.getAccountId(), newAccountModel);
	    return newAccountModel;
    }
    
    public void updateAccount(AccountTableView.AccountModel accountModel) {
        accountRepo.updateAccount(accountModel);
    }
    
    
    public String getAccountNameByAccountId(String accountId) {
        if (theSourceHashMapOfAccounts.containsKey(accountId)) {
            return theSourceHashMapOfAccounts.get(accountId).getAccountName();
        } else { return "ACCOUNT_DELETED"; }
    }
    
    
    private void updateTheSourceAccountMap() {this.theSourceHashMapOfAccounts = (HashMap<String, Account>) accountRepo.getAccountMap();}
    
    public AccountTableView.AccountModel createModelFromAccount(Account account) {
	    return new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType().name(), account.getBalance(), account.getAccountId());
     
    }
    
    public int deleteAccountById(AccountTableView.AccountModel accountModel) {
        if (accountModelMap.containsKey(accountModel.getAccountId())) {
            accountModelMap.remove(accountModel.getAccountId());
            int rowDeleted = accountRepo.deleteAccountById(accountModel.getAccountId());
            
            if (rowDeleted == 1 && theSourceHashMapOfAccounts.containsKey(accountModel.getAccountId())) {
                System.out.println("Deleted account with ID: " + accountModel.getAccountId());
                theSourceHashMapOfAccounts.remove(accountModel.getAccountId());
                accountModelMap.remove(accountModel.getAccountId());
                return rowDeleted;
            }
        }
        return 0;
    }
    
    public ObservableMap<String, AccountTableView.AccountModel> getAccountModelMap() {
        return accountModelMap;
    }
    
    



}

