package com.moneymanager.service;

import com.moneymanager.core.Budget;
import com.moneymanager.core.BudgetCategory;
import com.moneymanager.repos.BudgetCategoryRepo;
import com.moneymanager.repos.BudgetRepo;
import com.moneymanager.repos.SQLBudgetRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.model.BudgetCategoryModel;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public class BudgetService {
	BudgetRepo budgetRepo;
	BudgetCategoryRepo budgetCategoryRepo;
	TransactionRepo transactionRepo;

	public BudgetService(BudgetRepo budgetRepo, BudgetCategoryRepo budgetCategoryRepo, TransactionRepo transactionRepo) {
		this.budgetRepo = budgetRepo;
		this.budgetCategoryRepo = budgetCategoryRepo;
		this.transactionRepo = transactionRepo;
	}
	
	public Budget createBudget(String budgetName, YearMonth yearMonth) {
		Budget newBudget = new Budget(budgetName, yearMonth);
		budgetRepo.addBudgetAndReturnId(newBudget);
		System.out.print(newBudget.getBudgetId() + newBudget.getBudgetName() + newBudget.getYearMonth());
		return newBudget;
	}
	
	
	public ObservableList<BudgetCategoryModel> getCategoriesForBudget(String currentBudgetId) {
	}
}
