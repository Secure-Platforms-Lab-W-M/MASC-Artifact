// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext
{
     <T> T deserialize(final JsonElement p0, final Type p1) throws JsonParseException;
}
