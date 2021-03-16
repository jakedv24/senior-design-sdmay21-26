package edu.iastate.ece.sd.sdmay2126.runner.gui.driver;

/**
 * Indicates that a Selenium driver identification string is invalid.
 */
public class InvalidSeleniumDriverException extends Exception {
    public InvalidSeleniumDriverException(String driverName) {
        super("Selenium driver with name \"" + driverName + "\" is invalid.");
    }
}
