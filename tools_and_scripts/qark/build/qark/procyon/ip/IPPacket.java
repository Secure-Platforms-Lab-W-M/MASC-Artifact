// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package ip;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class IPPacket
{
    static Object ID_SYNC;
    static short curID;
    protected byte[] data;
    protected int ipHdrlen;
    protected IntBuffer ipHeader;
    protected int len;
    protected int offset;
    protected int version;
    
    static {
        IPPacket.curID = (short)(Math.random() * 32767.0);
        IPPacket.ID_SYNC = new Object();
    }
    
    public IPPacket(final byte[] data, final int offset, final int len) {
        this.version = 0;
        this.version = data[offset] >> 4;
        this.data = data;
        this.offset = offset;
        this.len = len;
        if (this.version == 4) {
            this.ipHeader = ByteBuffer.wrap(data, offset, 20).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
            this.ipHdrlen = 20;
            return;
        }
        if (this.version == 6) {
            this.ipHeader = ByteBuffer.wrap(data, offset, 40).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
            this.ipHdrlen = 40;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid Version:");
        sb.append(this.version);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private int calculateCheckSum() {
        if (this.version == 4) {
            return CheckSum.chkSum(this.data, this.offset, 20);
        }
        return 0;
    }
    
    private int[] copyFromHeader(final int n, final int n2) {
        this.ipHeader.position(n);
        final int[] array = new int[n2];
        this.ipHeader.get(array, 0, n2);
        return array;
    }
    
    public static IPPacket createIPPacket(final byte[] array, final int n, final int n2, final int n3, final int n4, final int n5, final int[] array2, final int[] array3) {
        array[n] = (byte)(n3 << 4 & 0xFF);
        final IPPacket ipPacket = new IPPacket(array, n, n2);
        ipPacket.initIPHeader(n4, n5, array2, array3);
        return ipPacket;
    }
    
    public static IPPacket createInitialIPPacket(final byte[] array, final int n, final int n2, final int n3) {
        array[n] = (byte)(n3 << 4 & 0xFF);
        final IPPacket ipPacket = new IPPacket(array, n, n2);
        ipPacket.initInitialIPHeader();
        return ipPacket;
    }
    
    private static int generateId() {
        synchronized (IPPacket.ID_SYNC) {
            ++IPPacket.curID;
            final short curID = IPPacket.curID;
            // monitorexit(IPPacket.ID_SYNC)
            return curID << 16;
        }
    }
    
    public static InetAddress int2ip(final int[] array) throws UnknownHostException {
        byte[] array2;
        if (array.length == 1) {
            array2 = new byte[] { (byte)(array[0] >> 24 & 0xFF), (byte)(array[0] >> 16 & 0xFF), (byte)(array[0] >> 8 & 0xFF), (byte)(array[0] & 0xFF) };
        }
        else {
            if (array.length != 4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid array length:");
                sb.append(array.length);
                throw new IllegalArgumentException(sb.toString());
            }
            array2 = new byte[] { (byte)(array[0] >> 24 & 0xFF), (byte)(array[0] >> 16 & 0xFF), (byte)(array[0] >> 8 & 0xFF), (byte)(array[0] & 0xFF), (byte)(array[1] >> 24 & 0xFF), (byte)(array[1] >> 16 & 0xFF), (byte)(array[1] >> 8 & 0xFF), (byte)(array[1] & 0xFF), (byte)(array[2] >> 24 & 0xFF), (byte)(array[2] >> 16 & 0xFF), (byte)(array[2] >> 8 & 0xFF), (byte)(array[2] & 0xFF), (byte)(array[3] >> 24 & 0xFF), (byte)(array[3] >> 16 & 0xFF), (byte)(array[3] >> 8 & 0xFF), (byte)(array[3] & 0xFF) };
        }
        return InetAddress.getByAddress(array2);
    }
    
    public static int[] ip2int(final InetAddress inetAddress) {
        final byte[] address = inetAddress.getAddress();
        if (address.length == 4) {
            return new int[] { (address[2] & 0xFF) << 8 | (address[3] & 0xFF) | (address[1] & 0xFF) << 16 | (address[0] & 0xFF) << 24 };
        }
        return new int[] { (address[3] & 0xFF) | (address[2] & 0xFF) << 8 | (address[1] & 0xFF) << 16 | (address[0] & 0xFF) << 24, (address[4] & 0xFF) << 24 | ((address[7] & 0xFF) | (address[6] & 0xFF) << 8 | (address[5] & 0xFF) << 16), (address[11] & 0xFF) | (address[10] & 0xFF) << 8 | (address[9] & 0xFF) << 16 | (address[8] & 0xFF) << 24, (address[15] & 0xFF) | (address[14] & 0xFF) << 8 | (address[13] & 0xFF) << 16 | (address[12] & 0xFF) << 24 };
    }
    
    public int checkCheckSum() {
        return this.calculateCheckSum();
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public int[] getDestIP() {
        if (this.version == 4) {
            return this.copyFromHeader(4, 1);
        }
        if (this.version == 6) {
            return this.copyFromHeader(6, 4);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public int getHeaderLength() {
        return this.ipHdrlen;
    }
    
    public int getLength() {
        if (this.version == 4) {
            return this.ipHeader.get(0) & 0xFFFF;
        }
        if (this.version == 6) {
            return 40 + (this.ipHeader.get(1) >>> 16);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public int getProt() {
        if (this.version == 4) {
            return this.ipHeader.get(2) >>> 16 & 0xFF;
        }
        if (this.version == 6) {
            return this.ipHeader.get(1) >>> 8 & 0xFF;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public int[] getSourceIP() {
        if (this.version == 4) {
            return this.copyFromHeader(3, 1);
        }
        if (this.version == 6) {
            return this.copyFromHeader(2, 4);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public int getTTL() {
        if (this.version == 4) {
            return this.ipHeader.get(2) >>> 24;
        }
        if (this.version == 6) {
            return this.ipHeader.get(1) & 0xFF;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public int getVersion() {
        return this.version;
    }
    
    protected void initIPHeader(final int n, final int n2, final int[] array, final int[] array2) {
        if (this.version == 4) {
            final int[] array3 = { 1157627904 + this.len, generateId(), (n << 24) + (n2 << 16), array[0], array2[0] };
            this.ipHeader.position(0);
            this.ipHeader.put(array3);
            array3[2] += this.calculateCheckSum();
            this.ipHeader.put(2, array3[2]);
            return;
        }
        if (this.version == 6) {
            final int version = this.version;
            final int len = this.len;
            this.ipHeader.position(0);
            this.ipHeader.put(new int[] { version << 28, (len - 40 << 16) + (n2 << 8) + n });
            this.ipHeader.put(array);
            this.ipHeader.put(array2);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    protected void initInitialIPHeader() {
        if (this.version == 4) {
            final int len = this.len;
            final int generateId = generateId();
            this.ipHeader.position(0);
            this.ipHeader.put(new int[] { 1157627904 + len, generateId, 0, 0, 0 });
            return;
        }
        if (this.version == 6) {
            final int version = this.version;
            final int len2 = this.len;
            this.ipHeader.position(0);
            this.ipHeader.put(new int[] { version << 28, len2 - 40 << 16 });
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
    
    public void updateHeader(final int n, final int n2, final int[] array, final int[] array2) {
        if (this.version == 4) {
            final int[] array3 = new int[3];
            this.ipHeader.position(2);
            this.ipHeader.get(array3);
            array3[0] = (n << 24) + (n2 << 16);
            array3[1] = array[0];
            array3[2] = array2[0];
            this.ipHeader.position(2);
            this.ipHeader.put(array3);
            array3[0] += this.calculateCheckSum();
            this.ipHeader.put(2, array3[0]);
            return;
        }
        if (this.version == 6) {
            final int value = this.ipHeader.get(1);
            this.ipHeader.position(1);
            this.ipHeader.put((0xFFFF0000 & value) + (n2 << 8) + n);
            this.ipHeader.put(array);
            this.ipHeader.put(array2);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal Version:");
        sb.append(this.version);
        throw new IllegalStateException(sb.toString());
    }
}
