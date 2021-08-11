package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;

interface EngineJobListener {
   void onEngineJobCancelled(EngineJob var1, Key var2);

   void onEngineJobComplete(EngineJob var1, Key var2, EngineResource var3);
}
