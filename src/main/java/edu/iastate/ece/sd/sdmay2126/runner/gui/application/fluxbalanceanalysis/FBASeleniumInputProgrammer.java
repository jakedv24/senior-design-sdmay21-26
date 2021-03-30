package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumIdentificationException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumInputProgrammer;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumKBaseHelper;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

public class FBASeleniumInputProgrammer implements SeleniumInputProgrammer {
    private WebDriver driver;

    public FBASeleniumInputProgrammer(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void programInputs(Job job, WebElement scopedFBACard) throws SeleniumIdentificationException,
            InterruptedException {
        System.out.println("Programming FBA inputs...");

        FBAParameters params = (FBAParameters) job.getParameters();

        // Reset the FBA Application
       // resetFBAIfRequired(scopedFBACard);
        SeleniumKBaseHelper.resetCardIfNeeded(scopedFBACard,driver);

        // Button will be obscured temporarily if we used the reset dialog, so try for 10 seconds before failing
        //SeleniumUtilities.clickUntilSuccessful(scopedFBACard, Duration.ofSeconds(10));

        // There are two buttons that show and hide advanced options we want to click the second one
        System.out.println("Toggling advanced options...");
        String showAdvancedCSSSelector = "button[title='show advanced']";
        SeleniumUtilities.waitForNMatches(driver, By.cssSelector(showAdvancedCSSSelector),
                2, Duration.ofSeconds(30), scopedFBACard);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> scopedFBACard.findElement(By.cssSelector(showAdvancedCSSSelector)))
                .click();

        // TODO: This used to default to bio1 if no matches
        SeleniumKBaseHelper.setSearchableOptionList(scopedFBACard, "target_reaction",
                Collections.singletonList(params.getReactionToMaximize()));

        SeleniumKBaseHelper.setCheckBox(scopedFBACard, "fva", params.isFluxVariabilityAnalysis());
        SeleniumKBaseHelper.setCheckBox(scopedFBACard, "minimize_flux", params.isMinimizeFlux());
        SeleniumKBaseHelper.setCheckBox(scopedFBACard, "simulate_ko", params.isSimulateAllSingleKos());

        SeleniumKBaseHelper.setSearchableOptionList(scopedFBACard, "feature_ko_list",
                params.getGeneKnockouts());

        SeleniumKBaseHelper.setSearchableOptionList(scopedFBACard, "reaction_ko_list",
                params.getReactionKnockouts());

        SeleniumKBaseHelper.setTextList(driver, scopedFBACard, "custom_bound_list",
                params.getCustomFluxBounds());

        SeleniumKBaseHelper.setSearchableOptionList(scopedFBACard, "media_supplement_list",
                params.getMediaSupplements());

        SeleniumKBaseHelper.setSearchableOptionList(scopedFBACard, "expression_condition",
                Collections.singletonList(params.getExpressionCondition()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "exp_threshold_percentile",
                Float.toString(params.getExpressionThreshold()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "exp_threshold_margin",
                Float.toString(params.getExpressionUncertainty()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "activation_coefficient",
                Float.toString(params.getActivationCoefficient()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "max_c_uptake",
                Float.toString(params.getMaxCarbonUptake()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "max_n_uptake",
                Float.toString(params.getMaxNitrogenUptake()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "max_p_uptake",
                Float.toString(params.getMaxPhosphateUptake()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "max_s_uptake",
                Float.toString(params.getMaxSulfurUptake()));

        SeleniumKBaseHelper.setTextBox(scopedFBACard, "max_o_uptake",
                Float.toString(params.getMaxOxygenUptake()));

        System.out.println("Programming FBA complete.");

    }

    private void resetFBAIfRequired(WebElement scopedFBACard) {
        System.out.println("Checking if FBA needs to be reset...");
        try {
            // Click "Reset" on FBA card
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(d -> scopedFBACard.findElement(By.cssSelector("button[class='btn btn-default -rerun']")))
                    .click();

            // Click confirmation on popup dialog
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("button[class='btn btn-primary']")))
                    .click();

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

