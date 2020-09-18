package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.InvalidApplicationException;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;

import javax.annotation.Nonnull;

public interface SeleniumOutputCollector {
    /**
     * Collects output for the specified job.
     * @throws InvalidApplicationException Thrown if this collector is incorrect for the job's application.
     */
    @Nonnull
    ApplicationOutput collectOutput(Job job) throws InvalidApplicationException;
}
