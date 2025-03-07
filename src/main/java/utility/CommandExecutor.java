package utility;

import models.FeaturedNeighbourhoods;
import models.Home;
import models.MainNavigation;
import models.Neighbourhood;

import org.openqa.selenium.WebDriver;

import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class CommandExecutor {

    protected static WebDriver driver;

    public void startWebDriver() { driver = new WebDriverInit().initializeDriver(); }

    public void goToPage(String url) { driver.get(url); }

    public void acceptCookies() { new Home(driver).acceptCookies(); }

    public void verifyPageTitle(String expectedPageTitle) { new Home(driver).verifyPageTitle(expectedPageTitle); }

    public void hoverOverNeighbourhoodsMenu() { new MainNavigation(driver).hoverOverNeighbourhoodsMenu(); }

    public void verifyListOfNeighbourhoods(List<String> neighbourhoods) {
        new MainNavigation(driver).verifyListOfNeighbourhoods(neighbourhoods);
    }

    public void verifyListContainsNeighbourhood(String selectedNeighbourhood) {
        new MainNavigation(driver).verifyListContainsNeighbourhood(selectedNeighbourhood);
    }

    public void verifySearchFunction(String searchString) {
        new MainNavigation(driver).verifySearchFunction(searchString);
    }

    public void goToFeaturedNeighbourhoodsPage() { new MainNavigation(driver).goToFeaturedNeighbourhoodsPage(); }

    public void backToHomePage() { new MainNavigation(driver).backToHomePage(); }

    public void verifyFeaturedNeighbourhoods(String defaultSelection) {
        new FeaturedNeighbourhoods(driver).verifyFeaturedNeighbourhoods(defaultSelection);
    }

    public void selectNeighbourhoodFromMap(String selectedNeighbourhood) {
        new FeaturedNeighbourhoods(driver).selectNeighbourhoodFromMap(selectedNeighbourhood);
    }

    public void verifyMapIconState(String selectedNeighbourhood, boolean hovered) {
        new FeaturedNeighbourhoods(driver).verifyMapIconState(selectedNeighbourhood, hovered);
    }

    public void verifyFindOutMoreBtn() { new FeaturedNeighbourhoods(driver).verifyFindOutMoreBtn(); }

    public void verifyWhereToEat(Map<String, String> restaurants, String selection) {
        new Neighbourhood(driver).verifyWhereToEat(restaurants, selection);
    }

    public void verifyBreadcrumbNavPresent(String expectedBreadcrumbNav) {
        new Neighbourhood(driver).verifyBreadcrumbNavPresent(expectedBreadcrumbNav);
    }

    public void navigateFromBreadcrumbNav(String navName) {
        new Neighbourhood(driver).navigateFromBreadcrumbNav(navName);
    }

    public void verifyNumOfMrtStations(int expectedNum) {
        new Neighbourhood(driver).verifyNumOfMrtStations(expectedNum);
    }

    public void closeWebDriver() {
        if (driver != null) {
            try {
                new WebDriverInit().closeDriver(driver);
            } catch (SocketException ignored) {}
            driver = null;
        }
    }
}
