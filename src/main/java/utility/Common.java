package utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Common {

    public static final String VISIT_SINGAPORE_URL = "https://www.visitsingapore.com/";

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * (long) 1000);
            System.out.println("Sleep for " + seconds + " seconds");
        } catch (Exception ignored) {}
    }

    public static void jsClickElement(WebDriver driver, WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
