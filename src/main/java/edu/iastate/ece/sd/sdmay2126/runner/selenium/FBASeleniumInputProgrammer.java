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
    public void programInputs(Job job) {
        FBAParameters params = (FBAParameters) job.getParameters();

        // Reset the FBA Application
        resetFBAIfRequired();

        WebElement codeBox = new WebDriverWait(driver, 10)
                .until(d -> d.findElements(By.cssSelector("div[class^='cell code_cell']")))
                .get(2);
        codeBox.click();


        //There are two buttons that show and hide adnaved options we want to click the second one
        new WebDriverWait(driver, 10)
                .until(d -> codeBox.findElements(By.cssSelector("button[title='show advanced']")))
                .get(1)
                .click();


        //WebElement fluxAnalysisBox = driver.findElements(By.cssSelector("div[class^='cell code_cell")).get(2);
        //fluxAnalysisBox.click();

        //the check boxes are children of divs
        WebElement fvaParent = driver.findElement(By.cssSelector("div[data-parameter='fva']"));
        WebElement fva = fvaParent.findElement(By.cssSelector("input[type='checkbox']"));
        //if the check box doesn't match what is selected the it is clicked
        if (fva.isSelected() != params.isFluxVariabilityAnalysis()) {

            fva.click();
        }

        WebElement minFluxParent = driver.findElement(By.cssSelector("div[data-parameter='minimize_flux']"));
        WebElement minFlux = minFluxParent.findElement(By.cssSelector("input[type='checkbox']"));
        if (minFlux.isSelected() != params.isMinimizeFlux()) {
            System.out.println("clicking flux");

            minFlux.click();
        }

        WebElement simKoParent = driver.findElement(By.cssSelector("div[data-parameter='simulate_ko']"));
        WebElement simKo = simKoParent.findElement(By.cssSelector("input[type='checkbox']"));
        if (simKo.isSelected() != params.isSimulateAllSingleKos()) {

            simKo.click();
        }

        //finds clear and sets the value of the activeCoefficient value
        WebElement activCoeParent = driver.findElement(By.cssSelector("div[data-parameter='activation_coefficient']"));
        WebElement activCoe = activCoeParent.findElement(By.cssSelector("input[class='form-control']"));
        activCoe.clear();
        activCoe.sendKeys(Float.toString(params.getActivationCoefficient()));
    }

    private void resetFBAIfRequired() {
        try {
            clickButtonWithCssSelector("button[class='btn btn-default -rerun']");
            clickButtonWithCssSelector("button[class='btn btn-primary']");
        } catch (TimeoutException e) {
            // No-Op : App does not need reset - no action required.
        }
    }

    private void clickButtonWithCssSelector(String selector) {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)))
                .click();
    }
}
