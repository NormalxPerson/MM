package com.moneymanager.service;

import com.moneymanager.core.Account;
import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.repos.TransactionRepo;

import java.util.ArrayList;
import java.util.List;

// TODO: Create a TransactionServiceInterface
public class TransactionService {
	private final TransactionRepo transRepo;
	private final AccountService accountService;
	
	public TransactionService(TransactionRepo transRepo, AccountService accountService) {
		this.transRepo = transRepo;
		this.accountService = accountService;
		
	}
	
	public void createTransactionListFromUser(double amount, String description, String date, String type, String accountId) {
		List<Transaction> transactionList = new ArrayList<>();
		Transaction transaction = TransactionFactory.createTransaction(amount, description, date, type, accountId, transRepo);
		System.out.println(transaction.toString());
		transactionList.add(transaction);
		transRepo.addTransactions(transactionList);
		updateAccountBalance(accountId, transaction.getAmount());
	}
	
	private void updateAccountBalance(String accountId, double amount) {
		accountService.updateBalance(accountId, amount);
		
	}
	
	public List<Transaction> getListOfAllTransactions() {
		return transRepo.getAllTransactions();
	}
	
	
	
	
}
