/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.net.nsd.NsdManager
 *  android.net.nsd.NsdManager$DiscoveryListener
 *  android.net.nsd.NsdManager$ResolveListener
 *  android.net.nsd.NsdServiceInfo
 *  android.os.Bundle
 *  android.text.Editable
 *  android.util.Log
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.ArrayAdapter
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.Toast
 */
package protect.babymonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import protect.babymonitor.ListenActivity;
import protect.babymonitor.ServiceInfoWrapper;

public class DiscoverActivity
extends Activity {
    final String TAG = "BabyMonitor";
    NsdManager.DiscoveryListener _discoveryListener;
    NsdManager _nsdManager;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void connectToChild(String string2, int n, String string3) {
        try {
            Log.d((String)"cipherName-59", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        Intent intent = new Intent(this.getApplicationContext(), ListenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("address", string2);
        bundle.putInt("port", n);
        bundle.putString("name", string3);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void loadDiscoveryViaAddress() {
        try {
            Log.d((String)"cipherName-38", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.setContentView(2130903041);
        ((Button)this.findViewById(2131296267)).setOnClickListener(new View.OnClickListener(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            public void onClick(View object) {
                block16 : {
                    Object object2;
                    block17 : {
                        try {
                            Log.d((String)"cipherName-39", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        Log.i((String)"BabyMonitor", (String)"Connecting to child device via address");
                        object = (EditText)DiscoverActivity.this.findViewById(2131296264);
                        object2 = (EditText)DiscoverActivity.this.findViewById(2131296265);
                        object = object.getText().toString();
                        object2 = object2.getText().toString();
                        if (object.length() == 0) {
                            Log.d((String)"cipherName-40", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block16;
                        }
                        try {
                            Log.d((String)"cipherName-41", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block17;
                        }
                        catch (NumberFormatException numberFormatException) {
                            try {
                                Log.d((String)"cipherName-42", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            Toast.makeText((Context)DiscoverActivity.this, (int)2131034129, (int)1).show();
                            return;
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            break block17;
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                    }
                    int n = Integer.parseInt((String)object2);
                    DiscoverActivity.this.connectToChild((String)object, n, (String)object);
                    return;
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block16;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                Toast.makeText((Context)DiscoverActivity.this, (int)2131034128, (int)1).show();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void loadDiscoveryViaMdns() {
        try {
            Log.d((String)"cipherName-37", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.setContentView(2130903042);
        this.startServiceDiscovery("_babymonitor._tcp.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onCreate(Bundle bundle) {
        Log.i((String)"BabyMonitor", (String)"Baby monitor start");
        try {
            Log.d((String)"cipherName-34", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this._nsdManager = (NsdManager)this.getSystemService("servicediscovery");
        super.onCreate(bundle);
        this.setContentView(2130903040);
        ((Button)this.findViewById(2131296258)).setOnClickListener(new View.OnClickListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onClick(View view) {
                try {
                    Log.d((String)"cipherName-35", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                DiscoverActivity.this.loadDiscoveryViaMdns();
            }
        });
        ((Button)this.findViewById(2131296260)).setOnClickListener(new View.OnClickListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onClick(View view) {
                try {
                    Log.d((String)"cipherName-36", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                DiscoverActivity.this.loadDiscoveryViaAddress();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onDestroy() {
        Log.i((String)"BabyMonitor", (String)"Baby monitoring stop");
        try {
            Log.d((String)"cipherName-43", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (this._discoveryListener != null) {
            try {
                Log.d((String)"cipherName-44", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Log.i((String)"BabyMonitor", (String)"Unregistering monitoring service");
            this._nsdManager.stopServiceDiscovery(this._discoveryListener);
            this._discoveryListener = null;
        }
        super.onDestroy();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void startServiceDiscovery(final String string2) {
        try {
            Log.d((String)"cipherName-45", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        final NsdManager nsdManager = (NsdManager)this.getSystemService("servicediscovery");
        ListView listView = (ListView)this.findViewById(2131296269);
        final ArrayAdapter arrayAdapter = new ArrayAdapter((Context)this, 2130903046);
        listView.setAdapter((ListAdapter)arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onItemClick(AdapterView<?> object, View view, int n, long l) {
                try {
                    Log.d((String)"cipherName-46", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                object = (ServiceInfoWrapper)object.getItemAtPosition(n);
                DiscoverActivity.this.connectToChild(object.getAddress(), object.getPort(), object.getName());
            }
        });
        this._discoveryListener = new NsdManager.DiscoveryListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onDiscoveryStarted(String string22) {
                try {
                    Log.d((String)"cipherName-47", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.d((String)"BabyMonitor", (String)"Service discovery started");
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onDiscoveryStopped(String string22) {
                try {
                    Log.d((String)"cipherName-56", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.i((String)"BabyMonitor", (String)("Discovery stopped: " + string22));
            }

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                block15 : {
                    block16 : {
                        try {
                            Log.d((String)"cipherName-48", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        Log.d((String)"BabyMonitor", (String)("Service discovery success: " + (Object)nsdServiceInfo));
                        if (!nsdServiceInfo.getServiceType().equals(string2)) {
                            Log.d((String)"cipherName-49", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block15;
                        }
                        if (nsdServiceInfo.getServiceName().contains("ProtectBabyMonitor")) {
                            Log.d((String)"cipherName-50", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block16;
                        }
                        try {
                            Log.d((String)"cipherName-54", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        Log.d((String)"BabyMonitor", (String)("Unknown Service name: " + nsdServiceInfo.getServiceName()));
                        return;
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            break block16;
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                    }
                    NsdManager.ResolveListener resolveListener = new NsdManager.ResolveListener(){

                        /*
                         * Enabled aggressive block sorting
                         * Enabled unnecessary exception pruning
                         * Enabled aggressive exception aggregation
                         */
                        public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int n) {
                            try {
                                Log.d((String)"cipherName-51", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            Log.e((String)"BabyMonitor", (String)("Resolve failed: error " + n + " for service: " + (Object)nsdServiceInfo));
                        }

                        /*
                         * Enabled aggressive block sorting
                         * Enabled unnecessary exception pruning
                         * Enabled aggressive exception aggregation
                         */
                        public void onServiceResolved(final NsdServiceInfo nsdServiceInfo) {
                            try {
                                Log.d((String)"cipherName-52", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            Log.i((String)"BabyMonitor", (String)("Resolve Succeeded: " + (Object)nsdServiceInfo));
                            DiscoverActivity.this.runOnUiThread(new Runnable(){

                                /*
                                 * Enabled aggressive block sorting
                                 * Enabled unnecessary exception pruning
                                 * Enabled aggressive exception aggregation
                                 */
                                @Override
                                public void run() {
                                    try {
                                        Log.d((String)"cipherName-53", (String)Cipher.getInstance("DES").getAlgorithm());
                                    }
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                    arrayAdapter.add((Object)new ServiceInfoWrapper(nsdServiceInfo));
                                }
                            });
                        }

                    };
                    DiscoverActivity.this._nsdManager.resolveService(nsdServiceInfo, resolveListener);
                    return;
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block15;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                Log.d((String)"BabyMonitor", (String)("Unknown Service Type: " + nsdServiceInfo.getServiceType()));
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
                try {
                    Log.d((String)"cipherName-55", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.e((String)"BabyMonitor", (String)("Service lost: " + (Object)nsdServiceInfo));
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onStartDiscoveryFailed(String string22, int n) {
                try {
                    Log.d((String)"cipherName-57", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.e((String)"BabyMonitor", (String)("Discovery failed: Error code: " + n));
                nsdManager.stopServiceDiscovery((NsdManager.DiscoveryListener)this);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onStopDiscoveryFailed(String string22, int n) {
                try {
                    Log.d((String)"cipherName-58", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.e((String)"BabyMonitor", (String)("Discovery failed: Error code: " + n));
                nsdManager.stopServiceDiscovery((NsdManager.DiscoveryListener)this);
            }

        };
        nsdManager.discoverServices(string2, 1, this._discoveryListener);
    }

}

