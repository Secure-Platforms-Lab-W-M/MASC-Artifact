/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ExifOrientationStream
extends FilterInputStream {
    private static final byte[] EXIF_SEGMENT;
    private static final int ORIENTATION_POSITION;
    private static final int SEGMENT_LENGTH;
    private static final int SEGMENT_START_POSITION = 2;
    private final byte orientation;
    private int position;

    static {
        int n;
        byte[] arrby;
        byte[] arrby2 = arrby = new byte[29];
        arrby2[0] = -1;
        arrby2[1] = -31;
        arrby2[2] = 0;
        arrby2[3] = 28;
        arrby2[4] = 69;
        arrby2[5] = 120;
        arrby2[6] = 105;
        arrby2[7] = 102;
        arrby2[8] = 0;
        arrby2[9] = 0;
        arrby2[10] = 77;
        arrby2[11] = 77;
        arrby2[12] = 0;
        arrby2[13] = 0;
        arrby2[14] = 0;
        arrby2[15] = 0;
        arrby2[16] = 0;
        arrby2[17] = 8;
        arrby2[18] = 0;
        arrby2[19] = 1;
        arrby2[20] = 1;
        arrby2[21] = 18;
        arrby2[22] = 0;
        arrby2[23] = 2;
        arrby2[24] = 0;
        arrby2[25] = 0;
        arrby2[26] = 0;
        arrby2[27] = 1;
        arrby2[28] = 0;
        EXIF_SEGMENT = arrby;
        SEGMENT_LENGTH = n = arrby.length;
        ORIENTATION_POSITION = n + 2;
    }

    public ExifOrientationStream(InputStream object, int n) {
        super((InputStream)object);
        if (n >= -1 && n <= 8) {
            this.orientation = (byte)n;
            return;
        }
        object = new StringBuilder();
        object.append("Cannot add invalid orientation: ");
        object.append(n);
        throw new IllegalArgumentException(object.toString());
    }

    @Override
    public void mark(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        int n;
        int n2 = this.position;
        n2 = n2 >= 2 && n2 <= (n = ORIENTATION_POSITION) ? (n2 == n ? (int)this.orientation : EXIF_SEGMENT[n2 - 2] & 255) : super.read();
        if (n2 != -1) {
            ++this.position;
        }
        return n2;
    }

    @Override
    public int read(byte[] arrby, int n, int n2) throws IOException {
        int n3 = this.position;
        int n4 = ORIENTATION_POSITION;
        if (n3 > n4) {
            n = super.read(arrby, n, n2);
        } else if (n3 == n4) {
            arrby[n] = this.orientation;
            n = 1;
        } else if (n3 < 2) {
            n = super.read(arrby, n, 2 - n3);
        } else {
            n2 = Math.min(n4 - n3, n2);
            System.arraycopy(EXIF_SEGMENT, this.position - 2, arrby, n, n2);
            n = n2;
        }
        if (n > 0) {
            this.position += n;
        }
        return n;
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long l) throws IOException {
        if ((l = super.skip(l)) > 0L) {
            this.position = (int)((long)this.position + l);
        }
        return l;
    }
}

