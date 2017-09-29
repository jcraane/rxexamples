package nl.capaxit.rxexamples.imagescaling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScalingSpecification {
    private static final double DEFAULT_SCALING_MULTIPLIER = 1.0;
    private static final Map<String, Double> SCALING = initScalingSpecs();

    private static Map<String, Double> initScalingSpecs() {
        final HashMap<String, Double> scaling = new HashMap<>();
        scaling.put("ios-1.0", DEFAULT_SCALING_MULTIPLIER);
        scaling.put("mdpi", DEFAULT_SCALING_MULTIPLIER);
        scaling.put("hdpi", 1.5);
        scaling.put("ios-2.0", 2.0);
        scaling.put("xhdpi", 2.0);
        scaling.put("ios-3.0", 3.0);
        scaling.put("xxhdpi", 3.0);
        scaling.put("xxxhdpi", 4.0);
        return scaling;
    }

    public static List<String> getIdentifiers() {
        return new ArrayList<>(SCALING.keySet());
    }

    public static Double getMultiplier(final String scaleType) {
        return initScalingSpecs().getOrDefault(scaleType, DEFAULT_SCALING_MULTIPLIER);
    }
}
