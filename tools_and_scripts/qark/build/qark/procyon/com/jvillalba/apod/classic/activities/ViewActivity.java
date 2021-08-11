// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.activities;

import android.view.MenuItem;
import android.view.Menu;
import java.io.Serializable;
import android.view.View;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.squareup.picasso.Target;
import com.jvillalba.apod.classic.model.PicassoDownloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import android.net.Uri;
import android.content.Intent;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.os.Build$VERSION;
import android.content.Context;
import android.widget.Toast;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import com.jvillalba.apod.classic.model.NASA;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity
{
    private final int WRITE_EXTERNAL_STORAGE_CODE;
    private ImageView imageAPOD;
    private String imageName;
    private NASA nasaAPOD;
    
    public ViewActivity() {
        this.WRITE_EXTERNAL_STORAGE_CODE = 100;
    }
    
    private boolean CheckPermission(final String s) {
        try {
            Log.d("cipherName-101", Cipher.getInstance("DES").getAlgorithm());
            return this.checkCallingOrSelfPermission(s) == 0;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.checkCallingOrSelfPermission(s) == 0;
        }
        catch (NoSuchPaddingException ex2) {
            return this.checkCallingOrSelfPermission(s) == 0;
        }
    }
    
    private void OlderVersions(final String ex) {
        while (true) {
            try {
                Log.d("cipherName-94", Cipher.getInstance("DES").getAlgorithm());
                Label_0043: {
                    if (!this.CheckPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
                        break Label_0043;
                    }
                    try {
                        Log.d("cipherName-95", Cipher.getInstance("DES").getAlgorithm());
                        this.downloadPicasso((String)ex);
                        return;
                        try {
                            Log.d("cipherName-96", Cipher.getInstance("DES").getAlgorithm());
                            Toast.makeText((Context)this, (CharSequence)"You Declined The Access", 0).show();
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex) {}
                    }
                    catch (NoSuchAlgorithmException ex2) {}
                    catch (NoSuchPaddingException ex3) {}
                }
            }
            catch (NoSuchAlgorithmException ex4) {
                continue;
            }
            catch (NoSuchPaddingException ex5) {
                continue;
            }
            break;
        }
    }
    
    private void checkPermission(final String ex) {
        while (true) {
            try {
                Log.d("cipherName-86", Cipher.getInstance("DES").getAlgorithm());
                Label_0216: {
                    if (Build$VERSION.SDK_INT < 23) {
                        break Label_0216;
                    }
                    try {
                        Log.d("cipherName-87", Cipher.getInstance("DES").getAlgorithm());
                        Label_0065: {
                            if (!this.CheckPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
                                break Label_0065;
                            }
                            try {
                                Log.d("cipherName-88", Cipher.getInstance("DES").getAlgorithm());
                                this.downloadPicasso((String)ex);
                                return;
                                try {
                                    Log.d("cipherName-89", Cipher.getInstance("DES").getAlgorithm());
                                    Label_0118: {
                                        if (this.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                                            break Label_0118;
                                        }
                                        try {
                                            Log.d("cipherName-90", Cipher.getInstance("DES").getAlgorithm());
                                            ActivityCompat.requestPermissions(this, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 100);
                                            return;
                                            try {
                                                Log.d("cipherName-91", Cipher.getInstance("DES").getAlgorithm());
                                                Toast.makeText((Context)this, (CharSequence)"Please, enable the storage permission", 0).show();
                                                final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                intent.addCategory("android.intent.category.DEFAULT");
                                                intent.setData(Uri.parse("package:" + this.getPackageName()));
                                                intent.addFlags(268435456);
                                                intent.addFlags(1073741824);
                                                intent.addFlags(8388608);
                                                this.startActivity(intent);
                                                return;
                                                try {
                                                    Log.d("cipherName-92", Cipher.getInstance("DES").getAlgorithm());
                                                    this.OlderVersions((String)ex);
                                                }
                                                catch (NoSuchAlgorithmException ex2) {}
                                                catch (NoSuchPaddingException ex3) {}
                                            }
                                            catch (NoSuchAlgorithmException ex4) {}
                                            catch (NoSuchPaddingException ex5) {}
                                        }
                                        catch (NoSuchAlgorithmException ex6) {}
                                        catch (NoSuchPaddingException ex7) {}
                                    }
                                }
                                catch (NoSuchAlgorithmException ex) {}
                                catch (NoSuchPaddingException ex) {}
                            }
                            catch (NoSuchAlgorithmException ex8) {}
                            catch (NoSuchPaddingException ex9) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex10) {}
                    catch (NoSuchPaddingException ex11) {}
                }
            }
            catch (NoSuchAlgorithmException ex12) {
                continue;
            }
            catch (NoSuchPaddingException ex13) {
                continue;
            }
            break;
        }
    }
    
    private void downloadPicasso(final String s) {
        while (true) {
            try {
                Log.d("cipherName-93", Cipher.getInstance("DES").getAlgorithm());
                Picasso.with((Context)this).load(this.nasaAPOD.getUrl()).networkPolicy(NetworkPolicy.OFFLINE, new NetworkPolicy[0]).into(new PicassoDownloader(s, (Context)this));
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
    
    @NonNull
    private String getConcat(final TextView textView, String concat) {
        while (true) {
            try {
                Log.d("cipherName-104", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-105", Cipher.getInstance("DES").getAlgorithm());
                        concat = textView.getText().toString().concat((String)concat);
                        return (String)concat;
                    }
                    catch (Exception concat) {
                        try {
                            Log.d("cipherName-106", Cipher.getInstance("DES").getAlgorithm());
                            return textView.getText().toString();
                        }
                        catch (NoSuchAlgorithmException concat) {}
                        catch (NoSuchPaddingException concat) {}
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
    
    @NonNull
    private String getImageNameApod() {
        while (true) {
            try {
                Log.d("cipherName-102", Cipher.getInstance("DES").getAlgorithm());
                final String substring = this.nasaAPOD.getUrl().substring(this.nasaAPOD.getUrl().lastIndexOf(47) + 1);
                return substring.substring(0, substring.lastIndexOf("."));
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
    
    private void setDataNasaAPOD(final NASA nasa) {
        while (true) {
            try {
                Log.d("cipherName-103", Cipher.getInstance("DES").getAlgorithm());
                this.imageAPOD = this.findViewById(2131230806);
                Picasso.with((Context)this).load(nasa.getUrl()).error(2131492865).fit().into(this.imageAPOD);
                final TextView textView = this.findViewById(2131230889);
                textView.setText((CharSequence)this.getConcat(textView, nasa.getTitle()));
                final TextView textView2 = this.findViewById(2131230886);
                textView2.setText((CharSequence)this.getConcat(textView2, nasa.getDate()));
                final TextView textView3 = this.findViewById(2131230771);
                textView3.setText((CharSequence)this.getConcat(textView3, nasa.getCopyright()));
                final TextView textView4 = this.findViewById(2131230791);
                textView4.setText((CharSequence)nasa.getExplanation());
                textView4.setMovementMethod((MovementMethod)new ScrollingMovementMethod());
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
    protected void onCreate(Bundle extras) {
        super.onCreate(extras);
        while (true) {
            try {
                Log.d("cipherName-82", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2131361821);
                this.getSupportActionBar().setIcon(2131492864);
                this.getSupportActionBar().setDisplayUseLogoEnabled(true);
                this.getSupportActionBar().setDisplayShowHomeEnabled(true);
                extras = this.getIntent().getExtras();
                Serializable serializable;
                if (extras != null) {
                    serializable = extras.getSerializable("Nasa");
                }
                else {
                    serializable = null;
                }
                this.setDataNasaAPOD(this.nasaAPOD = (NASA)serializable);
                this.imageAPOD.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-83", Cipher.getInstance("DES").getAlgorithm());
                                final Intent intent = new Intent((Context)ViewActivity.this, (Class)ImageActivity.class);
                                intent.putExtra("HD_URL", ViewActivity.this.nasaAPOD.getHdurl());
                                intent.addFlags(268435456);
                                intent.addFlags(1073741824);
                                intent.addFlags(8388608);
                                ViewActivity.this.startActivity(intent);
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
                });
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
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        while (true) {
            try {
                Log.d("cipherName-84", Cipher.getInstance("DES").getAlgorithm());
                this.getMenuInflater().inflate(2131427328, menu);
                return super.onCreateOptionsMenu(menu);
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
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        while (true) {
            try {
                Log.d("cipherName-85", Cipher.getInstance("DES").getAlgorithm());
                switch (menuItem.getItemId()) {
                    default: {
                        return super.onOptionsItemSelected(menuItem);
                    }
                    case 2131230782: {
                        this.checkPermission(this.imageName = this.getImageNameApod());
                        return true;
                    }
                }
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
    public void onRequestPermissionsResult(final int p0, @NonNull final String[] p1, @NonNull final int[] p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: iload_1        
        //    16: tableswitch {
        //              200: 44
        //          default: 36
        //        }
        //    36: aload_0        
        //    37: iload_1        
        //    38: aload_2        
        //    39: aload_3        
        //    40: invokespecial   android/support/v7/app/AppCompatActivity.onRequestPermissionsResult:(I[Ljava/lang/String;[I)V
        //    43: return         
        //    44: aload_2        
        //    45: iconst_0       
        //    46: aaload         
        //    47: astore_2       
        //    48: aload_3        
        //    49: iconst_0       
        //    50: iaload         
        //    51: istore_1       
        //    52: aload_2        
        //    53: ldc             "android.permission.WRITE_EXTERNAL_STORAGE"
        //    55: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    58: ifeq            43
        //    61: ldc_w           "cipherName-98"
        //    64: ldc             "DES"
        //    66: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    69: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    72: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    75: pop            
        //    76: iload_1        
        //    77: ifne            107
        //    80: ldc_w           "cipherName-99"
        //    83: ldc             "DES"
        //    85: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    88: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    91: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    94: pop            
        //    95: aload_0        
        //    96: ldc_w           "Thanks, Now You Can Download Images"
        //    99: iconst_0       
        //   100: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //   103: invokevirtual   android/widget/Toast.show:()V
        //   106: return         
        //   107: ldc_w           "cipherName-100"
        //   110: ldc             "DES"
        //   112: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   115: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   118: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   121: pop            
        //   122: aload_0        
        //   123: ldc             "You Declined The Access"
        //   125: iconst_0       
        //   126: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
        //   129: invokevirtual   android/widget/Toast.show:()V
        //   132: return         
        //   133: astore_2       
        //   134: goto            122
        //   137: astore_2       
        //   138: goto            122
        //   141: astore_2       
        //   142: goto            95
        //   145: astore_2       
        //   146: goto            95
        //   149: astore_2       
        //   150: goto            76
        //   153: astore_2       
        //   154: goto            76
        //   157: astore          4
        //   159: goto            15
        //   162: astore          4
        //   164: goto            15
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     157    162    Ljava/security/NoSuchAlgorithmException;
        //  0      15     162    167    Ljavax/crypto/NoSuchPaddingException;
        //  61     76     149    153    Ljava/security/NoSuchAlgorithmException;
        //  61     76     153    157    Ljavax/crypto/NoSuchPaddingException;
        //  80     95     141    145    Ljava/security/NoSuchAlgorithmException;
        //  80     95     145    149    Ljavax/crypto/NoSuchPaddingException;
        //  107    122    133    137    Ljava/security/NoSuchAlgorithmException;
        //  107    122    137    141    Ljavax/crypto/NoSuchPaddingException;
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
}
