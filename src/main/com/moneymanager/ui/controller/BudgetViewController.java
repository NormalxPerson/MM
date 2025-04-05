package com.moneymanager.ui.controller;

import com.moneymanager.service.BudgetService;
import com.moneymanager.ui.model.BudgetCategoryModel;
import com.moneymanager.ui.view.BudgetCategoryContainer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox budgetCardsContainerView;
	private String currentBudgetId;
	private BudgetCategoryContainer container;
	private BudgetService budgetService;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	this.container = new BudgetCategoryContainer();
	budgetCardsContainerView.getChildren().add(container);
		
		container.getAddCategoryButton().setOnAction(event -> {
			System.out.println("Add Category");
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
				budgetService.getCategoriesForBudget(currentBudgetId);
		
		container.setCategoryModels(categories);
	}
	
	public VBox getBudgetContainer() { return budgetCardsContainerView; }
	
	
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
