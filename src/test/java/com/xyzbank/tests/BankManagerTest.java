package com.xyzbank.tests;

import com.xyzbank.testdata.TestData;
import com.xyzbank.utils.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("XYZ Bank Application")
@Feature("Bank Manager Operations")
public class BankManagerTest extends BaseTest {

        @BeforeEach
        @Step("Navigate to Bank Manager Dashboard")
        public void navigateToBankManager() {
                loginPage.clickBankManagerLogin();
        }

        // ─── Add Customer Tests ───────────────────────────────────────────────────

        @Test
        @DisplayName("TC_BM_001: Verify that Bank Manager should successfully add a new customer")
        @Story("Add Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can add a new customer with valid first name, last name, and postal code. \n\n"
                        +
                        "**Impact:** Registration is a core feature; failure stops new business and account creation.")
        public void verifyAddCustomerSuccessfully() {
                String alertText = bankManagerPage.addCustomer(
                                TestData.NEW_CUSTOMER_FIRST_NAME,
                                TestData.NEW_CUSTOMER_LAST_NAME,
                                TestData.NEW_CUSTOMER_POSTAL_CODE);

                Assertions.assertTrue(alertText.contains("Customer added successfully"),
                                "Expected success alert but got: " + alertText);
        }

        @Test
        @DisplayName("TC_BM_002: Verify that added customer should appear in the Customers list")
        @Story("Add Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that after adding a customer, they appear in the customers list for management. \n\n" +
                        "**Impact:** Data persistence and UI synchronization are critical for accurate record management.")
        public void verifyNewCustomerAppearsInList() {
                bankManagerPage.addCustomer(
                                TestData.NEW_CUSTOMER_FIRST_NAME,
                                TestData.NEW_CUSTOMER_LAST_NAME,
                                TestData.NEW_CUSTOMER_POSTAL_CODE);

                boolean isPresent = bankManagerPage.isCustomerPresent(
                                TestData.NEW_CUSTOMER_FIRST_NAME + " " + TestData.NEW_CUSTOMER_LAST_NAME);

                Assertions.assertTrue(isPresent,
                                "Customer should appear in the customers list after being added.");
        }

        @Test
        @DisplayName("TC_BM_003: Verify that system should reject customer with numeric first name")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the system correctly rejects customer addition if the name contains numeric characters. \n\n"
                        +
                        "**Impact:** Prevention of garbage data in the database ensures name integrity for legal/audit purposes.")
        public void verifyAddCustomerWithNumericName() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.INVALID_FIRST_NAME_NUMBERS);
                bankManagerPage.enterLastName(TestData.NEW_CUSTOMER_LAST_NAME);
                bankManagerPage.enterPostalCode(TestData.NEW_CUSTOMER_POSTAL_CODE);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assertions.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not allow customer names with numbers. Alert: " + alertText);
                } catch (Exception e) {
                        Assertions.assertTrue(true, "Form validation correctly prevented submission.");
                }
        }

        @Test
        @DisplayName("TC_BM_004: Verify that system should reject customer with alpha-only postal code")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the postal code field only accepts numeric characters and rejects alphabetic inputs. \n\n"
                        +
                        "**Impact:** Incorrect postal codes can lead to billing errors and logistics failures.")
        public void verifyAddCustomerWithAlphaPostalCode() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.NEW_CUSTOMER_FIRST_NAME);
                bankManagerPage.enterLastName(TestData.NEW_CUSTOMER_LAST_NAME);
                bankManagerPage.enterPostalCode(TestData.INVALID_POSTAL_CODE_ALPHA);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assertions.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not accept alphabetic postal codes. Alert: " + alertText);
                } catch (Exception e) {
                        Assertions.assertTrue(true, "Form validation correctly prevented submission.");
                }
        }

        @Test
        @DisplayName("TC_BM_005: Verify that system should reject empty form submission")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.MINOR)
        @Description("Verify that the system prevents adding a customer when all fields are empty, ensuring data completeness. \n\n"
                        +
                        "**Impact:** Prevents the creation of ghost accounts which clutter the system and skew analytics.")
        public void verifyAddCustomerWithEmptyFields() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.EMPTY_FIELD);
                bankManagerPage.enterLastName(TestData.EMPTY_FIELD);
                bankManagerPage.enterPostalCode(TestData.EMPTY_FIELD);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assertions.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not allow empty form submission.");
                } catch (Exception e) {
                        Assertions.assertTrue(true, "Form validation correctly prevented empty submission.");
                }
        }

        // ─── Open Account Tests ───────────────────────────────────────────────────

        @Test
        @DisplayName("TC_BM_006: Verify that Bank Manager should successfully open an account for a customer")
        @Story("Open Account")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can successfully link a new account to an existing customer with a chosen currency. \n\n"
                        +
                        "**Impact:** This is a blocker feature. If accounts cannot be opened, the banking system cannot function.")
        public void verifyOpenAccountSuccessfully() {
                String alertText = bankManagerPage.openAccount(TestData.CUSTOMER_HARRY, TestData.CURRENCY_DOLLAR);

                Assertions.assertTrue(alertText.contains("Account created successfully"),
                                "Expected account creation success but got: " + alertText);
        }

        @Test
        @DisplayName("TC_BM_007: Verify that account created alert should contain account number")
        @Story("Open Account")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that upon successful account creation, a unique numeric account number is generated and displayed. \n\n"
                        +
                        "**Impact:** Customers need account numbers to perform any banking transactions.")
        public void verifyOpenAccountAlertContainsAccountNumber() {
                String alertText = bankManagerPage.openAccount(TestData.CUSTOMER_HERMIONE, TestData.CURRENCY_POUND);

                Assertions.assertTrue(alertText.contains("Account created successfully"),
                                "Account creation alert should confirm success. Got: " + alertText);
                Assertions.assertTrue(alertText.trim().matches(".*\\d+.*"),
                                "Alert should contain the new account number. Got: " + alertText);
        }

        @Test
        @DisplayName("TC_BM_008: Verify that Bank Manager can open multiple accounts for one customer")
        @Story("Open Account")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that a bank manager can create multiple accounts with different currencies for one customer. \n\n"
                        +
                        "**Impact:** Supports multi-currency banking which is essential for international customers.")
        public void verifyOpenMultipleAccountsForSameCustomer() {
                String alertDollar = bankManagerPage.openAccount(TestData.CUSTOMER_RON, TestData.CURRENCY_DOLLAR);
                Assertions.assertTrue(alertDollar.contains("Account created successfully"),
                                "Dollar account should be created successfully.");

                String alertPound = bankManagerPage.openAccount(TestData.CUSTOMER_RON, TestData.CURRENCY_POUND);
                Assertions.assertTrue(alertPound.contains("Account created successfully"),
                                "Pound account should be created successfully.");
        }

        // ─── Delete Customer Tests ────────────────────────────────────────────────

        @Test
        @DisplayName("TC_BM_009: Verify that Bank Manager should successfully delete a customer")
        @Story("Delete Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can delete an existing customer from the system, removing their records. \n\n"
                        +
                        "**Impact:** Supports 'Right to be Forgotten' (GDPR) and system manual cleanup.")
        public void verifyDeleteCustomerSuccessfully() {
                bankManagerPage.addCustomer("Delete", "Me", "99999");

                bankManagerPage.clickCustomersTab();
                bankManagerPage.searchCustomer("Delete Me");
                int countBefore = bankManagerPage.getCustomerCount();

                bankManagerPage.deleteCustomer("Delete Me");

                int countAfter = bankManagerPage.getCustomerCount();

                Assertions.assertTrue(countAfter < countBefore,
                                "Customer count should decrease after deletion.");
        }

        @Test
        @DisplayName("TC_BM_010: Verify that deleted customer should not appear in the customers list")
        @Story("Delete Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that after deletion, the customer record is purged and no longer searchable. \n\n" +
                        "**Impact:** Ensures data integrity and that deleted users cannot access banking features.")
        public void verifyDeletedCustomerNotPresentInList() {
                String uniqueFirstName = "Unique";
                String uniqueLastName = "DeleteTest";
                bankManagerPage.addCustomer(uniqueFirstName, uniqueLastName, "55555");

                bankManagerPage.clickCustomersTab();
                bankManagerPage.deleteCustomer(uniqueFirstName + " " + uniqueLastName);

                boolean isPresent = bankManagerPage.isCustomerPresent(uniqueFirstName + " " + uniqueLastName);
                Assertions.assertFalse(isPresent,
                                "Deleted customer should no longer appear in the customers list.");
        }
}
