package com.automation.pages.customer;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@SuppressWarnings("unused")
public final class CustomerAccountPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//span[contains(@class,'fontBig')]")
    private WebElement customerName;

    @FindBy(id = "accountSelect")
    private WebElement accountDropdown;

    @FindBy(xpath = "//div[contains(@class,'center')]/strong[2]")
    private WebElement accountBalance;

    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositTab;

    @FindBy(xpath = "//button[contains(text(),'Withdrawl')]")
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Get logged-in customer name")
    public String getCustomerName() {
        return wait.until(ExpectedConditions.visibilityOf(customerName)).getText();
    }

    @Step("Get selected account number")
    public String getSelectedAccountNumber() {
        return new Select(accountDropdown).getFirstSelectedOption().getText();
    }

    @Step("Select account number: {accountNumber}")
    public void selectAccount(String accountNumber) {
        new Select(accountDropdown).selectByVisibleText(accountNumber);
    }

    @Step("Get current account balance")
    public String getAccountBalance() {
        return wait.until(ExpectedConditions.visibilityOf(accountBalance)).getText();
    }

    @Step("Click on Deposit tab")
    public void clickDepositTab() {
        wait.until(ExpectedConditions.elementToBeClickable(depositTab)).click();
    }

    @Step("Click on Withdrawl tab")
    public void clickWithdrawlTab() {
        wait.until(ExpectedConditions.elementToBeClickable(withdrawalTab)).click();
    }

    @Step("Enter amount: {amount}")
    public void enterAmount(String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='amount']")))
                .sendKeys(amount);
    }

    @Step("Click on Submit button")
    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
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
            return wait.until(ExpectedConditions.visibilityOf(depositSuccessMessage)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if withdrawal was successful")
    public boolean isWithdrawalSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(withdrawalSuccessMessage)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Click on Transactions tab")
    public void clickTransactionsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(transactionsTab)).click();
    }

    @Step("Get transaction count")
    public int getTransactionCount() {
        try {
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//table[contains(@class,'table')]//tbody/tr")));
            return driver.findElements(By.xpath("//table[contains(@class,'table')]//tbody/tr")).size();
        } catch (Exception ignored) {
            return 0;
        }
    }

    @Step("Click on Logout button")
    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }

    @Step("Verify if Logout button is displayed")
    public boolean isLogoutButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(logoutButton)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Reset button is displayed")
    public boolean isResetButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(resetButton)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if transaction failed")
    public boolean isTransactionFailed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(transactionFailedMessage)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if customer is logged in")
    public boolean isLoggedIn() {
        return isLogoutButtonDisplayed();
    }
}
