package com.moneymanager.repos;

import com.moneymanager.core.BudgetCategory;

public interface BudgetCategoryRepo {
	String addCategoryAndReturnId(BudgetCategory budgetCategory);
	int deleteCategoryById(String budgetCategoryId);
}
