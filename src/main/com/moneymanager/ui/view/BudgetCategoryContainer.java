package com.moneymanager.ui.view;

import com.moneymanager.ui.model.BudgetCategoryCard;
import com.moneymanager.ui.model.BudgetCategoryModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class BudgetCategoryContainer extends VBox {
	private ObservableList<BudgetCategoryModel> categoryModels;
	private Map<String, BudgetCategoryCard> cardMap = new HashMap<>();
	
	private HBox cardsContainer;
	private ScrollPane scrollPane;
	private Button addCategoryButton;
	
	public BudgetCategoryContainer(ObservableList<YearMonth> budgetMonths) {
		this.categoryModels = FXCollections.observableArrayList();
		initializeUI();
		setupListeners();
		ComboBox<YearMonth> yearMonthComboBox = new ComboBox<>(budgetMonths);
		yearMonthComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(YearMonth object) {
				return object != null ? object.getMonth().toString() : "";
			}
			@Override
			public YearMonth fromString(String string) {
				for (YearMonth yearMonth : budgetMonths) {
					if (yearMonth.toString().equals(string)) {
						return yearMonth;
					}
				}
				return YearMonth.now();
			}
		});
		
		ComboBox<YearMonth> yearComboBox = new ComboBox<>();
		cardsContainer.getChildren().add(yearMonthComboBox);
	}
	
	private void initializeUI() {
		
		




		// Create header with title and add button
		Label titleLabel = new Label("Budget Categories");
		titleLabel.getStyleClass().add("section-header");
		
		addCategoryButton = new Button("+");
		addCategoryButton.getStyleClass().addAll("floating-action-button", "mini-fab");
		
		HBox headerBox = new HBox(titleLabel, addCategoryButton);
		headerBox.setSpacing(10);
		headerBox.setPadding(new Insets(0, 0, 10, 0));
		HBox.setHgrow(titleLabel, Priority.ALWAYS);
		
		// Create scrollable container for cards
		cardsContainer = new HBox();
		cardsContainer.setSpacing(16);
		cardsContainer.setPadding(new Insets(5, 5, 5, 5));
		
		scrollPane = new ScrollPane(cardsContainer);
		scrollPane.setFitToHeight(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true); // Enable mouse panning
		scrollPane.getStyleClass().add("no-scroll-bars");
		
		// Add everything to the main container
		this.getChildren().addAll(headerBox, scrollPane);
		this.setSpacing(10);
		this.setPadding(new Insets(15));
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		
		// Add some initial style classes
		this.getStyleClass().add("budget-categories-container");
	}
	
	private void setupListeners() {
		// Listen for changes to the categories list
		categoryModels.addListener((ListChangeListener<BudgetCategoryModel>) change -> {
			while (change.next()) {
				if (change.wasAdded()) {
					for (BudgetCategoryModel model : change.getAddedSubList()) {
						addCard(model);
					}
				}
				if (change.wasRemoved()) {
					for (BudgetCategoryModel model : change.getRemoved()) {
						removeCard(model.getCategoryId());
					}
				}
			}
		});
	}
	
	private void addCard(BudgetCategoryModel model) {
		BudgetCategoryCard card = BudgetCategoryCard.fromModel(model);
		card.setPrefWidth(280);
		card.setMinWidth(280);
		card.setMaxWidth(280);
		
		// Store the card in our map for later reference
		cardMap.put(model.getCategoryId(), card);
		
		// Add to the UI
		cardsContainer.getChildren().add(card);
		
		// Add a click listener (could be used for editing)
		card.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				// Handle single click (select)
				System.out.println("Selected category: " + model.getCategoryName());
			} else if (event.getClickCount() == 2) {
				// Handle double click (edit)
				System.out.println("Edit category: " + model.getCategoryName());
			}
		});
	}
	
	private void removeCard(String categoryId) {
		BudgetCategoryCard card = cardMap.remove(categoryId);
		if (card != null) {
			cardsContainer.getChildren().remove(card);
		}
	}
	
	public void updateCard(BudgetCategoryModel model) {
		BudgetCategoryCard card = cardMap.get(model.getCategoryId());
		if (card != null) {
			card.setCategoryName(model.getCategoryName());
			card.setDescription(model.getDescription());
			card.setAllocatedAmount(model.getAllocatedAmount());
			card.setSpentAmount(model.getSpentAmount());
		}
	}
	
	public ObservableList<BudgetCategoryModel> getCategoryModels() {
		return categoryModels;
	}
	
	public void setCategoryModels(ObservableList<BudgetCategoryModel> models) {
		// Clear current cards
		cardsContainer.getChildren().clear();
		cardMap.clear();
		
		// Set new models
		this.categoryModels.setAll(models);
	}
	
	public Button getAddCategoryButton() {
		return addCategoryButton;
	}
	
}
