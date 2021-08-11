package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import java.util.Map;

class EngineKeyFactory {
   EngineKey buildKey(Object var1, Key var2, int var3, int var4, Map var5, Class var6, Class var7, Options var8) {
      return new EngineKey(var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
