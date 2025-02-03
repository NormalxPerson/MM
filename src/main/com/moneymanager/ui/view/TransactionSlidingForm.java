package com.moneymanager.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private ObservableList<AccountTableView.AccountModel> accountModels;
	
	public TransactionSlidingForm(ObservableList<AccountTableView.AccountModel> accounts) {
		this.accountModels = FXCollections.observableArrayList(accounts);
		this.setSpacing(10);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		initializeTransactionForm();
		this.getStyleClass().add("sliding-form");
		
		HBox hBox = new HBox(10, closeButton, saveButton);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		
		this.getChildren().addAll(transactionAmountField, transactionDescriptionField, transactionDatePicker, transactionTypeComboBox, accountComboBox);
		
		this.setVisible(false);
		this.setManaged(false);
		
		closeButton.setOnAction(e -> hideForm());
	}
	
	private void initializeTransactionForm() {
		transactionAmountField = new TextField("Amount");
		transactionDescriptionField = new TextField("Description");
		transactionDatePicker = new DatePicker(LocalDate.now());
		
		transactionTypeComboBox = new ComboBox<>();
		transactionTypeComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
		transactionTypeComboBox.setValue("EXPENSE");
		
		accountComboBox = new ComboBox<>();
		accountComboBox.setItems(accountModels);
		if (!accountModels.isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
		else {accountComboBox.setPromptText("Account"); }
		
		saveButton = new Button("Save");
		closeButton = new Button("Close");
		
	
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
		if (!accountModels.isEmpty()) { accountComboBox.getSelectionModel().selectFirst(); }
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
