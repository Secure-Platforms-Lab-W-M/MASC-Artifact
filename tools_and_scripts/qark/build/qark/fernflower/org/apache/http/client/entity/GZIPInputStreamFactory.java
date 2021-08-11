package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GZIPInputStreamFactory implements InputStreamFactory {
   private static final GZIPInputStreamFactory INSTANCE = new GZIPInputStreamFactory();

   public static GZIPInputStreamFactory getInstance() {
      return INSTANCE;
   }

   public InputStream create(InputStream var1) throws IOException {
      return new GZIPInputStream(var1);
   }
}
