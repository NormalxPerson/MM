package com.moneymanager.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String DB_NAME = "finance.db";
	private static final String APP_DIR = USER_HOME + File.separator + ".moneymanager";
	private static final String CONNECTION_URL = "jdbc:sqlite:" + APP_DIR + File.separator + DB_NAME;
	
	private static DatabaseConnection instance;
	private Connection connection;
	
	private DatabaseConnection() {
		// Initialize when constructed
		try {
			createDatabaseDirectory();
			connection = DriverManager.getConnection(CONNECTION_URL);
			DatabaseInitializer.initializeDatabase(connection);
			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to initialize database connection", e);
		}
		
	}
	

	
	public Connection getConnection() throws SQLException {
		// Check if connection is closed or null
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(CONNECTION_URL);
		}
		return connection;
	}
	
	public static DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
	private void createDatabaseDirectory() {
		File dbDir = new File(APP_DIR);
		if (!dbDir.exists()) {
			boolean created = dbDir.mkdirs();
			if (!created) {
				throw new RuntimeException("Could not create directory: " + APP_DIR);
			}
			System.out.println("Created database directory: " + APP_DIR);
		}
	}
}
