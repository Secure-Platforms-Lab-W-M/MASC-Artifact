/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide;

public final class MemoryCategory
extends Enum<MemoryCategory> {
    private static final /* synthetic */ MemoryCategory[] $VALUES;
    public static final /* enum */ MemoryCategory HIGH;
    public static final /* enum */ MemoryCategory LOW;
    public static final /* enum */ MemoryCategory NORMAL;
    private final float multiplier;

    static {
        MemoryCategory memoryCategory;
        LOW = new MemoryCategory(0.5f);
        NORMAL = new MemoryCategory(1.0f);
        HIGH = memoryCategory = new MemoryCategory(1.5f);
        $VALUES = new MemoryCategory[]{LOW, NORMAL, memoryCategory};
    }

    private MemoryCategory(float f) {
        this.multiplier = f;
    }

    public static MemoryCategory valueOf(String string2) {
        return Enum.valueOf(MemoryCategory.class, string2);
    }

    public static MemoryCategory[] values() {
        return (MemoryCategory[])$VALUES.clone();
    }

    public float getMultiplier() {
        return this.multiplier;
    }
}

