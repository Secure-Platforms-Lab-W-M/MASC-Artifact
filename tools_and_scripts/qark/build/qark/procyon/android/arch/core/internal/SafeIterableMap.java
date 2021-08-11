// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.core.internal;

import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.WeakHashMap;
import android.support.annotation.RestrictTo;
import java.util.Map;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SafeIterableMap<K, V> implements Iterable<Map.Entry<K, V>>
{
    private Entry<K, V> mEnd;
    private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators;
    private int mSize;
    private Entry<K, V> mStart;
    
    public SafeIterableMap() {
        this.mIterators = new WeakHashMap<SupportRemove<K, V>, Boolean>();
        this.mSize = 0;
    }
    
    public Iterator<Map.Entry<K, V>> descendingIterator() {
        final DescendingIterator descendingIterator = new DescendingIterator<K, V>(this.mEnd, this.mStart);
        this.mIterators.put((SupportRemove<K, V>)descendingIterator, false);
        return (Iterator<Map.Entry<K, V>>)descendingIterator;
    }
    
    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SafeIterableMap)) {
            return false;
        }
        final SafeIterableMap safeIterableMap = (SafeIterableMap)o;
        if (this.size() != safeIterableMap.size()) {
            return false;
        }
        final Iterator<Map.Entry<K, V>> iterator = this.iterator();
        final Iterator iterator2 = safeIterableMap.iterator();
        while (iterator.hasNext() && iterator2.hasNext()) {
            final Map.Entry entry = (Map.Entry)iterator.next();
            final Object next = iterator2.next();
            if (entry == null && next != null) {
                return false;
            }
            if (entry != null && !entry.equals(next)) {
                return false;
            }
        }
        return !iterator.hasNext() && !iterator2.hasNext();
    }
    
    protected Entry<K, V> get(final K k) {
        Entry<K, V> entry;
        for (entry = this.mStart; entry != null; entry = entry.mNext) {
            if (entry.mKey.equals(k)) {
                return entry;
            }
        }
        return entry;
    }
    
    @NonNull
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        final AscendingIterator ascendingIterator = new AscendingIterator<K, V>(this.mStart, this.mEnd);
        this.mIterators.put((SupportRemove<K, V>)ascendingIterator, false);
        return (Iterator<Map.Entry<K, V>>)ascendingIterator;
    }
    
    public IteratorWithAdditions iteratorWithAdditions() {
        final IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions();
        this.mIterators.put((SupportRemove<K, V>)iteratorWithAdditions, false);
        return iteratorWithAdditions;
    }
    
    public Map.Entry<K, V> newest() {
        return this.mEnd;
    }
    
    protected Entry<K, V> put(@NonNull final K k, @NonNull final V v) {
        final Entry<K, V> mEnd = new Entry<K, V>(k, v);
        ++this.mSize;
        final Entry<K, V> mEnd2 = this.mEnd;
        if (mEnd2 == null) {
            this.mStart = mEnd;
            this.mEnd = this.mStart;
            return mEnd;
        }
        mEnd2.mNext = mEnd;
        mEnd.mPrevious = mEnd2;
        return this.mEnd = mEnd;
    }
    
    public V putIfAbsent(@NonNull final K k, @NonNull final V v) {
        final Entry<K, V> value = this.get(k);
        if (value != null) {
            return value.mValue;
        }
        this.put(k, v);
        return null;
    }
    
    public V remove(@NonNull final K k) {
        final Entry<K, V> value = this.get(k);
        if (value == null) {
            return null;
        }
        --this.mSize;
        if (!this.mIterators.isEmpty()) {
            final Iterator<SupportRemove<K, V>> iterator = this.mIterators.keySet().iterator();
            while (iterator.hasNext()) {
                iterator.next().supportRemove(value);
            }
        }
        if (value.mPrevious != null) {
            value.mPrevious.mNext = value.mNext;
        }
        else {
            this.mStart = value.mNext;
        }
        if (value.mNext != null) {
            value.mNext.mPrevious = value.mPrevious;
        }
        else {
            this.mEnd = value.mPrevious;
        }
        value.mNext = null;
        value.mPrevious = null;
        return value.mValue;
    }
    
    public int size() {
        return this.mSize;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        final Iterator<Map.Entry<?, ?>> iterator = (Iterator<Map.Entry<?, ?>>)this.iterator();
        while (iterator.hasNext()) {
            sb.append(((Map.Entry<?, ?>)iterator.next()).toString());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    static class AscendingIterator<K, V> extends ListIterator<K, V>
    {
        AscendingIterator(final Entry<K, V> entry, final Entry<K, V> entry2) {
            super(entry, entry2);
        }
        
        @Override
        Entry<K, V> backward(final Entry<K, V> entry) {
            return entry.mPrevious;
        }
        
        @Override
        Entry<K, V> forward(final Entry<K, V> entry) {
            return entry.mNext;
        }
    }
    
    private static class DescendingIterator<K, V> extends ListIterator<K, V>
    {
        DescendingIterator(final Entry<K, V> entry, final Entry<K, V> entry2) {
            super(entry, entry2);
        }
        
        @Override
        Entry<K, V> backward(final Entry<K, V> entry) {
            return entry.mNext;
        }
        
        @Override
        Entry<K, V> forward(final Entry<K, V> entry) {
            return entry.mPrevious;
        }
    }
    
    static class Entry<K, V> implements Map.Entry<K, V>
    {
        @NonNull
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        @NonNull
        final V mValue;
        
        Entry(@NonNull final K mKey, @NonNull final V mValue) {
            this.mKey = mKey;
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry entry = (Entry)o;
            return this.mKey.equals(entry.mKey) && this.mValue.equals(entry.mValue);
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
        public V setValue(final V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mKey);
            sb.append("=");
            sb.append(this.mValue);
            return sb.toString();
        }
    }
    
    private class IteratorWithAdditions implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V>
    {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;
        
        private IteratorWithAdditions() {
            this.mBeforeStart = true;
        }
        
        @Override
        public boolean hasNext() {
            if (this.mBeforeStart) {
                return SafeIterableMap.this.mStart != null;
            }
            final Entry<K, V> mCurrent = this.mCurrent;
            return mCurrent != null && mCurrent.mNext != null;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                this.mCurrent = SafeIterableMap.this.mStart;
            }
            else {
                final Entry<K, V> mCurrent = this.mCurrent;
                Map.Entry<K, V> mNext;
                if (mCurrent != null) {
                    mNext = (Map.Entry<K, V>)mCurrent.mNext;
                }
                else {
                    mNext = null;
                }
                this.mCurrent = (Entry<K, V>)mNext;
            }
            return this.mCurrent;
        }
        
        @Override
        public void supportRemove(@NonNull final Entry<K, V> entry) {
            final Entry<K, V> mCurrent = this.mCurrent;
            if (entry == mCurrent) {
                this.mCurrent = mCurrent.mPrevious;
                this.mBeforeStart = (this.mCurrent == null);
            }
        }
    }
    
    private abstract static class ListIterator<K, V> implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V>
    {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;
        
        ListIterator(final Entry<K, V> mNext, final Entry<K, V> mExpectedEnd) {
            this.mExpectedEnd = mExpectedEnd;
            this.mNext = mNext;
        }
        
        private Entry<K, V> nextNode() {
            final Entry<K, V> mNext = this.mNext;
            final Entry<K, V> mExpectedEnd = this.mExpectedEnd;
            if (mNext != mExpectedEnd && mExpectedEnd != null) {
                return this.forward(mNext);
            }
            return null;
        }
        
        abstract Entry<K, V> backward(final Entry<K, V> p0);
        
        abstract Entry<K, V> forward(final Entry<K, V> p0);
        
        @Override
        public boolean hasNext() {
            return this.mNext != null;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            final Entry<K, V> mNext = this.mNext;
            this.mNext = this.nextNode();
            return mNext;
        }
        
        @Override
        public void supportRemove(@NonNull final Entry<K, V> entry) {
            if (this.mExpectedEnd == entry && entry == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            final Entry<K, V> mExpectedEnd = this.mExpectedEnd;
            if (mExpectedEnd == entry) {
                this.mExpectedEnd = this.backward(mExpectedEnd);
            }
            if (this.mNext == entry) {
                this.mNext = this.nextNode();
            }
        }
    }
    
    interface SupportRemove<K, V>
    {
        void supportRemove(@NonNull final Entry<K, V> p0);
    }
}
