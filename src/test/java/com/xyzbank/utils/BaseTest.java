package com.xyzbank.utils;

import com.xyzbank.pages.*;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected BankManagerPage bankManagerPage;
    protected CustomerLoginPage customerLoginPage;
    protected CustomerAccountPage accountPage;
    protected static final String BASE_URL = System.getProperty("base.url",
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.getDriver();
        navigateWithRetry(BASE_URL, 3);
        loginPage = new LoginPage(driver);
        bankManagerPage = new BankManagerPage(driver);
        customerLoginPage = new CustomerLoginPage(driver);
        accountPage = new CustomerAccountPage(driver);
    }

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

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                captureScreenshot(result.getName());
            } catch (Exception e) {
                System.err.println("Screenshot failed (browser may be dead): " + e.getMessage());
            }
        }
        DriverManager.quitDriver();
    }

    @Attachment(value = "Screenshot - {testName}", type = "image/png")
    public byte[] captureScreenshot(String testName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}