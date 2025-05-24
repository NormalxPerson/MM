package com.moneymanager.ui.view;


import com.moneymanager.ui.viewModel.CSVParserViewModel;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.StringConverter;

import java.io.File;
import java.util.function.Consumer;

public class CSVParserViewBuilder implements Builder<Region> {
	
	private final CSVParserViewModel viewModel;
	private Button fileChooserButton;
	
	private Consumer<Button> handleFileSelection;
	
	public CSVParserViewBuilder(CSVParserViewModel viewModel, Consumer<> handler) {
		this.viewModel = viewModel;
		this.handleFileSelection = handler;
	}
	
	@Override
	public Region build() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		mainPane.setStyle("-fx-background-color: #E7E0EC; -fx-border-color: red; -fx-border-width: 2;");
		
		mainPane.setTop(createTopSection());
		return mainPane;
	}
	
	private Node createTopSection() {
		VBox topSection = new VBox(10);
		topSection.setPadding(new Insets(5,0,15,0));
		topSection.setAlignment(Pos.CENTER);
		
		Label title = new Label("Import CSV File");
		title.getStyleClass().add("headline-label");
		
		topSection.getChildren().setAll(title, createAccountComboBox());
		
		return topSection;
	}
	
	private Node createFileChooser() {
		VBox fileChooserBox = new VBox(10);
		fileChooserBox.setAlignment(Pos.CENTER);
		
		fileChooserButton = new Button("Choose CSV File");
		fileChooserButton.getStyleClass().addAll("button", "md3-rounded-medium");
		
		fileChooserButton.disableProperty().bind(viewModel.selectedAccountModelProperty().isNull());
		
		Label fileLabel = new Label("No File Selected");
		fileLabel.textProperty().bind(
				Bindings.createStringBinding(() -> {
					File file = viewModel.getFile();
					return file != null ? file.getName() : "No File Selected";
				}, viewModel.fileProperty())
		);
		
		fileChooserButton.setOnAction(event -> handleFileSelection.accept(fileChooserButton));
		fileChooserBox.getChildren().addAll(fileChooserButton, fileLabel);
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
