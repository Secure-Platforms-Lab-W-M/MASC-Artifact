package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public class UnitTranscoder implements ResourceTranscoder {
   private static final UnitTranscoder UNIT_TRANSCODER = new UnitTranscoder();

   public static ResourceTranscoder get() {
      return UNIT_TRANSCODER;
   }

   public Resource transcode(Resource var1, Options var2) {
      return var1;
   }
}
