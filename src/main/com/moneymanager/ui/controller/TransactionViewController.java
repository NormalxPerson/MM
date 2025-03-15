package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.TransactionForm;
import com.moneymanager.ui.view.TransactionTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionViewController extends AbstractViewController implements Initializable, BaseViewController {
	
	@FXML
	private VBox transactionContainer;
	
	private TransactionTableView transactionTableView;
	private TransactionForm transactionSlidingForm;
	private TransactionService transactionService;
	
	private TransactionTableView.TransactionModel selectedTransactionModel;
	
	
	public TransactionViewController() {
		this.transactionTableView = new TransactionTableView();
		this.tableView = this.transactionTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.container = transactionContainer;
	}
	
	public void postInitialize() {
		refreshTransactionTable(transactionService.getObservableTransactionModelsList());
		transactionSlidingForm = new TransactionForm(transactionService, floatingActionButton);
		this.editingForm = this.transactionSlidingForm;
		transactionContainer.getChildren().addAll(transactionTableView, transactionSlidingForm);
		VBox.setVgrow(transactionTableView, Priority.ALWAYS);
		setupRowSelection();
	}
	
	@Override
	public void showCreationDialog() {
		System.out.println("showCreationDialog");
	}
	
	@Override
	protected <T> void handleSaveEvent(FormEvent<T> formSaveEvent) {
	
	}
	
	@Override
	protected <T> void handleDeleteEvent(FormEvent<T> formDeleteEvent) {
	
	}
	
	@Override
	protected <T> void handleCloseEvent(FormEvent<T> formCloseEvent) {
	
	}
	
	
	public void refreshTransactionTable(ObservableList<TransactionTableView.TransactionModel> transactionModels) {
		transactionTableView.populateTransactionTable(transactionModels);
	}
	
	public VBox getTransactionContainer() { return this.transactionContainer; }
	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
		postInitialize();
	}
	
	@Override
	protected void handleRowClick(TableRow<?> row, MouseEvent event) { // Implement abstract method
		if (!row.isEmpty() && event.getClickCount() == 1 ) {
			System.out.println("Clicked: " + row.getItem());
			transactionSlidingForm.setCurrentModel( (TransactionTableView.TransactionModel) row.getItem()); // Cast to AccountModel
		}
	}
}


