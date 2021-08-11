// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.graphics.drawable.BitmapDrawable;

public class MyPackageInfo implements Comparable<MyPackageInfo>
{
    protected MyActivityInfo[] activities;
    protected BitmapDrawable icon;
    protected int icon_resource;
    protected String icon_resource_name;
    protected String name;
    protected String package_name;
    
    public MyPackageInfo(final PackageInfo p0, final PackageManager p1, final PackageManagerCache p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: ldc             "cipherName-70"
        //     6: ldc             "DES"
        //     8: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    11: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    14: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    17: pop            
        //    18: aload_0        
        //    19: aload_1        
        //    20: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //    23: putfield        de/szalkowski/activitylauncher/MyPackageInfo.package_name:Ljava/lang/String;
        //    26: aload_1        
        //    27: getfield        android/content/pm/PackageInfo.applicationInfo:Landroid/content/pm/ApplicationInfo;
        //    30: astore          8
        //    32: aload           8
        //    34: ifnull          219
        //    37: ldc             "cipherName-71"
        //    39: ldc             "DES"
        //    41: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    44: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    47: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    50: pop            
        //    51: aload_0        
        //    52: aload_2        
        //    53: aload           8
        //    55: invokevirtual   android/content/pm/PackageManager.getApplicationLabel:(Landroid/content/pm/ApplicationInfo;)Ljava/lang/CharSequence;
        //    58: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //    63: putfield        de/szalkowski/activitylauncher/MyPackageInfo.name:Ljava/lang/String;
        //    66: ldc             "cipherName-72"
        //    68: ldc             "DES"
        //    70: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    73: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    76: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    79: pop            
        //    80: aload_0        
        //    81: aload_2        
        //    82: aload           8
        //    84: invokevirtual   android/content/pm/PackageManager.getApplicationIcon:(Landroid/content/pm/ApplicationInfo;)Landroid/graphics/drawable/Drawable;
        //    87: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //    90: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //    93: aload_0        
        //    94: aload           8
        //    96: getfield        android/content/pm/ApplicationInfo.icon:I
        //    99: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource:I
        //   102: aload_0        
        //   103: aconst_null    
        //   104: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource_name:Ljava/lang/String;
        //   107: aload_0        
        //   108: getfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource:I
        //   111: ifeq            159
        //   114: ldc             "cipherName-75"
        //   116: ldc             "DES"
        //   118: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   121: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   124: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   127: pop            
        //   128: ldc             "cipherName-76"
        //   130: ldc             "DES"
        //   132: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   135: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   138: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   141: pop            
        //   142: aload_0        
        //   143: aload_2        
        //   144: aload           8
        //   146: invokevirtual   android/content/pm/PackageManager.getResourcesForApplication:(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
        //   149: aload_0        
        //   150: getfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource:I
        //   153: invokevirtual   android/content/res/Resources.getResourceName:(I)Ljava/lang/String;
        //   156: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource_name:Ljava/lang/String;
        //   159: aload_1        
        //   160: getfield        android/content/pm/PackageInfo.activities:[Landroid/content/pm/ActivityInfo;
        //   163: ifnonnull       282
        //   166: ldc             "cipherName-78"
        //   168: ldc             "DES"
        //   170: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   173: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   176: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   179: pop            
        //   180: aload_0        
        //   181: iconst_0       
        //   182: anewarray       Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   185: putfield        de/szalkowski/activitylauncher/MyPackageInfo.activities:[Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   188: return         
        //   189: astore          9
        //   191: ldc             "cipherName-73"
        //   193: ldc             "DES"
        //   195: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   198: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   201: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   204: pop            
        //   205: aload_0        
        //   206: aload_2        
        //   207: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
        //   210: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //   213: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //   216: goto            93
        //   219: ldc             "cipherName-74"
        //   221: ldc             "DES"
        //   223: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   226: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   229: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   232: pop            
        //   233: aload_0        
        //   234: aload_1        
        //   235: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //   238: putfield        de/szalkowski/activitylauncher/MyPackageInfo.name:Ljava/lang/String;
        //   241: aload_0        
        //   242: aload_2        
        //   243: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
        //   246: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //   249: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //   252: aload_0        
        //   253: iconst_0       
        //   254: putfield        de/szalkowski/activitylauncher/MyPackageInfo.icon_resource:I
        //   257: goto            102
        //   260: astore_2       
        //   261: ldc             "cipherName-77"
        //   263: ldc             "DES"
        //   265: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   268: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   271: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   274: pop            
        //   275: goto            159
        //   278: astore_2       
        //   279: goto            159
        //   282: ldc             "cipherName-79"
        //   284: ldc             "DES"
        //   286: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   289: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   292: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   295: pop            
        //   296: aload_0        
        //   297: aload_1        
        //   298: invokestatic    de/szalkowski/activitylauncher/MyPackageInfo.countActivitiesFromInfo:(Landroid/content/pm/PackageInfo;)I
        //   301: anewarray       Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   304: putfield        de/szalkowski/activitylauncher/MyPackageInfo.activities:[Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   307: aload_1        
        //   308: getfield        android/content/pm/PackageInfo.activities:[Landroid/content/pm/ActivityInfo;
        //   311: astore_2       
        //   312: aload_2        
        //   313: arraylength    
        //   314: istore          7
        //   316: iconst_0       
        //   317: istore          5
        //   319: iconst_0       
        //   320: istore          4
        //   322: iload           5
        //   324: iload           7
        //   326: if_icmpge       463
        //   329: aload_2        
        //   330: iload           5
        //   332: aaload         
        //   333: astore          8
        //   335: ldc             "cipherName-80"
        //   337: ldc             "DES"
        //   339: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   342: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   345: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   348: pop            
        //   349: aload           8
        //   351: invokevirtual   android/content/pm/ActivityInfo.isEnabled:()Z
        //   354: ifeq            581
        //   357: aload           8
        //   359: getfield        android/content/pm/ActivityInfo.exported:Z
        //   362: ifeq            581
        //   365: ldc             "cipherName-81"
        //   367: ldc             "DES"
        //   369: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   372: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   375: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   378: pop            
        //   379: getstatic       de/szalkowski/activitylauncher/MyPackageInfo.$assertionsDisabled:Z
        //   382: ifne            408
        //   385: aload           8
        //   387: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   390: aload_1        
        //   391: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //   394: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   397: ifne            408
        //   400: new             Ljava/lang/AssertionError;
        //   403: dup            
        //   404: invokespecial   java/lang/AssertionError.<init>:()V
        //   407: athrow         
        //   408: new             Landroid/content/ComponentName;
        //   411: dup            
        //   412: aload           8
        //   414: getfield        android/content/pm/ActivityInfo.packageName:Ljava/lang/String;
        //   417: aload           8
        //   419: getfield        android/content/pm/ActivityInfo.name:Ljava/lang/String;
        //   422: invokespecial   android/content/ComponentName.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   425: astore          8
        //   427: aload_0        
        //   428: getfield        de/szalkowski/activitylauncher/MyPackageInfo.activities:[Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   431: astore          9
        //   433: iload           4
        //   435: iconst_1       
        //   436: iadd           
        //   437: istore          6
        //   439: aload           9
        //   441: iload           4
        //   443: aload_3        
        //   444: aload           8
        //   446: invokevirtual   de/szalkowski/activitylauncher/PackageManagerCache.getActivityInfo:(Landroid/content/ComponentName;)Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   449: aastore        
        //   450: iload           6
        //   452: istore          4
        //   454: iload           5
        //   456: iconst_1       
        //   457: iadd           
        //   458: istore          5
        //   460: goto            322
        //   463: aload_0        
        //   464: getfield        de/szalkowski/activitylauncher/MyPackageInfo.activities:[Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   467: invokestatic    java/util/Arrays.sort:([Ljava/lang/Object;)V
        //   470: return         
        //   471: astore          9
        //   473: goto            379
        //   476: astore          9
        //   478: goto            379
        //   481: astore          9
        //   483: goto            349
        //   486: astore          9
        //   488: goto            349
        //   491: astore_2       
        //   492: goto            296
        //   495: astore_2       
        //   496: goto            296
        //   499: astore_1       
        //   500: goto            180
        //   503: astore_1       
        //   504: goto            180
        //   507: astore_2       
        //   508: goto            159
        //   511: astore          9
        //   513: goto            142
        //   516: astore          9
        //   518: goto            142
        //   521: astore          9
        //   523: goto            128
        //   526: astore          9
        //   528: goto            128
        //   531: astore          9
        //   533: goto            233
        //   536: astore          9
        //   538: goto            233
        //   541: astore          9
        //   543: goto            205
        //   546: astore          9
        //   548: goto            205
        //   551: astore          9
        //   553: goto            80
        //   556: astore          9
        //   558: goto            80
        //   561: astore          9
        //   563: goto            51
        //   566: astore          9
        //   568: goto            51
        //   571: astore          8
        //   573: goto            18
        //   576: astore          8
        //   578: goto            18
        //   581: goto            454
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  4      18     571    576    Ljava/security/NoSuchAlgorithmException;
        //  4      18     576    581    Ljavax/crypto/NoSuchPaddingException;
        //  37     51     561    566    Ljava/security/NoSuchAlgorithmException;
        //  37     51     566    571    Ljavax/crypto/NoSuchPaddingException;
        //  66     80     551    556    Ljava/security/NoSuchAlgorithmException;
        //  66     80     556    561    Ljavax/crypto/NoSuchPaddingException;
        //  66     80     189    551    Ljava/lang/ClassCastException;
        //  80     93     189    551    Ljava/lang/ClassCastException;
        //  114    128    521    526    Ljava/security/NoSuchAlgorithmException;
        //  114    128    526    531    Ljavax/crypto/NoSuchPaddingException;
        //  128    142    511    516    Ljava/security/NoSuchAlgorithmException;
        //  128    142    516    521    Ljavax/crypto/NoSuchPaddingException;
        //  128    142    260    282    Ljava/lang/Exception;
        //  142    159    260    282    Ljava/lang/Exception;
        //  166    180    499    503    Ljava/security/NoSuchAlgorithmException;
        //  166    180    503    507    Ljavax/crypto/NoSuchPaddingException;
        //  191    205    541    546    Ljava/security/NoSuchAlgorithmException;
        //  191    205    546    551    Ljavax/crypto/NoSuchPaddingException;
        //  219    233    531    536    Ljava/security/NoSuchAlgorithmException;
        //  219    233    536    541    Ljavax/crypto/NoSuchPaddingException;
        //  261    275    278    282    Ljava/security/NoSuchAlgorithmException;
        //  261    275    507    511    Ljavax/crypto/NoSuchPaddingException;
        //  282    296    491    495    Ljava/security/NoSuchAlgorithmException;
        //  282    296    495    499    Ljavax/crypto/NoSuchPaddingException;
        //  335    349    481    486    Ljava/security/NoSuchAlgorithmException;
        //  335    349    486    491    Ljavax/crypto/NoSuchPaddingException;
        //  365    379    471    476    Ljava/security/NoSuchAlgorithmException;
        //  365    379    476    481    Ljavax/crypto/NoSuchPaddingException;
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    
    private static int countActivitiesFromInfo(final PackageInfo p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: ldc             "DES"
        //     4: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     7: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    10: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    13: pop            
        //    14: iconst_0       
        //    15: istore_2       
        //    16: aload_0        
        //    17: getfield        android/content/pm/PackageInfo.activities:[Landroid/content/pm/ActivityInfo;
        //    20: astore_0       
        //    21: aload_0        
        //    22: arraylength    
        //    23: istore          4
        //    25: iconst_0       
        //    26: istore_1       
        //    27: iload_1        
        //    28: iload           4
        //    30: if_icmpge       99
        //    33: aload_0        
        //    34: iload_1        
        //    35: aaload         
        //    36: astore          5
        //    38: ldc             "cipherName-83"
        //    40: ldc             "DES"
        //    42: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    45: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    48: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    51: pop            
        //    52: iload_2        
        //    53: istore_3       
        //    54: aload           5
        //    56: invokevirtual   android/content/pm/ActivityInfo.isEnabled:()Z
        //    59: ifeq            90
        //    62: iload_2        
        //    63: istore_3       
        //    64: aload           5
        //    66: getfield        android/content/pm/ActivityInfo.exported:Z
        //    69: ifeq            90
        //    72: ldc             "cipherName-84"
        //    74: ldc             "DES"
        //    76: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    79: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    82: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    85: pop            
        //    86: iload_2        
        //    87: iconst_1       
        //    88: iadd           
        //    89: istore_3       
        //    90: iload_1        
        //    91: iconst_1       
        //    92: iadd           
        //    93: istore_1       
        //    94: iload_3        
        //    95: istore_2       
        //    96: goto            27
        //    99: iload_2        
        //   100: ireturn        
        //   101: astore          5
        //   103: goto            86
        //   106: astore          5
        //   108: goto            86
        //   111: astore          6
        //   113: goto            52
        //   116: astore          6
        //   118: goto            52
        //   121: astore          5
        //   123: goto            14
        //   126: astore          5
        //   128: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     121    126    Ljava/security/NoSuchAlgorithmException;
        //  0      14     126    131    Ljavax/crypto/NoSuchPaddingException;
        //  38     52     111    116    Ljava/security/NoSuchAlgorithmException;
        //  38     52     116    121    Ljavax/crypto/NoSuchPaddingException;
        //  72     86     101    106    Ljava/security/NoSuchAlgorithmException;
        //  72     86     106    111    Ljavax/crypto/NoSuchPaddingException;
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
    
    @Override
    public int compareTo(final MyPackageInfo myPackageInfo) {
        while (true) {
            try {
                Log.d("cipherName-91", Cipher.getInstance("DES").getAlgorithm());
                final int compareTo = this.name.compareTo(myPackageInfo.name);
                if (compareTo != 0) {
                    return compareTo;
                }
                return this.package_name.compareTo(myPackageInfo.package_name);
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
    
    @Override
    public boolean equals(final Object ex) {
        while (true) {
            try {
                Log.d("cipherName-92", Cipher.getInstance("DES").getAlgorithm());
                Label_0042: {
                    if (ex.getClass().equals(MyPackageInfo.class)) {
                        break Label_0042;
                    }
                    try {
                        Log.d("cipherName-93", Cipher.getInstance("DES").getAlgorithm());
                        return false;
                        return this.package_name.equals(((MyPackageInfo)ex).package_name);
                    }
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex) {}
                }
            }
            catch (NoSuchAlgorithmException ex2) {
                continue;
            }
            catch (NoSuchPaddingException ex3) {
                continue;
            }
            break;
        }
    }
    
    public int getActivitiesCount() {
        try {
            Log.d("cipherName-85", Cipher.getInstance("DES").getAlgorithm());
            return this.activities.length;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.activities.length;
        }
        catch (NoSuchPaddingException ex2) {
            return this.activities.length;
        }
    }
    
    public MyActivityInfo getActivity(final int n) {
        try {
            Log.d("cipherName-86", Cipher.getInstance("DES").getAlgorithm());
            return this.activities[n];
        }
        catch (NoSuchAlgorithmException ex) {
            return this.activities[n];
        }
        catch (NoSuchPaddingException ex2) {
            return this.activities[n];
        }
    }
    
    public BitmapDrawable getIcon() {
        try {
            Log.d("cipherName-88", Cipher.getInstance("DES").getAlgorithm());
            return this.icon;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.icon;
        }
        catch (NoSuchPaddingException ex2) {
            return this.icon;
        }
    }
    
    public String getIconResourceName() {
        try {
            Log.d("cipherName-90", Cipher.getInstance("DES").getAlgorithm());
            return this.icon_resource_name;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.icon_resource_name;
        }
        catch (NoSuchPaddingException ex2) {
            return this.icon_resource_name;
        }
    }
    
    public String getName() {
        try {
            Log.d("cipherName-89", Cipher.getInstance("DES").getAlgorithm());
            return this.name;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.name;
        }
        catch (NoSuchPaddingException ex2) {
            return this.name;
        }
    }
    
    public String getPackageName() {
        try {
            Log.d("cipherName-87", Cipher.getInstance("DES").getAlgorithm());
            return this.package_name;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.package_name;
        }
        catch (NoSuchPaddingException ex2) {
            return this.package_name;
        }
    }
}
