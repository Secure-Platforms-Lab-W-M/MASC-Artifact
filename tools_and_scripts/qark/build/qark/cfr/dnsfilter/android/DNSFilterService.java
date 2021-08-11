/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.annotation.TargetApi
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.net.ConnectivityManager
 *  android.net.LinkProperties
 *  android.net.Network
 *  android.net.NetworkInfo
 *  android.net.VpnService
 *  android.net.VpnService$Builder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 */
package dnsfilter.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import dnsfilter.ConfigurationAccess;
import dnsfilter.DNSFilterManager;
import dnsfilter.DNSFilterProxy;
import dnsfilter.android.AndroidEnvironment;
import dnsfilter.android.ConnectionChangeReceiver;
import dnsfilter.android.DNSProxyActivity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;
import util.Utils;

public class DNSFilterService
extends VpnService {
    private static String ADDRESS_IPV4;
    private static String ADDRESS_IPV6;
    public static DNSFilterManager DNSFILTER;
    public static DNSFilterProxy DNSFILTERPROXY;
    private static boolean DNS_PROXY_PORT_IS_REDIRECTED;
    private static DNSFilterService INSTANCE;
    private static boolean JUST_STARTED;
    private static String KILL_DNSCRYPTPROXY;
    protected static Intent SERVICE;
    private static String START_DNSCRYPTPROXY;
    private static String VIRTUALDNS_IPV4;
    private static String VIRTUALDNS_IPV6;
    private static boolean dnsProxyMode;
    protected static DNSReqForwarder dnsReqForwarder;
    private static boolean is_running;
    private static boolean rootMode;
    private static int startCounter;
    private static boolean vpnInAdditionToProxyMode;
    private boolean blocking = false;
    boolean dnsCryptProxyStartTriggered = false;
    boolean manageDNSCryptProxy = false;
    private int mtu;
    PendingIntent pendingIntent;
    private VPNRunner vpnRunner = null;

    static {
        VIRTUALDNS_IPV4 = "10.10.10.10";
        VIRTUALDNS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa1";
        ADDRESS_IPV4 = "10.0.2.15";
        ADDRESS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa2";
        SERVICE = null;
        START_DNSCRYPTPROXY = "dnscrypt-proxy";
        KILL_DNSCRYPTPROXY = "killall dnscrypt-proxy";
        DNSFILTER = null;
        DNSFILTERPROXY = null;
        INSTANCE = null;
        JUST_STARTED = false;
        DNS_PROXY_PORT_IS_REDIRECTED = false;
        dnsProxyMode = false;
        rootMode = false;
        vpnInAdditionToProxyMode = false;
        is_running = false;
        dnsReqForwarder = new DNSReqForwarder();
        startCounter = 0;
    }

    static /* synthetic */ boolean access$100(DNSFilterService dNSFilterService) {
        return dNSFilterService.blocking;
    }

    /*
     * Exception decompiling
     */
    public static void detectDNSServers() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 8[CATCHBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    @SuppressLint(value={"NewApi"})
    private void excludeApp(String string2, VpnService.Builder object) {
        try {
            object.addDisallowedApplication(string2);
            return;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            object = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error during app whitelisting:");
            stringBuilder.append(nameNotFoundException.getMessage());
            object.logLine(stringBuilder.toString());
            return;
        }
    }

    private String getChannel() {
        NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("DNS Filter", (CharSequence)"DNS Filter", 3));
        }
        return "DNS Filter";
    }

    @TargetApi(value=21)
    private static Network[] getConnectedNetworks(ConnectivityManager connectivityManager, int n) {
        ArrayList<Network> arrayList = new ArrayList<Network>();
        Network[] arrnetwork = connectivityManager.getAllNetworks();
        for (int i = 0; i < arrnetwork.length; ++i) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(arrnetwork[i]);
            if (networkInfo == null || networkInfo.getType() != n && n != -1 || !networkInfo.isConnected()) continue;
            arrayList.add(arrnetwork[i]);
        }
        return arrayList.toArray((T[])new Network[arrayList.size()]);
    }

    @TargetApi(value=21)
    private static String[] getDNSviaConnectivityManager() {
        if (Build.VERSION.SDK_INT < 21) {
            return new String[0];
        }
        HashSet<String> hashSet = new HashSet<String>();
        ConnectivityManager connectivityManager = (ConnectivityManager)INSTANCE.getSystemService("connectivity");
        Object object2 = DNSFilterService.getConnectedNetworks(connectivityManager, 1);
        Network network = object2;
        if (object2.length == 0) {
            network = DNSFilterService.getConnectedNetworks(connectivityManager, -1);
        }
        for (Object object2 : network) {
            connectivityManager.getNetworkInfo((Network)object2);
            object2 = connectivityManager.getLinkProperties((Network)object2).getDnsServers();
            for (int i = 0; i < object2.size(); ++i) {
                hashSet.add(((InetAddress)object2.get(i)).getHostAddress());
            }
        }
        return hashSet.toArray(new String[hashSet.size()]);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String[] getDNSviaSysProps() {
        int n;
        Method method;
        String[] arrstring;
        String[] arrstring2;
        int n2;
        try {
            arrstring = new String[]();
            method = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            arrstring2 = new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
            n2 = arrstring2.length;
            n = 0;
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return new String[0];
        }
        while (n < n2) {
            String string2 = (String)method.invoke(null, arrstring2[n]);
            if (string2 != null && !string2.equals("")) {
                arrstring.add(string2);
            }
            ++n;
        }
        return arrstring.toArray(new String[arrstring.size()]);
    }

    private ParcelFileDescriptor initVPN() throws Exception {
        CharSequence charSequence2;
        int n;
        this.mtu = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("MTU", "3000"));
        VpnService.Builder builder = new VpnService.Builder((VpnService)this);
        builder.setSession("DNS Filter");
        if (DNSFilterService.supportsIPVersion(4)) {
            builder.addAddress(ADDRESS_IPV4, 24).addDnsServer(VIRTUALDNS_IPV4).addRoute(VIRTUALDNS_IPV4, 32);
        }
        if (DNSFilterService.supportsIPVersion(6)) {
            builder.addAddress(ADDRESS_IPV6, 48).addDnsServer(VIRTUALDNS_IPV6).addRoute(VIRTUALDNS_IPV6, 128);
        }
        Object object = new StringTokenizer(DNSFILTER.getConfig().getProperty("routeIPs", ""), ";");
        int n2 = n = object.countTokens();
        if (n != 0) {
            n2 = n;
            if (Build.VERSION.SDK_INT < 21) {
                n2 = 0;
                Logger.getLogger().logLine("WARNING!: Setting 'routeIPs' not supported for Android version below 5.01!\n Setting ignored!");
            }
        }
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            int n4;
            block15 : {
                CharSequence charSequence2 = object.nextToken().trim();
                Object object2 = Logger.getLogger();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Additional route IP:");
                stringBuilder.append((String)charSequence2);
                object2.logLine(stringBuilder.toString());
                object2 = InetAddress.getByName(charSequence2);
                n4 = 32;
                if (!(object2 instanceof Inet6Address)) break block15;
                n4 = 128;
            }
            try {
                builder.addRoute(InetAddress.getByName(charSequence2), n4);
                continue;
            }
            catch (UnknownHostException unknownHostException) {
                Logger.getLogger().logException(unknownHostException);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.addDisallowedApplication("dnsfilter.android");
        }
        object = new StringTokenizer(DNSFILTER.getConfig().getProperty("androidAppWhiteList", ""), ",");
        n2 = n = object.countTokens();
        if (n != 0) {
            n2 = n;
            if (Build.VERSION.SDK_INT < 21) {
                n2 = 0;
                Logger.getLogger().logLine("WARNING!: Application whitelisting not supported for Android version below 5.01!\n Setting ignored!");
            }
        }
        for (n = n3; n < n2; ++n) {
            this.excludeApp(object.nextToken().trim(), builder);
        }
        if (Build.VERSION.SDK_INT >= 24 && Build.VERSION.SDK_INT <= 27) {
            object = Logger.getLogger();
            charSequence2 = new StringBuilder();
            charSequence2.append("Running on SDK");
            charSequence2.append(Build.VERSION.SDK_INT);
            object.logLine(charSequence2.toString());
            this.excludeApp("com.android.vending", builder);
            this.excludeApp("com.google.android.apps.docs", builder);
            this.excludeApp("com.google.android.apps.photos", builder);
            this.excludeApp("com.google.android.gm", builder);
            this.excludeApp("com.google.android.apps.translate", builder);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setBlocking(true);
            Logger.getLogger().logLine("Using Blocking Mode!");
            this.blocking = true;
        }
        builder.setMtu(this.mtu);
        return builder.setConfigureIntent(this.pendingIntent).establish();
    }

    public static void onReload() throws IOException {
        DNSFilterService dNSFilterService = INSTANCE;
        if (dNSFilterService != null) {
            dNSFilterService.reload();
            return;
        }
        throw new IOException("Service instance is null!");
    }

    public static void possibleNetworkChange() {
        DNSFilterService.detectDNSServers();
        if (rootMode) {
            dnsReqForwarder.updateForward();
        }
    }

    private static void runOSCommand(boolean bl, String object) throws Exception {
        int n;
        Object object2 = Logger.getLogger();
        Object object3 = new StringBuilder();
        object3.append("Exec '");
        object3.append((String)object);
        object3.append("' !");
        object2.logLine(object3.toString());
        object2 = Runtime.getRuntime().exec("su");
        object3 = new DataOutputStream(object2.getOutputStream());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append("\n");
        object3.writeBytes(stringBuilder.toString());
        object3.flush();
        object3.writeBytes("exit\n");
        object3.flush();
        object3 = object2.getInputStream();
        object = new byte[1024];
        while ((n = object3.read((byte[])object)) != -1) {
            Logger.getLogger().log(new String((byte[])object, 0, n));
        }
        object3 = object2.getErrorStream();
        while ((n = object3.read((byte[])object)) != -1) {
            Logger.getLogger().log(new String((byte[])object, 0, n));
        }
        object2.waitFor();
        n = object2.exitValue();
        if (n != 0 && !bl) {
            object = new StringBuilder();
            object.append("Error in process execution: ");
            object.append(n);
            throw new Exception(object.toString());
        }
    }

    private static void runOSCommand(final boolean bl, boolean bl2, final String string2) throws Exception {
        if (!bl2) {
            DNSFilterService.runOSCommand(bl, string2);
            return;
        }
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    DNSFilterService.runOSCommand(bl, string2);
                    return;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    Logger.getLogger().logException(exception);
                    return;
                }
            }
        }).start();
    }

    private void setUpPortRedir() {
        if (DNS_PROXY_PORT_IS_REDIRECTED) {
            return;
        }
        try {
            DNSFilterService.runOSCommand(false, "iptables -t nat -A PREROUTING -p udp --dport 53 -j REDIRECT --to-port 5300");
            DNS_PROXY_PORT_IS_REDIRECTED = true;
            return;
        }
        catch (Exception exception) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception during setting Port redirection:");
            stringBuilder.append(exception.toString());
            loggerInterface.logLine(stringBuilder.toString());
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean shutdown() {
        block10 : {
            if (!DNSFilterService.is_running) {
                return true;
            }
            if (DNSFilterService.DNSFILTER == null || DNSFilterService.DNSFILTER.canStop()) break block10;
            Logger.getLogger().logLine("Cannot stop - pending operation!");
            return false;
            {
                catch (Exception var1_2) {
                    Logger.getLogger().logException(var1_2);
                    return false;
                }
            }
        }
        try {
            this.unregisterReceiver((BroadcastReceiver)ConnectionChangeReceiver.getInstance());
            ** GOTO lbl17
        }
        catch (Exception var1_1) {
            var1_1.printStackTrace();
lbl17: // 2 sources:
            if (DNSFilterService.rootMode) {
                DNSFilterService.dnsReqForwarder.clearForward();
            }
            if (this.vpnRunner != null) {
                VPNRunner.access$300(this.vpnRunner);
            }
            if (DNSFilterService.DNSFILTERPROXY != null) {
                DNSFilterService.DNSFILTERPROXY.stop();
                DNSFilterService.DNSFILTERPROXY = null;
                Logger.getLogger().logLine("DNSFilterProxy Mode stopped!");
            }
            if (DNSFilterService.DNSFILTER != null) {
                DNSFilterService.DNSFILTER.stop();
                DNSFilterService.DNSFILTER = null;
                Logger.getLogger().logLine("DNSFilter stopped!");
            }
            this.stopService(DNSFilterService.SERVICE);
            DNSFilterService.SERVICE = null;
            DNSFilterService.is_running = false;
            Thread.sleep(200L);
            return true;
        }
    }

    public static boolean stop(boolean bl) {
        DNSFilterService dNSFilterService = INSTANCE;
        if (dNSFilterService == null) {
            return true;
        }
        if (dNSFilterService.manageDNSCryptProxy && bl) {
            try {
                DNSFilterService.runOSCommand(false, KILL_DNSCRYPTPROXY);
                dNSFilterService.dnsCryptProxyStartTriggered = false;
            }
            catch (Exception exception) {
                Logger.getLogger().logException(exception);
            }
        }
        if (dNSFilterService.shutdown()) {
            INSTANCE = null;
            return true;
        }
        return false;
    }

    private static boolean supportsIPVersion(int n) throws Exception {
        String string2 = DNSFilterManager.getInstance().getConfig().getProperty("ipVersionSupport", "4, 6");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(n);
        if (string2.indexOf(stringBuilder.toString()) != -1) {
            return true;
        }
        return false;
    }

    public void onDestroy() {
        Logger.getLogger().logLine("destroyed");
        this.shutdown();
        super.onDestroy();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public int onStartCommand(Intent var1_1, int var2_5, int var3_6) {
        block16 : {
            AndroidEnvironment.initEnvironment((Context)this);
            DNSFilterService.INSTANCE = this;
            DNSFilterService.SERVICE = var1_1;
            if (DNSFilterService.DNSFILTER == null) break block16;
            Logger.getLogger().logLine("DNS Filter already running!");
            ** GOTO lbl51
        }
        var1_1 = new StringBuilder();
        var1_1.append(DNSProxyActivity.WORKPATH.getAbsolutePath());
        var1_1.append("/");
        DNSFilterManager.WORKDIR = var1_1.toString();
        DNSFilterService.DNSFILTER = DNSFilterManager.getInstance();
        DNSFilterService.DNSFILTER.init();
        DNSFilterService.JUST_STARTED = true;
        DNSFilterService.dnsProxyMode = Boolean.parseBoolean(DNSFilterService.DNSFILTER.getConfig().getProperty("dnsProxyOnAndroid", "false"));
        DNSFilterService.rootMode = Boolean.parseBoolean(DNSFilterService.DNSFILTER.getConfig().getProperty("rootModeOnAndroid", "false"));
        DNSFilterService.vpnInAdditionToProxyMode = Boolean.parseBoolean(DNSFilterService.DNSFILTER.getConfig().getProperty("vpnInAdditionToProxyMode", "false"));
        if (DNSFilterService.rootMode && !DNSFilterService.dnsProxyMode) {
            DNSFilterService.rootMode = false;
            Logger.getLogger().logLine("WARNING! Root Mode only possible in combination with DNS Proxy Mode!");
        }
        if (DNSFilterService.rootMode) {
            DNSFilterService.dnsReqForwarder.clean();
            DNSFilterService.dnsReqForwarder.updateForward();
        }
        this.registerReceiver((BroadcastReceiver)ConnectionChangeReceiver.getInstance(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        DNSFilterService.detectDNSServers();
        if (DNSFilterService.dnsProxyMode) {
            if (DNSFilterService.rootMode) {
                this.setUpPortRedir();
            }
            DNSFilterService.DNSFILTERPROXY = new DNSFilterProxy(5300);
            new Thread(DNSFilterService.DNSFILTERPROXY).start();
        }
        this.manageDNSCryptProxy = Boolean.parseBoolean(DNSFilterService.DNSFILTER.getConfig().getProperty("manageDNSCryptProxy", "false"));
        if (!this.manageDNSCryptProxy || (var4_7 = this.dnsCryptProxyStartTriggered)) ** GOTO lbl50
        {
            catch (Exception var1_4) {
                DNSFilterService.DNSFILTER = null;
                Logger.getLogger().logException(var1_4);
                return 1;
            }
        }
        try {
            DNSFilterService.runOSCommand(true, false, DNSFilterService.KILL_DNSCRYPTPROXY);
            var1_1 = new StringBuilder();
            var1_1.append(DNSFilterService.START_DNSCRYPTPROXY);
            var1_1.append(" ");
            var1_1.append(DNSFilterService.DNSFILTER.getConfig().getProperty("dnsCryptProxyStartOptions", ""));
            DNSFilterService.runOSCommand(false, true, var1_1.toString());
            this.dnsCryptProxyStartTriggered = true;
            ** GOTO lbl50
        }
        catch (Exception var1_2) {
            Logger.getLogger().logException(var1_2);
lbl50: // 3 sources:
            DNSFilterService.is_running = true;
lbl51: // 2 sources:
            try {
                this.pendingIntent = PendingIntent.getActivity((Context)this, (int)0, (Intent)new Intent((Context)this, DNSProxyActivity.class), (int)0);
                if (!DNSFilterService.dnsProxyMode || DNSFilterService.vpnInAdditionToProxyMode) {
                    var1_1 = this.initVPN();
                    if (var1_1 != null) {
                        DNSFilterService.startCounter = var2_5 = DNSFilterService.startCounter + 1;
                        this.vpnRunner = new VPNRunner(var2_5, (ParcelFileDescriptor)var1_1);
                        new Thread(this.vpnRunner).start();
                    } else {
                        Logger.getLogger().logLine("Error! Cannot get VPN Interface! Try restart!");
                    }
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    var1_1 = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder((Context)this, this.getChannel()) : new Notification.Builder((Context)this);
                    var1_1 = var1_1.setContentTitle((CharSequence)"DNSFilter is running!").setSmallIcon(2130771978).setContentIntent(this.pendingIntent).build();
                } else {
                    var1_1 = new Notification(2130771978, (CharSequence)"DNSFilter is running!", 0L);
                }
                this.startForeground(1, (Notification)var1_1);
                return 1;
            }
            catch (Exception var1_3) {
                Logger.getLogger().logException(var1_3);
                return 1;
            }
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void reload() throws IOException {
        if (!dnsProxyMode || vpnInAdditionToProxyMode) {
            int n;
            if (this.vpnRunner != null) {
                this.vpnRunner.stop();
            }
            DNSFILTER = DNSFilterManager.getInstance();
            ParcelFileDescriptor parcelFileDescriptor = this.initVPN();
            if (parcelFileDescriptor == null) throw new IOException("Error! Cannot get VPN Interface! Try restart!");
            startCounter = n = startCounter + 1;
            this.vpnRunner = new VPNRunner(n, parcelFileDescriptor);
            new Thread(this.vpnRunner).start();
        }
        JUST_STARTED = true;
        DNSFilterService.detectDNSServers();
        return;
        catch (Exception exception) {
            throw new IOException("Cannot initialize VPN!", exception);
        }
    }

    protected static class DNSReqForwarder {
        String forwardip = null;
        String ipFilePath;

        protected DNSReqForwarder() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ExecutionEnvironment.getEnvironment().getWorkDir());
            stringBuilder.append("forward_ip");
            this.ipFilePath = stringBuilder.toString();
        }

        public void clean() {
            Serializable serializable = new File(this.ipFilePath);
            try {
                if (serializable.exists()) {
                    FileInputStream fileInputStream = new FileInputStream((File)serializable);
                    CharSequence charSequence = new String(Utils.readFully(fileInputStream, 100));
                    fileInputStream.close();
                    if (!serializable.delete()) {
                        charSequence = new StringBuilder();
                        charSequence.append("Cannot delete ");
                        charSequence.append(this.ipFilePath);
                        throw new IOException(charSequence.toString());
                    }
                    Logger.getLogger().logLine("Cleaning up a previous redirect from previous not correctly terminated execution!");
                    serializable = new StringBuilder();
                    serializable.append("iptables -t nat -D OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                    serializable.append((String)charSequence);
                    serializable.append(":5300");
                    DNSFilterService.runOSCommand(false, serializable.toString());
                }
                return;
            }
            catch (Exception exception) {
                Logger.getLogger().logLine(exception.toString());
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void clearForward() {
            synchronized (this) {
                block7 : {
                    String string2 = this.forwardip;
                    if (string2 != null) break block7;
                    return;
                }
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("iptables -t nat -D OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                    stringBuilder.append(this.forwardip);
                    stringBuilder.append(":5300");
                    DNSFilterService.runOSCommand(false, stringBuilder.toString());
                    this.forwardip = null;
                    if (new File(this.ipFilePath).delete()) return;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Cannot delete ");
                    stringBuilder2.append(this.ipFilePath);
                    throw new IOException(stringBuilder2.toString());
                }
                catch (Exception exception) {
                    Logger.getLogger().logLine(exception.getMessage());
                    return;
                }
                finally {
                }
            }
        }

        public String getALocalIpAddress() throws IOException {
            Object object = null;
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> enumeration2 = networkInterface.getInetAddresses();
                while (enumeration2.hasMoreElements()) {
                    Object object2 = enumeration2.nextElement();
                    Object object3 = object;
                    if (!object2.isLoopbackAddress()) {
                        object3 = object;
                        if (object2 instanceof Inet4Address) {
                            object2 = object2.getHostAddress();
                            if (networkInterface.getName().startsWith("tun")) {
                                return object2;
                            }
                            object3 = object;
                            if (object == null) {
                                object3 = object2;
                            }
                        }
                    }
                    object = object3;
                }
            }
            return object;
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void updateForward() {
            // MONITORENTER : this
            String string2 = this.getALocalIpAddress();
            if (string2 == null || string2.equals(this.forwardip)) return;
            {
                this.clearForward();
                Object object = new StringBuilder();
                object.append("iptables -t nat -I OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                object.append(string2);
                object.append(":5300");
                DNSFilterService.runOSCommand(false, object.toString());
                this.forwardip = string2;
                object = new FileOutputStream(this.ipFilePath);
                object.write(string2.getBytes());
                object.flush();
                object.close();
                return;
            }
            catch (Exception exception) {
                Logger.getLogger().logLine(exception.getMessage());
            }
            // MONITOREXIT : this
        }
    }

    class VPNRunner
    implements Runnable {
        int id;
        FileInputStream in;
        FileOutputStream out;
        boolean stopped;
        Thread thread;
        ParcelFileDescriptor vpnInterface;

        private VPNRunner(int n, ParcelFileDescriptor parcelFileDescriptor) {
            this.in = null;
            this.out = null;
            this.thread = null;
            this.stopped = false;
            this.id = n;
            this.vpnInterface = parcelFileDescriptor;
            this.in = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            this.out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            Logger.getLogger().logLine("VPN Connected!");
        }

        private void stop() {
            this.stopped = true;
            try {
                this.in.close();
                this.out.close();
                this.vpnInterface.close();
                if (this.thread != null) {
                    this.thread.interrupt();
                }
                return;
            }
            catch (Exception exception) {
                Logger.getLogger().logException(exception);
                return;
            }
        }

        /*
         * Exception decompiling
         */
        @Override
        public void run() {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 7[WHILELOOP]
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
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
    }

}

