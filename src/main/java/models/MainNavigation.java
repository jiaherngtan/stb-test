package models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import utility.AssertFactory;
import utility.Common;

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

    @FindBy(xpath = "//div[contains(@class, 'dropDownContainer')]//a[text()='Featured Neighbourhoods']")
    WebElement featuredNeighbourhoods;

    @FindBy(xpath = "//*[text()='Neighbourhoods']/following-sibling::div//a")
    List<WebElement> neighbourhoods;

    @FindBy(xpath = "//div[contains(@class, 'search-button')]")
    WebElement btnSearch;

    @FindBy(id = "searchField")
    WebElement searchField;

    // Navigation
    public void hoverOverNeighbourhoodsMenu() {
        // Hover to the neighbourhoods sub-menu
        Actions actions = new Actions(driver);
        actions.moveToElement(navNeighbourhoods).perform();
        Common.sleep(3);
    }

    // Navigation
    public void goToFeaturedNeighbourhoodsPage() {
        hoverOverNeighbourhoodsMenu();
        Common.waitUntilVisible(featuredNeighbourhoods);
        Common.jsClickElement(driver, featuredNeighbourhoods);
    }

    // Navigation
    public void backToHomePage() {
        // Go back to home page by clicking the main logo
        Common.jsClickElement(driver, mainLogo);
    }

    public void verifyListOfNeighbourhoods(List<String> testData) {
        // Assert the number of selections
        AssertFactory.assertSameValue(neighbourhoods.size(), testData.size());

        // Verify selections are valid against test data
        boolean listItemsAreValid = true;
        for (WebElement neighbourhood : neighbourhoods) {
            // Get neighbourhood name and remove any inner tag if there is any
            String neighbourhoodName = Objects.requireNonNull(neighbourhood.getDomProperty("innerHTML"))
                    .split("<")[0];
            Log.info(neighbourhoodName);
            if (!testData.contains(neighbourhoodName))
                listItemsAreValid = false;
        }
        Assert.assertTrue(listItemsAreValid);
    }

    public void verifyListContainsNeighbourhood(String target) {
        boolean selectionFound = false;
        for (WebElement neighbourhood : neighbourhoods) {
            // Get neighbourhood name and remove any inner tag if there is any
            String neighbourhoodName = Objects.requireNonNull(neighbourhood.getDomProperty("innerHTML"))
                    .split("<")[0];
            if (neighbourhoodName.equalsIgnoreCase(target)) {
                selectionFound = true;
                break;
            }
        }
        Assert.assertTrue(selectionFound);
        Log.info("Passed - selection {} found", target);
    }

    public void verifySearchFunction(String searchString) {
        // Verify search button is visible and clickable
        AssertFactory.assertElementIsDisplayed(btnSearch);
        btnSearch.click();

        // Verify search field is visible and perform search
        Common.waitUntilVisible(searchField);
        Common.sleep(1);
        searchField.sendKeys(searchString);
        Common.sleep(1);
        searchField.sendKeys(Keys.ENTER);
        Common.sleep(5);

        // Select the first search result
        WebElement firstSelection = driver.findElement(By.xpath(
                "//div[@class='searchResult']//a"));
        Common.waitUntilVisible(firstSelection);
        firstSelection.click();
        Common.sleep(5);
    }
}
