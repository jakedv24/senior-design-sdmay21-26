package edu.iastate.ece.sd.sdmay2126.runner.gui.authentication;

import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumIdentificationException;

/**
 * Perform an authentication flow, such as Globus OAuth, to some session.
 */
public interface SeleniumAuthenticationFlow {
    /**
     * Should perform the authentication flow.
     */
    void authenticateSession() throws SeleniumIdentificationException, InterruptedException;
}
