package edu.iastate.ece.sd.sdmay2126.application;

/**
 * Output from some particular KBase application execution. Will correspond to some {@link ApplicationParameters} for
 * the same {@link ApplicationType}.
 */
public interface ApplicationOutput {
    /**
     * @return The particular KBase {@link ApplicationType} which these parameters should be applied to.
     */
    ApplicationType getApplication();
}
