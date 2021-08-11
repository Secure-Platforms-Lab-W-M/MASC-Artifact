/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.ConfigurationAccess;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.Set;
import util.Logger;
import util.LoggerInterface;

public class DNSResponsePatcher {
    private static Set FILTER;
    private static LoggerInterface TRAFFIC_LOG;
    private static boolean checkCNAME;
    private static boolean checkIP;
    private static long filterCnt;
    private static byte[] ipv4_localhost;
    private static byte[] ipv6_localhost;
    private static long okCnt;

    static {
        FILTER = null;
        TRAFFIC_LOG = null;
        okCnt = 0L;
        filterCnt = 0L;
        checkIP = false;
        checkCNAME = true;
        try {
            ipv4_localhost = InetAddress.getByName("127.0.0.1").getAddress();
            ipv6_localhost = InetAddress.getByName("::1").getAddress();
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
        }
    }

    private static boolean filter(String string2) {
        boolean bl = FILTER == null ? false : FILTER.contains(string2);
        if (bl) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FILTERED:");
            stringBuilder.append(string2);
            loggerInterface.logLine(stringBuilder.toString());
        } else {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ALLOWED:");
            stringBuilder.append(string2);
            loggerInterface.logLine(stringBuilder.toString());
        }
        if (!bl) {
            ++okCnt;
            return bl;
        }
        ++filterCnt;
        return bl;
    }

    private static boolean filterIP(String string2) {
        boolean bl;
        Object object;
        StringBuilder stringBuilder;
        if (FILTER == null) {
            bl = false;
        } else {
            object = FILTER;
            stringBuilder = new StringBuilder();
            stringBuilder.append("%IP%");
            stringBuilder.append(string2);
            bl = object.contains(stringBuilder.toString());
        }
        if (bl) {
            object = Logger.getLogger();
            stringBuilder = new StringBuilder();
            stringBuilder.append("FILTERED:");
            stringBuilder.append(string2);
            object.logLine(stringBuilder.toString());
        }
        if (!bl) {
            ++okCnt;
            return bl;
        }
        ++filterCnt;
        return bl;
    }

    public static long getFilterCount() {
        return filterCnt;
    }

    public static long getOkCount() {
        return okCnt;
    }

    public static void init(Set set, LoggerInterface loggerInterface) {
        FILTER = set;
        TRAFFIC_LOG = loggerInterface;
        okCnt = 0L;
        filterCnt = 0L;
        try {
            checkIP = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkResolvedIP", "false"));
            checkCNAME = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkCNAME", "false"));
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] patchResponse(String var0, byte[] var1_3, int var2_4) throws IOException {
        block41 : {
            block43 : {
                block37 : {
                    block38 : {
                        block32 : {
                            block34 : {
                                block36 : {
                                    block35 : {
                                        var15_5 = ByteBuffer.wrap((byte[])var1_3, var2_4, var1_3.length - var2_4);
                                        var15_5.getShort();
                                        var15_5.getShort();
                                        var7_6 = var15_5.getShort();
                                        var9_7 = var15_5.getShort();
                                        var15_5.getShort();
                                        var15_5.getShort();
                                        var3_8 = 0;
                                        var12_9 = "";
                                        var5_10 = 0;
                                        do {
                                            block33 : {
                                                var6_12 = 1;
                                                if (var5_10 >= var7_6) break block32;
                                                var12_9 = DNSResponsePatcher.readDomainName(var15_5, var2_4);
                                                var8_13 = var15_5.getShort();
                                                if (var8_13 != 1) {
                                                    var4_11 = var3_8;
                                                    if (var8_13 != 28) break block33;
                                                }
                                                var4_11 = var6_12;
                                                if (var3_8 != 0) break block33;
                                                if (!DNSResponsePatcher.filter((String)var12_9)) break block34;
                                                var4_11 = var6_12;
                                            }
lbl28: // 3 sources:
                                            if (DNSResponsePatcher.TRAFFIC_LOG != null) {
                                                var1_3 = DNSResponsePatcher.TRAFFIC_LOG;
                                                var13_14 = new StringBuilder();
                                                var13_14.append((String)var0);
                                                var13_14.append(", Q-");
                                                var13_14.append(var8_13);
                                                var13_14.append(", ");
                                                var13_14.append((String)var12_9);
                                                var13_14.append(", <empty>");
                                                var1_3.logLine(var13_14.toString());
                                            }
                                            var15_5.getShort();
                                            ++var5_10;
                                            var3_8 = var4_11;
                                        } while (true);
lbl42: // 2 sources:
                                        if (var6_12 >= var9_7) return var15_5.array();
                                        var14_17 = DNSResponsePatcher.readDomainName(var15_5, var2_4);
                                        var10_15 = var15_5.getShort();
                                        var15_5.getShort();
                                        var15_5.getInt();
                                        var11_16 = var15_5.getShort();
                                        var8_13 = 0;
                                        if (var10_15 == 1) break block35;
                                        var13_14 = var12_9;
                                        var7_6 = var5_10;
                                        var4_11 = var8_13;
                                        if (var10_15 != 28) ** GOTO lbl125
                                    }
                                    var1_3 = var12_9;
                                    var3_8 = var5_10;
                                    if (var5_10 == 0) {
                                        var1_3 = var12_9;
                                        var3_8 = var5_10;
                                        if (!DNSResponsePatcher.checkCNAME) break block36;
                                        var1_3 = var12_9;
                                        var3_8 = var5_10;
                                        if (var14_17.equals(var12_9)) break block36;
                                        if (var5_10 != 0) break block37;
                                        if (DNSResponsePatcher.filter(var14_17)) {
                                        }
                                        break block38;
                                    }
                                }
lbl73: // 2 sources:
                                do {
                                    block39 : {
                                        if (var3_8 != 0) {
                                            var5_10 = 1;
                                            if (var10_15 == 1) {
                                                var15_5.put(DNSResponsePatcher.ipv4_localhost);
                                                var13_14 = var1_3;
                                                var7_6 = var3_8;
                                                var4_11 = var5_10;
                                            }
                                            var13_14 = var1_3;
                                            var7_6 = var3_8;
                                            var4_11 = var5_10;
                                            if (var10_15 == 28) {
                                                var15_5.put(DNSResponsePatcher.ipv6_localhost);
                                                var13_14 = var1_3;
                                                var7_6 = var3_8;
                                                var4_11 = var5_10;
                                            }
                                        } else {
                                            block40 : {
                                                var13_14 = var1_3;
                                                var7_6 = var3_8;
                                                var4_11 = var8_13;
                                                if (!DNSResponsePatcher.checkIP) break block39;
                                                var12_9 = new byte[var11_16];
                                                var15_5.get((byte[])var12_9);
                                                var15_5.position(var15_5.position() - var11_16);
                                                var13_14 = var1_3;
                                                var7_6 = var3_8;
                                                var4_11 = var8_13;
                                                if (!DNSResponsePatcher.filterIP(InetAddress.getByAddress((byte[])var12_9).getHostAddress())) break block39;
                                                var5_10 = 1;
                                                if (var10_15 != 1) break block40;
                                                var15_5.put(DNSResponsePatcher.ipv4_localhost);
                                                var13_14 = var1_3;
                                                var7_6 = var3_8;
                                                var4_11 = var5_10;
                                            }
                                            var13_14 = var1_3;
                                            var7_6 = var3_8;
                                            var4_11 = var5_10;
                                            if (var10_15 == 28) {
                                                var15_5.put(DNSResponsePatcher.ipv6_localhost);
                                                var4_11 = var5_10;
                                                var7_6 = var3_8;
                                                var13_14 = var1_3;
                                            }
                                        }
                                    }
                                    if (var4_11 != 0) ** GOTO lbl129
                                    var15_5.position(var15_5.position() + var11_16);
lbl129: // 2 sources:
                                    if (DNSResponsePatcher.TRAFFIC_LOG == null) break block41;
                                    var1_3 = new byte[var11_16];
                                    var15_5.position(var15_5.position() - var11_16);
                                    if (var10_15 != 5) ** GOTO lbl136
                                    var1_3 = DNSResponsePatcher.readDomainName(var15_5, var2_4);
                                    ** GOTO lbl142
lbl136: // 1 sources:
                                    var15_5.get((byte[])var1_3);
                                    if (var10_15 == 1 || var10_15 == 28) ** GOTO lbl141
                                    try {
                                        block42 : {
                                            var1_3 = new String((byte[])var1_3);
                                            break block42;
lbl141: // 1 sources:
                                            var1_3 = InetAddress.getByAddress((byte[])var1_3).getHostAddress();
                                        }
                                        var12_9 = DNSResponsePatcher.TRAFFIC_LOG;
                                        var16_18 = new StringBuilder();
                                        var16_18.append((String)var0);
                                        var16_18.append(", A-");
                                        var16_18.append(var10_15);
                                        var16_18.append(", ");
                                        var16_18.append(var14_17);
                                        var16_18.append(", ");
                                        var16_18.append((String)var1_3);
                                        var16_18.append(", /Length:");
                                        var16_18.append(var11_16);
                                        var12_9.logLine(var16_18.toString());
                                        break block41;
                                    }
                                    catch (Exception var0_1) {
                                        throw new IOException("Invalid DNS Response Message Structure", var0_1);
                                    }
                                    catch (IOException var0_2) {
                                        throw var0_2;
                                    }
                                    break;
                                } while (true);
                            }
                            var4_11 = 0;
                            ** GOTO lbl28
                        }
                        var6_12 = 0;
                        var5_10 = var3_8;
                        ** GOTO lbl42
                    }
                    var3_8 = 0;
                    break block43;
                }
                var3_8 = 1;
            }
            var1_3 = var14_17;
            ** while (true)
        }
        ++var6_12;
        var12_9 = var13_14;
        var5_10 = var7_6;
        ** GOTO lbl42
    }

    private static String readDomainName(ByteBuffer byteBuffer, int n) throws IOException {
        byte[] arrby = new byte[64];
        String string2 = "";
        String string3 = "";
        int n2 = -1;
        int n3 = -1;
        while (n2 != 0) {
            byte by = byteBuffer.get();
            if (by != 0) {
                if ((by & 192) == 0) {
                    byteBuffer.get(arrby, 0, by);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(string2);
                    stringBuilder.append(string3);
                    stringBuilder.append(new String(arrby, 0, (int)by));
                    string2 = stringBuilder.toString();
                    string3 = ".";
                    n2 = by;
                    continue;
                }
                byteBuffer.position(byteBuffer.position() - 1);
                short s = byteBuffer.getShort();
                n2 = n3;
                if (n3 == -1) {
                    n2 = byteBuffer.position();
                }
                byteBuffer.position((s & 16383) + n);
                n3 = n2;
                n2 = by;
                continue;
            }
            n2 = by;
            if (by != 0) continue;
            n2 = by;
            if (n3 == -1) continue;
            byteBuffer.position(n3);
            n2 = by;
        }
        return string2;
    }
}

