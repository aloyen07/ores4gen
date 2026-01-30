package ru.aloyenz.ores4gen.core;

import java.util.List;
import java.util.Random;

public class WeightedRandomPicker<T> {
    private final List<T> items;
    private final List<Double> chances;
    private final double totalChance;
    private final Random random = new Random();

    public WeightedRandomPicker(List<T> items, List<Double> chances) {
        this.items = items;
        this.chances = chances;
        this.totalChance = chances.stream().mapToDouble(Double::doubleValue).sum();
    }

    public T pick() {
        double r = random.nextDouble() * totalChance;
        double cumulative = 0.0;
        for (int i = 0; i < items.size(); i++) {
            cumulative += chances.get(i);
            if (r < cumulative) {
                return items.get(i);
            }
        }
        return items.getLast(); // fallback
    }
}