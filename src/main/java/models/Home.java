package models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utility.AssertFactory;

import java.util.Objects;

public class Home {

    final WebDriver driver;

    public Home(WebDriver driver) {
        this.driver = driver;
        // Create all WebElements
        PageFactory.initElements(driver, this);
    }

    // Page Objects
    @FindBy(id = "onetrust-banner-sdk")
    WebElement cookiePrompt;

    @FindBy(xpath = "//button[text()='ACCEPT ALL']")
    WebElement btnAcceptCookies;

    public void acceptCookies() {
        if (cookiePrompt.isDisplayed())
            btnAcceptCookies.click();
    }

    public void verifyPageTitle(String expectedPageTitle) {
        AssertFactory.assertSameText(Objects.requireNonNull(driver.getTitle()), expectedPageTitle);
    }
}
