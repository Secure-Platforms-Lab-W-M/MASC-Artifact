// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import android.text.Spannable;
import util.TimoutNotificator;
import util.TimeoutListener;
import android.net.VpnService;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.view.Menu;
import android.graphics.drawable.ColorDrawable;
import android.widget.TableLayout;
import android.text.method.KeyListener;
import dnsfilter.DNSFilterManager;
import android.os.StrictMode;
import android.os.StrictMode$ThreadPolicy$Builder;
import android.os.Bundle;
import android.view.View;
import android.view.ActionMode;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.content.Context;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import util.GroupedLogger;
import android.content.Intent;
import android.net.Uri;
import android.widget.HorizontalScrollView;
import java.io.IOException;
import android.text.Editable;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import android.text.Html;
import android.os.Build$VERSION;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import util.Logger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import android.os.Environment;
import android.text.Spanned;
import java.util.Properties;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.CheckBox;
import android.app.Dialog;
import android.widget.EditText;
import android.view.MenuItem;
import android.widget.TextView;
import java.io.File;
import dnsfilter.ConfigurationAccess;
import android.view.View$OnFocusChangeListener;
import android.view.View$OnTouchListener;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.ActionMode$Callback;
import android.content.DialogInterface$OnKeyListener;
import android.text.TextWatcher;
import util.LoggerInterface;
import android.view.View$OnClickListener;
import android.app.Activity;

public class DNSProxyActivity extends Activity implements View$OnClickListener, LoggerInterface, TextWatcher, DialogInterface$OnKeyListener, ActionMode$Callback, MenuItem$OnMenuItemClickListener, View$OnTouchListener, View$OnFocusChangeListener
{
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
    protected ScrollView scrollView;
    protected Button startBtn;
    protected Button stopBtn;
    
    static {
        DNSProxyActivity.BOOT_START = false;
        DNSProxyActivity.advDNSConfigDia_open = false;
        DNSProxyActivity.SCROLL_PAUSE = "II  ";
        DNSProxyActivity.SCROLL_CONTINUE = ">>  ";
        DNSProxyActivity.scroll_locked = false;
        DNSProxyActivity.donate_field_color = 0;
        DNSProxyActivity.donate_field_txt = fromHtml("<strong>Want to support us? Feel free to <a href='https://www.paypal.me/iZenz'>DONATE</a></strong>!");
        DNSProxyActivity.additionalHostsChanged = false;
        DNSProxyActivity.appStart = true;
        final StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/PersonalDNSFilter");
        DNSProxyActivity.WORKPATH = new File(sb.toString());
        DNSProxyActivity.ADDITIONAL_HOSTS_TO_LONG = "additionalHosts.txt too long to edit here!\nSize Limit: 512 KB!\nUse other editor!";
        DNSProxyActivity.config = null;
        DNSProxyActivity.debug = false;
        DNSProxyActivity.NO_ACTION_MENU = 0;
        DNSProxyActivity.IN_FILTER_PREF = "X \t";
        DNSProxyActivity.NO_FILTER_PREF = "\u2713\t";
        DNSProxyActivity.normalLogFormat = "($CONTENT)";
        DNSProxyActivity.CONFIG = ConfigurationAccess.getLocal();
        DNSProxyActivity.switchingConfig = false;
        DNSProxyActivity.MsgTO = new MsgTimeoutListener();
    }
    
    public DNSProxyActivity() {
        this.scrollView = null;
    }
    
    private void addToLogView(final String s) {
        final StringTokenizer stringTokenizer = new StringTokenizer(s, "\n");
        while (stringTokenizer.hasMoreElements()) {
            final String nextToken = stringTokenizer.nextToken();
            final boolean startsWith = nextToken.startsWith(DNSProxyActivity.IN_FILTER_PREF);
            final boolean startsWith2 = nextToken.startsWith(DNSProxyActivity.NO_FILTER_PREF);
            if (!startsWith && !startsWith2) {
                String s2 = "\n";
                if (!stringTokenizer.hasMoreElements()) {
                    s2 = s2;
                    if (!s.endsWith("\n")) {
                        s2 = "";
                    }
                }
                DNSProxyActivity.logOutView.append((CharSequence)fromHtml(DNSProxyActivity.normalLogFormat.replace("($CONTENT)", nextToken)));
                DNSProxyActivity.logOutView.append((CharSequence)s2);
            }
            else {
                String s3;
                if (startsWith) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(DNSProxyActivity.filterLogFormat.replace("($CONTENT)", nextToken));
                    sb.append("<br>");
                    s3 = sb.toString();
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(DNSProxyActivity.acceptLogFormat.replace("($CONTENT)", nextToken));
                    sb2.append("<br>");
                    s3 = sb2.toString();
                }
                DNSProxyActivity.logOutView.append((CharSequence)fromHtml(s3));
            }
        }
    }
    
    private FilterConfig.FilterConfigEntry[] buildFilterEntries(Properties ex) {
        final String property = ((Properties)ex).getProperty("filterAutoUpdateURL", "");
        final String property2 = ((Properties)ex).getProperty("filterAutoUpdateURL_IDs", "");
        final String property3 = ((Properties)ex).getProperty("filterAutoUpdateURL_switchs", "");
        ex = (MalformedURLException)((Properties)ex).getProperty("filterAutoUpdateURL_categories", "");
        final StringTokenizer stringTokenizer = new StringTokenizer(property, ";");
        final StringTokenizer stringTokenizer2 = new StringTokenizer(property2, ";");
        final StringTokenizer stringTokenizer3 = new StringTokenizer(property3, ";");
        final StringTokenizer stringTokenizer4 = new StringTokenizer((String)ex, ";");
        final int countTokens = stringTokenizer.countTokens();
        final FilterConfig.FilterConfigEntry[] array = new FilterConfig.FilterConfigEntry[countTokens];
        for (int i = 0; i < countTokens; ++i) {
            final MalformedURLException ex2 = null;
            final String trim = stringTokenizer.nextToken().trim();
            String s;
            if (stringTokenizer2.hasMoreTokens()) {
                s = stringTokenizer2.nextToken().trim();
                ex = ex2;
            }
            else {
                try {
                    ex = (MalformedURLException)new URL(trim);
                    try {
                        ex = (MalformedURLException)(s = ((URL)ex).getHost());
                    }
                    catch (MalformedURLException ex) {}
                }
                catch (MalformedURLException ex4) {}
                Logger.getLogger().logException(ex);
                s = "-";
                ex = ex2;
            }
            if (stringTokenizer4.hasMoreTokens()) {
                ex = (MalformedURLException)stringTokenizer4.nextToken().trim();
            }
            else if (ex == null) {
                try {
                    ex = (MalformedURLException)new URL(trim).getHost();
                }
                catch (MalformedURLException ex3) {
                    Logger.getLogger().logException(ex3);
                    ex = (MalformedURLException)"-";
                }
            }
            boolean boolean1 = true;
            if (stringTokenizer3.hasMoreTokens()) {
                boolean1 = Boolean.parseBoolean(stringTokenizer3.nextToken().trim());
            }
            array[i] = new FilterConfig.FilterConfigEntry(boolean1, (String)ex, s, trim);
        }
        return array;
    }
    
    private void doAsyncCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // monitorenter(this)
                try {
                    try {
                        this.wait(1000L);
                    }
                    finally {}
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                DNSProxyActivity.this.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final String access$800 = DNSProxyActivity.this.getSelectedText(true);
                        if (DNSProxyActivity.NO_ACTION_MENU >= 0 && !access$800.equals("")) {
                            DNSProxyActivity.this.findViewById(2130837527).setVisibility(0);
                        }
                    }
                });
                // monitorexit(this)
                return;
            }
            // monitorexit(this)
        }).start();
    }
    
    private void dump(final Exception ex) {
        final StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSProxyActivity.WORKPATH);
            sb.append("/dump-");
            sb.append(System.currentTimeMillis());
            sb.append(".txt");
            final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("TIME: ");
            sb2.append(new Date());
            sb2.append("\nVERSION: ");
            sb2.append("1504000");
            sb2.append("\n\n");
            fileOutputStream.write(sb2.toString().getBytes());
            fileOutputStream.write(stringWriter.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static Spanned fromHtml(final String s) {
        if (Build$VERSION.SDK_INT >= 24) {
            return Html.fromHtml(s, 0);
        }
        return Html.fromHtml(s);
    }
    
    private String getBackupSubFolder() {
        String s = ".";
        final String string = ((TextView)this.findViewById(2130837504)).getText().toString();
        if (DNSProxyActivity.selectedBackup == -1 && string.equals("<default>")) {
            return ".";
        }
        if (DNSProxyActivity.selectedBackup != -1) {
            s = DNSProxyActivity.availableBackups[DNSProxyActivity.selectedBackup];
        }
        if (!string.equals(s)) {
            final int n = 0;
            for (int i = 0; i < DNSProxyActivity.availableBackups.length; ++i) {
                if (string.equals(DNSProxyActivity.availableBackups[i])) {
                    DNSProxyActivity.selectedBackup = i;
                    return string;
                }
            }
            final ArrayList<Comparable> list = new ArrayList<Comparable>();
            for (int j = 0; j < DNSProxyActivity.availableBackups.length; ++j) {
                list.add(DNSProxyActivity.availableBackups[j]);
            }
            list.add(string);
            Collections.sort(list);
            DNSProxyActivity.availableBackups = list.toArray(new String[list.size()]);
            for (int k = n; k < DNSProxyActivity.availableBackups.length; ++k) {
                if (string.equals(DNSProxyActivity.availableBackups[k])) {
                    DNSProxyActivity.selectedBackup = k;
                    return string;
                }
            }
            Logger.getLogger().logException(new Exception("Something is wrong!"));
            return string;
        }
        return string;
    }
    
    private String getFallbackDNSSettingFromUI() {
        final String string = DNSProxyActivity.manualDNSView.getText().toString();
        String s = "";
        final StringTokenizer stringTokenizer = new StringTokenizer(string, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            final String trim = stringTokenizer.nextToken().trim();
            String string2 = s;
            if (!trim.startsWith("#")) {
                string2 = s;
                if (!trim.equals("")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(trim);
                    sb.append(" ;");
                    string2 = sb.toString();
                }
            }
            s = string2;
        }
        String substring = s;
        if (!s.equals("")) {
            substring = s.substring(0, s.length() - 2);
        }
        return substring;
    }
    
    private String getSelectedText(final boolean b) {
        int selectionStart = DNSProxyActivity.logOutView.getSelectionStart();
        final int selectionEnd = DNSProxyActivity.logOutView.getSelectionEnd();
        String string = "";
        if (selectionEnd > selectionStart) {
            final Editable text = DNSProxyActivity.logOutView.getText();
            int n = selectionStart;
            int n2 = selectionEnd;
            if (b) {
                while (((Spannable)text).charAt(selectionStart) != '\n' && selectionStart > 0) {
                    --selectionStart;
                }
                n = selectionStart;
                n2 = selectionEnd;
                if (selectionStart != 0) {
                    n = selectionStart + 1;
                    n2 = selectionEnd;
                }
                while (n2 < ((Spannable)text).length() - 1 && ((Spannable)text).charAt(n2) != '\n') {
                    ++n2;
                }
                DNSProxyActivity.logOutView.setSelection(n, n2);
            }
            string = ((Spannable)text).subSequence(n, n2).toString();
        }
        return string;
    }
    
    private void handleAdvancedConfig(final CheckBox checkBox) {
        ((TextView)this.findViewById(2130837521)).setText((CharSequence)"");
        if (!DNSProxyActivity.advancedConfigCheck.isChecked()) {
            this.setVisibilityForAdvCfg(0);
            this.findViewById(2130837543).setVisibility(8);
            DNSProxyActivity.filterCfg.clear();
            this.findViewById(2130837513).setVisibility(8);
            this.findViewById(2130837515).setVisibility(8);
            DNSProxyActivity.appWhiteListCheck.setChecked(false);
            DNSProxyActivity.appSelector.clear();
            this.findViewById(2130837523).setVisibility(8);
            DNSProxyActivity.editAdditionalHostsCheck.setChecked(false);
            DNSProxyActivity.editFilterLoadCheck.setChecked(false);
            DNSProxyActivity.backupRestoreCheck.setChecked(false);
            DNSProxyActivity.editAdditionalHostsCheck.setChecked(false);
            DNSProxyActivity.additionalHostsField.setText((CharSequence)"");
            DNSProxyActivity.additionalHostsChanged = false;
            return;
        }
        this.setVisibilityForAdvCfg(8);
        if (Build$VERSION.SDK_INT >= 21 && DNSProxyActivity.CONFIG.isLocal()) {
            DNSProxyActivity.appWhiteListCheck.setVisibility(0);
            DNSProxyActivity.appWhitelistingEnabled = true;
        }
        else {
            DNSProxyActivity.appWhiteListCheck.setVisibility(8);
            DNSProxyActivity.appWhiteListCheck.setChecked(false);
            DNSProxyActivity.appWhitelistingEnabled = false;
        }
        this.findViewById(2130837515).setVisibility(0);
        DNSProxyActivity.keepAwakeCheck.setVisibility(0);
        DNSProxyActivity.proxyModeCheck.setVisibility(0);
        DNSProxyActivity.rootModeCheck.setVisibility(0);
        DNSProxyActivity.enableCloakProtectCheck.setVisibility(0);
        DNSProxyActivity.editAdditionalHostsCheck.setVisibility(0);
        DNSProxyActivity.editFilterLoadCheck.setVisibility(0);
        DNSProxyActivity.backupRestoreCheck.setVisibility(0);
        CheckBox checkBox2 = checkBox;
        if (checkBox == null) {
            if (DNSProxyActivity.editAdditionalHostsCheck.isChecked()) {
                checkBox2 = DNSProxyActivity.editAdditionalHostsCheck;
            }
            else if (DNSProxyActivity.editFilterLoadCheck.isChecked()) {
                checkBox2 = DNSProxyActivity.editFilterLoadCheck;
            }
            else if (DNSProxyActivity.backupRestoreCheck.isChecked()) {
                checkBox2 = DNSProxyActivity.backupRestoreCheck;
            }
            else {
                checkBox2 = checkBox;
                if (DNSProxyActivity.appWhiteListCheck.isChecked()) {
                    checkBox2 = DNSProxyActivity.appWhiteListCheck;
                }
            }
        }
        if (checkBox2 != DNSProxyActivity.advancedConfigCheck && checkBox2 != null) {
            if (checkBox2.isChecked()) {
                DNSProxyActivity.keepAwakeCheck.setVisibility(8);
                DNSProxyActivity.proxyModeCheck.setVisibility(8);
                DNSProxyActivity.rootModeCheck.setVisibility(8);
                DNSProxyActivity.enableCloakProtectCheck.setVisibility(8);
                if (checkBox2 != DNSProxyActivity.editAdditionalHostsCheck) {
                    DNSProxyActivity.editAdditionalHostsCheck.setChecked(false);
                    DNSProxyActivity.editAdditionalHostsCheck.setVisibility(8);
                }
                if (checkBox2 != DNSProxyActivity.editFilterLoadCheck) {
                    DNSProxyActivity.editFilterLoadCheck.setChecked(false);
                    DNSProxyActivity.editFilterLoadCheck.setVisibility(8);
                }
                if (checkBox2 != DNSProxyActivity.appWhiteListCheck) {
                    DNSProxyActivity.appWhiteListCheck.setChecked(false);
                    DNSProxyActivity.appWhiteListCheck.setVisibility(8);
                }
                if (checkBox2 != DNSProxyActivity.backupRestoreCheck) {
                    DNSProxyActivity.backupRestoreCheck.setChecked(false);
                    DNSProxyActivity.backupRestoreCheck.setVisibility(8);
                }
            }
            else {
                DNSProxyActivity.keepAwakeCheck.setVisibility(0);
                DNSProxyActivity.proxyModeCheck.setVisibility(0);
                DNSProxyActivity.rootModeCheck.setVisibility(0);
                DNSProxyActivity.enableCloakProtectCheck.setVisibility(0);
                DNSProxyActivity.editAdditionalHostsCheck.setVisibility(0);
                DNSProxyActivity.editFilterLoadCheck.setVisibility(0);
                if (DNSProxyActivity.appWhitelistingEnabled) {
                    DNSProxyActivity.appWhiteListCheck.setVisibility(0);
                }
                DNSProxyActivity.backupRestoreCheck.setVisibility(0);
            }
        }
        if (DNSProxyActivity.backupRestoreCheck.isChecked()) {
            this.findViewById(2130837523).setVisibility(0);
            try {
                DNSProxyActivity.availableBackups = DNSProxyActivity.CONFIG.getAvailableBackups();
                DNSProxyActivity.selectedBackup = -1;
                ((TextView)this.findViewById(2130837504)).setText((CharSequence)"<default>");
            }
            catch (IOException ex) {
                Logger.getLogger().logException(ex);
                DNSProxyActivity.backupRestoreCheck.setChecked(false);
                this.findViewById(2130837523).setVisibility(8);
            }
        }
        else {
            this.findViewById(2130837523).setVisibility(8);
        }
        if (DNSProxyActivity.appWhiteListCheck.isChecked()) {
            DNSProxyActivity.appWhiteListScroll.setVisibility(0);
            DNSProxyActivity.appSelector.loadAppList();
        }
        else {
            DNSProxyActivity.appSelector.clear();
            DNSProxyActivity.appWhiteListScroll.setVisibility(8);
        }
        if (DNSProxyActivity.editFilterLoadCheck.isChecked()) {
            DNSProxyActivity.filterCfg.load();
            this.findViewById(2130837543).setVisibility(0);
        }
        else {
            this.findViewById(2130837543).setVisibility(8);
            DNSProxyActivity.filterCfg.clear();
        }
        if (DNSProxyActivity.editAdditionalHostsCheck.isChecked()) {
            this.loadAdditionalHosts();
            this.findViewById(2130837513).setVisibility(0);
            return;
        }
        DNSProxyActivity.additionalHostsField.setText((CharSequence)"");
        DNSProxyActivity.additionalHostsChanged = false;
        this.findViewById(2130837513).setVisibility(8);
    }
    
    private void handleBackUpIdChange(final boolean b) {
        if (b && DNSProxyActivity.selectedBackup == DNSProxyActivity.availableBackups.length - 1) {
            DNSProxyActivity.selectedBackup = -1;
        }
        else if (!b && DNSProxyActivity.selectedBackup == -1) {
            DNSProxyActivity.selectedBackup = DNSProxyActivity.availableBackups.length - 1;
        }
        else if (b) {
            ++DNSProxyActivity.selectedBackup;
        }
        else if (!b) {
            --DNSProxyActivity.selectedBackup;
        }
        String text = "<default>";
        if (DNSProxyActivity.selectedBackup != -1) {
            text = DNSProxyActivity.availableBackups[DNSProxyActivity.selectedBackup];
        }
        ((TextView)this.findViewById(2130837504)).setText((CharSequence)text);
    }
    
    private void handleDNSConfigDialog() {
        DNSProxyActivity.advDNSConfigDia.show();
        ((HorizontalScrollView)DNSProxyActivity.advDNSConfigDia.findViewById(2130837554)).fullScroll(17);
        DNSProxyActivity.advDNSConfigDia_open = true;
    }
    
    private void handleDonate() {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.paypal.me/IZenz")));
    }
    
    private void handleRemoteControl() {
        if (DNSProxyActivity.switchingConfig) {
            return;
        }
        DNSProxyActivity.switchingConfig = true;
        if (DNSProxyActivity.CONFIG.isLocal()) {
            try {
                final String property = ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_host", "");
                final String property2 = ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_keyphrase", "");
                if (!property.equals("") && !property.equals("0.0.0.0")) {
                    if (!property2.equals("")) {
                        try {
                            new Thread(new Runnable() {
                                final /* synthetic */ int val$port = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("client_remote_ctrl_port", "3333"));
                                
                                @Override
                                public void run() {
                                    final DNSProxyActivity this$0 = DNSProxyActivity.this;
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Connecting: ");
                                    sb.append(property);
                                    sb.append(":");
                                    sb.append(this.val$port);
                                    this$0.message(sb.toString());
                                    DNSProxyActivity.MsgTO.setTimeout(150000);
                                    try {
                                        DNSProxyActivity.this.onRemoteConnected(ConfigurationAccess.getRemote(DNSProxyActivity.myLogger, property, this.val$port, property2));
                                    }
                                    catch (IOException ex) {
                                        final LoggerInterface logger = Logger.getLogger();
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Remote Connect failed!");
                                        sb2.append(ex.toString());
                                        logger.logLine(sb2.toString());
                                        DNSProxyActivity.this.message("Remote Connect Failed!");
                                        DNSProxyActivity.switchingConfig = false;
                                    }
                                }
                            }).start();
                            return;
                        }
                        catch (Exception ex2) {
                            throw new IOException("Invalid connect_remote_ctrl_port");
                        }
                    }
                }
                throw new IOException("Remote Control not configured!");
            }
            catch (IOException ex) {
                this.message(ex.getMessage());
                DNSProxyActivity.CONFIG = ConfigurationAccess.getLocal();
                DNSProxyActivity.switchingConfig = false;
            }
            return;
        }
        DNSProxyActivity.CONFIG.releaseConfiguration();
        DNSProxyActivity.CONFIG = ConfigurationAccess.getLocal();
        DNSProxyActivity.myLogger = this;
        ((GroupedLogger)Logger.getLogger()).attachLogger(this);
        this.loadAndApplyConfig(false);
        final StringBuilder sb = new StringBuilder();
        sb.append("CONNECTED TO ");
        sb.append(DNSProxyActivity.CONFIG);
        this.message(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("=>CONNECTED to ");
        sb2.append(DNSProxyActivity.CONFIG);
        sb2.append("<=");
        this.logLine(sb2.toString());
    }
    
    private void handleRestart() {
        if (DNSProxyActivity.CONFIG.isLocal()) {
            if (!DNSFilterService.stop(false)) {
                return;
            }
            this.startup();
        }
        else {
            try {
                DNSProxyActivity.CONFIG.restart();
                this.loadAndApplyConfig(false);
            }
            catch (IOException ex) {
                Logger.getLogger().logException(ex);
            }
        }
    }
    
    private void handleScrollLock() {
        if (DNSProxyActivity.scroll_locked) {
            DNSProxyActivity.scroll_locked = false;
            DNSProxyActivity.scrollLockField.setText((CharSequence)DNSProxyActivity.SCROLL_PAUSE);
            DNSProxyActivity.logOutView.setSelection(DNSProxyActivity.logOutView.getText().length());
            this.scrollView.fullScroll(130);
            return;
        }
        DNSProxyActivity.scroll_locked = true;
        DNSProxyActivity.scrollLockField.setText((CharSequence)DNSProxyActivity.SCROLL_CONTINUE);
    }
    
    private void initAppAndStartup() {
        this.logLine("Initializing ...");
        if (DNSProxyActivity.BOOT_START) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("Running on SDK");
            sb.append(Build$VERSION.SDK_INT);
            logger.logLine(sb.toString());
            if (Build$VERSION.SDK_INT >= 20) {
                this.finish();
            }
            DNSProxyActivity.BOOT_START = false;
        }
        this.loadAndApplyConfig(true);
        DNSProxyActivity.appStart = false;
    }
    
    private void onRemoteConnected(final ConfigurationAccess config) {
        DNSProxyActivity.CONFIG = config;
        ((GroupedLogger)Logger.getLogger()).detachLogger(DNSProxyActivity.myLogger);
        this.loadAndApplyConfig(false);
        final StringBuilder sb = new StringBuilder();
        sb.append("CONNECTED TO ");
        sb.append(DNSProxyActivity.CONFIG);
        this.message(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("=>CONNECTED to ");
        sb2.append(DNSProxyActivity.CONFIG);
        sb2.append("<=");
        this.logLine(sb2.toString());
    }
    
    private void persistConfig() {
        while (true) {
            while (true) {
                Label_0983: {
                    Label_0981: {
                        boolean b;
                        try {
                            final int persistAdditionalHosts = this.persistAdditionalHosts() ? 1 : 0;
                            final boolean checked = DNSProxyActivity.enableAdFilterCheck.isChecked();
                            if (DNSProxyActivity.filterReloadIntervalView.getText().toString().equals("")) {
                                DNSProxyActivity.filterReloadIntervalView.setText((CharSequence)"7");
                            }
                            final String[] filterCfgStrings = this.getFilterCfgStrings(DNSProxyActivity.filterCfg.getFilterEntries());
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(DNSProxyActivity.CONFIG.readConfig())));
                            final String line;
                            if ((line = bufferedReader.readLine()) == null) {
                                bufferedReader.close();
                                byteArrayOutputStream.flush();
                                byteArrayOutputStream.close();
                                if (persistAdditionalHosts != 0) {
                                    this.updateConfig(byteArrayOutputStream.toByteArray());
                                }
                                return;
                            }
                            final boolean startsWith = line.trim().startsWith("detectDNS");
                            b = false;
                            String s;
                            if (startsWith) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("detectDNS = ");
                                sb.append(DNSProxyActivity.manualDNSCheck.isChecked() ^ true);
                                s = sb.toString();
                            }
                            else if (line.trim().startsWith("fallbackDNS")) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("fallbackDNS = ");
                                sb2.append(this.getFallbackDNSSettingFromUI());
                                s = sb2.toString();
                            }
                            else if (line.trim().startsWith("filterAutoUpdateURL_IDs")) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("filterAutoUpdateURL_IDs = ");
                                sb3.append(filterCfgStrings[1]);
                                s = sb3.toString();
                            }
                            else if (line.trim().startsWith("filterAutoUpdateURL_switchs")) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("filterAutoUpdateURL_switchs = ");
                                sb4.append(filterCfgStrings[0]);
                                s = sb4.toString();
                            }
                            else if (line.trim().startsWith("filterAutoUpdateURL_categories")) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("filterAutoUpdateURL_categories = ");
                                sb5.append(filterCfgStrings[3]);
                                s = sb5.toString();
                            }
                            else if (line.trim().startsWith("filterAutoUpdateURL")) {
                                final StringBuilder sb6 = new StringBuilder();
                                sb6.append("filterAutoUpdateURL = ");
                                sb6.append(filterCfgStrings[2]);
                                s = sb6.toString();
                            }
                            else if (line.trim().startsWith("reloadIntervalDays")) {
                                final StringBuilder sb7 = new StringBuilder();
                                sb7.append("reloadIntervalDays = ");
                                sb7.append(DNSProxyActivity.filterReloadIntervalView.getText());
                                s = sb7.toString();
                            }
                            else if (line.trim().startsWith("AUTOSTART")) {
                                final StringBuilder sb8 = new StringBuilder();
                                sb8.append("AUTOSTART = ");
                                sb8.append(DNSProxyActivity.enableAutoStartCheck.isChecked());
                                s = sb8.toString();
                            }
                            else if (line.trim().startsWith("androidAppWhiteList")) {
                                final StringBuilder sb9 = new StringBuilder();
                                sb9.append("androidAppWhiteList = ");
                                sb9.append(DNSProxyActivity.appSelector.getSelectedAppPackages());
                                s = sb9.toString();
                            }
                            else if (line.trim().startsWith("checkCNAME")) {
                                final StringBuilder sb10 = new StringBuilder();
                                sb10.append("checkCNAME = ");
                                sb10.append(DNSProxyActivity.enableCloakProtectCheck.isChecked());
                                s = sb10.toString();
                            }
                            else if (line.trim().startsWith("androidKeepAwake")) {
                                final StringBuilder sb11 = new StringBuilder();
                                sb11.append("androidKeepAwake = ");
                                sb11.append(DNSProxyActivity.keepAwakeCheck.isChecked());
                                s = sb11.toString();
                            }
                            else if (line.trim().startsWith("dnsProxyOnAndroid")) {
                                final StringBuilder sb12 = new StringBuilder();
                                sb12.append("dnsProxyOnAndroid = ");
                                sb12.append(DNSProxyActivity.proxyModeCheck.isChecked());
                                s = sb12.toString();
                            }
                            else if (line.trim().startsWith("rootModeOnAndroid")) {
                                final StringBuilder sb13 = new StringBuilder();
                                sb13.append("rootModeOnAndroid = ");
                                sb13.append(DNSProxyActivity.rootModeCheck.isChecked());
                                s = sb13.toString();
                            }
                            else if (line.trim().startsWith("#!!!filterHostsFile") && checked) {
                                s = line.replace("#!!!filterHostsFile", "filterHostsFile");
                            }
                            else {
                                s = line;
                                if (line.trim().startsWith("filterHostsFile")) {
                                    s = line;
                                    if (!checked) {
                                        s = line.replace("filterHostsFile", "#!!!filterHostsFile");
                                    }
                                }
                            }
                            final StringBuilder sb14 = new StringBuilder();
                            sb14.append(s);
                            sb14.append("\r\n");
                            byteArrayOutputStream.write(sb14.toString().getBytes());
                            if (persistAdditionalHosts != 0 || !line.equals(s)) {
                                break Label_0981;
                            }
                        }
                        catch (Exception ex) {
                            Logger.getLogger().logException(ex);
                            return;
                        }
                        final int persistAdditionalHosts = b ? 1 : 0;
                        break Label_0983;
                    }
                    final int persistAdditionalHosts = 1;
                }
                continue;
            }
        }
    }
    
    private void setMessage(final Spanned spanned, final int n) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                DNSProxyActivity.donate_field.setBackgroundColor(n);
                DNSProxyActivity.donate_field.setText((CharSequence)spanned);
            }
        });
    }
    
    private void setVisibilityForAdvCfg(final int n) {
        DNSProxyActivity.enableAdFilterCheck.setVisibility(n);
        DNSProxyActivity.enableAutoStartCheck.setVisibility(n);
        this.findViewById(2130837563).setVisibility(n);
        this.reloadFilterBtn.setVisibility(n);
        this.startBtn.setVisibility(n);
        this.stopBtn.setVisibility(n);
    }
    
    private void startSvc() {
        this.startService(new Intent((Context)this, (Class)DNSFilterService.class));
    }
    
    public void afterTextChanged(final Editable editable) {
        DNSProxyActivity.additionalHostsChanged = true;
    }
    
    protected void applyCopiedHosts(String s, final boolean b) {
        this.findViewById(2130837527).setVisibility(8);
        final StringTokenizer stringTokenizer = new StringTokenizer(s, "\n");
        s = "";
        while (stringTokenizer.hasMoreTokens()) {
            final String nextToken = stringTokenizer.nextToken();
            String string = null;
            Label_0109: {
                if (!nextToken.startsWith(DNSProxyActivity.IN_FILTER_PREF)) {
                    string = s;
                    if (!nextToken.startsWith(DNSProxyActivity.NO_FILTER_PREF)) {
                        break Label_0109;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(nextToken.substring(1).trim());
                sb.append("\n");
                string = sb.toString();
            }
            s = string;
        }
        try {
            DNSProxyActivity.CONFIG.updateFilter(s.trim(), b);
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void closeLogger() {
    }
    
    protected void doBackup() {
        final TextView textView = (TextView)this.findViewById(2130837521);
        try {
            DNSProxyActivity.CONFIG.doBackup(this.getBackupSubFolder());
            textView.setTextColor(Color.parseColor("#23751C"));
            textView.setText((CharSequence)"Backup Success!");
        }
        catch (IOException ex) {
            textView.setTextColor(Color.parseColor("#D03D06"));
            final StringBuilder sb = new StringBuilder();
            sb.append("Backup Failed! ");
            sb.append(ex.getMessage());
            textView.setText((CharSequence)sb.toString());
        }
    }
    
    protected void doRestore() {
        final TextView textView = (TextView)this.findViewById(2130837521);
        try {
            DNSProxyActivity.CONFIG.doRestore(this.getBackupSubFolder());
            textView.setTextColor(Color.parseColor("#23751C"));
            this.loadAndApplyConfig(false);
            textView.setText((CharSequence)"Restore Success!");
        }
        catch (IOException ex) {
            textView.setTextColor(Color.parseColor("#D03D06"));
            final StringBuilder sb = new StringBuilder();
            sb.append("Restore Failed! ");
            sb.append(ex.getMessage());
            textView.setText((CharSequence)sb.toString());
        }
    }
    
    protected void doRestoreDefaults() {
        final TextView textView = (TextView)this.findViewById(2130837521);
        try {
            DNSProxyActivity.CONFIG.doRestoreDefaults();
            textView.setTextColor(Color.parseColor("#23751C"));
            this.loadAndApplyConfig(false);
            textView.setText((CharSequence)"Restore Success!");
        }
        catch (IOException ex) {
            textView.setTextColor(Color.parseColor("#D03D06"));
            final StringBuilder sb = new StringBuilder();
            sb.append("Restore Failed! ");
            sb.append(ex.getMessage());
            textView.setText((CharSequence)sb.toString());
        }
    }
    
    protected Properties getConfig() {
        try {
            return DNSProxyActivity.CONFIG.getConfig();
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
            return null;
        }
    }
    
    public String[] getFilterCfgStrings(final FilterConfig.FilterConfigEntry[] array) {
        final String[] array2 = { "", "", "", "" };
        String s = "";
        for (int i = 0; i < array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(array2[0]);
            sb.append(s);
            sb.append(array[i].active);
            array2[0] = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(array2[1]);
            sb2.append(s);
            sb2.append(array[i].id);
            array2[1] = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(array2[2]);
            sb3.append(s);
            sb3.append(array[i].url);
            array2[2] = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(array2[3]);
            sb4.append(s);
            sb4.append(array[i].category);
            array2[3] = sb4.toString();
            s = "; ";
        }
        return array2;
    }
    
    protected void handleExitApp() {
        synchronized (this) {
            DNSFilterService.stop(true);
            final Intent intent = new Intent((Context)this, (Class)DNSProxyActivity.class);
            intent.addFlags(268468224);
            intent.putExtra("SHOULD_FINISH", true);
            this.startActivity(intent);
        }
    }
    
    protected void handlefilterReload() {
        try {
            DNSProxyActivity.CONFIG.triggerUpdateFilter();
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    protected void loadAdditionalHosts() {
        try {
            final byte[] additionalHosts = DNSProxyActivity.CONFIG.getAdditionalHosts(524288);
            if (additionalHosts == null) {
                DNSProxyActivity.additionalHostsField.setText((CharSequence)DNSProxyActivity.ADDITIONAL_HOSTS_TO_LONG);
                DNSProxyActivity.additionalHostsField.setEnabled(false);
                return;
            }
            DNSProxyActivity.additionalHostsField.setText((CharSequence)new String(additionalHosts));
            DNSProxyActivity.additionalHostsChanged = false;
        }
        catch (IOException ex) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("Can not load /PersonalDNSFilter/additionalHosts.txt!\n");
            sb.append(ex.toString());
            logger.logLine(sb.toString());
        }
    }
    
    protected void loadAndApplyConfig(final boolean b) {
        DNSProxyActivity.config = this.getConfig();
        if (DNSProxyActivity.config != null) {
            this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    DNSProxyActivity.filterLogFormat = DNSProxyActivity.config.getProperty("filterLogFormat", "<font color='#D03D06'>($CONTENT)</font>");
                    DNSProxyActivity.acceptLogFormat = DNSProxyActivity.config.getProperty("acceptLogFormat", "<font color='#23751C'>($CONTENT)</font>");
                    DNSProxyActivity.normalLogFormat = DNSProxyActivity.config.getProperty("normalLogFormat", "($CONTENT)");
                    try {
                        DNSProxyActivity.logOutView.setTextSize(2, (float)Integer.parseInt(DNSProxyActivity.config.getProperty("logTextSize", "14")));
                    }
                    catch (Exception ex) {
                        final LoggerInterface logger = Logger.getLogger();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Error in log Text Size setting! ");
                        sb.append(ex.toString());
                        logger.logLine(sb.toString());
                    }
                    DNSProxyActivity.debug = Boolean.parseBoolean(DNSProxyActivity.config.getProperty("debug", "false"));
                    final CheckBox manualDNSCheck = DNSProxyActivity.manualDNSCheck;
                    final boolean boolean1 = Boolean.parseBoolean(DNSProxyActivity.config.getProperty("detectDNS", "true"));
                    boolean checked = true;
                    manualDNSCheck.setChecked(boolean1 ^ true);
                    final EditText manualDNSView = DNSProxyActivity.manualDNSView;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("# Format: <IP>::<PORT>::<PROTOCOL>::<URL END POINT>\n# IPV6 Addresses with '::' must be in brackets '[IPV6]'!\n# Cloudflare examples below:\n# 1.1.1.1::53::UDP (Default DNS on UDP port 53 / just 1.1.1.1 will work as well)\n# 1.1.1.1::853::DOT::cloudflare-dns.com (DNS over TLS, domain name is optional)\n# 1.1.1.1::443::DOH::https://cloudflare-dns.com/dns-query (DNS over HTTPS)\n\n");
                    sb2.append(DNSProxyActivity.config.getProperty("fallbackDNS").replace(";", "\n").replace(" ", ""));
                    manualDNSView.setText((CharSequence)sb2.toString());
                    DNSProxyActivity.filterCfg.setEntries(DNSProxyActivity.this.buildFilterEntries(DNSProxyActivity.config));
                    DNSProxyActivity.filterReloadIntervalView.setText((CharSequence)DNSProxyActivity.config.getProperty("reloadIntervalDays", "7"));
                    final CheckBox enableAdFilterCheck = DNSProxyActivity.enableAdFilterCheck;
                    if (DNSProxyActivity.config.getProperty("filterHostsFile") == null) {
                        checked = false;
                    }
                    enableAdFilterCheck.setChecked(checked);
                    DNSProxyActivity.enableAutoStartCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("AUTOSTART", "false")));
                    DNSProxyActivity.enableCloakProtectCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("checkCNAME", "true")));
                    DNSProxyActivity.keepAwakeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("androidKeepAwake", "false")));
                    DNSProxyActivity.proxyModeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("dnsProxyOnAndroid", "false")));
                    DNSProxyActivity.rootModeCheck.setChecked(Boolean.parseBoolean(DNSProxyActivity.config.getProperty("rootModeOnAndroid", "false")));
                    DNSProxyActivity.appSelector.setSelectedApps(DNSProxyActivity.config.getProperty("androidAppWhiteList", ""));
                    if (!DNSProxyActivity.CONFIG.isLocal()) {
                        DNSProxyActivity.this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, DNSProxyActivity.this.getResources().getDrawable(2130771970), (Drawable)null);
                    }
                    else {
                        DNSProxyActivity.this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, DNSProxyActivity.this.getResources().getDrawable(2130771979), (Drawable)null);
                    }
                    DNSProxyActivity.switchingConfig = false;
                }
            });
            if (b) {
                this.startup();
            }
            return;
        }
        DNSProxyActivity.switchingConfig = false;
    }
    
    public void log(final String s) {
        this.runOnUiThread((Runnable)new MyUIThreadLogger(s));
    }
    
    public void logException(final Exception ex) {
        final StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        final StringBuilder sb = new StringBuilder();
        sb.append(stringWriter.toString());
        sb.append("\n");
        this.runOnUiThread((Runnable)new MyUIThreadLogger(sb.toString()));
    }
    
    public void logLine(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("\n");
        this.runOnUiThread((Runnable)new MyUIThreadLogger(sb.toString()));
    }
    
    public void message(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<strong>");
        sb.append(s);
        sb.append("</strong>");
        this.setMessage(fromHtml(sb.toString()), Color.parseColor("#ffcc00"));
        DNSProxyActivity.MsgTO.setTimeout(5000);
    }
    
    public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
        if (menuItem == DNSProxyActivity.add_filter) {
            this.onCopyFilterFromLogView(true);
            return false;
        }
        if (menuItem == DNSProxyActivity.remove_filter) {
            this.onCopyFilterFromLogView(false);
        }
        return false;
    }
    
    public void onActionModeStarted(final ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 0 && n2 == -1) {
            this.startSvc();
            return;
        }
        if (n == 0 && n2 != -1) {
            Logger.getLogger().logLine("VPN Dialog not accepted!\r\nPress Restart to display Dialog again!");
        }
    }
    
    public void onClick(final View view) {
        final boolean switchingConfig = DNSProxyActivity.switchingConfig;
        boolean b = false;
        if (switchingConfig) {
            DNSProxyActivity.advancedConfigCheck.setChecked(false);
            Logger.getLogger().logLine("Config Switch in progress - Wait!");
            return;
        }
        if (view == DNSProxyActivity.logOutView) {
            this.findViewById(2130837527).setVisibility(8);
            this.showFilterRate(true);
            return;
        }
        if (view == DNSProxyActivity.addFilterBtn) {
            this.onCopyFilterFromLogView(true);
            return;
        }
        if (view == DNSProxyActivity.removeFilterBtn) {
            this.onCopyFilterFromLogView(false);
            return;
        }
        if (view == DNSProxyActivity.donate_field) {
            this.handleDonate();
            return;
        }
        if (view == this.helpBtn) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.zenz-home.com/personaldnsfilter/help/help.php")));
            return;
        }
        if (view == DNSProxyActivity.dnsField) {
            this.handleDNSConfigDialog();
            return;
        }
        if (view == DNSProxyActivity.scrollLockField) {
            this.handleScrollLock();
            return;
        }
        if (view == DNSProxyActivity.backupBtn) {
            this.doBackup();
            return;
        }
        if (view == DNSProxyActivity.restoreBtn) {
            this.doRestore();
            return;
        }
        if (view == DNSProxyActivity.restoreDefaultsBtn) {
            this.doRestoreDefaults();
            return;
        }
        if (view != DNSProxyActivity.backupDnBtn && view != DNSProxyActivity.backupUpBtn) {
            if (view == DNSProxyActivity.rootModeCheck && DNSProxyActivity.rootModeCheck.isChecked() && !DNSProxyActivity.proxyModeCheck.isChecked()) {
                DNSProxyActivity.proxyModeCheck.setChecked(true);
                Logger.getLogger().logLine("Enabled also DNS Proxy Mode as required by Root Mode!");
            }
            if (view == DNSProxyActivity.proxyModeCheck && !DNSProxyActivity.proxyModeCheck.isChecked() && DNSProxyActivity.rootModeCheck.isChecked()) {
                DNSProxyActivity.rootModeCheck.setChecked(false);
                Logger.getLogger().logLine("Disabled also Root Mode as it requires DNS Proxy Mode!");
            }
            this.persistConfig();
            if (view == this.remoteCtrlBtn) {
                DNSProxyActivity.advancedConfigCheck.setChecked(false);
                this.handleAdvancedConfig(null);
                this.handleRemoteControl();
            }
            if (view == this.startBtn || view == DNSProxyActivity.enableAdFilterCheck) {
                this.handleRestart();
            }
            if (view == this.stopBtn) {
                this.handleExitApp();
            }
            if (view == this.reloadFilterBtn) {
                this.handlefilterReload();
            }
            if (view == DNSProxyActivity.advancedConfigCheck || view == DNSProxyActivity.editAdditionalHostsCheck || view == DNSProxyActivity.editFilterLoadCheck || view == DNSProxyActivity.appWhiteListCheck || view == DNSProxyActivity.backupRestoreCheck) {
                this.handleAdvancedConfig((CheckBox)view);
            }
            if (view == DNSProxyActivity.keepAwakeCheck) {
                if (DNSProxyActivity.keepAwakeCheck.isChecked()) {
                    this.remoteWakeLock();
                    return;
                }
                this.remoteReleaseWakeLock();
            }
            return;
        }
        if (view == DNSProxyActivity.backupUpBtn) {
            b = true;
        }
        this.handleBackUpIdChange(b);
    }
    
    public void onCopyFilterFromLogView(final boolean b) {
        final String selectedText = this.getSelectedText(false);
        DNSProxyActivity.logOutView.clearFocus();
        this.applyCopiedHosts(selectedText.trim(), b);
    }
    
    public void onCreate(Bundle bundle) {
        StringBuilder sb;
        Object text;
        float textSize;
        Object o;
        Object o2;
        StringBuilder sb2;
        StringBuilder sb3;
        StringBuilder sb4;
        FilterConfig.FilterConfigEntry[] filterEntries;
        String currentCategory;
        String string;
        String string2;
        boolean checked;
        Drawable background;
        String s;
        boolean checked2;
        boolean checked3;
        boolean checked4;
        boolean checked5;
        boolean checked6;
        boolean checked7;
        boolean checked8;
        boolean checked9;
        boolean checked10;
        boolean checked11;
        boolean checked12;
        String selectedAppPackages;
        boolean b;
        Label_1164_Outer:Label_1213_Outer:Label_1262_Outer:Label_1311_Outer:Label_1360_Outer:Label_1409_Outer:Label_1458_Outer:Label_1507_Outer:Label_1556_Outer:Label_1605_Outer:Label_1654_Outer:Label_1867_Outer:
        while (true) {
            while (true) {
            Label_2024:
                while (true) {
                Label_2018:
                    while (true) {
                    Label_2012:
                        while (true) {
                        Label_2006:
                            while (true) {
                            Label_2000:
                                while (true) {
                                Label_1994:
                                    while (true) {
                                    Label_1988:
                                        while (true) {
                                        Label_1982:
                                            while (true) {
                                            Label_1976:
                                                while (true) {
                                                Label_1970:
                                                    while (true) {
                                                    Label_1964:
                                                        while (true) {
                                                        Label_1958:
                                                            while (true) {
                                                                try {
                                                                    StrictMode.setThreadPolicy(new StrictMode$ThreadPolicy$Builder().build());
                                                                    super.onCreate(bundle);
                                                                    AndroidEnvironment.initEnvironment((Context)this);
                                                                    DNSProxyActivity.MsgTO.setActivity(this);
                                                                    if (this.getIntent().getBooleanExtra("SHOULD_FINISH", false)) {
                                                                        this.finish();
                                                                        System.exit(0);
                                                                    }
                                                                    sb = new StringBuilder();
                                                                    sb.append(DNSProxyActivity.WORKPATH.getAbsolutePath());
                                                                    sb.append("/");
                                                                    DNSFilterManager.WORKDIR = sb.toString();
                                                                    this.setContentView(2130903044);
                                                                    text = null;
                                                                    textSize = -1.0f;
                                                                    if (DNSProxyActivity.logOutView != null) {
                                                                        text = DNSProxyActivity.logOutView.getText();
                                                                        textSize = DNSProxyActivity.logOutView.getTextSize();
                                                                    }
                                                                    DNSProxyActivity.logOutView = (EditText)this.findViewById(2130837550);
                                                                    if (textSize != -1.0f) {
                                                                        DNSProxyActivity.logOutView.setTextSize(0, textSize);
                                                                    }
                                                                    if (text != null) {
                                                                        DNSProxyActivity.logOutView.setText((CharSequence)text);
                                                                    }
                                                                    else {
                                                                        DNSProxyActivity.logOutView.setText((CharSequence)fromHtml("<strong><em>****This is personalDNSfilter V+1504000****</em></strong><br><br>"));
                                                                    }
                                                                    DNSProxyActivity.logOutView.setKeyListener((KeyListener)null);
                                                                    DNSProxyActivity.logOutView.setCustomSelectionActionModeCallback((ActionMode$Callback)this);
                                                                    DNSProxyActivity.logOutView.setOnTouchListener((View$OnTouchListener)this);
                                                                    DNSProxyActivity.logOutView.setOnFocusChangeListener((View$OnFocusChangeListener)this);
                                                                    DNSProxyActivity.logOutView.setOnClickListener((View$OnClickListener)this);
                                                                    bundle = (Bundle)"<unknown>";
                                                                    o = "-1";
                                                                    try {
                                                                        o2 = (bundle = (Bundle)DNSProxyActivity.CONFIG.getVersion());
                                                                        sb2 = new StringBuilder();
                                                                        bundle = (Bundle)o2;
                                                                        sb2.append(DNSProxyActivity.CONFIG.openConnectionsCount());
                                                                        bundle = (Bundle)o2;
                                                                        sb2.append("");
                                                                        bundle = (Bundle)o2;
                                                                        bundle = (Bundle)sb2.toString();
                                                                    }
                                                                    catch (IOException ex) {
                                                                        sb3 = new StringBuilder();
                                                                        sb3.append(ex.toString());
                                                                        sb3.append("\n");
                                                                        this.addToLogView(sb3.toString());
                                                                        o2 = bundle;
                                                                        bundle = (Bundle)o;
                                                                    }
                                                                    sb4 = new StringBuilder();
                                                                    sb4.append("personalDNSfilter V");
                                                                    sb4.append((String)o2);
                                                                    sb4.append(" (Connections:");
                                                                    sb4.append((String)bundle);
                                                                    sb4.append(")");
                                                                    this.setTitle((CharSequence)sb4.toString());
                                                                    filterEntries = null;
                                                                    currentCategory = null;
                                                                    if (DNSProxyActivity.filterCfg != null) {
                                                                        filterEntries = DNSProxyActivity.filterCfg.getFilterEntries();
                                                                        currentCategory = DNSProxyActivity.filterCfg.getCurrentCategory();
                                                                        DNSProxyActivity.filterCfg.clear();
                                                                    }
                                                                    DNSProxyActivity.filterCfg = new FilterConfig((TableLayout)this.findViewById(2130837542), (Button)this.findViewById(2130837508), (Button)this.findViewById(2130837507), (TextView)this.findViewById(2130837525));
                                                                    if (filterEntries != null) {
                                                                        DNSProxyActivity.filterCfg.setEntries(filterEntries);
                                                                        DNSProxyActivity.filterCfg.setCurrentCategory(currentCategory);
                                                                    }
                                                                    string = "";
                                                                    if (DNSProxyActivity.filterReloadIntervalView != null) {
                                                                        string = DNSProxyActivity.filterReloadIntervalView.getText().toString();
                                                                    }
                                                                    (DNSProxyActivity.filterReloadIntervalView = (EditText)this.findViewById(2130837544)).setText((CharSequence)string);
                                                                    string2 = "";
                                                                    (DNSProxyActivity.advDNSConfigDia = new Dialog((Context)this, 2131034112)).setContentView(2130903041);
                                                                    DNSProxyActivity.advDNSConfigDia.setTitle((CharSequence)this.getResources().getString(2130968587));
                                                                    DNSProxyActivity.advDNSConfigDia.setOnKeyListener((DialogInterface$OnKeyListener)this);
                                                                    if (DNSProxyActivity.manualDNSCheck != null && DNSProxyActivity.manualDNSCheck.isChecked()) {
                                                                        checked = true;
                                                                        (DNSProxyActivity.manualDNSCheck = (CheckBox)DNSProxyActivity.advDNSConfigDia.findViewById(2130837553)).setChecked(checked);
                                                                        if (DNSProxyActivity.manualDNSView != null) {
                                                                            string2 = DNSProxyActivity.manualDNSView.getText().toString();
                                                                        }
                                                                        (DNSProxyActivity.manualDNSView = (EditText)DNSProxyActivity.advDNSConfigDia.findViewById(2130837552)).setText((CharSequence)string2);
                                                                        (this.startBtn = (Button)this.findViewById(2130837564)).setOnClickListener((View$OnClickListener)this);
                                                                        (this.stopBtn = (Button)this.findViewById(2130837565)).setOnClickListener((View$OnClickListener)this);
                                                                        (this.reloadFilterBtn = (Button)this.findViewById(2130837540)).setOnClickListener((View$OnClickListener)this);
                                                                        (this.helpBtn = (Button)this.findViewById(2130837545)).setOnClickListener((View$OnClickListener)this);
                                                                        this.remoteCtrlBtn = (Button)this.findViewById(2130837559);
                                                                        if (!DNSProxyActivity.CONFIG.isLocal()) {
                                                                            this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(2130771970), (Drawable)null);
                                                                        }
                                                                        else {
                                                                            this.remoteCtrlBtn.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(2130771979), (Drawable)null);
                                                                        }
                                                                        this.remoteCtrlBtn.setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.backupBtn = (Button)this.findViewById(2130837520)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.restoreBtn = (Button)this.findViewById(2130837509)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.restoreDefaultsBtn = (Button)this.findViewById(2130837510)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.backupDnBtn = (Button)this.findViewById(2130837505)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.backupUpBtn = (Button)this.findViewById(2130837506)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.addFilterBtn = (TextView)this.findViewById(2130837512)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.removeFilterBtn = (TextView)this.findViewById(2130837560)).setOnClickListener((View$OnClickListener)this);
                                                                        (DNSProxyActivity.donate_field = (TextView)this.findViewById(2130837529)).setText((CharSequence)DNSProxyActivity.donate_field_txt);
                                                                        DNSProxyActivity.donate_field.setOnClickListener((View$OnClickListener)this);
                                                                        background = DNSProxyActivity.donate_field.getBackground();
                                                                        if (background instanceof ColorDrawable) {
                                                                            DNSProxyActivity.donate_field_color = ((ColorDrawable)background).getColor();
                                                                        }
                                                                        DNSProxyActivity.scrollLockField = (TextView)this.findViewById(2130837563);
                                                                        if (DNSProxyActivity.scroll_locked) {
                                                                            DNSProxyActivity.scrollLockField.setText((CharSequence)DNSProxyActivity.SCROLL_CONTINUE);
                                                                        }
                                                                        else {
                                                                            DNSProxyActivity.scrollLockField.setText((CharSequence)DNSProxyActivity.SCROLL_PAUSE);
                                                                        }
                                                                        DNSProxyActivity.scrollLockField.setOnClickListener((View$OnClickListener)this);
                                                                        s = "";
                                                                        this.scrollView = (ScrollView)this.findViewById(2130837551);
                                                                        if (DNSProxyActivity.dnsField != null) {
                                                                            s = DNSProxyActivity.dnsField.getText().toString();
                                                                        }
                                                                        (DNSProxyActivity.dnsField = (TextView)this.findViewById(2130837528)).setText((CharSequence)s);
                                                                        DNSProxyActivity.dnsField.setEnabled(true);
                                                                        DNSProxyActivity.dnsField.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.enableAdFilterCheck == null || !DNSProxyActivity.enableAdFilterCheck.isChecked()) {
                                                                            break Label_1958;
                                                                        }
                                                                        checked2 = true;
                                                                        (DNSProxyActivity.enableAdFilterCheck = (CheckBox)this.findViewById(2130837532)).setChecked(checked2);
                                                                        DNSProxyActivity.enableAdFilterCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.enableAutoStartCheck == null || !DNSProxyActivity.enableAutoStartCheck.isChecked()) {
                                                                            break Label_1964;
                                                                        }
                                                                        checked3 = true;
                                                                        (DNSProxyActivity.enableAutoStartCheck = (CheckBox)this.findViewById(2130837533)).setChecked(checked3);
                                                                        DNSProxyActivity.enableAutoStartCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.backupRestoreCheck == null || !DNSProxyActivity.backupRestoreCheck.isChecked()) {
                                                                            break Label_1970;
                                                                        }
                                                                        checked4 = true;
                                                                        (DNSProxyActivity.backupRestoreCheck = (CheckBox)this.findViewById(2130837522)).setChecked(checked4);
                                                                        DNSProxyActivity.backupRestoreCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.appWhiteListCheck == null || !DNSProxyActivity.appWhiteListCheck.isChecked()) {
                                                                            break Label_1976;
                                                                        }
                                                                        checked5 = true;
                                                                        (DNSProxyActivity.appWhiteListCheck = (CheckBox)this.findViewById(2130837519)).setChecked(checked5);
                                                                        DNSProxyActivity.appWhiteListCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.keepAwakeCheck == null || !DNSProxyActivity.keepAwakeCheck.isChecked()) {
                                                                            break Label_1982;
                                                                        }
                                                                        checked6 = true;
                                                                        (DNSProxyActivity.keepAwakeCheck = (CheckBox)this.findViewById(2130837546)).setChecked(checked6);
                                                                        DNSProxyActivity.keepAwakeCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.proxyModeCheck == null || !DNSProxyActivity.proxyModeCheck.isChecked()) {
                                                                            break Label_1988;
                                                                        }
                                                                        checked7 = true;
                                                                        (DNSProxyActivity.proxyModeCheck = (CheckBox)this.findViewById(2130837556)).setChecked(checked7);
                                                                        DNSProxyActivity.proxyModeCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.rootModeCheck == null || !DNSProxyActivity.rootModeCheck.isChecked()) {
                                                                            break Label_1994;
                                                                        }
                                                                        checked8 = true;
                                                                        (DNSProxyActivity.rootModeCheck = (CheckBox)this.findViewById(2130837561)).setChecked(checked8);
                                                                        DNSProxyActivity.rootModeCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.enableCloakProtectCheck == null || !DNSProxyActivity.enableCloakProtectCheck.isChecked()) {
                                                                            break Label_2000;
                                                                        }
                                                                        checked9 = true;
                                                                        (DNSProxyActivity.enableCloakProtectCheck = (CheckBox)this.findViewById(2130837526)).setChecked(checked9);
                                                                        DNSProxyActivity.enableCloakProtectCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.advancedConfigCheck == null || !DNSProxyActivity.advancedConfigCheck.isChecked()) {
                                                                            break Label_2006;
                                                                        }
                                                                        checked10 = true;
                                                                        (DNSProxyActivity.advancedConfigCheck = (CheckBox)this.findViewById(2130837516)).setChecked(checked10);
                                                                        DNSProxyActivity.advancedConfigCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.editFilterLoadCheck == null || !DNSProxyActivity.editFilterLoadCheck.isChecked()) {
                                                                            break Label_2012;
                                                                        }
                                                                        checked11 = true;
                                                                        (DNSProxyActivity.editFilterLoadCheck = (CheckBox)this.findViewById(2130837531)).setChecked(checked11);
                                                                        DNSProxyActivity.editFilterLoadCheck.setOnClickListener((View$OnClickListener)this);
                                                                        if (DNSProxyActivity.editAdditionalHostsCheck == null || !DNSProxyActivity.editAdditionalHostsCheck.isChecked()) {
                                                                            break Label_2018;
                                                                        }
                                                                        checked12 = true;
                                                                        (DNSProxyActivity.editAdditionalHostsCheck = (CheckBox)this.findViewById(2130837530)).setChecked(checked12);
                                                                        DNSProxyActivity.editAdditionalHostsCheck.setOnClickListener((View$OnClickListener)this);
                                                                        DNSProxyActivity.appWhiteListScroll = (ScrollView)this.findViewById(2130837518);
                                                                        selectedAppPackages = "";
                                                                        if (DNSProxyActivity.appSelector != null) {
                                                                            DNSProxyActivity.appSelector.clear();
                                                                            selectedAppPackages = DNSProxyActivity.appSelector.getSelectedAppPackages();
                                                                        }
                                                                        (DNSProxyActivity.appSelector = (AppSelectorView)this.findViewById(2130837517)).setSelectedApps(selectedAppPackages);
                                                                        if (DNSProxyActivity.additionalHostsField != null) {
                                                                            s = DNSProxyActivity.additionalHostsField.getText().toString();
                                                                        }
                                                                        (DNSProxyActivity.additionalHostsField = (EditText)this.findViewById(2130837514)).setText((CharSequence)s);
                                                                        DNSProxyActivity.additionalHostsField.addTextChangedListener((TextWatcher)this);
                                                                        this.findViewById(2130837527).setVisibility(8);
                                                                        this.handleAdvancedConfig(null);
                                                                        if (DNSProxyActivity.myLogger == null) {
                                                                            Logger.setLogger(new GroupedLogger(new LoggerInterface[] { this }));
                                                                            DNSProxyActivity.myLogger = this;
                                                                            b = true;
                                                                            if (Build$VERSION.SDK_INT >= 23) {
                                                                                b = b;
                                                                                if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                                                                                    b = false;
                                                                                    this.requestPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 1);
                                                                                    Logger.getLogger().logLine("Need Storage Permissions to start!");
                                                                                }
                                                                            }
                                                                            if (DNSProxyActivity.appStart && b) {
                                                                                this.initAppAndStartup();
                                                                            }
                                                                            return;
                                                                        }
                                                                        if (DNSProxyActivity.CONFIG.isLocal()) {
                                                                            ((GroupedLogger)Logger.getLogger()).detachLogger(DNSProxyActivity.myLogger);
                                                                            ((GroupedLogger)Logger.getLogger()).attachLogger(this);
                                                                            DNSProxyActivity.myLogger = this;
                                                                            break Label_2024;
                                                                        }
                                                                        break Label_2024;
                                                                    }
                                                                }
                                                                catch (Exception ex2) {
                                                                    this.dump(ex2);
                                                                    throw new RuntimeException(ex2);
                                                                }
                                                                checked = false;
                                                                continue Label_1164_Outer;
                                                            }
                                                            checked2 = false;
                                                            continue Label_1213_Outer;
                                                        }
                                                        checked3 = false;
                                                        continue Label_1262_Outer;
                                                    }
                                                    checked4 = false;
                                                    continue Label_1311_Outer;
                                                }
                                                checked5 = false;
                                                continue Label_1360_Outer;
                                            }
                                            checked6 = false;
                                            continue Label_1409_Outer;
                                        }
                                        checked7 = false;
                                        continue Label_1458_Outer;
                                    }
                                    checked8 = false;
                                    continue Label_1507_Outer;
                                }
                                checked9 = false;
                                continue Label_1556_Outer;
                            }
                            checked10 = false;
                            continue Label_1605_Outer;
                        }
                        checked11 = false;
                        continue Label_1654_Outer;
                    }
                    checked12 = false;
                    continue Label_1867_Outer;
                }
                continue;
            }
        }
    }
    
    public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
        DNSProxyActivity.NO_ACTION_MENU = -1;
        final String selectedText = this.getSelectedText(true);
        if (Build$VERSION.SDK_INT < 23) {
            this.findViewById(2130837527).setVisibility(0);
        }
        if (selectedText.indexOf(DNSProxyActivity.NO_FILTER_PREF) != -1) {
            DNSProxyActivity.add_filter = menu.add(2130968581);
        }
        if (selectedText.indexOf(DNSProxyActivity.IN_FILTER_PREF) != -1) {
            DNSProxyActivity.remove_filter = menu.add(2130968600);
        }
        return true;
    }
    
    public void onDestroyActionMode(final ActionMode actionMode) {
        if (Build$VERSION.SDK_INT < 23) {
            this.findViewById(2130837527).setVisibility(8);
        }
    }
    
    public void onFocusChange(final View view, final boolean b) {
        if (!b) {
            this.findViewById(2130837527).setVisibility(8);
        }
    }
    
    public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
        if (n == 4 || n == 3) {
            dialogInterface.dismiss();
            DNSProxyActivity.advDNSConfigDia_open = false;
            this.persistConfig();
        }
        return false;
    }
    
    public boolean onMenuItemClick(final MenuItem menuItem) {
        return false;
    }
    
    public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
        return false;
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        if (array2[0] == 0) {
            this.initAppAndStartup();
            return;
        }
        System.exit(-1);
    }
    
    protected void onRestoreInstanceState(final Bundle bundle) {
        if (DNSProxyActivity.advDNSConfigDia_open) {
            DNSProxyActivity.advDNSConfigDia.show();
            ((HorizontalScrollView)DNSProxyActivity.advDNSConfigDia.findViewById(2130837554)).fullScroll(17);
        }
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        if (DNSProxyActivity.advDNSConfigDia_open) {
            DNSProxyActivity.advDNSConfigDia.dismiss();
        }
    }
    
    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (Build$VERSION.SDK_INT < 23) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            final String selectedText = this.getSelectedText(true);
            if (DNSProxyActivity.NO_ACTION_MENU >= 0 && !selectedText.equals("")) {
                if (DNSProxyActivity.NO_ACTION_MENU <= 1) {
                    ++DNSProxyActivity.NO_ACTION_MENU;
                    this.doAsyncCheck();
                }
                if (DNSProxyActivity.NO_ACTION_MENU > 1) {
                    this.findViewById(2130837527).setVisibility(0);
                }
            }
        }
        return false;
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (b && !DNSProxyActivity.scroll_locked) {
            DNSProxyActivity.logOutView.setSelection(DNSProxyActivity.logOutView.getText().length());
            this.scrollView.fullScroll(130);
        }
    }
    
    protected boolean persistAdditionalHosts() {
        final String string = DNSProxyActivity.additionalHostsField.getText().toString();
        if (!string.equals("") && !string.equals(DNSProxyActivity.ADDITIONAL_HOSTS_TO_LONG) && DNSProxyActivity.additionalHostsChanged) {
            try {
                DNSProxyActivity.CONFIG.updateAdditionalHosts(string.getBytes());
            }
            catch (IOException ex) {
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot persistAdditionalHosts!\n");
                sb.append(ex.toString());
                logger.logLine(sb.toString());
            }
        }
        return DNSProxyActivity.additionalHostsChanged;
    }
    
    public void remoteReleaseWakeLock() {
        try {
            DNSProxyActivity.CONFIG.releaseWakeLock();
        }
        catch (IOException ex) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("releaseWakeLock failed! ");
            sb.append(ex);
            logger.logLine(sb.toString());
        }
    }
    
    public void remoteWakeLock() {
        try {
            DNSProxyActivity.CONFIG.wakeLock();
        }
        catch (IOException ex) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("WakeLock failed! ");
            sb.append(ex);
            logger.logLine(sb.toString());
        }
    }
    
    protected void showFilterRate(final boolean b) {
        try {
            final long[] filterStatistics = DNSProxyActivity.CONFIG.getFilterStatistics();
            final long n = filterStatistics[0] + filterStatistics[1];
            if (n != 0L) {
                final long n2 = 100L * filterStatistics[1] / n;
                if (b) {
                    final LoggerInterface myLogger = DNSProxyActivity.myLogger;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Block rate: ");
                    sb.append(n2);
                    sb.append("% (");
                    sb.append(filterStatistics[1]);
                    sb.append(" blocked)!");
                    myLogger.message(sb.toString());
                }
                else {
                    final LoggerInterface myLogger2 = DNSProxyActivity.myLogger;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Block rate: ");
                    sb2.append(n2);
                    sb2.append("% (");
                    sb2.append(filterStatistics[1]);
                    sb2.append(" blocked)!");
                    myLogger2.logLine(sb2.toString());
                }
            }
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    protected void startup() {
        if (DNSFilterService.SERVICE != null) {
            Logger.getLogger().logLine("DNSFilterService is running!");
            Logger.getLogger().logLine("Filter Statistic since last restart:");
            this.showFilterRate(false);
            return;
        }
        while (true) {
            while (true) {
                Label_0139: {
                    try {
                        if (Boolean.parseBoolean(this.getConfig().getProperty("vpnInAdditionToProxyMode", "false")) || !Boolean.parseBoolean(this.getConfig().getProperty("dnsProxyOnAndroid", "false"))) {
                            break Label_0139;
                        }
                        final int n = 1;
                        Intent prepare = null;
                        if (n == 0) {
                            prepare = VpnService.prepare(this.getApplicationContext());
                        }
                        if (prepare != null) {
                            this.startActivityForResult(prepare, 0);
                        }
                        else {
                            this.startSvc();
                        }
                    }
                    catch (Exception ex) {
                        Logger.getLogger().logException(ex);
                        return;
                    }
                    catch (NullPointerException ex2) {
                        Logger.getLogger().logLine("Seems we are on Android 4.4 or older!");
                        this.startSvc();
                    }
                    break;
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    protected void updateConfig(final byte[] array) throws IOException {
        DNSProxyActivity.CONFIG.updateConfig(array);
    }
    
    private static class MsgTimeoutListener implements TimeoutListener
    {
        DNSProxyActivity activity;
        long timeout;
        
        private MsgTimeoutListener() {
            this.timeout = Long.MAX_VALUE;
        }
        
        private void setTimeout(final int n) {
            this.timeout = System.currentTimeMillis() + n;
            TimoutNotificator.getInstance().register(this);
        }
        
        @Override
        public long getTimoutTime() {
            return this.timeout;
        }
        
        public void setActivity(final DNSProxyActivity activity) {
            this.activity = activity;
        }
        
        @Override
        public void timeoutNotification() {
            if (DNSProxyActivity.CONFIG.isLocal()) {
                this.activity.setMessage(DNSProxyActivity.donate_field_txt, DNSProxyActivity.donate_field_color);
                return;
            }
            final DNSProxyActivity activity = this.activity;
            final StringBuilder sb = new StringBuilder();
            sb.append("<font color='#F7FB0A'><strong>");
            sb.append(DNSProxyActivity.CONFIG);
            sb.append("</strong></font>");
            activity.setMessage(fromHtml(sb.toString()), DNSProxyActivity.donate_field_color);
        }
    }
    
    private class MyUIThreadLogger implements Runnable
    {
        private String m_logStr;
        
        public MyUIThreadLogger(final String logStr) {
            this.m_logStr = logStr;
        }
        
        @Override
        public void run() {
            synchronized (this) {
                if (!DNSProxyActivity.scroll_locked) {
                    this.m_logStr = this.m_logStr.replace("FILTERED:", DNSProxyActivity.IN_FILTER_PREF);
                    this.m_logStr = this.m_logStr.replace("ALLOWED:", DNSProxyActivity.NO_FILTER_PREF);
                    DNSProxyActivity.this.addToLogView(this.m_logStr);
                    final int length = DNSProxyActivity.logOutView.getText().length();
                    if (length >= 10000) {
                        Editable text;
                        int n;
                        for (text = DNSProxyActivity.logOutView.getText(), n = length / 2; ((Spannable)text).charAt(n) != '\n' && n < ((Spannable)text).length() - 1; ++n) {}
                        DNSProxyActivity.logOutView.setText(((Spannable)text).subSequence(n, ((Spannable)text).length()));
                    }
                    if (!DNSProxyActivity.advancedConfigCheck.isChecked()) {
                        DNSProxyActivity.logOutView.setSelection(DNSProxyActivity.logOutView.getText().length());
                        DNSProxyActivity.this.scrollView.fullScroll(130);
                    }
                }
                String version = "<unknown>";
                final String s = "-1";
                String lastDNSAddress = "<unknown>";
                String s2 = s;
                String text2;
                try {
                    final String s3 = version = DNSProxyActivity.CONFIG.getVersion();
                    s2 = s;
                    final StringBuilder sb = new StringBuilder();
                    version = s3;
                    s2 = s;
                    sb.append(DNSProxyActivity.CONFIG.openConnectionsCount());
                    version = s3;
                    s2 = s;
                    sb.append("");
                    version = s3;
                    s2 = s;
                    final String string = sb.toString();
                    version = s3;
                    s2 = string;
                    lastDNSAddress = DNSProxyActivity.CONFIG.getLastDNSAddress();
                    version = s3;
                    s2 = string;
                    text2 = lastDNSAddress;
                }
                catch (IOException ex) {
                    final DNSProxyActivity this$0 = DNSProxyActivity.this;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(ex.toString());
                    sb2.append("\n");
                    this$0.addToLogView(sb2.toString());
                    text2 = lastDNSAddress;
                }
                final DNSProxyActivity this$2 = DNSProxyActivity.this;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("personalDNSfilter V");
                sb3.append(version);
                sb3.append(" (Connections:");
                sb3.append(s2);
                sb3.append(")");
                this$2.setTitle((CharSequence)sb3.toString());
                DNSProxyActivity.dnsField.setText((CharSequence)text2);
            }
        }
    }
}
