package com.moneymanager.core;

import java.util.Date;

public class Transaction {
	private String id;
	private double amount;
	private String description;
	private String date;
	private String type;
	private String accountId;

	public Transaction(String id, double amount, String description, String date, String type, String accountId) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = type;
		this.accountId = accountId;
	}
	
	@Override
	public String toString() {
		return String.format("Transaction{amount=%.2f, description=%s, date=%s, type=%s, accountId=%s}", amount, description, date, type, accountId);
	}

}
