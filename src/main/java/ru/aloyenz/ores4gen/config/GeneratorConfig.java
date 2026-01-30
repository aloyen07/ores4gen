package ru.aloyenz.ores4gen.config;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import ru.aloyenz.ores4gen.exception.ConfigException;

import java.util.List;

public class GeneratorConfig {

    public boolean enabled;
    @SerializedName("default_block")
    public Block defaultBlock;
    public List<BlockGenerationChance> chances;

    public GeneratorConfig(boolean enabled, Block defaultBlock, List<BlockGenerationChance> chances) {
        this.enabled = enabled;
        this.defaultBlock = defaultBlock;
        this.chances = chances;
    }

    public void validate() throws ConfigException {
        if (defaultBlock == null) {
            throw new ConfigException("Default block is null");
        }

        if (chances == null || chances.isEmpty()) {
            throw new ConfigException("Chances list is null or empty");
        }

        for (BlockGenerationChance chance : chances) {
            if (chance.block == null) {
                throw new ConfigException("Block in chances list is null");
            }
            if (chance.chance < 0) {
                throw new ConfigException("Chance for block " + chance.block + " is negative");
            }
        }
    }
}
