package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@SuppressWarnings("unused")
public final class LoginPage {

    private final WaitUtils waitUtils;

    @FindBy(xpath = "//button[contains(text(),'Customer Login')]")
    private WebElement customerLoginButton;

    @FindBy(xpath = "//button[contains(text(),'Bank Manager Login')]")
    private WebElement bankManagerLoginButton;

    @FindBy(className = "mainHeading")
    private WebElement bankTitle;

    public LoginPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Click on Customer Login button")
    public void clickCustomerLogin() {
        waitUtils.waitForClickable(customerLoginButton).click();
    }

    @Step("Click on Bank Manager Login button")
    public void clickBankManagerLogin() {
        waitUtils.waitForClickable(bankManagerLoginButton).click();
    }

    @Step("Verify if Login Page is loaded")
    public boolean isPageLoaded() {
        try {
            return waitUtils.waitForVisible(bankTitle).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Customer Login button is displayed")
    public boolean isCustomerLoginButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(customerLoginButton).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Bank Manager Login button is displayed")
    public boolean isBankManagerLoginButtonDisplayed() {
        try {
            return waitUtils.waitForVisible(bankManagerLoginButton).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
