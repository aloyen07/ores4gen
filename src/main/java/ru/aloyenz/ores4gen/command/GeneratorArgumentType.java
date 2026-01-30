package ru.aloyenz.ores4gen.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.aloyenz.ores4gen.core.Generator;

import java.util.Arrays;

public class GeneratorArgumentType {
    private static final DynamicCommandExceptionType INVALID_GENERATOR_EXCEPTION = new DynamicCommandExceptionType(
            generator -> Text.stringifiedTranslatable("argument.generator.invalid", generator)
    );

    public static final SuggestionProvider<ServerCommandSource> SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(
                    Arrays.stream(Generator.values()).map(Generator::getName),
                    builder
            );

    public static StringArgumentType generator() {
        return StringArgumentType.word();
    }

    public static Generator getGenerator(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        String generatorName = StringArgumentType.getString(context, name);
        Generator generator = Generator.byName(generatorName);
        if (generator == null) {
            throw INVALID_GENERATOR_EXCEPTION.create(generatorName);
        }
        return generator;
    }
}

