/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    public <Z> void append(Class<Z> class_, ResourceEncoder<Z> resourceEncoder) {
        synchronized (this) {
            this.encoders.add(new Entry<Z>(class_, resourceEncoder));
            return;
        }
    }

    public <Z> ResourceEncoder<Z> get(Class<Z> object) {
        synchronized (this) {
            int n = this.encoders.size();
            for (int i = 0; i < n; ++i) {
                Entry<Z> entry = this.encoders.get(i);
                if (!entry.handles(object)) continue;
                object = entry.encoder;
                return object;
            }
            return null;
        }
    }

    public <Z> void prepend(Class<Z> class_, ResourceEncoder<Z> resourceEncoder) {
        synchronized (this) {
            this.encoders.add(0, new Entry<Z>(class_, resourceEncoder));
            return;
        }
    }

    private static final class Entry<T> {
        final ResourceEncoder<T> encoder;
        private final Class<T> resourceClass;

        Entry(Class<T> class_, ResourceEncoder<T> resourceEncoder) {
            this.resourceClass = class_;
            this.encoder = resourceEncoder;
        }

        boolean handles(Class<?> class_) {
            return this.resourceClass.isAssignableFrom(class_);
        }
    }

}

