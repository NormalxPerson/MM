package com.moneymanager.ui.controller;

import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.interactor.BudgetInteractor;
import com.moneymanager.ui.state.BudgetOverviewMod;

public class BudgetOverviewController {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetInteractor budgetInteractor;
	
	
	public BudgetOverviewController(BudgetService budgetService, TransactionService transactionService) {
		this.budgetOverviewModel = new BudgetOverviewMod();
		this.budgetInteractor = new BudgetInteractor(budgetOverviewModel, budgetService, transactionService);
		
	}
	
	
}
