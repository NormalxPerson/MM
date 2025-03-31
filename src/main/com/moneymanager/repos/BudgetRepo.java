package com.moneymanager.repos;

import com.moneymanager.core.Budget;

public interface BudgetRepo {
	String  addBudgetAndReturnId(Budget budget);
	int deleteBudget(String budgetId);
}
