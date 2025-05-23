package com.moneymanager.core;

import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

public class Budget {
	private String budgetId;
	private String budgetName;
	// YearMonth = YYYY-MM
	private YearMonth yearMonth;
	
	public Budget(YearMonth yearMonth) {
		this.budgetId = UUID.randomUUID().toString();
		this.yearMonth = yearMonth;
		generateBudgetName(yearMonth);
	}
	
	public Budget(String budgetId, String budgetName, YearMonth yearMonth) {
		this.budgetId = budgetId;
		this.budgetName = budgetName;
		this.yearMonth = yearMonth;
	}
	
	private void generateBudgetName(YearMonth yearMonth) {
		this.budgetName = yearMonth.getMonth().toString();
		
	}
	
	public String getBudgetId() {
		return budgetId;
	}
	public String getBudgetName() {
		return budgetName;
	}
	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}
	public YearMonth getYearMonth() {
		return yearMonth;
	}
/*	public void setYearMonth(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
	}*/
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Budget budget)) return false;
		return Objects.equals(budgetId, budget.budgetId) && Objects.equals(yearMonth, budget.yearMonth);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(budgetId, yearMonth);
	}
}
