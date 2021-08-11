/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.media.AudioRecord
 *  android.net.nsd.NsdManager
 *  android.net.nsd.NsdManager$RegistrationListener
 *  android.net.nsd.NsdServiceInfo
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  android.os.Bundle
 *  android.text.format.Formatter
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.widget.TextView
 */
package protect.babymonitor;

import android.app.Activity;
import android.media.AudioRecord;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MonitorActivity
extends Activity {
    final String TAG = "BabyMonitor";
    NsdManager _nsdManager;
    NsdManager.RegistrationListener _registrationListener;
    Thread _serviceThread;

    static /* synthetic */ void access$000(MonitorActivity monitorActivity, int n) {
        monitorActivity.registerService(n);
    }

    static /* synthetic */ void access$100(MonitorActivity monitorActivity) {
        monitorActivity.unregisterService();
    }

    static /* synthetic */ void access$200(MonitorActivity monitorActivity, Socket socket) throws IOException {
        monitorActivity.serviceConnection(socket);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void registerService(final int n) {
        try {
            Log.d((String)"cipherName-26", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName("ProtectBabyMonitor");
        nsdServiceInfo.setServiceType("_babymonitor._tcp.");
        nsdServiceInfo.setPort(n);
        this._registrationListener = new NsdManager.RegistrationListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int n2) {
                try {
                    Log.d((String)"cipherName-29", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.e((String)"BabyMonitor", (String)("Registration failed: " + n2));
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onServiceRegistered(NsdServiceInfo object) {
                try {
                    Log.d((String)"cipherName-27", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                object = object.getServiceName();
                Log.i((String)"BabyMonitor", (String)("Service name: " + (String)object));
                MonitorActivity.this.runOnUiThread(new Runnable((String)object){
                    final /* synthetic */ String val$serviceName;
                    {
                        this.val$serviceName = string2;
                    }

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    @Override
                    public void run() {
                        try {
                            Log.d((String)"cipherName-28", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        ((TextView)MonitorActivity.this.findViewById(2131296273)).setText(2131034143);
                        ((TextView)MonitorActivity.this.findViewById(2131296276)).setText((CharSequence)this.val$serviceName);
                        ((TextView)MonitorActivity.this.findViewById(2131296280)).setText((CharSequence)Integer.toString(n));
                    }
                });
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                try {
                    Log.d((String)"cipherName-30", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.i((String)"BabyMonitor", (String)"Unregistering service");
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int n2) {
                try {
                    Log.d((String)"cipherName-31", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.e((String)"BabyMonitor", (String)("Unregistration failed: " + n2));
            }

        };
        this._nsdManager.registerService(nsdServiceInfo, 1, this._registrationListener);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void serviceConnection(Socket var1_1) throws IOException {
        try {
            Log.d((String)"cipherName-3", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException var4_6) {
        }
        catch (NoSuchPaddingException var4_7) {}
        this.runOnUiThread(new Runnable(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                try {
                    Log.d((String)"cipherName-4", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                ((TextView)MonitorActivity.this.findViewById(2131296273)).setText(2131034140);
            }
        });
        var2_8 = AudioRecord.getMinBufferSize((int)11025, (int)16, (int)2);
        var4_5 = new AudioRecord(1, 11025, 16, 2, var2_8);
        var3_9 = var2_8 * 2;
        var5_10 = new byte[var3_9];
        try {
            Log.d((String)"cipherName-5", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException var6_14) {
        }
        catch (NoSuchPaddingException var6_15) {}
        var4_5.startRecording();
        var6_13 = var1_1.getOutputStream();
        var1_1.setSendBufferSize(var3_9);
        Log.d((String)"BabyMonitor", (String)("Socket send buffer size: " + var1_1.getSendBufferSize()));
lbl25: // 2 sources:
        if (var1_1.isConnected() == false) return;
        if (Thread.currentThread().isInterrupted() != false) return;
        try {
            Log.d((String)"cipherName-6", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException var7_17) {
        }
        catch (NoSuchPaddingException var7_18) {}
        finally {
            Log.d((String)"cipherName-7", (String)Cipher.getInstance("DES").getAlgorithm());
            var4_5.stop();
        }
        var6_13.write(var5_10, 0, var4_5.read(var5_10, 0, var2_8));
        ** GOTO lbl25
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void unregisterService() {
        try {
            Log.d((String)"cipherName-32", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (this._registrationListener != null) {
            try {
                Log.d((String)"cipherName-33", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Log.i((String)"BabyMonitor", (String)"Unregistering monitoring service");
            this._nsdManager.unregisterService(this._registrationListener);
            this._registrationListener = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onCreate(Bundle bundle) {
        Log.i((String)"BabyMonitor", (String)"Baby monitor start");
        try {
            Log.d((String)"cipherName-8", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this._nsdManager = (NsdManager)this.getSystemService("servicediscovery");
        super.onCreate(bundle);
        this.setContentView(2130903044);
        this._serviceThread = new Thread(new Runnable(){

            /*
             * Exception decompiling
             */
            @Override
            public void run() {
                // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:397)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:475)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
                // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
                // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
                // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
                // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
                // org.benf.cfr.reader.Main.doJar(Main.java:134)
                // org.benf.cfr.reader.Main.main(Main.java:189)
                throw new IllegalStateException("Decompilation failed");
            }
        });
        this._serviceThread.start();
        this.runOnUiThread(new Runnable(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                int n;
                TextView textView;
                block11 : {
                    try {
                        Log.d((String)"cipherName-18", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    textView = (TextView)MonitorActivity.this.findViewById(2131296279);
                    n = ((WifiManager)MonitorActivity.this.getSystemService("wifi")).getConnectionInfo().getIpAddress();
                    if (n != 0) {
                        Log.d((String)"cipherName-19", (String)Cipher.getInstance("DES").getAlgorithm());
                        break block11;
                    }
                    try {
                        Log.d((String)"cipherName-20", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    textView.setText(2131034144);
                    return;
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block11;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                textView.setText((CharSequence)Formatter.formatIpAddress((int)n));
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean onCreateOptionsMenu(Menu menu2) {
        try {
            Log.d((String)"cipherName-23", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.getMenuInflater().inflate(2131230720, menu2);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onDestroy() {
        Log.i((String)"BabyMonitor", (String)"Baby monitor stop");
        try {
            Log.d((String)"cipherName-21", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.unregisterService();
        if (this._serviceThread != null) {
            try {
                Log.d((String)"cipherName-22", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this._serviceThread.interrupt();
            this._serviceThread = null;
        }
        super.onDestroy();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        try {
            Log.d((String)"cipherName-24", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (menuItem.getItemId() != 2131296287) return super.onOptionsItemSelected(menuItem);
        try {
            Log.d((String)"cipherName-25", (String)Cipher.getInstance("DES").getAlgorithm());
            return true;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return true;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return true;
        }
    }

}

