package com.moneymanager.service;

import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.repos.TransactionRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Create a TransactionServiceInterface
public class TransactionService {
	private final TransactionRepo transRepo;
	
	public TransactionService(TransactionRepo transRepo) {
		this.transRepo = transRepo;
	}
	
	public void addTransaction(double amount, String description, String date, String type, String accountId) {
		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(TransactionFactory.createTransaction(amount, description, date, type, accountId, transRepo));
		transRepo.addTransactions(transactionList);
	}
	
	
	
}
