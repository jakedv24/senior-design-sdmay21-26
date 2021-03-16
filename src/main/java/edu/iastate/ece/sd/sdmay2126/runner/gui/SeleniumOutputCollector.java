package edu.iastate.ece.sd.sdmay2126.runner.gui;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.InvalidApplicationException;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

public interface SeleniumOutputCollector {
    /**
     * Collects output for the specified job.
     * @throws InvalidApplicationException Thrown if this collector is incorrect for the job's application.
     */
    @Nonnull
    ApplicationOutput collectOutput(Job job, WebElement scopedElement) throws InvalidApplicationException;
}
