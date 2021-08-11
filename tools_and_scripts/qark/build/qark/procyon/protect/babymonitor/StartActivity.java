// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package protect.babymonitor;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.content.Intent;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.Button;
import javax.crypto.Cipher;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

public class StartActivity extends Activity
{
    static final String TAG = "BabyMonitor";
    
    protected void onCreate(final Bundle bundle) {
        Log.i("BabyMonitor", "Baby monitor launched");
        while (true) {
            try {
                Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
                super.onCreate(bundle);
                this.setContentView(2130903045);
                ((Button)this.findViewById(2131296282)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
                                Log.i("BabyMonitor", "Starting up monitor");
                                StartActivity.this.startActivity(new Intent(StartActivity.this.getApplicationContext(), (Class)MonitorActivity.class));
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
                ((Button)this.findViewById(2131296284)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
                                Log.i("BabyMonitor", "Starting connection activity");
                                StartActivity.this.startActivity(new Intent(StartActivity.this.getApplicationContext(), (Class)DiscoverActivity.class));
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
