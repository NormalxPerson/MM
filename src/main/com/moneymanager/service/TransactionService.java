package com.moneymanager.service;

import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

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
	
	public TransactionTableView.TransactionModel createAddAndGetNewTransactionModel(double amount, String description, String date, String type, String accountId, String categoryId) {
		// Create transaction using TransactionFactory
		Transaction newTransaction = TransactionFactory.createTransaction(amount, description, date, type, accountId, categoryId, transRepo);
		
		// Insert the transaction into the database
		transRepo.addTransaction(newTransaction);
		return createTransactionModelFromTransaction(newTransaction);
		
	}
	
	@Override
	public ObservableList<TransactionTableView.TransactionModel> getObservableTransactionModelsList() {
		loadObservableList();
		return transactionModels;
	}
	
	@Override
	public TransactionTableView.TransactionModel createTransactionFromUser(double amount, String description, String date, String type, String accountId, String categoryId) {
		Transaction transaction = TransactionFactory.createTransaction(amount, description, date, type, accountId, categoryId, transRepo);
		System.out.println("TransactionService.createTransactionFromUser: " + transaction.toString());
		
		transRepo.addTransaction(transaction);
		TransactionTableView.TransactionModel newTransactionModel = createNewTransactionModel(transaction);
		transactionModels.add(newTransactionModel);
		
		updateAccountBalance(transaction.getAccountId(), transaction.getAmount());
		return newTransactionModel;
		
		
	}
	
	public void updateAccountBalance(String accountId, double amount) {
		accountService.updateBalance(accountId, amount);
		
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
		String accountName = accountService.getAccountNameByAccountId(transaction.getAccountId());
		return new TransactionTableView.TransactionModel.Builder(
				transaction.getId(),
				transaction.getDate(),
				transaction.getAmount(),
				transaction.getDescription(),
				transaction.getType().getDisplayName())
				.accountId(transaction.getAccountId()) // This can safely be null
				.accountName(accountName)
				.categoryId(transaction.getCategoryId()) // This can safely be null
				// Add category name if needed
				.build();
	}
	
	public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() {
		return accountService.getAccountModelObservableList();
	}
	
	public ObservableMap<String, AccountTableView.AccountModel> getAccountModelObservableMap() {
		return accountService.getAccountModelMap();
	}
	
	public TransactionTableView.TransactionModel createTransactionModelFromTransaction(Transaction transaction) {
		String accountName = accountService.getAccountNameByAccountId(transaction.getAccountId());
		return new TransactionTableView.TransactionModel.Builder(
				transaction.getId(),
				transaction.getDate(),
				transaction.getAmount(),
				transaction.getDescription(),
				transaction.getType().getDisplayName())
				.accountId(transaction.getAccountId()) // This can safely be null
				.accountName(accountName)
				.categoryId(transaction.getCategoryId()) // This can safely be null
				// Add category name if needed
				.build();
	}
	
	public TransactionTableView.TransactionModel createAndAddTransaction(Map<String, Object> fieldValues) {
		Transaction transaction = TransactionFactory.createTransaction(fieldValues, transRepo);
		transRepo.addTransaction(transaction);
		TransactionTableView.TransactionModel newTransactionModel = createNewTransactionModel(transaction);
		transactionModels.add(newTransactionModel);
		
		updateAccountBalance(transaction.getAccountId(), transaction.getAmount());
		return newTransactionModel;
		
	}
	
	public void updateTransaction(TransactionTableView.TransactionModel transactionModel) {
		transRepo.updateTransaction(transactionModel);
	}
	
	public int deleteTransaction(TransactionTableView.TransactionModel transactionModel) {
		if (transactionModels.remove(transactionModel)) {
			 return transRepo.deleteTransaction(transactionModel.getTransactionId());
		}
		return 0;
	}
	
	public TransactionRepo getTransactionRepo() {
		return transRepo;
	}
	

	
	
	
	
}
