package edu.iastate.ece.sd.sdmay2126.orchestration;

/**
 * Callback for taking action after an application's execution was canceled for some reason.
 * Action may include storing the unexecuted job and parameters for later execution.
 */
public interface JobCanceled {
    /**
     * Called by the {@link edu.iastate.ece.sd.sdmay2126.orchestration.JobManager}
     * if a queued job's execution is canceled.
     * @param job The canceled job.
     */
    void onCancellation(Job job);
}
