package com.xyzbank.pages;

import com.xyzbank.utils.WaitUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BankManagerPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    // Navigation tabs
    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement addCustomerTab;

    @FindBy(xpath = "//button[contains(text(),'Open Account')]")
    private WebElement openAccountTab;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement customersTab;

    @FindBy(xpath = "//button[text()='Home']")
    private WebElement homeButton;

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

    public BankManagerPage clickAddCustomerTab() {
        waitUtils.waitForClickable(addCustomerTab).click();
        return this;
    }

    public BankManagerPage enterFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        return this;
    }

    public BankManagerPage enterLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        return this;
    }

    public BankManagerPage enterPostalCode(String postalCode) {
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
        return this;
    }

    public BankManagerPage clickAddCustomerSubmit() {
        waitUtils.waitForClickable(addCustomerSubmitButton).click();
        return this;
    }

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
        } catch (Exception e) {
            return "";
        }
    }

    // ─── Open Account ─────────────────────────────────────────────────────────

    public BankManagerPage clickOpenAccountTab() {
        waitUtils.waitForClickable(openAccountTab).click();
        return this;
    }

    public BankManagerPage selectCustomerForAccount(String customerName) {
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
        return this;
    }

    public BankManagerPage selectCurrency(String currency) {
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
        return this;
    }

    public BankManagerPage clickOpenAccountSubmit() {
        waitUtils.waitForClickable(openAccountSubmitButton).click();
        return this;
    }

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
        } catch (Exception e) {
            return "";
        }
    }

    // ─── Customers List ───────────────────────────────────────────────────────

    public BankManagerPage clickCustomersTab() {
        waitUtils.waitForClickable(customersTab).click();
        return this;
    }

    public List<WebElement> getCustomerRows() {
        return customerRows;
    }

    public int getCustomerCount() {
        return customerRows.size();
    }

    public BankManagerPage searchCustomer(String name) {
        waitUtils.waitForVisible(searchInput).clear();
        searchInput.sendKeys(name);
        // Small delay for search to filter if necessary, or wait for row count to
        // satisfy condition
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
        return this;
    }

    public BankManagerPage deleteCustomer(String customerName) {
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
                return this;
            }
        }
        return this;
    }

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

    public LoginPage clickHome() {
        homeButton.click();
        return new LoginPage(driver);
    }
}
