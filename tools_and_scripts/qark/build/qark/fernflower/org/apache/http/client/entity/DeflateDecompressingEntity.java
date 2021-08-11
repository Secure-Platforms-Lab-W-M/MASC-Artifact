package org.apache.http.client.entity;

import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity {
   public DeflateDecompressingEntity(HttpEntity var1) {
      super(var1, DeflateInputStreamFactory.getInstance());
   }
}
