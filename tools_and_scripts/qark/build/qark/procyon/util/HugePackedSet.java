// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Set;

public class HugePackedSet implements Set
{
    private static final String IDX_VERSION = "1.0";
    private int count;
    private String loadedFromPath;
    private ObjectPackagingManager objMgr;
    private int slotCount;
    private int[] slotSizes;
    private PackedSortedList[] subsets;
    
    public HugePackedSet(int i, final ObjectPackagingManager objMgr) {
        this.count = 0;
        this.slotSizes = null;
        this.subsets = null;
        this.loadedFromPath = null;
        this.objMgr = objMgr;
        this.slotCount = i;
        this.slotSizes = new int[this.slotCount];
        this.subsets = new PackedSortedList[this.slotCount];
        for (i = 0; i < this.slotCount; ++i) {
            this.slotSizes[i] = 0;
        }
    }
    
    private HugePackedSet(final String loadedFromPath, final PackedSortedList[] subsets, final int slotCount, final int count, final ObjectPackagingManager objMgr) {
        this.count = 0;
        this.slotSizes = null;
        this.subsets = null;
        this.loadedFromPath = null;
        this.loadedFromPath = loadedFromPath;
        this.objMgr = objMgr;
        this.slotCount = slotCount;
        this.subsets = subsets;
        this.count = count;
    }
    
    public static boolean checkIndexVersion(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("/IDX_VERSION");
        if (new File(sb.toString()).exists()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("/IDX_VERSION");
            final FileInputStream fileInputStream = new FileInputStream(sb2.toString());
            final byte[] array = new byte[10];
            final int read = fileInputStream.read(array);
            fileInputStream.close();
            return new String(array, 0, read).equals("1.0");
        }
        return false;
    }
    
    public static HugePackedSet load(final String s, final boolean b, final ObjectPackagingManager objectPackagingManager) throws IOException {
        if (!checkIndexVersion(s)) {
            throw new IOException("Incompatible Index Version - Rebuild Index!");
        }
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (true) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("/idx");
            sb.append(n);
            if (!new File(sb.toString()).exists()) {
                break;
            }
            ++n;
        }
        PackedSortedList[] array;
        for (array = new PackedSortedList[n]; i < array.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("/idx");
            sb2.append(i);
            array[i] = PackedSortedList.load(sb2.toString(), b, objectPackagingManager);
            n2 += array[i].size();
        }
        return new HugePackedSet(s, array, n, n2, objectPackagingManager);
    }
    
    @Override
    public boolean add(final Object o) {
        final boolean add = this.subsets[Math.abs(o.hashCode() % this.slotCount)].add(o);
        if (add) {
            ++this.count;
        }
        return add;
    }
    
    @Override
    public boolean addAll(final Collection collection) {
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
    public boolean contains(final Object o) {
        return this.subsets[Math.abs(o.hashCode() % this.slotCount)].contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public void finalPrepare() {
        for (int i = 0; i < this.slotCount; ++i) {
            this.subsets[i] = new PackedSortedList(this.slotSizes[i], this.objMgr);
        }
        this.slotSizes = null;
    }
    
    public void finalPrepare(int i) {
        final int n = (int)(i * 1.2) / this.slotCount;
        for (i = 0; i < this.slotCount; ++i) {
            this.subsets[i] = new PackedSortedList(n, this.objMgr);
        }
        this.slotSizes = null;
    }
    
    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public void persist(final String loadedFromPath) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(loadedFromPath);
        sb.append(".tmp");
        File file = new File(sb.toString());
        final int n = 0;
        for (int i = 0; i < 2; ++i) {
            if (file.exists()) {
                final File[] listFiles = file.listFiles();
                for (int j = 0; j < listFiles.length; ++j) {
                    listFiles[j].delete();
                }
                file.delete();
            }
            file = new File(loadedFromPath);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(loadedFromPath);
        sb2.append(".tmp");
        final File file2 = new File(sb2.toString());
        file2.mkdir();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(file2.getAbsolutePath());
        sb3.append("/IDX_VERSION");
        final FileOutputStream fileOutputStream = new FileOutputStream(sb3.toString());
        fileOutputStream.write("1.0".getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        for (int k = n; k < this.slotCount; ++k) {
            final PackedSortedList list = this.subsets[k];
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(file2.getAbsolutePath());
            sb4.append("/idx");
            sb4.append(k);
            list.persist(sb4.toString());
        }
        file2.renameTo(new File(loadedFromPath));
        this.loadedFromPath = loadedFromPath;
    }
    
    public void prepareInsert(final Object o) {
        final int[] slotSizes = this.slotSizes;
        final int abs = Math.abs(o.hashCode() % this.slotCount);
        ++slotSizes[abs];
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public boolean retainAll(final Collection collection) {
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
    public Object[] toArray(final Object[] array) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    public void updatePersist() throws IOException {
        if (this.loadedFromPath == null) {
            throw new IOException("Can not update non persisted index!");
        }
        final File file = new File(this.loadedFromPath);
        for (int i = 0; i < this.slotCount; ++i) {
            final PackedSortedList list = this.subsets[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(file.getAbsolutePath());
            sb.append("/idx");
            sb.append(i);
            list.persist(sb.toString());
        }
    }
}
