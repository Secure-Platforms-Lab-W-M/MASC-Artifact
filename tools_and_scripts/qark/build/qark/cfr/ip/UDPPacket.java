/*
 * Decompiled with CFR 0_124.
 */
package ip;

import ip.CheckSum;
import ip.IPPacket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class UDPPacket
extends IPPacket {
    private IntBuffer udpHeader;

    public UDPPacket(byte[] arrby, int n, int n2) {
        super(arrby, n, n2);
        this.udpHeader = ByteBuffer.wrap(arrby, this.ipHdrlen + n, 8).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
    }

    private int calculateCheckSum(boolean bl) {
        block7 : {
            int n;
            int n2;
            block6 : {
                block5 : {
                    if (this.version != 4) break block5;
                    n = this.ipHeader.get(2);
                    this.ipHeader.put(2, 1114112 + this.len - this.ipHdrlen);
                    n2 = CheckSum.chkSum(this.data, this.offset + 8, this.len - 8);
                    this.ipHeader.put(2, n);
                    break block6;
                }
                if (this.version != 6) break block7;
                n = this.ipHeader.get(0);
                int n3 = this.ipHeader.get(1);
                this.ipHeader.put(0, this.len - this.ipHdrlen);
                this.ipHeader.put(1, 17);
                n2 = CheckSum.chkSum(this.data, this.offset, this.len);
                this.ipHeader.position(0);
                this.ipHeader.put(new int[]{n, n3});
            }
            n = n2;
            if (bl) {
                n = n2;
                if (n2 == 0) {
                    n = 65535;
                }
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public static UDPPacket createUDPPacket(byte[] object, int n, int n2, int n3) {
        object[n] = (byte)(n3 << 4 & 255);
        object = new UDPPacket((byte[])object, n, n2);
        object.initInitialIPHeader();
        return object;
    }

    @Override
    public int checkCheckSum() {
        return this.calculateCheckSum(false);
    }

    public int getDestPort() {
        return this.udpHeader.get(0) & 65535;
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

    public void updateHeader(int n, int n2) {
        int[] arrn = new int[]{(n << 16) + n2, this.len - this.ipHdrlen << 16};
        this.udpHeader.position(0);
        this.udpHeader.put(arrn);
        arrn[1] = arrn[1] + this.calculateCheckSum(true);
        this.udpHeader.put(1, arrn[1]);
    }
}

