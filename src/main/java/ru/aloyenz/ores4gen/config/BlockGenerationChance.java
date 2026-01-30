package ru.aloyenz.ores4gen.config;

import net.minecraft.block.Block;

public class BlockGenerationChance {

    public Block block;
    public double chance;

    public BlockGenerationChance(Block block, double chance) {
        this.block = block;
        this.chance = chance;
    }

    public Block getBlock() {
        return block;
    }

    public double getChance() {
        return chance;
    }
}
