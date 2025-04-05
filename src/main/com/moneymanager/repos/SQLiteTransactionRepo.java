package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.ui.view.TransactionTableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTransactionRepo implements TransactionRepo {
	private final DatabaseConnection dbConnection;
	// Date format converters
	private static final DateTimeFormatter APP_FORMAT = DateTimeFormatter.ofPattern("M-d-yy");
	private static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	// Convert from application format to database format
	private String appDateToDbDate(String appDate) {
		return LocalDate.parse(appDate, APP_FORMAT).format(DB_FORMAT);
	}
	
	// Convert from database format to application format
	private String dbDateToAppDate(String dbDate) {
		return LocalDate.parse(dbDate, DB_FORMAT).format(APP_FORMAT);
	}
	
	public SQLiteTransactionRepo() {
		this.dbConnection = DatabaseConnection.getInstance();
	}
	
	@Override
	public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE transactionDate BETWEEN ? AND ?";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			// Convert dates to database format
			stmt.setString(1, startDate.toString());
			stmt.setString(2, endDate.toString());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					transactions.add(TransactionFactory.createTransaction(rs));
				}
			}
		}catch (SQLException e) { System.out.println(e.getMessage()); }
		return transactions;
	}
	
	@Override
	public void addTransactions(List<Transaction> transactions) {
		String sql = "INSERT INTO transactions (transactionId, transactionAmount, transactionDescription, transactionDate, transactionType, accountId) VALUES (?, ?, ?, ?, ?, ?)";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			
			for (Transaction transaction : transactions) {
				statement.setString(1, transaction.getId());
				statement.setInt(2, (int) Math.round(transaction.getAmount() * 100));
				statement.setString(3, transaction.getDescription());
				statement.setString(4, appDateToDbDate(transaction.getFormattedDate())); // Assuming date is stored as TEXT in your database
				statement.setString(5, transaction.getType().name());
				statement.setString(6, transaction.getAccountId());
				
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println("Error adding transactions from list: " + e.getMessage());
		}
	}
	
	@Override
	public void addTransaction(Transaction transaction) {
		addTransactions(List.of(transaction));
	}
	
	@Override
	public List<Transaction> getTransactionsByAccountId(String accountId) {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE account_id = ?";
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			 stmt.setString(1, accountId);
			 try (ResultSet rs = stmt.executeQuery()) {
				 while (rs.next()) {
					 Transaction transaction = TransactionFactory.createTransaction(rs);
					 
					 transactions.add(transaction);
				 }
			 }
	
		} catch (SQLException e) {
			System.out.println("Error getting transactions by account id: " + accountId);
		}
		return transactions;
	}
	
	@Override
	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {
			
			while (rs.next()) {
				Transaction transaction = TransactionFactory.createTransaction(rs);
				
				transactions.add(transaction);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return transactions;
	}
	
	@Override
	public String getLastTransactionIdForDate(String date) {
		String sql = "SELECT transactionId FROM transactions WHERE transactionDate = ? ORDER BY transactionId DESC LIMIT 1";
		try (Connection connection = dbConnection.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, appDateToDbDate(date));
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("transactionId");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("TransactionRepo.getLastTransactionIdForDateFailed to fetch last transaction ID for date: " + date, e);}
		return null;
	}
	

	
	@Override
	public void updateTransaction(TransactionTableView.TransactionModel transactionModel) {
		String sql = "UPDATE transactions set transactionDate = ?, transactionAmount = ?, transactionDescription = ?, transactionType = ?, accountId = ? WHERE transactionId = ?";
		int amountInCents = (int) Math.round(transactionModel.getTransactionAmount() * 100);
		
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
			stmt.setString(1, appDateToDbDate(transactionModel.getTransactionDate().toString()));
			stmt.setInt(2, amountInCents);
			stmt.setString(3, transactionModel.getTransactionDescription());
			stmt.setString(4, transactionModel.getTransactionType().name());
			stmt.setInt(5, Integer.parseInt(transactionModel.getTransactionAccountId()));
			stmt.setString(6, transactionModel.getTransactionId());
			
			int rowsUpdated = stmt.executeUpdate();
			
			if (rowsUpdated == 0) {
				System.err.println("No transaction found with ID: " + transactionModel.getTransactionId());
			}
		} catch (SQLException e) { throw new RuntimeException(e);}
	}
	@Override
	public int deleteTransaction(String transactionId) {
		String sql = "DELETE FROM transactions WHERE transactionId = ?";
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
			stmt.setString(1, transactionId);
			
			return stmt.executeUpdate();
		}catch (SQLException e) {
			System.err.println("Error deleting transaction with ID: " + transactionId);
		}
		return 0;
	}
}
