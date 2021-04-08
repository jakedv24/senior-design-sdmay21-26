# senior-design-sdmay21-26

This application uses [Selenium](https://www.selenium.dev/) web drivers to automate using KBase in the browser.

FBA Application jobs can be configured through a GUI, or through the command line via a configuration file. The configuration files can be used to queue multiple jobs from a single file. For an example of a configuration file, keep reading.

# Setting up the Application using Docker (Recommended)

The application can be configured to use Docker containers to host the selenium webdriver. This provides the benefit of not needing to keep your downloaded WebDriver up to date with your machine's browser version, as this may auto-update periodically. It also allows the application to be run in an automated fashion, on any browser version desired.

Currently, a dockerized web driver can only be launched from the command-line, not from the GUI. See [program flags](#program-flags) for syntax.

**Pull the docker container that holds the desired browser and version.**

`docker pull selenium/standalone-chrome:88.0`

See [SeleniumHQ/docker-selenium](https://github.com/SeleniumHQ/docker-selenium) for available browser images.

**Start the docker container hosting the webdriver**

`docker run -d -p 4444:4444 --shm-size 2g selenium/standalone-chrome:88.0`

**Run the application pointing at the docker container as a remote web driver.**

`java -jar senior-design-sdmay21-26.jar -f "path/to/inputFile" -c "path/to/configFile" -r "http://localhost:4444/wd/hub"`

# Setting up the Application Locally

This application uses [Selenium](https://www.selenium.dev/) to automate using KBase in the browser.

A Selenium WebDriver matching the Chrome version on the machine has to be manually installed.

1. Create a package at the root project directory called `drivers`.
   The path will look like `senior-design-sdmay21-26/drivers`.

2. Add the chromedriver from seleniun and name the file `chromedriver` in the `drivers` path. (No file extension)
   `senior-design-sdmay21-26/drivers/chromedriver`

3. Run the application

# Running the application

## Using the input GUI

Run the application as normal. Click through the GUI and run jobs. The GUI is able to accept configuration files in a graphical format.

## Using configuration files

Run the application with a `-f <pathToConfigFile>` flag. This will read from the config file at that path.

### Configuration file documentation

Configuration files are in JSON format as follows.

Jobs is an array of all jobs to queue up to be ran.

```
{
  "jobs": [
    {
      "fluxVariabilityAnalysis": BOOLEAN,
      "minimizeFlux": BOOLEAN,
      "simulateAllSingleKos": BOOLEAN,
      "activationCoefficient": DOUBLE,
      "maxCarbonUptake": DOUBLE,
      "maxNitrogenUptake": DOUBLE,
      "maxPhosphateUptake": DOUBLE,
      "maxSulfurUptake": DOUBLE,
      "maxOxygenUptake": DOUBLE,
      "reactionToMaximize": STRING,
      "expressionThreshold": DOUBLE,
      "expressionUncertainty": DOUBLE,
      "geneKnockouts": [STRING ARRAY],
      "mediaSupplements": [STRING ARRAY],
      "customFluxBounds": [STRING ARRAY],
      "reactionKnockouts": [STRING ARRAY],
      "deleteCard": BOOLEAN,
      "expressionCondition": STRING
    }
  ]
}
```

For more examples, see [this test file](/src/test/java/edu/iastate/ece/sd/sdmay2126/input/test_input_json_valid_file_single.json) to see an example of all parameters available
to the configuration file.

# Program Flags

| Flag | Usage                                        | Description                                                                                              |
| ---- | -------------------------------------------- | -------------------------------------------------------------------------------------------------------- |
| `-c` | `-c app.config` Optional                     | Use app config information in the file path that contains user credentials for KBase..                   |
| `-r` | `-r "http://localhost:4444/wd/hub"` Optional | Use a remote webdriver at a URL. Can be used for Docker                                                  |
| `-f` | `-f filePath.json` Optional                  | Runs the application with a configuration file instead of using a GUI.                                   |
| `-t` | `-t FBA` Optional                            | Runs the application with the specified KBase type. Only valid currently is `FBA`. Default is also `FBA` |
