package org.apache.commons.text.similarity;

import org.apache.commons.lang3.Validate;

public class EditDistanceFrom {
   private final EditDistance editDistance;
   private final CharSequence left;

   public EditDistanceFrom(EditDistance var1, CharSequence var2) {
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The edit distance may not be null.");
      this.editDistance = var1;
      this.left = var2;
   }

   public Object apply(CharSequence var1) {
      return this.editDistance.apply(this.left, var1);
   }

   public EditDistance getEditDistance() {
      return this.editDistance;
   }

   public CharSequence getLeft() {
      return this.left;
   }
}
