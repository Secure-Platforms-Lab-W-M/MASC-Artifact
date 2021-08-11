package com.bumptech.glide.load;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public interface Key {
   Charset CHARSET = Charset.forName("UTF-8");
   String STRING_CHARSET_NAME = "UTF-8";

   boolean equals(Object var1);

   int hashCode();

   void updateDiskCacheKey(MessageDigest var1);
}
