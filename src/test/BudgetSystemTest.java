

import com.moneymanager.core.Budget;
import com.moneymanager.core.BudgetCategory;
import com.moneymanager.repos.BudgetRepo;
import com.moneymanager.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Comprehensive test suite for the Budget system in Money Manager
 */

@ExtendWith(MockitoExtension.class)
public class BudgetSystemTest {
	
	@Mock
	private BudgetRepo budgetRepo;
	
	@Mock
	private BudgetService budgetService;
	
	private Budget testBudget;
	private BudgetCategory testCategory;
	private List<BudgetCategory> testCategories;
	
	@BeforeEach
	void setUp() {
		// Set up test data
		String monthName = YearMonth.now().getMonth().toString();
		testBudget = new Budget(YearMonth.now());
		testCategory = new BudgetCategory(testBudget.getBudgetId(), "Groceries", "Food and household items", 500.0);
		
		testCategories = new ArrayList<>();
		testCategories.add(testCategory);
		testCategories.add(new BudgetCategory(testBudget.getBudgetId(), "Utilities", "Electricity, water, gas", 200.0));
		testCategories.add(new BudgetCategory(testBudget.getBudgetId(), "Entertainment", "Movies, games, events", 150.0));
	}
	
	// ======== MODEL TESTS ========
	
	@Test
	void testBudgetCreation() {
		// Test that a Budget object is created correctly
		String budgetName = "APRIL Budget";
		YearMonth yearMonth = YearMonth.now();
		
		Budget budget = new Budget(YearMonth.now());
		
		assertNotNull(budget);
		assertNotNull(budget.getBudgetId());
		assertEquals(budgetName, budget.getBudgetName());
		assertEquals(yearMonth, budget.getYearMonth());
		assertEquals(testBudget.getBudgetName(), budget.getBudgetName());
	}
	
	@Test
	void testBudgetCategoryCreation() {
		// Test that a BudgetCategory object is created correctly
		String budgetId = testBudget.getBudgetId();
		String categoryName = "Test Category";
		String description = "Test description";
		double amount = 100.0;
		
		BudgetCategory category = new BudgetCategory(budgetId, categoryName, description, amount);
		
		assertNotNull(category);
		assertNotNull(category.getCategoryId());
		assertEquals(budgetId, category.getBudgetId());
		assertEquals(categoryName, category.getCategoryName());
		assertEquals(description, category.getDescription());
		assertEquals(amount, category.getAllocatedAmount());
	}
	
	@Test
	void testBudgetNameUpdate() {
		// Test that a Budget's name can be updated
		String newName = "Updated Budget Name";
		testBudget.setBudgetName(newName);
		assertEquals(newName, testBudget.getBudgetName());
	}
	
	
	@Test
	void testBudgetCategoryUpdate() {
		// Test that a BudgetCategory's properties can be updated
		String newName = "Updated Category";
		String newDescription = "Updated description";
		double newAmount = 200.0;
		
		testCategory.setCategoryName(newName);
		testCategory.setDescription(newDescription);
		testCategory.setAllocatedAmount(newAmount);
		
		assertEquals(newName, testCategory.getCategoryName());
		assertEquals(newDescription, testCategory.getDescription());
		assertEquals(newAmount, testCategory.getAllocatedAmount());
	}
	
	@Test
	void testBudgetEquality() {
		// Test that Budget equals() and hashCode() work correctly
		Budget budget1 = new Budget(YearMonth.now());
		Budget budget2 = new Budget(YearMonth.now().plusMonths(1));
		
		// Use reflection to set same ID for testing equals
		try {
			java.lang.reflect.Field idField = Budget.class.getDeclaredField("budgetId");
			idField.setAccessible(true);
			String sharedId = "shared-id-for-test";
			idField.set(budget1, sharedId);
			idField.set(budget2, sharedId);
			
			assertNotEquals(budget1, budget2);
			assertNotEquals(budget1.hashCode(), budget2.hashCode());
		} catch (Exception e) {
			fail("Failed to set budget ID via reflection: " + e.getMessage());
		}
	}
	
	@Test
	void testAddingBudgetCategory() {}
}
	/*
	@Test
	void testBudgetCategoryEquality() {
		// Test that BudgetCategory equals() and hashCode() work correctly
		BudgetCategory category1 = new BudgetCategory("budget-id", "Category 1", "Desc 1", 100.0);
		BudgetCategory category2 = new BudgetCategory("budget-id", "Category 2", "Desc 2", 200.0);
		
		// Use reflection to set same IDs for testing equals
		try {
			java.lang.reflect.Field idField = BudgetCategory.class.getDeclaredField("categoryId");
			idField.setAccessible(true);
			String sharedId = "shared-category-id";
			idField.set(category1, sharedId);
			idField.set(category2, sharedId);
			
			assertEquals(category1, category2);
			assertEquals(category1.hashCode(), category2.hashCode());
		} catch (Exception e) {
			fail("Failed to set category ID via reflection: " + e.getMessage());
		}
	}
	
	// ======== REPOSITORY TESTS ========
	
	@Test
	void testSaveBudget() {
		// Configure mock repository
		when(budgetRepo.addBudget(any(Budget.class))).thenReturn(testBudget.getBudgetId());
		
		// Test saving a budget
		String savedId = budgetRepo.addBudget(testBudget);
		
		assertNotNull(savedId);
		assertEquals(testBudget.getBudgetId(), savedId);
		verify(budgetRepo).addBudget(testBudget);
	}
	
	@Test
	void testGetBudgetById() {
		// Configure mock repository
		when(budgetRepo.getBudgetById(testBudget.getBudgetId())).thenReturn(testBudget);
		
		// Test retrieving a budget by ID
		Budget retrievedBudget = budgetRepo.getBudgetById(testBudget.getBudgetId());
		
		assertNotNull(retrievedBudget);
		assertEquals(testBudget.getBudgetId(), retrievedBudget.getBudgetId());
		verify(budgetRepo).getBudgetById(testBudget.getBudgetId());
	}
	
	@Test
	void testGetBudgetByYearMonth() {
		// Configure mock repository
		when(budgetRepo.getBudgetByYearMonth(testBudget.getYearMonth())).thenReturn(testBudget);
		
		// Test retrieving a budget by year-month
		Budget retrievedBudget = budgetRepo.getBudgetByYearMonth(testBudget.getYearMonth());
		
		assertNotNull(retrievedBudget);
		assertEquals(testBudget.getYearMonth(), retrievedBudget.getYearMonth());
		verify(budgetRepo).getBudgetByYearMonth(testBudget.getYearMonth());
	}
	
	@Test
	void testGetAllBudgets() {
		// Create a list of budgets for testing
		List<Budget> budgets = new ArrayList<>();
		budgets.add(testBudget);
		budgets.add(new Budget("Next Month", YearMonth.now().plusMonths(1)));
		
		// Configure mock repository
		when(budgetRepo.getAllBudgets()).thenReturn(budgets);
		
		// Test retrieving all budgets
		List<Budget> retrievedBudgets = budgetRepo.getAllBudgets();
		
		assertNotNull(retrievedBudgets);
		assertEquals(2, retrievedBudgets.size());
		verify(budgetRepo).getAllBudgets();
	}
	
	@Test
	void testUpdateBudget() {
		// Configure mock repository
		doNothing().when(budgetRepo).updateBudget(any(Budget.class));
		
		// Test updating a budget
		testBudget.setBudgetName("Updated Budget Name");
		budgetRepo.updateBudget(testBudget);
		
		verify(budgetRepo).updateBudget(testBudget);
	}
	
	@Test
	void testDeleteBudget() {
		// Configure mock repository
		when(budgetRepo.deleteBudget(testBudget.getBudgetId())).thenReturn(1);
		
		// Test deleting a budget
		int rowsDeleted = budgetRepo.deleteBudget(testBudget.getBudgetId());
		
		assertEquals(1, rowsDeleted);
		verify(budgetRepo).deleteBudget(testBudget.getBudgetId());
	}
	
	@Test
	void testAddBudgetCategory() {
		// Configure mock repository
		when(budgetRepo.addCategory(any(BudgetCategory.class))).thenReturn(testCategory.getCategoryId());
		
		// Test adding a budget category
		String savedId = budgetRepo.addCategory(testCategory);
		
		assertNotNull(savedId);
		assertEquals(testCategory.getCategoryId(), savedId);
		verify(budgetRepo).addCategory(testCategory);
	}
	
	@Test
	void testGetCategoriesByBudgetId() {
		// Configure mock repository
		when(budgetRepo.getCategoriesByBudgetId(testBudget.getBudgetId())).thenReturn(testCategories);
		
		// Test retrieving categories by budget ID
		List<BudgetCategory> retrievedCategories = budgetRepo.getCategoriesByBudgetId(testBudget.getBudgetId());
		
		assertNotNull(retrievedCategories);
		assertEquals(testCategories.size(), retrievedCategories.size());
		verify(budgetRepo).getCategoriesByBudgetId(testBudget.getBudgetId());
	}
	
	@Test
	void testUpdateBudgetCategory() {
		// Configure mock repository
		doNothing().when(budgetRepo).updateCategory(any(BudgetCategory.class));
		
		// Test updating a budget category
		testCategory.setCategoryName("Updated Category Name");
		testCategory.setAllocatedAmount(300.0);
		budgetRepo.updateCategory(testCategory);
		
		verify(budgetRepo).updateCategory(testCategory);
	}
	
	@Test
	void testDeleteBudgetCategory() {
		// Configure mock repository
		when(budgetRepo.deleteCategory(testCategory.getCategoryId())).thenReturn(1);
		
		// Test deleting a budget category
		int rowsDeleted = budgetRepo.deleteCategory(testCategory.getCategoryId());
		
		assertEquals(1, rowsDeleted);
		verify(budgetRepo).deleteCategory(testCategory.getCategoryId());
	}
	
	// ======== SERVICE TESTS ========
	
	@Test
	void testCreateBudget() {
		// Configure mock service
		String budgetName = "Test Budget";
		YearMonth yearMonth = YearMonth.now();
		when(budgetService.createBudget(budgetName, yearMonth)).thenReturn(testBudget);
		
		// Test creating a budget via service
		Budget createdBudget = budgetService.createBudget(budgetName, yearMonth);
		
		assertNotNull(createdBudget);
		assertEquals(budgetName, createdBudget.getBudgetName());
		assertEquals(yearMonth, createdBudget.getYearMonth());
		verify(budgetService).createBudget(budgetName, yearMonth);
	}
	
	@Test
	void testAddCategoryToBudget() {
		// Configure mock service
		when(budgetService.addCategoryToBudget(
				testBudget.getBudgetId(),
				testCategory.getCategoryName(),
				testCategory.getDescription(),
				testCategory.getAllocatedAmount()
		)).thenReturn(testCategory);
		
		// Test adding a category to a budget
		BudgetCategory addedCategory = budgetService.addCategoryToBudget(
				testBudget.getBudgetId(),
				testCategory.getCategoryName(),
				testCategory.getDescription(),
				testCategory.getAllocatedAmount()
		);
		
		assertNotNull(addedCategory);
		assertEquals(testCategory.getCategoryName(), addedCategory.getCategoryName());
		assertEquals(testCategory.getAllocatedAmount(), addedCategory.getAllocatedAmount());
		verify(budgetService).addCategoryToBudget(
				testBudget.getBudgetId(),
				testCategory.getCategoryName(),
				testCategory.getDescription(),
				testCategory.getAllocatedAmount()
		);
	}
	
	@Test
	void testGetBudgetModels() {
		// Create expected models list
		List<BudgetTableView.BudgetModel> expectedModels = new ArrayList<>();
		
		// Configure mock service
		when(budgetService.getBudgetModelObservableList()).thenReturn(expectedModels);
		
		// Test getting budget models
		List<BudgetTableView.BudgetModel> models = budgetService.getBudgetModelObservableList();
		
		assertNotNull(models);
		verify(budgetService).getBudgetModelObservableList();
	}
	
	@Test
	void testCalculateCategorySpending() {
		// Setup test data
		String categoryId = testCategory.getCategoryId();
		YearMonth yearMonth = YearMonth.now();
		double expectedSpending = 350.0;
		
		// Configure mock service
		when(budgetService.calculateCategorySpending(categoryId, yearMonth)).thenReturn(expectedSpending);
		
		// Test calculating category spending
		double actualSpending = budgetService.calculateCategorySpending(categoryId, yearMonth);
		
		assertEquals(expectedSpending, actualSpending);
		verify(budgetService).calculateCategorySpending(categoryId, yearMonth);
	}
	
	@Test
	void testGetBudgetSummary() {
		// Setup test data
		YearMonth yearMonth = YearMonth.now();
		Map<String, Double> expectedSummary = new HashMap<>();
		expectedSummary.put("Groceries", 350.0);
		expectedSummary.put("Utilities", 175.0);
		expectedSummary.put("Entertainment", 120.0);
		
		// Configure mock service
		when(budgetService.getBudgetSummary(yearMonth)).thenReturn(expectedSummary);
		
		// Test getting budget summary
		Map<String, Double> actualSummary = budgetService.getBudgetSummary(yearMonth);
		
		assertNotNull(actualSummary);
		assertEquals(expectedSummary.size(), actualSummary.size());
		assertEquals(expectedSummary.get("Groceries"), actualSummary.get("Groceries"));
		verify(budgetService).getBudgetSummary(yearMonth);
	}
	
	@Test
	void testAssignTransactionToCategory() {
		// Setup test data
		String transactionId = "test-transaction-id";
		String categoryId = testCategory.getCategoryId();
		
		// Configure mock service
		doNothing().when(budgetService).assignTransactionToCategory(transactionId, categoryId);
		
		// Test assigning transaction to category
		budgetService.assignTransactionToCategory(transactionId, categoryId);
		
		verify(budgetService).assignTransactionToCategory(transactionId, categoryId);
	}
	
	@Test
	void testGetTransactionsByCategoryId() {
		// Setup test data
		String categoryId = testCategory.getCategoryId();
		List<Transaction> expectedTransactions = new ArrayList<>();
		
		// Configure mock service
		when(budgetService.getTransactionsByCategoryId(categoryId)).thenReturn(expectedTransactions);
		
		// Test getting transactions by category ID
		List<Transaction> actualTransactions = budgetService.getTransactionsByCategoryId(categoryId);
		
		assertNotNull(actualTransactions);
		verify(budgetService).getTransactionsByCategoryId(categoryId);
	}
	
	@Test
	void testCopyBudget() {
		// Setup test data
		YearMonth sourceMonth = YearMonth.now();
		YearMonth targetMonth = YearMonth.now().plusMonths(1);
		Budget expectedNewBudget = new Budget("Copy of Monthly Budget", targetMonth);
		
		// Configure mock service
		when(budgetService.copyBudget(sourceMonth, targetMonth)).thenReturn(expectedNewBudget);
		
		// Test copying a budget to a new month
		Budget copiedBudget = budgetService.copyBudget(sourceMonth, targetMonth);
		
		assertNotNull(copiedBudget);
		assertEquals(targetMonth, copiedBudget.getYearMonth());
		verify(budgetService).copyBudget(sourceMonth, targetMonth);
	}
	
	// ======== INTEGRATION TESTS ========
	// These would be better implemented as actual integration tests,
	// but here's a sketch of what to test when your components are ready
	
	@Test
	void testBudgetRepoServiceIntegration() {
		// This would be an actual integration test with a real DB connection
		BudgetRepo realRepo = new SQLiteBudgetRepo();
		BudgetService service = new BudgetService(realRepo);
		
		// Test budget creation and retrieval flow
		Budget budget = service.createBudget("Integration Test Budget", YearMonth.now());
		assertNotNull(budget);
		
		BudgetCategory category = service.addCategoryToBudget(
				budget.getBudgetId(), "Test Category", "Test Description", 100.0);
		assertNotNull(category);
		
		List<BudgetCategory> categories = service.getCategoriesByBudgetId(budget.getBudgetId());
		assertFalse(categories.isEmpty());
		
		// Clean up
		service.deleteBudget(budget.getBudgetId());
	}
}

*/