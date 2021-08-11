// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Arrays;
import java.io.ObjectStreamException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;
import java.io.Serializable;
import java.util.AbstractMap;

public final class LinkedHashTreeMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    private static final Comparator<Comparable> NATURAL_ORDER;
    Comparator<? super K> comparator;
    private EntrySet entrySet;
    final Node<K, V> header;
    private KeySet keySet;
    int modCount;
    int size;
    Node<K, V>[] table;
    int threshold;
    
    static {
        NATURAL_ORDER = new Comparator<Comparable>() {
            @Override
            public int compare(final Comparable comparable, final Comparable comparable2) {
                return comparable.compareTo(comparable2);
            }
        };
    }
    
    public LinkedHashTreeMap() {
        this((Comparator)LinkedHashTreeMap.NATURAL_ORDER);
    }
    
    public LinkedHashTreeMap(Comparator<? super K> natural_ORDER) {
        this.size = 0;
        this.modCount = 0;
        if (natural_ORDER == null) {
            natural_ORDER = LinkedHashTreeMap.NATURAL_ORDER;
        }
        this.comparator = (Comparator<? super K>)natural_ORDER;
        this.header = new Node<K, V>();
        this.table = (Node<K, V>[])new Node[16];
        this.threshold = this.table.length / 2 + this.table.length / 4;
    }
    
    private void doubleCapacity() {
        this.table = doubleCapacity(this.table);
        this.threshold = this.table.length / 2 + this.table.length / 4;
    }
    
    static <K, V> Node<K, V>[] doubleCapacity(final Node<K, V>[] array) {
        final int length = array.length;
        final Node[] array2 = new Node[length * 2];
        final AvlIterator<K, V> avlIterator = new AvlIterator<K, V>();
        final AvlBuilder<K, V> avlBuilder = new AvlBuilder<K, V>();
        final AvlBuilder<K, V> avlBuilder2 = new AvlBuilder<K, V>();
        for (int i = 0; i < length; ++i) {
            final Node<K, V> node = array[i];
            if (node != null) {
                avlIterator.reset(node);
                int n = 0;
                int n2 = 0;
                while (true) {
                    final Node<K, V> next = avlIterator.next();
                    if (next == null) {
                        break;
                    }
                    if ((next.hash & length) == 0x0) {
                        ++n;
                    }
                    else {
                        ++n2;
                    }
                }
                avlBuilder.reset(n);
                avlBuilder2.reset(n2);
                avlIterator.reset(node);
                while (true) {
                    final Node<K, V> next2 = avlIterator.next();
                    if (next2 == null) {
                        break;
                    }
                    if ((next2.hash & length) == 0x0) {
                        avlBuilder.add(next2);
                    }
                    else {
                        avlBuilder2.add(next2);
                    }
                }
                Entry<K, V> root;
                if (n > 0) {
                    root = (Entry<K, V>)avlBuilder.root();
                }
                else {
                    root = null;
                }
                array2[i] = (Node)root;
                Entry<K, V> root2;
                if (n2 > 0) {
                    root2 = (Entry<K, V>)avlBuilder2.root();
                }
                else {
                    root2 = null;
                }
                array2[i + length] = (Node)root2;
            }
        }
        return (Node<K, V>[])array2;
    }
    
    private boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    private void rebalance(Node<K, V> parent, final boolean b) {
        while (parent != null) {
            final Node<K, V> left = parent.left;
            final Node<K, V> right = parent.right;
            int height;
            if (left != null) {
                height = left.height;
            }
            else {
                height = 0;
            }
            int height2;
            if (right != null) {
                height2 = right.height;
            }
            else {
                height2 = 0;
            }
            final int n = height - height2;
            if (n == -2) {
                final Node<K, V> left2 = right.left;
                final Node<K, V> right2 = right.right;
                int height3;
                if (right2 != null) {
                    height3 = right2.height;
                }
                else {
                    height3 = 0;
                }
                int height4;
                if (left2 != null) {
                    height4 = left2.height;
                }
                else {
                    height4 = 0;
                }
                final int n2 = height4 - height3;
                if (n2 == -1 || (n2 == 0 && !b)) {
                    this.rotateLeft(parent);
                }
                else {
                    assert n2 == 1;
                    this.rotateRight(right);
                    this.rotateLeft(parent);
                }
                if (b) {
                    break;
                }
            }
            else if (n == 2) {
                final Node<K, V> left3 = left.left;
                final Node<K, V> right3 = left.right;
                int height5;
                if (right3 != null) {
                    height5 = right3.height;
                }
                else {
                    height5 = 0;
                }
                int height6;
                if (left3 != null) {
                    height6 = left3.height;
                }
                else {
                    height6 = 0;
                }
                final int n3 = height6 - height5;
                if (n3 == 1 || (n3 == 0 && !b)) {
                    this.rotateRight(parent);
                }
                else {
                    assert n3 == -1;
                    this.rotateLeft(left);
                    this.rotateRight(parent);
                }
                if (b) {
                    break;
                }
            }
            else if (n == 0) {
                parent.height = height + 1;
                if (b) {
                    return;
                }
            }
            else {
                assert n == 1;
                parent.height = Math.max(height, height2) + 1;
                if (!b) {
                    return;
                }
            }
            parent = parent.parent;
        }
    }
    
    private void replaceInParent(final Node<K, V> node, final Node<K, V> node2) {
        final Node<K, V> parent = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = parent;
        }
        if (parent == null) {
            this.table[node.hash & this.table.length - 1] = node2;
            return;
        }
        if (parent.left == node) {
            parent.left = node2;
            return;
        }
        assert parent.right == node;
        parent.right = node2;
    }
    
    private void rotateLeft(final Node<K, V> node) {
        final int n = 0;
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> left2 = right.left;
        final Node<K, V> right2 = right.right;
        node.right = left2;
        if (left2 != null) {
            left2.parent = node;
        }
        this.replaceInParent(node, right);
        right.left = node;
        node.parent = right;
        int height;
        if (left != null) {
            height = left.height;
        }
        else {
            height = 0;
        }
        int height2;
        if (left2 != null) {
            height2 = left2.height;
        }
        else {
            height2 = 0;
        }
        node.height = Math.max(height, height2) + 1;
        final int height3 = node.height;
        int height4 = n;
        if (right2 != null) {
            height4 = right2.height;
        }
        right.height = Math.max(height3, height4) + 1;
    }
    
    private void rotateRight(final Node<K, V> node) {
        final int n = 0;
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> left2 = left.left;
        final Node<K, V> right2 = left.right;
        node.left = right2;
        if (right2 != null) {
            right2.parent = node;
        }
        this.replaceInParent(node, left);
        left.right = node;
        node.parent = left;
        int height;
        if (right != null) {
            height = right.height;
        }
        else {
            height = 0;
        }
        int height2;
        if (right2 != null) {
            height2 = right2.height;
        }
        else {
            height2 = 0;
        }
        node.height = Math.max(height, height2) + 1;
        final int height3 = node.height;
        int height4 = n;
        if (left2 != null) {
            height4 = left2.height;
        }
        left.height = Math.max(height3, height4) + 1;
    }
    
    private static int secondaryHash(int n) {
        n ^= (n >>> 20 ^ n >>> 12);
        return n >>> 7 ^ n ^ n >>> 4;
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
        ++this.modCount;
        final Node<K, V> header = this.header;
        Node<K, V> next2;
        for (Node<K, V> next = header.next; next != header; next = next2) {
            next2 = next.next;
            next.prev = null;
            next.next = null;
        }
        header.prev = header;
        header.next = header;
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.findByObject(o) != null;
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        final EntrySet entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        return this.entrySet = new EntrySet();
    }
    
    Node<K, V> find(final K k, final boolean b) {
        final Node<K, V> node = null;
        final Comparator<? super K> comparator = this.comparator;
        final Node<K, V>[] table = this.table;
        final int secondaryHash = secondaryHash(k.hashCode());
        final int n = secondaryHash & table.length - 1;
        Object o = table[n];
        int n2 = 0;
        Object o2 = o;
        if (o != null) {
            Comparable<K> comparable;
            if (comparator == LinkedHashTreeMap.NATURAL_ORDER) {
                comparable = (Comparable<K>)k;
            }
            else {
                comparable = null;
            }
            while (true) {
                if (comparable != null) {
                    n2 = comparable.compareTo(((Node)o).key);
                }
                else {
                    n2 = comparator.compare((Object)k, (Object)((Node)o).key);
                }
                if (n2 == 0) {
                    return (Node<K, V>)o;
                }
                Entry<K, V> entry;
                if (n2 < 0) {
                    entry = ((Node)o).left;
                }
                else {
                    entry = ((Node)o).right;
                }
                if (entry == null) {
                    break;
                }
                o = entry;
            }
            o2 = o;
        }
        Label_0134: {
            break Label_0134;
        }
        o = node;
        if (b) {
            final Node<K, V> header = this.header;
            Node node2;
            if (o2 == null) {
                if (comparator == LinkedHashTreeMap.NATURAL_ORDER && !(k instanceof Comparable)) {
                    throw new ClassCastException(k.getClass().getName() + " is not Comparable");
                }
                node2 = new Node<K, V>((Node<Object, Object>)o2, k, secondaryHash, (Node<Object, Object>)header, (Node<Object, Object>)header.prev);
                table[n] = (Node<K, V>)node2;
            }
            else {
                node2 = new Node<K, V>((Node<K, V>)o2, k, secondaryHash, header, header.prev);
                if (n2 < 0) {
                    ((Node)o2).left = node2;
                }
                else {
                    ((Node)o2).right = node2;
                }
                this.rebalance((Node<K, V>)o2, true);
            }
            if (this.size++ > this.threshold) {
                this.doubleCapacity();
            }
            ++this.modCount;
            return (Node<K, V>)node2;
        }
        return (Node<K, V>)o;
    }
    
    Node<K, V> findByEntry(final Entry<?, ?> entry) {
        final Node<K, V> byObject = this.findByObject(entry.getKey());
        int n;
        if (byObject != null && this.equal(byObject.value, entry.getValue())) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            return byObject;
        }
        return null;
    }
    
    Node<K, V> findByObject(final Object o) {
        Node<Object, V> find = null;
        if (o == null) {
            return (Node<K, V>)find;
        }
        try {
            find = this.find(o, false);
            return (Node<K, V>)find;
        }
        catch (ClassCastException ex) {
            return null;
        }
    }
    
    @Override
    public V get(final Object o) {
        final Node<K, V> byObject = this.findByObject(o);
        if (byObject != null) {
            return byObject.value;
        }
        return null;
    }
    
    @Override
    public Set<K> keySet() {
        final KeySet keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        return this.keySet = new KeySet();
    }
    
    @Override
    public V put(final K k, final V value) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        final Node<K, V> find = this.find(k, true);
        final V value2 = find.value;
        find.value = value;
        return value2;
    }
    
    @Override
    public V remove(final Object o) {
        final Node<K, V> removeInternalByKey = this.removeInternalByKey(o);
        if (removeInternalByKey != null) {
            return removeInternalByKey.value;
        }
        return null;
    }
    
    void removeInternal(final Node<K, V> node, final boolean b) {
        if (b) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> parent = node.parent;
        if (left != null && right != null) {
            Node<K, V> node2;
            if (left.height > right.height) {
                node2 = left.last();
            }
            else {
                node2 = right.first();
            }
            this.removeInternal(node2, false);
            int height = 0;
            final Node<K, V> left2 = node.left;
            if (left2 != null) {
                height = left2.height;
                node2.left = left2;
                left2.parent = node2;
                node.left = null;
            }
            int height2 = 0;
            final Node<K, V> right2 = node.right;
            if (right2 != null) {
                height2 = right2.height;
                node2.right = right2;
                right2.parent = node2;
                node.right = null;
            }
            node2.height = Math.max(height, height2) + 1;
            this.replaceInParent(node, node2);
            return;
        }
        if (left != null) {
            this.replaceInParent(node, left);
            node.left = null;
        }
        else if (right != null) {
            this.replaceInParent(node, right);
            node.right = null;
        }
        else {
            this.replaceInParent(node, null);
        }
        this.rebalance(parent, false);
        --this.size;
        ++this.modCount;
    }
    
    Node<K, V> removeInternalByKey(final Object o) {
        final Node<K, V> byObject = this.findByObject(o);
        if (byObject != null) {
            this.removeInternal(byObject, true);
        }
        return byObject;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    static final class AvlBuilder<K, V>
    {
        private int leavesSkipped;
        private int leavesToSkip;
        private int size;
        private Node<K, V> stack;
        
        void add(final Node<K, V> stack) {
            stack.right = null;
            stack.parent = null;
            stack.left = null;
            stack.height = 1;
            if (this.leavesToSkip > 0 && (this.size & 0x1) == 0x0) {
                ++this.size;
                --this.leavesToSkip;
                ++this.leavesSkipped;
            }
            stack.parent = this.stack;
            this.stack = stack;
            ++this.size;
            if (this.leavesToSkip > 0 && (this.size & 0x1) == 0x0) {
                ++this.size;
                --this.leavesToSkip;
                ++this.leavesSkipped;
            }
            for (int n = 4; (this.size & n - 1) == n - 1; n *= 2) {
                if (this.leavesSkipped == 0) {
                    final Node<K, V> stack2 = this.stack;
                    final Node<K, V> parent = stack2.parent;
                    final Node<K, V> parent2 = (Node<K, V>)parent.parent;
                    parent.parent = (Node<K, V>)parent2.parent;
                    this.stack = parent;
                    parent.left = (Node<K, V>)parent2;
                    parent.right = stack2;
                    parent.height = stack2.height + 1;
                    parent2.parent = (Node<K, V>)parent;
                    stack2.parent = parent;
                }
                else if (this.leavesSkipped == 1) {
                    final Node<K, V> stack3 = this.stack;
                    final Node<K, V> parent3 = stack3.parent;
                    this.stack = parent3;
                    parent3.right = stack3;
                    parent3.height = stack3.height + 1;
                    stack3.parent = parent3;
                    this.leavesSkipped = 0;
                }
                else if (this.leavesSkipped == 2) {
                    this.leavesSkipped = 0;
                }
            }
        }
        
        void reset(final int n) {
            this.leavesToSkip = Integer.highestOneBit(n) * 2 - 1 - n;
            this.size = 0;
            this.leavesSkipped = 0;
            this.stack = null;
        }
        
        Node<K, V> root() {
            final Node<K, V> stack = this.stack;
            if (stack.parent != null) {
                throw new IllegalStateException();
            }
            return stack;
        }
    }
    
    static class AvlIterator<K, V>
    {
        private Node<K, V> stackTop;
        
        public Node<K, V> next() {
            final Node<K, V> stackTop = this.stackTop;
            if (stackTop == null) {
                return null;
            }
            Node<K, V> parent = stackTop.parent;
            stackTop.parent = null;
            for (Node<K, V> node = stackTop.right; node != null; node = node.left) {
                node.parent = parent;
                parent = node;
            }
            this.stackTop = parent;
            return stackTop;
        }
        
        void reset(Node<K, V> left) {
            Node<K, V> node = null;
            while (left != null) {
                left.parent = node;
                node = left;
                left = left.left;
            }
            this.stackTop = node;
        }
    }
    
    final class EntrySet extends AbstractSet<Entry<K, V>>
    {
        @Override
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return o instanceof Entry && LinkedHashTreeMap.this.findByEntry((Entry<?, ?>)o) != null;
        }
        
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator<Entry<K, V>>() {
                @Override
                public Entry<K, V> next() {
                    return ((LinkedTreeMapIterator)this).nextNode();
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            if (o instanceof Entry) {
                final Node<K, V> byEntry = LinkedHashTreeMap.this.findByEntry((Entry<?, ?>)o);
                if (byEntry != null) {
                    LinkedHashTreeMap.this.removeInternal(byEntry, true);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return LinkedHashTreeMap.this.size;
        }
    }
    
    final class KeySet extends AbstractSet<K>
    {
        @Override
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return LinkedHashTreeMap.this.containsKey(o);
        }
        
        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMapIterator<K>() {
                @Override
                public K next() {
                    return ((LinkedTreeMapIterator)this).nextNode().key;
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            return LinkedHashTreeMap.this.removeInternalByKey(o) != null;
        }
        
        @Override
        public int size() {
            return LinkedHashTreeMap.this.size;
        }
    }
    
    private abstract class LinkedTreeMapIterator<T> implements Iterator<T>
    {
        int expectedModCount;
        Node<K, V> lastReturned;
        Node<K, V> next;
        
        LinkedTreeMapIterator() {
            this.next = LinkedHashTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }
        
        @Override
        public final boolean hasNext() {
            return this.next != LinkedHashTreeMap.this.header;
        }
        
        final Node<K, V> nextNode() {
            final Node<K, V> next = this.next;
            if (next == LinkedHashTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedHashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = next.next;
            return this.lastReturned = next;
        }
        
        @Override
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedHashTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }
    }
    
    static final class Node<K, V> implements Entry<K, V>
    {
        final int hash;
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;
        
        Node() {
            this.key = null;
            this.hash = -1;
            this.prev = this;
            this.next = this;
        }
        
        Node(final Node<K, V> parent, final K key, final int hash, final Node<K, V> next, final Node<K, V> prev) {
            this.parent = parent;
            this.key = key;
            this.hash = hash;
            this.height = 1;
            this.next = next;
            this.prev = prev;
            prev.next = this;
            next.prev = this;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                if (this.key == null) {
                    b2 = b;
                    if (entry.getKey() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.key.equals(entry.getKey())) {
                        return b2;
                    }
                }
                if (this.value == null) {
                    b2 = b;
                    if (entry.getValue() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.value.equals(entry.getValue())) {
                        return b2;
                    }
                }
                b2 = true;
            }
            return b2;
        }
        
        public Node<K, V> first() {
            Node node = this;
            for (Node<K, V> node2 = node.left; node2 != null; node2 = node.left) {
                node = node2;
            }
            return (Node<K, V>)node;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int hashCode2;
            if (this.key == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.key.hashCode();
            }
            if (this.value != null) {
                hashCode = this.value.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        public Node<K, V> last() {
            Node node = this;
            for (Node<K, V> node2 = node.right; node2 != null; node2 = node.right) {
                node = node2;
            }
            return (Node<K, V>)node;
        }
        
        @Override
        public V setValue(final V value) {
            final V value2 = this.value;
            this.value = value;
            return value2;
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
