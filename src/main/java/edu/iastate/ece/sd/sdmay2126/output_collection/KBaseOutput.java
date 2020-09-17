package edu.iastate.ece.sd.sdmay2126.output_collection;

import java.util.Collection;

public class KBaseOutput {
    private Float objectiveValue;
    private Collection<String> jobLogs;

    public KBaseOutput(Float objectiveValue, Collection<String> jobLogs) {
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
}
