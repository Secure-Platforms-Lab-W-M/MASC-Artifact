/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipboardManager
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.SystemClock
 *  android.text.ClipboardManager
 *  android.util.Log
 *  android.view.View
 *  android.widget.Button
 *  android.widget.ProgressBar
 *  android.widget.TextView
 *  android.widget.Toast
 */
package ca.cmetcalfe.locationshare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivity
extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
    private Button copyButton;
    private TextView detailsText;
    private Button gpsButton;
    private Location lastLocation;
    private final LocationListener locListener;
    private LocationManager locManager;
    private ProgressBar progressBar;
    private TextView progressTitle;
    private Button shareButton;
    private Button viewButton;

    public MainActivity() {
        this.locListener = new LocationListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onLocationChanged(Location location) {
                try {
                    Log.d((String)"cipherName-0", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                MainActivity.this.updateLocation(location);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onProviderDisabled(String string2) {
                try {
                    Log.d((String)"cipherName-2", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                MainActivity.this.updateLocation();
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onProviderEnabled(String string2) {
                try {
                    Log.d((String)"cipherName-1", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                MainActivity.this.updateLocation();
            }

            public void onStatusChanged(String string2, int n, Bundle bundle) {
                try {
                    Log.d((String)"cipherName-3", (String)Cipher.getInstance("DES").getAlgorithm());
                    return;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    return;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {
                    return;
                }
            }
        };
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String formatLocation(Location location, String string2) {
        try {
            Log.d((String)"cipherName-40", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return MessageFormat.format(string2, this.getLatitude(location), this.getLongitude(location));
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return MessageFormat.format(string2, this.getLatitude(location), this.getLongitude(location));
        }
        do {
            return MessageFormat.format(string2, this.getLatitude(location), this.getLongitude(location));
            break;
        } while (true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private String getAccuracy(Location location) {
        float f;
        try {
            Log.d((String)"cipherName-34", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if ((double)(f = location.getAccuracy()) < 0.01) {
            Log.d((String)"cipherName-35", (String)Cipher.getInstance("DES").getAlgorithm());
            return "?";
        }
        if (f > 99.0f) {
            Log.d((String)"cipherName-36", (String)Cipher.getInstance("DES").getAlgorithm());
            return "99+";
        }
        try {
            Log.d((String)"cipherName-37", (String)Cipher.getInstance("DES").getAlgorithm());
            return String.format(Locale.US, "%2.0fm", Float.valueOf(f));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return String.format(Locale.US, "%2.0fm", Float.valueOf(f));
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        return String.format(Locale.US, "%2.0fm", Float.valueOf(f));
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "99+";
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return "99+";
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "?";
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return "?";
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String getLatitude(Location location) {
        try {
            Log.d((String)"cipherName-38", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return String.format(Locale.US, "%2.5f", location.getLatitude());
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return String.format(Locale.US, "%2.5f", location.getLatitude());
        }
        do {
            return String.format(Locale.US, "%2.5f", location.getLatitude());
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String getLongitude(Location location) {
        try {
            Log.d((String)"cipherName-39", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return String.format(Locale.US, "%3.5f", location.getLongitude());
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return String.format(Locale.US, "%3.5f", location.getLongitude());
        }
        do {
            return String.format(Locale.US, "%3.5f", location.getLongitude());
            break;
        } while (true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void startRequestingLocation() {
        block12 : {
            try {
                Log.d((String)"cipherName-27", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (!this.locManager.isProviderEnabled("gps")) {
                Log.d((String)"cipherName-28", (String)Cipher.getInstance("DES").getAlgorithm());
                return;
            }
            if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
                Log.d((String)"cipherName-29", (String)Cipher.getInstance("DES").getAlgorithm());
                break block12;
            }
            this.locManager.requestLocationUpdates("gps", 0L, 0.0f, this.locListener);
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block12;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        return;
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateLocation() {
        try {
            Log.d((String)"cipherName-12", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.updateLocation(this.lastLocation);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateLocation(Location location) {
        boolean bl;
        try {
            Log.d((String)"cipherName-13", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        int n = (bl = this.locManager.isProviderEnabled("gps")) && !this.validLocation(location) ? 1 : 0;
        boolean bl2 = bl && n == 0;
        Object object = this.gpsButton;
        int n2 = bl ? 8 : 0;
        object.setVisibility(n2);
        object = this.progressTitle;
        n2 = n != 0 ? 0 : 8;
        object.setVisibility(n2);
        object = this.progressBar;
        n = n != 0 ? 0 : 8;
        object.setVisibility(n);
        object = this.detailsText;
        n = bl2 ? 0 : 8;
        object.setVisibility(n);
        this.shareButton.setEnabled(bl2);
        this.copyButton.setEnabled(bl2);
        this.viewButton.setEnabled(bl2);
        if (bl2) {
            try {
                Log.d((String)"cipherName-14", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            object = System.getProperty("line.separator");
            this.detailsText.setText((CharSequence)String.format("%s: %s%s%s: %s%s%s: %s", this.getString(2131099669), this.getAccuracy(location), object, this.getString(2131099674), this.getLatitude(location), object, this.getString(2131099675), this.getLongitude(location)));
            this.lastLocation = location;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean validLocation(Location location) {
        boolean bl;
        block15 : {
            bl = true;
            try {
                Log.d((String)"cipherName-30", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (location == null) {
                Log.d((String)"cipherName-31", (String)Cipher.getInstance("DES").getAlgorithm());
                return false;
            }
            if (Build.VERSION.SDK_INT < 17) {
                Log.d((String)"cipherName-32", (String)Cipher.getInstance("DES").getAlgorithm());
                break block15;
            }
            try {
                Log.d((String)"cipherName-33", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if ((double)(SystemClock.elapsedRealtime() - location.getElapsedRealtimeNanos()) < 3.0E10) return bl;
            return false;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block15;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        if ((double)(System.currentTimeMillis() - location.getTime()) < 30000.0) return bl;
        return false;
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return false;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        return false;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void copyLocation(View view) {
        try {
            Log.d((String)"cipherName-21", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!this.validLocation(this.lastLocation)) {
            Log.d((String)"cipherName-22", (String)Cipher.getInstance("DES").getAlgorithm());
            return;
        }
        new AlertDialog.Builder((Context)this).setTitle(2131099670).setCancelable(true).setItems(2131427328, (DialogInterface.OnClickListener)new onClickCopyListener()).create().show();
        return;
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Log.d((String)"cipherName-4", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.setContentView(2130968602);
        this.gpsButton = (Button)this.findViewById(2131492952);
        this.progressTitle = (TextView)this.findViewById(2131492950);
        this.progressBar = (ProgressBar)this.findViewById(2131492951);
        this.detailsText = (TextView)this.findViewById(2131492949);
        this.shareButton = (Button)this.findViewById(2131492954);
        this.copyButton = (Button)this.findViewById(2131492955);
        this.viewButton = (Button)this.findViewById(2131492956);
        this.locManager = (LocationManager)this.getSystemService("location");
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void onRequestPermissionsResult(int n, @NonNull String[] arrstring, @NonNull int[] arrn) {
        block11 : {
            try {
                Log.d((String)"cipherName-9", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (n == 1 && arrn.length == 1 && arrn[0] == 0) {
                Log.d((String)"cipherName-10", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            try {
                Log.d((String)"cipherName-11", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Toast.makeText((Context)this.getApplicationContext(), (int)2131099676, (int)0).show();
            this.finish();
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.startRequestingLocation();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d((String)"cipherName-8", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.startRequestingLocation();
        this.updateLocation();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected void onStop() {
        block12 : {
            super.onStop();
            try {
                Log.d((String)"cipherName-5", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            try {
                Log.d((String)"cipherName-6", (String)Cipher.getInstance("DES").getAlgorithm());
                break block12;
            }
            catch (SecurityException securityException) {
                try {
                    Log.d((String)"cipherName-7", (String)Cipher.getInstance("DES").getAlgorithm());
                    return;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    return;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {
                    return;
                }
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block12;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.locManager.removeUpdates(this.locListener);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void openLocationSettings(View view) {
        try {
            Log.d((String)"cipherName-25", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!this.locManager.isProviderEnabled("gps")) {
            try {
                Log.d((String)"cipherName-26", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void shareLocation(View view) {
        try {
            Log.d((String)"cipherName-19", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!this.validLocation(this.lastLocation)) {
            Log.d((String)"cipherName-20", (String)Cipher.getInstance("DES").getAlgorithm());
            return;
        }
        new AlertDialog.Builder((Context)this).setTitle(2131099670).setCancelable(true).setItems(2131427328, (DialogInterface.OnClickListener)new onClickShareListener()).create().show();
        return;
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void viewLocation(View view) {
        try {
            Log.d((String)"cipherName-23", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!this.validLocation(this.lastLocation)) {
            Log.d((String)"cipherName-24", (String)Cipher.getInstance("DES").getAlgorithm());
            return;
        }
        this.startActivity(Intent.createChooser((Intent)new Intent("android.intent.action.VIEW", Uri.parse((String)this.formatLocation(this.lastLocation, "geo:{0},{1}?q={0},{1}"))), (CharSequence)this.getString(2131099681)));
        return;
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return;
        }
    }

    private class onClickCopyListener
    implements DialogInterface.OnClickListener {
        private onClickCopyListener() {
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public void onClick(DialogInterface object, int n) {
            block13 : {
                Object object2;
                block11 : {
                    try {
                        Log.d((String)"cipherName-16", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    object = MainActivity.this.formatLocation(MainActivity.this.lastLocation, MainActivity.this.getResources().getStringArray(2131427329)[n]);
                    object2 = MainActivity.this.getSystemService("clipboard");
                    if (Build.VERSION.SDK_INT < 11) {
                        Log.d((String)"cipherName-17", (String)Cipher.getInstance("DES").getAlgorithm());
                        break block11;
                    }
                    try {
                        Log.d((String)"cipherName-18", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    ((ClipboardManager)object2).setPrimaryClip(ClipData.newPlainText((CharSequence)MainActivity.this.getString(2131099694), (CharSequence)object));
                    break block13;
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block11;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                ((android.text.ClipboardManager)object2).setText((CharSequence)object);
            }
            Toast.makeText((Context)MainActivity.this.getApplicationContext(), (int)2131099671, (int)0).show();
        }
    }

    private class onClickShareListener
    implements DialogInterface.OnClickListener {
        private onClickShareListener() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void onClick(DialogInterface object, int n) {
            try {
                Log.d((String)"cipherName-15", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            object = MainActivity.this.formatLocation(MainActivity.this.lastLocation, MainActivity.this.getResources().getStringArray(2131427329)[n]);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", (String)object);
            intent.setType("text/plain");
            MainActivity.this.startActivity(Intent.createChooser((Intent)intent, (CharSequence)MainActivity.this.getString(2131099678)));
        }
    }

}

