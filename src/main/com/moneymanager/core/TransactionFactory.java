package com.moneymanager.core;

import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionFactory {
	private static final DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("M-d-yy");
	private static final DateTimeFormatter transactionIdFormat = DateTimeFormatter.ofPattern("yyMd");
	private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static Transaction createTransaction(double amount, String description, String date, String type, String accountId, TransactionRepo transRepo) {
		System.out.printf("Transaction Factory: amount=%s, description=%s, date=%s, type=%s, accountId=%s\n", amount, description, date, type, accountId);
		
		LocalDate convertedDate = validateDate(date);
		
		validateType(type);
		validateAmount(amount, type);
		double adjustedAmount;
		if (type.equals("EXPENSE")) {
			adjustedAmount = Math.abs(amount) * -1;
		} else {
			adjustedAmount = Math.abs(amount);
		}
		
		String generatedId = generateTransactionId(convertedDate, transRepo);
		
		return new Transaction(generatedId, adjustedAmount, description, convertedDate, type, accountId);
	}
	
	public static Transaction createTransaction(String transId, double amount, String description, String date, String type, String accountID) {
		System.out.printf("Transaction Factory: transId=%s amount=%s, description=%s, date=%s, type=%s, accountId=%s\n",transId, amount, description, date, type, accountId);
		
		LocalDate convertedDate = validateDate(date);
		return new Transaction(transId, amount, description, convertedDate, type, accountId);
	}
	
	
	private static void validateType(String type) {
		
		if (type == null || (!type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expense"))) {
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
	
	private static LocalDate validateDate(String date) {
		try {
			return LocalDate.parse(date, inputDateFormat);
			
		} catch (Exception e) {
			try {
				return LocalDate.parse(date, transactionDateFormat);
			} catch (Exception e2) {
				System.out.println("Error parsing transaction date: " + date);
			throw new IllegalArgumentException("Invalid Transaction Date! Use format MM-dd-yy");
			}
		}
	}
	
	private static String generateTransactionId(LocalDate date, TransactionRepo transRepo) {
		String dateId = transactionIdFormat.format(date);
		
		return dateId + "-" + transRepo.getTransactionCountByDate(String.valueOf(date));
	
	}
	
	
}
