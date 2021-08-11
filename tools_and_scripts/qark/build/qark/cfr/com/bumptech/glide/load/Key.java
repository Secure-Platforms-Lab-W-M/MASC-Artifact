/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public interface Key {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String STRING_CHARSET_NAME = "UTF-8";

    public boolean equals(Object var1);

    public int hashCode();

    public void updateDiskCacheKey(MessageDigest var1);
}

