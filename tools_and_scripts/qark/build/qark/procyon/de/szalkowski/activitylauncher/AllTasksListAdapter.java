// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import java.util.List;
import android.content.Context;
import android.widget.BaseExpandableListAdapter;

public class AllTasksListAdapter extends BaseExpandableListAdapter
{
    protected Context context;
    protected List<MyPackageInfo> packages;
    
    public AllTasksListAdapter(final Context p0, final AsyncProvider.Updater p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   android/widget/BaseExpandableListAdapter.<init>:()V
        //     4: aload_0        
        //     5: aconst_null    
        //     6: putfield        de/szalkowski/activitylauncher/AllTasksListAdapter.packages:Ljava/util/List;
        //     9: ldc             "cipherName-147"
        //    11: ldc             "DES"
        //    13: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    16: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    19: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    22: pop            
        //    23: aload_0        
        //    24: aload_1        
        //    25: putfield        de/szalkowski/activitylauncher/AllTasksListAdapter.context:Landroid/content/Context;
        //    28: aload_1        
        //    29: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    32: astore          4
        //    34: aload           4
        //    36: invokestatic    de/szalkowski/activitylauncher/PackageManagerCache.getPackageManagerCache:(Landroid/content/pm/PackageManager;)Lde/szalkowski/activitylauncher/PackageManagerCache;
        //    39: astore_1       
        //    40: aload           4
        //    42: iconst_0       
        //    43: invokevirtual   android/content/pm/PackageManager.getInstalledPackages:(I)Ljava/util/List;
        //    46: astore          4
        //    48: aload_0        
        //    49: new             Ljava/util/ArrayList;
        //    52: dup            
        //    53: aload           4
        //    55: invokeinterface java/util/List.size:()I
        //    60: invokespecial   java/util/ArrayList.<init>:(I)V
        //    63: putfield        de/szalkowski/activitylauncher/AllTasksListAdapter.packages:Ljava/util/List;
        //    66: aload_2        
        //    67: aload           4
        //    69: invokeinterface java/util/List.size:()I
        //    74: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.updateMax:(I)V
        //    77: aload_2        
        //    78: iconst_0       
        //    79: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.update:(I)V
        //    82: iconst_0       
        //    83: istore_3       
        //    84: iload_3        
        //    85: aload           4
        //    87: invokeinterface java/util/List.size:()I
        //    92: if_icmpge       219
        //    95: ldc             "cipherName-148"
        //    97: ldc             "DES"
        //    99: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   102: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   105: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   108: pop            
        //   109: aload_2        
        //   110: iload_3        
        //   111: iconst_1       
        //   112: iadd           
        //   113: invokevirtual   de/szalkowski/activitylauncher/AsyncProvider$Updater.update:(I)V
        //   116: aload           4
        //   118: iload_3        
        //   119: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   124: checkcast       Landroid/content/pm/PackageInfo;
        //   127: astore          5
        //   129: ldc             "cipherName-149"
        //   131: ldc             "DES"
        //   133: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   136: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   139: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   142: pop            
        //   143: aload_1        
        //   144: aload           5
        //   146: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //   149: invokevirtual   de/szalkowski/activitylauncher/PackageManagerCache.getPackageInfo:(Ljava/lang/String;)Lde/szalkowski/activitylauncher/MyPackageInfo;
        //   152: astore          5
        //   154: aload           5
        //   156: invokevirtual   de/szalkowski/activitylauncher/MyPackageInfo.getActivitiesCount:()I
        //   159: ifle            188
        //   162: ldc             "cipherName-150"
        //   164: ldc             "DES"
        //   166: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   169: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   172: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   175: pop            
        //   176: aload_0        
        //   177: getfield        de/szalkowski/activitylauncher/AllTasksListAdapter.packages:Ljava/util/List;
        //   180: aload           5
        //   182: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   187: pop            
        //   188: iload_3        
        //   189: iconst_1       
        //   190: iadd           
        //   191: istore_3       
        //   192: goto            84
        //   195: astore          5
        //   197: ldc             "cipherName-151"
        //   199: ldc             "DES"
        //   201: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   204: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   207: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   210: pop            
        //   211: goto            188
        //   214: astore          5
        //   216: goto            188
        //   219: aload_0        
        //   220: getfield        de/szalkowski/activitylauncher/AllTasksListAdapter.packages:Ljava/util/List;
        //   223: invokestatic    java/util/Collections.sort:(Ljava/util/List;)V
        //   226: return         
        //   227: astore          5
        //   229: goto            188
        //   232: astore          6
        //   234: goto            176
        //   237: astore          6
        //   239: goto            176
        //   242: astore          6
        //   244: goto            143
        //   247: astore          6
        //   249: goto            143
        //   252: astore          5
        //   254: goto            109
        //   257: astore          5
        //   259: goto            109
        //   262: astore          4
        //   264: goto            23
        //   267: astore          4
        //   269: goto            23
        //    Signature:
        //  (Landroid/content/Context;Lde/szalkowski/activitylauncher/AsyncProvider$Updater;)V [from metadata: (Landroid/content/Context;Lde/szalkowski/activitylauncher/AsyncProvider<Lde/szalkowski/activitylauncher/AllTasksListAdapter;>.Updater;)V]
        //  
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  9      23     262    267    Ljava/security/NoSuchAlgorithmException;
        //  9      23     267    272    Ljavax/crypto/NoSuchPaddingException;
        //  95     109    252    257    Ljava/security/NoSuchAlgorithmException;
        //  95     109    257    262    Ljavax/crypto/NoSuchPaddingException;
        //  129    143    242    247    Ljava/security/NoSuchAlgorithmException;
        //  129    143    247    252    Ljavax/crypto/NoSuchPaddingException;
        //  129    143    195    219    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  143    162    195    219    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  162    176    232    237    Ljava/security/NoSuchAlgorithmException;
        //  162    176    237    242    Ljavax/crypto/NoSuchPaddingException;
        //  162    176    195    219    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  176    188    195    219    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  197    211    214    219    Ljava/security/NoSuchAlgorithmException;
        //  197    211    227    232    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0176:
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
    
    public Object getChild(final int n, final int n2) {
        try {
            Log.d("cipherName-152", Cipher.getInstance("DES").getAlgorithm());
            return this.packages.get(n).getActivity(n2);
        }
        catch (NoSuchAlgorithmException ex) {
            return this.packages.get(n).getActivity(n2);
        }
        catch (NoSuchPaddingException ex2) {
            return this.packages.get(n).getActivity(n2);
        }
    }
    
    public long getChildId(final int n, final int n2) {
        try {
            Log.d("cipherName-153", Cipher.getInstance("DES").getAlgorithm());
            return n2;
        }
        catch (NoSuchAlgorithmException ex) {
            return n2;
        }
        catch (NoSuchPaddingException ex2) {
            return n2;
        }
    }
    
    public View getChildView(final int n, final int n2, final boolean b, final View view, final ViewGroup viewGroup) {
        while (true) {
            try {
                Log.d("cipherName-154", Cipher.getInstance("DES").getAlgorithm());
                final MyActivityInfo myActivityInfo = (MyActivityInfo)this.getChild(n, n2);
                final View inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903042, (ViewGroup)null);
                ((TextView)inflate.findViewById(16908308)).setText((CharSequence)myActivityInfo.getName());
                ((TextView)inflate.findViewById(16908309)).setText((CharSequence)myActivityInfo.getComponentName().getClassName());
                ((ImageView)inflate.findViewById(16908294)).setImageDrawable((Drawable)myActivityInfo.getIcon());
                return inflate;
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
    
    public int getChildrenCount(final int n) {
        try {
            Log.d("cipherName-155", Cipher.getInstance("DES").getAlgorithm());
            return this.packages.get(n).getActivitiesCount();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.packages.get(n).getActivitiesCount();
        }
        catch (NoSuchPaddingException ex2) {
            return this.packages.get(n).getActivitiesCount();
        }
    }
    
    public Object getGroup(final int n) {
        try {
            Log.d("cipherName-156", Cipher.getInstance("DES").getAlgorithm());
            return this.packages.get(n);
        }
        catch (NoSuchAlgorithmException ex) {
            return this.packages.get(n);
        }
        catch (NoSuchPaddingException ex2) {
            return this.packages.get(n);
        }
    }
    
    public int getGroupCount() {
        try {
            Log.d("cipherName-157", Cipher.getInstance("DES").getAlgorithm());
            return this.packages.size();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.packages.size();
        }
        catch (NoSuchPaddingException ex2) {
            return this.packages.size();
        }
    }
    
    public long getGroupId(final int n) {
        try {
            Log.d("cipherName-158", Cipher.getInstance("DES").getAlgorithm());
            return n;
        }
        catch (NoSuchAlgorithmException ex) {
            return n;
        }
        catch (NoSuchPaddingException ex2) {
            return n;
        }
    }
    
    public View getGroupView(final int n, final boolean b, final View view, final ViewGroup viewGroup) {
        while (true) {
            try {
                Log.d("cipherName-159", Cipher.getInstance("DES").getAlgorithm());
                final MyPackageInfo myPackageInfo = (MyPackageInfo)this.getGroup(n);
                final View inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903043, (ViewGroup)null);
                ((TextView)inflate.findViewById(16908308)).setText((CharSequence)myPackageInfo.getName());
                ((ImageView)inflate.findViewById(16908294)).setImageDrawable((Drawable)myPackageInfo.getIcon());
                return inflate;
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
    
    public boolean hasStableIds() {
        try {
            Log.d("cipherName-160", Cipher.getInstance("DES").getAlgorithm());
            return false;
        }
        catch (NoSuchAlgorithmException ex) {
            return false;
        }
        catch (NoSuchPaddingException ex2) {
            return false;
        }
    }
    
    public boolean isChildSelectable(final int n, final int n2) {
        try {
            Log.d("cipherName-161", Cipher.getInstance("DES").getAlgorithm());
            return true;
        }
        catch (NoSuchAlgorithmException ex) {
            return true;
        }
        catch (NoSuchPaddingException ex2) {
            return true;
        }
    }
}
