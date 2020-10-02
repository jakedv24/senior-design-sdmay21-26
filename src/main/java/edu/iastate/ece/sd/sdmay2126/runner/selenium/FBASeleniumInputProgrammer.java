package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FBASeleniumInputProgrammer implements SeleniumInputProgrammer {
    private WebDriver driver;

    public FBASeleniumInputProgrammer(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void programInputs(Job job) throws SeleniumIdentificationException, InterruptedException {
        System.out.println("Programming FBA inputs...");

        FBAParameters params = (FBAParameters) job.getParameters();

        // Reset the FBA Application
        resetFBAIfRequired();

        System.out.println("Looking for code cell...");
        WebElement codeBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("div[class^='cell code_cell']")))
                .get(2);
        // Button will be obscured temporarily if we used the reset dialog, so try for 10 seconds before failing
        SeleniumUtilities.clickUntilSuccessful(codeBox, Duration.ofSeconds(10));

        // There are two buttons that show and hide advanced options we want to click the second one
        System.out.println("Toggling advanced options...");
        String showAdvancedCSSSelector = "button[title='show advanced']";
        SeleniumUtilities.waitForNMatches(driver, By.cssSelector(showAdvancedCSSSelector), 2, Duration.ofSeconds(30));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> codeBox.findElements(By.cssSelector(showAdvancedCSSSelector)))
                .get(1)
                .click();

        // The check boxes are children of divs
        System.out.println("Setting FVA...");
        WebElement fva = driver
                .findElement(By.cssSelector("div[data-parameter='fva']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        // If the check box doesn't match what is selected the it is clicked
        if (fva.isSelected() != params.isFluxVariabilityAnalysis()) {
            fva.click();
        }

        System.out.println("Setting minimize flux...");
        WebElement minFlux = driver
                .findElement(By.cssSelector("div[data-parameter='minimize_flux']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        if (minFlux.isSelected() != params.isMinimizeFlux()) {
            System.out.println("clicking flux");

            minFlux.click();
        }

        System.out.println("Setting simulate KO...");
        WebElement simKo = driver
                .findElement(By.cssSelector("div[data-parameter='simulate_ko']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        if (simKo.isSelected() != params.isSimulateAllSingleKos()) {

            simKo.click();
        }

        // Finds clear and sets the value of the activeCoefficient value
        System.out.println("Setting activation coefficient...");
        WebElement activCoe = driver
                .findElement(By.cssSelector("div[data-parameter='activation_coefficient']"))
                .findElement(By.cssSelector("input[class='form-control']"));
        activCoe.clear();
        activCoe.sendKeys(Float.toString(params.getActivationCoefficient()));

        System.out.println("Programming FBA complete.");
    }

    private void resetFBAIfRequired() {
        System.out.println("Checking if FBA needs to be reset...");
        try {
            clickButtonWithCssSelector("button[class='btn btn-default -rerun']");
            clickButtonWithCssSelector("button[class='btn btn-primary']");
            System.out.println("FBA has been reset.");
        } catch (TimeoutException e) {
            // No-Op : App does not need reset - no action required.
            System.out.println("FBA did not require a reset.");
        }
    }

    private void clickButtonWithCssSelector(String selector) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)))
                .click();
    }
}
