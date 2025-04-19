package com.moneymanager.ui.controller;

import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.interactor.BudgetInteractor;
import com.moneymanager.ui.state.BudgetOverviewMod;
import com.moneymanager.ui.view.BudgetOverviewBuilder;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetOverviewController implements BaseViewController {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetInteractor budgetInteractor;
	private final BudgetOverviewBuilder budgetOverviewBuilder;
	private final Region budgetRegion;
	
	public BudgetOverviewController(BudgetService budgetService, TransactionService transactionService) {
		this.budgetOverviewModel = new BudgetOverviewMod();
		this.budgetInteractor = new BudgetInteractor(budgetOverviewModel, budgetService, transactionService);
		this.budgetOverviewBuilder = new BudgetOverviewBuilder(budgetOverviewModel);
		
	}
	
	public Region getView() {
		return budgetOverviewBuilder.build();
	}
	
	
	@Override
	public void hideForm() {
	
	}
	
	@Override
	public void unselectRow() {
	
	}
	
	@Override
	public void showCreationDialog() {
	
	}
}
