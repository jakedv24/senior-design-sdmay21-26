package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FBASeleniumApplicationExecutor implements SeleniumApplicationExecutor {
    private final WebDriver driver;

    FBASeleniumApplicationExecutor(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void executeApplication(Job job) throws SeleniumIdentificationException, InterruptedException {
        System.out.println("Executing FBA application...");

        // Wait until the run button is enabled after a reset
        System.out.println("Locating 3 run buttons...");
        List<WebElement> runButtons = SeleniumUtilities.waitForNMatches(
                driver,
                By.cssSelector("button[data-button='runApp']"),
                3,
                Duration.ofSeconds(10)
        );

        // FBA is the 3rd application on the page, thus the 3rd run button (0-indexed)
        System.out.println("Run buttons located; clicking the FBA run...");
        runButtons.get(2).click();

        // TODO: Wait for execution to cease
    }
}
