package services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverInit {

    protected static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        WebDriverInit.driver = driver;
    }

    public WebDriver initializeDriver() {
        //System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\webdrivers\\msedgedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }
}
