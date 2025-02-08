package com.moneymanager.ui.view;

import com.moneymanager.service.AccountService;
import com.moneymanager.ui.controller.NavigationController;
import com.moneymanager.ui.event.FormClosedEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AccountSlidingForm extends VBox {
	private TextField accountNameField;
	private TextField bankNameField;
	private ComboBox<String> accountTypeField;
	private Button saveButton;
	private Button closeButton;
	private AccountService accountService;

	public AccountSlidingForm(AccountService accountService) {
		this.accountService = accountService;
		this.setSpacing(10);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		initializeAccountForm();
		this.getStyleClass().add("sliding-form");
		
		HBox buttonBox = new HBox(10, closeButton, saveButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		this.getChildren().addAll(accountNameField, bankNameField, accountTypeField, buttonBox);
		
		this.setVisible(false);
		this.setManaged(false);

	}

	private void initializeAccountForm() {
		accountNameField = new TextField("Account Name");
		accountNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		bankNameField = new TextField("Bank Name");
		bankNameField.getStyleClass().addAll("text-field", "md3-rounded-small"); // Apply text-field styles and rounded corners
		
		accountTypeField = new ComboBox<>();
		accountTypeField.getItems().addAll("Debit", "Credit");
		accountTypeField.getSelectionModel().selectFirst();
		accountTypeField.getStyleClass().add("md3-rounded-small"); // Example: rounded corners for ComboBox - adjust if needed
		
		saveButton = new Button("Save");
		saveButton.getStyleClass().addAll("button", "md3-rounded-medium"); // Apply button styles and rounded corners
		
		closeButton = new Button("Close");
		closeButton.getStyleClass().addAll("button", "md3-rounded-medium"); // Apply button styles and rounded corners
		closeButton.setOnAction(event -> {
			hideForm();
			Event.fireEvent(this, new FormClosedEvent());
		});
	}
	
	public void setUpFields() {
		accountNameField.setPromptText("Account Name");
		bankNameField.setPromptText("Bank Name");
		accountTypeField.getSelectionModel().selectFirst();
	}
	
	public void clearFields() {
		accountNameField.clear();
		bankNameField.clear();
	}
	
	public void showForm() {
		setVisible(true);
		setManaged(true);
		setUpFields();
	}
	
	public void hideForm() {
		setVisible(false);
		setManaged(false);
		clearFields();
	}


	
}


