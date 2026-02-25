package com.xyzbank.utils;

import io.qameta.allure.Step;

import com.xyzbank.pages.BankManagerPage;
import com.xyzbank.pages.CustomerAccountPage;
import com.xyzbank.pages.CustomerLoginPage;
import com.xyzbank.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(TestWatcherExtension.class)
public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected BankManagerPage bankManagerPage;
    protected CustomerLoginPage customerLoginPage;
    protected CustomerAccountPage accountPage;
    protected static final String BASE_URL = getPropOrEnv("base.url",
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");

    private static String getPropOrEnv(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isEmpty()) {
            value = System.getenv(key.toUpperCase().replace(".", "_"));
        }
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeEach
    @Step("Setup Test Environment: Initialize driver and navigate to Bank Application")
    public void setUp() {
        driver = DriverManager.getDriver();
        navigateWithRetry(BASE_URL, 3);
        loginPage = new LoginPage(driver);

        // Wait for page to be truly ready before initializing other page objects
        if (!loginPage.isPageLoaded()) {
            throw new RuntimeException("Login page failed to load correctly!");
        }

        bankManagerPage = new BankManagerPage(driver);
        customerLoginPage = new CustomerLoginPage(driver);
        accountPage = new CustomerAccountPage(driver);
    }

    @Step("Open Browser and navigate to Bank Application")
    private void navigateWithRetry(String url, int maxAttempts) {
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                driver.get(url);
                return;
            } catch (Exception e) {
                attempt++;
                System.err.println("Navigation attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxAttempts) {
                    throw new RuntimeException(
                            "Could not load " + url + " after " + maxAttempts + " attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @AfterEach
    public void tearDown() {
        // Driver quit is now handled by TestWatcherExtension to ensure screenshots are
        // captured on failure
    }
}