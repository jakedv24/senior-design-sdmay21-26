package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;

/**
 * Executes an already-configured KBase application.
 */
public interface SeleniumApplicationExecutor {
    /**
     * Given some job, should execute its application. Preconditions include parameters having already been applied.
     */
    void executeApplication(Job job) throws SeleniumIdentificationException;
}
