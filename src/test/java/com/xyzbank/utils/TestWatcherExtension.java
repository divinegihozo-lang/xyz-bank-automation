package com.xyzbank.utils;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestWatcherExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testInstance).getDriver();
            if (driver != null) {
                // Check if test failed
                if (context.getExecutionException().isPresent()) {
                    captureScreenshot(context.getDisplayName(), driver);
                }
            }
        }
        DriverManager.quitDriver();
    }

    @Attachment(value = "Failure Screenshot - {testName}", type = "image/png")
    public byte[] captureScreenshot(String testName, WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return new byte[0];
        }
    }
}
