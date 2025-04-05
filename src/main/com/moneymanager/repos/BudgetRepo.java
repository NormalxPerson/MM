package com.moneymanager.repos;

import com.moneymanager.core.Budget;

import java.time.YearMonth;

public interface BudgetRepo {
	String  addBudgetAndReturnId(Budget budget);
	int deleteBudget(String budgetId);
	Budget getBudgetByYearMonth(YearMonth yearMonth);
}
