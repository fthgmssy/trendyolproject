package trendyolproject.Pages;

import trendyolproject.base.PriceUtils;
import org.openqa.selenium.*;
import java.util.ArrayList;
import java.util.List;

public class ProductListPage {

    private final WebDriver driver;
    private final By productCard = By.cssSelector(".prdct-cntnr-wrppr .p-card-wrppr"); // ihtiyaca göre güncelle

    public ProductListPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getCards() {
        return driver.findElements(productCard);
    }

    public List<Double> getAllPrices() {
        List<Double> out = new ArrayList<>();
        for (WebElement card : getCards()) {
            try {
                String priceText = PriceUtils.findPriceText(card);
                out.add(PriceUtils.parsePrice(priceText));
            } catch (Exception ignored) { /* fiyatı olmayanları atla */ }
        }
        return out;
    }

    public long countAdidasProducts() {
        return getCards().stream()
                .map(c -> c.getText().toLowerCase())
                .filter(t -> t.contains("adidas"))
                .count();
    }
}

