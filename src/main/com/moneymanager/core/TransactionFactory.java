package com.moneymanager.core;
import com.moneymanager.exceptions.ValidationException;
import com.moneymanager.repos.TransactionRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionFactory {
	private static final DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("M-d-yy");
	private static final DateTimeFormatter transactionIdFormat = DateTimeFormatter.ofPattern("M-d-yyyy");
	private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static Transaction createTransaction(double amount, String description, String date, String type, String accountId, TransactionRepo transRepo) {
		System.out.printf("Transaction Factory: before Validation: amount=%s, description=%s, strdate: %s, date=%s, type=%s, accountId=%s\n", amount, description,date.getClass(), date, type, accountId);
		
		LocalDate convertedDate = validateDate(date);
		String formattedType = validateType(type);
		double adjustedAmount = validateAndAdjustAmount(amount, formattedType);
		String generatedId = generateTransactionId(convertedDate, accountId, transRepo);
	
		return new Transaction(generatedId, adjustedAmount, description, convertedDate, formattedType, accountId);
	}
	
	public static Transaction createTransaction(ResultSet rs) throws SQLException {
		System.out.printf("Transaction Factory: ResultSet: %s\n",rs.toString());
		
		String id = rs.getString("transactionId");
		double amount = rs.getInt("transactionAmount") / 100.0;
		String description = rs.getString("transactionDescription");
		LocalDate date = rs.getDate("transactionDate").toLocalDate();
		String type = rs.getString("transactionType");
		String accountId = rs.getString("accountId");
		
		return new Transaction(id, amount, description, date, type, accountId);
		
	}
	
	
	private static String validateType(String type) {
		
		if (type == null || (!type.equalsIgnoreCase("INCOME") && !type.equalsIgnoreCase("EXPENSE"))) {
			throw new ValidationException.InvalidTransactionTypeException(type, "TransactionFactory.validateType");
		}
		return type.toUpperCase();
	}
	
	private static LocalDate validateDate(String date) {
		DateTimeFormatter[] formatters = {inputDateFormat, transactionDateFormat, transactionIdFormat};
		
		for (DateTimeFormatter formatter : formatters) {
			try {
				return LocalDate.parse(date, formatter);
			} catch (Exception ignored) {
				// Continue to the next formatter
			}
		}
		throw new ValidationException.InvalidTransactionDateException(date, "TransactionFactory.validateDate");
	}
	
	private static double validateAndAdjustAmount(double amount, String type) {
		if (type.equals("INCOME")) {
			if (amount < 0) {
				throw new ValidationException.InvalidTransactionAmountException(amount, "TransactionFactory.validateAndAdjustAmount: Income cannot have a negative amount");
			}
			return amount; // Positive amount for INCOME
		} else if (type.equals("EXPENSE")) {
			if (amount > 0) {
				return -Math.abs(amount); // Convert positive to negative for EXPENSE
			}
			return amount; // Already negative for EXPENSE
		} else {
			throw new ValidationException.InvalidTransactionTypeException(type, "TransactionFactory.validateAndAdjustAmount");
		}
	}
	
	private static String generateTransactionId(LocalDate date, String accountId, TransactionRepo transRepo) {
		// Format the date as "m-d-yyyy"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yyyy");
		String formattedDate = date.format(formatter);
		
		// Get the last transaction ID for the date
		String lastTransactionId = transRepo.getLastTransactionIdForDate(formattedDate);
		
		int nextCount = 1; // Default to 1 if no previous transactions exist
		if (lastTransactionId != null) {
			// Extract the last count from the transaction ID
			String[] parts = lastTransactionId.split("-");
			if (parts.length > 2) {
				try {
					nextCount = Integer.parseInt(parts[2]) + 1;
				} catch (NumberFormatException e) {
					throw new RuntimeException("Failed to parse transaction count from ID: " + lastTransactionId, e);
				}
			}
		}
		
		// Generate the new transaction ID
		return String.format("ACC%s-%s-%d", accountId, formattedDate, nextCount);
	}

	
	
}
