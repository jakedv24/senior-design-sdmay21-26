package edu.iastate.ece.sd.sdmay2126.util;

import java.util.Random;

public class RandomUtil {
    private static final Random RANDOM = new Random();

    public static int getRandInRange(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}
