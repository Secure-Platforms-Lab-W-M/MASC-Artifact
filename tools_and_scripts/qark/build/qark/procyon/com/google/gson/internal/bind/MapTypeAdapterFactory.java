// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind;

import java.util.Iterator;
import com.google.gson.internal.Streams;
import java.util.ArrayList;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.TypeAdapterFactory;

public final class MapTypeAdapterFactory implements TypeAdapterFactory
{
    final boolean complexMapKeySerialization;
    private final ConstructorConstructor constructorConstructor;
    
    public MapTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }
    
    private TypeAdapter<?> getKeyAdapter(final Gson gson, final Type type) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            return TypeAdapters.BOOLEAN_AS_STRING;
        }
        return gson.getAdapter(TypeToken.get(type));
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        if (!Map.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        final Type[] mapKeyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, $Gson$Types.getRawType(type));
        return (TypeAdapter<T>)new Adapter(gson, mapKeyAndValueTypes[0], (TypeAdapter<Object>)this.getKeyAdapter(gson, mapKeyAndValueTypes[0]), mapKeyAndValueTypes[1], (TypeAdapter<Object>)gson.getAdapter(TypeToken.get(mapKeyAndValueTypes[1])), (ObjectConstructor<? extends Map<Object, Object>>)this.constructorConstructor.get(typeToken));
    }
    
    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>>
    {
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        
        public Adapter(final Gson gson, final Type type, final TypeAdapter<K> typeAdapter, final Type type2, final TypeAdapter<V> typeAdapter2, final ObjectConstructor<? extends Map<K, V>> constructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(gson, typeAdapter, type);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(gson, typeAdapter2, type2);
            this.constructor = constructor;
        }
        
        private String keyToString(final JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                final JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (asJsonPrimitive.isNumber()) {
                    return String.valueOf(asJsonPrimitive.getAsNumber());
                }
                if (asJsonPrimitive.isBoolean()) {
                    return Boolean.toString(asJsonPrimitive.getAsBoolean());
                }
                if (asJsonPrimitive.isString()) {
                    return asJsonPrimitive.getAsString();
                }
                throw new AssertionError();
            }
            else {
                if (jsonElement.isJsonNull()) {
                    return "null";
                }
                throw new AssertionError();
            }
        }
        
        @Override
        public Map<K, V> read(final JsonReader jsonReader) throws IOException {
            final JsonToken peek = jsonReader.peek();
            if (peek == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            final Map map = (Map)this.constructor.construct();
            if (peek == JsonToken.BEGIN_ARRAY) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginArray();
                    final K read = this.keyTypeAdapter.read(jsonReader);
                    if (map.put(read, this.valueTypeAdapter.read(jsonReader)) != null) {
                        throw new JsonSyntaxException("duplicate key: " + read);
                    }
                    jsonReader.endArray();
                }
                jsonReader.endArray();
                return (Map<K, V>)map;
            }
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                JsonReaderInternalAccess.INSTANCE.promoteNameToValue(jsonReader);
                final K read2 = this.keyTypeAdapter.read(jsonReader);
                if (map.put(read2, this.valueTypeAdapter.read(jsonReader)) != null) {
                    throw new JsonSyntaxException("duplicate key: " + read2);
                }
            }
            jsonReader.endObject();
            return (Map<K, V>)map;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final Map<K, V> map) throws IOException {
            if (map == null) {
                jsonWriter.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                jsonWriter.beginObject();
                for (final Map.Entry<K, V> entry : map.entrySet()) {
                    jsonWriter.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
                return;
            }
            int n = false ? 1 : 0;
            final ArrayList<JsonElement> list = new ArrayList<JsonElement>(map.size());
            final ArrayList<V> list2 = new ArrayList<V>(map.size());
            for (final Map.Entry<K, V> entry2 : map.entrySet()) {
                final JsonElement jsonTree = this.keyTypeAdapter.toJsonTree(entry2.getKey());
                list.add(jsonTree);
                list2.add(entry2.getValue());
                boolean b;
                if (jsonTree.isJsonArray() || jsonTree.isJsonObject()) {
                    b = true;
                }
                else {
                    b = false;
                }
                n |= (b ? 1 : 0);
            }
            if (n != 0) {
                jsonWriter.beginArray();
                for (int i = 0; i < list.size(); ++i) {
                    jsonWriter.beginArray();
                    Streams.write((JsonElement)list.get(i), jsonWriter);
                    this.valueTypeAdapter.write(jsonWriter, (V)list2.get(i));
                    jsonWriter.endArray();
                }
                jsonWriter.endArray();
                return;
            }
            jsonWriter.beginObject();
            for (int j = 0; j < list.size(); ++j) {
                jsonWriter.name(this.keyToString((JsonElement)list.get(j)));
                this.valueTypeAdapter.write(jsonWriter, (V)list2.get(j));
            }
            jsonWriter.endObject();
        }
    }
}
