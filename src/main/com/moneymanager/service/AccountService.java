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
    
    public void loadAccountModelsObservableList() {
        updateTheSourceAccountMap();
        accountModelObservableList.clear();
        
        for (Account account : theSourceHashMapOfAccounts.values()) {
            AccountTableView.AccountModel accountModel = new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType().name(), account.getBalance(), account.getAccountId());
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

    
    public AccountTableView.AccountModel createAndAddAccount(String accountName, String bankName, String accountType, Double balance) {
        Account newAccount = new Account(accountName, bankName, accountType, balance);
        String newId = accountRepo.addAccountAndReturnId(newAccount);
        newAccount.setAccountId(newId);
        
        theSourceHashMapOfAccounts.put(newId,newAccount);
        
        AccountTableView.AccountModel newAccountModel = createModelFromAccount(newAccount);
        accountModelObservableList.add(newAccountModel);
	    return newAccountModel;
    }
    
    public void updateAccount(AccountTableView.AccountModel accountModel) {
        accountRepo.updateAccount(accountModel);
    }
    
    
    public String getAccountNameByAccountId(String accountId) {
        return theSourceHashMapOfAccounts.get(accountId).getAccountName();
    }
    
    public HashMap<String, Account> getAccountMap() {return theSourceHashMapOfAccounts;}
    
    private void updateTheSourceAccountMap() {this.theSourceHashMapOfAccounts = (HashMap<String, Account>) accountRepo.getAccountMap();}
    
    public List<Account> getAccountList() {
	    return new ArrayList<>(accountRepo.getAllAccounts());
    }
    
    public AccountTableView.AccountModel createModelFromAccount(Account account) {
	    return new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType().name(), account.getBalance(), account.getAccountId());
     
    }
    
    public int deleteAccountById(AccountTableView.AccountModel accountModel) {
        if (accountModelObservableList.contains(accountModel)) {
            accountModelObservableList.remove(accountModel);
            int rowDeleted = accountRepo.deleteAccountById(accountModel.getAccountId());
            
            if (rowDeleted == 1 && theSourceHashMapOfAccounts.containsKey(accountModel.getAccountId())) {
                System.out.println("Deleted account with ID: " + accountModel.getAccountId());
                theSourceHashMapOfAccounts.remove(accountModel.getAccountId());
                return rowDeleted;
            }
        }
        return 0;
    }



}

