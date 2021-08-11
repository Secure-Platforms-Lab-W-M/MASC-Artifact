// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;

abstract class MapCollections<K, V>
{
    EntrySet mEntrySet;
    KeySet mKeySet;
    ValuesCollection mValues;
    
    public static <K, V> boolean containsAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!map.containsKey(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> boolean equalsSetHelper(final Set<T> set, final Object o) {
        if (set == o) {
            return true;
        }
        if (o instanceof Set) {
            final Set set2 = (Set)o;
            try {
                return set.size() == set2.size() && set.containsAll(set2);
            }
            catch (ClassCastException ex) {
                return false;
            }
            catch (NullPointerException ex2) {
                return false;
            }
        }
        return false;
    }
    
    public static <K, V> boolean removeAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final int size = map.size();
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            map.remove(iterator.next());
        }
        return size != map.size();
    }
    
    public static <K, V> boolean retainAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final int size = map.size();
        final Iterator<K> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return size != map.size();
    }
    
    protected abstract void colClear();
    
    protected abstract Object colGetEntry(final int p0, final int p1);
    
    protected abstract Map<K, V> colGetMap();
    
    protected abstract int colGetSize();
    
    protected abstract int colIndexOfKey(final Object p0);
    
    protected abstract int colIndexOfValue(final Object p0);
    
    protected abstract void colPut(final K p0, final V p1);
    
    protected abstract void colRemoveAt(final int p0);
    
    protected abstract V colSetValue(final int p0, final V p1);
    
    public Set<Map.Entry<K, V>> getEntrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet();
        }
        return this.mEntrySet;
    }
    
    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet();
        }
        return this.mKeySet;
    }
    
    public Collection<V> getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection();
        }
        return this.mValues;
    }
    
    public Object[] toArrayHelper(final int n) {
        final int colGetSize = this.colGetSize();
        final Object[] array = new Object[colGetSize];
        for (int i = 0; i < colGetSize; ++i) {
            array[i] = this.colGetEntry(i, n);
        }
        return array;
    }
    
    public <T> T[] toArrayHelper(T[] array, final int n) {
        final int colGetSize = this.colGetSize();
        if (array.length < colGetSize) {
            array = (T[])Array.newInstance(array.getClass().getComponentType(), colGetSize);
        }
        for (int i = 0; i < colGetSize; ++i) {
            array[i] = (T)this.colGetEntry(i, n);
        }
        if (array.length > colGetSize) {
            array[colGetSize] = null;
            return array;
        }
        return array;
    }
    
    final class ArrayIterator<T> implements Iterator<T>
    {
        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;
        
        ArrayIterator(final int mOffset) {
            this.mCanRemove = false;
            this.mOffset = mOffset;
            this.mSize = MapCollections.this.colGetSize();
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }
        
        @Override
        public T next() {
            if (this.hasNext()) {
                final Object colGetEntry = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
                ++this.mIndex;
                this.mCanRemove = true;
                return (T)colGetEntry;
            }
            throw new NoSuchElementException();
        }
        
        @Override
        public void remove() {
            if (this.mCanRemove) {
                --this.mIndex;
                --this.mSize;
                this.mCanRemove = false;
                MapCollections.this.colRemoveAt(this.mIndex);
                return;
            }
            throw new IllegalStateException();
        }
    }
    
    final class EntrySet implements Set<Map.Entry<K, V>>
    {
        @Override
        public boolean add(final Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> collection) {
            final int colGetSize = MapCollections.this.colGetSize();
            for (final Map.Entry<K, V> entry : collection) {
                MapCollections.this.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != MapCollections.this.colGetSize();
        }
        
        @Override
        public void clear() {
            MapCollections.this.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry entry = (Map.Entry)o;
            final int colIndexOfKey = MapCollections.this.colIndexOfKey(entry.getKey());
            return colIndexOfKey >= 0 && ContainerHelpers.equal(MapCollections.this.colGetEntry(colIndexOfKey, 1), entry.getValue());
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            final Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (!this.contains(iterator.next())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean equals(final Object o) {
            return MapCollections.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int n = 0;
            for (int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
                final MapCollections this$0 = MapCollections.this;
                int hashCode = 0;
                final Object colGetEntry = this$0.colGetEntry(i, 0);
                final Object colGetEntry2 = MapCollections.this.colGetEntry(i, 1);
                int hashCode2;
                if (colGetEntry == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = colGetEntry.hashCode();
                }
                if (colGetEntry2 != null) {
                    hashCode = colGetEntry2.hashCode();
                }
                n += (hashCode ^ hashCode2);
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator();
        }
        
        @Override
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            throw new UnsupportedOperationException();
        }
    }
    
    final class KeySet implements Set<K>
    {
        @Override
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            MapCollections.this.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapCollections.this.colIndexOfKey(o) >= 0;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), collection);
        }
        
        @Override
        public boolean equals(final Object o) {
            return MapCollections.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int n = 0;
            for (int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
                final MapCollections this$0 = MapCollections.this;
                int hashCode = 0;
                final Object colGetEntry = this$0.colGetEntry(i, 0);
                if (colGetEntry != null) {
                    hashCode = colGetEntry.hashCode();
                }
                n += hashCode;
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }
        
        @Override
        public Iterator<K> iterator() {
            return new ArrayIterator<K>(0);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfKey = MapCollections.this.colIndexOfKey(o);
            if (colIndexOfKey >= 0) {
                MapCollections.this.colRemoveAt(colIndexOfKey);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), collection);
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), collection);
        }
        
        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(0);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return MapCollections.this.toArrayHelper(array, 0);
        }
    }
    
    final class MapIterator implements Iterator<Entry<K, V>>, Entry<K, V>
    {
        int mEnd;
        boolean mEntryValid;
        int mIndex;
        
        MapIterator() {
            this.mEntryValid = false;
            this.mEnd = MapCollections.this.colGetSize() - 1;
            this.mIndex = -1;
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry entry = (Entry)o;
            return ContainerHelpers.equal(entry.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(entry.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1));
        }
        
        @Override
        public K getKey() {
            if (this.mEntryValid) {
                return (K)MapCollections.this.colGetEntry(this.mIndex, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }
        
        @Override
        public V getValue() {
            if (this.mEntryValid) {
                return (V)MapCollections.this.colGetEntry(this.mIndex, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }
        
        @Override
        public final int hashCode() {
            if (this.mEntryValid) {
                final MapCollections this$0 = MapCollections.this;
                final int mIndex = this.mIndex;
                int hashCode = 0;
                final Object colGetEntry = this$0.colGetEntry(mIndex, 0);
                final Object colGetEntry2 = MapCollections.this.colGetEntry(this.mIndex, 1);
                int hashCode2;
                if (colGetEntry == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = colGetEntry.hashCode();
                }
                if (colGetEntry2 != null) {
                    hashCode = colGetEntry2.hashCode();
                }
                return hashCode ^ hashCode2;
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }
        
        @Override
        public Entry<K, V> next() {
            if (this.hasNext()) {
                ++this.mIndex;
                this.mEntryValid = true;
                return this;
            }
            throw new NoSuchElementException();
        }
        
        @Override
        public void remove() {
            if (this.mEntryValid) {
                MapCollections.this.colRemoveAt(this.mIndex);
                --this.mIndex;
                --this.mEnd;
                this.mEntryValid = false;
                return;
            }
            throw new IllegalStateException();
        }
        
        @Override
        public V setValue(final V v) {
            if (this.mEntryValid) {
                return MapCollections.this.colSetValue(this.mIndex, v);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getKey());
            sb.append("=");
            sb.append(this.getValue());
            return sb.toString();
        }
    }
    
    final class ValuesCollection implements Collection<V>
    {
        @Override
        public boolean add(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            MapCollections.this.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return MapCollections.this.colIndexOfValue(o) >= 0;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            final Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (!this.contains(iterator.next())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean isEmpty() {
            return MapCollections.this.colGetSize() == 0;
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ArrayIterator<V>(1);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfValue = MapCollections.this.colIndexOfValue(o);
            if (colIndexOfValue >= 0) {
                MapCollections.this.colRemoveAt(colIndexOfValue);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            int colGetSize = MapCollections.this.colGetSize();
            boolean b = false;
            for (int i = 0; i < colGetSize; ++i) {
                if (collection.contains(MapCollections.this.colGetEntry(i, 1))) {
                    MapCollections.this.colRemoveAt(i);
                    --i;
                    --colGetSize;
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            int colGetSize = MapCollections.this.colGetSize();
            boolean b = false;
            for (int i = 0; i < colGetSize; ++i) {
                if (!collection.contains(MapCollections.this.colGetEntry(i, 1))) {
                    MapCollections.this.colRemoveAt(i);
                    --i;
                    --colGetSize;
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public int size() {
            return MapCollections.this.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return MapCollections.this.toArrayHelper(1);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return MapCollections.this.toArrayHelper(array, 1);
        }
    }
}
