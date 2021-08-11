// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.content.ComponentName;

public class MyActivityInfo implements Comparable<MyActivityInfo>
{
    protected ComponentName component_name;
    protected BitmapDrawable icon;
    protected int icon_resource;
    protected String icon_resource_name;
    protected String name;
    
    public MyActivityInfo(final ComponentName p0, final PackageManager p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: ldc             "cipherName-132"
        //     6: ldc             "DES"
        //     8: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    11: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    14: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    17: pop            
        //    18: aload_0        
        //    19: aload_1        
        //    20: putfield        de/szalkowski/activitylauncher/MyActivityInfo.component_name:Landroid/content/ComponentName;
        //    23: ldc             "cipherName-133"
        //    25: ldc             "DES"
        //    27: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    30: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    33: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    36: pop            
        //    37: aload_2        
        //    38: aload_1        
        //    39: iconst_0       
        //    40: invokevirtual   android/content/pm/PackageManager.getActivityInfo:(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;
        //    43: astore_3       
        //    44: aload_0        
        //    45: aload_3        
        //    46: aload_2        
        //    47: invokevirtual   android/content/pm/ActivityInfo.loadLabel:(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;
        //    50: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //    55: putfield        de/szalkowski/activitylauncher/MyActivityInfo.name:Ljava/lang/String;
        //    58: ldc             "cipherName-134"
        //    60: ldc             "DES"
        //    62: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    65: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    68: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    71: pop            
        //    72: aload_0        
        //    73: aload_3        
        //    74: aload_2        
        //    75: invokevirtual   android/content/pm/ActivityInfo.loadIcon:(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;
        //    78: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //    81: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //    84: aload_0        
        //    85: aload_3        
        //    86: invokevirtual   android/content/pm/ActivityInfo.getIconResource:()I
        //    89: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
        //    92: aload_0        
        //    93: aconst_null    
        //    94: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource_name:Ljava/lang/String;
        //    97: aload_0        
        //    98: getfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
        //   101: ifeq            148
        //   104: ldc             "cipherName-137"
        //   106: ldc             "DES"
        //   108: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   111: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   114: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   117: pop            
        //   118: ldc             "cipherName-138"
        //   120: ldc             "DES"
        //   122: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   125: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   128: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   131: pop            
        //   132: aload_0        
        //   133: aload_2        
        //   134: aload_1        
        //   135: invokevirtual   android/content/pm/PackageManager.getResourcesForActivity:(Landroid/content/ComponentName;)Landroid/content/res/Resources;
        //   138: aload_0        
        //   139: getfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
        //   142: invokevirtual   android/content/res/Resources.getResourceName:(I)Ljava/lang/String;
        //   145: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource_name:Ljava/lang/String;
        //   148: return         
        //   149: astore          4
        //   151: ldc             "cipherName-135"
        //   153: ldc             "DES"
        //   155: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   158: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   161: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   164: pop            
        //   165: aload_0        
        //   166: aload_2        
        //   167: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
        //   170: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //   173: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //   176: goto            84
        //   179: astore_3       
        //   180: ldc             "cipherName-136"
        //   182: ldc             "DES"
        //   184: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   187: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   190: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   193: pop            
        //   194: aload_0        
        //   195: aload_1        
        //   196: invokevirtual   android/content/ComponentName.getShortClassName:()Ljava/lang/String;
        //   199: putfield        de/szalkowski/activitylauncher/MyActivityInfo.name:Ljava/lang/String;
        //   202: aload_0        
        //   203: aload_2        
        //   204: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
        //   207: checkcast       Landroid/graphics/drawable/BitmapDrawable;
        //   210: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
        //   213: aload_0        
        //   214: iconst_0       
        //   215: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
        //   218: goto            92
        //   221: astore_1       
        //   222: ldc             "cipherName-139"
        //   224: ldc             "DES"
        //   226: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   229: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   232: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   235: pop            
        //   236: return         
        //   237: astore_1       
        //   238: return         
        //   239: astore_1       
        //   240: return         
        //   241: astore_3       
        //   242: goto            132
        //   245: astore_3       
        //   246: goto            132
        //   249: astore_3       
        //   250: goto            118
        //   253: astore_3       
        //   254: goto            118
        //   257: astore_3       
        //   258: goto            194
        //   261: astore_3       
        //   262: goto            194
        //   265: astore          4
        //   267: goto            165
        //   270: astore          4
        //   272: goto            165
        //   275: astore          4
        //   277: goto            72
        //   280: astore          4
        //   282: goto            72
        //   285: astore_3       
        //   286: goto            37
        //   289: astore_3       
        //   290: goto            37
        //   293: astore_3       
        //   294: goto            18
        //   297: astore_3       
        //   298: goto            18
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  4      18     293    297    Ljava/security/NoSuchAlgorithmException;
        //  4      18     297    301    Ljavax/crypto/NoSuchPaddingException;
        //  23     37     285    289    Ljava/security/NoSuchAlgorithmException;
        //  23     37     289    293    Ljavax/crypto/NoSuchPaddingException;
        //  23     37     179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  37     58     179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  58     72     275    280    Ljava/security/NoSuchAlgorithmException;
        //  58     72     280    285    Ljavax/crypto/NoSuchPaddingException;
        //  58     72     149    275    Ljava/lang/ClassCastException;
        //  58     72     179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  72     84     149    275    Ljava/lang/ClassCastException;
        //  72     84     179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  84     92     179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  104    118    249    253    Ljava/security/NoSuchAlgorithmException;
        //  104    118    253    257    Ljavax/crypto/NoSuchPaddingException;
        //  118    132    241    245    Ljava/security/NoSuchAlgorithmException;
        //  118    132    245    249    Ljavax/crypto/NoSuchPaddingException;
        //  118    132    221    241    Ljava/lang/Exception;
        //  132    148    221    241    Ljava/lang/Exception;
        //  151    165    265    270    Ljava/security/NoSuchAlgorithmException;
        //  151    165    270    275    Ljavax/crypto/NoSuchPaddingException;
        //  151    165    179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  165    176    179    265    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  180    194    257    261    Ljava/security/NoSuchAlgorithmException;
        //  180    194    261    265    Ljavax/crypto/NoSuchPaddingException;
        //  222    236    237    239    Ljava/security/NoSuchAlgorithmException;
        //  222    236    239    241    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 144, Size: 144
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
    
    @Override
    public int compareTo(final MyActivityInfo myActivityInfo) {
        while (true) {
            try {
                Log.d("cipherName-144", Cipher.getInstance("DES").getAlgorithm());
                final int compareTo = this.name.compareTo(myActivityInfo.name);
                if (compareTo != 0) {
                    return compareTo;
                }
                return this.component_name.compareTo(myActivityInfo.component_name);
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
                Log.d("cipherName-145", Cipher.getInstance("DES").getAlgorithm());
                Label_0042: {
                    if (ex.getClass().equals(MyPackageInfo.class)) {
                        break Label_0042;
                    }
                    try {
                        Log.d("cipherName-146", Cipher.getInstance("DES").getAlgorithm());
                        return false;
                        return this.component_name.equals((Object)((MyActivityInfo)ex).component_name);
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
    
    public ComponentName getComponentName() {
        try {
            Log.d("cipherName-140", Cipher.getInstance("DES").getAlgorithm());
            return this.component_name;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.component_name;
        }
        catch (NoSuchPaddingException ex2) {
            return this.component_name;
        }
    }
    
    public BitmapDrawable getIcon() {
        try {
            Log.d("cipherName-141", Cipher.getInstance("DES").getAlgorithm());
            return this.icon;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.icon;
        }
        catch (NoSuchPaddingException ex2) {
            return this.icon;
        }
    }
    
    public String getIconResouceName() {
        try {
            Log.d("cipherName-143", Cipher.getInstance("DES").getAlgorithm());
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
            Log.d("cipherName-142", Cipher.getInstance("DES").getAlgorithm());
            return this.name;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.name;
        }
        catch (NoSuchPaddingException ex2) {
            return this.name;
        }
    }
}
