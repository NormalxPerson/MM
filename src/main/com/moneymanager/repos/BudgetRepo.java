package com.moneymanager.repos;

import com.moneymanager.core.Budget;

import java.time.YearMonth;
import java.util.List;

public interface BudgetRepo {
	String  addBudgetAndReturnId(Budget budget);
	int deleteBudget(String budgetId);
	Budget getBudgetByYearMonth(YearMonth yearMonth);
	List<Budget> getAllBudgets();
}
