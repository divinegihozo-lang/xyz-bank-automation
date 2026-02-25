package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@SuppressWarnings("unused")
public final class CustomerLoginPage {

    private final WaitUtils waitUtils;

    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    public CustomerLoginPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Select customer name: {customerName}")
    public void selectCustomer(String customerName) {
        waitUtils.waitForVisible(customerDropdown);
        Select select = new Select(customerDropdown);
        select.selectByVisibleText(customerName);
    }

    @Step("Click on Login button")
    public void clickLogin() {
        waitUtils.waitForClickable(loginButton).click();
    }

    @Step("Login as customer: {customerName}")
    public void loginAs(String customerName) {
        selectCustomer(customerName);
        clickLogin();
    }

    @Step("Verify if Customer dropdown is displayed")
    public boolean isCustomerDropdownDisplayed() {
        return customerDropdown.isDisplayed();
    }

}
