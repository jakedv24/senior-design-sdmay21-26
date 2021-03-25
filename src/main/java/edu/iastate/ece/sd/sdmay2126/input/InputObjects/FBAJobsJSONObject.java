package edu.iastate.ece.sd.sdmay2126.input.InputObjects;

import java.util.List;

public class FBAJobsJSONObject {
    private final List<FBAParametersJSONObject> jobs;

    public FBAJobsJSONObject(List<FBAParametersJSONObject> jobs) {
        this.jobs = jobs;
    }

    public List<FBAParametersJSONObject> getJobs() {
        return jobs;
    }
}
