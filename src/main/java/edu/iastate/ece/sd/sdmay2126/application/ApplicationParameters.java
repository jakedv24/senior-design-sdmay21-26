package edu.iastate.ece.sd.sdmay2126.application;

/**
 * Parameters for some particular KBase {@link ApplicationType} execution.
 * May be considered, in aggregate, a sufficiently
 * unique identifier to cache application execution results.
 */
public interface ApplicationParameters {
    /**
     * @return The particular KBase {@link ApplicationType} which these parameters should be applied to.
     */
    ApplicationType getApplication();
}
