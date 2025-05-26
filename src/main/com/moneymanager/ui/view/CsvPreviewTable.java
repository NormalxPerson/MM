package com.moneymanager.ui.view;

import com.moneymanager.csv.CsvParser;
import com.moneymanager.ui.viewModel.CSVParserViewModel;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class CsvPreviewTable implements Builder<Region> {
	private final CSVParserViewModel viewModel;
	private TableView<ObservableList<String>> tableView;
	
	public CsvPreviewTable(CSVParserViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public Region build() {
		tableView = new TableView<>();
		setupTable();
		setupBindings();
		return tableView;
	}
	
	private void setupTable() {
		tableView.getStyleClass().addAll("table-view", "md3-rounded-medium");
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		// Set up for spreadsheet-like appearance
		tableView.getStyleClass().add("csv-preview-table");
		tableView.setRowFactory(tv -> {
			var row = new javafx.scene.control.TableRow<ObservableList<String>>();
			row.itemProperty().addListener((obs, oldItem, newItem) -> {
				if (newItem != null) {
					// Add alternating row colors for better readability
					if (row.getIndex() % 2 == 0) {
						row.getStyleClass().removeAll("odd-row");
						row.getStyleClass().add("even-row");
					} else {
						row.getStyleClass().removeAll("even-row");
						row.getStyleClass().add("odd-row");
					}
				}
			});
			return row;
		});
	}
	
	private void setupBindings() {
		// Automatically update table when CSV data changes
		viewModel.csvDataProperty().addListener((obs, oldData, newData) -> {
			loadDataIntoTable(newData);
		});
		
		// Load initial data if available
		if (viewModel.getCsvData() != null) {
			loadDataIntoTable(viewModel.getCsvData());
		}
	}
	
	private void loadDataIntoTable(CsvParser.CsvData csvData) {
		// Clear existing columns and data
		tableView.getColumns().clear();
		tableView.getItems().clear();
		
		if (csvData == null || csvData.getColumnCount() == 0) {
			return;
		}
		
		// Create columns dynamically based on CSV headers
		createColumns(csvData.getHeaders());
		
		// Load data into the table
		loadData(csvData.getRows(), csvData.getColumnCount());
	}
	
	private void createColumns(List<String> headers) {
		for (int columnIndex = 0; columnIndex < headers.size(); columnIndex++) {
			final int colIndex = columnIndex;
			String headerText = headers.get(columnIndex);
			
			// Create column with header text
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(headerText);
			
			// Set up cell value factory
			column.setCellValueFactory(param -> {
				ObservableList<String> rowData = param.getValue();
				if (colIndex < rowData.size()) {
					return new ReadOnlyStringWrapper(rowData.get(colIndex));
				} else {
					return new ReadOnlyStringWrapper("");
				}
			});
			
			// Set column properties for spreadsheet-like appearance
			column.setPrefWidth(120); // Default width
			column.setMinWidth(80);
			column.setMaxWidth(200);
			column.setResizable(true);
			column.setSortable(false); // Disable sorting to maintain CSV order
			
			// Add custom cell factory for better formatting
			column.setCellFactory(col -> {
				var cell = new javafx.scene.control.TableCell<ObservableList<String>, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item);
							// Add tooltip for long text
							if (item.length() > 20) {
								setTooltip(new javafx.scene.control.Tooltip(item));
							}
						}
					}
				};
				cell.getStyleClass().add("csv-cell");
				return cell;
			});
			
			tableView.getColumns().add(column);
		}
	}
	
	private void loadData(List<List<String>> rows, int columnCount) {
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
		
		for (List<String> row : rows) {
			ObservableList<String> rowData = FXCollections.observableArrayList(row);
			// Ensure all rows have the same number of columns as headers
			while (rowData.size() < columnCount) {
				rowData.add("");
			}
			data.add(rowData);
		}
		
		tableView.setItems(data);
	}
	
	public boolean hasData() {
		return viewModel.getCsvData() != null && viewModel.getCsvData().getRowCount() > 0;
	}
}
