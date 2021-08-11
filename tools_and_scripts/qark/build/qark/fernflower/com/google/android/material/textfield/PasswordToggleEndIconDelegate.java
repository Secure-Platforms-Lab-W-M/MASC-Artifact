package com.google.android.material.textfield;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import androidx.appcompat.content.res.AppCompatResources;
import com.google.android.material.R.drawable;
import com.google.android.material.R.string;

class PasswordToggleEndIconDelegate extends EndIconDelegate {
   private final TextInputLayout.OnEditTextAttachedListener onEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
      public void onEditTextAttached(TextInputLayout var1) {
         EditText var2 = var1.getEditText();
         var1.setEndIconVisible(true);
         PasswordToggleEndIconDelegate.this.endIconView.setChecked(true ^ PasswordToggleEndIconDelegate.this.hasPasswordTransformation());
         var2.removeTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
         var2.addTextChangedListener(PasswordToggleEndIconDelegate.this.textWatcher);
      }
   };
   private final TextInputLayout.OnEndIconChangedListener onEndIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
      public void onEndIconChanged(TextInputLayout var1, int var2) {
         EditText var3 = var1.getEditText();
         if (var3 != null && var2 == 1) {
            var3.setTransformationMethod(PasswordTransformationMethod.getInstance());
         }

      }
   };
   private final TextWatcher textWatcher = new TextWatcher() {
      public void afterTextChanged(Editable var1) {
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         PasswordToggleEndIconDelegate.this.endIconView.setChecked(PasswordToggleEndIconDelegate.this.hasPasswordTransformation() ^ true);
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   };

   PasswordToggleEndIconDelegate(TextInputLayout var1) {
      super(var1);
   }

   private boolean hasPasswordTransformation() {
      EditText var1 = this.textInputLayout.getEditText();
      return var1 != null && var1.getTransformationMethod() instanceof PasswordTransformationMethod;
   }

   void initialize() {
      this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, drawable.design_password_eye));
      this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(string.password_toggle_content_description));
      this.textInputLayout.setEndIconOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            EditText var3 = PasswordToggleEndIconDelegate.this.textInputLayout.getEditText();
            if (var3 != null) {
               int var2 = var3.getSelectionEnd();
               if (PasswordToggleEndIconDelegate.this.hasPasswordTransformation()) {
                  var3.setTransformationMethod((TransformationMethod)null);
               } else {
                  var3.setTransformationMethod(PasswordTransformationMethod.getInstance());
               }

               if (var2 >= 0) {
                  var3.setSelection(var2);
               }

            }
         }
      });
      this.textInputLayout.addOnEditTextAttachedListener(this.onEditTextAttachedListener);
      this.textInputLayout.addOnEndIconChangedListener(this.onEndIconChangedListener);
   }
}
