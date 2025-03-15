package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.AccountForm;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AccountViewController extends AbstractViewController {
	
	@FXML
	private VBox accountContainer;
	
	private AccountTableView accountTableView;
	private AccountForm accountSlidingForm;
	private AccountForm accountCreationForm;
	private AccountService accountService;
	
	
	public AccountViewController() {
		this.accountTableView = new AccountTableView();
		this.tableView = this.accountTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container = accountContainer;
	}
	
	public void postInitialize() {
		
		refreshAccountTable(accountService.getAccountModelObservableList());
		
		
		accountSlidingForm = new AccountForm();
		this.editingForm = accountSlidingForm;
		
		this.accountCreationForm = new AccountForm();
		this.creationDialogForm = accountCreationForm;
		
		
		accountContainer.getChildren().addAll(accountTableView, accountSlidingForm);
		VBox.setVgrow(accountTableView, Priority.ALWAYS);
		setupRowSelection();
		editingForm.addEventHandler(FormEvent.SAVE, this::handleSaveEvent);
		editingForm.addEventHandler(FormEvent.DELETE, this::handleDeleteEvent);
		editingForm.addEventHandler(FormEvent.CLOSE, this::handleCloseEvent);
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
		postInitialize();
	}
	

	@Override
	protected void handleRowClick(TableRow<?> row, MouseEvent event) { // Implement abstract method
		if (!row.isEmpty() && event.getClickCount() == 1 ) {
			AccountTableView.AccountModel selectedModel = (AccountTableView.AccountModel) row.getItem();
			System.out.println("Clicked: " + selectedModel);
			
			// Show and load data without worrying about re-adding the form
			accountSlidingForm.setCurrentModel(selectedModel);
			accountSlidingForm.setVisible(true);
			accountSlidingForm.setManaged(true);
		}
	}
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
		
	}
	
	@Override
	protected void handleSaveEvent(FormEvent event) {
		if (event.getEventType() == FormEvent.SAVE) {
			AccountTableView.AccountModel model = (AccountTableView.AccountModel) event.getModel();
			Map<String, Object> changedValues = event.getFieldValues();
			
			try {
				// Extract changedValues
				String accountName = (String) changedValues.get("accountName");
				System.out.println("Before Account Name: " + accountName);
				String bankName = (String) changedValues.get("bankName");
				String accountType = (String) changedValues.get("accountType");
				double accountBalance = 0.0;
				try {
					String balanceStr = (String) changedValues.get("accountBalance");
					if (balanceStr != null && !balanceStr.isEmpty()) {
						accountBalance = Double.parseDouble(balanceStr);
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid balance format: " + changedValues.get("accountBalance"));
					return;
				}
				
				
				// Determine if this is a new account or an existing one
				if (model == null) {
					// Create new account
					accountService.createAddAndGetNewAccountModel(accountName, bankName, accountType, accountBalance);
					System.out.println("Account created successfully");
				} else {
					// Update existing account
					if (!model.getAccountName().equals(accountName)) {
						model.setAccountName(accountName);
						System.out.println("Account name updated successfully");
					}
					if (!model.getBankName().equals(bankName)) {
						model.setBankName(bankName);
						System.out.println("Bank name updated successfully");
					}
					if (!model.getAccountType().equals(accountType)) {
						model.setAccountType(accountType);
						System.out.println("Account type updated successfully");
					}
					if (!model.getAccountBalance().equals(accountBalance)) {
						model.setAccountBalance(accountBalance);
						System.out.println("Account balance updated successfully");
					}
					
					for (String fieldName : changedValues.keySet()) {
						Control field = accountSlidingForm.getField(fieldName);
						if (field != null) {
							field.getStyleClass().remove("error-border");
							field.getStyleClass().add("success-border");
						}
					}
					accountService.updateAccount(model);
					System.out.println("Account updated successfully");
					System.out.println("Account name: " + model.getAccountName()
					+ " BankName: " + model.getBankName() + " AccountType: " + model.getAccountType() + " AccountBalance: " + model.getAccountBalance()
					);
				}
				
				// Refresh the table with updated data
				refreshAccountTable(accountService.getAccountModelObservableList());
				
				// Hide the form
				//hideForm();
				
				// Clear table selection
				//unselectRow();
				
			} catch (Exception e) {
				System.out.println("Error saving account: " + e.getMessage());
			}
		}
	}
	
	@Override
	protected void handleDeleteEvent(FormEvent formDeleteEvent) {
	
	}
	
	@Override
	protected void handleCloseEvent(FormEvent formCloseEvent) {
	
	}
	
	
}

