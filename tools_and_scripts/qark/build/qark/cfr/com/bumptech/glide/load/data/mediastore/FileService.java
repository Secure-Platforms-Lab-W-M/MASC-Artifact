/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data.mediastore;

import java.io.File;

class FileService {
    FileService() {
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public File get(String string2) {
        return new File(string2);
    }

    public long length(File file) {
        return file.length();
    }
}

