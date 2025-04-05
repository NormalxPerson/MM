package com.moneymanager.core;

import java.util.Objects;
import java.util.UUID;

public class BudgetCategory {
	private String categoryId;
	private String budgetId;
	private String categoryName;
	private String description;
	private double allocatedAmount;
	
	public BudgetCategory(String budgetId, String categoryName, String description, double allocatedAmount) {
		this.categoryId = UUID.randomUUID().toString();
		this.budgetId = budgetId;
		this.categoryName = categoryName;
		this.description = description;
		this.allocatedAmount = allocatedAmount;
	}
	
	public BudgetCategory(String categoryId, String budgetId, String categoryName, String description, double allocatedAmount) {
		this.categoryId = categoryId;
		this.budgetId = budgetId;
		this.categoryName = categoryName;
		this.description = description;
		this.allocatedAmount = allocatedAmount;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public String getBudgetId() {
		return budgetId;
	}
	
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getAllocatedAmount() {
		return allocatedAmount;
	}
	
	public void setAllocatedAmount(double allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BudgetCategory that)) return false;
		return Objects.equals(categoryId, that.categoryId) && Objects.equals(budgetId, that.budgetId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(categoryId, budgetId);
	}
}
