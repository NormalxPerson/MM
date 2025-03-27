package com.moneymanager.core;
import com.moneymanager.exceptions.ValidationException;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionTableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TransactionFactory {
	private static final DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("M-d-yy");
	private static final DateTimeFormatter transactionIdFormat = DateTimeFormatter.ofPattern("M-d-yyyy");
	private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static Transaction createTransaction(double amount, String description, String date, String type, String accountId, TransactionRepo transRepo) {
		System.out.printf("Transaction Factory: before Validation: amount=%s, description=%s, strdate: %s, date=%s, type=%s, accountId=%s\n", amount, description,date.getClass(), date, type, accountId);
		
		LocalDate convertedDate = validateDate(date);
		Transaction.TransactionType formattedType = validateType(type);
		double adjustedAmount = validateAndAdjustAmount(amount, formattedType);
		String generatedId = generateTransactionId(convertedDate, accountId, transRepo);
	
		return new Transaction(generatedId, adjustedAmount, description, convertedDate, formattedType.getDisplayName(), accountId);
	}
	
	public static Transaction createTransaction(ResultSet rs) throws SQLException {
		System.out.printf("Transaction Factory: ResultSet: %s\n",rs.toString());
		
		String id = rs.getString("transactionId");
		double amount = rs.getInt("transactionAmount") / 100.0;
		String description = rs.getString("transactionDescription");
		LocalDate date = validateDate(rs.getString("transactionDate"));
		String type = rs.getString("transactionType");
		String accountId = rs.getString("accountId");
		
		return new Transaction(id, amount, description, date, type, accountId);
		
	}
	
	public static Transaction createTransaction(Map<String, Object> fieldValues, TransactionRepo transRepo) {
			
			String description = (String) fieldValues.get("transactionDescription");
			LocalDate date = validateDate(String.valueOf(fieldValues.get("transactionDate")));
			TransactionTableView.TransactionModel.TransactionType transactionModelType = (TransactionTableView.TransactionModel.TransactionType) fieldValues.get("transactionType");
			Transaction.TransactionType transactionType = validateType(transactionModelType.getDisplayName());
			
			
			double amount = validateAndAdjustAmount(Double.parseDouble((String) fieldValues.get("transactionAmount")), transactionType);
			AccountTableView.AccountModel accountModel = (AccountTableView.AccountModel) fieldValues.get("transactionAccount");
			
			String accountId = accountModel.getAccountId();
			String accountName = accountModel.getAccountName();
			
			String generatedId = generateTransactionId(date, accountId, transRepo);
		System.out.printf("Values to create Transaction in TransactionFactory.createTransaction(Map<String, Object> fieldValues, TransactionRepo transRepo)\n\tgeneratedId=%s\n\tamount=%s\n\tdescription=%s", generatedId, amount, description);
			return new Transaction(generatedId, amount, description, date, transactionType.getDisplayName(), accountId);
			
		
	}
	
	
	private static Transaction.TransactionType validateType(String type) {
		
		return switch (type.toUpperCase()) {
			case "INCOME" -> Transaction.TransactionType.INCOME;
			case "EXPENSE" -> Transaction.TransactionType.EXPENSE;
			default -> null;
		};
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
	
	private static double validateAndAdjustAmount(double amount, Transaction.TransactionType type) {
		if (type.equals(Transaction.TransactionType.INCOME)) {
			if (amount < 0) {
				throw new ValidationException.InvalidTransactionAmountException(amount, "TransactionFactory.validateAndAdjustAmount: Income cannot have a negative amount");
			}
			return amount; // Positive amount for INCOME
		} else if (type.equals(Transaction.TransactionType.EXPENSE)) {
			if (amount > 0) {
				return -Math.abs(amount); // Convert positive to negative for EXPENSE
			}
			return amount; // Already negative for EXPENSE
		} else {
			throw new ValidationException.InvalidTransactionTypeException(type.getDisplayName(), "TransactionFactory.validateAndAdjustAmount");
		}
	}
	
	private static String generateTransactionId(LocalDate date, String accountId, TransactionRepo transRepo) {
		DateTimeFormatter transactionFormatter = DateTimeFormatter.ofPattern("M-d-yy");
		DateTimeFormatter idFormatter = DateTimeFormatter.ofPattern("Mdyy");
		String formattedIdDate = date.format(idFormatter);
		
		// Get the last transaction ID for the date
		String lastTransactionId = transRepo.getLastTransactionIdForDate(date.format(transactionFormatter));
		
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
		return String.format("ACC%s-%s-%d", accountId, formattedIdDate, nextCount);
	}

	
	
}
