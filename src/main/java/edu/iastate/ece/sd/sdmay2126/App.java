package edu.iastate.ece.sd.sdmay2126;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;

/**
 * Basic KBase driver for narrative interaction.
 */
public class App {
    private static final GUIForm GUI = new GUIForm();

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {
        //Give the GUI a more authentic feel according to use OS
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            //Generate our GUI, this has control of the web driver.
            GUI.setVisible(true);
        });
    }

    /**
     * Produces a web driver for automation.
     */
    static WebDriver getDriver() {
        /*
         * When we begin to decrease the hackiness and make this user-friendly,
         * we may want to explore downloading this into the user's home directory.
         *
         * Note that these drivers are device and browser specific. Docs/drivers:
         * https://www.selenium.dev/documentation/en/webdriver/driver_requirements/
         */

        /*
         * ===== CHROME =====
         * Chrome driver: https://chromedriver.storage.googleapis.com/index.html
         *
         * System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
         *
         * return new ChromeDriver(
         *      new ChromeOptions().setHeadless(false)
         * );
         */

        /*
         * ===== FIREFOX =====
         * Firefox driver: https://github.com/mozilla/geckodriver/releases
         */

        System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver");

        return new FirefoxDriver(
                // I wanna watch it
                new FirefoxOptions().setHeadless(false)
        );
    }

    /**
     * Performs a Globus authentication flow, assuming the login page is loaded.
     */
    static void performGlobusAuthFlow(WebDriver driver) throws InterruptedException {
        System.out.println("Initiating Globus authentication...");

        // Locate the login iframe
        WebElement loginFrame = new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("iframe")));

        // Swap the driver context into the frame
        driver.switchTo().frame(loginFrame);

        // Locate and click the Globus auth
        new WebDriverWait(driver, 10)
                .until(d -> d.findElements(By.className("signin-button")))
                .get(2) // Globus is #3
                .click();

        // Switch back to the window (from the iframe)
        driver.switchTo().defaultContent();

        // Locate the Globus "continue" button
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.name("identity_provider")))
                .click();

        // Locate and fill the username and password fields
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.id("ember24")))
                .sendKeys("sdmay2126");
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.id("ember25")))
                .sendKeys("sdmay2126pw");

        // Submit the login form
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.cssSelector("input[type=\"submit\"]")))
                .click();

        // Locate the confirmation iframe
        WebElement confirmFrame = new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("iframe")));

        // Swap the driver context into the frame
        driver.switchTo().frame(confirmFrame);

        // Acknowledge account
        new WebDriverWait(driver, 10)
                .until(d -> d.findElement(By.tagName("button")))
                .click();

        // Swap back to the window
        driver.switchTo().defaultContent();
    }

	/*
	private static int incrementingRunnerIdentifier = 1;
	private static int incrementingJobIdentifier = 1;

	public static void runnerTesting_main(String[] args) throws InterruptedException, JobManagerStoppedException {
		// Start the manager with demo. runners (they'll start initializing)
		JobManager manager = new JobManager((byte) 5,
				onReady -> new DemonstrationRunner(onReady, incrementingRunnerIdentifier++));
		new Thread(manager).start();

		// Wait just long enough for initialization to complete
		Thread.sleep(6000);

		// Start throwing jobs onto the queue
		for (int i = 0; i < 20; i++) {
			FBAParameters params = new FBAParameters(false, false, false);
			params.setActivationCoefficient(incrementingJobIdentifier++);

			System.out.println("Application scheduling job " + incrementingJobIdentifier);
			manager.scheduleJob(new Job(params));

			Thread.sleep(RandomUtil.getRandInRange(500, 1500));
		}

		Thread.sleep(20000);
	}
	*/

}