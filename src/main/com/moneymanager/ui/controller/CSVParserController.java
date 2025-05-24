package com.moneymanager.ui.controller;

import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.interactor.CSVParserInteractor;
import com.moneymanager.ui.viewModel.CSVParserViewModel;
import com.moneymanager.ui.view.CSVParserViewBuilder;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CSVParserController implements BaseViewController{
	private final CSVParserViewModel viewModel;
	private final CSVParserInteractor interactor;
	private CSVParserViewBuilder viewBuilder;
	
	
	public CSVParserController(TransactionService transactionService) {
		this.viewModel = new CSVParserViewModel(transactionService.getAccountModelObservableList());
		this.interactor = new CSVParserInteractor(transactionService, viewModel);
		this.viewBuilder = new CSVParserViewBuilder(this.viewModel, fileChooserButton -> handleFileSelection(fileChooserButton));
	}
	
	public Region getView() {
		return viewBuilder.build();
	}
	
	public void handleFileSelection(Button fileChooserButton) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select CSV file");
		
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
		
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		Stage stage = (Stage) fileChooserButton.getScene().getWindow();
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		if (selectedFile != null) {
			viewModel.setFile(selectedFile);
		}
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
