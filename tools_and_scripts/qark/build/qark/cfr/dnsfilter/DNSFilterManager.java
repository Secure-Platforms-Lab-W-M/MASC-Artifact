/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.BlockedHosts;
import dnsfilter.ConfigurationAccess;
import dnsfilter.DNSCommunicator;
import dnsfilter.DNSResolver;
import dnsfilter.DNSResponsePatcher;
import dnsfilter.remote.RemoteAccessServer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import util.ExecutionEnvironment;
import util.FileLogger;
import util.Logger;
import util.LoggerInterface;
import util.Utils;

public class DNSFilterManager
extends ConfigurationAccess {
    private static String DOWNLOADED_FF_PREFIX;
    private static DNSFilterManager INSTANCE;
    private static LoggerInterface TRAFFIC_LOG;
    public static final String VERSION = "1504000";
    public static String WORKDIR;
    private static boolean aborted;
    private static String additionalHostsImportTS;
    public static boolean debug;
    private static boolean filterHostsFileRemoveDuplicates;
    private static int filterListCacheSize;
    private static long filterReloadIntervalDays;
    private static String filterReloadURL;
    private static String filterhostfile;
    private static BlockedHosts hostFilter;
    private static Hashtable hostsFilterOverRule;
    private static long nextReload;
    private static int okCacheSize;
    private static boolean reloadUrlChanged;
    private static RemoteAccessServer remoteAccessManager;
    private static boolean updatingFilter;
    private static boolean validIndex;
    private AutoFilterUpdater autoFilterUpdater;
    protected Properties config = null;
    private boolean reloading_filter = false;
    private boolean serverStopped = true;

    static {
        INSTANCE = new DNSFilterManager();
        WORKDIR = "";
        okCacheSize = 500;
        filterListCacheSize = 500;
        additionalHostsImportTS = "0";
        aborted = false;
        hostFilter = null;
        hostsFilterOverRule = null;
        DOWNLOADED_FF_PREFIX = "# Downloaded by personalDNSFilter at: ";
        updatingFilter = false;
    }

    private DNSFilterManager() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void abortFilterUpdate() {
        aborted = true;
        DNSFilterManager dNSFilterManager = INSTANCE;
        synchronized (dNSFilterManager) {
            do {
                boolean bl;
                if (!(bl = updatingFilter)) {
                    aborted = false;
                    return;
                }
                try {
                    INSTANCE.wait();
                }
                catch (InterruptedException interruptedException) {
                    Logger.getLogger().logException(interruptedException);
                    continue;
                }
                break;
            } while (true);
        }
    }

    static /* synthetic */ boolean access$002(DNSFilterManager dNSFilterManager, boolean bl) {
        dNSFilterManager.reloading_filter = bl;
        return bl;
    }

    static /* synthetic */ void access$100(DNSFilterManager dNSFilterManager) throws IOException {
        dNSFilterManager.rebuildIndex();
    }

    static /* synthetic */ long access$302(long l) {
        nextReload = l;
        return l;
    }

    static /* synthetic */ boolean access$400(DNSFilterManager dNSFilterManager) throws IOException {
        return dNSFilterManager.updateFilter();
    }

    static /* synthetic */ boolean access$502(boolean bl) {
        validIndex = bl;
        return bl;
    }

    static /* synthetic */ void access$600(DNSFilterManager dNSFilterManager, boolean bl) throws IOException {
        dNSFilterManager.reloadFilter(bl);
    }

    static /* synthetic */ long access$700() {
        return filterReloadIntervalDays;
    }

    private void checkHostName(String string2) throws IOException {
        if (string2.length() > 253) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid Hostname: ");
            stringBuilder.append(string2);
            throw new IOException(stringBuilder.toString());
        }
    }

    private void copyFromAssets(String object, String object2) throws IOException {
        object = ExecutionEnvironment.getEnvironment().getAsset((String)object);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("./");
        stringBuilder.append(WORKDIR);
        stringBuilder.append((String)object2);
        object2 = new File(stringBuilder.toString());
        object2.getParentFile().mkdirs();
        Utils.copyFully((InputStream)object, new FileOutputStream((File)object2), true);
    }

    private void copyLocalFile(String object, String string2) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WORKDIR);
        stringBuilder.append((String)object);
        object = new File(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(WORKDIR);
        stringBuilder.append(string2);
        Utils.copyFile((File)object, new File(stringBuilder.toString()));
    }

    private void createDefaultConfiguration() {
        try {
            Object object = new StringBuilder();
            object.append(WORKDIR);
            object.append(".");
            new File(object.toString()).mkdir();
            this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("additionalHosts.txt");
            if (!new File(object.toString()).exists()) {
                this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
            }
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("VERSION.TXT");
            object = new File(object.toString());
            object.createNewFile();
            object = new FileOutputStream((File)object);
            object.write("1504000".getBytes());
            object.flush();
            object.close();
            Logger.getLogger().logLine("Default configuration created successfully!");
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logLine("FAILED creating default Configuration!");
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    private String getFilterReloadURL(Properties object) {
        String string2 = object.getProperty("filterAutoUpdateURL", "");
        object = object.getProperty("filterAutoUpdateURL_switchs", "");
        StringTokenizer stringTokenizer = new StringTokenizer(string2, ";");
        StringTokenizer stringTokenizer2 = new StringTokenizer((String)object, ";");
        int n = stringTokenizer.countTokens();
        string2 = "";
        object = "";
        for (int i = 0; i < n; ++i) {
            String string3 = stringTokenizer.nextToken().trim();
            boolean bl = true;
            if (stringTokenizer2.hasMoreTokens()) {
                bl = Boolean.parseBoolean(stringTokenizer2.nextToken().trim());
            }
            String string4 = string2;
            Object object2 = object;
            if (bl) {
                object2 = new StringBuilder();
                object2.append(string2);
                object2.append((String)object);
                object2.append(string3);
                string4 = object2.toString();
                object2 = "; ";
            }
            string2 = string4;
            object = object2;
        }
        return string2;
    }

    public static DNSFilterManager getInstance() {
        return INSTANCE;
    }

    private void initEnv() {
        debug = false;
        filterReloadURL = null;
        filterhostfile = null;
        filterReloadIntervalDays = 4L;
        nextReload = 0L;
        reloadUrlChanged = false;
        filterHostsFileRemoveDuplicates = false;
        validIndex = true;
        hostFilter = null;
        hostsFilterOverRule = null;
        additionalHostsImportTS = "0";
        this.reloading_filter = false;
    }

    private byte[] mergeAndPersistConfig(byte[] object) throws IOException {
        int n;
        Object object2 = new String((byte[])object);
        int n2 = object2.indexOf("\n#!!!filterHostsFile =");
        int n3 = 0;
        n2 = n2 != -1 ? 1 : 0;
        object = object2;
        if (n2 != 0) {
            object = object2.replace("\n#!!!filterHostsFile =", "\nfilterHostsFile =");
        }
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(object.getBytes()));
        String[] arrstring = properties.keySet().toArray(new String[0]);
        Object object3 = new BufferedReader(new InputStreamReader(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf")));
        object = new StringBuilder();
        object.append(WORKDIR);
        object.append("dnsfilter.conf");
        File file = new File(object.toString());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        boolean bl = properties.getProperty("previousAutoUpdateURL", "").trim().equals("https://adaway.org/hosts.txt; https://hosts-file.net/ad_servers.txt; https://hosts-file.net/emd.txt");
        do {
            StringBuilder stringBuilder;
            object = object2 = object3.readLine();
            if (object2 == null) break;
            if (bl && object.startsWith("filterAutoUpdateURL")) {
                object2 = Logger.getLogger();
                stringBuilder = new StringBuilder();
                stringBuilder.append("Taking over default configuration: ");
                stringBuilder.append((String)object);
                object2.logLine(stringBuilder.toString());
            } else {
                for (n = 0; n < arrstring.length; ++n) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(arrstring[n]);
                    stringBuilder.append(" =");
                    object2 = object;
                    if (object.startsWith(stringBuilder.toString())) {
                        if (arrstring[n].equals("filterHostsFile") && n2 != 0) {
                            object = new StringBuilder();
                            object.append("#!!!filterHostsFile = ");
                            object.append(properties.getProperty(arrstring[n], ""));
                            object2 = object.toString();
                        } else {
                            object = new StringBuilder();
                            object.append(arrstring[n]);
                            object.append(" = ");
                            object.append(properties.getProperty(arrstring[n], ""));
                            object2 = object.toString();
                        }
                    }
                    object = object2;
                }
            }
            object2 = new StringBuilder();
            object2.append((String)object);
            object2.append("\r\n");
            fileOutputStream.write(object2.toString().getBytes());
        } while (true);
        object3.close();
        object = new Properties();
        object.load(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf"));
        n = 1;
        for (n2 = n3; n2 < arrstring.length; ++n2) {
            n3 = n;
            if (!object.containsKey(arrstring[n2])) {
                if (n != 0) {
                    fileOutputStream.write("\r\n# Merged custom config from previous config file:\r\n".getBytes());
                }
                n3 = 0;
                object2 = new StringBuilder();
                object2.append(arrstring[n2]);
                object2.append(" = ");
                object2.append(properties.getProperty(arrstring[n2], ""));
                object2 = object2.toString();
                object3 = new StringBuilder();
                object3.append((String)object2);
                object3.append("\r\n");
                fileOutputStream.write(object3.toString().getBytes());
            }
            n = n3;
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        Logger.getLogger().logLine("Merged configuration 'dnsfilter.conf' after update to version 1504000!");
        object = new FileInputStream(file);
        object2 = Utils.readFully((InputStream)object, 1024);
        object.close();
        return object2;
    }

    private String[] parseHosts(String arrstring) throws IOException {
        if (!(arrstring.startsWith("#") || arrstring.startsWith("!") || arrstring.trim().equals(""))) {
            arrstring = (arrstring = new StringTokenizer((String)arrstring)).countTokens() >= 2 ? new String[]{arrstring.nextToken().trim(), arrstring.nextToken().trim()} : new String[]{"127.0.0.1", arrstring.nextToken().trim()};
            this.checkHostName(arrstring[1]);
            return arrstring;
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    private void rebuildIndex() throws IOException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 54[SIMPLE_IF_TAKEN]
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void reloadFilter(boolean var1_1) throws IOException {
        try {
            ExecutionEnvironment.getEnvironment().wakeLock();
            var3_2 = new StringBuilder();
            var3_2.append(DNSFilterManager.WORKDIR);
            var3_2.append(DNSFilterManager.filterhostfile);
            var3_3 = new File(var3_2.toString());
            var4_5 = new StringBuilder();
            var4_5.append(DNSFilterManager.WORKDIR);
            var4_5.append(DNSFilterManager.filterhostfile);
            var4_5.append(".DLD_CNT");
            var4_6 = new File(var4_5.toString());
            var5_9 = new StringBuilder();
            var5_9.append(DNSFilterManager.WORKDIR);
            var5_9.append("additionalHosts.txt");
            var5_10 = new File(var5_9.toString());
            if (!var5_10.exists()) {
                var5_10.createNewFile();
            }
            var6_11 = new StringBuilder();
            var6_11.append("");
            var6_11.append(var5_10.lastModified());
            var2_12 = var6_11.toString().equals(DNSFilterManager.additionalHostsImportTS);
            DNSFilterManager.nextReload = var3_3.exists() != false && var4_6.exists() != false && DNSFilterManager.reloadUrlChanged == false ? DNSFilterManager.filterReloadIntervalDays * 24L * 60L * 60L * 1000L + var4_6.lastModified() : 0L;
            var4_7 = new StringBuilder();
            var4_7.append(DNSFilterManager.WORKDIR);
            var4_7.append(DNSFilterManager.filterhostfile);
            var4_7.append(".idx");
            var4_8 = new File(var4_7.toString());
            if (!var4_8.exists() || !DNSFilterManager.validIndex || !BlockedHosts.checkIndexVersion(var4_8.getAbsolutePath())) ** GOTO lbl37
            DNSFilterManager.hostFilter = BlockedHosts.loadPersistedIndex(var4_8.getAbsolutePath(), false, DNSFilterManager.okCacheSize, DNSFilterManager.filterListCacheSize, DNSFilterManager.hostsFilterOverRule);
        }
        catch (Throwable var3_4) {}
        if ((var2_12 ^ true) == false) return;
        if (var3_3.exists() == false) return;
        if (DNSFilterManager.nextReload == 0L) return;
        new Thread(new AsyncIndexBuilder()).start();
        return;
lbl37: // 1 sources:
        if (var3_3.exists() == false) return;
        if (DNSFilterManager.nextReload == 0L) return;
        if (!var1_1) {
            this.rebuildIndex();
            return;
        }
        new Thread(new AsyncIndexBuilder()).start();
        return;
        throw var3_4;
        finally {
            ExecutionEnvironment.getEnvironment().releaseWakeLock();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private boolean updateFilter() throws IOException {
        var12_1 = DNSFilterManager.INSTANCE;
        // MONITORENTER : var12_1
        try {
            DNSFilterManager.updatingFilter = true;
            ExecutionEnvironment.getEnvironment().wakeLock();
            var9_2 = new StringBuilder();
            var9_2.append(DNSFilterManager.WORKDIR);
            var9_2.append(DNSFilterManager.filterhostfile);
            var9_2.append(".tmp");
            var13_8 = new FileOutputStream(var9_2.toString());
            var9_2 = new StringBuilder();
            var9_2.append(DNSFilterManager.DOWNLOADED_FF_PREFIX);
            var9_2.append(new Date());
            var9_2.append("from URLs: ");
            var9_2.append(DNSFilterManager.filterReloadURL);
            var9_2.append("\n");
            var13_8.write(var9_2.toString().getBytes());
            var9_2 = new StringTokenizer(DNSFilterManager.filterReloadURL, ";");
            var6_9 = var9_2.countTokens();
            var1_10 = 0;
            var2_11 = 0;
            do {
                block27 : {
                    if (var2_11 >= var6_9) ** GOTO lbl-1000
                    var14_19 = var9_2.nextToken().trim();
                    try {
                        if (var14_19.equals("")) ** GOTO lbl144
                        var10_17 = Logger.getLogger();
                        var11_18 = new byte[]();
                        var11_18.append("Connecting: ");
                        var11_18.append(var14_19);
                        var10_17.message(var11_18.toString());
                        var8_16 = var14_19.startsWith("file://");
                        if (var8_16) break block27;
                    }
                    catch (IOException var9_6) {
                        // empty catch block
                        ** break;
                    }
                    try {
                        var10_17 = new URL(var14_19).openConnection();
                        var11_18 = new StringBuilder();
                        var11_18.append("Mozilla/5.0 (");
                        var11_18.append(System.getProperty("os.name"));
                        var11_18.append("; ");
                        var11_18.append(System.getProperty("os.version"));
                        var11_18.append(")");
                        var10_17.setRequestProperty("User-Agent", var11_18.toString());
                        var10_17.setConnectTimeout(120000);
                        var10_17.setReadTimeout(120000);
                        var10_17 = new BufferedInputStream(var10_17.getInputStream(), 2048);
                        ** GOTO lbl55
                    }
                    catch (IOException var9_3) {
                        ** break;
                    }
                }
                var10_17 = new BufferedInputStream(new FileInputStream(var14_19.substring(7)), 2048);
lbl55: // 2 sources:
                var11_18 = new byte[2048];
                var3_12 = 0;
                var5_14 = 0;
                var4_13 = 100000;
                ** GOTO lbl85
lbl-1000: // 1 sources:
                {
                    block30 : {
                        block29 : {
                            this.updateIndexReloadInfoConfFile(DNSFilterManager.filterReloadURL, "0");
                            DNSFilterManager.reloadUrlChanged = false;
                            Logger.getLogger().logLine("Updating filter completed!");
                            var13_8.flush();
                            var13_8.close();
                            var9_2 = new StringBuilder();
                            var9_2.append(DNSFilterManager.WORKDIR);
                            var9_2.append(DNSFilterManager.filterhostfile);
                            var9_2 = new File(var9_2.toString());
                            if (var9_2.exists()) {
                                if (var9_2.delete() == false) throw new IOException("Renaming downloaded .tmp file to Filter file failed!");
                            }
                            var9_2 = new StringBuilder();
                            var9_2.append(DNSFilterManager.WORKDIR);
                            var9_2.append(DNSFilterManager.filterhostfile);
                            var9_2.append(".tmp");
                            var9_2 = new File(var9_2.toString());
                            var10_17 = new StringBuilder();
                            var10_17.append(DNSFilterManager.WORKDIR);
                            var10_17.append(DNSFilterManager.filterhostfile);
                            var9_2.renameTo(new File(var10_17.toString()));
                            var9_2 = new StringBuilder();
                            var9_2.append(DNSFilterManager.WORKDIR);
                            var9_2.append(DNSFilterManager.filterhostfile);
                            this.writeDownloadInfoFile(var1_10, new File(var9_2.toString()).lastModified());
                            return true;
lbl85: // 1 sources:
                            do {
                                block28 : {
                                    var15_20 = this.readHostFileEntry((InputStream)var10_17, (byte[])var11_18);
                                    if (var15_20[1] == -1) break;
                                    if (DNSFilterManager.aborted) break;
                                    if (var15_20[1] == 0) continue;
                                    var7_15 = var15_20[1];
                                    var16_21 /* !! */  = new String((byte[])var11_18, 0, var7_15);
                                    if (var16_21 /* !! */  == null || var16_21 /* !! */ .equals("localhost")) break block28;
                                    if (var15_20[0] == 1) {
                                        ++var5_14;
                                    } else {
                                        var17_22 = new StringBuilder();
                                        var17_22.append(var16_21 /* !! */ );
                                        var17_22.append("\n");
                                        var13_8.write(var17_22.toString().getBytes());
                                        ++var1_10;
                                    }
                                }
                                if ((var3_12 += var15_20[1]) <= var4_13) continue;
                                var15_20 = Logger.getLogger();
                                var16_21 /* !! */  = new StringBuilder();
                                var16_21 /* !! */ .append("Loading Filter - Bytes received:");
                                var16_21 /* !! */ .append(var3_12);
                                var15_20.message(var16_21 /* !! */ .toString());
                                var4_13 += 100000;
                                continue;
                                break;
                            } while (true);
                            try {
                                var10_17.close();
                                if (!DNSFilterManager.aborted) break block29;
                                Logger.getLogger().logLine("Aborting Filter Update!");
                                Logger.getLogger().message("Filter Update aborted!");
                                var13_8.flush();
                                var13_8.close();
                                ExecutionEnvironment.getEnvironment().releaseWakeLock();
                                DNSFilterManager.updatingFilter = false;
                                DNSFilterManager.INSTANCE.notifyAll();
                            }
                            catch (IOException var9_4) {
                                ** break;
                            }
                            // MONITOREXIT : var12_1
                            return false;
                        }
                        if (var5_14 != 0) {
                            var10_17 = Logger.getLogger();
                            var11_18 = new StringBuilder();
                            var11_18.append("WARNING! - ");
                            var11_18.append(var5_14);
                            var11_18.append(" skipped entrie(s) for ");
                            var11_18.append(var14_19);
                            var11_18.append("! Wildcards are only supported in additionalHosts.txt!");
                            var10_17.logLine(var11_18.toString());
                            break block30;
                        }
                        break block30;
                        catch (IOException var9_5) {
                            ** break;
                        }
                    }
                    ++var2_11;
                    continue;
                }
                break;
            } while (true);
        }
        catch (Throwable var9_7) {}
lbl-1000: // 4 sources:
        {
            ** try [egrp 12[TRYBLOCK] [27 : 780->1088)] { 
lbl149: // 1 sources:
            var10_17 = new StringBuilder();
            var10_17.append("ERROR loading filter: ");
            var10_17.append(var14_19);
            var10_17 = var10_17.toString();
            Logger.getLogger().message((String)var10_17);
            Logger.getLogger().logLine((String)var10_17);
            var13_8.close();
            throw var9_2;
        }
        throw var9_7;
lbl158: // 1 sources:
        finally {
            ExecutionEnvironment.getEnvironment().releaseWakeLock();
            DNSFilterManager.updatingFilter = false;
            DNSFilterManager.INSTANCE.notifyAll();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateIndexReloadInfoConfFile(String object, String string2) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(WORKDIR);
            stringBuilder.append("dnsfilter.conf");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(stringBuilder.toString())));
            boolean bl = false;
            boolean bl2 = false;
            do {
                void var7_14;
                String string3;
                boolean bl3;
                boolean bl4;
                String string4 = string3 = bufferedReader.readLine();
                if (string3 == null) break;
                if (object != null && string4.startsWith("previousAutoUpdateURL")) {
                    bl3 = true;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("previousAutoUpdateURL = ");
                    stringBuilder2.append((String)object);
                    String string5 = stringBuilder2.toString();
                    bl4 = bl2;
                } else {
                    bl3 = bl;
                    bl4 = bl2;
                    String string6 = string4;
                    if (string2 != null) {
                        bl3 = bl;
                        bl4 = bl2;
                        String string7 = string4;
                        if (string4.startsWith("additionalHosts_lastImportTS")) {
                            bl4 = true;
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("additionalHosts_lastImportTS = ");
                            stringBuilder3.append(string2);
                            String string8 = stringBuilder3.toString();
                            bl3 = bl;
                        }
                    }
                }
                string4 = new StringBuilder();
                string4.append((String)var7_14);
                string4.append("\r\n");
                byteArrayOutputStream.write(string4.toString().getBytes());
                bl = bl3;
                bl2 = bl4;
            } while (true);
            if (!bl && object != null) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("previousAutoUpdateURL = ");
                stringBuilder4.append((String)object);
                stringBuilder4.append("\r\n");
                byteArrayOutputStream.write(stringBuilder4.toString().getBytes());
            }
            if (!bl2 && string2 != null) {
                object = new StringBuilder();
                object.append("additionalHosts_lastImportTS = ");
                object.append(string2);
                object.append("\r\n");
                byteArrayOutputStream.write(object.toString().getBytes());
            }
            byteArrayOutputStream.flush();
            bufferedReader.close();
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("dnsfilter.conf");
            object = new FileOutputStream(object.toString());
            object.write(byteArrayOutputStream.toByteArray());
            object.flush();
            object.close();
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    private void writeDownloadInfoFile(int n, long l) throws IOException {
        Object object = new StringBuilder();
        object.append(WORKDIR);
        object.append(filterhostfile);
        object.append(".DLD_CNT");
        object = new FileOutputStream(object.toString());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        stringBuilder.append("\n");
        object.write(stringBuilder.toString().getBytes());
        stringBuilder = new StringBuilder();
        stringBuilder.append(l);
        stringBuilder.append("\n");
        object.write(stringBuilder.toString().getBytes());
        object.flush();
        object.close();
    }

    private void writeNewEntries(boolean bl, HashSet<String> object, BufferedWriter bufferedWriter) throws IOException {
        String string2 = "";
        if (!bl) {
            string2 = "!";
        }
        if (hostsFilterOverRule == null) {
            hostsFilterOverRule = new Hashtable();
            hostFilter.setHostsFilterOverRule(hostsFilterOverRule);
        }
        object = object.iterator();
        while (object.hasNext()) {
            String string3 = (String)object.next();
            hostsFilterOverRule.remove(string3);
            hostFilter.clearCache(string3);
            boolean bl2 = bl && hostFilter.contains(string3);
            if (!bl2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n");
                stringBuilder.append(string2);
                stringBuilder.append(string3);
                bufferedWriter.write(stringBuilder.toString());
                if (!bl) {
                    hostsFilterOverRule.put(string3, false);
                } else {
                    hostFilter.update(string3);
                }
            }
            if (!bl) continue;
            hostFilter.updatePersist();
        }
    }

    public boolean canStop() {
        return this.reloading_filter ^ true;
    }

    @Override
    public void doBackup(String string2) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/dnsfilter.conf");
            this.copyLocalFile("dnsfilter.conf", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/additionalHosts.txt");
            this.copyLocalFile("additionalHosts.txt", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/VERSION.TXT");
            this.copyLocalFile("VERSION.TXT", stringBuilder.toString());
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doRestore(String string2) throws IOException {
        try {
            if (!this.canStop()) {
                throw new IOException("Cannot stop! Pending Operation!");
            }
            this.stop();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/dnsfilter.conf");
            this.copyLocalFile(stringBuilder.toString(), "dnsfilter.conf");
            stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/additionalHosts.txt");
            this.copyLocalFile(stringBuilder.toString(), "additionalHosts.txt");
            stringBuilder = new StringBuilder();
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            stringBuilder.append("/VERSION.TXT");
            this.copyLocalFile(stringBuilder.toString(), "VERSION.TXT");
            if (this.config != null && (string2 = this.config.getProperty("filterHostsFile")) != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(WORKDIR);
                stringBuilder.append(string2);
                new File(stringBuilder.toString()).delete();
            }
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doRestoreDefaults() throws IOException {
        try {
            String string2;
            if (!this.canStop()) {
                throw new IOException("Cannot stop! Pending Operation!");
            }
            this.stop();
            this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
            this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
            if (this.config != null && (string2 = this.config.getProperty("filterHostsFile")) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WORKDIR);
                stringBuilder.append(string2);
                new File(stringBuilder.toString()).delete();
            }
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    @Override
    public byte[] getAdditionalHosts(int n) throws IOException {
        Object object;
        block3 : {
            try {
                object = new StringBuilder();
                object.append(WORKDIR);
                object.append("additionalHosts.txt");
                object = new File(object.toString());
                if (object.length() <= (long)n) break block3;
                return null;
            }
            catch (IOException iOException) {
                throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
            }
        }
        object = new FileInputStream((File)object);
        byte[] arrby = Utils.readFully((InputStream)object, 1024);
        object.close();
        return arrby;
    }

    @Override
    public String[] getAvailableBackups() throws IOException {
        String[] arrstring = new String[]();
        arrstring.append(WORKDIR);
        arrstring.append("backup");
        Serializable serializable = new File(arrstring.toString());
        arrstring = new String[]{};
        if (serializable.exists()) {
            arrstring = serializable.list();
        }
        serializable = new ArrayList();
        for (String string2 : arrstring) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(WORKDIR);
            stringBuilder.append("backup/");
            stringBuilder.append(string2);
            if (!new File(stringBuilder.toString()).isDirectory()) continue;
            serializable.add(string2);
        }
        Collections.sort(serializable);
        return serializable.toArray(new String[0]);
    }

    @Override
    public Properties getConfig() throws IOException {
        try {
            Object object;
            if (this.config == null) {
                object = this.readConfig();
                this.config = new Properties();
                this.config.load(new ByteArrayInputStream((byte[])object));
            }
            object = this.config;
            return object;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return null;
        }
    }

    @Override
    public long[] getFilterStatistics() {
        return new long[]{DNSResponsePatcher.getOkCount(), DNSResponsePatcher.getFilterCount()};
    }

    @Override
    public String getLastDNSAddress() {
        return DNSCommunicator.getInstance().getLastDNSAddress();
    }

    @Override
    public String getVersion() {
        return "1504000";
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void init() throws IOException {
        Map.Entry entry2222;
        Object object;
        block20 : {
            if (!this.serverStopped) {
                throw new IllegalStateException("Cannot start! Already running!");
            }
            this.initEnv();
            Logger.getLogger().logLine("***Initializing PersonalDNSFilter Version 1504000!***");
            object = Logger.getLogger();
            entry2222 = new StringBuilder();
            entry2222.append("Using Directory: ");
            entry2222.append(WORKDIR);
            object.logLine(entry2222.toString());
            object = this.readConfig();
            this.config = new Properties();
            this.config.load(new ByteArrayInputStream((byte[])object));
            this.serverStopped = false;
            object = remoteAccessManager;
            if (object == null) {
                try {
                    int n = Integer.parseInt(this.config.getProperty("server_remote_ctrl_port", "-1"));
                    object = this.config.getProperty("server_remote_ctrl_keyphrase", "");
                    if (n != -1) {
                        remoteAccessManager = new RemoteAccessServer(n, (String)object);
                    }
                }
                catch (Exception exception) {
                    Logger.getLogger().logException(exception);
                }
            }
            okCacheSize = Integer.parseInt(this.config.getProperty("allowedHostsCacheSize", "1000").trim());
            filterListCacheSize = Integer.parseInt(this.config.getProperty("filterHostsCacheSize", "1000").trim());
            if (this.config.getProperty("androidKeepAwake", "true").equalsIgnoreCase("true")) {
                ExecutionEnvironment.getEnvironment().wakeLock();
            }
            additionalHostsImportTS = this.config.getProperty("additionalHosts_lastImportTS", "0");
            try {
                if (this.config.getProperty("enableTrafficLog", "true").equalsIgnoreCase("true")) {
                    object = new StringBuilder();
                    object.append(WORKDIR);
                    object.append("log");
                    TRAFFIC_LOG = new FileLogger(object.toString(), this.config.getProperty("trafficLogName", "trafficlog"), Integer.parseInt(this.config.getProperty("trafficLogSize", "1048576").trim()), Integer.parseInt(this.config.getProperty("trafficLogSlotCount", "2").trim()), "timestamp, client:port, request type, domain name, answer");
                    ((FileLogger)TRAFFIC_LOG).enableTimestamp(true);
                    Logger.setLogger(TRAFFIC_LOG, "TrafficLogger");
                    break block20;
                }
                TRAFFIC_LOG = null;
            }
            catch (NumberFormatException numberFormatException) {
                Logger.getLogger().logLine("Cannot parse log configuration!");
                throw new IOException(numberFormatException);
            }
        }
        debug = Boolean.parseBoolean(this.config.getProperty("debug", "false"));
        filterHostsFileRemoveDuplicates = true;
        filterhostfile = this.config.getProperty("filterHostsFile");
        if (filterhostfile == null) return;
        for (Map.Entry entry2222 : this.config.entrySet()) {
            String string2 = (String)entry2222.getKey();
            if (!string2.startsWith("filter.")) continue;
            if (hostsFilterOverRule == null) {
                hostsFilterOverRule = new Hashtable();
            }
            hostsFilterOverRule.put(string2.substring(7), new Boolean(Boolean.parseBoolean(((String)entry2222.getValue()).trim())));
        }
        object = new StringBuilder();
        object.append(WORKDIR);
        object.append("additionalHosts.txt");
        object = new File(object.toString());
        if (object.exists()) {
            object = new BufferedReader(new InputStreamReader(new FileInputStream((File)object)));
            while ((entry2222 = object.readLine()) != null) {
                if (!entry2222.startsWith("!")) continue;
                if (hostsFilterOverRule == null) {
                    hostsFilterOverRule = new Hashtable();
                }
                hostsFilterOverRule.put(entry2222.substring(1).trim(), new Boolean(false));
            }
            object.close();
        }
        filterReloadURL = this.getFilterReloadURL(this.config);
        filterReloadIntervalDays = Integer.parseInt(this.config.getProperty("reloadIntervalDays", "4"));
        object = this.config.getProperty("previousAutoUpdateURL");
        if (filterReloadURL != null) {
            reloadUrlChanged = filterReloadURL.equals(object) ^ true;
        }
        this.reloadFilter(true);
        if (filterReloadURL != null) {
            this.autoFilterUpdater = new AutoFilterUpdater();
            object = new Thread(this.autoFilterUpdater);
            object.setDaemon(true);
            object.start();
        }
        DNSResponsePatcher.init(hostFilter, TRAFFIC_LOG);
        return;
        catch (NumberFormatException numberFormatException) {
            Logger.getLogger().logLine("Cannot parse cache size configuration!");
            throw new IOException(numberFormatException);
        }
    }

    @Override
    public int openConnectionsCount() {
        return DNSResolver.getResolverCount();
    }

    @Override
    public byte[] readConfig() throws ConfigurationAccess.ConfigurationAccessException {
        byte[] arrby;
        Object object = new StringBuilder();
        object.append(WORKDIR);
        object.append("dnsfilter.conf");
        byte[] arrby2 = new byte[](object.toString());
        object = arrby2;
        if (!arrby2.exists()) {
            object = Logger.getLogger();
            arrby = new byte[]();
            arrby.append(arrby2);
            arrby.append(" not found! - creating default config!");
            object.logLine(arrby.toString());
            this.createDefaultConfiguration();
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("dnsfilter.conf");
            object = new File(object.toString());
        }
        try {
            object = new FileInputStream((File)object);
            arrby = Utils.readFully((InputStream)object, 1024);
            object.close();
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("additionalHosts.txt");
            object = new File(object.toString());
            if (!object.exists()) {
                object.createNewFile();
                object = new FileOutputStream((File)object);
                Utils.copyFully(ExecutionEnvironment.getEnvironment().getAsset("additionalHosts.txt"), (OutputStream)object, true);
            }
            object = new StringBuilder();
            object.append(WORKDIR);
            object.append("VERSION.TXT");
            arrby2 = new File(object.toString());
            object = "";
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
        if (arrby2.exists()) {
            arrby2 = new FileInputStream((File)arrby2);
            object = new String(Utils.readFully((InputStream)arrby2, 100));
            arrby2.close();
        }
        arrby2 = arrby;
        if (!object.equals("1504000")) {
            arrby2 = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Updated version! Previous version:");
            stringBuilder.append((String)object);
            stringBuilder.append(", current version:");
            stringBuilder.append("1504000");
            arrby2.logLine(stringBuilder.toString());
            this.createDefaultConfiguration();
            arrby2 = this.mergeAndPersistConfig(arrby);
        }
        return arrby2;
    }

    public int[] readHostFileEntry(InputStream object, byte[] arrby) throws IOException {
        int n;
        int n2 = 0;
        int n3 = object.read();
        while (n3 == 35) {
            n3 = n = Utils.skipLine((InputStream)object);
            if (n == -1) continue;
            n3 = Utils.skipWhitespace((InputStream)object, n);
        }
        if (n3 == -1) {
            return new int[]{0, -1};
        }
        if (arrby.length == 0) {
            throw new IOException("Buffer Overflow!");
        }
        if (n3 == 42) {
            n2 = 1;
        }
        arrby[0] = (byte)n3;
        n = 0;
        int n4 = 1;
        int n5 = n3;
        int n6 = n2;
        block1 : while (n5 != -1 && n5 != 10) {
            int n7 = n;
            int n8 = n5;
            n2 = n6;
            n3 = n4;
            do {
                block14 : {
                    block13 : {
                        n4 = n3;
                        n6 = n2;
                        n5 = n8;
                        n = n7;
                        if (n8 == -1) continue block1;
                        n4 = n3;
                        n6 = n2;
                        n5 = n8;
                        n = n7;
                        if (n8 == 10) continue block1;
                        n8 = object.read();
                        if (n8 == 9 || n8 == 32) break block13;
                        n4 = n3;
                        n5 = n2;
                        n = n8;
                        n6 = n7;
                        if (n8 != 13) break block14;
                    }
                    if (n7 == 1) {
                        Utils.skipLine((InputStream)object);
                        return new int[]{n2, n3};
                    }
                    n6 = 1;
                    n5 = 0;
                    n = Utils.skipWhitespace((InputStream)object, n8);
                    n4 = 0;
                }
                if (n == 42) {
                    n5 = 1;
                }
                n3 = n4;
                n2 = n5;
                n8 = n;
                n7 = n6;
                if (n == -1) continue;
                if (n4 == arrby.length) {
                    throw new IOException("Buffer Overflow!");
                }
                if (n < 32 && n < 9 && n > 13) {
                    object = new StringBuilder();
                    object.append("Non Printable character: ");
                    object.append(n);
                    object.append("(");
                    object.append((char)n);
                    object.append(")");
                    throw new IOException(object.toString());
                }
                arrby[n4] = (byte)n;
                n3 = n4 + 1;
                n2 = n5;
                n8 = n;
                n7 = n6;
            } while (true);
        }
        return new int[]{n6, n4 - 1};
    }

    @Override
    public void releaseConfiguration() {
    }

    @Override
    public void releaseWakeLock() {
        ExecutionEnvironment.getEnvironment().releaseWakeLock();
    }

    @Override
    public void restart() throws IOException {
        try {
            this.stop();
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void stop() throws IOException {
        if (this.serverStopped) {
            return;
        }
        this.abortFilterUpdate();
        synchronized (this) {
            if (this.autoFilterUpdater != null) {
                this.autoFilterUpdater.stop();
                this.autoFilterUpdater = null;
            }
            this.notifyAll();
            if (hostFilter != null) {
                hostFilter.clear();
            }
            DNSResponsePatcher.init(null, null);
            if (TRAFFIC_LOG != null) {
                TRAFFIC_LOG.closeLogger();
                Logger.removeLogger("TrafficLogger");
            }
            this.serverStopped = true;
            ExecutionEnvironment.getEnvironment().releaseAllWakeLocks();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void triggerUpdateFilter() {
        if (this.reloading_filter) {
            Logger.getLogger().logLine("Filter Reload currently running!");
            return;
        }
        if (filterReloadURL != null) {
            synchronized (this) {
                nextReload = 0L;
                this.notifyAll();
                return;
            }
        }
        Logger.getLogger().logLine("DNS Filter: Setting 'filterAutoUpdateURL' not configured - cannot update filter!");
    }

    @Override
    public void updateAdditionalHosts(byte[] arrby) throws IOException {
        try {
            Object object = new StringBuilder();
            object.append(WORKDIR);
            object.append("additionalHosts.txt");
            object = new FileOutputStream(object.toString());
            object.write(arrby);
            object.flush();
            object.close();
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    @Override
    public void updateConfig(byte[] arrby) throws IOException {
        try {
            Object object = new StringBuilder();
            object.append(WORKDIR);
            object.append("dnsfilter.conf");
            object = new FileOutputStream(object.toString());
            object.write(arrby);
            object.flush();
            object.close();
            this.config.load(new ByteArrayInputStream(arrby));
            Logger.getLogger().message("Config Changed!\nRestart might be required!");
            return;
        }
        catch (IOException iOException) {
            throw new ConfigurationAccess.ConfigurationAccessException(iOException.getMessage(), iOException);
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public void updateFilter(String var1_1, boolean var2_6) throws IOException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 4[TRYBLOCK]
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

    @Override
    public void wakeLock() {
        ExecutionEnvironment.getEnvironment().wakeLock();
    }

    private class AsyncIndexBuilder
    implements Runnable {
        private AsyncIndexBuilder() {
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void run() {
            DNSFilterManager.access$002(DNSFilterManager.this, true);
            DNSFilterManager.access$100(DNSFilterManager.this);
lbl4: // 2 sources:
            do {
                DNSFilterManager.access$002(DNSFilterManager.this, false);
                return;
                break;
            } while (true);
            {
                catch (Throwable var1_1) {
                }
                catch (IOException var1_2) {}
                {
                    Logger.getLogger().logException(var1_2);
                    ** continue;
                }
            }
            DNSFilterManager.access$002(DNSFilterManager.this, false);
            throw var1_1;
        }
    }

    private class AutoFilterUpdater
    implements Runnable {
        private Object monitor;
        boolean running;
        boolean stopRequest;

        public AutoFilterUpdater() {
            this.stopRequest = false;
            this.running = false;
            this.monitor = INSTANCE;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void waitUntilNextFilterReload() throws InterruptedException {
            Object object = this.monitor;
            synchronized (object) {
                while (nextReload > System.currentTimeMillis() && !this.stopRequest) {
                    this.monitor.wait(10000L);
                }
                return;
            }
        }

        /*
         * Exception decompiling
         */
        @Override
        public void run() {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 14[WHILELOOP]
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

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void stop() {
            this.stopRequest = true;
            Object object = this.monitor;
            synchronized (object) {
                this.monitor.notifyAll();
                boolean bl;
                while (bl = this.running) {
                    try {
                        this.monitor.wait();
                    }
                    catch (InterruptedException interruptedException) {
                        Logger.getLogger().logException(interruptedException);
                        continue;
                    }
                    break;
                }
                return;
            }
        }
    }

}

