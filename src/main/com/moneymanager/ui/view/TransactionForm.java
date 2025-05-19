package com.moneymanager.ui.view;

import com.moneymanager.core.BudgetCategory;
import com.moneymanager.ui.event.FormEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	private ComboBox<BudgetCategory> budgetCategoryComboBox;
	private ComboBox<AccountTableView.AccountModel> accountComboBox;
	private CheckBox updateBalanceCheckBox;
	private VBox updateBalanceFieldBox;
	
	private ObservableMap<String, AccountTableView.AccountModel> accountModelMap;
	private ObservableMap<String, BudgetCategory> categoryMap;
	
	
	public TransactionForm(ObservableMap<String, AccountTableView.AccountModel> accountModelsMap, ObservableList<AccountTableView.AccountModel> accountModelList, ObservableMap<String, BudgetCategory> budgetCategoryMap) {
		super();
		this.accountModelMap = accountModelsMap;
		this.categoryMap = budgetCategoryMap;
		initializeFields(accountModelList);
		setupValidators();
	}
	
	
	protected void initializeFields(ObservableList<AccountTableView.AccountModel> accountModelList) {
		transactionAmountField = new TextField();
		transactionDescriptionField = new TextField();
		transactionDatePicker = new DatePicker(LocalDate.now());
		transactionTypeComboBox = new ComboBox<>();
		budgetCategoryComboBox = new ComboBox<>();
		accountComboBox = new ComboBox<>();
		updateBalanceCheckBox = new CheckBox();
		
		budgetCategoryComboBox.setItems(FXCollections.observableArrayList(categoryMap.values()));
		budgetCategoryComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(BudgetCategory object) {
				return object != null ? object.getCategoryName() : null;
			}
			
			@Override
			public BudgetCategory fromString(String string) {
				for (BudgetCategory category : categoryMap.values()) {
					if (category.getCategoryId().equalsIgnoreCase(string)) {
						return category;
					}
				}
				return null;
			}
		});
		
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
		
		accountComboBox.setItems(accountModelList);
		accountComboBox.getSelectionModel().selectFirst();
		
		Label transactionAmountLabel = new Label("Transaction Amount");
		Label transactionDescriptionLabel = new Label("Transaction Description");
		Label transactionDateLabel = new Label("Transaction Date");
		Label transactionTypeLabel = new Label("Transaction Type");
		Label budgetCategoryLabel = new Label("Budget Category");
		Label accountLabel = new Label("Account for Transaction");
		Label accountBalanceLabel = new Label("Update Account Balance?");
		
		VBox amountFieldBox = new VBox(2, transactionAmountLabel, transactionAmountField);
		VBox descriptionFieldBox = new VBox(2, transactionDescriptionLabel, transactionDescriptionField);
		VBox dateFieldBox = new VBox(2, transactionDateLabel, transactionDatePicker);
		VBox typeFieldBox = new VBox(2, transactionTypeLabel, transactionTypeComboBox);
		VBox budgetCategoryBox = new VBox(2, budgetCategoryLabel, budgetCategoryComboBox);
		VBox accountFieldBox = new VBox(2, accountLabel, accountComboBox);
		updateBalanceFieldBox = new VBox(2, accountBalanceLabel, updateBalanceCheckBox);
		updateBalanceFieldBox.setAlignment(Pos.CENTER);
		
		HBox dateAndCheckbox = new HBox(16, dateFieldBox, updateBalanceFieldBox);
		HBox typeAndCheckbox = new HBox(16, typeFieldBox, budgetCategoryBox);
		
		this.getChildren().addAll(amountFieldBox, descriptionFieldBox, dateAndCheckbox, typeAndCheckbox, accountFieldBox);
	
		registerField("transactionAmount", transactionAmountField, amountFieldBox);
		registerField("transactionDescription", transactionDescriptionField, descriptionFieldBox);
		registerField("transactionDate", transactionDatePicker, dateFieldBox);
		registerField("transactionType", transactionTypeComboBox, typeFieldBox);
		registerField("budgetCategory", budgetCategoryComboBox, budgetCategoryBox);
		registerField("transactionAccount", accountComboBox, accountFieldBox);
		registerField("updateBalance", updateBalanceCheckBox, updateBalanceFieldBox);
	
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
			
			String selectedCategoryId = transactionModel.getTransactionCategoryId();
			if (selectedCategoryId != null) {
				for (BudgetCategory cats : categoryMap.values()) {
					if (cats.getCategoryId().equals(selectedCategoryId)) {
						budgetCategoryComboBox.getSelectionModel().select(cats);
					}
				}
			}
			
			String selectedAccountId = transactionModel.getTransactionAccountId();
			if (selectedAccountId != null && accountModelMap.containsKey(selectedAccountId)) {
				accountComboBox.getSelectionModel().select(accountModelMap.get(selectedAccountId));
			} else { accountComboBox.getSelectionModel().clearSelection(); }
		}
		updateBalanceCheckBox.setSelected(false);
		
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
		updateBalanceCheckBox.setSelected(false);
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
		updateBalanceFieldBox.setVisible(false);
		updateBalanceFieldBox.setManaged(false);
		
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
