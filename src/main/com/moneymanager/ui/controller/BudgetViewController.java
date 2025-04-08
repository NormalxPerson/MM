package com.moneymanager.ui.controller;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.service.BudgetService;
import com.moneymanager.ui.model.BudgetCategoryModel;
import com.moneymanager.ui.view.BudgetCategoryContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class BudgetViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox budgetCardsContainerView;
	private String currentBudgetId;
	private BudgetCategoryContainer container;
	private BudgetService budgetService;
	
	ObservableList<YearMonth> listOfYearMonths;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	generateMonths();
	this.container = new BudgetCategoryContainer(listOfYearMonths);
	budgetCardsContainerView.getChildren().add(container);
		
		container.getAddCategoryButton().setOnAction(event -> {
			/*budgetService.createBudget("April", YearMonth.now());
			String budgetId = budgetService.getBudgetFromYearMonth(YearMonth.now()).getBudgetId();
			budgetService.createBudgetCategoryFromInput(budgetId, "food", "", 500.0);
			refreshCategories();*/
		});
	}
	
	public void setBudgetCategoryService(BudgetService service) {
		this.budgetService = service;
	}
	
	public void setCurrentBudgetId(String budgetId) {
		this.currentBudgetId = budgetId;
		if (budgetService != null) {
			refreshCategories();
		}
	}
	
	private void refreshCategories() {
		ObservableList<BudgetCategoryModel> categories =
				budgetService.getCategoriesForBudget(YearMonth.now());
		
		container.setCategoryModels(categories);
	}
	
	public VBox getBudgetContainer() { return budgetCardsContainerView; }
	
	private void generateMonths() {
		listOfYearMonths = FXCollections.observableArrayList();
		for (int i = 1; i <= 12; i++) {
			YearMonth yearMonth = YearMonth.of(2025, i);
			listOfYearMonths.add(yearMonth);
		}
		for (YearMonth yearMonth : listOfYearMonths) {
			System.out.println(yearMonth.toString());
		}
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
