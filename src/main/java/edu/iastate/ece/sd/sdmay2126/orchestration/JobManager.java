package edu.iastate.ece.sd.sdmay2126.orchestration;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.runner.Runner;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerInstantiator;
import edu.iastate.ece.sd.sdmay2126.runner.RunnerNotReadyException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles delegating queued jobs to a finite set of runners and resources.
 */
public class JobManager implements Runnable {
    private static final byte DEFAULT_RUNNER_COUNT = 2;

    /** The maximum concurrent runner execution allowed for this manager. */
    private final byte totalRunnerCount;

    /** All of this manager's runners. */
    private final List<Runner> runners;
    /** Runners which have indicated resource availability to process a job. */
    private final BlockingQueue<Runner> availableRunners;
    /** Jobs which have been prepared and queued for execution. */
    private final BlockingQueue<Job> jobQueue;

    // Graceful stop indicator, even if mid-queue
    private boolean stopped = false;

    /**
     * Creates a job manager with the default runner count.
     *
     * @param runnerInstantiator Provisions runners for the manager.
     */
    public JobManager(RunnerInstantiator runnerInstantiator) {
        this(DEFAULT_RUNNER_COUNT, runnerInstantiator);
    }

    /**
     * Creates a job manager with the specified runner count.
     */
    public JobManager(byte totalRunnerCount, RunnerInstantiator runnerInstantiator) {
        this.totalRunnerCount = totalRunnerCount;
        runners = new LinkedList<>();
        availableRunners = new LinkedBlockingQueue<>();
        jobQueue = new LinkedBlockingQueue<>();

        // Initialize the runners
        for (int i = 0; i < totalRunnerCount; i++) {
            // Note that this does NOT indicate resource availability; that's the runner's responsibility
            Runner newRunner = runnerInstantiator.createRunner(
                    (runner) -> {
                        try {
                            this.indicateAvailability(runner);
                        } catch (JobManagerStoppedException e) {
                            // TODO: Handle better (can we pass this to the runner somehow?)
                            e.printStackTrace();
                        }
                    });
            runners.add(newRunner);

            // Start the runner's initialization process
            new Thread(newRunner).start();
        }
    }

    /**
     * Schedules a prepared job for execution by some runner, once one comes available.
     *
     * @throws JobManagerStoppedException Thrown if the manager was stopped.
     */
    public void scheduleJob(Job job) throws JobManagerStoppedException {
        if (stopped)
            throw new JobManagerStoppedException();

        jobQueue.add(job);
    }

    /**
     * Attempts to unschedule the specified job. Note that this call depends on the caller beating the runners to the job.
     */
    public void unscheduleJob(Job job) {
        jobQueue.remove(job);
    }

    /**
     * Indicates that the specified runner has resource availability to process jobs.
     *
     * @throws JobManagerStoppedException Thrown if the manager was stopped.
     */
    public void indicateAvailability(Runner runner) throws JobManagerStoppedException {
        if (stopped)
            throw new JobManagerStoppedException();

        availableRunners.add(runner);
    }

    /**
     * Attempts to cancel the specified runner's resource availability. Note that this depends on the caller beating
     * some job being queued for processing.
     */
    public void cancelAvailability(Runner runner) {
        availableRunners.remove(runner);
    }

    /**
     * Instruct the runner to stop execution gracefully.
     */
    public void stopManager() {
        stopped = true;
    }

    @Override
    public void run() {
        // First we have the execution loop
        try {
            while (!stopped) {
                // TODO: Can these calls be made reactive s.t. we can respond to a
                //      "stop" without processing a job/runner first?

                // Will block until something is added, if the queue is exhausted
                System.out.println("Job Manager: Job manager waiting for a job");
                Job nextJob = jobQueue.take();

                // Will block until some runner's resources come available
                System.out.println("Job Manager: Job manager waiting for a runner");
                Runner runner = availableRunners.take();

                // Instruct the runner to take this job
                try {
                    System.out.println("Job Manager: Processing job " + ((FBAParameters) nextJob.getParameters()).getActivationCoefficient());
                    // Note that this is asynchronous, and the runner has callback responsibility
                    runner.runJob(nextJob);
                } catch (RunnerNotReadyException e) {
                    // Runner wasn't ready after-all (we should log this); reschedule the job
                    System.out.println("Job Manager: ERROR! Runner wasn't ready");
                    jobQueue.add(nextJob);
                }
            }
        } catch (InterruptedException e) { /* TODO: Something reasonable */ }

        // Stop our runners
        for (Runner runner : runners)
            runner.stopRunner();

        // We're gracefully stopping; provide an opportunity to save-off pending jobs
        if (!jobQueue.isEmpty()) {
            for (Job unexecutedJob : jobQueue) {
                // If the job had a cancellation callback specified, call it
                JobCanceled callback = unexecutedJob.getCancellationCallback();
                if (callback != null) {
                    callback.onCancellation(unexecutedJob);
                }
            }
        }
    }
}
