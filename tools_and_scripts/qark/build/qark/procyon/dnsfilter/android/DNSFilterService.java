// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import java.io.OutputStream;
import ip.UDPPacket;
import ip.IPPacket;
import dnsfilter.DNSResolver;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import util.Utils;
import java.io.FileInputStream;
import java.io.File;
import util.ExecutionEnvironment;
import android.app.Notification;
import android.app.Notification$Builder;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import java.net.Inet6Address;
import dnsfilter.ConfigurationAccess;
import android.os.ParcelFileDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.HashSet;
import android.annotation.TargetApi;
import android.net.NetworkInfo;
import java.util.ArrayList;
import android.net.Network;
import android.net.ConnectivityManager;
import android.app.NotificationChannel;
import android.os.Build$VERSION;
import android.app.NotificationManager;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager$NameNotFoundException;
import android.net.VpnService$Builder;
import util.LoggerInterface;
import dnsfilter.DNSCommunicator;
import java.io.IOException;
import java.util.StringTokenizer;
import java.net.InetAddress;
import dnsfilter.DNSServer;
import java.util.Vector;
import util.Logger;
import android.app.PendingIntent;
import android.content.Intent;
import dnsfilter.DNSFilterProxy;
import dnsfilter.DNSFilterManager;
import android.net.VpnService;

public class DNSFilterService extends VpnService
{
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
    private boolean blocking;
    boolean dnsCryptProxyStartTriggered;
    boolean manageDNSCryptProxy;
    private int mtu;
    PendingIntent pendingIntent;
    private VPNRunner vpnRunner;
    
    static {
        DNSFilterService.VIRTUALDNS_IPV4 = "10.10.10.10";
        DNSFilterService.VIRTUALDNS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa1";
        DNSFilterService.ADDRESS_IPV4 = "10.0.2.15";
        DNSFilterService.ADDRESS_IPV6 = "fdc8:1095:91e1:aaaa:aaaa:aaaa:aaaa:aaa2";
        DNSFilterService.SERVICE = null;
        DNSFilterService.START_DNSCRYPTPROXY = "dnscrypt-proxy";
        DNSFilterService.KILL_DNSCRYPTPROXY = "killall dnscrypt-proxy";
        DNSFilterService.DNSFILTER = null;
        DNSFilterService.DNSFILTERPROXY = null;
        DNSFilterService.INSTANCE = null;
        DNSFilterService.JUST_STARTED = false;
        DNSFilterService.DNS_PROXY_PORT_IS_REDIRECTED = false;
        DNSFilterService.dnsProxyMode = false;
        DNSFilterService.rootMode = false;
        DNSFilterService.vpnInAdditionToProxyMode = false;
        DNSFilterService.is_running = false;
        DNSFilterService.dnsReqForwarder = new DNSReqForwarder();
        DNSFilterService.startCounter = 0;
    }
    
    public DNSFilterService() {
        this.blocking = false;
        this.vpnRunner = null;
        this.manageDNSCryptProxy = false;
        this.dnsCryptProxyStartTriggered = false;
    }
    
    public static void detectDNSServers() {
        while (true) {
            while (true) {
                int n2 = 0;
                Label_0657: {
                    try {
                        final DNSFilterManager dnsfilter = DNSFilterService.DNSFILTER;
                        if (dnsfilter == null) {
                            return;
                        }
                        final boolean boolean1 = Boolean.parseBoolean(dnsfilter.getConfig().getProperty("detectDNS", "true"));
                        int int1 = 15000;
                        try {
                            int1 = Integer.parseInt(dnsfilter.getConfig().getProperty("dnsRequestTimeout", "15000"));
                        }
                        catch (Exception ex) {
                            Logger.getLogger().logException(ex);
                        }
                        if (!boolean1 && !DNSFilterService.JUST_STARTED) {
                            return;
                        }
                        final int n = 0;
                        DNSFilterService.JUST_STARTED = false;
                        if (DNSProxyActivity.debug) {
                            Logger.getLogger().logLine("Detecting DNS Servers...");
                        }
                        final Vector<DNSServer> vector = new Vector<DNSServer>();
                        if (boolean1 && !DNSFilterService.rootMode) {
                            final String[] dnSviaConnectivityManager = getDNSviaConnectivityManager();
                            String[] dnSviaSysProps;
                            if (dnSviaConnectivityManager.length == 0) {
                                if (DNSProxyActivity.debug) {
                                    Logger.getLogger().logLine("Fallback DNS detection via SystemProperties");
                                }
                                dnSviaSysProps = getDNSviaSysProps();
                            }
                            else {
                                dnSviaSysProps = dnSviaConnectivityManager;
                                if (DNSProxyActivity.debug) {
                                    Logger.getLogger().logLine("DNS detection via ConnectivityManager");
                                    dnSviaSysProps = dnSviaConnectivityManager;
                                }
                            }
                            int i = 0;
                            try {
                                while (i < dnSviaSysProps.length) {
                                    final String s = dnSviaSysProps[i];
                                    if (s != null && !s.equals("")) {
                                        if (DNSProxyActivity.debug) {
                                            final LoggerInterface logger = Logger.getLogger();
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("DNS:");
                                            sb.append(s);
                                            logger.logLine(sb.toString());
                                        }
                                        if (!s.equals(DNSFilterService.VIRTUALDNS_IPV4) && !s.equals(DNSFilterService.VIRTUALDNS_IPV6)) {
                                            vector.add(DNSServer.getInstance().createDNSServer(0, InetAddress.getByName(s), 53, int1, null));
                                        }
                                    }
                                    ++i;
                                }
                            }
                            catch (Exception ex2) {
                                Logger.getLogger().logException(ex2);
                            }
                        }
                        if (vector.isEmpty()) {
                            if (boolean1 && DNSFilterService.rootMode) {
                                Logger.getLogger().message("DNS Detection not possible in rootMode!");
                            }
                            final StringTokenizer stringTokenizer = new StringTokenizer(dnsfilter.getConfig().getProperty("fallbackDNS", ""), ";");
                            final int countTokens = stringTokenizer.countTokens();
                            n2 = n;
                            if (n2 < countTokens) {
                                final String trim = stringTokenizer.nextToken().trim();
                                if (DNSProxyActivity.debug) {
                                    final LoggerInterface logger2 = Logger.getLogger();
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("DNS:");
                                    sb2.append(trim);
                                    logger2.logLine(sb2.toString());
                                }
                                try {
                                    final DNSServer dnsServer = DNSServer.getInstance().createDNSServer(trim, int1);
                                    if (DNSFilterService.rootMode && dnsServer.getPort() == 53) {
                                        throw new IOException("Port 53 not allowed when running in Root Mode! Use Dot or DoH!");
                                    }
                                    vector.add(DNSServer.getInstance().createDNSServer(trim, int1));
                                }
                                catch (Exception ex3) {
                                    final LoggerInterface logger3 = Logger.getLogger();
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Cannot create DNS Server for ");
                                    sb3.append(trim);
                                    sb3.append("!\n");
                                    sb3.append(ex3.toString());
                                    logger3.logLine(sb3.toString());
                                    final LoggerInterface logger4 = Logger.getLogger();
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Invalid DNS Server entry: '");
                                    sb4.append(trim);
                                    sb4.append("'");
                                    logger4.message(sb4.toString());
                                }
                                break Label_0657;
                            }
                        }
                        DNSCommunicator.getInstance().setDNSServers(vector.toArray(new DNSServer[vector.size()]));
                        return;
                    }
                    catch (IOException ex4) {
                        Logger.getLogger().logException(ex4);
                        return;
                    }
                }
                ++n2;
                continue;
            }
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void excludeApp(final String s, final VpnService$Builder vpnService$Builder) {
        try {
            vpnService$Builder.addDisallowedApplication(s);
        }
        catch (PackageManager$NameNotFoundException ex) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("Error during app whitelisting:");
            sb.append(ex.getMessage());
            logger.logLine(sb.toString());
        }
    }
    
    private String getChannel() {
        final NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
        if (Build$VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel("DNS Filter", (CharSequence)"DNS Filter", 3));
        }
        return "DNS Filter";
    }
    
    @TargetApi(21)
    private static Network[] getConnectedNetworks(final ConnectivityManager connectivityManager, final int n) {
        final ArrayList<Network> list = new ArrayList<Network>();
        final Network[] allNetworks = connectivityManager.getAllNetworks();
        for (int i = 0; i < allNetworks.length; ++i) {
            final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(allNetworks[i]);
            if (networkInfo != null && (networkInfo.getType() == n || n == -1) && networkInfo.isConnected()) {
                list.add(allNetworks[i]);
            }
        }
        return list.toArray(new Network[list.size()]);
    }
    
    @TargetApi(21)
    private static String[] getDNSviaConnectivityManager() {
        if (Build$VERSION.SDK_INT < 21) {
            return new String[0];
        }
        final HashSet<String> set = new HashSet<String>();
        final ConnectivityManager connectivityManager = (ConnectivityManager)DNSFilterService.INSTANCE.getSystemService("connectivity");
        Network[] array;
        if ((array = getConnectedNetworks(connectivityManager, 1)).length == 0) {
            array = getConnectedNetworks(connectivityManager, -1);
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final Network network = array[i];
            connectivityManager.getNetworkInfo(network);
            final List dnsServers = connectivityManager.getLinkProperties(network).getDnsServers();
            for (int j = 0; j < dnsServers.size(); ++j) {
                set.add(dnsServers.get(j).getHostAddress());
            }
        }
        return set.toArray(new String[set.size()]);
    }
    
    private static String[] getDNSviaSysProps() {
        while (true) {
            while (true) {
                int n;
                try {
                    final HashSet<String> set = new HashSet<String>();
                    final Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
                    final String[] array = { "net.dns1", "net.dns2", "net.dns3", "net.dns4" };
                    final int length = array.length;
                    n = 0;
                    if (n >= length) {
                        return set.toArray(new String[set.size()]);
                    }
                    final String s = (String)method.invoke(null, array[n]);
                    if (s != null && !s.equals("")) {
                        set.add(s);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger().logException(ex);
                    return new String[0];
                }
                ++n;
                continue;
            }
        }
    }
    
    private ParcelFileDescriptor initVPN() throws Exception {
        this.mtu = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("MTU", "3000"));
        final VpnService$Builder vpnService$Builder = new VpnService$Builder((VpnService)this);
        vpnService$Builder.setSession("DNS Filter");
        if (supportsIPVersion(4)) {
            vpnService$Builder.addAddress(DNSFilterService.ADDRESS_IPV4, 24).addDnsServer(DNSFilterService.VIRTUALDNS_IPV4).addRoute(DNSFilterService.VIRTUALDNS_IPV4, 32);
        }
        if (supportsIPVersion(6)) {
            vpnService$Builder.addAddress(DNSFilterService.ADDRESS_IPV6, 48).addDnsServer(DNSFilterService.VIRTUALDNS_IPV6).addRoute(DNSFilterService.VIRTUALDNS_IPV6, 128);
        }
        final StringTokenizer stringTokenizer = new StringTokenizer(DNSFilterService.DNSFILTER.getConfig().getProperty("routeIPs", ""), ";");
        final int countTokens = stringTokenizer.countTokens();
        int n;
        if ((n = countTokens) != 0) {
            n = countTokens;
            if (Build$VERSION.SDK_INT < 21) {
                n = 0;
                Logger.getLogger().logLine("WARNING!: Setting 'routeIPs' not supported for Android version below 5.01!\n Setting ignored!");
            }
        }
        final int n2 = 0;
        for (int i = 0; i < n; ++i) {
            final String trim = stringTokenizer.nextToken().trim();
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("Additional route IP:");
            sb.append(trim);
            logger.logLine(sb.toString());
            try {
                final InetAddress byName = InetAddress.getByName(trim);
                int n3 = 32;
                if (byName instanceof Inet6Address) {
                    n3 = 128;
                }
                vpnService$Builder.addRoute(InetAddress.getByName(trim), n3);
            }
            catch (UnknownHostException ex) {
                Logger.getLogger().logException(ex);
            }
        }
        if (Build$VERSION.SDK_INT >= 21) {
            vpnService$Builder.addDisallowedApplication("dnsfilter.android");
        }
        final StringTokenizer stringTokenizer2 = new StringTokenizer(DNSFilterService.DNSFILTER.getConfig().getProperty("androidAppWhiteList", ""), ",");
        final int countTokens2 = stringTokenizer2.countTokens();
        int n4;
        if ((n4 = countTokens2) != 0) {
            n4 = countTokens2;
            if (Build$VERSION.SDK_INT < 21) {
                n4 = 0;
                Logger.getLogger().logLine("WARNING!: Application whitelisting not supported for Android version below 5.01!\n Setting ignored!");
            }
        }
        for (int j = n2; j < n4; ++j) {
            this.excludeApp(stringTokenizer2.nextToken().trim(), vpnService$Builder);
        }
        if (Build$VERSION.SDK_INT >= 24 && Build$VERSION.SDK_INT <= 27) {
            final LoggerInterface logger2 = Logger.getLogger();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Running on SDK");
            sb2.append(Build$VERSION.SDK_INT);
            logger2.logLine(sb2.toString());
            this.excludeApp("com.android.vending", vpnService$Builder);
            this.excludeApp("com.google.android.apps.docs", vpnService$Builder);
            this.excludeApp("com.google.android.apps.photos", vpnService$Builder);
            this.excludeApp("com.google.android.gm", vpnService$Builder);
            this.excludeApp("com.google.android.apps.translate", vpnService$Builder);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            vpnService$Builder.setBlocking(true);
            Logger.getLogger().logLine("Using Blocking Mode!");
            this.blocking = true;
        }
        vpnService$Builder.setMtu(this.mtu);
        return vpnService$Builder.setConfigureIntent(this.pendingIntent).establish();
    }
    
    public static void onReload() throws IOException {
        final DNSFilterService instance = DNSFilterService.INSTANCE;
        if (instance != null) {
            instance.reload();
            return;
        }
        throw new IOException("Service instance is null!");
    }
    
    public static void possibleNetworkChange() {
        detectDNSServers();
        if (DNSFilterService.rootMode) {
            DNSFilterService.dnsReqForwarder.updateForward();
        }
    }
    
    private static void runOSCommand(final boolean b, final String s) throws Exception {
        final LoggerInterface logger = Logger.getLogger();
        final StringBuilder sb = new StringBuilder();
        sb.append("Exec '");
        sb.append(s);
        sb.append("' !");
        logger.logLine(sb.toString());
        final Process exec = Runtime.getRuntime().exec("su");
        final DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("\n");
        dataOutputStream.writeBytes(sb2.toString());
        dataOutputStream.flush();
        dataOutputStream.writeBytes("exit\n");
        dataOutputStream.flush();
        final InputStream inputStream = exec.getInputStream();
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            Logger.getLogger().log(new String(array, 0, read));
        }
        final InputStream errorStream = exec.getErrorStream();
        while (true) {
            final int read2 = errorStream.read(array);
            if (read2 == -1) {
                break;
            }
            Logger.getLogger().log(new String(array, 0, read2));
        }
        exec.waitFor();
        final int exitValue = exec.exitValue();
        if (exitValue != 0 && !b) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Error in process execution: ");
            sb3.append(exitValue);
            throw new Exception(sb3.toString());
        }
    }
    
    private static void runOSCommand(final boolean b, final boolean b2, final String s) throws Exception {
        if (!b2) {
            runOSCommand(b, s);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOSCommand(b, s);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger().logException(ex);
                }
            }
        }).start();
    }
    
    private void setUpPortRedir() {
        if (DNSFilterService.DNS_PROXY_PORT_IS_REDIRECTED) {
            return;
        }
        try {
            runOSCommand(false, "iptables -t nat -A PREROUTING -p udp --dport 53 -j REDIRECT --to-port 5300");
            DNSFilterService.DNS_PROXY_PORT_IS_REDIRECTED = true;
        }
        catch (Exception ex) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception during setting Port redirection:");
            sb.append(ex.toString());
            logger.logLine(sb.toString());
        }
    }
    
    private boolean shutdown() {
        if (!DNSFilterService.is_running) {
            return true;
        }
        try {
            if (DNSFilterService.DNSFILTER != null && !DNSFilterService.DNSFILTER.canStop()) {
                Logger.getLogger().logLine("Cannot stop - pending operation!");
                return false;
            }
            try {
                this.unregisterReceiver((BroadcastReceiver)ConnectionChangeReceiver.getInstance());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (DNSFilterService.rootMode) {
                DNSFilterService.dnsReqForwarder.clearForward();
            }
            if (this.vpnRunner != null) {
                this.vpnRunner.stop();
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
        catch (Exception ex2) {
            Logger.getLogger().logException(ex2);
            return false;
        }
    }
    
    public static boolean stop(final boolean b) {
        final DNSFilterService instance = DNSFilterService.INSTANCE;
        if (instance == null) {
            return true;
        }
        if (instance.manageDNSCryptProxy && b) {
            try {
                runOSCommand(false, DNSFilterService.KILL_DNSCRYPTPROXY);
                instance.dnsCryptProxyStartTriggered = false;
            }
            catch (Exception ex) {
                Logger.getLogger().logException(ex);
            }
        }
        if (instance.shutdown()) {
            DNSFilterService.INSTANCE = null;
            return true;
        }
        return false;
    }
    
    private static boolean supportsIPVersion(final int n) throws Exception {
        final String property = DNSFilterManager.getInstance().getConfig().getProperty("ipVersionSupport", "4, 6");
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(n);
        return property.indexOf(sb.toString()) != -1;
    }
    
    public void onDestroy() {
        Logger.getLogger().logLine("destroyed");
        this.shutdown();
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent service, int n, final int n2) {
        AndroidEnvironment.initEnvironment((Context)this);
        DNSFilterService.INSTANCE = this;
        DNSFilterService.SERVICE = service;
        Label_0375: {
            if (DNSFilterService.DNSFILTER != null) {
                Logger.getLogger().logLine("DNS Filter already running!");
                break Label_0375;
            }
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append(DNSProxyActivity.WORKPATH.getAbsolutePath());
                sb.append("/");
                DNSFilterManager.WORKDIR = sb.toString();
                (DNSFilterService.DNSFILTER = DNSFilterManager.getInstance()).init();
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
                detectDNSServers();
                if (DNSFilterService.dnsProxyMode) {
                    if (DNSFilterService.rootMode) {
                        this.setUpPortRedir();
                    }
                    DNSFilterService.DNSFILTERPROXY = new DNSFilterProxy(5300);
                    new Thread(DNSFilterService.DNSFILTERPROXY).start();
                }
                this.manageDNSCryptProxy = Boolean.parseBoolean(DNSFilterService.DNSFILTER.getConfig().getProperty("manageDNSCryptProxy", "false"));
                if (this.manageDNSCryptProxy && !this.dnsCryptProxyStartTriggered) {
                    try {
                        runOSCommand(true, false, DNSFilterService.KILL_DNSCRYPTPROXY);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(DNSFilterService.START_DNSCRYPTPROXY);
                        sb2.append(" ");
                        sb2.append(DNSFilterService.DNSFILTER.getConfig().getProperty("dnsCryptProxyStartOptions", ""));
                        runOSCommand(false, true, sb2.toString());
                        this.dnsCryptProxyStartTriggered = true;
                    }
                    catch (Exception ex) {
                        Logger.getLogger().logException(ex);
                    }
                }
                DNSFilterService.is_running = true;
                try {
                    this.pendingIntent = PendingIntent.getActivity((Context)this, 0, new Intent((Context)this, (Class)DNSProxyActivity.class), 0);
                    if (!DNSFilterService.dnsProxyMode || DNSFilterService.vpnInAdditionToProxyMode) {
                        final ParcelFileDescriptor initVPN = this.initVPN();
                        if (initVPN != null) {
                            n = ++DNSFilterService.startCounter;
                            this.vpnRunner = new VPNRunner(n, initVPN);
                            new Thread(this.vpnRunner).start();
                        }
                        else {
                            Logger.getLogger().logLine("Error! Cannot get VPN Interface! Try restart!");
                        }
                    }
                    Notification build;
                    if (Build$VERSION.SDK_INT >= 16) {
                        Notification$Builder notification$Builder;
                        if (Build$VERSION.SDK_INT >= 26) {
                            notification$Builder = new Notification$Builder((Context)this, this.getChannel());
                        }
                        else {
                            notification$Builder = new Notification$Builder((Context)this);
                        }
                        build = notification$Builder.setContentTitle((CharSequence)"DNSFilter is running!").setSmallIcon(2130771978).setContentIntent(this.pendingIntent).build();
                    }
                    else {
                        build = new Notification(2130771978, (CharSequence)"DNSFilter is running!", 0L);
                    }
                    this.startForeground(1, build);
                    return 1;
                }
                catch (Exception ex2) {
                    Logger.getLogger().logException(ex2);
                    return 1;
                }
            }
            catch (Exception ex3) {
                DNSFilterService.DNSFILTER = null;
                Logger.getLogger().logException(ex3);
                return 1;
            }
        }
    }
    
    public void reload() throws IOException {
        Label_0080: {
            if (DNSFilterService.dnsProxyMode && !DNSFilterService.vpnInAdditionToProxyMode) {
                break Label_0080;
            }
            if (this.vpnRunner != null) {
                this.vpnRunner.stop();
            }
            DNSFilterService.DNSFILTER = DNSFilterManager.getInstance();
            try {
                final ParcelFileDescriptor initVPN = this.initVPN();
                if (initVPN != null) {
                    this.vpnRunner = new VPNRunner(++DNSFilterService.startCounter, initVPN);
                    new Thread(this.vpnRunner).start();
                    DNSFilterService.JUST_STARTED = true;
                    detectDNSServers();
                    return;
                }
                throw new IOException("Error! Cannot get VPN Interface! Try restart!");
            }
            catch (Exception ex) {
                throw new IOException("Cannot initialize VPN!", ex);
            }
        }
    }
    
    protected static class DNSReqForwarder
    {
        String forwardip;
        String ipFilePath;
        
        protected DNSReqForwarder() {
            this.forwardip = null;
            final StringBuilder sb = new StringBuilder();
            sb.append(ExecutionEnvironment.getEnvironment().getWorkDir());
            sb.append("forward_ip");
            this.ipFilePath = sb.toString();
        }
        
        public void clean() {
            final File file = new File(this.ipFilePath);
            try {
                if (file.exists()) {
                    final FileInputStream fileInputStream = new FileInputStream(file);
                    final String s = new String(Utils.readFully(fileInputStream, 100));
                    fileInputStream.close();
                    if (!file.delete()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Cannot delete ");
                        sb.append(this.ipFilePath);
                        throw new IOException(sb.toString());
                    }
                    Logger.getLogger().logLine("Cleaning up a previous redirect from previous not correctly terminated execution!");
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("iptables -t nat -D OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                    sb2.append(s);
                    sb2.append(":5300");
                    runOSCommand(false, sb2.toString());
                }
            }
            catch (Exception ex) {
                Logger.getLogger().logLine(ex.toString());
            }
        }
        
        public void clearForward() {
            synchronized (this) {
                if (this.forwardip == null) {
                    return;
                }
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("iptables -t nat -D OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                    sb.append(this.forwardip);
                    sb.append(":5300");
                    runOSCommand(false, sb.toString());
                    this.forwardip = null;
                    if (!new File(this.ipFilePath).delete()) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Cannot delete ");
                        sb2.append(this.ipFilePath);
                        throw new IOException(sb2.toString());
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger().logLine(ex.getMessage());
                }
            }
        }
        
        public String getALocalIpAddress() throws IOException {
            String s = null;
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();
                final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress = inetAddresses.nextElement();
                    String s2 = s;
                    if (!inetAddress.isLoopbackAddress()) {
                        s2 = s;
                        if (inetAddress instanceof Inet4Address) {
                            final String hostAddress = inetAddress.getHostAddress();
                            if (networkInterface.getName().startsWith("tun")) {
                                return hostAddress;
                            }
                            if ((s2 = s) == null) {
                                s2 = hostAddress;
                            }
                        }
                    }
                    s = s2;
                }
            }
            return s;
        }
        
        public void updateForward() {
            // monitorenter(this)
            try {
                try {
                    final String aLocalIpAddress = this.getALocalIpAddress();
                    if (aLocalIpAddress != null && !aLocalIpAddress.equals(this.forwardip)) {
                        this.clearForward();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("iptables -t nat -I OUTPUT -p udp --dport 53 -j DNAT --to-destination ");
                        sb.append(aLocalIpAddress);
                        sb.append(":5300");
                        runOSCommand(false, sb.toString());
                        this.forwardip = aLocalIpAddress;
                        final FileOutputStream fileOutputStream = new FileOutputStream(this.ipFilePath);
                        fileOutputStream.write(aLocalIpAddress.getBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
                finally {}
            }
            catch (Exception ex) {
                Logger.getLogger().logLine(ex.getMessage());
            }
            // monitorexit(this)
            return;
        }
        // monitorexit(this)
    }
    
    class VPNRunner implements Runnable
    {
        int id;
        FileInputStream in;
        FileOutputStream out;
        boolean stopped;
        Thread thread;
        ParcelFileDescriptor vpnInterface;
        
        private VPNRunner(final int id, final ParcelFileDescriptor vpnInterface) {
            this.in = null;
            this.out = null;
            this.thread = null;
            this.stopped = false;
            this.id = id;
            this.vpnInterface = vpnInterface;
            this.in = new FileInputStream(vpnInterface.getFileDescriptor());
            this.out = new FileOutputStream(vpnInterface.getFileDescriptor());
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
            }
            catch (Exception ex) {
                Logger.getLogger().logException(ex);
            }
        }
        
        @Override
        public void run() {
        Label_0839_Outer:
            while (true) {
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb = new StringBuilder();
                sb.append("VPN Runner Thread ");
                sb.append(this.id);
                sb.append(" started!");
                logger.logLine(sb.toString());
                this.thread = Thread.currentThread();
                while (true) {
                    Label_0929: {
                        try {
                            final int int1 = Integer.parseInt(DNSFilterManager.getInstance().getConfig().getProperty("maxResolverCount", "100"));
                            while (!this.stopped) {
                                final byte[] array = new byte[DNSServer.getBufSize()];
                                final int read = this.in.read(array);
                                if (this.stopped) {
                                    break;
                                }
                                boolean b = false;
                                if (DNSResolver.getResolverCount() > int1) {
                                    final LoggerInterface logger2 = Logger.getLogger();
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Max Resolver Count reached: ");
                                    sb2.append(int1);
                                    logger2.message(sb2.toString());
                                    b = true;
                                }
                                if (read > 0) {
                                    try {
                                        final IPPacket ipPacket = new IPPacket(array, 0, read);
                                        if (ipPacket.getVersion() == 6 && DNSProxyActivity.debug) {
                                            final LoggerInterface logger3 = Logger.getLogger();
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append("!!!IPV6 Packet!!! Protocol:");
                                            sb3.append(ipPacket.getProt());
                                            logger3.logLine(sb3.toString());
                                            final LoggerInterface logger4 = Logger.getLogger();
                                            final StringBuilder sb4 = new StringBuilder();
                                            sb4.append("SourceAddress:");
                                            sb4.append(IPPacket.int2ip(ipPacket.getSourceIP()));
                                            logger4.logLine(sb4.toString());
                                            final LoggerInterface logger5 = Logger.getLogger();
                                            final StringBuilder sb5 = new StringBuilder();
                                            sb5.append("DestAddress:");
                                            sb5.append(IPPacket.int2ip(ipPacket.getDestIP()));
                                            logger5.logLine(sb5.toString());
                                            final LoggerInterface logger6 = Logger.getLogger();
                                            final StringBuilder sb6 = new StringBuilder();
                                            sb6.append("TTL:");
                                            sb6.append(ipPacket.getTTL());
                                            logger6.logLine(sb6.toString());
                                            final LoggerInterface logger7 = Logger.getLogger();
                                            final StringBuilder sb7 = new StringBuilder();
                                            sb7.append("Length:");
                                            sb7.append(ipPacket.getLength());
                                            logger7.logLine(sb7.toString());
                                            if (ipPacket.getProt() == 0) {
                                                Logger.getLogger().logLine("Hopp by Hopp Header");
                                                final LoggerInterface logger8 = Logger.getLogger();
                                                final StringBuilder sb8 = new StringBuilder();
                                                sb8.append("NextHeader:");
                                                sb8.append(array[40] & 0xFF);
                                                logger8.logLine(sb8.toString());
                                                final LoggerInterface logger9 = Logger.getLogger();
                                                final StringBuilder sb9 = new StringBuilder();
                                                sb9.append("Hdr Ext Len:");
                                                sb9.append(array[41] & 0xFF);
                                                logger9.logLine(sb9.toString());
                                                if ((array[40] & 0xFF) == 0x3A) {
                                                    final LoggerInterface logger10 = Logger.getLogger();
                                                    final StringBuilder sb10 = new StringBuilder();
                                                    sb10.append("Received ICMP IPV6 Paket Type:");
                                                    sb10.append(array[48] & 0xFF);
                                                    logger10.logLine(sb10.toString());
                                                }
                                            }
                                        }
                                        if (ipPacket.checkCheckSum() != 0) {
                                            throw new IOException("IP Header Checksum Error!");
                                        }
                                        if (ipPacket.getProt() == 1 && DNSProxyActivity.debug) {
                                            final LoggerInterface logger11 = Logger.getLogger();
                                            final StringBuilder sb11 = new StringBuilder();
                                            sb11.append("Received ICMP Paket Type:");
                                            sb11.append(array[20] & 0xFF);
                                            logger11.logLine(sb11.toString());
                                        }
                                        if (ipPacket.getProt() != 17) {
                                            break Label_0929;
                                        }
                                        final UDPPacket udpPacket = new UDPPacket(array, 0, read);
                                        if (udpPacket.checkCheckSum() != 0) {
                                            throw new IOException("UDP packet Checksum Error!");
                                        }
                                        if (!b) {
                                            new Thread(new DNSResolver(udpPacket, this.out)).start();
                                            break Label_0929;
                                        }
                                        break Label_0929;
                                    }
                                    catch (Exception ex) {
                                        Logger.getLogger().logException(ex);
                                        break Label_0929;
                                    }
                                    catch (IOException ex2) {
                                        final LoggerInterface logger12 = Logger.getLogger();
                                        final StringBuilder sb12 = new StringBuilder();
                                        sb12.append("IOEXCEPTION: ");
                                        sb12.append(ex2.toString());
                                        logger12.logLine(sb12.toString());
                                        break Label_0929;
                                    }
                                    break Label_0929;
                                }
                                if (DNSFilterService.this.blocking) {
                                    continue Label_0839_Outer;
                                }
                                Thread.sleep(1000L);
                            }
                        }
                        catch (Exception ex3) {
                            if (!this.stopped) {
                                Logger.getLogger().logLine("VPN);Runner died!");
                                Logger.getLogger().logException(ex3);
                            }
                        }
                        break;
                    }
                    continue;
                }
            }
            final LoggerInterface logger13 = Logger.getLogger();
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("VPN Runner Thread ");
            sb13.append(this.id);
            sb13.append(" terminated!");
            logger13.logLine(sb13.toString());
        }
    }
}
