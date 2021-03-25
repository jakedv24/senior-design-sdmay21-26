package edu.iastate.ece.sd.sdmay2126.input.file;

import com.google.gson.Gson;
import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.input.file.InputObjects.FBAJobsJSONObject;
import edu.iastate.ece.sd.sdmay2126.input.file.InputObjects.FBAParametersJSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

public class JSONFileInputReader extends FileInputReader<FBAParameters> {
    @Override
    public List<FBAParameters> parseFromFile(String pathToFile) throws FileNotFoundException {
        File f = new File(pathToFile);

        if (!f.exists()) {
            throw new FileNotFoundException();
        }

        Reader fileReader = new FileReader(f);
        FBAJobsJSONObject fbaJobsJSON = new Gson().fromJson(fileReader, FBAJobsJSONObject.class);

        // TODO validate inputs for required / correct inputs, as well as set default values for optional params.
        // This logic should be extracted from the GUI to be re-usable by both portions. Ideally, the FBAParameters
        // constructor should throw exceptions for incorrect configurations.

        return fbaJobsJSON.getJobs().stream()
                .map(FBAParametersJSONObject::generateFBAParameters)
                .collect(Collectors.toList());
    }
}
