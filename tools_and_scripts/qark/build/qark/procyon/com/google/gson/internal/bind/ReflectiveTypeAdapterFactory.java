// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind;

import java.util.Iterator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.ObjectConstructor;
import java.util.ArrayList;
import java.util.Collections;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import com.google.gson.internal.$Gson$Types;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import java.lang.reflect.Type;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.TypeAdapterFactory;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder, final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
    }
    
    private BoundField createBoundField(final Gson gson, final Field field, final String s, final TypeToken<?> typeToken, final boolean b, final boolean b2) {
        final boolean primitive = Primitives.isPrimitive(typeToken.getRawType());
        final JsonAdapter jsonAdapter = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = null;
        if (jsonAdapter != null) {
            typeAdapter = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
        }
        final boolean b3 = typeAdapter != null;
        TypeAdapter<?> adapter;
        if ((adapter = typeAdapter) == null) {
            adapter = gson.getAdapter(typeToken);
        }
        return (BoundField)new BoundField(s, b, b2) {
            @Override
            void read(final JsonReader jsonReader, final Object o) throws IOException, IllegalAccessException {
                final Object read = adapter.read(jsonReader);
                if (read != null || !primitive) {
                    field.set(o, read);
                }
            }
            
            @Override
            void write(final JsonWriter jsonWriter, final Object o) throws IOException, IllegalAccessException {
                final Object value = field.get(o);
                TypeAdapter<Object> val$typeAdapter;
                if (b3) {
                    val$typeAdapter = (TypeAdapter<Object>)adapter;
                }
                else {
                    val$typeAdapter = new TypeAdapterRuntimeTypeWrapper<Object>(gson, adapter, typeToken.getType());
                }
                val$typeAdapter.write(jsonWriter, value);
            }
            
            public boolean writeField(final Object o) throws IOException, IllegalAccessException {
                return this.serialized && field.get(o) != o;
            }
        };
    }
    
    static boolean excludeField(final Field field, final boolean b, final Excluder excluder) {
        return !excluder.excludeClass(field.getType(), b) && !excluder.excludeField(field, b);
    }
    
    private Map<String, BoundField> getBoundFields(final Gson gson, TypeToken<?> value, Class<?> rawType) {
        final LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (!rawType.isInterface()) {
            final Type type = value.getType();
            while (rawType != Object.class) {
                final Field[] declaredFields = rawType.getDeclaredFields();
                for (int length = declaredFields.length, i = 0; i < length; ++i) {
                    final Field field = declaredFields[i];
                    boolean excludeField = this.excludeField(field, true);
                    final boolean excludeField2 = this.excludeField(field, false);
                    if (excludeField || excludeField2) {
                        field.setAccessible(true);
                        final Type resolve = $Gson$Types.resolve(value.getType(), rawType, field.getGenericType());
                        final List<String> fieldNames = this.getFieldNames(field);
                        BoundField boundField = null;
                        BoundField boundField3;
                        for (int j = 0; j < fieldNames.size(); ++j, boundField = boundField3) {
                            final String s = fieldNames.get(j);
                            if (j != 0) {
                                excludeField = false;
                            }
                            final BoundField boundField2 = linkedHashMap.put(s, this.createBoundField(gson, field, s, TypeToken.get(resolve), excludeField, excludeField2));
                            if ((boundField3 = boundField) == null) {
                                boundField3 = boundField2;
                            }
                        }
                        if (boundField != null) {
                            throw new IllegalArgumentException(type + " declares multiple JSON fields named " + boundField.name);
                        }
                    }
                }
                value = TypeToken.get($Gson$Types.resolve(value.getType(), rawType, rawType.getGenericSuperclass()));
                rawType = value.getRawType();
            }
        }
        return linkedHashMap;
    }
    
    private List<String> getFieldNames(final Field field) {
        final SerializedName serializedName = field.getAnnotation(SerializedName.class);
        List<String> singletonList;
        if (serializedName == null) {
            singletonList = Collections.singletonList(this.fieldNamingPolicy.translateName(field));
        }
        else {
            final String value = serializedName.value();
            final String[] alternate = serializedName.alternate();
            if (alternate.length == 0) {
                return Collections.singletonList(value);
            }
            final ArrayList list = new ArrayList<String>(alternate.length + 1);
            list.add(value);
            final int length = alternate.length;
            int n = 0;
            while (true) {
                singletonList = (List<String>)list;
                if (n >= length) {
                    break;
                }
                list.add(alternate[n]);
                ++n;
            }
        }
        return singletonList;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Class<? super T> rawType = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(rawType)) {
            return null;
        }
        return new Adapter<T>((ObjectConstructor<Object>)this.constructorConstructor.get(typeToken), this.getBoundFields(gson, typeToken, rawType));
    }
    
    public boolean excludeField(final Field field, final boolean b) {
        return excludeField(field, b, this.excluder);
    }
    
    public static final class Adapter<T> extends TypeAdapter<T>
    {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;
        
        Adapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }
        
        @Override
        public T read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            final T construct = this.constructor.construct();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    final BoundField boundField = this.boundFields.get(jsonReader.nextName());
                    if (boundField != null && boundField.deserialized) {
                        goto Label_0084;
                    }
                    jsonReader.skipValue();
                }
            }
            catch (IllegalStateException ex) {
                throw new JsonSyntaxException(ex);
            }
            catch (IllegalAccessException ex2) {
                throw new AssertionError((Object)ex2);
            }
            jsonReader.endObject();
            return construct;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            if (t == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(t)) {
                        jsonWriter.name(boundField.name);
                        boundField.write(jsonWriter, t);
                    }
                }
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError((Object)ex);
            }
            jsonWriter.endObject();
        }
    }
    
    abstract static class BoundField
    {
        final boolean deserialized;
        final String name;
        final boolean serialized;
        
        protected BoundField(final String name, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract void read(final JsonReader p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract boolean writeField(final Object p0) throws IOException, IllegalAccessException;
    }
}
