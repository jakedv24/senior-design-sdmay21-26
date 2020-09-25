package edu.iastate.ece.sd.sdmay2126.runner;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;

/**
 * Responsible for executing applications with parameters specified by jobs.
 *
 * A runner may engage in costly initialization procedures, but they should be handled as part of its thread and NOT
 * during construction. Runners are responsible for accurately communicating resource availability to the job manager,
 */
public interface Runner extends Runnable {
    /**
     * Indicates that the runner should cease execution.
     */
    void stopRunner();

    /**
     * Instructs the runner to begin processing the specified job. Preconditions include the runner having indicated to
     * the manager that resources were available.
     */
    void runJob(Job job) throws RunnerNotReadyException, InterruptedException;
}
