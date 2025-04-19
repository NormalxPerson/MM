package com.moneymanager.ui.interactor;

import com.moneymanager.core.Budget;
import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.model.BudgetCategoryCard;
import com.moneymanager.ui.model.BudgetCategoryModel;
import com.moneymanager.ui.state.BudgetOverviewMod;
import javafx.collections.ObservableList;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetInteractor {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetService budgetService;
	private final TransactionService transactionService;
	
	private HashMap<YearMonth, BudgetService.BudgetWithCategories> yearMonthBudgetWithCategoriesMap = new HashMap<>();
	
	public BudgetInteractor(BudgetOverviewMod budgetOverviewModel, BudgetService budgetService, TransactionService transactionService) {
		this.budgetOverviewModel = budgetOverviewModel;
		this.budgetService = budgetService;
		this.transactionService = transactionService;
		updateBudgetMap();
		addListenerOnModSelectedYearMonth();
		loadBudgetForMonth(budgetOverviewModel.getSelectedYearMonth());
		
	}
	
	private void updateBudgetMap() {
		List<BudgetService.BudgetWithCategories> budsAndCats = budgetService.getBudgetsWithCategories();
		for (BudgetService.BudgetWithCategories budAndCat : budsAndCats) {
			yearMonthBudgetWithCategoriesMap.put(budAndCat.getBudget().getYearMonth(), budAndCat);
		}
	}
	
	private void addListenerOnModSelectedYearMonth() {
		budgetOverviewModel.selectedYearMonthProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				loadBudgetForMonth(newValue);
			}
		});
	}
	
	private void loadBudgetForMonth(YearMonth yearMonth) {
		if (!yearMonthBudgetWithCategoriesMap.containsKey(yearMonth)) {
			updateBudgetMap();
			if (!yearMonthBudgetWithCategoriesMap.containsKey(yearMonth)) {
				System.out.println("No budget found for " + yearMonth);
				budgetOverviewModel.getCategoryCards().removeAll();
				budgetOverviewModel.setTotalAllocated(0.0);
				budgetOverviewModel.setTotalSpent(0.0);
				budgetOverviewModel.setBudgetName(yearMonth.getMonth().toString());
				budgetOverviewModel.setBudgetId(null);
			}
			
		}
		else {
			BudgetService.BudgetWithCategories budgetWithCategories = yearMonthBudgetWithCategoriesMap.get(yearMonth);
			Budget budget = budgetWithCategories.getBudget();
			List<BudgetCategoryCard> categoryCards = new ArrayList<>();
			for (BudgetCategoryModel categoryModel : budgetWithCategories.getCategories()) {
				categoryCards.add(BudgetCategoryCard.fromModel(categoryModel));
			}
			
			
			budgetOverviewModel.setCategoryCards(categoryCards);
			budgetOverviewModel.setTotalAllocated(budgetWithCategories.getTotalAllocatedAmount());
			budgetOverviewModel.setTotalSpent(budgetWithCategories.getTotalSpentAmount());
			budgetOverviewModel.setBudgetName(budget.getBudgetName());
			budgetOverviewModel.setBudgetId(budget.getBudgetId());
			
		}
	}
}
