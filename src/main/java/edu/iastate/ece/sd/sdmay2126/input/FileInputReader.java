package edu.iastate.ece.sd.sdmay2126.input;

import java.io.FileNotFoundException;

/**
 * Interface for reading input from a file.
 */
public interface FileInputReader<T> {
    public T parseFromFile(String pathToFile) throws FileNotFoundException;
}
