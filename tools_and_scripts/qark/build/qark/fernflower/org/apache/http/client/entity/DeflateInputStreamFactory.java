package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

public class DeflateInputStreamFactory implements InputStreamFactory {
   private static final DeflateInputStreamFactory INSTANCE = new DeflateInputStreamFactory();

   public static DeflateInputStreamFactory getInstance() {
      return INSTANCE;
   }

   public InputStream create(InputStream var1) throws IOException {
      return new DeflateInputStream(var1);
   }
}
