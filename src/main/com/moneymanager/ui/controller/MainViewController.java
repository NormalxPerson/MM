package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
	
	@FXML
	private VBox navigationBar;
	
	@FXML
	private Button accountsButton;
	
	@FXML
	private Button transactionsButton;
	
	@FXML
	private HBox contentArea;
	
	private AccountTableView accountTableView;
	private TransactionTableView transactionTableView;

	private AccountService accountService;
	private TransactionService transactionService;
	
	@FXML
	private VBox accountFields;
	
	@FXML
	private VBox transactionFields;
	
	@FXML
	private TextField accountNameField;
	
	@FXML
	private TextField bankNameField;
	
	@FXML
	private ComboBox<String> accountTypeComboBox;
	
	@FXML
	private ComboBox<String> transactionTypeComboBox;
	
	@FXML
	private TextField transactionDescriptionField;
	
	@FXML
	private TextField transactionAmountField;
	
	@FXML
	private DatePicker transactionDatePicker;
	
	@FXML
	private ComboBox<AccountTableView.AccountModel> accountComboBox;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		transactionTableView = new TransactionTableView();
		accountTableView = new AccountTableView();
		HBox.setHgrow(accountTableView, Priority.ALWAYS);
		HBox.setHgrow(transactionTableView, Priority.ALWAYS);
		showAccounts();
		
		accountsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showAccounts();
			}
		});
		
		transactionsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showTransactions();
			}
		});
	}
	
	private void showAccounts() {
		contentArea.getChildren().clear();
		contentArea.getChildren().add(accountTableView);
		showAccountFields();
	}
	
	private void showTransactions() {
		contentArea.getChildren().clear();
		contentArea.getChildren().add(transactionTableView);
		showTransactionFields();
	}
	
	public void postInitialize() {
		
		if (accountService != null) {
			populateAccountTable();
		}
		if (transactionService != null) {
			populateTransactionTable();
		}
		transactionDatePicker.setValue(LocalDate.now());
		transactionTypeComboBox.getSelectionModel().select(1);
		accountTypeComboBox.getSelectionModel().select(0);
	}
	
	private void populateAccountTable() {
		ObservableList<AccountTableView.AccountModel> accountModels = accountService.getAccountModelObservableList();
		accountTableView.setItems(accountModels);
		accountComboBox.setItems(accountModels);
		accountComboBox.getSelectionModel().selectFirst();
		
	}
	
	private void populateTransactionTable() {
		transactionTableView.setItems(transactionService.getObservableTransactionModelsList());
		
	}
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@FXML
	private void handleAddAccount() {
		String accountName = accountNameField.getText();
		String bankName = bankNameField.getText();
		String accountType = accountTypeComboBox.getValue();
		
		if (accountName.isEmpty() || bankName.isEmpty() || accountType == null) {
			System.out.println("Please fill in all fields.");
			return;
		}
		
		try {
			accountService.createAccount(accountName, bankName, accountType);
//			populateAccountTable(); // Refresh the table
			accountNameField.clear();
			bankNameField.clear();
			accountTypeComboBox.getSelectionModel().clearSelection();
			System.out.println("Account added successfully.");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@FXML
	private void handleAddTransaction() {
		String description = transactionDescriptionField.getText();
		String amountText = transactionAmountField.getText();
		String transactionType = transactionTypeComboBox.getValue();
		String accountId = (accountComboBox.getValue() != null) ? accountComboBox.getValue().getAccountId() : null;
		DateTimeFormatter transactionDateFormat = DateTimeFormatter.ofPattern("M-d-yy");
		
		if (description.isEmpty() || amountText.isEmpty() || transactionType == null || accountId == null) {
			System.out.println("Please fill in all fields.");
			return;
		}
		
		try {
			// Parse the date
			String strDate;
			if (transactionDatePicker.getValue() != null) {
				// Use the date picker value if selected
				strDate = transactionDateFormat.format(transactionDatePicker.getValue());
			} else {
				// Parse manually entered text
				String manualDateText = transactionDatePicker.getEditor().getText();
				if (manualDateText.isEmpty()) {
					System.out.println("Please enter a valid date.");
					return;
				}
				// Validate and parse the manual input
				strDate = transactionDateFormat.format(LocalDate.parse(manualDateText, transactionDateFormat));
			}
			
			
			try {
				double amount = Double.parseDouble(amountText);
				System.out.println("handleAddTransaction(): strDate = " + strDate);
				transactionService.createTransactionFromUser(amount, description, strDate, transactionType, accountId);
//				populateTransactionTable();
//				populateAccountTable();// Refresh the tables
				transactionDescriptionField.clear();
				transactionAmountField.clear();
				transactionTypeComboBox.getSelectionModel().select(1);
				transactionDatePicker.setValue(LocalDate.now());
				System.out.println("Transaction added successfully.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid amount format.");
			}
		} catch (Exception e) {
			System.out.println("Error creating Transaction in handleAddTransaction()" + e.getMessage());
		}
	}
	
	@FXML
	private void showAccountFields() {
		// Show account fields, hide transaction fields
		accountFields.setVisible(true);
		accountFields.setManaged(true);
		transactionFields.setVisible(false);
		transactionFields.setManaged(false);
	}
	
	@FXML
	private void showTransactionFields() {
		// Show transaction fields, hide account fields
		transactionFields.setVisible(true);
		transactionFields.setManaged(true);
		accountFields.setVisible(false);
		accountFields.setManaged(false);
	}
	

}
