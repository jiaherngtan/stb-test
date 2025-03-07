package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utility.AssertFactory;
import utility.Common;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Neighbourhood {

    private static final Logger Log = LogManager.getLogger(Neighbourhood.class);

    final WebDriver driver;

    public Neighbourhood(WebDriver driver) {
        this.driver = driver;
        // Create all WebElements
        PageFactory.initElements(driver, this);
    }

    // Page Objects
    @FindBy(xpath = "//div[contains(@class, 'stb-breadcrumbs_container')]/a")
    List<WebElement> breadcrumbNav;

    public void verifyWhereToEat(Map<String, String> restaurants, String selection) {
        // Get the expected URL
        String urlRestaurant = restaurants.get(selection);

        // Current URL
        String urlCurrent = driver.getCurrentUrl();

        // Identify the WebElement of the selected restaurant
        String selectionXPath = String.format("//*[text()='Where to Eat']/../../..//a[@aria-label='%s']", selection);
        Log.info("xpath: {}", selectionXPath);
        WebElement navSelectionWebsite = driver.findElement(By.xpath(selectionXPath));

        // Click only when it is in view (aria-hidden = false), otherwise toggle the next button
        boolean visible = Objects.requireNonNull(navSelectionWebsite.findElement(By.xpath("./../../.."))
                .getDomAttribute("aria-hidden")).equalsIgnoreCase("false");

        WebElement btnArrowRight = driver.findElement(By.xpath(
                "//*[text()='Where to Eat']/../..//button[contains(@class, 'stb-button_arrow-right')]"));
        Common.waitUntilVisible(btnArrowRight);

        while (!visible) {
            Common.jsClickElement(driver, btnArrowRight);
            Log.info("Clicked right");
            Common.sleep(1);
            visible = Objects.requireNonNull(navSelectionWebsite.findElement(By.xpath("./../../.."))
                    .getDomAttribute("aria-hidden")).equalsIgnoreCase("false");
        }
        Log.info("Restaurant is visible");
        Common.jsClickElement(driver, navSelectionWebsite);
        Common.sleep(5);

        // As new window is opened, instantiate a window handles to store all the windows
        Object[] windowHandles = driver.getWindowHandles().toArray();

        // Make sure the detail page is opened on a new tab
        // windowHandles[0] - original tab; windowHandles[1] - new tab
        driver.switchTo().window((String) windowHandles[1]);
        Common.sleep(3);
        AssertFactory.assertSameText(driver.getCurrentUrl(), urlRestaurant);

        // Close the current tab and switch back to neighbourhood page
        driver.close();
        driver.switchTo().window((String) windowHandles[0]);
        Common.sleep(3);
        AssertFactory.assertSameText(driver.getCurrentUrl(), urlCurrent);
    }

    public void verifyBreadcrumbNavPresent(String expectedBreadcrumbNav) {
        // Verify the presence of breadcrumb navigation
        StringBuilder builder = new StringBuilder();
        for (WebElement nav : breadcrumbNav) {
            builder.append(nav.getText()).append(";");
        }
        String actualBreadcrumbNav = builder.toString();
        Log.info(actualBreadcrumbNav);
        AssertFactory.assertSameText(
                actualBreadcrumbNav.substring(0, actualBreadcrumbNav.length() - 1), expectedBreadcrumbNav);
    }

    public void navigateFromBreadcrumbNav(String navName) {
        // Go to the destined location via breadcrumb navigation
        for (WebElement nav : breadcrumbNav) {
            String currNav = nav.getText();
            if (currNav.equalsIgnoreCase(navName)) {
                nav.click();
                break;
            }
        }

        // Verify successful navigation by comparing the heading
        WebElement heading = driver.findElement(By.tagName("h1"));
        Common.waitUntilVisible(heading);
        AssertFactory.assertSameText(heading.getText(), navName);
    }

    public void verifyNumOfMrtStations(int expectedNum) {
        // Verify the number of MRT stations
        WebElement nearestStationTitle = driver.findElement(By.xpath(
                "//div[@class='title']/following-sibling::div//*[text()='Nearest Station']"));
        List<WebElement> stations = nearestStationTitle.findElements(By.xpath(
                "./following-sibling::div/p"));

        for (WebElement station : stations) {
            Log.info("Station: " + station.getDomProperty("innerHTML"));
        }
        AssertFactory.assertSameValue(stations.size(), expectedNum);
    }
}
