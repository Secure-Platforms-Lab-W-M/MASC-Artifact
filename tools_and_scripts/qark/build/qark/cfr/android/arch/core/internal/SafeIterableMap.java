/*
 * Decompiled with CFR 0_124.
 */
package android.arch.core.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class SafeIterableMap<K, V>
implements Iterable<Map.Entry<K, V>> {
    private Entry<K, V> mEnd;
    private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators = new WeakHashMap();
    private int mSize = 0;
    private Entry<K, V> mStart;

    public Iterator<Map.Entry<K, V>> descendingIterator() {
        DescendingIterator<K, V> descendingIterator = new DescendingIterator<K, V>(this.mEnd, this.mStart);
        this.mIterators.put(descendingIterator, false);
        return descendingIterator;
    }

    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }

    public boolean equals(Object iterator) {
        if (iterator == this) {
            return true;
        }
        if (!(iterator instanceof SafeIterableMap)) {
            return false;
        }
        Object object = (SafeIterableMap)((Object)iterator);
        if (this.size() != object.size()) {
            return false;
        }
        iterator = this.iterator();
        object = object.iterator();
        while (iterator.hasNext() && object.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            Object e = object.next();
            if (entry == null && e != null) {
                return false;
            }
            if (entry == null || entry.equals(e)) continue;
            return false;
        }
        if (!iterator.hasNext() && !object.hasNext()) {
            return true;
        }
        return false;
    }

    protected Entry<K, V> get(K k) {
        Entry<K, V> entry = this.mStart;
        while (entry != null) {
            if (entry.mKey.equals(k)) {
                return entry;
            }
            entry = entry.mNext;
        }
        return entry;
    }

    @NonNull
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        AscendingIterator<K, V> ascendingIterator = new AscendingIterator<K, V>(this.mStart, this.mEnd);
        this.mIterators.put(ascendingIterator, false);
        return ascendingIterator;
    }

    public SafeIterableMap<K, V> iteratorWithAdditions() {
        IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions();
        this.mIterators.put(iteratorWithAdditions, false);
        return iteratorWithAdditions;
    }

    public Map.Entry<K, V> newest() {
        return this.mEnd;
    }

    protected Entry<K, V> put(@NonNull K object, @NonNull V entry) {
        object = new Entry<K, V>(object, entry);
        ++this.mSize;
        entry = this.mEnd;
        if (entry == null) {
            this.mStart = object;
            this.mEnd = this.mStart;
            return object;
        }
        entry.mNext = object;
        object.mPrevious = entry;
        this.mEnd = object;
        return object;
    }

    public V putIfAbsent(@NonNull K k, @NonNull V v) {
        Entry<K, V> entry = this.get(k);
        if (entry != null) {
            return entry.mValue;
        }
        this.put(k, v);
        return null;
    }

    public V remove(@NonNull K object) {
        if ((object = this.get(object)) == null) {
            return null;
        }
        --this.mSize;
        if (!this.mIterators.isEmpty()) {
            Iterator<SupportRemove<K, V>> iterator = this.mIterators.keySet().iterator();
            while (iterator.hasNext()) {
                iterator.next().supportRemove((Entry<K, V>)object);
            }
        }
        if (object.mPrevious != null) {
            object.mPrevious.mNext = object.mNext;
        } else {
            this.mStart = object.mNext;
        }
        if (object.mNext != null) {
            object.mNext.mPrevious = object.mPrevious;
        } else {
            this.mEnd = object.mPrevious;
        }
        object.mNext = null;
        object.mPrevious = null;
        return object.mValue;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Iterator<Map.Entry<K, V>> iterator = this.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (!iterator.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    static class AscendingIterator<K, V>
    extends ListIterator<K, V> {
        AscendingIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            super(entry, entry2);
        }

        @Override
        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mPrevious;
        }

        @Override
        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mNext;
        }
    }

    private static class DescendingIterator<K, V>
    extends ListIterator<K, V> {
        DescendingIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            super(entry, entry2);
        }

        @Override
        Entry<K, V> backward(Entry<K, V> entry) {
            return entry.mNext;
        }

        @Override
        Entry<K, V> forward(Entry<K, V> entry) {
            return entry.mPrevious;
        }
    }

    static class Entry<K, V>
    implements Map.Entry<K, V> {
        @NonNull
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        @NonNull
        final V mValue;

        Entry(@NonNull K k, @NonNull V v) {
            this.mKey = k;
            this.mValue = v;
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Entry)) {
                return false;
            }
            object = (Entry)object;
            if (this.mKey.equals(object.mKey) && this.mValue.equals(object.mValue)) {
                return true;
            }
            return false;
        }

        @NonNull
        @Override
        public K getKey() {
            return this.mKey;
        }

        @NonNull
        @Override
        public V getValue() {
            return this.mValue;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mKey);
            stringBuilder.append("=");
            stringBuilder.append(this.mValue);
            return stringBuilder.toString();
        }
    }

    private class IteratorWithAdditions
    implements Iterator<Map.Entry<K, V>>,
    SupportRemove<K, V> {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;

        private IteratorWithAdditions() {
            this.mBeforeStart = true;
        }

        @Override
        public boolean hasNext() {
            if (this.mBeforeStart) {
                if (SafeIterableMap.this.mStart != null) {
                    return true;
                }
                return false;
            }
            Entry<K, V> entry = this.mCurrent;
            if (entry != null && entry.mNext != null) {
                return true;
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                this.mCurrent = SafeIterableMap.this.mStart;
            } else {
                Entry<K, V> entry = this.mCurrent;
                entry = entry != null ? entry.mNext : null;
                this.mCurrent = entry;
            }
            return this.mCurrent;
        }

        @Override
        public void supportRemove(@NonNull Entry<K, V> entry) {
            Entry<K, V> entry2 = this.mCurrent;
            if (entry == entry2) {
                this.mCurrent = entry2.mPrevious;
                boolean bl = this.mCurrent == null;
                this.mBeforeStart = bl;
                return;
            }
        }
    }

    private static abstract class ListIterator<K, V>
    implements Iterator<Map.Entry<K, V>>,
    SupportRemove<K, V> {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;

        ListIterator(Entry<K, V> entry, Entry<K, V> entry2) {
            this.mExpectedEnd = entry2;
            this.mNext = entry;
        }

        private Entry<K, V> nextNode() {
            Entry<K, V> entry = this.mNext;
            Entry<K, V> entry2 = this.mExpectedEnd;
            if (entry != entry2 && entry2 != null) {
                return this.forward(entry);
            }
            return null;
        }

        abstract Entry<K, V> backward(Entry<K, V> var1);

        abstract Entry<K, V> forward(Entry<K, V> var1);

        @Override
        public boolean hasNext() {
            if (this.mNext != null) {
                return true;
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            Entry<K, V> entry = this.mNext;
            this.mNext = this.nextNode();
            return entry;
        }

        @Override
        public void supportRemove(@NonNull Entry<K, V> entry) {
            Entry<K, V> entry2;
            if (this.mExpectedEnd == entry && entry == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            if ((entry2 = this.mExpectedEnd) == entry) {
                this.mExpectedEnd = this.backward(entry2);
            }
            if (this.mNext == entry) {
                this.mNext = this.nextNode();
                return;
            }
        }
    }

    static interface SupportRemove<K, V> {
        public void supportRemove(@NonNull Entry<K, V> var1);
    }

}

