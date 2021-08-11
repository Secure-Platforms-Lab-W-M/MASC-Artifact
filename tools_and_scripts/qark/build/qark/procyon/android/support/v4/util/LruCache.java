// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class LruCache<K, V>
{
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    private int safeSizeOf(final K k, final V v) {
        final int size = this.sizeOf(k, v);
        if (size >= 0) {
            return size;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Negative size: ");
        sb.append(k);
        sb.append("=");
        sb.append(v);
        throw new IllegalStateException(sb.toString());
    }
    
    protected V create(final K k) {
        return null;
    }
    
    public final int createCount() {
        synchronized (this) {
            return this.createCount;
        }
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    public final V get(final K p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          151
        //     4: aload_0        
        //     5: monitorenter   
        //     6: aload_0        
        //     7: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    10: aload_1        
        //    11: invokevirtual   java/util/LinkedHashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_2       
        //    15: aload_2        
        //    16: ifnull          33
        //    19: aload_0        
        //    20: aload_0        
        //    21: getfield        android/support/v4/util/LruCache.hitCount:I
        //    24: iconst_1       
        //    25: iadd           
        //    26: putfield        android/support/v4/util/LruCache.hitCount:I
        //    29: aload_0        
        //    30: monitorexit    
        //    31: aload_2        
        //    32: areturn        
        //    33: aload_0        
        //    34: aload_0        
        //    35: getfield        android/support/v4/util/LruCache.missCount:I
        //    38: iconst_1       
        //    39: iadd           
        //    40: putfield        android/support/v4/util/LruCache.missCount:I
        //    43: aload_0        
        //    44: monitorexit    
        //    45: aload_0        
        //    46: aload_1        
        //    47: invokevirtual   android/support/v4/util/LruCache.create:(Ljava/lang/Object;)Ljava/lang/Object;
        //    50: astore_2       
        //    51: aload_2        
        //    52: ifnonnull       57
        //    55: aconst_null    
        //    56: areturn        
        //    57: aload_0        
        //    58: monitorenter   
        //    59: aload_0        
        //    60: aload_0        
        //    61: getfield        android/support/v4/util/LruCache.createCount:I
        //    64: iconst_1       
        //    65: iadd           
        //    66: putfield        android/support/v4/util/LruCache.createCount:I
        //    69: aload_0        
        //    70: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    73: aload_1        
        //    74: aload_2        
        //    75: invokevirtual   java/util/LinkedHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    78: astore_3       
        //    79: aload_3        
        //    80: ifnull          96
        //    83: aload_0        
        //    84: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    87: aload_1        
        //    88: aload_3        
        //    89: invokevirtual   java/util/LinkedHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    92: pop            
        //    93: goto            111
        //    96: aload_0        
        //    97: aload_0        
        //    98: getfield        android/support/v4/util/LruCache.size:I
        //   101: aload_0        
        //   102: aload_1        
        //   103: aload_2        
        //   104: invokespecial   android/support/v4/util/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //   107: iadd           
        //   108: putfield        android/support/v4/util/LruCache.size:I
        //   111: aload_0        
        //   112: monitorexit    
        //   113: aload_3        
        //   114: ifnull          127
        //   117: aload_0        
        //   118: iconst_0       
        //   119: aload_1        
        //   120: aload_2        
        //   121: aload_3        
        //   122: invokevirtual   android/support/v4/util/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   125: aload_3        
        //   126: areturn        
        //   127: aload_0        
        //   128: aload_0        
        //   129: getfield        android/support/v4/util/LruCache.maxSize:I
        //   132: invokevirtual   android/support/v4/util/LruCache.trimToSize:(I)V
        //   135: aload_2        
        //   136: areturn        
        //   137: astore_1       
        //   138: aload_0        
        //   139: monitorexit    
        //   140: aload_1        
        //   141: athrow         
        //   142: astore_1       
        //   143: aload_0        
        //   144: monitorexit    
        //   145: aload_1        
        //   146: athrow         
        //   147: astore_1       
        //   148: goto            143
        //   151: new             Ljava/lang/NullPointerException;
        //   154: dup            
        //   155: ldc             "key == null"
        //   157: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   160: athrow         
        //    Signature:
        //  (TK;)TV;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      15     142    143    Any
        //  19     31     147    151    Any
        //  33     45     147    151    Any
        //  59     79     137    142    Any
        //  83     93     137    142    Any
        //  96     111    137    142    Any
        //  111    113    137    142    Any
        //  138    140    137    142    Any
        //  143    145    147    151    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0033:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public final int missCount() {
        synchronized (this) {
            return this.missCount;
        }
    }
    
    public final V put(final K k, final V v) {
        if (k != null && v != null) {
            while (true) {
                while (true) {
                    Label_0109: {
                        synchronized (this) {
                            ++this.putCount;
                            this.size += this.safeSizeOf(k, v);
                            final V put = this.map.put(k, v);
                            if (put != null) {
                                this.size -= this.safeSizeOf(k, put);
                                // monitorexit(this)
                                if (put != null) {
                                    this.entryRemoved(false, k, put, v);
                                }
                                this.trimToSize(this.maxSize);
                                return put;
                            }
                            break Label_0109;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        throw new NullPointerException("key == null || value == null");
    }
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    public final V remove(final K p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          64
        //     4: aload_0        
        //     5: monitorenter   
        //     6: aload_0        
        //     7: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    10: aload_1        
        //    11: invokevirtual   java/util/LinkedHashMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_2       
        //    15: aload_2        
        //    16: ifnull          74
        //    19: aload_0        
        //    20: aload_0        
        //    21: getfield        android/support/v4/util/LruCache.size:I
        //    24: aload_0        
        //    25: aload_1        
        //    26: aload_2        
        //    27: invokespecial   android/support/v4/util/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    30: isub           
        //    31: putfield        android/support/v4/util/LruCache.size:I
        //    34: goto            37
        //    37: aload_0        
        //    38: monitorexit    
        //    39: aload_2        
        //    40: ifnull          53
        //    43: aload_0        
        //    44: iconst_0       
        //    45: aload_1        
        //    46: aload_2        
        //    47: aconst_null    
        //    48: invokevirtual   android/support/v4/util/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    51: aload_2        
        //    52: areturn        
        //    53: aload_2        
        //    54: areturn        
        //    55: astore_1       
        //    56: aload_0        
        //    57: monitorexit    
        //    58: aload_1        
        //    59: athrow         
        //    60: astore_1       
        //    61: goto            56
        //    64: new             Ljava/lang/NullPointerException;
        //    67: dup            
        //    68: ldc             "key == null"
        //    70: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    73: athrow         
        //    74: goto            37
        //    Signature:
        //  (TK;)TV;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      15     55     56     Any
        //  19     34     60     64     Any
        //  37     39     60     64     Any
        //  56     58     60     64     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0037:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void resize(final int maxSize) {
        if (maxSize > 0) {
            synchronized (this) {
                this.maxSize = maxSize;
                // monitorexit(this)
                this.trimToSize(maxSize);
                return;
            }
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    public final Map<K, V> snapshot() {
        synchronized (this) {
            return new LinkedHashMap<K, V>((Map<? extends K, ? extends V>)this.map);
        }
    }
    
    @Override
    public final String toString() {
        while (true) {
            while (true) {
                synchronized (this) {
                    final int n = this.hitCount + this.missCount;
                    if (n != 0) {
                        final int n2 = this.hitCount * 100 / n;
                        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, n2);
                    }
                }
                final int n2 = 0;
                continue;
            }
        }
    }
    
    public void trimToSize(final int p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        android/support/v4/util/LruCache.size:I
        //     6: iflt            132
        //     9: aload_0        
        //    10: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    13: invokevirtual   java/util/LinkedHashMap.isEmpty:()Z
        //    16: ifeq            26
        //    19: aload_0        
        //    20: getfield        android/support/v4/util/LruCache.size:I
        //    23: ifne            132
        //    26: aload_0        
        //    27: getfield        android/support/v4/util/LruCache.size:I
        //    30: iload_1        
        //    31: if_icmple       129
        //    34: aload_0        
        //    35: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    38: invokevirtual   java/util/LinkedHashMap.isEmpty:()Z
        //    41: ifeq            47
        //    44: goto            129
        //    47: aload_0        
        //    48: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    51: invokevirtual   java/util/LinkedHashMap.entrySet:()Ljava/util/Set;
        //    54: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    59: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    64: checkcast       Ljava/util/Map$Entry;
        //    67: astore_3       
        //    68: aload_3        
        //    69: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //    74: astore_2       
        //    75: aload_3        
        //    76: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //    81: astore_3       
        //    82: aload_0        
        //    83: getfield        android/support/v4/util/LruCache.map:Ljava/util/LinkedHashMap;
        //    86: aload_2        
        //    87: invokevirtual   java/util/LinkedHashMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    90: pop            
        //    91: aload_0        
        //    92: aload_0        
        //    93: getfield        android/support/v4/util/LruCache.size:I
        //    96: aload_0        
        //    97: aload_2        
        //    98: aload_3        
        //    99: invokespecial   android/support/v4/util/LruCache.safeSizeOf:(Ljava/lang/Object;Ljava/lang/Object;)I
        //   102: isub           
        //   103: putfield        android/support/v4/util/LruCache.size:I
        //   106: aload_0        
        //   107: aload_0        
        //   108: getfield        android/support/v4/util/LruCache.evictionCount:I
        //   111: iconst_1       
        //   112: iadd           
        //   113: putfield        android/support/v4/util/LruCache.evictionCount:I
        //   116: aload_0        
        //   117: monitorexit    
        //   118: aload_0        
        //   119: iconst_1       
        //   120: aload_2        
        //   121: aload_3        
        //   122: aconst_null    
        //   123: invokevirtual   android/support/v4/util/LruCache.entryRemoved:(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   126: goto            0
        //   129: aload_0        
        //   130: monitorexit    
        //   131: return         
        //   132: new             Ljava/lang/StringBuilder;
        //   135: dup            
        //   136: invokespecial   java/lang/StringBuilder.<init>:()V
        //   139: astore_2       
        //   140: aload_2        
        //   141: aload_0        
        //   142: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   145: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   151: pop            
        //   152: aload_2        
        //   153: ldc             ".sizeOf() is reporting inconsistent results!"
        //   155: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   158: pop            
        //   159: new             Ljava/lang/IllegalStateException;
        //   162: dup            
        //   163: aload_2        
        //   164: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   167: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   170: athrow         
        //   171: astore_2       
        //   172: aload_0        
        //   173: monitorexit    
        //   174: aload_2        
        //   175: athrow         
        //   176: astore_2       
        //   177: goto            172
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      26     171    172    Any
        //  26     44     171    172    Any
        //  47     75     171    172    Any
        //  75     118    176    180    Any
        //  129    131    171    172    Any
        //  132    171    171    172    Any
        //  172    174    176    180    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0129:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
