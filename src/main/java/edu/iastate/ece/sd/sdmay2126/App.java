package edu.iastate.ece.sd.sdmay2126;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.configuration.ConfigurationLoader;
import edu.iastate.ece.sd.sdmay2126.input.FileInputReader;
import edu.iastate.ece.sd.sdmay2126.input.JSONFileInputReader;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManagerStoppedException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.SeleniumRunner;
import edu.iastate.ece.sd.sdmay2126.runner.gui.driver.InvalidSeleniumDriverException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.driver.SeleniumDriverUtilities;
import edu.iastate.ece.sd.sdmay2126.runner.gui.driver.SeleniumDrivers;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumConfiguration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Basic KBase driver for narrative interaction.
 */
public class App {
    private static GUIForm gui;

    /**
     * @param args [-t <jobType = FBA (default) | FUTURE_TYPE>, -f <path to config file for job inputs>]
     */
    public static void main(String[] args) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        parseProgramArgsAndRunCorrectMode(args);
    }

    private static void parseProgramArgsAndRunCorrectMode(String[] args)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException {

        boolean fbaType = true;
        boolean fromConfigFile = false;
        boolean remoteWebDriver = false;
        String inputFileName = "";
        String configFileName = "app.config";
        String remoteWebDriverURL = "";

        try {
            CommandLine commandLine = configureAndParseOptions(args);
            if (commandLine.hasOption("f")) {
                fromConfigFile = true;
                inputFileName = commandLine.getOptionValue("f");

                if (!commandLine.hasOption("c")) {
                    throw new Error("Configuration file is required when reading from an input file");
                }
            }

            if (commandLine.hasOption("c")) {
                configFileName = commandLine.getOptionValue("c");
            }

            if (commandLine.hasOption("r")) {
                remoteWebDriver = true;
                remoteWebDriverURL = commandLine.getOptionValue("r");
            }

            if (commandLine.hasOption("t") && !commandLine.getOptionValue("t").equals("FBA")) {
                fbaType = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Load configuration files
        Properties appProps;
        Properties envProps;
        try {
            ConfigurationLoader configuration;

            if (fromConfigFile && remoteWebDriver) {
                System.out.println("From remote web driver: " + remoteWebDriverURL);
                configuration = new ConfigurationLoader(configFileName, "chrome_remote", remoteWebDriverURL);
            } else {
                configuration = new ConfigurationLoader();
            }

            configuration.loadConfiguration();

            appProps = configuration.getApplicationProperties();
            envProps = configuration.getEnvironmentProperties();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        initializeConfigurationsAndStart(fbaType, fromConfigFile, inputFileName, appProps, envProps);
    }

    private static void initializeConfigurationsAndStart(boolean fbaType,
                                                         boolean fromConfigFile,
                                                         String inputFileName,
                                                         Properties appProps,
                                                         Properties envProps)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException {

        // Pull specific configuration values from the files
        String globusUser;
        String globusPass;
        String driverPath;
        SeleniumDrivers driverType;
        String narrativeIdentifier;
        boolean seleniumHeadless;
        try {
            globusUser = appProps.getProperty("kbase.auth.globus.user");
            globusPass = appProps.getProperty("kbase.auth.globus.pass");
            driverType = SeleniumDriverUtilities.getDriverFromString(envProps.getProperty("selenium.driver.type"));
            driverPath = envProps.getProperty("selenium.driver.path");
            narrativeIdentifier = appProps.getProperty("kbase.narrative_identifier");
            seleniumHeadless = Boolean.parseBoolean(appProps.getProperty("selenium.headless"));
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
                        new SeleniumConfiguration(globusUser, globusPass, driverType,
                                driverPath, narrativeIdentifier, seleniumHeadless)
                ),
                1 // Let's leave it at a single runner for now
        );

        // Input config file
        if (fromConfigFile && fbaType) {
            runHeadlessMode(manager, inputFileName);
        } else {
            runGUIMode(manager);
        }
    }

    private static CommandLine configureAndParseOptions(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("c", true, "path to app config file");
        options.addOption("f", true, "path to input file");
        options.addOption("t", true, "kbase test type (ex. FBA)");
        options.addOption("r", true, "path to a remote web driver url - for docker use");

        return new DefaultParser().parse(options, args);
    }

    private static void runGUIMode(JobManager manager) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        //Give the GUI a more authentic feel according to use OS
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        gui = new GUIForm(manager);
        SwingUtilities.invokeLater(() -> {
            //Generate our GUI, this has control of the web driver.
            gui.setVisible(true);
        });
    }

    private static void runHeadlessMode(JobManager manager, String inputFileName) {
        FileInputReader<FBAParameters> fbaFileInputReader = new JSONFileInputReader();
        try {
            for (FBAParameters jobParams :  fbaFileInputReader.parseFromFile(inputFileName)) {
                manager.scheduleJob(new Job(jobParams));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Config file not found.");
            System.exit(-1);
        } catch (JobManagerStoppedException e) {
            e.printStackTrace();
        }
    }
}