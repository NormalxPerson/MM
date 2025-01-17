package com.moneymanager.service;

import com.moneymanager.repos.TransactionRepo;
// TODO: Create a TransactionServiceInterface
public class TransactionService {
	private final TransactionRepo transRepo;
	
	public TransactionService(TransactionRepo transRepo) {
		this.transRepo = transRepo;
	}
	
	
	
}
