package edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumIdentificationException;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumUtilities;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationFlow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;

/**
 * A Selenium automation flow for 3rd-party authentication provider, Globus.
 */
public class GlobusAuthenticationFlow implements SeleniumAuthenticationFlow {
    private final WebDriver driver;
    private final String username;
    private final String password;

    public GlobusAuthenticationFlow(@Nonnull WebDriver driver,
                                    GlobusAuthenticationConfiguration authenticationConfiguration) {
        this.driver = driver;
        username = authenticationConfiguration.getUsername();
        password = authenticationConfiguration.getPassword();
    }

    @Override
    public void authenticateSession() throws SeleniumIdentificationException, InterruptedException {
        System.out.println("Initiating Globus authentication...");

        // Locate the login iframe
        System.out.println("Finding auth provider iframe...");
        WebElement authProviderFrame = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.tagName("iframe")));

        driver.switchTo().frame(authProviderFrame);

        // Wait for the login buttons to load
        System.out.println("Looking for login buttons...");
        List<WebElement> loginButtons = SeleniumUtilities.waitForNMatches(
                driver, By.className("signin-button"), 3, Duration.ofSeconds(10), null);

        // Click the Globus auth button (we'll wait for it to render)
        System.out.println("Clicking Globus login...");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> loginButtons.get(2).isDisplayed());
        loginButtons.get(2).click();

        // Switch back to the window (from the iframe)
        driver.switchTo().defaultContent();

        // Locate the Globus "continue" button
        System.out.println("Watching for Continue button...");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.name("identity_provider")))
                .click();

        // Locate and fill the username and password fields
        System.out.println("Filling form...");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.id("ember24")))
                .sendKeys(username);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.id("ember25")))
                .sendKeys(password);

        // Submit the login form
        System.out.println("Submitting form...");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.cssSelector("input[type=\"submit\"]")))
                .click();

        for (int retry = 0; retry < 3; retry++) {
            // Locate the confirmation iframe
            System.out.println("Locating confirmation iframe...");
            WebElement confirmFrame = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> d.findElement(By.tagName("iframe")));

            // Switch to the account confirmation frame
            driver.switchTo().frame(confirmFrame);

            // Acknowledge account
            try {
                System.out.println("Acknowledging account (try " + (retry + 1) + " of 3)...");
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(d -> d.findElement(By.tagName("button")))
                        .click();

                // Successful; break to avoid retrying
                break;
            } catch (Exception e) {
                System.out.println("Failed to identify the confirmation button");
            } finally {
                // Swap back to the window
                driver.switchTo().defaultContent();
            }
        }
    }
}
