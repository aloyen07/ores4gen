package ru.aloyenz.ores4gen.config;

import ru.aloyenz.ores4gen.exception.ConfigException;

import java.io.File;

public class ConfigHolder {

    private static Config instance;
    private static final File configFile = new File("config/ores4gen.json");

    public static Config getInstance() {
        return instance;
    }

    public static void reloadConfig() throws ConfigException {
        instance = Config.loadOrCreateDefault(configFile);
    }

    public static void saveConfig() throws ConfigException {
        Config.saveConfig(configFile, instance);
    }
}
