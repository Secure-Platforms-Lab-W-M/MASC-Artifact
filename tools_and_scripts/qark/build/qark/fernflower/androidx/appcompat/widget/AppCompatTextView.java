package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import androidx.core.widget.TintableCompoundDrawablesView;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppCompatTextView extends TextView implements TintableBackgroundView, TintableCompoundDrawablesView, AutoSizeableTextView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private Future mPrecomputedTextFuture;
   private final AppCompatTextClassifierHelper mTextClassifierHelper;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16842884);
   }

   public AppCompatTextView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      AppCompatBackgroundHelper var4 = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper = var4;
      var4.loadFromAttributes(var2, var3);
      AppCompatTextHelper var5 = new AppCompatTextHelper(this);
      this.mTextHelper = var5;
      var5.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
      this.mTextClassifierHelper = new AppCompatTextClassifierHelper(this);
   }

   private void consumeTextFutureAndSetBlocking() {
      Future var1 = this.mPrecomputedTextFuture;
      if (var1 != null) {
         try {
            this.mPrecomputedTextFuture = null;
            TextViewCompat.setPrecomputedText(this, (PrecomputedTextCompat)var1.get());
            return;
         } catch (InterruptedException var2) {
         } catch (ExecutionException var3) {
            return;
         }
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatTextHelper var2 = this.mTextHelper;
      if (var2 != null) {
         var2.applyCompoundDrawablesTints();
      }

   }

   public int getAutoSizeMaxTextSize() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMaxTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMaxTextSize() : -1;
      }
   }

   public int getAutoSizeMinTextSize() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeMinTextSize();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeMinTextSize() : -1;
      }
   }

   public int getAutoSizeStepGranularity() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeStepGranularity();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeStepGranularity() : -1;
      }
   }

   public int[] getAutoSizeTextAvailableSizes() {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         return super.getAutoSizeTextAvailableSizes();
      } else {
         AppCompatTextHelper var1 = this.mTextHelper;
         return var1 != null ? var1.getAutoSizeTextAvailableSizes() : new int[0];
      }
   }

   public int getAutoSizeTextType() {
      boolean var2 = PLATFORM_SUPPORTS_AUTOSIZE;
      byte var1 = 0;
      if (var2) {
         if (super.getAutoSizeTextType() == 1) {
            var1 = 1;
         }

         return var1;
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         return var3 != null ? var3.getAutoSizeTextType() : 0;
      }
   }

   public int getFirstBaselineToTopHeight() {
      return TextViewCompat.getFirstBaselineToTopHeight(this);
   }

   public int getLastBaselineToBottomHeight() {
      return TextViewCompat.getLastBaselineToBottomHeight(this);
   }

   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintList() : null;
   }

   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintMode() : null;
   }

   public ColorStateList getSupportCompoundDrawablesTintList() {
      return this.mTextHelper.getCompoundDrawableTintList();
   }

   public Mode getSupportCompoundDrawablesTintMode() {
      return this.mTextHelper.getCompoundDrawableTintMode();
   }

   public CharSequence getText() {
      this.consumeTextFutureAndSetBlocking();
      return super.getText();
   }

   public TextClassifier getTextClassifier() {
      if (VERSION.SDK_INT < 28) {
         AppCompatTextClassifierHelper var1 = this.mTextClassifierHelper;
         if (var1 != null) {
            return var1.getTextClassifier();
         }
      }

      return super.getTextClassifier();
   }

   public PrecomputedTextCompat.Params getTextMetricsParamsCompat() {
      return TextViewCompat.getTextMetricsParams(this);
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(var1), var1, this);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      AppCompatTextHelper var6 = this.mTextHelper;
      if (var6 != null) {
         var6.onLayout(var1, var2, var3, var4, var5);
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.consumeTextFutureAndSetBlocking();
      super.onMeasure(var1, var2);
   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      if (this.mTextHelper != null && !PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled()) {
         this.mTextHelper.autoSizeText();
      }

   }

   public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
      } else {
         AppCompatTextHelper var5 = this.mTextHelper;
         if (var5 != null) {
            var5.setAutoSizeTextTypeUniformWithConfiguration(var1, var2, var3, var4);
         }

      }
   }

   public void setAutoSizeTextTypeUniformWithPresetSizes(int[] var1, int var2) throws IllegalArgumentException {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setAutoSizeTextTypeUniformWithPresetSizes(var1, var2);
         }

      }
   }

   public void setAutoSizeTextTypeWithDefaults(int var1) {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setAutoSizeTextTypeWithDefaults(var1);
      } else {
         AppCompatTextHelper var2 = this.mTextHelper;
         if (var2 != null) {
            var2.setAutoSizeTextTypeWithDefaults(var1);
         }

      }
   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(int var1) {
      super.setBackgroundResource(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundResource(var1);
      }

   }

   public void setCompoundDrawables(Drawable var1, Drawable var2, Drawable var3, Drawable var4) {
      super.setCompoundDrawables(var1, var2, var3, var4);
      AppCompatTextHelper var5 = this.mTextHelper;
      if (var5 != null) {
         var5.onSetCompoundDrawables();
      }

   }

   public void setCompoundDrawablesRelative(Drawable var1, Drawable var2, Drawable var3, Drawable var4) {
      super.setCompoundDrawablesRelative(var1, var2, var3, var4);
      AppCompatTextHelper var5 = this.mTextHelper;
      if (var5 != null) {
         var5.onSetCompoundDrawables();
      }

   }

   public void setCompoundDrawablesRelativeWithIntrinsicBounds(int var1, int var2, int var3, int var4) {
      Context var9 = this.getContext();
      Drawable var8 = null;
      Drawable var5;
      if (var1 != 0) {
         var5 = AppCompatResources.getDrawable(var9, var1);
      } else {
         var5 = null;
      }

      Drawable var6;
      if (var2 != 0) {
         var6 = AppCompatResources.getDrawable(var9, var2);
      } else {
         var6 = null;
      }

      Drawable var7;
      if (var3 != 0) {
         var7 = AppCompatResources.getDrawable(var9, var3);
      } else {
         var7 = null;
      }

      if (var4 != 0) {
         var8 = AppCompatResources.getDrawable(var9, var4);
      }

      this.setCompoundDrawablesRelativeWithIntrinsicBounds(var5, var6, var7, var8);
      AppCompatTextHelper var10 = this.mTextHelper;
      if (var10 != null) {
         var10.onSetCompoundDrawables();
      }

   }

   public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable var1, Drawable var2, Drawable var3, Drawable var4) {
      super.setCompoundDrawablesRelativeWithIntrinsicBounds(var1, var2, var3, var4);
      AppCompatTextHelper var5 = this.mTextHelper;
      if (var5 != null) {
         var5.onSetCompoundDrawables();
      }

   }

   public void setCompoundDrawablesWithIntrinsicBounds(int var1, int var2, int var3, int var4) {
      Context var9 = this.getContext();
      Drawable var8 = null;
      Drawable var5;
      if (var1 != 0) {
         var5 = AppCompatResources.getDrawable(var9, var1);
      } else {
         var5 = null;
      }

      Drawable var6;
      if (var2 != 0) {
         var6 = AppCompatResources.getDrawable(var9, var2);
      } else {
         var6 = null;
      }

      Drawable var7;
      if (var3 != 0) {
         var7 = AppCompatResources.getDrawable(var9, var3);
      } else {
         var7 = null;
      }

      if (var4 != 0) {
         var8 = AppCompatResources.getDrawable(var9, var4);
      }

      this.setCompoundDrawablesWithIntrinsicBounds(var5, var6, var7, var8);
      AppCompatTextHelper var10 = this.mTextHelper;
      if (var10 != null) {
         var10.onSetCompoundDrawables();
      }

   }

   public void setCompoundDrawablesWithIntrinsicBounds(Drawable var1, Drawable var2, Drawable var3, Drawable var4) {
      super.setCompoundDrawablesWithIntrinsicBounds(var1, var2, var3, var4);
      AppCompatTextHelper var5 = this.mTextHelper;
      if (var5 != null) {
         var5.onSetCompoundDrawables();
      }

   }

   public void setCustomSelectionActionModeCallback(Callback var1) {
      super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, var1));
   }

   public void setFirstBaselineToTopHeight(int var1) {
      if (VERSION.SDK_INT >= 28) {
         super.setFirstBaselineToTopHeight(var1);
      } else {
         TextViewCompat.setFirstBaselineToTopHeight(this, var1);
      }
   }

   public void setLastBaselineToBottomHeight(int var1) {
      if (VERSION.SDK_INT >= 28) {
         super.setLastBaselineToBottomHeight(var1);
      } else {
         TextViewCompat.setLastBaselineToBottomHeight(this, var1);
      }
   }

   public void setLineHeight(int var1) {
      TextViewCompat.setLineHeight(this, var1);
   }

   public void setPrecomputedText(PrecomputedTextCompat var1) {
      TextViewCompat.setPrecomputedText(this, var1);
   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   public void setSupportCompoundDrawablesTintList(ColorStateList var1) {
      this.mTextHelper.setCompoundDrawableTintList(var1);
      this.mTextHelper.applyCompoundDrawablesTints();
   }

   public void setSupportCompoundDrawablesTintMode(Mode var1) {
      this.mTextHelper.setCompoundDrawableTintMode(var1);
      this.mTextHelper.applyCompoundDrawablesTints();
   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      AppCompatTextHelper var3 = this.mTextHelper;
      if (var3 != null) {
         var3.onSetTextAppearance(var1, var2);
      }

   }

   public void setTextClassifier(TextClassifier var1) {
      if (VERSION.SDK_INT < 28) {
         AppCompatTextClassifierHelper var2 = this.mTextClassifierHelper;
         if (var2 != null) {
            var2.setTextClassifier(var1);
            return;
         }
      }

      super.setTextClassifier(var1);
   }

   public void setTextFuture(Future var1) {
      this.mPrecomputedTextFuture = var1;
      if (var1 != null) {
         this.requestLayout();
      }

   }

   public void setTextMetricsParamsCompat(PrecomputedTextCompat.Params var1) {
      TextViewCompat.setTextMetricsParams(this, var1);
   }

   public void setTextSize(int var1, float var2) {
      if (PLATFORM_SUPPORTS_AUTOSIZE) {
         super.setTextSize(var1, var2);
      } else {
         AppCompatTextHelper var3 = this.mTextHelper;
         if (var3 != null) {
            var3.setTextSize(var1, var2);
         }

      }
   }

   public void setTypeface(Typeface var1, int var2) {
      Object var4 = null;
      Typeface var3 = (Typeface)var4;
      if (var1 != null) {
         var3 = (Typeface)var4;
         if (var2 > 0) {
            var3 = TypefaceCompat.create(this.getContext(), var1, var2);
         }
      }

      if (var3 != null) {
         var1 = var3;
      }

      super.setTypeface(var1, var2);
   }
}
