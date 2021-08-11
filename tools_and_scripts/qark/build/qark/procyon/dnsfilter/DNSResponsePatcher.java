// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.nio.ByteBuffer;
import java.io.IOException;
import util.Logger;
import java.net.InetAddress;
import util.LoggerInterface;
import java.util.Set;

public class DNSResponsePatcher
{
    private static Set FILTER;
    private static LoggerInterface TRAFFIC_LOG;
    private static boolean checkCNAME;
    private static boolean checkIP;
    private static long filterCnt;
    private static byte[] ipv4_localhost;
    private static byte[] ipv6_localhost;
    private static long okCnt;
    
    static {
        DNSResponsePatcher.FILTER = null;
        DNSResponsePatcher.TRAFFIC_LOG = null;
        DNSResponsePatcher.okCnt = 0L;
        DNSResponsePatcher.filterCnt = 0L;
        DNSResponsePatcher.checkIP = false;
        DNSResponsePatcher.checkCNAME = true;
        try {
            DNSResponsePatcher.ipv4_localhost = InetAddress.getByName("127.0.0.1").getAddress();
            DNSResponsePatcher.ipv6_localhost = InetAddress.getByName("::1").getAddress();
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    private static boolean filter(final String s) {
        final boolean b = DNSResponsePatcher.FILTER != null && DNSResponsePatcher.FILTER.contains(s);
        if (b) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("FILTERED:");
            sb.append(s);
            logger.logLine(sb.toString());
        }
        else {
            final LoggerInterface logger2 = Logger.getLogger();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ALLOWED:");
            sb2.append(s);
            logger2.logLine(sb2.toString());
        }
        if (!b) {
            ++DNSResponsePatcher.okCnt;
            return b;
        }
        ++DNSResponsePatcher.filterCnt;
        return b;
    }
    
    private static boolean filterIP(final String s) {
        boolean contains;
        if (DNSResponsePatcher.FILTER == null) {
            contains = false;
        }
        else {
            final Set filter = DNSResponsePatcher.FILTER;
            final StringBuilder sb = new StringBuilder();
            sb.append("%IP%");
            sb.append(s);
            contains = filter.contains(sb.toString());
        }
        if (contains) {
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("FILTERED:");
            sb2.append(s);
            logger.logLine(sb2.toString());
        }
        if (!contains) {
            ++DNSResponsePatcher.okCnt;
            return contains;
        }
        ++DNSResponsePatcher.filterCnt;
        return contains;
    }
    
    public static long getFilterCount() {
        return DNSResponsePatcher.filterCnt;
    }
    
    public static long getOkCount() {
        return DNSResponsePatcher.okCnt;
    }
    
    public static void init(final Set filter, final LoggerInterface traffic_LOG) {
        DNSResponsePatcher.FILTER = filter;
        DNSResponsePatcher.TRAFFIC_LOG = traffic_LOG;
        DNSResponsePatcher.okCnt = 0L;
        DNSResponsePatcher.filterCnt = 0L;
        try {
            DNSResponsePatcher.checkIP = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkResolvedIP", "false"));
            DNSResponsePatcher.checkCNAME = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkCNAME", "false"));
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    public static byte[] patchResponse(final String s, byte[] array, final int n) throws IOException {
    Label_0217_Outer:
        while (true) {
        Label_0341_Outer:
            while (true) {
                String s2 = null;
                int n5 = 0;
                int n8 = 0;
                Label_0803: {
                    while (true) {
                        String domainName2 = null;
                        Label_0790: {
                            ByteBuffer wrap;
                            short short1;
                            short short2;
                            int n2 = 0;
                            String domainName;
                            int n3;
                            boolean b;
                            short short3;
                            int n4;
                            LoggerInterface traffic_LOG;
                            StringBuilder sb;
                            short short4;
                            short short5;
                            boolean b2;
                            String s3;
                            int n6;
                            boolean b4;
                            boolean b3;
                            final int n7;
                            byte[] array2;
                            String s4;
                            boolean b5;
                            LoggerInterface traffic_LOG2;
                            StringBuilder sb2;
                            Label_0550_Outer:Label_0653_Outer:Label_0569_Outer:
                            while (true) {
                                Label_0778: {
                                    Label_0775: {
                                        try {
                                            wrap = ByteBuffer.wrap(array, n, array.length - n);
                                            wrap.getShort();
                                            wrap.getShort();
                                            short1 = wrap.getShort();
                                            short2 = wrap.getShort();
                                            wrap.getShort();
                                            wrap.getShort();
                                            n2 = 0;
                                            domainName = "";
                                            n3 = 0;
                                            while (true) {
                                                b = true;
                                                if (n3 >= short1) {
                                                    break Label_0550_Outer;
                                                }
                                                domainName = readDomainName(wrap, n);
                                                short3 = wrap.getShort();
                                                if (short3 == 1) {
                                                    break;
                                                }
                                                n4 = n2;
                                                if (short3 == 28) {
                                                    break;
                                                }
                                                if (DNSResponsePatcher.TRAFFIC_LOG != null) {
                                                    traffic_LOG = DNSResponsePatcher.TRAFFIC_LOG;
                                                    sb = new StringBuilder();
                                                    sb.append(s);
                                                    sb.append(", Q-");
                                                    sb.append(short3);
                                                    sb.append(", ");
                                                    sb.append(domainName);
                                                    sb.append(", <empty>");
                                                    traffic_LOG.logLine(sb.toString());
                                                }
                                                wrap.getShort();
                                                ++n3;
                                                n2 = n4;
                                            }
                                            n4 = (b ? 1 : 0);
                                            if (n2 != 0) {
                                                break Label_0778;
                                            }
                                            if (filter(domainName)) {
                                                n4 = (b ? 1 : 0);
                                                break Label_0778;
                                            }
                                            break Label_0775;
                                            // iftrue(Label_0286:, short4 == 1)
                                            // iftrue(Label_0341:, !DNSResponsePatcher.checkCNAME)
                                            // iftrue(Label_0550:, !filterIP(InetAddress.getByAddress(array2).getHostAddress()))
                                            // iftrue(Label_0376:, short4 != 1)
                                            // iftrue(Label_0550:, short4 != 28)
                                            // iftrue(Label_0569:, b3)
                                            // iftrue(Label_0645:, short4 == 1 || short4 == 28)
                                            // iftrue(Label_0550:, short4 != 28)
                                            // iftrue(Label_0795:, n7 != 0 || filter(domainName2))
                                            // iftrue(Label_0752:, n8 >= short2)
                                            // iftrue(Label_0550:, !DNSResponsePatcher.checkIP)
                                            // iftrue(Label_0610:, short4 != 5)
                                            // iftrue(Label_0341:, domainName2.equals((Object)domainName))
                                            // iftrue(Label_0514:, short4 != 1)
                                            // iftrue(Label_0803:, DNSResponsePatcher.TRAFFIC_LOG == null)
                                            // iftrue(Label_0550:, short4 != 28)
                                            // iftrue(Label_0415:, n6 == 0)
                                            // iftrue(Label_0341:, n6 = n7 != 0)
                                            while (true) {
                                            Label_0653:
                                                while (true) {
                                                    Label_0633: {
                                                        while (true) {
                                                        Label_0286:
                                                            while (true) {
                                                                Block_8: {
                                                                    while (true) {
                                                                        Label_0550:Block_23_Outer:
                                                                        while (true) {
                                                                            Block_20: {
                                                                                Block_18: {
                                                                                    while (true) {
                                                                                        Block_10: {
                                                                                            while (true) {
                                                                                                while (true) {
                                                                                                Block_15_Outer:
                                                                                                    while (true) {
                                                                                                        domainName2 = readDomainName(wrap, n);
                                                                                                        short4 = wrap.getShort();
                                                                                                        wrap.getShort();
                                                                                                        wrap.getInt();
                                                                                                        short5 = wrap.getShort();
                                                                                                        b2 = false;
                                                                                                        break Block_8;
                                                                                                        while (true) {
                                                                                                            wrap.put(DNSResponsePatcher.ipv4_localhost);
                                                                                                            s2 = s3;
                                                                                                            n5 = n6;
                                                                                                            b3 = b4;
                                                                                                            break Label_0550;
                                                                                                            s3 = domainName;
                                                                                                            n6 = n7;
                                                                                                            break Block_10;
                                                                                                            array2 = new byte[short5];
                                                                                                            wrap.get(array2);
                                                                                                            wrap.position(wrap.position() - short5);
                                                                                                            s2 = s3;
                                                                                                            n5 = n6;
                                                                                                            b3 = b2;
                                                                                                            break Block_18;
                                                                                                            s4 = readDomainName(wrap, n);
                                                                                                            break Label_0653;
                                                                                                            Label_0645: {
                                                                                                                s4 = InetAddress.getByAddress(array).getHostAddress();
                                                                                                            }
                                                                                                            break Label_0653;
                                                                                                            b4 = true;
                                                                                                            continue Label_0550_Outer;
                                                                                                        }
                                                                                                        Label_0376: {
                                                                                                            s2 = s3;
                                                                                                        }
                                                                                                        n5 = n6;
                                                                                                        b3 = b4;
                                                                                                        Block_16: {
                                                                                                            break Block_16;
                                                                                                            break Label_0653;
                                                                                                            Label_0610:
                                                                                                            wrap.get(array);
                                                                                                            break Label_0633;
                                                                                                        }
                                                                                                        wrap.put(DNSResponsePatcher.ipv6_localhost);
                                                                                                        s2 = s3;
                                                                                                        n5 = n6;
                                                                                                        b3 = b4;
                                                                                                        continue Label_0550;
                                                                                                        Label_0514:
                                                                                                        s2 = s3;
                                                                                                        n5 = n6;
                                                                                                        b3 = b5;
                                                                                                        break Block_20;
                                                                                                        break Label_0790;
                                                                                                        continue Block_15_Outer;
                                                                                                    }
                                                                                                    Label_0415: {
                                                                                                        s2 = s3;
                                                                                                    }
                                                                                                    n5 = n6;
                                                                                                    b3 = b2;
                                                                                                    continue Block_23_Outer;
                                                                                                }
                                                                                                array = new byte[short5];
                                                                                                wrap.position(wrap.position() - short5);
                                                                                                continue Label_0653_Outer;
                                                                                            }
                                                                                        }
                                                                                        s3 = domainName;
                                                                                        n6 = n7;
                                                                                        continue Label_0217_Outer;
                                                                                    }
                                                                                }
                                                                                b5 = true;
                                                                                wrap.put(DNSResponsePatcher.ipv4_localhost);
                                                                                s2 = s3;
                                                                                n5 = n6;
                                                                                b3 = b5;
                                                                                continue Label_0550;
                                                                            }
                                                                            wrap.put(DNSResponsePatcher.ipv6_localhost);
                                                                            b3 = b5;
                                                                            n5 = n6;
                                                                            s2 = s3;
                                                                            continue Label_0550;
                                                                        }
                                                                        continue Label_0569_Outer;
                                                                    }
                                                                    traffic_LOG2 = DNSResponsePatcher.TRAFFIC_LOG;
                                                                    sb2 = new StringBuilder();
                                                                    sb2.append(s);
                                                                    sb2.append(", A-");
                                                                    sb2.append(short4);
                                                                    sb2.append(", ");
                                                                    sb2.append(domainName2);
                                                                    sb2.append(", ");
                                                                    sb2.append(s4);
                                                                    sb2.append(", /Length:");
                                                                    sb2.append(short5);
                                                                    traffic_LOG2.logLine(sb2.toString());
                                                                    break Label_0803;
                                                                }
                                                                s2 = domainName;
                                                                n5 = n7;
                                                                b3 = b2;
                                                                break Label_0286;
                                                                continue Label_0217_Outer;
                                                            }
                                                            s3 = domainName;
                                                            continue Label_0653_Outer;
                                                        }
                                                        Label_0752: {
                                                            return wrap.array();
                                                        }
                                                    }
                                                    s4 = new String(array);
                                                    continue Label_0653;
                                                }
                                                wrap.position(wrap.position() + short5);
                                                continue Label_0341_Outer;
                                            }
                                        }
                                        catch (Exception ex) {
                                            throw new IOException("Invalid DNS Response Message Structure", ex);
                                        }
                                        catch (IOException ex2) {
                                            throw ex2;
                                        }
                                    }
                                    n4 = 0;
                                }
                                continue Label_0217_Outer;
                            }
                            n8 = 0;
                            n7 = n2;
                            continue Label_0341_Outer;
                        }
                        int n6 = 0;
                        Label_0797: {
                            break Label_0797;
                            Label_0795: {
                                n6 = 1;
                            }
                        }
                        String s3 = domainName2;
                        continue;
                    }
                }
                ++n8;
                String domainName = s2;
                int n7 = n5;
                continue;
            }
        }
    }
    
    private static String readDomainName(final ByteBuffer byteBuffer, final int n) throws IOException {
        final byte[] array = new byte[64];
        String string = "";
        String s = "";
        int i = -1;
        int n2 = -1;
        while (i != 0) {
            final byte value = byteBuffer.get();
            if (value != 0) {
                if ((value & 0xC0) == 0x0) {
                    byteBuffer.get(array, 0, value);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    sb.append(s);
                    sb.append(new String(array, 0, value));
                    string = sb.toString();
                    s = ".";
                    i = value;
                }
                else {
                    byteBuffer.position(byteBuffer.position() - 1);
                    final short short1 = byteBuffer.getShort();
                    int position;
                    if ((position = n2) == -1) {
                        position = byteBuffer.position();
                    }
                    byteBuffer.position((short1 & 0x3FFF) + n);
                    n2 = position;
                    i = value;
                }
            }
            else {
                if ((i = value) != 0) {
                    continue;
                }
                i = value;
                if (n2 == -1) {
                    continue;
                }
                byteBuffer.position(n2);
                i = value;
            }
        }
        return string;
    }
}
