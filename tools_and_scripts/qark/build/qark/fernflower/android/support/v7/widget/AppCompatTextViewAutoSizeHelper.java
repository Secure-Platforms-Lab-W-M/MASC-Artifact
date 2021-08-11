package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R$styleable;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

class AppCompatTextViewAutoSizeHelper {
   private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
   private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
   private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
   private static final String TAG = "ACTVAutoSizeHelper";
   private static final RectF TEMP_RECTF = new RectF();
   static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0F;
   private static final int VERY_WIDE = 1048576;
   private static Hashtable sTextViewMethodByNameCache = new Hashtable();
   private float mAutoSizeMaxTextSizeInPx = -1.0F;
   private float mAutoSizeMinTextSizeInPx = -1.0F;
   private float mAutoSizeStepGranularityInPx = -1.0F;
   private int[] mAutoSizeTextSizesInPx = new int[0];
   private int mAutoSizeTextType = 0;
   private final Context mContext;
   private boolean mHasPresetAutoSizeValues = false;
   private boolean mNeedsAutoSizeText = false;
   private TextPaint mTempTextPaint;
   private final TextView mTextView;

   AppCompatTextViewAutoSizeHelper(TextView var1) {
      this.mTextView = var1;
      this.mContext = this.mTextView.getContext();
   }

   private int[] cleanupAutoSizePresetSizes(int[] var1) {
      int var3 = var1.length;
      if (var3 == 0) {
         return var1;
      } else {
         Arrays.sort(var1);
         ArrayList var5 = new ArrayList();

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            int var4 = var1[var2];
            if (var4 > 0 && Collections.binarySearch(var5, var4) < 0) {
               var5.add(var4);
            }
         }

         if (var3 == var5.size()) {
            return var1;
         } else {
            var3 = var5.size();
            var1 = new int[var3];

            for(var2 = 0; var2 < var3; ++var2) {
               var1[var2] = (Integer)var5.get(var2);
            }

            return var1;
         }
      }
   }

   private void clearAutoSizeConfiguration() {
      this.mAutoSizeTextType = 0;
      this.mAutoSizeMinTextSizeInPx = -1.0F;
      this.mAutoSizeMaxTextSizeInPx = -1.0F;
      this.mAutoSizeStepGranularityInPx = -1.0F;
      this.mAutoSizeTextSizesInPx = new int[0];
      this.mNeedsAutoSizeText = false;
   }

   @TargetApi(23)
   private StaticLayout createStaticLayoutForMeasuring(CharSequence var1, Alignment var2, int var3, int var4) {
      TextDirectionHeuristic var5 = (TextDirectionHeuristic)this.invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
      Builder var6 = Builder.obtain(var1, 0, var1.length(), this.mTempTextPaint, var3).setAlignment(var2).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
      if (var4 == -1) {
         var4 = Integer.MAX_VALUE;
      }

      return var6.setMaxLines(var4).setTextDirection(var5).build();
   }

   @TargetApi(14)
   private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence var1, Alignment var2, int var3) {
      float var4;
      float var5;
      boolean var6;
      if (VERSION.SDK_INT >= 16) {
         var4 = this.mTextView.getLineSpacingMultiplier();
         var5 = this.mTextView.getLineSpacingExtra();
         var6 = this.mTextView.getIncludeFontPadding();
      } else {
         var4 = (Float)this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", 1.0F);
         var5 = (Float)this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", 0.0F);
         var6 = (Boolean)this.invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", true);
      }

      return new StaticLayout(var1, this.mTempTextPaint, var3, var2, var4, var5, var6);
   }

   private int findLargestTextSizeWhichFits(RectF var1) {
      int var4 = this.mAutoSizeTextSizesInPx.length;
      if (var4 != 0) {
         int var3 = 0;
         int var2 = 0 + 1;
         --var4;

         while(var2 <= var4) {
            var3 = (var2 + var4) / 2;
            if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[var3], var1)) {
               int var5 = var3 + 1;
               var3 = var2;
               var2 = var5;
            } else {
               var4 = var3 - 1;
               var3 = var4;
            }
         }

         return this.mAutoSizeTextSizesInPx[var3];
      } else {
         IllegalStateException var6 = new IllegalStateException("No available text sizes to choose from.");
         throw var6;
      }
   }

   @Nullable
   private Method getTextViewMethod(@NonNull String var1) {
      Exception var10000;
      label43: {
         boolean var10001;
         Method var3;
         try {
            var3 = (Method)sTextViewMethodByNameCache.get(var1);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label43;
         }

         Method var2 = var3;
         if (var3 != null) {
            return var2;
         }

         try {
            var3 = TextView.class.getDeclaredMethod(var1);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label43;
         }

         var2 = var3;
         if (var3 == null) {
            return var2;
         }

         try {
            var3.setAccessible(true);
            sTextViewMethodByNameCache.put(var1, var3);
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         var2 = var3;
         return var2;
      }

      Exception var7 = var10000;
      StringBuilder var8 = new StringBuilder();
      var8.append("Failed to retrieve TextView#");
      var8.append(var1);
      var8.append("() method");
      Log.w("ACTVAutoSizeHelper", var8.toString(), var7);
      return null;
   }

   private Object invokeAndReturnWithDefault(@NonNull Object var1, @NonNull String var2, @NonNull Object var3) {
      Object var6 = null;
      boolean var4 = false;

      Throwable var10000;
      label564: {
         label565: {
            boolean var10001;
            Exception var57;
            try {
               try {
                  var1 = this.getTextViewMethod(var2).invoke(var1);
                  break label565;
               } catch (Exception var55) {
                  var57 = var55;
               }
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label564;
            }

            boolean var5 = true;
            var4 = var5;

            StringBuilder var7;
            try {
               var7 = new StringBuilder();
            } catch (Throwable var54) {
               var10000 = var54;
               var10001 = false;
               break label564;
            }

            var4 = var5;

            try {
               var7.append("Failed to invoke TextView#");
            } catch (Throwable var53) {
               var10000 = var53;
               var10001 = false;
               break label564;
            }

            var4 = var5;

            try {
               var7.append(var2);
            } catch (Throwable var52) {
               var10000 = var52;
               var10001 = false;
               break label564;
            }

            var4 = var5;

            try {
               var7.append("() method");
            } catch (Throwable var51) {
               var10000 = var51;
               var10001 = false;
               break label564;
            }

            var4 = var5;

            try {
               Log.w("ACTVAutoSizeHelper", var7.toString(), var57);
            } catch (Throwable var50) {
               var10000 = var50;
               var10001 = false;
               break label564;
            }

            var1 = var6;
            if (false) {
               return var1;
            }

            var1 = var6;
            if (false) {
               return var1;
            }

            return var3;
         }

         Object var59 = var1;
         var1 = var1;
         if (var59 != null) {
            return var1;
         }

         var1 = var59;
         if (true) {
            return var1;
         }

         return var3;
      }

      Throwable var58 = var10000;
      if (true && var4) {
      }

      throw var58;
   }

   private void setRawTextSize(float var1) {
      if (var1 != this.mTextView.getPaint().getTextSize()) {
         this.mTextView.getPaint().setTextSize(var1);
         boolean var2 = false;
         if (VERSION.SDK_INT >= 18) {
            var2 = this.mTextView.isInLayout();
         }

         if (this.mTextView.getLayout() != null) {
            this.mNeedsAutoSizeText = false;

            label35: {
               Exception var10000;
               label43: {
                  boolean var10001;
                  Method var3;
                  try {
                     var3 = this.getTextViewMethod("nullLayouts");
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label43;
                  }

                  if (var3 == null) {
                     break label35;
                  }

                  try {
                     var3.invoke(this.mTextView);
                     break label35;
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               }

               Exception var6 = var10000;
               Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", var6);
            }

            if (!var2) {
               this.mTextView.requestLayout();
            } else {
               this.mTextView.forceLayout();
            }

            this.mTextView.invalidate();
         }
      }

   }

   private boolean setupAutoSizeText() {
      if (this.supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
         if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
            int var2 = 1;

            float var1;
            for(var1 = (float)Math.round(this.mAutoSizeMinTextSizeInPx); Math.round(this.mAutoSizeStepGranularityInPx + var1) <= Math.round(this.mAutoSizeMaxTextSizeInPx); var1 += this.mAutoSizeStepGranularityInPx) {
               ++var2;
            }

            int[] var4 = new int[var2];
            var1 = this.mAutoSizeMinTextSizeInPx;

            for(int var3 = 0; var3 < var2; ++var3) {
               var4[var3] = Math.round(var1);
               var1 += this.mAutoSizeStepGranularityInPx;
            }

            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var4);
         }

         this.mNeedsAutoSizeText = true;
      } else {
         this.mNeedsAutoSizeText = false;
      }

      return this.mNeedsAutoSizeText;
   }

   private void setupAutoSizeUniformPresetSizes(TypedArray var1) {
      int var3 = var1.length();
      int[] var4 = new int[var3];
      if (var3 > 0) {
         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2] = var1.getDimensionPixelSize(var2, -1);
         }

         this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var4);
         this.setupAutoSizeUniformPresetSizesConfiguration();
      }

   }

   private boolean setupAutoSizeUniformPresetSizesConfiguration() {
      int var1 = this.mAutoSizeTextSizesInPx.length;
      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mHasPresetAutoSizeValues = var2;
      if (this.mHasPresetAutoSizeValues) {
         this.mAutoSizeTextType = 1;
         int[] var3 = this.mAutoSizeTextSizesInPx;
         this.mAutoSizeMinTextSizeInPx = (float)var3[0];
         this.mAutoSizeMaxTextSizeInPx = (float)var3[var1 - 1];
         this.mAutoSizeStepGranularityInPx = -1.0F;
      }

      return this.mHasPresetAutoSizeValues;
   }

   private boolean suggestedSizeFitsInSpace(int var1, RectF var2) {
      CharSequence var5 = this.mTextView.getText();
      int var3;
      if (VERSION.SDK_INT >= 16) {
         var3 = this.mTextView.getMaxLines();
      } else {
         var3 = -1;
      }

      TextPaint var4 = this.mTempTextPaint;
      if (var4 == null) {
         this.mTempTextPaint = new TextPaint();
      } else {
         var4.reset();
      }

      this.mTempTextPaint.set(this.mTextView.getPaint());
      this.mTempTextPaint.setTextSize((float)var1);
      Alignment var6 = (Alignment)this.invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Alignment.ALIGN_NORMAL);
      StaticLayout var7;
      if (VERSION.SDK_INT >= 23) {
         var7 = this.createStaticLayoutForMeasuring(var5, var6, Math.round(var2.right), var3);
      } else {
         var7 = this.createStaticLayoutForMeasuringPre23(var5, var6, Math.round(var2.right));
      }

      if (var3 != -1 && (var7.getLineCount() > var3 || var7.getLineEnd(var7.getLineCount() - 1) != var5.length())) {
         return false;
      } else {
         return (float)var7.getHeight() <= var2.bottom;
      }
   }

   private boolean supportsAutoSizeText() {
      return !(this.mTextView instanceof AppCompatEditText);
   }

   private void validateAndSetAutoSizeTextTypeUniformConfiguration(float var1, float var2, float var3) throws IllegalArgumentException {
      StringBuilder var4;
      if (var1 > 0.0F) {
         if (var2 > var1) {
            if (var3 > 0.0F) {
               this.mAutoSizeTextType = 1;
               this.mAutoSizeMinTextSizeInPx = var1;
               this.mAutoSizeMaxTextSizeInPx = var2;
               this.mAutoSizeStepGranularityInPx = var3;
               this.mHasPresetAutoSizeValues = false;
            } else {
               var4 = new StringBuilder();
               var4.append("The auto-size step granularity (");
               var4.append(var3);
               var4.append("px) is less or equal to (0px)");
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("Maximum auto-size text size (");
            var4.append(var2);
            var4.append("px) is less or equal to minimum auto-size ");
            var4.append("text size (");
            var4.append(var1);
            var4.append("px)");
            throw new IllegalArgumentException(var4.toString());
         }
      } else {
         var4 = new StringBuilder();
         var4.append("Minimum auto-size text size (");
         var4.append(var1);
         var4.append("px) is less or equal to (0px)");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void autoSizeText() {
      if (this.isAutoSizeEnabled()) {
         if (this.mNeedsAutoSizeText) {
            label267: {
               if (this.mTextView.getMeasuredHeight() <= 0) {
                  return;
               }

               if (this.mTextView.getMeasuredWidth() <= 0) {
                  return;
               }

               int var2;
               if ((Boolean)this.invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false)) {
                  var2 = 1048576;
               } else {
                  var2 = this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
               }

               int var3 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
               if (var2 <= 0) {
                  return;
               }

               if (var3 <= 0) {
                  return;
               }

               RectF var4 = TEMP_RECTF;
               synchronized(var4){}

               Throwable var10000;
               boolean var10001;
               label255: {
                  try {
                     TEMP_RECTF.setEmpty();
                     TEMP_RECTF.right = (float)var2;
                     TEMP_RECTF.bottom = (float)var3;
                     float var1 = (float)this.findLargestTextSizeWhichFits(TEMP_RECTF);
                     if (var1 != this.mTextView.getTextSize()) {
                        this.setTextSizeInternal(0, var1);
                     }
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label255;
                  }

                  label252:
                  try {
                     break label267;
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label252;
                  }
               }

               while(true) {
                  Throwable var5 = var10000;

                  try {
                     throw var5;
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }

         this.mNeedsAutoSizeText = true;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeMaxTextSize() {
      return Math.round(this.mAutoSizeMaxTextSizeInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeMinTextSize() {
      return Math.round(this.mAutoSizeMinTextSizeInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeStepGranularity() {
      return Math.round(this.mAutoSizeStepGranularityInPx);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextSizesInPx;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getAutoSizeTextType() {
      return this.mAutoSizeTextType;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isAutoSizeEnabled() {
      return this.supportsAutoSizeText() && this.mAutoSizeTextType != 0;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      float var4 = -1.0F;
      float var5 = -1.0F;
      float var3 = -1.0F;
      TypedArray var8 = this.mContext.obtainStyledAttributes(var1, R$styleable.AppCompatTextView, var2, 0);
      if (var8.hasValue(R$styleable.AppCompatTextView_autoSizeTextType)) {
         this.mAutoSizeTextType = var8.getInt(R$styleable.AppCompatTextView_autoSizeTextType, 0);
      }

      if (var8.hasValue(R$styleable.AppCompatTextView_autoSizeStepGranularity)) {
         var3 = var8.getDimension(R$styleable.AppCompatTextView_autoSizeStepGranularity, -1.0F);
      }

      if (var8.hasValue(R$styleable.AppCompatTextView_autoSizeMinTextSize)) {
         var4 = var8.getDimension(R$styleable.AppCompatTextView_autoSizeMinTextSize, -1.0F);
      }

      if (var8.hasValue(R$styleable.AppCompatTextView_autoSizeMaxTextSize)) {
         var5 = var8.getDimension(R$styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0F);
      }

      if (var8.hasValue(R$styleable.AppCompatTextView_autoSizePresetSizes)) {
         var2 = var8.getResourceId(R$styleable.AppCompatTextView_autoSizePresetSizes, 0);
         if (var2 > 0) {
            TypedArray var7 = var8.getResources().obtainTypedArray(var2);
            this.setupAutoSizeUniformPresetSizes(var7);
            var7.recycle();
         }
      }

      var8.recycle();
      if (this.supportsAutoSizeText()) {
         if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
               DisplayMetrics var9 = this.mContext.getResources().getDisplayMetrics();
               float var6 = var4;
               if (var4 == -1.0F) {
                  var6 = TypedValue.applyDimension(2, 12.0F, var9);
               }

               var4 = var5;
               if (var5 == -1.0F) {
                  var4 = TypedValue.applyDimension(2, 112.0F, var9);
               }

               var5 = var3;
               if (var3 == -1.0F) {
                  var5 = 1.0F;
               }

               this.validateAndSetAutoSizeTextTypeUniformConfiguration(var6, var4, var5);
            }

            this.setupAutoSizeText();
            return;
         }
      } else {
         this.mAutoSizeTextType = 0;
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (this.supportsAutoSizeText()) {
         DisplayMetrics var5 = this.mContext.getResources().getDisplayMetrics();
         this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(var4, (float)var1, var5), TypedValue.applyDimension(var4, (float)var2, var5), TypedValue.applyDimension(var4, (float)var3, var5));
         if (this.setupAutoSizeText()) {
            this.autoSizeText();
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException {
      if (this.supportsAutoSizeText()) {
         int var4 = var1.length;
         if (var4 <= 0) {
            this.mHasPresetAutoSizeValues = false;
         } else {
            int[] var6 = new int[var4];
            int[] var5;
            if (var2 == 0) {
               var5 = Arrays.copyOf(var1, var4);
            } else {
               DisplayMetrics var7 = this.mContext.getResources().getDisplayMetrics();
               int var3 = 0;

               while(true) {
                  var5 = var6;
                  if (var3 >= var4) {
                     break;
                  }

                  var6[var3] = Math.round(TypedValue.applyDimension(var2, (float)var1[var3], var7));
                  ++var3;
               }
            }

            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var5);
            if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
               StringBuilder var8 = new StringBuilder();
               var8.append("None of the preset sizes is valid: ");
               var8.append(Arrays.toString(var1));
               throw new IllegalArgumentException(var8.toString());
            }
         }

         if (this.setupAutoSizeText()) {
            this.autoSizeText();
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setAutoSizeTextTypeWithDefaults(int var1) {
      if (this.supportsAutoSizeText()) {
         if (var1 != 0) {
            if (var1 != 1) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown auto-size text type: ");
               var3.append(var1);
               throw new IllegalArgumentException(var3.toString());
            }

            DisplayMetrics var2 = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0F, var2), TypedValue.applyDimension(2, 112.0F, var2), 1.0F);
            if (this.setupAutoSizeText()) {
               this.autoSizeText();
               return;
            }
         } else {
            this.clearAutoSizeConfiguration();
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setTextSizeInternal(int var1, float var2) {
      Context var3 = this.mContext;
      Resources var4;
      if (var3 == null) {
         var4 = Resources.getSystem();
      } else {
         var4 = var3.getResources();
      }

      this.setRawTextSize(TypedValue.applyDimension(var1, var2, var4.getDisplayMetrics()));
   }
}
