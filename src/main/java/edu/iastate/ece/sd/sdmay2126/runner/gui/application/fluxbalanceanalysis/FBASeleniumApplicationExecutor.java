package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumApplicationExecutor;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumIdentificationException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class FBASeleniumApplicationExecutor implements SeleniumApplicationExecutor {
    private final WebDriver driver;

    public FBASeleniumApplicationExecutor(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void executeApplication(Job job, WebElement scopedFBACard) throws SeleniumIdentificationException,
            InterruptedException {
        System.out.println("Executing FBA application...");

        // Wait until the run button is enabled after a reset
        System.out.println("Locating FBA run button...");
        List<WebElement> runButtons = SeleniumUtilities.waitForNMatches(
                driver,
                By.cssSelector("button[data-button='runApp']"),
                1,
                Duration.ofSeconds(10),
                scopedFBACard
        );

        System.out.println("Run buttons located; clicking the FBA run...");
        runButtons.get(0).click();

        // Locate the cancel buttons
        System.out.println("Locating cancel buttons...");
        List<WebElement> cancelButtons = SeleniumUtilities.waitForNMatches(
                driver,
                By.cssSelector("button[data-button='cancel']"),
                1,
                Duration.ofSeconds(10),
                scopedFBACard
        );

        // Wait for the 3rd cancel to disappear, indicating the end of the simulation
        System.out.println("Waiting for cancel button to disappear, indicating simulation completion...");
        SeleniumUtilities.waitForVisibilityChange(cancelButtons.get(0), false, Duration.ofMinutes(30));
        System.out.println("Simulation complete, as signaled by the cancel button");
    }
}
