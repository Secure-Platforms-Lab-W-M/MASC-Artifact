package androidx.appcompat.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import androidx.appcompat.R.attr;
import androidx.appcompat.view.ActionMode;
import androidx.core.view.KeyEventDispatcher;

public class AppCompatDialog extends Dialog implements AppCompatCallback {
   private AppCompatDelegate mDelegate;
   private final KeyEventDispatcher.Component mKeyDispatcher;

   public AppCompatDialog(Context var1) {
      this(var1, 0);
   }

   public AppCompatDialog(Context var1, int var2) {
      super(var1, getThemeResId(var1, var2));
      this.mKeyDispatcher = new KeyEventDispatcher.Component() {
         public boolean superDispatchKeyEvent(KeyEvent var1) {
            return AppCompatDialog.this.superDispatchKeyEvent(var1);
         }
      };
      AppCompatDelegate var3 = this.getDelegate();
      var3.setTheme(getThemeResId(var1, var2));
      var3.onCreate((Bundle)null);
   }

   protected AppCompatDialog(Context var1, boolean var2, OnCancelListener var3) {
      super(var1, var2, var3);
      this.mKeyDispatcher = new KeyEventDispatcher.Component() {
         public boolean superDispatchKeyEvent(KeyEvent var1) {
            return AppCompatDialog.this.superDispatchKeyEvent(var1);
         }
      };
   }

   private static int getThemeResId(Context var0, int var1) {
      int var2 = var1;
      if (var1 == 0) {
         TypedValue var3 = new TypedValue();
         var0.getTheme().resolveAttribute(attr.dialogTheme, var3, true);
         var2 = var3.resourceId;
      }

      return var2;
   }

   public void addContentView(View var1, LayoutParams var2) {
      this.getDelegate().addContentView(var1, var2);
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      View var2 = this.getWindow().getDecorView();
      return KeyEventDispatcher.dispatchKeyEvent(this.mKeyDispatcher, var2, this, var1);
   }

   public View findViewById(int var1) {
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

   public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1) {
      return null;
   }

   public void setContentView(int var1) {
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

   boolean superDispatchKeyEvent(KeyEvent var1) {
      return super.dispatchKeyEvent(var1);
   }

   public boolean supportRequestWindowFeature(int var1) {
      return this.getDelegate().requestWindowFeature(var1);
   }
}
