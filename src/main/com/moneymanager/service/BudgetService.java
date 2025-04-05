package com.moneymanager.service;

import com.moneymanager.core.Budget;
import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.Transaction;
import com.moneymanager.repos.BudgetCategoryRepo;
import com.moneymanager.repos.BudgetRepo;
import com.moneymanager.repos.TransactionRepo;
import com.moneymanager.ui.model.BudgetCategoryModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetService {
	BudgetRepo budgetRepo;
	BudgetCategoryRepo budgetCategoryRepo;
	TransactionRepo transactionRepo;

	public BudgetService(BudgetRepo budgetRepo, BudgetCategoryRepo budgetCategoryRepo, TransactionRepo transactionRepo) {
		this.budgetRepo = budgetRepo;
		this.budgetCategoryRepo = budgetCategoryRepo;
		this.transactionRepo = transactionRepo;
	}
	
	public void createBudgetCategoryFromInput(String budgetId, String categoryName, String categoryDescription, Double allocatedAmount ) {
		BudgetCategory newCategory = new BudgetCategory(budgetId, categoryName, categoryDescription, allocatedAmount);
		budgetCategoryRepo.addBudgetCategory(newCategory);
	}
	
	public Budget createBudget(String budgetName, YearMonth yearMonth) {
		Budget newBudget = new Budget(budgetName, yearMonth);
		budgetRepo.addBudgetAndReturnId(newBudget);
		System.out.print(newBudget.getBudgetId() + newBudget.getBudgetName() + newBudget.getYearMonth());
		return newBudget;
	}
	
	public Budget getBudgetFromYearMonth(YearMonth yearMonth) {
		return budgetRepo.getBudgetByYearMonth(yearMonth);
	}
	
	
	public ObservableList<BudgetCategoryModel> getCategoriesForBudget(YearMonth yearMonth) {
		BudgetWithCategories budsAndCats = new BudgetWithCategories(getBudgetFromYearMonth(yearMonth));
		return budsAndCats.getCategories();
	}
	
	public class BudgetWithCategories {
		private Boolean categoriesLoaded = false;
		private Budget budget;
		private ObservableList<BudgetCategoryModel> categories = FXCollections.observableArrayList();
		private DoubleProperty totalAllocatedAmount = new SimpleDoubleProperty(0);
		private DoubleProperty totalSpentAmount = new SimpleDoubleProperty(0);
		
		public BudgetWithCategories(Budget budget) {
			this.budget = budget;
		}
		
		public Budget getBudget() {
			return budget;
		}
		
		public ObservableList<BudgetCategoryModel> getCategories() {
			if (!categoriesLoaded) {
				loadCategoriesWithSpentAmount();
			}
			return categories;
		}
		
		private void loadCategoriesWithSpentAmount() {
			if (categoriesLoaded) {
				return;
			}
			try {
				List<BudgetCategory> rawCategories = budgetCategoryRepo.getCategoriesByBudgetId(budget.getBudgetId());
				YearMonth budgetMonth = budget.getYearMonth();
				LocalDate startDate = budgetMonth.atDay(1);
				LocalDate endDate = budgetMonth.atEndOfMonth();
				
				List<Transaction> monthTransactions = transactionRepo.getTransactionsByDateRange(startDate, endDate);
				Map<String, Double> categorySpending = new HashMap<>();
				
				for (Transaction transaction : monthTransactions) {
					String categoryId = transaction.getCategoryId();
					if (categoryId != null && !categoryId.isEmpty()) {
						double amount = transaction.getAmount();
						
						if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
							categorySpending.merge(categoryId, Math.abs(amount), Double::sum);
						}
					}
				}
				
				// Step 4: Create BudgetCategoryModel objects for each category
				double totalAllocatedSum = 0;
				double totalSpentSum = 0;
				
				for (BudgetCategory category : rawCategories) {
					String categoryId = category.getCategoryId();
					double allocatedAmount = category.getAllocatedAmount();
					double spentAmount = categorySpending.getOrDefault(categoryId, 0.0);
					
					// Create and add the model
					BudgetCategoryModel model = new BudgetCategoryModel(
							categoryId,
							budget.getBudgetId(),
							category.getCategoryName(),
							category.getDescription(),
							allocatedAmount,
							spentAmount
					);
					
					// Add to our observable list
					categories.add(model);
					
					// Track totals for the budget
					totalAllocatedSum += allocatedAmount;
					totalSpentSum += spentAmount;
				}
				
				// Step 5: Update budget totals
				this.totalAllocatedAmount.set(totalAllocatedSum);
				this.totalSpentAmount.set(totalSpentSum);
				
				// Mark as loaded
				categoriesLoaded = true;
			} catch (Exception e) {
				// Log the error
				System.err.println("Error loading budget categories: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
