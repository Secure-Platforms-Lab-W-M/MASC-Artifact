// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.Iterator;
import util.Utils;
import java.io.IOException;
import util.LoggerInterface;
import util.Logger;
import util.ExecutionEnvironment;
import java.util.Hashtable;
import util.LRUCache;
import java.util.Vector;
import util.HugePackedSet;
import util.ObjectPackagingManager;
import java.util.Set;

public class BlockedHosts implements Set
{
    private static Object NOT_NULL;
    private static ObjectPackagingManager PACK_MGR;
    private HugePackedSet blockedHostsHashes;
    private Vector<String[]> blockedPatterns;
    private boolean exclusiveLock;
    private LRUCache filterListCache;
    private Hashtable hostsFilterOverRule;
    private LRUCache okCache;
    private int sharedLocks;
    
    static {
        BlockedHosts.PACK_MGR = new MyPackagingManager();
        BlockedHosts.NOT_NULL = new Object();
    }
    
    public BlockedHosts(int n, int n2, final int n3, final Hashtable hostsFilterOverRule) {
        this.sharedLocks = 0;
        this.exclusiveLock = false;
        this.okCache = new LRUCache(n2);
        this.filterListCache = new LRUCache(n3);
        if (ExecutionEnvironment.getEnvironment().debug()) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("CACHE SIZE:");
            sb.append(n2);
            sb.append(", ");
            sb.append(n3);
            logger.logLine(sb.toString());
        }
        this.hostsFilterOverRule = hostsFilterOverRule;
        n2 = (n /= 6000);
        if (n2 % 2 == 0) {
            n = n2 + 1;
        }
        this.blockedHostsHashes = new HugePackedSet(n, BlockedHosts.PACK_MGR);
    }
    
    private BlockedHosts(final HugePackedSet blockedHostsHashes, final Vector<String[]> blockedPatterns, final int n, final int n2, final Hashtable hostsFilterOverRule) {
        this.sharedLocks = 0;
        this.exclusiveLock = false;
        this.blockedHostsHashes = blockedHostsHashes;
        this.blockedPatterns = blockedPatterns;
        this.okCache = new LRUCache(n);
        this.filterListCache = new LRUCache(n2);
        if (ExecutionEnvironment.getEnvironment().debug()) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("CACHE SIZE:");
            sb.append(n);
            sb.append(", ");
            sb.append(n2);
            logger.logLine(sb.toString());
        }
        this.hostsFilterOverRule = hostsFilterOverRule;
    }
    
    public static boolean checkIndexVersion(final String s) throws IOException {
        return HugePackedSet.checkIndexVersion(s);
    }
    
    private boolean contains(String substring, long longStringHash, final boolean b, final boolean b2) {
        int i = 0;
        while (i != -1) {
            if (this.hostsFilterOverRule != null) {
                final Boolean value = this.hostsFilterOverRule.get(substring);
                if (value != null) {
                    return value;
                }
            }
            if (this.blockedHostsHashes.contains(longStringHash)) {
                return true;
            }
            if (b2 && this.containsPatternMatch(substring)) {
                return true;
            }
            if (b) {
                final int index = substring.indexOf(46);
                if ((i = index) == -1) {
                    continue;
                }
                substring = substring.substring(index + 1);
                longStringHash = Utils.getLongStringHash(substring);
                i = index;
            }
            else {
                i = -1;
            }
        }
        return false;
    }
    
    private boolean containsPatternMatch(final String s) {
        if (this.blockedPatterns == null) {
            return false;
        }
        final Iterator<String[]> iterator = this.blockedPatterns.iterator();
        while (iterator.hasNext()) {
            if (wildCardMatch(iterator.next(), s)) {
                return true;
            }
        }
        return false;
    }
    
    private Vector<String[]> getInitializedPatternStruct() {
        if (this.blockedPatterns == null) {
            this.blockedPatterns = new Vector<String[]>();
        }
        return this.blockedPatterns;
    }
    
    public static BlockedHosts loadPersistedIndex(final String s, final boolean b, final int n, final int n2, final Hashtable hashtable) throws IOException {
        Vector<String[]> vector = null;
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("/blockedpatterns");
        final File file = new File(sb.toString());
        if (file.exists()) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            vector = new Vector<String[]>();
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                vector.addElement(line.trim().split("\\*", -1));
            }
            bufferedReader.close();
        }
        return new BlockedHosts(HugePackedSet.load(s, b, BlockedHosts.PACK_MGR), vector, n, n2, hashtable);
    }
    
    private static boolean wildCardMatch(final String[] array, String substring) {
        for (int i = 0; i < array.length; ++i) {
            final String s = array[i];
            int n;
            if (i < array.length - 1) {
                n = substring.indexOf(s);
            }
            else {
                n = substring.lastIndexOf(s);
            }
            if (i == 0 && !s.equals("") && n != 0) {
                return false;
            }
            if (i == array.length - 1 && !s.equals("") && s.length() + n != substring.length()) {
                return false;
            }
            if (n == -1) {
                return false;
            }
            substring = substring.substring(s.length() + n);
        }
        return true;
    }
    
    @Override
    public boolean add(final Object o) {
        if (((String)o).indexOf("*") == -1) {
            return this.blockedHostsHashes.add(Utils.getLongStringHash(((String)o).toLowerCase()));
        }
        this.getInitializedPatternStruct().addElement(((String)o).trim().toLowerCase().split("\\*", -1));
        return true;
    }
    
    @Override
    public boolean addAll(final Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    @Override
    public void clear() {
        this.blockedHostsHashes.clear();
        this.filterListCache.clear();
        this.okCache.clear();
        this.hostsFilterOverRule = null;
        if (this.blockedPatterns != null) {
            this.blockedPatterns.clear();
        }
    }
    
    public void clearCache(final String s) {
        final long longStringHash = Utils.getLongStringHash(s.toLowerCase());
        this.okCache.remove(longStringHash);
        this.filterListCache.remove(longStringHash);
    }
    
    @Override
    public boolean contains(final Object o) {
        try {
            this.lock(0);
            boolean b = false;
            String s2;
            final String s = s2 = ((String)o).toLowerCase();
            if (s.startsWith("%ip%")) {
                b = true;
                s2 = s.substring(4);
            }
            final long longStringHash = Utils.getLongStringHash(s2);
            if (this.okCache.get(longStringHash) != null) {
                return false;
            }
            if (this.filterListCache.get(longStringHash) != null) {
                return true;
            }
            if (this.contains(s2, longStringHash, !b, !b)) {
                this.filterListCache.put(longStringHash, BlockedHosts.NOT_NULL);
                return true;
            }
            this.okCache.put(longStringHash, BlockedHosts.NOT_NULL);
            return false;
        }
        finally {
            this.unLock(0);
        }
    }
    
    @Override
    public boolean containsAll(final Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    public void finalPrepare() {
        this.blockedHostsHashes.finalPrepare();
    }
    
    public void finalPrepare(final int n) {
        this.blockedHostsHashes.finalPrepare(n);
    }
    
    @Override
    public boolean isEmpty() {
        return this.blockedHostsHashes.isEmpty();
    }
    
    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    public void lock(final int n) {
        // monitorenter(this)
    Label_0079:
        while (true) {
            if (n != 0) {
                if (n == 1) {
                    break Label_0079;
                }
                break Label_0079;
            }
            Label_0064_Outer:Label_0075_Outer:
            while (true) {
                while (true) {
                    try {
                    Label_0019_Outer:
                        while (this.exclusiveLock) {
                            while (true) {
                                try {
                                    this.wait();
                                    continue Label_0019_Outer;
                                }
                                catch (Exception ex) {
                                    continue Label_0064_Outer;
                                }
                                break;
                            }
                            break;
                        }
                        ++this.sharedLocks;
                        // monitorexit(this)
                        return;
                        Label_0056: {
                            this.exclusiveLock = true;
                        }
                        continue Label_0079;
                        while (true) {
                            while (true) {
                                try {
                                    this.wait();
                                    break Label_0079;
                                }
                                catch (InterruptedException ex2) {
                                    continue Label_0075_Outer;
                                }
                                break;
                            }
                        Block_5:
                            while (true) {
                                break Block_5;
                                continue;
                            }
                            continue Label_0075_Outer;
                        }
                    }
                    // monitorexit(this)
                    // iftrue(Label_0056:, !this.exclusiveLock)
                    // iftrue(Label_0064:, this.sharedLocks != 0)
                    finally {}
                    continue;
                }
            }
            break;
        }
    }
    
    protected void migrateTo(final BlockedHosts blockedHosts) {
        this.okCache.clear();
        this.okCache = blockedHosts.okCache;
        this.filterListCache.clear();
        this.filterListCache = blockedHosts.filterListCache;
        this.hostsFilterOverRule = blockedHosts.hostsFilterOverRule;
        this.blockedPatterns = blockedHosts.blockedPatterns;
        this.blockedHostsHashes = blockedHosts.blockedHostsHashes;
    }
    
    public void persist(String string) throws IOException {
        try {
            this.lock(1);
            this.blockedHostsHashes.persist(string);
            if (this.blockedPatterns != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append("/blockedpatterns");
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(sb.toString()));
                for (final String[] array : this.blockedPatterns) {
                    string = array[0];
                    for (int i = 1; i < array.length; ++i) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(string);
                        sb2.append("*");
                        sb2.append(array[i]);
                        string = sb2.toString();
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(string);
                    sb3.append("\n");
                    bufferedOutputStream.write(sb3.toString().getBytes());
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        }
        finally {
            this.unLock(1);
        }
    }
    
    public void prepareInsert(final String s) {
        if (s.indexOf("*") == -1) {
            this.blockedHostsHashes.prepareInsert(Utils.getLongStringHash(s.toLowerCase()));
        }
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    @Override
    public boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    @Override
    public boolean retainAll(final Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    public void setHostsFilterOverRule(final Hashtable hostsFilterOverRule) {
        if (hostsFilterOverRule == null) {
            throw new IllegalArgumentException("Argument null not allowed!");
        }
        this.hostsFilterOverRule = hostsFilterOverRule;
    }
    
    @Override
    public int size() {
        return this.blockedHostsHashes.size();
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    @Override
    public Object[] toArray(final Object[] array) {
        throw new UnsupportedOperationException("Not supported!");
    }
    
    public void unLock(final int n) {
        // monitorenter(this)
        Label_0037: {
            if (n != 0) {
                break Label_0037;
            }
        Label_0061_Outer:
            while (true) {
                while (true) {
                    try {
                        if (this.sharedLocks > 0) {
                            --this.sharedLocks;
                            if (this.sharedLocks == 0) {
                                this.notifyAll();
                            }
                        }
                        // monitorexit(this)
                        // monitorexit(this)
                        while (true) {
                            while (true) {
                                return;
                                this.exclusiveLock = false;
                                this.notifyAll();
                                continue Label_0061_Outer;
                            }
                            continue;
                        }
                    }
                    // iftrue(Label_0065:, n != 1 || !this.exclusiveLock)
                    finally {}
                    continue;
                }
            }
        }
    }
    
    public boolean update(final Object o) throws IOException {
        try {
            this.lock(1);
            if (((String)o).indexOf("*") != -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Wildcard not supported for update:");
                sb.append(o);
                throw new IOException(sb.toString());
            }
            final long longStringHash = Utils.getLongStringHash(((String)o).toLowerCase());
            this.okCache.remove(longStringHash);
            this.filterListCache.remove(longStringHash);
            return this.blockedHostsHashes.add(longStringHash);
        }
        finally {
            this.unLock(1);
        }
    }
    
    public void updatePersist() throws IOException {
        try {
            this.lock(1);
            this.blockedHostsHashes.updatePersist();
        }
        finally {
            this.unLock(1);
        }
    }
    
    private static class MyPackagingManager implements ObjectPackagingManager
    {
        @Override
        public Object bytesToObject(final byte[] array, final int n) {
            return Utils.byteArrayToLong(array, n);
        }
        
        @Override
        public int objectSize() {
            return 8;
        }
        
        @Override
        public void objectToBytes(final Object o, final byte[] array, final int n) {
            Utils.writeLongToByteArray((long)o, array, n);
        }
    }
}
