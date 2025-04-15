package com.moneymanager.ui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BudgetCategoryModel {
	private StringProperty categoryId = new SimpleStringProperty();
	private StringProperty budgetId = new SimpleStringProperty();
	private StringProperty categoryName = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private DoubleProperty allocatedAmount = new SimpleDoubleProperty();
	private DoubleProperty spentAmount = new SimpleDoubleProperty();
	
	public BudgetCategoryModel(String categoryId, String budgetId, String categoryName,
	                           String description, double allocatedAmount, double spentAmount) {
		this.categoryId.set(categoryId);
		this.budgetId.set(budgetId);
		this.categoryName.set(categoryName);
		this.description.set(description);
		this.allocatedAmount.set(allocatedAmount);
		this.spentAmount.set(spentAmount);
	}
	
	public StringProperty categoryIdProperty() { return categoryId; }
	public StringProperty budgetIdProperty() { return budgetId; }
	public StringProperty categoryNameProperty() { return categoryName; }
	public StringProperty descriptionProperty() { return description; }
	public DoubleProperty allocatedAmountProperty() { return allocatedAmount; }
	public DoubleProperty spentAmountProperty() { return spentAmount; }
	
	// Getters and setters
	public String getCategoryId() { return categoryId.get(); }
	public void setCategoryId(String categoryId) { this.categoryId.set(categoryId); }
	
	public String getBudgetId() { return budgetId.get(); }
	public void setBudgetId(String budgetId) { this.budgetId.set(budgetId); }
	
	public String getCategoryName() { return categoryName.getName(); }
	public void setCategoryName(String categoryName) { this.categoryName.set(categoryName); }
	
	public String getDescription() { return description.get(); }
	public void setDescription(String description) { this.description.set(description); }
	
	public double getAllocatedAmount() { return allocatedAmount.get(); }
	public void setAllocatedAmount(double allocatedAmount) { this.allocatedAmount.set(allocatedAmount); }
	
	public double getSpentAmount() { return spentAmount.get(); }
	public void setSpentAmount(double spentAmount) { this.spentAmount.set(spentAmount); }
}

