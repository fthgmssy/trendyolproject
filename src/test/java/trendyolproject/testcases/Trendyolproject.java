package trendyolproject.testcases;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import trendyolproject.Pages.ProductListPage;
import trendyolproject.base.BaseTest;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Trendyolproject extends BaseTest {

    String url = "https://trendyol.com";

    @Step("Browser open")
    @Test(priority=1)
    public void openbrowser(){
        setUp(url);
    }

    @Step("Cookie accept")
    @Test(priority = 2)
    public void cookiekabulet(){
        cookieaccept();
    }

    @Step("Handle pop-up")
    @Test(priority = 3)
    public void handlepopup(){
    //    popuphandle();
    }

    @Step("Search for shoes")
    @Test(priority = 4)
    public void searchshoes(){
        WebElement searchBox = driver.findElement(By.cssSelector("input[data-testid='suggestion']"));
        WebElement searchButton = driver.findElement(By.cssSelector("[data-testid='search-icon']"));
        searchBox.sendKeys("erkek ayakkabı");
        searchButton.click();
    }

    @Step("Check list after search at lease 1 result appear")
    @Test(priority = 5)
    public void listcontrol(){
        List<WebElement> results = driver.findElements(By.cssSelector(".prdct-cntnr-wrppr"));
        Assert.assertTrue(results.size() > 0, "No search results found");
    }

    @Step("Pegination work check")
    @Test(priority = 6)
    public void paginationcheck() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long scrollHeight = (long) js.executeScript("return document.body.scrollHeight");

        for (int i = 0; i < scrollHeight; i += 300) {
            js.executeScript("window.scrollBy(0, 300)");
            Thread.sleep(500);
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("pi=2"));

        WebElement scrollup =  driver.findElement(By.xpath("//span[contains(text(),'Yukarı Çık')]"));
        scrollup.click();
    }

    @Step("Apply filter and check results")
    @Test(priority = 7)
    public void applyfilter(){
        WebElement adidasFilter = driver.findElement(By.xpath("//div[contains(text(),'adidas')]"));
        adidasFilter.click();

        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prdct-cntnr-wrppr")));

    }

    @Step("List the products and adidas product number")
    @Test(priority = 8)
    public void listproducts(){
        List<WebElement> urunlistesi = driver.findElements(By.cssSelector(".prdct-desc-cntnr-ttl"));

        int toplamUrunSayisi = urunlistesi.size();
        int adidasUrunSayaci = 0;

        for (WebElement urun : urunlistesi) {
            String urunIcerik = urun.getText().toLowerCase();

            if (urunIcerik.contains("adidas")) {
                adidasUrunSayaci++;
                System.out.println("✅ Adidas bulundu: " + urun.getText());
            } else {
                System.out.println("❌ Adidas yok: " + urun.getText());
            }
        }
        System.out.println("Toplam " + adidasUrunSayaci + " adet Adidas ürünü bulundu.");

    }

    @Step("Price filter")
    @Test(priority = 10)
    public void filterprice() {
        WebElement fiyat = driver.findElement(By.xpath("//div[@class='fltr-cntnr-ttl'][normalize-space()='Fiyat']"));
        fiyat.click();

        WebElement fiyatfiltre = driver.findElement(By.xpath("//div[normalize-space()='2000 TL - 3000 TL']"));
        fiyatfiltre.click();
    }

    @Step("Price list and check prices")
    @Test(priority = 11)
    public void pricelistcheck(){
        List<WebElement> fiyatListesi = driver.findElements(By.cssSelector(".prdct-cntnr-wrppr"));

        int inRange = 0, outRange = 0;

        for (WebElement fiyatEl : fiyatListesi) {
            String raw = fiyatEl.getText();

            // Regex ile fiyatı ayıkla
            Matcher m = Pattern.compile("(\\d{1,3}(?:[\\.\\s]\\d{3})*(?:,\\d{1,2})?)").matcher(raw);
            if (!m.find()) {
                System.out.println("⚠️ Fiyat bulunamadı: " + raw);
                continue;
            }

            String fiyatText = m.group(1);
            double fiyat = Double.parseDouble(fiyatText.replace(".", "").replace(",", "."));

            if (fiyat >= 2000 && fiyat <= 3000) {
                inRange++;
                System.out.println("✅ Aralıkta: " + fiyat);
            } else {
                outRange++;
                System.out.println("❌ Dışında: " + fiyat);
            }
        }

        System.out.println("Aralıkta: " + inRange);
        System.out.println("Dışında: " + outRange);
    }


    @Step("Negative test case")
    @Test(priority = 12)
    public void trendyolnegativecase(){
        WebElement searchBox = driver.findElement(By.cssSelector("input[data-testid='suggestion']"));
        WebElement searchButton = driver.findElement(By.cssSelector("[data-testid='search-icon']"));
        searchBox.sendKeys("adsafasds");
        searchButton.click();
    }

    @Step("Negative test result check")
    @Test(priority = 13)
    public void trendyolnegativeresultcheck(){
        WebElement result = driver.findElement(By.xpath("//div[@class='srch-rslt-title']"));
        String answer = result.getText();

        String expectedText = "aramanız için ürün bulunamadı.";
        Assert.assertTrue(answer.contains(expectedText),
                "Beklenen mesaj bulunamadı! Actual: " + answer);
    }
}
