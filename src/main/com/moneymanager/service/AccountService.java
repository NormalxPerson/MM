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
    
    public void createAccount(AccountTableView.AccountModel accountModel) {
        System.out.println(accountModel.toString());
        Account newAccount = new Account(accountModel.getAccountName(), accountModel.getBankName(), accountModel.getAccountType(), accountModel.getAccountBalance());
        System.out.println(newAccount.toString());
        accountRepo.addAccount(newAccount);
    }
    
    public AccountTableView.AccountModel createAndGetBlankAccountModel() {
        AccountTableView.AccountModel blankAccountModel = new AccountTableView.AccountModel("", "", "", 0.00, "");
	    
	    accountModelObservableList.add(blankAccountModel);

        return blankAccountModel;
    }
    
    public void removeBlankAccountModel(AccountTableView.AccountModel accountModel) {
        accountModelObservableList.remove(accountModel);
    }
    
    public void addModelToAccountTableView(AccountTableView.AccountModel accountModel) {
        
        accountModelObservableList.add(accountModel);
    }
    
    public AccountTableView.AccountModel createAddAndGetNewAccountModel(String accountName, String bankName, String accountType, Double balance) {
        String  id = String.valueOf(accountModelObservableList.size() + 1);
        Account newAccount = new Account(id, accountName, bankName, accountType, balance);
        accountRepo.addAccount(newAccount);
	    return createModelFromAccount(newAccount);
    }
    
    public void updateAccount(AccountTableView.AccountModel accountModel) {
        accountRepo.updateAccount(accountModel);
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
    
    public AccountTableView.AccountModel createModelFromAccount(Account account) {
	    return new AccountTableView.AccountModel(account.getAccountName(), account.getBankName(), account.getAccountType(), account.getBalance(), account.getAccountId());
     
    }



}

