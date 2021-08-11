package com.bumptech.glide.load.data;

import java.io.IOException;

public interface DataRewinder {
   void cleanup();

   Object rewindAndGet() throws IOException;

   public interface Factory {
      DataRewinder build(Object var1);

      Class getDataClass();
   }
}
