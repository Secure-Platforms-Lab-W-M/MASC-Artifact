// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public final class JsonArray extends JsonElement implements Iterable<JsonElement>
{
    private final List<JsonElement> elements;
    
    public JsonArray() {
        this.elements = new ArrayList<JsonElement>();
    }
    
    public JsonArray(final int n) {
        this.elements = new ArrayList<JsonElement>(n);
    }
    
    public void add(final JsonElement jsonElement) {
        JsonElement instance = jsonElement;
        if (jsonElement == null) {
            instance = JsonNull.INSTANCE;
        }
        this.elements.add(instance);
    }
    
    public void add(final Boolean b) {
        final List<JsonElement> elements = this.elements;
        JsonElement instance;
        if (b == null) {
            instance = JsonNull.INSTANCE;
        }
        else {
            instance = new JsonPrimitive(b);
        }
        elements.add(instance);
    }
    
    public void add(final Character c) {
        final List<JsonElement> elements = this.elements;
        JsonElement instance;
        if (c == null) {
            instance = JsonNull.INSTANCE;
        }
        else {
            instance = new JsonPrimitive(c);
        }
        elements.add(instance);
    }
    
    public void add(final Number n) {
        final List<JsonElement> elements = this.elements;
        JsonElement instance;
        if (n == null) {
            instance = JsonNull.INSTANCE;
        }
        else {
            instance = new JsonPrimitive(n);
        }
        elements.add(instance);
    }
    
    public void add(final String s) {
        final List<JsonElement> elements = this.elements;
        JsonElement instance;
        if (s == null) {
            instance = JsonNull.INSTANCE;
        }
        else {
            instance = new JsonPrimitive(s);
        }
        elements.add(instance);
    }
    
    public void addAll(final JsonArray jsonArray) {
        this.elements.addAll(jsonArray.elements);
    }
    
    public boolean contains(final JsonElement jsonElement) {
        return this.elements.contains(jsonElement);
    }
    
    @Override
    public JsonArray deepCopy() {
        JsonArray jsonArray2;
        if (!this.elements.isEmpty()) {
            final JsonArray jsonArray = new JsonArray(this.elements.size());
            final Iterator<JsonElement> iterator = this.elements.iterator();
            while (true) {
                jsonArray2 = jsonArray;
                if (!iterator.hasNext()) {
                    break;
                }
                jsonArray.add(iterator.next().deepCopy());
            }
        }
        else {
            jsonArray2 = new JsonArray();
        }
        return jsonArray2;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements));
    }
    
    public JsonElement get(final int n) {
        return this.elements.get(n);
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBigDecimal();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBigInteger();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBoolean();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public byte getAsByte() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsByte();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public char getAsCharacter() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsCharacter();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public double getAsDouble() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsDouble();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public float getAsFloat() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsFloat();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int getAsInt() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsInt();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public long getAsLong() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsLong();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public Number getAsNumber() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsNumber();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public short getAsShort() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsShort();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public String getAsString() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsString();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int hashCode() {
        return this.elements.hashCode();
    }
    
    @Override
    public Iterator<JsonElement> iterator() {
        return this.elements.iterator();
    }
    
    public JsonElement remove(final int n) {
        return this.elements.remove(n);
    }
    
    public boolean remove(final JsonElement jsonElement) {
        return this.elements.remove(jsonElement);
    }
    
    public JsonElement set(final int n, final JsonElement jsonElement) {
        return this.elements.set(n, jsonElement);
    }
    
    public int size() {
        return this.elements.size();
    }
}
