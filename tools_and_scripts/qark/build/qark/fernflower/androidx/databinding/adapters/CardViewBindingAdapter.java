package androidx.databinding.adapters;

import androidx.cardview.widget.CardView;

public class CardViewBindingAdapter {
   public static void setContentPadding(CardView var0, int var1) {
      var0.setContentPadding(var1, var1, var1, var1);
   }

   public static void setContentPaddingBottom(CardView var0, int var1) {
      var0.setContentPadding(var0.getContentPaddingLeft(), var0.getContentPaddingTop(), var0.getContentPaddingRight(), var1);
   }

   public static void setContentPaddingLeft(CardView var0, int var1) {
      var0.setContentPadding(var1, var0.getContentPaddingTop(), var0.getContentPaddingRight(), var0.getContentPaddingBottom());
   }

   public static void setContentPaddingRight(CardView var0, int var1) {
      var0.setContentPadding(var0.getContentPaddingLeft(), var0.getContentPaddingTop(), var1, var0.getContentPaddingBottom());
   }

   public static void setContentPaddingTop(CardView var0, int var1) {
      var0.setContentPadding(var0.getContentPaddingLeft(), var1, var0.getContentPaddingRight(), var0.getContentPaddingBottom());
   }
}
