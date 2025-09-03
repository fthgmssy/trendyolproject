package trendyolproject.base;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public static WebDriver driver;


    public void setUp(String url) {
        System.setProperty("webdriver.gecko.driver",
                System.getProperty("user.dir") + "//src//main//resources//drivers//geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true); // Headless modu aktif et

        driver = new FirefoxDriver();
        // driver.manage().window().maximize();
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(70000, TimeUnit.MILLISECONDS);

    }

    public void setUpchrome(String url) {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "//src//main//resources//drivers//chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(70000, TimeUnit.MILLISECONDS);
    }


    public void cookieaccept(){
        WebDriverWait wait = new WebDriverWait(driver,5);

        try {
            WebElement acceptAllBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Tümünü Kabul Et']")));
            acceptAllBtn.click();
            System.out.println("Cookies accepted successfully.");
        } catch (Exception e) {
            System.out.println("Cookie popup not displayed.");
        }
    }


    public void popuphandle(){
        try {
            WebDriverWait wait = new WebDriverWait(driver,5);
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-close")));
            System.out.println("Popup appeared!");

            WebElement closeBtn = popup.findElement(By.cssSelector(".modal-close"));
            closeBtn.click();
            System.out.println("Popup closed successfully.");
        } catch (Exception e) {
            System.out.println("Popup did not appear.");
        }

    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        //   extent.flush();
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            Allure.addAttachment("Failure Screenshot: ", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        } else {
            // test.pass(result.getName()+ "Testcase Passed");
        }
    }


}
