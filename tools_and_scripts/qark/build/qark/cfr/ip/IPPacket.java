/*
 * Decompiled with CFR 0_124.
 */
package ip;

import ip.CheckSum;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class IPPacket {
    static Object ID_SYNC;
    static short curID;
    protected byte[] data;
    protected int ipHdrlen;
    protected IntBuffer ipHeader;
    protected int len;
    protected int offset;
    protected int version = 0;

    static {
        curID = (short)(Math.random() * 32767.0);
        ID_SYNC = new Object();
    }

    public IPPacket(byte[] object, int n, int n2) {
        this.version = object[n] >> 4;
        this.data = object;
        this.offset = n;
        this.len = n2;
        if (this.version == 4) {
            this.ipHeader = ByteBuffer.wrap((byte[])object, n, 20).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
            this.ipHdrlen = 20;
            return;
        }
        if (this.version == 6) {
            this.ipHeader = ByteBuffer.wrap((byte[])object, n, 40).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
            this.ipHdrlen = 40;
            return;
        }
        object = new StringBuilder();
        object.append("Invalid Version:");
        object.append(this.version);
        throw new IllegalArgumentException(object.toString());
    }

    private int calculateCheckSum() {
        if (this.version == 4) {
            return CheckSum.chkSum(this.data, this.offset, 20);
        }
        return 0;
    }

    private int[] copyFromHeader(int n, int n2) {
        this.ipHeader.position(n);
        int[] arrn = new int[n2];
        this.ipHeader.get(arrn, 0, n2);
        return arrn;
    }

    public static IPPacket createIPPacket(byte[] object, int n, int n2, int n3, int n4, int n5, int[] arrn, int[] arrn2) {
        object[n] = (byte)(n3 << 4 & 255);
        object = new IPPacket((byte[])object, n, n2);
        object.initIPHeader(n4, n5, arrn, arrn2);
        return object;
    }

    public static IPPacket createInitialIPPacket(byte[] object, int n, int n2, int n3) {
        object[n] = (byte)(n3 << 4 & 255);
        object = new IPPacket((byte[])object, n, n2);
        object.initInitialIPHeader();
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int generateId() {
        Object object = ID_SYNC;
        synchronized (object) {
            short s = IPPacket.curID = (short)(curID + 1);
            return s << 16;
        }
    }

    public static InetAddress int2ip(int[] arrn) throws UnknownHostException {
        block4 : {
            block3 : {
                block2 : {
                    if (arrn.length != 1) break block2;
                    byte[] arrby = new byte[]{(byte)(arrn[0] >> 24 & 255), (byte)(arrn[0] >> 16 & 255), (byte)(arrn[0] >> 8 & 255), (byte)(arrn[0] & 255)};
                    arrn = arrby;
                    break block3;
                }
                if (arrn.length != 4) break block4;
                arrn = new byte[]{(byte)(arrn[0] >> 24 & 255), (byte)(arrn[0] >> 16 & 255), (byte)(arrn[0] >> 8 & 255), (byte)(arrn[0] & 255), (byte)(arrn[1] >> 24 & 255), (byte)(arrn[1] >> 16 & 255), (byte)(arrn[1] >> 8 & 255), (byte)(arrn[1] & 255), (byte)(arrn[2] >> 24 & 255), (byte)(arrn[2] >> 16 & 255), (byte)(arrn[2] >> 8 & 255), (byte)(arrn[2] & 255), (byte)(arrn[3] >> 24 & 255), (byte)(arrn[3] >> 16 & 255), (byte)(arrn[3] >> 8 & 255), (byte)(arrn[3] & 255)};
            }
            return InetAddress.getByAddress((byte[])arrn);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid array length:");
        stringBuilder.append(arrn.length);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static int[] ip2int(InetAddress arrby) {
        if ((arrby = arrby.getAddress()).length == 4) {
            byte by = arrby[3];
            return new int[]{(arrby[2] & 255) << 8 | by & 255 | (arrby[1] & 255) << 16 | (arrby[0] & 255) << 24};
        }
        byte by = arrby[3];
        byte by2 = arrby[2];
        byte by3 = arrby[1];
        byte by4 = arrby[0];
        byte by5 = arrby[7];
        byte by6 = arrby[6];
        byte by7 = arrby[5];
        return new int[]{by & 255 | (by2 & 255) << 8 | (by3 & 255) << 16 | (by4 & 255) << 24, (arrby[4] & 255) << 24 | (by5 & 255 | (by6 & 255) << 8 | (by7 & 255) << 16), arrby[11] & 255 | (arrby[10] & 255) << 8 | (arrby[9] & 255) << 16 | (arrby[8] & 255) << 24, arrby[15] & 255 | (arrby[14] & 255) << 8 | (arrby[13] & 255) << 16 | (arrby[12] & 255) << 24};
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getHeaderLength() {
        return this.ipHdrlen;
    }

    public int getLength() {
        if (this.version == 4) {
            return this.ipHeader.get(0) & 65535;
        }
        if (this.version == 6) {
            return 40 + (this.ipHeader.get(1) >>> 16);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getOffset() {
        return this.offset;
    }

    public int getProt() {
        if (this.version == 4) {
            return this.ipHeader.get(2) >>> 16 & 255;
        }
        if (this.version == 6) {
            return this.ipHeader.get(1) >>> 8 & 255;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int[] getSourceIP() {
        if (this.version == 4) {
            return this.copyFromHeader(3, 1);
        }
        if (this.version == 6) {
            return this.copyFromHeader(2, 4);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getTTL() {
        if (this.version == 4) {
            return this.ipHeader.get(2) >>> 24;
        }
        if (this.version == 6) {
            return this.ipHeader.get(1) & 255;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getVersion() {
        return this.version;
    }

    protected void initIPHeader(int n, int n2, int[] object, int[] arrn) {
        if (this.version == 4) {
            int[] arrn2 = new int[]{1157627904 + this.len, IPPacket.generateId(), (n << 24) + (n2 << 16), object[0], arrn[0]};
            this.ipHeader.position(0);
            this.ipHeader.put(arrn2);
            arrn2[2] = arrn2[2] + this.calculateCheckSum();
            this.ipHeader.put(2, arrn2[2]);
            return;
        }
        if (this.version == 6) {
            int n3 = this.version;
            int n4 = this.len;
            this.ipHeader.position(0);
            this.ipHeader.put(new int[]{n3 << 28, (n4 - 40 << 16) + (n2 << 8) + n});
            this.ipHeader.put((int[])object);
            this.ipHeader.put(arrn);
            return;
        }
        object = new StringBuilder();
        object.append("Illegal Version:");
        object.append(this.version);
        throw new IllegalStateException(object.toString());
    }

    protected void initInitialIPHeader() {
        if (this.version == 4) {
            int n = this.len;
            int n2 = IPPacket.generateId();
            this.ipHeader.position(0);
            this.ipHeader.put(new int[]{1157627904 + n, n2, 0, 0, 0});
            return;
        }
        if (this.version == 6) {
            int n = this.version;
            int n3 = this.len;
            this.ipHeader.position(0);
            this.ipHeader.put(new int[]{n << 28, n3 - 40 << 16});
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Version:");
        stringBuilder.append(this.version);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void updateHeader(int n, int n2, int[] object, int[] arrn) {
        if (this.version == 4) {
            int[] arrn2 = new int[3];
            this.ipHeader.position(2);
            this.ipHeader.get(arrn2);
            arrn2[0] = (n << 24) + (n2 << 16);
            arrn2[1] = object[0];
            arrn2[2] = arrn[0];
            this.ipHeader.position(2);
            this.ipHeader.put(arrn2);
            arrn2[0] = arrn2[0] + this.calculateCheckSum();
            this.ipHeader.put(2, arrn2[0]);
            return;
        }
        if (this.version == 6) {
            int n3 = this.ipHeader.get(1);
            this.ipHeader.position(1);
            this.ipHeader.put((-65536 & n3) + (n2 << 8) + n);
            this.ipHeader.put((int[])object);
            this.ipHeader.put(arrn);
            return;
        }
        object = new StringBuilder();
        object.append("Illegal Version:");
        object.append(this.version);
        throw new IllegalStateException(object.toString());
    }
}

