package com.moneymanager.ui.view;

import com.moneymanager.ui.state.BudgetOverviewMod;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class BudgetOverviewBuilder implements Builder<Region> {
	
	private final BudgetOverviewMod viewModel;
	private final Runnable createBudgetHandler;
	
	
	public BudgetOverviewBuilder(BudgetOverviewMod viewModel, Runnable createBudgetHandler) {
		this.viewModel = viewModel;
		this.createBudgetHandler = createBudgetHandler;
	}
	
	@Override
	public Region build() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		mainPane.setStyle("-fx-background-color: -fx-md3-background-color;");
		
		BudgetCardContainerBuilder cardContainerBuilder = new BudgetCardContainerBuilder(viewModel);
		
		Node topSection = createTopSection(cardContainerBuilder);
		mainPane.setTop(topSection);
		
		Node centerStackPane = createCenterStackPane(cardContainerBuilder);
		mainPane.setCenter(centerStackPane);
		
		return mainPane;
	}
	
	private Node createCenterStackPane(BudgetCardContainerBuilder cardContainerBuilder) {
		StackPane stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);
		
		Label textLabel = new Label();
		textLabel.textProperty().bind(Bindings.concat("No Budget has been created for ", viewModel.getBudgetName()));
		textLabel.getStyleClass().add("label");
		
		Button createBudgetButton = new Button("Create Budget");
		createBudgetButton.setOnAction(e -> createBudgetHandler.run());
		
		VBox placeholderBox = new VBox(10, textLabel, createBudgetButton);
		placeholderBox.setAlignment(Pos.CENTER);
		placeholderBox.visibleProperty().bind(Bindings.not(viewModel.budgetExistsProperty()));
		placeholderBox.managedProperty().bind(placeholderBox.visibleProperty());
		
		ScrollPane cardScrollPane = (ScrollPane) cardContainerBuilder.build();
		
		
		cardScrollPane.visibleProperty().bind(viewModel.budgetExistsProperty());
		cardScrollPane.managedProperty().bind(cardScrollPane.visibleProperty());
		
		stackPane.getChildren().addAll(cardScrollPane, placeholderBox);
		return stackPane;
	}
	
	private Node createTopSection(BudgetCardContainerBuilder cardContainerBuilder) {
		VBox topSection = new VBox(10);
		topSection.setPadding(new Insets(5,0,15,0));
		topSection.setAlignment(Pos.CENTER);
		
		Label budgetNameLabel = new Label();
		budgetNameLabel.textProperty().bind(viewModel.getBudgetName());
		budgetNameLabel.getStyleClass().add("headline-label");
		
		HBox summaryBox = new HBox(20);
		summaryBox.setAlignment(Pos.CENTER);
		
		Label allocatedLabel = createSummaryLabel("Allocated:", viewModel.getTotalAllocated().asString("$%.2f"));
		Label spentLabel = createSummaryLabel("Spent:", viewModel.getTotalSpent().asString("$%.2f"));
		Label remainingLabel = createRemainingLabel();
		
		summaryBox.getChildren().setAll(allocatedLabel, spentLabel, remainingLabel);
		summaryBox.visibleProperty().bind(viewModel.budgetExistsProperty());
		summaryBox.managedProperty().bind(summaryBox.visibleProperty());
		
		Node comboBoxHeader = cardContainerBuilder.buildHeaderBox();
		VBox.setMargin(comboBoxHeader, new Insets(5,0,0,0));
		
		topSection.getChildren().addAll(budgetNameLabel, summaryBox, comboBoxHeader);
		
		return topSection;
	}
	
	private Label createSummaryLabel(String prefix, StringBinding binding) {
		Label label = new Label();
		label.textProperty().bind(Bindings.concat(prefix, "", binding));
		label.getStyleClass().add("label");
		return label;
	}
	
	private Label createRemainingLabel() {
		Label label = new Label();
		StringBinding remainingBinding = viewModel.getTotalAllocated().subtract(viewModel.getTotalSpent()).asString("$%.2f");
		label.textProperty().bind(Bindings.concat("Remaining: ", remainingBinding));
		label.getStyleClass().add("label");
		return label;
	}
}
