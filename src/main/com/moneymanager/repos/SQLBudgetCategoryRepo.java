package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.Transaction;
import com.moneymanager.core.TransactionFactory;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLBudgetCategoryRepo implements BudgetCategoryRepo {
	private DatabaseConnection databaseConnection;
	
	public SQLBudgetCategoryRepo() {
		this.databaseConnection = DatabaseConnection.getInstance();
	}
	
	@Override
	public String addCategoryAndReturnId(BudgetCategory category) {
		String sql = "INSERT INTO budget_categories (categoryId, budgetId, categoryName, allocatedAmount) VALUES (?, ?, ?, ?)";
		int allocatedAmountInCents = (int) Math.round(category.getAllocatedAmount() * 100);
		
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
	
	@Override
	public void addBudgetCategory(BudgetCategory budgetCategory) {
		addCategoryAndReturnId(budgetCategory);
	
	}
	
	@Override
	public List<BudgetCategory> getCategoriesByBudgetId(String budgetId) {
		List<BudgetCategory> categoryList = new ArrayList<>();
		String sql = "SELECT * FROM budget_categories WHERE budgetId = ?";
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, budgetId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					categoryList.add(createCategoryFromResultSet(rs));
				}
			}
		} catch (SQLException e) { System.out.println(e.getMessage()); }
		return categoryList;
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
