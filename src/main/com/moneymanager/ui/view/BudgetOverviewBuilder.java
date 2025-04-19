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
		
		Node topSection = createTopSection();
		mainPane.setTop(topSection);
		
		BudgetCardContainerBuilder cardContainerBuilder = new BudgetCardContainerBuilder(viewModel);
		Node comboBoxHeader = cardContainerBuilder.buildHeaderBox();
		
		Node cardContainer = createCardContainer(cardContainerBuilder);
		
		VBox centerArea = new VBox(10);
		centerArea.getChildren().addAll(comboBoxHeader, cardContainer);
		VBox.setVgrow(comboBoxHeader, Priority.ALWAYS);
		mainPane.setCenter(centerArea);
		
		return mainPane;
	}
	
	private Node createCardContainer(BudgetCardContainerBuilder cardContainerBuilder) {
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
		
		
		Region cardRegion = cardContainerBuilder.build();
		
		cardRegion.visibleProperty().bind(viewModel.budgetExistsProperty());
		cardRegion.managedProperty().bind(cardRegion.visibleProperty());
		
		stackPane.getChildren().addAll(cardRegion, placeholderBox);
		return stackPane;
	}
	
	private Node createTopSection() {
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
		summaryBox.managedProperty().bind(viewModel.budgetExistsProperty());
		
		topSection.getChildren().addAll(budgetNameLabel, summaryBox);
		
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
