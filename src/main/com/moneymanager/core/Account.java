package com.moneymanager.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
	
	public enum AccountType {
		DEBIT("Debit"),
		CREDIT("Credit");
		
		private final String displayName;
		
		AccountType(String displayName) {
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
	
	private String accountName;
	private String bankName;
	private AccountType accountType;
	private String accountId;
	private double balance;
	private List<Transaction> transactionList;

	public Account(String accountName, String bankName, String accountType, double balance) {
		this.accountId = UUID.randomUUID().toString();
		this.accountName = accountName;
		this.bankName = bankName;
		this.accountType = AccountType.valueOf(accountType.toUpperCase());
		this.balance = balance;
		this.transactionList = new ArrayList<>();
	}

	public Account(String accountId, String accountName, String bankName, String accountType, double balance) {
		this.accountName = accountName;
		this.bankName = bankName;
		this.accountType = AccountType.valueOf(accountType.toUpperCase());
		this.accountId = accountId;
		this.balance = balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	//public void setAccountId(String accountId) { this.accountId = accountId; }

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

	public AccountType getAccountType() {
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
