import models.FeaturedNeighbourhoods;
import models.Home;
import models.MainNavigation;

import models.Neighbourhood;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import utility.AssertFactory;
import utility.Common;
import utility.WebDriverInit;

import java.util.List;
import java.util.Map;

public class WebAppTest {

    protected static WebDriver driver;

    protected static MainNavigation mainNavigation;

    protected static Home home;

    protected static FeaturedNeighbourhoods featuredNeighbourhoods;

    protected static Neighbourhood neighbourhood;

    @BeforeSuite
    public void testSuiteSetup() {
        // Initialize WebDriver and all the models
        driver = new WebDriverInit().initializeDriver();
        mainNavigation = new MainNavigation(driver);
        home = new Home(driver);
        featuredNeighbourhoods = new FeaturedNeighbourhoods(driver);
        neighbourhood = new Neighbourhood(driver);

        // Go to homepage
        driver.get(Common.VISIT_SINGAPORE_URL);
        Common.sleep(3);

        // Accept cookies if any
        home.acceptCookies();

        Common.sleep(3);
    }

    @Test(priority = 1, groups = {"Start from Homepage"},
            description = "verify the page title on the home page")
    public void verifyPageTitle() {
        // Test data
        String expectedPageTitle = "Visit Singapore Official Site - Discover Singapore's Best Attractions";
        AssertFactory.assertSameText(mainNavigation.getTitle(), expectedPageTitle);
    }

    @Test(priority = 2, groups = {"Start from Homepage"},
            description = "verify list of selections under 'Neighbourhoods' in nav menu, " +
            "and <neighbourhood> is one of the selection")
    public void verifyListOfNeighbourhoods() {
        // Test data
        List<String> neighbourhoods = List.of(
                "Featured Neighbourhoods", "Chinatown", "Civic District", "Kampong Gelam", "Katong-Joo Chiat",
                "Little India", "Mandai", "Marina Bay", "Orchard Road", "Sentosa Island", "Singapore River");
        String selectedNeighbourhood = "Orchard Road";

        mainNavigation.verifyListOfNeighbourhoods(neighbourhoods);
        mainNavigation.verifyListContainsNeighbourhood(selectedNeighbourhood);
    }

    @Test(priority = 3, groups = {"Start from Homepage"},
            description = "verify able to visit 'Featured Neighbourhood and showing the correct default selection")
    public void verifyFeaturedNeighbourhoods() {
        // Test data
        String defaultSelection = "Civic District";

        mainNavigation.goToFeaturedNeighbourhoodsPage();
        featuredNeighbourhoods.verifyFeaturedNeighbourhoods(defaultSelection);
    }

    @Test(priority = 4, description = "select the <neighbourhood> icon from the map and verify the description")
    public void verifyNeighbourhoodIcon() {
        // Test data
        String neighbourhood = "Marina Bay";

        //mainNavigation.goToFeaturedNeighbourhoodsPage();
        featuredNeighbourhoods.selectNeighbourhoodFromMap(neighbourhood);
        featuredNeighbourhoods.verifyMapIconState(neighbourhood, true);
    }

    @Test(priority = 5, description = "switch to Sentosa icon and make sure both icons change")
    public void switchSelectionOnMap() {
        // Test data
        String neighbourhood = "Marina Bay";
        String newNeighbourhood = "Sentosa";

        featuredNeighbourhoods.selectNeighbourhoodFromMap(newNeighbourhood);
        featuredNeighbourhoods.verifyMapIconState(neighbourhood, false);
        featuredNeighbourhoods.verifyMapIconState(newNeighbourhood, true);
    }

    @Test(priority = 6, description = "select 'Find out more' button")
    public void verifyFindOutMoreBtn() {
        featuredNeighbourhoods.verifyFindOutMoreBtn();
    }

    @Test(priority = 7, description = "select a restaurant from 'Where to eat', " +
            "make sure detail page opened on a new tab, " +
            "close the tab and switch back to neighbourhood page")
    public void selectWhereToEat() {
        // Test data
        Map<String, String> restaurants = Map.of(
                "Ocean Restaurant", "https://www.rwsentosa.com/en/dine/signature-restaurants/ocean-restaurant",
                "Imamura", "https://imamurasg.com/");
        String selection = "Ocean Restaurant";

        neighbourhood.verifyWhereToEat(restaurants, selection);
    }

    @Test(priority = 8, groups = {"Start from Homepage"}, description = "check the search icon functionalities, " +
            "perform search and verify the presence of breadcrumb navigation feature")
    public void verifySearchFunction() {
        // Test data
        String searchString = "Gardens by the bay";
        String expectedBreadcrumbNav = "Home;Featured Neighbourhoods;Marina Bay"; // separated by semicolon

        mainNavigation.verifySearchFunction(searchString);
        neighbourhood.verifyBreadcrumbNavPresent(expectedBreadcrumbNav);
    }

    @Test(priority = 9, description = "go to <neighbourhood> via breadcrumb navigation, and verify the numbers of MRT")
    public void verifyBreadcrumbNav() {
        // Test data
        String navName = "Marina Bay";
        int expectedNum = 4;

        neighbourhood.verifyBreadcrumbNav(navName);
        neighbourhood.verifyNumOfMrtStations(expectedNum);
    }

    @BeforeGroups(groups={"Start from Homepage"}, alwaysRun = true)
    public void goToHome() {
        mainNavigation.backToHomePage();}

    @AfterMethod
    public void sleep() {
        Common.sleep(3);
    }

    @AfterSuite
    public void cleanUp() {
        driver.quit();
    }
}
