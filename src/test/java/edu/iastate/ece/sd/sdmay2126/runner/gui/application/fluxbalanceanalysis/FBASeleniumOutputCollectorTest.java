package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.FakeWebElement;
import edu.iastate.ece.sd.sdmay2126.application.FBAOutput;
import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis.FBASeleniumOutputCollector.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class FBASeleniumOutputCollectorTest {
    private WebDriver mockWebDriver = Mockito.mock(WebDriver.class);
    private WebElement mockFBACard = Mockito.mock(WebElement.class);
    private FBASeleniumOutputCollector classToTest;

    @Before
    public void setUp() {
        when(mockWebDriver.findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH)))
                .thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElement(By.xpath(OBJECTIVE_VALUE_VALUE_PATH)))
                .thenThrow(new NoSuchElementException("no element found"));
        when(mockFBACard.findElement(By.xpath(JOB_STATUS_BUTTON_PATH)))
                .thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElements(By.xpath(LOG_TEXT_CLASS_NAME)))
                .thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElements(By.xpath(FBA_OUTPUT_SECTION_PATH)))
                .thenThrow(new NoSuchElementException("no element found"));

        classToTest = new FBASeleniumOutputCollector(mockWebDriver);
    }

    @Test
    public void collectorWillReturnDefaultOfNegOneObjectiveValueIfLabelNotFound() {
        Job job = new Job(new FBAParameters(false));
       FBAOutput result = (FBAOutput) classToTest.collectOutput(job, mockFBACard);

        assertThat(result.getObjectiveValue(), is(-1f));
    }

    @Test
    public void collectorWillReturnDefaultOfEmptyCollectionIfLogElementsAreNotFound() {
        Job job = new Job(new FBAParameters(false));
        FBAOutput result = (FBAOutput) classToTest.collectOutput(job, mockFBACard);

        assertTrue(result.getJobLogs().isEmpty());
    }

    @Test
    public void collectorWillReturnDefaultOfNegOneObjectiveValueIfLabelCannotBeParsedToFloat() {
        FakeWebElement labelElement = new FakeWebElement();
        doReturn(labelElement).when(mockWebDriver).findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH));
        doReturn(new FakeWebElement("not a float"))
                .when(mockWebDriver).findElement(withTagName(OBJECTIVE_VALUE_VALUE_PATH).toRightOf(labelElement));

        Job job = new Job(new FBAParameters(false));
       FBAOutput result = (FBAOutput) classToTest.collectOutput(job, mockFBACard);

        assertThat(result.getObjectiveValue(), is(-1f));
    }

    @Test
    public void collectorWillReturnLogsIfElementsFound() {
        List<FakeWebElement> webElementsToReturn = new ArrayList<>();
        webElementsToReturn.add(new FakeWebElement("foo"));
        webElementsToReturn.add(new FakeWebElement("bar"));

        doReturn(new FakeWebElement("a")).when(mockFBACard).findElement(By.xpath(JOB_STATUS_BUTTON_PATH));
        doReturn(webElementsToReturn).when(mockFBACard).findElements(By.className(LOG_TEXT_CLASS_NAME));

        Job job = new Job(new FBAParameters(false));
        FBAOutput result = (FBAOutput) classToTest.collectOutput(job, mockFBACard);

        assertThat(result.getJobLogs().size(), is(2));
        assertTrue(result.getJobLogs().contains("foo"));
        assertTrue(result.getJobLogs().contains("bar"));
    }
}