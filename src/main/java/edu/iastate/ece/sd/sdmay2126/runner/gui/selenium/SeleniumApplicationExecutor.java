package edu.iastate.ece.sd.sdmay2126.runner.gui.selenium;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.WebElement;

/**
 * Executes an already-configured KBase application.
 */
public interface SeleniumApplicationExecutor {
    /**
     * Given some job, should execute its application. Preconditions include parameters having already been applied.
     */
    void executeApplication(Job job, WebElement scopedElement) throws SeleniumIdentificationException,
            InterruptedException;
}
