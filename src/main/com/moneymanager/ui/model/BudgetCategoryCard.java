package com.moneymanager.ui.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BudgetCategoryCard extends VBox {
	private StringProperty categoryId = new SimpleStringProperty();
	private StringProperty budgetId = new SimpleStringProperty();
	private StringProperty categoryName = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private DoubleProperty allocatedAmount = new SimpleDoubleProperty();
	private DoubleProperty spentAmount = new SimpleDoubleProperty();
	private DoubleProperty remainingAmount = new SimpleDoubleProperty();
	private DoubleProperty progress = new SimpleDoubleProperty();
	
	private Label categoryNameLabel;
	private Label allocatedAmountLabel;
	private Label remainingAmountLabel;
	private ProgressBar progressBar;
	private Label progressLabel;
	
	
	
	public BudgetCategoryCard() {
		initializeView();
		setupBindings();
		setupStyles();
	}

	private void initializeView() {
		categoryNameLabel = new Label();
		categoryNameLabel.getStyleClass().add("category-name");
		
		HBox nameBox = new HBox(8, categoryNameLabel);
		nameBox.setAlignment(Pos.CENTER_LEFT);
		
		// Amounts section
		Label allocatedPrefix = new Label("Budget: ");
		allocatedAmountLabel = new Label();
		
		HBox amountBox = new HBox(allocatedPrefix, allocatedAmountLabel);
		amountBox.setAlignment(Pos.CENTER_LEFT);
		
		Label remainingPrefix = new Label("Remaining: ");
		remainingAmountLabel = new Label();
		
		HBox remainingBox = new HBox(remainingPrefix, remainingAmountLabel);
		remainingBox.setAlignment(Pos.CENTER_LEFT);
		
		// Progress section
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(Double.MAX_VALUE);
		
		progressLabel = new Label();
		progressLabel.getStyleClass().add("percentage-label");
		
		HBox progressBox = new HBox(progressBar, progressLabel);
		progressBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setMargin(progressLabel, new Insets(0, 0, 0, 8));
		HBox.setHgrow(progressBar, Priority.ALWAYS);
		
		// Put it all together
		this.getChildren().addAll(nameBox, amountBox, remainingBox, progressBox);
		
		// Add some padding and spacing
		this.setPadding(new Insets(12));
		this.setSpacing(8);
	}
	
	private void setupBindings() {
		// Bind labels to properties
		categoryNameLabel.textProperty().bind(categoryName);
		allocatedAmountLabel.textProperty().bind(allocatedAmount.asString("$%.2f"));
		remainingAmountLabel.textProperty().bind(remainingAmount.asString("$%.2f"));
		progressLabel.textProperty().bind(progress.multiply(100).asString("%.0f%%"));
		
		// Bind progress bar to progress property
		progressBar.progressProperty().bind(progress);
		
		// Update status colors when values change
		remainingAmount.addListener((obs, oldVal, newVal) -> updateRemainingColor());
		progress.addListener((obs, oldVal, newVal) -> updateProgressColor());
	}
	
	private void setupStyles() {
		this.getStyleClass().add("budget-category-card");
		this.getStyleClass().add("md3-rounded-medium");
		
		categoryNameLabel.getStyleClass().add("title-label");
		progressBar.getStyleClass().add("budget-progress-bar");
		
		updateRemainingColor();
		updateProgressColor();
	}
	
	private void updateRemainingColor() {
		double remaining = remainingAmount.get();
		
		if (remaining < 0) {
			remainingAmountLabel.setStyle("-fx-text-fill: #e74c3c;"); // Red for overspent
		} else if (remaining < allocatedAmount.get() * 0.2) {
			remainingAmountLabel.setStyle("-fx-text-fill: #f39c12;"); // Orange for low funds
		} else {
			remainingAmountLabel.setStyle("-fx-text-fill: #2ecc71;"); // Green for healthy funds
		}
	}
	
	private void updateProgressColor() {
		double progressValue = progress.get();
		
		if (progressValue >= 1.0) {
			progressBar.setStyle("-fx-accent: #e74c3c;"); // Red if overspent
		} else if (progressValue >= 0.8) {
			progressBar.setStyle("-fx-accent: #f39c12;"); // Orange if close to limit
		} else {
			progressBar.setStyle("-fx-accent: #2ecc71;"); // Green if under budget
		}
	}
	
	public String getCategoryId() { return categoryId.get(); }
	public void setCategoryId(String id) { this.categoryId.set(id); }
	public StringProperty categoryIdProperty() { return categoryId; }
	
	public String getBudgetId() { return budgetId.get(); }
	public void setBudgetId(String id) { this.budgetId.set(id); }
	public StringProperty budgetIdProperty() { return budgetId; }
	
	public String getCategoryName() { return categoryName.get(); }
	public void setCategoryName(String name) { this.categoryName.set(name); }
	public StringProperty categoryNameProperty() { return categoryName; }
	
	public String getDescription() { return description.get(); }
	public void setDescription(String desc) { this.description.set(desc); }
	public StringProperty descriptionProperty() { return description; }
	
	public double getAllocatedAmount() { return allocatedAmount.get(); }
	public void setAllocatedAmount(double amount) { this.allocatedAmount.set(amount); }
	public DoubleProperty allocatedAmountProperty() { return allocatedAmount; }
	
	public double getSpentAmount() { return spentAmount.get(); }
	public void setSpentAmount(double amount) { this.spentAmount.set(amount); }
	public DoubleProperty spentAmountProperty() { return spentAmount; }
	
	public double getRemainingAmount() { return remainingAmount.get(); }
	public DoubleProperty remainingAmountProperty() { return remainingAmount; }
	
	public double getProgress() { return progress.get(); }
	public DoubleProperty progressProperty() { return progress; }
	
	// Helper to update calculated fields
	/*private void updateRemainingAmount() {
		double allocated = getAllocatedAmount();
		double spent = getSpentAmount();
		
		remainingAmount.set(allocated - spent);
		progress.set(allocated > 0 ? spent / allocated : 0);
	}*/
	
	// Factory method to create a card from data
	public static BudgetCategoryCard fromModel(BudgetCategoryModel model) {
		BudgetCategoryCard card = new BudgetCategoryCard();
		card.categoryId.bind(model.categoryIdProperty());
		card.budgetId.bind(model.budgetIdProperty());
		card.categoryName.bind(model.categoryNameProperty());
		card.description.bind(model.descriptionProperty());
		card.allocatedAmount.bind(model.allocatedAmountProperty());
		card.spentAmount.bind(model.spentAmountProperty());
		
		card.remainingAmount.bind(
				Bindings.subtract(card.allocatedAmount, card.spentAmount));
		if (card.allocatedAmount.get() > 0.0) { card.progress.bind (
				Bindings.divide(card.spentAmount,card.allocatedAmount));
		}
		return card;
	}
	
	public static Node createAddBudgetCategoryCard(Runnable createBudgetCategoryHandler) {
		StackPane pane = new StackPane();
		pane.setAlignment(Pos.CENTER);
		pane.getStyleClass().add("budget-category-card");
		pane.getStyleClass().add("md3-rounded-medium");
		
		BudgetCategoryCard fakeCard = new BudgetCategoryCard();
		fakeCard.setVisible(false);
		
		Label createNewCategoryLabel = new Label("Create new category?");
		createNewCategoryLabel.getStyleClass().add("label");
		
		Button createNewCategoryButton = new Button("+");
		createNewCategoryButton.getStyleClass().add("create-category-button");
		createNewCategoryButton.setOnAction(e -> createBudgetCategoryHandler.run());
		
		VBox vbox = new VBox(10, createNewCategoryLabel, createNewCategoryButton);
		vbox.setAlignment(Pos.CENTER);
		
		pane.getChildren().addAll(fakeCard, vbox);
		return pane;
	}
}
