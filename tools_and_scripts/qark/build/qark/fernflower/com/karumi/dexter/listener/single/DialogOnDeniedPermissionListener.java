package com.karumi.dexter.listener.single;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import com.karumi.dexter.listener.PermissionDeniedResponse;

public class DialogOnDeniedPermissionListener extends BasePermissionListener {
   private final Context context;
   private final Drawable icon;
   private final String message;
   private final String positiveButtonText;
   private final String title;

   private DialogOnDeniedPermissionListener(Context var1, String var2, String var3, String var4, Drawable var5) {
      this.context = var1;
      this.title = var2;
      this.message = var3;
      this.positiveButtonText = var4;
      this.icon = var5;
   }

   // $FF: synthetic method
   DialogOnDeniedPermissionListener(Context var1, String var2, String var3, String var4, Drawable var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   public void onPermissionDenied(PermissionDeniedResponse var1) {
      super.onPermissionDenied(var1);
      (new android.app.AlertDialog.Builder(this.context)).setTitle(this.title).setMessage(this.message).setPositiveButton(this.positiveButtonText, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
         }
      }).setIcon(this.icon).show();
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

      public static DialogOnDeniedPermissionListener.Builder withContext(Context var0) {
         return new DialogOnDeniedPermissionListener.Builder(var0);
      }

      public DialogOnDeniedPermissionListener build() {
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

         return new DialogOnDeniedPermissionListener(this.context, var1, var2, var3, this.icon);
      }

      public DialogOnDeniedPermissionListener.Builder withButtonText(int var1) {
         this.buttonText = this.context.getString(var1);
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withButtonText(String var1) {
         this.buttonText = var1;
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withIcon(int var1) {
         this.icon = this.context.getResources().getDrawable(var1);
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withIcon(Drawable var1) {
         this.icon = var1;
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withMessage(int var1) {
         this.message = this.context.getString(var1);
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withMessage(String var1) {
         this.message = var1;
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withTitle(int var1) {
         this.title = this.context.getString(var1);
         return this;
      }

      public DialogOnDeniedPermissionListener.Builder withTitle(String var1) {
         this.title = var1;
         return this;
      }
   }
}
