package com.xyzbank.tests;

import com.xyzbank.testdata.TestData;
import com.xyzbank.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("XYZ Bank Application")
@Feature("Bank Manager Operations")
public class BankManagerTest extends BaseTest {

        @BeforeMethod
        public void navigateToBankManager() {
                loginPage.clickBankManagerLogin();
        }

        // ─── Add Customer Tests ───────────────────────────────────────────────────

        @Test(description = "TC_BM_001: Verify that Bank Manager should successfully add a new customer")
        @Story("Add Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can add a new customer with valid first name, last name, and postal code.")
        public void verifyAddCustomerSuccessfully() {
                String alertText = bankManagerPage.addCustomer(
                                TestData.NEW_CUSTOMER_FIRST_NAME,
                                TestData.NEW_CUSTOMER_LAST_NAME,
                                TestData.NEW_CUSTOMER_POSTAL_CODE);

                Assert.assertTrue(alertText.contains("Customer added successfully"),
                                "Expected success alert but got: " + alertText);
        }

        @Test(description = "TC_BM_002: Verify that added customer should appear in the Customers list")
        @Story("Add Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that after adding a customer, they appear in the customers list.")
        public void verifyNewCustomerAppearsInList() {
                bankManagerPage.addCustomer(
                                TestData.NEW_CUSTOMER_FIRST_NAME,
                                TestData.NEW_CUSTOMER_LAST_NAME,
                                TestData.NEW_CUSTOMER_POSTAL_CODE);

                boolean isPresent = bankManagerPage.isCustomerPresent(
                                TestData.NEW_CUSTOMER_FIRST_NAME + " " + TestData.NEW_CUSTOMER_LAST_NAME);

                Assert.assertTrue(isPresent,
                                "Customer should appear in the customers list after being added.");
        }

        @Test(description = "TC_BM_003: Verify that system should reject customer with numeric first name")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the system does not allow adding a customer with numbers in the name.")
        public void verifyAddCustomerWithNumericName() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.INVALID_FIRST_NAME_NUMBERS);
                bankManagerPage.enterLastName(TestData.NEW_CUSTOMER_LAST_NAME);
                bankManagerPage.enterPostalCode(TestData.NEW_CUSTOMER_POSTAL_CODE);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assert.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not allow customer names with numbers. Alert: " + alertText);
                } catch (Exception e) {
                        Assert.assertTrue(true, "Form validation correctly prevented submission.");
                }
        }

        @Test(description = "TC_BM_004: Verify that system should reject customer with alpha-only postal code")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that the postal code field only accepts numeric characters.")
        public void verifyAddCustomerWithAlphaPostalCode() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.NEW_CUSTOMER_FIRST_NAME);
                bankManagerPage.enterLastName(TestData.NEW_CUSTOMER_LAST_NAME);
                bankManagerPage.enterPostalCode(TestData.INVALID_POSTAL_CODE_ALPHA);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assert.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not accept alphabetic postal codes. Alert: " + alertText);
                } catch (Exception e) {
                        Assert.assertTrue(true, "Form validation correctly prevented submission.");
                }
        }

        @Test(description = "TC_BM_005: Verify that system should reject empty form submission")
        @Story("Add Customer - Validation")
        @Severity(SeverityLevel.MINOR)
        @Description("Verify that the system prevents adding a customer when all fields are empty.")
        public void verifyAddCustomerWithEmptyFields() {
                bankManagerPage.clickAddCustomerTab();
                bankManagerPage.enterFirstName(TestData.EMPTY_FIELD);
                bankManagerPage.enterLastName(TestData.EMPTY_FIELD);
                bankManagerPage.enterPostalCode(TestData.EMPTY_FIELD);
                bankManagerPage.clickAddCustomerSubmit();

                try {
                        String alertText = driver.switchTo().alert().getText();
                        driver.switchTo().alert().dismiss();
                        Assert.assertFalse(alertText.contains("Customer added successfully"),
                                        "System should not allow empty form submission.");
                } catch (Exception e) {
                        Assert.assertTrue(true, "Form validation correctly prevented empty submission.");
                }
        }

        // ─── Open Account Tests ───────────────────────────────────────────────────

        @Test(description = "TC_BM_006: Verify that Bank Manager should successfully open an account for a customer")
        @Story("Open Account")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can open a new bank account for an existing customer.")
        public void verifyOpenAccountSuccessfully() {
                String alertText = bankManagerPage.openAccount(TestData.CUSTOMER_HARRY, TestData.CURRENCY_DOLLAR);

                Assert.assertTrue(alertText.contains("Account created successfully"),
                                "Expected account creation success but got: " + alertText);
        }

        @Test(description = "TC_BM_007: Verify that account created alert should contain account number")
        @Story("Open Account")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that upon successful account creation, an account number is provided in the alert.")
        public void verifyOpenAccountAlertContainsAccountNumber() {
                String alertText = bankManagerPage.openAccount(TestData.CUSTOMER_HERMIONE, TestData.CURRENCY_POUND);

                Assert.assertTrue(alertText.contains("Account created successfully"),
                                "Account creation alert should confirm success. Got: " + alertText);
                Assert.assertTrue(alertText.trim().matches(".*\\d+.*"),
                                "Alert should contain the new account number. Got: " + alertText);
        }

        @Test(description = "TC_BM_008: Verify that Bank Manager can open multiple accounts for one customer")
        @Story("Open Account")
        @Severity(SeverityLevel.NORMAL)
        @Description("Verify that a bank manager can create multiple accounts with different currencies for one customer.")
        public void verifyOpenMultipleAccountsForSameCustomer() {
                String alertDollar = bankManagerPage.openAccount(TestData.CUSTOMER_RON, TestData.CURRENCY_DOLLAR);
                Assert.assertTrue(alertDollar.contains("Account created successfully"),
                                "Dollar account should be created successfully.");

                String alertPound = bankManagerPage.openAccount(TestData.CUSTOMER_RON, TestData.CURRENCY_POUND);
                Assert.assertTrue(alertPound.contains("Account created successfully"),
                                "Pound account should be created successfully.");
        }

        // ─── Delete Customer Tests ────────────────────────────────────────────────

        @Test(description = "TC_BM_009: Verify that Bank Manager should successfully delete a customer")
        @Story("Delete Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that a bank manager can delete an existing customer from the system.")
        public void verifyDeleteCustomerSuccessfully() {
                bankManagerPage.addCustomer("Delete", "Me", "99999");

                bankManagerPage.clickCustomersTab();
                bankManagerPage.searchCustomer("Delete Me");
                int countBefore = bankManagerPage.getCustomerCount();

                bankManagerPage.deleteCustomer("Delete Me");

                int countAfter = bankManagerPage.getCustomerCount();

                Assert.assertTrue(countAfter < countBefore,
                                "Customer count should decrease after deletion.");
        }

        @Test(description = "TC_BM_010: Verify that deleted customer should not appear in the customers list")
        @Story("Delete Customer")
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify that after deletion, the customer no longer appears in the system.")
        public void verifyDeletedCustomerNotPresentInList() {
                String uniqueFirstName = "Unique";
                String uniqueLastName = "DeleteTest";
                bankManagerPage.addCustomer(uniqueFirstName, uniqueLastName, "55555");

                bankManagerPage.clickCustomersTab();
                bankManagerPage.deleteCustomer(uniqueFirstName + " " + uniqueLastName);

                boolean isPresent = bankManagerPage.isCustomerPresent(uniqueFirstName + " " + uniqueLastName);
                Assert.assertFalse(isPresent,
                                "Deleted customer should no longer appear in the customers list.");
        }
}
