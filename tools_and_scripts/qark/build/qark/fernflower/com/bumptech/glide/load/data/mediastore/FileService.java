package com.bumptech.glide.load.data.mediastore;

import java.io.File;

class FileService {
   public boolean exists(File var1) {
      return var1.exists();
   }

   public File get(String var1) {
      return new File(var1);
   }

   public long length(File var1) {
      return var1.length();
   }
}
