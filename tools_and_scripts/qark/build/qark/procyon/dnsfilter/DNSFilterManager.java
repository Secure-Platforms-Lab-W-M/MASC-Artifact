// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import util.FileLogger;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.util.HashSet;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.io.OutputStream;
import util.Utils;
import java.io.FileOutputStream;
import java.io.File;
import util.ExecutionEnvironment;
import java.io.IOException;
import util.Logger;
import java.util.Properties;
import dnsfilter.remote.RemoteAccessServer;
import java.util.Hashtable;
import util.LoggerInterface;

public class DNSFilterManager extends ConfigurationAccess
{
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
    protected Properties config;
    private boolean reloading_filter;
    private boolean serverStopped;
    
    static {
        DNSFilterManager.INSTANCE = new DNSFilterManager();
        DNSFilterManager.WORKDIR = "";
        DNSFilterManager.okCacheSize = 500;
        DNSFilterManager.filterListCacheSize = 500;
        DNSFilterManager.additionalHostsImportTS = "0";
        DNSFilterManager.aborted = false;
        DNSFilterManager.hostFilter = null;
        DNSFilterManager.hostsFilterOverRule = null;
        DNSFilterManager.DOWNLOADED_FF_PREFIX = "# Downloaded by personalDNSFilter at: ";
        DNSFilterManager.updatingFilter = false;
    }
    
    private DNSFilterManager() {
        this.serverStopped = true;
        this.reloading_filter = false;
        this.config = null;
    }
    
    private void abortFilterUpdate() {
        DNSFilterManager.aborted = true;
        synchronized (DNSFilterManager.INSTANCE) {
        Label_0024_Outer:
            while (DNSFilterManager.updatingFilter) {
                while (true) {
                    try {
                        DNSFilterManager.INSTANCE.wait();
                        continue Label_0024_Outer;
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger().logException(ex);
                        continue;
                    }
                    break;
                }
                break;
            }
            DNSFilterManager.aborted = false;
        }
    }
    
    private void checkHostName(final String s) throws IOException {
        if (s.length() > 253) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid Hostname: ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
    }
    
    private void copyFromAssets(final String s, final String s2) throws IOException {
        final InputStream asset = ExecutionEnvironment.getEnvironment().getAsset(s);
        final StringBuilder sb = new StringBuilder();
        sb.append("./");
        sb.append(DNSFilterManager.WORKDIR);
        sb.append(s2);
        final File file = new File(sb.toString());
        file.getParentFile().mkdirs();
        Utils.copyFully(asset, new FileOutputStream(file), true);
    }
    
    private void copyLocalFile(final String s, final String s2) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSFilterManager.WORKDIR);
        sb.append(s);
        final File file = new File(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(DNSFilterManager.WORKDIR);
        sb2.append(s2);
        Utils.copyFile(file, new File(sb2.toString()));
    }
    
    private void createDefaultConfiguration() {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append(".");
            new File(sb.toString()).mkdir();
            this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(DNSFilterManager.WORKDIR);
            sb2.append("additionalHosts.txt");
            if (!new File(sb2.toString()).exists()) {
                this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(DNSFilterManager.WORKDIR);
            sb3.append("VERSION.TXT");
            final File file = new File(sb3.toString());
            file.createNewFile();
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write("1504000".getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Logger.getLogger().logLine("Default configuration created successfully!");
        }
        catch (IOException ex) {
            Logger.getLogger().logLine("FAILED creating default Configuration!");
            Logger.getLogger().logException(ex);
        }
    }
    
    private String getFilterReloadURL(final Properties properties) {
        final String property = properties.getProperty("filterAutoUpdateURL", "");
        final String property2 = properties.getProperty("filterAutoUpdateURL_switchs", "");
        final StringTokenizer stringTokenizer = new StringTokenizer(property, ";");
        final StringTokenizer stringTokenizer2 = new StringTokenizer(property2, ";");
        final int countTokens = stringTokenizer.countTokens();
        String s = "";
        String s2 = "";
        String string;
        String s3;
        for (int i = 0; i < countTokens; ++i, s = string, s2 = s3) {
            final String trim = stringTokenizer.nextToken().trim();
            boolean boolean1 = true;
            if (stringTokenizer2.hasMoreTokens()) {
                boolean1 = Boolean.parseBoolean(stringTokenizer2.nextToken().trim());
            }
            string = s;
            s3 = s2;
            if (boolean1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(s2);
                sb.append(trim);
                string = sb.toString();
                s3 = "; ";
            }
        }
        return s;
    }
    
    public static DNSFilterManager getInstance() {
        return DNSFilterManager.INSTANCE;
    }
    
    private void initEnv() {
        DNSFilterManager.debug = false;
        DNSFilterManager.filterReloadURL = null;
        DNSFilterManager.filterhostfile = null;
        DNSFilterManager.filterReloadIntervalDays = 4L;
        DNSFilterManager.nextReload = 0L;
        DNSFilterManager.reloadUrlChanged = false;
        DNSFilterManager.filterHostsFileRemoveDuplicates = false;
        DNSFilterManager.validIndex = true;
        DNSFilterManager.hostFilter = null;
        DNSFilterManager.hostsFilterOverRule = null;
        DNSFilterManager.additionalHostsImportTS = "0";
        this.reloading_filter = false;
    }
    
    private byte[] mergeAndPersistConfig(final byte[] array) throws IOException {
        final String s = new String(array);
        final int index = s.indexOf("\n#!!!filterHostsFile =");
        final int n = 0;
        final boolean b = index != -1;
        String replace = s;
        if (b) {
            replace = s.replace("\n#!!!filterHostsFile =", "\nfilterHostsFile =");
        }
        final Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(replace.getBytes()));
        final String[] array2 = ((Hashtable<Object, V>)properties).keySet().toArray(new String[0]);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf")));
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSFilterManager.WORKDIR);
        sb.append("dnsfilter.conf");
        final File file = new File(sb.toString());
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final boolean equals = properties.getProperty("previousAutoUpdateURL", "").trim().equals("https://adaway.org/hosts.txt; https://hosts-file.net/ad_servers.txt; https://hosts-file.net/emd.txt");
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (equals && line.startsWith("filterAutoUpdateURL")) {
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Taking over default configuration: ");
                sb2.append(line);
                logger.logLine(sb2.toString());
            }
            else {
                String s2;
                for (int i = 0; i < array2.length; ++i, line = s2) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(array2[i]);
                    sb3.append(" =");
                    s2 = line;
                    if (line.startsWith(sb3.toString())) {
                        if (array2[i].equals("filterHostsFile") && b) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("#!!!filterHostsFile = ");
                            sb4.append(properties.getProperty(array2[i], ""));
                            s2 = sb4.toString();
                        }
                        else {
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append(array2[i]);
                            sb5.append(" = ");
                            sb5.append(properties.getProperty(array2[i], ""));
                            s2 = sb5.toString();
                        }
                    }
                }
            }
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(line);
            sb6.append("\r\n");
            fileOutputStream.write(sb6.toString().getBytes());
        }
        bufferedReader.close();
        final Properties properties2 = new Properties();
        properties2.load(ExecutionEnvironment.getEnvironment().getAsset("dnsfilter.conf"));
        int n2 = 1;
        int n3;
        for (int j = n; j < array2.length; ++j, n2 = n3) {
            n3 = n2;
            if (!properties2.containsKey(array2[j])) {
                if (n2 != 0) {
                    fileOutputStream.write("\r\n# Merged custom config from previous config file:\r\n".getBytes());
                }
                n3 = 0;
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(array2[j]);
                sb7.append(" = ");
                sb7.append(properties.getProperty(array2[j], ""));
                final String string = sb7.toString();
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(string);
                sb8.append("\r\n");
                fileOutputStream.write(sb8.toString().getBytes());
            }
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        Logger.getLogger().logLine("Merged configuration 'dnsfilter.conf' after update to version 1504000!");
        final FileInputStream fileInputStream = new FileInputStream(file);
        final byte[] fully = Utils.readFully(fileInputStream, 1024);
        fileInputStream.close();
        return fully;
    }
    
    private String[] parseHosts(final String s) throws IOException {
        if (!s.startsWith("#") && !s.startsWith("!") && !s.trim().equals("")) {
            final StringTokenizer stringTokenizer = new StringTokenizer(s);
            String[] array;
            if (stringTokenizer.countTokens() >= 2) {
                array = new String[] { stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim() };
            }
            else {
                array = new String[] { "127.0.0.1", stringTokenizer.nextToken().trim() };
            }
            this.checkHostName(array[1]);
            return array;
        }
        return null;
    }
    
    private void rebuildIndex() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          19
        //     5: aload           19
        //     7: monitorenter   
        //     8: iconst_1       
        //     9: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //    12: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //    15: ldc_w           "Reading filter file and building index...!"
        //    18: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //    23: new             Ljava/lang/StringBuilder;
        //    26: dup            
        //    27: invokespecial   java/lang/StringBuilder.<init>:()V
        //    30: astore          10
        //    32: aload           10
        //    34: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //    37: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    40: pop            
        //    41: aload           10
        //    43: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //    46: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    49: pop            
        //    50: new             Ljava/io/File;
        //    53: dup            
        //    54: aload           10
        //    56: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    59: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    62: astore          20
        //    64: new             Ljava/lang/StringBuilder;
        //    67: dup            
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: astore          10
        //    73: aload           10
        //    75: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: pop            
        //    82: aload           10
        //    84: ldc             "additionalHosts.txt"
        //    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    89: pop            
        //    90: new             Ljava/io/File;
        //    93: dup            
        //    94: aload           10
        //    96: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    99: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   102: astore          13
        //   104: new             Ljava/lang/StringBuilder;
        //   107: dup            
        //   108: invokespecial   java/lang/StringBuilder.<init>:()V
        //   111: astore          10
        //   113: aload           10
        //   115: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: pop            
        //   122: aload           10
        //   124: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload           10
        //   133: ldc_w           ".idx"
        //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   139: pop            
        //   140: new             Ljava/io/File;
        //   143: dup            
        //   144: aload           10
        //   146: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   149: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   152: astore          16
        //   154: new             Ljava/io/BufferedReader;
        //   157: dup            
        //   158: new             Ljava/io/InputStreamReader;
        //   161: dup            
        //   162: new             Ljava/io/FileInputStream;
        //   165: dup            
        //   166: aload           20
        //   168: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   171: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   174: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   177: astore          12
        //   179: new             Ljava/io/BufferedReader;
        //   182: dup            
        //   183: new             Ljava/io/InputStreamReader;
        //   186: dup            
        //   187: new             Ljava/io/FileInputStream;
        //   190: dup            
        //   191: aload           13
        //   193: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   196: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   199: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   202: astore          15
        //   204: iconst_0       
        //   205: istore          6
        //   207: iconst_m1      
        //   208: istore_1       
        //   209: aload           12
        //   211: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   214: astore          14
        //   216: iconst_0       
        //   217: istore_2       
        //   218: aload           14
        //   220: getstatic       dnsfilter/DNSFilterManager.DOWNLOADED_FF_PREFIX:Ljava/lang/String;
        //   223: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   226: istore          9
        //   228: iload           9
        //   230: ifeq            1916
        //   233: iconst_1       
        //   234: istore_2       
        //   235: new             Ljava/lang/StringBuilder;
        //   238: dup            
        //   239: invokespecial   java/lang/StringBuilder.<init>:()V
        //   242: astore          10
        //   244: aload           10
        //   246: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   252: pop            
        //   253: aload           10
        //   255: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   261: pop            
        //   262: aload           10
        //   264: ldc_w           ".DLD_CNT"
        //   267: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   270: pop            
        //   271: new             Ljava/io/File;
        //   274: dup            
        //   275: aload           10
        //   277: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   280: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   283: astore          10
        //   285: aload           10
        //   287: invokevirtual   java/io/File.exists:()Z
        //   290: ifeq            403
        //   293: new             Ljava/io/BufferedInputStream;
        //   296: dup            
        //   297: new             Ljava/io/FileInputStream;
        //   300: dup            
        //   301: aload           10
        //   303: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   306: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //   309: astore          10
        //   311: sipush          1024
        //   314: newarray        B
        //   316: astore          11
        //   318: aload           10
        //   320: aload           11
        //   322: iconst_1       
        //   323: iconst_1       
        //   324: invokestatic    util/Utils.readLineBytesFromStream:(Ljava/io/InputStream;[BZZ)I
        //   327: istore_1       
        //   328: new             Ljava/lang/String;
        //   331: dup            
        //   332: aload           11
        //   334: iconst_0       
        //   335: iload_1        
        //   336: invokespecial   java/lang/String.<init>:([BII)V
        //   339: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   342: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   345: istore_1       
        //   346: aload           10
        //   348: aload           11
        //   350: iconst_1       
        //   351: iconst_1       
        //   352: invokestatic    util/Utils.readLineBytesFromStream:(Ljava/io/InputStream;[BZZ)I
        //   355: istore_3       
        //   356: iload_3        
        //   357: iconst_m1      
        //   358: if_icmpeq       1911
        //   361: new             Ljava/lang/String;
        //   364: dup            
        //   365: aload           11
        //   367: iconst_0       
        //   368: iload_3        
        //   369: invokespecial   java/lang/String.<init>:([BII)V
        //   372: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   375: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   378: aload           20
        //   380: invokevirtual   java/io/File.lastModified:()J
        //   383: lcmp           
        //   384: ifeq            390
        //   387: goto            1911
        //   390: aload           10
        //   392: invokevirtual   java/io/InputStream.close:()V
        //   395: goto            403
        //   398: astore          10
        //   400: goto            408
        //   403: goto            459
        //   406: astore          10
        //   408: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   411: astore          11
        //   413: new             Ljava/lang/StringBuilder;
        //   416: dup            
        //   417: invokespecial   java/lang/StringBuilder.<init>:()V
        //   420: astore          17
        //   422: aload           17
        //   424: ldc_w           "Error retrieving Number of downloaded hosts\n"
        //   427: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   430: pop            
        //   431: aload           17
        //   433: aload           10
        //   435: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   438: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   441: pop            
        //   442: aload           11
        //   444: aload           17
        //   446: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   449: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   454: iconst_m1      
        //   455: istore_1       
        //   456: goto            459
        //   459: iload_1        
        //   460: iconst_m1      
        //   461: if_icmpne       489
        //   464: iconst_1       
        //   465: aload           20
        //   467: invokevirtual   java/io/File.length:()J
        //   470: aload           13
        //   472: invokevirtual   java/io/File.length:()J
        //   475: ladd           
        //   476: ldc2_w          30
        //   479: ldiv           
        //   480: l2i            
        //   481: invokestatic    java/lang/Math.max:(II)I
        //   484: istore          5
        //   486: goto            503
        //   489: iload_1        
        //   490: aload           13
        //   492: invokevirtual   java/io/File.length:()J
        //   495: ldc2_w          20
        //   498: ldiv           
        //   499: l2i            
        //   500: iadd           
        //   501: istore          5
        //   503: new             Ldnsfilter/BlockedHosts;
        //   506: dup            
        //   507: iload           5
        //   509: getstatic       dnsfilter/DNSFilterManager.okCacheSize:I
        //   512: getstatic       dnsfilter/DNSFilterManager.filterListCacheSize:I
        //   515: getstatic       dnsfilter/DNSFilterManager.hostsFilterOverRule:Ljava/util/Hashtable;
        //   518: invokespecial   dnsfilter/BlockedHosts.<init>:(IIILjava/util/Hashtable;)V
        //   521: astore          18
        //   523: aload           14
        //   525: astore          11
        //   527: iconst_0       
        //   528: istore          7
        //   530: aload           11
        //   532: astore          10
        //   534: iload           6
        //   536: istore_3       
        //   537: iload           7
        //   539: istore          4
        //   541: iload_2        
        //   542: ifeq            1919
        //   545: aload           11
        //   547: astore          10
        //   549: iload           6
        //   551: istore_3       
        //   552: iload           7
        //   554: istore          4
        //   556: iload_1        
        //   557: iconst_m1      
        //   558: if_icmpeq       1919
        //   561: aconst_null    
        //   562: astore          10
        //   564: iload_1        
        //   565: istore_3       
        //   566: iconst_1       
        //   567: istore          4
        //   569: goto            1919
        //   572: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //   575: ifne            1939
        //   578: aload           10
        //   580: ifnonnull       593
        //   583: aload           11
        //   585: aload           15
        //   587: if_acmpeq       1926
        //   590: goto            593
        //   593: aload           10
        //   595: ifnonnull       613
        //   598: aload           11
        //   600: invokevirtual   java/io/BufferedReader.close:()V
        //   603: aload           15
        //   605: astore          10
        //   607: iload_3        
        //   608: istore          6
        //   610: goto            667
        //   613: aload_0        
        //   614: aload           10
        //   616: invokespecial   dnsfilter/DNSFilterManager.parseHosts:(Ljava/lang/String;)[Ljava/lang/String;
        //   619: astore          12
        //   621: aload           12
        //   623: ifnull          1929
        //   626: aload           11
        //   628: astore          10
        //   630: iload_3        
        //   631: istore          6
        //   633: aload           12
        //   635: iconst_1       
        //   636: aaload         
        //   637: ldc_w           "localhost"
        //   640: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   643: ifne            667
        //   646: aload           18
        //   648: aload           12
        //   650: iconst_1       
        //   651: aaload         
        //   652: invokevirtual   dnsfilter/BlockedHosts.prepareInsert:(Ljava/lang/String;)V
        //   655: iload_3        
        //   656: iconst_1       
        //   657: iadd           
        //   658: istore          6
        //   660: aload           11
        //   662: astore          10
        //   664: goto            667
        //   667: aload           10
        //   669: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   672: astore          12
        //   674: aload           10
        //   676: astore          11
        //   678: aload           12
        //   680: astore          10
        //   682: iload           6
        //   684: istore_3       
        //   685: goto            572
        //   688: aload           10
        //   690: astore          12
        //   692: aload           11
        //   694: invokevirtual   java/io/BufferedReader.close:()V
        //   697: aload           11
        //   699: aload           15
        //   701: if_acmpeq       709
        //   704: aload           15
        //   706: invokevirtual   java/io/BufferedReader.close:()V
        //   709: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //   712: ifeq            751
        //   715: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   718: ldc_w           "Aborting Indexing!"
        //   721: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   726: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   729: ldc_w           "Indexing Aborted!"
        //   732: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   737: iconst_0       
        //   738: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //   741: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //   744: invokevirtual   java/lang/Object.notifyAll:()V
        //   747: aload           19
        //   749: monitorexit    
        //   750: return         
        //   751: iload           4
        //   753: ifne            764
        //   756: aload           18
        //   758: invokevirtual   dnsfilter/BlockedHosts.finalPrepare:()V
        //   761: goto            771
        //   764: aload           18
        //   766: iload           5
        //   768: invokevirtual   dnsfilter/BlockedHosts.finalPrepare:(I)V
        //   771: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   774: astore          10
        //   776: new             Ljava/lang/StringBuilder;
        //   779: dup            
        //   780: invokespecial   java/lang/StringBuilder.<init>:()V
        //   783: astore          11
        //   785: aload           11
        //   787: ldc_w           "Building index for "
        //   790: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   793: pop            
        //   794: aload           11
        //   796: iload_3        
        //   797: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   800: pop            
        //   801: aload           11
        //   803: ldc_w           " entries...!"
        //   806: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   809: pop            
        //   810: aload           10
        //   812: aload           11
        //   814: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   817: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   822: new             Ljava/io/BufferedReader;
        //   825: dup            
        //   826: new             Ljava/io/InputStreamReader;
        //   829: dup            
        //   830: new             Ljava/io/FileInputStream;
        //   833: dup            
        //   834: aload           20
        //   836: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   839: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   842: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   845: astore          15
        //   847: new             Ljava/io/BufferedReader;
        //   850: dup            
        //   851: new             Ljava/io/InputStreamReader;
        //   854: dup            
        //   855: new             Ljava/io/FileInputStream;
        //   858: dup            
        //   859: aload           13
        //   861: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   864: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   867: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   870: astore          17
        //   872: new             Ljava/lang/StringBuilder;
        //   875: dup            
        //   876: invokespecial   java/lang/StringBuilder.<init>:()V
        //   879: astore          10
        //   881: aload           10
        //   883: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   886: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   889: pop            
        //   890: aload           10
        //   892: ldc_w           "uniqueentries.tmp"
        //   895: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   898: pop            
        //   899: new             Ljava/io/File;
        //   902: dup            
        //   903: aload           10
        //   905: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   908: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   911: astore          21
        //   913: aconst_null    
        //   914: astore          10
        //   916: getstatic       dnsfilter/DNSFilterManager.filterHostsFileRemoveDuplicates:Z
        //   919: ifeq            1942
        //   922: new             Ljava/io/BufferedOutputStream;
        //   925: dup            
        //   926: new             Ljava/io/FileOutputStream;
        //   929: dup            
        //   930: aload           21
        //   932: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   935: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   938: astore          10
        //   940: new             Ljava/lang/StringBuilder;
        //   943: dup            
        //   944: invokespecial   java/lang/StringBuilder.<init>:()V
        //   947: astore          11
        //   949: aload           11
        //   951: aload           14
        //   953: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   956: pop            
        //   957: aload           11
        //   959: ldc_w           "\n"
        //   962: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   965: pop            
        //   966: aload           10
        //   968: aload           11
        //   970: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   973: invokevirtual   java/lang/String.getBytes:()[B
        //   976: invokevirtual   java/io/BufferedOutputStream.write:([B)V
        //   979: aload           10
        //   981: astore          14
        //   983: goto            986
        //   986: iload_2        
        //   987: ifeq            1949
        //   990: aload           15
        //   992: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   995: pop            
        //   996: goto            1949
        //   999: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //  1002: ifne            2009
        //  1005: aload           15
        //  1007: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //  1010: astore          12
        //  1012: aload           12
        //  1014: ifnonnull       1027
        //  1017: aload           15
        //  1019: aload           17
        //  1021: if_acmpeq       1979
        //  1024: goto            1027
        //  1027: aload           12
        //  1029: ifnonnull       1982
        //  1032: aload           15
        //  1034: invokevirtual   java/io/BufferedReader.close:()V
        //  1037: aload           17
        //  1039: astore          15
        //  1041: iload_1        
        //  1042: istore          5
        //  1044: goto            999
        //  1047: iconst_2       
        //  1048: anewarray       Ljava/lang/String;
        //  1051: astore          16
        //  1053: aload           16
        //  1055: iconst_0       
        //  1056: ldc             ""
        //  1058: aastore        
        //  1059: aload           16
        //  1061: iconst_1       
        //  1062: aload           12
        //  1064: aastore        
        //  1065: goto            1076
        //  1068: aload_0        
        //  1069: aload           12
        //  1071: invokespecial   dnsfilter/DNSFilterManager.parseHosts:(Ljava/lang/String;)[Ljava/lang/String;
        //  1074: astore          16
        //  1076: aload           16
        //  1078: ifnull          2006
        //  1081: aload           16
        //  1083: iconst_1       
        //  1084: aaload         
        //  1085: ldc_w           "localhost"
        //  1088: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1091: ifne            2003
        //  1094: aload           11
        //  1096: aload           16
        //  1098: iconst_1       
        //  1099: aaload         
        //  1100: invokevirtual   dnsfilter/BlockedHosts.add:(Ljava/lang/Object;)Z
        //  1103: ifne            1111
        //  1106: iload_1        
        //  1107: istore_2       
        //  1108: goto            1172
        //  1111: iload_1        
        //  1112: iconst_1       
        //  1113: iadd           
        //  1114: istore_2       
        //  1115: aload           15
        //  1117: aload           17
        //  1119: if_acmpeq       2000
        //  1122: getstatic       dnsfilter/DNSFilterManager.filterHostsFileRemoveDuplicates:Z
        //  1125: ifeq            2000
        //  1128: new             Ljava/lang/StringBuilder;
        //  1131: dup            
        //  1132: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1135: astore          18
        //  1137: aload           18
        //  1139: aload           16
        //  1141: iconst_1       
        //  1142: aaload         
        //  1143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1146: pop            
        //  1147: aload           18
        //  1149: ldc_w           "\n"
        //  1152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1155: pop            
        //  1156: aload           14
        //  1158: aload           18
        //  1160: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1163: invokevirtual   java/lang/String.getBytes:()[B
        //  1166: invokevirtual   java/io/BufferedOutputStream.write:([B)V
        //  1169: goto            1172
        //  1172: iload           6
        //  1174: iconst_1       
        //  1175: iadd           
        //  1176: istore          8
        //  1178: iload           8
        //  1180: istore          6
        //  1182: iload_2        
        //  1183: istore_1       
        //  1184: iload           8
        //  1186: sipush          10000
        //  1189: irem           
        //  1190: ifne            2006
        //  1193: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1196: astore          16
        //  1198: new             Ljava/lang/StringBuilder;
        //  1201: dup            
        //  1202: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1205: astore          18
        //  1207: aload           18
        //  1209: ldc_w           "Building index for "
        //  1212: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1215: pop            
        //  1216: aload           18
        //  1218: iload           8
        //  1220: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1223: pop            
        //  1224: aload           18
        //  1226: ldc_w           "/"
        //  1229: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1232: pop            
        //  1233: aload           18
        //  1235: iload_3        
        //  1236: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1239: pop            
        //  1240: aload           18
        //  1242: ldc_w           " entries completed!"
        //  1245: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1248: pop            
        //  1249: aload           16
        //  1251: aload           18
        //  1253: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1256: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //  1261: iload           8
        //  1263: istore          6
        //  1265: iload_2        
        //  1266: istore_1       
        //  1267: goto            2006
        //  1270: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1273: astore          12
        //  1275: new             Ljava/lang/StringBuilder;
        //  1278: dup            
        //  1279: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1282: astore          16
        //  1284: aload           16
        //  1286: ldc_w           "Building index for "
        //  1289: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1292: pop            
        //  1293: aload           16
        //  1295: iload           6
        //  1297: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1300: pop            
        //  1301: aload           16
        //  1303: ldc_w           "/"
        //  1306: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1309: pop            
        //  1310: aload           16
        //  1312: iload_3        
        //  1313: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1316: pop            
        //  1317: aload           16
        //  1319: ldc_w           " entries completed!"
        //  1322: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1325: pop            
        //  1326: aload           12
        //  1328: aload           16
        //  1330: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1333: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //  1338: aload           15
        //  1340: invokevirtual   java/io/BufferedReader.close:()V
        //  1343: aload           15
        //  1345: aload           17
        //  1347: if_acmpeq       1355
        //  1350: aload           17
        //  1352: invokevirtual   java/io/BufferedReader.close:()V
        //  1355: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //  1358: ifeq            1397
        //  1361: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1364: ldc_w           "Indexing Aborted!"
        //  1367: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //  1372: getstatic       dnsfilter/DNSFilterManager.filterHostsFileRemoveDuplicates:Z
        //  1375: ifeq            1383
        //  1378: aload           14
        //  1380: invokevirtual   java/io/BufferedOutputStream.close:()V
        //  1383: iconst_0       
        //  1384: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //  1387: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //  1390: invokevirtual   java/lang/Object.notifyAll:()V
        //  1393: aload           19
        //  1395: monitorexit    
        //  1396: return         
        //  1397: getstatic       dnsfilter/DNSFilterManager.filterHostsFileRemoveDuplicates:Z
        //  1400: ifeq            2012
        //  1403: aload           14
        //  1405: invokevirtual   java/io/BufferedOutputStream.flush:()V
        //  1408: aload           14
        //  1410: invokevirtual   java/io/BufferedOutputStream.close:()V
        //  1413: aload           20
        //  1415: invokevirtual   java/io/File.delete:()Z
        //  1418: pop            
        //  1419: aload           21
        //  1421: aload           20
        //  1423: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //  1426: pop            
        //  1427: iload           4
        //  1429: ifeq            2012
        //  1432: new             Ljava/lang/StringBuilder;
        //  1435: dup            
        //  1436: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1439: astore          12
        //  1441: aload           12
        //  1443: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //  1446: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1449: pop            
        //  1450: aload           12
        //  1452: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //  1455: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1458: pop            
        //  1459: aload_0        
        //  1460: iload           5
        //  1462: new             Ljava/io/File;
        //  1465: dup            
        //  1466: aload           12
        //  1468: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1471: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  1474: invokevirtual   java/io/File.lastModified:()J
        //  1477: invokespecial   dnsfilter/DNSFilterManager.writeDownloadInfoFile:(IJ)V
        //  1480: goto            1483
        //  1483: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1486: astore          12
        //  1488: aload           12
        //  1490: ifnull          1498
        //  1493: iconst_1       
        //  1494: istore_2       
        //  1495: goto            1500
        //  1498: iconst_0       
        //  1499: istore_2       
        //  1500: iload_2        
        //  1501: ifeq            1519
        //  1504: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1507: iconst_1       
        //  1508: invokevirtual   dnsfilter/BlockedHosts.lock:(I)V
        //  1511: goto            1519
        //  1514: astore          10
        //  1516: goto            1874
        //  1519: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1522: astore          12
        //  1524: new             Ljava/lang/StringBuilder;
        //  1527: dup            
        //  1528: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1531: astore          14
        //  1533: aload           14
        //  1535: ldc_w           "Persisting index for "
        //  1538: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1541: pop            
        //  1542: aload           14
        //  1544: iload_3        
        //  1545: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1548: pop            
        //  1549: aload           14
        //  1551: ldc_w           " entries...!"
        //  1554: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1557: pop            
        //  1558: aload           12
        //  1560: aload           14
        //  1562: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1565: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //  1570: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1573: astore          12
        //  1575: new             Ljava/lang/StringBuilder;
        //  1578: dup            
        //  1579: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1582: astore          14
        //  1584: aload           14
        //  1586: ldc_w           "Index contains "
        //  1589: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1592: pop            
        //  1593: aload           14
        //  1595: iload_1        
        //  1596: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1599: pop            
        //  1600: aload           14
        //  1602: ldc_w           " unique entries!"
        //  1605: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1608: pop            
        //  1609: aload           12
        //  1611: aload           14
        //  1613: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1616: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //  1621: new             Ljava/lang/StringBuilder;
        //  1624: dup            
        //  1625: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1628: astore          12
        //  1630: aload           12
        //  1632: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //  1635: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1638: pop            
        //  1639: aload           12
        //  1641: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //  1644: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1647: pop            
        //  1648: aload           12
        //  1650: ldc_w           ".idx"
        //  1653: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1656: pop            
        //  1657: aload           12
        //  1659: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1662: astore          12
        //  1664: aload           11
        //  1666: aload           12
        //  1668: invokevirtual   dnsfilter/BlockedHosts.persist:(Ljava/lang/String;)V
        //  1671: aload           11
        //  1673: invokevirtual   dnsfilter/BlockedHosts.clear:()V
        //  1676: aload           13
        //  1678: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //  1681: astore          11
        //  1683: getstatic       dnsfilter/DNSFilterManager.okCacheSize:I
        //  1686: istore_1       
        //  1687: getstatic       dnsfilter/DNSFilterManager.filterListCacheSize:I
        //  1690: istore_3       
        //  1691: getstatic       dnsfilter/DNSFilterManager.hostsFilterOverRule:Ljava/util/Hashtable;
        //  1694: astore          12
        //  1696: aload           11
        //  1698: iconst_0       
        //  1699: iload_1        
        //  1700: iload_3        
        //  1701: aload           12
        //  1703: invokestatic    dnsfilter/BlockedHosts.loadPersistedIndex:(Ljava/lang/String;ZIILjava/util/Hashtable;)Ldnsfilter/BlockedHosts;
        //  1706: astore          11
        //  1708: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1711: astore          12
        //  1713: aload           12
        //  1715: ifnull          1734
        //  1718: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1721: aload           11
        //  1723: invokevirtual   dnsfilter/BlockedHosts.migrateTo:(Ldnsfilter/BlockedHosts;)V
        //  1726: goto            1748
        //  1729: astore          10
        //  1731: goto            1874
        //  1734: aload           11
        //  1736: putstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1739: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1742: getstatic       dnsfilter/DNSFilterManager.TRAFFIC_LOG:Lutil/LoggerInterface;
        //  1745: invokestatic    dnsfilter/DNSResponsePatcher.init:(Ljava/util/Set;Lutil/LoggerInterface;)V
        //  1748: iload_2        
        //  1749: ifeq            2015
        //  1752: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1755: iconst_1       
        //  1756: invokevirtual   dnsfilter/BlockedHosts.unLock:(I)V
        //  1759: goto            1762
        //  1762: iconst_1       
        //  1763: putstatic       dnsfilter/DNSFilterManager.validIndex:Z
        //  1766: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //  1769: ldc_w           "Processing new filter file completed!"
        //  1772: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //  1777: new             Ljava/lang/StringBuilder;
        //  1780: dup            
        //  1781: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1784: astore          11
        //  1786: aload           11
        //  1788: ldc             ""
        //  1790: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1793: pop            
        //  1794: aload           11
        //  1796: aload           10
        //  1798: invokevirtual   java/io/File.lastModified:()J
        //  1801: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  1804: pop            
        //  1805: aload           11
        //  1807: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1810: putstatic       dnsfilter/DNSFilterManager.additionalHostsImportTS:Ljava/lang/String;
        //  1813: aload_0        
        //  1814: getstatic       dnsfilter/DNSFilterManager.filterReloadURL:Ljava/lang/String;
        //  1817: getstatic       dnsfilter/DNSFilterManager.additionalHostsImportTS:Ljava/lang/String;
        //  1820: invokespecial   dnsfilter/DNSFilterManager.updateIndexReloadInfoConfFile:(Ljava/lang/String;Ljava/lang/String;)V
        //  1823: iconst_0       
        //  1824: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //  1827: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //  1830: invokevirtual   java/lang/Object.notifyAll:()V
        //  1833: aload           19
        //  1835: monitorexit    
        //  1836: return         
        //  1837: astore          10
        //  1839: goto            1844
        //  1842: astore          10
        //  1844: goto            1874
        //  1847: astore          10
        //  1849: goto            1874
        //  1852: astore          10
        //  1854: goto            1874
        //  1857: astore          10
        //  1859: goto            1874
        //  1862: astore          10
        //  1864: goto            1874
        //  1867: astore          10
        //  1869: goto            1874
        //  1872: astore          10
        //  1874: iload_2        
        //  1875: ifeq            1885
        //  1878: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //  1881: iconst_1       
        //  1882: invokevirtual   dnsfilter/BlockedHosts.unLock:(I)V
        //  1885: aload           10
        //  1887: athrow         
        //  1888: astore          10
        //  1890: iconst_0       
        //  1891: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //  1894: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //  1897: invokevirtual   java/lang/Object.notifyAll:()V
        //  1900: aload           10
        //  1902: athrow         
        //  1903: astore          10
        //  1905: aload           19
        //  1907: monitorexit    
        //  1908: aload           10
        //  1910: athrow         
        //  1911: iconst_m1      
        //  1912: istore_1       
        //  1913: goto            390
        //  1916: goto            459
        //  1919: aload           12
        //  1921: astore          11
        //  1923: goto            572
        //  1926: goto            688
        //  1929: aload           11
        //  1931: astore          10
        //  1933: iload_3        
        //  1934: istore          6
        //  1936: goto            667
        //  1939: goto            688
        //  1942: aload           10
        //  1944: astore          14
        //  1946: goto            986
        //  1949: iconst_0       
        //  1950: istore          8
        //  1952: iconst_0       
        //  1953: istore          6
        //  1955: aload           18
        //  1957: astore          11
        //  1959: aload           13
        //  1961: astore          10
        //  1963: aload           16
        //  1965: astore          13
        //  1967: iload_1        
        //  1968: istore          5
        //  1970: iload_2        
        //  1971: istore          7
        //  1973: iload           8
        //  1975: istore_1       
        //  1976: goto            999
        //  1979: goto            1270
        //  1982: iload           7
        //  1984: ifeq            1997
        //  1987: aload           15
        //  1989: aload           17
        //  1991: if_acmpne       1047
        //  1994: goto            1068
        //  1997: goto            1068
        //  2000: goto            1172
        //  2003: goto            2006
        //  2006: goto            999
        //  2009: goto            1270
        //  2012: goto            1483
        //  2015: goto            1762
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  8      204    1888   1903   Any
        //  209    216    1888   1903   Any
        //  218    228    1888   1903   Any
        //  235    328    406    408    Ljava/lang/Exception;
        //  235    328    1888   1903   Any
        //  328    356    398    403    Ljava/lang/Exception;
        //  328    356    1888   1903   Any
        //  361    387    398    403    Ljava/lang/Exception;
        //  361    387    1888   1903   Any
        //  390    395    398    403    Ljava/lang/Exception;
        //  390    395    1888   1903   Any
        //  408    454    1888   1903   Any
        //  464    486    1888   1903   Any
        //  489    503    1888   1903   Any
        //  503    523    1888   1903   Any
        //  572    578    1888   1903   Any
        //  598    603    1888   1903   Any
        //  613    621    1888   1903   Any
        //  633    655    1888   1903   Any
        //  667    674    1888   1903   Any
        //  692    697    1888   1903   Any
        //  704    709    1888   1903   Any
        //  709    737    1888   1903   Any
        //  737    750    1903   1911   Any
        //  756    761    1888   1903   Any
        //  764    771    1888   1903   Any
        //  771    913    1888   1903   Any
        //  916    979    1888   1903   Any
        //  990    996    1888   1903   Any
        //  999    1012   1888   1903   Any
        //  1032   1037   1888   1903   Any
        //  1047   1053   1888   1903   Any
        //  1068   1076   1888   1903   Any
        //  1081   1106   1888   1903   Any
        //  1122   1169   1888   1903   Any
        //  1193   1261   1888   1903   Any
        //  1270   1343   1888   1903   Any
        //  1350   1355   1888   1903   Any
        //  1355   1383   1888   1903   Any
        //  1383   1396   1903   1911   Any
        //  1397   1427   1888   1903   Any
        //  1432   1480   1888   1903   Any
        //  1483   1488   1888   1903   Any
        //  1504   1511   1514   1519   Any
        //  1519   1664   1872   1874   Any
        //  1664   1676   1867   1872   Any
        //  1676   1683   1862   1867   Any
        //  1683   1687   1857   1862   Any
        //  1687   1691   1852   1857   Any
        //  1691   1696   1847   1852   Any
        //  1696   1708   1842   1844   Any
        //  1708   1713   1837   1842   Any
        //  1718   1726   1729   1734   Any
        //  1734   1748   1837   1842   Any
        //  1752   1759   1888   1903   Any
        //  1762   1823   1888   1903   Any
        //  1823   1836   1903   1911   Any
        //  1878   1885   1888   1903   Any
        //  1885   1888   1888   1903   Any
        //  1890   1903   1903   1911   Any
        //  1905   1908   1903   1911   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0751:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void reloadFilter(final boolean b) throws IOException {
        try {
            ExecutionEnvironment.getEnvironment().wakeLock();
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append(DNSFilterManager.filterhostfile);
            final File file = new File(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(DNSFilterManager.WORKDIR);
            sb2.append(DNSFilterManager.filterhostfile);
            sb2.append(".DLD_CNT");
            final File file2 = new File(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(DNSFilterManager.WORKDIR);
            sb3.append("additionalHosts.txt");
            final File file3 = new File(sb3.toString());
            if (!file3.exists()) {
                file3.createNewFile();
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(file3.lastModified());
            final boolean equals = sb4.toString().equals(DNSFilterManager.additionalHostsImportTS);
            if (file.exists() && file2.exists() && !DNSFilterManager.reloadUrlChanged) {
                DNSFilterManager.nextReload = DNSFilterManager.filterReloadIntervalDays * 24L * 60L * 60L * 1000L + file2.lastModified();
            }
            else {
                DNSFilterManager.nextReload = 0L;
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(DNSFilterManager.WORKDIR);
            sb5.append(DNSFilterManager.filterhostfile);
            sb5.append(".idx");
            final File file4 = new File(sb5.toString());
            if (file4.exists() && DNSFilterManager.validIndex && BlockedHosts.checkIndexVersion(file4.getAbsolutePath())) {
                DNSFilterManager.hostFilter = BlockedHosts.loadPersistedIndex(file4.getAbsolutePath(), false, DNSFilterManager.okCacheSize, DNSFilterManager.filterListCacheSize, DNSFilterManager.hostsFilterOverRule);
                if ((equals ^ true) && file.exists() && DNSFilterManager.nextReload != 0L) {
                    new Thread(new AsyncIndexBuilder()).start();
                }
            }
            else if (file.exists() && DNSFilterManager.nextReload != 0L) {
                if (!b) {
                    this.rebuildIndex();
                }
                else {
                    new Thread(new AsyncIndexBuilder()).start();
                }
            }
        }
        finally {
            ExecutionEnvironment.getEnvironment().releaseWakeLock();
        }
    }
    
    private boolean updateFilter() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          12
        //     5: aload           12
        //     7: monitorenter   
        //     8: iconst_1       
        //     9: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //    12: invokestatic    util/ExecutionEnvironment.getEnvironment:()Lutil/ExecutionEnvironmentInterface;
        //    15: invokeinterface util/ExecutionEnvironmentInterface.wakeLock:()V
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: invokespecial   java/lang/StringBuilder.<init>:()V
        //    27: astore          9
        //    29: aload           9
        //    31: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    37: pop            
        //    38: aload           9
        //    40: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: pop            
        //    47: aload           9
        //    49: ldc_w           ".tmp"
        //    52: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    55: pop            
        //    56: new             Ljava/io/FileOutputStream;
        //    59: dup            
        //    60: aload           9
        //    62: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    65: invokespecial   java/io/FileOutputStream.<init>:(Ljava/lang/String;)V
        //    68: astore          13
        //    70: new             Ljava/lang/StringBuilder;
        //    73: dup            
        //    74: invokespecial   java/lang/StringBuilder.<init>:()V
        //    77: astore          9
        //    79: aload           9
        //    81: getstatic       dnsfilter/DNSFilterManager.DOWNLOADED_FF_PREFIX:Ljava/lang/String;
        //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    87: pop            
        //    88: aload           9
        //    90: new             Ljava/util/Date;
        //    93: dup            
        //    94: invokespecial   java/util/Date.<init>:()V
        //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   100: pop            
        //   101: aload           9
        //   103: ldc_w           "from URLs: "
        //   106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   109: pop            
        //   110: aload           9
        //   112: getstatic       dnsfilter/DNSFilterManager.filterReloadURL:Ljava/lang/String;
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   118: pop            
        //   119: aload           9
        //   121: ldc_w           "\n"
        //   124: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   127: pop            
        //   128: aload           13
        //   130: aload           9
        //   132: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   135: invokevirtual   java/lang/String.getBytes:()[B
        //   138: invokevirtual   java/io/OutputStream.write:([B)V
        //   141: new             Ljava/util/StringTokenizer;
        //   144: dup            
        //   145: getstatic       dnsfilter/DNSFilterManager.filterReloadURL:Ljava/lang/String;
        //   148: ldc_w           ";"
        //   151: invokespecial   java/util/StringTokenizer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   154: astore          9
        //   156: aload           9
        //   158: invokevirtual   java/util/StringTokenizer.countTokens:()I
        //   161: istore          6
        //   163: iconst_0       
        //   164: istore_1       
        //   165: iconst_0       
        //   166: istore_2       
        //   167: iload_2        
        //   168: iload           6
        //   170: if_icmpge       841
        //   173: aload           9
        //   175: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   178: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   181: astore          14
        //   183: aload           14
        //   185: ldc             ""
        //   187: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   190: ifne            771
        //   193: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   196: astore          10
        //   198: new             Ljava/lang/StringBuilder;
        //   201: dup            
        //   202: invokespecial   java/lang/StringBuilder.<init>:()V
        //   205: astore          11
        //   207: aload           11
        //   209: ldc_w           "Connecting: "
        //   212: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   215: pop            
        //   216: aload           11
        //   218: aload           14
        //   220: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   223: pop            
        //   224: aload           10
        //   226: aload           11
        //   228: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   231: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   236: aload           14
        //   238: ldc_w           "file://"
        //   241: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   244: istore          8
        //   246: iload           8
        //   248: ifne            379
        //   251: new             Ljava/net/URL;
        //   254: dup            
        //   255: aload           14
        //   257: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   260: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   263: astore          10
        //   265: new             Ljava/lang/StringBuilder;
        //   268: dup            
        //   269: invokespecial   java/lang/StringBuilder.<init>:()V
        //   272: astore          11
        //   274: aload           11
        //   276: ldc_w           "Mozilla/5.0 ("
        //   279: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   282: pop            
        //   283: aload           11
        //   285: ldc_w           "os.name"
        //   288: invokestatic    java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   291: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   294: pop            
        //   295: aload           11
        //   297: ldc_w           "; "
        //   300: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   303: pop            
        //   304: aload           11
        //   306: ldc_w           "os.version"
        //   309: invokestatic    java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   312: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   315: pop            
        //   316: aload           11
        //   318: ldc_w           ")"
        //   321: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   324: pop            
        //   325: aload           10
        //   327: ldc_w           "User-Agent"
        //   330: aload           11
        //   332: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   335: invokevirtual   java/net/URLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   338: aload           10
        //   340: ldc_w           120000
        //   343: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
        //   346: aload           10
        //   348: ldc_w           120000
        //   351: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
        //   354: new             Ljava/io/BufferedInputStream;
        //   357: dup            
        //   358: aload           10
        //   360: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //   363: sipush          2048
        //   366: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;I)V
        //   369: astore          10
        //   371: goto            405
        //   374: astore          9
        //   376: goto            780
        //   379: new             Ljava/io/BufferedInputStream;
        //   382: dup            
        //   383: new             Ljava/io/FileInputStream;
        //   386: dup            
        //   387: aload           14
        //   389: bipush          7
        //   391: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   394: invokespecial   java/io/FileInputStream.<init>:(Ljava/lang/String;)V
        //   397: sipush          2048
        //   400: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;I)V
        //   403: astore          10
        //   405: sipush          2048
        //   408: newarray        B
        //   410: astore          11
        //   412: iconst_0       
        //   413: istore_3       
        //   414: iconst_0       
        //   415: istore          5
        //   417: ldc_w           100000
        //   420: istore          4
        //   422: aload_0        
        //   423: aload           10
        //   425: aload           11
        //   427: invokevirtual   dnsfilter/DNSFilterManager.readHostFileEntry:(Ljava/io/InputStream;[B)[I
        //   430: astore          15
        //   432: aload           15
        //   434: iconst_1       
        //   435: iaload         
        //   436: iconst_m1      
        //   437: if_icmpeq       618
        //   440: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //   443: ifne            618
        //   446: aload           15
        //   448: iconst_1       
        //   449: iaload         
        //   450: ifeq            422
        //   453: aload           15
        //   455: iconst_1       
        //   456: iaload         
        //   457: istore          7
        //   459: new             Ljava/lang/String;
        //   462: dup            
        //   463: aload           11
        //   465: iconst_0       
        //   466: iload           7
        //   468: invokespecial   java/lang/String.<init>:([BII)V
        //   471: astore          16
        //   473: aload           16
        //   475: ifnull          1142
        //   478: aload           16
        //   480: ldc_w           "localhost"
        //   483: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   486: ifne            1142
        //   489: aload           15
        //   491: iconst_0       
        //   492: iaload         
        //   493: iconst_1       
        //   494: if_icmpne       506
        //   497: iload           5
        //   499: iconst_1       
        //   500: iadd           
        //   501: istore          5
        //   503: goto            552
        //   506: new             Ljava/lang/StringBuilder;
        //   509: dup            
        //   510: invokespecial   java/lang/StringBuilder.<init>:()V
        //   513: astore          17
        //   515: aload           17
        //   517: aload           16
        //   519: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   522: pop            
        //   523: aload           17
        //   525: ldc_w           "\n"
        //   528: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   531: pop            
        //   532: aload           13
        //   534: aload           17
        //   536: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   539: invokevirtual   java/lang/String.getBytes:()[B
        //   542: invokevirtual   java/io/OutputStream.write:([B)V
        //   545: iload_1        
        //   546: iconst_1       
        //   547: iadd           
        //   548: istore_1       
        //   549: goto            552
        //   552: iload_3        
        //   553: aload           15
        //   555: iconst_1       
        //   556: iaload         
        //   557: iadd           
        //   558: istore_3       
        //   559: iload_3        
        //   560: iload           4
        //   562: if_icmple       1145
        //   565: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   568: astore          15
        //   570: new             Ljava/lang/StringBuilder;
        //   573: dup            
        //   574: invokespecial   java/lang/StringBuilder.<init>:()V
        //   577: astore          16
        //   579: aload           16
        //   581: ldc_w           "Loading Filter - Bytes received:"
        //   584: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   587: pop            
        //   588: aload           16
        //   590: iload_3        
        //   591: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   594: pop            
        //   595: aload           15
        //   597: aload           16
        //   599: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   602: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   607: iload           4
        //   609: ldc_w           100000
        //   612: iadd           
        //   613: istore          4
        //   615: goto            1145
        //   618: aload           10
        //   620: invokevirtual   java/io/InputStream.close:()V
        //   623: getstatic       dnsfilter/DNSFilterManager.aborted:Z
        //   626: ifeq            684
        //   629: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   632: ldc_w           "Aborting Filter Update!"
        //   635: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   640: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   643: ldc_w           "Filter Update aborted!"
        //   646: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   651: aload           13
        //   653: invokevirtual   java/io/OutputStream.flush:()V
        //   656: aload           13
        //   658: invokevirtual   java/io/OutputStream.close:()V
        //   661: invokestatic    util/ExecutionEnvironment.getEnvironment:()Lutil/ExecutionEnvironmentInterface;
        //   664: invokeinterface util/ExecutionEnvironmentInterface.releaseWakeLock:()V
        //   669: iconst_0       
        //   670: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //   673: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //   676: invokevirtual   java/lang/Object.notifyAll:()V
        //   679: aload           12
        //   681: monitorexit    
        //   682: iconst_0       
        //   683: ireturn        
        //   684: iload           5
        //   686: ifeq            758
        //   689: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   692: astore          10
        //   694: new             Ljava/lang/StringBuilder;
        //   697: dup            
        //   698: invokespecial   java/lang/StringBuilder.<init>:()V
        //   701: astore          11
        //   703: aload           11
        //   705: ldc_w           "WARNING! - "
        //   708: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   711: pop            
        //   712: aload           11
        //   714: iload           5
        //   716: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   719: pop            
        //   720: aload           11
        //   722: ldc_w           " skipped entrie(s) for "
        //   725: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   728: pop            
        //   729: aload           11
        //   731: aload           14
        //   733: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   736: pop            
        //   737: aload           11
        //   739: ldc_w           "! Wildcards are only supported in additionalHosts.txt!"
        //   742: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   745: pop            
        //   746: aload           10
        //   748: aload           11
        //   750: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   753: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   758: goto            771
        //   761: astore          9
        //   763: goto            780
        //   766: astore          9
        //   768: goto            780
        //   771: iload_2        
        //   772: iconst_1       
        //   773: iadd           
        //   774: istore_2       
        //   775: goto            167
        //   778: astore          9
        //   780: new             Ljava/lang/StringBuilder;
        //   783: dup            
        //   784: invokespecial   java/lang/StringBuilder.<init>:()V
        //   787: astore          10
        //   789: aload           10
        //   791: ldc_w           "ERROR loading filter: "
        //   794: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   797: pop            
        //   798: aload           10
        //   800: aload           14
        //   802: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   805: pop            
        //   806: aload           10
        //   808: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   811: astore          10
        //   813: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   816: aload           10
        //   818: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   823: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   826: aload           10
        //   828: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   833: aload           13
        //   835: invokevirtual   java/io/OutputStream.close:()V
        //   838: aload           9
        //   840: athrow         
        //   841: aload_0        
        //   842: getstatic       dnsfilter/DNSFilterManager.filterReloadURL:Ljava/lang/String;
        //   845: ldc             "0"
        //   847: invokespecial   dnsfilter/DNSFilterManager.updateIndexReloadInfoConfFile:(Ljava/lang/String;Ljava/lang/String;)V
        //   850: iconst_0       
        //   851: putstatic       dnsfilter/DNSFilterManager.reloadUrlChanged:Z
        //   854: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   857: ldc_w           "Updating filter completed!"
        //   860: invokeinterface util/LoggerInterface.logLine:(Ljava/lang/String;)V
        //   865: aload           13
        //   867: invokevirtual   java/io/OutputStream.flush:()V
        //   870: aload           13
        //   872: invokevirtual   java/io/OutputStream.close:()V
        //   875: new             Ljava/lang/StringBuilder;
        //   878: dup            
        //   879: invokespecial   java/lang/StringBuilder.<init>:()V
        //   882: astore          9
        //   884: aload           9
        //   886: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   889: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   892: pop            
        //   893: aload           9
        //   895: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //   898: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   901: pop            
        //   902: new             Ljava/io/File;
        //   905: dup            
        //   906: aload           9
        //   908: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   911: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   914: astore          9
        //   916: aload           9
        //   918: invokevirtual   java/io/File.exists:()Z
        //   921: ifeq            946
        //   924: aload           9
        //   926: invokevirtual   java/io/File.delete:()Z
        //   929: ifeq            935
        //   932: goto            946
        //   935: new             Ljava/io/IOException;
        //   938: dup            
        //   939: ldc_w           "Renaming downloaded .tmp file to Filter file failed!"
        //   942: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   945: athrow         
        //   946: new             Ljava/lang/StringBuilder;
        //   949: dup            
        //   950: invokespecial   java/lang/StringBuilder.<init>:()V
        //   953: astore          9
        //   955: aload           9
        //   957: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   960: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   963: pop            
        //   964: aload           9
        //   966: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //   969: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   972: pop            
        //   973: aload           9
        //   975: ldc_w           ".tmp"
        //   978: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   981: pop            
        //   982: new             Ljava/io/File;
        //   985: dup            
        //   986: aload           9
        //   988: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   991: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   994: astore          9
        //   996: new             Ljava/lang/StringBuilder;
        //   999: dup            
        //  1000: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1003: astore          10
        //  1005: aload           10
        //  1007: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //  1010: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1013: pop            
        //  1014: aload           10
        //  1016: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //  1019: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1022: pop            
        //  1023: aload           9
        //  1025: new             Ljava/io/File;
        //  1028: dup            
        //  1029: aload           10
        //  1031: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1034: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  1037: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //  1040: pop            
        //  1041: new             Ljava/lang/StringBuilder;
        //  1044: dup            
        //  1045: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1048: astore          9
        //  1050: aload           9
        //  1052: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //  1055: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1058: pop            
        //  1059: aload           9
        //  1061: getstatic       dnsfilter/DNSFilterManager.filterhostfile:Ljava/lang/String;
        //  1064: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1067: pop            
        //  1068: aload_0        
        //  1069: iload_1        
        //  1070: new             Ljava/io/File;
        //  1073: dup            
        //  1074: aload           9
        //  1076: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1079: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  1082: invokevirtual   java/io/File.lastModified:()J
        //  1085: invokespecial   dnsfilter/DNSFilterManager.writeDownloadInfoFile:(IJ)V
        //  1088: invokestatic    util/ExecutionEnvironment.getEnvironment:()Lutil/ExecutionEnvironmentInterface;
        //  1091: invokeinterface util/ExecutionEnvironmentInterface.releaseWakeLock:()V
        //  1096: iconst_0       
        //  1097: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //  1100: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //  1103: invokevirtual   java/lang/Object.notifyAll:()V
        //  1106: aload           12
        //  1108: monitorexit    
        //  1109: iconst_1       
        //  1110: ireturn        
        //  1111: astore          9
        //  1113: invokestatic    util/ExecutionEnvironment.getEnvironment:()Lutil/ExecutionEnvironmentInterface;
        //  1116: invokeinterface util/ExecutionEnvironmentInterface.releaseWakeLock:()V
        //  1121: iconst_0       
        //  1122: putstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //  1125: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //  1128: invokevirtual   java/lang/Object.notifyAll:()V
        //  1131: aload           9
        //  1133: athrow         
        //  1134: astore          9
        //  1136: aload           12
        //  1138: monitorexit    
        //  1139: aload           9
        //  1141: athrow         
        //  1142: goto            552
        //  1145: goto            422
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  8      163    1111   1134   Any
        //  173    183    1111   1134   Any
        //  183    246    778    780    Ljava/io/IOException;
        //  183    246    1111   1134   Any
        //  251    371    374    379    Ljava/io/IOException;
        //  251    371    1111   1134   Any
        //  379    405    778    780    Ljava/io/IOException;
        //  379    405    1111   1134   Any
        //  405    412    778    780    Ljava/io/IOException;
        //  405    412    1111   1134   Any
        //  422    432    766    771    Ljava/io/IOException;
        //  422    432    1111   1134   Any
        //  440    446    766    771    Ljava/io/IOException;
        //  440    446    1111   1134   Any
        //  459    473    761    766    Ljava/io/IOException;
        //  459    473    1111   1134   Any
        //  478    489    761    766    Ljava/io/IOException;
        //  478    489    1111   1134   Any
        //  506    545    761    766    Ljava/io/IOException;
        //  506    545    1111   1134   Any
        //  565    607    761    766    Ljava/io/IOException;
        //  565    607    1111   1134   Any
        //  618    661    761    766    Ljava/io/IOException;
        //  618    661    1111   1134   Any
        //  661    682    1134   1142   Any
        //  689    758    761    766    Ljava/io/IOException;
        //  689    758    1111   1134   Any
        //  780    841    1111   1134   Any
        //  841    932    1111   1134   Any
        //  935    946    1111   1134   Any
        //  946    1088   1111   1134   Any
        //  1088   1109   1134   1142   Any
        //  1113   1134   1134   1142   Any
        //  1136   1139   1134   1142   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0684:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void updateIndexReloadInfoConfFile(final String s, final String s2) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append("dnsfilter.conf");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sb.toString())));
            int n = 0;
            int n2 = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int n3;
                String s3;
                int n4;
                if (s != null && line.startsWith("previousAutoUpdateURL")) {
                    n3 = 1;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("previousAutoUpdateURL = ");
                    sb2.append(s);
                    s3 = sb2.toString();
                    n4 = n2;
                }
                else {
                    n3 = n;
                    n4 = n2;
                    s3 = line;
                    if (s2 != null) {
                        n3 = n;
                        n4 = n2;
                        s3 = line;
                        if (line.startsWith("additionalHosts_lastImportTS")) {
                            n4 = 1;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("additionalHosts_lastImportTS = ");
                            sb3.append(s2);
                            s3 = sb3.toString();
                            n3 = n;
                        }
                    }
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(s3);
                sb4.append("\r\n");
                byteArrayOutputStream.write(sb4.toString().getBytes());
                n = n3;
                n2 = n4;
            }
            if (n == 0 && s != null) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("previousAutoUpdateURL = ");
                sb5.append(s);
                sb5.append("\r\n");
                byteArrayOutputStream.write(sb5.toString().getBytes());
            }
            if (n2 == 0 && s2 != null) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("additionalHosts_lastImportTS = ");
                sb6.append(s2);
                sb6.append("\r\n");
                byteArrayOutputStream.write(sb6.toString().getBytes());
            }
            byteArrayOutputStream.flush();
            bufferedReader.close();
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(DNSFilterManager.WORKDIR);
            sb7.append("dnsfilter.conf");
            final FileOutputStream fileOutputStream = new FileOutputStream(sb7.toString());
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    private void writeDownloadInfoFile(final int n, final long n2) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSFilterManager.WORKDIR);
        sb.append(DNSFilterManager.filterhostfile);
        sb.append(".DLD_CNT");
        final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(n);
        sb2.append("\n");
        fileOutputStream.write(sb2.toString().getBytes());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(n2);
        sb3.append("\n");
        fileOutputStream.write(sb3.toString().getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
    
    private void writeNewEntries(final boolean b, final HashSet<String> set, final BufferedWriter bufferedWriter) throws IOException {
        String s = "";
        if (!b) {
            s = "!";
        }
        if (DNSFilterManager.hostsFilterOverRule == null) {
            DNSFilterManager.hostsFilterOverRule = new Hashtable();
            DNSFilterManager.hostFilter.setHostsFilterOverRule(DNSFilterManager.hostsFilterOverRule);
        }
        for (final String s2 : set) {
            DNSFilterManager.hostsFilterOverRule.remove(s2);
            DNSFilterManager.hostFilter.clearCache(s2);
            if (!b || !DNSFilterManager.hostFilter.contains(s2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append(s);
                sb.append(s2);
                bufferedWriter.write(sb.toString());
                if (!b) {
                    DNSFilterManager.hostsFilterOverRule.put(s2, false);
                }
                else {
                    DNSFilterManager.hostFilter.update(s2);
                }
            }
            if (b) {
                DNSFilterManager.hostFilter.updatePersist();
            }
        }
    }
    
    public boolean canStop() {
        return this.reloading_filter ^ true;
    }
    
    @Override
    public void doBackup(final String s) throws IOException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("backup/");
            sb.append(s);
            sb.append("/dnsfilter.conf");
            this.copyLocalFile("dnsfilter.conf", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("backup/");
            sb2.append(s);
            sb2.append("/additionalHosts.txt");
            this.copyLocalFile("additionalHosts.txt", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("backup/");
            sb3.append(s);
            sb3.append("/VERSION.TXT");
            this.copyLocalFile("VERSION.TXT", sb3.toString());
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void doRestore(String property) throws IOException {
        try {
            if (!this.canStop()) {
                throw new IOException("Cannot stop! Pending Operation!");
            }
            this.stop();
            final StringBuilder sb = new StringBuilder();
            sb.append("backup/");
            sb.append(property);
            sb.append("/dnsfilter.conf");
            this.copyLocalFile(sb.toString(), "dnsfilter.conf");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("backup/");
            sb2.append(property);
            sb2.append("/additionalHosts.txt");
            this.copyLocalFile(sb2.toString(), "additionalHosts.txt");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("backup/");
            sb3.append(property);
            sb3.append("/VERSION.TXT");
            this.copyLocalFile(sb3.toString(), "VERSION.TXT");
            if (this.config != null) {
                property = this.config.getProperty("filterHostsFile");
                if (property != null) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(DNSFilterManager.WORKDIR);
                    sb4.append(property);
                    new File(sb4.toString()).delete();
                }
            }
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void doRestoreDefaults() throws IOException {
        try {
            if (!this.canStop()) {
                throw new IOException("Cannot stop! Pending Operation!");
            }
            this.stop();
            this.copyFromAssets("dnsfilter.conf", "dnsfilter.conf");
            this.copyFromAssets("additionalHosts.txt", "additionalHosts.txt");
            if (this.config != null) {
                final String property = this.config.getProperty("filterHostsFile");
                if (property != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(DNSFilterManager.WORKDIR);
                    sb.append(property);
                    new File(sb.toString()).delete();
                }
            }
            this.init();
            ExecutionEnvironment.getEnvironment().onReload();
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public byte[] getAdditionalHosts(final int n) throws IOException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append("additionalHosts.txt");
            final File file = new File(sb.toString());
            if (file.length() > n) {
                return null;
            }
            final FileInputStream fileInputStream = new FileInputStream(file);
            final byte[] fully = Utils.readFully(fileInputStream, 1024);
            fileInputStream.close();
            return fully;
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public String[] getAvailableBackups() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSFilterManager.WORKDIR);
        sb.append("backup");
        final File file = new File(sb.toString());
        String[] list = new String[0];
        if (file.exists()) {
            list = file.list();
        }
        final ArrayList<Comparable> list2 = new ArrayList<Comparable>();
        for (int length = list.length, i = 0; i < length; ++i) {
            final String s = list[i];
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(DNSFilterManager.WORKDIR);
            sb2.append("backup/");
            sb2.append(s);
            if (new File(sb2.toString()).isDirectory()) {
                list2.add(s);
            }
        }
        Collections.sort(list2);
        return list2.toArray(new String[0]);
    }
    
    @Override
    public Properties getConfig() throws IOException {
        try {
            if (this.config == null) {
                (this.config = new Properties()).load(new ByteArrayInputStream(this.readConfig()));
            }
            return this.config;
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
            return null;
        }
    }
    
    @Override
    public long[] getFilterStatistics() {
        return new long[] { DNSResponsePatcher.getOkCount(), DNSResponsePatcher.getFilterCount() };
    }
    
    @Override
    public String getLastDNSAddress() {
        return DNSCommunicator.getInstance().getLastDNSAddress();
    }
    
    @Override
    public String getVersion() {
        return "1504000";
    }
    
    public void init() throws IOException {
    Label_0460_Outer:
        while (true) {
            while (true) {
                Label_0845: {
                    try {
                        if (!this.serverStopped) {
                            throw new IllegalStateException("Cannot start! Already running!");
                        }
                        this.initEnv();
                        Logger.getLogger().logLine("***Initializing PersonalDNSFilter Version 1504000!***");
                        final LoggerInterface logger = Logger.getLogger();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Using Directory: ");
                        sb.append(DNSFilterManager.WORKDIR);
                        logger.logLine(sb.toString());
                        (this.config = new Properties()).load(new ByteArrayInputStream(this.readConfig()));
                        this.serverStopped = false;
                        if (DNSFilterManager.remoteAccessManager == null) {
                            try {
                                final int int1 = Integer.parseInt(this.config.getProperty("server_remote_ctrl_port", "-1"));
                                final String property = this.config.getProperty("server_remote_ctrl_keyphrase", "");
                                if (int1 != -1) {
                                    DNSFilterManager.remoteAccessManager = new RemoteAccessServer(int1, property);
                                }
                            }
                            catch (Exception ex) {
                                Logger.getLogger().logException(ex);
                            }
                        }
                        try {
                            DNSFilterManager.okCacheSize = Integer.parseInt(this.config.getProperty("allowedHostsCacheSize", "1000").trim());
                            DNSFilterManager.filterListCacheSize = Integer.parseInt(this.config.getProperty("filterHostsCacheSize", "1000").trim());
                            if (this.config.getProperty("androidKeepAwake", "true").equalsIgnoreCase("true")) {
                                ExecutionEnvironment.getEnvironment().wakeLock();
                            }
                            DNSFilterManager.additionalHostsImportTS = this.config.getProperty("additionalHosts_lastImportTS", "0");
                            try {
                                if (this.config.getProperty("enableTrafficLog", "true").equalsIgnoreCase("true")) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append(DNSFilterManager.WORKDIR);
                                    sb2.append("log");
                                    DNSFilterManager.TRAFFIC_LOG = new FileLogger(sb2.toString(), this.config.getProperty("trafficLogName", "trafficlog"), Integer.parseInt(this.config.getProperty("trafficLogSize", "1048576").trim()), Integer.parseInt(this.config.getProperty("trafficLogSlotCount", "2").trim()), "timestamp, client:port, request type, domain name, answer");
                                    ((FileLogger)DNSFilterManager.TRAFFIC_LOG).enableTimestamp(true);
                                    Logger.setLogger(DNSFilterManager.TRAFFIC_LOG, "TrafficLogger");
                                }
                                else {
                                    DNSFilterManager.TRAFFIC_LOG = null;
                                }
                                DNSFilterManager.debug = Boolean.parseBoolean(this.config.getProperty("debug", "false"));
                                DNSFilterManager.filterHostsFileRemoveDuplicates = true;
                                DNSFilterManager.filterhostfile = this.config.getProperty("filterHostsFile");
                                if (DNSFilterManager.filterhostfile == null) {
                                    break;
                                }
                                final Iterator<Map.Entry<Object, Object>> iterator = this.config.entrySet().iterator();
                                if (!iterator.hasNext()) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append(DNSFilterManager.WORKDIR);
                                    sb3.append("additionalHosts.txt");
                                    final File file = new File(sb3.toString());
                                    if (file.exists()) {
                                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                                        while (true) {
                                            final String line = bufferedReader.readLine();
                                            if (line == null) {
                                                break;
                                            }
                                            if (!line.startsWith("!")) {
                                                continue Label_0460_Outer;
                                            }
                                            if (DNSFilterManager.hostsFilterOverRule == null) {
                                                DNSFilterManager.hostsFilterOverRule = new Hashtable();
                                            }
                                            DNSFilterManager.hostsFilterOverRule.put(line.substring(1).trim(), new Boolean(false));
                                        }
                                        bufferedReader.close();
                                    }
                                    DNSFilterManager.filterReloadURL = this.getFilterReloadURL(this.config);
                                    DNSFilterManager.filterReloadIntervalDays = Integer.parseInt(this.config.getProperty("reloadIntervalDays", "4"));
                                    final String property2 = this.config.getProperty("previousAutoUpdateURL");
                                    if (DNSFilterManager.filterReloadURL != null) {
                                        DNSFilterManager.reloadUrlChanged = (DNSFilterManager.filterReloadURL.equals(property2) ^ true);
                                    }
                                    this.reloadFilter(true);
                                    if (DNSFilterManager.filterReloadURL != null) {
                                        this.autoFilterUpdater = new AutoFilterUpdater();
                                        final Thread thread = new Thread(this.autoFilterUpdater);
                                        thread.setDaemon(true);
                                        thread.start();
                                    }
                                    DNSResponsePatcher.init(DNSFilterManager.hostFilter, DNSFilterManager.TRAFFIC_LOG);
                                    return;
                                }
                                final Map.Entry<Object, Object> entry = iterator.next();
                                final String s = entry.getKey();
                                if (s.startsWith("filter.")) {
                                    if (DNSFilterManager.hostsFilterOverRule == null) {
                                        DNSFilterManager.hostsFilterOverRule = new Hashtable();
                                    }
                                    DNSFilterManager.hostsFilterOverRule.put(s.substring(7), new Boolean(Boolean.parseBoolean(entry.getValue().trim())));
                                    break Label_0845;
                                }
                                break Label_0845;
                            }
                            catch (NumberFormatException ex2) {
                                Logger.getLogger().logLine("Cannot parse log configuration!");
                                throw new IOException(ex2);
                            }
                        }
                        catch (NumberFormatException ex3) {
                            try {
                                Logger.getLogger().logLine("Cannot parse cache size configuration!");
                                throw new IOException(ex3);
                            }
                            catch (IOException ex4) {
                                throw ex4;
                            }
                        }
                    }
                    catch (IOException ex5) {}
                }
                continue;
            }
        }
    }
    
    @Override
    public int openConnectionsCount() {
        return DNSResolver.getResolverCount();
    }
    
    @Override
    public byte[] readConfig() throws ConfigurationAccessException {
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSFilterManager.WORKDIR);
        sb.append("dnsfilter.conf");
        File file2;
        final File file = file2 = new File(sb.toString());
        if (!file.exists()) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(file);
            sb2.append(" not found! - creating default config!");
            logger.logLine(sb2.toString());
            this.createDefaultConfiguration();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(DNSFilterManager.WORKDIR);
            sb3.append("dnsfilter.conf");
            file2 = new File(sb3.toString());
        }
        try {
            final FileInputStream fileInputStream = new FileInputStream(file2);
            final byte[] fully = Utils.readFully(fileInputStream, 1024);
            fileInputStream.close();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(DNSFilterManager.WORKDIR);
            sb4.append("additionalHosts.txt");
            final File file3 = new File(sb4.toString());
            if (!file3.exists()) {
                file3.createNewFile();
                Utils.copyFully(ExecutionEnvironment.getEnvironment().getAsset("additionalHosts.txt"), new FileOutputStream(file3), true);
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(DNSFilterManager.WORKDIR);
            sb5.append("VERSION.TXT");
            final File file4 = new File(sb5.toString());
            String s = "";
            if (file4.exists()) {
                final FileInputStream fileInputStream2 = new FileInputStream(file4);
                s = new String(Utils.readFully(fileInputStream2, 100));
                fileInputStream2.close();
            }
            byte[] mergeAndPersistConfig = fully;
            if (!s.equals("1504000")) {
                final LoggerInterface logger2 = Logger.getLogger();
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Updated version! Previous version:");
                sb6.append(s);
                sb6.append(", current version:");
                sb6.append("1504000");
                logger2.logLine(sb6.toString());
                this.createDefaultConfiguration();
                mergeAndPersistConfig = this.mergeAndPersistConfig(fully);
            }
            return mergeAndPersistConfig;
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    public int[] readHostFileEntry(final InputStream inputStream, final byte[] array) throws IOException {
        int n = 0;
        int i;
        int skipLine;
        for (i = inputStream.read(); i == 35; i = Utils.skipWhitespace(inputStream, skipLine)) {
            skipLine = Utils.skipLine(inputStream);
            if ((i = skipLine) != -1) {}
        }
        if (i == -1) {
            return new int[] { 0, -1 };
        }
        if (array.length == 0) {
            throw new IOException("Buffer Overflow!");
        }
        if (i == 42) {
            n = 1;
        }
        array[0] = (byte)i;
        int n2 = 0;
        int n3 = 1;
        int n4 = i;
        int n5 = n;
    Label_0099:
        while (n4 != -1 && n4 != 10) {
            int n6 = n2;
            int n7 = n4;
            int n8 = n5;
            int n9 = n3;
            int skipWhitespace = 0;
            Block_18: {
                while (true) {
                    n3 = n9;
                    n5 = n8;
                    n4 = n7;
                    n2 = n6;
                    if (n7 == -1) {
                        continue Label_0099;
                    }
                    n3 = n9;
                    n5 = n8;
                    n4 = n7;
                    n2 = n6;
                    if (n7 == 10) {
                        continue Label_0099;
                    }
                    final int read = inputStream.read();
                    int n10 = 0;
                    int n11 = 0;
                    int n12 = 0;
                    Label_0253: {
                        if (read != 9 && read != 32) {
                            n10 = n9;
                            n11 = n8;
                            skipWhitespace = read;
                            n12 = n6;
                            if (read != 13) {
                                break Label_0253;
                            }
                        }
                        if (n6 == 1) {
                            break;
                        }
                        n12 = 1;
                        n11 = 0;
                        skipWhitespace = Utils.skipWhitespace(inputStream, read);
                        n10 = 0;
                    }
                    if (skipWhitespace == 42) {
                        n11 = 1;
                    }
                    n9 = n10;
                    n8 = n11;
                    n7 = skipWhitespace;
                    n6 = n12;
                    if (skipWhitespace == -1) {
                        continue;
                    }
                    if (n10 == array.length) {
                        throw new IOException("Buffer Overflow!");
                    }
                    if (skipWhitespace < 32 && skipWhitespace < 9 && skipWhitespace > 13) {
                        break Block_18;
                    }
                    array[n10] = (byte)skipWhitespace;
                    n9 = n10 + 1;
                    n8 = n11;
                    n7 = skipWhitespace;
                    n6 = n12;
                }
                Utils.skipLine(inputStream);
                return new int[] { n8, n9 };
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Non Printable character: ");
            sb.append(skipWhitespace);
            sb.append("(");
            sb.append((char)skipWhitespace);
            sb.append(")");
            throw new IOException(sb.toString());
        }
        return new int[] { n5, n3 - 1 };
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
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
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
            if (DNSFilterManager.hostFilter != null) {
                DNSFilterManager.hostFilter.clear();
            }
            DNSResponsePatcher.init(null, null);
            if (DNSFilterManager.TRAFFIC_LOG != null) {
                DNSFilterManager.TRAFFIC_LOG.closeLogger();
                Logger.removeLogger("TrafficLogger");
            }
            this.serverStopped = true;
            ExecutionEnvironment.getEnvironment().releaseAllWakeLocks();
        }
    }
    
    @Override
    public void triggerUpdateFilter() {
        if (this.reloading_filter) {
            Logger.getLogger().logLine("Filter Reload currently running!");
            return;
        }
        if (DNSFilterManager.filterReloadURL != null) {
            synchronized (this) {
                DNSFilterManager.nextReload = 0L;
                this.notifyAll();
                return;
            }
        }
        Logger.getLogger().logLine("DNS Filter: Setting 'filterAutoUpdateURL' not configured - cannot update filter!");
    }
    
    @Override
    public void updateAdditionalHosts(final byte[] array) throws IOException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append("additionalHosts.txt");
            final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            fileOutputStream.write(array);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void updateConfig(final byte[] array) throws IOException {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(DNSFilterManager.WORKDIR);
            sb.append("dnsfilter.conf");
            final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            fileOutputStream.write(array);
            fileOutputStream.flush();
            fileOutputStream.close();
            this.config.load(new ByteArrayInputStream(array));
            Logger.getLogger().message("Config Changed!\nRestart might be required!");
        }
        catch (IOException ex) {
            throw new ConfigurationAccessException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void updateFilter(final String p0, final boolean p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_3       
        //     2: getstatic       dnsfilter/DNSFilterManager.updatingFilter:Z
        //     5: ifeq            14
        //     8: aload_0        
        //     9: invokespecial   dnsfilter/DNSFilterManager.abortFilterUpdate:()V
        //    12: iconst_1       
        //    13: istore_3       
        //    14: getstatic       dnsfilter/DNSFilterManager.INSTANCE:Ldnsfilter/DNSFilterManager;
        //    17: astore          10
        //    19: aload           10
        //    21: monitorenter   
        //    22: iload_3        
        //    23: istore          5
        //    25: aload_1        
        //    26: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    29: ldc             ""
        //    31: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    34: ifne            901
        //    37: iload_3        
        //    38: istore          5
        //    40: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //    43: ifnonnull       49
        //    46: goto            813
        //    49: iload_3        
        //    50: istore          5
        //    52: new             Ljava/util/StringTokenizer;
        //    55: dup            
        //    56: aload_1        
        //    57: ldc_w           "\n"
        //    60: invokespecial   java/util/StringTokenizer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    63: astore_1       
        //    64: iload_3        
        //    65: istore          5
        //    67: new             Ljava/util/HashSet;
        //    70: dup            
        //    71: invokespecial   java/util/HashSet.<init>:()V
        //    74: astore          11
        //    76: iload_3        
        //    77: istore          5
        //    79: aload_1        
        //    80: invokevirtual   java/util/StringTokenizer.hasMoreTokens:()Z
        //    83: istore          6
        //    85: iload           6
        //    87: ifeq            136
        //    90: aload_1        
        //    91: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //    94: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    97: astore          8
        //    99: getstatic       dnsfilter/DNSFilterManager.hostFilter:Ldnsfilter/BlockedHosts;
        //   102: aload           8
        //   104: invokevirtual   dnsfilter/BlockedHosts.contains:(Ljava/lang/Object;)Z
        //   107: istore          6
        //   109: iload_2        
        //   110: ifeq            840
        //   113: iload           6
        //   115: ifeq            121
        //   118: goto            840
        //   121: aload           11
        //   123: aload           8
        //   125: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   128: pop            
        //   129: goto            76
        //   132: astore_1       
        //   133: goto            817
        //   136: iload_3        
        //   137: istore          5
        //   139: new             Ljava/lang/StringBuilder;
        //   142: dup            
        //   143: invokespecial   java/lang/StringBuilder.<init>:()V
        //   146: astore_1       
        //   147: iload_3        
        //   148: istore          5
        //   150: aload_1        
        //   151: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   154: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   157: pop            
        //   158: iload_3        
        //   159: istore          5
        //   161: aload_1        
        //   162: ldc             "additionalHosts.txt"
        //   164: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   167: pop            
        //   168: iload_3        
        //   169: istore          5
        //   171: new             Ljava/io/File;
        //   174: dup            
        //   175: aload_1        
        //   176: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   179: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   182: astore          12
        //   184: iload_3        
        //   185: istore          5
        //   187: new             Ljava/lang/StringBuilder;
        //   190: dup            
        //   191: invokespecial   java/lang/StringBuilder.<init>:()V
        //   194: astore_1       
        //   195: iload_3        
        //   196: istore          5
        //   198: aload_1        
        //   199: getstatic       dnsfilter/DNSFilterManager.WORKDIR:Ljava/lang/String;
        //   202: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   205: pop            
        //   206: iload_3        
        //   207: istore          5
        //   209: aload_1        
        //   210: ldc_w           "additionalHosts.txt.tmp"
        //   213: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   216: pop            
        //   217: iload_3        
        //   218: istore          5
        //   220: new             Ljava/io/File;
        //   223: dup            
        //   224: aload_1        
        //   225: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   228: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   231: astore_1       
        //   232: iload_3        
        //   233: istore          5
        //   235: new             Ljava/io/BufferedReader;
        //   238: dup            
        //   239: new             Ljava/io/InputStreamReader;
        //   242: dup            
        //   243: new             Ljava/io/FileInputStream;
        //   246: dup            
        //   247: aload           12
        //   249: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   252: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   255: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   258: astore          14
        //   260: iload_3        
        //   261: istore          5
        //   263: new             Ljava/io/BufferedWriter;
        //   266: dup            
        //   267: new             Ljava/io/OutputStreamWriter;
        //   270: dup            
        //   271: new             Ljava/io/FileOutputStream;
        //   274: dup            
        //   275: aload_1        
        //   276: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   279: invokespecial   java/io/OutputStreamWriter.<init>:(Ljava/io/OutputStream;)V
        //   282: invokespecial   java/io/BufferedWriter.<init>:(Ljava/io/Writer;)V
        //   285: astore          13
        //   287: iconst_0       
        //   288: istore          6
        //   290: iconst_0       
        //   291: istore          4
        //   293: iload_3        
        //   294: istore          5
        //   296: aload           14
        //   298: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   301: astore          9
        //   303: aload           9
        //   305: ifnull          499
        //   308: iload_3        
        //   309: istore          5
        //   311: aload           9
        //   313: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   316: astore          8
        //   318: aload           8
        //   320: ldc             ""
        //   322: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   325: ifeq            858
        //   328: aload           9
        //   330: ldc_w           "#"
        //   333: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   336: ifeq            852
        //   339: goto            858
        //   342: aload           9
        //   344: ldc_w           "!"
        //   347: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   350: ifeq            864
        //   353: aload           9
        //   355: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   358: iconst_1       
        //   359: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   362: astore          8
        //   364: goto            367
        //   367: iload           5
        //   369: ifeq            385
        //   372: aload           11
        //   374: aload           8
        //   376: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
        //   379: ifne            871
        //   382: goto            385
        //   385: new             Ljava/lang/StringBuilder;
        //   388: dup            
        //   389: invokespecial   java/lang/StringBuilder.<init>:()V
        //   392: astore          8
        //   394: aload           8
        //   396: aload           9
        //   398: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   401: pop            
        //   402: aload           8
        //   404: ldc_w           "\n"
        //   407: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   410: pop            
        //   411: aload           13
        //   413: aload           8
        //   415: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   418: invokevirtual   java/io/BufferedWriter.write:(Ljava/lang/String;)V
        //   421: iload           6
        //   423: istore          7
        //   425: iload           6
        //   427: ifne            440
        //   430: aload           9
        //   432: ldc_w           "##### AUTOMATIC ENTRIES BELOW! #####"
        //   435: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   438: istore          7
        //   440: iload           4
        //   442: istore          5
        //   444: iload           4
        //   446: ifne            890
        //   449: iload_2        
        //   450: ifeq            464
        //   453: aload           9
        //   455: ldc_w           "## Blacklisted Entries! ##"
        //   458: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   461: ifne            874
        //   464: iload_2        
        //   465: ifne            880
        //   468: aload           9
        //   470: ldc_w           "## Whitelisted Entries! ##"
        //   473: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   476: ifeq            880
        //   479: goto            874
        //   482: iload           4
        //   484: ifeq            886
        //   487: aload_0        
        //   488: iload_2        
        //   489: aload           11
        //   491: aload           13
        //   493: invokespecial   dnsfilter/DNSFilterManager.writeNewEntries:(ZLjava/util/HashSet;Ljava/io/BufferedWriter;)V
        //   496: goto            886
        //   499: aload           14
        //   501: invokevirtual   java/io/BufferedReader.close:()V
        //   504: iload           6
        //   506: ifne            555
        //   509: new             Ljava/lang/StringBuilder;
        //   512: dup            
        //   513: invokespecial   java/lang/StringBuilder.<init>:()V
        //   516: astore          8
        //   518: aload           8
        //   520: ldc_w           "\n"
        //   523: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   526: pop            
        //   527: aload           8
        //   529: ldc_w           "##### AUTOMATIC ENTRIES BELOW! #####"
        //   532: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   535: pop            
        //   536: aload           8
        //   538: ldc_w           "\n"
        //   541: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   544: pop            
        //   545: aload           13
        //   547: aload           8
        //   549: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   552: invokevirtual   java/io/BufferedWriter.write:(Ljava/lang/String;)V
        //   555: iload           4
        //   557: ifne            668
        //   560: iload_2        
        //   561: ifeq            613
        //   564: new             Ljava/lang/StringBuilder;
        //   567: dup            
        //   568: invokespecial   java/lang/StringBuilder.<init>:()V
        //   571: astore          8
        //   573: aload           8
        //   575: ldc_w           "\n"
        //   578: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   581: pop            
        //   582: aload           8
        //   584: ldc_w           "## Blacklisted Entries! ##"
        //   587: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   590: pop            
        //   591: aload           8
        //   593: ldc_w           "\n"
        //   596: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   599: pop            
        //   600: aload           13
        //   602: aload           8
        //   604: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   607: invokevirtual   java/io/BufferedWriter.write:(Ljava/lang/String;)V
        //   610: goto            659
        //   613: new             Ljava/lang/StringBuilder;
        //   616: dup            
        //   617: invokespecial   java/lang/StringBuilder.<init>:()V
        //   620: astore          8
        //   622: aload           8
        //   624: ldc_w           "\n"
        //   627: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   630: pop            
        //   631: aload           8
        //   633: ldc_w           "## Whitelisted Entries! ##"
        //   636: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   639: pop            
        //   640: aload           8
        //   642: ldc_w           "\n"
        //   645: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   648: pop            
        //   649: aload           13
        //   651: aload           8
        //   653: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   656: invokevirtual   java/io/BufferedWriter.write:(Ljava/lang/String;)V
        //   659: aload_0        
        //   660: iload_2        
        //   661: aload           11
        //   663: aload           13
        //   665: invokespecial   dnsfilter/DNSFilterManager.writeNewEntries:(ZLjava/util/HashSet;Ljava/io/BufferedWriter;)V
        //   668: aload           13
        //   670: invokevirtual   java/io/BufferedWriter.flush:()V
        //   673: aload           13
        //   675: invokevirtual   java/io/BufferedWriter.close:()V
        //   678: aload           12
        //   680: invokevirtual   java/io/File.delete:()Z
        //   683: pop            
        //   684: aload_1        
        //   685: aload           12
        //   687: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //   690: pop            
        //   691: new             Ljava/lang/StringBuilder;
        //   694: dup            
        //   695: invokespecial   java/lang/StringBuilder.<init>:()V
        //   698: astore_1       
        //   699: aload_1        
        //   700: ldc             ""
        //   702: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   705: pop            
        //   706: aload_1        
        //   707: aload           12
        //   709: invokevirtual   java/io/File.lastModified:()J
        //   712: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   715: pop            
        //   716: aload_1        
        //   717: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   720: putstatic       dnsfilter/DNSFilterManager.additionalHostsImportTS:Ljava/lang/String;
        //   723: aload_0        
        //   724: getstatic       dnsfilter/DNSFilterManager.filterReloadURL:Ljava/lang/String;
        //   727: getstatic       dnsfilter/DNSFilterManager.additionalHostsImportTS:Ljava/lang/String;
        //   730: invokespecial   dnsfilter/DNSFilterManager.updateIndexReloadInfoConfFile:(Ljava/lang/String;Ljava/lang/String;)V
        //   733: invokestatic    util/Logger.getLogger:()Lutil/LoggerInterface;
        //   736: astore_1       
        //   737: new             Ljava/lang/StringBuilder;
        //   740: dup            
        //   741: invokespecial   java/lang/StringBuilder.<init>:()V
        //   744: astore          8
        //   746: aload           8
        //   748: ldc_w           "Updated "
        //   751: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   754: pop            
        //   755: aload           8
        //   757: aload           11
        //   759: invokevirtual   java/util/HashSet.size:()I
        //   762: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   765: pop            
        //   766: aload           8
        //   768: ldc_w           " host(s)!"
        //   771: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   774: pop            
        //   775: aload_1        
        //   776: aload           8
        //   778: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   781: invokeinterface util/LoggerInterface.message:(Ljava/lang/String;)V
        //   786: iload_3        
        //   787: ifeq            809
        //   790: new             Ljava/lang/Thread;
        //   793: dup            
        //   794: new             Ldnsfilter/DNSFilterManager$AsyncIndexBuilder;
        //   797: dup            
        //   798: aload_0        
        //   799: aconst_null    
        //   800: invokespecial   dnsfilter/DNSFilterManager$AsyncIndexBuilder.<init>:(Ldnsfilter/DNSFilterManager;Ldnsfilter/DNSFilterManager$1;)V
        //   803: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //   806: invokevirtual   java/lang/Thread.start:()V
        //   809: aload           10
        //   811: monitorexit    
        //   812: return         
        //   813: aload           10
        //   815: monitorexit    
        //   816: return         
        //   817: aload           10
        //   819: monitorexit    
        //   820: aload_1        
        //   821: athrow         
        //   822: astore_1       
        //   823: goto            817
        //   826: astore_1       
        //   827: new             Ldnsfilter/ConfigurationAccess$ConfigurationAccessException;
        //   830: dup            
        //   831: aload_1        
        //   832: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   835: aload_1        
        //   836: invokespecial   dnsfilter/ConfigurationAccess$ConfigurationAccessException.<init>:(Ljava/lang/String;Ljava/io/IOException;)V
        //   839: athrow         
        //   840: iload_2        
        //   841: ifne            129
        //   844: iload           6
        //   846: ifeq            129
        //   849: goto            121
        //   852: iconst_0       
        //   853: istore          5
        //   855: goto            342
        //   858: iconst_1       
        //   859: istore          5
        //   861: goto            342
        //   864: aload           9
        //   866: astore          8
        //   868: goto            367
        //   871: goto            421
        //   874: iconst_1       
        //   875: istore          4
        //   877: goto            482
        //   880: iconst_0       
        //   881: istore          4
        //   883: goto            482
        //   886: iload           4
        //   888: istore          5
        //   890: iload           5
        //   892: istore          4
        //   894: iload           7
        //   896: istore          6
        //   898: goto            293
        //   901: goto            813
        //   904: astore_1       
        //   905: goto            817
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      12     826    840    Ljava/io/IOException;
        //  14     22     826    840    Ljava/io/IOException;
        //  25     37     904    908    Any
        //  40     46     904    908    Any
        //  52     64     904    908    Any
        //  67     76     904    908    Any
        //  79     85     904    908    Any
        //  90     109    132    136    Any
        //  121    129    132    136    Any
        //  139    147    904    908    Any
        //  150    158    904    908    Any
        //  161    168    904    908    Any
        //  171    184    904    908    Any
        //  187    195    904    908    Any
        //  198    206    904    908    Any
        //  209    217    904    908    Any
        //  220    232    904    908    Any
        //  235    260    904    908    Any
        //  263    287    904    908    Any
        //  296    303    904    908    Any
        //  311    318    904    908    Any
        //  318    339    822    826    Any
        //  342    364    822    826    Any
        //  372    382    822    826    Any
        //  385    421    822    826    Any
        //  430    440    822    826    Any
        //  453    464    822    826    Any
        //  468    479    822    826    Any
        //  487    496    822    826    Any
        //  499    504    822    826    Any
        //  509    555    822    826    Any
        //  564    610    822    826    Any
        //  613    659    822    826    Any
        //  659    668    822    826    Any
        //  668    786    822    826    Any
        //  790    809    822    826    Any
        //  809    812    822    826    Any
        //  813    816    822    826    Any
        //  817    820    822    826    Any
        //  820    822    826    840    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0049:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void wakeLock() {
        ExecutionEnvironment.getEnvironment().wakeLock();
    }
    
    private class AsyncIndexBuilder implements Runnable
    {
        @Override
        public void run() {
            DNSFilterManager.this.reloading_filter = true;
            while (true) {
                try {
                    try {
                        DNSFilterManager.this.rebuildIndex();
                        DNSFilterManager.this.reloading_filter = false;
                        return;
                    }
                    finally {}
                }
                catch (IOException ex) {
                    Logger.getLogger().logException(ex);
                    continue;
                }
                break;
            }
            DNSFilterManager.this.reloading_filter = false;
        }
    }
    
    private class AutoFilterUpdater implements Runnable
    {
        private Object monitor;
        boolean running;
        boolean stopRequest;
        
        public AutoFilterUpdater() {
            this.stopRequest = false;
            this.running = false;
            this.monitor = DNSFilterManager.INSTANCE;
        }
        
        private void waitUntilNextFilterReload() throws InterruptedException {
            synchronized (this.monitor) {
                while (DNSFilterManager.nextReload > System.currentTimeMillis() && !this.stopRequest) {
                    this.monitor.wait(10000L);
                }
            }
        }
        
        @Override
        public void run() {
            while (true) {
                final Object monitor = this.monitor;
                // monitorenter(monitor)
            Label_0241_Outer:
                while (true) {
                    while (true) {
                        Label_0420: {
                            try {
                                this.running = true;
                                int n = 0;
                                if (!this.stopRequest) {
                                    final LoggerInterface logger = Logger.getLogger();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("DNS Filter: Next filter reload:");
                                    sb.append(new Date(DNSFilterManager.nextReload));
                                    logger.logLine(sb.toString());
                                    try {
                                        this.waitUntilNextFilterReload();
                                    }
                                    catch (InterruptedException ex2) {}
                                    if (!this.stopRequest) {
                                        while (true) {
                                            try {
                                                try {
                                                    DNSFilterManager.this.reloading_filter = true;
                                                    Logger.getLogger().logLine("DNS Filter: Reloading hosts filter ...");
                                                    if (DNSFilterManager.this.updateFilter()) {
                                                        DNSFilterManager.validIndex = false;
                                                        DNSFilterManager.this.reloadFilter(false);
                                                        Logger.getLogger().logLine("Reloading hosts filter ... completed!");
                                                    }
                                                    DNSFilterManager.nextReload = System.currentTimeMillis() + DNSFilterManager.filterReloadIntervalDays * 24L * 60L * 60L * 1000L;
                                                    n = 0;
                                                    final DNSFilterManager this$0 = DNSFilterManager.this;
                                                    this$0.reloading_filter = false;
                                                }
                                                finally {}
                                            }
                                            catch (Exception ex) {
                                                Logger.getLogger().logLine("Cannot update hosts filter file!");
                                                Logger.getLogger().logLine(ex.toString());
                                                if (n < 10) {
                                                    if (n >= 5) {
                                                        break Label_0420;
                                                    }
                                                    final long n2 = 60000L;
                                                    DNSFilterManager.nextReload = System.currentTimeMillis() + n2;
                                                    final LoggerInterface logger2 = Logger.getLogger();
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("Retry at: ");
                                                    sb2.append(new Date(DNSFilterManager.nextReload));
                                                    logger2.logLine(sb2.toString());
                                                    ++n;
                                                }
                                                else {
                                                    Logger.getLogger().logLine("Giving up! Reload skipped!");
                                                    DNSFilterManager.nextReload = System.currentTimeMillis() + DNSFilterManager.filterReloadIntervalDays * 24L * 60L * 60L * 1000L;
                                                    n = 0;
                                                }
                                                final DNSFilterManager this$2 = DNSFilterManager.this;
                                                continue Label_0241_Outer;
                                            }
                                            break;
                                        }
                                        DNSFilterManager.this.reloading_filter = false;
                                    }
                                }
                                Logger.getLogger().logLine("DNS Filter: AutoFilterUpdater stopped!");
                                return;
                            }
                            finally {
                                this.running = false;
                                this.monitor.notifyAll();
                            }
                            try {
                                return;
                            }
                            finally {
                            }
                            // monitorexit(monitor)
                        }
                        final long n2 = 3600000L;
                        continue;
                    }
                    continue Label_0241_Outer;
                }
            }
        }
        
        public void stop() {
            this.stopRequest = true;
            synchronized (this.monitor) {
                this.monitor.notifyAll();
            Label_0035_Outer:
                while (this.running) {
                    while (true) {
                        try {
                            this.monitor.wait();
                            continue Label_0035_Outer;
                        }
                        catch (InterruptedException ex) {
                            Logger.getLogger().logException(ex);
                            continue;
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
}
