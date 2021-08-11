// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package protect.babymonitor;

import android.net.nsd.NsdManager$ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.Button;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import javax.crypto.Cipher;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager$DiscoveryListener;
import android.app.Activity;

public class DiscoverActivity extends Activity
{
    final String TAG;
    NsdManager$DiscoveryListener _discoveryListener;
    NsdManager _nsdManager;
    
    public DiscoverActivity() {
        this.TAG = "BabyMonitor";
    }
    
    private void connectToChild(final String s, final int n, final String s2) {
        while (true) {
            try {
                Log.d("cipherName-59", Cipher.getInstance("DES").getAlgorithm());
                final Intent intent = new Intent(this.getApplicationContext(), (Class)ListenActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putString("address", s);
                bundle.putInt("port", n);
                bundle.putString("name", s2);
                intent.putExtras(bundle);
                this.startActivity(intent);
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
    
    private void loadDiscoveryViaAddress() {
        while (true) {
            try {
                Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2130903041);
                ((Button)this.findViewById(2131296267)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(View string) {
                        while (true) {
                            try {
                                Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
                                Log.i("BabyMonitor", "Connecting to child device via address");
                                final EditText editText = (EditText)DiscoverActivity.this.findViewById(2131296264);
                                final EditText editText2 = (EditText)DiscoverActivity.this.findViewById(2131296265);
                                string = (NumberFormatException)editText.getText().toString();
                                final String string2 = editText2.getText().toString();
                                Label_0099: {
                                    if (((String)string).length() != 0) {
                                        break Label_0099;
                                    }
                                    try {
                                        Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
                                        Toast.makeText((Context)DiscoverActivity.this, 2131034128, 1).show();
                                        return;
                                        try {
                                            try {
                                                Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
                                                DiscoverActivity.this.connectToChild((String)string, Integer.parseInt(string2), (String)string);
                                            }
                                            catch (NumberFormatException string) {
                                                try {
                                                    Log.d("cipherName-42", Cipher.getInstance("DES").getAlgorithm());
                                                    Toast.makeText((Context)DiscoverActivity.this, 2131034129, 1).show();
                                                }
                                                catch (NoSuchAlgorithmException string) {}
                                                catch (NoSuchPaddingException string) {}
                                            }
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
    
    private void loadDiscoveryViaMdns() {
        while (true) {
            try {
                Log.d("cipherName-37", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2130903042);
                this.startServiceDiscovery("_babymonitor._tcp.");
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
    
    protected void onCreate(final Bundle bundle) {
        Log.i("BabyMonitor", "Baby monitor start");
        while (true) {
            try {
                Log.d("cipherName-34", Cipher.getInstance("DES").getAlgorithm());
                this._nsdManager = (NsdManager)this.getSystemService("servicediscovery");
                super.onCreate(bundle);
                this.setContentView(2130903040);
                ((Button)this.findViewById(2131296258)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-35", Cipher.getInstance("DES").getAlgorithm());
                                DiscoverActivity.this.loadDiscoveryViaMdns();
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
                ((Button)this.findViewById(2131296260)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-36", Cipher.getInstance("DES").getAlgorithm());
                                DiscoverActivity.this.loadDiscoveryViaAddress();
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
    
    protected void onDestroy() {
        Log.i("BabyMonitor", "Baby monitoring stop");
        while (true) {
            try {
                Log.d("cipherName-43", Cipher.getInstance("DES").getAlgorithm());
                Label_0067: {
                    if (this._discoveryListener == null) {
                        break Label_0067;
                    }
                    try {
                        Log.d("cipherName-44", Cipher.getInstance("DES").getAlgorithm());
                        Log.i("BabyMonitor", "Unregistering monitoring service");
                        this._nsdManager.stopServiceDiscovery(this._discoveryListener);
                        this._discoveryListener = null;
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
    
    public void startServiceDiscovery(final String s) {
        while (true) {
            try {
                Log.d("cipherName-45", Cipher.getInstance("DES").getAlgorithm());
                final NsdManager nsdManager = (NsdManager)this.getSystemService("servicediscovery");
                final ListView listView = (ListView)this.findViewById(2131296269);
                final ArrayAdapter adapter = new ArrayAdapter((Context)this, 2130903046);
                listView.setAdapter((ListAdapter)adapter);
                listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(AdapterView<?> serviceInfoWrapper, final View view, final int n, final long n2) {
                        while (true) {
                            try {
                                Log.d("cipherName-46", Cipher.getInstance("DES").getAlgorithm());
                                serviceInfoWrapper = (ServiceInfoWrapper)((AdapterView)serviceInfoWrapper).getItemAtPosition(n);
                                DiscoverActivity.this.connectToChild(serviceInfoWrapper.getAddress(), serviceInfoWrapper.getPort(), serviceInfoWrapper.getName());
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
                nsdManager.discoverServices(s, 1, this._discoveryListener = (NsdManager$DiscoveryListener)new NsdManager$DiscoveryListener() {
                    public void onDiscoveryStarted(final String s) {
                        while (true) {
                            try {
                                Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
                                Log.d("BabyMonitor", "Service discovery started");
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
                    
                    public void onDiscoveryStopped(final String s) {
                        while (true) {
                            try {
                                Log.d("cipherName-56", Cipher.getInstance("DES").getAlgorithm());
                                Log.i("BabyMonitor", "Discovery stopped: " + s);
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
                    
                    public void onServiceFound(final NsdServiceInfo nsdServiceInfo) {
                        while (true) {
                            try {
                                Log.d("cipherName-48", Cipher.getInstance("DES").getAlgorithm());
                                Log.d("BabyMonitor", "Service discovery success: " + nsdServiceInfo);
                                Label_0096: {
                                    if (nsdServiceInfo.getServiceType().equals(s)) {
                                        break Label_0096;
                                    }
                                    try {
                                        Log.d("cipherName-49", Cipher.getInstance("DES").getAlgorithm());
                                        Log.d("BabyMonitor", "Unknown Service Type: " + nsdServiceInfo.getServiceType());
                                        return;
                                        // iftrue(Label_0144:, !nsdServiceInfo.getServiceName().contains((CharSequence)"ProtectBabyMonitor"))
                                        try {
                                            Log.d("cipherName-50", Cipher.getInstance("DES").getAlgorithm());
                                            DiscoverActivity.this._nsdManager.resolveService(nsdServiceInfo, (NsdManager$ResolveListener)new NsdManager$ResolveListener() {
                                                public void onResolveFailed(final NsdServiceInfo nsdServiceInfo, final int n) {
                                                    while (true) {
                                                        try {
                                                            Log.d("cipherName-51", Cipher.getInstance("DES").getAlgorithm());
                                                            Log.e("BabyMonitor", "Resolve failed: error " + n + " for service: " + nsdServiceInfo);
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
                                                
                                                public void onServiceResolved(final NsdServiceInfo nsdServiceInfo) {
                                                    while (true) {
                                                        try {
                                                            Log.d("cipherName-52", Cipher.getInstance("DES").getAlgorithm());
                                                            Log.i("BabyMonitor", "Resolve Succeeded: " + nsdServiceInfo);
                                                            DiscoverActivity.this.runOnUiThread((Runnable)new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    while (true) {
                                                                        try {
                                                                            Log.d("cipherName-53", Cipher.getInstance("DES").getAlgorithm());
                                                                            adapter.add((Object)new ServiceInfoWrapper(nsdServiceInfo));
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
                                            });
                                            return;
                                            try {
                                                Label_0144: {
                                                    Log.d("cipherName-54", Cipher.getInstance("DES").getAlgorithm());
                                                }
                                                Log.d("BabyMonitor", "Unknown Service name: " + nsdServiceInfo.getServiceName());
                                            }
                                            catch (NoSuchAlgorithmException ex) {}
                                            catch (NoSuchPaddingException ex2) {}
                                        }
                                        catch (NoSuchAlgorithmException ex3) {}
                                        catch (NoSuchPaddingException ex4) {}
                                    }
                                    catch (NoSuchAlgorithmException ex5) {}
                                    catch (NoSuchPaddingException ex6) {}
                                }
                            }
                            catch (NoSuchAlgorithmException ex7) {
                                continue;
                            }
                            catch (NoSuchPaddingException ex8) {
                                continue;
                            }
                            break;
                        }
                    }
                    
                    public void onServiceLost(final NsdServiceInfo nsdServiceInfo) {
                        while (true) {
                            try {
                                Log.d("cipherName-55", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("BabyMonitor", "Service lost: " + nsdServiceInfo);
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
                    
                    public void onStartDiscoveryFailed(final String s, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-57", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("BabyMonitor", "Discovery failed: Error code: " + n);
                                nsdManager.stopServiceDiscovery((NsdManager$DiscoveryListener)this);
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
                    
                    public void onStopDiscoveryFailed(final String s, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-58", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("BabyMonitor", "Discovery failed: Error code: " + n);
                                nsdManager.stopServiceDiscovery((NsdManager$DiscoveryListener)this);
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
}
