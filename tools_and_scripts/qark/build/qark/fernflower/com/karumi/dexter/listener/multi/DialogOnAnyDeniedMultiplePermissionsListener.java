package com.karumi.dexter.listener.multi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import com.karumi.dexter.MultiplePermissionsReport;

public class DialogOnAnyDeniedMultiplePermissionsListener extends BaseMultiplePermissionsListener {
   private final Context context;
   private final Drawable icon;
   private final String message;
   private final String positiveButtonText;
   private final String title;

   private DialogOnAnyDeniedMultiplePermissionsListener(Context var1, String var2, String var3, String var4, Drawable var5) {
      this.context = var1;
      this.title = var2;
      this.message = var3;
      this.positiveButtonText = var4;
      this.icon = var5;
   }

   // $FF: synthetic method
   DialogOnAnyDeniedMultiplePermissionsListener(Context var1, String var2, String var3, String var4, Drawable var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private void showDialog() {
      (new android.app.AlertDialog.Builder(this.context)).setTitle(this.title).setMessage(this.message).setPositiveButton(this.positiveButtonText, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
         }
      }).setIcon(this.icon).show();
   }

   public void onPermissionsChecked(MultiplePermissionsReport var1) {
      super.onPermissionsChecked(var1);
      if (!var1.areAllPermissionsGranted()) {
         this.showDialog();
      }

   }

   public static class Builder {
      private String buttonText;
      private final Context context;
      private Drawable icon;
      private String message;
      private String title;

      private Builder(Context var1) {
         this.context = var1;
      }

      public static DialogOnAnyDeniedMultiplePermissionsListener.Builder withContext(Context var0) {
         return new DialogOnAnyDeniedMultiplePermissionsListener.Builder(var0);
      }

      public DialogOnAnyDeniedMultiplePermissionsListener build() {
         String var1 = this.title;
         if (var1 == null) {
            var1 = "";
         }

         String var2 = this.message;
         if (var2 == null) {
            var2 = "";
         }

         String var3 = this.buttonText;
         if (var3 == null) {
            var3 = "";
         }

         return new DialogOnAnyDeniedMultiplePermissionsListener(this.context, var1, var2, var3, this.icon);
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withButtonText(int var1) {
         this.buttonText = this.context.getString(var1);
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withButtonText(String var1) {
         this.buttonText = var1;
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withIcon(int var1) {
         this.icon = this.context.getResources().getDrawable(var1);
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withIcon(Drawable var1) {
         this.icon = var1;
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withMessage(int var1) {
         this.message = this.context.getString(var1);
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withMessage(String var1) {
         this.message = var1;
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withTitle(int var1) {
         this.title = this.context.getString(var1);
         return this;
      }

      public DialogOnAnyDeniedMultiplePermissionsListener.Builder withTitle(String var1) {
         this.title = var1;
         return this;
      }
   }
}
