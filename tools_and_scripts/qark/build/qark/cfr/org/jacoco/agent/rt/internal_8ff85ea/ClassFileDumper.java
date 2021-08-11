/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.data.CRC64;

class ClassFileDumper {
    private final File location;

    ClassFileDumper(String string2) {
        if (string2 == null) {
            this.location = null;
            return;
        }
        this.location = new File(string2);
    }

    void dump(String object, byte[] arrby) throws IOException {
        if (this.location != null) {
            Object object2;
            int n = object.lastIndexOf(47);
            if (n != -1) {
                object2 = new File(this.location, object.substring(0, n));
                String string2 = object.substring(n + 1);
                object = object2;
                object2 = string2;
            } else {
                File file = this.location;
                object2 = object;
                object = file;
            }
            object.mkdirs();
            object = new FileOutputStream(new File((File)object, String.format("%s.%016x.class", object2, CRC64.checksum(arrby))));
            object.write(arrby);
            object.close();
        }
    }
}

