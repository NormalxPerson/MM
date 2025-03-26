package com.moneymanager.ui.view;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.validation.FormValidationSupport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.util.Map;

public class TransactionForm extends AbstractForm<TransactionTableView.TransactionModel> {
	
	private TextField transactionAmountField;
	private TextField transactionDescriptionField;
	private DatePicker transactionDatePicker;
	private ComboBox<TransactionTableView.TransactionModel.TransactionType> transactionTypeComboBox;
	private ComboBox<AccountTableView.AccountModel> accountComboBox;
	
	private ObservableMap<String, AccountTableView.AccountModel> accountModelMap;

	
	public TransactionForm(ObservableMap<String, AccountTableView.AccountModel> accountModelsMap) {
		super();
		this.accountModelMap = accountModelsMap;
		initializeFields();
		setupValidators();
	}
	
	
	protected void initializeFields() {
		transactionAmountField = new TextField();
		transactionDescriptionField = new TextField();
		transactionDatePicker = new DatePicker(LocalDate.now());
		transactionTypeComboBox = new ComboBox<>();
		accountComboBox = new ComboBox<>();
		
		transactionTypeComboBox.setItems(FXCollections.observableArrayList(TransactionTableView.TransactionModel.TransactionType.values()));
		transactionTypeComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(TransactionTableView.TransactionModel.TransactionType object) {
				return object != null ? object.getDisplayName() : "";
			}
			
			@Override
			public TransactionTableView.TransactionModel.TransactionType fromString(String string) {
				for (TransactionTableView.TransactionModel.TransactionType type : TransactionTableView.TransactionModel.TransactionType.values()) {
					if (type.getDisplayName().equals(string)) {
						return type;
					}
				}
				return TransactionTableView.TransactionModel.TransactionType.EXPENSE;
			}
		});
		transactionTypeComboBox.getSelectionModel().selectLast();
		
		accountComboBox.setItems(FXCollections.observableArrayList(accountModelMap.values()));
		accountComboBox.getSelectionModel().selectFirst();
		
		Label transactionAmountLabel = new Label("Transaction Amount");
		Label transactionDescriptionLabel = new Label("Transaction Description");
		Label transactionDateLabel = new Label("Transaction Date");
		Label transactionTypeLabel = new Label("Transaction Type");
		Label accountLabel = new Label("Account for Transaction");
		
		VBox amountFieldBox = new VBox(2, transactionAmountLabel, transactionAmountField);
		VBox descriptionFieldBox = new VBox(2, transactionDescriptionLabel, transactionDescriptionField);
		VBox dateFieldBox = new VBox(2, transactionDateLabel, transactionDatePicker);
		VBox typeFieldBox = new VBox(2, transactionTypeLabel, transactionTypeComboBox);
		VBox accountFieldBox = new VBox(2, accountLabel, accountComboBox);
		
		this.getChildren().addAll(amountFieldBox, descriptionFieldBox, dateFieldBox, typeFieldBox, accountFieldBox);
	
		registerField("transactionAmount", transactionAmountField, amountFieldBox);
		registerField("transactionDescription", transactionDescriptionField, descriptionFieldBox);
		registerField("transactionDate", transactionDatePicker, dateFieldBox);
		registerField("transactionType", transactionTypeComboBox, typeFieldBox);
		registerField("transactionAccount", accountComboBox, accountFieldBox);
	
	}
	
	@Override
	protected void setupValidators() {
	
		validationSupport.registerValidator(
				transactionAmountField,
				Validator.combine(
						Validator.createEmptyValidator("Transaction Amount is required"),
						Validator.createRegexValidator("Balance must be a number", "-?\\d+(\\.\\d+)?", Severity.ERROR)
				)
		);
		
		validationSupport.registerValidator(
				transactionDescriptionField,
				Validator.createEmptyValidator("Transaction Description Required")
		);
		
		validationSupport.registerValidator(
				transactionDatePicker,
				Validator.createEmptyValidator("Transaction Date Required")
		);
		
		validationSupport.registerValidator(
				transactionTypeComboBox,
				Validator.createEmptyValidator("Transaction Type Required")
		);
		
		validationSupport.registerValidator(
				accountComboBox,
				Validator.createEmptyValidator("Account Needed For Transaction")
		);
	}
	
	@Override
	protected void loadModelDataIntoForm(TransactionTableView.TransactionModel transactionModel) {
		if (transactionModel == null) {
			clearFormFields();
		} else {
			transactionAmountField.setText(String.format("%.2f", transactionModel.getTransactionAmount()));
			transactionDescriptionField.setText(transactionModel.getTransactionDescription());
			transactionDatePicker.setValue(transactionModel.getTransactionDate());
			transactionTypeComboBox.getSelectionModel().select(transactionModel.getTransactionType());
			
			String selectedAccountId = transactionModel.getTransactionAccountId();
			if (selectedAccountId != null && accountModelMap.containsKey(selectedAccountId)) {
				accountComboBox.getSelectionModel().select(accountModelMap.get(selectedAccountId));
			} else { accountComboBox.getSelectionModel().clearSelection(); }
		}
		
		fieldChangeTracker.resetModifications();
		validationSupport.revalidate();
	}
	
	
	@Override
	protected void onSaveAction() {

	}
	
	@Override
	protected void onDeleteAction() {
		Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmDialog.setTitle("Confirm Deletion");
		confirmDialog.setHeaderText("Delete Account");
		confirmDialog.setContentText("Are you sure you want to delete Transaction:\n\tAmount: " + currentModel.getTransactionAmount() + "\n\tDescription: " + currentModel.getTransactionDescription());
		
		confirmDialog.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				// Fire delete event
				fireDeleteEvent();
			}
		});
	}
	
	public void clearFormFields() {
		transactionAmountField.clear();
		transactionDescriptionField.clear();
		transactionDatePicker.setValue(LocalDate.of(2025, 1, 1));
		transactionTypeComboBox.getSelectionModel().clearSelection();
		accountComboBox.getSelectionModel().clearSelection();
	}
	
	
	
	private void showChanges(Map<String, String> changes) {
		transactionAmountField.setStyle("");
		transactionDescriptionField.setStyle("");
		transactionDatePicker.setStyle("");
		transactionTypeComboBox.setStyle("");
		accountComboBox.setStyle("");
		
		if (changes.containsKey("amount")) {
			transactionAmountField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("description")) {
			transactionDescriptionField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("date")) {
			transactionDatePicker.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("type")) {
			transactionTypeComboBox.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
		if (changes.containsKey("accountId")) {
			accountComboBox.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
		}
	}
	
	@Override
	public void openCreationDialog() {
		this.setVisible(true);
		this.setManaged(true);
		this.getChildren().remove(buttonBox);
		
		loadModelDataIntoForm(null);
		transactionDatePicker.setValue(LocalDate.now());
		transactionTypeComboBox.getSelectionModel().select(TransactionTableView.TransactionModel.TransactionType.EXPENSE);
		accountComboBox.getSelectionModel().selectFirst();
		
		
		Dialog<TransactionTableView.TransactionModel> dialog = new Dialog<>();
		dialog.setTitle("Create a New Transaction");
		
		ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
		
		dialog.getDialogPane().setContent(this);
		
		Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.disableProperty().bind(isSaveable.not());
		
		saveButton.addEventFilter(ActionEvent.ACTION, event -> {
			if (isSaveable.get()) {
				Map<String, Object> values = fieldChangeTracker.getModifiedValues();
				FormEvent<TransactionTableView.TransactionModel> createEvent =
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

	
	
}
