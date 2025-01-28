package com.moneymanager.exceptions;

public class ValidationException extends RuntimeException {
	private final String location;
	
	public ValidationException(String message, String location) {
		super(String.format("%s (Location: %s)", message, location));
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	
	public static class InvalidTransactionDateException extends ValidationException {
		public InvalidTransactionDateException(String date, String location) {
			super(String.format("Invalid transaction date: %s", date), location);
		}
	}
	
	public static class InvalidTransactionTypeException extends ValidationException {
		public InvalidTransactionTypeException(String type, String location) {
			super(String.format("Invalid transaction type: '%s'. Must be 'Income' or 'Expense'.", type), location);
		}
	}
	
	public static class InvalidTransactionAmountException extends ValidationException {
		public InvalidTransactionAmountException(double amount, String location) {
			super(String.format("Invalid transaction amount: %.2f. Must be greater than zero.", amount), location);
		}
	}
}


