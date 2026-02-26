package com.automation.pages.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@SuppressWarnings("unused")
public final class BankManagerPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation tabs
    @FindBy(xpath = "//button[contains(@ng-click,'addCust')]")
    private WebElement addCustomerTab;

    @FindBy(xpath = "//button[contains(@ng-click,'openAccount')]")
    private WebElement openAccountTab;

    @FindBy(xpath = "//button[contains(@ng-click,'showCust')]")
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ─── Add Customer ─────────────────────────────────────────────────────────

    @Step("Click on Add Customer tab")
    public void clickAddCustomerTab() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerTab)).click();
    }

    @Step("Enter First Name: {firstName}")
    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput)).clear();
        firstNameInput.sendKeys(firstName);
    }

    @Step("Enter Last Name: {lastName}")
    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOf(lastNameInput)).clear();
        lastNameInput.sendKeys(lastName);
    }

    @Step("Enter Postal Code: {postalCode}")
    public void enterPostalCode(String postalCode) {
        wait.until(ExpectedConditions.visibilityOf(postalCodeInput)).clear();
        postalCodeInput.sendKeys(postalCode);
    }

    @Step("Click on Add Customer submit button")
    public void clickAddCustomerSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerSubmitButton)).click();
    }

    @Step("Add new customer: {firstName} {lastName}")
    public String addCustomer(String firstName, String lastName, String postalCode) {
        clickAddCustomerTab();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        clickAddCustomerSubmit();
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception ignored) {
            return "";
        }
    }

    // ─── Open Account ─────────────────────────────────────────────────────────

    @Step("Click on Open Account tab")
    public void clickOpenAccountTab() {
        wait.until(ExpectedConditions.elementToBeClickable(openAccountTab)).click();
    }

    @Step("Select customer for account: {customerName}")
    public void selectCustomerForAccount(String customerName) {
        wait.until(ExpectedConditions.visibilityOf(customerSelectDropdown));
        wait.until(d -> {
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
        wait.until(ExpectedConditions.visibilityOf(currencyDropdown));
        wait.until(d -> {
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
        wait.until(ExpectedConditions.elementToBeClickable(openAccountSubmitButton)).click();
    }

    @Step("Open new account for customer: {customerName} with currency: {currency}")
    public String openAccount(String customerName, String currency) {
        clickOpenAccountTab();
        selectCustomerForAccount(customerName);
        selectCurrency(currency);
        clickOpenAccountSubmit();
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception ignored) {
            return "";
        }
    }

    // ─── Customers List ───────────────────────────────────────────────────────

    @Step("Click on Customers tab")
    public void clickCustomersTab() {
        wait.until(ExpectedConditions.elementToBeClickable(customersTab)).click();
    }

    @Step("Get customer count")
    public int getCustomerCount() {
        return customerRows.size();
    }

    @Step("Search for customer: {name}")
    public void searchCustomer(String name) {
        clickCustomersTab();
        wait.until(ExpectedConditions.visibilityOf(searchInput)).clear();
        searchInput.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        searchInput.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        searchInput.sendKeys(name);
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }

    @Step("Delete customer: {customerName}")
    public void deleteCustomer(String customerName) {
        searchCustomer(customerName);
        List<WebElement> rows = driver.findElements(
                org.openqa.selenium.By.xpath("//table[contains(@class,'table')]//tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(customerName.toLowerCase())) {
                try {
                    WebElement deleteBtn = row.findElement(
                            org.openqa.selenium.By.xpath(".//button[contains(text(),'Delete')]"));
                    wait.until(ExpectedConditions.elementToBeClickable(deleteBtn)).click();
                } catch (Exception e) {
                    driver.findElement(org.openqa.selenium.By.xpath("//button[text()='Delete']")).click();
                }
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
        searchCustomer(customerName);
        List<WebElement> rows = driver.findElements(
                org.openqa.selenium.By.xpath("//table[contains(@class,'table')]//tbody/tr"));

        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(customerName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
