package edu.iastate.ece.sd.sdmay2126.orchestration;

/**
 * Callback for taking action after some job has failed execution.
 */
public interface JobFailed {
    /**
     * Called in instances where a job's execution fails.
     * @param job The job which failed.
     * @param cause Optionally, a cause for the failure.
     */
    void onFailure(Job job, Throwable cause);
}
