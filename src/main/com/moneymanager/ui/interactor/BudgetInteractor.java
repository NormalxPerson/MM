package com.moneymanager.ui.interactor;

import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.state.BudgetOverviewMod;

public class BudgetInteractor {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetService budgetService;
	private final TransactionService transactionService;
	
	public BudgetInteractor(BudgetOverviewMod budgetOverviewModel, BudgetService budgetService, TransactionService transactionService) {
		this.budgetOverviewModel = budgetOverviewModel;
		this.budgetService = budgetService;
		this.transactionService = transactionService;
	}
}
