package com.moneymanager.ui.view;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.AddingModelEvent;
import com.moneymanager.ui.event.FormClosedEvent;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountSlidingForm extends AbstractSlidingForm<AccountTableView.AccountModel> {
	private TextField accountNameField;
	private TextField bankNameField;
	private ComboBox<String> accountTypeField;
	private TextField accountBalanceField;
	
	private AccountService accountService;
	
	
	
	public AccountSlidingForm(AccountService accountService) {
		super();
		addButton.setText("Add Account");
		this.accountService = accountService;
		initializeLayout();
	}
	
	@Override
	protected void initializeLayout() {
		accountNameField = new TextField("Account Name");
		bankNameField = new TextField("Bank Name");
		accountTypeField = new ComboBox<>();
		accountBalanceField = new TextField("Account Balance");
		
		accountTypeField.setItems(FXCollections.observableArrayList("DEBT", "CREDIT"));
		accountTypeField.getSelectionModel().selectFirst();
		
		Label accountNameLabel = new Label("Account Name");
		Label bankNameLabel = new Label("Bank Name");
		Label accountTypeLabel = new Label("Account Type");
		Label accountBalanceLabel = new Label("Account Balance");
		
		accountNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		bankNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		accountTypeField.getStyleClass().add("md3-rounded-small"); // Example: rounded corners for ComboBox - adjust if needed
		accountBalanceField.getStyleClass().addAll("text-field", "md3-rounded-small");
		
		VBox nameFieldBox = new VBox(2, accountNameLabel, accountNameField);
		VBox bankNameFieldBox = new VBox(2,bankNameLabel, bankNameField);
		VBox typeFieldBox = new VBox(2, accountTypeLabel, accountTypeField);
		VBox balanceFieldBox = new VBox(2, accountBalanceLabel, accountBalanceField);

		this.getChildren().addAll(nameFieldBox, bankNameFieldBox, typeFieldBox, balanceFieldBox);
		
		fieldMap.put("accountName", accountNameField);
		fieldMap.put("bankName", bankNameField);
		fieldMap.put("accountType", accountTypeField);
		fieldMap.put("accountBalance", accountBalanceField);
	}
	
	@Override
	protected void loadModelDataIntoForm(AccountTableView.AccountModel accountModel) {
		if (accountModel == null) {
			resetFormFields();
		} else {
		
			accountNameField.setText(accountModel.getAccountName());
			bankNameField.setText(accountModel.getBankName());
			accountTypeField.setValue(accountModel.getAccountType());
			accountBalanceField.setText(String.format("%.2f", accountModel.getAccountBalance()));		}
	}
	
	@Override
	public void onAddAction() {
		resetFormFields();
		setUpForAddingModel();
		this.setVisible(true);
		this.setManaged(true);
	}
	
	@Override
	protected void onSaveAction() { // Implemented in AccountSlidingForm
		Map<String, String> fieldValues = captureFieldValues(); // Call abstract method
		Map<String, List<String>> constraints = getFieldConstraints(); // Call abstract method
		
		Map<String, String> oldValues = null; // Get old values before changes if needed for comparison
		if (getFormStatus() == FormStatus.EDITING && currentModel != null) {
			oldValues = captureCurrentFieldValues(); // Implement this method to capture old values
		}
		
		Map<String, String> errors = validateFields(fieldValues, constraints);
		
		if (errors.isEmpty()) {
			try {
				String accountName = accountNameField.getText();
				String bankName = bankNameField.getText();
				String accountType = accountTypeField.getValue();
				double accountBalance = Double.parseDouble(accountBalanceField.getText());
				
				if (getFormStatus() == FormStatus.ADDING) {
					this.currentModel = accountService.createAddAndGetNewAccountModel(accountName, bankName, accountType, accountBalance );
					accountService.loadAccountModelsObservableList();
					resetFormFields(); // Clear form after successful add
					hideForm();
				} else if (getFormStatus() == FormStatus.EDITING && currentModel != null) {
					if (oldValues != null) { // Compare only in EDITING mode and if oldValues were captured
						Map<String, String> changes = hasModelChanged(oldValues, fieldValues); // Now calls the generic version and showChanges
						if (!changes.isEmpty()) {
							this.currentModel.setAccountName(accountName);
							this.currentModel.setBankName(bankName);
							this.currentModel.setAccountType(accountType);
							this.currentModel.setAccountBalance(accountBalance);
							System.out.println("Changes detected: " + changes);
							
						}
						
					}
				}
				Event.fireEvent(this, new FormClosedEvent()); // If you have such an event
			} catch (IllegalArgumentException e) {
				System.out.println("Validation Error: " + e.getMessage()); // Or handle more gracefully
			}
		} else {
			showValidationErrors(errors, fieldMap);
		}
	}
	
	
	@Override
	protected Map<String, String> captureFieldValues() { // Implemented in AccountSlidingForm
		Map<String, String> fieldValues = new HashMap<>();
		fieldValues.put("accountName", accountNameField.getText());
		fieldValues.put("bankName", bankNameField.getText());
		fieldValues.put("accountType", accountTypeField.getValue());
		fieldValues.put("accountBalance", accountBalanceField.getText());
		return fieldValues;
	}
	
	@Override
	protected Map<String, List<String>> getFieldConstraints() { // Implemented in AccountSlidingForm
		Map<String, List<String>> constraints = new HashMap<>();
		constraints.put("accountName", List.of("required"));
		constraints.put("bankName", List.of("required"));
		constraints.put("accountType", List.of("required", "options:Checking,Savings,Credit Card,Investment"));
		constraints.put("accountBalance", List.of("required", "double"));
		return constraints;
	}
	
	// ** New method to capture current field values ** (moved to be used with oldValues comparison)
	private Map<String, String> captureCurrentFieldValues() {
		Map<String, String> currentValues = new HashMap<>();
		currentValues.put("accountName", accountNameField.getText());
		currentValues.put("bankName", bankNameField.getText());
		currentValues.put("accountType", accountTypeField.getValue());
		currentValues.put("accountBalance", accountBalanceField.getText());
		return currentValues;
	}
	
	@Override
	protected void restoreDefaultStyleClasses(Control field) {
		if (field instanceof TextField) {
			field.getStyleClass().addAll("text-field", "md3-rounded-small"); // Default styles for TextField
		} else if (field instanceof ComboBox) {
			field.getStyleClass().add("md3-rounded-small"); // Default styles for ComboBox
		}
		// Add more conditions for other Control types if needed in your forms
	}
	
	
	@Override
	protected void onCancelAction() {
		hideForm();
	}
	
	@Override
	protected void onDeleteAction() {
		return;
	}
	
	private Map<String, String> hasAccountModelChanged(String accountName, String bankName, String accountType, double accountBalance) {
		Map<String, String> changes = new HashMap<>();
		
		if (!currentModel.getAccountName().equals(accountName)) {
			changes.put("accountName", "Changed Account Name");
		}
		
		if (!currentModel.getBankName().equals(bankName)) {
			changes.put("bankName", "Changed Bank Name");
		}
		if (!currentModel.getAccountType().equals(accountType)) {
			changes.put("accountType", "Changed Account Type");
		}
		if (currentModel.getAccountBalance() != accountBalance) {
			changes.put("accountBalance", "Changed Account Balance");
		}
		return changes;
	}
	
	@Override
	public void setUpFields() {
	
	}
	
	@Override
	public void setUpForAddingModel() {
		setFormStatus(FormStatus.ADDING);
		updateSelectedModel(accountService.createAndGetBlankAccountModel());
		loadModelDataIntoForm(this.currentModel);
		if (!this.isVisible()) {
			this.setVisible(true);
			this.setManaged(true);
		}
		Event.fireEvent(this, new AddingModelEvent());
	}
	
	protected void removeBlankAccountModel() {
		accountService.removeBlankAccountModel(this.currentModel);
		this.currentModel = null;
	}
	
	@Override
	public void hideForm() {
		if (this.status == FormStatus.ADDING) {
			removeBlankAccountModel();
		}
		clearFormFields();
		setVisible(false);
		setManaged(false);
		this.status = FormStatus.CLOSED;
		Event.fireEvent(this, new FormClosedEvent());
	}
	
	@Override
	protected void clearFormFields() {
	
	}
	
	private Map<String, String> captureModelValues(AccountTableView.AccountModel model) {
		Map<String, String> modelValues = new HashMap<>();
		if (model != null) {
			modelValues.put("accountName", model.getAccountName());
			modelValues.put("bankName", model.getBankName());
			modelValues.put("accountType", model.getAccountType());
			modelValues.put("accountBalance", String.valueOf(model.getAccountBalance()));
		}
		return modelValues;
	}
	
	public Map<String, String> validateAccountFields(String accountName, String bankName, String accountType, String  balanceInput) {
		Map<String, String> errors = new HashMap<>();
		
		if (accountName == null || accountName.trim().isEmpty()) {
			errors.put("accountName", "Account name cannot be empty.");
		}
		if (bankName == null || bankName.trim().isEmpty()) {
			errors.put("bankName", "Bank name cannot be empty.");
		}
		if (accountType == null || (!accountType.equals("DEBT") && !accountType.equals("CREDIT"))) {
			errors.put("accountType", "Account type must be DEBT or CREDIT.");
		}
		if (balanceInput == null || balanceInput.trim().isEmpty()) {
			errors.put("balance", "Balance cannot be empty.");
		} else {
			try {
				Double.parseDouble(balanceInput);
			} catch (NumberFormatException e) {
				errors.put("balance", "Balance must be a valid number.");
			}
		}
		
		return errors;
	}
	
	private void showValidationErrors(Map<String, String> errors) {
		// Clear previous styles
		accountNameField.setStyle("");
		bankNameField.setStyle("");
		accountTypeField.setStyle("");
		accountBalanceField.setStyle("");
		
		// Apply error styles
		if (errors.containsKey("accountName")) {
			accountNameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
		if (errors.containsKey("bankName")) {
			bankNameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
		if (errors.containsKey("accountType")) {
			accountTypeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
		if (errors.containsKey("balance")) {
			accountBalanceField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
	}
	
	private void showChanges(Map<String, String> changes) {
		// Clear previous styles
		accountNameField.setStyle("");
		bankNameField.setStyle("");
		accountTypeField.setStyle("");
		accountBalanceField.setStyle("");
		
		// Apply error styles
		if (changes.containsKey("accountName")) {
			accountNameField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("bankName")) {
			bankNameField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("accountType")) {
			accountTypeField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("balance")) {
			accountBalanceField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
	}
	
	private void resetFormFields() {
		accountNameField.clear();
		bankNameField.clear();
		accountTypeField.setValue(null);
		accountBalanceField.clear();
		resetFieldStyles(fieldMap.values()); // Use the generic resetFieldStyles from AbstractSlidingForm!
	}



	
}


