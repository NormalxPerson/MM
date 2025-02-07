package com.moneymanager.ui.view;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormClosedEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class TransactionSlidingForm extends VBox {
	
	private TextField transactionAmountField;
	private TextField transactionDescriptionField;
	private DatePicker transactionDatePicker;
	private ComboBox<String> transactionTypeComboBox;
	private ComboBox<AccountTableView.AccountModel> accountComboBox;
	private Button saveButton;
	private Button closeButton;
	private TransactionService transactionService;
	
	public TransactionSlidingForm(TransactionService transactionService) {
		this.transactionService = transactionService;
		this.setSpacing(10);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		initializeTransactionForm();
		this.getStyleClass().add("sliding-form");
		
		HBox buttonBox = new HBox(10, closeButton, saveButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		
		this.getChildren().addAll(transactionAmountField, transactionDescriptionField, transactionDatePicker, transactionTypeComboBox, accountComboBox, buttonBox);
		
		this.setVisible(false);
		this.setManaged(false);
		
		closeButton.setOnAction(e -> {
			hideForm();
			Event.fireEvent(this, new FormClosedEvent());
		});
	}
	private void initializeTransactionForm() {
		transactionAmountField = new TextField("Amount");
		transactionAmountField.getStyleClass().addAll("text-field", "md3-rounded-small");
		transactionDescriptionField = new TextField("Description");
		transactionDescriptionField.getStyleClass().addAll("text-field", "md3-rounded-small");
		transactionDatePicker = new DatePicker(LocalDate.now());
		transactionDatePicker.getStyleClass().add("md3-rounded-small");
		transactionTypeComboBox = new ComboBox<>();
		transactionTypeComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
		transactionTypeComboBox.setValue("EXPENSE");
		transactionTypeComboBox.getStyleClass().add("md3-rounded-small");
		
		accountComboBox = new ComboBox<>();
		accountComboBox.setItems(transactionService.getAccountModelObservableList());
		if (!transactionService.getAccountModelObservableList().isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
		else {accountComboBox.setPromptText("Account"); }
		
		saveButton = new Button("Save");
		saveButton.getStyleClass().addAll("button", "md3-rounded-medium");
		closeButton = new Button("Close");
		closeButton.getStyleClass().addAll("button", "md3-rounded-medium"); // Apply button styles and rounded corners
		
		
		
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
		clearFields();

	}
	
	public void clearFields() {
		transactionAmountField.clear();
		transactionDescriptionField.clear();
		transactionDatePicker.setValue(null);
		transactionTypeComboBox.getSelectionModel().clearSelection();
		accountComboBox.getSelectionModel().clearSelection();
	}
}
