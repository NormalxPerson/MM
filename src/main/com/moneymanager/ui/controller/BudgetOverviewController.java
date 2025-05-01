package com.moneymanager.ui.controller;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.interactor.BudgetInteractor;
import com.moneymanager.ui.model.BudgetCategoryCard;
import com.moneymanager.ui.model.BudgetCategoryModel;
import com.moneymanager.ui.state.BudgetOverviewMod;
import com.moneymanager.ui.validation.FormValidationSupport;
import com.moneymanager.ui.view.BudgetOverviewBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class BudgetOverviewController implements BaseViewController {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetInteractor budgetInteractor;
	private final BudgetOverviewBuilder budgetOverviewBuilder;
	
	//temp
	private final BudgetService budgetService;
	
	public BudgetOverviewController(BudgetService budgetService, TransactionService transactionService) {
		this.budgetOverviewModel = new BudgetOverviewMod();
		createAddBudgetCatCard();
		this.budgetInteractor = new BudgetInteractor(budgetOverviewModel, budgetService, transactionService);
		this.budgetOverviewBuilder = new BudgetOverviewBuilder(budgetOverviewModel, budgetInteractor::createBudget);
	
		//temp
		this.budgetService = budgetService;
	}
	
	public Region getView() {
		Region view = budgetOverviewBuilder.build();
		view.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return view;

	}
	
	public void createAddBudgetCatCard() {
		budgetOverviewModel.setCategoryCreationCard(BudgetCategoryCard.createAddBudgetCategoryCard(this::openCategoryCreationDialog));
	}
	
	public void openCategoryCreationDialog() {
		
		Dialog<BudgetCategoryModel> categoryCreationDialog = new Dialog<>();
		categoryCreationDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		Button saveButton = (Button) categoryCreationDialog.getDialogPane().lookupButton(ButtonType.OK);

		VBox labelsAndFields = buildCreationFields(saveButton);
		categoryCreationDialog.setTitle("Create Budget Category");
		categoryCreationDialog.setHeaderText("Header Text");
		
		
		
		//categoryCreationDialog.getDialogPane().getButtonTypes().addAll( ButtonType.CANCEL);
		categoryCreationDialog.getDialogPane().setContent(labelsAndFields);
		
		categoryCreationDialog.show();
		
		//budgetService.createBudgetCategoryFromInput(budgetOverviewModel.getBudgetId().get(), "food", "description", 50.0);
		//budgetInteractor.loadBudgetForMonth(YearMonth.now());
	}
	
	private VBox buildCreationFields(Button saveButton) {
		TextField categoryNameField = new TextField();
		Label categoryNameLabel = new Label("Category name:");
		HBox categoryNameBox = new HBox(2, categoryNameLabel, categoryNameField);
		
		TextField categoryDescriptionField = new TextField();
		Label categoryDescriptionLabel = new Label("Category description:");
		HBox categoryDescriptionBox = new HBox(2, categoryDescriptionLabel, categoryDescriptionField);
		
		TextField planedAmountField = new TextField();
		Label plannedAmountLabel = new Label("Planned amount:");
		HBox plannedAmountBox = new HBox(2, plannedAmountLabel, planedAmountField);
		
		VBox labelAndFields = new VBox(5, categoryNameBox, categoryDescriptionBox, plannedAmountBox);
		
		
		
		Predicate<String> isPositiveDouble = input -> {
			if (input == null || input.trim().isEmpty()) {
				return false;
			}
				double allocatedAmount = Double.parseDouble(input.trim());
				return allocatedAmount > 0;
		};
		ValidationSupport validationSupport = new ValidationSupport();
		validationSupport.registerValidator(categoryNameField, Validator.createEmptyValidator("Category name cannot be empty"));
		validationSupport.registerValidator(categoryDescriptionField, Validator.createEmptyValidator("Category description cannot be empty"));
		validationSupport.registerValidator(planedAmountField, Validator.createPredicateValidator(isPositiveDouble, "Must be a positive number"));
		
		saveButton.disableProperty().bind(validationSupport.invalidProperty());
		
		
		
		return labelAndFields;
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
