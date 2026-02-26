package com.automation.pages.common;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@SuppressWarnings("unused")
public final class LoginPage {

    private final WebDriverWait wait;

    @FindBy(xpath = "//button[contains(text(),'Customer Login')]")
    private WebElement customerLoginButton;

    @FindBy(xpath = "//button[contains(text(),'Bank Manager Login')]")
    private WebElement bankManagerLoginButton;

    @FindBy(className = "mainHeading")
    private WebElement bankTitle;

    public LoginPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Click on Customer Login button")
    public void clickCustomerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(customerLoginButton)).click();
    }

    @Step("Click on Bank Manager Login button")
    public void clickBankManagerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(bankManagerLoginButton)).click();
    }

    @Step("Verify if Login Page is loaded")
    public boolean isPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(bankTitle)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Customer Login button is displayed")
    public boolean isCustomerLoginButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(customerLoginButton)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Step("Verify if Bank Manager Login button is displayed")
    public boolean isBankManagerLoginButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(bankManagerLoginButton)).isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
