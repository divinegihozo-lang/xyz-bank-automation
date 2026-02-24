package com.xyzbank.tests;

import com.xyzbank.pages.CustomerLoginPage;
import com.xyzbank.pages.BankManagerPage;
import com.xyzbank.pages.LoginPage;
import com.xyzbank.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("XYZ Bank Application")
@Feature("Login Page")
public class LoginPageTest extends BaseTest {

    @Test(description = "TC_LP_001: Verify that login page should load successfully")
    @Story("Page Navigation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that the XYZ Bank login page loads and displays correctly.")
    public void verifyLoginPageLoads() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded.");
    }

    @Test(description = "TC_LP_002: Verify that Customer Login button should be visible")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the Customer Login button is visible on the main page.")
    public void verifyCustomerLoginButtonVisible() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isCustomerLoginButtonDisplayed(),
                "Customer Login button should be visible.");
    }

    @Test(description = "TC_LP_003: Verify that Bank Manager Login button should be visible")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the Bank Manager Login button is visible on the main page.")
    public void verifyBankManagerLoginButtonVisible() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isBankManagerLoginButtonDisplayed(),
                "Bank Manager Login button should be visible.");
    }

    @Test(description = "TC_LP_004: Verify that clicking Customer Login navigates to customer login form")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify navigation to the customer login form.")
    public void verifyNavigateToCustomerLogin() {
        LoginPage loginPage = new LoginPage(driver);
        CustomerLoginPage customerLoginPage = loginPage.clickCustomerLogin();
        Assert.assertTrue(customerLoginPage.isCustomerDropdownDisplayed(),
                "Customer login dropdown should be displayed.");
    }

    @Test(description = "TC_LP_005: Verify that clicking Bank Manager Login navigates to manager dashboard")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify navigation to the bank manager dashboard.")
    public void verifyNavigateToBankManagerDashboard() {
        LoginPage loginPage = new LoginPage(driver);
        BankManagerPage managerPage = loginPage.clickBankManagerLogin();
        Assert.assertNotNull(managerPage,
                "Bank Manager dashboard page should load.");
    }
}
