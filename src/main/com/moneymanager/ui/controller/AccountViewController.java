package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.AccountForm;
import com.moneymanager.ui.view.AccountTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
			
			accountSlidingForm.setCurrentModel(selectedModel);
		}
	}
	
	@Override
	public void refreshView() {
	
	}
	
	@Override
	protected <T> void handleNewSaveEvent(FormEvent<T> formNewSaveEvent) {
		if (formNewSaveEvent.getEventType() == FormEvent.NEWSAVE) {
			Map<String, Object> fieldValues = formNewSaveEvent.getFieldValues();
			
			try {
				// Extract values from the fields
				String accountName = (String) fieldValues.get("accountName");
				String bankName = (String) fieldValues.get("bankName");
				AccountTableView.AccountModel.AccountType type = (AccountTableView.AccountModel.AccountType) fieldValues.get("accountType");
				
				
				double accountBalance = 0.0;
				try {
					String balanceStr = (String) fieldValues.get("accountBalance");
					if (balanceStr != null && !balanceStr.isEmpty()) {
						accountBalance = Double.parseDouble(balanceStr);
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid balance format: " + fieldValues.get("accountBalance"));
					return;
				}
				System.out.printf("Account Name: %s\nBank Name: %s\nAccount Type: %s\nAccount Balance: %s\n", accountName, bankName, type.getDisplayName(), accountBalance);

				AccountTableView.AccountModel newAccountModel = accountService.createAndAddAccount(accountName, bankName, type.getDisplayName(), accountBalance);
				accountTableView.getSelectionModel().select(newAccountModel);
				accountSlidingForm.setCurrentModel(newAccountModel);
			} catch (Exception e) {
				System.out.println("Error creating new account " + e.getMessage());
			}
		}
	}
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
		
	}
	
	@Override
	protected void handleSaveEvent(FormEvent event) {
		if (event.getEventType() == FormEvent.SAVE) {
			FormEvent<AccountTableView.AccountModel> accountFormEvent = (FormEvent<AccountTableView.AccountModel>) event;
			AccountTableView.AccountModel model = (AccountTableView.AccountModel) event.getModel();
			Map<String, Object> changedValues = event.getFieldValues();
			
			for (String key : changedValues.keySet()) {
				model.makeChanges(key,changedValues.get(key));
			}
			
			try {
				if (model == null) {
					System.out.println("model is null in AccountViewController.handleSaveEvent");
				}
					for (String fieldName : changedValues.keySet()) {
						Control field = accountSlidingForm.getField(fieldName);
						if (field != null) {
							field.getStyleClass().remove("error-border");
							field.getStyleClass().add("success-border");
							
							Timeline timeline = new Timeline(
									new KeyFrame(Duration.seconds(3), e -> {
										field.getStyleClass().remove("success-border");
									})
							);
							timeline.play();
						}
					}
			
					accountService.updateAccount(model);
					System.out.println("From Controller, Account updated successfully");
					System.out.println("From Controller, Account name: " + model.getAccountName()
					+ " BankName: " + model.getBankName() + " AccountType: " + model.getAccountType() + " AccountBalance: " + model.getAccountBalance()
					);
				
				
				// Refresh the table with updated data
				refreshAccountTable(accountService.getAccountModelObservableList());
				
			} catch (Exception e) {
				System.out.println("Error saving account: " + e.getMessage());
				System.out.printf("");
			}
		}
	}
	
	@Override
	protected void handleDeleteEvent(FormEvent formDeleteEvent) {
		AccountTableView.AccountModel selectedModel = (AccountTableView.AccountModel) formDeleteEvent.getModel();
		int rowDeleted = accountService.deleteAccountById(selectedModel);
		if (rowDeleted == 0) {
			Alert deleteErrorDialog = new Alert(Alert.AlertType.ERROR);
			deleteErrorDialog.setTitle("Error Deleting Account");
			deleteErrorDialog.setHeaderText("I am not sure why but something went wrong!");
			deleteErrorDialog.setContentText("Something went wrong!");
			deleteErrorDialog.showAndWait();
		} else if (rowDeleted == 1) {
			Alert deleteSuccessDialog = new Alert(Alert.AlertType.INFORMATION);
			deleteSuccessDialog.setTitle("Account Deleted!");
			deleteSuccessDialog.setHeaderText("Account Successfully Deleted!");
			deleteSuccessDialog.setContentText("Sheee Gonee!");
			deleteSuccessDialog.showAndWait();
			
			FormEvent<FormEvent> deleteAccountFormEvent = new FormEvent<>(FormEvent.DELETEACCOUNT);
			accountContainer.fireEvent(deleteAccountFormEvent);
			accountSlidingForm.fireCloseEvent();
		}
	}
	
	@Override
	protected void handleCloseEvent(FormEvent formCloseEvent) {
		tableView.getSelectionModel().clearSelection();
	}
	
	
}

