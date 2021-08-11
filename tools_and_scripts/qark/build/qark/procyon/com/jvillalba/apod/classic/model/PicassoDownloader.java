// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.model;

import com.squareup.picasso.Picasso;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;
import com.squareup.picasso.Target;

public class PicassoDownloader implements Target
{
    private final Context context;
    private final String folder_apod;
    private final String name;
    
    public PicassoDownloader(final String s, final Context context) {
        this.folder_apod = "/NasaApod/";
        while (true) {
            try {
                Log.d("cipherName-30", Cipher.getInstance("DES").getAlgorithm());
                this.name = s.concat(".png");
                this.context = context;
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
    
    private void saveImage(final Bitmap p0) {
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
        //    14: new             Ljava/io/File;
        //    17: dup            
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: getstatic       android/os/Environment.DIRECTORY_PICTURES:Ljava/lang/String;
        //    28: invokestatic    android/os/Environment.getExternalStoragePublicDirectory:(Ljava/lang/String;)Ljava/io/File;
        //    31: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    37: ldc             "/NasaApod/"
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    45: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    48: astore_2       
        //    49: aload_2        
        //    50: invokevirtual   java/io/File.mkdirs:()Z
        //    53: pop            
        //    54: new             Ljava/io/File;
        //    57: dup            
        //    58: aload_2        
        //    59: aload_0        
        //    60: getfield        com/jvillalba/apod/classic/model/PicassoDownloader.name:Ljava/lang/String;
        //    63: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    66: astore_2       
        //    67: ldc             "cipherName-34"
        //    69: ldc             "DES"
        //    71: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    74: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    77: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    80: pop            
        //    81: aload_2        
        //    82: invokevirtual   java/io/File.isFile:()Z
        //    85: ifne            165
        //    88: ldc             "cipherName-35"
        //    90: ldc             "DES"
        //    92: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    95: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    98: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   101: pop            
        //   102: aload_2        
        //   103: invokevirtual   java/io/File.createNewFile:()Z
        //   106: pop            
        //   107: new             Ljava/io/FileOutputStream;
        //   110: dup            
        //   111: aload_2        
        //   112: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   115: astore_3       
        //   116: aload_1        
        //   117: getstatic       android/graphics/Bitmap$CompressFormat.PNG:Landroid/graphics/Bitmap$CompressFormat;
        //   120: bipush          100
        //   122: aload_3        
        //   123: invokevirtual   android/graphics/Bitmap.compress:(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //   126: pop            
        //   127: aload_3        
        //   128: invokevirtual   java/io/FileOutputStream.close:()V
        //   131: aload_0        
        //   132: getfield        com/jvillalba/apod/classic/model/PicassoDownloader.context:Landroid/content/Context;
        //   135: ldc             "Download OK. /Pictures/NasaApod/"
        //   137: iconst_0       
        //   138: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //   141: invokevirtual   android/widget/Toast.show:()V
        //   144: aload_0        
        //   145: getfield        com/jvillalba/apod/classic/model/PicassoDownloader.context:Landroid/content/Context;
        //   148: iconst_1       
        //   149: anewarray       Ljava/lang/String;
        //   152: dup            
        //   153: iconst_0       
        //   154: aload_2        
        //   155: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   158: aastore        
        //   159: aconst_null    
        //   160: aconst_null    
        //   161: invokestatic    android/media/MediaScannerConnection.scanFile:(Landroid/content/Context;[Ljava/lang/String;[Ljava/lang/String;Landroid/media/MediaScannerConnection$OnScanCompletedListener;)V
        //   164: return         
        //   165: ldc             "cipherName-36"
        //   167: ldc             "DES"
        //   169: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   172: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   175: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   178: pop            
        //   179: aload_0        
        //   180: getfield        com/jvillalba/apod/classic/model/PicassoDownloader.context:Landroid/content/Context;
        //   183: ldc             "This image has already been downloaded"
        //   185: iconst_0       
        //   186: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //   189: invokevirtual   android/widget/Toast.show:()V
        //   192: return         
        //   193: astore_1       
        //   194: ldc             "cipherName-37"
        //   196: ldc             "DES"
        //   198: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   201: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   204: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   207: pop            
        //   208: aload_0        
        //   209: getfield        com/jvillalba/apod/classic/model/PicassoDownloader.context:Landroid/content/Context;
        //   212: ldc             "ERROR to Write Image"
        //   214: iconst_0       
        //   215: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //   218: invokevirtual   android/widget/Toast.show:()V
        //   221: return         
        //   222: astore_1       
        //   223: goto            208
        //   226: astore_1       
        //   227: goto            208
        //   230: astore_1       
        //   231: goto            179
        //   234: astore_1       
        //   235: goto            179
        //   238: astore_3       
        //   239: goto            102
        //   242: astore_3       
        //   243: goto            102
        //   246: astore_3       
        //   247: goto            81
        //   250: astore_3       
        //   251: goto            81
        //   254: astore_2       
        //   255: goto            14
        //   258: astore_2       
        //   259: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     254    258    Ljava/security/NoSuchAlgorithmException;
        //  0      14     258    262    Ljavax/crypto/NoSuchPaddingException;
        //  67     81     246    250    Ljava/security/NoSuchAlgorithmException;
        //  67     81     250    254    Ljavax/crypto/NoSuchPaddingException;
        //  67     81     193    230    Ljava/lang/Exception;
        //  81     88     193    230    Ljava/lang/Exception;
        //  88     102    238    242    Ljava/security/NoSuchAlgorithmException;
        //  88     102    242    246    Ljavax/crypto/NoSuchPaddingException;
        //  88     102    193    230    Ljava/lang/Exception;
        //  102    164    193    230    Ljava/lang/Exception;
        //  165    179    230    234    Ljava/security/NoSuchAlgorithmException;
        //  165    179    234    238    Ljavax/crypto/NoSuchPaddingException;
        //  165    179    193    230    Ljava/lang/Exception;
        //  179    192    193    230    Ljava/lang/Exception;
        //  194    208    222    226    Ljava/security/NoSuchAlgorithmException;
        //  194    208    226    230    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0102:
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
    
    @Override
    public void onBitmapFailed(final Drawable drawable) {
        while (true) {
            try {
                Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
                Toast.makeText(this.context, (CharSequence)"ERROR to Download Image", 0).show();
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
    public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
        while (true) {
            try {
                Log.d("cipherName-32", Cipher.getInstance("DES").getAlgorithm());
                this.saveImage(bitmap);
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
    public void onPrepareLoad(final Drawable drawable) {
        try {
            Log.d("cipherName-31", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex2) {}
    }
}
