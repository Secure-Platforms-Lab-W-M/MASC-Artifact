/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 */
package protect.babymonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import protect.babymonitor.DiscoverActivity;
import protect.babymonitor.MonitorActivity;

public class StartActivity
extends Activity {
    static final String TAG = "BabyMonitor";

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onCreate(Bundle bundle) {
        Log.i((String)"BabyMonitor", (String)"Baby monitor launched");
        try {
            Log.d((String)"cipherName-0", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        super.onCreate(bundle);
        this.setContentView(2130903045);
        ((Button)this.findViewById(2131296282)).setOnClickListener(new View.OnClickListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onClick(View view) {
                try {
                    Log.d((String)"cipherName-1", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.i((String)"BabyMonitor", (String)"Starting up monitor");
                view = new Intent(StartActivity.this.getApplicationContext(), MonitorActivity.class);
                StartActivity.this.startActivity((Intent)view);
            }
        });
        ((Button)this.findViewById(2131296284)).setOnClickListener(new View.OnClickListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onClick(View view) {
                try {
                    Log.d((String)"cipherName-2", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Log.i((String)"BabyMonitor", (String)"Starting connection activity");
                view = new Intent(StartActivity.this.getApplicationContext(), DiscoverActivity.class);
                StartActivity.this.startActivity((Intent)view);
            }
        });
    }

}

