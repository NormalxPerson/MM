package com.moneymanager.ui.model;

import com.moneymanager.core.Transaction;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TransactionModel {
	private StringProperty id = new SimpleStringProperty();
	private DoubleProperty amount;
	private StringProperty description;
	private ObjectProperty<LocalDate> date;
	private StringProperty type;
	private StringProperty accountId;
	private StringProperty accountName;
	
	//pass a transaction and build model. Also keep original. Maybe create in TransactionService
	public TransactionModel(Transaction transaction, String accountName) {
		this.id.set(transaction.getId());
		this.amount.set(transaction.getAmount());
		this.description.set(transaction.getDescription());
		this.date.set(transaction.getDate());
		this.type.set(transaction.getType());
		this.accountId.set(transaction.getAccountId());
		this.accountName.set(accountName);
	}
	
	public StringProperty getId() {
		return id;
	}
	
	public StringProperty getAccountName() {
		return accountName;
	}
	
	public DoubleProperty getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount.set(amount);
		//update db
	}
	
	public StringProperty getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public ObjectProperty<LocalDate> getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date.set(LocalDate.parse(date));
	}
	
	public StringProperty getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type.set(type);
	}
	
	public StringProperty getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId.set(accountId);
	}
	
}
