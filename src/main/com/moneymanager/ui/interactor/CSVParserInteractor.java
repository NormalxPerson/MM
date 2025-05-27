package com.moneymanager.ui.interactor;

import com.moneymanager.csv.CsvParser;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.viewModel.CSVParserViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVParserInteractor {
	private final TransactionService transactionService;
	private final CSVParserViewModel viewModel;
	
	public CSVParserInteractor(TransactionService transactionService, CSVParserViewModel viewModel) {
		this.transactionService = transactionService;
		this.viewModel = viewModel;
		setupFileListener();
		
	}
	
	private void setupFileListener() {
		viewModel.fileProperty().addListener((observable, oldFile, newFile) -> {
			if (newFile != null && newFile.exists() && newFile.getName().endsWith(".csv")) {
				System.out.println("File selected, auto-parsing: " + newFile.getName());
				try {
					parseCsvFile(newFile);
				} catch (RuntimeException e) {
					System.out.println("Error parsing file: listener Interactor" + newFile.getName());
				}
			} else if (newFile == null) {
				System.out.println("File selection cleared");
				viewModel.setCsvData(null);
			}
		});
	}
	
	public void parseCsvFile(File csvFile) {
		try {
			System.out.println("Parsing CSV file: " + csvFile.getName());
			CsvParser.CsvData csvData = CsvParser.parseCsvFile(csvFile, true);
			
			viewModel.setCsvData(csvData);
			
			System.out.println("Columns:");
			for (String column : csvData.getHeaders()) {
				System.out.println("\t"+column);
			}
			System.out.println(csvData.getRows().get(1));
			System.out.println("CSV parsed successfully: " + csvData.getRowCount() + " rows, " + csvData.getColumnCount() + " columns");
			
		} catch (IOException e) {
			System.err.println("Failed to parse CSV: " + e.getMessage());
			e.printStackTrace();
			
			// Clear data and show error
			viewModel.setCsvData(null);
			throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
		} catch (Exception e) {
			System.err.println("Unexpected error parsing CSV: " + e.getMessage());
			e.printStackTrace();
			
			viewModel.setCsvData(null);
			throw new RuntimeException("Unexpected error while parsing CSV file", e);
		}
	}
	
	public Map<Integer, String> getCsvHeaders() {
		return CsvParser.getHeaderMapFromFile(viewModel.getFile());
	}
	
}
	

