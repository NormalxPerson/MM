package com.moneymanager.ui.view;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.AddingModelEvent;
import com.moneymanager.ui.event.FormClosedEvent;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class AccountSlidingForm extends SlidingForm<AccountTableView.AccountModel> {
	private TextField accountNameField;
	private TextField bankNameField;
	private ComboBox<String> accountTypeField;
	private TextField accountBalanceField;
	
	private AccountService accountService;

	public AccountSlidingForm(AccountService accountService) {
		super();
		addButton.setText("Add Account");
		this.accountService = accountService;
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
	}
	
	@Override
	protected void loadModelDataIntoForm(AccountTableView.AccountModel accountModel) {
		if (accountModel == null) {
			clearFormFields();
		} else {
		
			accountNameField.setText(accountModel.getAccountName());
			bankNameField.setText(accountModel.getBankName());
			accountTypeField.setValue(accountModel.getAccountType());
			accountBalanceField.setText(String.format("%.2f", accountModel.getAccountBalance()));		}
	}
	
	@Override
	protected void onAddAction() {
		
		setUpForAddingModel();
		this.setVisible(true);
		this.setManaged(true);
	}
	
	@Override
	protected void onSaveAction() {
		String accountName = accountNameField.getText();
		String bankName = bankNameField.getText();
		String accountType = accountTypeField.getValue();
		String accountBalance = accountBalanceField.getText();
		double accountBalanceDouble;
		
		Map<String, String> errorMap = validateAccountFields(accountName, bankName, accountType, accountBalance);
		if (!errorMap.isEmpty()) {
			showValidationErrors(errorMap);
		}
		if (errorMap.isEmpty()) {
			accountBalanceDouble = Double.parseDouble(accountBalance);
			if (status == FormStatus.ADDING) {
				removeBlankAccountModel();
				this.currentModel = accountService.createAddAndGetNewAccountModel(accountName, bankName, accountType, accountBalanceDouble);
				accountService.loadAccountModelsObservableList();
				hideForm();
			} else if (status == FormStatus.EDITING) {
				Map<String, String> changes = hasAccountModelChanged(accountName, bankName, accountType, accountBalanceDouble);
				if (!changes.isEmpty()) {
					showChanges(changes);
					
					System.out.println(changes.keySet());
					currentModel.setAccountName(accountName);
					currentModel.setBankName(bankName);
					currentModel.setAccountType(accountType);
					currentModel.setAccountBalance(accountBalanceDouble);
					
					accountService.updateAccount(currentModel);
				}
			}
		}
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
	
	private void clearFormFields() {
		accountNameField.clear();
		bankNameField.clear();
		accountBalanceField.clear();
	}
	
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
	
	private void removeBlankAccountModel() {
		accountService.removeBlankAccountModel(this.currentModel);
		this.currentModel = null;
	}

	public void hideForm() {
		
		if (this.status == FormStatus.ADDING) {
			removeBlankAccountModel();
		}
		resetFieldStyles();
		clearFormFields();
		setVisible(false);
		setManaged(false);
		this.status = FormStatus.CLOSED;
		Event.fireEvent(this, new FormClosedEvent());
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
	
	private void resetFieldStyles() {
		// Clear previous styles
		accountNameField.setStyle("");
		bankNameField.setStyle("");
		accountTypeField.setStyle("");
		accountBalanceField.setStyle("");
		
		accountNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		bankNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		accountTypeField.getStyleClass().add("md3-rounded-small"); // Example: rounded corners for ComboBox - adjust if needed
		accountBalanceField.getStyleClass().addAll("text-field", "md3-rounded-small");
	}
	
	public TextField getAccountNameField() {
		return accountNameField;
	}
	
	public TextField getBankNameField() {
		return bankNameField;
	}
	
	public ComboBox<String> getAccountTypeField() {
		return accountTypeField;
	}
	
	public TextField getAccountBalanceField() {return accountBalanceField;}


	
}


