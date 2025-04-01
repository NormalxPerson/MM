
import com.moneymanager.core.Account;
import com.moneymanager.core.Transaction;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import com.moneymanager.ui.view.AccountTableView;
import com.moneymanager.ui.view.TransactionTableView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {
	
	private SQLiteAccountRepo accountRepo;
	private AccountService accountService;
	private SQLiteTransactionRepo transactionRepo;
	private TransactionService transactionService;
	private String testAccountId;
	
	@BeforeEach
	void setUp() {
		resetDatabase();
		// Initialize the repositories
		accountRepo = new SQLiteAccountRepo();
		accountService = new AccountService(accountRepo);
		transactionRepo = new SQLiteTransactionRepo();
		// Initialize the service
		transactionService = new TransactionService(transactionRepo, accountService);
		
		// Create a test account and store its ID
		AccountTableView.AccountModel accountModel = accountService.createAndAddAccount("Test Account", "Test Bank", "DEBIT", 0.0);
		System.out.println("Test AccountId: " + accountModel.getAccountId());
		testAccountId = accountModel.getAccountId();
	}
	
	@AfterEach
	void tearDown() {
		// Reset the database after each test
		resetDatabase();
	}
	
	@Test
	public void testGetTestAccount() {
		// Get account from the map
		Account testAccount = accountService.getAccountMap().get(testAccountId);
		
		// Verify the test account was created properly
		assertNotNull(testAccount);
		assertEquals("Test Account", testAccount.getAccountName());
		assertEquals("Test Bank", testAccount.getBankName());
		assertEquals(Account.AccountType.DEBIT, testAccount.getAccountType());
		assertEquals(0.0, testAccount.getBalance(), 0.001);
	}
	
	@Test
	public void testCreateTransactionFromUser() {
		// Get the account
		Account testAccount = accountService.getAccountMap().get(testAccountId);
		double initialBalance = testAccount.getBalance();
		
		// Create a transaction
		double amount = 500.0;
		String description = "Test Transaction";
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("M-d-yy"));
		String type = "INCOME";
		
		transactionService.createTransactionFromUser(amount, description, date, type, testAccountId, null);
		
		// Verify transaction was created
		List<Transaction> transactions = transactionRepo.getAllTransactions();
		assertEquals(1, transactions.size());
		
		Transaction transaction = transactions.get(0);
		assertEquals(amount, transaction.getAmount());
		assertEquals(description, transaction.getDescription());
		assertEquals(type.toUpperCase(), transaction.getType().name());
		assertEquals(testAccountId, transaction.getAccountId());
		
		// Verify account balance was updated
		Account updatedAccount = accountService.getAccountMap().get(testAccountId);
		assertEquals(initialBalance + amount, updatedAccount.getBalance(), 0.001);
	}
	
	@Test
	public void testCreateAndAddTransaction() {
		// Get the account
		Account testAccount = accountService.getAccountMap().get(testAccountId);
		double initialBalance = testAccount.getBalance();
		
		// Create transaction data
		Map<String, Object> fieldValues = new HashMap<>();
		fieldValues.put("transactionAmount", "300.0");
		fieldValues.put("transactionDescription", "Test Add Transaction");
		fieldValues.put("transactionDate", LocalDate.now());
		fieldValues.put("transactionType", TransactionTableView.TransactionModel.TransactionType.INCOME);
		
		// Get the account model from the account service
		AccountTableView.AccountModel accountModel = accountService.getAccountModelMap().get(testAccountId);
		fieldValues.put("transactionAccount", accountModel);
		
		// Create the transaction
		TransactionTableView.TransactionModel result = transactionService.createAndAddTransaction(fieldValues);
		
		// Verify transaction was created
		assertNotNull(result);
		assertEquals("Test Add Transaction", result.getTransactionDescription());
		assertEquals(300.0, result.getTransactionAmount());
		assertEquals(testAccountId, result.getTransactionAccountId());
		
		// Verify account balance was updated
		Account updatedAccount = accountService.getAccountMap().get(testAccountId);
		assertEquals(initialBalance + 300.0, updatedAccount.getBalance(), 0.001);
	}
	
	@Test
	public void testAddMultipleTransactions() {
		// Get the account
		Account testAccount = accountService.getAccountMap().get(testAccountId);
		double initialBalance = testAccount.getBalance();
		
		// Add multiple transactions
		transactionService.createTransactionFromUser(500, "Test Income 1",
				LocalDate.now().format(DateTimeFormatter.ofPattern("M-d-yy")),
				"INCOME", testAccountId, null);
		
		transactionService.createTransactionFromUser(300, "Test Income 2",
				LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("M-d-yy")),
				"INCOME", testAccountId, null);
		
		transactionService.createTransactionFromUser(200, "Test Expense",
				LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("M-d-yy")),
				"EXPENSE", testAccountId, null);
		
		// Verify transactions were created
		List<Transaction> transactions = transactionRepo.getAllTransactions();
		assertEquals(3, transactions.size());
		
		// Verify account balance reflects all transactions
		Account updatedAccount = accountService.getAccountMap().get(testAccountId);
		// Income adds, expense subtracts
		assertEquals(initialBalance + 500 + 300 - 200, updatedAccount.getBalance(), 0.001);
	}
	
	@Test
	public void testDeleteTransaction() {
		// Create a transaction
		TransactionTableView.TransactionModel model = transactionService.createTransactionFromUser(500, "Transaction to Delete",
				LocalDate.now().format(DateTimeFormatter.ofPattern("M-d-yy")),
				"INCOME", testAccountId, null);
		
		// Get the transaction
		List<Transaction> transactions = transactionRepo.getAllTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		
		// Delete the transaction
		int result = transactionService.deleteTransaction(model);
		
		// Verify deletion
		assertEquals(1, result);
		List<Transaction> afterDelete = transactionRepo.getAllTransactions();
		assertEquals(0, afterDelete.size());
	}
	
	@Test
	public void testUpdateTransaction() {
		// Create a transaction
		TransactionTableView.TransactionModel model = transactionService.createTransactionFromUser(500, "Original Description", LocalDate.now().format(DateTimeFormatter.ofPattern("M-d-yy")), "INCOME", "1", null);
		
		// Get the transaction
		List<Transaction> transactions = transactionRepo.getAllTransactions();
		assertEquals(1, transactions.size());
		Transaction transaction = transactions.get(0);
		
		// Create a transaction model for update
		model.setTransactionDescription("Updated Description");
		// Update the transaction
		transactionService.updateTransaction(model);
		
		// Verify update
		List<Transaction> afterUpdate = transactionRepo.getAllTransactions();
		assertEquals(1, afterUpdate.size());
		
		// Verify description was updated by querying the database
		List<Transaction> updatedTransactions = transactionRepo.getAllTransactions();
		Transaction updated = updatedTransactions.get(0);
		assertEquals("Updated Description", updated.getDescription());
	}
	
	private void resetDatabase() {
		try {
			// Get the singleton connection
			DatabaseConnection connection = DatabaseConnection.getInstance();
			connection.getConnection().createStatement().execute("PRAGMA foreign_keys = OFF");
			// Clear the database tables
			connection.getConnection().createStatement().execute("DELETE FROM budget_categories");
			connection.getConnection().createStatement().execute("DELETE FROM budgets");
			connection.getConnection().createStatement().execute("DELETE FROM csv_strategies");
			connection.getConnection().createStatement().execute("DELETE FROM transactions");
			connection.getConnection().createStatement().execute("DELETE FROM accounts");
			
			connection.getConnection().createStatement().execute("PRAGMA foreign_keys = ON");
			connection.getConnection().createStatement().execute("DELETE FROM sqlite_sequence WHERE name IN ('accounts', 'transactions', 'csv_strategies', 'budgets', 'budget_categories')");
		} catch (SQLException e) {
			throw new RuntimeException("Failed to reset database", e);
		}
	}
}