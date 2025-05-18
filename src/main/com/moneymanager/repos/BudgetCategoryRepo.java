package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.BudgetCategoryAllocation;

import java.util.List;

public interface BudgetCategoryRepo {
	String addCategoryAndReturnId(BudgetCategory budgetCategory);
	int deleteCategoryById(String budgetCategoryId);
	void addBudgetCategory(BudgetCategory budgetCategory);
	List<BudgetCategory> getAllCategories();
	
	void saveBudgetCategoryAllocation(BudgetCategoryAllocation budgetCategoryAllocation);
	List<BudgetCategoryAllocation> getAllocationsForBudget(String budgetId);
	
	void updateCategory(BudgetCategory category);
	void updateBudgetCategoryAllocation(BudgetCategoryAllocation allocation);
	BudgetCategoryAllocation getAllocationByIds(String categoryId, String budgetId);
	
}
