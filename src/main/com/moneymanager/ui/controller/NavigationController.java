package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable {
	
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
			public void handle(ActionEvent event) {showTransactions();}
		});
	}
	
	
	
	public void showTransactions() {
		contentArea.getChildren().clear();
		contentArea.getChildren().add(transactionViewController.getTransactionContainer());
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
