/*
 * Decompiled with CFR 0_124.
 */
package com.jcraft.jzlib;

interface Checksum {
    public Checksum copy();

    public long getValue();

    public void reset();

    public void reset(long var1);

    public void update(byte[] var1, int var2, int var3);
}

