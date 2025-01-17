package com.moneymanager.core;

import java.util.ArrayList;
import java.util.List;

public class Account {
	private String accountName;
	private String bankName;
	private String accountType;
	private String accountId;
	private double balance;
	private List<Transaction> transactionList;

	public Account(String accountName, String bankName, String accountType) {
		this.accountName = accountName;
		this.bankName = bankName;
		this.accountType = accountType;
		this.balance = 0;
		this.transactionList = new ArrayList<>();
	}

	public Account(String accountId, String accountName, String bankName, String accountType, double balance) {
		this.accountName = accountName;
		this.bankName = bankName;
		this.accountType = accountType;
		this.accountId = accountId;
		this.balance = balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public double getBalance() {
		return balance;
	}

	public String getBankName() {
		return bankName;
	}

	public String getAccountType() {
		return accountType;
	}
	
	@Override
	public String toString() {
		return "Account{" +
				"accountName='" + accountName + '\'' +
				", bankName='" + bankName + '\'' +
				", accountType='" + accountType + '\'' +
				", accountId='" + accountId + '\'' +
				", balance=" + balance +
				", transactionList=" + transactionList +
				'}';
	}
}
