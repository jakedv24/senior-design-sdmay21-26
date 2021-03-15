# senior-design-sdmay21-26
Senior Design Project.

# Setting up the Application using Docker (Recommended)
The application can be configured to use Docker containers to host the selenium webdriver.

**Pull the docker container that holds the desired browser version.**

``docker pull selenium/standalone-chrome:88.0``

See [SeleniumHQ/docker-selenium](https://github.com/SeleniumHQ/docker-selenium) for available browser images.

**Start the docker container hosting the webdriver**

``docker run -d -p 4444:4444 --shm-size 2g selenium/standalone-chrome:88.0``

**Run the application pointing at the docker container as a remote web driver.**

``java -jar senior-design-sdmay21-26.jar -f "path/to/inputFile" -c "path/to/configFile" -r "http://localhost:4444/wd/hub"``


# Setting up the Application Locally
This application uses [Selenium](https://www.selenium.dev/) to automate using KBase in the browser.

A WebDriver matching the Chrome version on the machine has to be manually installed.

1. Create a package at the root project directory called `drivers`. 
The path will look like `senior-design-sdmay21-26/drivers`.

2. Add the chromedriver from seleniun and name the file `chromedriver` in the `drivers` path. (No file extension)
`senior-design-sdmay21-26/drivers/chromedriver`

3. Run the application

# Running the application
## Using the input GUI
Run the application as normal. Click through the GUI and run jobs.

## Using configuration files
Run the application with a `-f <pathToConfigFile>` flag. This will read from the config file at that path.

# Program Flags
| Flag        | Usage | Description |
| ----------- | ----------- |----------- |
| `-c`      | `-c app.config` Optional      | Use app config information in the file path that contains user credentials for KBase..| 
| `-r`      | `-r "http://localhost:4444/wd/hub"` Optional | Use a remote webdriver at a URL. Can be used for Docker| 
| `-f`      | `-f filePath.json` Optional      | Runs the application with a configuration file instead of using a GUI.| 
| `-t`     | `-t FBA` Optional | Runs the application with the specified KBase type. Only valid currently is `FBA`. Default is also `FBA`| 
