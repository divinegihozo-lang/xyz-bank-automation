package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@SuppressWarnings({ "unused", "MismatchedQueryAndUpdateOfCollection" })
public final class CustomerAccountPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    @FindBy(xpath = "//div[contains(text(),'Welcome')]/strong")
    private WebElement customerName;

    @FindBy(id = "accountSelect")
    private WebElement accountDropdown;

    @FindBy(xpath = "//div[contains(@class,'center')]/strong[2]")
    private WebElement accountBalance;

    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositTab;

    @FindBy(xpath = "//button[contains(@ng-click,'Withdrawl')]")
    private WebElement withdrawalTab;

    @FindBy(xpath = "//button[contains(@class,'logout')]")
    private WebElement logoutButton;

    @FindBy(xpath = "//button[contains(@ng-click,'transactions')]")
    private WebElement transactionsTab;

    @FindBy(xpath = "//span[contains(text(),'Deposit Successful')]")
    private WebElement depositSuccessMessage;

    @FindBy(xpath = "//span[contains(text(),'Transaction Failed')]")
    private WebElement transactionFailedMessage;

    @FindBy(xpath = "//span[contains(text(),'Withdrawl Successful')]")
    private WebElement withdrawalSuccessMessage;

    @FindBy(xpath = "//button[text()='Reset']")
    private WebElement resetButton;

    public CustomerAccountPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Get logged-in customer name")
    public String getCustomerName() {
        return waitUtils.waitForVisible(customerName).getText();
    }

    @Step("Get selected account number")
    public String getSelectedAccountNumber() {
        return new org.openqa.selenium.support.ui.Select(accountDropdown).getFirstSelectedOption().getText();
    }

    @Step("Select account number: {accountNumber}")
    public void selectAccount(String accountNumber) {
        new org.openqa.selenium.support.ui.Select(accountDropdown).selectByVisibleText(accountNumber);
    }

    @Step("Get current account balance")
    public String getAccountBalance() {
        return waitUtils.waitForVisible(accountBalance).getText();
    }

    @Step("Click on Deposit tab")
    public void clickDepositTab() {
        waitUtils.waitForClickable(depositTab).click();
    }

    @Step("Click on Withdrawl tab")
    public void clickWithdrawlTab() {
        waitUtils.waitForClickable(withdrawalTab).click();
    }

    @Step("Enter amount: {amount}")
    public void enterAmount(String amount) {
        waitUtils.waitForVisible(By.xpath("//input[@placeholder='amount']")).sendKeys(amount);
    }

    @Step("Click on Submit button")
    public void clickSubmit() {
        waitUtils.waitForClickable(By.xpath("//button[@type='submit']")).click();
    }

    @Step("Deposit amount: {amount}")
    public void deposit(String amount) {
        clickDepositTab();
        enterAmount(amount);
        clickSubmit();
    }

    @Step("Withdraw amount: {amount}")
    public void withdraw(String amount) {
        clickWithdrawlTab();
        // Small wait for transition in UI
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        enterAmount(amount);
        clickSubmit();
    }

    @Step("Verify if deposit was successful")
    public boolean isDepositSuccessful() {
        try {
            return waitUtils.waitForVisible(depositSuccessMessage).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if withdrawal was successful")
    public boolean isWithdrawalSuccessful() {
        try {
            return waitUtils.waitForVisible(withdrawalSuccessMessage).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Click on Transactions tab")
    public void clickTransactionsTab() {
        waitUtils.waitForClickable(transactionsTab).click();
    }

    @Step("Get transaction count")
    public int getTransactionCount() {
        try {
            waitUtils.waitForVisible(By.xpath("//table[contains(@class,'table')]//tbody/tr"));
            return driver.findElements(By.xpath("//table[contains(@class,'table')]//tbody/tr")).size();
        } catch (Exception ignored) {
            return 0;
        }
    }

    @Step("Click on Logout button")
    public void clickLogout() {
        waitUtils.waitForClickable(logoutButton).click();
    }

    @Step("Verify if Logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(logoutButton).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Reset button is displayed")
    public boolean isResetButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(resetButton).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if transaction failed")
    public boolean isTransactionFailed() {
        try {
            return waitUtils.waitForVisible(transactionFailedMessage).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if customer is logged in")
    public boolean isLoggedIn() {
        return isLogoutButtonDisplayed();
    }
}
