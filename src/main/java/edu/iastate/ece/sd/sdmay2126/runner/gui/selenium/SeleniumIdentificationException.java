package edu.iastate.ece.sd.sdmay2126.runner.gui.selenium;

/**
 * Throw in instances where Selenium automation was unable to identify some element in KBase.
 */
public class SeleniumIdentificationException extends Exception {
    public SeleniumIdentificationException(String message) {
        super(message);
    }

    public SeleniumIdentificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
