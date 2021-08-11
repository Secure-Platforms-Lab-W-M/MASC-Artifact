/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnKeyListener
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.graphics.Color
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.net.VpnService
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.os.StrictMode$ThreadPolicy$Builder
 *  android.text.Editable
 *  android.text.Html
 *  android.text.Spanned
 *  android.text.TextWatcher
 *  android.text.method.KeyListener
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnFocusChangeListener
 *  android.view.View$OnTouchListener
 *  android.widget.Button
 *  android.widget.CheckBox
 *  android.widget.EditText
 *  android.widget.HorizontalScrollView
 *  android.widget.ScrollView
 *  android.widget.TableLayout
 *  android.widget.TextView
 */
package dnsfilter.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import dnsfilter.ConfigurationAccess;
import dnsfilter.DNSFilterManager;
import dnsfilter.android.AndroidEnvironment;
import dnsfilter.android.AppSelectorView;
import dnsfilter.android.DNSFilterService;
import dnsfilter.android.FilterConfig;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import util.GroupedLogger;
import util.Logger;
import util.LoggerInterface;
import util.TimeoutListener;
import util.TimoutNotificator;

public class DNSProxyActivity
extends Activity
implements View.OnClickListener,
LoggerInterface,
TextWatcher,
DialogInterface.OnKeyListener,
ActionMode.Callback,
MenuItem.OnMenuItemClickListener,
View.OnTouchListener,
View.OnFocusChangeListener {
    protected static String ADDITIONAL_HOSTS_TO_LONG;
    protected static boolean BOOT_START;
    protected static ConfigurationAccess CONFIG;
    protected static String IN_FILTER_PREF;
    private static MsgTimeoutListener MsgTO;
    protected static int NO_ACTION_MENU;
    protected static String NO_FILTER_PREF;
    protected static String SCROLL_CONTINUE;
    protected static String SCROLL_PAUSE;
    protected static File WORKPATH;
    protected static String acceptLogFormat;
    protected static TextView addFilterBtn;
    protected static MenuItem add_filter;
    protected static boolean additionalHostsChanged;
    protected static EditText additionalHostsField;
    protected static Dialog advDNSConfigDia;
    protected static boolean advDNSConfigDia_open;
    protected static CheckBox advancedConfigCheck;
    protected static AppSelectorView appSelector;
    protected static boolean appStart;
    protected static CheckBox appWhiteListCheck;
    protected static ScrollView appWhiteListScroll;
    protected static boolean appWhitelistingEnabled;
    protected static String[] availableBackups;
    protected static Button backupBtn;
    protected static Button backupDnBtn;
    protected static CheckBox backupRestoreCheck;
    protected static Button backupUpBtn;
    protected static Properties config;
    protected static boolean debug;
    protected static TextView dnsField;
    protected static TextView donate_field;
    protected static int donate_field_color;
    protected static Spanned donate_field_txt;
    protected static CheckBox editAdditionalHostsCheck;
    protected static CheckBox editFilterLoadCheck;
    protected static CheckBox enableAdFilterCheck;
    protected static CheckBox enableAutoStartCheck;
    protected static CheckBox enableCloakProtectCheck;
    protected static FilterConfig filterCfg;
    protected static String filterLogFormat;
    protected static EditText filterReloadIntervalView;
    protected static CheckBox keepAwakeCheck;
    protected static EditText logOutView;
    protected static CheckBox manualDNSCheck;
    protected static EditText manualDNSView;
    protected static LoggerInterface myLogger;
    protected static String normalLogFormat;
    protected static CheckBox proxyModeCheck;
    protected static TextView removeFilterBtn;
    protected static MenuItem remove_filter;
    protected static Button restoreBtn;
    protected static Button restoreDefaultsBtn;
    protected static CheckBox rootModeCheck;
    protected static TextView scrollLockField;
    protected static boolean scroll_locked;
    protected static int selectedBackup;
    protected static boolean switchingConfig;
    protected Button helpBtn;
    protected Button reloadFilterBtn;
    protected Button remoteCtrlBtn;
    protected ScrollView scrollView = null;
    protected Button startBtn;
    protected Button stopBtn;

    static {
        BOOT_START = false;
        advDNSConfigDia_open = false;
        SCROLL_PAUSE = "II  ";
        SCROLL_CONTINUE = ">>  ";
        scroll_locked = false;
        donate_field_color = 0;
        donate_field_txt = DNSProxyActivity.fromHtml("<strong>Want to support us? Feel free to <a href='https://www.paypal.me/iZenz'>DONATE</a></strong>!");
        additionalHostsChanged = false;
        appStart = true;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append("/PersonalDNSFilter");
        WORKPATH = new File(stringBuilder.toString());
        ADDITIONAL_HOSTS_TO_LONG = "additionalHosts.txt too long to edit here!\nSize Limit: 512 KB!\nUse other editor!";
        config = null;
        debug = false;
        NO_ACTION_MENU = 0;
        IN_FILTER_PREF = "X \t";
        NO_FILTER_PREF = "\u2713\t";
        normalLogFormat = "($CONTENT)";
        CONFIG = ConfigurationAccess.getLocal();
        switchingConfig = false;
        MsgTO = new MsgTimeoutListener();
    }

    private void addToLogView(String string2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string2, "\n");
        while (stringTokenizer.hasMoreElements()) {
            CharSequence charSequence2;
            CharSequence charSequence2;
            String string3 = stringTokenizer.nextToken();
            boolean bl = string3.startsWith(IN_FILTER_PREF);
            boolean bl2 = string3.startsWith(NO_FILTER_PREF);
            if (!bl && !bl2) {
                String string4;
                charSequence2 = string4 = "\n";
                if (!stringTokenizer.hasMoreElements()) {
                    charSequence2 = string4;
                    if (!string2.endsWith("\n")) {
                        charSequence2 = "";
                    }
                }
                logOutView.append((CharSequence)DNSProxyActivity.fromHtml(normalLogFormat.replace("($CONTENT)", string3)));
                logOutView.append(charSequence2);
                continue;
            }
            if (bl) {
                charSequence2 = new StringBuilder();
                charSequence2.append(filterLogFormat.replace("($CONTENT)", string3));
                charSequence2.append("<br>");
                charSequence2 = charSequence2.toString();
            } else {
                charSequence2 = new StringBuilder();
                charSequence2.append(acceptLogFormat.replace("($CONTENT)", string3));
                charSequence2.append("<br>");
                charSequence2 = charSequence2.toString();
            }
            logOutView.append((CharSequence)DNSProxyActivity.fromHtml(charSequence2));
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private FilterConfig.FilterConfigEntry[] buildFilterEntries(Properties object) {
        String string2 = object.getProperty("filterAutoUpdateURL", "");
        String string3 = object.getProperty("filterAutoUpdateURL_IDs", "");
        Object object2 = object.getProperty("filterAutoUpdateURL_switchs", "");
        object = object.getProperty("filterAutoUpdateURL_categories", "");
        StringTokenizer stringTokenizer = new StringTokenizer(string2, ";");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string3, ";");
        StringTokenizer stringTokenizer3 = new StringTokenizer((String)object2, ";");
        StringTokenizer stringTokenizer4 = new StringTokenizer((String)object, ";");
        int n = stringTokenizer.countTokens();
        FilterConfig.FilterConfigEntry[] arrfilterConfigEntry = new FilterConfig.FilterConfigEntry[n];
        int i = 0;
        while (i < n) {
            String string4;
            block13 : {
                Object var8_16 = null;
                string4 = stringTokenizer.nextToken().trim();
                if (stringTokenizer2.hasMoreTokens()) {
                    object2 = stringTokenizer2.nextToken().trim();
                    object = var8_16;
                } else {
                    block14 : {
                        object = new URL(string4);
                        try {
                            object2 = object = object.getHost();
                            break block13;
                        }
                        catch (MalformedURLException malformedURLException) {
                            break block14;
                        }
                        catch (MalformedURLException malformedURLException) {
                            // empty catch block
                        }
                    }
                    Logger.getLogger().logException((Exception)object);
                    object2 = "-";
                    object = var8_16;
                }
            }
            if (stringTokenizer4.hasMoreTokens()) {
                object = stringTokenizer4.nextToken().trim();
            } else if (object == null) {
                try {
                    object = new URL(string4).getHost();
                }
                catch (MalformedURLException malformedURLException) {
                    Logger.getLogger().logException(malformedURLException);
                    object = "-";
                }
            }
            boolean bl = true;
            if (stringTokenizer3.hasMoreTokens()) {
                bl = Boolean.parseBoolean(stringTokenizer3.nextToken().trim());
            }
            arrfilterConfigEntry[i] = new FilterConfig.FilterConfigEntry(bl, (String)object, (String)object2, string4);
            ++i;
        }
        return arrfilterConfigEntry;
    }

    private void doAsyncCheck() {
        new Thread(new Runnable(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                block5 : {
                    // MONITORENTER : this
                    this.wait(1000L);
                    break block5;
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
                Runnable runnable = new Runnable(){

                    @Override
                    public void run() {
                        String string2 = DNSProxyActivity.this.getSelectedText(true);
                        if (DNSProxyActivity.NO_ACTION_MENU >= 0 && !string2.equals("")) {
                            DNSProxyActivity.this.findViewById(2130837527).setVisibility(0);
                        }
                    }
                };
                DNSProxyActivity.this.runOnUiThread(runnable);
                // MONITOREXIT : this
            }

        }).start();
    }

    private void dump(Exception object) {
        StringWriter stringWriter = new StringWriter();
        object.printStackTrace(new PrintWriter(stringWriter));
        try {
            object = new StringBuilder();
            object.append(WORKPATH);
            object.append("/dump-");
            object.append(System.currentTimeMillis());
            object.append(".txt");
            object = new FileOutputStream(object.toString());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TIME: ");
            stringBuilder.append(new Date());
            stringBuilder.append("\nVERSION: ");
            stringBuilder.append("1504000");
            stringBuilder.append("\n\n");
            object.write(stringBuilder.toString().getBytes());
            object.write(stringWriter.toString().getBytes());
            object.flush();
            object.close();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private static Spanned fromHtml(String string2) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml((String)string2, (int)0);
        }
        return Html.fromHtml((String)string2);
    }

    private String getBackupSubFolder() {
        Object object = ".";
        String string2 = ((TextView)this.findViewById(2130837504)).getText().toString();
        if (selectedBackup == -1 && string2.equals("<default>")) {
            return ".";
        }
        if (selectedBackup != -1) {
            object = availableBackups[selectedBackup];
        }
        if (!string2.equals(object)) {
            int n;
            int n2 = 0;
            for (n = 0; n < availableBackups.length; ++n) {
                if (!string2.equals(availableBackups[n])) continue;
                selectedBackup = n;
                return string2;
            }
            object = new ArrayList();
            for (n = 0; n < availableBackups.length; ++n) {
                object.add(availableBackups[n]);
            }
            object.add(string2);
            Collections.sort(object);
            availableBackups = object.toArray(new String[object.size()]);
            for (n = n2; n < availableBackups.length; ++n) {
                if (!string2.equals(availableBackups[n])) continue;
                selectedBackup = n;
                return string2;
            }
            Logger.getLogger().logException(new Exception("Something is wrong!"));
            return string2;
        }
        return string2;
    }

    private String getFallbackDNSSettingFromUI() {
        CharSequence charSequence = manualDNSView.getText().toString();
        String string2 = "";
        StringTokenizer stringTokenizer = new StringTokenizer((String)charSequence, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String string3 = stringTokenizer.nextToken().trim();
            charSequence = string2;
            if (!string3.startsWith("#")) {
                charSequence = string2;
                if (!string3.equals("")) {
                    charSequence = new StringBuilder();
                    charSequence.append(string2);
                    charSequence.append(string3);
                    charSequence.append(" ;");
                    charSequence = charSequence.toString();
                }
            }
            string2 = charSequence;
        }
        charSequence = string2;
        if (!string2.equals("")) {
            charSequence = string2.substring(0, string2.length() - 2);
        }
        return charSequence;
    }

    private String getSelectedText(boolean bl) {
        int n;
        int n2 = logOutView.getSelectionEnd();
        String string2 = "";
        if (n2 > n) {
            string2 = logOutView.getText();
            int n3 = n;
            int n4 = n2;
            if (bl) {
                for (n = DNSProxyActivity.logOutView.getSelectionStart(); string2.charAt(n) != '\n' && n > 0; --n) {
                }
                n3 = n;
                n4 = n2;
                if (n != 0) {
                    n3 = n + 1;
                    n4 = n2;
                }
                while (n4 < string2.length() - 1 && string2.charAt(n4) != '\n') {
                    ++n4;
                }
                logOutView.setSelection(n3, n4);
            }
            string2 = string2.subSequence(n3, n4).toString();
        }
        return string2;
    }

    private void handleAdvancedConfig(CheckBox checkBox) {
        ((TextView)this.findViewById(2130837521)).setText((CharSequence)"");
        if (advancedConfigCheck.isChecked()) {
            this.setVisibilityForAdvCfg(8);
            if (Build.VERSION.SDK_INT >= 21 && CONFIG.isLocal()) {
                appWhiteListCheck.setVisibility(0);
                appWhitelistingEnabled = true;
            } else {
                appWhiteListCheck.setVisibility(8);
                appWhiteListCheck.setChecked(false);
                appWhitelistingEnabled = false;
            }
            this.findViewById(2130837515).setVisibility(0);
            keepAwakeCheck.setVisibility(0);
            proxyModeCheck.setVisibility(0);
            rootModeCheck.setVisibility(0);
            enableCloakProtectCheck.setVisibility(0);
            editAdditionalHostsCheck.setVisibility(0);
            editFilterLoadCheck.setVisibility(0);
            backupRestoreCheck.setVisibility(0);
            CheckBox checkBox2 = checkBox;
            if (checkBox == null) {
                if (editAdditionalHostsCheck.isChecked()) {
                    checkBox2 = editAdditionalHostsCheck;
                } else if (editFilterLoadCheck.isChecked()) {
                    checkBox2 = editFilterLoadCheck;
                } else if (backupRestoreCheck.isChecked()) {
                    checkBox2 = backupRestoreCheck;
                } else {
                    checkBox2 = checkBox;
                    if (appWhiteListCheck.isChecked()) {
                        checkBox2 = appWhiteListCheck;
                    }
                }
            }
            if (checkBox2 != advancedConfigCheck && checkBox2 != null) {
                if (checkBox2.isChecked()) {
                    keepAwakeCheck.setVisibility(8);
                    proxyModeCheck.setVisibility(8);
                    rootModeCheck.setVisibility(8);
                    enableCloakProtectCheck.setVisibility(8);
                    if (checkBox2 != editAdditionalHostsCheck) {
                        editAdditionalHostsCheck.setChecked(false);
                        editAdditionalHostsCheck.setVisibility(8);
                    }
                    if (checkBox2 != editFilterLoadCheck) {
                        editFilterLoadCheck.setChecked(false);
                        editFilterLoadCheck.setVisibility(8);
                    }
                    if (checkBox2 != appWhiteListCheck) {
                        appWhiteListCheck.setChecked(false);
                        appWhiteListCheck.setVisibility(8);
                    }
                    if (checkBox2 != backupRestoreCheck) {
                        backupRestoreCheck.setChecked(false);
                        backupRestoreCheck.setVisibility(8);
                    }
                } else {
                    keepAwakeCheck.setVisibility(0);
                    proxyModeCheck.setVisibility(0);
                    rootModeCheck.setVisibility(0);
                    enableCloakProtectCheck.setVisibility(0);
                    editAdditionalHostsCheck.setVisibility(0);
                    editFilterLoadCheck.setVisibility(0);
                    if (appWhitelistingEnabled) {
                        appWhiteListCheck.setVisibility(0);
                    }
                    backupRestoreCheck.setVisibility(0);
                }
            }
            if (backupRestoreCheck.isChecked()) {
                this.findViewById(2130837523).setVisibility(0);
                try {
                    availableBackups = CONFIG.getAvailableBackups();
                    selectedBackup = -1;
                    ((TextView)this.findViewById(2130837504)).setText((CharSequence)"<default>");
                }
                catch (IOException iOException) {
                    Logger.getLogger().logException(iOException);
                    backupRestoreCheck.setChecked(false);
                    this.findViewById(2130837523).setVisibility(8);
                }
            } else {
                this.findViewById(2130837523).setVisibility(8);
            }
            if (appWhiteListCheck.isChecked()) {
                appWhiteListScroll.setVisibility(0);
                appSelector.loadAppList();
            } else {
                appSelector.clear();
                appWhiteListScroll.setVisibility(8);
            }
            if (editFilterLoadCheck.isChecked()) {
                filterCfg.load();
                this.findViewById(2130837543).setVisibility(0);
            } else {
                this.findViewById(2130837543).setVisibility(8);
                filterCfg.clear();
            }
            if (editAdditionalHostsCheck.isChecked()) {
                this.loadAdditionalHosts();
                this.findViewById(2130837513).setVisibility(0);
                return;
            }
            additionalHostsField.setText((CharSequence)"");
            additionalHostsChanged = false;
            this.findViewById(2130837513).setVisibility(8);
            return;
        }
        this.setVisibilityForAdvCfg(0);
        this.findViewById(2130837543).setVisibility(8);
        filterCfg.clear();
        this.findViewById(2130837513).setVisibility(8);
        this.findViewById(2130837515).setVisibility(8);
        appWhiteListCheck.setChecked(false);
        appSelector.clear();
        this.findViewById(2130837523).setVisibility(8);
        editAdditionalHostsCheck.setChecked(false);
        editFilterLoadCheck.setChecked(false);
        backupRestoreCheck.setChecked(false);
        editAdditionalHostsCheck.setChecked(false);
        additionalHostsField.setText((CharSequence)"");
        additionalHostsChanged = false;
    }

    private void handleBackUpIdChange(boolean bl) {
        if (bl && selectedBackup == availableBackups.length - 1) {
            selectedBackup = -1;
        } else if (!bl && selectedBackup == -1) {
            selectedBackup = availableBackups.length - 1;
        } else if (bl) {
            ++selectedBackup;
        } else if (!bl) {
            --selectedBackup;
        }
        String string2 = "<default>";
        if (selectedBackup != -1) {
            string2 = availableBackups[selectedBackup];
        }
        ((TextView)this.findViewById(2130837504)).setText((CharSequence)string2);
    }

    private void handleDNSConfigDialog() {
        advDNSConfigDia.show();
        ((HorizontalScrollView)advDNSConfigDia.findViewById(2130837554)).fullScroll(17);
        advDNSConfigDia_open = true;
    }

    private void handleDonate() {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"https://www.paypal.me/IZenz")));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void handleRemoteControl() {
        if (switchingConfig) {
            return;
        }
        switchingConfig = true;
        if (CONFIG.isLocal()) {
            boolean bl;
            final String string2 = ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_host", "");
            final String string3 = ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_keyphrase", "");
            if (string2.equals("") || string2.equals("0.0.0.0") || (bl = string3.equals(""))) throw new IOException("Remote Control not configured!");
            try {
                final int n = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_port", "3333"));
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        DNSProxyActivity dNSProxyActivity = DNSProxyActivity.this;
                        Object object = new StringBuilder();
                        object.append("Connecting: ");
                        object.append(string2);
                        object.append(":");
                        object.append(n);
                        dNSProxyActivity.message(object.toString());
                        MsgTO.setTimeout(150000);
                        try {
                            DNSProxyActivity.this.onRemoteConnected(ConfigurationAccess.getRemote(DNSProxyActivity.myLogger, string2, n, string3));
                            return;
                        }
                        catch (IOException iOException) {
                            object = Logger.getLogger();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Remote Connect failed!");
                            stringBuilder.append(iOException.toString());
                            object.logLine(stringBuilder.toString());
                            DNSProxyActivity.this.message("Remote Connect Failed!");
                            DNSProxyActivity.switchingConfig = false;
                            return;
                        }
                    }
                }).start();
                return;
            }
            catch (Exception exception) {
                try {
                    throw new IOException("Invalid connect_remote_ctrl_port");
                }
                catch (IOException iOException) {
                    this.message(iOException.getMessage());
                    CONFIG = ConfigurationAccess.getLocal();
                    switchingConfig = false;
                }
            }
            return;
        }
        CONFIG.releaseConfiguration();
        CONFIG = ConfigurationAccess.getLocal();
        myLogger = this;
        ((GroupedLogger)Logger.getLogger()).attachLogger(this);
        this.loadAndApplyConfig(false);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CONNECTED TO ");
        stringBuilder.append(CONFIG);
        this.message(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("=>CONNECTED to ");
        stringBuilder.append(CONFIG);
        stringBuilder.append("<=");
        this.logLine(stringBuilder.toString());
    }

    private void handleRestart() {
        if (CONFIG.isLocal()) {
            if (!DNSFilterService.stop(false)) {
                return;
            }
            this.startup();
            return;
        }
        try {
            CONFIG.restart();
            this.loadAndApplyConfig(false);
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    private void handleScrollLock() {
        if (scroll_locked) {
            scroll_locked = false;
            scrollLockField.setText((CharSequence)SCROLL_PAUSE);
            logOutView.setSelection(logOutView.getText().length());
            this.scrollView.fullScroll(130);
            return;
        }
        scroll_locked = true;
        scrollLockField.setText((CharSequence)SCROLL_CONTINUE);
    }

    private void initAppAndStartup() {
        this.logLine("Initializing ...");
        if (BOOT_START) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Running on SDK");
            stringBuilder.append(Build.VERSION.SDK_INT);
            loggerInterface.logLine(stringBuilder.toString());
            if (Build.VERSION.SDK_INT >= 20) {
                this.finish();
            }
            BOOT_START = false;
        }
        this.loadAndApplyConfig(true);
        appStart = false;
    }

    private void onRemoteConnected(ConfigurationAccess object) {
        CONFIG = object;
        ((GroupedLogger)Logger.getLogger()).detachLogger(myLogger);
        this.loadAndApplyConfig(false);
        object = new StringBuilder();
        object.append("CONNECTED TO ");
        object.append(CONFIG);
        this.message(object.toString());
        object = new StringBuilder();
        object.append("=>CONNECTED to ");
        object.append(CONFIG);
        object.append("<=");
        this.logLine(object.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void persistConfig() {
        ByteArrayOutputStream byteArrayOutputStream;
        boolean bl;
        String[] arrstring;
        BufferedReader bufferedReader;
        boolean bl2;
        try {
            bl = this.persistAdditionalHosts();
            bl2 = enableAdFilterCheck.isChecked();
            if (filterReloadIntervalView.getText().toString().equals("")) {
                filterReloadIntervalView.setText((CharSequence)"7");
            }
            arrstring = this.getFilterCfgStrings(filterCfg.getFilterEntries());
            byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(CONFIG.readConfig())));
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return;
        }
        do {
            block37 : {
                boolean bl3;
                block36 : {
                    block35 : {
                        CharSequence charSequence;
                        String string2 = charSequence = bufferedReader.readLine();
                        if (charSequence == null) break block35;
                        boolean bl4 = string2.trim().startsWith("detectDNS");
                        bl3 = false;
                        if (bl4) {
                            charSequence = new StringBuilder();
                            charSequence.append("detectDNS = ");
                            charSequence.append(manualDNSCheck.isChecked() ^ true);
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("fallbackDNS")) {
                            charSequence = new StringBuilder();
                            charSequence.append("fallbackDNS = ");
                            charSequence.append(this.getFallbackDNSSettingFromUI());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("filterAutoUpdateURL_IDs")) {
                            charSequence = new StringBuilder();
                            charSequence.append("filterAutoUpdateURL_IDs = ");
                            charSequence.append(arrstring[1]);
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("filterAutoUpdateURL_switchs")) {
                            charSequence = new StringBuilder();
                            charSequence.append("filterAutoUpdateURL_switchs = ");
                            charSequence.append(arrstring[0]);
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("filterAutoUpdateURL_categories")) {
                            charSequence = new StringBuilder();
                            charSequence.append("filterAutoUpdateURL_categories = ");
                            charSequence.append(arrstring[3]);
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("filterAutoUpdateURL")) {
                            charSequence = new StringBuilder();
                            charSequence.append("filterAutoUpdateURL = ");
                            charSequence.append(arrstring[2]);
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("reloadIntervalDays")) {
                            charSequence = new StringBuilder();
                            charSequence.append("reloadIntervalDays = ");
                            charSequence.append((Object)filterReloadIntervalView.getText());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("AUTOSTART")) {
                            charSequence = new StringBuilder();
                            charSequence.append("AUTOSTART = ");
                            charSequence.append(enableAutoStartCheck.isChecked());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("androidAppWhiteList")) {
                            charSequence = new StringBuilder();
                            charSequence.append("androidAppWhiteList = ");
                            charSequence.append(appSelector.getSelectedAppPackages());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("checkCNAME")) {
                            charSequence = new StringBuilder();
                            charSequence.append("checkCNAME = ");
                            charSequence.append(enableCloakProtectCheck.isChecked());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("androidKeepAwake")) {
                            charSequence = new StringBuilder();
                            charSequence.append("androidKeepAwake = ");
                            charSequence.append(keepAwakeCheck.isChecked());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("dnsProxyOnAndroid")) {
                            charSequence = new StringBuilder();
                            charSequence.append("dnsProxyOnAndroid = ");
                            charSequence.append(proxyModeCheck.isChecked());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("rootModeOnAndroid")) {
                            charSequence = new StringBuilder();
                            charSequence.append("rootModeOnAndroid = ");
                            charSequence.append(rootModeCheck.isChecked());
                            charSequence = charSequence.toString();
                        } else if (string2.trim().startsWith("#!!!filterHostsFile") && bl2) {
                            charSequence = string2.replace("#!!!filterHostsFile", "filterHostsFile");
                        } else {
                            charSequence = string2;
                            if (string2.trim().startsWith("filterHostsFile")) {
                                charSequence = string2;
                                if (!bl2) {
                                    charSequence = string2.replace("filterHostsFile", "#!!!filterHostsFile");
                                }
                            }
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append((String)charSequence);
                        stringBuilder.append("\r\n");
                        byteArrayOutputStream.write(stringBuilder.toString().getBytes());
                        if (!bl && string2.equals(charSequence)) break block36;
                        break block37;
                    }
                    bufferedReader.close();
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    if (bl) {
                        this.updateConfig(byteArrayOutputStream.toByteArray());
                    }
                    return;
                }
                bl = bl3;
                continue;
            }
            bl = true;
        } while (true);
    }

    private void setMessage(final Spanned spanned, final int n) {
        this.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                DNSProxyActivity.donate_field.setBackgroundColor(n);
                DNSProxyActivity.donate_field.setText((CharSequence)spanned);
            }
        });
    }

    private void setVisibilityForAdvCfg(int n) {
        enableAdFilterCheck.setVisibility(n);
        enableAutoStartCheck.setVisibility(n);
        this.findViewById(2130837563).setVisibility(n);
        this.reloadFilterBtn.setVisibility(n);
        this.startBtn.setVisibility(n);
        this.stopBtn.setVisibility(n);
    }

    private void startSvc() {
        this.startService(new Intent((Context)this, DNSFilterService.class));
    }

    public void afterTextChanged(Editable editable) {
        additionalHostsChanged = true;
    }

    protected void applyCopiedHosts(String string2, boolean bl) {
        this.findViewById(2130837527).setVisibility(8);
        StringTokenizer stringTokenizer = new StringTokenizer(string2, "\n");
        string2 = "";
        while (stringTokenizer.hasMoreTokens()) {
            CharSequence charSequence2;
            block6 : {
                CharSequence charSequence2;
                String string3;
                block5 : {
                    string3 = stringTokenizer.nextToken();
                    if (string3.startsWith(IN_FILTER_PREF)) break block5;
                    charSequence2 = string2;
                    if (!string3.startsWith(NO_FILTER_PREF)) break block6;
                }
                charSequence2 = new StringBuilder();
                charSequence2.append(string2);
                charSequence2.append(string3.substring(1).trim());
                charSequence2.append("\n");
                charSequence2 = charSequence2.toString();
            }
            string2 = charSequence2;
        }
        try {
            CONFIG.updateFilter(string2.trim(), bl);
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
    }

    @Override
    public void closeLogger() {
    }

    protected void doBackup() {
        TextView textView = (TextView)this.findViewById(2130837521);
        try {
            CONFIG.doBackup(this.getBackupSubFolder());
            textView.setTextColor(Color.parseColor((String)"#23751C"));
            textView.setText((CharSequence)"Backup Success!");
            return;
        }
        catch (IOException iOException) {
            textView.setTextColor(Color.parseColor((String)"#D03D06"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Backup Failed! ");
            stringBuilder.append(iOException.getMessage());
            textView.setText((CharSequence)stringBuilder.toString());
            return;
        }
    }

    protected void doRestore() {
        TextView textView = (TextView)this.findViewById(2130837521);
        try {
            CONFIG.doRestore(this.getBackupSubFolder());
            textView.setTextColor(Color.parseColor((String)"#23751C"));
            this.loadAndApplyConfig(false);
            textView.setText((CharSequence)"Restore Success!");
            return;
        }
        catch (IOException iOException) {
            textView.setTextColor(Color.parseColor((String)"#D03D06"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Restore Failed! ");
            stringBuilder.append(iOException.getMessage());
            textView.setText((CharSequence)stringBuilder.toString());
            return;
        }
    }

    protected void doRestoreDefaults() {
        TextView textView = (TextView)this.findViewById(2130837521);
        try {
            CONFIG.doRestoreDefaults();
            textView.setTextColor(Color.parseColor((String)"#23751C"));
            this.loadAndApplyConfig(false);
            textView.setText((CharSequence)"Restore Success!");
            return;
        }
        catch (IOException iOException) {
            textView.setTextColor(Color.parseColor((String)"#D03D06"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Restore Failed! ");
            stringBuilder.append(iOException.getMessage());
            textView.setText((CharSequence)stringBuilder.toString());
            return;
        }
    }

    protected Properties getConfig() {
        try {
            Properties properties = CONFIG.getConfig();
            return properties;
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return null;
        }
    }

    public String[] getFilterCfgStrings(FilterConfig.FilterConfigEntry[] arrfilterConfigEntry) {
        String[] arrstring = new String[]{"", "", "", ""};
        String string2 = "";
        for (int i = 0; i < arrfilterConfigEntry.length; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(arrstring[0]);
            stringBuilder.append(string2);
            stringBuilder.append(arrfilterConfigEntry[i].active);
            arrstring[0] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(arrstring[1]);
            stringBuilder.append(string2);
            stringBuilder.append(arrfilterConfigEntry[i].id);
            arrstring[1] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(arrstring[2]);
            stringBuilder.append(string2);
            stringBuilder.append(arrfilterConfigEntry[i].url);
            arrstring[2] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(arrstring[3]);
            stringBuilder.append(string2);
            stringBuilder.append(arrfilterConfigEntry[i].category);
            arrstring[3] = stringBuilder.toString();
            string2 = "; ";
        }
        return arrstring;
    }

    protected void handleExitApp() {
        synchronized (this) {
            DNSFilterService.stop(true);
            Intent intent = new Intent((Context)this, DNSProxyActivity.class);
            intent.addFlags(268468224);
            intent.putExtra("SHOULD_FINISH", true);
            this.startActivity(intent);
            return;
        }
    }

    protected void handlefilterReload() {
        try {
            CONFIG.triggerUpdateFilter();
            return;
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void loadAdditionalHosts() {
        try {
            byte[] arrby = CONFIG.getAdditionalHosts(524288);
            if (arrby == null) {
                additionalHostsField.setText((CharSequence)ADDITIONAL_HOSTS_TO_LONG);
                additionalHostsField.setEnabled(false);
                return;
            }
            additionalHostsField.setText((CharSequence)new String(arrby));
            additionalHostsChanged = false;
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not load /PersonalDNSFilter/additionalHosts.txt!\n");
            stringBuilder.append(iOException.toString());
            loggerInterface.logLine(stringBuilder.toString());
            return;
        }
    }

    protected void loadAndApplyConfig(boolean bl) {
        config = this.getConfig();
        if (config != null) {
            this.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Object object;
                    DNSProxyActivity.filterLogFormat = DNSProxyActivity.config.getProperty("filterLogFormat", "<font color='#D03D06'>($CONTENT)</font>");
                    DNSProxyActivity.acceptLogFormat = DNSProxyActivity.config.getProperty("acceptLogFormat", "<font color='#23751C'>($CONTENT)</font>");
                    DNSProxyActivity.normalLogFormat = DNSProxyActivity.config.getProperty("normalLogFormat", "($CONTENT)");
                    try {
                        int n = Integer.parseInt(DNSProxyActivity.config.getProperty("logTextSize", "14"));
                        DNSProxyActivity.logOutView.setTextSize(2, (float)n);
                    }
                    catch (Exception exception) {
                        object = Logger.getLogger();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Error in log Text Size setting! ");
                        stringBuilder.append(exception.toString());
                        object.logLine(stringBuilder.toString());
                    }
                    DNSProxyActivity.debug = Boolean.parseBoolean(DNSProxyActivity.config.getProperty("debug", "false"));
                    CheckBox checkBox = DNSProxyActivity.manualDNSCheck;
                    boolean bl = Boolean.parseBoolean(DNSProxyActivity.config.getProperty("detectDNS", "true"));
                    boolean bl2 = true;
                    checkBox.setChecked(bl ^ true);
                    checkBox = DNSProxyActivity.manualDNSView;
                    object = new StringBuilder();
                    object.append("# Format: <IP>::<PORT>::<PROTOCOL>::<URL END POINT>\n# IPV6 Addresses with '::' must be in brackets '[IPV6]'!\n# Cloudflare examples below:\n# 1.1.1.1::53::UDP (Default DNS on UDP port 53 / just 1.1.1.1 will work as well)\n# 1.1.1.1::853::DOT::cloudflare-dns.com (DNS over TLS, domain name is optional)\n# 1.1.1.1::443::DOH::https://cloudflare-dns.com/dns-query (DNS over HTTPS)\n\n");
                    object.append(DNSProxyActivity.config.getProperty("fallbackDNS").replace(";", "\n").replace(" ", ""));
                    checkBox.setText((CharSequence)object.toString());
                    checkBox = DNSProxyActivity.this.buildFilterEntries(DNSProxyActivity.config);
                    DNSProxyActivity.filterCfg.setEntries((FilterConfig.FilterConfigEntry[])checkBox);
                    DNSProxyActivity.filterReloadIntervalView.setText((CharSequence)DNSProxyActivity.config.getProperty("reloadIntervalDays", "7"));
                    checkBox = DNSProxyActivity.enableAdFilterCheck;
                    if (DNSProxyActivity.config.getProperty("filterHostsFile") == null) {
                        bl2 = false;
                    }
                    checkBox.setChecked(bl2);
                    DNSProxyActivity.enableAutoStartCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("AUTOSTART", "false")));
                    DNSProxyActivity.enableCloakProtectCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("checkCNAME", "true")));
                    DNSProxyActivity.keepAwakeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("androidKeepAwake", "false")));
                    DNSProxyActivity.proxyModeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("dnsProxyOnAndroid", "false")));
                    DNSProxyActivity.rootModeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("rootModeOnAndroid", "false")));
                    DNSProxyActivity.appSelector.setSelectedApps(DNSProxyActivity.config.getProperty("androidAppWhiteList", ""));
                    if (!DNSProxyActivity.CONFIG.isLocal()) {
                        DNSProxyActivity.this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, DNSProxyActivity.this.getResources().getDrawable(2130771970), null);
                    } else {
                        DNSProxyActivity.this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, DNSProxyActivity.this.getResources().getDrawable(2130771979), null);
                    }
                    DNSProxyActivity.switchingConfig = false;
                }
            });
            if (bl) {
                this.startup();
            }
            return;
        }
        switchingConfig = false;
    }

    @Override
    public void log(String string2) {
        this.runOnUiThread((Runnable)new MyUIThreadLogger(string2));
    }

    @Override
    public void logException(Exception serializable) {
        StringWriter stringWriter = new StringWriter();
        serializable.printStackTrace(new PrintWriter(stringWriter));
        serializable = new StringBuilder();
        serializable.append(stringWriter.toString());
        serializable.append("\n");
        this.runOnUiThread((Runnable)new MyUIThreadLogger(serializable.toString()));
    }

    @Override
    public void logLine(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("\n");
        this.runOnUiThread((Runnable)new MyUIThreadLogger(stringBuilder.toString()));
    }

    @Override
    public void message(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<strong>");
        stringBuilder.append(string2);
        stringBuilder.append("</strong>");
        this.setMessage(DNSProxyActivity.fromHtml(stringBuilder.toString()), Color.parseColor((String)"#ffcc00"));
        MsgTO.setTimeout(5000);
    }

    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem == add_filter) {
            this.onCopyFilterFromLogView(true);
            return false;
        }
        if (menuItem == remove_filter) {
            this.onCopyFilterFromLogView(false);
        }
        return false;
    }

    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
    }

    public void onActivityResult(int n, int n2, Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 0 && n2 == -1) {
            this.startSvc();
            return;
        }
        if (n == 0 && n2 != -1) {
            Logger.getLogger().logLine("VPN Dialog not accepted!\r\nPress Restart to display Dialog again!");
        }
    }

    public void onClick(View view) {
        boolean bl = switchingConfig;
        boolean bl2 = false;
        if (bl) {
            advancedConfigCheck.setChecked(false);
            Logger.getLogger().logLine("Config Switch in progress - Wait!");
            return;
        }
        if (view == logOutView) {
            this.findViewById(2130837527).setVisibility(8);
            this.showFilterRate(true);
            return;
        }
        if (view == addFilterBtn) {
            this.onCopyFilterFromLogView(true);
            return;
        }
        if (view == removeFilterBtn) {
            this.onCopyFilterFromLogView(false);
            return;
        }
        if (view == donate_field) {
            this.handleDonate();
            return;
        }
        if (view == this.helpBtn) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"https://www.zenz-home.com/personaldnsfilter/help/help.php")));
            return;
        }
        if (view == dnsField) {
            this.handleDNSConfigDialog();
            return;
        }
        if (view == scrollLockField) {
            this.handleScrollLock();
            return;
        }
        if (view == backupBtn) {
            this.doBackup();
            return;
        }
        if (view == restoreBtn) {
            this.doRestore();
            return;
        }
        if (view == restoreDefaultsBtn) {
            this.doRestoreDefaults();
            return;
        }
        if (view != backupDnBtn && view != backupUpBtn) {
            if (view == rootModeCheck && rootModeCheck.isChecked() && !proxyModeCheck.isChecked()) {
                proxyModeCheck.setChecked(true);
                Logger.getLogger().logLine("Enabled also DNS Proxy Mode as required by Root Mode!");
            }
            if (view == proxyModeCheck && !proxyModeCheck.isChecked() && rootModeCheck.isChecked()) {
                rootModeCheck.setChecked(false);
                Logger.getLogger().logLine("Disabled also Root Mode as it requires DNS Proxy Mode!");
            }
            this.persistConfig();
            if (view == this.remoteCtrlBtn) {
                advancedConfigCheck.setChecked(false);
                this.handleAdvancedConfig(null);
                this.handleRemoteControl();
            }
            if (view == this.startBtn || view == enableAdFilterCheck) {
                this.handleRestart();
            }
            if (view == this.stopBtn) {
                this.handleExitApp();
            }
            if (view == this.reloadFilterBtn) {
                this.handlefilterReload();
            }
            if (view == advancedConfigCheck || view == editAdditionalHostsCheck || view == editFilterLoadCheck || view == appWhiteListCheck || view == backupRestoreCheck) {
                this.handleAdvancedConfig((CheckBox)view);
            }
            if (view == keepAwakeCheck) {
                if (keepAwakeCheck.isChecked()) {
                    this.remoteWakeLock();
                    return;
                }
                this.remoteReleaseWakeLock();
            }
            return;
        }
        if (view == backupUpBtn) {
            bl2 = true;
        }
        this.handleBackUpIdChange(bl2);
    }

    public void onCopyFilterFromLogView(boolean bl) {
        String string2 = this.getSelectedText(false);
        logOutView.clearFocus();
        this.applyCopiedHosts(string2.trim(), bl);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onCreate(Bundle object) {
        try {
            boolean bl;
            Object object2;
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)new StrictMode.ThreadPolicy.Builder().build());
            super.onCreate((Bundle)object);
            AndroidEnvironment.initEnvironment((Context)this);
            MsgTO.setActivity(this);
            if (this.getIntent().getBooleanExtra("SHOULD_FINISH", false)) {
                this.finish();
                System.exit(0);
            }
            object = new StringBuilder();
            object.append(WORKPATH.getAbsolutePath());
            object.append("/");
            DNSFilterManager.WORKDIR = object.toString();
            this.setContentView(2130903044);
            object = null;
            float f = -1.0f;
            if (logOutView != null) {
                object = logOutView.getText();
                f = logOutView.getTextSize();
            }
            logOutView = (EditText)this.findViewById(2130837550);
            if (f != -1.0f) {
                logOutView.setTextSize(0, f);
            }
            if (object != null) {
                logOutView.setText((CharSequence)object);
            } else {
                logOutView.setText((CharSequence)DNSProxyActivity.fromHtml("<strong><em>****This is personalDNSfilter V+1504000****</em></strong><br><br>"));
            }
            logOutView.setKeyListener(null);
            logOutView.setCustomSelectionActionModeCallback((ActionMode.Callback)this);
            logOutView.setOnTouchListener((View.OnTouchListener)this);
            logOutView.setOnFocusChangeListener((View.OnFocusChangeListener)this);
            logOutView.setOnClickListener((View.OnClickListener)this);
            object = "<unknown>";
            String string2 = "-1";
            try {
                object = object2 = CONFIG.getVersion();
                StringBuilder stringBuilder = new StringBuilder();
                object = object2;
                stringBuilder.append(CONFIG.openConnectionsCount());
                object = object2;
                stringBuilder.append("");
                object = object2;
                String string3 = stringBuilder.toString();
                object = string3;
            }
            catch (IOException iOException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(iOException.toString());
                stringBuilder.append("\n");
                this.addToLogView(stringBuilder.toString());
                object2 = object;
                object = string2;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("personalDNSfilter V");
            stringBuilder.append((String)object2);
            stringBuilder.append(" (Connections:");
            stringBuilder.append((String)object);
            stringBuilder.append(")");
            this.setTitle((CharSequence)stringBuilder.toString());
            object = null;
            object2 = null;
            if (filterCfg != null) {
                object = filterCfg.getFilterEntries();
                object2 = filterCfg.getCurrentCategory();
                filterCfg.clear();
            }
            Button button = (Button)this.findViewById(2130837508);
            Button button2 = (Button)this.findViewById(2130837507);
            TextView textView = (TextView)this.findViewById(2130837525);
            filterCfg = new FilterConfig((TableLayout)this.findViewById(2130837542), button, button2, textView);
            if (object != null) {
                filterCfg.setEntries((FilterConfig.FilterConfigEntry[])object);
                filterCfg.setCurrentCategory((String)object2);
            }
            object = "";
            if (filterReloadIntervalView != null) {
                object = filterReloadIntervalView.getText().toString();
            }
            filterReloadIntervalView = (EditText)this.findViewById(2130837544);
            filterReloadIntervalView.setText((CharSequence)object);
            object = "";
            advDNSConfigDia = new Dialog((Context)this, 2131034112);
            advDNSConfigDia.setContentView(2130903041);
            advDNSConfigDia.setTitle((CharSequence)this.getResources().getString(2130968587));
            advDNSConfigDia.setOnKeyListener((DialogInterface.OnKeyListener)this);
            boolean bl2 = manualDNSCheck != null && manualDNSCheck.isChecked();
            manualDNSCheck = (CheckBox)advDNSConfigDia.findViewById(2130837553);
            manualDNSCheck.setChecked(bl2);
            if (manualDNSView != null) {
                object = manualDNSView.getText().toString();
            }
            manualDNSView = (EditText)advDNSConfigDia.findViewById(2130837552);
            manualDNSView.setText((CharSequence)object);
            this.startBtn = (Button)this.findViewById(2130837564);
            this.startBtn.setOnClickListener((View.OnClickListener)this);
            this.stopBtn = (Button)this.findViewById(2130837565);
            this.stopBtn.setOnClickListener((View.OnClickListener)this);
            this.reloadFilterBtn = (Button)this.findViewById(2130837540);
            this.reloadFilterBtn.setOnClickListener((View.OnClickListener)this);
            this.helpBtn = (Button)this.findViewById(2130837545);
            this.helpBtn.setOnClickListener((View.OnClickListener)this);
            this.remoteCtrlBtn = (Button)this.findViewById(2130837559);
            if (!CONFIG.isLocal()) {
                this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(2130771970), null);
            } else {
                this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, this.getResources().getDrawable(2130771979), null);
            }
            this.remoteCtrlBtn.setOnClickListener((View.OnClickListener)this);
            backupBtn = (Button)this.findViewById(2130837520);
            backupBtn.setOnClickListener((View.OnClickListener)this);
            restoreBtn = (Button)this.findViewById(2130837509);
            restoreBtn.setOnClickListener((View.OnClickListener)this);
            restoreDefaultsBtn = (Button)this.findViewById(2130837510);
            restoreDefaultsBtn.setOnClickListener((View.OnClickListener)this);
            backupDnBtn = (Button)this.findViewById(2130837505);
            backupDnBtn.setOnClickListener((View.OnClickListener)this);
            backupUpBtn = (Button)this.findViewById(2130837506);
            backupUpBtn.setOnClickListener((View.OnClickListener)this);
            addFilterBtn = (TextView)this.findViewById(2130837512);
            addFilterBtn.setOnClickListener((View.OnClickListener)this);
            removeFilterBtn = (TextView)this.findViewById(2130837560);
            removeFilterBtn.setOnClickListener((View.OnClickListener)this);
            donate_field = (TextView)this.findViewById(2130837529);
            donate_field.setText((CharSequence)donate_field_txt);
            donate_field.setOnClickListener((View.OnClickListener)this);
            object = donate_field.getBackground();
            if (object instanceof ColorDrawable) {
                donate_field_color = ((ColorDrawable)object).getColor();
            }
            scrollLockField = (TextView)this.findViewById(2130837563);
            if (scroll_locked) {
                scrollLockField.setText((CharSequence)SCROLL_CONTINUE);
            } else {
                scrollLockField.setText((CharSequence)SCROLL_PAUSE);
            }
            scrollLockField.setOnClickListener((View.OnClickListener)this);
            object = "";
            this.scrollView = (ScrollView)this.findViewById(2130837551);
            if (dnsField != null) {
                object = dnsField.getText().toString();
            }
            dnsField = (TextView)this.findViewById(2130837528);
            dnsField.setText((CharSequence)object);
            dnsField.setEnabled(true);
            dnsField.setOnClickListener((View.OnClickListener)this);
            bl2 = enableAdFilterCheck != null && enableAdFilterCheck.isChecked();
            enableAdFilterCheck = (CheckBox)this.findViewById(2130837532);
            enableAdFilterCheck.setChecked(bl2);
            enableAdFilterCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = enableAutoStartCheck != null && enableAutoStartCheck.isChecked();
            enableAutoStartCheck = (CheckBox)this.findViewById(2130837533);
            enableAutoStartCheck.setChecked(bl2);
            enableAutoStartCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = backupRestoreCheck != null && backupRestoreCheck.isChecked();
            backupRestoreCheck = (CheckBox)this.findViewById(2130837522);
            backupRestoreCheck.setChecked(bl2);
            backupRestoreCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = appWhiteListCheck != null && appWhiteListCheck.isChecked();
            appWhiteListCheck = (CheckBox)this.findViewById(2130837519);
            appWhiteListCheck.setChecked(bl2);
            appWhiteListCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = keepAwakeCheck != null && keepAwakeCheck.isChecked();
            keepAwakeCheck = (CheckBox)this.findViewById(2130837546);
            keepAwakeCheck.setChecked(bl2);
            keepAwakeCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = proxyModeCheck != null && proxyModeCheck.isChecked();
            proxyModeCheck = (CheckBox)this.findViewById(2130837556);
            proxyModeCheck.setChecked(bl2);
            proxyModeCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = rootModeCheck != null && rootModeCheck.isChecked();
            rootModeCheck = (CheckBox)this.findViewById(2130837561);
            rootModeCheck.setChecked(bl2);
            rootModeCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = enableCloakProtectCheck != null && enableCloakProtectCheck.isChecked();
            enableCloakProtectCheck = (CheckBox)this.findViewById(2130837526);
            enableCloakProtectCheck.setChecked(bl2);
            enableCloakProtectCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = advancedConfigCheck != null && advancedConfigCheck.isChecked();
            advancedConfigCheck = (CheckBox)this.findViewById(2130837516);
            advancedConfigCheck.setChecked(bl2);
            advancedConfigCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = editFilterLoadCheck != null && editFilterLoadCheck.isChecked();
            editFilterLoadCheck = (CheckBox)this.findViewById(2130837531);
            editFilterLoadCheck.setChecked(bl2);
            editFilterLoadCheck.setOnClickListener((View.OnClickListener)this);
            bl2 = editAdditionalHostsCheck != null && editAdditionalHostsCheck.isChecked();
            editAdditionalHostsCheck = (CheckBox)this.findViewById(2130837530);
            editAdditionalHostsCheck.setChecked(bl2);
            editAdditionalHostsCheck.setOnClickListener((View.OnClickListener)this);
            appWhiteListScroll = (ScrollView)this.findViewById(2130837518);
            object2 = "";
            if (appSelector != null) {
                appSelector.clear();
                object2 = appSelector.getSelectedAppPackages();
            }
            appSelector = (AppSelectorView)this.findViewById(2130837517);
            appSelector.setSelectedApps((String)object2);
            if (additionalHostsField != null) {
                object = additionalHostsField.getText().toString();
            }
            additionalHostsField = (EditText)this.findViewById(2130837514);
            additionalHostsField.setText((CharSequence)object);
            additionalHostsField.addTextChangedListener((TextWatcher)this);
            this.findViewById(2130837527).setVisibility(8);
            this.handleAdvancedConfig(null);
            if (myLogger != null) {
                if (CONFIG.isLocal()) {
                    ((GroupedLogger)Logger.getLogger()).detachLogger(myLogger);
                    ((GroupedLogger)Logger.getLogger()).attachLogger(this);
                    myLogger = this;
                }
            } else {
                Logger.setLogger(new GroupedLogger(new LoggerInterface[]{this}));
                myLogger = this;
            }
            boolean bl3 = bl = true;
            if (Build.VERSION.SDK_INT >= 23) {
                bl3 = bl;
                if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    bl3 = false;
                    this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                    Logger.getLogger().logLine("Need Storage Permissions to start!");
                }
            }
            if (appStart && bl3) {
                this.initAppAndStartup();
            }
            return;
        }
        catch (Exception exception) {
            this.dump(exception);
            throw new RuntimeException(exception);
        }
    }

    public boolean onCreateActionMode(ActionMode object, Menu menu) {
        NO_ACTION_MENU = -1;
        object = this.getSelectedText(true);
        if (Build.VERSION.SDK_INT < 23) {
            this.findViewById(2130837527).setVisibility(0);
        }
        if (object.indexOf(NO_FILTER_PREF) != -1) {
            add_filter = menu.add(2130968581);
        }
        if (object.indexOf(IN_FILTER_PREF) != -1) {
            remove_filter = menu.add(2130968600);
        }
        return true;
    }

    public void onDestroyActionMode(ActionMode actionMode) {
        if (Build.VERSION.SDK_INT < 23) {
            this.findViewById(2130837527).setVisibility(8);
        }
    }

    public void onFocusChange(View view, boolean bl) {
        if (!bl) {
            this.findViewById(2130837527).setVisibility(8);
        }
    }

    public boolean onKey(DialogInterface dialogInterface, int n, KeyEvent keyEvent) {
        if (n == 4 || n == 3) {
            dialogInterface.dismiss();
            advDNSConfigDia_open = false;
            this.persistConfig();
        }
        return false;
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public void onRequestPermissionsResult(int n, String[] arrstring, int[] arrn) {
        super.onRequestPermissionsResult(n, arrstring, arrn);
        if (arrn[0] == 0) {
            this.initAppAndStartup();
            return;
        }
        System.exit(-1);
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        if (advDNSConfigDia_open) {
            advDNSConfigDia.show();
            ((HorizontalScrollView)advDNSConfigDia.findViewById(2130837554)).fullScroll(17);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        if (advDNSConfigDia_open) {
            advDNSConfigDia.dismiss();
        }
    }

    public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
    }

    public boolean onTouch(View object, MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            object = this.getSelectedText(true);
            if (NO_ACTION_MENU >= 0 && !object.equals("")) {
                if (NO_ACTION_MENU <= 1) {
                    this.doAsyncCheck();
                }
                if (++NO_ACTION_MENU > 1) {
                    this.findViewById(2130837527).setVisibility(0);
                }
            }
        }
        return false;
    }

    public void onWindowFocusChanged(boolean bl) {
        super.onWindowFocusChanged(bl);
        if (bl && !scroll_locked) {
            logOutView.setSelection(logOutView.getText().length());
            this.scrollView.fullScroll(130);
        }
    }

    protected boolean persistAdditionalHosts() {
        String string2 = additionalHostsField.getText().toString();
        if (!string2.equals("") && !string2.equals(ADDITIONAL_HOSTS_TO_LONG) && additionalHostsChanged) {
            try {
                CONFIG.updateAdditionalHosts(string2.getBytes());
            }
            catch (IOException iOException) {
                LoggerInterface loggerInterface = Logger.getLogger();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot persistAdditionalHosts!\n");
                stringBuilder.append(iOException.toString());
                loggerInterface.logLine(stringBuilder.toString());
            }
        }
        return additionalHostsChanged;
    }

    public void remoteReleaseWakeLock() {
        try {
            CONFIG.releaseWakeLock();
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("releaseWakeLock failed! ");
            stringBuilder.append(iOException);
            loggerInterface.logLine(stringBuilder.toString());
            return;
        }
    }

    public void remoteWakeLock() {
        try {
            CONFIG.wakeLock();
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("WakeLock failed! ");
            stringBuilder.append(iOException);
            loggerInterface.logLine(stringBuilder.toString());
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void showFilterRate(boolean bl) {
        long[] arrl;
        try {
            arrl = CONFIG.getFilterStatistics();
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return;
        }
        long l = arrl[0] + arrl[1];
        if (l == 0L) return;
        l = 100L * arrl[1] / l;
        if (bl) {
            LoggerInterface loggerInterface = myLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Block rate: ");
            stringBuilder.append(l);
            stringBuilder.append("% (");
            stringBuilder.append(arrl[1]);
            stringBuilder.append(" blocked)!");
            loggerInterface.message(stringBuilder.toString());
            return;
        }
        LoggerInterface loggerInterface = myLogger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Block rate: ");
        stringBuilder.append(l);
        stringBuilder.append("% (");
        stringBuilder.append(arrl[1]);
        stringBuilder.append(" blocked)!");
        loggerInterface.logLine(stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void startup() {
        if (DNSFilterService.SERVICE != null) {
            Logger.getLogger().logLine("DNSFilterService is running!");
            Logger.getLogger().logLine("Filter Statistic since last restart:");
            this.showFilterRate(false);
            return;
        }
        try {
            boolean bl = !Boolean.parseBoolean(this.getConfig().getProperty("vpnInAdditionToProxyMode", "false")) && Boolean.parseBoolean(this.getConfig().getProperty("dnsProxyOnAndroid", "false"));
            Intent intent = null;
            if (!bl && (intent = VpnService.prepare((Context)this.getApplicationContext())) != null) {
                this.startActivityForResult(intent, 0);
                return;
            }
            this.startSvc();
            return;
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return;
        }
        catch (NullPointerException nullPointerException) {
            Logger.getLogger().logLine("Seems we are on Android 4.4 or older!");
            this.startSvc();
        }
    }

    protected void updateConfig(byte[] arrby) throws IOException {
        CONFIG.updateConfig(arrby);
    }

    private static class MsgTimeoutListener
    implements TimeoutListener {
        DNSProxyActivity activity;
        long timeout = Long.MAX_VALUE;

        private MsgTimeoutListener() {
        }

        private void setTimeout(int n) {
            this.timeout = System.currentTimeMillis() + (long)n;
            TimoutNotificator.getInstance().register(this);
        }

        @Override
        public long getTimoutTime() {
            return this.timeout;
        }

        public void setActivity(DNSProxyActivity dNSProxyActivity) {
            this.activity = dNSProxyActivity;
        }

        @Override
        public void timeoutNotification() {
            if (DNSProxyActivity.CONFIG.isLocal()) {
                this.activity.setMessage(DNSProxyActivity.donate_field_txt, DNSProxyActivity.donate_field_color);
                return;
            }
            DNSProxyActivity dNSProxyActivity = this.activity;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<font color='#F7FB0A'><strong>");
            stringBuilder.append(DNSProxyActivity.CONFIG);
            stringBuilder.append("</strong></font>");
            dNSProxyActivity.setMessage(DNSProxyActivity.fromHtml(stringBuilder.toString()), DNSProxyActivity.donate_field_color);
        }
    }

    private class MyUIThreadLogger
    implements Runnable {
        private String m_logStr;

        public MyUIThreadLogger(String string2) {
            this.m_logStr = string2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            synchronized (this) {
                String string2;
                String string3;
                if (!DNSProxyActivity.scroll_locked) {
                    this.m_logStr = this.m_logStr.replace("FILTERED:", DNSProxyActivity.IN_FILTER_PREF);
                    this.m_logStr = this.m_logStr.replace("ALLOWED:", DNSProxyActivity.NO_FILTER_PREF);
                    DNSProxyActivity.this.addToLogView(this.m_logStr);
                    int n = DNSProxyActivity.logOutView.getText().length();
                    if (n >= 10000) {
                        string3 = DNSProxyActivity.logOutView.getText();
                        n /= 2;
                        while (string3.charAt(n) != '\n' && n < string3.length() - 1) {
                            ++n;
                        }
                        DNSProxyActivity.logOutView.setText(string3.subSequence(n, string3.length()));
                    }
                    if (!DNSProxyActivity.advancedConfigCheck.isChecked()) {
                        DNSProxyActivity.logOutView.setSelection(DNSProxyActivity.logOutView.getText().length());
                        DNSProxyActivity.this.scrollView.fullScroll(130);
                    }
                }
                string3 = "<unknown>";
                Object object = "-1";
                String string4 = "<unknown>";
                Object object2 = object;
                try {
                    String string5;
                    string3 = string2 = DNSProxyActivity.CONFIG.getVersion();
                    object2 = object;
                    StringBuilder stringBuilder = new StringBuilder();
                    string3 = string2;
                    object2 = object;
                    stringBuilder.append(DNSProxyActivity.CONFIG.openConnectionsCount());
                    string3 = string2;
                    object2 = object;
                    stringBuilder.append("");
                    string3 = string2;
                    object2 = object;
                    object = stringBuilder.toString();
                    string3 = string2;
                    object2 = object;
                    String string6 = string5 = DNSProxyActivity.CONFIG.getLastDNSAddress();
                    string3 = string2;
                    object2 = object;
                    string2 = string6;
                }
                catch (IOException iOException) {
                    object = DNSProxyActivity.this;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(iOException.toString());
                    stringBuilder.append("\n");
                    ((DNSProxyActivity)object).addToLogView(stringBuilder.toString());
                    string2 = string4;
                }
                object = DNSProxyActivity.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("personalDNSfilter V");
                stringBuilder.append(string3);
                stringBuilder.append(" (Connections:");
                stringBuilder.append((String)object2);
                stringBuilder.append(")");
                object.setTitle((CharSequence)stringBuilder.toString());
                DNSProxyActivity.dnsField.setText((CharSequence)string2);
                return;
            }
        }
    }

}

