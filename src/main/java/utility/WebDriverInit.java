package utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.SocketException;

public class WebDriverInit {

    protected static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public WebDriver initializeDriver() {
        //System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\webdrivers\\msedgedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    public void closeDriver(WebDriver driver) throws SocketException {
        driver.quit();
    }
}
