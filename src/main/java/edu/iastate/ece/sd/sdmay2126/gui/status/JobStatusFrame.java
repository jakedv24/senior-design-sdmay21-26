package edu.iastate.ece.sd.sdmay2126.gui.status;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobResult;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JobStatusFrame extends JFrame {
    private final List<Job> jobs; // Index corresponds to JobStatusTable row index

    private final JobStatusPanel statusPanel;
    private final JobSummaryPanel summaryPanel;

    public JobStatusFrame() {
        jobs = new ArrayList<>();

        statusPanel = new JobStatusPanel();
        summaryPanel = new JobSummaryPanel();

        getContentPane().add(statusPanel);
        getContentPane().add(summaryPanel, BorderLayout.SOUTH);

        setTitle("Job Statuses");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    public void updateJobStatus(Job job) {
        int jobTableIndex = jobs.indexOf(job);

        // Add if this is a new job
        if (jobTableIndex < 0) {
            jobTableIndex = jobs.size();
            jobs.add(job);
        }

        // Update the table's status for this job
        switch (job.getResult()) {
            case QUEUED:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Queued");
                break;
            case RUNNING:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Running");
                break;
            case SUCCESS:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Success");
                break;
            case FAILURE:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Failure");
                break;
            case CANCELLED:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Cancelled");
                break;
            default:
                statusPanel.getStatusTable().updateRowStatus(jobTableIndex, "Unknown");
                break;
        }

        // Recompute statistics
        long successfulJobs = jobs.stream().filter(j -> j.getResult() == JobResult.SUCCESS).count();
        long completedJobs = jobs.stream().filter(j -> j.getResult() != JobResult.QUEUED).count();
        if (completedJobs > 0) {
            double successRate = ((double) successfulJobs / completedJobs) * 100;
            summaryPanel.updateSummaryText("Job Total: " + jobs.size() + " (" + String.format("%.2f", successRate) + "% successful)");
        } else {
            summaryPanel.updateSummaryText("Job Total: " + jobs.size());
        }

    }
}
