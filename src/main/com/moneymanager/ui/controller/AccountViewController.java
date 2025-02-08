package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.FormOpenedEvent;
import com.moneymanager.ui.view.AccountSlidingForm;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox accountContainer;
	private boolean formOpened = false;
	
	private AccountTableView accountTableView;
	private AccountSlidingForm accountSlidingForm;
	private AccountService accountService;
	
	private AccountTableView.AccountModel selectedAccountModel;
	
	
	public AccountViewController() {
		this.accountTableView = new AccountTableView();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		accountTableView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1 && !formOpened) {
				showForm();
				accountContainer.fireEvent(new FormOpenedEvent());
			}
			
			if (event.getClickCount() == 1 && formOpened) {

				selectedAccountModel = accountTableView.getSelectionModel().getSelectedItem();
				if (selectedAccountModel != null) {
					accountSlidingForm.getAccountNameField().setText(selectedAccountModel.getAccountName());
					accountSlidingForm.getBankNameField().setText(selectedAccountModel.getBankName());
					if (accountSlidingForm.getAccountTypeField().getItems().contains(selectedAccountModel.getAccountType())) {
						accountSlidingForm.getAccountTypeField().setValue(selectedAccountModel.getAccountType());
					}
				}
			}
		});
		
		
	}
	
	public void postInitialize() {
		refreshAccountTable(accountService.getAccountModelObservableList());
		accountSlidingForm = new AccountSlidingForm(accountService);
		accountContainer.getChildren().addAll(accountTableView, accountSlidingForm);
		VBox.setVgrow(accountTableView, Priority.ALWAYS);
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
		postInitialize();
	}
	
	@Override
	public void showForm() { accountSlidingForm.showForm(); formOpened = true; }
	
	@Override
	public void hideForm() { accountSlidingForm.hideForm(); formOpened = false; }
	
	@Override
	public void setFormStatus(boolean status) { formOpened = status; }
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
		
	}
	
	
	
}

