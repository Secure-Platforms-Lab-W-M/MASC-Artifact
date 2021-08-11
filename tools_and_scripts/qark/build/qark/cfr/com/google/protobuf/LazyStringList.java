/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import java.util.List;

public interface LazyStringList
extends List<String> {
    public void add(ByteString var1);

    public ByteString getByteString(int var1);

    public List<?> getUnderlyingElements();
}

