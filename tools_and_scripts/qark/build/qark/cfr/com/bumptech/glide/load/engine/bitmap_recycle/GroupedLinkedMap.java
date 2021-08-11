/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GroupedLinkedMap<K extends Poolable, V> {
    private final LinkedEntry<K, V> head = new LinkedEntry();
    private final Map<K, LinkedEntry<K, V>> keyToEntry = new HashMap<K, LinkedEntry<K, V>>();

    GroupedLinkedMap() {
    }

    private void makeHead(LinkedEntry<K, V> linkedEntry) {
        GroupedLinkedMap.removeEntry(linkedEntry);
        linkedEntry.prev = this.head;
        linkedEntry.next = this.head.next;
        GroupedLinkedMap.updateEntry(linkedEntry);
    }

    private void makeTail(LinkedEntry<K, V> linkedEntry) {
        GroupedLinkedMap.removeEntry(linkedEntry);
        linkedEntry.prev = this.head.prev;
        linkedEntry.next = this.head;
        GroupedLinkedMap.updateEntry(linkedEntry);
    }

    private static <K, V> void removeEntry(LinkedEntry<K, V> linkedEntry) {
        linkedEntry.prev.next = linkedEntry.next;
        linkedEntry.next.prev = linkedEntry.prev;
    }

    private static <K, V> void updateEntry(LinkedEntry<K, V> linkedEntry) {
        linkedEntry.next.prev = linkedEntry;
        linkedEntry.prev.next = linkedEntry;
    }

    public V get(K linkedEntry) {
        LinkedEntry<K, V> linkedEntry2 = this.keyToEntry.get(linkedEntry);
        if (linkedEntry2 == null) {
            linkedEntry2 = new LinkedEntry(linkedEntry);
            this.keyToEntry.put(linkedEntry, linkedEntry2);
            linkedEntry = linkedEntry2;
        } else {
            linkedEntry.offer();
            linkedEntry = linkedEntry2;
        }
        this.makeHead(linkedEntry);
        return linkedEntry.removeLast();
    }

    public void put(K linkedEntry, V v) {
        LinkedEntry<K, V> linkedEntry2 = this.keyToEntry.get(linkedEntry);
        if (linkedEntry2 == null) {
            linkedEntry2 = new LinkedEntry(linkedEntry);
            this.makeTail(linkedEntry2);
            this.keyToEntry.put(linkedEntry, linkedEntry2);
            linkedEntry = linkedEntry2;
        } else {
            linkedEntry.offer();
            linkedEntry = linkedEntry2;
        }
        linkedEntry.add(v);
    }

    public V removeLast() {
        LinkedEntry linkedEntry = this.head.prev;
        while (!linkedEntry.equals(this.head)) {
            Object v = linkedEntry.removeLast();
            if (v != null) {
                return v;
            }
            GroupedLinkedMap.removeEntry(linkedEntry);
            this.keyToEntry.remove(linkedEntry.key);
            ((Poolable)linkedEntry.key).offer();
            linkedEntry = linkedEntry.prev;
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("GroupedLinkedMap( ");
        LinkedEntry linkedEntry = this.head.next;
        boolean bl = false;
        while (!linkedEntry.equals(this.head)) {
            bl = true;
            stringBuilder.append('{');
            stringBuilder.append(linkedEntry.key);
            stringBuilder.append(':');
            stringBuilder.append(linkedEntry.size());
            stringBuilder.append("}, ");
            linkedEntry = linkedEntry.next;
        }
        if (bl) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }

    private static class LinkedEntry<K, V> {
        final K key;
        LinkedEntry<K, V> next;
        LinkedEntry<K, V> prev;
        private List<V> values;

        LinkedEntry() {
            this(null);
        }

        LinkedEntry(K k) {
            this.prev = this;
            this.next = this;
            this.key = k;
        }

        public void add(V v) {
            if (this.values == null) {
                this.values = new ArrayList<V>();
            }
            this.values.add(v);
        }

        public V removeLast() {
            int n = this.size();
            if (n > 0) {
                return this.values.remove(n - 1);
            }
            return null;
        }

        public int size() {
            List<V> list = this.values;
            if (list != null) {
                return list.size();
            }
            return 0;
        }
    }

}

