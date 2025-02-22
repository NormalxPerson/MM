package com.moneymanager.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
	private final String id;
	private final double amount;
	private final String description;
	private final LocalDate date;
	private String formattedDate;
	private final String type;
	private final String accountId;

	public Transaction(String id, double amount, String description, LocalDate date, String type, String accountId) {
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
	public LocalDate getDate() { return date; }
	public String getType() { return type; }
	public String getAccountId() { return accountId; }
	
	public String getFormattedDate() {
		if (formattedDate == null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yy");
			this.formattedDate = date.format(formatter);
		}
		return formattedDate;
	}
	
	@Override
	public String toString() {
		return String.format("Transaction{amount=%.2f, description=%s, date=%s, type=%s, accountId=%s}", amount, description, date, type, accountId);
	}

}
