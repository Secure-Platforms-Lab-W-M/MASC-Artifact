// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.publicsuffix;

import java.net.IDN;
import okhttp3.internal.platform.Platform;
import java.io.InterruptedIOException;
import java.io.IOException;
import okio.BufferedSource;
import java.io.InputStream;
import java.io.Closeable;
import okio.Source;
import okio.GzipSource;
import okio.Okio;
import okhttp3.internal.Util;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PublicSuffixDatabase
{
    private static final String[] EMPTY_RULE;
    private static final byte EXCEPTION_MARKER = 33;
    private static final String[] PREVAILING_RULE;
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    private static final byte[] WILDCARD_LABEL;
    private static final PublicSuffixDatabase instance;
    private final AtomicBoolean listRead;
    private byte[] publicSuffixExceptionListBytes;
    private byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch;
    
    static {
        WILDCARD_LABEL = new byte[] { 42 };
        EMPTY_RULE = new String[0];
        PREVAILING_RULE = new String[] { "*" };
        instance = new PublicSuffixDatabase();
    }
    
    public PublicSuffixDatabase() {
        this.listRead = new AtomicBoolean(false);
        this.readCompleteLatch = new CountDownLatch(1);
    }
    
    private static String binarySearchBytes(final byte[] array, final byte[][] array2, final int n) {
        int n2 = 0;
        int length = array.length;
        final String s = null;
        String s2;
        while (true) {
            s2 = s;
            if (n2 >= length) {
                break;
            }
            int n3;
            for (n3 = (n2 + length) / 2; n3 > -1 && array[n3] != 10; --n3) {}
            int n4;
            int n5;
            for (n4 = n3 + 1, n5 = 1; array[n4 + n5] != 10; ++n5) {}
            final int n6 = n4 + n5 - n4;
            int n7 = n;
            int n8 = 0;
            int n9 = 0;
            int n10 = 0;
            int n12;
            while (true) {
                int n11;
                if (n10 != 0) {
                    n11 = 46;
                    n10 = 0;
                }
                else {
                    n11 = (array2[n7][n8] & 0xFF);
                }
                n12 = n11 - (array[n4 + n9] & 0xFF);
                if (n12 != 0) {
                    break;
                }
                final int n13 = n9 + 1;
                final int n14 = ++n8;
                if ((n9 = n13) == n6) {
                    break;
                }
                n8 = n14;
                n9 = n13;
                if (array2[n7].length != n14) {
                    continue;
                }
                n8 = n14;
                n9 = n13;
                if (n7 == array2.length - 1) {
                    break;
                }
                ++n7;
                n8 = -1;
                n10 = 1;
                n9 = n13;
            }
            if (n12 < 0) {
                length = n4 - 1;
            }
            else if (n12 > 0) {
                n2 = n4 + n5 + 1;
            }
            else {
                final int n15 = n6 - n9;
                int n16 = array2[n7].length - n8;
                for (int i = n7 + 1; i < array2.length; ++i) {
                    n16 += array2[i].length;
                }
                if (n16 < n15) {
                    length = n4 - 1;
                }
                else {
                    if (n16 <= n15) {
                        s2 = new String(array, n4, n6, Util.UTF_8);
                        break;
                    }
                    n2 = n4 + n5 + 1;
                }
            }
        }
        return s2;
    }
    
    private String[] findMatchingRule(final String[] p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.listRead:Ljava/util/concurrent/atomic/AtomicBoolean;
        //     4: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //     7: ifne            50
        //    10: aload_0        
        //    11: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.listRead:Ljava/util/concurrent/atomic/AtomicBoolean;
        //    14: iconst_0       
        //    15: iconst_1       
        //    16: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.compareAndSet:(ZZ)Z
        //    19: ifeq            50
        //    22: aload_0        
        //    23: invokespecial   okhttp3/internal/publicsuffix/PublicSuffixDatabase.readTheListUninterruptibly:()V
        //    26: aload_0        
        //    27: monitorenter   
        //    28: aload_0        
        //    29: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.publicSuffixListBytes:[B
        //    32: ifnonnull       64
        //    35: new             Ljava/lang/IllegalStateException;
        //    38: dup            
        //    39: ldc             "Unable to load publicsuffixes.gz resource from the classpath."
        //    41: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    44: athrow         
        //    45: astore_1       
        //    46: aload_0        
        //    47: monitorexit    
        //    48: aload_1        
        //    49: athrow         
        //    50: aload_0        
        //    51: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.readCompleteLatch:Ljava/util/concurrent/CountDownLatch;
        //    54: invokevirtual   java/util/concurrent/CountDownLatch.await:()V
        //    57: goto            26
        //    60: astore_3       
        //    61: goto            26
        //    64: aload_0        
        //    65: monitorexit    
        //    66: aload_1        
        //    67: arraylength    
        //    68: anewarray       [B
        //    71: astore          6
        //    73: iconst_0       
        //    74: istore_2       
        //    75: iload_2        
        //    76: aload_1        
        //    77: arraylength    
        //    78: if_icmpge       101
        //    81: aload           6
        //    83: iload_2        
        //    84: aload_1        
        //    85: iload_2        
        //    86: aaload         
        //    87: getstatic       okhttp3/internal/Util.UTF_8:Ljava/nio/charset/Charset;
        //    90: invokevirtual   java/lang/String.getBytes:(Ljava/nio/charset/Charset;)[B
        //    93: aastore        
        //    94: iload_2        
        //    95: iconst_1       
        //    96: iadd           
        //    97: istore_2       
        //    98: goto            75
        //   101: aconst_null    
        //   102: astore_3       
        //   103: iconst_0       
        //   104: istore_2       
        //   105: aload_3        
        //   106: astore_1       
        //   107: iload_2        
        //   108: aload           6
        //   110: arraylength    
        //   111: if_icmpge       129
        //   114: aload_0        
        //   115: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.publicSuffixListBytes:[B
        //   118: aload           6
        //   120: iload_2        
        //   121: invokestatic    okhttp3/internal/publicsuffix/PublicSuffixDatabase.binarySearchBytes:([B[[BI)Ljava/lang/String;
        //   124: astore_1       
        //   125: aload_1        
        //   126: ifnull          264
        //   129: aconst_null    
        //   130: astore          4
        //   132: aload           4
        //   134: astore_3       
        //   135: aload           6
        //   137: arraylength    
        //   138: iconst_1       
        //   139: if_icmple       188
        //   142: aload           6
        //   144: invokevirtual   [[B.clone:()Ljava/lang/Object;
        //   147: checkcast       [[B
        //   150: astore          5
        //   152: iconst_0       
        //   153: istore_2       
        //   154: aload           4
        //   156: astore_3       
        //   157: iload_2        
        //   158: aload           5
        //   160: arraylength    
        //   161: iconst_1       
        //   162: isub           
        //   163: if_icmpge       188
        //   166: aload           5
        //   168: iload_2        
        //   169: getstatic       okhttp3/internal/publicsuffix/PublicSuffixDatabase.WILDCARD_LABEL:[B
        //   172: aastore        
        //   173: aload_0        
        //   174: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.publicSuffixListBytes:[B
        //   177: aload           5
        //   179: iload_2        
        //   180: invokestatic    okhttp3/internal/publicsuffix/PublicSuffixDatabase.binarySearchBytes:([B[[BI)Ljava/lang/String;
        //   183: astore_3       
        //   184: aload_3        
        //   185: ifnull          271
        //   188: aconst_null    
        //   189: astore          5
        //   191: aload           5
        //   193: astore          4
        //   195: aload_3        
        //   196: ifnull          231
        //   199: iconst_0       
        //   200: istore_2       
        //   201: aload           5
        //   203: astore          4
        //   205: iload_2        
        //   206: aload           6
        //   208: arraylength    
        //   209: iconst_1       
        //   210: isub           
        //   211: if_icmpge       231
        //   214: aload_0        
        //   215: getfield        okhttp3/internal/publicsuffix/PublicSuffixDatabase.publicSuffixExceptionListBytes:[B
        //   218: aload           6
        //   220: iload_2        
        //   221: invokestatic    okhttp3/internal/publicsuffix/PublicSuffixDatabase.binarySearchBytes:([B[[BI)Ljava/lang/String;
        //   224: astore          4
        //   226: aload           4
        //   228: ifnull          278
        //   231: aload           4
        //   233: ifnull          285
        //   236: new             Ljava/lang/StringBuilder;
        //   239: dup            
        //   240: invokespecial   java/lang/StringBuilder.<init>:()V
        //   243: ldc             "!"
        //   245: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   248: aload           4
        //   250: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   253: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   256: ldc             "\\."
        //   258: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   261: astore_3       
        //   262: aload_3        
        //   263: areturn        
        //   264: iload_2        
        //   265: iconst_1       
        //   266: iadd           
        //   267: istore_2       
        //   268: goto            105
        //   271: iload_2        
        //   272: iconst_1       
        //   273: iadd           
        //   274: istore_2       
        //   275: goto            154
        //   278: iload_2        
        //   279: iconst_1       
        //   280: iadd           
        //   281: istore_2       
        //   282: goto            201
        //   285: aload_1        
        //   286: ifnonnull       297
        //   289: aload_3        
        //   290: ifnonnull       297
        //   293: getstatic       okhttp3/internal/publicsuffix/PublicSuffixDatabase.PREVAILING_RULE:[Ljava/lang/String;
        //   296: areturn        
        //   297: aload_1        
        //   298: ifnull          333
        //   301: aload_1        
        //   302: ldc             "\\."
        //   304: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   307: astore_1       
        //   308: aload_3        
        //   309: ifnull          340
        //   312: aload_3        
        //   313: ldc             "\\."
        //   315: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   318: astore          4
        //   320: aload_1        
        //   321: astore_3       
        //   322: aload_1        
        //   323: arraylength    
        //   324: aload           4
        //   326: arraylength    
        //   327: if_icmpgt       262
        //   330: aload           4
        //   332: areturn        
        //   333: getstatic       okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE:[Ljava/lang/String;
        //   336: astore_1       
        //   337: goto            308
        //   340: getstatic       okhttp3/internal/publicsuffix/PublicSuffixDatabase.EMPTY_RULE:[Ljava/lang/String;
        //   343: astore          4
        //   345: goto            320
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  28     45     45     50     Any
        //  46     48     45     50     Any
        //  50     57     60     64     Ljava/lang/InterruptedException;
        //  64     66     45     50     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public static PublicSuffixDatabase get() {
        return PublicSuffixDatabase.instance;
    }
    
    private void readTheList() throws IOException {
        final InputStream resourceAsStream = PublicSuffixDatabase.class.getResourceAsStream("publicsuffixes.gz");
        if (resourceAsStream == null) {
            return;
        }
        final BufferedSource buffer = Okio.buffer(new GzipSource(Okio.source(resourceAsStream)));
        byte[] array;
        byte[] array2;
        try {
            array = new byte[buffer.readInt()];
            buffer.readFully(array);
            array2 = new byte[buffer.readInt()];
            buffer.readFully(array2);
            Util.closeQuietly(buffer);
            // monitorenter(this)
            final PublicSuffixDatabase publicSuffixDatabase = this;
            final byte[] array3 = array;
            publicSuffixDatabase.publicSuffixListBytes = array3;
            final PublicSuffixDatabase publicSuffixDatabase2 = this;
            final byte[] array4 = array2;
            publicSuffixDatabase2.publicSuffixExceptionListBytes = array4;
            final PublicSuffixDatabase publicSuffixDatabase3 = this;
            // monitorexit(publicSuffixDatabase3)
            final PublicSuffixDatabase publicSuffixDatabase4 = this;
            final CountDownLatch countDownLatch = publicSuffixDatabase4.readCompleteLatch;
            countDownLatch.countDown();
            return;
        }
        finally {
            Util.closeQuietly(buffer);
        }
        try {
            final PublicSuffixDatabase publicSuffixDatabase = this;
            final byte[] array3 = array;
            publicSuffixDatabase.publicSuffixListBytes = array3;
            final PublicSuffixDatabase publicSuffixDatabase2 = this;
            final byte[] array4 = array2;
            publicSuffixDatabase2.publicSuffixExceptionListBytes = array4;
            final PublicSuffixDatabase publicSuffixDatabase3 = this;
            // monitorexit(publicSuffixDatabase3)
            final PublicSuffixDatabase publicSuffixDatabase4 = this;
            final CountDownLatch countDownLatch = publicSuffixDatabase4.readCompleteLatch;
            countDownLatch.countDown();
        }
        finally {
        }
        // monitorexit(this)
    }
    
    private void readTheListUninterruptibly() {
        boolean b = false;
        try {
            this.readTheList();
        }
        catch (InterruptedIOException ex2) {
            b = true;
        }
        catch (IOException ex) {
            Platform.get().log(5, "Failed to read public suffix list", ex);
        }
        finally {
            if (b) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public String getEffectiveTldPlusOne(final String s) {
        if (s == null) {
            throw new NullPointerException("domain == null");
        }
        final String[] split = IDN.toUnicode(s).split("\\.");
        final String[] matchingRule = this.findMatchingRule(split);
        if (split.length == matchingRule.length && matchingRule[0].charAt(0) != '!') {
            return null;
        }
        int i;
        if (matchingRule[0].charAt(0) == '!') {
            i = split.length - matchingRule.length;
        }
        else {
            i = split.length - (matchingRule.length + 1);
        }
        final StringBuilder sb = new StringBuilder();
        for (String[] split2 = s.split("\\."); i < split2.length; ++i) {
            sb.append(split2[i]).append('.');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    void setListBytes(final byte[] publicSuffixListBytes, final byte[] publicSuffixExceptionListBytes) {
        this.publicSuffixListBytes = publicSuffixListBytes;
        this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }
}
