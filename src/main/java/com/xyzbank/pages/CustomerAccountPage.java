package com.xyzbank.pages;

import com.xyzbank.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CustomerAccountPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    @FindBy(xpath = "//strong[@class='ng-binding'][1]")
    private WebElement customerName;

    @FindBy(id = "accountSelect")
    private WebElement accountDropdown;

    @FindBy(xpath = "//div[contains(@class,'center')]/strong[1]")
    private WebElement accountNumber;

    @FindBy(xpath = "//div[contains(@class,'center')]/strong[2]")
    private WebElement accountBalance;

    @FindBy(xpath = "//div[contains(@class,'center')]/strong[3]")
    private WebElement accountCurrency;

    @FindBy(xpath = "//button[contains(text(),'Deposit')]")
    private WebElement depositTab;

    @FindBy(xpath = "//button[contains(text(),'Withdraw')]")
    private WebElement withdrawalTab;

    @FindBy(xpath = "//button[contains(text(),'Transactions')]")
    private WebElement transactionsTab;

    @FindBy(xpath = "//button[contains(text(),'Logout')]")
    private WebElement logoutButton;

    // Deposit form
    @FindBy(xpath = "//input[@placeholder='amount']")
    private WebElement amountInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//span[contains(@class,'error')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//span[contains(text(),'Deposit Successful')]")
    private WebElement depositSuccessMessage;

    @FindBy(xpath = "//span[contains(text(),'Transaction Failed')]")
    private WebElement transactionFailedMessage;

    // Transactions table
    @FindBy(xpath = "//table[@class='table table-bordered table-striped']//tr[position()>1]")
    private List<WebElement> transactionRows;

    @FindBy(xpath = "//button[text()='Reset']")
    private WebElement resetButton;

    @FindBy(xpath = "//button[text()='Back']")
    private WebElement backButton;

    public CustomerAccountPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public String getCustomerName() {
        return waitUtils.waitForVisible(customerName).getText();
    }

    public String getAccountBalance() {
        return waitUtils.waitForVisible(accountBalance).getText().trim();
    }

    public void selectAccount(String accountNo) {
        Select select = new Select(accountDropdown);
        select.selectByVisibleText(accountNo);
    }

    // ─── Deposit ──────────────────────────────────────────────────────────────

    public CustomerAccountPage clickDepositTab() {
        waitUtils.waitForClickable(depositTab).click();
        // Wait for Deposit form label
        try {
            waitUtils.waitForVisible(org.openqa.selenium.By.xpath("//label[contains(text(),'Deposited')]"));
        } catch (Exception ignored) {
        }
        return this;
    }

    public CustomerAccountPage enterAmount(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
        return this;
    }

    public CustomerAccountPage clickSubmit() {
        try {
            waitUtils.waitForClickable(submitButton).click();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            driver.findElement(org.openqa.selenium.By.xpath("//button[@type='submit']")).click();
        }
        return this;
    }

    public CustomerAccountPage deposit(String amount) {
        clickDepositTab();
        enterAmount(amount);
        clickSubmit();
        return this;
    }

    public boolean isDepositSuccessful() {
        try {
            return waitUtils.waitForVisible(depositSuccessMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Withdrawal ───────────────────────────────────────────────────────────

    public CustomerAccountPage clickWithdrawalTab() {
        waitUtils.waitForClickable(withdrawalTab).click();
        try {
            waitUtils.waitForVisible(org.openqa.selenium.By.xpath("//label[contains(text(),'Withdrawn')]"));
        } catch (Exception ignored) {
        }
        return this;
    }

    public CustomerAccountPage withdraw(String amount) {
        clickWithdrawalTab();
        enterAmount(amount);
        clickSubmit();
        return this;
    }

    public boolean isTransactionFailed() {
        try {
            return transactionFailedMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Transactions ─────────────────────────────────────────────────────────

    public CustomerAccountPage clickTransactionsTab() {
        waitUtils.waitForClickable(transactionsTab).click();
        return this;
    }

    public List<WebElement> getTransactionRows() {
        return transactionRows;
    }

    public int getTransactionCount() {
        try {
            waitUtils.waitForVisible(org.openqa.selenium.By
                    .xpath("//table[@class='table table-bordered table-striped']//tr[position()>1]"));
        } catch (Exception ignored) {
        }
        return transactionRows.size();
    }

    public boolean isResetButtonDisplayed() {
        try {
            return resetButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isResetButtonEnabled() {
        try {
            return resetButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Navigation ───────────────────────────────────────────────────────────

    public LoginPage clickLogout() {
        logoutButton.click();
        return new LoginPage(driver);
    }

    public boolean isLogoutButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(logoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoggedIn() {
        return isLogoutButtonDisplayed();
    }
}
