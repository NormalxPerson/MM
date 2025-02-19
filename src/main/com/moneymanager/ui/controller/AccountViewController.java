package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.event.FormOpenedEvent;
import com.moneymanager.ui.view.AccountSlidingForm;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.SlidingForm;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
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
		
		accountTableView.setRowFactory(tv -> {
			TableRow<AccountTableView.AccountModel> row = new TableRow<>();
			
			row.setOnMouseClicked(event -> {
				if (accountSlidingForm.getFormStatus() == SlidingForm.FormStatus.ADDING) {
					// Prevent selection of any row while adding
					event.consume(); // Consume the event to stop further processing
					selectBlankRow();
					return;
				}
				if (!row.isEmpty() && event.getClickCount() == 1 ) {
					if (accountSlidingForm.getFormStatus().equals(SlidingForm.FormStatus.CLOSED)) {
						accountContainer.fireEvent(new FormOpenedEvent());
					}
					this.selectedAccountModel = row.getItem();
					// handle click
					System.out.println("Clicked row: " + selectedAccountModel);
					accountSlidingForm.showForm(SlidingForm.FormStatus.EDITING, selectedAccountModel);
				}
			});
			return row;
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
	public void selectBlankRow() {
		int row = accountTableView.getItems().size()-1;
		if (row >= 0) {
			accountTableView.getSelectionModel().select(row);
		}
	}
	
	@Override
	public void setFormForBlankModel() {
		accountSlidingForm.setUpForAddingModel();
	}
	
	@Override
	public void unselectRow() {
		accountTableView.getSelectionModel().clearSelection();
	}
	
	@Override
	public void showForm() {
		accountSlidingForm.setUpForAddingModel(); }
	
	@Override
	public void hideForm() { accountSlidingForm.hideForm(); formOpened = false; }
	
	@Override
	public void setFormStatus(boolean status) { formOpened = status; }
	
	public VBox getAccountContainer() { return this.accountContainer;}
	
	public void refreshAccountTable(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		accountTableView.populateAccountTable(accountModelObservableList);
		
	}
	
	
	
}

