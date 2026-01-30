package ru.aloyenz.ores4gen;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aloyenz.ores4gen.command.MainCommand;
import ru.aloyenz.ores4gen.core.Generators;

public class Ores4gen implements ModInitializer {

    private static final Logger log = LogManager.getLogger(Ores4gen.class);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher,
                                                    commandRegistryAccess,
                                                    registrationEnvironment) -> MainCommand.register(commandDispatcher));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                Generators.reload();
            } catch (Exception e) {
                log.error("Failed to reload generators.", e);
                Generators.withDefaults();
            }
        });
    }
}
