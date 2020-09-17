package edu.iastate.ece.sd.sdmay2126.output_collection;

import edu.iastate.ece.sd.sdmay2126.FakeWebElement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static edu.iastate.ece.sd.sdmay2126.output_collection.SeleniumOutputCollector.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;

public class SeleniumOutputCollectorTest {
    private WebDriver mockWebDriver = Mockito.mock(WebDriver.class);
    private SeleniumOutputCollector classToTest;

    @Before
    public void setUp() {
        when(mockWebDriver.findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH))).thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElement(By.xpath(OBJECTIVE_VALUE_VALUE_TAG_NAME))).thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElement(By.xpath(JOB_STATUS_BUTTON_PATH))).thenThrow(new NoSuchElementException("no element found"));
        when(mockWebDriver.findElements(By.xpath(LOG_TEXT_CLASS_NAME))).thenThrow(new NoSuchElementException("no element found"));

        classToTest = new SeleniumOutputCollector(mockWebDriver);
    }

    @Test
    public void collectorWillReturnDefaultOfNegOneObjectiveValueIfLabelNotFound() {
        KBaseOutput result = classToTest.collectOutput();

        assertThat(result.getObjectiveValue(), is(-1f));
    }

    @Test
    public void collectorWillReturnDefaultOfEmptyCollectionIfLogElementsAreNotFound() {
        KBaseOutput result = classToTest.collectOutput();

        assertTrue(result.getJobLogs().isEmpty());
    }

    @Test
    public void collectorWillReturnDefaultOfNegOneObjectiveValueIfLabelCannotBeParsedToFloat() {
        FakeWebElement labelElement = new FakeWebElement();
        doReturn(labelElement).when(mockWebDriver).findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH));
        doReturn(new FakeWebElement("not a float")).when(mockWebDriver).findElement(withTagName(OBJECTIVE_VALUE_VALUE_TAG_NAME).toRightOf(labelElement));

        KBaseOutput result = classToTest.collectOutput();

        assertThat(result.getObjectiveValue(), is(-1f));
    }

    @Test
    public void collectorWillReturnFloatObjectiveValueIfLabelCanBeParsedToFloat() {
        FakeWebElement labelElement = new FakeWebElement();
        doReturn(labelElement).when(mockWebDriver).findElement(By.xpath(OBJECTIVE_VALUE_LABEL_PATH));
        doReturn(new FakeWebElement("2.9")).when(mockWebDriver).findElement(withTagName(OBJECTIVE_VALUE_VALUE_TAG_NAME).toRightOf(labelElement));

        KBaseOutput result = classToTest.collectOutput();

        assertThat(result.getObjectiveValue(), is(2.9f));
    }

    @Test
    public void collectorWillReturnLogsIfElementsFound() {
        List<FakeWebElement> webElementsToReturn = new ArrayList<>();
        webElementsToReturn.add(new FakeWebElement("foo"));
        webElementsToReturn.add(new FakeWebElement("bar"));

        doReturn(new FakeWebElement()).when(mockWebDriver).findElement(By.xpath(JOB_STATUS_BUTTON_PATH));
        doReturn(webElementsToReturn).when(mockWebDriver).findElements(By.className(LOG_TEXT_CLASS_NAME));

        KBaseOutput result = classToTest.collectOutput();

        assertThat(result.getJobLogs().size(), is(2));
        assertTrue(result.getJobLogs().contains("foo"));
        assertTrue(result.getJobLogs().contains("bar"));
    }
}