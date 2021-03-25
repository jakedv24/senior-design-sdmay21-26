package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.FBAOutput;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumIdentificationException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumOutputCollector;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumUtilities;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class FBASeleniumOutputCollector implements SeleniumOutputCollector {
    static final String OBJECTIVE_VALUE_LABEL_PATH = "//b[text()='Objective value']";
    static final String JOB_STATUS_BUTTON_PATH = ".//button[@title='Job Status']";
    static final String FBA_OUTPUT_SECTION_PATH = "//div[contains(text(), 'Output from Run Flux Balance')]";
    static final String OBJECTIVE_VALUE_VALUE_PATH = "./../../*";
    static final String LOG_TEXT_CLASS_NAME = "kblog-text";

    private WebDriver webDriver;

    public FBASeleniumOutputCollector(@Nonnull WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Nonnull
    @Override
    public ApplicationOutput collectOutput(Job job, WebElement scopedFBACard) {
        try {
            // try to sleep to let output load
            // TODO: Can we detect when an element comes available instead of waiting?
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Calculating FBA output...");
        return new FBAOutput(getObjectiveValue(), getLogs(scopedFBACard));
    }

    @NotNull
    private Collection<String> getLogs(WebElement scopedFBACard) {
        try {
            // sleep to let logs load from outpout
            scopedFBACard.findElement(By.xpath(JOB_STATUS_BUTTON_PATH))
                    .click();

            // TODO: Can we detect when logs are loaded instead of waiting?
            Thread.sleep(2000);

            return scopedFBACard.findElements(By.className(LOG_TEXT_CLASS_NAME))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException | InterruptedException e) {
            System.out.println("Unable to find log elements.");

            return Collections.emptySet();
        }
    }

    private float getObjectiveValue() {
        System.out.println("Collecting FBA Objective Value...");

        float objectiveValue = -1f;
        try {
            System.out.println("Finding FBA Output Section...");

            SeleniumUtilities.clickUntilSuccessful(webDriver.findElements(By.xpath(FBA_OUTPUT_SECTION_PATH)).get(0));

            System.out.println("Output Section Collected, Finding Objective Value Label...");

            WebElement objectiveValueLabel = SeleniumUtilities.waitForNMatches(webDriver,
                    By.xpath(OBJECTIVE_VALUE_LABEL_PATH), 1, Duration.ofSeconds(30), null)
                    .get(0);

            System.out.println("Objective Value Label found, Finding Numeric Value to Right of");

            String objectiveValueNumeric = objectiveValueLabel.findElements(By.xpath(OBJECTIVE_VALUE_VALUE_PATH))
                    .get(1)
                    .getText();

            objectiveValue = Float.parseFloat(objectiveValueNumeric);
            System.out.println("Objective value collected for FBA job...");
        } catch (NoSuchElementException e) {
            System.out.println("Unable to find objective value element.");
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse result objective value into float.");
        } catch (InterruptedException | SeleniumIdentificationException e) {
            System.out.println("Unable to find objective value label.");
        }

        return objectiveValue;
    }
}
