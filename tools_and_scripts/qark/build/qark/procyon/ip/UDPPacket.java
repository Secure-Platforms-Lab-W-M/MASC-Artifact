// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package ip;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class UDPPacket extends IPPacket
{
    private IntBuffer udpHeader;
    
    public UDPPacket(final byte[] array, final int n, final int n2) {
        super(array, n, n2);
        this.udpHeader = ByteBuffer.wrap(array, this.ipHdrlen + n, 8).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
    }
    
    private int calculateCheckSum(final boolean b) {
        int n;
        if (this.version == 4) {
            final int value = this.ipHeader.get(2);
            this.ipHeader.put(2, 1114112 + this.len - this.ipHdrlen);
            n = CheckSum.chkSum(this.data, this.offset + 8, this.len - 8);
            this.ipHeader.put(2, value);
        }
        else {
            if (this.version != 6) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Illegal version:");
                sb.append(this.version);
                throw new IllegalStateException(sb.toString());
            }
            final int value2 = this.ipHeader.get(0);
            final int value3 = this.ipHeader.get(1);
            this.ipHeader.put(0, this.len - this.ipHdrlen);
            this.ipHeader.put(1, 17);
            n = CheckSum.chkSum(this.data, this.offset, this.len);
            this.ipHeader.position(0);
            this.ipHeader.put(new int[] { value2, value3 });
        }
        int n2 = n;
        if (b && (n2 = n) == 0) {
            n2 = 65535;
        }
        return n2;
    }
    
    public static UDPPacket createUDPPacket(final byte[] array, final int n, final int n2, final int n3) {
        array[n] = (byte)(n3 << 4 & 0xFF);
        final UDPPacket udpPacket = new UDPPacket(array, n, n2);
        udpPacket.initInitialIPHeader();
        return udpPacket;
    }
    
    @Override
    public int checkCheckSum() {
        return this.calculateCheckSum(false);
    }
    
    public int getDestPort() {
        return this.udpHeader.get(0) & 0xFFFF;
    }
    
    @Override
    public int getHeaderLength() {
        return this.ipHdrlen + 8;
    }
    
    public int getIPPacketLength() {
        return super.getLength();
    }
    
    public int getIPPacketOffset() {
        return super.getOffset();
    }
    
    @Override
    public int getLength() {
        return this.udpHeader.get(1) >>> 16;
    }
    
    @Override
    public int getOffset() {
        return super.getOffset() + this.ipHdrlen;
    }
    
    public int getSourcePort() {
        return this.udpHeader.get(0) >>> 16;
    }
    
    public void updateHeader(final int n, final int n2) {
        final int[] array = { (n << 16) + n2, this.len - this.ipHdrlen << 16 };
        this.udpHeader.position(0);
        this.udpHeader.put(array);
        array[1] += this.calculateCheckSum(true);
        this.udpHeader.put(1, array[1]);
    }
}
