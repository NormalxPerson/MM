package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;

import java.util.List;

public interface BudgetCategoryRepo {
	String addCategoryAndReturnId(BudgetCategory budgetCategory);
	int deleteCategoryById(String budgetCategoryId);
	void addBudgetCategory(BudgetCategory budgetCategory);
	List<BudgetCategory> getCategoriesByBudgetId(String budgetId);
}
