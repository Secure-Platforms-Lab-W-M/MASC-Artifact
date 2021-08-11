package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;

public class FileDecoder implements ResourceDecoder {
   public Resource decode(File var1, int var2, int var3, Options var4) {
      return new FileResource(var1);
   }

   public boolean handles(File var1, Options var2) {
      return true;
   }
}
