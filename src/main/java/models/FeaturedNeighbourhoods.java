package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utility.AssertFactory;
import utility.Common;

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
        // Verify able to visit "Featured Neighbourhoods" by checking the label under <span>
        WebElement featuredNeighbourhoodTxt = driver.findElement(By.xpath(
                "//span[text()='Featured Neighbourhoods']"));
        AssertFactory.assertElementIsDisplayed(featuredNeighbourhoodTxt);

        // Verify the default selection
        // Current selection can be determined by current expanded selection with keyword "expand" under class
        Common.waitUntilVisible(currentSelection);
        String currentSelectionTitle = currentSelection.getDomAttribute("data-id");
        assert currentSelectionTitle != null;
        AssertFactory.assertSameText(currentSelectionTitle, defaultSelection);
    }

    public WebElement getIcon(String icon) {
        // Identify the xpath of the icon and click it
        String selectionXPath = String.format(
                "//div[contains(@class, 'stb-neighbourhood_map-icon')]/img[@alt='%s']", icon);
        WebElement selectedIcon = driver.findElement(By.xpath(selectionXPath));
        Common.waitUntilVisible(selectedIcon);
        return selectedIcon;
    }

    public void selectNeighbourhoodFromMap(String selectedNeighbourhood) {
        // Click the icon
        Common.jsClickElement(driver, getIcon(selectedNeighbourhood));
        Log.info("Selected neighbourhood {} clicked", selectedNeighbourhood);
        Common.sleep(3);

        // Verify the current selection is updated
        String currentSelectionTitle = currentSelection.getDomAttribute("data-id");
        assert currentSelectionTitle != null;
        AssertFactory.assertSameText(currentSelectionTitle, selectedNeighbourhood);
    }

    public void verifyMapIconState(String selectedNeighbourhood, boolean hovered) {
        // State "default-state" if not selected, state will be "hover-state" if selected
        WebElement elem = getIcon(selectedNeighbourhood);
        String property = elem.getDomProperty("src");
        if (hovered) {
            AssertFactory.assertContainsText(property, "hover-state");
        } else {
            AssertFactory.assertContainsText(property, "default-state");
        }
    }

    public void verifyFindOutMoreBtn() {
        WebElement btnFindOutMore = currentSelection.findElement(By.xpath(
                ".//a[text()='Find Out More']"));
        /*Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> btnFindOutMore.isDisplayed());*/
        AssertFactory.assertElementIsDisplayed(btnFindOutMore);

        // Click the Find Out More button
        Common.jsClickElement(driver, btnFindOutMore);
    }
}