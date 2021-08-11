// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

public final class JsonNull extends JsonElement
{
    public static final JsonNull INSTANCE;
    
    static {
        INSTANCE = new JsonNull();
    }
    
    @Deprecated
    public JsonNull() {
    }
    
    @Override
    public JsonNull deepCopy() {
        return JsonNull.INSTANCE;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || o instanceof JsonNull;
    }
    
    @Override
    public int hashCode() {
        return JsonNull.class.hashCode();
    }
}
