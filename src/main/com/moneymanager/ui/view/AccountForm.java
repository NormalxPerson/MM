package com.moneymanager.ui.view;

import com.moneymanager.ui.event.FormEvent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;

import java.util.Map;

public class AccountForm extends AbstractForm<AccountTableView.AccountModel> {
	private TextField accountNameField;
	private TextField bankNameField;
	private ComboBox<AccountTableView.AccountModel.AccountType> accountTypeField;
	private TextField accountBalanceField;
	
	
	public AccountForm() {
		super();
		initializeFields();
		setupValidators();
	}
	
	protected void initializeFields() {
		accountNameField = new TextField();
		bankNameField = new TextField();
		accountTypeField = new ComboBox<>();
		accountBalanceField = new TextField();
		
		accountTypeField.setItems(FXCollections.observableArrayList(AccountTableView.AccountModel.AccountType.values()));
		
		// Add StringConverter for AccountType
		accountTypeField.setConverter(new StringConverter<AccountTableView.AccountModel.AccountType>() {
			@Override
			public String toString(AccountTableView.AccountModel.AccountType object) {
				return object != null ? object.getDisplayName() : "";
			}
			
			@Override
			public AccountTableView.AccountModel.AccountType fromString(String string) {
				// Find the AccountType that matches this display name
				for (AccountTableView.AccountModel.AccountType type : AccountTableView.AccountModel.AccountType.values()) {
					if (type.getDisplayName().equals(string)) {
						return type;
					}
				}
				// Default to DEBT if no match is found
				return AccountTableView.AccountModel.AccountType.DEBT;
			}
		});
		
		accountTypeField.getSelectionModel().selectFirst();
		
		Label accountNameLabel = new Label("Account Name");
		Label bankNameLabel = new Label("Bank Name");
		Label accountTypeLabel = new Label("Account Type");
		Label accountBalanceLabel = new Label("Account Balance");
		
		VBox nameFieldBox = new VBox(2, accountNameLabel, accountNameField);
		VBox bankNameFieldBox = new VBox(2,bankNameLabel, bankNameField);
		VBox typeFieldBox = new VBox(2, accountTypeLabel, accountTypeField);
		VBox balanceFieldBox = new VBox(2, accountBalanceLabel, accountBalanceField);

		this.getChildren().addAll(nameFieldBox, bankNameFieldBox, typeFieldBox, balanceFieldBox);
		
		registerField("accountName", accountNameField, nameFieldBox);
		registerField("bankName", bankNameField, bankNameFieldBox);
		registerField("accountType", accountTypeField, typeFieldBox);
		registerField("accountBalance", accountBalanceField, balanceFieldBox);
		
	}
	
	protected void setupValidators() {
		// Register validators with the ValidationSupport system
		validationSupport.registerValidator(
				accountNameField,
				Validator.createEmptyValidator("Account name is required")
		);
		
		validationSupport.registerValidator(
				bankNameField,
				Validator.createEmptyValidator("Bank name is required")
		);
		
		validationSupport.registerValidator(
				accountTypeField,
				Validator.createEmptyValidator("Account type is required")
		);
		
		validationSupport.registerValidator(
				accountBalanceField,
				Validator.combine(
						Validator.createEmptyValidator("Account balance is required"),
						Validator.createRegexValidator("Balance must be a number", "-?\\d+(\\.\\d+)?", Severity.ERROR)
				)
		);
	}
	
	@Override
	protected void loadModelDataIntoForm(AccountTableView.AccountModel accountModel) {
		if (accountModel == null) {
			// Clear all fields for a new account
			accountNameField.clear();
			bankNameField.clear();
			accountTypeField.getSelectionModel().selectFirst();
			accountBalanceField.clear();
		} else {
			// Load data from existing account
			accountNameField.setText(accountModel.getAccountName());
			bankNameField.setText(accountModel.getBankName());
			accountTypeField.setValue(accountModel.getAccountType());
			accountBalanceField.setText(String.format("%.2f", accountModel.getAccountBalance()));
		}
		
		// Reset modification flags since we just loaded data
		fieldChangeTracker.resetModifications();
		// Validate the form
		validationSupport.revalidate();
	}
	

	
	@Override
	public void openCreationDialog() {
		Dialog<Map<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create Account");
		
		// Create Save and Cancel buttons
		ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
		

		
		dialog.getDialogPane().setContent(this); // Set form UI inside dialog
		
		// Get the Save button and validate before allowing close
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.addEventFilter(ActionEvent.ACTION, event -> {
			//	Map<String, String> errors = tempForm.validateFields(tempForm.captureFieldValues(), tempForm.getFieldConstraints());
/*			//if (!errors.isEmpty()) {
				for (String fieldName : errors.keySet()) {
					Control field = tempForm.fieldMap.get(fieldName);
					if (field != null && !field.getStyleClass().contains("error-border")) {
							field.getStyleClass().add("error-border");
					}
				}
			}
				//showValidationErrors(errors, fieldMap); // Show errors if validation fails
				event.consume(); // Prevent dialog from closing
			
		});
		
		// Handle dialog result
		//dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				// Get user input
				Map<String, String> fieldValues = tempForm.captureFieldValues();
				//String accountName = accountNameField.getText();
				//String bankName = bankNameField.getText();
				//String accountType = accountTypeField.getValue();
				//double accountBalance = Double.parseDouble(accountBalanceField.getText());
				
				// Create and return the new AccountModel
				//return new AccountTableView.AccountModel(accountName, bankName, accountType, accountBalance, "");
				return fieldValues;
			}
			return null; // Return null if canceled
		});
		
		// Show dialog and get result
		dialog.showAndWait().ifPresent(fieldValues -> {
			if (fieldValues != null) {
				// Add new model to the observable list
				accountService.createAddAndGetNewAccountModel(
						fieldValues.get("accountName"),
						fieldValues.get("bankName"),
						fieldValues.get("accountType"),
						Double.parseDouble(fieldValues.get("accountBalance"))
				);
				accountService.loadAccountModelsObservableList(); // Refresh account list
			}
		*/
		});
 
	}
	
	
	
	
	@Override
	protected void onDeleteAction() {
		// Ask for confirmation before deleting
		Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDialog.setTitle("Confirm Deletion");
		confirmDialog.setHeaderText("Delete Account");
		confirmDialog.setContentText("Are you sure you want to delete: " + currentModel.getAccountName() + "?");
		
		confirmDialog.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				// Fire delete event
				fireDeleteEvent();
			}
		});
	}
	
	@Override
	protected void onSaveAction() {
		// First validate the form
		validationSupport.revalidate();
		
		// Check if form is valid and modified
		if (isSaveable.get()) {
			System.out.println(this.currentModel.toString());
/*			for (Control field : fieldMap.values()) {
				System.out.println(field.toString());
			}*/
			
			for (Object obj : fieldChangeTracker.getModifiedValues().values()) {
				System.out.println(obj.toString());
			}
			// Let fireSaveEvent handle collecting values and firing the event
			fireSaveEvent();
		} else if (!isValid.get()) {
			System.out.println("Please correct the validation errors");
		} else if (!isModified.get()) {
			System.out.println("No changes to save");
		}
	}
	
	
}


