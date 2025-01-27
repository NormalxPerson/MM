package com.moneymanager.ui.model;

import com.moneymanager.core.Transaction;
import javafx.beans.property.*;

import java.time.LocalDate;

public class TransactionModelOLD {
	private StringProperty id = new SimpleStringProperty();
	private DoubleProperty amount = new SimpleDoubleProperty();
	private StringProperty description = new SimpleStringProperty();
	private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
	private StringProperty type = new SimpleStringProperty();
	private StringProperty accountId = new SimpleStringProperty();
	private StringProperty accountName = new SimpleStringProperty();
	
	//pass a transaction and build model. Also keep original. Maybe create in TransactionService
	public TransactionModelOLD(Transaction transaction, String accountName) {
		this.id.set(transaction.getId());
		this.amount.set(transaction.getAmount());
		this.description.set(transaction.getDescription());
		this.date.set(transaction.getDate());
		this.type.set(transaction.getType());
		this.accountId.set(transaction.getAccountId());
		this.accountName.set(accountName);
	}
	
	public String getId() {
		return id.get();
	}
	
	public String getAccountName() {
		return accountName.getName();
	}
	
	public Double getAmount() {
		return amount.get();
	}
	
	public void setAmount(double amount) {
		this.amount.set(amount);
		//update db
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public LocalDate getDate() {
		return date.get();
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
	
	public String getAccountId() {
		return accountId.get();
	}
	
	public void setAccountId(String accountId) {
		this.accountId.set(accountId);
	}
	
}
