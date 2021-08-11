// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.View;
import android.content.res.Resources;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager;
import android.content.Context;
import android.widget.BaseAdapter;

public class IconListAdapter extends BaseAdapter
{
    private Context context;
    private String[] icons;
    private PackageManager pm;
    
    public IconListAdapter(final Context p0, final AsyncProvider.Updater p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   android/widget/BaseAdapter.<init>:()V
        //     4: ldc             "cipherName-119"
        //     6: ldc             "DES"
        //     8: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    11: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    14: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    17: pop            
        //    18: new             Ljava/util/TreeSet;
        //    21: dup            
        //    22: invokespecial   java/util/TreeSet.<init>:()V
        //    25: astore          5
        //    27: aload_0        
        //    28: aload_1        
        //    29: putfield        de/szalkowski/activitylauncher/IconListAdapter.context:Landroid/content/Context;
        //    32: aload_0        
        //    33: aload_1        
        //    34: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    37: putfield        de/szalkowski/activitylauncher/IconListAdapter.pm:Landroid/content/pm/PackageManager;
        //    40: aload_0        
        //    41: getfield        de/szalkowski/activitylauncher/IconListAdapter.pm:Landroid/content/pm/PackageManager;
        //    44: iconst_0       
        //    45: invokevirtual   android/content/pm/PackageManager.getInstalledPackages:(I)Ljava/util/List;
        //    48: astore_1       
        //    49: aload_2        
        //    50: aload_1        
        //    51: invokeinterface java/util/List.size:()I
        //    56: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.updateMax:(I)V
        //    59: aload_2        
        //    60: iconst_0       
        //    61: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.update:(I)V
        //    64: aload_0        
        //    65: getfield        de/szalkowski/activitylauncher/IconListAdapter.pm:Landroid/content/pm/PackageManager;
        //    68: invokestatic    de/szalkowski/activitylauncher/PackageManagerCache.getPackageManagerCache:(Landroid/content/pm/PackageManager;)Lde/szalkowski/activitylauncher/PackageManagerCache;
        //    71: astore          6
        //    73: iconst_0       
        //    74: istore_3       
        //    75: iload_3        
        //    76: aload_1        
        //    77: invokeinterface java/util/List.size:()I
        //    82: if_icmpge       242
        //    85: ldc             "cipherName-120"
        //    87: ldc             "DES"
        //    89: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    92: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    95: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    98: pop            
        //    99: aload_2        
        //   100: iload_3        
        //   101: iconst_1       
        //   102: iadd           
        //   103: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.update:(I)V
        //   106: aload_1        
        //   107: iload_3        
        //   108: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   113: checkcast       Landroid/content/pm/PackageInfo;
        //   116: astore          7
        //   118: ldc             "cipherName-121"
        //   120: ldc             "DES"
        //   122: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   125: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   128: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   131: pop            
        //   132: aload           6
        //   134: aload           7
        //   136: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //   139: invokevirtual   de/szalkowski/activitylauncher/PackageManagerCache.getPackageInfo:(Ljava/lang/String;)Lde/szalkowski/activitylauncher/MyPackageInfo;
        //   142: astore          7
        //   144: iconst_0       
        //   145: istore          4
        //   147: iload           4
        //   149: aload           7
        //   151: invokevirtual   de/szalkowski/activitylauncher/MyPackageInfo.getActivitiesCount:()I
        //   154: if_icmpge       235
        //   157: ldc             "cipherName-122"
        //   159: ldc             "DES"
        //   161: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   164: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   167: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   170: pop            
        //   171: aload           7
        //   173: iload           4
        //   175: invokevirtual   de/szalkowski/activitylauncher/MyPackageInfo.getActivity:(I)Lde/szalkowski/activitylauncher/MyActivityInfo;
        //   178: invokevirtual   de/szalkowski/activitylauncher/MyActivityInfo.getIconResouceName:()Ljava/lang/String;
        //   181: astore          8
        //   183: aload           8
        //   185: ifnull          210
        //   188: ldc             "cipherName-123"
        //   190: ldc             "DES"
        //   192: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   195: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   198: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   201: pop            
        //   202: aload           5
        //   204: aload           8
        //   206: invokevirtual   java/util/TreeSet.add:(Ljava/lang/Object;)Z
        //   209: pop            
        //   210: iload           4
        //   212: iconst_1       
        //   213: iadd           
        //   214: istore          4
        //   216: goto            147
        //   219: astore          7
        //   221: ldc             "cipherName-124"
        //   223: ldc             "DES"
        //   225: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   228: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   231: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   234: pop            
        //   235: iload_3        
        //   236: iconst_1       
        //   237: iadd           
        //   238: istore_3       
        //   239: goto            75
        //   242: aload_0        
        //   243: aload           5
        //   245: invokevirtual   java/util/TreeSet.size:()I
        //   248: anewarray       Ljava/lang/String;
        //   251: putfield        de/szalkowski/activitylauncher/IconListAdapter.icons:[Ljava/lang/String;
        //   254: aload_0        
        //   255: aload           5
        //   257: aload_0        
        //   258: getfield        de/szalkowski/activitylauncher/IconListAdapter.icons:[Ljava/lang/String;
        //   261: invokevirtual   java/util/TreeSet.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   264: checkcast       [Ljava/lang/String;
        //   267: putfield        de/szalkowski/activitylauncher/IconListAdapter.icons:[Ljava/lang/String;
        //   270: return         
        //   271: astore          7
        //   273: goto            235
        //   276: astore          7
        //   278: goto            235
        //   281: astore          9
        //   283: goto            202
        //   286: astore          9
        //   288: goto            202
        //   291: astore          8
        //   293: goto            171
        //   296: astore          8
        //   298: goto            171
        //   301: astore          8
        //   303: goto            132
        //   306: astore          8
        //   308: goto            132
        //   311: astore          7
        //   313: goto            99
        //   316: astore          7
        //   318: goto            99
        //   321: astore          5
        //   323: goto            18
        //   326: astore          5
        //   328: goto            18
        //    Signature:
        //  (Landroid/content/Context;Lde/szalkowski/activitylauncher/AsyncProvider$Updater;)V [from metadata: (Landroid/content/Context;Lde/szalkowski/activitylauncher/AsyncProvider<Lde/szalkowski/activitylauncher/IconListAdapter;>.Updater;)V]
        //  
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  4      18     321    326    Ljava/security/NoSuchAlgorithmException;
        //  4      18     326    331    Ljavax/crypto/NoSuchPaddingException;
        //  85     99     311    316    Ljava/security/NoSuchAlgorithmException;
        //  85     99     316    321    Ljavax/crypto/NoSuchPaddingException;
        //  118    132    301    306    Ljava/security/NoSuchAlgorithmException;
        //  118    132    306    311    Ljavax/crypto/NoSuchPaddingException;
        //  118    132    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  132    144    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  147    157    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  157    171    291    296    Ljava/security/NoSuchAlgorithmException;
        //  157    171    296    301    Ljavax/crypto/NoSuchPaddingException;
        //  157    171    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  171    183    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  188    202    281    286    Ljava/security/NoSuchAlgorithmException;
        //  188    202    286    291    Ljavax/crypto/NoSuchPaddingException;
        //  188    202    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  202    210    219    281    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  221    235    271    276    Ljava/security/NoSuchAlgorithmException;
        //  221    235    276    281    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0171:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public static Drawable getIcon(String substring, final PackageManager packageManager) {
        while (true) {
            try {
                Log.d("cipherName-128", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-129", Cipher.getInstance("DES").getAlgorithm());
                        final String substring2 = ((String)substring).substring(0, ((String)substring).indexOf(58));
                        final String substring3 = ((String)substring).substring(((String)substring).indexOf(58) + 1, ((String)substring).indexOf(47));
                        substring = ((String)substring).substring(((String)substring).indexOf(47) + 1, ((String)substring).length());
                        final Resources resourcesForApplication = packageManager.getResourcesForApplication(substring2);
                        return resourcesForApplication.getDrawable(resourcesForApplication.getIdentifier((String)substring, substring3, substring2));
                    }
                    catch (Exception substring) {
                        try {
                            Log.d("cipherName-130", Cipher.getInstance("DES").getAlgorithm());
                            return packageManager.getDefaultActivityIcon();
                        }
                        catch (NoSuchAlgorithmException substring) {}
                        catch (NoSuchPaddingException substring) {}
                    }
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex2) {}
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
    
    public int getCount() {
        try {
            Log.d("cipherName-125", Cipher.getInstance("DES").getAlgorithm());
            return this.icons.length;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.icons.length;
        }
        catch (NoSuchPaddingException ex2) {
            return this.icons.length;
        }
    }
    
    public Object getItem(final int n) {
        try {
            Log.d("cipherName-126", Cipher.getInstance("DES").getAlgorithm());
            return this.icons[n];
        }
        catch (NoSuchAlgorithmException ex) {
            return this.icons[n];
        }
        catch (NoSuchPaddingException ex2) {
            return this.icons[n];
        }
    }
    
    public long getItemId(final int n) {
        try {
            Log.d("cipherName-127", Cipher.getInstance("DES").getAlgorithm());
            return 0L;
        }
        catch (NoSuchAlgorithmException ex) {
            return 0L;
        }
        catch (NoSuchPaddingException ex2) {
            return 0L;
        }
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        while (true) {
            try {
                Log.d("cipherName-131", Cipher.getInstance("DES").getAlgorithm());
                final ImageView imageView = new ImageView(this.context);
                imageView.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(50, 50));
                imageView.setImageDrawable(getIcon(this.icons[n], this.pm));
                return (View)imageView;
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
}
