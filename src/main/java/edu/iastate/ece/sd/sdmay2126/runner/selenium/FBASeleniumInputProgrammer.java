package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Parameter;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class FBASeleniumInputProgrammer implements SeleniumInputProgrammer {
    private WebDriver driver;

    public FBASeleniumInputProgrammer(WebDriver driver) {
        this.driver = driver;
    }

    public void programInputFormWithCssSelectorAndKeys(String cssSelec, WebElement scopedFBACard, String keys) {
        WebElement tempVar = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='" + cssSelec + "']"))
                .findElement(By.cssSelector("input[class='form-control']"));
        tempVar.clear();
        tempVar.sendKeys(keys);
    }

    @Override
    public void programInputs(Job job, WebElement scopedFBACard) throws SeleniumIdentificationException,
            InterruptedException {
        System.out.println("Programming FBA inputs...");

        FBAParameters params = (FBAParameters) job.getParameters();

        // Reset the FBA Application
        resetFBAIfRequired();

        // Button will be obscured temporarily if we used the reset dialog, so try for 10 seconds before failing
        SeleniumUtilities.clickUntilSuccessful(scopedFBACard, Duration.ofSeconds(10));

        // There are two buttons that show and hide advanced options we want to click the second one
        System.out.println("Toggling advanced options...");
        String showAdvancedCSSSelector = "button[title='show advanced']";
        SeleniumUtilities.waitForNMatches(driver, By.cssSelector(showAdvancedCSSSelector),
                2, Duration.ofSeconds(30), scopedFBACard);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> scopedFBACard.findElement(By.cssSelector(showAdvancedCSSSelector)))
                .click();

        programCheckBoxes(scopedFBACard, params);

        programBasicFloatInputs(scopedFBACard, params);

        programGeneKnockOuts(scopedFBACard, params);

        programReactionToMaximize(scopedFBACard, params);

        programReactionKnockOuts(scopedFBACard, params);

        programCustomFLuxBounds(scopedFBACard, params);

        System.out.println("Programming FBA complete.");

    }

    private void programCheckBoxes(WebElement scopedFBACard, FBAParameters params) {
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

    }


    private void programBasicFloatInputs(WebElement scopedFBACard, FBAParameters params) {
        // Finds clear and sets the value of the activeCoefficient value
        System.out.println("Setting activation coefficient...");
        programInputFormWithCssSelectorAndKeys("activation_coefficient", scopedFBACard,
                Float.toString(params.getActivationCoefficient()));

        //Finds clears and sets the value of the maxCarbonUptake value
        System.out.println("Setting max carbon uptake...");
        programInputFormWithCssSelectorAndKeys("max_c_uptake", scopedFBACard,
                Float.toString(params.getMaxCarbonUptake()));

        //Finds clears and sets the value of the maxNitrogenUptake value
        System.out.println("Setting max nitrogen uptake...");
        programInputFormWithCssSelectorAndKeys("max_n_uptake", scopedFBACard,
                Float.toString(params.getMaxNitrogenUptake()));

        //Finds clears and sets the value of the maxPhosphateUptake value
        System.out.println("Setting max phosphate uptake...");
        programInputFormWithCssSelectorAndKeys("max_p_uptake", scopedFBACard,
                Float.toString(params.getMaxPhosphateUptake()));

        //Finds clears and sets the value of the maxSulfurUptake value
        System.out.println("Setting max sulfur uptake...");
        programInputFormWithCssSelectorAndKeys("max_s_uptake", scopedFBACard,
                Float.toString(params.getMaxSulfurUptake()));

        //Finds clears and sets the value of the maxOxygenUptake value
        System.out.println("Setting max oxygen uptake...");
        programInputFormWithCssSelectorAndKeys("max_o_uptake", scopedFBACard,
                Float.toString(params.getMaxOxygenUptake()));

        //Finds clears and sets the value of the expressionThreshold value
        System.out.println("Setting expression threshold...");
        programInputFormWithCssSelectorAndKeys("exp_threshold_percentile", scopedFBACard,
                Float.toString(params.getExpressionThreshold()));

        //Finds clears and sets the value of the expressionUncertainty value
        System.out.println("Setting expression uncertainty...");
        programInputFormWithCssSelectorAndKeys("exp_threshold_margin", scopedFBACard,
                Float.toString(params.getExpressionUncertainty()));
    }

    private void programGeneKnockOuts(WebElement scopedFBACard, FBAParameters params) {
        System.out.println("Setting GeneKnockouts");
        WebElement geneKnockouts = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='feature_ko_list']"));
        LinkedList<String> geneKnockOuts = params.getGeneKnockouts();

        int i = 0;
        while (true) { //clear out all text regions
            String indexString = "div[data-index='" + i + "']";
            Boolean isPresent = geneKnockouts
                    .findElements(By.cssSelector(indexString)).size() > 0;
            if (isPresent) {
                WebElement geneKnockoutRow = geneKnockouts
                        .findElement(By.cssSelector(indexString));
                WebElement geneKnockoutsSpanClose = geneKnockoutRow
                        .findElement(By.cssSelector("span[class='fa fa-close']"));
                geneKnockoutsSpanClose.click();
                i++;
            } else {
                break;
            }
        }

        i = 0;
        if (geneKnockOuts != null) {
            for (String geneKO : geneKnockOuts) {
                WebElement geneKnockoutsButton = geneKnockouts
                        .findElement(By.cssSelector("button[class='btn btn-default']"));
                geneKnockoutsButton.click();

                String indexString = "div[data-index='" + i + "']";
                WebElement geneKnockoutRow = geneKnockouts
                        .findElement(By.cssSelector(indexString));

                WebElement geneKnockoutsText = geneKnockoutRow
                        .findElement(By.cssSelector("input[class='form-control']"));
                geneKnockoutsText.clear();
                geneKnockoutsText.sendKeys(geneKO);
                i++;
            }
        }

    }

   private void programReactionToMaximize(WebElement scopedFBACard, FBAParameters params) {
       //Set reaction to maximize
       System.out.println("Setting reaction to Maximize...");
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
       if (params.getReactionToMaximize() != null) {
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
       } else {
           reactionToMaxSearchBox.sendKeys("bio1");
           WebElement availableRMItems = reactionToMaximizeArea
                   .findElement(By.cssSelector("div[data-element='available-items"));
           List<WebElement> foundRMItem = availableRMItems
                   .findElements(By.cssSelector("span[class='kb-btn-icon']"));
           foundRMItem = availableRMItems.findElements(By.cssSelector("span[class='kb-btn-icon']"));
           foundRMItem.get(0).click();

       }

    }

    private void programReactionKnockOuts(WebElement scopedFBACard, FBAParameters params) {
        //setting reaction knockouts
        System.out.println("Setting reaction knockouts");
        //clear out old selections if any
        WebElement reactionKnockoutArea = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='reaction_ko_list']"));
        WebElement selectedItemsRK = reactionKnockoutArea
                .findElement(By.cssSelector("div[data-element='selected-items']"));
        boolean removeElementsFromReactionKnockout = true;
        while (removeElementsFromReactionKnockout) {
            try {
                WebElement alreadySelectedKO = selectedItemsRK
                        .findElement(By.cssSelector("span[class='fa fa-minus-circle']"));
                alreadySelectedKO.click();

            } catch (Exception e) {
                removeElementsFromReactionKnockout = false;

            }
        }
        //put in new elements
        WebElement reactionKOSearchBox = reactionKnockoutArea
                .findElement(By.cssSelector("input[class='form-contol']"));
        WebElement availableKOItems = reactionKnockoutArea
                .findElement(By.cssSelector("div[data-element='available-items']"));
        LinkedList<String> reactionKOS = params.getReactionKnockouts();
        if (reactionKOS != null) {
            for (String reactionKO : reactionKOS
            ) {
                reactionKOSearchBox.clear();
                reactionKOSearchBox.sendKeys(reactionKO);
                List<WebElement> foundKOItems = availableKOItems
                        .findElements(By.cssSelector("span[class='kb-btn-icon']"));
                if (foundKOItems.size() == 0) {
                    System.out.println("Unable to find: " + reactionKO);
                } else {
                    foundKOItems.get(0).click();
                }
            }
        }

    }

    private void programCustomFLuxBounds(WebElement scopedFBACard, FBAParameters params) {
        System.out.println("Setting Custom Flux Bounds");
        WebElement customFluxBoundsArea = scopedFBACard
                .findElement(By.cssSelector("div[data-parameter='custom_bound_list']"));
        LinkedList<String> customBounds = params.getCustomFluxBounds();
        int i = 0;
        while (true) {
            String current = "div[data-index='" + i + "']";
            Boolean alreadyExist = customFluxBoundsArea
                    .findElements(By.cssSelector(current)).size() > 0;
            if (alreadyExist) {
                WebElement customBoundsElement = customFluxBoundsArea
                        .findElement(By.cssSelector(current));
                WebElement delete = customBoundsElement
                        .findElement(By.cssSelector("span[class='fa fa-close']"));
                delete.click();
                i++;
            } else {
                break;
            }
        }
        i = 0;
        if(customBounds != null)
        {
            for (String cBound : customBounds) {
                WebElement button = customFluxBoundsArea
                        .findElement(By.cssSelector("button[class='btn btn-default']"));
                button.click();

                String current = "div[data-index='" + i + "']";
                WebElement currentElement = customFluxBoundsArea
                        .findElement(By.cssSelector(current));
                WebElement customText = currentElement
                        .findElement(By.cssSelector("input[class='form-control']"));
               // customText.clear();
                customText.sendKeys(cBound);
                i++;
            }

        }


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

