package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

public class GzipDecompressingEntity extends DecompressingEntity {
   public GzipDecompressingEntity(HttpEntity var1) {
      super(var1, GZIPInputStreamFactory.getInstance());
   }
}
