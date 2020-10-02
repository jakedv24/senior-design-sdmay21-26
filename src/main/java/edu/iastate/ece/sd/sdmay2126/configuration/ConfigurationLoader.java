package edu.iastate.ece.sd.sdmay2126.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load application and environmental configuration.
 */
public class ConfigurationLoader {
    private final Properties applicationProperties;
    private final Properties environmentProperties;

    public ConfigurationLoader() {
        applicationProperties = new Properties();
        environmentProperties = new Properties();

        // Set reasonable defaults for the env.config, since it won't be checked-in
        environmentProperties.setProperty("selenium.driver.type", "CHROME");
        environmentProperties.setProperty("selenium.driver.path", "./drivers/chromedriver");
    }

    public Properties getApplicationProperties() {
        return applicationProperties;
    }

    public Properties getEnvironmentProperties() {
        return environmentProperties;
    }

    /**
     * Attempts to load the app.config and env.config files. Note that env.config is optional.
     */
    public void loadConfiguration() throws IOException {
        // Load the required app.config
        loadConfiguration("app.config", applicationProperties);

        // Attempt to load the optional env.config
        try {
            loadConfiguration("env.config", environmentProperties);
        } catch (FileNotFoundException e) {
            System.out.println("Application could not find env.config; continuing with defaults");
        }
    }

    /**
     * Loads the specified configuration file into the provided properties.
     */
    private void loadConfiguration(String file, Properties properties) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.err.println("Application configuration \"" + file + "\" was not found");
            throw e;
        } catch (IOException e) {
            System.err.println("Application failed to read \"" + file + "\"");
            throw e;
        }
    }
}
