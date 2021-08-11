// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class Java9Support
{
    public static final int V1_9 = 53;
    
    private Java9Support() {
    }
    
    public static byte[] downgrade(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        putShort(array2, 6, 52);
        return array2;
    }
    
    public static byte[] downgradeIfRequired(final byte[] array) {
        if (isPatchRequired(array)) {
            return downgrade(array);
        }
        return array;
    }
    
    public static boolean isPatchRequired(final byte[] array) {
        return readShort(array, 6) == 53;
    }
    
    private static void putShort(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 >>> 8);
        array[n + 1] = (byte)n2;
    }
    
    public static byte[] readFully(final InputStream inputStream) throws IOException {
        if (inputStream != null) {
            final byte[] array = new byte[1024];
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                final int read = inputStream.read(array);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        }
        throw new IllegalArgumentException();
    }
    
    private static short readShort(final byte[] array, final int n) {
        return (short)((array[n] & 0xFF) << 8 | (array[n + 1] & 0xFF));
    }
    
    public static void upgrade(final byte[] array) {
        putShort(array, 6, 53);
    }
}
