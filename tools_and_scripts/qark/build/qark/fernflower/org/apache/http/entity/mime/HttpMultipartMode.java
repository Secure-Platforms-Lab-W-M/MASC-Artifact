package org.apache.http.entity.mime;

public enum HttpMultipartMode {
   BROWSER_COMPATIBLE,
   RFC6532,
   STRICT;

   static {
      HttpMultipartMode var0 = new HttpMultipartMode("RFC6532", 2);
      RFC6532 = var0;
   }
}
