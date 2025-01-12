package com.moneymanager.repos;

import com.moneymanager.core.Transaction;
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
	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions";
		
		try (Connection connection = dbConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery()) {
			
			while (rs.next()) {
				Transaction transaction = new Transaction(
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
