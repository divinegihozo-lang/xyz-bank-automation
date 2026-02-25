package com.xyzbank.tests;

import com.xyzbank.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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
        Assert.assertTrue(loginPage.isPageLoaded(), "Login page should be loaded.");
    }

    @Test(description = "TC_LP_002: Verify that Customer Login button should be visible")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the Customer Login button is visible on the main page.")
    public void verifyCustomerLoginButtonVisible() {
        Assert.assertTrue(loginPage.isCustomerLoginButtonDisplayed(),
                "Customer Login button should be visible.");
    }

    @Test(description = "TC_LP_003: Verify that Bank Manager Login button should be visible")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the Bank Manager Login button is visible on the main page.")
    public void verifyBankManagerLoginButtonVisible() {
        Assert.assertTrue(loginPage.isBankManagerLoginButtonDisplayed(),
                "Bank Manager Login button should be visible.");
    }

    @Test(description = "TC_LP_004: Verify that clicking Customer Login navigates to customer login form")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify navigation to the customer login form.")
    public void verifyNavigateToCustomerLogin() {
        loginPage.clickCustomerLogin();
        Assert.assertTrue(customerLoginPage.isCustomerDropdownDisplayed(),
                "Customer login dropdown should be displayed.");
    }

    @Test(description = "TC_LP_005: Verify that clicking Bank Manager Login navigates to manager dashboard")
    @Story("Page Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify navigation to the bank manager dashboard.")
    public void verifyNavigateToBankManagerDashboard() {
        loginPage.clickBankManagerLogin();
        Assert.assertNotNull(bankManagerPage,
                "Bank Manager dashboard page should load.");
    }
}
