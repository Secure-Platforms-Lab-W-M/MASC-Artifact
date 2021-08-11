/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import util.Encryption;

public class PaddingCipherOutputStream
extends OutputStream {
    private int bufSize;
    private ByteArrayOutputStream bytesBuffer;
    private DataOutputStream lowerOut;
    private OutputStream underlying;

    public PaddingCipherOutputStream(OutputStream outputStream, int n) throws IOException {
        this.bufSize = n;
        this.underlying = outputStream;
        this.bytesBuffer = new ByteArrayOutputStream(1024);
    }

    private void init() throws IOException {
        if (this.lowerOut != null) {
            return;
        }
        this.lowerOut = new DataOutputStream(this.underlying);
        this.lowerOut.writeInt(Encryption.ENCR_INIT_BYTES.length);
        this.lowerOut.write(Encryption.ENCR_INIT_BYTES);
    }

    @Override
    public void close() throws IOException {
        this.init();
        this.writeNext();
        this.lowerOut.flush();
        this.lowerOut.close();
    }

    @Override
    public void flush() throws IOException {
        this.init();
        this.writeNext();
        this.lowerOut.flush();
    }

    @Override
    public void write(int n) throws IOException {
        this.init();
        this.bytesBuffer.write(n);
        this.bytesBuffer.flush();
        if (this.bytesBuffer.size() >= this.bufSize) {
            this.writeNext();
        }
    }

    @Override
    public void write(byte[] arrby) throws IOException {
        this.write(arrby, 0, arrby.length);
    }

    @Override
    public void write(byte[] arrby, int n, int n2) throws IOException {
        this.init();
        this.bytesBuffer.write(arrby, n, n2);
        this.bytesBuffer.flush();
        if (this.bytesBuffer.size() >= this.bufSize) {
            this.writeNext();
        }
    }

    public void writeNext() throws IOException {
        this.bytesBuffer.flush();
        if (this.bytesBuffer.size() == 0) {
            return;
        }
        byte[] arrby = this.bytesBuffer.toByteArray();
        this.bytesBuffer.reset();
        try {
            arrby = Encryption.encrypt(arrby);
            this.lowerOut.writeInt(arrby.length);
            this.lowerOut.write(arrby);
            return;
        }
        catch (Exception exception) {
            throw new IOException(exception.getMessage());
        }
        catch (IOException iOException) {
            throw iOException;
        }
    }
}

