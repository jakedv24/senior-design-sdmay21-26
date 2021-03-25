package edu.iastate.ece.sd.sdmay2126.runner.gui.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.WebDriver;


public interface SeleniumResultsCardDeleter {
    void DeleteResultsCard(Job job) throws InterruptedException, SeleniumIdentificationException;
}
