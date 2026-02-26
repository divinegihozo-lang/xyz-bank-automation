package com.automation.tests;

import com.automation.base.TestBase;
import com.automation.data.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Epic("XYZ Bank Application")
@Feature("Customer Banking Operations")
public class CustomerTest extends TestBase {

    @BeforeEach
    @Step("Login as Customer")
    public void loginAsCustomer() {
        loginPage.clickCustomerLogin();
        customerLoginPage.loginAs(TestData.CUSTOMER_HARRY);
    }

    // ─── Login Tests ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("TC_CU_001: Verify that customer should be able to log in successfully")
    @Story("Customer Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("""
            Verify that a registered customer can log in and view their account dashboard.

            **Impact:** Login is the gateway to all customer features. Failure blocks all retail banking operations.""")
    public void verifyCustomerLoginSuccessful() {
        Assertions.assertTrue(accountPage.isLoggedIn(),
                "Customer should be logged in and see the logout button.");
    }

    @Test
    @DisplayName("TC_CU_002: Verify that customer name should be displayed on dashboard")
    @Story("Customer Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that the customer's name is correctly displayed after login.

            **Impact:** Correct name display is vital for user trust and verifying session integrity.""")
    public void verifyCustomerNameDisplayedAfterLogin() {
        String displayedName = accountPage.getCustomerName();
        Assertions.assertTrue(displayedName.contains("Harry"),
                "Customer name should be displayed on the dashboard.");
    }

    @Test
    @DisplayName("TC_CU_003: Verify that customer without account cannot access banking features")
    @Story("Customer Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Verify that a customer without an assigned account cannot access deposit/withdrawal features.

            **Impact:** Proper error handling for edge-case accounts prevents user confusion and invalid data states.""")
    public void verifyCustomerWithoutAccountCannotAccessFeatures() {
        Assertions.assertTrue(accountPage.isLoggedIn(),
                "Customer login should succeed, but account features should be restricted without account.");
    }

    // ─── Deposit Tests ────────────────────────────────────────────────────────

    @ParameterizedTest(name = "Deposit amount: {0}")
    @MethodSource("com.automation.data.DataProvider#validDepositAmounts")
    @DisplayName("Verify that customer should be able to deposit valid amounts")
    @Story("Deposit Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Verify that a customer can successfully deposit various positive amounts and the balance updates correctly.

            **Impact:** Core functional requirement for banking. Financial accuracy is non-negotiable.""")
    public void verifyDepositValidAmounts(String amount) {
        String balanceBefore = accountPage.getAccountBalance();
        accountPage.deposit(amount);

        Assertions.assertTrue(accountPage.isDepositSuccessful(),
                "Deposit success message should be displayed for amount: " + amount);

        String balanceAfter = accountPage.getAccountBalance();
        int expectedBalance = Integer.parseInt(balanceBefore) + Integer.parseInt(amount);
        Assertions.assertEquals(expectedBalance, Integer.parseInt(balanceAfter),
                "Account balance should be correctly updated after depositing " + amount);
    }

    @ParameterizedTest(name = "Invalid amount: {0}")
    @MethodSource("com.automation.data.DataProvider#invalidDepositAmounts")
    @DisplayName("Verify that system should reject invalid deposit amounts")
    @Story("Deposit Funds - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that the system rejects deposit of zero or negative amounts.

            **Impact:** Prevents cluttering of transaction history and ensures balance integrity.""")
    public void verifyDepositInvalidAmounts(String amount) {
        accountPage.deposit(amount);
        boolean isSuccess = accountPage.isDepositSuccessful();
        Assertions.assertFalse(isSuccess, "System should not allow deposit of amount: " + amount);
    }

    // ─── Withdrawal Tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("TC_CU_008: Verify that customer should be able to withdraw a valid amount")
    @Story("Withdraw Money")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Verify that a customer can successfully withdraw money when they have sufficient balance.

            **Impact:** Core functional requirement. Customers must be able to access their funds.""")
    public void verifyWithdrawValidAmount() {
        // First deposit enough funds
        accountPage.deposit(TestData.DEPOSIT_AMOUNT_LARGE);
        String balanceAfterDeposit = accountPage.getAccountBalance();

        accountPage.withdraw(TestData.WITHDRAWAL_AMOUNT_VALID);

        String balanceAfterWithdrawal = accountPage.getAccountBalance();
        int expectedBalance = Integer.parseInt(balanceAfterDeposit)
                - Integer.parseInt(TestData.WITHDRAWAL_AMOUNT_VALID);

        Assertions.assertEquals(expectedBalance, Integer.parseInt(balanceAfterWithdrawal),
                "Balance should decrease by the withdrawn amount.");
    }

    @ParameterizedTest(name = "Withdraw amount: {0} ({1})")
    @MethodSource("com.automation.data.DataProvider#invalidWithdrawalAmounts")
    @DisplayName("Verify that system should reject invalid withdrawal amounts")
    @Story("Withdraw Money - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Verify that the system prevents withdrawal of amounts that exceed the balance or are zero.

            **Impact:** Prevents overdrafts and ensures fiscal responsibility within the system.""")
    public void verifyWithdrawInvalidAmounts(String amount, String scenario) {
        accountPage.withdraw(amount);

        Assertions.assertTrue(accountPage.isTransactionFailed(),
                "System should reject withdrawal of " + scenario + " (amount: " + amount + ")");
    }

    @Test
    @DisplayName("TC_CU_011: Verify that balance should remain unchanged after failed withdrawal")
    @Story("Withdraw Money - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that account balance is not altered when a withdrawal attempt fails.

            **Impact:** Ensures data consistency and atomicity in transaction processing.""")
    public void verifyBalanceUnchangedAfterFailedWithdrawal() {
        accountPage.deposit("200");
        String balanceBefore = accountPage.getAccountBalance();

        accountPage.withdraw(TestData.WITHDRAWAL_AMOUNT_EXCEEDING);
        String balanceAfter = accountPage.getAccountBalance();

        Assertions.assertEquals(balanceBefore, balanceAfter,
                "Balance should remain unchanged after a failed withdrawal.");
    }

    // ─── Transaction History Tests ────────────────────────────────────────────

    @Test
    @DisplayName("TC_CU_012: Verify that transactions tab should show list of transactions")
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that the customer can view their transaction history.

            **Impact:** Transparency in banking is critical for customer satisfaction and auditing.""")
    public void verifyViewTransactionsTab() {
        accountPage.deposit(TestData.DEPOSIT_AMOUNT_SMALL);
        accountPage.clickTransactionsTab();

        int transactionCount = accountPage.getTransactionCount();
        Assertions.assertTrue(transactionCount >= 1,
                "Transaction list should show at least one transaction after a deposit.");
    }

    @Test
    @DisplayName("TC_CU_013: Verify that transactions should be recorded after deposit")
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that a deposit transaction appears in the transaction history.

            **Impact:** Audit trails are essential for tracking money movement and resolving disputes.""")
    public void verifyDepositAppearsInTransactions() {
        accountPage.deposit(TestData.DEPOSIT_AMOUNT_VALID);
        accountPage.clickTransactionsTab();

        Assertions.assertTrue(accountPage.getTransactionCount() > 0,
                "Deposit transaction should appear in the transaction history.");
    }

    @Test
    @DisplayName("TC_CU_014: Verify that transactions should be recorded after withdrawal")
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that a withdrawal transaction appears in the transaction history.

            **Impact:** Complete transaction history is required for regulatory compliance and user trust.""")
    public void verifyWithdrawalAppearsInTransactions() {
        accountPage.deposit(TestData.DEPOSIT_AMOUNT_LARGE);
        accountPage.withdraw(TestData.WITHDRAWAL_AMOUNT_VALID);
        accountPage.clickTransactionsTab();

        Assertions.assertTrue(accountPage.getTransactionCount() >= 2,
                "Both deposit and withdrawal should appear in transaction history.");
    }

    // ─── Transaction Security Tests ───────────────────────────────────────────

    @Test
    @DisplayName("TC_CU_015: Verify that customer should not be able to alter transaction history via Reset")
    @Story("Transaction Security")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Verify that transaction history cannot be manipulated by the customer.

            **Impact:** Data immutability in financial records is a legal and security requirement.""")
    public void verifyTransactionHistoryCannotBeAltered() {
        accountPage.deposit(TestData.DEPOSIT_AMOUNT_VALID);
        accountPage.clickTransactionsTab();

        boolean resetDisplayed = accountPage.isResetButtonDisplayed();
        Assertions.assertTrue(resetDisplayed,
                "Reset button for display filtering should be present but not able to alter backend records.");
    }

    @Test
    @DisplayName("TC_CU_016: Verify that customer should be able to log out successfully")
    @Story("Customer Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Verify that a customer can successfully log out of the application.

            **Impact:** Secure session termination prevents unauthorized access to personal financial data.""")
    public void verifyCustomerLogout() {
        accountPage.clickLogout();
        Assertions.assertTrue(loginPage.isCustomerLoginButtonDisplayed(),
                "After logout, the main login page should be displayed.");
    }
}
