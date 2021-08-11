/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import util.ExecutionEnvironment;
import util.HugePackedSet;
import util.LRUCache;
import util.Logger;
import util.LoggerInterface;
import util.ObjectPackagingManager;
import util.Utils;

public class BlockedHosts
implements Set {
    private static Object NOT_NULL;
    private static ObjectPackagingManager PACK_MGR;
    private HugePackedSet blockedHostsHashes;
    private Vector<String[]> blockedPatterns;
    private boolean exclusiveLock = false;
    private LRUCache filterListCache;
    private Hashtable hostsFilterOverRule;
    private LRUCache okCache;
    private int sharedLocks = 0;

    static {
        PACK_MGR = new MyPackagingManager();
        NOT_NULL = new Object();
    }

    public BlockedHosts(int n, int n2, int n3, Hashtable hashtable) {
        this.okCache = new LRUCache(n2);
        this.filterListCache = new LRUCache(n3);
        if (ExecutionEnvironment.getEnvironment().debug()) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CACHE SIZE:");
            stringBuilder.append(n2);
            stringBuilder.append(", ");
            stringBuilder.append(n3);
            loggerInterface.logLine(stringBuilder.toString());
        }
        this.hostsFilterOverRule = hashtable;
        n = n2 = n / 6000;
        if (n2 % 2 == 0) {
            n = n2 + 1;
        }
        this.blockedHostsHashes = new HugePackedSet(n, PACK_MGR);
    }

    private BlockedHosts(HugePackedSet object, Vector<String[]> serializable, int n, int n2, Hashtable hashtable) {
        this.blockedHostsHashes = object;
        this.blockedPatterns = serializable;
        this.okCache = new LRUCache(n);
        this.filterListCache = new LRUCache(n2);
        if (ExecutionEnvironment.getEnvironment().debug()) {
            object = Logger.getLogger();
            serializable = new StringBuilder();
            serializable.append("CACHE SIZE:");
            serializable.append(n);
            serializable.append(", ");
            serializable.append(n2);
            object.logLine(serializable.toString());
        }
        this.hostsFilterOverRule = hashtable;
    }

    public static boolean checkIndexVersion(String string2) throws IOException {
        return HugePackedSet.checkIndexVersion(string2);
    }

    private boolean contains(String string2, long l, boolean bl, boolean bl2) {
        int n = 0;
        while (n != -1) {
            Object v;
            if (this.hostsFilterOverRule != null && (v = this.hostsFilterOverRule.get(string2)) != null) {
                return (Boolean)v;
            }
            if (this.blockedHostsHashes.contains(l)) {
                return true;
            }
            if (bl2 && this.containsPatternMatch(string2)) {
                return true;
            }
            if (bl) {
                int n2;
                n = n2 = string2.indexOf(46);
                if (n2 == -1) continue;
                string2 = string2.substring(n2 + 1);
                l = Utils.getLongStringHash(string2);
                n = n2;
                continue;
            }
            n = -1;
        }
        return false;
    }

    private boolean containsPatternMatch(String string2) {
        if (this.blockedPatterns == null) {
            return false;
        }
        Iterator<String[]> iterator = this.blockedPatterns.iterator();
        while (iterator.hasNext()) {
            if (!BlockedHosts.wildCardMatch(iterator.next(), string2)) continue;
            return true;
        }
        return false;
    }

    private Vector<String[]> getInitializedPatternStruct() {
        if (this.blockedPatterns == null) {
            this.blockedPatterns = new Vector();
        }
        return this.blockedPatterns;
    }

    public static BlockedHosts loadPersistedIndex(String string2, boolean bl, int n, int n2, Hashtable hashtable) throws IOException {
        Vector<String[]> vector = null;
        Object object = new StringBuilder();
        object.append(string2);
        object.append("/blockedpatterns");
        object = new File(object.toString());
        if (object.exists()) {
            String string3;
            object = new BufferedReader(new InputStreamReader(new FileInputStream((File)object)));
            vector = new Vector<String[]>();
            while ((string3 = object.readLine()) != null) {
                vector.addElement(string3.trim().split("\\*", -1));
            }
            object.close();
        }
        return new BlockedHosts(HugePackedSet.load(string2, bl, PACK_MGR), vector, n, n2, hashtable);
    }

    private static boolean wildCardMatch(String[] arrstring, String string2) {
        for (int i = 0; i < arrstring.length; ++i) {
            String string3 = arrstring[i];
            int n = i < arrstring.length - 1 ? string2.indexOf(string3) : string2.lastIndexOf(string3);
            if (i == 0 && !string3.equals("") && n != 0) {
                return false;
            }
            if (i == arrstring.length - 1 && !string3.equals("") && string3.length() + n != string2.length()) {
                return false;
            }
            if (n == -1) {
                return false;
            }
            string2 = string2.substring(string3.length() + n);
        }
        return true;
    }

    @Override
    public boolean add(Object object) {
        if (((String)object).indexOf("*") == -1) {
            return this.blockedHostsHashes.add(Utils.getLongStringHash(((String)object).toLowerCase()));
        }
        this.getInitializedPatternStruct().addElement(((String)object).trim().toLowerCase().split("\\*", -1));
        return true;
    }

    @Override
    public boolean addAll(Collection collection) {
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

    public void clearCache(String string2) {
        long l = Utils.getLongStringHash(string2.toLowerCase());
        this.okCache.remove(l);
        this.filterListCache.remove(l);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean contains(Object object) {
        try {
            Object object2;
            long l;
            this.lock(0);
            boolean bl = false;
            object = object2 = ((String)object).toLowerCase();
            if (object2.startsWith("%ip%")) {
                bl = true;
                object = object2.substring(4);
            }
            if ((object2 = this.okCache.get(l = Utils.getLongStringHash((String)object))) != null) {
                this.unLock(0);
                return false;
            }
            object2 = this.filterListCache.get(l);
            if (object2 != null) {
                this.unLock(0);
                return true;
            }
            boolean bl2 = !bl;
            boolean bl3 = !bl;
            if (this.contains((String)object, l, bl2, bl3)) {
                this.filterListCache.put(l, NOT_NULL);
                return true;
            }
            this.okCache.put(l, NOT_NULL);
            return false;
        }
        catch (Throwable throwable) {}
        throw throwable;
    }

    @Override
    public boolean containsAll(Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }

    public void finalPrepare() {
        this.blockedHostsHashes.finalPrepare();
    }

    public void finalPrepare(int n) {
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

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public void lock(int var1_1) {
        synchronized (this) {
            block13 : {
                block12 : {
                    if (var1_1 != 0) break block13;
                    try {
                        while (var2_2 = this.exclusiveLock) {
                            try {
                                this.wait();
                            }
                            catch (Exception var3_3) {}
                        }
                        ++this.sharedLocks;
                        break block12;
lbl12: // 3 sources:
                        if (this.sharedLocks == 0 && !this.exclusiveLock) {
                            this.exclusiveLock = true;
                            break block12;
                        }
                        try {
                            this.wait();
                        }
                        catch (InterruptedException var3_4) {}
                        ** GOTO lbl12
                    }
                    catch (Throwable var3_5) {
                        ** continue;
                    }
lbl20: // 1 sources:
                    do {
                        throw var3_5;
                        break;
                    } while (true);
                }
                do {
                    return;
                    break;
                } while (true);
            }
            if (var1_1 != 1) ** continue;
            ** GOTO lbl12
        }
    }

    protected void migrateTo(BlockedHosts blockedHosts) {
        this.okCache.clear();
        this.okCache = blockedHosts.okCache;
        this.filterListCache.clear();
        this.filterListCache = blockedHosts.filterListCache;
        this.hostsFilterOverRule = blockedHosts.hostsFilterOverRule;
        this.blockedPatterns = blockedHosts.blockedPatterns;
        this.blockedHostsHashes = blockedHosts.blockedHostsHashes;
    }

    /*
     * Exception decompiling
     */
    public void persist(String var1_1) throws IOException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 4[UNCONDITIONALDOLOOP]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public void prepareInsert(String string2) {
        if (string2.indexOf("*") == -1) {
            this.blockedHostsHashes.prepareInsert(Utils.getLongStringHash(string2.toLowerCase()));
        }
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("Not supported!");
    }

    public void setHostsFilterOverRule(Hashtable hashtable) {
        if (hashtable == null) {
            throw new IllegalArgumentException("Argument null not allowed!");
        }
        this.hostsFilterOverRule = hashtable;
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
    public Object[] toArray(Object[] arrobject) {
        throw new UnsupportedOperationException("Not supported!");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void unLock(int var1_1) {
        block5 : {
            // MONITORENTER : this
            if (var1_1 != 0) ** GOTO lbl9
            if (this.sharedLocks <= 0) return;
            {
                --this.sharedLocks;
                if (this.sharedLocks != 0) return;
                {
                    this.notifyAll();
                    return;
                }
            }
lbl9: // 1 sources:
            if (var1_1 != 1 || !this.exclusiveLock) break block5;
            this.exclusiveLock = false;
            this.notifyAll();
            return;
        }
        // MONITOREXIT : this
        return;
    }

    public boolean update(Object object) throws IOException {
        try {
            this.lock(1);
            if (((String)object).indexOf("*") != -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Wildcard not supported for update:");
                stringBuilder.append(object);
                throw new IOException(stringBuilder.toString());
            }
            long l = Utils.getLongStringHash(((String)object).toLowerCase());
            this.okCache.remove(l);
            this.filterListCache.remove(l);
            boolean bl = this.blockedHostsHashes.add(l);
            return bl;
        }
        finally {
            this.unLock(1);
        }
    }

    public void updatePersist() throws IOException {
        try {
            this.lock(1);
            this.blockedHostsHashes.updatePersist();
            return;
        }
        finally {
            this.unLock(1);
        }
    }

    private static class MyPackagingManager
    implements ObjectPackagingManager {
        private MyPackagingManager() {
        }

        @Override
        public Object bytesToObject(byte[] arrby, int n) {
            return Utils.byteArrayToLong(arrby, n);
        }

        @Override
        public int objectSize() {
            return 8;
        }

        @Override
        public void objectToBytes(Object object, byte[] arrby, int n) {
            Utils.writeLongToByteArray((Long)object, arrby, n);
        }
    }

}

