package com.moneymanager.ui.viewModel;

import com.moneymanager.csv.CsvParser;
import com.moneymanager.ui.view.AccountTableView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.io.File;

public class CSVParserViewModel {
	private final ObservableList<AccountTableView.AccountModel> accountModelObservableList;
	private ObjectProperty<AccountTableView.AccountModel> selectedAccountModel = new SimpleObjectProperty<>();
	private ObjectProperty<File> file = new SimpleObjectProperty<>();
	private ObjectProperty<CsvParser.CsvData> csvData = new SimpleObjectProperty<>();
	
	private BooleanProperty hasHeaders = new SimpleBooleanProperty(true);

	public CSVParserViewModel(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		this.accountModelObservableList = accountModelObservableList;
	}
	
	public AccountTableView.AccountModel getSelectedAccountModel() { return selectedAccountModel.get(); }
	public ObjectProperty<AccountTableView.AccountModel> selectedAccountModelProperty() { return selectedAccountModel; }
	
	public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() { return accountModelObservableList; }

	public void setFile (File file) { this.file.set(file); }
	public File getFile() { return file.get(); }
	public ObjectProperty<File> fileProperty() { return file; }
	
	public CsvParser.CsvData getCsvData() { return csvData.get(); }
	public void setCsvData (CsvParser.CsvData csvData) { this.csvData.set(csvData); }
	public ObjectProperty<CsvParser.CsvData> csvDataProperty() { return csvData; }
	
	public boolean getHasHeaders() { return hasHeaders.get(); }
	public BooleanProperty hasHeadersProperty() { return hasHeaders; }
	public void setHasHeaders (boolean hasHeaders) { this.hasHeaders.set(hasHeaders); }
	
}


