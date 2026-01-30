package ru.aloyenz.ores4gen.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.aloyenz.ores4gen.config.BlockGenerationChance;
import ru.aloyenz.ores4gen.config.ConfigHolder;
import ru.aloyenz.ores4gen.config.GeneratorConfig;
import ru.aloyenz.ores4gen.core.Generator;
import ru.aloyenz.ores4gen.core.Generators;


public class MainCommand {

    private static final SuggestionProvider<ServerCommandSource> states =
            (context, builder) -> CommandSource.suggestMatching(
                    new String[]{"enabled", "disabled", "get"},
                    builder
            );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("ores4gen")
                        .requires(CommandManager.requirePermissionLevel(2))
                        .executes(MainCommand::getInfo)
                        .then(
                                CommandManager.literal("info")
                                        .executes(MainCommand::getInfo)
                        )
                        .then(
                                CommandManager.literal("reload")
                                        .executes(MainCommand::reload)
                        )
                        .then(
                                CommandManager.literal("state")
                                        .executes(MainCommand::getStates)
                                        .then(
                                                CommandManager.argument(
                                                        "generator", GeneratorArgumentType.generator())
                                                        .suggests(GeneratorArgumentType.SUGGESTIONS)
                                                        .then(
                                                                CommandManager.argument("state",
                                                                        StringArgumentType.greedyString())
                                                                        .suggests(states)
                                                                        .executes(MainCommand::setOrGetState)
                                                        )
                                        )
                        )
                        .then(
                                CommandManager.literal("saveToConfig")
                                        .executes(MainCommand::save)
                        )
        );
    }

    private static int getInfo(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(() -> Text.literal("═══════════════════════════════════").formatted(Formatting.GOLD), false);
        context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.info.title").formatted(Formatting.YELLOW, Formatting.BOLD), false);
        context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.info.subtitle").formatted(Formatting.LIGHT_PURPLE, Formatting.ITALIC), false);
        context.getSource().sendFeedback(() -> Text.literal("═══════════════════════════════════").formatted(Formatting.GOLD), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.reload.start").formatted(Formatting.YELLOW), false);
            Generators.reload();
            context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.reload.success").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.ores4gen.reload.error", e.getMessage()).formatted(Formatting.RED));
            return 0;
        }
    }

    private static int getStates(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(() -> Text.literal("═══════════════════════════════════").formatted(Formatting.AQUA), false);
        context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.state.title").formatted(Formatting.AQUA, Formatting.BOLD), false);
        context.getSource().sendFeedback(() -> Text.literal("═══════════════════════════════════").formatted(Formatting.AQUA), false);

        for (Generator gen : Generator.values()) {
            if (gen == Generator.ALL) {
                boolean globalEnabled = ConfigHolder.getInstance().enabled;
                Formatting color = globalEnabled ? Formatting.GREEN : Formatting.RED;
                String statusKey = globalEnabled ? "command.ores4gen.state.enabled" : "command.ores4gen.state.disabled";
                context.getSource().sendFeedback(() -> Text.literal("  ")
                    .append(Text.translatable("command.ores4gen.state.global").formatted(Formatting.GOLD, Formatting.BOLD))
                    .append(Text.literal(" "))
                    .append(Text.translatable(statusKey).formatted(color, Formatting.BOLD)), false);
            } else {
                boolean enabled = gen.isEnabled();
                Formatting color = enabled ? Formatting.GREEN : Formatting.RED;
                String statusKey = enabled ? "command.ores4gen.state.enabled_lower" : "command.ores4gen.state.disabled_lower";
                String name = gen.getName().substring(0, 1).toUpperCase() + gen.getName().substring(1);
                context.getSource().sendFeedback(() -> Text.literal("")
                    .append(Text.literal("  • ").formatted(Formatting.GRAY))
                    .append(Text.literal(name + ": ").formatted(Formatting.YELLOW))
                    .append(Text.translatable(statusKey).formatted(color)), false);
            }
        }

        context.getSource().sendFeedback(() -> Text.literal("───────────────────────────────────").formatted(Formatting.DARK_GRAY), false);
        return 1;
    }

    private static int setOrGetState(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Generator generator = GeneratorArgumentType.getGenerator(context, "generator");
        String state = StringArgumentType.getString(context, "state").toLowerCase();

        String genName = generator.getName().substring(0, 1).toUpperCase() + generator.getName().substring(1);

        switch (state) {
            case "get" -> {
                context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.generator.title").formatted(Formatting.GOLD)
                        .append(Text.literal(" " + genName).formatted(Formatting.YELLOW, Formatting.BOLD)), false);

                boolean enabled = generator.isEnabled();
                Formatting statusColor = enabled ? Formatting.GREEN : Formatting.RED;
                String statusKey = enabled ? "command.ores4gen.state.enabled" : "command.ores4gen.state.disabled";

                context.getSource().sendFeedback(() -> Text.literal("  ")
                        .append(Text.translatable("command.ores4gen.generator.status").formatted(Formatting.GOLD))
                        .append(Text.literal(" "))
                        .append(Text.translatable(statusKey).formatted(statusColor, Formatting.BOLD)), false);

                GeneratorConfig config = generator.getConfig();
                if (config != null && config.chances != null && !config.chances.isEmpty()) {
                    context.getSource().sendFeedback(() -> Text.literal("  ")
                            .append(Text.translatable("command.ores4gen.generator.chances").formatted(Formatting.AQUA)), false);

                    double totalWeight = 0;
                    for (BlockGenerationChance chance : config.chances) {
                        totalWeight += chance.chance;
                    }

                    final double finalTotalWeight = totalWeight;

                    for (BlockGenerationChance chance : config.chances) {
                        String blockId = Registries.BLOCK.getId(chance.block).getPath().replace("_", " ");
                        String blockName = blockId.substring(0, 1).toUpperCase() + blockId.substring(1);
                        double weight = chance.chance;
                        double percentage = (weight / finalTotalWeight) * 100.0;

                        Formatting chanceColor;
                        if (percentage >= 50) {
                            chanceColor = Formatting.GREEN;
                        } else if (percentage >= 10) {
                            chanceColor = Formatting.YELLOW;
                        } else if (percentage >= 1) {
                            chanceColor = Formatting.GOLD;
                        } else {
                            chanceColor = Formatting.RED;
                        }

                        final String finalBlockName = blockName;
                        final double finalWeight = weight;
                        final double finalPercentage = percentage;
                        final Formatting finalChanceColor = chanceColor;

                        context.getSource().sendFeedback(() -> Text.literal("    • ").formatted(Formatting.DARK_GRAY)
                                .append(Text.literal(finalBlockName + ": ").formatted(Formatting.WHITE))
                                .append(Text.literal(String.format("%.2f", finalWeight)).formatted(Formatting.GRAY))
                                .append(Text.literal(" (").formatted(Formatting.DARK_GRAY))
                                .append(Text.literal(String.format("%.2f%%", finalPercentage)).formatted(finalChanceColor))
                                .append(Text.literal(")").formatted(Formatting.DARK_GRAY)), false);
                    }
                } else if (generator == Generator.ALL) {
                    context.getSource().sendFeedback(() -> Text.literal("  ")
                            .append(Text.translatable("command.ores4gen.generator.global_info").formatted(Formatting.GRAY, Formatting.ITALIC)), false);
                }

                return 1;
            }
            case "enabled" -> {
                generator.enable();
                context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.set.enabled", genName).formatted(Formatting.GREEN), true);
                return 1;
            }
            case "disabled" -> {
                generator.disable();
                context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.set.disabled", genName).formatted(Formatting.RED), true);
                return 1;
            }
            default -> {
                context.getSource().sendError(Text.translatable("command.ores4gen.set.invalid").formatted(Formatting.RED));
                return 0;
            }
        }
    }

    private static int save(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.save.start").formatted(Formatting.YELLOW), false);
            ConfigHolder.saveConfig();
            context.getSource().sendFeedback(() -> Text.translatable("command.ores4gen.save.success").formatted(Formatting.GREEN), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.ores4gen.save.error", e.getMessage()).formatted(Formatting.RED));
            return 0;
        }
    }
}
