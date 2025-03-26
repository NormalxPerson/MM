package com.moneymanager.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
	
	public enum  TransactionType {
		INCOME("Income"),
		EXPENSE("Expense");
		
		private String displayName;
		
		TransactionType(String displayName) {
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		@Override
		public String toString() {
			return displayName;
		}
		
	}
	
	private String id;
	private double amount;
	private String description;
	private LocalDate date;
	private String formattedDate;
	private TransactionType type;
	private String accountId;

	public Transaction(String id, double amount, String description, LocalDate date, String type, String accountId) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = TransactionType.valueOf(type.toUpperCase());
		this.accountId = accountId;
	}
	
	public Transaction(String id, double amount, String description, LocalDate date, String type) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = TransactionType.valueOf(type.toUpperCase());
		this.accountId = null;
	}
	
	public String getId() { return id; }
	public double getAmount() { return amount; }
	public String getDescription() { return description; }
	public LocalDate getDate() { return date; }
	public TransactionType getType() { return type; }
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
