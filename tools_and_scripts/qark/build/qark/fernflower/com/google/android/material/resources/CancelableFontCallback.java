package com.google.android.material.resources;

import android.graphics.Typeface;

public final class CancelableFontCallback extends TextAppearanceFontCallback {
   private final CancelableFontCallback.ApplyFont applyFont;
   private boolean cancelled;
   private final Typeface fallbackFont;

   public CancelableFontCallback(CancelableFontCallback.ApplyFont var1, Typeface var2) {
      this.fallbackFont = var2;
      this.applyFont = var1;
   }

   private void updateIfNotCancelled(Typeface var1) {
      if (!this.cancelled) {
         this.applyFont.apply(var1);
      }

   }

   public void cancel() {
      this.cancelled = true;
   }

   public void onFontRetrievalFailed(int var1) {
      this.updateIfNotCancelled(this.fallbackFont);
   }

   public void onFontRetrieved(Typeface var1, boolean var2) {
      this.updateIfNotCancelled(var1);
   }

   public interface ApplyFont {
      void apply(Typeface var1);
   }
}
