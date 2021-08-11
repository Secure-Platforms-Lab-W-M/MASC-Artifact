/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

final class Util {
    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    private Util() {
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return;
            }
            catch (Exception exception) {
                return;
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    static void deleteContents(File object) throws IOException {
        File[] arrfile = object.listFiles();
        if (arrfile != null) {
            int n = arrfile.length;
            for (int i = 0; i < n; ++i) {
                object = arrfile[i];
                if (object.isDirectory()) {
                    Util.deleteContents((File)object);
                }
                if (object.delete()) {
                    continue;
                }
                arrfile = new StringBuilder();
                arrfile.append("failed to delete file: ");
                arrfile.append(object);
                throw new IOException(arrfile.toString());
            }
            return;
        }
        arrfile = new StringBuilder();
        arrfile.append("not a readable directory: ");
        arrfile.append(object);
        throw new IOException(arrfile.toString());
    }

    static String readFully(Reader reader) throws IOException {
        try {
            int n;
            Object object = new StringWriter();
            char[] arrc = new char[1024];
            while ((n = reader.read(arrc)) != -1) {
                object.write(arrc, 0, n);
            }
            object = object.toString();
            return object;
        }
        finally {
            reader.close();
        }
    }
}

