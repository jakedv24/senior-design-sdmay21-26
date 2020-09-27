package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FBASeleniumApplicationExecutor implements SeleniumApplicationExecutor {
    private final WebDriver driver;

    FBASeleniumApplicationExecutor(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void executeApplication(Job job) {
        try {
            // Wait until the run button is enabled after a reset.
            Thread.sleep(2000);
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> d.findElements(By.cssSelector("button[data-button='runApp']")))
                    .get(2) // FBA is the 3rd application on the page, thus the 3rd run button (0-indexed)
                    .click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
