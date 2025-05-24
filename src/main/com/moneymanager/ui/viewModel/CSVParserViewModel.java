package com.moneymanager.ui.viewModel;

import com.moneymanager.ui.view.AccountTableView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.io.File;

public class CSVParserViewModel {
	private final ObservableList<AccountTableView.AccountModel> accountModelObservableList;
	private ObjectProperty<AccountTableView.AccountModel> selectedAccountModel = new SimpleObjectProperty<>();
	private ObjectProperty<File> file = new SimpleObjectProperty<>();

	public CSVParserViewModel(ObservableList<AccountTableView.AccountModel> accountModelObservableList) {
		this.accountModelObservableList = accountModelObservableList;
	}
	
	public AccountTableView.AccountModel getSelectedAccountModel() { return selectedAccountModel.get(); }
	public ObjectProperty<AccountTableView.AccountModel> selectedAccountModelProperty() { return selectedAccountModel; }
	
	public ObservableList<AccountTableView.AccountModel> getAccountModelObservableList() { return accountModelObservableList; }

	public void setFile (File file) { this.file.set(file); }
	public File getFile() { return file.get(); }
	public ObjectProperty<File> fileProperty() { return file; }
}


