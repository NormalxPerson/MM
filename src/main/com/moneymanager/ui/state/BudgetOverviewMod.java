package com.moneymanager.ui.state;

import com.moneymanager.service.BudgetService;
import com.moneymanager.ui.model.BudgetCategoryCard;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.YearMonth;
import java.util.List;

public class BudgetOverviewMod {
	private ObjectProperty<YearMonth> selectedYearMonth = new SimpleObjectProperty<>(YearMonth.now());
	private ObservableList<BudgetCategoryCard> categoryCards = FXCollections.observableArrayList();
	private DoubleProperty totalAllocated = new SimpleDoubleProperty();
	private DoubleProperty totalSpent = new SimpleDoubleProperty();
	private StringProperty budgetName = new SimpleStringProperty("");
	private StringProperty budgetId = new SimpleStringProperty("");
	
	
	
	
	
	
	public YearMonth getSelectedYearMonth() { return selectedYearMonth.get(); }
	public void setSelectedYearMonth(YearMonth selectedYearMonth) { this.selectedYearMonth.set(selectedYearMonth); }
	public ObjectProperty<YearMonth> selectedYearMonthProperty() { return selectedYearMonth; }
	
	public ObservableList<BudgetCategoryCard> getCategoryCards() { return categoryCards; }
	public void setCategoryCards(List<BudgetCategoryCard> categoryCards) {
		this.categoryCards.setAll(categoryCards);
	}
	
	public DoubleProperty getTotalAllocated() { return totalAllocated; }
	public void setTotalAllocated(Double totalAllocated) { this.totalAllocated.set(totalAllocated); }
	
	public DoubleProperty getTotalSpent() { return totalSpent; }
	public void setTotalSpent(Double totalSpent) { this.totalSpent.set(totalSpent); }
	
	public StringProperty getBudgetName() { return budgetName; }
	public void setBudgetName(String budgetName) { this.budgetName.set(budgetName); }
	
	public StringProperty getBudgetId() { return budgetId; }
	public void setBudgetId(String budgetId) { this.budgetId.set(budgetId); }
	
}
