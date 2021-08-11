/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.ImageHeaderParser;
import java.util.ArrayList;
import java.util.List;

public final class ImageHeaderParserRegistry {
    private final List<ImageHeaderParser> parsers = new ArrayList<ImageHeaderParser>();

    public void add(ImageHeaderParser imageHeaderParser) {
        synchronized (this) {
            this.parsers.add(imageHeaderParser);
            return;
        }
    }

    public List<ImageHeaderParser> getParsers() {
        synchronized (this) {
            List<ImageHeaderParser> list = this.parsers;
            return list;
        }
    }
}

