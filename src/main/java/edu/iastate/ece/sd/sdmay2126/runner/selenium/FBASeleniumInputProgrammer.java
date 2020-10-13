package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FBASeleniumInputProgrammer implements SeleniumInputProgrammer {
    private WebDriver driver;

    public FBASeleniumInputProgrammer(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void programInputs(Job job, WebElement scopedFBACard) throws SeleniumIdentificationException, InterruptedException {
        System.out.println("Programming FBA inputs...");

        FBAParameters params = (FBAParameters) job.getParameters();

        // Reset the FBA Application
        resetFBAIfRequired();

        // Button will be obscured temporarily if we used the reset dialog, so try for 10 seconds before failing
        SeleniumUtilities.clickUntilSuccessful(scopedFBACard, Duration.ofSeconds(10));

        // There are two buttons that show and hide advanced options we want to click the second one
        System.out.println("Toggling advanced options...");
        String showAdvancedCSSSelector = "button[title='show advanced']";
        SeleniumUtilities.waitForNMatches(driver, By.cssSelector(showAdvancedCSSSelector), 2, Duration.ofSeconds(30));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> scopedFBACard.findElement(By.cssSelector(showAdvancedCSSSelector)))
                .click();

        // The check boxes are children of divs
        System.out.println("Setting FVA...");
        WebElement fva = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='fva']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        // If the check box doesn't match what is selected the it is clicked
        if (fva.isSelected() != params.isFluxVariabilityAnalysis()) {
            fva.click();
        }

        System.out.println("Setting minimize flux...");
        WebElement minFlux = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='minimize_flux']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        if (minFlux.isSelected() != params.isMinimizeFlux()) {
            System.out.println("clicking flux");

            minFlux.click();
        }

        System.out.println("Setting simulate KO...");
        WebElement simKo = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='simulate_ko']"))
                .findElement(By.cssSelector("input[type='checkbox']"));
        if (simKo.isSelected() != params.isSimulateAllSingleKos()) {
            simKo.click();
        }

        // Finds clear and sets the value of the activeCoefficient value
        System.out.println("Setting activation coefficient...");
        WebElement activCoe = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='activation_coefficient']"))
                .findElement(By.cssSelector("input[class='form-control']"));
        activCoe.clear();
        activCoe.sendKeys(Float.toString(params.getActivationCoefficient()));

        //Set reaction to maximize
        System.out.println("Setting reaction to Maximize");
        //clear out any old selections
        WebElement reactionToMaximizeArea = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='target_reaction']"));


        WebElement selectedItemsRM = reactionToMaximizeArea
                .findElement(By.cssSelector("div[data-element='selected-items']"));
        List<WebElement> alreadySelectedRM = selectedItemsRM
                .findElements(By.cssSelector("span[class='fa fa-minus-circle']"));


        if (!alreadySelectedRM.isEmpty()) {
            alreadySelectedRM.get(0).click();
        }
        //search for item
        WebElement reactionToMaxSearchBox = reactionToMaximizeArea
                .findElement(By.cssSelector("input[class='form-contol']")); //control is misspelled on the kbase html
        reactionToMaxSearchBox.clear();
        reactionToMaxSearchBox.sendKeys(params.getReactionToMaximize());
        WebElement availableRMItems = reactionToMaximizeArea
                .findElement(By.cssSelector("div[data-element='available-items"));
        List<WebElement> foundRMItem = availableRMItems
                .findElements(By.cssSelector("span[class='kb-btn-icon']"));
        if (foundRMItem.size() == 0) {
            System.out.println("Unable to find " + params.getReactionToMaximize() + ". defaulting to bio1");
            reactionToMaxSearchBox.clear();
            reactionToMaxSearchBox.sendKeys("bio1");
            foundRMItem = availableRMItems.findElements(By.cssSelector("span[class='kb-btn-icon']"));
        }
        foundRMItem.get(0).click();


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
