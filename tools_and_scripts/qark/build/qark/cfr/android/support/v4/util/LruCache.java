/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int n) {
        if (n > 0) {
            this.maxSize = n;
            this.map = new LinkedHashMap(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private int safeSizeOf(K k, V v) {
        int n = this.sizeOf(k, v);
        if (n >= 0) {
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Negative size: ");
        stringBuilder.append(k);
        stringBuilder.append("=");
        stringBuilder.append(v);
        throw new IllegalStateException(stringBuilder.toString());
    }

    protected V create(K k) {
        return null;
    }

    public final int createCount() {
        synchronized (this) {
            int n = this.createCount;
            return n;
        }
    }

    protected void entryRemoved(boolean bl, K k, V v, V v2) {
    }

    public final void evictAll() {
        this.trimToSize(-1);
    }

    public final int evictionCount() {
        synchronized (this) {
            int n = this.evictionCount;
            return n;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V get(K var1_1) {
        block11 : {
            if (var1_1 == null) throw new NullPointerException("key == null");
            // MONITORENTER : this
            var2_3 = this.map.get(var1_1);
            if (var2_3 == null) ** GOTO lbl9
            ++this.hitCount;
            // MONITOREXIT : this
            return var2_3;
lbl9: // 1 sources:
            ++this.missCount;
            // MONITOREXIT : this
            var2_3 = this.create(var1_1);
            if (var2_3 != null) break block11;
            return null;
        }
        // MONITORENTER : this
        ++this.createCount;
        var3_4 = this.map.put(var1_1, var2_3);
        if (var3_4 != null) {
            this.map.put(var1_1, var3_4);
        } else {
            this.size += this.safeSizeOf(var1_1, var2_3);
        }
        // MONITOREXIT : this
        if (var3_4 != null) {
            this.entryRemoved(false, var1_1, var2_3, var3_4);
            return var3_4;
        }
        this.trimToSize(this.maxSize);
        return var2_3;
    }

    public final int hitCount() {
        synchronized (this) {
            int n = this.hitCount;
            return n;
        }
    }

    public final int maxSize() {
        synchronized (this) {
            int n = this.maxSize;
            return n;
        }
    }

    public final int missCount() {
        synchronized (this) {
            int n = this.missCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V put(K k, V v) {
        if (k == null) throw new NullPointerException("key == null || value == null");
        if (v == null) throw new NullPointerException("key == null || value == null");
        // MONITORENTER : this
        ++this.putCount;
        this.size += this.safeSizeOf(k, v);
        V v2 = this.map.put(k, v);
        if (v2 != null) {
            this.size -= this.safeSizeOf(k, v2);
        }
        // MONITOREXIT : this
        if (v2 != null) {
            this.entryRemoved(false, k, v2, v);
        }
        this.trimToSize(this.maxSize);
        return v2;
    }

    public final int putCount() {
        synchronized (this) {
            int n = this.putCount;
            return n;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V remove(K k) {
        if (k == null) throw new NullPointerException("key == null");
        // MONITORENTER : this
        V v = this.map.remove(k);
        if (v != null) {
            this.size -= this.safeSizeOf(k, v);
            // MONITOREXIT : this
        }
        if (v == null) return v;
        this.entryRemoved(false, k, v, null);
        return v;
        catch (Throwable throwable) {
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void resize(int n) {
        if (n > 0) {
            synchronized (this) {
                this.maxSize = n;
            }
            this.trimToSize(n);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    public final int size() {
        synchronized (this) {
            int n = this.size;
            return n;
        }
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final Map<K, V> snapshot() {
        synchronized (this) {
            LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<K, V>(this.map);
            return linkedHashMap;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final String toString() {
        synchronized (this) {
            int n = this.hitCount + this.missCount;
            n = n != 0 ? this.hitCount * 100 / n : 0;
            return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, n);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void trimToSize(int n) {
        StringBuilder stringBuilder /* !! */ ;
        do {
            // MONITORENTER : this
            if (this.size < 0 || this.map.isEmpty() && this.size != 0) break;
            if (this.size <= n) {
                // MONITOREXIT : this
                return;
            }
            if (this.map.isEmpty()) {
                return;
            }
            Map.Entry entry = this.map.entrySet().iterator().next();
            stringBuilder /* !! */  = entry.getKey();
            entry = entry.getValue();
            this.map.remove(stringBuilder /* !! */ );
            this.size -= this.safeSizeOf(stringBuilder /* !! */ , entry);
            ++this.evictionCount;
            // MONITOREXIT : this
            this.entryRemoved(true, stringBuilder /* !! */ , entry, null);
            continue;
            break;
        } while (true);
        stringBuilder /* !! */  = new StringBuilder();
        stringBuilder /* !! */ .append(this.getClass().getName());
        stringBuilder /* !! */ .append(".sizeOf() is reporting inconsistent results!");
        throw new IllegalStateException(stringBuilder /* !! */ .toString());
        catch (Throwable throwable) {
            throw throwable;
        }
    }
}

