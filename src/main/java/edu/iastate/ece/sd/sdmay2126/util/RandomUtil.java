package edu.iastate.ece.sd.sdmay2126.util;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();

    public static int getRandInRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
