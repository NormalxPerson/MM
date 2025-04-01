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
	
	private void saveTransaction(Transaction transaction) {
		transRepo.addTransactions(Collections.singletonList(transaction));
		transactionModels.add(createNewTransactionModel(transaction));
	}
	
	public void updateAccountBalance(String accountId, double amount) {
		accountService.updateBalance(accountId, amount);
		
	}
	
	public void addNewTransactionModelToTable(TransactionTableView.TransactionModel transaction) {
		transactionModels.add(transaction);
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
		TransactionTableView.TransactionModel newModel;
		if (transaction.getAccountId() != null && !transaction.getAccountId().isEmpty()) {
			String accountName = accountService.getAccountNameByAccountId(transaction.getAccountId());
			
			newModel = new TransactionTableView.TransactionModel(transaction.getId(), transaction.getDate(),transaction.getAmount(), transaction.getDescription(), transaction.getType().getDisplayName(), transaction.getAccountId(), accountName);
		} else {
			newModel = new TransactionTableView.TransactionModel(transaction.getId(), transaction.getDate(), transaction.getAmount(), transaction.getDescription(), transaction.getType().toString());
		}
		if (transaction.getCategoryId() != null && !transaction.getCategoryId().isEmpty()) {
			newModel.setTransactionCategoryId(transaction.getCategoryId());
		}
		return newModel;
	}
	
	public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() {
		return accountService.getAccountModelObservableList();
	}
	
	public ObservableMap<String, AccountTableView.AccountModel> getAccountModelObservableMap() {
		return accountService.getAccountModelMap();
	}
	
	public TransactionTableView.TransactionModel createTransactionModelFromTransaction(Transaction transaction) {
		String accountName = accountService.getAccountNameByAccountId(transaction.getAccountId());
		TransactionTableView.TransactionModel newModel = new TransactionTableView.TransactionModel(
				transaction.getId(),
				transaction.getDate(),
				transaction.getAmount(),
				transaction.getDescription(),
				transaction.getType().toString(),
				transaction.getAccountId(),
				accountName
		);
		if (transaction.getCategoryId() != null && !transaction.getCategoryId().isEmpty()) {
			newModel.setTransactionCategoryId(transaction.getCategoryId());
		}
		return newModel;
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
