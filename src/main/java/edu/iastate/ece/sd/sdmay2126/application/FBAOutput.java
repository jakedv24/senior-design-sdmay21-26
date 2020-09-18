package edu.iastate.ece.sd.sdmay2126.application;

import java.util.Collection;

/**
 * Simulation output data from a Flux Balanace Analysis.
 */
public class FBAOutput implements ApplicationOutput {
    private Float objectiveValue;
    private Collection<String> jobLogs;

    public FBAOutput(Float objectiveValue, Collection<String> jobLogs) {
        this.objectiveValue = objectiveValue;
        this.jobLogs = jobLogs;
    }

    public Float getObjectiveValue() {
        return objectiveValue;
    }

    public void setObjectiveValue(Float objectiveValue) {
        this.objectiveValue = objectiveValue;
    }

    public Collection<String> getJobLogs() {
        return jobLogs;
    }

    public void setJobLogs(Collection<String> jobLogs) {
        this.jobLogs = jobLogs;
    }

    @Override
    public Application getApplication() {
        return Application.FBA;
    }
}
