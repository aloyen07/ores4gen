package ru.aloyenz.ores4gen.core;

import ru.aloyenz.ores4gen.config.ConfigHolder;
import ru.aloyenz.ores4gen.config.GeneratorConfig;

public enum Generator {

    COBBLESTONE("cobblestone",
            () -> ConfigHolder.getInstance().cobblestoneGenerator.enabled = false,
            () -> ConfigHolder.getInstance().cobblestoneGenerator.enabled = true
    ),
    STONE("stone",
            () -> ConfigHolder.getInstance().stoneGenerator.enabled = false,
            () -> ConfigHolder.getInstance().stoneGenerator.enabled = true
    ),
    OBSIDIAN("obsidian",
            () -> ConfigHolder.getInstance().obsidianGenerator.enabled = false,
            () -> ConfigHolder.getInstance().obsidianGenerator.enabled = true
    ),
    BASALT("basalt",
            () -> ConfigHolder.getInstance().basaltGenerator.enabled = false,
            () -> ConfigHolder.getInstance().basaltGenerator.enabled = true
    ),
    ALL("all",
            () -> ConfigHolder.getInstance().enabled = false,
            () -> ConfigHolder.getInstance().enabled = true
    );

    private final String name;
    private final Runnable disableGenerator;
    private final Runnable enableGenerator;

    Generator(String name, Runnable disableGenerator, Runnable enableGenerator) {
        this.name = name;
        this.disableGenerator = disableGenerator;
        this.enableGenerator = enableGenerator;
    }

    public String getName() {
        return name;
    }

    public static Generator byName(String name) {
        for (Generator generator : values()) {
            if (generator.name.equals(name)) {
                return generator;
            }
        }
        return null;
    }

    public void disable() {
        disableGenerator.run();
    }

    public void enable() {
        enableGenerator.run();
    }

    public boolean isEnabled() {
        return switch (this) {
            case COBBLESTONE -> ConfigHolder.getInstance().enabled && ConfigHolder.getInstance().cobblestoneGenerator.enabled;
            case STONE -> ConfigHolder.getInstance().enabled && ConfigHolder.getInstance().stoneGenerator.enabled;
            case OBSIDIAN -> ConfigHolder.getInstance().enabled && ConfigHolder.getInstance().obsidianGenerator.enabled;
            case BASALT -> ConfigHolder.getInstance().enabled && ConfigHolder.getInstance().basaltGenerator.enabled;
            case ALL -> ConfigHolder.getInstance().enabled;
        };
    }

    public GeneratorConfig getConfig() {
        return switch (this) {
            case COBBLESTONE -> ConfigHolder.getInstance().cobblestoneGenerator;
            case STONE -> ConfigHolder.getInstance().stoneGenerator;
            case OBSIDIAN -> ConfigHolder.getInstance().obsidianGenerator;
            case BASALT -> ConfigHolder.getInstance().basaltGenerator;
            case ALL -> null;
        };
    }

}
