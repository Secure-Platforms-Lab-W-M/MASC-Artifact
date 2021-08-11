// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.oF2pks.kalturadeviceinfos;

import android.media.MediaDrm$ProvisionRequest;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import android.util.Base64;
import java.net.URL;
import java.net.HttpURLConnection;
import android.media.MediaDrm;
import android.widget.Toast;
import java.io.FileWriter;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View$OnClickListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.text.util.Linkify;
import android.graphics.Typeface;
import android.widget.TextView;
import java.io.File;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.net.Uri;
import android.os.Build;
import android.os.Build$VERSION;
import android.provider.Settings$Secure;
import android.util.Log;
import javax.crypto.Cipher;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    String report;
    
    private String displayIDs() {
        while (true) {
            try {
                Log.d("cipherName-132", Cipher.getInstance("DES").getAlgorithm());
                String s2;
                final String s = s2 = "\nANDROIDid: " + Settings$Secure.getString(this.getContentResolver(), "android_id") + "\n";
                if (Build$VERSION.SDK_INT < 28) {
                    s2 = s + "SERIALid:  " + Build.SERIAL + "\n";
                }
                final Cursor query = this.getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), (String[])null, (String)null, new String[] { "android_id" }, (String)null);
                Label_0151: {
                    if (query != null) {
                        break Label_0151;
                    }
                    try {
                        Log.d("cipherName-133", Cipher.getInstance("DES").getAlgorithm());
                        return s2 + "GSFid:     unknow\n";
                        // iftrue(Label_0254:, query.getString(1) != null)
                        while (true) {
                            try {
                                Label_0180: {
                                    Log.d("cipherName-134", Cipher.getInstance("DES").getAlgorithm());
                                }
                                final String s3 = s2 += "GSFid:     unknow\n";
                                if (query.getString(1) == null) {
                                    s2 = s3 + "No account, nu gsf...";
                                }
                                query.close();
                                return s2;
                                Label_0254:
                                final String hexString = Long.toHexString(Long.parseLong(query.getString(1)));
                                query.close();
                                s2 = s2 + "\nGSFid:     " + hexString.toUpperCase().trim() + "\n\n";
                                s2 += "REGISTER GSF https://www.google.com/android/uncertified\n\n";
                                return s2 + "More info https://www.xda-developers.com/how-to-fix-device-not-certified-by-google-error/";
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                            continue;
                        }
                    }
                    // iftrue(Label_0180:, !query.moveToFirst() || query.getColumnCount() < 2)
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
    
    private Intent intentWithText(final String s, final String s2) {
        while (true) {
            try {
                Log.d("cipherName-116", Cipher.getInstance("DES").getAlgorithm());
                final Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", s2);
                intent.putExtra("android.intent.extra.SUBJECT", s);
                intent.setType("text/plain");
                return intent;
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
    
    private void provisionFailed(final Exception ex) {
        try {
            Log.d("cipherName-113", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex2) {}
        catch (NoSuchPaddingException ex3) {}
    }
    
    private void provisionSuccessful() {
        try {
            Log.d("cipherName-114", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex2) {}
    }
    
    private void shareReport() {
        while (true) {
            try {
                Log.d("cipherName-115", Cipher.getInstance("DES").getAlgorithm());
                this.startActivity(Intent.createChooser(this.intentWithText("Kaltura Device Info - Report" + Build.BRAND + "/" + Build.MODEL + "/" + Build$VERSION.RELEASE + "/" + Build$VERSION.SDK_INT, this.report), this.getResources().getText(2131558441)));
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
    
    private void showActionsDialog() {
        while (true) {
            try {
                Log.d("cipherName-107", Cipher.getInstance("DES").getAlgorithm());
                final String prop = Utils.getProp("ro.vndk.version");
                final StringBuilder append = new StringBuilder().append("Treble linker namespace (").append(prop);
                String s;
                if (Utils.getProp("ro.vndk.lite").equals("true")) {
                    s = "lite)";
                }
                else {
                    s = ")";
                }
                new AlertDialog.Builder((Context)this).setTitle("Select action").setItems(new String[] { "Share...", "Refresh", "PRIVATE Ids GSF/Serial/Android", "proc/meminfo", "proc/cpuinfo", "?/etc/gps.conf", "getprop (aio)", "df (mounts)", "dumpsys media.extractor", append.append(s).toString(), "Matrix" }, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-108", Cipher.getInstance("DES").getAlgorithm());
                                switch (n) {
                                    default: {}
                                    case 0: {
                                        MainActivity.this.shareReport();
                                    }
                                    case 1: {
                                        new CollectorTask().execute((Object[])new Boolean[] { false });
                                    }
                                    case 2: {
                                        MainActivity.this.showIDs();
                                    }
                                    case 3: {
                                        MainActivity.this.showZinfo("cat /proc/meminfo", true, false);
                                    }
                                    case 4: {
                                        MainActivity.this.showZinfo("cat /proc/cpuinfo", false, true);
                                    }
                                    case 5: {
                                        if (new File("/system/etc/gps.conf").exists()) {
                                            MainActivity.this.showZinfo("cat /system/etc/gps.conf", true, true);
                                            return;
                                        }
                                        MainActivity.this.showZinfo("cat /vendor/etc/gps.conf", true, true);
                                    }
                                    case 6: {
                                        MainActivity.this.showZinfo("getprop", false, false);
                                    }
                                    case 7: {
                                        MainActivity.this.showZinfo("df", true, false);
                                    }
                                    case 8: {
                                        MainActivity.this.showZinfo("dumpsys media.extractor", false, true);
                                    }
                                    case 9: {
                                        if (Utils.getProp("ro.vndk.lite").equals("true")) {
                                            MainActivity.this.showZinfo("cat /system/etc/ld.config.vndk_lite.txt", false, false);
                                            return;
                                        }
                                        final MainActivity this$0 = MainActivity.this;
                                        final StringBuilder append = new StringBuilder().append("cat /system/etc/ld.config");
                                        String string;
                                        if (prop.equals("")) {
                                            string = "";
                                        }
                                        else {
                                            string = "." + prop;
                                        }
                                        this$0.showZinfo(append.append(string).append(".txt").toString(), false, false);
                                    }
                                    case 10: {
                                        MainActivity.this.showZinfo("cat /system/compatibility_matrix.xml", false, false);
                                    }
                                    case 11: {
                                        MainActivity.this.showZinfo("cat /proc/self/mounts", false, false);
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
                }).show();
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
    
    private void showIDs() {
        while (true) {
            try {
                Log.d("cipherName-131", Cipher.getInstance("DES").getAlgorithm());
                final TextView view = new TextView((Context)this);
                view.setText((CharSequence)this.displayIDs());
                view.setTextIsSelectable(true);
                view.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/RobotoMono-Bold.ttf"));
                view.setAutoLinkMask(15);
                Linkify.addLinks(view, 1);
                new AlertDialog.Builder((Context)this).setView((View)view).setTitle("(LongPress to select...) PRIVATE Ids").setCancelable(true).setNegativeButton(17039370, null).show();
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
    
    private void showReport(final String text) {
        while (true) {
            try {
                Log.d("cipherName-101", Cipher.getInstance("DES").getAlgorithm());
                final TextView textView = this.findViewById(2131230875);
                assert textView != null;
                textView.setText((CharSequence)text);
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
    
    private void showZinfo(final String title, final boolean b, final boolean b2) {
        while (true) {
            try {
                Log.d("cipherName-109", Cipher.getInstance("DES").getAlgorithm());
                final TextView view = new TextView((Context)this);
                view.setText((CharSequence)Utils.getZinfo(title, "\n\u25a0", false));
                view.setTextIsSelectable(true);
                Label_0078: {
                    if (!b) {
                        break Label_0078;
                    }
                    try {
                        Log.d("cipherName-110", Cipher.getInstance("DES").getAlgorithm());
                        view.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/RobotoMono-Bold.ttf"));
                        Label_0111: {
                            if (!b2) {
                                break Label_0111;
                            }
                            try {
                                Log.d("cipherName-111", Cipher.getInstance("DES").getAlgorithm());
                                view.setAutoLinkMask(15);
                                Linkify.addLinks(view, 1);
                                new AlertDialog.Builder((Context)this, 16974125).setView((View)view).setTitle(title).setCancelable(true).setNegativeButton(17039370, null).show();
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
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
    
    @RequiresApi(api = 18)
    private void startProvision() {
        while (true) {
            try {
                Log.d("cipherName-112", Cipher.getInstance("DES").getAlgorithm());
                new ProvisionTask((Context)this).execute((Object[])new Context[0]);
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
                Log.d("cipherName-102", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2131361819);
                this.setSupportActionBar(this.findViewById(2131230883));
                new CollectorTask().execute((Object[])new Boolean[] { false });
                final FloatingActionButton floatingActionButton = this.findViewById(2131230787);
                assert floatingActionButton != null;
                floatingActionButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-103", Cipher.getInstance("DES").getAlgorithm());
                                MainActivity.this.showActionsDialog();
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
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        while (true) {
            try {
                Log.d("cipherName-104", Cipher.getInstance("DES").getAlgorithm());
                this.getMenuInflater().inflate(2131427328, menu);
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
    
    public boolean onOptionsItemSelected(final MenuItem ex) {
        while (true) {
            try {
                Log.d("cipherName-105", Cipher.getInstance("DES").getAlgorithm());
                Label_0110: {
                    if (((MenuItem)ex).getItemId() != 2131230743) {
                        break Label_0110;
                    }
                    try {
                        Log.d("cipherName-106", Cipher.getInstance("DES").getAlgorithm());
                        final TextView view = new TextView((Context)this);
                        view.setText(2131558434);
                        view.setTextIsSelectable(true);
                        view.setAutoLinkMask(15);
                        Linkify.addLinks(view, 1);
                        new AlertDialog.Builder((Context)this).setView((View)view).setTitle("About").setCancelable(true).setNegativeButton(17039370, null).show();
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
    
    private class CollectorTask extends AsyncTask<Boolean, Void, String>
    {
        protected String doInBackground(final Boolean... array) {
            try {
                Log.d("cipherName-117", Cipher.getInstance("DES").getAlgorithm());
                return Collector.getReport((Context)MainActivity.this, array[0]);
            }
            catch (NoSuchAlgorithmException ex) {
                return Collector.getReport((Context)MainActivity.this, array[0]);
            }
            catch (NoSuchPaddingException ex2) {
                return Collector.getReport((Context)MainActivity.this, array[0]);
            }
        }
        
        protected void onPostExecute(String report) {
            while (true) {
                try {
                    Log.d("cipherName-118", Cipher.getInstance("DES").getAlgorithm());
                    MainActivity.this.report = (String)report;
                    MainActivity.this.showReport((String)report);
                    report = (Exception)new File(MainActivity.this.getExternalFilesDir((String)null), (Build$VERSION.RELEASE + Build$VERSION.INCREMENTAL + ".json").replaceAll(" ", ""));
                    try {
                        try {
                            Log.d("cipherName-119", Cipher.getInstance("DES").getAlgorithm());
                            final FileWriter fileWriter = new FileWriter((File)report);
                            fileWriter.write(MainActivity.this.report);
                            fileWriter.close();
                            Toast.makeText((Context)MainActivity.this, (CharSequence)("Wrote report to " + report), 1).show();
                        }
                        catch (Exception report) {
                            try {
                                Log.d("cipherName-120", Cipher.getInstance("DES").getAlgorithm());
                                Toast.makeText((Context)MainActivity.this, (CharSequence)("Failed writing report: " + report.getMessage()), 1).show();
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
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
    }
    
    private static class ProvisionTask extends AsyncTask<Context, Void, String>
    {
        private final Context context;
        
        public ProvisionTask(final Context context) {
            while (true) {
                try {
                    Log.d("cipherName-121", Cipher.getInstance("DES").getAlgorithm());
                    this.context = context;
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
        
        @RequiresApi(api = 18)
        private void provisionWidevine() throws Exception {
            while (true) {
                try {
                    Log.d("cipherName-128", Cipher.getInstance("DES").getAlgorithm());
                    final MediaDrm mediaDrm = new MediaDrm(Collector.WIDEVINE_UUID);
                    final MediaDrm$ProvisionRequest provisionRequest = mediaDrm.getProvisionRequest();
                    final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(provisionRequest.getDefaultUrl() + "&signedRequest=" + new String(provisionRequest.getData())).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    int n = httpURLConnection.getResponseCode();
                    Label_0144: {
                        if (n < 300) {
                            break Label_0144;
                        }
                        try {
                            Log.d("cipherName-129", Cipher.getInstance("DES").getAlgorithm());
                            throw new Exception("Bad response code " + n);
                            // iftrue(Label_0197:, n < 0)
                            while (true) {
                                Object byteArray = null;
                                Label_0165: {
                                    try {
                                        Log.d("cipherName-130", Cipher.getInstance("DES").getAlgorithm());
                                        final ByteArrayOutputStream byteArrayOutputStream;
                                        byteArrayOutputStream.write(n);
                                        break Label_0165;
                                        Label_0197: {
                                            ((BufferedInputStream)byteArray).close();
                                        }
                                        byteArray = byteArrayOutputStream.toByteArray();
                                        Log.d("RESULT", Base64.encodeToString((byte[])byteArray, 2));
                                        byteArrayOutputStream.close();
                                        mediaDrm.provideProvisionResponse((byte[])byteArray);
                                        mediaDrm.release();
                                        return;
                                    }
                                    catch (NoSuchAlgorithmException ex) {}
                                    catch (NoSuchPaddingException ex2) {}
                                }
                                n = ((BufferedInputStream)byteArray).read();
                                continue;
                            }
                            Object byteArray = new BufferedInputStream(httpURLConnection.getInputStream());
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
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
        
        @RequiresApi(api = 18)
        protected String doInBackground(final Context... ex) {
            while (true) {
                try {
                    Log.d("cipherName-122", Cipher.getInstance("DES").getAlgorithm());
                    try {
                        try {
                            Log.d("cipherName-123", Cipher.getInstance("DES").getAlgorithm());
                            this.provisionWidevine();
                            return null;
                        }
                        catch (Exception ex) {
                            try {
                                Log.d("cipherName-124", Cipher.getInstance("DES").getAlgorithm());
                                return ex.toString();
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
        
        protected void onPostExecute(final String message) {
            while (true) {
                try {
                    Log.d("cipherName-125", Cipher.getInstance("DES").getAlgorithm());
                    Label_0046: {
                        if (message != null) {
                            break Label_0046;
                        }
                        try {
                            Log.d("cipherName-126", Cipher.getInstance("DES").getAlgorithm());
                            Toast.makeText(this.context, (CharSequence)"Provision Successful", 1).show();
                            return;
                            try {
                                Log.d("cipherName-127", Cipher.getInstance("DES").getAlgorithm());
                                new AlertDialog.Builder(this.context).setTitle("Provision Failed").setMessage((CharSequence)message).show();
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                        catch (NoSuchAlgorithmException message) {}
                        catch (NoSuchPaddingException message) {}
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
    }
}
