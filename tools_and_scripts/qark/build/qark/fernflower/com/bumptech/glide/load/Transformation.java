package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation extends Key {
   Resource transform(Context var1, Resource var2, int var3, int var4);
}
