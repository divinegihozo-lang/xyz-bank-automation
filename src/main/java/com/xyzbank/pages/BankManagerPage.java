package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

@SuppressWarnings({ "unused", "MismatchedQueryAndUpdateOfCollection" })
public final class BankManagerPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    // Navigation tabs
    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement addCustomerTab;

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountTab;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement customersTab;

    // Add Customer Form
    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@placeholder='Post Code']")
    private WebElement postalCodeInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement addCustomerSubmitButton;

    // Open Account Form
    @FindBy(id = "userSelect")
    private WebElement customerSelectDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement openAccountSubmitButton;

    // Customers Table
    @FindBy(xpath = "//table[@class='table table-bordered table-striped']/tbody/tr")
    private List<WebElement> customerRows;

    @FindBy(xpath = "//input[@placeholder='Search Customer']")
    private WebElement searchInput;

    public BankManagerPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ─── Add Customer ─────────────────────────────────────────────────────────

    @Step("Click on Add Customer tab")
    public void clickAddCustomerTab() {
        waitUtils.waitForClickable(addCustomerTab).click();
    }

    @Step("Enter First Name: {firstName}")
    public void enterFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    @Step("Enter Last Name: {lastName}")
    public void enterLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    @Step("Enter Postal Code: {postalCode}")
    public void enterPostalCode(String postalCode) {
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
    }

    @Step("Click on Add Customer submit button")
    public void clickAddCustomerSubmit() {
        waitUtils.waitForClickable(addCustomerSubmitButton).click();
    }

    @Step("Add new customer: {firstName} {lastName}")
    @SuppressWarnings("UnusedReturnValue")
    public String addCustomer(String firstName, String lastName, String postalCode) {
        clickAddCustomerTab();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        clickAddCustomerSubmit();
        try {
            if (waitUtils.waitForAlertPresent()) {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                alert.accept();
                return alertText;
            }
            return "";
        } catch (Exception ignored) {
            return "";
        }
    }

    // ─── Open Account ─────────────────────────────────────────────────────────

    @Step("Click on Open Account tab")
    public void clickOpenAccountTab() {
        waitUtils.waitForClickable(openAccountTab).click();
    }

    @Step("Select customer for account: {customerName}")
    public void selectCustomerForAccount(String customerName) {
        waitUtils.waitForVisible(customerSelectDropdown);
        waitUtils.waitForCondition(driver -> {
            Select select = new Select(customerSelectDropdown);
            List<WebElement> options = select.getOptions();
            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(customerName.trim())) {
                    return true;
                }
            }
            return false;
        });
        new Select(customerSelectDropdown).selectByVisibleText(customerName);
    }

    @Step("Select currency: {currency}")
    public void selectCurrency(String currency) {
        waitUtils.waitForVisible(currencyDropdown);
        waitUtils.waitForCondition(driver -> {
            Select select = new Select(currencyDropdown);
            List<WebElement> options = select.getOptions();
            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(currency.trim())) {
                    return true;
                }
            }
            return false;
        });
        new Select(currencyDropdown).selectByVisibleText(currency);
    }

    @Step("Click on Open Account submit button")
    public void clickOpenAccountSubmit() {
        waitUtils.waitForClickable(openAccountSubmitButton).click();
    }

    @Step("Open new account for customer: {customerName} with currency: {currency}")
    @SuppressWarnings("UnusedReturnValue")
    public String openAccount(String customerName, String currency) {
        clickOpenAccountTab();
        selectCustomerForAccount(customerName);
        selectCurrency(currency);
        clickOpenAccountSubmit();
        try {
            if (waitUtils.waitForAlertPresent()) {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                alert.accept();
                return alertText;
            }
            return "";
        } catch (Exception ignored) {
            return "";
        }
    }

    // ─── Customers List ───────────────────────────────────────────────────────

    @Step("Click on Customers tab")
    public void clickCustomersTab() {
        waitUtils.waitForClickable(customersTab).click();
    }

    @Step("Get customer count")
    public int getCustomerCount() {
        return customerRows.size();
    }

    @Step("Search for customer: {name}")
    public void searchCustomer(String name) {
        waitUtils.waitForVisible(searchInput).clear();
        searchInput.sendKeys(name);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
    }

    @Step("Delete customer: {customerName}")
    public void deleteCustomer(String customerName) {
        searchCustomer(customerName);
        for (WebElement row : customerRows) {
            if (row.getText().contains(customerName)) {
                WebElement deleteBtn = row.findElement(
                        org.openqa.selenium.By.xpath(".//button[contains(text(),'Delete')]"));
                deleteBtn.click();
                // Wait a bit for list to refresh
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                return;
            }
        }
    }

    @Step("Check if customer is present: {customerName}")
    public boolean isCustomerPresent(String customerName) {
        clickCustomersTab();
        try {
            waitUtils.waitForVisible(
                    org.openqa.selenium.By.xpath("//table[@class='table table-bordered table-striped']/tbody/tr"));
        } catch (Exception ignored) {
        }
        searchCustomer(customerName);
        for (WebElement row : customerRows) {
            if (row.getText().contains(customerName)) {
                return true;
            }
        }
        return false;
    }

}
