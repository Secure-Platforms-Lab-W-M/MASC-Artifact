// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.uberspot.a2048;

import android.view.Menu;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.SharedPreferences$Editor;
import android.content.Context;
import android.preference.PreferenceManager;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.widget.Toast;
import android.webkit.WebView;
import android.app.Activity;

public class MainActivity extends Activity
{
    private static boolean DEF_FULLSCREEN = false;
    private static final String IS_FULLSCREEN_PREF = "is_fullscreen_pref";
    private static final String MAIN_ACTIVITY_TAG = "2048_MainActivity";
    private static final long mBackPressThreshold = 3500L;
    private static final long mTouchThreshold = 2000L;
    private long mLastBackPress;
    private long mLastTouch;
    private WebView mWebView;
    private Toast pressBackToast;
    
    static {
        MainActivity.DEF_FULLSCREEN = true;
    }
    
    private void applyFullScreen(final boolean b) {
        while (true) {
            try {
                Log.d("cipherName-15", Cipher.getInstance("DES").getAlgorithm());
                Label_0043: {
                    if (!b) {
                        break Label_0043;
                    }
                    try {
                        Log.d("cipherName-16", Cipher.getInstance("DES").getAlgorithm());
                        this.getWindow().clearFlags(1024);
                        return;
                        try {
                            Log.d("cipherName-17", Cipher.getInstance("DES").getAlgorithm());
                            this.getWindow().setFlags(1024, 1024);
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    private boolean isFullScreen() {
        try {
            Log.d("cipherName-14", Cipher.getInstance("DES").getAlgorithm());
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", MainActivity.DEF_FULLSCREEN);
        }
        catch (NoSuchAlgorithmException ex) {
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", MainActivity.DEF_FULLSCREEN);
        }
        catch (NoSuchPaddingException ex2) {
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", MainActivity.DEF_FULLSCREEN);
        }
    }
    
    private void saveFullScreen(final boolean b) {
        while (true) {
            try {
                Log.d("cipherName-13", Cipher.getInstance("DES").getAlgorithm());
                final SharedPreferences$Editor edit = PreferenceManager.getDefaultSharedPreferences((Context)this).edit();
                edit.putBoolean("is_fullscreen_pref", b);
                edit.commit();
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
    
    public void onBackPressed() {
        while (true) {
            try {
                Log.d("cipherName-19", Cipher.getInstance("DES").getAlgorithm());
                final long currentTimeMillis = System.currentTimeMillis();
                Label_0061: {
                    if (Math.abs(currentTimeMillis - this.mLastBackPress) <= 3500L) {
                        break Label_0061;
                    }
                    try {
                        Log.d("cipherName-20", Cipher.getInstance("DES").getAlgorithm());
                        this.pressBackToast.show();
                        this.mLastBackPress = currentTimeMillis;
                        return;
                        this.pressBackToast.cancel();
                        try {
                            Log.d("cipherName-21", Cipher.getInstance("DES").getAlgorithm());
                            super.onBackPressed();
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            Log.d("cipherName-18", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex2) {}
    }
    
    @SuppressLint({ "SetJavaScriptEnabled", "NewApi", "ShowToast" })
    protected void onCreate(final Bundle p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokespecial   android/app/Activity.onCreate:(Landroid/os/Bundle;)V
        //     5: ldc             "cipherName-0"
        //     7: ldc             "DES"
        //     9: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    12: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    15: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    18: pop            
        //    19: aload_0        
        //    20: iconst_1       
        //    21: invokevirtual   com/uberspot/a2048/MainActivity.requestWindowFeature:(I)Z
        //    24: pop            
        //    25: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    28: bipush          11
        //    30: if_icmplt       58
        //    33: ldc             "cipherName-1"
        //    35: ldc             "DES"
        //    37: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    40: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    43: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    46: pop            
        //    47: aload_0        
        //    48: invokevirtual   com/uberspot/a2048/MainActivity.getWindow:()Landroid/view/Window;
        //    51: ldc             16777216
        //    53: ldc             16777216
        //    55: invokevirtual   android/view/Window.setFlags:(II)V
        //    58: aload_0        
        //    59: aload_0        
        //    60: invokespecial   com/uberspot/a2048/MainActivity.isFullScreen:()Z
        //    63: invokespecial   com/uberspot/a2048/MainActivity.applyFullScreen:(Z)V
        //    66: iconst_0       
        //    67: istore_2       
        //    68: ldc             "cipherName-2"
        //    70: ldc             "DES"
        //    72: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    75: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    78: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    81: pop            
        //    82: aload_0        
        //    83: invokevirtual   com/uberspot/a2048/MainActivity.getContentResolver:()Landroid/content/ContentResolver;
        //    86: ldc             "accelerometer_rotation"
        //    88: invokestatic    android/provider/Settings$System.getInt:(Landroid/content/ContentResolver;Ljava/lang/String;)I
        //    91: istore_3       
        //    92: iload_3        
        //    93: iconst_1       
        //    94: if_icmpne       347
        //    97: iconst_1       
        //    98: istore_2       
        //    99: aload_0        
        //   100: invokevirtual   com/uberspot/a2048/MainActivity.getResources:()Landroid/content/res/Resources;
        //   103: invokevirtual   android/content/res/Resources.getConfiguration:()Landroid/content/res/Configuration;
        //   106: getfield        android/content/res/Configuration.screenLayout:I
        //   109: bipush          15
        //   111: iand           
        //   112: istore_3       
        //   113: iload_3        
        //   114: iconst_3       
        //   115: if_icmpeq       123
        //   118: iload_3        
        //   119: iconst_4       
        //   120: if_icmpne       146
        //   123: iload_2        
        //   124: ifeq            146
        //   127: ldc             "cipherName-4"
        //   129: ldc             "DES"
        //   131: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   134: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   137: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   140: pop            
        //   141: aload_0        
        //   142: iconst_4       
        //   143: invokevirtual   com/uberspot/a2048/MainActivity.setRequestedOrientation:(I)V
        //   146: aload_0        
        //   147: ldc             2130968576
        //   149: invokevirtual   com/uberspot/a2048/MainActivity.setContentView:(I)V
        //   152: new             Lde/cketti/library/changelog/ChangeLog;
        //   155: dup            
        //   156: aload_0        
        //   157: invokespecial   de/cketti/library/changelog/ChangeLog.<init>:(Landroid/content/Context;)V
        //   160: astore          4
        //   162: aload           4
        //   164: invokevirtual   de/cketti/library/changelog/ChangeLog.isFirstRun:()Z
        //   167: ifeq            192
        //   170: ldc             "cipherName-5"
        //   172: ldc             "DES"
        //   174: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   177: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   180: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   183: pop            
        //   184: aload           4
        //   186: invokevirtual   de/cketti/library/changelog/ChangeLog.getLogDialog:()Landroid/app/AlertDialog;
        //   189: invokevirtual   android/app/AlertDialog.show:()V
        //   192: aload_0        
        //   193: aload_0        
        //   194: ldc             2130903040
        //   196: invokevirtual   com/uberspot/a2048/MainActivity.findViewById:(I)Landroid/view/View;
        //   199: checkcast       Landroid/webkit/WebView;
        //   202: putfield        com/uberspot/a2048/MainActivity.mWebView:Landroid/webkit/WebView;
        //   205: aload_0        
        //   206: getfield        com/uberspot/a2048/MainActivity.mWebView:Landroid/webkit/WebView;
        //   209: invokevirtual   android/webkit/WebView.getSettings:()Landroid/webkit/WebSettings;
        //   212: astore          4
        //   214: aload           4
        //   216: iconst_1       
        //   217: invokevirtual   android/webkit/WebSettings.setJavaScriptEnabled:(Z)V
        //   220: aload           4
        //   222: iconst_1       
        //   223: invokevirtual   android/webkit/WebSettings.setDomStorageEnabled:(Z)V
        //   226: aload           4
        //   228: iconst_1       
        //   229: invokevirtual   android/webkit/WebSettings.setDatabaseEnabled:(Z)V
        //   232: aload           4
        //   234: getstatic       android/webkit/WebSettings$RenderPriority.HIGH:Landroid/webkit/WebSettings$RenderPriority;
        //   237: invokevirtual   android/webkit/WebSettings.setRenderPriority:(Landroid/webkit/WebSettings$RenderPriority;)V
        //   240: aload           4
        //   242: new             Ljava/lang/StringBuilder;
        //   245: dup            
        //   246: invokespecial   java/lang/StringBuilder.<init>:()V
        //   249: aload_0        
        //   250: invokevirtual   com/uberspot/a2048/MainActivity.getFilesDir:()Ljava/io/File;
        //   253: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   256: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   259: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   262: ldc_w           "/databases"
        //   265: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   268: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   271: invokevirtual   android/webkit/WebSettings.setDatabasePath:(Ljava/lang/String;)V
        //   274: aload_1        
        //   275: ifnull          381
        //   278: ldc_w           "cipherName-6"
        //   281: ldc             "DES"
        //   283: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   286: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   289: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   292: pop            
        //   293: aload_0        
        //   294: getfield        com/uberspot/a2048/MainActivity.mWebView:Landroid/webkit/WebView;
        //   297: aload_1        
        //   298: invokevirtual   android/webkit/WebView.restoreState:(Landroid/os/Bundle;)Landroid/webkit/WebBackForwardList;
        //   301: pop            
        //   302: aload_0        
        //   303: invokevirtual   com/uberspot/a2048/MainActivity.getApplication:()Landroid/app/Application;
        //   306: ldc_w           2131099667
        //   309: iconst_0       
        //   310: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;II)Landroid/widget/Toast;
        //   313: invokevirtual   android/widget/Toast.show:()V
        //   316: aload_0        
        //   317: getfield        com/uberspot/a2048/MainActivity.mWebView:Landroid/webkit/WebView;
        //   320: new             Lcom/uberspot/a2048/MainActivity$1;
        //   323: dup            
        //   324: aload_0        
        //   325: invokespecial   com/uberspot/a2048/MainActivity$1.<init>:(Lcom/uberspot/a2048/MainActivity;)V
        //   328: invokevirtual   android/webkit/WebView.setOnTouchListener:(Landroid/view/View$OnTouchListener;)V
        //   331: aload_0        
        //   332: aload_0        
        //   333: invokevirtual   com/uberspot/a2048/MainActivity.getApplicationContext:()Landroid/content/Context;
        //   336: ldc_w           2131099666
        //   339: iconst_0       
        //   340: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;II)Landroid/widget/Toast;
        //   343: putfield        com/uberspot/a2048/MainActivity.pressBackToast:Landroid/widget/Toast;
        //   346: return         
        //   347: iconst_0       
        //   348: istore_2       
        //   349: goto            99
        //   352: astore          4
        //   354: ldc_w           "cipherName-3"
        //   357: ldc             "DES"
        //   359: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   362: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   365: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   368: pop            
        //   369: ldc             "2048_MainActivity"
        //   371: ldc_w           "Settings could not be loaded"
        //   374: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   377: pop            
        //   378: goto            99
        //   381: ldc_w           "cipherName-7"
        //   384: ldc             "DES"
        //   386: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   389: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   392: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   395: pop            
        //   396: aload_0        
        //   397: getfield        com/uberspot/a2048/MainActivity.mWebView:Landroid/webkit/WebView;
        //   400: new             Ljava/lang/StringBuilder;
        //   403: dup            
        //   404: invokespecial   java/lang/StringBuilder.<init>:()V
        //   407: ldc_w           "file:///android_asset/2048/index.html?lang="
        //   410: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   413: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //   416: invokevirtual   java/util/Locale.getLanguage:()Ljava/lang/String;
        //   419: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   422: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   425: invokevirtual   android/webkit/WebView.loadUrl:(Ljava/lang/String;)V
        //   428: goto            302
        //   431: astore_1       
        //   432: goto            396
        //   435: astore_1       
        //   436: goto            396
        //   439: astore          4
        //   441: goto            293
        //   444: astore          4
        //   446: goto            293
        //   449: astore          5
        //   451: goto            184
        //   454: astore          5
        //   456: goto            184
        //   459: astore          4
        //   461: goto            141
        //   464: astore          4
        //   466: goto            141
        //   469: astore          4
        //   471: goto            369
        //   474: astore          4
        //   476: goto            369
        //   479: astore          4
        //   481: goto            82
        //   484: astore          4
        //   486: goto            82
        //   489: astore          4
        //   491: goto            47
        //   494: astore          4
        //   496: goto            47
        //   499: astore          4
        //   501: goto            19
        //   504: astore          4
        //   506: goto            19
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                
        //  -----  -----  -----  -----  ----------------------------------------------------
        //  5      19     499    504    Ljava/security/NoSuchAlgorithmException;
        //  5      19     504    509    Ljavax/crypto/NoSuchPaddingException;
        //  33     47     489    494    Ljava/security/NoSuchAlgorithmException;
        //  33     47     494    499    Ljavax/crypto/NoSuchPaddingException;
        //  68     82     479    484    Ljava/security/NoSuchAlgorithmException;
        //  68     82     484    489    Ljavax/crypto/NoSuchPaddingException;
        //  68     82     352    479    Landroid/provider/Settings$SettingNotFoundException;
        //  82     92     352    479    Landroid/provider/Settings$SettingNotFoundException;
        //  127    141    459    464    Ljava/security/NoSuchAlgorithmException;
        //  127    141    464    469    Ljavax/crypto/NoSuchPaddingException;
        //  170    184    449    454    Ljava/security/NoSuchAlgorithmException;
        //  170    184    454    459    Ljavax/crypto/NoSuchPaddingException;
        //  278    293    439    444    Ljava/security/NoSuchAlgorithmException;
        //  278    293    444    449    Ljavax/crypto/NoSuchPaddingException;
        //  354    369    469    474    Ljava/security/NoSuchAlgorithmException;
        //  354    369    474    479    Ljavax/crypto/NoSuchPaddingException;
        //  381    396    431    435    Ljava/security/NoSuchAlgorithmException;
        //  381    396    435    439    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0141:
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
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            Log.d("cipherName-12", Cipher.getInstance("DES").getAlgorithm());
            return true;
        }
        catch (NoSuchAlgorithmException ex) {
            return true;
        }
        catch (NoSuchPaddingException ex2) {
            return true;
        }
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-11", Cipher.getInstance("DES").getAlgorithm());
                this.mWebView.saveState(bundle);
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
