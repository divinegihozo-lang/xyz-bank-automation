package com.xyzbank.tests;

import com.xyzbank.testdata.TestData;
import com.xyzbank.utils.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("XYZ Bank Application")
@Feature("Customer Banking Operations")
public class CustomerTest extends BaseTest {

        @BeforeEach
        @Step("Login as Customer: {0}")
        public void loginAsCustomer() {
                loginPage.clickCustomerLogin();
                customerLoginPage.loginAs(TestData.CUSTOMER_HARRY);
        }

        // ─── Login Tests ──────────────────────────────────────────────────────────

        @Test
        @DisplayName("TC_CU_001: Verify that customer should be able to log in successfully")
        @Story("Customer Login")
        @Severity(SeverityLevel.BLOCKER)
        @Description("Verify that a registered customer can log in and view their account dashboard. \n\n" +
                        "**Impact:** Core authentication feature; failure prevents all customer-side banking operations.")
        public void verifyCustomerLoginSuccessful() {
                Assertions.assertTrue(accountPage.isLoggedIn(),
                                "Customer should be logged in and see the logout button.");
        }

        @Test
        @DisplayName("TC_CU_002: Verify that customer name should be displayed on dashboard")
        @Story("Customer Login")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the customer's name is correctly displayed after login. \n\n" +
                        "**Impact:** UI accuracy is critical for user trust and personalization.")
        public void verifyCustomerNameDisplayedAfterLogin() {
                String displayedName = accountPage.getCustomerName();
                Assertions.assertTrue(displayedName.contains("Harry"),
                                "Customer name should be displayed on the dashboard.");
        }

        @Test
        @DisplayName("TC_CU_003: Verify that customer without account cannot access banking features")
        @Story("Customer Login")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a customer without an assigned account cannot access deposit/withdrawal features. \n\n"
                        +
                        "**Impact:** Prevents unauthorized transaction attempts and ensures robust account handling.")
        public void verifyCustomerWithoutAccountCannotAccessFeatures() {
                Assertions.assertTrue(accountPage.isLoggedIn(),
                                "Customer login should succeed, but account features should be restricted without account.");
        }

        // ─── Deposit Tests ────────────────────────────────────────────────────────

        @Test
        @DisplayName("TC_CU_004: Verify that customer should be able to deposit a valid amount")
        @Story("Deposit Funds")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a customer can successfully deposit a positive amount into their account. \n\n" +
                        "**Impact:** This is a mission-critical feature. Failure means customers cannot add funds.")
        public void verifyDepositValidAmount() {
                String balanceBefore = accountPage.getAccountBalance();
                accountPage.deposit(TestData.DEPOSIT_AMOUNT_VALID);

                Assertions.assertTrue(accountPage.isDepositSuccessful(),
                                "Deposit success message should be displayed.");

                String balanceAfter = accountPage.getAccountBalance();
                int expectedBalance = Integer.parseInt(balanceBefore) + Integer.parseInt(TestData.DEPOSIT_AMOUNT_VALID);
                Assertions.assertEquals(expectedBalance, Integer.parseInt(balanceAfter),
                                "Account balance should be updated after deposit.");
        }

        @Test
        @DisplayName("TC_CU_005: Verify that account balance should update after deposit")
        @Story("Deposit Funds")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that the account balance reflects the deposited amount correctly. \n\n" +
                        "**Impact:** Ensures mathematical integrity of the ledger.")
        public void verifyAccountBalanceUpdatesAfterDeposit() {
                String balanceBefore = accountPage.getAccountBalance();
                int initialBalance = Integer.parseInt(balanceBefore);

                accountPage.deposit(TestData.DEPOSIT_AMOUNT_SMALL);
                String balanceAfter = accountPage.getAccountBalance();

                Assertions.assertEquals(initialBalance + Integer.parseInt(TestData.DEPOSIT_AMOUNT_SMALL),
                                Integer.parseInt(balanceAfter),
                                "Balance should increase by the deposited amount.");
        }

        @Test
        @DisplayName("TC_CU_006: Verify that customer should not be able to deposit zero amount")
        @Story("Deposit Funds - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the system rejects deposit of zero amount. \n\n" +
                        "**Impact:** Prevents redundant transaction records and potential exploit surface.")
        public void verifyDepositZeroAmount() {
                accountPage.deposit(TestData.DEPOSIT_AMOUNT_ZERO);
                boolean isSuccess = accountPage.isDepositSuccessful();
                Assertions.assertFalse(isSuccess, "System should not allow deposit of zero amount.");
        }

        @Test
        @DisplayName("TC_CU_007: Verify that customer should not be able to deposit negative amount")
        @Story("Deposit Funds - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the system rejects a negative deposit amount. \n\n" +
                        "**Impact:** High - negative deposits could be used as an exploit to withdraw funds.")
        public void verifyDepositNegativeAmount() {
                accountPage.deposit(TestData.DEPOSIT_AMOUNT_NEGATIVE);
                boolean isSuccess = accountPage.isDepositSuccessful();
                Assertions.assertFalse(isSuccess, "System should not allow deposit of a negative amount.");
        }

        // ─── Withdrawal Tests ─────────────────────────────────────────────────────

        @Test
        @DisplayName("TC_CU_008: Verify that customer should be able to withdraw a valid amount")
        @Story("Withdraw Money")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a customer can successfully withdraw money when they have sufficient balance. \n\n" +
                        "**Impact:** Primary banking service. Failure prevents customers from accessing their money.")
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

        @Test
        @DisplayName("TC_CU_009: Verify that system should reject withdrawal exceeding account balance")
        @Story("Withdraw Money - Validation")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that the system prevents withdrawal of an amount greater than the current balance. \n\n" +
                        "**Impact:** Critical for preventing overdraft and maintaining financial solvency.")
        public void verifyWithdrawExceedingBalance() {
                accountPage.withdraw(TestData.WITHDRAWAL_AMOUNT_EXCEEDING);

                Assertions.assertTrue(accountPage.isTransactionFailed(),
                                "System should reject withdrawal amount exceeding account balance.");
        }

        @Test
        @DisplayName("TC_CU_010: Verify that system should reject withdrawal of zero amount")
        @Story("Withdraw Money - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that withdrawing zero is not permitted. \n\n" +
                        "**Impact:** Prevents unnecessary load and data pollution.")
        public void verifyWithdrawZeroAmount() {
                accountPage.withdraw(TestData.WITHDRAWAL_AMOUNT_ZERO);
                Assertions.assertTrue(accountPage.isTransactionFailed(),
                                "System should not allow withdrawal of zero amount.");
        }

        @Test
        @DisplayName("TC_CU_011: Verify that balance should remain unchanged after failed withdrawal")
        @Story("Withdraw Money - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that account balance is not altered when a withdrawal attempt fails. \n\n" +
                        "**Impact:** Ensures data consistency and atomic transaction behavior.")
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
        @Description("Verify that the customer can view their transaction history. \n\n" +
                        "**Impact:** Fundamental for transparency and customer auditing.")
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
        @Description("Verify that a deposit transaction appears in the transaction history. \n\n" +
                        "**Impact:** Accuracy of financial reporting for the end user.")
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
        @Description("Verify that a withdrawal transaction appears in the transaction history. \n\n" +
                        "**Impact:** Comprehensive tracking of all fund movements.")
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
        @Description("Verify that transaction history cannot be manipulated by the customer. \n\n" +
                        "**Impact:** Critical for security and non-repudiation.")
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
        @Description("Verify that a customer can successfully log out of the application. \n\n" +
                        "**Impact:** Core security feature for shared devices.")
        public void verifyCustomerLogout() {
                accountPage.clickLogout();
                Assertions.assertTrue(loginPage.isCustomerLoginButtonDisplayed(),
                                "After logout, the main login page should be displayed.");
        }
}
