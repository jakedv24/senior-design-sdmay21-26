package edu.iastate.ece.sd.sdmay2126.output;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JobOutputWriter {

    public boolean outputToFile(@Nonnull Job job, @Nonnull String outputFileName) {
        return outputToFile(job, outputFileName, true);
    }

    public boolean outputToFile(@Nonnull Job job, @Nonnull String outputFileName, boolean shouldOverwrite) {
        String outputDirectoryName = "./output/";
        File jobOutputFile = new File(outputDirectoryName + outputFileName + ".txt");

        if (shouldOverwrite) {
            //noinspection ResultOfMethodCallIgnored
            jobOutputFile.delete();
        }

        File outputDirectory = new File(outputDirectoryName);

        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        try {
            FileWriter fileWriter = new FileWriter(jobOutputFile, false);
            fileWriter.write(getOutputStringForJob(job));
            fileWriter.close();

            return true;
        } catch (IOException e) {
           System.out.println("Unable to open file for job output");
            return false;
        }
    }

    @Nonnull
    abstract String getOutputStringForJob(@Nonnull Job job);
}
