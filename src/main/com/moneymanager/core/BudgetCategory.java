package com.moneymanager.core;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BudgetCategory {
	private String categoryId;
	private String parentCategoryId = null;
	private String categoryName;
	private String description;
	private List<BudgetCategory> childrenCategories;
	
	public BudgetCategory(String categoryName, String description) {
		this.categoryId = UUID.randomUUID().toString();
		this.categoryName = categoryName;
		this.description = description;
	}
	
	public BudgetCategory(String categoryId, String parentCategoryId, String categoryName, String description) {
		this.categoryId = categoryId;
		this.parentCategoryId = parentCategoryId;
		this.categoryName = categoryName;
		this.description = description;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public String getParentCategoryId() { return parentCategoryId; }
	public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
	
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
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BudgetCategory that)) return false;
		return Objects.equals(categoryId, that.categoryId) && Objects.equals(parentCategoryId, that.parentCategoryId) && Objects.equals(categoryName, that.categoryName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(categoryId, categoryName);
	}
}
