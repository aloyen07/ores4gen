package ru.aloyenz.ores4gen.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import ru.aloyenz.ores4gen.exception.ConfigException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY)
            .registerTypeAdapter(Block.class, new BlockTypeAdapter())
            .create();

    public static Config loadOrCreateDefault(File file) throws ConfigException {
        if (!file.exists()) {
            saveDefaultConfig(file);
        }

        return loadConfig(file);
    }

    public static Config loadConfig(File file) throws ConfigException {
        try (var reader = new java.io.FileReader(file)) {
            Config cfg = GSON.fromJson(reader, Config.class);
            validate(cfg);
            return cfg;
        } catch (IOException e) {
            throw new ConfigException("Failed to read config file", e);
        }
    }

    private static void saveDefaultConfig(File file) throws ConfigException {
        Config cfg = new Config();
        cfg.cobblestoneGenerator = new GeneratorConfig(true, Blocks.COBBLESTONE, List.of(
                new BlockGenerationChance(Blocks.COBBLESTONE, 98),
                new BlockGenerationChance(Blocks.COAL_ORE, 1),
                new BlockGenerationChance(Blocks.IRON_ORE, 0.5),
                new BlockGenerationChance(Blocks.GOLD_ORE, 0.3),
                new BlockGenerationChance(Blocks.DIAMOND_ORE, 0.1),
                new BlockGenerationChance(Blocks.EMERALD_ORE, 0.1)
        ));
        cfg.stoneGenerator = new GeneratorConfig(true, Blocks.STONE, List.of(
                new BlockGenerationChance(Blocks.STONE, 90),
                new BlockGenerationChance(Blocks.COAL_ORE, 2),
                new BlockGenerationChance(Blocks.IRON_ORE, 1.5),
                new BlockGenerationChance(Blocks.GOLD_ORE, 1.3),
                new BlockGenerationChance(Blocks.LAPIS_ORE, 1.2),
                new BlockGenerationChance(Blocks.REDSTONE_ORE, 2),
                new BlockGenerationChance(Blocks.DIAMOND_ORE, 1),
                new BlockGenerationChance(Blocks.EMERALD_ORE, 1)
        ));
        cfg.obsidianGenerator = new GeneratorConfig(true, Blocks.OBSIDIAN, List.of(
                new BlockGenerationChance(Blocks.OBSIDIAN, 99.8),
                new BlockGenerationChance(Blocks.CRYING_OBSIDIAN, 0.2)
        ));
        cfg.basaltGenerator = new GeneratorConfig(true, Blocks.BASALT, List.of(
                new BlockGenerationChance(Blocks.BASALT, 96.5),
                new BlockGenerationChance(Blocks.BLACKSTONE, 2),
                new BlockGenerationChance(Blocks.GILDED_BLACKSTONE, 1),
                new BlockGenerationChance(Blocks.ANCIENT_DEBRIS, 0.5)
        ));

        saveConfig(file, cfg);
    }

    public static void saveConfig(File file, Config config) throws ConfigException {
        validate(config);
        try (var writer = new java.io.FileWriter(file)) {
            GSON.toJson(config, writer);
            writer.flush();
        } catch (Exception e) {
            throw new ConfigException("Failed to save config file", e);
        }
    }

    public static void validate(Config config) throws ConfigException {
        config.cobblestoneGenerator.validate();
        config.stoneGenerator.validate();
        config.basaltGenerator.validate();
        config.obsidianGenerator.validate();
    }

    public boolean enabled = true;
    @SerializedName("cobblestone_generator")
    public GeneratorConfig cobblestoneGenerator;
    @SerializedName("stone_generator")
    public GeneratorConfig stoneGenerator;
    @SerializedName("basalt_generator")
    public GeneratorConfig basaltGenerator;
    @SerializedName("obsidian_generator")
    public GeneratorConfig obsidianGenerator;

}
