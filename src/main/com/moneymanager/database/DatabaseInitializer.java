package com.moneymanager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
	private static final String CREATE_TRANSACTION_TABLE = """
			CREATE TABLE IF NOT EXISTS transactions (
				transactionId TEXT PRIMARY KEY,
				transactionDate TEXT NOT NULL,""" /*Stored as YYYY-MM-DD */ + """
				transactionAmount INTEGER NOT NULL,
				transactionDescription TEXT NULL,
				transactionType TEXT NOT NULL,
				accountId INTEGER NOT NULL,
				categoryId Text NULL,
				FOREIGN KEY (accountId) REFERENCES accounts(accountId)
				FOREIGN KEY (categoryId) REFERENCES categories(categoryId) ON DELETE SET NULL
			);""";
	
	private static final String CREATE_ACCOUNTS_TABLE = """
			CREATE TABLE IF NOT EXISTS accounts (
				accountId INTEGER PRIMARY KEY AUTOINCREMENT,
				accountName TEXT NOT NULL,
				bankName TEXT NOT NULL,
				accountBalance integer NOT NULL,
				accountType TEXT NOT NULL,
				CONSTRAINT valid_account_type CHECK(accountType IN ('DEBIT', 'CREDIT'))
			);""";
	
	private static final String CREATE_CSV_STRATEGIES_TABLE = """
			CREATE TABLE IF NOT EXISTS csv_strategies (
				transactionId INTEGER PRIMARY KEY AUTOINCREMENT,
				accountId INTEGER NOT NULL,
				headerPattern TEXT NOT NULL,
				columnMappings TEXT NOT NULL,
				dateFormat TEXT,
				FOREIGN KEY (accountId) REFERENCES accounts(accountId) ON DELETE set NULL
			);""";
	
	private static final String CREATE_BUDGET_TABLE = """
			CREATE TABLE IF NOT EXISTS budgets (
			budgetId Text Primary Key not NULL,
			budgetName Text not NULL,
			budgetYearMonth Text not NULL UNIQUE
				);"""; // YearMonth as YYYY-MM
	
	private static final String CREATE_CATEGORIES_TABLE = """
			CREATE TABLE IF NOT EXISTS categories (
			categoryId Text PRIMARY KEY,
			parentCategoryId Text NULL,
			categoryName Text NOT NULL,
			categoryDescription Text NULL,
			FOREIGN KEY (parentCategoryId) REFERENCES categories(categoryId) ON DELETE SET NULL
			);""";
	
	private static final String CREATE_BUDGET_ALLOCATIONS_TABLE = """
			CREATE TABLE IF NOT EXISTS budget_allocations (
			allocationId TEXT PRIMARY KEY,
			budgetId TEXT NOT NULL,
			categoryId TEXT NOT NULL,
			allocatedAmount INTEGER NOT NULL,
			FOREIGN KEY (budgetId) REFERENCES budgets(budgetId) ON DELETE CASCADE
			FOREIGN KEY (categoryId) REFERENCES categories(categoryId) ON DELETE CASCADE
			);""";
	
	public static void initializeDatabase(Connection dbConnection) throws SQLException {
		try (Statement stmt = dbConnection.createStatement()) {
			
			System.out.println("Starting database initialization...");
			stmt.execute(CREATE_TRANSACTION_TABLE);
			stmt.execute(CREATE_ACCOUNTS_TABLE);
			stmt.execute(CREATE_CSV_STRATEGIES_TABLE);
			stmt.execute(CREATE_BUDGET_TABLE);
			stmt.execute(CREATE_CATEGORIES_TABLE);
			stmt.execute(CREATE_BUDGET_ALLOCATIONS_TABLE);
			
			// Enable foreign keys and set busy timeout
			stmt.execute("PRAGMA foreign_keys = ON");
			stmt.execute("PRAGMA busy_timeout = 30000");
			System.out.println("Database initialization complete.");
		} catch (SQLException e) {
			System.out.println("Database initialization failed." + e.getMessage());
		}
	}
}
