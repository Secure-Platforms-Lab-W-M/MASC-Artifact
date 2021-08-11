/*
 * Decompiled with CFR 0_124.
 */
package androidx.multidex;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.ZipException;

final class ZipUtil {
    private static final int BUFFER_SIZE = 16384;
    private static final int ENDHDR = 22;
    private static final int ENDSIG = 101010256;

    ZipUtil() {
    }

    static long computeCrcOfCentralDir(RandomAccessFile randomAccessFile, CentralDirectory arrby) throws IOException {
        CRC32 cRC32 = new CRC32();
        long l = arrby.size;
        randomAccessFile.seek(arrby.offset);
        int n = (int)Math.min(16384L, l);
        arrby = new byte[16384];
        n = randomAccessFile.read(arrby, 0, n);
        while (n != -1) {
            cRC32.update(arrby, 0, n);
            if ((l -= (long)n) == 0L) break;
            n = randomAccessFile.read(arrby, 0, (int)Math.min(16384L, l));
        }
        return cRC32.getValue();
    }

    static CentralDirectory findCentralDirectory(RandomAccessFile randomAccessFile) throws IOException, ZipException {
        long l = randomAccessFile.length() - 22L;
        if (l >= 0L) {
            long l2;
            long l3 = l2 = l - 65536L;
            if (l2 < 0L) {
                l3 = 0L;
            }
            int n = Integer.reverseBytes(101010256);
            do {
                randomAccessFile.seek(l);
                if (randomAccessFile.readInt() != n) continue;
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                randomAccessFile.skipBytes(2);
                CentralDirectory centralDirectory = new CentralDirectory();
                centralDirectory.size = (long)Integer.reverseBytes(randomAccessFile.readInt()) & 0xFFFFFFFFL;
                centralDirectory.offset = (long)Integer.reverseBytes(randomAccessFile.readInt()) & 0xFFFFFFFFL;
                return centralDirectory;
            } while (--l >= l3);
            throw new ZipException("End Of Central Directory signature not found");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("File too short to be a zip file: ");
        stringBuilder.append(randomAccessFile.length());
        throw new ZipException(stringBuilder.toString());
    }

    static long getZipCrc(File object) throws IOException {
        object = new RandomAccessFile((File)object, "r");
        try {
            long l = ZipUtil.computeCrcOfCentralDir((RandomAccessFile)object, ZipUtil.findCentralDirectory((RandomAccessFile)object));
            return l;
        }
        finally {
            object.close();
        }
    }

    static class CentralDirectory {
        long offset;
        long size;

        CentralDirectory() {
        }
    }

}

