package edu.iastate.ece.sd.sdmay2126.util;

import java.util.Random;

public class RandomUtil {
    private static final Random RANDOM = new Random();

    public static float getRandInRange(float min, float max) {
        return min + RANDOM.nextFloat() * (max-min);
    }

    public static boolean getRandBoolean(int min, int max){
        int x = RANDOM.nextDouble() >= 0.5? 1 : 0;
        if(x == 1){
            return true;
        }
        return  false;
    }
}
