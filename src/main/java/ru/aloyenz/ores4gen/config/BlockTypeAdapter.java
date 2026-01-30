package ru.aloyenz.ores4gen.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class BlockTypeAdapter extends TypeAdapter<Block> {

    @Override
    public void write(JsonWriter jsonWriter, Block block) throws IOException {
        if (block == null) {
            jsonWriter.nullValue();
            return;
        }
        Identifier id = Registries.BLOCK.getId(block);
        jsonWriter.value(id.toString());
    }

    @Override
    public Block read(JsonReader jsonReader) throws IOException {
        String id = jsonReader.nextString();
        if (id == null || id.isEmpty()) {
            return null;
        }
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) {
            return null;
        }
        return Registries.BLOCK.get(identifier);
    }
}
