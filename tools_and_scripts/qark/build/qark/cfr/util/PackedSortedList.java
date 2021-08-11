/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import util.ObjectPackagingManager;

public class PackedSortedList
implements List,
RandomAccess {
    private int count = 0;
    private byte[] datapack = null;
    private boolean keepInMemory;
    private boolean loaded = false;
    private ObjectPackagingManager objMgr;
    private int object_size;
    private RandomAccessFile persistedPackData = null;
    private int persistedPackDataRefs = 0;
    private File persistedPackFile;
    private boolean persistentOutdated;

    public PackedSortedList(int n, ObjectPackagingManager objectPackagingManager) {
        this.objMgr = objectPackagingManager;
        this.object_size = objectPackagingManager.objectSize();
        this.datapack = new byte[this.object_size * n];
        this.keepInMemory = true;
        this.loaded = true;
    }

    private PackedSortedList(byte[] arrby, int n, boolean bl, File file, ObjectPackagingManager objectPackagingManager) throws IOException {
        this.objMgr = objectPackagingManager;
        this.object_size = objectPackagingManager.objectSize();
        this.datapack = arrby;
        this.count = n;
        this.keepInMemory = bl;
        this.persistedPackFile = file;
        this.persistentOutdated = false;
        if (bl) {
            this.loadinMemory();
        }
    }

    private void addInternal(int n, Object object) {
        if (!this.loaded) {
            this.loadinMemory();
        }
        byte[] arrby = this.datapack;
        if (this.count >= this.datapack.length / this.object_size) {
            arrby = new byte[this.datapack.length + 1000 * this.object_size];
            System.arraycopy(this.datapack, 0, arrby, 0, this.object_size * n);
        }
        if (n != this.count) {
            System.arraycopy(this.datapack, this.object_size * n, arrby, this.object_size * n + this.object_size, (this.count - n) * this.object_size);
        }
        this.datapack = arrby;
        this.objMgr.objectToBytes(object, this.datapack, this.object_size * n);
        this.persistentOutdated = true;
        ++this.count;
    }

    private void aquireDataPack() throws FileNotFoundException {
        synchronized (this) {
            if (this.persistedPackData == null) {
                if (this.persistedPackDataRefs > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Inconsistent State! persistedPackData is null but there are ");
                    stringBuilder.append(this.persistedPackDataRefs);
                    stringBuilder.append(" references!");
                    throw new IllegalStateException(stringBuilder.toString());
                }
                this.persistedPackData = new RandomAccessFile(this.persistedPackFile, "r");
            }
            ++this.persistedPackDataRefs;
            return;
        }
    }

    private int binarySearch(Object object) {
        return Collections.binarySearch(this, object);
    }

    public static PackedSortedList load(String string2, boolean bl, ObjectPackagingManager object) throws IOException {
        File file = new File(string2);
        int n = (int)file.length();
        if (file.exists() && file.canRead()) {
            return new PackedSortedList(null, n / object.objectSize(), bl, file, (ObjectPackagingManager)object);
        }
        object = new StringBuilder();
        object.append("Cannot read ");
        object.append(string2);
        throw new IOException(object.toString());
    }

    private void loadinMemory() {
        FileInputStream fileInputStream = new FileInputStream(this.persistedPackFile);
        int n = this.count * this.objMgr.objectSize();
        this.datapack = new byte[n];
        int n2 = 0;
        do {
            int n3 = fileInputStream.read(this.datapack, n2, n - n2);
            if (n3 == -1 || n2 == n) break;
            n2 += n3;
            continue;
            break;
        } while (true);
        try {
            fileInputStream.close();
            this.loaded = true;
            return;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    private void releaseDataPack() {
        synchronized (this) {
            try {
                --this.persistedPackDataRefs;
                if (this.persistedPackDataRefs < 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Inconsistent State! persistedPackDataRefs = ");
                    stringBuilder.append(this.persistedPackDataRefs);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                if (this.persistedPackDataRefs == 0) {
                    this.persistedPackData.close();
                    this.persistedPackData = null;
                }
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return;
        }
    }

    public void add(int n, Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Object object) {
        int n = - this.binarySearch(object) + 1;
        if (n < 0) {
            return false;
        }
        this.addInternal(n, object);
        return true;
    }

    public boolean addAll(int n, Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection object) {
        object = object.iterator();
        while (object.hasNext()) {
            this.add(object.next());
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

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public boolean contains(Object object) {
        int n;
        Throwable throwable222;
        if (!this.loaded) {
            this.aquireDataPack();
            n = this.binarySearch(object);
            this.releaseDataPack();
        } else {
            n = this.binarySearch(object);
        }
        if (n <= -1) return false;
        return true;
        {
            catch (Throwable throwable222) {
            }
            catch (Exception exception) {}
            {
                throw new IllegalStateException(exception);
            }
        }
        this.releaseDataPack();
        throw throwable222;
    }

    @Override
    public boolean containsAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public Object get(int n) {
        Throwable throwable2;
        if (n >= this.count) {
            return null;
        }
        n = this.object_size * n;
        if (this.loaded) {
            return this.objMgr.bytesToObject(this.datapack, n);
        }
        this.aquireDataPack();
        byte[] arrby = new byte[this.object_size];
        Object object = this.persistedPackData;
        // MONITORENTER : object
        this.persistedPackData.seek(n);
        this.persistedPackData.readFully(arrby);
        // MONITOREXIT : object
        try {
            object = this.objMgr.bytesToObject(arrby, 0);
            this.releaseDataPack();
            return object;
        }
        catch (Throwable throwable2) {
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
        this.releaseDataPack();
        throw throwable2;
    }

    @Override
    public int indexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        if (this.count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }

    public ListIterator listIterator(int n) {
        throw new UnsupportedOperationException();
    }

    public void persist(String object) throws IOException {
        if (this.persistentOutdated) {
            if (!this.loaded) {
                throw new IOException("PackedSortedList can not be persisted when not in Memory!");
            }
            this.persistedPackFile = new File((String)object);
            object = new FileOutputStream(this.persistedPackFile);
            try {
                object.write(this.datapack, 0, this.count * this.object_size);
                object.flush();
                this.persistentOutdated = false;
            }
            finally {
                object.close();
            }
        }
        if (!this.keepInMemory) {
            this.datapack = null;
            this.loaded = false;
        }
    }

    public Object remove(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public Object set(int n, Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.count;
    }

    public List subList(int n, int n2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray(Object[] arrobject) {
        throw new UnsupportedOperationException();
    }
}

