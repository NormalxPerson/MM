package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTransactionRepo implements TransactionRepo {
	private final DatabaseConnection dbConnection;
	
	public SQLiteTransactionRepo() {
		this.dbConnection = DatabaseConnection.getInstance();
	}
	
	@Override
	public void addTransactions(List<Transaction> transactions) {
		String sql = "INSERT INTO transactions (transactionId, transactionAmount, transactionDescription, transactionDate, transactionType, account_id) VALUES (?, ?, ?, ?, ?, ?)";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			
			for (Transaction transaction : transactions) {
				statement.setString(1, transaction.getId());
				statement.setDouble(2, transaction.getAmount());
				statement.setString(3, transaction.getDescription());
				statement.setString(4, transaction.getDate()); // Assuming date is stored as TEXT in your database
				statement.setString(5, transaction.getType());
				statement.setString(6, transaction.getAccountId());
				
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println("Error adding transactions from list: " + e.getMessage());
		}
	}
	
	@Override
	public int getTransactionCountByDate(String date) {
		String sql = "SELECT COUNT(*) as count FROM transactions WHERE date = ?";
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
					 Transaction transaction = TransactionFactory.createTransaction(
							 rs.getString("transactionID"),
							 rs.getDouble("transactionAmount"),
							 rs.getString("transactionDescription"),
							 rs.getString("transactionDate"),
							 rs.getString("transactionType"),
							 rs.getString("accountId"));
					 
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
				Transaction transaction = TransactionFactory.createTransaction(
						rs.getString("transactionID"),
						rs.getDouble("transactionAmount"),
						rs.getString("transactionDescription"),
						rs.getString("transactionDate"),
						rs.getString("transactionType"),
						rs.getString("accountId"));
				
				transactions.add(transaction);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return transactions;
	}
}
