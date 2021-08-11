/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import util.Encryption;

public class PaddingCipherInputStream
extends InputStream {
    private ByteArrayInputStream byteBuf;
    private boolean eof = false;
    private boolean init = false;
    private DataInputStream lowerIn;

    public PaddingCipherInputStream(InputStream inputStream) throws IOException {
        this.lowerIn = new DataInputStream(inputStream);
    }

    private ByteArrayInputStream getNewBytes() throws IOException {
        Object object;
        try {
            object = new byte[this.lowerIn.readInt()];
            this.lowerIn.readFully((byte[])object);
        }
        catch (EOFException eOFException) {
            this.eof = true;
            this.byteBuf = null;
            return null;
        }
        if (object.length == 0) {
            this.byteBuf = null;
            this.eof = true;
            return null;
        }
        try {
            this.byteBuf = new ByteArrayInputStream(Encryption.decrypt((byte[])object));
            object = this.byteBuf;
            return object;
        }
        catch (Exception exception) {
            throw new IOException(exception.getMessage());
        }
    }

    private void initRead() throws IOException {
        if (this.init) {
            return;
        }
        int n = -1;
        this.init = true;
        try {
            int n2;
            n = n2 = this.lowerIn.readInt();
        }
        catch (EOFException eOFException) {
            this.eof = true;
        }
        if (!this.eof) {
            if (n != Encryption.ENCR_INIT_BYTES.length) {
                throw new IOException("Wrong keyphrase!");
            }
            byte[] arrby = new byte[n];
            this.lowerIn.readFully(arrby);
            for (n = 0; n < arrby.length; ++n) {
                if (arrby[n] == Encryption.ENCR_INIT_BYTES[n]) continue;
                throw new IOException("Wrong keyphrase!");
            }
        }
    }

    @Override
    public int available() throws IOException {
        if (this.byteBuf != null) {
            return this.byteBuf.available();
        }
        return 0;
    }

    @Override
    public void close() throws IOException {
        this.lowerIn.close();
    }

    @Override
    public int read() throws IOException {
        if (!this.init) {
            this.initRead();
        }
        if (this.eof) {
            return -1;
        }
        if (this.byteBuf == null || this.byteBuf.available() == 0) {
            this.byteBuf = this.getNewBytes();
            if (this.byteBuf == null) {
                return -1;
            }
        }
        return this.byteBuf.read();
    }

    @Override
    public int read(byte[] arrby, int n, int n2) throws IOException {
        if (!this.init) {
            this.initRead();
        }
        if (this.available() != 0) {
            return this.byteBuf.read(arrby, n, n2);
        }
        if (this.eof) {
            return -1;
        }
        this.byteBuf = this.getNewBytes();
        if (this.eof) {
            return -1;
        }
        return this.byteBuf.read(arrby, n, n2);
    }
}

