// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T>
{
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;
    
    TypeAdapterRuntimeTypeWrapper(final Gson context, final TypeAdapter<T> delegate, final Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }
    
    private Type getRuntimeTypeIfMoreSpecific(final Type type, final Object o) {
        Type class1 = type;
        if (o != null) {
            if (type != Object.class && !(type instanceof TypeVariable)) {
                class1 = type;
                if (!(type instanceof Class)) {
                    return class1;
                }
            }
            class1 = o.getClass();
        }
        return class1;
    }
    
    @Override
    public T read(final JsonReader jsonReader) throws IOException {
        return this.delegate.read(jsonReader);
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final T t) throws IOException {
        TypeAdapter<?> typeAdapter = this.delegate;
        final Type runtimeTypeIfMoreSpecific = this.getRuntimeTypeIfMoreSpecific(this.type, t);
        if (runtimeTypeIfMoreSpecific != this.type) {
            typeAdapter = this.context.getAdapter(TypeToken.get(runtimeTypeIfMoreSpecific));
            if (typeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter) {
                if (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                    typeAdapter = this.delegate;
                }
            }
        }
        typeAdapter.write(jsonWriter, t);
    }
}
