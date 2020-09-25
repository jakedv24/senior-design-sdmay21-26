package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.application.ApplicationType;
import edu.iastate.ece.sd.sdmay2126.application.InvalidApplicationException;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManagerStoppedException;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobResult;
import edu.iastate.ece.sd.sdmay2126.runner.Runner;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotInitializedException;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotReadyException;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationFlow;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus.GlobusAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus.GlobusAuthenticationFlow;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverChrome;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverFirefox;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * KBase job runner using Selenium WebDrivers.
 */
public class SeleniumRunner implements Runner {
    private final JobManager jobManager;
    private final SeleniumConfiguration configuration;
    private WebDriver driver;

    /** Synchronizes job delegation from the manager with the runner. */
    private final BlockingQueue<Job> nextJob;

    /** Indicates runner status and availability. */
    private boolean initialized = false, waiting = true, stopped = false;

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
        if (!initialized || !waiting)
            throw new RunnerNotReadyException();

        nextJob.put(job);
    }

    @Override
    public void run() {
        // Perform runner initialization asynchronously from the construction
        try {
            initializeRunner();
        } catch (JobManagerStoppedException | InterruptedException e) {
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

                try
                {
                    // Execute the job, provided that the session is ready
                    executeRunner(nextJob);

                    // Update the job's result status
                    // TODO: Set to FAILURE for the recoverable and job-specific failures
                    nextJob.setResult(JobResult.SUCCESS);
                }
                catch (RunnerNotInitializedException e) { /* TODO */ e.printStackTrace(); }
                catch (InvalidApplicationException e) { /* TODO */ e.printStackTrace(); }

                // Reopen availability and notify the manager
                waiting = true;
                jobManager.indicateAvailability(this);
            }
        }
        catch (InterruptedException | JobManagerStoppedException e) { /* TODO */ e.printStackTrace(); }
    }

    /**
     * Performs KBase authentication flows and, ultimately, produces a ready-to-run narrative session.
     */
    private void initializeRunner() throws JobManagerStoppedException, InterruptedException {
        // Initialize the web driver
        driver = getDriver();

        // Perform the initial narrative load
        System.out.println("Loading KBase narrative...");
        driver.get("https://narrative.kbase.us/narrative/" + configuration.getNarrativeIdentifier());

        // Let things load
        // TODO: Detect the loaded page reactively
        Thread.sleep(3000);

        // Initialize and perform the authentication flow
        getAuthFlow().authenticateSession();

        // Wait a bit while, post-auth, the Jupyter backend initializes/provisions resources
        // TODO: Detect the load completion reactively
        Thread.sleep(15000);

        // Mark initialization complete and indicate availability to the manager
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
    private void executeRunner(Job job) throws RunnerNotInitializedException, InvalidApplicationException {
        if (!initialized)
            throw new RunnerNotInitializedException();

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
    private void executeFBARunner(Job job) throws InvalidApplicationException {
        if (job.getApplication() != ApplicationType.FBA)
            throw new InvalidApplicationException();

        // First program the application
        new FBASeleniumInputProgrammer(driver).programInputs(job);

        // Next execute the application
        new FBASeleniumApplicationExecutor(driver).executeApplication(job);

        // Lastly collect results
        // TODO: Uncomment once the executor is working (else errors on unfound output elements)
        // job.setOutput(new FBASeleniumOutputCollector(driver).collectOutput(job));
    }
}
