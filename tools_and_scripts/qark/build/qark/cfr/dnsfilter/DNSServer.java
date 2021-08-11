/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.ConfigurationAccess;
import dnsfilter.DoH;
import dnsfilter.TCP;
import dnsfilter.UDP;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;
import util.Logger;
import util.LoggerInterface;
import util.conpool.Connection;

public class DNSServer {
    public static final int DOH = 3;
    public static final int DOT = 2;
    private static DNSServer INSTANCE;
    public static final int TCP = 1;
    public static final int UDP = 0;
    private static int bufSize;
    private static int maxBufSize;
    protected InetSocketAddress address;
    protected int timeout;

    static {
        bufSize = 1024;
        maxBufSize = -1;
        INSTANCE = new DNSServer(null, 0, 0);
        Connection.setPoolTimeoutSeconds(30);
    }

    protected DNSServer(InetAddress inetAddress, int n, int n2) {
        this.address = new InetSocketAddress(inetAddress, n);
        this.timeout = n2;
    }

    public static int getBufSize() {
        return bufSize;
    }

    public static DNSServer getInstance() {
        return INSTANCE;
    }

    public static int getProtoFromString(String string2) throws IOException {
        if ((string2 = string2.toUpperCase()).equals("UDP")) {
            return 0;
        }
        if (string2.equals("TCP")) {
            return 1;
        }
        if (string2.equals("DOT")) {
            return 2;
        }
        if (string2.equals("DOH")) {
            return 3;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Protocol: ");
        stringBuilder.append(string2);
        throw new IOException(stringBuilder.toString());
    }

    public DNSServer createDNSServer(int n, InetAddress serializable, int n2, int n3, String string2) throws IOException {
        switch (n) {
            default: {
                serializable = new StringBuilder();
                serializable.append("Invalid protocol:");
                serializable.append(n);
                throw new IllegalArgumentException(serializable.toString());
            }
            case 3: {
                return new DoH((InetAddress)serializable, n2, n3, string2);
            }
            case 2: {
                return new TCP((InetAddress)serializable, n2, n3, true, string2);
            }
            case 1: {
                return new TCP((InetAddress)serializable, n2, n3, false, string2);
            }
            case 0: 
        }
        return new UDP((InetAddress)serializable, n2, n3);
    }

    public DNSServer createDNSServer(String object, int n) throws IOException {
        int n2;
        Object object2;
        String string2 = null;
        Object object3 = null;
        if (object.startsWith("[")) {
            n2 = object.indexOf("]");
            object2 = object;
            if (n2 != -1) {
                object3 = object.substring(1, n2);
                object2 = object.substring(n2);
            }
        } else {
            String string3 = object.toUpperCase();
            object3 = string2;
            object2 = object;
            if (string3.indexOf("::UDP") == -1) {
                object3 = string2;
                object2 = object;
                if (string3.indexOf("::DOT") == -1) {
                    object3 = string2;
                    object2 = object;
                    if (string3.indexOf("::DOH") == -1) {
                        object2 = "";
                        object3 = object;
                    }
                }
            }
        }
        object2 = object2.split("::");
        object = object3;
        if (object3 == null) {
            object = object2[0];
        }
        n2 = 53;
        if (object2.length > 1) {
            try {
                n2 = Integer.parseInt((String)object2[1]);
            }
            catch (NumberFormatException numberFormatException) {
                throw new IOException("Invalid Port!", numberFormatException);
            }
        }
        int n3 = 0;
        if (object2.length > 2) {
            n3 = DNSServer.getProtoFromString((String)object2[2]);
        }
        object3 = null;
        if (object2.length > 3) {
            object3 = object2[3];
        }
        return DNSServer.getInstance().createDNSServer(n3, InetAddress.getByName((String)object), n2, n, (String)object3);
    }

    public boolean equals(Object object) {
        if (object != null && object.getClass().equals(this.getClass())) {
            return this.address.equals(((DNSServer)object).address);
        }
        return false;
    }

    public InetAddress getAddress() {
        return this.address.getAddress();
    }

    public int getPort() {
        return this.address.getPort();
    }

    public String getProtocolName() {
        return "";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void readResponseFromStream(DataInputStream object, int n, DatagramPacket datagramPacket) throws IOException {
        if (datagramPacket.getOffset() + n > datagramPacket.getData().length) {
            synchronized (DNSServer.class) {
                int n2 = maxBufSize;
                if (n2 == -1) {
                    try {
                        maxBufSize = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("MTU", "3000"));
                    }
                    catch (Exception exception) {
                        throw new IOException(exception);
                    }
                    catch (IOException iOException) {
                        throw iOException;
                    }
                }
                if (datagramPacket.getOffset() + n < maxBufSize && bufSize < datagramPacket.getOffset() + n) {
                    bufSize = Math.min(1024 * ((datagramPacket.getOffset() + n) / 1024 + 1), maxBufSize);
                    LoggerInterface loggerInterface = Logger.getLogger();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("BUFFER RESIZE:");
                    stringBuilder.append(bufSize);
                    loggerInterface.logLine(stringBuilder.toString());
                } else if (datagramPacket.getOffset() + n >= maxBufSize) {
                    object = new StringBuilder();
                    object.append("Max Response Buffer to small for response of length ");
                    object.append(n);
                    throw new IOException(object.toString());
                }
                datagramPacket.setData(new byte[bufSize], datagramPacket.getOffset(), bufSize - datagramPacket.getOffset());
            }
        }
        object.readFully(datagramPacket.getData(), datagramPacket.getOffset(), n);
        datagramPacket.setLength(n);
    }

    public void resolve(DatagramPacket datagramPacket, DatagramPacket datagramPacket2) throws IOException {
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.address.getAddress().getHostAddress());
        stringBuilder.append("]::");
        stringBuilder.append(this.address.getPort());
        stringBuilder.append("::");
        stringBuilder.append(this.getProtocolName());
        return stringBuilder.toString();
    }
}

