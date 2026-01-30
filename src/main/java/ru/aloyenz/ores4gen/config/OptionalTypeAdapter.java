package ru.aloyenz.ores4gen.config;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {

    private final TypeAdapter<T> adapter;

    public OptionalTypeAdapter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void write(JsonWriter out, Optional<T> value) throws IOException {
        if (value.isEmpty()) {
            out.nullValue();
        } else {
            adapter.write(out, value.get());
        }
    }

    @Override
    public Optional<T> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return Optional.empty();
        }
        return Optional.ofNullable(adapter.read(in));
    }

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("unchecked")
        public <t> TypeAdapter<t> create(Gson gson, TypeToken<t> typeToken) {
            Type type = typeToken.getType();
            if (typeToken.getRawType() != Optional.class) {
                return null;
            }

            Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
            TypeAdapter<?> elementAdapter = gson.getAdapter(TypeToken.get(elementType));
            return (TypeAdapter<t>) new OptionalTypeAdapter<>(elementAdapter);
        }
    };
}
