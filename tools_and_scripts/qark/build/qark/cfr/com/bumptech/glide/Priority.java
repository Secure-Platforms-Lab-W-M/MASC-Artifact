/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide;

public final class Priority
extends Enum<Priority> {
    private static final /* synthetic */ Priority[] $VALUES;
    public static final /* enum */ Priority HIGH;
    public static final /* enum */ Priority IMMEDIATE;
    public static final /* enum */ Priority LOW;
    public static final /* enum */ Priority NORMAL;

    static {
        Priority priority;
        IMMEDIATE = new Priority();
        HIGH = new Priority();
        NORMAL = new Priority();
        LOW = priority = new Priority();
        $VALUES = new Priority[]{IMMEDIATE, HIGH, NORMAL, priority};
    }

    private Priority() {
    }

    public static Priority valueOf(String string2) {
        return Enum.valueOf(Priority.class, string2);
    }

    public static Priority[] values() {
        return (Priority[])$VALUES.clone();
    }
}

