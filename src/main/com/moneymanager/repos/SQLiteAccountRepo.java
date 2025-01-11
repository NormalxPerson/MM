package com.moneymanager.repos;

import com.moneymanager.core.Account;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAccountRepo implements AccountRepo {
    private final DatabaseConnection dbConnection;
    
    public SQLiteAccountRepo() {
        
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public List<Account> getAllAccounts() {
        ArrayList<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Account account = new Account(
                        rs.getString("accountId"),
                        rs.getString("accountName"),
                        rs.getString("bankName"),
                        rs.getString("accountType"),
                        rs.getDouble("accountBalance")
                );
                accountList.add(account);
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return accountList;
    }
    
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO accounts (accountName, bankName, accountBalance, accountType) VALUES (?, ?, ?, ?)";
        
        int maxRetries = 3;
        int attempt = 0;
        while (attempt < maxRetries) {
            try (Connection connection = dbConnection.getConnection()) {
                if (connection.isValid(5)) {
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, account.getAccountName());
                        stmt.setString(2, account.getBankName());
                        stmt.setDouble(3, account.getBalance());
                        stmt.setString(4, account.getAccountType());
                        
                        stmt.executeUpdate();
                        System.out.println("Successfully added account: " + account.getAccountName());
                        return;
                    }
                }
            } catch (SQLException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    String errorMessage = String.format(
                            "Failed to add account '%s' Type: '%s' after %d attempts. Error: %s",
                            account.getAccountName(), account.getAccountType(), maxRetries, e.getMessage());
                    System.err.println(errorMessage);
                    throw new RuntimeException(errorMessage, e);
                }
                // If we have more retries, wait a bit before trying again
                try {
                    Thread.sleep(1000);  // Wait 1 second before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
                System.out.println("Retrying database operation... Attempt " + (attempt + 1));
            }
            
        }
    }
}