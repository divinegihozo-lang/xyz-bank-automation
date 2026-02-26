package com.automation.utils;

import com.automation.base.TestBase;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

public class ScreenshotWatcher implements TestWatcher, AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            Object testInstance = context.getRequiredTestInstance();
            if (testInstance instanceof TestBase) {
                WebDriver driver = ((TestBase) testInstance).getDriver();
                if (driver != null) {
                    AllureUtils.takeScreenshot(driver);
                }
            }
        }
    }

}
