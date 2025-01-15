package com.moneymanager.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionFactory {
	private static final DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	
	public static Transaction createTransaction(double amount, String description, String date, String type, String accountId) {
	
	validateType(type);
	validateAmount(amount, type);
	if (type.equals("expense")) {
		double adjustedAmount = Math.abs(amount) * -1; }
	else { double adjustedAmount = Math.abs(amount); }
	validateDate(date);
	
	
	
	}
	
	private static void validateType(String type) {
		if (type == null || (!type.equals("income") && !type.equals("expense"))) {
			throw new IllegalArgumentException("Transaction type must be Income/Expense");
		}
	}
	private static void validateAmount(double amount, String type) {
		if (amount == 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}
		if (amount < 0 && type.equals("income")) {
			throw new IllegalArgumentException("Income Transaction must be greater than zero");
		}
	}
	
	private static void validateDate(String date) {
		try {
			LocalDate.parse(date, transactionDateFormat);
		} catch (Exception e) { throw new IllegalArgumentException("Invalid Transaction Date! Use format MM-dd-yyyy"); }
	}
	
	private static String generateTransactionId(String date) {
	
	}
	
	
}
