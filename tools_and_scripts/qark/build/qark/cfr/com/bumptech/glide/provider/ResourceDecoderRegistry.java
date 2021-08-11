/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
    private final List<String> bucketPriorityList = new ArrayList<String>();
    private final Map<String, List<Entry<?, ?>>> decoders = new HashMap();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private List<Entry<?, ?>> getOrAddEntryList(String string2) {
        synchronized (this) {
            List list;
            if (!this.bucketPriorityList.contains(string2)) {
                this.bucketPriorityList.add(string2);
            }
            List list2 = list = this.decoders.get(string2);
            if (list == null) {
                list2 = new ArrayList();
                this.decoders.put(string2, list2);
            }
            return list2;
        }
    }

    public <T, R> void append(String string2, ResourceDecoder<T, R> resourceDecoder, Class<T> class_, Class<R> class_2) {
        synchronized (this) {
            this.getOrAddEntryList(string2).add(new Entry<T, R>(class_, class_2, resourceDecoder));
            return;
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public <T, R> List<ResourceDecoder<T, R>> getDecoders(Class<T> class_, Class<R> class_2) {
        synchronized (this) {
            ArrayList<ResourceDecoder<T, R>> arrayList = new ArrayList<ResourceDecoder<T, R>>();
            Iterator<String> iterator = this.bucketPriorityList.iterator();
            while (iterator.hasNext()) {
                Iterator iterator2 = iterator.next();
                if ((iterator2 = this.decoders.get(iterator2)) == null) continue;
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    Entry entry = (Entry)iterator2.next();
                    if (!entry.handles(class_, class_2)) continue;
                    arrayList.add(entry.decoder);
                }
            }
            return arrayList;
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public <T, R> List<Class<R>> getResourceClasses(Class<T> class_, Class<R> class_2) {
        synchronized (this) {
            ArrayList<Class<R>> arrayList = new ArrayList<Class<R>>();
            Iterator<String> iterator = this.bucketPriorityList.iterator();
            while (iterator.hasNext()) {
                Iterator iterator2 = iterator.next();
                if ((iterator2 = this.decoders.get(iterator2)) == null) continue;
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    Entry entry = (Entry)iterator2.next();
                    if (!entry.handles(class_, class_2) || arrayList.contains(entry.resourceClass)) continue;
                    arrayList.add(entry.resourceClass);
                }
            }
            return arrayList;
        }
    }

    public <T, R> void prepend(String string2, ResourceDecoder<T, R> resourceDecoder, Class<T> class_, Class<R> class_2) {
        synchronized (this) {
            this.getOrAddEntryList(string2).add(0, new Entry<T, R>(class_, class_2, resourceDecoder));
            return;
        }
    }

    public void setBucketPriorityList(List<String> list) {
        synchronized (this) {
            Object object = new ArrayList<String>(this.bucketPriorityList);
            this.bucketPriorityList.clear();
            for (String string2 : list) {
                this.bucketPriorityList.add(string2);
            }
            object = object.iterator();
            while (object.hasNext()) {
                String string3 = (String)object.next();
                if (list.contains(string3)) continue;
                this.bucketPriorityList.add(string3);
            }
            return;
        }
    }

    private static class Entry<T, R> {
        private final Class<T> dataClass;
        final ResourceDecoder<T, R> decoder;
        final Class<R> resourceClass;

        public Entry(Class<T> class_, Class<R> class_2, ResourceDecoder<T, R> resourceDecoder) {
            this.dataClass = class_;
            this.resourceClass = class_2;
            this.decoder = resourceDecoder;
        }

        public boolean handles(Class<?> class_, Class<?> class_2) {
            if (this.dataClass.isAssignableFrom(class_) && class_2.isAssignableFrom(this.resourceClass)) {
                return true;
            }
            return false;
        }
    }

}

