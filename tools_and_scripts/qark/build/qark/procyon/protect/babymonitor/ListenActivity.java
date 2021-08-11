// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package protect.babymonitor;

import android.widget.TextView;
import android.os.Bundle;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.media.MediaPlayer$OnCompletionListener;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import javax.crypto.Cipher;
import java.io.IOException;
import java.net.Socket;
import android.app.Activity;

public class ListenActivity extends Activity
{
    final String TAG;
    String _address;
    Thread _listenThread;
    String _name;
    int _port;
    
    public ListenActivity() {
        this.TAG = "BabyMonitor";
    }
    
    private void playAlert() {
        while (true) {
            try {
                Log.d("cipherName-79", Cipher.getInstance("DES").getAlgorithm());
                final MediaPlayer create = MediaPlayer.create((Context)this, 2130968576);
                Label_0064: {
                    if (create == null) {
                        break Label_0064;
                    }
                    try {
                        Log.d("cipherName-80", Cipher.getInstance("DES").getAlgorithm());
                        Log.i("BabyMonitor", "Playing alert");
                        create.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
                            public void onCompletion(final MediaPlayer mediaPlayer) {
                                while (true) {
                                    try {
                                        Log.d("cipherName-81", Cipher.getInstance("DES").getAlgorithm());
                                        mediaPlayer.release();
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
                        create.start();
                        return;
                        try {
                            Log.d("cipherName-82", Cipher.getInstance("DES").getAlgorithm());
                            Log.e("BabyMonitor", "Failed to play alert");
                        }
                        catch (NoSuchAlgorithmException create) {}
                        catch (NoSuchPaddingException create) {}
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
    
    private void streamAudio(final Socket p0) throws IllegalArgumentException, IllegalStateException, IOException {
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
        //    14: ldc             "BabyMonitor"
        //    16: ldc             "Setting up stream"
        //    18: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //    21: pop            
        //    22: sipush          11025
        //    25: iconst_4       
        //    26: iconst_2       
        //    27: invokestatic    android/media/AudioTrack.getMinBufferSize:(III)I
        //    30: istore_3       
        //    31: new             Landroid/media/AudioTrack;
        //    34: dup            
        //    35: iconst_3       
        //    36: sipush          11025
        //    39: iconst_4       
        //    40: iconst_2       
        //    41: iload_3        
        //    42: iconst_1       
        //    43: invokespecial   android/media/AudioTrack.<init>:(IIIIII)V
        //    46: astore          4
        //    48: aload_0        
        //    49: iconst_3       
        //    50: invokevirtual   protect/babymonitor/ListenActivity.setVolumeControlStream:(I)V
        //    53: aload_1        
        //    54: invokevirtual   java/net/Socket.getInputStream:()Ljava/io/InputStream;
        //    57: astore          5
        //    59: iconst_0       
        //    60: istore_2       
        //    61: aload           4
        //    63: invokevirtual   android/media/AudioTrack.play:()V
        //    66: ldc             "cipherName-66"
        //    68: ldc             "DES"
        //    70: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    73: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    76: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    79: pop            
        //    80: iload_3        
        //    81: iconst_2       
        //    82: imul           
        //    83: newarray        B
        //    85: astore          6
        //    87: aload_1        
        //    88: invokevirtual   java/net/Socket.isConnected:()Z
        //    91: ifeq            193
        //    94: iload_2        
        //    95: iconst_m1      
        //    96: if_icmpeq       193
        //    99: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   102: invokevirtual   java/lang/Thread.isInterrupted:()Z
        //   105: ifne            193
        //   108: ldc             "cipherName-67"
        //   110: ldc             "DES"
        //   112: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   115: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   118: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   121: pop            
        //   122: aload           5
        //   124: aload           6
        //   126: invokevirtual   java/io/InputStream.read:([B)I
        //   129: istore_3       
        //   130: iload_3        
        //   131: istore_2       
        //   132: iload_3        
        //   133: ifle            87
        //   136: ldc             "cipherName-68"
        //   138: ldc             "DES"
        //   140: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   143: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   146: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   149: pop            
        //   150: aload           4
        //   152: aload           6
        //   154: iconst_0       
        //   155: iload_3        
        //   156: invokevirtual   android/media/AudioTrack.write:([BII)I
        //   159: pop            
        //   160: iload_3        
        //   161: istore_2       
        //   162: goto            87
        //   165: astore          5
        //   167: ldc             "cipherName-69"
        //   169: ldc             "DES"
        //   171: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   174: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   177: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   180: pop            
        //   181: aload           4
        //   183: invokevirtual   android/media/AudioTrack.stop:()V
        //   186: aload_1        
        //   187: invokevirtual   java/net/Socket.close:()V
        //   190: aload           5
        //   192: athrow         
        //   193: ldc             "cipherName-69"
        //   195: ldc             "DES"
        //   197: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   200: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   203: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   206: pop            
        //   207: aload           4
        //   209: invokevirtual   android/media/AudioTrack.stop:()V
        //   212: aload_1        
        //   213: invokevirtual   java/net/Socket.close:()V
        //   216: return         
        //   217: astore          6
        //   219: goto            181
        //   222: astore          6
        //   224: goto            181
        //   227: astore          5
        //   229: goto            207
        //   232: astore          5
        //   234: goto            207
        //   237: astore          7
        //   239: goto            150
        //   242: astore          7
        //   244: goto            150
        //   247: astore          7
        //   249: goto            122
        //   252: astore          7
        //   254: goto            122
        //   257: astore          6
        //   259: goto            80
        //   262: astore          6
        //   264: goto            80
        //   267: astore          4
        //   269: goto            14
        //   272: astore          4
        //   274: goto            14
        //    Exceptions:
        //  throws java.lang.IllegalArgumentException
        //  throws java.lang.IllegalStateException
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     267    272    Ljava/security/NoSuchAlgorithmException;
        //  0      14     272    277    Ljavax/crypto/NoSuchPaddingException;
        //  66     80     257    262    Ljava/security/NoSuchAlgorithmException;
        //  66     80     262    267    Ljavax/crypto/NoSuchPaddingException;
        //  66     80     165    227    Any
        //  80     87     165    227    Any
        //  87     94     165    227    Any
        //  99     108    165    227    Any
        //  108    122    247    252    Ljava/security/NoSuchAlgorithmException;
        //  108    122    252    257    Ljavax/crypto/NoSuchPaddingException;
        //  108    122    165    227    Any
        //  122    130    165    227    Any
        //  136    150    237    242    Ljava/security/NoSuchAlgorithmException;
        //  136    150    242    247    Ljavax/crypto/NoSuchPaddingException;
        //  136    150    165    227    Any
        //  150    160    165    227    Any
        //  167    181    217    222    Ljava/security/NoSuchAlgorithmException;
        //  167    181    222    227    Ljavax/crypto/NoSuchPaddingException;
        //  193    207    227    232    Ljava/security/NoSuchAlgorithmException;
        //  193    207    232    237    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0193:
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
    
    protected void onCreate(Bundle extras) {
        super.onCreate(extras);
        while (true) {
            try {
                Log.d("cipherName-70", Cipher.getInstance("DES").getAlgorithm());
                extras = this.getIntent().getExtras();
                this._address = extras.getString("address");
                this._port = extras.getInt("port");
                this._name = extras.getString("name");
                this.setContentView(2130903043);
                this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Log.d("cipherName-71", Cipher.getInstance("DES").getAlgorithm());
                                ((TextView)ListenActivity.this.findViewById(2131296271)).setText((CharSequence)ListenActivity.this._name);
                                ((TextView)ListenActivity.this.findViewById(2131296273)).setText(2131034130);
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
                (this._listenThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                        //    14: ldc             "cipherName-73"
                        //    16: ldc             "DES"
                        //    18: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //    21: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //    24: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //    27: pop            
                        //    28: new             Ljava/net/Socket;
                        //    31: dup            
                        //    32: aload_0        
                        //    33: getfield        protect/babymonitor/ListenActivity$2.this$0:Lprotect/babymonitor/ListenActivity;
                        //    36: getfield        protect/babymonitor/ListenActivity._address:Ljava/lang/String;
                        //    39: aload_0        
                        //    40: getfield        protect/babymonitor/ListenActivity$2.this$0:Lprotect/babymonitor/ListenActivity;
                        //    43: getfield        protect/babymonitor/ListenActivity._port:I
                        //    46: invokespecial   java/net/Socket.<init>:(Ljava/lang/String;I)V
                        //    49: astore_1       
                        //    50: aload_0        
                        //    51: getfield        protect/babymonitor/ListenActivity$2.this$0:Lprotect/babymonitor/ListenActivity;
                        //    54: aload_1        
                        //    55: invokestatic    protect/babymonitor/ListenActivity.access$000:(Lprotect/babymonitor/ListenActivity;Ljava/net/Socket;)V
                        //    58: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
                        //    61: invokevirtual   java/lang/Thread.isInterrupted:()Z
                        //    64: ifne            103
                        //    67: ldc             "cipherName-76"
                        //    69: ldc             "DES"
                        //    71: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //    74: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //    77: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //    80: pop            
                        //    81: aload_0        
                        //    82: getfield        protect/babymonitor/ListenActivity$2.this$0:Lprotect/babymonitor/ListenActivity;
                        //    85: invokestatic    protect/babymonitor/ListenActivity.access$100:(Lprotect/babymonitor/ListenActivity;)V
                        //    88: aload_0        
                        //    89: getfield        protect/babymonitor/ListenActivity$2.this$0:Lprotect/babymonitor/ListenActivity;
                        //    92: new             Lprotect/babymonitor/ListenActivity$2$1;
                        //    95: dup            
                        //    96: aload_0        
                        //    97: invokespecial   protect/babymonitor/ListenActivity$2$1.<init>:(Lprotect/babymonitor/ListenActivity$2;)V
                        //   100: invokevirtual   protect/babymonitor/ListenActivity.runOnUiThread:(Ljava/lang/Runnable;)V
                        //   103: return         
                        //   104: astore_1       
                        //   105: ldc             "cipherName-74"
                        //   107: ldc             "DES"
                        //   109: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   112: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   115: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   118: pop            
                        //   119: ldc             "BabyMonitor"
                        //   121: ldc             "Failed to stream audio"
                        //   123: aload_1        
                        //   124: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                        //   127: pop            
                        //   128: goto            58
                        //   131: astore_1       
                        //   132: ldc             "cipherName-75"
                        //   134: ldc             "DES"
                        //   136: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   139: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   142: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   145: pop            
                        //   146: ldc             "BabyMonitor"
                        //   148: ldc             "Failed to stream audio"
                        //   150: aload_1        
                        //   151: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                        //   154: pop            
                        //   155: goto            58
                        //   158: astore_1       
                        //   159: goto            81
                        //   162: astore_1       
                        //   163: goto            81
                        //   166: astore_2       
                        //   167: goto            146
                        //   170: astore_2       
                        //   171: goto            146
                        //   174: astore_2       
                        //   175: goto            119
                        //   178: astore_2       
                        //   179: goto            119
                        //   182: astore_1       
                        //   183: goto            28
                        //   186: astore_1       
                        //   187: goto            28
                        //   190: astore_1       
                        //   191: goto            14
                        //   194: astore_1       
                        //   195: goto            14
                        //    Exceptions:
                        //  Try           Handler
                        //  Start  End    Start  End    Type                                    
                        //  -----  -----  -----  -----  ----------------------------------------
                        //  0      14     190    194    Ljava/security/NoSuchAlgorithmException;
                        //  0      14     194    198    Ljavax/crypto/NoSuchPaddingException;
                        //  14     28     182    186    Ljava/security/NoSuchAlgorithmException;
                        //  14     28     186    190    Ljavax/crypto/NoSuchPaddingException;
                        //  14     28     104    182    Ljava/net/UnknownHostException;
                        //  14     28     131    174    Ljava/io/IOException;
                        //  28     58     104    182    Ljava/net/UnknownHostException;
                        //  28     58     131    174    Ljava/io/IOException;
                        //  67     81     158    162    Ljava/security/NoSuchAlgorithmException;
                        //  67     81     162    166    Ljavax/crypto/NoSuchPaddingException;
                        //  105    119    174    178    Ljava/security/NoSuchAlgorithmException;
                        //  105    119    178    182    Ljavax/crypto/NoSuchPaddingException;
                        //  132    146    166    170    Ljava/security/NoSuchAlgorithmException;
                        //  132    146    170    174    Ljavax/crypto/NoSuchPaddingException;
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0081:
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
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:494)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                })).start();
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
    
    public void onDestroy() {
        this._listenThread.interrupt();
        while (true) {
            try {
                Log.d("cipherName-78", Cipher.getInstance("DES").getAlgorithm());
                this._listenThread = null;
                super.onDestroy();
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
