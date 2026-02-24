package com.xyzbank.pages;

import com.xyzbank.helpers.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CustomerLoginPage {

    private final WaitUtils waitUtils;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//button[text()='Home']")
    private WebElement homeButton;

    public CustomerLoginPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void selectCustomer(String customerName) {
        waitUtils.waitForVisible(customerDropdown);
        Select select = new Select(customerDropdown);
        select.selectByVisibleText(customerName);
    }

    public void clickLogin() {
        waitUtils.waitForClickable(loginButton).click();
    }

    public void loginAs(String customerName) {
        selectCustomer(customerName);
        clickLogin();
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
