/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TranscoderRegistry {
    private final List<Entry<?, ?>> transcoders = new ArrayList();

    public <Z, R> ResourceTranscoder<Z, R> get(Class<Z> resourceTranscoder, Class<R> class_) {
        synchronized (this) {
            block6 : {
                if (!class_.isAssignableFrom(resourceTranscoder)) break block6;
                resourceTranscoder = UnitTranscoder.get();
                return resourceTranscoder;
            }
            for (Entry<R, R> entry : this.transcoders) {
                if (!entry.handles(resourceTranscoder, class_)) continue;
                resourceTranscoder = entry.transcoder;
                return resourceTranscoder;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No transcoder registered to transcode from ");
            stringBuilder.append(resourceTranscoder);
            stringBuilder.append(" to ");
            stringBuilder.append(class_);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public <Z, R> List<Class<R>> getTranscodeClasses(Class<Z> class_, Class<R> class_2) {
        synchronized (this) {
            ArrayList<Class<R>> arrayList;
            block5 : {
                arrayList = new ArrayList<Class<R>>();
                if (!class_2.isAssignableFrom(class_)) break block5;
                arrayList.add(class_2);
                return arrayList;
            }
            Iterator iterator = this.transcoders.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().handles(class_, class_2)) continue;
                arrayList.add(class_2);
            }
            return arrayList;
        }
    }

    public <Z, R> void register(Class<Z> class_, Class<R> class_2, ResourceTranscoder<Z, R> resourceTranscoder) {
        synchronized (this) {
            this.transcoders.add(new Entry<Z, R>(class_, class_2, resourceTranscoder));
            return;
        }
    }

    private static final class Entry<Z, R> {
        private final Class<Z> fromClass;
        private final Class<R> toClass;
        final ResourceTranscoder<Z, R> transcoder;

        Entry(Class<Z> class_, Class<R> class_2, ResourceTranscoder<Z, R> resourceTranscoder) {
            this.fromClass = class_;
            this.toClass = class_2;
            this.transcoder = resourceTranscoder;
        }

        public boolean handles(Class<?> class_, Class<?> class_2) {
            if (this.fromClass.isAssignableFrom(class_) && class_2.isAssignableFrom(this.toClass)) {
                return true;
            }
            return false;
        }
    }

}

