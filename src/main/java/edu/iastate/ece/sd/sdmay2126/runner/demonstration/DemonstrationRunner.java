package edu.iastate.ece.sd.sdmay2126.runner.demonstration;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobResult;
import edu.iastate.ece.sd.sdmay2126.runner.Runner;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotReadyException;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerReady;
import edu.iastate.ece.sd.sdmay2126.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class DemonstrationRunner implements Runner {
    private final RunnerReady runnerReady;
    private final BlockingQueue<Job> nextJob;
    private final Random random;
    private final int runnerIdentifier; // For nicer console output
    private boolean initialized = false, waiting = true, stopped = false;

    public DemonstrationRunner(RunnerReady runnerReady, int runnerIdentifier) {
        this.runnerReady = runnerReady;
        this.runnerIdentifier = runnerIdentifier;
        this.nextJob = new SynchronousQueue<>();
        random = new Random();

        print("Constructed");
    }

    @Override
    public void stopRunner() {
        print("Attempting to stop");
        this.stopped = true;
    }

    @Override
    public void runJob(Job job) throws RunnerNotReadyException, InterruptedException {
        if (!initialized || !waiting)
            throw new RunnerNotReadyException();

        print("Queuing job");
        nextJob.put(job);
    }

    @Override
    public void run() {
        try
        {
            initialize();

            print("Beginning to process jobs");
            while (!stopped) {
                print("Waiting for a job to be delegated");

                // Block until a job is delegated to this runner
                Job nextJob = this.nextJob.take();

                print("Received job from manager");

                // Mark the runner busy
                waiting = false;

                // Execute the job, provided that the session is ready
                processJob(nextJob);

                // Update the job's result status
                // TODO: Set to FAILURE for the recoverable and job-specific failures
                nextJob.setResult(JobResult.SUCCESS);

                // Reopen availability and notify the manager
                waiting = true;
                print("Indicating availability to manager");
                runnerReady.runnerReady(this);
            }
            print("Stopped processing jobs");
        }
        catch (Exception e) {}
    }

    private void initialize() throws InterruptedException {
        print("Beginning initialization");

        // Sleep random duration between 1 and 5 secs
        int millis = RandomUtil.getRandInRange(1000, 5000);
        Thread.sleep(millis);

        initialized = true;
        runnerReady.runnerReady(this);

        print("Initialization complete in " + millis + " millis");
    }

    private void processJob(Job job) throws InterruptedException {
        // Use the AC to identify the job
        FBAParameters params = (FBAParameters) job.getParameters();
        print("Processing job with AC: " + params.getActivationCoefficient());

        // Sleep random duration between 5 and 10 secs
        int millis = RandomUtil.getRandInRange(5000, 10000);
        Thread.sleep(millis);

        print("Done processing job with AC " + params.getActivationCoefficient() + " in " + millis + " millis");
    }

    private void print(String msg) {
        System.out.println(runnerIdentifier + " " + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + ": " + msg);
    }
}
