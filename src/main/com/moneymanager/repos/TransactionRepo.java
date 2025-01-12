package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import java.util.List;

public interface TransactionRepo {
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsByAccountId(String accountId);
	void addTransactions(List<Transaction> transactions);
}
