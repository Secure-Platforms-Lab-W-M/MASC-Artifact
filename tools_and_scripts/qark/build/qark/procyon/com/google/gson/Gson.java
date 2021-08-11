// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.Streams;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;
import java.io.StringReader;
import java.io.Reader;
import java.io.EOFException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.Primitives;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collection;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Type;
import java.util.Collections;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import java.util.List;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ConstructorConstructor;
import java.util.Map;
import com.google.gson.reflect.TypeToken;

public final class Gson
{
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private static final TypeToken<?> NULL_KEY_SURROGATE;
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final List<TypeAdapterFactory> factories;
    private final FieldNamingStrategy fieldNamingStrategy;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final boolean lenient;
    private final boolean prettyPrinting;
    private final boolean serializeNulls;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache;
    
    static {
        NULL_KEY_SURROGATE = TypeToken.get((Class<?>)Object.class);
    }
    
    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }
    
    Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingStrategy, final Map<Type, InstanceCreator<?>> map, final boolean serializeNulls, final boolean b, final boolean generateNonExecutableJson, final boolean htmlSafe, final boolean prettyPrinting, final boolean lenient, final boolean b2, final LongSerializationPolicy longSerializationPolicy, final List<TypeAdapterFactory> list) {
        this.calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
        this.typeTokenCache = new ConcurrentHashMap<TypeToken<?>, TypeAdapter<?>>();
        this.constructorConstructor = new ConstructorConstructor(map);
        this.excluder = excluder;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.serializeNulls = serializeNulls;
        this.generateNonExecutableJson = generateNonExecutableJson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        this.lenient = lenient;
        final ArrayList<TypeAdapterFactory> list2 = new ArrayList<TypeAdapterFactory>();
        list2.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        list2.add(ObjectTypeAdapter.FACTORY);
        list2.add(excluder);
        list2.addAll((Collection<?>)list);
        list2.add(TypeAdapters.STRING_FACTORY);
        list2.add(TypeAdapters.INTEGER_FACTORY);
        list2.add(TypeAdapters.BOOLEAN_FACTORY);
        list2.add(TypeAdapters.BYTE_FACTORY);
        list2.add(TypeAdapters.SHORT_FACTORY);
        final TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
        list2.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
        list2.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(b2)));
        list2.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(b2)));
        list2.add(TypeAdapters.NUMBER_FACTORY);
        list2.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        list2.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        list2.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        list2.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        list2.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        list2.add(TypeAdapters.CHARACTER_FACTORY);
        list2.add(TypeAdapters.STRING_BUILDER_FACTORY);
        list2.add(TypeAdapters.STRING_BUFFER_FACTORY);
        list2.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        list2.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        list2.add(TypeAdapters.URL_FACTORY);
        list2.add(TypeAdapters.URI_FACTORY);
        list2.add(TypeAdapters.UUID_FACTORY);
        list2.add(TypeAdapters.CURRENCY_FACTORY);
        list2.add(TypeAdapters.LOCALE_FACTORY);
        list2.add(TypeAdapters.INET_ADDRESS_FACTORY);
        list2.add(TypeAdapters.BIT_SET_FACTORY);
        list2.add(DateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.CALENDAR_FACTORY);
        list2.add(TimeTypeAdapter.FACTORY);
        list2.add(SqlDateTypeAdapter.FACTORY);
        list2.add(TypeAdapters.TIMESTAMP_FACTORY);
        list2.add(ArrayTypeAdapter.FACTORY);
        list2.add(TypeAdapters.CLASS_FACTORY);
        list2.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        list2.add(new MapTypeAdapterFactory(this.constructorConstructor, b));
        list2.add(this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
        list2.add(TypeAdapters.ENUM_FACTORY);
        list2.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder, this.jsonAdapterFactory));
        this.factories = (List<TypeAdapterFactory>)Collections.unmodifiableList((List<?>)list2);
    }
    
    private static void assertFullConsumption(final Object o, final JsonReader jsonReader) {
        if (o != null) {
            try {
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            }
            catch (MalformedJsonException ex) {
                throw new JsonSyntaxException(ex);
            }
            catch (IOException ex2) {
                throw new JsonIOException(ex2);
            }
        }
    }
    
    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public AtomicLong read(final JsonReader jsonReader) throws IOException {
                return new AtomicLong(typeAdapter.read(jsonReader).longValue());
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicLong atomicLong) throws IOException {
                typeAdapter.write(jsonWriter, atomicLong.get());
            }
        }.nullSafe();
    }
    
    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public AtomicLongArray read(final JsonReader jsonReader) throws IOException {
                final ArrayList<Long> list = new ArrayList<Long>();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    list.add(typeAdapter.read(jsonReader).longValue());
                }
                jsonReader.endArray();
                final int size = list.size();
                final AtomicLongArray atomicLongArray = new AtomicLongArray(size);
                for (int i = 0; i < size; ++i) {
                    atomicLongArray.set(i, (long)list.get(i));
                }
                return atomicLongArray;
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final AtomicLongArray atomicLongArray) throws IOException {
                jsonWriter.beginArray();
                for (int i = 0; i < atomicLongArray.length(); ++i) {
                    typeAdapter.write(jsonWriter, atomicLongArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
    }
    
    static void checkValidFloatingPoint(final double n) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            throw new IllegalArgumentException(n + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }
    
    private TypeAdapter<Number> doubleAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.checkValidFloatingPoint(n.doubleValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private TypeAdapter<Number> floatAdapter(final boolean b) {
        if (b) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return (float)jsonReader.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.checkValidFloatingPoint(n.floatValue());
                jsonWriter.value(n);
            }
        };
    }
    
    private static TypeAdapter<Number> longAdapter(final LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextLong();
            }
            
            @Override
            public void write(final JsonWriter jsonWriter, final Number n) throws IOException {
                if (n == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(n.toString());
            }
        };
    }
    
    public Excluder excluder() {
        return this.excluder;
    }
    
    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }
    
    public <T> T fromJson(final JsonElement jsonElement, final Class<T> clazz) throws JsonSyntaxException {
        return Primitives.wrap(clazz).cast(this.fromJson(jsonElement, (Type)clazz));
    }
    
    public <T> T fromJson(final JsonElement jsonElement, final Type type) throws JsonSyntaxException {
        if (jsonElement == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(jsonElement), type);
    }
    
    public <T> T fromJson(final JsonReader jsonReader, final Type type) throws JsonIOException, JsonSyntaxException {
        boolean b = true;
        jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            jsonReader.peek();
            b = false;
            return (T)this.getAdapter(TypeToken.get(type)).read(jsonReader);
        }
        catch (EOFException ex) {
            if (b) {
                return null;
            }
            throw new JsonSyntaxException(ex);
        }
        catch (IllegalStateException ex3) {}
        catch (IOException ex4) {
            final IOException ex2;
            throw new JsonSyntaxException(ex2);
        }
    }
    
    public <T> T fromJson(final Reader reader, final Class<T> clazz) throws JsonSyntaxException, JsonIOException {
        final JsonReader jsonReader = this.newJsonReader(reader);
        final Object fromJson = this.fromJson(jsonReader, clazz);
        assertFullConsumption(fromJson, jsonReader);
        return Primitives.wrap(clazz).cast(fromJson);
    }
    
    public <T> T fromJson(final Reader reader, final Type type) throws JsonIOException, JsonSyntaxException {
        final JsonReader jsonReader = this.newJsonReader(reader);
        final Object fromJson = this.fromJson(jsonReader, type);
        assertFullConsumption(fromJson, jsonReader);
        return (T)fromJson;
    }
    
    public <T> T fromJson(final String s, final Class<T> clazz) throws JsonSyntaxException {
        return Primitives.wrap(clazz).cast(this.fromJson(s, (Type)clazz));
    }
    
    public <T> T fromJson(final String s, final Type type) throws JsonSyntaxException {
        if (s == null) {
            return null;
        }
        return this.fromJson(new StringReader(s), type);
    }
    
    public <T> TypeAdapter<T> getAdapter(final TypeToken<T> typeToken) {
        final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = this.typeTokenCache;
        TypeToken<?> null_KEY_SURROGATE;
        if (typeToken == null) {
            null_KEY_SURROGATE = Gson.NULL_KEY_SURROGATE;
        }
        else {
            null_KEY_SURROGATE = typeToken;
        }
        final TypeAdapter<T> typeAdapter = typeTokenCache.get(null_KEY_SURROGATE);
        if (typeAdapter != null) {
            return typeAdapter;
        }
        final Map<TypeToken<?>, FutureTypeAdapter<?>> map = this.calls.get();
        boolean b = false;
        Map<TypeToken<T>, FutureTypeAdapter<?>> map2;
        if ((map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)map) == null) {
            map2 = (Map<TypeToken<T>, FutureTypeAdapter<?>>)new HashMap<TypeToken<T>, FutureTypeAdapter<Object>>();
            this.calls.set((Map<TypeToken<?>, FutureTypeAdapter<?>>)map2);
            b = true;
        }
        final FutureTypeAdapter<?> futureTypeAdapter = map2.get(typeToken);
        if (futureTypeAdapter != null) {
            return (TypeAdapter<T>)futureTypeAdapter;
        }
        try {
            final FutureTypeAdapter<Object> futureTypeAdapter2 = new FutureTypeAdapter<Object>();
            map2.put(typeToken, futureTypeAdapter2);
            final Iterator<TypeAdapterFactory> iterator = this.factories.iterator();
            while (iterator.hasNext()) {
                final TypeAdapter<?> create = iterator.next().create(this, (TypeToken<?>)typeToken);
                if (create != null) {
                    futureTypeAdapter2.setDelegate(create);
                    this.typeTokenCache.put(typeToken, create);
                    return (TypeAdapter<T>)create;
                }
            }
            throw new IllegalArgumentException("GSON cannot handle " + typeToken);
        }
        finally {
            map2.remove(typeToken);
            if (b) {
                this.calls.remove();
            }
        }
    }
    
    public <T> TypeAdapter<T> getAdapter(final Class<T> clazz) {
        return this.getAdapter((TypeToken<T>)TypeToken.get((Class<T>)clazz));
    }
    
    public <T> TypeAdapter<T> getDelegateAdapter(final TypeAdapterFactory typeAdapterFactory, final TypeToken<T> typeToken) {
        TypeAdapterFactory jsonAdapterFactory = typeAdapterFactory;
        if (!this.factories.contains(typeAdapterFactory)) {
            jsonAdapterFactory = this.jsonAdapterFactory;
        }
        int n = 0;
        for (final TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (n == 0) {
                if (typeAdapterFactory2 != jsonAdapterFactory) {
                    continue;
                }
                n = 1;
            }
            else {
                final TypeAdapter<T> create = typeAdapterFactory2.create(this, typeToken);
                if (create != null) {
                    return create;
                }
                continue;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + typeToken);
    }
    
    public boolean htmlSafe() {
        return this.htmlSafe;
    }
    
    public JsonReader newJsonReader(final Reader reader) {
        final JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(this.lenient);
        return jsonReader;
    }
    
    public JsonWriter newJsonWriter(final Writer writer) throws IOException {
        if (this.generateNonExecutableJson) {
            writer.write(")]}'\n");
        }
        final JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }
    
    public boolean serializeNulls() {
        return this.serializeNulls;
    }
    
    public String toJson(final JsonElement jsonElement) {
        final StringWriter stringWriter = new StringWriter();
        this.toJson(jsonElement, stringWriter);
        return stringWriter.toString();
    }
    
    public String toJson(final Object o) {
        if (o == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        return this.toJson(o, o.getClass());
    }
    
    public String toJson(final Object o, final Type type) {
        final StringWriter stringWriter = new StringWriter();
        this.toJson(o, type, stringWriter);
        return stringWriter.toString();
    }
    
    public void toJson(final JsonElement jsonElement, final JsonWriter jsonWriter) throws JsonIOException {
        final boolean lenient = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        final boolean htmlSafe = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        final boolean serializeNulls = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, jsonWriter);
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
        finally {
            jsonWriter.setLenient(lenient);
            jsonWriter.setHtmlSafe(htmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        }
    }
    
    public void toJson(final JsonElement jsonElement, final Appendable appendable) throws JsonIOException {
        try {
            this.toJson(jsonElement, this.newJsonWriter(Streams.writerForAppendable(appendable)));
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
    }
    
    public void toJson(final Object o, final Appendable appendable) throws JsonIOException {
        if (o != null) {
            this.toJson(o, o.getClass(), appendable);
            return;
        }
        this.toJson(JsonNull.INSTANCE, appendable);
    }
    
    public void toJson(final Object o, final Type type, final JsonWriter jsonWriter) throws JsonIOException {
        final TypeAdapter<?> adapter = this.getAdapter(TypeToken.get(type));
        final boolean lenient = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        final boolean htmlSafe = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        final boolean serializeNulls = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            adapter.write(jsonWriter, o);
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
        finally {
            jsonWriter.setLenient(lenient);
            jsonWriter.setHtmlSafe(htmlSafe);
            jsonWriter.setSerializeNulls(serializeNulls);
        }
    }
    
    public void toJson(final Object o, final Type type, final Appendable appendable) throws JsonIOException {
        try {
            this.toJson(o, type, this.newJsonWriter(Streams.writerForAppendable(appendable)));
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
    }
    
    public JsonElement toJsonTree(final Object o) {
        if (o == null) {
            return JsonNull.INSTANCE;
        }
        return this.toJsonTree(o, o.getClass());
    }
    
    public JsonElement toJsonTree(final Object o, final Type type) {
        final JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        this.toJson(o, type, jsonTreeWriter);
        return jsonTreeWriter.get();
    }
    
    @Override
    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + ",factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }
    
    static class FutureTypeAdapter<T> extends TypeAdapter<T>
    {
        private TypeAdapter<T> delegate;
        
        @Override
        public T read(final JsonReader jsonReader) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            return this.delegate.read(jsonReader);
        }
        
        public void setDelegate(final TypeAdapter<T> delegate) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = delegate;
        }
        
        @Override
        public void write(final JsonWriter jsonWriter, final T t) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(jsonWriter, t);
        }
    }
}
