// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.TypeAdapter;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object>
{
    public static final TypeAdapterFactory FACTORY;
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Type type = typeToken.getType();
                if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class)type).isArray())) {
                    return null;
                }
                final Type arrayComponentType = $Gson$Types.getArrayComponentType(type);
                return (TypeAdapter<T>)new ArrayTypeAdapter(gson, (TypeAdapter<Object>)gson.getAdapter(TypeToken.get(arrayComponentType)), (Class<Object>)$Gson$Types.getRawType(arrayComponentType));
            }
        };
    }
    
    public ArrayTypeAdapter(final Gson gson, final TypeAdapter<E> typeAdapter, final Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(gson, typeAdapter, componentType);
        this.componentType = componentType;
    }
    
    @Override
    public Object read(final JsonReader jsonReader) throws IOException {
        Object o;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            o = null;
        }
        else {
            final ArrayList<E> list = new ArrayList<E>();
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                list.add(this.componentTypeAdapter.read(jsonReader));
            }
            jsonReader.endArray();
            final int size = list.size();
            final Object instance = Array.newInstance(this.componentType, size);
            int n = 0;
            while (true) {
                o = instance;
                if (n >= size) {
                    break;
                }
                Array.set(instance, n, list.get(n));
                ++n;
            }
        }
        return o;
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Object o) throws IOException {
        if (o == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        for (int i = 0; i < Array.getLength(o); ++i) {
            this.componentTypeAdapter.write(jsonWriter, (E)Array.get(o, i));
        }
        jsonWriter.endArray();
    }
}
