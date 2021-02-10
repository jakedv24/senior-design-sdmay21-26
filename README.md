# senior-design-sdmay21-26
Senior Design Project.

# Setting up the Application
This application uses [Selenium](https://www.selenium.dev/) to automate using KBase in the browser.

A WebDriver matching the Chrome version on the machine has to be manually installed.
(Looking to auto-detect and do this as an enhancement in the future.)

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
| `-f`      | `-f filePath.json` Optional      | Runs the application with a configuration file instead of using a GUI.| 
| `-t`     | `-t FBA` Optional | Runs the application with the specified KBase type. Only valid currently is `FBA`. Default is also `FBA`| 
