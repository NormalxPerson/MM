package com.moneymanager.ui.view;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.AddingModelEvent;
import com.moneymanager.ui.event.FormClosedEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionSlidingForm extends SlidingForm<TransactionTableView.TransactionModel> {
	
	private TextField transactionAmountField;
	private TextField transactionDescriptionField;
	private DatePicker transactionDatePicker;
	private ComboBox<String> transactionTypeComboBox;
	private ComboBox<AccountTableView.AccountModel> accountComboBox;

	private TransactionService transactionService;
	
	public TransactionSlidingForm(TransactionService transactionService) {
		super();
		addButton.setText("Add Transaction");
		this.transactionService = transactionService;
	}
	
	@Override
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
	protected void onAddAction() {
		setFormStatus(FormStatus.ADDING);
		clearFormFields();
		if (!this.isVisible()) {
			this.setVisible(true);
			this.setManaged(true);
		}
		Event.fireEvent(this, new AddingModelEvent());
	}
	
	@Override
	protected void onSaveAction() {
		double amount = Double.parseDouble(transactionAmountField.getText());
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
		
		hideForm();
	}

	
	@Override
	protected void onCancelAction() {
		hideForm();
	}
	
	@Override
	protected void onDeleteAction() {
		return;
	}
	
	public void showForm() {
		setVisible(true);
		setManaged(true);
		setUpFields();
	}
	
	public void setUpFields() {
		transactionAmountField.setPromptText("Amount");
		transactionDescriptionField.setPromptText("Description");
		transactionDatePicker.setValue(LocalDate.now());
		transactionTypeComboBox.setValue("EXPENSE");
		if (!transactionService.getAccountModelObservableList().isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
		else {accountComboBox.setPromptText("Account"); }
	}
	
	public void hideForm() {
		setVisible(false);
		setManaged(false);
		clearFormFields();

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
	
	private Map<String, String> hasTransactionModelChanged(double amount, String description, String date, String type, String accountId) {
		
		Map<String, String> oldValues = Map.of(
				"amount", String.valueOf(currentModel.getTransactionAmount()),
				"description", currentModel.getTransactionDescription(),
				"date", currentModel.getTransactionDate().toString(),
				"type", currentModel.getTransactionType(),
				"accountId", currentModel.getTransactionAccountId()
				
		);
		
		Map<String, String> newValues = Map.of(
				"amount", String.valueOf(amount),
				"description", description,
				"date", date,
				"type", type,
				"accountId", accountId
		);
		
		return hasModelChanged(oldValues, newValues);
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
	
	
	
}
