// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class ContentTypeDetector
{
    private static final int BUFFER_SIZE = 8;
    public static final int CLASSFILE = -889275714;
    public static final int GZFILE = 529203200;
    public static final int PACK200FILE = -889270259;
    public static final int UNKNOWN = -1;
    public static final int ZIPFILE = 1347093252;
    private final InputStream in;
    private final int type;
    
    public ContentTypeDetector(final InputStream in) throws IOException {
        if (in.markSupported()) {
            this.in = in;
        }
        else {
            this.in = new BufferedInputStream(in, 8);
        }
        this.in.mark(8);
        this.type = determineType(this.in);
        this.in.reset();
    }
    
    private static int determineType(final InputStream inputStream) throws IOException {
        final int int1 = readInt(inputStream);
        Label_0092: {
            if (int1 == -889275714) {
                final int int2 = readInt(inputStream);
                if (int2 != 196653) {
                    switch (int2) {
                        default: {
                            break Label_0092;
                        }
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53: {
                            break;
                        }
                    }
                }
                return -889275714;
            }
            if (int1 == -889270259) {
                return -889270259;
            }
            if (int1 == 1347093252) {
                return 1347093252;
            }
        }
        if ((0xFFFF0000 & int1) == 0x1F8B0000) {
            return 529203200;
        }
        return -1;
    }
    
    private static int readInt(final InputStream inputStream) throws IOException {
        return inputStream.read() << 24 | inputStream.read() << 16 | inputStream.read() << 8 | inputStream.read();
    }
    
    public InputStream getInputStream() {
        return this.in;
    }
    
    public int getType() {
        return this.type;
    }
}
