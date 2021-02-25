package edu.iastate.ece.sd.sdmay2126.orchestration;

/**
 * Callback for taking action after a job has had some update.
 */
public interface JobUpdate {
    /**
     * Called after a job's state is updated (result, output, etc).
     * @param job The job which was updated.
     */
    void onUpdate(Job job);
}
