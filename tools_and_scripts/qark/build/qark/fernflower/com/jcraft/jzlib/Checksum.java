package com.jcraft.jzlib;

interface Checksum {
   Checksum copy();

   long getValue();

   void reset();

   void reset(long var1);

   void update(byte[] var1, int var2, int var3);
}
