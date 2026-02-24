package com.xyzbank.pages;

import com.xyzbank.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    @FindBy(xpath = "//button[contains(text(),'Customer Login')]")
    private WebElement customerLoginButton;

    @FindBy(xpath = "//button[contains(text(),'Bank Manager Login')]")
    private WebElement bankManagerLoginButton;

    @FindBy(className = "mainHeading")
    private WebElement bankTitle;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickCustomerLogin() {
        waitUtils.waitForClickable(customerLoginButton).click();
    }

    public void clickBankManagerLogin() {
        waitUtils.waitForClickable(bankManagerLoginButton).click();
    }

    public boolean isPageLoaded() {
        try {
            return waitUtils.waitForVisible(bankTitle).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCustomerLoginButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(customerLoginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBankManagerLoginButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(bankManagerLoginButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
