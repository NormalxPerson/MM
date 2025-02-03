package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable {
	
	@FXML
	private Button fab;
	
	@FXML
	private ToggleButton accountsButton;
	
	@FXML
	private ToggleButton transactionsButton;
	
	@FXML
	private HBox contentArea;
	
	private AccountService accountService;
	private TransactionService transactionService;
	
	private AccountViewController accountViewController;
	private TransactionViewController transactionViewController;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.accountViewController = new AccountViewController();
		this.transactionViewController = new TransactionViewController();
		
		accountsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {showAccounts();}
		});
		
		transactionsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {showTransactions();}
		});
		
		fab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleFabAction(event);
			}
		});
	}
	
	@FXML
	private void handleFabAction(ActionEvent event) {
		if (accountsButton.isSelected()) {
			//accountViewController.showAccountForm();
		} else if (transactionsButton.isSelected()) {
			transactionViewController.showTransactionForm();
		}
	}
	
	
	public void showTransactions() {
		Parent transactionContainer = transactionViewController.getTransactionContainer();
		contentArea.getChildren().clear();
		contentArea.getChildren().add(transactionContainer);
		HBox.setHgrow(transactionContainer, Priority.ALWAYS);
	}
	
	public void showAccounts() {
		contentArea.getChildren().clear();
	//	contentArea.getChildren().add(accountViewController.getAccountContainer());
	}
	public void setUpControllers() {
		
		
		transactionViewController.setTransactionService(transactionService);
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
}
