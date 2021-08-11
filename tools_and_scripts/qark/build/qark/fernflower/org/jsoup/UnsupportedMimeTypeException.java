package org.jsoup;

import java.io.IOException;

public class UnsupportedMimeTypeException extends IOException {
   private String mimeType;
   private String url;

   public UnsupportedMimeTypeException(String var1, String var2, String var3) {
      super(var1);
      this.mimeType = var2;
      this.url = var3;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getUrl() {
      return this.url;
   }

   public String toString() {
      return super.toString() + ". Mimetype=" + this.mimeType + ", URL=" + this.url;
   }
}
