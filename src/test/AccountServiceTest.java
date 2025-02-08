import com.moneymanager.core.Account;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {
	private AccountRepo accountRepo;
	private AccountService accountService;
	
	@BeforeEach
	void setUp() {
		// Create a mock repository
		accountRepo = mock(AccountRepo.class);
		accountService = new AccountService(accountRepo);
	}
	
	@Test
	void getAllAccounts_WhenAccountsExist_ShouldReturnAccounts() {
		// Arrange - set up test data
		List<Account> expectedAccounts = Arrays.asList(
				new Account("Checking", "Chase", "DEBT"),
				new Account("Savings", "Wells Fargo", "CREDIT")
		);
		// Tell mockito what to return when getAllAccounts is called
		when(accountRepo.getAllAccounts()).thenReturn(expectedAccounts);
		
		// Act - call the method we're testing
		List<Account> actualAccounts = accountService.getAccountList();
		for (Account account : actualAccounts) {
			System.out.println(account.toString());
		}
		// Assert - verify the results
		assertEquals(2, actualAccounts.size());
		assertEquals("Checking", actualAccounts.get(0).getAccountName());
		assertEquals("Savings", actualAccounts.get(1).getAccountName());
	}
	
	@Test
	void createAccountShouldSucceedWithValidInputs() {
		// Arrange
		String accountName = "Checking";
		String bankName = "Chase";
		String accountType = "DEBT";
		
		// Act
		Account created = accountService.createAccount(accountName, bankName, accountType);
		
		// Assert
		assertNotNull(created);
		assertEquals(accountName, created.getAccountName());
		assertEquals(bankName, created.getBankName());
		assertEquals(accountType, created.getAccountType());
		verify(accountRepo).addAccount(created); // Verify repo was called
	}
	
	@Test
	void createAccountShouldThrowExceptionForEmptyName() {
		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			accountService.createAccount("", "Chase", "DEBT");
		});
		assertEquals("Account name cannot be empty", exception.getMessage());
	}
	
	@Test
	void createAccountShouldThrowExceptionForInvalidType() {
		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			accountService.createAccount("Checking", "Chase", "INVALID");
		});
		assertEquals("Account type must be DEBT or CREDIT", exception.getMessage());
	}
}