package com.google.android.material.animation;

import android.util.Property;
import android.view.ViewGroup;
import com.google.android.material.R.id;

public class ChildrenAlphaProperty extends Property {
   public static final Property CHILDREN_ALPHA = new ChildrenAlphaProperty("childrenAlpha");

   private ChildrenAlphaProperty(String var1) {
      super(Float.class, var1);
   }

   public Float get(ViewGroup var1) {
      Float var2 = (Float)var1.getTag(id.mtrl_internal_children_alpha_tag);
      return var2 != null ? var2 : 1.0F;
   }

   public void set(ViewGroup var1, Float var2) {
      float var3 = var2;
      var1.setTag(id.mtrl_internal_children_alpha_tag, var3);
      int var4 = 0;

      for(int var5 = var1.getChildCount(); var4 < var5; ++var4) {
         var1.getChildAt(var4).setAlpha(var3);
      }

   }
}
