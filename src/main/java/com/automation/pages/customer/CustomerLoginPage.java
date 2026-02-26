package com.automation.pages.customer;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@SuppressWarnings("unused")
public final class CustomerLoginPage {

    private final WebDriverWait wait;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    public CustomerLoginPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Select customer name: {customerName}")
    public void selectCustomer(String customerName) {
        wait.until(ExpectedConditions.visibilityOf(customerDropdown));
        Select select = new Select(customerDropdown);
        select.selectByVisibleText(customerName);
    }

    @Step("Click on Login button")
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    @Step("Login as customer: {customerName}")
    public void loginAs(String customerName) {
        selectCustomer(customerName);
        clickLogin();
    }

    @Step("Verify if Customer dropdown is displayed")
    public boolean isCustomerDropdownDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(customerDropdown)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
