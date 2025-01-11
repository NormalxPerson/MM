package com.moneymanager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
	private static final String CREATE_TRANSACTION_TABLE = """
			CREATE TABLE IF NOT EXISTS transactions(
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				date TEXT NOT NULL,
				amount integer NOT NULL,
				description TEXT NOT NULL,
				type TEXT CHECK(type IN ('INCOME', 'EXPENSE')) NOT NULL
			);""";
	
	private static final String CREATE_ACCOUNTS_TABLE = """
			CREATE TABLE IF NOT EXISTS accounts (
				accountId INTEGER PRIMARY KEY AUTOINCREMENT,
				accountName TEXT NOT NULL,
				bankName TEXT NOT NULL,
				accountBalance integer NOT NULL,
				accountType TEXT NOT NULL,
				CONSTRAINT valid_account_type CHECK(accountType IN ('DEBT', 'CREDIT'))
			);""";
	
	private static final String CREATE_CSV_STRATEGIES_TABLE = """
			CREATE TABLE IF NOT EXISTS csv_strategies (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				account_id INTEGER NOT NULL,
				header_pattern TEXT NOT NULL,
				column_mappings TEXT NOT NULL,
				date_format TEXT,
				FOREIGN KEY (account_id) REFERENCES accounts(id)
			);""";
	
	public static void initializeDatabase(Connection dbConnection) throws SQLException {
		try (Statement stmt = dbConnection.createStatement()) {
			
			System.out.println("Starting database initialization...");
			stmt.execute(CREATE_TRANSACTION_TABLE);
			stmt.execute(CREATE_ACCOUNTS_TABLE);
			stmt.execute(CREATE_CSV_STRATEGIES_TABLE);
			
			// Enable foreign keys and set busy timeout
			stmt.execute("PRAGMA foreign_keys = ON");
			stmt.execute("PRAGMA busy_timeout = 30000");
			System.out.println("Database initialization complete.");
		} catch (SQLException e) {
			System.out.println("Database initialization failed." + e.getMessage());
		}
	}
}
