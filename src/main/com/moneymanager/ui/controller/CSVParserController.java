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
		this.viewBuilder = new CSVParserViewBuilder(this.viewModel, this::handleFileSelected);
	}
	
	private void handleFileSelected(File selectedFile) {
		if (selectedFile != null) {
			viewModel.setFile(selectedFile);
			// Add any further processing of the file here or in the interactor
			System.out.println("File selected in Controller: " + selectedFile.getAbsolutePath());
		}
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
