package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.BudgetCategoryAllocation;
import com.moneymanager.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLBudgetCategoryRepo implements BudgetCategoryRepo {
	private DatabaseConnection databaseConnection;
	
	public SQLBudgetCategoryRepo() {
		this.databaseConnection = DatabaseConnection.getInstance();
	}
	
	@Override
	public String addCategoryAndReturnId(BudgetCategory category) {
		String sql = "INSERT INTO categories (categoryId, parentCategoryId, categoryName, categoryDescription) VALUES (?, ?, ?, ?)";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, category.getCategoryId());
			stmt.setString(2, category.getParentCategoryId());
			stmt.setString(3, category.getCategoryName());
			stmt.setString(4, category.getDescription());
			
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
		String sql = "DELETE FROM categories WHERE categoryId = ?";
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
	public List<BudgetCategory> getAllCategories() {
		
		HashMap<String, BudgetCategory> categoryMap = new HashMap<>();
		String sql = "SELECT * FROM categories";
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					BudgetCategory category = createCategoryFromResultSet(rs);
					categoryMap.put(category.getCategoryId(), category);
				}
			}
		} catch (SQLException e) { System.out.println(e.getMessage()); }
		
		for (BudgetCategory category : categoryMap.values()) {
			if (category.getParentCategoryId() != null) {
				categoryMap.get(category.getParentCategoryId()).addChildToList(category);
			}
		}
		return categoryMap.values().stream().toList();
	}
	
	@Override
	public void saveBudgetCategoryAllocation(BudgetCategoryAllocation budgetCategoryAllocation) {
		String sql = "INSERT INTO budget_allocations (allocationId, budgetId, categoryId, allocatedAmount) VALUES (?, ?, ?, ?)";
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, budgetCategoryAllocation.getAllocationId());
			stmt.setString(2, budgetCategoryAllocation.getBudgetId());
			stmt.setString(3, budgetCategoryAllocation.getCategoryId());
			stmt.setInt(4, (int) Math.round(budgetCategoryAllocation.getAllocatedAmount() * 100));
			
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating budget category failed, no rows affected.");
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add budget category", e);
		}
	}
	
	@Override
	public List<BudgetCategoryAllocation> getAllocationsForBudget(String budgetId) {
		List<BudgetCategoryAllocation> allocations = new ArrayList<>();
		String sql = "SELECT * FROM budget_allocations WHERE budgetId = ?";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			 stmt.setString(1, budgetId);
			 
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					allocations.add(createAllocationFromResultSet(rs));
				}
			}
		} catch (SQLException e) { System.out.println(e.getMessage()); }
		
		return allocations;
	}
	
	@Override
	public void updateCategory(BudgetCategory category) {
		String sql = "UPDATE categories SET categoryName = ?, categoryDescription = ?, parentCategoryId = ? WHERE categoryId = ?";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, category.getCategoryName());
			stmt.setString(2, category.getDescription());
			stmt.setString(3, category.getParentCategoryId());
			stmt.setString(4, category.getCategoryId());
			
			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated == 0) {
				throw new SQLException("Updating budget category failed, no rows affected.");
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to update budget category", e);
		}
	}
	
	@Override
	public void updateBudgetCategoryAllocation(BudgetCategoryAllocation allocation) {
		String sql = "UPDATE budget_allocations SET allocatedAmount = ? WHERE categoryId = ? AND budgetId = ?";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			// Convert to cents for storage (following your existing pattern)
			int amountInCents = (int) Math.round(allocation.getAllocatedAmount() * 100);
			
			stmt.setInt(1, amountInCents);
			stmt.setString(2, allocation.getCategoryId());
			stmt.setString(3, allocation.getBudgetId());
			
			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated == 0) {
				throw new SQLException("Updating budget allocation failed, no rows affected.");
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to update budget category allocation", e);
		}
	}
	
	@Override
	public BudgetCategoryAllocation getAllocationByIds(String categoryId, String budgetId) {
		String sql = "SELECT * FROM budget_allocations WHERE categoryId = ? AND budgetId = ?";
		
		try (Connection connection = databaseConnection.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, categoryId);
			stmt.setString(2, budgetId);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return createAllocationFromResultSet(rs);
				}
			}
		} catch (SQLException e) { throw new RuntimeException("Failed to retrieve budget allocation", e); }
		return null;
	}
	
	private BudgetCategoryAllocation createAllocationFromResultSet(ResultSet rs) throws SQLException {
		double amount = rs.getInt("allocatedAmount") / 100.0;
		return new BudgetCategoryAllocation(rs.getString("allocationId"),
				rs.getString("budgetId"),
				rs.getString("categoryId"),
				amount);
	}
	
	private BudgetCategory createCategoryFromResultSet(ResultSet rs) throws SQLException {
		String categoryId = rs.getString("categoryId");
		String parentCategoryId = rs.getString("parentCategoryId");
		String categoryName = rs.getString("categoryName");
		String categoryDescription = rs.getString("categoryDescription");
		
		// Create a category instance with description defaulting to empty string
		// as it's not stored in the database
		BudgetCategory category = new BudgetCategory(categoryId, parentCategoryId, categoryName, categoryDescription);
		
		
		return category;
	}
}
