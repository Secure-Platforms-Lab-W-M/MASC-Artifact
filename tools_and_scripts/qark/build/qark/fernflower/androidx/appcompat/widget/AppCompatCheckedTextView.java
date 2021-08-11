package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.TextViewCompat;

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
      AppCompatTextHelper var4 = new AppCompatTextHelper(this);
      this.mTextHelper = var4;
      var4.loadFromAttributes(var2, var3);
      this.mTextHelper.applyCompoundDrawablesTints();
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, TINT_ATTRS, var3, 0);
      this.setCheckMarkDrawable(var5.getDrawable(0));
      var5.recycle();
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatTextHelper var1 = this.mTextHelper;
      if (var1 != null) {
         var1.applyCompoundDrawablesTints();
      }

   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      return AppCompatHintHelper.onCreateInputConnection(super.onCreateInputConnection(var1), var1, this);
   }

   public void setCheckMarkDrawable(int var1) {
      this.setCheckMarkDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setCustomSelectionActionModeCallback(Callback var1) {
      super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, var1));
   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      AppCompatTextHelper var3 = this.mTextHelper;
      if (var3 != null) {
         var3.onSetTextAppearance(var1, var2);
      }

   }
}
