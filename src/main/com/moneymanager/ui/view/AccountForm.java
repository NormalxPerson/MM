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
				return AccountTableView.AccountModel.AccountType.DEBIT;
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
			accountTypeField.getSelectionModel().clearSelection();
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
		this.setVisible(true);
		this.setManaged(true);
		
		this.getChildren().remove(buttonBox);
		
		loadModelDataIntoForm(null);
		//fix because i get field values from modified fields only.
		accountTypeField.getSelectionModel().selectFirst();
		
		Dialog<AccountTableView.AccountModel> dialog = new Dialog<>();
		dialog.setTitle("Create a New Account");
		
		// Create Save and Cancel buttons
		ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
		
		
		dialog.getDialogPane().setContent(this); // Set form UI inside dialog
		
		// Get the Save button and validate before allowing close
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.disableProperty().bind(isSaveable.not());
		
		saveButton.addEventFilter(ActionEvent.ACTION, event -> {
			if (isSaveable.get()) {
				Map<String, Object> values = fieldChangeTracker.getModifiedValues();
				FormEvent<AccountTableView.AccountModel> createEvent =
						new FormEvent<>(FormEvent.NEWSAVE, null, values);
				fireEvent(createEvent);
				fieldChangeTracker.resetModifications();
			} else {
				// Prevent dialog from closing if validation fails
				event.consume();
			}
		});
		
		dialog.showAndWait();
	
		
	}
	
	
	@Override
	protected void onSaveAction() {
	
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
	
	
	
}


