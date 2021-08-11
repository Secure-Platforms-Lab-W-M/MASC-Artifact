/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.gifdecoder;

import android.util.Log;
import com.bumptech.glide.gifdecoder.GifFrame;
import com.bumptech.glide.gifdecoder.GifHeader;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class GifHeaderParser {
    static final int DEFAULT_FRAME_DELAY = 10;
    private static final int DESCRIPTOR_MASK_INTERLACE_FLAG = 64;
    private static final int DESCRIPTOR_MASK_LCT_FLAG = 128;
    private static final int DESCRIPTOR_MASK_LCT_SIZE = 7;
    private static final int EXTENSION_INTRODUCER = 33;
    private static final int GCE_DISPOSAL_METHOD_SHIFT = 2;
    private static final int GCE_MASK_DISPOSAL_METHOD = 28;
    private static final int GCE_MASK_TRANSPARENT_COLOR_FLAG = 1;
    private static final int IMAGE_SEPARATOR = 44;
    private static final int LABEL_APPLICATION_EXTENSION = 255;
    private static final int LABEL_COMMENT_EXTENSION = 254;
    private static final int LABEL_GRAPHIC_CONTROL_EXTENSION = 249;
    private static final int LABEL_PLAIN_TEXT_EXTENSION = 1;
    private static final int LSD_MASK_GCT_FLAG = 128;
    private static final int LSD_MASK_GCT_SIZE = 7;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_BLOCK_SIZE = 256;
    static final int MIN_FRAME_DELAY = 2;
    private static final String TAG = "GifHeaderParser";
    private static final int TRAILER = 59;
    private final byte[] block = new byte[256];
    private int blockSize = 0;
    private GifHeader header;
    private ByteBuffer rawData;

    private boolean err() {
        if (this.header.status != 0) {
            return true;
        }
        return false;
    }

    private int read() {
        byte by;
        try {
            by = this.rawData.get();
        }
        catch (Exception exception) {
            this.header.status = 1;
            return 0;
        }
        return by & 255;
    }

    private void readBitmap() {
        this.header.currentFrame.ix = this.readShort();
        this.header.currentFrame.iy = this.readShort();
        this.header.currentFrame.iw = this.readShort();
        this.header.currentFrame.ih = this.readShort();
        int n = this.read();
        boolean bl = false;
        boolean bl2 = (n & 128) != 0;
        int n2 = (int)Math.pow(2.0, (n & 7) + 1);
        Object object = this.header.currentFrame;
        if ((n & 64) != 0) {
            bl = true;
        }
        object.interlace = bl;
        this.header.currentFrame.lct = bl2 ? this.readColorTable(n2) : null;
        this.header.currentFrame.bufferFrameStart = this.rawData.position();
        this.skipImageData();
        if (this.err()) {
            return;
        }
        object = this.header;
        ++object.frameCount;
        this.header.frames.add(this.header.currentFrame);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void readBlock() {
        int n;
        this.blockSize = n = this.read();
        int n2 = 0;
        if (n <= 0) return;
        n = 0;
        do {
            int n3 = n;
            try {
                if (n2 >= this.blockSize) return;
                n3 = n;
                n3 = n = this.blockSize - n2;
                this.rawData.get(this.block, n2, n);
                n2 += n;
            }
            catch (Exception exception) {
                if (Log.isLoggable((String)"GifHeaderParser", (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error Reading Block n: ");
                    stringBuilder.append(n2);
                    stringBuilder.append(" count: ");
                    stringBuilder.append(n3);
                    stringBuilder.append(" blockSize: ");
                    stringBuilder.append(this.blockSize);
                    Log.d((String)"GifHeaderParser", (String)stringBuilder.toString(), (Throwable)exception);
                }
                this.header.status = 1;
                return;
            }
        } while (true);
    }

    private int[] readColorTable(int n) {
        int n2;
        int[] arrn;
        byte[] arrby = new byte[n * 3];
        try {
            this.rawData.get(arrby);
            arrn = new int[256];
            n2 = 0;
        }
        catch (BufferUnderflowException bufferUnderflowException) {
            if (Log.isLoggable((String)"GifHeaderParser", (int)3)) {
                Log.d((String)"GifHeaderParser", (String)"Format Error Reading Color Table", (Throwable)bufferUnderflowException);
            }
            this.header.status = 1;
            return null;
        }
        for (int i = 0; i < n; ++i) {
            int n3 = n2 + 1;
            n2 = arrby[n2];
            int n4 = n3 + 1;
            arrn[i] = -16777216 | (n2 & 255) << 16 | (arrby[n3] & 255) << 8 | arrby[n4] & 255;
            n2 = n4 + 1;
        }
        return arrn;
    }

    private void readContents() {
        this.readContents(Integer.MAX_VALUE);
    }

    private void readContents(int n) {
        boolean bl = false;
        while (!bl && !this.err() && this.header.frameCount <= n) {
            int n2 = this.read();
            if (n2 != 33) {
                if (n2 != 44) {
                    if (n2 != 59) {
                        this.header.status = 1;
                        continue;
                    }
                    bl = true;
                    continue;
                }
                if (this.header.currentFrame == null) {
                    this.header.currentFrame = new GifFrame();
                }
                this.readBitmap();
                continue;
            }
            n2 = this.read();
            if (n2 != 1) {
                if (n2 != 249) {
                    if (n2 != 254) {
                        if (n2 != 255) {
                            this.skip();
                            continue;
                        }
                        this.readBlock();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (n2 = 0; n2 < 11; ++n2) {
                            stringBuilder.append((char)this.block[n2]);
                        }
                        if (stringBuilder.toString().equals("NETSCAPE2.0")) {
                            this.readNetscapeExt();
                            continue;
                        }
                        this.skip();
                        continue;
                    }
                    this.skip();
                    continue;
                }
                this.header.currentFrame = new GifFrame();
                this.readGraphicControlExt();
                continue;
            }
            this.skip();
        }
    }

    private void readGraphicControlExt() {
        this.read();
        int n = this.read();
        int n2 = this.header.currentFrame.dispose = (n & 28) >> 2;
        boolean bl = true;
        if (n2 == 0) {
            this.header.currentFrame.dispose = 1;
        }
        GifFrame gifFrame = this.header.currentFrame;
        if ((n & 1) == 0) {
            bl = false;
        }
        gifFrame.transparency = bl;
        n = n2 = this.readShort();
        if (n2 < 2) {
            n = 10;
        }
        this.header.currentFrame.delay = n * 10;
        this.header.currentFrame.transIndex = this.read();
        this.read();
    }

    private void readHeader() {
        Object object = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            object.append((char)this.read());
        }
        if (!object.toString().startsWith("GIF")) {
            this.header.status = 1;
            return;
        }
        this.readLSD();
        if (this.header.gctFlag && !this.err()) {
            object = this.header;
            object.gct = this.readColorTable(object.gctSize);
            object = this.header;
            object.bgColor = object.gct[this.header.bgIndex];
        }
    }

    private void readLSD() {
        this.header.width = this.readShort();
        this.header.height = this.readShort();
        int n = this.read();
        GifHeader gifHeader = this.header;
        boolean bl = (n & 128) != 0;
        gifHeader.gctFlag = bl;
        this.header.gctSize = (int)Math.pow(2.0, (n & 7) + 1);
        this.header.bgIndex = this.read();
        this.header.pixelAspect = this.read();
    }

    private void readNetscapeExt() {
        do {
            this.readBlock();
            byte[] arrby = this.block;
            if (arrby[0] != 1) continue;
            byte by = arrby[1];
            byte by2 = arrby[2];
            this.header.loopCount = (by2 & 255) << 8 | by & 255;
        } while (this.blockSize > 0 && !this.err());
    }

    private int readShort() {
        return this.rawData.getShort();
    }

    private void reset() {
        this.rawData = null;
        Arrays.fill(this.block, (byte)0);
        this.header = new GifHeader();
        this.blockSize = 0;
    }

    private void skip() {
        int n;
        do {
            n = this.read();
            int n2 = Math.min(this.rawData.position() + n, this.rawData.limit());
            this.rawData.position(n2);
        } while (n > 0);
    }

    private void skipImageData() {
        this.read();
        this.skip();
    }

    public void clear() {
        this.rawData = null;
        this.header = null;
    }

    public boolean isAnimated() {
        this.readHeader();
        if (!this.err()) {
            this.readContents(2);
        }
        if (this.header.frameCount > 1) {
            return true;
        }
        return false;
    }

    public GifHeader parseHeader() {
        if (this.rawData != null) {
            if (this.err()) {
                return this.header;
            }
            this.readHeader();
            if (!this.err()) {
                this.readContents();
                if (this.header.frameCount < 0) {
                    this.header.status = 1;
                }
            }
            return this.header;
        }
        throw new IllegalStateException("You must call setData() before parseHeader()");
    }

    public GifHeaderParser setData(ByteBuffer byteBuffer) {
        this.reset();
        this.rawData = byteBuffer = byteBuffer.asReadOnlyBuffer();
        byteBuffer.position(0);
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public GifHeaderParser setData(byte[] arrby) {
        if (arrby != null) {
            this.setData(ByteBuffer.wrap(arrby));
            return this;
        }
        this.rawData = null;
        this.header.status = 2;
        return this;
    }
}

