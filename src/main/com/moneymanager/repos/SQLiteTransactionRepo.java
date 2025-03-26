package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTransactionRepo implements TransactionRepo {
	private final DatabaseConnection dbConnection;
	
	public SQLiteTransactionRepo() {
		this.dbConnection = DatabaseConnection.getInstance();
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
				statement.setString(4, transaction.getFormattedDate()); // Assuming date is stored as TEXT in your database
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
	public int getTransactionCountByDate(String date) {
		String sql = "SELECT COUNT(*) as count FROM transactions WHERE transactionDate = ?";
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			 stmt.setString(1, date);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		} catch (SQLException e) { throw new RuntimeException(e);}
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
			stmt.setString(1, date);
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
	public void updateTransaction(Transaction transaction) {
		String sql = "UPDATE transactions SET transactionAmount = ?, transactionDescription = ?, transactionDate = ?, transactionType = ?, accountId = ? WHERE transactionId = ?";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setInt(1, (int) Math.round(transaction.getAmount() * 100)); // Convert to cents
			stmt.setString(2, transaction.getDescription());
			stmt.setString(3, transaction.getDate().toString()); // Store LocalDate as TEXT
			stmt.setString(4, transaction.getType().name());
			stmt.setString(5, transaction.getAccountId());
			stmt.setString(6, transaction.getId());
			
			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated == 0) {
				System.err.println("No transaction found with ID: " + transaction.getId());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to update transaction with ID: " + transaction.getId(), e);
		}
	}
}
