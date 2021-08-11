// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.cache2;

import java.nio.channels.ReadableByteChannel;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import okio.Buffer;
import java.nio.channels.FileChannel;

final class FileOperator
{
    private final FileChannel fileChannel;
    
    FileOperator(final FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }
    
    public void read(long n, final Buffer buffer, long transferTo) throws IOException {
        long n2 = transferTo;
        if (transferTo < 0L) {
            throw new IndexOutOfBoundsException();
        }
        while (n2 > 0L) {
            transferTo = this.fileChannel.transferTo(n, n2, buffer);
            n += transferTo;
            n2 -= transferTo;
        }
    }
    
    public void write(long n, final Buffer buffer, long transfer) throws IOException {
        if (transfer >= 0L) {
            long n2 = transfer;
            if (transfer <= buffer.size()) {
                while (n2 > 0L) {
                    transfer = this.fileChannel.transferFrom(buffer, n, n2);
                    n += transfer;
                    n2 -= transfer;
                }
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }
}
