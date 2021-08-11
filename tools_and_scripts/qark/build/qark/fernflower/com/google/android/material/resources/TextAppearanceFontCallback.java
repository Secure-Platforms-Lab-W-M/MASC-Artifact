package com.google.android.material.resources;

import android.graphics.Typeface;

public abstract class TextAppearanceFontCallback {
   public abstract void onFontRetrievalFailed(int var1);

   public abstract void onFontRetrieved(Typeface var1, boolean var2);
}
