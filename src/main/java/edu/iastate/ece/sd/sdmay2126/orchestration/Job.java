package edu.iastate.ece.sd.sdmay2126.orchestration;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationOutput;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationParameters;
import edu.iastate.ece.sd.sdmay2126.application.ApplicationType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a unique execution of some {@link ApplicationType}.
 */
public class Job {

    private final ApplicationParameters parameters;
    private final JobUpdate updateCallback;
    private ApplicationOutput output;
    private JobResult result = JobResult.UNKNOWN;

    public Job(ApplicationParameters parameters) {
        this(parameters, null);
    }

    public Job(@NotNull ApplicationParameters parameters, JobUpdate updateCallback) {
        this.parameters = parameters;
        this.updateCallback = updateCallback;
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

    public void setOutput(ApplicationOutput output) {
        this.output = output;
        callUpdateCallback();
    }

    public void setResult(JobResult result) {
        this.result = result;
        callUpdateCallback();
    }

    private void callUpdateCallback() {
        if (updateCallback != null) {
            updateCallback.onUpdate(this);
        }
    }
}
