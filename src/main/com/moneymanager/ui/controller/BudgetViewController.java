package com.moneymanager.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetViewController implements Initializable, BaseViewController{
	
	@FXML
	private VBox budgetCardContainer;
	
	public BudgetViewController() {
	
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	}
	
	public VBox getBudgetContainer() { return budgetCardContainer; }
	
	
	@Override
	public void hideForm() {
	
	}
	
	@Override
	public void unselectRow() {
	
	}
	
	@Override
	public void showCreationDialog() {
	
	}
}
