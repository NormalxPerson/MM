package com.moneymanager.core;

import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionFactory {
	private static final DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	private static final DateTimeFormatter transactionIdFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	
	public static Transaction createTransaction(double amount, String description, String date, String type, String accountId, TransactionRepo transRepo) {
	
	validateType(type);
	validateAmount(amount, type);
	double adjustedAmount;
	if (type.equals("expense")) {
		adjustedAmount = Math.abs(amount) * -1; }
	else { adjustedAmount = Math.abs(amount); }
	
	validateDate(date);
	String transactionId = generateTransactionId(date, transRepo);
		
		return new Transaction(transactionId, adjustedAmount, description, date, type, accountId);
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
	
	private static String generateTransactionId(String date, TransactionRepo transRepo) {
		LocalDate transactionDate = LocalDate.parse(date, transactionDateFormat);
		String dateId = transactionIdFormat.format(transactionDate);
		
		return dateId + "-" + transRepo.getTransactionsCountByDate(date);
	
	}
	
	
}
