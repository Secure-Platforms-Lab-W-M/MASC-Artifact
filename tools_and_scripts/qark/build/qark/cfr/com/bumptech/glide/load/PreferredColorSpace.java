/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

public final class PreferredColorSpace
extends Enum<PreferredColorSpace> {
    private static final /* synthetic */ PreferredColorSpace[] $VALUES;
    public static final /* enum */ PreferredColorSpace DISPLAY_P3;
    public static final /* enum */ PreferredColorSpace SRGB;

    static {
        PreferredColorSpace preferredColorSpace;
        SRGB = new PreferredColorSpace();
        DISPLAY_P3 = preferredColorSpace = new PreferredColorSpace();
        $VALUES = new PreferredColorSpace[]{SRGB, preferredColorSpace};
    }

    private PreferredColorSpace() {
    }

    public static PreferredColorSpace valueOf(String string2) {
        return Enum.valueOf(PreferredColorSpace.class, string2);
    }

    public static PreferredColorSpace[] values() {
        return (PreferredColorSpace[])$VALUES.clone();
    }
}

