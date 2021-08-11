package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.File;
import java.net.URL;

interface ModelTypes {
   Object load(Bitmap var1);

   Object load(Drawable var1);

   Object load(Uri var1);

   Object load(File var1);

   Object load(Integer var1);

   Object load(Object var1);

   Object load(String var1);

   @Deprecated
   Object load(URL var1);

   Object load(byte[] var1);
}
