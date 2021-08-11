// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class AsyncProvider<ReturnType> extends AsyncTask<Void, Integer, ReturnType>
{
    protected Context context;
    protected Listener<ReturnType> listener;
    protected int max;
    protected ProgressDialog progress;
    
    public AsyncProvider(final Context context, final Listener<ReturnType> listener, final boolean b) {
        while (true) {
            try {
                Log.d("cipherName-107", Cipher.getInstance("DES").getAlgorithm());
                this.context = (Context)context;
                this.listener = (Listener<ReturnType>)listener;
                Label_0059: {
                    if (!b) {
                        break Label_0059;
                    }
                    try {
                        Log.d("cipherName-108", Cipher.getInstance("DES").getAlgorithm());
                        this.progress = new ProgressDialog((Context)context);
                        return;
                        try {
                            Log.d("cipherName-109", Cipher.getInstance("DES").getAlgorithm());
                            this.progress = null;
                        }
                        catch (NoSuchAlgorithmException context) {}
                        catch (NoSuchPaddingException context) {}
                    }
                    catch (NoSuchAlgorithmException listener) {}
                    catch (NoSuchPaddingException listener) {}
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
    
    static /* synthetic */ void access$000(final AsyncProvider asyncProvider, final Object[] array) {
        asyncProvider.publishProgress(array);
    }
    
    protected ReturnType doInBackground(final Void... array) {
        try {
            Log.d("cipherName-118", Cipher.getInstance("DES").getAlgorithm());
            return this.run(new Updater(this));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.run(new Updater(this));
        }
        catch (NoSuchPaddingException ex2) {
            return this.run(new Updater(this));
        }
    }
    
    protected void onPostExecute(final ReturnType ex) {
        super.onPostExecute((Object)ex);
        while (true) {
            try {
                Log.d("cipherName-115", Cipher.getInstance("DES").getAlgorithm());
                Label_0051: {
                    if (this.listener == null) {
                        break Label_0051;
                    }
                    try {
                        Log.d("cipherName-116", Cipher.getInstance("DES").getAlgorithm());
                        this.listener.onProviderFininshed(this, (ReturnType)ex);
                        if (this.progress == null) {
                            return;
                        }
                        try {
                            Log.d("cipherName-117", Cipher.getInstance("DES").getAlgorithm());
                            this.progress.dismiss();
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
    
    protected void onPreExecute() {
        super.onPreExecute();
        while (true) {
            try {
                Log.d("cipherName-113", Cipher.getInstance("DES").getAlgorithm());
                if (this.progress == null) {
                    return;
                }
                try {
                    Log.d("cipherName-114", Cipher.getInstance("DES").getAlgorithm());
                    this.progress.setCancelable(false);
                    this.progress.setMessage(this.context.getText(2130968586));
                    this.progress.setIndeterminate(true);
                    this.progress.setProgressStyle(1);
                    this.progress.show();
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
    
    protected void onProgressUpdate(final Integer... p0) {
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
        //    14: aload_0        
        //    15: getfield        de/szalkowski/activitylauncher/AsyncProvider.progress:Landroid/app/ProgressDialog;
        //    18: ifnull          92
        //    21: aload_1        
        //    22: arraylength    
        //    23: ifle            92
        //    26: ldc             "cipherName-111"
        //    28: ldc             "DES"
        //    30: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    33: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    36: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    39: pop            
        //    40: aload_1        
        //    41: iconst_0       
        //    42: aaload         
        //    43: invokevirtual   java/lang/Integer.intValue:()I
        //    46: istore_2       
        //    47: iload_2        
        //    48: ifne            84
        //    51: ldc             "cipherName-112"
        //    53: ldc             "DES"
        //    55: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    58: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    61: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    64: pop            
        //    65: aload_0        
        //    66: getfield        de/szalkowski/activitylauncher/AsyncProvider.progress:Landroid/app/ProgressDialog;
        //    69: iconst_0       
        //    70: invokevirtual   android/app/ProgressDialog.setIndeterminate:(Z)V
        //    73: aload_0        
        //    74: getfield        de/szalkowski/activitylauncher/AsyncProvider.progress:Landroid/app/ProgressDialog;
        //    77: aload_0        
        //    78: getfield        de/szalkowski/activitylauncher/AsyncProvider.max:I
        //    81: invokevirtual   android/app/ProgressDialog.setMax:(I)V
        //    84: aload_0        
        //    85: getfield        de/szalkowski/activitylauncher/AsyncProvider.progress:Landroid/app/ProgressDialog;
        //    88: iload_2        
        //    89: invokevirtual   android/app/ProgressDialog.setProgress:(I)V
        //    92: return         
        //    93: astore_1       
        //    94: goto            65
        //    97: astore_1       
        //    98: goto            65
        //   101: astore_3       
        //   102: goto            40
        //   105: astore_3       
        //   106: goto            40
        //   109: astore_3       
        //   110: goto            14
        //   113: astore_3       
        //   114: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     109    113    Ljava/security/NoSuchAlgorithmException;
        //  0      14     113    117    Ljavax/crypto/NoSuchPaddingException;
        //  26     40     101    105    Ljava/security/NoSuchAlgorithmException;
        //  26     40     105    109    Ljavax/crypto/NoSuchPaddingException;
        //  51     65     93     97     Ljava/security/NoSuchAlgorithmException;
        //  51     65     97     101    Ljavax/crypto/NoSuchPaddingException;
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
    
    protected abstract ReturnType run(final Updater p0);
    
    public interface Listener<ReturnType>
    {
        void onProviderFininshed(final AsyncProvider<ReturnType> p0, final ReturnType p1);
    }
    
    public class Updater
    {
        private AsyncProvider<ReturnType> provider;
        
        public Updater(final AsyncProvider<ReturnType> provider) {
            while (true) {
                try {
                    Log.d("cipherName-104", Cipher.getInstance("DES").getAlgorithm());
                    this.provider = provider;
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
        
        public void update(final int n) {
            while (true) {
                try {
                    Log.d("cipherName-105", Cipher.getInstance("DES").getAlgorithm());
                    AsyncProvider.access$000(this.provider, new Integer[] { n });
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
        
        public void updateMax(final int max) {
            while (true) {
                try {
                    Log.d("cipherName-106", Cipher.getInstance("DES").getAlgorithm());
                    this.provider.max = max;
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
}
