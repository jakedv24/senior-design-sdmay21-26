package edu.iastate.ece.sd.sdmay2126.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;

import javax.annotation.Nonnull;

public class JSONJobOutputWriter extends JobOutputWriter {

    @Nonnull
    @Override
    String getOutputStringForJob(@Nonnull Job job) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.toJson(job);
    }
}
