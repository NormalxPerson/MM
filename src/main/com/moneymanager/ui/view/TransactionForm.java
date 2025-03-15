package com.moneymanager.ui.view;

import com.moneymanager.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.util.Map;

public class TransactionForm extends AbstractForm<TransactionTableView.TransactionModel> {
	
	private TextField transactionAmountField;
	private TextField transactionDescriptionField;
	private DatePicker transactionDatePicker;
	private ComboBox<String> transactionTypeComboBox;
	private ComboBox<AccountTableView.AccountModel> accountComboBox;

	private TransactionService transactionService;
	
	public TransactionForm(TransactionService transactionService, FloatingActionButton floatingActionButton) {
		super();
		this.transactionService = transactionService;
		initializeLayout();
	}
	
	
	protected void initializeLayout() {
		transactionAmountField = new TextField("Amount");
		transactionDescriptionField = new TextField("Description");
		transactionDatePicker = new DatePicker(LocalDate.now());
		
		transactionTypeComboBox = new ComboBox<>();
		transactionTypeComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
		transactionTypeComboBox.getSelectionModel().selectLast();
		
		accountComboBox = new ComboBox<>();
		accountComboBox.setItems(transactionService.getAccountModelObservableList());
		if (!transactionService.getAccountModelObservableList().isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
		else {accountComboBox.setPromptText("Account"); }
		
		Label transactionAmountLabel = new Label("Amount");
		Label transactionDescriptionLabel = new Label("Description");
		Label transactionDateLabel = new Label("Date");
		Label transactionTypeLabel = new Label("Type");
		Label accountLabel = new Label("Account");
		
		transactionAmountField.getStyleClass().addAll("text-field", "md3-rounded-small");
		transactionDescriptionField.getStyleClass().addAll("text-field", "md3-rounded-small");
		transactionDatePicker.getStyleClass().add("md3-rounded-small");
		transactionTypeComboBox.getStyleClass().add("md3-rounded-small");
		
		VBox amountFieldBox = new VBox(2, transactionAmountLabel, transactionAmountField);
		VBox descriptionFieldBox = new VBox(2, transactionDescriptionLabel, transactionDescriptionField);
		VBox dateFieldBox = new VBox(2, transactionDateLabel, transactionDatePicker);
		VBox typeFieldBox = new VBox(2, transactionTypeLabel, transactionTypeComboBox);
		VBox accountFieldBox = new VBox(2, accountLabel, accountComboBox);
		
		this.getChildren().addAll(amountFieldBox, descriptionFieldBox, dateFieldBox, typeFieldBox, accountFieldBox);
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
			accountComboBox.getSelectionModel().select(Integer.parseInt(transactionModel.getTransactionAccountId())-1);
		}
	}
	
	
	@Override
	protected void onSaveAction() {
/*		double amount = Double.parseDouble(transactionAmountField.getText());
		String description = transactionDescriptionField.getText();
		String date = transactionDatePicker.getValue().toString();
		String type = transactionTypeComboBox.getValue();
		String accountId = accountComboBox.getValue().getAccountId();
		
		Map<String, String> fieldValues = Map.of(
				"amount", String.valueOf(amount),
				"description", description,
				"date", date,
				"type", type,
				"accountId", accountId
		);
		
		Map<String, List<String>> constraints = Map.of(
				"amount", List.of("required", "double"),
				"description", List.of("required"),
				"date", List.of("required"),
				"type", List.of("required", "options:INCOME,EXPENSE"),
				"accountId", List.of("required")
		);
		
		Map<String, String> errors = validateFields(fieldValues, constraints);
		if (!errors.isEmpty()) {
			showValidationErrors(errors, getTransactionFieldMap());
			return;
		}
		
		if (status == FormStatus.EDITING) {
			Map<String, String> changes = hasTransactionModelChanged(amount, description, date, type, accountId);
			
			if (!changes.isEmpty()) {
				showChanges(changes); // Highlight changed fields
				currentModel.setTransactionAmount(amount);
				currentModel.setTransactionDescription(description);
				currentModel.setTransactionDate(LocalDate.parse(date));
				currentModel.setTransactionType(type);
				currentModel.setTransactionAccountId(accountId);
				
				transactionService.updateTransaction(currentModel);
			}
		} else if (status == FormStatus.ADDING) {
			this.currentModel = transactionService.createAddAndGetNewTransactionModel(amount, description, date, type, accountId);
			transactionService.addNewTransactionModelToTable(this.currentModel);
		}
		
		hideForm();*/
	}


	
	@Override
	protected void onDeleteAction() {
		return;
	}

	

	
	
	
	public void setUpFields() {
		transactionAmountField.setPromptText("Amount");
		transactionDescriptionField.setPromptText("Description");
		transactionDatePicker.setValue(LocalDate.now());
		transactionTypeComboBox.setValue("EXPENSE");
		if (!transactionService.getAccountModelObservableList().isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
		else {accountComboBox.setPromptText("Account"); }
	}
	
	public void clearFormFields() {
		transactionAmountField.clear();
		transactionDescriptionField.clear();
		transactionDatePicker.setValue(LocalDate.now());
		transactionTypeComboBox.getSelectionModel().select("EXPENSE");
		accountComboBox.getSelectionModel().selectFirst();
	}

	
	private Map<String, Control> getTransactionFieldMap() {
		return Map.of(
				"amount", transactionAmountField,
				"description", transactionDescriptionField,
				"date", transactionDatePicker,
				"type", transactionTypeComboBox,
				"accountId", accountComboBox
		);
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
		Dialog<TransactionTableView.TransactionModel> dialog = new Dialog<>();
		dialog.setTitle("Create Account");
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		dialog.getDialogPane().getButtonTypes().clear();
		ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
		
		dialog.getDialogPane().setContent(this);
	}

	
	
}
