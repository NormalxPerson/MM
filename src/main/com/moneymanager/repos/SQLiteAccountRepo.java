package com.moneymanager.repos;

import com.moneymanager.core.Account;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.ui.view.AccountTableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteAccountRepo implements AccountRepo {
    private final DatabaseConnection dbConnection;
    
    public SQLiteAccountRepo() {
        
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public void updateAccountBalance(Account account) {
        String sql = "UPDATE accounts SET accountBalance = ? WHERE accountId = ?";
        
        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
            int balanceInCents = (int) Math.round(account.getBalance() * 100);
            stmt.setInt(1, balanceInCents);
            stmt.setInt(2, Integer.parseInt(account.getAccountId()));
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("No account found with ID: " + account.getAccountId());
            }
        } catch (SQLException e) { throw new RuntimeException("Failed to update account balance", e); }
    }
    
    @Override
    public Account getAccountById(String accountId) {
        String sql = "SELECT * FROM accounts WHERE accountId = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(accountId)); // Assuming accountId is an integer in the database
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balanceInDollars = rs.getInt("accountBalance") / 100.0;
                return new Account(
                        String.valueOf(rs.getInt("accountId")),
                        rs.getString("accountName"),
                        rs.getString("bankName"),
                        rs.getString("accountType"),
                        balanceInDollars
                );
            } else {
                throw new IllegalArgumentException("Account not found for ID: " + accountId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve account", e);
        }
    }
    
    @Override
    public HashMap<String, Account> getAccountMap() {
        List<Account> accountList = getAllAccounts();
        HashMap<String, Account> accountMap = new HashMap<>();
        for (Account account : accountList) {
            accountMap.put(account.getAccountId(), account);
        }
        return accountMap;
    }
    
    @Override
    public List<Account> getAllAccounts() {
        ArrayList<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                double balanceInDollars = rs.getDouble("accountBalance") / 100;
                Account account = new Account(
                        String.valueOf(rs.getInt("accountId")),
                        rs.getString("accountName"),
                        rs.getString("bankName"),
                        rs.getString("accountType"),
                        balanceInDollars
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
        int balanceInCents = (int) Math.round(account.getBalance() * 100);
        int maxRetries = 3;
        int attempt = 0;
        while (attempt < maxRetries) {
            try (Connection connection = dbConnection.getConnection()) {
                if (connection.isValid(5)) {
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, account.getAccountName());
                        stmt.setString(2, account.getBankName());
                        stmt.setDouble(3, balanceInCents);
                        stmt.setString(4, account.getAccountType().toUpperCase());
                        
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
    
    public void updateAccount(AccountTableView.AccountModel account) {
        String sql = "UPDATE accounts SET accountName = ?, bankName = ?, accountBalance = ?, accountType = ? WHERE accountId = ?";
        int balanceInCents = (int) Math.round(account.getAccountBalance() * 100);
        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, account.getAccountName());
            stmt.setString(2, account.getBankName());
            stmt.setDouble(3, balanceInCents);
            stmt.setString(4, account.getAccountType());
            stmt.setInt(5, Integer.parseInt(account.getAccountId()));
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("No account found with ID: " + account.getAccountId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update account", e);
        }
    }
}