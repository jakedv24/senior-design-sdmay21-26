package edu.iastate.ece.sd.sdmay2126.input;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Interface for reading input from a file.
 */
public abstract class FileInputReader<T> {
    public abstract List<T> parseFromFile(String pathToFile) throws FileNotFoundException;
}
