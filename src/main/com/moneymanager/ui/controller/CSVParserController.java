package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.interactor.CSVParserInteractor;
import com.moneymanager.ui.viewModel.CSVParserViewModel;
import com.moneymanager.ui.view.CSVParserViewBuilder;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CSVParserController implements BaseViewController{
	private final CSVParserViewModel viewModel;
	private final CSVParserInteractor interactor;
	private CSVParserViewBuilder viewBuilder;
	
	
	public CSVParserController(TransactionService transactionService) {
		this.viewModel = new CSVParserViewModel(transactionService.getAccountModelObservableList());
		this.interactor = new CSVParserInteractor(transactionService, viewModel);
		this.viewBuilder = new CSVParserViewBuilder(this.viewModel, this::handleFileSelected);
	}
	
	private void handleFileSelected(File selectedFile) {
		if (selectedFile != null) {
			if (selectedFile.getName().endsWith(".csv")) {
				viewModel.setFile(selectedFile);
				openConfigTransactionDialog(interactor.getCsvHeaders());
			}
			
			// Add any further processing of the file here or in the interactor
			System.out.println("File selected in Controller: " + selectedFile.getAbsolutePath());
		}
	}
	
	private void openConfigTransactionDialog(Map<Integer, String> headerMap) {
		ObservableList<String> headersObservableList = FXCollections.observableArrayList(new HashSet<>(headerMap.values()));
		
		ListView<String> headersListView = new ListView<>(headersObservableList);
		headersListView.setOrientation(Orientation.HORIZONTAL);
		headersListView.setPrefHeight(70);
		
		
		
		Label headersListLabel = new Label("Available CSV Headers:");
		
		VBox listBox = new VBox(headersListLabel,headersListView);
		HBox.setHgrow(listBox, Priority.ALWAYS);
		
		listBox.setStyle("-fx-background-color: #E7E0EC; -fx-border-color: yellow; -fx-border-width: 2;");
		
		ComboBox<String> dateComboBox = new ComboBox<>(headersObservableList);
		ComboBox<String> descriptionComboBox = new ComboBox<>(headersObservableList);
		ComboBox<String> incomeComboBox = new ComboBox<>(headersObservableList);
		ComboBox<String> expenseComboBox = new ComboBox<>(headersObservableList);
		
		
		// Create the dialog layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		
		// Add labels and combo boxes
		grid.add(new Label("Date Column:"), 0, 0);
		grid.add(dateComboBox, 1, 0);
		
		grid.add(new Label("Description Column:"), 0, 1);
		grid.add(descriptionComboBox, 1, 1);
		
		grid.add(new Label("Income/Credit Column:"), 0, 2);
		grid.add(incomeComboBox, 1, 2);
		
		grid.add(new Label("Expense/Debit Column:"), 0, 3);
		grid.add(expenseComboBox, 1, 3);
		
		// Create info label
		Label infoLabel = new Label("Note: Income and Expense can use the same column if amounts are signed (+/-)");
		infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666666;");
		infoLabel.setWrapText(true);
		
		grid.add(infoLabel, 0, 4, 2, 1);
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: #E7E0EC; -fx-border-color: blue; -fx-border-width: 2;");
		
		
		VBox content = new VBox(listBox, grid);
		content.setStyle("-fx-background-color: #E7E0EC; -fx-border-color: red; -fx-border-width: 2;");
		HBox.setHgrow(content, Priority.ALWAYS);
		content.setFillWidth(content.isFillWidth());
		
		Dialog<List<String>> choiceDialog = new Dialog<>();
		choiceDialog.setResizable(true);
		
		DialogPane dialogPane = choiceDialog.getDialogPane();
		dialogPane.setContent(content);
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		
		
		
		
		
		
		
		choiceDialog.showAndWait();
	}
	
	public Region getView() {
		return viewBuilder.build();
	}
	

	
	
	
	
	
	
	
	
	
	@Override
	public void hideForm() {
	
	}
	@Override
	public void unselectRow() {
	
	}
	@Override
	public void showCreationDialog() {
	
	}
	@Override
	public void refreshView() {
	
	}
}
