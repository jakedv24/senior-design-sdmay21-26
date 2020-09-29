package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Provides basic, composable Selenium operations.
 */
public class SeleniumUtilities {
    /**
     * Reduce spinning while waiting.
     */
    private static final int POLLING_DELAY_MILLIS = 50;

    /**
     * Waits for an element's visibility to change.
     *
     * @param element           The element to watch.
     * @param desiredVisibility The visibility to wait for.
     * @param maxWait           How long to wait for.
     */
    public static void waitForVisibilityChange(WebElement element, boolean desiredVisibility, Duration maxWait)
            throws SeleniumIdentificationException {
        long startTime = System.currentTimeMillis();
        try {
            // Loop while element doesn't match desired visibility until max time elapsed
            while (desiredVisibility != element.isDisplayed()) {
                // Fail if we've exceeded our wait period
                if (System.currentTimeMillis() - startTime >= maxWait.toMillis()) {
                    throw new SeleniumIdentificationException("Failed to detect a visibility change.");
                }

                // Reduce spinning with polling delay
                Thread.sleep(POLLING_DELAY_MILLIS);
            }
        } catch (StaleElementReferenceException | InterruptedException e) {
            // In cases where desiredVisibility is false, a StaleElementReferenceException may be thrown
        }
    }

    /**
     * Convenience method to call click until successful with a default value of 30 seconds.
     *
     * @param element the web element to attempt clicking
     */
    public static void clickUntilSuccessful(WebElement element) throws InterruptedException,
            SeleniumIdentificationException {
        clickUntilSuccessful(element, Duration.ofSeconds(30));
    }

    /**
     * Will attempt to click something, even if obscured, until successful. Note that this is necessary since we can't
     * determine if something is obscured until we try to interact with it.
     *
     * @param element The element to click.
     * @param maxWait How long to try before failing.
     */
    public static void clickUntilSuccessful(WebElement element, Duration maxWait) throws InterruptedException,
            SeleniumIdentificationException {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < maxWait.toMillis()) {
            try {
                element.click();
                return;
            } catch (ElementClickInterceptedException ignore) {
                // Sleep to avoid unnecessary spinning
                Thread.sleep(POLLING_DELAY_MILLIS);
            } catch (StaleElementReferenceException e) {
                return;
            }
        }

        throw new SeleniumIdentificationException("Could not successfully click the element in the wait period.");
    }

    /**
     * Waits until N elements matching the query are available.
     *
     * @param driver     The driver to query with.
     * @param query      The query whose results to consider.
     * @param minMatches The minimum-matches of the query to unblock.
     * @param maxWait    The maximum time to wait before failing.
     * @return The matches, which will have a size of >= minMatches.
     * @throws SeleniumIdentificationException If N results don't come available within the maxWait period.
     */
    public static List<WebElement> waitForNMatches(WebDriver driver, By query, int minMatches, Duration maxWait)
            throws InterruptedException, SeleniumIdentificationException {
        long startTime = System.currentTimeMillis();
        List<WebElement> elements;

        // Loop until we find N elements or timeout
        do {
            // Apply the query
            elements = new WebDriverWait(driver, Duration.ofMillis(POLLING_DELAY_MILLIS))
                    .until(d -> d.findElements(query));

            // Check if we've found the minimum-required
            if (elements.size() >= minMatches) {
                return elements;
            }

            // Reduce spinning with polling delay
            Thread.sleep(POLLING_DELAY_MILLIS);
        } while (System.currentTimeMillis() - startTime < maxWait.toMillis());

        throw new SeleniumIdentificationException("Could not identify " + minMatches + " elements within duration.");
    }
}
