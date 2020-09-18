package edu.iastate.ece.sd.sdmay2126.runner;

/**
 * Used to indicate to a manager that some runner is ready to process jobs.
 */
public interface RunnerReady {
    /**
     * Indicates that the specified runner is ready to process a job.
     */
    void runnerReady(Runner runner);
}
