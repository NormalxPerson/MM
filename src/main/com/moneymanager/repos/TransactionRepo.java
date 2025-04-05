package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import com.moneymanager.ui.view.TransactionTableView;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo {
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsByAccountId(String accountId);
	void addTransactions(List<Transaction> transactions);
	String getLastTransactionIdForDate(String date);
	void addTransaction(Transaction transaction);
	int deleteTransaction(String transactionId);
	void updateTransaction(TransactionTableView.TransactionModel transactionModel);
	List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate);
}
