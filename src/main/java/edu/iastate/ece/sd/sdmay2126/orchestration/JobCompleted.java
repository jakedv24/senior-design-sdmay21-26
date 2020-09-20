package edu.iastate.ece.sd.sdmay2126.orchestration;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;

/**
 * Callback for taking action after an application's execution with some {@link edu.iastate.ece.sd.sdmay2126.runner.Runner}.
 */
public interface JobCompleted {
    /**
     * Called by the {@link edu.iastate.ece.sd.sdmay2126.runner.Runner} after application execution.
     * @param output The application's result.
     */
    void onCompletion(ApplicationOutput output);
}
