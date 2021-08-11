// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class PaddingCipherOutputStream extends OutputStream
{
    private int bufSize;
    private ByteArrayOutputStream bytesBuffer;
    private DataOutputStream lowerOut;
    private OutputStream underlying;
    
    public PaddingCipherOutputStream(final OutputStream underlying, final int bufSize) throws IOException {
        this.bufSize = bufSize;
        this.underlying = underlying;
        this.bytesBuffer = new ByteArrayOutputStream(1024);
    }
    
    private void init() throws IOException {
        if (this.lowerOut != null) {
            return;
        }
        (this.lowerOut = new DataOutputStream(this.underlying)).writeInt(Encryption.ENCR_INIT_BYTES.length);
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
    public void write(final int n) throws IOException {
        this.init();
        this.bytesBuffer.write(n);
        this.bytesBuffer.flush();
        if (this.bytesBuffer.size() >= this.bufSize) {
            this.writeNext();
        }
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.init();
        this.bytesBuffer.write(array, n, n2);
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
        final byte[] byteArray = this.bytesBuffer.toByteArray();
        this.bytesBuffer.reset();
        try {
            final byte[] encrypt = Encryption.encrypt(byteArray);
            this.lowerOut.writeInt(encrypt.length);
            this.lowerOut.write(encrypt);
        }
        catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
        catch (IOException ex2) {
            throw ex2;
        }
    }
}
