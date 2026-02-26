package com.automation.base;

import com.automation.pages.manager.BankManagerPage;
import com.automation.pages.customer.CustomerAccountPage;
import com.automation.pages.customer.CustomerLoginPage;
import com.automation.pages.common.LoginPage;
import com.automation.utils.ScreenshotWatcher;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

@ExtendWith(ScreenshotWatcher.class)
public class TestBase {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected BankManagerPage bankManagerPage;
    protected CustomerLoginPage customerLoginPage;
    protected CustomerAccountPage accountPage;

    protected static final String BASE_URL = System.getProperty(
            "base.url",
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");

    @BeforeEach
    public void setUp() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver());
        }
        driver = driverThreadLocal.get();
        driver.get(BASE_URL);

        // Initialize page objects
        loginPage = new LoginPage(driver);
        bankManagerPage = new BankManagerPage(driver);
        customerLoginPage = new CustomerLoginPage(driver);
        accountPage = new CustomerAccountPage(driver);
    }

    @AfterEach
    public void tearDown() {
        WebDriver currentDriver = driverThreadLocal.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } catch (Exception e) {
                System.err.println("[TestBase] Warning during quit: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    private WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = buildChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(
                System.getProperty("headless", "false"));

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("excludeSwitches",
                List.of("enable-automation"));

        return options;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
