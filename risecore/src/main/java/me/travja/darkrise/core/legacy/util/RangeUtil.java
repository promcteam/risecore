package me.travja.darkrise.core.legacy.util;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.DoubleRange;

import java.util.Random;

public class RangeUtil {

    private static Random rand = new Random();

    public static double getRandomDouble(DoubleRange range) {
        double min = range.getMinimumDouble(), max = range.getMaximumDouble();
        if (Double.compare(min, max) == 0) {
            return max;
        }
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return (rand.nextDouble() * (max - min)) + min;
    }

    public static boolean getChance(double chance) {
        return (chance > 0) && ((chance >= 100) || (chance >= getRandomDouble(new DoubleRange(0d, 100d))));
    }

}
