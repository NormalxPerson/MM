package com.moneymanager.core;

import java.util.Date;

public class Transaction {
	private final String id;
	private final double amount;
	private final String description;
	private final String date;
	private final String type;
	private final String accountId;

	Transaction(String id, double amount, String description, String date, String type, String accountId) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = type;
		this.accountId = accountId;
	}
	
	public String getId() { return id; }
	public double getAmount() { return amount; }
	public String getDescription() { return description; }
	public String getDate() { return date; }
	public String getType() { return type; }
	public String getAccountId() { return accountId; }
	
	@Override
	public String toString() {
		return String.format("Transaction{amount=%.2f, description=%s, date=%s, type=%s, accountId=%s}", amount, description, date, type, accountId);
	}

}
