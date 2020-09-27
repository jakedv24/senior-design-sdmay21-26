package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.time.Duration;

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
        // Inspired by: https://stackoverflow.com/a/36847689
        Long startTime = System.currentTimeMillis();
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
}
