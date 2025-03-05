package utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AssertFactory {

    private static final Logger Log = LogManager.getLogger(AssertFactory.class);

    public static void assertElementIsDisplayed(WebElement element) {
        int elementSize = 0;
        try {
            if (element.isDisplayed()) {
                Log.info("Passed assert element is displayed.");
                elementSize = 1;
            }
        } catch (NoSuchElementException ex) {
            Log.info("Failed assert element is displayed.");
        }
        Assert.assertEquals(elementSize, 1);
    }

    public static void assertSameText(String actualText, String expectedText) {
        boolean result = actualText.equals(expectedText);
        if (result) {
            Log.info("Passed assert same text. Expected text: {}; Actual text: {}", expectedText, actualText);
        } else {
            Log.info("Failed assert same text. Expected text: {}; Actual text: {}", expectedText, actualText);
        }
        Assert.assertEquals(actualText, expectedText);
    }

    public static void assertContainsText(String actualText, String expectedText) {
        boolean result = StringUtils.containsIgnoreCase(actualText, expectedText);
        if (result) {
            Log.info("Passed assert contains text. Expected text: {}; Actual text: {}", expectedText, actualText);
        } else {
            Log.info("Failed assert contains text. Expected text: {}; Actual text: {}", expectedText, actualText);
        }
        Assert.assertTrue(result);
    }

    public static void assertSameValue(int actualCount, int expectedCount) {
        boolean result = actualCount == expectedCount;
        if (result) {
            Log.info("Passed assert same value. Expected count: {}; Actual count: {}", expectedCount, actualCount);
        } else {
            Log.info("Failed assert same value. Expected count: {}; Actual count: {}", expectedCount, actualCount);
        }
        Assert.assertEquals(actualCount, expectedCount);
    }
}
