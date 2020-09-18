package edu.iastate.ece.sd.sdmay2126.orchestration;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationType;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationParameters;

/**
 * Represents a unique execution of some {@link ApplicationType}.
 */
public class Job {

    private ApplicationParameters parameters;
    private JobCompleted completionCallback;
    private JobCanceled cancellationCallback;
    private ApplicationOutput output;
    private JobResult result = JobResult.UNKNOWN;

    public Job(ApplicationParameters parameters) {
        this(parameters, null, null);
    }

    public Job(ApplicationParameters parameters, JobCompleted completionCallback, JobCanceled cancellationCallback) {
        if (parameters == null)
            throw new IllegalArgumentException("Parameters are required to create a job.");

        this.parameters = parameters;
        this.completionCallback = completionCallback;
        this.cancellationCallback = cancellationCallback;
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

    public void setOutput(ApplicationOutput output) {
        this.output = output;
    }

    public void setResult(JobResult result) {
        this.result = result;
    }
}
