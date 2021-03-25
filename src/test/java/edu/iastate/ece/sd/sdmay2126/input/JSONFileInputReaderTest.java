package edu.iastate.ece.sd.sdmay2126.input;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JSONFileInputReaderTest {
    private static final String SINGLE_JOB_VALID_FILE_NAME =
            "src/test/java/edu/iastate/ece/sd/sdmay2126/input/test_input_json_valid_file_single.json";
    private static final String MULT_JOB_VALID_FILE_NAME =
            "src/test/java/edu/iastate/ece/sd/sdmay2126/input/test_input_json_valid_file_multiple.json";

    private JSONFileInputReader classToTest = new JSONFileInputReader();

    @Test(expected = FileNotFoundException.class)
    public void willFailWhenFileDoesNotExist() throws FileNotFoundException {
        classToTest.parseFromFile("this file does not exist");
    }

    @Test
    public void willParseSingleFBAParametersJobFromValidConfigurationFile() throws FileNotFoundException {
        List<FBAParameters> fbaParameters = classToTest.parseFromFile(SINGLE_JOB_VALID_FILE_NAME);
        assertThat(fbaParameters.size(), is(1));
        assertThat(fbaParameters.get(0).getActivationCoefficient(), is(0.5f));
    }

    @Test
    public void willParseMultipleFBAParametersJobFromValidConfigurationFile() throws FileNotFoundException {
        List<FBAParameters> fbaParameters = classToTest.parseFromFile(MULT_JOB_VALID_FILE_NAME);
        assertThat(fbaParameters.size(), is(2));
        assertThat(fbaParameters.get(0).getActivationCoefficient(), is(0.5f));
        assertThat(fbaParameters.get(1).getActivationCoefficient(), is(0.6f));
    }
}