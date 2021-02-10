package edu.iastate.ece.sd.sdmay2126.input;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JSONFileInputReaderTest {
    private static final String VALID_FILE_NAME =
            "src/test/java/edu/iastate/ece/sd/sdmay2126/input/test_input_json_valid_file.json";

    private JSONFileInputReader classToTest = new JSONFileInputReader();

    @Test(expected = FileNotFoundException.class)
    public void willFailWhenFileDoesNotExist() throws FileNotFoundException {
        classToTest.parseFromFile("this file does not exist");
    }

    @Test
    public void willParseFBAParametersFromValidConfigurationFile() throws FileNotFoundException {
        FBAParameters fbaParameters = classToTest.parseFromFile(VALID_FILE_NAME);
        assertThat(fbaParameters.getActivationCoefficient(), is(0.5f));
    }
}