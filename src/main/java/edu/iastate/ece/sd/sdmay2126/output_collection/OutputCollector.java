package edu.iastate.ece.sd.sdmay2126.output_collection;

import javax.annotation.Nonnull;

public interface OutputCollector {
    @Nonnull
    KBaseOutput collectOutput();
}
