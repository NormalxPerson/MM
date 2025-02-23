package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormOpenedEvent;
import com.moneymanager.ui.view.AbstractSlidingForm;
import com.moneymanager.ui.view.TransactionSlidingForm;
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
	private TransactionSlidingForm transactionSlidingForm;
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
		transactionSlidingForm = new TransactionSlidingForm(transactionService);
		this.slidingForm = this.transactionSlidingForm;
		transactionContainer.getChildren().addAll(transactionTableView, transactionSlidingForm);
		VBox.setVgrow(transactionTableView, Priority.ALWAYS);
		setupRowSelection();
	}
	
	@Override
	public void setFormForBlankModel() {
		transactionSlidingForm.onAddAction();
	}
	
	@Override
	protected void handleRowClick(TableRow<?> row, MouseEvent event) {
		if (!row.isEmpty() && event.getClickCount() == 1 ) {
			if (slidingForm.getFormStatus().equals(AbstractSlidingForm.FormStatus.CLOSED)) {
				transactionContainer.fireEvent(new FormOpenedEvent());
			}
			this.selectedTransactionModel = (TransactionTableView.TransactionModel) row.getItem(); // Cast to TransactionModel
			// handle click
			System.out.println("Clicked row: " + selectedTransactionModel);
			transactionSlidingForm.showForm(AbstractSlidingForm.FormStatus.EDITING, selectedTransactionModel);
		}
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


