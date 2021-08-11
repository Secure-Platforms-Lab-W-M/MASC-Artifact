/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import util.ObjectPackagingManager;
import util.PackedSortedList;

public class HugePackedSet
implements Set {
    private static final String IDX_VERSION = "1.0";
    private int count = 0;
    private String loadedFromPath = null;
    private ObjectPackagingManager objMgr;
    private int slotCount;
    private int[] slotSizes = null;
    private PackedSortedList[] subsets = null;

    public HugePackedSet(int n, ObjectPackagingManager objectPackagingManager) {
        this.objMgr = objectPackagingManager;
        this.slotCount = n;
        this.slotSizes = new int[this.slotCount];
        this.subsets = new PackedSortedList[this.slotCount];
        for (n = 0; n < this.slotCount; ++n) {
            this.slotSizes[n] = 0;
        }
    }

    private HugePackedSet(String string2, PackedSortedList[] arrpackedSortedList, int n, int n2, ObjectPackagingManager objectPackagingManager) {
        this.loadedFromPath = string2;
        this.objMgr = objectPackagingManager;
        this.slotCount = n;
        this.subsets = arrpackedSortedList;
        this.count = n2;
    }

    public static boolean checkIndexVersion(String object) throws IOException {
        byte[] arrby = new byte[]();
        arrby.append((String)object);
        arrby.append("/IDX_VERSION");
        if (new File(arrby.toString()).exists()) {
            arrby = new StringBuilder();
            arrby.append((String)object);
            arrby.append("/IDX_VERSION");
            object = new FileInputStream(arrby.toString());
            arrby = new byte[10];
            int n = object.read(arrby);
            object.close();
            return new String(arrby, 0, n).equals("1.0");
        }
        return false;
    }

    public static HugePackedSet load(String string2, boolean bl, ObjectPackagingManager objectPackagingManager) throws IOException {
        PackedSortedList[] arrpackedSortedList;
        if (!HugePackedSet.checkIndexVersion(string2)) {
            throw new IOException("Incompatible Index Version - Rebuild Index!");
        }
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        do {
            arrpackedSortedList = new PackedSortedList[]();
            arrpackedSortedList.append(string2);
            arrpackedSortedList.append("/idx");
            arrpackedSortedList.append(n2);
            if (!new File(arrpackedSortedList.toString()).exists()) break;
            ++n2;
        } while (true);
        arrpackedSortedList = new PackedSortedList[n2];
        while (n < arrpackedSortedList.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("/idx");
            stringBuilder.append(n);
            arrpackedSortedList[n] = PackedSortedList.load(stringBuilder.toString(), bl, objectPackagingManager);
            n3 += arrpackedSortedList[n].size();
            ++n;
        }
        return new HugePackedSet(string2, arrpackedSortedList, n2, n3, objectPackagingManager);
    }

    @Override
    public boolean add(Object object) {
        boolean bl = this.subsets[Math.abs(object.hashCode() % this.slotCount)].add(object);
        if (bl) {
            ++this.count;
        }
        return bl;
    }

    @Override
    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void clear() {
        this.slotSizes = new int[this.slotCount];
        for (int i = 0; i < this.slotCount; ++i) {
            this.subsets[i].clearAndReleaseAllMemory();
            this.slotSizes[i] = 0;
        }
    }

    @Override
    public boolean contains(Object object) {
        return this.subsets[Math.abs(object.hashCode() % this.slotCount)].contains(object);
    }

    @Override
    public boolean containsAll(Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void finalPrepare() {
        for (int i = 0; i < this.slotCount; ++i) {
            this.subsets[i] = new PackedSortedList(this.slotSizes[i], this.objMgr);
        }
        this.slotSizes = null;
    }

    public void finalPrepare(int n) {
        int n2 = (int)((double)n * 1.2) / this.slotCount;
        for (n = 0; n < this.slotCount; ++n) {
            this.subsets[n] = new PackedSortedList(n2, this.objMgr);
        }
        this.slotSizes = null;
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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void persist(String string2) throws IOException {
        int n;
        Object object;
        Serializable serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append(".tmp");
        serializable = new File(serializable.toString());
        int n2 = 0;
        for (n = 0; n < 2; ++n) {
            if (serializable.exists()) {
                object = serializable.listFiles();
                for (int i = 0; i < object.length; ++i) {
                    object[i].delete();
                }
                serializable.delete();
            }
            serializable = new File(string2);
        }
        serializable = new StringBuilder();
        serializable.append(string2);
        serializable.append(".tmp");
        serializable = new File(serializable.toString());
        serializable.mkdir();
        object = new StringBuilder();
        object.append(serializable.getAbsolutePath());
        object.append("/IDX_VERSION");
        object = new FileOutputStream(object.toString());
        object.write("1.0".getBytes());
        object.flush();
        object.close();
        for (n = n2; n < this.slotCount; ++n) {
            object = this.subsets[n];
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(serializable.getAbsolutePath());
            stringBuilder.append("/idx");
            stringBuilder.append(n);
            object.persist(stringBuilder.toString());
        }
        serializable.renameTo(new File(string2));
        this.loadedFromPath = string2;
    }

    public void prepareInsert(Object object) {
        int[] arrn = this.slotSizes;
        int n = Math.abs(object.hashCode() % this.slotCount);
        arrn[n] = arrn[n] + 1;
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Object[] toArray(Object[] arrobject) {
        throw new UnsupportedOperationException("Not supported!");
    }

    public void updatePersist() throws IOException {
        if (this.loadedFromPath == null) {
            throw new IOException("Can not update non persisted index!");
        }
        File file = new File(this.loadedFromPath);
        for (int i = 0; i < this.slotCount; ++i) {
            PackedSortedList packedSortedList = this.subsets[i];
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append("/idx");
            stringBuilder.append(i);
            packedSortedList.persist(stringBuilder.toString());
        }
    }
}

