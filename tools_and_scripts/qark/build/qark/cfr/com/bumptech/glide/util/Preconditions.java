/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.bumptech.glide.util;

import android.text.TextUtils;
import java.util.Collection;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean bl, String string2) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException(string2);
    }

    public static String checkNotEmpty(String string2) {
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            return string2;
        }
        throw new IllegalArgumentException("Must not be null or empty");
    }

    public static <T extends Collection<Y>, Y> T checkNotEmpty(T t) {
        if (!t.isEmpty()) {
            return t;
        }
        throw new IllegalArgumentException("Must not be empty.");
    }

    public static <T> T checkNotNull(T t) {
        return Preconditions.checkNotNull(t, "Argument must not be null");
    }

    public static <T> T checkNotNull(T t, String string2) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(string2);
    }
}

