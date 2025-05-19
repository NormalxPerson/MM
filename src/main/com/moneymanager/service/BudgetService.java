package com.moneymanager.service;

import com.moneymanager.core.Budget;
import com.moneymanager.core.BudgetCategory;
import com.moneymanager.core.BudgetCategoryAllocation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetService {
	BudgetRepo budgetRepo;
	BudgetCategoryRepo budgetCategoryRepo;
	TransactionRepo transactionRepo;
	ObservableList<BudgetCategory> budgetCategories = FXCollections.emptyObservableList();
	HashMap<String, BudgetCategory> categoryMap = new HashMap<>();

	public BudgetService(BudgetRepo budgetRepo, BudgetCategoryRepo budgetCategoryRepo, TransactionRepo transactionRepo) {
		this.budgetRepo = budgetRepo;
		this.budgetCategoryRepo = budgetCategoryRepo;
		this.transactionRepo = transactionRepo;
		updateCategories();
		
	}
	
	public BudgetCategory createBudgetCategoryFromInput( String parentCategoryId, String categoryName, String categoryDescription) {
		BudgetCategory newCat = new BudgetCategory( categoryName, categoryDescription, parentCategoryId);
		budgetCategoryRepo.addBudgetCategory(newCat);
		updateCategories();
		return newCat;
	}
	
	public void addNewCategoryToBudget(BudgetCategory newCat, String budgetId) {
		BudgetCategoryAllocation allocation = new BudgetCategoryAllocation(budgetId, newCat.getCategoryId(), 0.0);
		budgetCategoryRepo.saveBudgetCategoryAllocation(allocation);
	}
	
	//This is where I can allow the user to choose which allocated amounts to migrate to the budget
	public Budget createBudget(YearMonth yearMonth) {
		Budget newBudget = new Budget(yearMonth);
		budgetRepo.addBudgetAndReturnId(newBudget);
		
		//System.out.print(newBudget.getBudgetId() + newBudget.getBudgetName() + newBudget.getYearMonth());
		
		for (BudgetCategory budgetCategory : budgetCategories) {
		
		
		}
		
		
		return newBudget;
	}
	
	public Budget getBudgetFromYearMonth(YearMonth yearMonth) {
		return budgetRepo.getBudgetByYearMonth(yearMonth);
	}
	
	public List<BudgetWithCategories> getBudgetsWithCategories() {
		List<Budget> allBudgets = getAllBudgets();
		List<BudgetWithCategories> budgetsWithCategories = new ArrayList<>();
		
		for (Budget budget : allBudgets) {
			if (budget != null) {
				budgetsWithCategories.add(new BudgetWithCategories(budget));
			}
		}
		return budgetsWithCategories;
	}
	
	public List<BudgetCategoryModel> getCategoriesForBudget(YearMonth yearMonth) {
		BudgetWithCategories budsAndCats = new BudgetWithCategories(getBudgetFromYearMonth(yearMonth));
		return budsAndCats.getCategories();
	}
	
	public List<Budget> getAllBudgets() {
		return budgetRepo.getAllBudgets();
	}
	
	public void updateCategories() {
		for (BudgetCategory category : budgetCategoryRepo.getAllCategories()) {
			categoryMap.put(category.getCategoryId(), category);
		}
		this.budgetCategories.clear();
		this.budgetCategories = FXCollections.observableArrayList(categoryMap.values());
	}
	
	public ObservableList<BudgetCategory> getAllCategories() {
		updateCategories();
		return this.budgetCategories;
	}
	
	
	public void updateBudgetCategory(BudgetCategoryModel updatedBudgetCategoryModel) {
		
		BudgetCategory category = categoryMap.get(updatedBudgetCategoryModel.getCategoryId());
		if (category != null) {
			category.setCategoryName(updatedBudgetCategoryModel.getCategoryName());
			category.setDescription(updatedBudgetCategoryModel.getDescription());
			budgetCategoryRepo.updateCategory(category);
		}
		
		BudgetCategoryAllocation allocation = budgetCategoryRepo.getAllocationByIds(updatedBudgetCategoryModel.getCategoryId(), updatedBudgetCategoryModel.getBudgetId());
		if (allocation != null) {
			allocation.setAllocatedAmount(updatedBudgetCategoryModel.getAllocatedAmount());
			budgetCategoryRepo.updateBudgetCategoryAllocation(allocation);
		}
	}
	
	
	
	public class BudgetWithCategories {
		private Boolean categoriesLoaded = false;
		private Budget budget;
		private List<BudgetCategoryModel> categories = FXCollections.observableArrayList();
		List<BudgetCategoryAllocation> budgetCategoryAllocations;
		
		private DoubleProperty totalAllocatedAmount = new SimpleDoubleProperty(0);
		private DoubleProperty totalSpentAmount = new SimpleDoubleProperty(0);
		
		public BudgetWithCategories(Budget budget) {
			this.budget = budget;
			budgetCategoryAllocations = getBudgetAllocation(budget.getBudgetId());
			loadCategoriesWithSpentAmount();
		}
		
		private List<BudgetCategoryAllocation> getBudgetAllocation(String budgetId) {
			budgetCategoryAllocations = budgetCategoryRepo.getAllocationsForBudget(budgetId);
			return budgetCategoryAllocations;
		}
		
		public Budget getBudget() {
			return budget;
		}
		
		public List<BudgetCategoryModel> getCategories() {
			if (!categoriesLoaded) {
				loadCategoriesWithSpentAmount();
			}
			return categories;
		}
		
		private void loadCategoriesWithSpentAmount() {
			getBudgetAllocation(budget.getBudgetId());
			try {
				
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
				
				for (BudgetCategoryAllocation allocation : budgetCategoryAllocations) {
					BudgetCategory category = categoryMap.get(allocation.getCategoryId());
					double allocatedAmount = allocation.getAllocatedAmount();
					double spentAmount = categorySpending.getOrDefault(category.getCategoryId(), 0.0);
					// Create and add the model
					BudgetCategoryModel model = new BudgetCategoryModel(
							category.getCategoryId(),
							category.getParentCategoryId(),
							budget.getBudgetId(),
							allocation.getAllocationId(),
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
		
		public Double getTotalAllocatedAmount() { return totalAllocatedAmount.get(); }
	
		public Double getTotalSpentAmount() { return totalSpentAmount.get(); }
	}
}
