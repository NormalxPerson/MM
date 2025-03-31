package com.moneymanager.core;

import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

public class BudgetCategory {
	private String categoryId;
	private String budgetId;
	private String categoryName;
	private String description;
	private double budgetAmount;
	
	public BudgetCategory(String budgetId, String categoryName, String description, double budgetAmount) {
		this.categoryId = UUID.randomUUID().toString();
		this.budgetId = budgetId;
		this.categoryName = categoryName;
		this.description = description;
		this.budgetAmount = budgetAmount;
	}
	
	public BudgetCategory(String categoryId, String budgetId, String categoryName, String description, double budgetAmount) {
		this.categoryId = categoryId;
		this.budgetId = budgetId;
		this.categoryName = categoryName;
		this.description = description;
		this.budgetAmount = budgetAmount;
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
	
	public double getBudgetAmount() {
		return budgetAmount;
	}
	
	public void setBudgetAmount(double budgetAmount) {
		this.budgetAmount = budgetAmount;
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
