/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.Application
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.preference.PreferenceManager
 *  android.provider.Settings
 *  android.provider.Settings$SettingNotFoundException
 *  android.provider.Settings$System
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.Window
 *  android.webkit.WebBackForwardList
 *  android.webkit.WebSettings
 *  android.webkit.WebSettings$RenderPriority
 *  android.webkit.WebView
 *  android.widget.Toast
 */
package com.uberspot.a2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import de.cketti.library.changelog.ChangeLog;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivity
extends Activity {
    private static boolean DEF_FULLSCREEN = false;
    private static final String IS_FULLSCREEN_PREF = "is_fullscreen_pref";
    private static final String MAIN_ACTIVITY_TAG = "2048_MainActivity";
    private static final long mBackPressThreshold = 3500L;
    private static final long mTouchThreshold = 2000L;
    private long mLastBackPress;
    private long mLastTouch;
    private WebView mWebView;
    private Toast pressBackToast;

    static {
        DEF_FULLSCREEN = true;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void applyFullScreen(boolean bl) {
        block11 : {
            try {
                Log.d((String)"cipherName-15", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (bl) {
                Log.d((String)"cipherName-16", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            try {
                Log.d((String)"cipherName-17", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.getWindow().setFlags(1024, 1024);
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.getWindow().clearFlags(1024);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isFullScreen() {
        try {
            Log.d((String)"cipherName-14", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", DEF_FULLSCREEN);
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", DEF_FULLSCREEN);
        }
        do {
            return PreferenceManager.getDefaultSharedPreferences((Context)this).getBoolean("is_fullscreen_pref", DEF_FULLSCREEN);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void saveFullScreen(boolean bl) {
        try {
            Log.d((String)"cipherName-13", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences((Context)this).edit();
        editor.putBoolean("is_fullscreen_pref", bl);
        editor.commit();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void onBackPressed() {
        long l;
        block11 : {
            try {
                Log.d((String)"cipherName-19", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (Math.abs((l = System.currentTimeMillis()) - this.mLastBackPress) > 3500L) {
                Log.d((String)"cipherName-20", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            this.pressBackToast.cancel();
            try {
                Log.d((String)"cipherName-21", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            super.onBackPressed();
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.pressBackToast.show();
        this.mLastBackPress = l;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            Log.d((String)"cipherName-18", (String)Cipher.getInstance("DES").getAlgorithm());
            return;
        }
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
    @SuppressLint(value={"SetJavaScriptEnabled", "NewApi", "ShowToast"})
    protected void onCreate(Bundle bundle) {
        block39 : {
            block37 : {
                int n;
                boolean bl;
                block34 : {
                    block32 : {
                        super.onCreate(bundle);
                        try {
                            Log.d((String)"cipherName-0", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        this.requestWindowFeature(1);
                        if (Build.VERSION.SDK_INT >= 11) {
                            try {
                                Log.d((String)"cipherName-1", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            this.getWindow().setFlags(16777216, 16777216);
                        }
                        this.applyFullScreen(this.isFullScreen());
                        bl = false;
                        try {
                            Log.d((String)"cipherName-2", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block32;
                        }
                        catch (Settings.SettingNotFoundException settingNotFoundException) {
                            try {
                                Log.d((String)"cipherName-3", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            Log.d((String)"2048_MainActivity", (String)"Settings could not be loaded");
                            break block34;
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            break block32;
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                    }
                    bl = (n = Settings.System.getInt((ContentResolver)this.getContentResolver(), (String)"accelerometer_rotation")) == 1;
                }
                if ((n = this.getResources().getConfiguration().screenLayout & 15) == 3 || n == 4 && bl) {
                    try {
                        Log.d((String)"cipherName-4", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    this.setRequestedOrientation(4);
                }
                this.setContentView(2130968576);
                ChangeLog changeLog = new ChangeLog((Context)this);
                if (changeLog.isFirstRun()) {
                    try {
                        Log.d((String)"cipherName-5", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    changeLog.getLogDialog().show();
                }
                this.mWebView = (WebView)this.findViewById(2130903040);
                changeLog = this.mWebView.getSettings();
                changeLog.setJavaScriptEnabled(true);
                changeLog.setDomStorageEnabled(true);
                changeLog.setDatabaseEnabled(true);
                changeLog.setRenderPriority(WebSettings.RenderPriority.HIGH);
                changeLog.setDatabasePath(this.getFilesDir().getParentFile().getPath() + "/databases");
                if (bundle != null) {
                    Log.d((String)"cipherName-6", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block37;
                }
                try {
                    Log.d((String)"cipherName-7", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                this.mWebView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().getLanguage());
                break block39;
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    break block37;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
            }
            this.mWebView.restoreState(bundle);
        }
        Toast.makeText((Context)this.getApplication(), (int)2131099667, (int)0).show();
        this.mWebView.setOnTouchListener(new View.OnTouchListener(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean bl;
                block12 : {
                    long l;
                    bl = true;
                    try {
                        Log.d((String)"cipherName-8", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    l = System.currentTimeMillis();
                    if (motionEvent.getAction() == 1 && Math.abs(l - MainActivity.this.mLastTouch) > 2000L) {
                        Log.d((String)"cipherName-9", (String)Cipher.getInstance("DES").getAlgorithm());
                        break block12;
                    }
                    if (motionEvent.getAction() != 0) return false;
                    try {
                        Log.d((String)"cipherName-10", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    MainActivity.this.mLastTouch = l;
                    return false;
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block12;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                if (MainActivity.this.isFullScreen()) {
                    bl = false;
                }
                MainActivity.this.saveFullScreen(bl);
                MainActivity.this.applyFullScreen(bl);
                return false;
            }
        });
        this.pressBackToast = Toast.makeText((Context)this.getApplicationContext(), (int)2131099666, (int)0);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onCreateOptionsMenu(Menu menu2) {
        try {
            Log.d((String)"cipherName-12", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return true;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return true;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onSaveInstanceState(Bundle bundle) {
        try {
            Log.d((String)"cipherName-11", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.mWebView.saveState(bundle);
    }

}

