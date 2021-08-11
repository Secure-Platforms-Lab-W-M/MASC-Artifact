// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import java.io.FileInputStream;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.RandomAccess;
import java.util.List;

public class PackedSortedList implements List, RandomAccess
{
    private int count;
    private byte[] datapack;
    private boolean keepInMemory;
    private boolean loaded;
    private ObjectPackagingManager objMgr;
    private int object_size;
    private RandomAccessFile persistedPackData;
    private int persistedPackDataRefs;
    private File persistedPackFile;
    private boolean persistentOutdated;
    
    public PackedSortedList(final int n, final ObjectPackagingManager objMgr) {
        this.loaded = false;
        this.datapack = null;
        this.count = 0;
        this.persistedPackData = null;
        this.persistedPackDataRefs = 0;
        this.objMgr = objMgr;
        this.object_size = objMgr.objectSize();
        this.datapack = new byte[this.object_size * n];
        this.keepInMemory = true;
        this.loaded = true;
    }
    
    private PackedSortedList(final byte[] datapack, final int count, final boolean keepInMemory, final File persistedPackFile, final ObjectPackagingManager objMgr) throws IOException {
        this.loaded = false;
        this.datapack = null;
        this.count = 0;
        this.persistedPackData = null;
        this.persistedPackDataRefs = 0;
        this.objMgr = objMgr;
        this.object_size = objMgr.objectSize();
        this.datapack = datapack;
        this.count = count;
        this.keepInMemory = keepInMemory;
        this.persistedPackFile = persistedPackFile;
        this.persistentOutdated = false;
        if (keepInMemory) {
            this.loadinMemory();
        }
    }
    
    private void addInternal(final int n, final Object o) {
        if (!this.loaded) {
            this.loadinMemory();
        }
        byte[] datapack = this.datapack;
        if (this.count >= this.datapack.length / this.object_size) {
            datapack = new byte[this.datapack.length + 1000 * this.object_size];
            System.arraycopy(this.datapack, 0, datapack, 0, this.object_size * n);
        }
        if (n != this.count) {
            System.arraycopy(this.datapack, this.object_size * n, datapack, this.object_size * n + this.object_size, (this.count - n) * this.object_size);
        }
        this.datapack = datapack;
        this.objMgr.objectToBytes(o, this.datapack, this.object_size * n);
        this.persistentOutdated = true;
        ++this.count;
    }
    
    private void aquireDataPack() throws FileNotFoundException {
        synchronized (this) {
            if (this.persistedPackData == null) {
                if (this.persistedPackDataRefs > 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Inconsistent State! persistedPackData is null but there are ");
                    sb.append(this.persistedPackDataRefs);
                    sb.append(" references!");
                    throw new IllegalStateException(sb.toString());
                }
                this.persistedPackData = new RandomAccessFile(this.persistedPackFile, "r");
            }
            ++this.persistedPackDataRefs;
        }
    }
    
    private int binarySearch(final Object o) {
        return Collections.binarySearch(this, o);
    }
    
    public static PackedSortedList load(final String s, final boolean b, final ObjectPackagingManager objectPackagingManager) throws IOException {
        final File file = new File(s);
        final int n = (int)file.length();
        if (file.exists() && file.canRead()) {
            return new PackedSortedList(null, n / objectPackagingManager.objectSize(), b, file, objectPackagingManager);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot read ");
        sb.append(s);
        throw new IOException(sb.toString());
    }
    
    private void loadinMemory() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(this.persistedPackFile);
            final int n = this.count * this.objMgr.objectSize();
            this.datapack = new byte[n];
            int n2 = 0;
            while (true) {
                final int read = fileInputStream.read(this.datapack, n2, n - n2);
                if (read == -1 || n2 == n) {
                    break;
                }
                n2 += read;
            }
            fileInputStream.close();
            this.loaded = true;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private void releaseDataPack() {
        synchronized (this) {
            try {
                --this.persistedPackDataRefs;
                if (this.persistedPackDataRefs < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Inconsistent State! persistedPackDataRefs = ");
                    sb.append(this.persistedPackDataRefs);
                    throw new IllegalStateException(sb.toString());
                }
                if (this.persistedPackDataRefs == 0) {
                    this.persistedPackData.close();
                    this.persistedPackData = null;
                }
            }
            catch (IOException ex) {}
        }
    }
    // monitorexit(this)
    
    @Override
    public void add(final int n, final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final Object o) {
        final int n = -(this.binarySearch(o) + 1);
        if (n < 0) {
            return false;
        }
        this.addInternal(n, o);
        return true;
    }
    
    @Override
    public boolean addAll(final int n, final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection collection) {
        final Iterator<Object> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
        return true;
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    public void clearAndReleaseAllMemory() {
        this.count = 0;
        this.datapack = new byte[0];
    }
    
    @Override
    public boolean contains(final Object o) {
        int n;
        if (!this.loaded) {
            try {
                try {
                    this.aquireDataPack();
                    n = this.binarySearch(o);
                    this.releaseDataPack();
                }
                finally {}
            }
            catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            this.releaseDataPack();
        }
        else {
            n = this.binarySearch(o);
        }
        return n > -1;
    }
    
    @Override
    public boolean containsAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object get(int n) {
        if (n >= this.count) {
            return null;
        }
        n *= this.object_size;
        if (this.loaded) {
            return this.objMgr.bytesToObject(this.datapack, n);
        }
        try {
            this.aquireDataPack();
            final byte[] array = new byte[this.object_size];
            Object o = this.persistedPackData;
            synchronized (o) {
                this.persistedPackData.seek(n);
                this.persistedPackData.readFully(array);
                // monitorexit(o)
                o = this.objMgr.bytesToObject(array, 0);
                this.releaseDataPack();
                return o;
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        this.releaseDataPack();
    }
    
    @Override
    public int indexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator listIterator(final int n) {
        throw new UnsupportedOperationException();
    }
    
    public void persist(String s) throws IOException {
        if (this.persistentOutdated) {
            if (!this.loaded) {
                throw new IOException("PackedSortedList can not be persisted when not in Memory!");
            }
            this.persistedPackFile = new File(s);
            s = (String)new FileOutputStream(this.persistedPackFile);
            try {
                ((FileOutputStream)s).write(this.datapack, 0, this.count * this.object_size);
                ((OutputStream)s).flush();
                ((FileOutputStream)s).close();
                this.persistentOutdated = false;
            }
            finally {
                ((FileOutputStream)s).close();
            }
        }
        if (!this.keepInMemory) {
            this.datapack = null;
            this.loaded = false;
        }
    }
    
    @Override
    public Object remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object set(final int n, final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.count;
    }
    
    @Override
    public List subList(final int n, final int n2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray(final Object[] array) {
        throw new UnsupportedOperationException();
    }
}
