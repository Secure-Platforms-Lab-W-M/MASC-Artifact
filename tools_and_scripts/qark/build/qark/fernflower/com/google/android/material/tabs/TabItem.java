package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R.styleable;

public class TabItem extends View {
   public final int customLayout;
   public final Drawable icon;
   public final CharSequence text;

   public TabItem(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TabItem(Context var1, AttributeSet var2) {
      super(var1, var2);
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.TabItem);
      this.text = var3.getText(styleable.TabItem_android_text);
      this.icon = var3.getDrawable(styleable.TabItem_android_icon);
      this.customLayout = var3.getResourceId(styleable.TabItem_android_layout, 0);
      var3.recycle();
   }
}
