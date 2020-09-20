package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;

public class FBASeleniumGlobusAuthenticationFlow implements SeleniumAuthenticationFlow {
    private final WebDriver driver;
    private final String username, password;

    FBASeleniumGlobusAuthenticationFlow(@Nonnull WebDriver driver, String username, String password) {
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

    @Override
    public void authenticateSession() {
        // TODO
    }
}
