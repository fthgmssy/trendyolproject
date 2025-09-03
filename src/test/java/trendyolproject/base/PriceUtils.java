package trendyolproject.base;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceUtils {

    private static final By[] PRICE_CANDIDATES = new By[] {
            By.cssSelector(".prc-box-dscntd"),   // indirimli
            By.cssSelector(".prc-box-orgnl"),    // orijinal
            By.cssSelector("[class*='prc-']"),   // fallback
            By.cssSelector(".price-current"),    // olası tema
            By.cssSelector(".prc")               // genel fallback
    };

    /** Kart içinden fiyat metnini bul (yoksa kart text’inden regex ile). */
    public static String findPriceText(WebElement card) {
        for (By by : PRICE_CANDIDATES) {
            List<WebElement> found = card.findElements(by);
            if (!found.isEmpty()) {
                String t = found.get(0).getText();
                if (t != null && !t.isEmpty()) return t;
            }
        }
        String txt = card.getText();
        String fromRegex = firstPriceFromText(txt);
        if (fromRegex != null) return fromRegex;
        throw new NoSuchElementException("Fiyat bulunamadı.");
    }

    /** Metinden ilk fiyatı seç (örn: 1.899,99 TL / 2.500 TL). */
    public static String firstPriceFromText(String text) {
        if (text == null) return null;
        Pattern p = Pattern.compile("(\\d{1,3}(?:[\\.\\s]\\d{3})*(?:,\\d{1,2})?)\\s*(TL|₺)?");
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1) + (m.group(2) != null ? " " + m.group(2) : "") : null;
    }

    /** "1.899,99 TL" -> 1899.99; "2.500 TL" -> 2500.0 */
    public static double parsePrice(String raw) {
        String cleaned = raw.toUpperCase()
                .replace("TL","").replace("₺","")
                .replace(" ","").replace(".","")
                .replace(",",".");
        return Double.parseDouble(cleaned);
    }

    public static boolean isInRange(double price, double min, double max) {
        return price >= min && price <= max;
    }
}

