package com.xyzbank.pages;

import com.xyzbank.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CustomerLoginPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//button[text()='Home']")
    private WebElement homeButton;

    public CustomerLoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public CustomerLoginPage selectCustomer(String customerName) {
        waitUtils.waitForVisible(customerDropdown);
        Select select = new Select(customerDropdown);
        select.selectByVisibleText(customerName);
        return this;
    }

    public CustomerAccountPage clickLogin() {
        waitUtils.waitForClickable(loginButton).click();
        return new CustomerAccountPage(driver);
    }

    public CustomerAccountPage loginAs(String customerName) {
        selectCustomer(customerName);
        return clickLogin();
    }

    public boolean isLoginButtonDisplayed() {
        return loginButton.isDisplayed();
    }

    public boolean isCustomerDropdownDisplayed() {
        return customerDropdown.isDisplayed();
    }

    public void clickHome() {
        homeButton.click();
    }
}
