package com.moneymanager.ui.model;

public class BudgetCategoryModel {
	private String categoryId;
	private String budgetId;
	private String categoryName;
	private String description;
	private double allocatedAmount;
	private double spentAmount;
	
	public BudgetCategoryModel(String categoryId, String budgetId, String categoryName,
	                           String description, double allocatedAmount, double spentAmount) {
		this.categoryId = categoryId;
		this.budgetId = budgetId;
		this.categoryName = categoryName;
		this.description = description;
		this.allocatedAmount = allocatedAmount;
		this.spentAmount = spentAmount;
	}
	
	// Getters and setters
	public String getCategoryId() { return categoryId; }
	public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
	
	public String getBudgetId() { return budgetId; }
	public void setBudgetId(String budgetId) { this.budgetId = budgetId; }
	
	public String getCategoryName() { return categoryName; }
	public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public double getAllocatedAmount() { return allocatedAmount; }
	public void setAllocatedAmount(double allocatedAmount) { this.allocatedAmount = allocatedAmount; }
	
	public double getSpentAmount() { return spentAmount; }
	public void setSpentAmount(double spentAmount) { this.spentAmount = spentAmount; }
}

