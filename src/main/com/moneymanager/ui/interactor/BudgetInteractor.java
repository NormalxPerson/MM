package com.moneymanager.ui.interactor;

import com.moneymanager.core.Budget;
import com.moneymanager.core.BudgetCategory;
import com.moneymanager.service.BudgetService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.model.BudgetCategoryCard;
import com.moneymanager.ui.model.BudgetCategoryModel;
import com.moneymanager.ui.viewModel.BudgetOverviewMod;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class BudgetInteractor {
	private final BudgetOverviewMod budgetOverviewModel;
	private final BudgetService budgetService;
	private final TransactionService transactionService;
	
	private Consumer<BudgetCategoryModel> categoryEditHandler;
	
	private HashMap<YearMonth, BudgetService.BudgetWithCategories> yearMonthBudgetWithCategoriesMap = new HashMap<>();
	
	public BudgetInteractor(BudgetOverviewMod budgetOverviewModel, BudgetService budgetService, TransactionService transactionService, Consumer<BudgetCategoryModel> categoryEditHandler) {
		this.budgetOverviewModel = budgetOverviewModel;
		this.budgetService = budgetService;
		this.transactionService = transactionService;
		this.categoryEditHandler = categoryEditHandler;
		updateBudgetMap();
		addListenerOnModSelectedYearMonth();
		loadBudgetForMonth(budgetOverviewModel.getSelectedYearMonth());
		
	}
	
	public void addNewCategoryToCurrentBudget(BudgetCategory budgetCategory, String budgetId) {
		budgetService.addNewCategoryToBudget(budgetCategory,budgetId);
	}
	
	private void loadBudgetCategories() {
	
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
	
	private BudgetCategoryCard createCardFromModel(BudgetCategoryModel model) {
		return BudgetCategoryCard.fromModel(model, categoryEditHandler);
	}
	
	public void loadBudgetForMonth(YearMonth yearMonth) {
		updateBudgetMap();
		List<BudgetCategoryCard> categoryCards = new ArrayList<>();
		
		if (!yearMonthBudgetWithCategoriesMap.containsKey(yearMonth)) {
				System.out.println("No budget found for " + yearMonth);
				//categoryCards.add(budgetOverviewModel.getCategoryCreationCard());
				budgetOverviewModel.setCategoryCards(categoryCards);
				//budgetOverviewModel.getCategoryCards().add(budgetOverviewModel.getCategoryCreationCard());
				budgetOverviewModel.setTotalAllocated(0.0);
				budgetOverviewModel.setTotalSpent(0.0);
				budgetOverviewModel.setBudgetName(yearMonth.getMonth().toString());
				budgetOverviewModel.setBudgetId(null);
		}
			
		
		else {
			BudgetService.BudgetWithCategories budgetWithCategories = yearMonthBudgetWithCategoriesMap.get(yearMonth);
			Budget budget = budgetWithCategories.getBudget();
			for (BudgetCategoryModel categoryModel : budgetWithCategories.getCategories()) {
				categoryCards.add(createCardFromModel(categoryModel));
			}
			//categoryCards.add(budgetOverviewModel.getCategoryCreationCard());
			budgetOverviewModel.setCategoryCards(categoryCards);
			budgetOverviewModel.setTotalAllocated(budgetWithCategories.getTotalAllocatedAmount());
			budgetOverviewModel.setTotalSpent(budgetWithCategories.getTotalSpentAmount());
			budgetOverviewModel.setBudgetName(budget.getBudgetName());
			budgetOverviewModel.setBudgetId(budget.getBudgetId());
			
		}
	}
	
/*	public BudgetCategoryCard createBudgetCategory(BudgetCategoryModel categoryModel) {
	
	}*/
	
	public Budget createBudget() {
		updateBudgetMap();
		YearMonth selectedYearMonth = budgetOverviewModel.getSelectedYearMonth();
		if (!yearMonthBudgetWithCategoriesMap.containsKey(selectedYearMonth)) {
			Budget newBudget = budgetService.createBudget(selectedYearMonth);
			updateBudgetMap();
			loadBudgetForMonth(newBudget.getYearMonth());
			return newBudget;
			
		}
		return null;
	}
}
