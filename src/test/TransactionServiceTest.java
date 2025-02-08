import com.moneymanager.core.Account;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionServiceTest {
	
	private SQLiteAccountRepo accountRepo;
	private AccountService accountService;
	private SQLiteTransactionRepo transactionRepo;
	private TransactionService transactionService;
	private Account testAccount;
	
	@BeforeEach
	void setUp() {
		resetDatabase();
		// Initialize the repositories (connection obtained internally from the singleton)
		accountRepo = new SQLiteAccountRepo();
		accountService = new AccountService(accountRepo);
		transactionRepo = new SQLiteTransactionRepo();
		// Initialize the service
		transactionService = new TransactionService(transactionRepo, accountService);
		// Reset and initialize the database state
		
		accountService.createAccount("Test Name", "Test Bank", "DEBT");
		testAccount = accountService.getAccountList().get(0);
	}
	
	
	@AfterEach
	void tearDown() {
		// Reset the database after each test
		resetDatabase();
	}
	
	@Test
	public void getTestAccount() {
		// Create the account and retrieve its database-generated ID
		Account retrievedAccount = testAccount;
		System.out.println(testAccount.toString());
		// Fetch the account from the database using its generated ID
		
		// Assert the balance is initialized to 0
		//assertNotNull(retrievedAccount); // Ensure the account is retrieved
		assertEquals(0, retrievedAccount.getBalance()); // Verify initial balance is 0
		
		// Assert other fields
		assertEquals("Test Name", retrievedAccount.getAccountName());
		assertEquals("Test Bank", retrievedAccount.getBankName());
		assertEquals("DEBT", retrievedAccount.getAccountType());
	}
	
	@Test
	public void addTransactionShouldUpdateAccountBalance() {
		double initialBalance = testAccount.getBalance();  // Get initial balance
		// Add a transaction (using your transaction service)
		transactionService.createTransactionFromUser(500, "Test Transaction", "12-25-2024", "income", testAccount.getAccountId());  // Assuming account ID is 1
		
		// Retrieve the updated account from the database (using your account service)
		Account updatedAccount = accountService.getAccountByAccountId(testAccount.getAccountId());  // Replace with actual account ID if needed
		
		// Assert that the account balance has been updated correctly
		assertEquals(initialBalance + 500, updatedAccount.getBalance(), 0.001);  // Use delta for double comparison
	}
	
	@Test
	public void addMultipleTransactionsShouldUpdateAccountBalance() {
		double initialBalance = testAccount.getBalance();  // Get initial balance
		
		// Add multiple transactions
		transactionService.createTransactionFromUser(500, "Test Transaction 1", "12-25-2024", "income", testAccount.getAccountId());
		transactionService.createTransactionFromUser(300, "Test Transaction 2", "12-26-2024", "income", testAccount.getAccountId());
		transactionService.createTransactionFromUser(100, "Test Transaction 3", "12-27-2024", "expense", testAccount.getAccountId());  // Expense should subtract from balance
		
		// Retrieve the updated account
		Account updatedAccount = accountService.getAccountByAccountId(testAccount.getAccountId());
		
		// Assert the final balance
		assertEquals(initialBalance + 500 + 300 - 100, updatedAccount.getBalance(), 0.001);  // Expected balance after all transactions
	}
	
	
	private void resetDatabase() {
		try {
			// Get the singleton connection
			DatabaseConnection connection = DatabaseConnection.getInstance();
			connection.getConnection().createStatement().execute("PRAGMA foreign_keys = OFF");
			// Clear the database tables
			connection.getConnection().createStatement().execute("DELETE FROM csv_strategies");
			connection.getConnection().createStatement().execute("DELETE FROM transactions");
			connection.getConnection().createStatement().execute("DELETE FROM accounts");
			
			connection.getConnection().createStatement().execute("PRAGMA foreign_keys = ON");
			connection.getConnection().createStatement().execute("DELETE FROM sqlite_sequence WHERE name IN ('accounts', 'transactions', 'csv_strategies')");
		} catch (Exception e) {
			throw new RuntimeException("Failed to reset database", e);
		}
	}
}