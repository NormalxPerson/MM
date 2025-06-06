package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.event.FormEvent;
import com.moneymanager.ui.view.FloatingActionButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable{
	
	@FXML
	private StackPane stackPane;
	
	@FXML
	private FloatingActionButton fab;
	
	@FXML
	private ToggleButton accountsButton;
	
	@FXML
	private ToggleButton transactionsButton;
	
	@FXML
	private ToggleButton budgetButton;
	
	@FXML
	private ToggleButton csvParserButton;
	
	@FXML
	private ToggleGroup navigationGroup;
	
	@FXML
	private HBox contentArea;
	
	private AccountService accountService;
	private TransactionService transactionService;
	private BudgetService budgetService;
	
	private AccountViewController accountViewController;
	private TransactionViewController transactionViewController;
	
	private ViewManager viewManager;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ViewManager.initialize(contentArea);
		this.viewManager = ViewManager.getInstance();
		this.fab = FloatingActionButton.getInstance();
		
		BorderPane.setMargin(contentArea, new Insets(5));
		HBox.setHgrow(contentArea, Priority.ALWAYS);
		//Adding Event Handler to Content Area.
		
		contentArea.addEventHandler(FormEvent.DELETEACCOUNT, this::handleDeleteAccountEvent);
		
		fab.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleFabAction(event);
			}
		});
		
		
		// Listen for navigation changes. When a ToggleButton is selected,
		// retrieve its userData (the view name) and switch to that view.
		navigationGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
			// First, if there was a previously selected toggle, get its view name
			if (newToggle == null) {
				oldToggle.setSelected(true);
			}
			if (oldToggle != null && viewManager.getController() != null) {
				viewManager.getController().hideForm();
			}
			
			if (newToggle != null) {
				String viewName = newToggle.getUserData().toString();
				// Switch the view using the ViewManager.
				viewManager.switchTo(viewName);
				HBox.setHgrow(contentArea, Priority.ALWAYS);
			}
			// Optionally, show the FAB when switching views.
		});

		// FAB set up
		StackPane.setAlignment(fab, Pos.BOTTOM_RIGHT);
		fab.setTranslateX(-20); // Move 20 pixels to the left
		fab.setTranslateY(-20); // Move 20 pixels upwards
		stackPane.getChildren().add(fab);
	}
	
	private void handleFabAction(ActionEvent event) {
		// Use the currently selected view name to determine which view's controller to notify.
		if (navigationGroup.getSelectedToggle() != null && viewManager.getController() != null) {
			viewManager.getController().hideForm();
			viewManager.getController().showCreationDialog();
		}
	}
	
	private void handleDeleteAccountEvent(FormEvent event) {
		if (event.getEventType() == FormEvent.DELETEACCOUNT) {
			System.out.println("NavigationController.handleDeleteAccountEvent got event");
			transactionViewController = (TransactionViewController) viewManager.getControllerByName("transactionView");
			transactionViewController.refreshTransactionTable(transactionService.getObservableTransactionModelsList());
		}
	}
	
	public void setUpControllers() {
		
		// Create or load the AccountViewController via FXML. So I can get container from controller and not fxml
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/accountView.fxml"));
		Parent accountView = null;
		try {
			accountView = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AccountViewController accountsController = loader.getController();
		accountsController.setAccountService(accountService);
		accountsController.setFloatingActionButton(this.fab);
		this.viewManager.registerView(accountsButton.getUserData().toString(), accountsController.getAccountContainer(), accountsController);
		
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/transactionView.fxml"));
		Parent transView = null;
		try {
			transView = loader2.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TransactionViewController transactionsController = loader2.getController();
		transactionsController.setTransactionService(transactionService);
		transactionsController.setFloatingActionButton(this.fab);
		this.viewManager.registerView(transactionsButton.getUserData().toString(), transactionsController.getTransactionContainer(), transactionsController);
		
		setUpBudgetController();
		setUpCsvParserController();
		accountsButton.setSelected(true);
	}
	
	public void setUpBudgetController() {
		try {
			BudgetOverviewController budController = new BudgetOverviewController(budgetService, transactionService);
			
			Region budgetView = budController.getView();
			String budgetViewName = budgetButton.getUserData().toString();
			this.viewManager.registerView(budgetViewName, budgetView, budController);
		} catch (Exception e) { System.err.println("failed to load BudgetOverviewController: " + e.getMessage() ); }
	}
	
	public void setUpCsvParserController() {
		try {
			CSVParserController csvParserController = new CSVParserController(transactionService);
			Region csvParserView = csvParserController.getView();
			String csvParserViewName = csvParserButton.getUserData().toString();
			
			this.viewManager.registerView(csvParserViewName, csvParserView, csvParserController);
		} catch (Exception e) { System.err.println("failed to load CSVParserController: " + e.getMessage() ); }
	}

	
	
	
	public void setAccountService(AccountService accountService) { this.accountService = accountService;}
	
	public void setTransactionService(TransactionService transactionService) { this.transactionService = transactionService;}
	
	public void setBudgetService(BudgetService budgetService) { this.budgetService = budgetService; }
	
}