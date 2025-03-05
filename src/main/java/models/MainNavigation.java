package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import services.AssertFactory;
import services.Common;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class MainNavigation {

    private static final Logger Log = LogManager.getLogger(MainNavigation.class);

    final WebDriver driver;

    public MainNavigation(WebDriver driver) {
        this.driver = driver;
        // Create all WebElements
        PageFactory.initElements(driver, this);
    }

    // Page Objects
    @FindBy(xpath = "//img[@alt='Visit Singapore']/..")
    WebElement mainLogo;

    @FindBy(xpath = "//header[contains(@class, 'stbHeader')]//*[text()='Neighbourhoods']")
    WebElement navNeighbourhoods;

    @FindBy(xpath = "//a[text()='Featured Neighbourhoods']")
    WebElement featuredNeighbourhoods;

    @FindBy(xpath = "//*[text()='Neighbourhoods']/following-sibling::div//a")
    List<WebElement> neighbourhoods;

    @FindBy(xpath = "//div[contains(@class, 'search-button')]")
    WebElement btnSearch;

    @FindBy(id = "searchField")
    WebElement searchField;

    // Getter
    public String getTitle() {
        return driver.getTitle();
    }

    // Navigation
    public void goToFeaturedNeighbourhoodsPage() {
        // Hover to the neighbourhoods sub menu
        Actions actions = new Actions(driver);
        actions.moveToElement(navNeighbourhoods).perform();
        Common.sleep(3);
        Common.jsClickElement(driver, featuredNeighbourhoods);
    }

    // Navigation
    public void backToHomePage() {
        // Go back to home page by clicking the main logo
        mainLogo.click();
    }

    public void verifyListOfNeighbourhoods(List<String> testData) {
        try {
            // hover to sub-menu
            Actions actions = new Actions(driver);
            actions.moveToElement(navNeighbourhoods).perform();
            Common.sleep(3);

            // Verify selections are valid against test data
            for (WebElement neighbourhood : neighbourhoods) {
                // Get neighbourhood name and remove any inner tag
                String neighbourhoodName = Objects.requireNonNull(neighbourhood.getDomProperty("innerHTML"))
                        .split("<")[0];
                Log.info(neighbourhoodName);
                Assert.assertTrue(testData.contains(neighbourhoodName));
            }

            // Assert the number of selections
            AssertFactory.assertSameValue(neighbourhoods.size(), testData.size());
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }

    public void verifyListContainsNeighbourhood(String target) {
        try {
            boolean selectionFound = false;
            for (WebElement neighbourhood : neighbourhoods) {
                // Get neighbourhood name and remove any inner tag
                String neighbourhoodName = Objects.requireNonNull(neighbourhood.getDomProperty("innerHTML"))
                        .split("<")[0];
                if (neighbourhoodName.equals(target)) {
                    selectionFound = true;
                    break;
                }
            }
            Assert.assertTrue(selectionFound);
            Log.info("Passed - selection {} found", target);
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }

    public void verifySearchFunction(String searchString) {
        try {
            // Verify search button is visible and clickable
            AssertFactory.assertElementIsDisplayed(btnSearch);
            btnSearch.click();

            // Verify search field is visible and perform search
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(d -> searchField.isDisplayed());
            searchField.sendKeys(searchString);
            Common.sleep(1);
            searchField.sendKeys(Keys.ENTER);
            Common.sleep(5);

            // Select the first search result
            WebElement firstSelection = driver.findElement(By.xpath(
                    "//div[@class='searchResult']//a"));
            firstSelection.click();
            Common.sleep(10);
        } catch (NoSuchElementException | TimeoutException e) {
            Log.error(e.getMessage());
        }
    }
}
