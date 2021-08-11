// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.multidex;

import java.io.File;
import java.util.zip.ZipException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.io.RandomAccessFile;

final class ZipUtil
{
    private static final int BUFFER_SIZE = 16384;
    private static final int ENDHDR = 22;
    private static final int ENDSIG = 101010256;
    
    static long computeCrcOfCentralDir(final RandomAccessFile randomAccessFile, final CentralDirectory centralDirectory) throws IOException {
        final CRC32 crc32 = new CRC32();
        long size = centralDirectory.size;
        randomAccessFile.seek(centralDirectory.offset);
        final int n = (int)Math.min(16384L, size);
        final byte[] array = new byte[16384];
        for (int i = randomAccessFile.read(array, 0, n); i != -1; i = randomAccessFile.read(array, 0, (int)Math.min(16384L, size))) {
            crc32.update(array, 0, i);
            size -= i;
            if (size == 0L) {
                break;
            }
        }
        return crc32.getValue();
    }
    
    static CentralDirectory findCentralDirectory(final RandomAccessFile randomAccessFile) throws IOException, ZipException {
        long n = randomAccessFile.length() - 22L;
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("File too short to be a zip file: ");
            sb.append(randomAccessFile.length());
            throw new ZipException(sb.toString());
        }
        long n2 = n - 65536L;
        if (n2 < 0L) {
            n2 = 0L;
        }
        final int reverseBytes = Integer.reverseBytes(101010256);
        while (true) {
            randomAccessFile.seek(n);
            if (randomAccessFile.readInt() == reverseBytes) {
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                final CentralDirectory centralDirectory = new CentralDirectory();
                centralDirectory.size = ((long)Integer.reverseBytes(randomAccessFile.readInt()) & 0xFFFFFFFFL);
                centralDirectory.offset = ((long)Integer.reverseBytes(randomAccessFile.readInt()) & 0xFFFFFFFFL);
                return centralDirectory;
            }
            --n;
            if (n >= n2) {
                continue;
            }
            throw new ZipException("End Of Central Directory signature not found");
        }
    }
    
    static long getZipCrc(File file) throws IOException {
        file = (File)new RandomAccessFile(file, "r");
        try {
            return computeCrcOfCentralDir((RandomAccessFile)file, findCentralDirectory((RandomAccessFile)file));
        }
        finally {
            ((RandomAccessFile)file).close();
        }
    }
    
    static class CentralDirectory
    {
        long offset;
        long size;
    }
}
