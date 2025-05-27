package com.moneymanager.ui.view;


import com.moneymanager.ui.viewModel.CSVParserViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class CSVParserViewBuilder implements Builder<Region> {
	
	private final CSVParserViewModel viewModel;
	private Button fileActionButton;
	private final StringProperty fileButtonText = new SimpleStringProperty("Choose CSV File");
	
	private Consumer<File> onFileSelectedHandler;
	
	public CSVParserViewBuilder(CSVParserViewModel viewModel, Consumer<File> onFileSelectedHandler) {
		this.viewModel = viewModel;
		this.onFileSelectedHandler = onFileSelectedHandler;
		
		viewModel.fileProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				fileButtonText.set(newValue.getName());
			} else {
				fileButtonText.set("Choose CSV File");
			}
		});
	}
	
	@Override
	public Region build() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		mainPane.setStyle("-fx-background-color: #E7E0EC; -fx-border-color: red; -fx-border-width: 2;");
		
		mainPane.setTop(createTopSection());
		mainPane.setCenter(createPreviewTable());
		return mainPane;
	}
	
	private void openConfigTransactionDialog() {
		List<String> colors = List.of("Red", "Yellow", "Green");
		ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Yellow", colors);
		choiceDialog.showAndWait();
	}
	
	private Node createPreviewTable() {
		VBox centerSection = new VBox(10);
		centerSection.setPadding(new Insets(10, 5, 10, 5));
		
		// Preview section header
		Label previewLabel = new Label("Data Preview");
		previewLabel.getStyleClass().add("title-label");
		previewLabel.visibleProperty().bind(viewModel.csvDataProperty().isNotNull());
		previewLabel.managedProperty().bind(previewLabel.visibleProperty());
		
		// Create preview table using builder with view model binding
		CsvPreviewTable previewTableBuilder = new CsvPreviewTable(viewModel);
		Region tableRegion = previewTableBuilder.build();
		
		tableRegion.visibleProperty().bind(viewModel.csvDataProperty().isNotNull());
		tableRegion.managedProperty().bind(tableRegion.visibleProperty());
		
		// No need for manual data loading - table binds to viewModel.csvDataProperty() automatically
		
		// Placeholder when no data
		Label placeholderLabel = new Label("Select a CSV file to preview data");
		placeholderLabel.getStyleClass().add("label");
		placeholderLabel.setStyle("-fx-text-fill: #666666;");
		placeholderLabel.visibleProperty().bind(viewModel.csvDataProperty().isNull());
		placeholderLabel.managedProperty().bind(placeholderLabel.visibleProperty());
		
		StackPane centerStack = new StackPane();
		centerStack.getChildren().addAll(tableRegion, placeholderLabel);
		VBox.setVgrow(centerStack, Priority.ALWAYS);
		
		centerSection.getChildren().addAll(previewLabel, centerStack);
		
		return centerSection;
	}
	
	private Node createTopSection() {
		VBox topSection = new VBox(10);
		topSection.setPadding(new Insets(5,0,15,0));
		topSection.setAlignment(Pos.CENTER);
		
		Label title = new Label("Import CSV File");
		title.getStyleClass().add("headline-label");
		
		topSection.getChildren().setAll(title, createAccountComboBox(), createFileChooser());
		
		return topSection;
	}
	
	private Node createFileChooser() {
		VBox fileChooserBox = new VBox(10);
		fileChooserBox.setAlignment(Pos.CENTER);
		
		fileActionButton = new Button();
		fileActionButton.textProperty().bind(fileButtonText);
		fileActionButton.getStyleClass().addAll("button", "md3-rounded-medium");
		
		fileActionButton.disableProperty().bind(viewModel.selectedAccountModelProperty().isNull());
		
		fileActionButton.setOnAction(event -> {
			if (viewModel.getFile() == null) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select CSV file");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				
				Stage stage = (Stage) fileActionButton.getScene().getWindow();
				File selectedFile = fileChooser.showOpenDialog(stage);
				
				if (selectedFile != null) {
					// Pass the selected file to the handler provided by the controller
					if (onFileSelectedHandler != null) {
						onFileSelectedHandler.accept(selectedFile);
					}
				}
			} else {
				viewModel.setFile(null);
				if (onFileSelectedHandler != null) {
					onFileSelectedHandler.accept(null);
				}
			}
		});
		fileChooserBox.getChildren().addAll(fileActionButton);
		return fileChooserBox;
	}
	
	private Node createAccountComboBox() {
		Label accountLabel = new Label("Import to Account:");
		accountLabel.getStyleClass().add("label");
		
		ComboBox<AccountTableView.AccountModel> accountComboBox = new ComboBox<>();
		accountComboBox.setItems(viewModel.getAccountModelObservableList());
		accountComboBox.setConverter(createAccountConverter());

		accountComboBox.valueProperty().bindBidirectional(viewModel.selectedAccountModelProperty());
		
		return new VBox(10, accountLabel, accountComboBox);
	}
	
	private StringConverter<AccountTableView.AccountModel> createAccountConverter() {
		return new StringConverter<>() {
			@Override
			public String toString(AccountTableView.AccountModel accountModel) {
				return accountModel != null ? accountModel.getAccountName() : "";
			}
			
			@Override
			public AccountTableView.AccountModel fromString(String string) {
				return null; // Handled by ComboBox selection
			}
		};
	}
}
