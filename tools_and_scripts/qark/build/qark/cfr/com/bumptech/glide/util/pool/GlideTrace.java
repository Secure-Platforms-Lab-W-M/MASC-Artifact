/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util.pool;

public final class GlideTrace {
    private static final int MAX_LENGTH = 127;
    private static final boolean TRACING_ENABLED = false;

    private GlideTrace() {
    }

    public static void beginSection(String string2) {
    }

    public static void beginSectionFormat(String string2, Object object) {
    }

    public static void beginSectionFormat(String string2, Object object, Object object2) {
    }

    public static void beginSectionFormat(String string2, Object object, Object object2, Object object3) {
    }

    public static void endSection() {
    }

    private static String truncateTag(String string2) {
        if (string2.length() > 127) {
            return string2.substring(0, 126);
        }
        return string2;
    }
}

