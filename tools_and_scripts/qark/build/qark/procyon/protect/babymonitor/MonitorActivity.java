// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package protect.babymonitor;

import android.view.MenuItem;
import android.view.Menu;
import android.text.format.Formatter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import javax.crypto.Cipher;
import java.io.IOException;
import java.net.Socket;
import android.net.nsd.NsdManager$RegistrationListener;
import android.net.nsd.NsdManager;
import android.app.Activity;

public class MonitorActivity extends Activity
{
    final String TAG;
    NsdManager _nsdManager;
    NsdManager$RegistrationListener _registrationListener;
    Thread _serviceThread;
    
    public MonitorActivity() {
        this.TAG = "BabyMonitor";
    }
    
    private void registerService(final int port) {
        while (true) {
            try {
                Log.d("cipherName-26", Cipher.getInstance("DES").getAlgorithm());
                final NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
                nsdServiceInfo.setServiceName("ProtectBabyMonitor");
                nsdServiceInfo.setServiceType("_babymonitor._tcp.");
                nsdServiceInfo.setPort(port);
                this._registrationListener = (NsdManager$RegistrationListener)new NsdManager$RegistrationListener() {
                    public void onRegistrationFailed(final NsdServiceInfo nsdServiceInfo, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-29", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("BabyMonitor", "Registration failed: " + n);
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
                    
                    public void onServiceRegistered(NsdServiceInfo serviceName) {
                        while (true) {
                            try {
                                Log.d("cipherName-27", Cipher.getInstance("DES").getAlgorithm());
                                serviceName = (NsdServiceInfo)serviceName.getServiceName();
                                Log.i("BabyMonitor", "Service name: " + (String)serviceName);
                                MonitorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        while (true) {
                                            try {
                                                Log.d("cipherName-28", Cipher.getInstance("DES").getAlgorithm());
                                                ((TextView)MonitorActivity.this.findViewById(2131296273)).setText(2131034143);
                                                ((TextView)MonitorActivity.this.findViewById(2131296276)).setText((CharSequence)serviceName);
                                                ((TextView)MonitorActivity.this.findViewById(2131296280)).setText((CharSequence)Integer.toString(port));
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
                    
                    public void onServiceUnregistered(final NsdServiceInfo nsdServiceInfo) {
                        while (true) {
                            try {
                                Log.d("cipherName-30", Cipher.getInstance("DES").getAlgorithm());
                                Log.i("BabyMonitor", "Unregistering service");
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
                    
                    public void onUnregistrationFailed(final NsdServiceInfo nsdServiceInfo, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-31", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("BabyMonitor", "Unregistration failed: " + n);
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
                };
                this._nsdManager.registerService(nsdServiceInfo, 1, this._registrationListener);
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
    
    private void serviceConnection(final Socket p0) throws IOException {
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
        //    15: new             Lprotect/babymonitor/MonitorActivity$1;
        //    18: dup            
        //    19: aload_0        
        //    20: invokespecial   protect/babymonitor/MonitorActivity$1.<init>:(Lprotect/babymonitor/MonitorActivity;)V
        //    23: invokevirtual   protect/babymonitor/MonitorActivity.runOnUiThread:(Ljava/lang/Runnable;)V
        //    26: sipush          11025
        //    29: bipush          16
        //    31: iconst_2       
        //    32: invokestatic    android/media/AudioRecord.getMinBufferSize:(III)I
        //    35: istore_2       
        //    36: new             Landroid/media/AudioRecord;
        //    39: dup            
        //    40: iconst_1       
        //    41: sipush          11025
        //    44: bipush          16
        //    46: iconst_2       
        //    47: iload_2        
        //    48: invokespecial   android/media/AudioRecord.<init>:(IIIII)V
        //    51: astore          4
        //    53: iload_2        
        //    54: iconst_2       
        //    55: imul           
        //    56: istore_3       
        //    57: iload_3        
        //    58: newarray        B
        //    60: astore          5
        //    62: ldc             "cipherName-5"
        //    64: ldc             "DES"
        //    66: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    69: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    72: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    75: pop            
        //    76: aload           4
        //    78: invokevirtual   android/media/AudioRecord.startRecording:()V
        //    81: aload_1        
        //    82: invokevirtual   java/net/Socket.getOutputStream:()Ljava/io/OutputStream;
        //    85: astore          6
        //    87: aload_1        
        //    88: iload_3        
        //    89: invokevirtual   java/net/Socket.setSendBufferSize:(I)V
        //    92: ldc             "BabyMonitor"
        //    94: new             Ljava/lang/StringBuilder;
        //    97: dup            
        //    98: invokespecial   java/lang/StringBuilder.<init>:()V
        //   101: ldc             "Socket send buffer size: "
        //   103: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   106: aload_1        
        //   107: invokevirtual   java/net/Socket.getSendBufferSize:()I
        //   110: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   113: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   116: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   119: pop            
        //   120: aload_1        
        //   121: invokevirtual   java/net/Socket.isConnected:()Z
        //   124: ifeq            192
        //   127: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   130: invokevirtual   java/lang/Thread.isInterrupted:()Z
        //   133: ifne            192
        //   136: ldc             "cipherName-6"
        //   138: ldc             "DES"
        //   140: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   143: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   146: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   149: pop            
        //   150: aload           6
        //   152: aload           5
        //   154: iconst_0       
        //   155: aload           4
        //   157: aload           5
        //   159: iconst_0       
        //   160: iload_2        
        //   161: invokevirtual   android/media/AudioRecord.read:([BII)I
        //   164: invokevirtual   java/io/OutputStream.write:([BII)V
        //   167: goto            120
        //   170: astore_1       
        //   171: ldc             "cipherName-7"
        //   173: ldc             "DES"
        //   175: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   178: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   181: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   184: pop            
        //   185: aload           4
        //   187: invokevirtual   android/media/AudioRecord.stop:()V
        //   190: aload_1        
        //   191: athrow         
        //   192: ldc             "cipherName-7"
        //   194: ldc             "DES"
        //   196: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   199: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   202: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   205: pop            
        //   206: aload           4
        //   208: invokevirtual   android/media/AudioRecord.stop:()V
        //   211: return         
        //   212: astore          5
        //   214: goto            185
        //   217: astore          5
        //   219: goto            185
        //   222: astore_1       
        //   223: goto            206
        //   226: astore_1       
        //   227: goto            206
        //   230: astore          7
        //   232: goto            150
        //   235: astore          7
        //   237: goto            150
        //   240: astore          6
        //   242: goto            76
        //   245: astore          6
        //   247: goto            76
        //   250: astore          4
        //   252: goto            14
        //   255: astore          4
        //   257: goto            14
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     250    255    Ljava/security/NoSuchAlgorithmException;
        //  0      14     255    260    Ljavax/crypto/NoSuchPaddingException;
        //  62     76     240    245    Ljava/security/NoSuchAlgorithmException;
        //  62     76     245    250    Ljavax/crypto/NoSuchPaddingException;
        //  62     76     170    222    Any
        //  76     120    170    222    Any
        //  120    136    170    222    Any
        //  136    150    230    235    Ljava/security/NoSuchAlgorithmException;
        //  136    150    235    240    Ljavax/crypto/NoSuchPaddingException;
        //  136    150    170    222    Any
        //  150    167    170    222    Any
        //  171    185    212    217    Ljava/security/NoSuchAlgorithmException;
        //  171    185    217    222    Ljavax/crypto/NoSuchPaddingException;
        //  192    206    222    226    Ljava/security/NoSuchAlgorithmException;
        //  192    206    226    230    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0192:
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
    
    private void unregisterService() {
        while (true) {
            try {
                Log.d("cipherName-32", Cipher.getInstance("DES").getAlgorithm());
                if (this._registrationListener == null) {
                    return;
                }
                try {
                    Log.d("cipherName-33", Cipher.getInstance("DES").getAlgorithm());
                    Log.i("BabyMonitor", "Unregistering monitoring service");
                    this._nsdManager.unregisterService(this._registrationListener);
                    this._registrationListener = null;
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
    
    protected void onCreate(final Bundle bundle) {
        Log.i("BabyMonitor", "Baby monitor start");
        while (true) {
            try {
                Log.d("cipherName-8", Cipher.getInstance("DES").getAlgorithm());
                this._nsdManager = (NsdManager)this.getSystemService("servicediscovery");
                super.onCreate(bundle);
                this.setContentView(2130903044);
                (this._serviceThread = new Thread(new Runnable() {
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
                        //    14: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
                        //    17: invokevirtual   java/lang/Thread.isInterrupted:()Z
                        //    20: ifne            255
                        //    23: ldc             "cipherName-10"
                        //    25: ldc             "DES"
                        //    27: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //    30: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //    33: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //    36: pop            
                        //    37: ldc             "cipherName-11"
                        //    39: ldc             "DES"
                        //    41: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //    44: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //    47: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //    50: pop            
                        //    51: new             Ljava/net/ServerSocket;
                        //    54: dup            
                        //    55: iconst_0       
                        //    56: invokespecial   java/net/ServerSocket.<init>:(I)V
                        //    59: astore_2       
                        //    60: aload_2        
                        //    61: invokevirtual   java/net/ServerSocket.getLocalPort:()I
                        //    64: istore_1       
                        //    65: aload_0        
                        //    66: getfield        protect/babymonitor/MonitorActivity$2.this$0:Lprotect/babymonitor/MonitorActivity;
                        //    69: iload_1        
                        //    70: invokestatic    protect/babymonitor/MonitorActivity.access$000:(Lprotect/babymonitor/MonitorActivity;I)V
                        //    73: aload_2        
                        //    74: invokevirtual   java/net/ServerSocket.accept:()Ljava/net/Socket;
                        //    77: astore_3       
                        //    78: ldc             "BabyMonitor"
                        //    80: ldc             "Connection from parent device received"
                        //    82: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
                        //    85: pop            
                        //    86: aload_2        
                        //    87: invokevirtual   java/net/ServerSocket.close:()V
                        //    90: aconst_null    
                        //    91: astore_2       
                        //    92: aload_0        
                        //    93: getfield        protect/babymonitor/MonitorActivity$2.this$0:Lprotect/babymonitor/MonitorActivity;
                        //    96: invokestatic    protect/babymonitor/MonitorActivity.access$100:(Lprotect/babymonitor/MonitorActivity;)V
                        //    99: ldc             "cipherName-12"
                        //   101: ldc             "DES"
                        //   103: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   106: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   109: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   112: pop            
                        //   113: aload_0        
                        //   114: getfield        protect/babymonitor/MonitorActivity$2.this$0:Lprotect/babymonitor/MonitorActivity;
                        //   117: aload_3        
                        //   118: invokestatic    protect/babymonitor/MonitorActivity.access$200:(Lprotect/babymonitor/MonitorActivity;Ljava/net/Socket;)V
                        //   121: ldc             "cipherName-13"
                        //   123: ldc             "DES"
                        //   125: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   128: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   131: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   134: pop            
                        //   135: aload_3        
                        //   136: invokevirtual   java/net/Socket.close:()V
                        //   139: aload_2        
                        //   140: ifnull          14
                        //   143: ldc             "cipherName-15"
                        //   145: ldc             "DES"
                        //   147: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   150: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   153: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   156: pop            
                        //   157: ldc             "cipherName-16"
                        //   159: ldc             "DES"
                        //   161: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   164: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   167: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   170: pop            
                        //   171: aload_2        
                        //   172: invokevirtual   java/net/ServerSocket.close:()V
                        //   175: goto            14
                        //   178: astore_2       
                        //   179: ldc             "cipherName-17"
                        //   181: ldc             "DES"
                        //   183: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   186: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   189: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   192: pop            
                        //   193: ldc             "BabyMonitor"
                        //   195: ldc             "Failed to close stray connection"
                        //   197: aload_2        
                        //   198: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                        //   201: pop            
                        //   202: goto            14
                        //   205: astore_2       
                        //   206: ldc             "cipherName-13"
                        //   208: ldc             "DES"
                        //   210: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   213: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   216: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   219: pop            
                        //   220: aload_3        
                        //   221: invokevirtual   java/net/Socket.close:()V
                        //   224: aload_2        
                        //   225: athrow         
                        //   226: aconst_null    
                        //   227: astore_2       
                        //   228: astore_3       
                        //   229: ldc             "cipherName-14"
                        //   231: ldc             "DES"
                        //   233: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   236: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   239: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   242: pop            
                        //   243: ldc             "BabyMonitor"
                        //   245: ldc             "Connection failed"
                        //   247: aload_3        
                        //   248: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                        //   251: pop            
                        //   252: goto            139
                        //   255: return         
                        //   256: astore_3       
                        //   257: goto            193
                        //   260: astore_3       
                        //   261: goto            193
                        //   264: astore_3       
                        //   265: goto            171
                        //   268: astore_3       
                        //   269: goto            171
                        //   272: astore_3       
                        //   273: goto            157
                        //   276: astore_3       
                        //   277: goto            157
                        //   280: astore          4
                        //   282: goto            243
                        //   285: astore          4
                        //   287: goto            243
                        //   290: astore_3       
                        //   291: goto            229
                        //   294: astore          4
                        //   296: goto            220
                        //   299: astore          4
                        //   301: goto            220
                        //   304: astore          4
                        //   306: goto            135
                        //   309: astore          4
                        //   311: goto            135
                        //   314: astore          4
                        //   316: goto            113
                        //   319: astore          4
                        //   321: goto            113
                        //   324: astore_2       
                        //   325: goto            51
                        //   328: astore_2       
                        //   329: goto            51
                        //   332: astore_2       
                        //   333: goto            37
                        //   336: astore_2       
                        //   337: goto            37
                        //   340: astore_2       
                        //   341: goto            14
                        //   344: astore_2       
                        //   345: goto            14
                        //    Exceptions:
                        //  Try           Handler
                        //  Start  End    Start  End    Type                                    
                        //  -----  -----  -----  -----  ----------------------------------------
                        //  0      14     340    344    Ljava/security/NoSuchAlgorithmException;
                        //  0      14     344    348    Ljavax/crypto/NoSuchPaddingException;
                        //  23     37     332    336    Ljava/security/NoSuchAlgorithmException;
                        //  23     37     336    340    Ljavax/crypto/NoSuchPaddingException;
                        //  37     51     324    328    Ljava/security/NoSuchAlgorithmException;
                        //  37     51     328    332    Ljavax/crypto/NoSuchPaddingException;
                        //  37     51     226    229    Ljava/io/IOException;
                        //  51     60     226    229    Ljava/io/IOException;
                        //  60     90     290    294    Ljava/io/IOException;
                        //  92     99     226    229    Ljava/io/IOException;
                        //  99     113    314    319    Ljava/security/NoSuchAlgorithmException;
                        //  99     113    319    324    Ljavax/crypto/NoSuchPaddingException;
                        //  99     113    205    304    Any
                        //  113    121    205    304    Any
                        //  121    135    304    309    Ljava/security/NoSuchAlgorithmException;
                        //  121    135    309    314    Ljavax/crypto/NoSuchPaddingException;
                        //  121    135    226    229    Ljava/io/IOException;
                        //  135    139    226    229    Ljava/io/IOException;
                        //  143    157    272    276    Ljava/security/NoSuchAlgorithmException;
                        //  143    157    276    280    Ljavax/crypto/NoSuchPaddingException;
                        //  157    171    264    268    Ljava/security/NoSuchAlgorithmException;
                        //  157    171    268    272    Ljavax/crypto/NoSuchPaddingException;
                        //  157    171    178    264    Ljava/io/IOException;
                        //  171    175    178    264    Ljava/io/IOException;
                        //  179    193    256    260    Ljava/security/NoSuchAlgorithmException;
                        //  179    193    260    264    Ljavax/crypto/NoSuchPaddingException;
                        //  206    220    294    299    Ljava/security/NoSuchAlgorithmException;
                        //  206    220    299    304    Ljavax/crypto/NoSuchPaddingException;
                        //  206    220    226    229    Ljava/io/IOException;
                        //  220    226    226    229    Ljava/io/IOException;
                        //  229    243    280    285    Ljava/security/NoSuchAlgorithmException;
                        //  229    243    285    290    Ljavax/crypto/NoSuchPaddingException;
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.IndexOutOfBoundsException: Index: 164, Size: 164
                        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
                        //     at java.util.ArrayList.get(ArrayList.java:433)
                        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
                        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
                        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
                        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
                        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
                this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Log.d("cipherName-18", Cipher.getInstance("DES").getAlgorithm());
                                final TextView textView = (TextView)MonitorActivity.this.findViewById(2131296279);
                                final int ipAddress = ((WifiManager)MonitorActivity.this.getSystemService("wifi")).getConnectionInfo().getIpAddress();
                                Label_0073: {
                                    if (ipAddress == 0) {
                                        break Label_0073;
                                    }
                                    try {
                                        Log.d("cipherName-19", Cipher.getInstance("DES").getAlgorithm());
                                        textView.setText((CharSequence)Formatter.formatIpAddress(ipAddress));
                                        return;
                                        try {
                                            Log.d("cipherName-20", Cipher.getInstance("DES").getAlgorithm());
                                            textView.setText(2131034144);
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
                Log.d("cipherName-23", Cipher.getInstance("DES").getAlgorithm());
                this.getMenuInflater().inflate(2131230720, menu);
                return true;
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
    
    protected void onDestroy() {
        Log.i("BabyMonitor", "Baby monitor stop");
        while (true) {
            try {
                Log.d("cipherName-21", Cipher.getInstance("DES").getAlgorithm());
                this.unregisterService();
                Label_0059: {
                    if (this._serviceThread == null) {
                        break Label_0059;
                    }
                    try {
                        Log.d("cipherName-22", Cipher.getInstance("DES").getAlgorithm());
                        this._serviceThread.interrupt();
                        this._serviceThread = null;
                        super.onDestroy();
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
    
    public boolean onOptionsItemSelected(final MenuItem ex) {
        while (true) {
            try {
                Log.d("cipherName-24", Cipher.getInstance("DES").getAlgorithm());
                Label_0043: {
                    if (((MenuItem)ex).getItemId() != 2131296287) {
                        break Label_0043;
                    }
                    try {
                        Log.d("cipherName-25", Cipher.getInstance("DES").getAlgorithm());
                        return true;
                        return super.onOptionsItemSelected((MenuItem)ex);
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
}
