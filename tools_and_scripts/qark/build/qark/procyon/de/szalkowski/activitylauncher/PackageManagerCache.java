// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.content.pm.PackageManager$NameNotFoundException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import java.util.Map;

public class PackageManagerCache
{
    public static PackageManagerCache instance;
    protected Map<ComponentName, MyActivityInfo> activityInfos;
    protected Map<String, MyPackageInfo> packageInfos;
    protected PackageManager pm;
    
    static {
        PackageManagerCache.instance = null;
    }
    
    private PackageManagerCache(final PackageManager pm) {
        while (true) {
            try {
                Log.d("cipherName-23", Cipher.getInstance("DES").getAlgorithm());
                this.pm = pm;
                this.packageInfos = new HashMap<String, MyPackageInfo>();
                this.activityInfos = new HashMap<ComponentName, MyActivityInfo>();
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static PackageManagerCache getPackageManagerCache(final PackageManager packageManager) {
        while (true) {
            try {
                Log.d("cipherName-21", Cipher.getInstance("DES").getAlgorithm());
                Label_0045: {
                    if (PackageManagerCache.instance != null) {
                        break Label_0045;
                    }
                    try {
                        Log.d("cipherName-22", Cipher.getInstance("DES").getAlgorithm());
                        PackageManagerCache.instance = new PackageManagerCache(packageManager);
                        return PackageManagerCache.instance;
                    }
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex2) {}
                }
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
    
    MyActivityInfo getActivityInfo(final ComponentName p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     4: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     7: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    10: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    13: pop            
        //    14: aload_0        
        //    15: getfield        de/szalkowski/activitylauncher/PackageManagerCache.activityInfos:Ljava/util/Map;
        //    18: astore_2       
        //    19: aload_2        
        //    20: monitorenter   
        //    21: ldc             "cipherName-31"
        //    23: ldc             "DES"
        //    25: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    28: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    31: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    34: pop            
        //    35: aload_0        
        //    36: getfield        de/szalkowski/activitylauncher/PackageManagerCache.activityInfos:Ljava/util/Map;
        //    39: aload_1        
        //    40: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    45: ifeq            80
        //    48: ldc             "cipherName-32"
        //    50: ldc             "DES"
        //    52: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    55: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    58: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    61: pop            
        //    62: aload_0        
        //    63: getfield        de/szalkowski/activitylauncher/PackageManagerCache.activityInfos:Ljava/util/Map;
        //    66: aload_1        
        //    67: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    72: checkcast       Lde/szalkowski/activitylauncher/MyActivityInfo;
        //    75: astore_1       
        //    76: aload_2        
        //    77: monitorexit    
        //    78: aload_1        
        //    79: areturn        
        //    80: new             Lde/szalkowski/activitylauncher/MyActivityInfo;
        //    83: dup            
        //    84: aload_1        
        //    85: aload_0        
        //    86: getfield        de/szalkowski/activitylauncher/PackageManagerCache.pm:Landroid/content/pm/PackageManager;
        //    89: invokespecial   de/szalkowski/activitylauncher/MyActivityInfo.<init>:(Landroid/content/ComponentName;Landroid/content/pm/PackageManager;)V
        //    92: astore_3       
        //    93: aload_0        
        //    94: getfield        de/szalkowski/activitylauncher/PackageManagerCache.activityInfos:Ljava/util/Map;
        //    97: aload_1        
        //    98: aload_3        
        //    99: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   104: pop            
        //   105: aload_2        
        //   106: monitorexit    
        //   107: aload_3        
        //   108: areturn        
        //   109: astore_1       
        //   110: aload_2        
        //   111: monitorexit    
        //   112: aload_1        
        //   113: athrow         
        //   114: astore_3       
        //   115: goto            62
        //   118: astore_3       
        //   119: goto            62
        //   122: astore_3       
        //   123: goto            35
        //   126: astore_3       
        //   127: goto            35
        //   130: astore_2       
        //   131: goto            14
        //   134: astore_2       
        //   135: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     130    134    Ljava/security/NoSuchAlgorithmException;
        //  0      14     134    138    Ljavax/crypto/NoSuchPaddingException;
        //  21     35     122    126    Ljava/security/NoSuchAlgorithmException;
        //  21     35     126    130    Ljavax/crypto/NoSuchPaddingException;
        //  21     35     109    114    Any
        //  35     48     109    114    Any
        //  48     62     114    118    Ljava/security/NoSuchAlgorithmException;
        //  48     62     118    122    Ljavax/crypto/NoSuchPaddingException;
        //  48     62     109    114    Any
        //  62     78     109    114    Any
        //  80     107    109    114    Any
        //  110    112    109    114    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
    
    MyPackageInfo[] getAllPackageInfo() {
        try {
            Log.d("cipherName-29", Cipher.getInstance("DES").getAlgorithm());
            return null;
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
        catch (NoSuchPaddingException ex2) {
            return null;
        }
    }
    
    MyPackageInfo getPackageInfo(final String p0) throws PackageManager$NameNotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     4: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     7: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    10: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    13: pop            
        //    14: aload_0        
        //    15: getfield        de/szalkowski/activitylauncher/PackageManagerCache.packageInfos:Ljava/util/Map;
        //    18: astore_2       
        //    19: aload_2        
        //    20: monitorenter   
        //    21: ldc             "cipherName-25"
        //    23: ldc             "DES"
        //    25: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    28: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    31: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    34: pop            
        //    35: aload_0        
        //    36: getfield        de/szalkowski/activitylauncher/PackageManagerCache.packageInfos:Ljava/util/Map;
        //    39: aload_1        
        //    40: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //    45: ifeq            80
        //    48: ldc             "cipherName-26"
        //    50: ldc             "DES"
        //    52: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    55: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    58: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    61: pop            
        //    62: aload_0        
        //    63: getfield        de/szalkowski/activitylauncher/PackageManagerCache.packageInfos:Ljava/util/Map;
        //    66: aload_1        
        //    67: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    72: checkcast       Lde/szalkowski/activitylauncher/MyPackageInfo;
        //    75: astore_1       
        //    76: aload_2        
        //    77: monitorexit    
        //    78: aload_1        
        //    79: areturn        
        //    80: ldc             "cipherName-27"
        //    82: ldc             "DES"
        //    84: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    87: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    90: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    93: pop            
        //    94: aload_0        
        //    95: getfield        de/szalkowski/activitylauncher/PackageManagerCache.pm:Landroid/content/pm/PackageManager;
        //    98: aload_1        
        //    99: iconst_1       
        //   100: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   103: astore_3       
        //   104: new             Lde/szalkowski/activitylauncher/MyPackageInfo;
        //   107: dup            
        //   108: aload_3        
        //   109: aload_0        
        //   110: getfield        de/szalkowski/activitylauncher/PackageManagerCache.pm:Landroid/content/pm/PackageManager;
        //   113: aload_0        
        //   114: invokespecial   de/szalkowski/activitylauncher/MyPackageInfo.<init>:(Landroid/content/pm/PackageInfo;Landroid/content/pm/PackageManager;Lde/szalkowski/activitylauncher/PackageManagerCache;)V
        //   117: astore_3       
        //   118: aload_0        
        //   119: getfield        de/szalkowski/activitylauncher/PackageManagerCache.packageInfos:Ljava/util/Map;
        //   122: aload_1        
        //   123: aload_3        
        //   124: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   129: pop            
        //   130: aload_2        
        //   131: monitorexit    
        //   132: aload_3        
        //   133: areturn        
        //   134: astore_1       
        //   135: ldc             "cipherName-28"
        //   137: ldc             "DES"
        //   139: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   142: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   145: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   148: pop            
        //   149: aload_1        
        //   150: athrow         
        //   151: astore_1       
        //   152: aload_2        
        //   153: monitorexit    
        //   154: aload_1        
        //   155: athrow         
        //   156: astore_3       
        //   157: goto            149
        //   160: astore_3       
        //   161: goto            149
        //   164: astore_3       
        //   165: goto            94
        //   168: astore_3       
        //   169: goto            94
        //   172: astore_3       
        //   173: goto            62
        //   176: astore_3       
        //   177: goto            62
        //   180: astore_3       
        //   181: goto            35
        //   184: astore_3       
        //   185: goto            35
        //   188: astore_2       
        //   189: goto            14
        //   192: astore_2       
        //   193: goto            14
        //    Exceptions:
        //  throws android.content.pm.PackageManager$NameNotFoundException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  0      14     188    192    Ljava/security/NoSuchAlgorithmException;
        //  0      14     192    196    Ljavax/crypto/NoSuchPaddingException;
        //  21     35     180    184    Ljava/security/NoSuchAlgorithmException;
        //  21     35     184    188    Ljavax/crypto/NoSuchPaddingException;
        //  21     35     151    156    Any
        //  35     48     151    156    Any
        //  48     62     172    176    Ljava/security/NoSuchAlgorithmException;
        //  48     62     176    180    Ljavax/crypto/NoSuchPaddingException;
        //  48     62     151    156    Any
        //  62     78     151    156    Any
        //  80     94     164    168    Ljava/security/NoSuchAlgorithmException;
        //  80     94     168    172    Ljavax/crypto/NoSuchPaddingException;
        //  80     94     134    164    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  80     94     151    156    Any
        //  94     104    134    164    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  94     104    151    156    Any
        //  104    132    151    156    Any
        //  135    149    156    160    Ljava/security/NoSuchAlgorithmException;
        //  135    149    160    164    Ljavax/crypto/NoSuchPaddingException;
        //  135    149    151    156    Any
        //  149    151    151    156    Any
        //  152    154    151    156    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
