// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.EOFException;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PaddingCipherInputStream extends InputStream
{
    private ByteArrayInputStream byteBuf;
    private boolean eof;
    private boolean init;
    private DataInputStream lowerIn;
    
    public PaddingCipherInputStream(final InputStream inputStream) throws IOException {
        this.eof = false;
        this.init = false;
        this.lowerIn = new DataInputStream(inputStream);
    }
    
    private ByteArrayInputStream getNewBytes() throws IOException {
        try {
            final byte[] array = new byte[this.lowerIn.readInt()];
            this.lowerIn.readFully(array);
            if (array.length == 0) {
                this.byteBuf = null;
                this.eof = true;
                return null;
            }
            try {
                return this.byteBuf = new ByteArrayInputStream(Encryption.decrypt(array));
            }
            catch (Exception ex) {
                throw new IOException(ex.getMessage());
            }
        }
        catch (EOFException ex2) {
            this.eof = true;
            return this.byteBuf = null;
        }
    }
    
    private void initRead() throws IOException {
        if (this.init) {
            return;
        }
        int int1 = -1;
        this.init = true;
        try {
            int1 = this.lowerIn.readInt();
        }
        catch (EOFException ex) {
            this.eof = true;
        }
        if (!this.eof) {
            if (int1 != Encryption.ENCR_INIT_BYTES.length) {
                throw new IOException("Wrong keyphrase!");
            }
            final byte[] array = new byte[int1];
            this.lowerIn.readFully(array);
            for (int i = 0; i < array.length; ++i) {
                if (array[i] != Encryption.ENCR_INIT_BYTES[i]) {
                    throw new IOException("Wrong keyphrase!");
                }
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
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        if (!this.init) {
            this.initRead();
        }
        if (this.available() != 0) {
            return this.byteBuf.read(array, n, n2);
        }
        if (this.eof) {
            return -1;
        }
        this.byteBuf = this.getNewBytes();
        if (this.eof) {
            return -1;
        }
        return this.byteBuf.read(array, n, n2);
    }
}
