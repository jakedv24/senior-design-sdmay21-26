package edu.iastate.ece.sd.sdmay2126.input.file;

/**
 * Exception for incorrectly configured input files.
 */
public class IncorrectFBAFileConfigurationException extends Exception {
    @Override
    public String getMessage() {
        return "File was improperly configured for FBA job. Please see the README for info on the configuration file.";
    }
}
