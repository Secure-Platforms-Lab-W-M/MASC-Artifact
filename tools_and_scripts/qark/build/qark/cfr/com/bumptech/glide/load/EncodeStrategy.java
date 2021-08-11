/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

public final class EncodeStrategy
extends Enum<EncodeStrategy> {
    private static final /* synthetic */ EncodeStrategy[] $VALUES;
    public static final /* enum */ EncodeStrategy NONE;
    public static final /* enum */ EncodeStrategy SOURCE;
    public static final /* enum */ EncodeStrategy TRANSFORMED;

    static {
        EncodeStrategy encodeStrategy;
        SOURCE = new EncodeStrategy();
        TRANSFORMED = new EncodeStrategy();
        NONE = encodeStrategy = new EncodeStrategy();
        $VALUES = new EncodeStrategy[]{SOURCE, TRANSFORMED, encodeStrategy};
    }

    private EncodeStrategy() {
    }

    public static EncodeStrategy valueOf(String string2) {
        return Enum.valueOf(EncodeStrategy.class, string2);
    }

    public static EncodeStrategy[] values() {
        return (EncodeStrategy[])$VALUES.clone();
    }
}

