package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R$attr;
import android.support.v7.view.ActionMode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class AppCompatDialog extends Dialog implements AppCompatCallback {
   private AppCompatDelegate mDelegate;

   public AppCompatDialog(Context var1) {
      this(var1, 0);
   }

   public AppCompatDialog(Context var1, int var2) {
      super(var1, getThemeResId(var1, var2));
      this.getDelegate().onCreate((Bundle)null);
      this.getDelegate().applyDayNight();
   }

   protected AppCompatDialog(Context var1, boolean var2, OnCancelListener var3) {
      super(var1, var2, var3);
   }

   private static int getThemeResId(Context var0, int var1) {
      int var2 = var1;
      if (var1 == 0) {
         TypedValue var3 = new TypedValue();
         var0.getTheme().resolveAttribute(R$attr.dialogTheme, var3, true);
         var2 = var3.resourceId;
      }

      return var2;
   }

   public void addContentView(View var1, LayoutParams var2) {
      this.getDelegate().addContentView(var1, var2);
   }

   @Nullable
   public View findViewById(@IdRes int var1) {
      return this.getDelegate().findViewById(var1);
   }

   public AppCompatDelegate getDelegate() {
      if (this.mDelegate == null) {
         this.mDelegate = AppCompatDelegate.create((Dialog)this, this);
      }

      return this.mDelegate;
   }

   public ActionBar getSupportActionBar() {
      return this.getDelegate().getSupportActionBar();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void invalidateOptionsMenu() {
      this.getDelegate().invalidateOptionsMenu();
   }

   protected void onCreate(Bundle var1) {
      this.getDelegate().installViewFactory();
      super.onCreate(var1);
      this.getDelegate().onCreate(var1);
   }

   protected void onStop() {
      super.onStop();
      this.getDelegate().onStop();
   }

   public void onSupportActionModeFinished(ActionMode var1) {
   }

   public void onSupportActionModeStarted(ActionMode var1) {
   }

   @Nullable
   public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1) {
      return null;
   }

   public void setContentView(@LayoutRes int var1) {
      this.getDelegate().setContentView(var1);
   }

   public void setContentView(View var1) {
      this.getDelegate().setContentView(var1);
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.getDelegate().setContentView(var1, var2);
   }

   public void setTitle(int var1) {
      super.setTitle(var1);
      this.getDelegate().setTitle(this.getContext().getString(var1));
   }

   public void setTitle(CharSequence var1) {
      super.setTitle(var1);
      this.getDelegate().setTitle(var1);
   }

   public boolean supportRequestWindowFeature(int var1) {
      return this.getDelegate().requestWindowFeature(var1);
   }
}
