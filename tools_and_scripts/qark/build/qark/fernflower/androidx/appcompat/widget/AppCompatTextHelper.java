package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.LocaleList;
import android.os.Build.VERSION;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R.styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

class AppCompatTextHelper {
   private static final int MONOSPACE = 3;
   private static final int SANS = 1;
   private static final int SERIF = 2;
   private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
   private boolean mAsyncFontPending;
   private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
   private TintInfo mDrawableBottomTint;
   private TintInfo mDrawableEndTint;
   private TintInfo mDrawableLeftTint;
   private TintInfo mDrawableRightTint;
   private TintInfo mDrawableStartTint;
   private TintInfo mDrawableTint;
   private TintInfo mDrawableTopTint;
   private Typeface mFontTypeface;
   private int mFontWeight = -1;
   private int mStyle = 0;
   private final TextView mView;

   AppCompatTextHelper(TextView var1) {
      this.mView = var1;
      this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(var1);
   }

   private void applyCompoundDrawableTint(Drawable var1, TintInfo var2) {
      if (var1 != null && var2 != null) {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
      }

   }

   private static TintInfo createTintInfo(Context var0, AppCompatDrawableManager var1, int var2) {
      ColorStateList var3 = var1.getTintList(var0, var2);
      if (var3 != null) {
         TintInfo var4 = new TintInfo();
         var4.mHasTintList = true;
         var4.mTintList = var3;
         return var4;
      } else {
         return null;
      }
   }

   private void setCompoundDrawables(Drawable var1, Drawable var2, Drawable var3, Drawable var4, Drawable var5, Drawable var6) {
      if (VERSION.SDK_INT >= 17 && (var5 != null || var6 != null)) {
         Drawable[] var8 = this.mView.getCompoundDrawablesRelative();
         TextView var7 = this.mView;
         if (var5 != null) {
            var1 = var5;
         } else {
            var1 = var8[0];
         }

         if (var2 == null) {
            var2 = var8[1];
         }

         if (var6 != null) {
            var3 = var6;
         } else {
            var3 = var8[2];
         }

         if (var4 == null) {
            var4 = var8[3];
         }

         var7.setCompoundDrawablesRelativeWithIntrinsicBounds(var1, var2, var3, var4);
      } else if (var1 != null || var2 != null || var3 != null || var4 != null) {
         Drawable[] var11;
         if (VERSION.SDK_INT >= 17) {
            var11 = this.mView.getCompoundDrawablesRelative();
            if (var11[0] != null || var11[2] != null) {
               TextView var9 = this.mView;
               var5 = var11[0];
               if (var2 != null) {
                  var1 = var2;
               } else {
                  var1 = var11[1];
               }

               var2 = var11[2];
               if (var4 == null) {
                  var4 = var11[3];
               }

               var9.setCompoundDrawablesRelativeWithIntrinsicBounds(var5, var1, var2, var4);
               return;
            }
         }

         var11 = this.mView.getCompoundDrawables();
         TextView var10 = this.mView;
         if (var1 == null) {
            var1 = var11[0];
         }

         if (var2 == null) {
            var2 = var11[1];
         }

         if (var3 == null) {
            var3 = var11[2];
         }

         if (var4 == null) {
            var4 = var11[3];
         }

         var10.setCompoundDrawablesWithIntrinsicBounds(var1, var2, var3, var4);
         return;
      }

   }

   private void setCompoundTints() {
      TintInfo var1 = this.mDrawableTint;
      this.mDrawableLeftTint = var1;
      this.mDrawableTopTint = var1;
      this.mDrawableRightTint = var1;
      this.mDrawableBottomTint = var1;
      this.mDrawableStartTint = var1;
      this.mDrawableEndTint = var1;
   }

   private void setTextSizeInternal(int var1, float var2) {
      this.mAutoSizeTextHelper.setTextSizeInternal(var1, var2);
   }

   private void updateTypefaceAndStyle(Context var1, TintTypedArray var2) {
      this.mStyle = var2.getInt(styleable.TextAppearance_android_textStyle, this.mStyle);
      int var3 = VERSION.SDK_INT;
      boolean var7 = false;
      if (var3 >= 28) {
         var3 = var2.getInt(styleable.TextAppearance_android_textFontWeight, -1);
         this.mFontWeight = var3;
         if (var3 != -1) {
            this.mStyle = this.mStyle & 2 | 0;
         }
      }

      if (!var2.hasValue(styleable.TextAppearance_android_fontFamily) && !var2.hasValue(styleable.TextAppearance_fontFamily)) {
         if (var2.hasValue(styleable.TextAppearance_android_typeface)) {
            this.mAsyncFontPending = false;
            var3 = var2.getInt(styleable.TextAppearance_android_typeface, 1);
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     return;
                  }

                  this.mFontTypeface = Typeface.MONOSPACE;
                  return;
               }

               this.mFontTypeface = Typeface.SERIF;
               return;
            }

            this.mFontTypeface = Typeface.SANS_SERIF;
         }

      } else {
         this.mFontTypeface = null;
         if (var2.hasValue(styleable.TextAppearance_fontFamily)) {
            var3 = styleable.TextAppearance_fontFamily;
         } else {
            var3 = styleable.TextAppearance_android_fontFamily;
         }

         int var4 = this.mFontWeight;
         int var5 = this.mStyle;
         Typeface var21;
         boolean var6;
         if (!var1.isRestricted()) {
            label146: {
               AppCompatTextHelper.ApplyTextViewCallback var20 = new AppCompatTextHelper.ApplyTextViewCallback(this, var4, var5);

               boolean var10001;
               try {
                  var21 = var2.getFont(var3, this.mStyle, var20);
               } catch (UnsupportedOperationException var18) {
                  var10001 = false;
                  break label146;
               } catch (NotFoundException var19) {
                  var10001 = false;
                  break label146;
               }

               if (var21 != null) {
                  label124: {
                     label142: {
                        label122: {
                           label121: {
                              try {
                                 if (VERSION.SDK_INT < 28 || this.mFontWeight == -1) {
                                    break label142;
                                 }

                                 var21 = Typeface.create(var21, 0);
                                 var4 = this.mFontWeight;
                                 if ((this.mStyle & 2) != 0) {
                                    break label121;
                                 }
                              } catch (UnsupportedOperationException var16) {
                                 var10001 = false;
                                 break label146;
                              } catch (NotFoundException var17) {
                                 var10001 = false;
                                 break label146;
                              }

                              var6 = false;
                              break label122;
                           }

                           var6 = true;
                        }

                        try {
                           this.mFontTypeface = Typeface.create(var21, var4, var6);
                           break label124;
                        } catch (UnsupportedOperationException var14) {
                           var10001 = false;
                           break label146;
                        } catch (NotFoundException var15) {
                           var10001 = false;
                           break label146;
                        }
                     }

                     try {
                        this.mFontTypeface = var21;
                     } catch (UnsupportedOperationException var12) {
                        var10001 = false;
                        break label146;
                     } catch (NotFoundException var13) {
                        var10001 = false;
                        break label146;
                     }
                  }
               }

               label105: {
                  label104: {
                     try {
                        if (this.mFontTypeface != null) {
                           break label104;
                        }
                     } catch (UnsupportedOperationException var10) {
                        var10001 = false;
                        break label146;
                     } catch (NotFoundException var11) {
                        var10001 = false;
                        break label146;
                     }

                     var6 = true;
                     break label105;
                  }

                  var6 = false;
               }

               try {
                  this.mAsyncFontPending = var6;
               } catch (UnsupportedOperationException var8) {
                  var10001 = false;
               } catch (NotFoundException var9) {
                  var10001 = false;
               }
            }
         }

         if (this.mFontTypeface == null) {
            String var22 = var2.getString(var3);
            if (var22 != null) {
               if (VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                  var21 = Typeface.create(var22, 0);
                  var3 = this.mFontWeight;
                  var6 = var7;
                  if ((2 & this.mStyle) != 0) {
                     var6 = true;
                  }

                  this.mFontTypeface = Typeface.create(var21, var3, var6);
                  return;
               }

               this.mFontTypeface = Typeface.create(var22, this.mStyle);
            }
         }

      }
   }

   void applyCompoundDrawablesTints() {
      Drawable[] var1;
      if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
         var1 = this.mView.getCompoundDrawables();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableLeftTint);
         this.applyCompoundDrawableTint(var1[1], this.mDrawableTopTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableRightTint);
         this.applyCompoundDrawableTint(var1[3], this.mDrawableBottomTint);
      }

      if (VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
         var1 = this.mView.getCompoundDrawablesRelative();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableStartTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableEndTint);
      }

   }

   void autoSizeText() {
      this.mAutoSizeTextHelper.autoSizeText();
   }

   int getAutoSizeMaxTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
   }

   int getAutoSizeMinTextSize() {
      return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
   }

   int getAutoSizeStepGranularity() {
      return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
   }

   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
   }

   int getAutoSizeTextType() {
      return this.mAutoSizeTextHelper.getAutoSizeTextType();
   }

   ColorStateList getCompoundDrawableTintList() {
      TintInfo var1 = this.mDrawableTint;
      return var1 != null ? var1.mTintList : null;
   }

   Mode getCompoundDrawableTintMode() {
      TintInfo var1 = this.mDrawableTint;
      return var1 != null ? var1.mTintMode : null;
   }

   boolean isAutoSizeEnabled() {
      return this.mAutoSizeTextHelper.isAutoSizeEnabled();
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      Context var21 = this.mView.getContext();
      AppCompatDrawableManager var22 = AppCompatDrawableManager.get();
      TintTypedArray var9 = TintTypedArray.obtainStyledAttributes(var21, var1, styleable.AppCompatTextHelper, var2, 0);
      int var5 = var9.getResourceId(styleable.AppCompatTextHelper_android_textAppearance, -1);
      if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableLeft)) {
         this.mDrawableLeftTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableLeft, 0));
      }

      if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableTop)) {
         this.mDrawableTopTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableTop, 0));
      }

      if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableRight)) {
         this.mDrawableRightTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableRight, 0));
      }

      if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableBottom)) {
         this.mDrawableBottomTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableBottom, 0));
      }

      if (VERSION.SDK_INT >= 17) {
         if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableStart)) {
            this.mDrawableStartTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableStart, 0));
         }

         if (var9.hasValue(styleable.AppCompatTextHelper_android_drawableEnd)) {
            this.mDrawableEndTint = createTintInfo(var21, var22, var9.getResourceId(styleable.AppCompatTextHelper_android_drawableEnd, 0));
         }
      }

      var9.recycle();
      boolean var8 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
      boolean var6 = false;
      boolean var7 = false;
      boolean var3 = false;
      boolean var4 = false;
      Object var20 = null;
      ColorStateList var11 = null;
      Object var18 = null;
      Object var19 = null;
      ColorStateList var10 = null;
      Object var17 = null;
      ColorStateList var29 = null;
      Object var16 = null;
      String var12 = null;
      String var14 = null;
      String var13 = null;
      TintTypedArray var15 = null;
      if (var5 != -1) {
         TintTypedArray var23 = TintTypedArray.obtainStyledAttributes(var21, var5, styleable.TextAppearance);
         var6 = var7;
         var3 = var4;
         if (!var8) {
            var6 = var7;
            var3 = var4;
            if (var23.hasValue(styleable.TextAppearance_textAllCaps)) {
               var3 = true;
               var6 = var23.getBoolean(styleable.TextAppearance_textAllCaps, false);
            }
         }

         this.updateTypefaceAndStyle(var21, var23);
         var11 = (ColorStateList)var20;
         var10 = (ColorStateList)var19;
         var29 = (ColorStateList)var16;
         if (VERSION.SDK_INT < 23) {
            ColorStateList var34 = (ColorStateList)var18;
            if (var23.hasValue(styleable.TextAppearance_android_textColor)) {
               var34 = var23.getColorStateList(styleable.TextAppearance_android_textColor);
            }

            ColorStateList var37 = (ColorStateList)var17;
            if (var23.hasValue(styleable.TextAppearance_android_textColorHint)) {
               var37 = var23.getColorStateList(styleable.TextAppearance_android_textColorHint);
            }

            var11 = var34;
            var10 = var37;
            var29 = (ColorStateList)var16;
            if (var23.hasValue(styleable.TextAppearance_android_textColorLink)) {
               var29 = var23.getColorStateList(styleable.TextAppearance_android_textColorLink);
               var10 = var37;
               var11 = var34;
            }
         }

         var13 = var15;
         if (var23.hasValue(styleable.TextAppearance_textLocale)) {
            var13 = var23.getString(styleable.TextAppearance_textLocale);
         }

         var12 = var14;
         if (VERSION.SDK_INT >= 26) {
            var12 = var14;
            if (var23.hasValue(styleable.TextAppearance_fontVariationSettings)) {
               var12 = var23.getString(styleable.TextAppearance_fontVariationSettings);
            }
         }

         var23.recycle();
      }

      var15 = TintTypedArray.obtainStyledAttributes(var21, var1, styleable.TextAppearance, var2, 0);
      if (!var8 && var15.hasValue(styleable.TextAppearance_textAllCaps)) {
         var6 = var15.getBoolean(styleable.TextAppearance_textAllCaps, false);
         var3 = true;
      }

      if (VERSION.SDK_INT < 23) {
         if (var15.hasValue(styleable.TextAppearance_android_textColor)) {
            var11 = var15.getColorStateList(styleable.TextAppearance_android_textColor);
         }

         if (var15.hasValue(styleable.TextAppearance_android_textColorHint)) {
            var10 = var15.getColorStateList(styleable.TextAppearance_android_textColorHint);
         }

         if (var15.hasValue(styleable.TextAppearance_android_textColorLink)) {
            var29 = var15.getColorStateList(styleable.TextAppearance_android_textColorLink);
         }
      }

      if (var15.hasValue(styleable.TextAppearance_textLocale)) {
         var13 = var15.getString(styleable.TextAppearance_textLocale);
      }

      var14 = var12;
      if (VERSION.SDK_INT >= 26) {
         var14 = var12;
         if (var15.hasValue(styleable.TextAppearance_fontVariationSettings)) {
            var14 = var15.getString(styleable.TextAppearance_fontVariationSettings);
         }
      }

      if (VERSION.SDK_INT >= 28 && var15.hasValue(styleable.TextAppearance_android_textSize) && var15.getDimensionPixelSize(styleable.TextAppearance_android_textSize, -1) == 0) {
         this.mView.setTextSize(0, 0.0F);
      }

      this.updateTypefaceAndStyle(var21, var15);
      var15.recycle();
      if (var11 != null) {
         this.mView.setTextColor(var11);
      }

      if (var10 != null) {
         this.mView.setHintTextColor(var10);
      }

      if (var29 != null) {
         this.mView.setLinkTextColor(var29);
      }

      if (!var8 && var3) {
         this.setAllCaps(var6);
      }

      Typeface var30 = this.mFontTypeface;
      if (var30 != null) {
         if (this.mFontWeight == -1) {
            this.mView.setTypeface(var30, this.mStyle);
         } else {
            this.mView.setTypeface(var30);
         }
      }

      if (var14 != null) {
         this.mView.setFontVariationSettings(var14);
      }

      if (var13 != null) {
         if (VERSION.SDK_INT >= 24) {
            this.mView.setTextLocales(LocaleList.forLanguageTags(var13));
         } else if (VERSION.SDK_INT >= 21) {
            String var31 = var13.substring(0, var13.indexOf(44));
            this.mView.setTextLocale(Locale.forLanguageTag(var31));
         }
      }

      this.mAutoSizeTextHelper.loadFromAttributes(var1, var2);
      if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
         int[] var32 = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
         if (var32.length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0F) {
               this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
               this.mView.setAutoSizeTextTypeUniformWithPresetSizes(var32, 0);
            }
         }
      }

      TintTypedArray var39 = TintTypedArray.obtainStyledAttributes(var21, var1, styleable.AppCompatTextView);
      Drawable var40 = null;
      Drawable var24 = null;
      var2 = styleable.AppCompatTextView_drawableLeftCompat;
      Drawable var35 = null;
      var2 = var39.getResourceId(var2, -1);
      if (var2 != -1) {
         var24 = var22.getDrawable(var21, var2);
      }

      var2 = var39.getResourceId(styleable.AppCompatTextView_drawableTopCompat, -1);
      if (var2 != -1) {
         var35 = var22.getDrawable(var21, var2);
      }

      var2 = var39.getResourceId(styleable.AppCompatTextView_drawableRightCompat, -1);
      Drawable var33;
      if (var2 != -1) {
         var33 = var22.getDrawable(var21, var2);
      } else {
         var33 = null;
      }

      var2 = var39.getResourceId(styleable.AppCompatTextView_drawableBottomCompat, -1);
      Drawable var36;
      if (var2 != -1) {
         var36 = var22.getDrawable(var21, var2);
      } else {
         var36 = null;
      }

      var2 = var39.getResourceId(styleable.AppCompatTextView_drawableStartCompat, -1);
      Drawable var38;
      if (var2 != -1) {
         var38 = var22.getDrawable(var21, var2);
      } else {
         var38 = null;
      }

      var2 = var39.getResourceId(styleable.AppCompatTextView_drawableEndCompat, -1);
      if (var2 != -1) {
         var40 = var22.getDrawable(var21, var2);
      }

      this.setCompoundDrawables(var24, var35, var33, var36, var38, var40);
      if (var39.hasValue(styleable.AppCompatTextView_drawableTint)) {
         ColorStateList var25 = var39.getColorStateList(styleable.AppCompatTextView_drawableTint);
         TextViewCompat.setCompoundDrawableTintList(this.mView, var25);
      }

      if (var39.hasValue(styleable.AppCompatTextView_drawableTintMode)) {
         Mode var26 = DrawableUtils.parseTintMode(var39.getInt(styleable.AppCompatTextView_drawableTintMode, -1), (Mode)null);
         TextViewCompat.setCompoundDrawableTintMode(this.mView, var26);
      }

      var2 = var39.getDimensionPixelSize(styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
      int var28 = var39.getDimensionPixelSize(styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
      int var27 = var39.getDimensionPixelSize(styleable.AppCompatTextView_lineHeight, -1);
      var39.recycle();
      if (var2 != -1) {
         TextViewCompat.setFirstBaselineToTopHeight(this.mView, var2);
      }

      if (var28 != -1) {
         TextViewCompat.setLastBaselineToBottomHeight(this.mView, var28);
      }

      if (var27 != -1) {
         TextViewCompat.setLineHeight(this.mView, var27);
      }

   }

   void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
         this.autoSizeText();
      }

   }

   void onSetCompoundDrawables() {
      this.applyCompoundDrawablesTints();
   }

   void onSetTextAppearance(Context var1, int var2) {
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.TextAppearance);
      if (var3.hasValue(styleable.TextAppearance_textAllCaps)) {
         this.setAllCaps(var3.getBoolean(styleable.TextAppearance_textAllCaps, false));
      }

      if (VERSION.SDK_INT < 23 && var3.hasValue(styleable.TextAppearance_android_textColor)) {
         ColorStateList var4 = var3.getColorStateList(styleable.TextAppearance_android_textColor);
         if (var4 != null) {
            this.mView.setTextColor(var4);
         }
      }

      if (var3.hasValue(styleable.TextAppearance_android_textSize) && var3.getDimensionPixelSize(styleable.TextAppearance_android_textSize, -1) == 0) {
         this.mView.setTextSize(0, 0.0F);
      }

      this.updateTypefaceAndStyle(var1, var3);
      if (VERSION.SDK_INT >= 26 && var3.hasValue(styleable.TextAppearance_fontVariationSettings)) {
         String var5 = var3.getString(styleable.TextAppearance_fontVariationSettings);
         if (var5 != null) {
            this.mView.setFontVariationSettings(var5);
         }
      }

      var3.recycle();
      Typeface var6 = this.mFontTypeface;
      if (var6 != null) {
         this.mView.setTypeface(var6, this.mStyle);
      }

   }

   public void runOnUiThread(Runnable var1) {
      this.mView.post(var1);
   }

   void setAllCaps(boolean var1) {
      this.mView.setAllCaps(var1);
   }

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
   }

   void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
   }

   void setAutoSizeTextTypeWithDefaults(int var1) {
      this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(var1);
   }

   void setCompoundDrawableTintList(ColorStateList var1) {
      if (this.mDrawableTint == null) {
         this.mDrawableTint = new TintInfo();
      }

      this.mDrawableTint.mTintList = var1;
      TintInfo var3 = this.mDrawableTint;
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.mHasTintList = var2;
      this.setCompoundTints();
   }

   void setCompoundDrawableTintMode(Mode var1) {
      if (this.mDrawableTint == null) {
         this.mDrawableTint = new TintInfo();
      }

      this.mDrawableTint.mTintMode = var1;
      TintInfo var3 = this.mDrawableTint;
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.mHasTintMode = var2;
      this.setCompoundTints();
   }

   void setTextSize(int var1, float var2) {
      if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
         this.setTextSizeInternal(var1, var2);
      }

   }

   public void setTypefaceByCallback(Typeface var1) {
      if (this.mAsyncFontPending) {
         this.mView.setTypeface(var1);
         this.mFontTypeface = var1;
      }

   }

   private static class ApplyTextViewCallback extends ResourcesCompat.FontCallback {
      private final int mFontWeight;
      private final WeakReference mParent;
      private final int mStyle;

      ApplyTextViewCallback(AppCompatTextHelper var1, int var2, int var3) {
         this.mParent = new WeakReference(var1);
         this.mFontWeight = var2;
         this.mStyle = var3;
      }

      public void onFontRetrievalFailed(int var1) {
      }

      public void onFontRetrieved(Typeface var1) {
         AppCompatTextHelper var5 = (AppCompatTextHelper)this.mParent.get();
         if (var5 != null) {
            Typeface var4 = var1;
            if (VERSION.SDK_INT >= 28) {
               int var2 = this.mFontWeight;
               var4 = var1;
               if (var2 != -1) {
                  boolean var3;
                  if ((this.mStyle & 2) != 0) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  var4 = Typeface.create(var1, var2, var3);
               }
            }

            var5.runOnUiThread(new AppCompatTextHelper.ApplyTextViewCallback.TypefaceApplyCallback(this.mParent, var4));
         }
      }

      private class TypefaceApplyCallback implements Runnable {
         private final WeakReference mParent;
         private final Typeface mTypeface;

         TypefaceApplyCallback(WeakReference var2, Typeface var3) {
            this.mParent = var2;
            this.mTypeface = var3;
         }

         public void run() {
            AppCompatTextHelper var1 = (AppCompatTextHelper)this.mParent.get();
            if (var1 != null) {
               var1.setTypefaceByCallback(this.mTypeface);
            }
         }
      }
   }
}
