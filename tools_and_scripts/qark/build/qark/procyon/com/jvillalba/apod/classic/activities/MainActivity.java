// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.activities;

import java.io.File;
import android.view.MenuItem;
import android.view.Menu;
import android.support.v7.widget.DefaultItemAnimator;
import java.io.Serializable;
import android.content.Intent;
import com.jvillalba.apod.classic.model.NASA;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import com.jvillalba.apod.classic.controller.NasaController;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import com.jvillalba.apod.classic.adapter.MyAdapter;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private MyAdapter mAdapter;
    
    private void enforceIconBar() {
        while (true) {
            try {
                Log.d("cipherName-77", Cipher.getInstance("DES").getAlgorithm());
                this.getSupportActionBar().setIcon(2131492864);
                this.getSupportActionBar().setDisplayUseLogoEnabled(true);
                this.getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    
    private void getNasaAPODS() {
        while (true) {
            try {
                Log.d("cipherName-78", Cipher.getInstance("DES").getAlgorithm());
                new NasaController().getNASAAPODS(this.mAdapter, this.getApplicationContext());
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
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        while (true) {
            try {
                Log.d("cipherName-71", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2131361820);
                this.enforceIconBar();
                final RecyclerView recyclerView = this.findViewById(2131230839);
                final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this);
                this.mAdapter = new MyAdapter(2131361851, (MyAdapter.OnItemClickListener)new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final NASA ex, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-72", Cipher.getInstance("DES").getAlgorithm());
                                try {
                                    try {
                                        Log.d("cipherName-73", Cipher.getInstance("DES").getAlgorithm());
                                        final Intent intent = new Intent((Context)MainActivity.this, (Class)ViewActivity.class);
                                        intent.putExtra("Nasa", (Serializable)ex);
                                        MainActivity.this.startActivity(intent);
                                    }
                                    catch (Exception ex) {
                                        try {
                                            Log.d("cipherName-74", Cipher.getInstance("DES").getAlgorithm());
                                            ex.printStackTrace();
                                        }
                                        catch (NoSuchAlgorithmException ex2) {}
                                        catch (NoSuchPaddingException ex3) {}
                                    }
                                }
                                catch (NoSuchAlgorithmException ex4) {}
                                catch (NoSuchPaddingException ex5) {}
                            }
                            catch (NoSuchAlgorithmException ex6) {
                                continue;
                            }
                            catch (NoSuchPaddingException ex7) {
                                continue;
                            }
                            break;
                        }
                    }
                });
                recyclerView.setItemAnimator((RecyclerView.ItemAnimator)new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
                recyclerView.setAdapter((RecyclerView.Adapter)this.mAdapter);
                this.picassoClearCache(this.getApplicationContext().getCacheDir());
                this.getNasaAPODS();
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
                Log.d("cipherName-75", Cipher.getInstance("DES").getAlgorithm());
                this.getMenuInflater().inflate(2131427329, menu);
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
                Log.d("cipherName-76", Cipher.getInstance("DES").getAlgorithm());
                switch (menuItem.getItemId()) {
                    default: {
                        return super.onOptionsItemSelected(menuItem);
                    }
                    case 2131230840: {
                        this.recreate();
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
    
    public void picassoClearCache(final File p0) {
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
        //    14: aload_1        
        //    15: invokevirtual   java/io/File.isDirectory:()Z
        //    18: ifeq            85
        //    21: ldc             "cipherName-80"
        //    23: ldc             "DES"
        //    25: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    28: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    31: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    34: pop            
        //    35: aload_1        
        //    36: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //    39: astore          4
        //    41: aload           4
        //    43: arraylength    
        //    44: istore_3       
        //    45: iconst_0       
        //    46: istore_2       
        //    47: iload_2        
        //    48: iload_3        
        //    49: if_icmpge       85
        //    52: aload           4
        //    54: iload_2        
        //    55: aaload         
        //    56: astore          5
        //    58: ldc             "cipherName-81"
        //    60: ldc             "DES"
        //    62: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    65: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    68: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    71: pop            
        //    72: aload_0        
        //    73: aload           5
        //    75: invokevirtual   com/jvillalba/apod/classic/activities/MainActivity.picassoClearCache:(Ljava/io/File;)V
        //    78: iload_2        
        //    79: iconst_1       
        //    80: iadd           
        //    81: istore_2       
        //    82: goto            47
        //    85: aload_1        
        //    86: invokevirtual   java/io/File.delete:()Z
        //    89: pop            
        //    90: return         
        //    91: astore          6
        //    93: goto            72
        //    96: astore          6
        //    98: goto            72
        //   101: astore          4
        //   103: goto            35
        //   106: astore          4
        //   108: goto            35
        //   111: astore          4
        //   113: goto            14
        //   116: astore          4
        //   118: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     111    116    Ljava/security/NoSuchAlgorithmException;
        //  0      14     116    121    Ljavax/crypto/NoSuchPaddingException;
        //  21     35     101    106    Ljava/security/NoSuchAlgorithmException;
        //  21     35     106    111    Ljavax/crypto/NoSuchPaddingException;
        //  58     72     91     96     Ljava/security/NoSuchAlgorithmException;
        //  58     72     96     101    Ljavax/crypto/NoSuchPaddingException;
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
