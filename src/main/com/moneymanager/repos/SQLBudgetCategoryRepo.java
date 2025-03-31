package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLBudgetCategoryRepo implements BudgetCategoryRepo {
	private DatabaseConnection databaseConnection;
	
	public SQLBudgetCategoryRepo() {
		this.databaseConnection = DatabaseConnection.getInstance();
	}
	
	@Override
	public String addCategoryAndReturnId(BudgetCategory category) {
		String sql = "INSERT INTO budget_categories (categoryId, budgetId, categoryName, allocatedAmount) VALUES (?, ?, ?, ?)";
		int allocatedAmountInCents = (int) Math.round(category.getBudgetAmount() * 100);
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, category.getCategoryId());
			stmt.setString(2, category.getBudgetId());
			stmt.setString(3, category.getCategoryName());
			stmt.setInt(4, allocatedAmountInCents);
			
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating budget category failed, no rows affected.");
			}
			
			return category.getCategoryId();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add budget category", e);
		}
	}
	
	@Override
	public int deleteCategoryById(String categoryId) {
		String sql = "DELETE FROM budget_categories WHERE categoryId = ?";
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, categoryId);
			
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to delete budget category", e);
		}
	}
	
	private BudgetCategory createCategoryFromResultSet(ResultSet rs) throws SQLException {
		String categoryId = rs.getString("categoryId");
		String budgetId = rs.getString("budgetId");
		String categoryName = rs.getString("categoryName");
		double allocatedAmount = rs.getInt("allocatedAmount") / 100.0;
		
		// Create a category instance with description defaulting to empty string
		// as it's not stored in the database
		BudgetCategory category = new BudgetCategory(categoryId, budgetId, categoryName, "", allocatedAmount);
		
		
		return category;
	}
}
