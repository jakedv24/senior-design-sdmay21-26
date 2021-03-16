package edu.iastate.ece.sd.sdmay2126.runner.gui;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import org.openqa.selenium.WebElement;

/**
 * Given some job, configures the relevant KBase application.
 */
public interface SeleniumInputProgrammer {
    /**
     * Given some job, should program the relevant application according to its parameters.
     */
    void programInputs(Job job, WebElement scopedAppCard) throws SeleniumIdentificationException, InterruptedException;
}
