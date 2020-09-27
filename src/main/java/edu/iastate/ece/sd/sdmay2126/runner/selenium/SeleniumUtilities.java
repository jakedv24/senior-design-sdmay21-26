package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Provides basic, composable Selenium operations.
 */
public class SeleniumUtilities {
    /** Reduce spinning while waiting. */
    private static final int POLLING_DELAY_MILLIS = 50;

    /**
     * Waits for an element's visibility to change.
     * @param element The element to watch.
     * @param desiredVisibility The visibility to wait for.
     * @param maxWait How long to wait for.
     */
    public static void waitForVisibilityChange(WebElement element, boolean desiredVisibility, Duration maxWait) {
        long startTime = System.currentTimeMillis();
        try {
            // Loop while element doesn't match desired visibility until max time elapsed
            while ((desiredVisibility && element.isDisplayed()) && System.currentTimeMillis() - startTime < maxWait.toMillis()) {
                // Reduce spinning with polling delay
                Thread.sleep(POLLING_DELAY_MILLIS);
            }
        } catch (StaleElementReferenceException | InterruptedException e) {
            // In cases where desiredVisibility is false, a StaleElementReferenceException may be thrown
        }
    }

    /**
     * Waits until N elements matching the query are available.
     * @param parent The parent to query under.
     * @param query The query whose results to consider.
     * @param minMatches The minimum-matches of the query to unblock.
     * @param maxWait The maximum time to wait before failing.
     * @return The matches, which will have a size of >= minMatches.
     * @throws SeleniumIdentificationException If N results don't come available within the maxWait period.
     */
    public static List<WebElement> waitForNMatches(WebElement parent, By query, int minMatches, Duration maxWait) throws InterruptedException, SeleniumIdentificationException {
        long startTime = System.currentTimeMillis();
        List<WebElement> elements;

        // Loop until we find N elements or timeout
        do {
            // Apply the query
            elements = parent.findElements(query);

            // Check if we've found the minimum-required
            if (elements.size() >= minMatches) {
                return elements;
            }

            // Reduce spinning with polling delay
            Thread.sleep(POLLING_DELAY_MILLIS);
        } while(System.currentTimeMillis() - startTime < maxWait.toMillis());

        throw new SeleniumIdentificationException("Could not identify " + minMatches + " elements within duration.");
    }
}
