import org.testng.ITestResult;
import org.testng.annotations.*;

import utility.*;

import java.util.List;
import java.util.Map;

//@Listeners(RetryListener.class)
public class WebAppTest {

    private static final CommandExecutor commandExecutor = new CommandExecutor();

    @BeforeSuite
    public void testSuiteSetup() {
        // Initialize WebDriver
        commandExecutor.startWebDriver();

        // Go to homepage
        commandExecutor.goToPage(Common.VISIT_SINGAPORE_URL);
        Common.sleep(5);

        // Accept cookies if any
        commandExecutor.acceptCookies();
        Common.sleep(3);
    }

    @Test(priority = 1, retryAnalyzer = utility.RetryFailedTestCases.class, groups = {"Start from Homepage"},
            description = "verify the page title of the home page")
    public void verifyPageTitle() {
        // Test data
        String expectedPageTitle = "Visit Singapore Official Site - Discover Singapore's Best Attractions";

        commandExecutor.verifyPageTitle(expectedPageTitle);
    }

    @Test(priority = 2, retryAnalyzer = utility.RetryFailedTestCases.class,
            description = "verify list of selections under 'Neighbourhoods' in nav menu, " +
            "and <selected neighbourhood> is one of the selection")
    public void verifyListOfNeighbourhoods() {
        // Test data
        List<String> neighbourhoods = List.of(
                "Featured Neighbourhoods", "Chinatown", "Civic District", "Kampong Gelam", "Katong-Joo Chiat",
                "Little India", "Mandai", "Marina Bay", "Orchard Road", "Sentosa Island", "Singapore River");
        String selectedNeighbourhood = "Orchard Road";

        commandExecutor.hoverOverNeighbourhoodsMenu();
        commandExecutor.verifyListOfNeighbourhoods(neighbourhoods);
        commandExecutor.verifyListContainsNeighbourhood(selectedNeighbourhood);
    }

    @Test(priority = 3, retryAnalyzer = utility.RetryFailedTestCases.class, groups = {"Start from Homepage"},
            description = "verify able to visit 'Featured Neighbourhood' and showing the correct default selection")
    public void verifyFeaturedNeighbourhoods() {
        // Test data
        String defaultSelection = "Civic District";

        commandExecutor.goToFeaturedNeighbourhoodsPage();
        commandExecutor.verifyFeaturedNeighbourhoods(defaultSelection);
    }

    @Test(priority = 4, description = "select <selected neighbourhood> icon from the map and verify the description")
    public void verifyNeighbourhoodIcon() {
        // Test data
        String selectedNeighbourhood = "Marina Bay";

        //commandExecutor.goToFeaturedNeighbourhoodsPage();
        commandExecutor.selectNeighbourhoodFromMap(selectedNeighbourhood);
        commandExecutor.verifyMapIconState(selectedNeighbourhood, true);
    }

    @Test(priority = 5, description = "switch to <new selected neighbourhood> icon and make sure both icons change")
    public void switchSelectionOnMap() {
        // Test data
        String selectedNeighbourhood = "Marina Bay";
        String selectedNewNeighbourhood = "Sentosa";

        commandExecutor.selectNeighbourhoodFromMap(selectedNewNeighbourhood);
        commandExecutor.verifyMapIconState(selectedNeighbourhood, false);
        commandExecutor.verifyMapIconState(selectedNewNeighbourhood, true);
    }

    @Test(priority = 6, description = "click 'Find out more' button")
    public void verifyFindOutMoreBtn() {
        commandExecutor.verifyFindOutMoreBtn();
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

        commandExecutor.verifyWhereToEat(restaurants, selection);
    }

    @Test(priority = 8, retryAnalyzer = utility.RetryFailedTestCases.class, groups = {"Start from Homepage"},
            description = "check the search icon functionalities, " +
            "perform search and verify the presence of breadcrumb navigation feature")
    public void verifySearchFunction() {
        // Test data
        String searchString = "Gardens by the bay";
        String expectedBreadcrumbNav = "Home;Featured Neighbourhoods;Marina Bay"; // separated by semicolon

        commandExecutor.verifySearchFunction(searchString);
        commandExecutor.verifyBreadcrumbNavPresent(expectedBreadcrumbNav);
    }

    @Test(priority = 9, description = "go to <neighbourhood> via breadcrumb navigation, and verify the numbers of MRT")
    public void verifyBreadcrumbNav() {
        // Test data
        String navName = "Marina Bay";
        int expectedNumOfMRTs = 4;

        commandExecutor.navigateFromBreadcrumbNav(navName);
        commandExecutor.verifyNumOfMrtStations(expectedNumOfMRTs);
    }

    @BeforeMethod(onlyForGroups = {"Start from Homepage"}, alwaysRun = true)
    public void goToHome() {
        commandExecutor.backToHomePage();
        Common.sleep(3);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE || result.getStatus() == ITestResult.SKIP) {
            System.out.println("Test failed / skipped");
        }
    }

    @AfterSuite
    public void cleanUp() {
        commandExecutor.closeWebDriver();
    }
}
