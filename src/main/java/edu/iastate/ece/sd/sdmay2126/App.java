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
public class App 
{
	public static GUIForm GUI;

    public static void main( String[] args ) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
//		WebDriver driver = getDriver();
        //Give the GUI a more authentic feel according to use OS
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		GUI = new GUIForm();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Generate our GUI
				GUI.setVisible(true);
			}
		});
		//Don't start the narrative until we select the button to start it.
		while(GUI.RunDefault == false){}
		Thread.sleep(10000);
		WebDriver driver = getDriver();
        try {
        	System.out.println("Loading KBase narrative...");
        	driver.get("https://narrative.kbase.us/narrative/71238");

        	// Let things load
        	Thread.sleep(3000);

        	// Login with Globus
        	performGlobusAuthFlow(driver);
        	
        	// This load really takes a while
        	Thread.sleep(15000);
        	
        	// TODO: Interact with the narrative (fill form, click buttons, read output)
        	
        	// Wait so we can observe/mess around
        	System.out.println("Waiting 5 minutes so we can experiment");
        	Thread.sleep(5 * 60 * 1000);
        } finally {
        	System.out.println("Run done - closing driver and quitting.");
        	driver.close();
        }
    }
    
    /**
     * Produces a web driver for automation.
     */
    private static WebDriver getDriver() {
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
         *		new ChromeOptions().setHeadless(false)
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
    private static void performGlobusAuthFlow(WebDriver driver) throws InterruptedException {
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

    public static void startGUI(){

	}
}
