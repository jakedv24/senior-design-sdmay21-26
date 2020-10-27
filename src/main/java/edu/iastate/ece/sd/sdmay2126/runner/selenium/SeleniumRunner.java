package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationType;
import edu.iastate.ece.sd.sdmay2126.application.InvalidApplicationException;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManagerStoppedException;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobResult;
import edu.iastate.ece.sd.sdmay2126.output.JSONJobOutputWriter;
import edu.iastate.ece.sd.sdmay2126.output.JobOutputWriter;
import edu.iastate.ece.sd.sdmay2126.runner.Runner;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotInitializedException;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotReadyException;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationFlow;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus.GlobusAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus.GlobusAuthenticationFlow;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverChrome;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverFirefox;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import static edu.iastate.ece.sd.sdmay2126.application.ApplicationType.FBA;

/**
 * KBase job runner using Selenium WebDrivers.
 */
public class SeleniumRunner implements Runner {
    private final JobManager jobManager;
    private final SeleniumConfiguration configuration;
    private WebDriver driver;

    /**
     * Synchronizes job delegation from the manager with the runner.
     */
    private final BlockingQueue<Job> nextJob;

    /**
     * Indicates runner status and availability.
     */
    private boolean initialized = false;
    private boolean waiting = true;
    private boolean stopped = false;

    public SeleniumRunner(JobManager jobManager, SeleniumConfiguration configuration) {
        this.jobManager = jobManager;
        this.configuration = configuration;

        nextJob = new SynchronousQueue<>();
    }

    @Override
    public void stopRunner() {
        this.stopped = true;
    }

    @Override
    public void runJob(Job job) throws RunnerNotReadyException, InterruptedException {
        if (!initialized || !waiting) {
            throw new RunnerNotReadyException();
        }

        nextJob.put(job);
    }

    @Override
    public void run() {
        // Perform runner initialization asynchronously from the construction
        try {
            initializeRunner();
        } catch (JobManagerStoppedException | InterruptedException | SeleniumIdentificationException e) {
            // TODO: Better handling
            e.printStackTrace();
            return;
        }

        // Begin processing jobs
        try {
            while (!stopped) {
                // Block until a job is delegated to this runner
                Job nextJob = this.nextJob.take();

                // Mark the runner busy
                waiting = false;

                try {
                    // Execute the job, provided that the session is ready
                    executeRunner(nextJob);

                    // If we made it here, things should've been a success
                    nextJob.setResult(JobResult.SUCCESS);
                } catch (RunnerNotInitializedException | InvalidApplicationException
                        | SeleniumIdentificationException e) {
                    // TODO: Either scope logging to the runner, or delegate this to the job's
                    //  callback with a faillback to the manager
                    System.err.println("A job has failed, but the runner will reset and continue execution.");

                    // Flag the job as failed
                    nextJob.setResult(JobResult.FAILURE);

                    // Notify the manager of the failure
                    jobManager.notifyOfFailure(nextJob, e);
                }

                // Reopen availability and notify the manager
                waiting = true;
                jobManager.indicateAvailability(this);
            }
        } catch (InterruptedException | JobManagerStoppedException e) {
            System.err.println("A runner has unrecoverably failed. It will need to be reinitialized with the manager.");
            e.printStackTrace();
        }
    }

    /**
     * Performs KBase authentication flows and, ultimately, produces a ready-to-run narrative session.
     */
    private void initializeRunner() throws JobManagerStoppedException, InterruptedException,
            SeleniumIdentificationException {
        // Initialize the web driver
        System.out.println("Initializing driver...");
        driver = getDriver();

        // Perform the initial narrative load
        System.out.println("Loading KBase narrative...");
        driver.get("https://narrative.kbase.us/narrative/" + configuration.getNarrativeIdentifier());

        // Initialize and perform the authentication flow
        System.out.println("Beginning authentication flow...");
        getAuthFlow().authenticateSession();

        // Wait for the narrative to show the loading blocker
        System.out.println("Locating the load blocker...");
        WebElement loadingBlocker = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(By.id("kb-loading-blocker")));

        // Wait while the Jupyter backend initializes/provisions resources for the blocker to disappear
        System.out.println("Blocker located; waiting for it to disappear");
        SeleniumUtilities.waitForVisibilityChange(loadingBlocker, false, Duration.ofSeconds(30));

        // Mark initialization complete and indicate availability to the manager
        System.out.println("Narrative loaded; indicating availability to the manager");
        initialized = true;
        jobManager.indicateAvailability(this);
    }

    /**
     * Provides a Selenium WebDriver to automate through.
     */
    private WebDriver getDriver() {
        /*
         * TODO:
         * When we begin to decrease the hackiness and make this user-friendly,
         * we may want to explore downloading this into the user's home directory.
         *
         * Note that these drivers are device and browser specific. Docs/drivers:
         * https://www.selenium.dev/documentation/en/webdriver/driver_requirements/
         */

        SeleniumDriverConfiguration driverConfiguration = configuration.getDriverConfiguration();
        switch (driverConfiguration.getDriverType()) {
            case CHROME:
                return new SeleniumDriverChrome().initializeDriver(driverConfiguration);
            case FIREFOX:
                return new SeleniumDriverFirefox().initializeDriver(driverConfiguration);
            default:
                throw new IllegalArgumentException("Invalid web-driver type.");
        }
    }

    /**
     * Provides an authentication flow to automate.
     */
    private SeleniumAuthenticationFlow getAuthFlow() {
        switch (configuration.getAuthenticationConfiguration().getFlowType()) {
            case GLOBUS:
                return new GlobusAuthenticationFlow(driver,
                        (GlobusAuthenticationConfiguration) configuration.getAuthenticationConfiguration());
            default:
                throw new IllegalArgumentException("Invalid authentication flow.");
        }
    }

    /**
     * Given some job, executes the runner with an initialized session.
     */
    private void executeRunner(Job job) throws RunnerNotInitializedException, InvalidApplicationException,
            SeleniumIdentificationException, InterruptedException {
        System.out.println("Executing job...");

        if (!initialized) {
            throw new RunnerNotInitializedException();
        }

        switch (job.getApplication()) {
            case FBA:
                executeFBARunner(job);
                break;
            default:
                throw new InvalidApplicationException();
        }
    }

    /**
     * Executes an FBA application using the provided job.
     */
    private void executeFBARunner(Job job) throws
            InvalidApplicationException, SeleniumIdentificationException, InterruptedException {
        System.out.println("Executing FBA job...");

        if (job.getApplication() != ApplicationType.FBA) {
            throw new InvalidApplicationException();
        }

        System.out.println("Looking for FBA code cell to scope searches...");
        WebElement fbaCardScope = getFBACardScope();

        // First program the application
        new FBASeleniumInputProgrammer(driver).programInputs(job, fbaCardScope);

        // Next execute the application
//        new FBASeleniumApplicationExecutor(driver).executeApplication(job, fbaCardScope);
//
//        // Lastly collect results
//        job.setOutput(new FBASeleniumOutputCollector(driver).collectOutput(job, fbaCardScope));
//
//        System.out.println("Selenium Runner: Job complete!");
//
//        JobOutputWriter jobOutputWriter = new JSONJobOutputWriter();
//        jobOutputWriter.outputToFile(job, "FBAJson");
    }

    private WebElement getFBACardScope() {
        System.out.println("Looking for code cell...");
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("div[class^='cell code_cell']")))
                .get(2);
    }
}
