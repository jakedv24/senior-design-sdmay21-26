package edu.iastate.ece.sd.sdmay2126.orchestration;

/**
 * Indicates the success or failure of a job's execution.
 */
public enum JobResult {
    UNKNOWN,
    QUEUED,
    RUNNING,
    SUCCESS,
    FAILURE,
    CANCELLED
}
