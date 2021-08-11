package com.bumptech.glide.load.resource.bitmap;

import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ExifInterfaceImageHeaderParser implements ImageHeaderParser {
   public int getOrientation(InputStream var1, ArrayPool var2) throws IOException {
      int var3 = (new ExifInterface(var1)).getAttributeInt("Orientation", 1);
      return var3 == 0 ? -1 : var3;
   }

   public int getOrientation(ByteBuffer var1, ArrayPool var2) throws IOException {
      return this.getOrientation(ByteBufferUtil.toStream(var1), var2);
   }

   public ImageHeaderParser.ImageType getType(InputStream var1) {
      return ImageHeaderParser.ImageType.UNKNOWN;
   }

   public ImageHeaderParser.ImageType getType(ByteBuffer var1) {
      return ImageHeaderParser.ImageType.UNKNOWN;
   }
}
