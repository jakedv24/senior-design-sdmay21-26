package edu.iastate.ece.sd.sdmay2126.runner.gui.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;



public interface SeleniumResultsCardDeleter {
    void deleteResultsCard(Job job) throws InterruptedException, SeleniumIdentificationException;
}
