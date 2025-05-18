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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.YearMonth;
import java.util.Optional;
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
		this.budgetOverviewBuilder = new BudgetOverviewBuilder(budgetOverviewModel, budgetInteractor::createBudget, this::openCategoryCreationDialog);
	
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
		categoryCreationDialog.setTitle("Create Budget Category");
		categoryCreationDialog.setHeaderText("Create a new budget category");
		
		ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		categoryCreationDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField categoryNameField = new TextField();
		TextField categoryDescriptionField = new TextField();
		TextField allocatedAmountField = new TextField();
		
		grid.add(new Label("Category Name"), 0, 0);
		grid.add(categoryNameField, 1, 0);
		grid.add(new Label("Category Description"), 0, 1);
		grid.add(categoryDescriptionField, 1, 1);
		
		categoryCreationDialog.getDialogPane().setContent(grid);
		
		Node saveButton = categoryCreationDialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.setDisable(true);
		
		BooleanBinding validInput = Bindings.createBooleanBinding(() ->
						!categoryNameField.getText().trim().isEmpty(),
				categoryNameField.textProperty());
		
		saveButton.disableProperty().bind(validInput.not());
		
		categoryCreationDialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				try {
					String categoryName = categoryNameField.getText();
					String categoryDescription = categoryDescriptionField.getText();
					
					if (budgetOverviewModel.budgetExists()) {
						BudgetCategory newCat =
						budgetService.createBudgetCategoryFromInput(
								null,
								categoryName,
								categoryDescription
						);
						
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Add to Current Budget?");
						alert.setTitle("Add to Current Budget");
						alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
						Optional<ButtonType> result = alert.showAndWait();
						if (result.isPresent() && result.get() == ButtonType.YES) {
							budgetInteractor.addNewCategoryToCurrentBudget(newCat, budgetOverviewModel.getBudgetId().get());
						}
						
						budgetInteractor.loadBudgetForMonth(budgetOverviewModel.getSelectedYearMonth());
						return null;
					} else {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("No Budget");
						alert.setHeaderText("No Budget Exists");
						alert.setContentText("Please create a budget first before adding categories.");
						alert.showAndWait();
					}
				} catch (NumberFormatException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Invalid Amount");
					alert.setHeaderText("Invalid Amount Format");
					alert.setContentText("Please enter a valid number for the amount.");
					alert.showAndWait();
				}
			}
			return null;
		});
		categoryCreationDialog.showAndWait();
		
		//budgetService.createBudgetCategoryFromInput(budgetOverviewModel.getBudgetId().get(), "food", "description", 50.0);
		//budgetInteractor.loadBudgetForMonth(YearMonth.now());
	}
	
	private boolean isValidAmount(String text) {
		try {
			double amount = Double.parseDouble(text);
			return amount > 0;
		} catch (NumberFormatException e) {
			return false;
		}
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
