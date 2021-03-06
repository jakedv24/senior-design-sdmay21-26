package edu.iastate.ece.sd.sdmay2126.orchestration;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationParameters;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a unique execution of some {@link ApplicationType}.
 */
public class Job {

    private ApplicationParameters parameters;
    private JobCompleted completionCallback;
    private JobCanceled cancellationCallback;
    private JobFailed failureCallback;
    private ApplicationOutput output;
    private JobResult result = JobResult.UNKNOWN;

    public Job(ApplicationParameters parameters) {
        this(parameters, null, null, null);
    }

    public Job(@NotNull ApplicationParameters parameters, JobCompleted completionCallback,
               JobCanceled cancellationCallback, JobFailed failureCallback) {
        this.parameters = parameters;
        this.completionCallback = completionCallback;
        this.cancellationCallback = cancellationCallback;
        this.failureCallback = failureCallback;
    }

    public ApplicationType getApplication() {
        return parameters.getApplication();
    }

    public ApplicationParameters getParameters() {
        return parameters;
    }

    public ApplicationOutput getOutput() {
        return output;
    }

    public JobResult getResult() {
        return result;
    }

    public JobCompleted getCompletionCallback() {
        return completionCallback;
    }

    public JobCanceled getCancellationCallback() {
        return cancellationCallback;
    }

    public JobFailed getFailureCallback() {
        return failureCallback;
    }

    public void setOutput(ApplicationOutput output) {
        this.output = output;
    }

    public void setResult(JobResult result) {
        this.result = result;
    }
}
