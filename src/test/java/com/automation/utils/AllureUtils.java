package com.automation.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureUtils {

    public static void takeScreenshot(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
    }
}
