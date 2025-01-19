package com.moneymanager.ui.model;

public class TransactionModel {
	private String id;
	private double amount;
	private String description;
	private String date;
	private String type;
	private String accountId;
	private String accountName;
	
	//pass a transaction and build model. Also keep original. Maybe create in TransactionService
	public TransactionModel(String id, double amount, String description, String date, String type, String accountId, String accountName) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.type = type;
		this.accountId = accountId;
		this.accountName = accountName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
