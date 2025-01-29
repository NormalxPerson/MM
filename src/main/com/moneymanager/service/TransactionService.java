package com.moneymanager.service;

import com.moneymanager.core.Account;
import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// TODO: Create a TransactionServiceInterface
public class TransactionService implements TransactionServiceInterface {
	private final TransactionRepo transRepo;
	private final AccountService accountService;
	private final ObservableList<TransactionTableView.TransactionModel> transactionModels;
	
	public TransactionService(TransactionRepo transRepo, AccountService accountService) {
		this.transRepo = transRepo;
		this.accountService = accountService;
		this.transactionModels = FXCollections.observableArrayList();
	}
	
	@Override
	public ObservableList<TransactionTableView.TransactionModel> getObservableTransactionModelsList() {
		loadObservableList();
		return transactionModels;
	}
	
	@Override
	public void createTransactionFromUser(double amount, String description, String date, String type, String accountId) {
		Transaction transaction = TransactionFactory.createTransaction(amount, description, date, type, accountId, transRepo);
		System.out.println("TransactionService.createTransactionFromUser: " + transaction.toString());

		saveTransaction(transaction);
		updateAccountBalance(accountId, transaction.getAmount());
		
		
	}
	
	private void saveTransaction(Transaction transaction) {
		transRepo.addTransactions(Collections.singletonList(transaction));
		transactionModels.add(createNewTransactionModel(transaction));
	}
	
	private void updateAccountBalance(String accountId, double amount) {
		accountService.updateBalance(accountId, amount);
		
	}
	
	private void addNewTransactionModelToTable(Transaction transaction) {
		transactionModels.add(createNewTransactionModel(transaction));
	}
	
	@Override
	public List<Transaction> getListAllTransactions() {
		return transRepo.getAllTransactions();
	}
	
	private void loadObservableList() {
		transactionModels.clear();
		List<Transaction> transactions = transRepo.getAllTransactions();
		for (Transaction transaction : transactions) {
			transactionModels.add(createNewTransactionModel(transaction));
		}
	}
	
	private TransactionTableView.TransactionModel createNewTransactionModel(Transaction transaction) {
		return new TransactionTableView.TransactionModel(transaction.getId(), transaction.getDate(), transaction.getAmount(), transaction.getDescription(), transaction.getType(), transaction.getAccountId(), accountService.getAccountNameByAccountId(transaction.getAccountId()));
	}
	
	
	
	
}
