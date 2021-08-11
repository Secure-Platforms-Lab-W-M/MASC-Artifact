// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory
{
     <T> TypeAdapter<T> create(final Gson p0, final TypeToken<T> p1);
}
