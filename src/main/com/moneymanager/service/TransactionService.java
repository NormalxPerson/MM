package com.moneymanager.service;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

// TODO: Create a TransactionServiceInterface
public class TransactionService {
	private final TransactionRepo transRepo;
	private final AccountService accountService;
	private final ObservableList<TransactionTableView.TransactionModel> transactionModels;
	private ObservableMap<String, BudgetCategory> budgetCategories;
	
	public TransactionService(TransactionRepo transRepo, AccountService accountService) {
		this.transRepo = transRepo;
		this.accountService = accountService;
		this.transactionModels = FXCollections.observableArrayList();
	}
	
	public void setBudgetCategoryMap(ObservableMap<String, BudgetCategory> catList) {
		this.budgetCategories = catList;
	}
	
	public ObservableList<TransactionTableView.TransactionModel> getObservableTransactionModelsList() {
		loadObservableList();
		return transactionModels;
	}
	
/*	public TransactionTableView.TransactionModel createTransactionFromUser(double amount, String description, String date, String type, String accountId, String categoryId) {
		Transaction transaction = TransactionFactory.createTransaction(amount, description, date, type, accountId, categoryId, transRepo);
		System.out.println("TransactionService.createTransactionFromUser: " + transaction.toString());
		
		transRepo.addTransaction(transaction);
		TransactionTableView.TransactionModel newTransactionModel = createNewModelFromTransaction(transaction);
		transactionModels.add(newTransactionModel);
		
		updateAccountBalance(transaction.getAccountId(), transaction.getAmount());
		return newTransactionModel;
		
		
	}*/
	
	public void updateAccountBalance(String accountId, double amount) {
		accountService.updateBalance(accountId, amount);
		
	}
	
	private void loadObservableList() {
		transactionModels.clear();
		List<Transaction> transactions = transRepo.getAllTransactions();
		for (Transaction transaction : transactions) {
			transactionModels.add(createNewModelFromTransaction(transaction));
		}
	}
	
	private TransactionTableView.TransactionModel createNewModelFromTransaction(Transaction transaction) {
		String accountName = accountService.getAccountNameByAccountId(transaction.getAccountId());
		String catName = null;
		
		if (budgetCategories.containsKey(transaction.getCategoryId())) {
			catName = budgetCategories.get(transaction.getCategoryId()).getCategoryName();
		}
		
		return new TransactionTableView.TransactionModel.Builder(
				transaction.getId(),
				transaction.getDate(),
				transaction.getAmount(),
				transaction.getType().getDisplayName())
				.transactionDescription(transaction.getDescription())
				.accountId(transaction.getAccountId()) // This can safely be null
				.accountName(accountName)
				.categoryId(transaction.getCategoryId()) // This can safely be null
				.categoryName(catName)// Add category name if needed
				.build();
	}
	
	public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() {
		return accountService.getAccountModelObservableList();
	}
	
	public ObservableMap<String, AccountTableView.AccountModel> getAccountModelObservableMap() {
		return accountService.getAccountModelMap();
	}
	
/*	public TransactionTableView.TransactionModel createTransactionModelFromTransaction(Transaction transaction) {
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
	}*/
	
	public TransactionTableView.TransactionModel createFromFormValues(Map<String, Object> fieldValues) {
		Transaction transaction = TransactionFactory.createTransaction(fieldValues, transRepo);
		transRepo.addTransaction(transaction);
		TransactionTableView.TransactionModel newTransactionModel = createNewModelFromTransaction(transaction);
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
	
	public ObservableMap<String, BudgetCategory> getBudgetCategoryObservableMap() {
		return budgetCategories;
	}
	
	public TransactionRepo getTransactionRepo() {
		return transRepo;
	}
	

	
	
	
	
}
