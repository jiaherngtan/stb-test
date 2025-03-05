package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import utility.AssertFactory;
import utility.Common;

import java.time.Duration;

public class FeaturedNeighbourhoods {

    private static final Logger Log = LogManager.getLogger(FeaturedNeighbourhoods.class);

    final WebDriver driver;

    public FeaturedNeighbourhoods(WebDriver driver) {
        this.driver = driver;
        // Create all WebElements
        PageFactory.initElements(driver, this);
    }

    // Page Objects
    @FindBy(xpath = "//div[contains(@class, 'stb-neighbourhood_expand')]")
    WebElement currentSelection;

    public void verifyFeaturedNeighbourhoods(String defaultSelection) {
        try {
            // Verify able to visit "Featured Neighbourhoods" by checking the label under <span>
            WebElement featuredNeighbourhoodTxt = driver.findElement(By.xpath(
                    "//span[text()='Featured Neighbourhoods']"));
            AssertFactory.assertElementIsDisplayed(featuredNeighbourhoodTxt);

            // Verify the default selection
            // Current selection can be determined by current expanded selection with keyword "expand" under class
            AssertFactory.assertElementIsDisplayed(currentSelection);
            String currentSelectionTitle = currentSelection.getDomAttribute("data-id");
            assert currentSelectionTitle != null;
            AssertFactory.assertSameText(currentSelectionTitle, defaultSelection);
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }

    public WebElement getIcon(String icon) {
        // Identify the xpath of the icon and click it
        String selectionXPath = String.format(
                "//div[contains(@class, 'stb-neighbourhood_map-icon')]/img[@alt='%s']", icon);
        return driver.findElement(By.xpath(selectionXPath));
    }

    public void selectNeighbourhoodFromMap(String icon) {
        try {
            // Click the icon
            Common.jsClickElement(driver, getIcon(icon));
            Common.sleep(3);

            // Verify the current selection is updated
            String currentSelectionTitle = currentSelection.getDomAttribute("data-id");
            assert currentSelectionTitle != null;
            AssertFactory.assertSameText(currentSelectionTitle, icon);
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }

    public void verifyMapIconState(String icon, boolean hovered) {
        // State "default-state" if not selected, state will be "hover-state" if selected
        WebElement elem = getIcon(icon);
        String property = elem.getDomProperty("src");
        if (hovered) {
            AssertFactory.assertContainsText(property, "hover-state");
        } else {
            AssertFactory.assertContainsText(property, "default-state");
        }
    }

    public void verifyFindOutMoreBtn() {
        try {
            WebElement btnFindOutMore = currentSelection.findElement(By.xpath(
                    ".//a[text()='Find Out More']"));
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(d -> btnFindOutMore.isDisplayed());

            // Click the Find Out More button
            Common.jsClickElement(driver, btnFindOutMore);
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }
}