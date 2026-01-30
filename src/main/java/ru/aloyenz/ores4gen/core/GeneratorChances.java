package ru.aloyenz.ores4gen.core;

import net.minecraft.block.Block;
import ru.aloyenz.ores4gen.config.BlockGenerationChance;

import java.util.List;

public class GeneratorChances {

    private final WeightedRandomPicker<Block> weightedRandomPicker;

    public GeneratorChances(Block block) {
        this.weightedRandomPicker = new WeightedRandomPicker<>(
                List.of(block),
                List.of(1D)
        );
    }

    public GeneratorChances(List<BlockGenerationChance> chances) {
        this.weightedRandomPicker = new WeightedRandomPicker<>(
                chances.stream().map(BlockGenerationChance::getBlock).toList(),
                chances.stream().map(BlockGenerationChance::getChance).toList()
        );
    }

    public Block pickBlock() {
        return weightedRandomPicker.pick();
    }
}
