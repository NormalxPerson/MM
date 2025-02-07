package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.view.TransactionSlidingForm;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox transactionContainer;
	
	private TransactionTableView transactionTableView;
	private TransactionSlidingForm transactionSlidingForm;
	private TransactionService transactionService;
	
	
	public TransactionViewController() {
		this.transactionTableView = new TransactionTableView();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void postInitialize() {
		refreshTransactionTable(transactionService.getObservableTransactionModelsList());
		transactionSlidingForm = new TransactionSlidingForm(transactionService);
		transactionContainer.getChildren().addAll(transactionTableView, transactionSlidingForm);
		VBox.setVgrow(transactionTableView, Priority.ALWAYS);
	}
	
	@Override
	public void showForm() { transactionSlidingForm.showForm();}
	
	@Override
	public void hideForm() {
		transactionSlidingForm.hideForm();
	}
	
	public void refreshTransactionTable(ObservableList<TransactionTableView.TransactionModel> transactionModels) {
		transactionTableView.populateTransactionTable(transactionModels);
	}
	
	public VBox getTransactionContainer() { return this.transactionContainer; }
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
		postInitialize();
	}
}


