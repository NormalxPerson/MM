package com.moneymanager.ui.controller;

import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
	private static ViewManager instance;
	private final HBox mainContainer;
	private BaseViewController currentViewController;
	// Cache views by name for performance
	private final Map<String, Parent> viewCache = new HashMap<>();
	private final Map<String, BaseViewController> controllerCache = new HashMap<>();
	
	private AccountService accountService;
	private TransactionService transactionService;
	
	private ViewManager(HBox mainContainer) {
		this.mainContainer = mainContainer;
	}
	
	public static void initialize(HBox mainContainer) {
		if (instance == null) {
			instance = new ViewManager(mainContainer);
		}
	}
	
	public static ViewManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException("ViewManager is not initialized!");
		}
		return instance;
	}
	
	// Registers a view and its controller manually.
	public void registerView(String viewName, Parent view, BaseViewController controller) {
		viewCache.put(viewName, view);
		controllerCache.put(viewName, controller);
	}
	
	public Parent getView(String viewName) {
		if (!viewCache.containsKey(viewName)) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + viewName + ".fxml"));
				Parent view = loader.load();
				viewCache.put(viewName, view);
				controllerCache.put(viewName, loader.getController());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		currentViewController = controllerCache.get(viewName);
		return viewCache.get(viewName);
	}
	
	public void switchTo(String viewName) {
		Parent view = getView(viewName);
		if (view != null) {
			mainContainer.getChildren().setAll(view);
			HBox.setHgrow(view, Priority.ALWAYS);
		}
	}
	
	
	
	public BaseViewController getController() {
		return currentViewController;
	}
	
	public BaseViewController getControllerByName(String viewName) {
		if (controllerCache.containsKey(viewName)) {
			return controllerCache.get(viewName);
		}
		return null;
	}

}


