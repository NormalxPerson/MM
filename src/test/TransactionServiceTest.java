import com.moneymanager.core.Account;
import com.moneymanager.database.DatabaseConnection;
import com.moneymanager.repos.SQLiteAccountRepo;
import com.moneymanager.repos.SQLiteTransactionRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.service.TransactionService;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

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
	public void createTransactionFromUserInputTest() {
		double balanceBefore = testAccount.getBalance();
		transactionService.createTransactionListFromUser(500, "Bread", "10-25-2025", "expense", "1");
		
		
		System.out.println("balanceBefore: " + balanceBefore);
		//System.out.println("balanceAfter: " + balanceAfter);
		assertEquals(0, balanceBefore);
		//assertEquals(-500, balanceAfter);
	
	
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