package edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationFlow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nonnull;

/**
 * A Selenium automation flow for 3rd-party authentication provider, Globus.
 */
public class GlobusAuthenticationFlow implements SeleniumAuthenticationFlow {
    private final WebDriver driver;
    private final String username, password;

    public GlobusAuthenticationFlow(@Nonnull WebDriver driver, String username, String password) {
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

    @Override
    public void authenticateSession() {
        System.out.println("Initiating Globus authentication...");

        // Locate the login iframe
        WebElement loginFrame = new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("iframe")));

        // Swap the driver context into the frame
        driver.switchTo().frame(loginFrame);

        // Locate and click the Globus auth
        new WebDriverWait(driver, 10)
                .until(d -> d.findElements(By.className("signin-button")))
                .get(2) // Globus is #3
                .click();

        // Switch back to the window (from the iframe)
        driver.switchTo().defaultContent();

        // Locate the Globus "continue" button
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.name("identity_provider")))
                .click();

        // Locate and fill the username and password fields
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.id("ember24")))
                .sendKeys("sdmay2126");
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.id("ember25")))
                .sendKeys("sdmay2126pw");

        // Submit the login form
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.cssSelector("input[type=\"submit\"]")))
                .click();

        // Locate the confirmation iframe
        WebElement confirmFrame = new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("iframe")));

        // Swap the driver context into the frame
        driver.switchTo().frame(confirmFrame);

        // Acknowledge account
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("button")))
                .click();

        // Swap back to the window
        driver.switchTo().defaultContent();
    }
}
