package android.support.design.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class TextInputEditText extends AppCompatEditText {
   public TextInputEditText(Context var1) {
      super(var1);
   }

   public TextInputEditText(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public TextInputEditText(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      InputConnection var3 = super.onCreateInputConnection(var1);
      if (var3 != null && var1.hintText == null) {
         for(ViewParent var2 = this.getParent(); var2 instanceof View; var2 = var2.getParent()) {
            if (var2 instanceof TextInputLayout) {
               var1.hintText = ((TextInputLayout)var2).getHint();
               return var3;
            }
         }

         return var3;
      } else {
         return var3;
      }
   }
}
