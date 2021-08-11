package com.karumi.dexter.listener.single;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.listener.PermissionDeniedResponse;

public class SnackbarOnDeniedPermissionListener extends BasePermissionListener {
   private final String buttonText;
   private final int duration;
   private final OnClickListener onButtonClickListener;
   private final Snackbar.Callback snackbarCallback;
   private final String text;
   private final View view;

   private SnackbarOnDeniedPermissionListener(View var1, String var2, String var3, OnClickListener var4, Snackbar.Callback var5, int var6) {
      this.view = var1;
      this.text = var2;
      this.buttonText = var3;
      this.onButtonClickListener = var4;
      this.snackbarCallback = var5;
      this.duration = var6;
   }

   // $FF: synthetic method
   SnackbarOnDeniedPermissionListener(View var1, String var2, String var3, OnClickListener var4, Snackbar.Callback var5, int var6, Object var7) {
      this(var1, var2, var3, var4, var5, var6);
   }

   public void onPermissionDenied(PermissionDeniedResponse var1) {
      super.onPermissionDenied(var1);
      Snackbar var4 = Snackbar.make(this.view, this.text, this.duration);
      String var2 = this.buttonText;
      if (var2 != null) {
         OnClickListener var3 = this.onButtonClickListener;
         if (var3 != null) {
            var4.setAction(var2, var3);
         }
      }

      Snackbar.Callback var5 = this.snackbarCallback;
      if (var5 != null) {
         var4.setCallback(var5);
      }

      var4.show();
   }

   public static class Builder {
      private String buttonText;
      private int duration = 0;
      private OnClickListener onClickListener;
      private Snackbar.Callback snackbarCallback;
      private final String text;
      private final View view;

      private Builder(View var1, String var2) {
         this.view = var1;
         this.text = var2;
      }

      public static SnackbarOnDeniedPermissionListener.Builder with(View var0, int var1) {
         return with(var0, var0.getContext().getString(var1));
      }

      public static SnackbarOnDeniedPermissionListener.Builder with(View var0, String var1) {
         return new SnackbarOnDeniedPermissionListener.Builder(var0, var1);
      }

      public SnackbarOnDeniedPermissionListener build() {
         return new SnackbarOnDeniedPermissionListener(this.view, this.text, this.buttonText, this.onClickListener, this.snackbarCallback, this.duration);
      }

      public SnackbarOnDeniedPermissionListener.Builder withButton(int var1, OnClickListener var2) {
         return this.withButton(this.view.getContext().getString(var1), var2);
      }

      public SnackbarOnDeniedPermissionListener.Builder withButton(String var1, OnClickListener var2) {
         this.buttonText = var1;
         this.onClickListener = var2;
         return this;
      }

      public SnackbarOnDeniedPermissionListener.Builder withCallback(Snackbar.Callback var1) {
         this.snackbarCallback = var1;
         return this;
      }

      public SnackbarOnDeniedPermissionListener.Builder withDuration(int var1) {
         this.duration = var1;
         return this;
      }

      public SnackbarOnDeniedPermissionListener.Builder withOpenSettingsButton(int var1) {
         return this.withOpenSettingsButton(this.view.getContext().getString(var1));
      }

      public SnackbarOnDeniedPermissionListener.Builder withOpenSettingsButton(String var1) {
         this.buttonText = var1;
         this.onClickListener = new OnClickListener() {
            public void onClick(View var1) {
               Context var3 = Builder.this.view.getContext();
               StringBuilder var2 = new StringBuilder();
               var2.append("package:");
               var2.append(var3.getPackageName());
               Intent var4 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(var2.toString()));
               var4.addCategory("android.intent.category.DEFAULT");
               var4.setFlags(268435456);
               var3.startActivity(var4);
            }
         };
         return this;
      }
   }
}
