package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferEncoder implements Encoder {
   private static final String TAG = "ByteBufferEncoder";

   public boolean encode(ByteBuffer var1, File var2, Options var3) {
      try {
         ByteBufferUtil.toFile(var1, var2);
         return true;
      } catch (IOException var4) {
         if (Log.isLoggable("ByteBufferEncoder", 3)) {
            Log.d("ByteBufferEncoder", "Failed to write data", var4);
         }

         return false;
      }
   }
}
