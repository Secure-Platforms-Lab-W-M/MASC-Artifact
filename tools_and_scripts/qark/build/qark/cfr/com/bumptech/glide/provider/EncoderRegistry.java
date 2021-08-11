/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    public <T> void append(Class<T> class_, Encoder<T> encoder) {
        synchronized (this) {
            this.encoders.add(new Entry<T>(class_, encoder));
            return;
        }
    }

    public <T> Encoder<T> getEncoder(Class<T> object) {
        synchronized (this) {
            for (Entry entry : this.encoders) {
                if (!entry.handles(object)) continue;
                object = entry.encoder;
                return object;
            }
            return null;
        }
    }

    public <T> void prepend(Class<T> class_, Encoder<T> encoder) {
        synchronized (this) {
            this.encoders.add(0, new Entry<T>(class_, encoder));
            return;
        }
    }

    private static final class Entry<T> {
        private final Class<T> dataClass;
        final Encoder<T> encoder;

        Entry(Class<T> class_, Encoder<T> encoder) {
            this.dataClass = class_;
            this.encoder = encoder;
        }

        boolean handles(Class<?> class_) {
            return this.dataClass.isAssignableFrom(class_);
        }
    }

}

