package edu.iastate.ece.sd.sdmay2126.output_collection;

import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class SeleniumOutputCollector implements OutputCollector {
    static final String OBJECTIVE_VALUE_LABEL_PATH = "//td[text()='Objective value']";
    static final String JOB_STATUS_BUTTON_PATH = "//button[@title='Job Status']";
    static final String OBJECTIVE_VALUE_VALUE_TAG_NAME = "td";
    static final String LOG_TEXT_CLASS_NAME = "kblog-text";

    private WebDriver webDriver;

    public SeleniumOutputCollector(@Nonnull WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Nonnull
    @Override
    public KBaseOutput collectOutput() {
        return new KBaseOutput(getObjectiveValue(), getLogs());
    }

    @NotNull
    private Collection<String> getLogs() {
        try {
            webDriver.findElement(By.xpath(JOB_STATUS_BUTTON_PATH)).click();

            return webDriver.findElements(By.className(LOG_TEXT_CLASS_NAME))
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
            WebElement objectiveValueLabel = webDriver.findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH));
            String objectiveValueNumeric = webDriver.findElement(withTagName(OBJECTIVE_VALUE_VALUE_TAG_NAME)
                    .toRightOf(objectiveValueLabel))
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
