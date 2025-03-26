package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import com.moneymanager.ui.view.TransactionTableView;

import java.util.List;

public interface TransactionRepo {
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsByAccountId(String accountId);
	int getTransactionCountByDate(String date);
	void addTransactions(List<Transaction> transactions);
	String getLastTransactionIdForDate(String date);
	void addTransaction(Transaction transaction);
	void updateTransaction(Transaction transaction);
	int deleteTransaction(String transactionId);
	void updateTransaction(TransactionTableView.TransactionModel transactionModel);
}
