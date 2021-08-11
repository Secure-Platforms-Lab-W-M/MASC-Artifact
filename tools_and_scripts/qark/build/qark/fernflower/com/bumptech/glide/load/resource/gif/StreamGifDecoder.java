package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class StreamGifDecoder implements ResourceDecoder {
   private static final String TAG = "StreamGifDecoder";
   private final ArrayPool byteArrayPool;
   private final ResourceDecoder byteBufferDecoder;
   private final List parsers;

   public StreamGifDecoder(List var1, ResourceDecoder var2, ArrayPool var3) {
      this.parsers = var1;
      this.byteBufferDecoder = var2;
      this.byteArrayPool = var3;
   }

   private static byte[] inputStreamToBytes(InputStream var0) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(16384);

      IOException var10000;
      label38: {
         boolean var10001;
         byte[] var3;
         try {
            var3 = new byte[16384];
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label38;
         }

         while(true) {
            int var1;
            try {
               var1 = var0.read(var3);
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }

            if (var1 == -1) {
               try {
                  var2.flush();
                  return var2.toByteArray();
               } catch (IOException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break;
               }
            }

            try {
               var2.write(var3, 0, var1);
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }
         }
      }

      IOException var8 = var10000;
      if (Log.isLoggable("StreamGifDecoder", 5)) {
         Log.w("StreamGifDecoder", "Error reading data from stream", var8);
      }

      return null;
   }

   public Resource decode(InputStream var1, int var2, int var3, Options var4) throws IOException {
      byte[] var5 = inputStreamToBytes(var1);
      if (var5 == null) {
         return null;
      } else {
         ByteBuffer var6 = ByteBuffer.wrap(var5);
         return this.byteBufferDecoder.decode(var6, var2, var3, var4);
      }
   }

   public boolean handles(InputStream var1, Options var2) throws IOException {
      return !(Boolean)var2.get(GifOptions.DISABLE_ANIMATION) && ImageHeaderParserUtils.getType(this.parsers, var1, this.byteArrayPool) == ImageHeaderParser.ImageType.GIF;
   }
}
