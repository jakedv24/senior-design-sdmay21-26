package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.InvalidApplicationException;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FBASeleniumApplicationExecutor implements SeleniumApplicationExecutor {
    private final WebDriver driver;

    public FBASeleniumApplicationExecutor(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void executeApplication(Job job) {
        // run the application
        clickButtonWithCssSelector("button[data-button='runApp']");
    }

    private void clickButtonWithCssSelector(String selector) {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector))).click();
    }
}
