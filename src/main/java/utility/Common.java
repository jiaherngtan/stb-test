package utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class Common {

    public static final String VISIT_SINGAPORE_URL = "https://www.visitsingapore.com/";

    static WebDriver driver = WebDriverInit.getDriver();

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * (long) 1000);
            System.out.println("Sleep for " + seconds + " seconds");
        } catch (Exception ignored) {}
    }

    public static void jsClickElement(WebDriver driver, WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void waitUntilVisible(WebElement webElement) {
        WebDriverWait defaultWait = new WebDriverWait(Objects.requireNonNull(driver), Duration.ofSeconds(15));
        defaultWait.until(ExpectedConditions.visibilityOf(webElement));
    }
}
