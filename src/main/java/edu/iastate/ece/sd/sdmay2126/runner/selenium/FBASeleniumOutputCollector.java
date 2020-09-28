package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.FBAOutput;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class FBASeleniumOutputCollector implements SeleniumOutputCollector {
    static final String OBJECTIVE_VALUE_LABEL_PATH = "//td[text()='Objective value']";
    static final String JOB_STATUS_BUTTON_PATH = "//button[@title='Job Status']";
    static final String OBJECTIVE_VALUE_VALUE_TAG_NAME = "td";
    static final String LOG_TEXT_CLASS_NAME = "kblog-text";

    private WebDriver webDriver;

    public FBASeleniumOutputCollector(@Nonnull WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Nonnull
    @Override
    public ApplicationOutput collectOutput(Job job) {
        System.out.println("Collecting output");
        return new FBAOutput(getObjectiveValue(), getLogs());
    }

    @NotNull
    private Collection<String> getLogs() {
        try {
            System.out.println("Clicking job status button");
            new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(d -> d.findElement(By.xpath(JOB_STATUS_BUTTON_PATH)))
                    .click();

            System.out.println("Pulling log text");
            return new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(d -> d.findElements(By.className(LOG_TEXT_CLASS_NAME)))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            System.out.println("Unable to find log elements.");

            return Collections.emptySet();
        }
    }

    private float getObjectiveValue() {
        float objectiveValue = -1f;
        try {
            System.out.println("Locating objective value");
            WebElement objectiveValueLabel = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(d -> d.findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH)));
            String objectiveValueNumeric = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(d -> d.findElement(
                            withTagName(OBJECTIVE_VALUE_VALUE_TAG_NAME).toRightOf(objectiveValueLabel)))
                    .getText();

            objectiveValue = Float.parseFloat(objectiveValueNumeric);
        } catch (NoSuchElementException e) {
            System.out.println("Unable to find objective value element.");
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse result objective value into float.");
        }

        return objectiveValue;
    }
}
