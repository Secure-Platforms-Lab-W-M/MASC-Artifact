// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import java.io.IOException;
import java.io.FileOutputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.data.CRC64;
import java.io.File;

class ClassFileDumper
{
    private final File location;
    
    ClassFileDumper(final String s) {
        if (s == null) {
            this.location = null;
            return;
        }
        this.location = new File(s);
    }
    
    void dump(final String s, final byte[] array) throws IOException {
        if (this.location != null) {
            final int lastIndex = s.lastIndexOf(47);
            File file2;
            String s2;
            if (lastIndex != -1) {
                final File file = new File(this.location, s.substring(0, lastIndex));
                final String substring = s.substring(lastIndex + 1);
                file2 = file;
                s2 = substring;
            }
            else {
                final File location = this.location;
                s2 = s;
                file2 = location;
            }
            file2.mkdirs();
            final FileOutputStream fileOutputStream = new FileOutputStream(new File(file2, String.format("%s.%016x.class", s2, CRC64.checksum(array))));
            fileOutputStream.write(array);
            fileOutputStream.close();
        }
    }
}
