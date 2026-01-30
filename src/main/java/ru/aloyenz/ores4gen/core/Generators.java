package ru.aloyenz.ores4gen.core;

import net.minecraft.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aloyenz.ores4gen.config.Config;
import ru.aloyenz.ores4gen.config.ConfigHolder;
import ru.aloyenz.ores4gen.config.GeneratorConfig;
import ru.aloyenz.ores4gen.exception.ConfigException;

public class Generators {

    private static final Logger LOGGER = LogManager.getLogger("Ores4gen");

    private static GeneratorChances COBBLESTONE_GENERATOR;
    private static GeneratorChances STONE_GENERATOR;
    private static GeneratorChances OBSIDIAN_GENERATOR;
    private static GeneratorChances BASALT_GENERATOR;

    public static void reload() throws ConfigException {
        LOGGER.info("Reloading generators...");
        ConfigHolder.reloadConfig();

        Config config = ConfigHolder.getInstance();
        boolean enabled = config.enabled;
        COBBLESTONE_GENERATOR = getFromConfig(enabled, config.cobblestoneGenerator);
        STONE_GENERATOR = getFromConfig(enabled, config.stoneGenerator);
        OBSIDIAN_GENERATOR = getFromConfig(enabled, config.obsidianGenerator);
        BASALT_GENERATOR = getFromConfig(enabled, config.basaltGenerator);
    }

    public static void withDefaults() {
        COBBLESTONE_GENERATOR = new GeneratorChances(Blocks.COBBLESTONE);
        STONE_GENERATOR = new GeneratorChances(Blocks.STONE);
        OBSIDIAN_GENERATOR = new GeneratorChances(Blocks.OBSIDIAN);
        BASALT_GENERATOR = new GeneratorChances(Blocks.BASALT);
    }

    private static GeneratorChances getFromConfig(boolean enabledGlobal, GeneratorConfig config) {
        if (!enabledGlobal || !config.enabled) {
            return new GeneratorChances(config.defaultBlock);
        }

        return new GeneratorChances(config.chances);
    }

    public static GeneratorChances getCobblestoneGenerator() {
        return COBBLESTONE_GENERATOR;
    }

    public static GeneratorChances getStoneGenerator() {
        return STONE_GENERATOR;
    }

    public static GeneratorChances getObsidianGenerator() {
        return OBSIDIAN_GENERATOR;
    }

    public static GeneratorChances getBasaltGenerator() {
        return BASALT_GENERATOR;
    }
}
