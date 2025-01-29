package com.moneymanager.repos;

import com.moneymanager.core.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo {
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsByAccountId(String accountId);
	int getTransactionCountByDate(String date);
	void addTransactions(List<Transaction> transactions);
	String getLastTransactionIdForDate(String date);
}
