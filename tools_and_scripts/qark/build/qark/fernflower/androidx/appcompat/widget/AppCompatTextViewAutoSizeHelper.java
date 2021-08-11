package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.appcompat.R.styleable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

class AppCompatTextViewAutoSizeHelper {
   private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
   private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
   private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
   private static final String TAG = "ACTVAutoSizeHelper";
   private static final RectF TEMP_RECTF = new RectF();
   static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0F;
   private static final int VERY_WIDE = 1048576;
   private static ConcurrentHashMap sTextViewFieldByNameCache = new ConcurrentHashMap();
   private static ConcurrentHashMap sTextViewMethodByNameCache = new ConcurrentHashMap();
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
      this.mContext = var1.getContext();
   }

   private static Object accessAndReturnWithDefault(Object param0, String param1, Object param2) {
      // $FF: Couldn't be decompiled
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

   private StaticLayout createStaticLayoutForMeasuring(CharSequence var1, Alignment var2, int var3, int var4) {
      Builder var5 = Builder.obtain(var1, 0, var1.length(), this.mTempTextPaint, var3);
      Builder var9 = var5.setAlignment(var2).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
      if (var4 == -1) {
         var4 = Integer.MAX_VALUE;
      }

      var9.setMaxLines(var4);

      label42: {
         boolean var10001;
         TextDirectionHeuristic var10;
         label33: {
            try {
               if (VERSION.SDK_INT >= 29) {
                  var10 = this.mTextView.getTextDirectionHeuristic();
                  break label33;
               }
            } catch (ClassCastException var8) {
               var10001 = false;
               break label42;
            }

            try {
               var10 = (TextDirectionHeuristic)invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
            } catch (ClassCastException var7) {
               var10001 = false;
               break label42;
            }
         }

         try {
            var5.setTextDirection(var10);
            return var5.build();
         } catch (ClassCastException var6) {
            var10001 = false;
         }
      }

      Log.w("ACTVAutoSizeHelper", "Failed to obtain TextDirectionHeuristic, auto size may be incorrect");
      return var5.build();
   }

   private StaticLayout createStaticLayoutForMeasuringPre16(CharSequence var1, Alignment var2, int var3) {
      float var4 = (Float)accessAndReturnWithDefault(this.mTextView, "mSpacingMult", 1.0F);
      float var5 = (Float)accessAndReturnWithDefault(this.mTextView, "mSpacingAdd", 0.0F);
      boolean var6 = (Boolean)accessAndReturnWithDefault(this.mTextView, "mIncludePad", true);
      return new StaticLayout(var1, this.mTempTextPaint, var3, var2, var4, var5, var6);
   }

   private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence var1, Alignment var2, int var3) {
      float var4 = this.mTextView.getLineSpacingMultiplier();
      float var5 = this.mTextView.getLineSpacingExtra();
      boolean var6 = this.mTextView.getIncludeFontPadding();
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
         throw new IllegalStateException("No available text sizes to choose from.");
      }
   }

   private static Field getTextViewField(String var0) {
      NoSuchFieldException var10000;
      label43: {
         boolean var10001;
         Field var2;
         try {
            var2 = (Field)sTextViewFieldByNameCache.get(var0);
         } catch (NoSuchFieldException var5) {
            var10000 = var5;
            var10001 = false;
            break label43;
         }

         Field var1 = var2;
         if (var2 != null) {
            return var1;
         }

         try {
            var2 = TextView.class.getDeclaredField(var0);
         } catch (NoSuchFieldException var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         var1 = var2;
         if (var2 == null) {
            return var1;
         }

         try {
            var2.setAccessible(true);
            sTextViewFieldByNameCache.put(var0, var2);
         } catch (NoSuchFieldException var3) {
            var10000 = var3;
            var10001 = false;
            break label43;
         }

         var1 = var2;
         return var1;
      }

      NoSuchFieldException var6 = var10000;
      StringBuilder var7 = new StringBuilder();
      var7.append("Failed to access TextView#");
      var7.append(var0);
      var7.append(" member");
      Log.w("ACTVAutoSizeHelper", var7.toString(), var6);
      return null;
   }

   private static Method getTextViewMethod(String var0) {
      Exception var10000;
      label43: {
         boolean var10001;
         Method var2;
         try {
            var2 = (Method)sTextViewMethodByNameCache.get(var0);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label43;
         }

         Method var1 = var2;
         if (var2 != null) {
            return var1;
         }

         try {
            var2 = TextView.class.getDeclaredMethod(var0);
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         var1 = var2;
         if (var2 == null) {
            return var1;
         }

         try {
            var2.setAccessible(true);
            sTextViewMethodByNameCache.put(var0, var2);
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
            break label43;
         }

         var1 = var2;
         return var1;
      }

      Exception var6 = var10000;
      StringBuilder var7 = new StringBuilder();
      var7.append("Failed to retrieve TextView#");
      var7.append(var0);
      var7.append("() method");
      Log.w("ACTVAutoSizeHelper", var7.toString(), var6);
      return null;
   }

   private static Object invokeAndReturnWithDefault(Object var0, String var1, Object var2) {
      Object var5 = null;
      boolean var3 = false;

      label549: {
         Throwable var10000;
         label563: {
            boolean var10001;
            Exception var56;
            try {
               try {
                  var0 = getTextViewMethod(var1).invoke(var0);
                  break label549;
               } catch (Exception var54) {
                  var56 = var54;
               }
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label563;
            }

            boolean var4 = true;
            var3 = var4;

            StringBuilder var6;
            try {
               var6 = new StringBuilder();
            } catch (Throwable var53) {
               var10000 = var53;
               var10001 = false;
               break label563;
            }

            var3 = var4;

            try {
               var6.append("Failed to invoke TextView#");
            } catch (Throwable var52) {
               var10000 = var52;
               var10001 = false;
               break label563;
            }

            var3 = var4;

            try {
               var6.append(var1);
            } catch (Throwable var51) {
               var10000 = var51;
               var10001 = false;
               break label563;
            }

            var3 = var4;

            try {
               var6.append("() method");
            } catch (Throwable var50) {
               var10000 = var50;
               var10001 = false;
               break label563;
            }

            var3 = var4;

            try {
               Log.w("ACTVAutoSizeHelper", var6.toString(), var56);
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label563;
            }

            var0 = var5;
            if (false) {
               return var0;
            }

            var0 = var5;
            if (false) {
               return var0;
            }

            return var2;
         }

         Throwable var57 = var10000;
         if (true && var3) {
         }

         throw var57;
      }

      Object var58 = var0;
      var0 = var0;
      if (var58 != null) {
         return var0;
      } else {
         var0 = var58;
         if (true) {
            return var0;
         } else {
            return var2;
         }
      }
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
                     var3 = getTextViewMethod("nullLayouts");
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
            int var2 = (int)Math.floor((double)((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx)) + 1;
            int[] var3 = new int[var2];

            for(int var1 = 0; var1 < var2; ++var1) {
               var3[var1] = Math.round(this.mAutoSizeMinTextSizeInPx + (float)var1 * this.mAutoSizeStepGranularityInPx);
            }

            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(var3);
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
      if (var2) {
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
      TransformationMethod var6 = this.mTextView.getTransformationMethod();
      CharSequence var4 = var5;
      if (var6 != null) {
         CharSequence var8 = var6.getTransformation(var5, this.mTextView);
         var4 = var5;
         if (var8 != null) {
            var4 = var8;
         }
      }

      int var3;
      if (VERSION.SDK_INT >= 16) {
         var3 = this.mTextView.getMaxLines();
      } else {
         var3 = -1;
      }

      this.initTempTextPaint(var1);
      StaticLayout var7 = this.createLayout(var4, (Alignment)invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Alignment.ALIGN_NORMAL), Math.round(var2.right), var3);
      if (var3 != -1 && (var7.getLineCount() > var3 || var7.getLineEnd(var7.getLineCount() - 1) != var4.length())) {
         return false;
      } else {
         return (float)var7.getHeight() <= var2.bottom;
      }
   }

   private boolean supportsAutoSizeText() {
      return this.mTextView instanceof AppCompatEditText ^ true;
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
            var4.append("px) is less or equal to minimum auto-size text size (");
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

   void autoSizeText() {
      if (this.isAutoSizeEnabled()) {
         Throwable var10000;
         boolean var10001;
         label285: {
            if (this.mNeedsAutoSizeText) {
               if (this.mTextView.getMeasuredHeight() <= 0) {
                  return;
               }

               if (this.mTextView.getMeasuredWidth() <= 0) {
                  return;
               }

               boolean var4;
               if (VERSION.SDK_INT >= 29) {
                  var4 = this.mTextView.isHorizontallyScrollable();
               } else {
                  var4 = (Boolean)invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false);
               }

               int var2;
               if (var4) {
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

               RectF var5 = TEMP_RECTF;
               synchronized(var5){}

               try {
                  TEMP_RECTF.setEmpty();
                  TEMP_RECTF.right = (float)var2;
                  TEMP_RECTF.bottom = (float)var3;
                  float var1 = (float)this.findLargestTextSizeWhichFits(TEMP_RECTF);
                  if (var1 != this.mTextView.getTextSize()) {
                     this.setTextSizeInternal(0, var1);
                  }
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label285;
               }

               try {
                  ;
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label285;
               }
            }

            this.mNeedsAutoSizeText = true;
            return;
         }

         while(true) {
            Throwable var6 = var10000;

            try {
               throw var6;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               continue;
            }
         }
      }
   }

   StaticLayout createLayout(CharSequence var1, Alignment var2, int var3, int var4) {
      if (VERSION.SDK_INT >= 23) {
         return this.createStaticLayoutForMeasuring(var1, var2, var3, var4);
      } else {
         return VERSION.SDK_INT >= 16 ? this.createStaticLayoutForMeasuringPre23(var1, var2, var3) : this.createStaticLayoutForMeasuringPre16(var1, var2, var3);
      }
   }

   int getAutoSizeMaxTextSize() {
      return Math.round(this.mAutoSizeMaxTextSizeInPx);
   }

   int getAutoSizeMinTextSize() {
      return Math.round(this.mAutoSizeMinTextSizeInPx);
   }

   int getAutoSizeStepGranularity() {
      return Math.round(this.mAutoSizeStepGranularityInPx);
   }

   int[] getAutoSizeTextAvailableSizes() {
      return this.mAutoSizeTextSizesInPx;
   }

   int getAutoSizeTextType() {
      return this.mAutoSizeTextType;
   }

   void initTempTextPaint(int var1) {
      TextPaint var2 = this.mTempTextPaint;
      if (var2 == null) {
         this.mTempTextPaint = new TextPaint();
      } else {
         var2.reset();
      }

      this.mTempTextPaint.set(this.mTextView.getPaint());
      this.mTempTextPaint.setTextSize((float)var1);
   }

   boolean isAutoSizeEnabled() {
      return this.supportsAutoSizeText() && this.mAutoSizeTextType != 0;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      float var4 = -1.0F;
      float var5 = -1.0F;
      float var3 = -1.0F;
      TypedArray var8 = this.mContext.obtainStyledAttributes(var1, styleable.AppCompatTextView, var2, 0);
      if (var8.hasValue(styleable.AppCompatTextView_autoSizeTextType)) {
         this.mAutoSizeTextType = var8.getInt(styleable.AppCompatTextView_autoSizeTextType, 0);
      }

      if (var8.hasValue(styleable.AppCompatTextView_autoSizeStepGranularity)) {
         var3 = var8.getDimension(styleable.AppCompatTextView_autoSizeStepGranularity, -1.0F);
      }

      if (var8.hasValue(styleable.AppCompatTextView_autoSizeMinTextSize)) {
         var4 = var8.getDimension(styleable.AppCompatTextView_autoSizeMinTextSize, -1.0F);
      }

      if (var8.hasValue(styleable.AppCompatTextView_autoSizeMaxTextSize)) {
         var5 = var8.getDimension(styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0F);
      }

      if (var8.hasValue(styleable.AppCompatTextView_autoSizePresetSizes)) {
         var2 = var8.getResourceId(styleable.AppCompatTextView_autoSizePresetSizes, 0);
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

   void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (this.supportsAutoSizeText()) {
         DisplayMetrics var5 = this.mContext.getResources().getDisplayMetrics();
         this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(var4, (float)var1, var5), TypedValue.applyDimension(var4, (float)var2, var5), TypedValue.applyDimension(var4, (float)var3, var5));
         if (this.setupAutoSizeText()) {
            this.autoSizeText();
         }
      }

   }

   void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
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
