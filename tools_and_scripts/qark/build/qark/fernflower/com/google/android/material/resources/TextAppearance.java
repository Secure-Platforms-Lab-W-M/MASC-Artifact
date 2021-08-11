package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.R.styleable;

public class TextAppearance {
   private static final String TAG = "TextAppearance";
   private static final int TYPEFACE_MONOSPACE = 3;
   private static final int TYPEFACE_SANS = 1;
   private static final int TYPEFACE_SERIF = 2;
   private Typeface font;
   public final String fontFamily;
   private final int fontFamilyResourceId;
   private boolean fontResolved = false;
   public final ColorStateList shadowColor;
   public final float shadowDx;
   public final float shadowDy;
   public final float shadowRadius;
   public final boolean textAllCaps;
   public final ColorStateList textColor;
   public final ColorStateList textColorHint;
   public final ColorStateList textColorLink;
   public final float textSize;
   public final int textStyle;
   public final int typeface;

   public TextAppearance(Context var1, int var2) {
      TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.TextAppearance);
      this.textSize = var3.getDimension(styleable.TextAppearance_android_textSize, 0.0F);
      this.textColor = MaterialResources.getColorStateList(var1, var3, styleable.TextAppearance_android_textColor);
      this.textColorHint = MaterialResources.getColorStateList(var1, var3, styleable.TextAppearance_android_textColorHint);
      this.textColorLink = MaterialResources.getColorStateList(var1, var3, styleable.TextAppearance_android_textColorLink);
      this.textStyle = var3.getInt(styleable.TextAppearance_android_textStyle, 0);
      this.typeface = var3.getInt(styleable.TextAppearance_android_typeface, 1);
      var2 = MaterialResources.getIndexWithValue(var3, styleable.TextAppearance_fontFamily, styleable.TextAppearance_android_fontFamily);
      this.fontFamilyResourceId = var3.getResourceId(var2, 0);
      this.fontFamily = var3.getString(var2);
      this.textAllCaps = var3.getBoolean(styleable.TextAppearance_textAllCaps, false);
      this.shadowColor = MaterialResources.getColorStateList(var1, var3, styleable.TextAppearance_android_shadowColor);
      this.shadowDx = var3.getFloat(styleable.TextAppearance_android_shadowDx, 0.0F);
      this.shadowDy = var3.getFloat(styleable.TextAppearance_android_shadowDy, 0.0F);
      this.shadowRadius = var3.getFloat(styleable.TextAppearance_android_shadowRadius, 0.0F);
      var3.recycle();
   }

   private void createFallbackFont() {
      if (this.font == null) {
         String var2 = this.fontFamily;
         if (var2 != null) {
            this.font = Typeface.create(var2, this.textStyle);
         }
      }

      if (this.font == null) {
         int var1 = this.typeface;
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  this.font = Typeface.DEFAULT;
               } else {
                  this.font = Typeface.MONOSPACE;
               }
            } else {
               this.font = Typeface.SERIF;
            }
         } else {
            this.font = Typeface.SANS_SERIF;
         }

         this.font = Typeface.create(this.font, this.textStyle);
      }

   }

   public Typeface getFallbackFont() {
      this.createFallbackFont();
      return this.font;
   }

   public Typeface getFont(Context var1) {
      if (this.fontResolved) {
         return this.font;
      } else {
         if (!var1.isRestricted()) {
            label48: {
               Exception var10000;
               label46: {
                  boolean var10001;
                  Typeface var9;
                  try {
                     var9 = ResourcesCompat.getFont(var1, this.fontFamilyResourceId);
                     this.font = var9;
                  } catch (UnsupportedOperationException var6) {
                     var10001 = false;
                     break label48;
                  } catch (NotFoundException var7) {
                     var10001 = false;
                     break label48;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label46;
                  }

                  if (var9 == null) {
                     break label48;
                  }

                  try {
                     this.font = Typeface.create(var9, this.textStyle);
                     break label48;
                  } catch (UnsupportedOperationException var3) {
                     var10001 = false;
                     break label48;
                  } catch (NotFoundException var4) {
                     var10001 = false;
                     break label48;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }

               Exception var10 = var10000;
               StringBuilder var2 = new StringBuilder();
               var2.append("Error loading font ");
               var2.append(this.fontFamily);
               Log.d("TextAppearance", var2.toString(), var10);
            }
         }

         this.createFallbackFont();
         this.fontResolved = true;
         return this.font;
      }
   }

   public void getFontAsync(Context var1, final TextPaint var2, final TextAppearanceFontCallback var3) {
      this.updateTextPaintMeasureState(var2, this.getFallbackFont());
      this.getFontAsync(var1, new TextAppearanceFontCallback() {
         public void onFontRetrievalFailed(int var1) {
            var3.onFontRetrievalFailed(var1);
         }

         public void onFontRetrieved(Typeface var1, boolean var2x) {
            TextAppearance.this.updateTextPaintMeasureState(var2, var1);
            var3.onFontRetrieved(var1, var2x);
         }
      });
   }

   public void getFontAsync(Context var1, final TextAppearanceFontCallback var2) {
      if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
         this.getFont(var1);
      } else {
         this.createFallbackFont();
      }

      if (this.fontFamilyResourceId == 0) {
         this.fontResolved = true;
      }

      if (this.fontResolved) {
         var2.onFontRetrieved(this.font, true);
      } else {
         try {
            ResourcesCompat.getFont(var1, this.fontFamilyResourceId, new ResourcesCompat.FontCallback() {
               public void onFontRetrievalFailed(int var1) {
                  TextAppearance.this.fontResolved = true;
                  var2.onFontRetrievalFailed(var1);
               }

               public void onFontRetrieved(Typeface var1) {
                  TextAppearance var2x = TextAppearance.this;
                  var2x.font = Typeface.create(var1, var2x.textStyle);
                  TextAppearance.this.fontResolved = true;
                  var2.onFontRetrieved(TextAppearance.this.font, false);
               }
            }, (Handler)null);
         } catch (NotFoundException var4) {
            this.fontResolved = true;
            var2.onFontRetrievalFailed(1);
         } catch (Exception var5) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Error loading font ");
            var3.append(this.fontFamily);
            Log.d("TextAppearance", var3.toString(), var5);
            this.fontResolved = true;
            var2.onFontRetrievalFailed(-3);
            return;
         }

      }
   }

   public void updateDrawState(Context var1, TextPaint var2, TextAppearanceFontCallback var3) {
      this.updateMeasureState(var1, var2, var3);
      ColorStateList var8 = this.textColor;
      int var7;
      if (var8 != null) {
         var7 = var8.getColorForState(var2.drawableState, this.textColor.getDefaultColor());
      } else {
         var7 = -16777216;
      }

      var2.setColor(var7);
      float var4 = this.shadowRadius;
      float var5 = this.shadowDx;
      float var6 = this.shadowDy;
      var8 = this.shadowColor;
      if (var8 != null) {
         var7 = var8.getColorForState(var2.drawableState, this.shadowColor.getDefaultColor());
      } else {
         var7 = 0;
      }

      var2.setShadowLayer(var4, var5, var6, var7);
   }

   public void updateMeasureState(Context var1, TextPaint var2, TextAppearanceFontCallback var3) {
      if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
         this.updateTextPaintMeasureState(var2, this.getFont(var1));
      } else {
         this.getFontAsync(var1, var2, var3);
      }
   }

   public void updateTextPaintMeasureState(TextPaint var1, Typeface var2) {
      var1.setTypeface(var2);
      int var4 = this.textStyle & var2.getStyle();
      boolean var5;
      if ((var4 & 1) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      var1.setFakeBoldText(var5);
      float var3;
      if ((var4 & 2) != 0) {
         var3 = -0.25F;
      } else {
         var3 = 0.0F;
      }

      var1.setTextSkewX(var3);
      var1.setTextSize(this.textSize);
   }
}
