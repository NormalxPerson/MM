/*

import com.moneymanager.core.Account;
import com.moneymanager.repos.AccountRepo;
import com.moneymanager.service.AccountService;
import com.moneymanager.ui.view.AccountTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
	
	@Mock
	private AccountRepo accountRepo;
	
	private AccountService accountService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		accountService = new AccountService(accountRepo);
	}
	
	@Test
	void getAccountList_ShouldReturnListFromRepo() {
		// Arrange
		List<Account> expectedAccounts = Arrays.asList(
				new Account("1", "Checking", "Chase", "DEBIT", 100.0),
				new Account("2", "Savings", "Wells Fargo", "DEBIT", 500.0)
		);
		when(accountRepo.getAllAccounts()).thenReturn(expectedAccounts);
		
		// Act
		List<Account> actualAccounts = accountService.getAccountList();
		
		// Assert
		assertEquals(2, actualAccounts.size());
		assertEquals("Checking", actualAccounts.get(0).getAccountName());
		assertEquals("Savings", actualAccounts.get(1).getAccountName());
		verify(accountRepo).getAllAccounts();
	}
	
	@Test
	void createAndAddAccount_ShouldReturnNewAccountModel() {
		// Arrange
		String accountName = "Test Account";
		String bankName = "Test Bank";
		String accountType = "DEBIT";
		Double balance = 200.0;
		
		Account newAccount = new Account(accountName, bankName, accountType, balance);
		String generatedId = "test-id-123";
		
		when(accountRepo.addAccountAndReturnId(any(Account.class))).thenReturn(generatedId);
		
		HashMap<String, Account> accountMap = new HashMap<>();
		accountMap.put(generatedId, newAccount);
		when(accountRepo.getAccountMap()).thenReturn(accountMap);
		
		// Act
		AccountTableView.AccountModel result = accountService.createAndAddAccount(
				accountName, bankName, accountType, balance);
		
		// Assert
		assertNotNull(result);
		assertEquals(accountName, result.getAccountName());
		assertEquals(bankName, result.getBankName());
		assertEquals(accountType, result.getAccountType().name());
		assertEquals(balance, result.getAccountBalance());
		assertEquals(generatedId, result.getAccountId());
		verify(accountRepo).addAccountAndReturnId(any(Account.class));
	}

	@Test
	void getAccountNameByAccountId_ShouldReturnAccountName() {
		// Arrange
		String accountId = "test-id-123";
		String expectedName = "Test Account";
		
		Account account = new Account("1", expectedName, "Test Bank", "DEBIT", 100.0);
		System.out.println(account.toString());
		HashMap<String, Account> accountMap = new HashMap<>();
		accountMap.put("1", account);
		
		// Act
		String result = accountMap.get("1").getAccountName();
		
		// Assert
		assertEquals(expectedName, result);
	}
}*/
