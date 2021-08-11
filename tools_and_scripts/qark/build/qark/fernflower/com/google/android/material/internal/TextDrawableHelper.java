package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import java.lang.ref.WeakReference;

public class TextDrawableHelper {
   private WeakReference delegate = new WeakReference((Object)null);
   private final TextAppearanceFontCallback fontCallback = new TextAppearanceFontCallback() {
      public void onFontRetrievalFailed(int var1) {
         TextDrawableHelper.this.textWidthDirty = true;
         TextDrawableHelper.TextDrawableDelegate var2 = (TextDrawableHelper.TextDrawableDelegate)TextDrawableHelper.this.delegate.get();
         if (var2 != null) {
            var2.onTextSizeChange();
         }

      }

      public void onFontRetrieved(Typeface var1, boolean var2) {
         if (!var2) {
            TextDrawableHelper.this.textWidthDirty = true;
            TextDrawableHelper.TextDrawableDelegate var3 = (TextDrawableHelper.TextDrawableDelegate)TextDrawableHelper.this.delegate.get();
            if (var3 != null) {
               var3.onTextSizeChange();
            }

         }
      }
   };
   private TextAppearance textAppearance;
   private final TextPaint textPaint = new TextPaint(1);
   private float textWidth;
   private boolean textWidthDirty = true;

   public TextDrawableHelper(TextDrawableHelper.TextDrawableDelegate var1) {
      this.setDelegate(var1);
   }

   private float calculateTextWidth(CharSequence var1) {
      return var1 == null ? 0.0F : this.textPaint.measureText(var1, 0, var1.length());
   }

   public TextAppearance getTextAppearance() {
      return this.textAppearance;
   }

   public TextPaint getTextPaint() {
      return this.textPaint;
   }

   public float getTextWidth(String var1) {
      if (!this.textWidthDirty) {
         return this.textWidth;
      } else {
         float var2 = this.calculateTextWidth(var1);
         this.textWidth = var2;
         this.textWidthDirty = false;
         return var2;
      }
   }

   public boolean isTextWidthDirty() {
      return this.textWidthDirty;
   }

   public void setDelegate(TextDrawableHelper.TextDrawableDelegate var1) {
      this.delegate = new WeakReference(var1);
   }

   public void setTextAppearance(TextAppearance var1, Context var2) {
      if (this.textAppearance != var1) {
         this.textAppearance = var1;
         if (var1 != null) {
            var1.updateMeasureState(var2, this.textPaint, this.fontCallback);
            TextDrawableHelper.TextDrawableDelegate var3 = (TextDrawableHelper.TextDrawableDelegate)this.delegate.get();
            if (var3 != null) {
               this.textPaint.drawableState = var3.getState();
            }

            var1.updateDrawState(var2, this.textPaint, this.fontCallback);
            this.textWidthDirty = true;
         }

         TextDrawableHelper.TextDrawableDelegate var4 = (TextDrawableHelper.TextDrawableDelegate)this.delegate.get();
         if (var4 != null) {
            var4.onTextSizeChange();
            var4.onStateChange(var4.getState());
         }
      }

   }

   public void setTextWidthDirty(boolean var1) {
      this.textWidthDirty = var1;
   }

   public void updateTextPaintDrawState(Context var1) {
      this.textAppearance.updateDrawState(var1, this.textPaint, this.fontCallback);
   }

   public interface TextDrawableDelegate {
      int[] getState();

      boolean onStateChange(int[] var1);

      void onTextSizeChange();
   }
}
