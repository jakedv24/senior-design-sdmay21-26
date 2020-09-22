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
import edu.iastate.ece.sd.sdmay2126.runner.RunnerReady;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * KBase job runner using Selenium WebDrivers.
 */
public class SeleniumRunner implements Runner {
    private final SeleniumConfiguration configuration;
    private final RunnerReady runnerReady;
    private WebDriver driver;

    /** Synchronizes job delegation from the manager with the runner. */
    private final BlockingQueue<Job> nextJob;

    /** Indicates runner status and availability. */
    private boolean initialized = false, waiting = true, stopped = false;

    public SeleniumRunner(SeleniumConfiguration configuration, RunnerReady runnerReady, JobManager manager) {
        this.configuration = configuration;
        this.runnerReady = runnerReady;

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
        } catch (JobManagerStoppedException e) { /* TODO */ e.printStackTrace(); return; }

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
                runnerReady.runnerReady(this);
            }
        }
        catch (InterruptedException e) { /* TODO */ e.printStackTrace(); }
    }

    /**
     * Provides a Selenium WebDriver to automate through.
     */
    private WebDriver getDriver() {
        String driverLoc = configuration.getWebDriverLocation();
        return null; // TODO
    }

    /**
     * Performs KBase authentication flows and, ultimately, produces a ready-to-run narrative session.
     */
    private void initializeRunner() throws JobManagerStoppedException {
        // Initialize the authentication flow
        // TODO: Can we make this configurable (Globus vs. other OAuth flows)
        SeleniumAuthenticationFlow authenticationFlow
                = new SeleniumGlobusAuthenticationFlow(driver,
                    configuration.getGlobusUsername(), configuration.getGlobusPassword());

        // Perform the authentication flow
        authenticationFlow.authenticateSession();

        // Mark initialization complete and indicate availability to the manager
        initialized = true;
        runnerReady.runnerReady(this);
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
        job.setOutput(new FBASeleniumOutputCollector(driver).collectOutput(job));
    }
}
