package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumDriverChromeRemote implements SeleniumDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        try {
            return new RemoteWebDriver(new URL(driverConfiguration.getDriverLocation()), new ChromeOptions().setHeadless(false));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Error("Could not get remote URL.");
        }
    }
}
