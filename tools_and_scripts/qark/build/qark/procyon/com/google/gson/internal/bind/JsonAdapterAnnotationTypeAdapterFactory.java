// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.TypeAdapterFactory;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    
    public JsonAdapterAnnotationTypeAdapterFactory(final ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final JsonAdapter jsonAdapter = typeToken.getRawType().getAnnotation(JsonAdapter.class);
        if (jsonAdapter == null) {
            return null;
        }
        return (TypeAdapter<T>)this.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }
    
    TypeAdapter<?> getTypeAdapter(final ConstructorConstructor constructorConstructor, final Gson gson, final TypeToken<?> typeToken, final JsonAdapter jsonAdapter) {
        final TypeAdapter<?> construct = constructorConstructor.get((TypeToken<TypeAdapter<?>>)TypeToken.get(jsonAdapter.value())).construct();
        TypeAdapter<?> create;
        if (construct instanceof TypeAdapter) {
            create = construct;
        }
        else if (construct instanceof TypeAdapterFactory) {
            create = ((TypeAdapterFactory)construct).create(gson, typeToken);
        }
        else {
            if (!(construct instanceof JsonSerializer) && !(construct instanceof JsonDeserializer)) {
                throw new IllegalArgumentException("Invalid attempt to bind an instance of " + construct.getClass().getName() + " as a @JsonAdapter for " + typeToken.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
            }
            JsonSerializer<?> jsonSerializer;
            if (construct instanceof JsonSerializer) {
                jsonSerializer = (JsonSerializer<?>)construct;
            }
            else {
                jsonSerializer = null;
            }
            JsonDeserializer<?> jsonDeserializer;
            if (construct instanceof JsonDeserializer) {
                jsonDeserializer = (JsonDeserializer<?>)construct;
            }
            else {
                jsonDeserializer = null;
            }
            create = new TreeTypeAdapter<Object>((JsonSerializer<Object>)jsonSerializer, (JsonDeserializer<Object>)jsonDeserializer, gson, (TypeToken<Object>)typeToken, null);
        }
        TypeAdapter<?> nullSafe = create;
        if (create != null) {
            nullSafe = create;
            if (jsonAdapter.nullSafe()) {
                nullSafe = create.nullSafe();
            }
        }
        return nullSafe;
    }
}
