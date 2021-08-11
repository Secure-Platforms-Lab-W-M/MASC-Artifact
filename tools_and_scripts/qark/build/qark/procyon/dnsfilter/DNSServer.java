// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import util.LoggerInterface;
import util.Logger;
import java.net.DatagramPacket;
import java.io.DataInputStream;
import java.io.IOException;
import util.conpool.Connection;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DNSServer
{
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
        DNSServer.bufSize = 1024;
        DNSServer.maxBufSize = -1;
        DNSServer.INSTANCE = new DNSServer(null, 0, 0);
        Connection.setPoolTimeoutSeconds(30);
    }
    
    protected DNSServer(final InetAddress inetAddress, final int n, final int timeout) {
        this.address = new InetSocketAddress(inetAddress, n);
        this.timeout = timeout;
    }
    
    public static int getBufSize() {
        return DNSServer.bufSize;
    }
    
    public static DNSServer getInstance() {
        return DNSServer.INSTANCE;
    }
    
    public static int getProtoFromString(String upperCase) throws IOException {
        upperCase = upperCase.toUpperCase();
        if (upperCase.equals("UDP")) {
            return 0;
        }
        if (upperCase.equals("TCP")) {
            return 1;
        }
        if (upperCase.equals("DOT")) {
            return 2;
        }
        if (upperCase.equals("DOH")) {
            return 3;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid Protocol: ");
        sb.append(upperCase);
        throw new IOException(sb.toString());
    }
    
    public DNSServer createDNSServer(final int n, final InetAddress inetAddress, final int n2, final int n3, final String s) throws IOException {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid protocol:");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            case 3: {
                return new DoH(inetAddress, n2, n3, s);
            }
            case 2: {
                return new TCP(inetAddress, n2, n3, true, s);
            }
            case 1: {
                return new TCP(inetAddress, n2, n3, false, s);
            }
            case 0: {
                return new UDP(inetAddress, n2, n3);
            }
        }
    }
    
    public DNSServer createDNSServer(String s, final int n) throws IOException {
        final String s2 = null;
        String substring = null;
        String substring2;
        if (s.startsWith("[")) {
            final int index = s.indexOf("]");
            substring2 = s;
            if (index != -1) {
                substring = s.substring(1, index);
                substring2 = s.substring(index);
            }
        }
        else {
            final String upperCase = s.toUpperCase();
            substring = s2;
            substring2 = s;
            if (upperCase.indexOf("::UDP") == -1) {
                substring = s2;
                substring2 = s;
                if (upperCase.indexOf("::DOT") == -1) {
                    substring = s2;
                    substring2 = s;
                    if (upperCase.indexOf("::DOH") == -1) {
                        substring2 = "";
                        substring = s;
                    }
                }
            }
        }
        final String[] split = substring2.split("::");
        if ((s = substring) == null) {
            s = split[0];
        }
        int int1 = 53;
        if (split.length > 1) {
            try {
                int1 = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException ex) {
                throw new IOException("Invalid Port!", ex);
            }
        }
        int protoFromString = 0;
        if (split.length > 2) {
            protoFromString = getProtoFromString(split[2]);
        }
        String s3 = null;
        if (split.length > 3) {
            s3 = split[3];
        }
        return getInstance().createDNSServer(protoFromString, InetAddress.getByName(s), int1, n, s3);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o.getClass().equals(this.getClass()) && this.address.equals(((DNSServer)o).address);
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
    
    protected void readResponseFromStream(final DataInputStream dataInputStream, final int length, final DatagramPacket datagramPacket) throws IOException {
        if (datagramPacket.getOffset() + length > datagramPacket.getData().length) {
            synchronized (DNSServer.class) {
                if (DNSServer.maxBufSize == -1) {
                    try {
                        DNSServer.maxBufSize = Integer.parseInt(ConfigurationAccess.getLocal().getConfig().getProperty("MTU", "3000"));
                    }
                    catch (Exception ex) {
                        throw new IOException(ex);
                    }
                    catch (IOException ex2) {
                        throw ex2;
                    }
                }
                if (datagramPacket.getOffset() + length < DNSServer.maxBufSize && DNSServer.bufSize < datagramPacket.getOffset() + length) {
                    DNSServer.bufSize = Math.min(1024 * ((datagramPacket.getOffset() + length) / 1024 + 1), DNSServer.maxBufSize);
                    final LoggerInterface logger = Logger.getLogger();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("BUFFER RESIZE:");
                    sb.append(DNSServer.bufSize);
                    logger.logLine(sb.toString());
                }
                else if (datagramPacket.getOffset() + length >= DNSServer.maxBufSize) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Max Response Buffer to small for response of length ");
                    sb2.append(length);
                    throw new IOException(sb2.toString());
                }
                datagramPacket.setData(new byte[DNSServer.bufSize], datagramPacket.getOffset(), DNSServer.bufSize - datagramPacket.getOffset());
            }
        }
        dataInputStream.readFully(datagramPacket.getData(), datagramPacket.getOffset(), length);
        datagramPacket.setLength(length);
    }
    
    public void resolve(final DatagramPacket datagramPacket, final DatagramPacket datagramPacket2) throws IOException {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.address.getAddress().getHostAddress());
        sb.append("]::");
        sb.append(this.address.getPort());
        sb.append("::");
        sb.append(this.getProtocolName());
        return sb.toString();
    }
}
