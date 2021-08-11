package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView {
   private static final int[] TINT_ATTRS = new int[]{16843016};
   private final AppCompatTextHelper mTextHelper;

   public AppCompatCheckedTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatCheckedTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16843720);
   }

   public AppCompatCheckedTextView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mTextHelper = AppCompatTextHelper.create(this);
      this.mTextHelper.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, TINT_ATTRS, var3, 0);
      this.setCheckMarkDrawable(var4.getDrawable(0));
      var4.recycle();
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatTextHelper var1 = this.mTextHelper;
      if (var1 != null) {
         var1.applyCompoundDrawablesTints();
      }

   }

   public void setCheckMarkDrawable(@DrawableRes int var1) {
      this.setCheckMarkDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      AppCompatTextHelper var3 = this.mTextHelper;
      if (var3 != null) {
         var3.onSetTextAppearance(var1, var2);
      }

   }
}
