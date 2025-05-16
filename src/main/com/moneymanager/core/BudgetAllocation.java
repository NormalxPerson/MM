package com.moneymanager.core;

import java.util.UUID;

public class BudgetAllocation {
	private String allocationId;
	private String budgetId;
	private String categoryId;
	private double allocatedAmount;
	
	public BudgetAllocation(String budgetId, String categoryId, double allocatedAmount) {
		this.allocationId = UUID.randomUUID().toString();
		this.budgetId = budgetId;
		this.categoryId = categoryId;
		this.allocatedAmount = allocatedAmount;
	}
	
	public BudgetAllocation(String allocationId, String budgetId, String categoryId, double allocatedAmount) {
		this.allocationId = allocationId;
		this.budgetId = budgetId;
		this.categoryId = categoryId;
		this.allocatedAmount = allocatedAmount;
	}
	
	public String getAllocationId() {
		return allocationId;
	}
	
	public String getBudgetId() {
		return budgetId;
	}
	
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public double getAllocatedAmount() {
		return allocatedAmount;
	}
	
	public void setAllocatedAmount(double allocatedAmount) {
		this.allocatedAmount = allocatedAmount;
	}
}
