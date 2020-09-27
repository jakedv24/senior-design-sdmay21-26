package edu.iastate.ece.sd.sdmay2126.runner;

import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;

/**
 * Provides instantiation capabilities for job runners. Note that this provides an opportunity for configuration.
 */
public interface RunnerInstantiator {
    /**
     * Creates a new runner to be used by some manager.
     */
     Runner createRunner(JobManager jobManager);
}
