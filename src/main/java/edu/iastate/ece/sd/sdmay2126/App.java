package edu.iastate.ece.sd.sdmay2126;

import edu.iastate.ece.sd.sdmay2126.configuration.ConfigurationLoader;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumRunner;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.InvalidSeleniumDriverException;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverUtilities;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDrivers;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Basic KBase driver for narrative interaction.
 */
public class App {
    private static GUIForm gui;

    public static void main(String[] args) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Load configuration files
        Properties appProps;
        Properties envProps;
        try {
            ConfigurationLoader configuration = new ConfigurationLoader();
            configuration.loadConfiguration();

            appProps = configuration.getApplicationProperties();
            envProps = configuration.getEnvironmentProperties();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Pull specific configuration values from the files
        String globusUser;
        String globusPass;
        String driverPath;
        SeleniumDrivers driverType;
        int narrativeIdentifier;
        try {
            globusUser = appProps.getProperty("kbase.auth.globus.user");
            globusPass = appProps.getProperty("kbase.auth.globus.pass");
            driverType = SeleniumDriverUtilities.getDriverFromString(envProps.getProperty("selenium.driver.type"));
            driverPath = envProps.getProperty("selenium.driver.path");
            narrativeIdentifier = Integer.parseInt(appProps.getProperty("kbase.narrative_identifier"));
        } catch (InvalidSeleniumDriverException e) {
            System.err.println("Invalid Selenium driver specified in configuration.");
            e.printStackTrace();
            return;
        }

        // Initialize the job manager
        JobManager manager = new JobManager();
        new Thread(manager).start();

        // Initialize and add a Selenium runner to the manager
        manager.initializeRunners(
                jobManager -> new SeleniumRunner(
                        manager,
                        new SeleniumConfiguration(globusUser, globusPass, driverType, driverPath, narrativeIdentifier)
                ),
                1 // Let's leave it at a single runner for now
        );

        //Give the GUI a more authentic feel according to use OS
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        gui = new GUIForm(manager);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Generate our GUI, this has control of the web driver.
                gui.setVisible(true);
            }
        });
    }
}