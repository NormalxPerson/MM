package com.moneymanager.repos;

import com.moneymanager.core.Budget;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SQLBudgetRepo implements BudgetRepo {
	private DatabaseConnection databaseConnection;
	private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
	
	public SQLBudgetRepo() {
		this.databaseConnection = DatabaseConnection.getInstance();
	}
	
	public String addBudgetAndReturnId(Budget newBudget) {
		// budgetYearMonth YYYY-MM
		String sql = "INSERT INTO budgets (budgetId, budgetName, budgetYearMonth) VALUES (?, ?, ?)";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			
			statement.setString(1, newBudget.getBudgetId());
			statement.setString(2, newBudget.getBudgetName());
			statement.setString(3, newBudget.getYearMonth().toString());
			
			statement.executeUpdate();
			
			return newBudget.getBudgetId();
		} catch (SQLException e) {
			System.err.println("Error Adding New Budget: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public int deleteBudget(String budgetId) {
		String sql = "DELETE FROM budgets WHERE budgetId = ?";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, budgetId);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error Deleting Budget: " + e.getMessage());
		}
		return 0;
	}
	
	public Budget getBudgetByYearMonth(YearMonth yearMonth) {
		String sql = "SELECT * FROM budgets WHERE budgetYearMonth = ?";
		try ( Connection connection = databaseConnection.getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, yearMonth.toString());
			
			try (ResultSet rs = stmt.executeQuery()) {
				return createBudgetFromResultSet(rs);
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	@Override
	public List<Budget> getAllBudgets() {
		String sql = "SELECT * FROM budgets";
		List<Budget> budgets = new ArrayList<>();
		try (Connection connection = databaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					budgets.add(createBudgetFromResultSet(rs));
				}
			}
		} catch (SQLException e) { System.out.println("Error Getting All Budgets: " + e.getMessage()); }
		return budgets;
	}
	
	private Budget createBudgetFromResultSet(ResultSet rs) throws SQLException {
		String budgetId = rs.getString("budgetId");
		String budgetName = rs.getString("budgetName");
		String yearMonthStr = rs.getString("budgetYearMonth");
		YearMonth yearMonth = YearMonth.parse(yearMonthStr, YEAR_MONTH_FORMATTER);
		
		Budget dbBudget = new Budget(budgetId, budgetName, yearMonth);
		
		return dbBudget;
	}
	
}
