package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.view.AccountSlidingForm;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox accountContainer;
	
	private AccountTableView accountTableView;
	private AccountSlidingForm accountSlidingForm;
	private AccountService accountService;
	
	public AccountViewController() {
		this.accountTableView = new AccountTableView();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
	public void showForm() { accountSlidingForm.showForm();}
	
	@Override
	public void hideForm() { accountSlidingForm.hideForm();}
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
	}
	
	
}

