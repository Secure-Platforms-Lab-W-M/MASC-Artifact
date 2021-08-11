package androidx.appcompat.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.FragmentActivity;

public class AppCompatActivity extends FragmentActivity implements AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider {
   private AppCompatDelegate mDelegate;
   private Resources mResources;

   public AppCompatActivity() {
   }

   public AppCompatActivity(int var1) {
      super(var1);
   }

   private boolean performMenuItemShortcut(int var1, KeyEvent var2) {
      if (VERSION.SDK_INT < 26 && !var2.isCtrlPressed() && !KeyEvent.metaStateHasNoModifiers(var2.getMetaState()) && var2.getRepeatCount() == 0 && !KeyEvent.isModifierKey(var2.getKeyCode())) {
         Window var3 = this.getWindow();
         if (var3 != null && var3.getDecorView() != null && var3.getDecorView().dispatchKeyShortcutEvent(var2)) {
            return true;
         }
      }

      return false;
   }

   public void addContentView(View var1, LayoutParams var2) {
      this.getDelegate().addContentView(var1, var2);
   }

   protected void attachBaseContext(Context var1) {
      super.attachBaseContext(var1);
      this.getDelegate().attachBaseContext(var1);
   }

   public void closeOptionsMenu() {
      ActionBar var1 = this.getSupportActionBar();
      if (this.getWindow().hasFeature(0) && (var1 == null || !var1.closeOptionsMenu())) {
         super.closeOptionsMenu();
      }

   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      ActionBar var3 = this.getSupportActionBar();
      return var2 == 82 && var3 != null && var3.onMenuKeyEvent(var1) ? true : super.dispatchKeyEvent(var1);
   }

   public View findViewById(int var1) {
      return this.getDelegate().findViewById(var1);
   }

   public AppCompatDelegate getDelegate() {
      if (this.mDelegate == null) {
         this.mDelegate = AppCompatDelegate.create((Activity)this, this);
      }

      return this.mDelegate;
   }

   public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
      return this.getDelegate().getDrawerToggleDelegate();
   }

   public MenuInflater getMenuInflater() {
      return this.getDelegate().getMenuInflater();
   }

   public Resources getResources() {
      if (this.mResources == null && VectorEnabledTintResources.shouldBeUsed()) {
         this.mResources = new VectorEnabledTintResources(this, super.getResources());
      }

      Resources var2 = this.mResources;
      Resources var1 = var2;
      if (var2 == null) {
         var1 = super.getResources();
      }

      return var1;
   }

   public ActionBar getSupportActionBar() {
      return this.getDelegate().getSupportActionBar();
   }

   public Intent getSupportParentActivityIntent() {
      return NavUtils.getParentActivityIntent(this);
   }

   public void invalidateOptionsMenu() {
      this.getDelegate().invalidateOptionsMenu();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (this.mResources != null) {
         DisplayMetrics var2 = super.getResources().getDisplayMetrics();
         this.mResources.updateConfiguration(var1, var2);
      }

      this.getDelegate().onConfigurationChanged(var1);
   }

   public void onContentChanged() {
      this.onSupportContentChanged();
   }

   protected void onCreate(Bundle var1) {
      AppCompatDelegate var2 = this.getDelegate();
      var2.installViewFactory();
      var2.onCreate(var1);
      super.onCreate(var1);
   }

   public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder var1) {
      var1.addParentStack((Activity)this);
   }

   protected void onDestroy() {
      super.onDestroy();
      this.getDelegate().onDestroy();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return this.performMenuItemShortcut(var1, var2) ? true : super.onKeyDown(var1, var2);
   }

   public final boolean onMenuItemSelected(int var1, MenuItem var2) {
      if (super.onMenuItemSelected(var1, var2)) {
         return true;
      } else {
         ActionBar var3 = this.getSupportActionBar();
         return var2.getItemId() == 16908332 && var3 != null && (var3.getDisplayOptions() & 4) != 0 ? this.onSupportNavigateUp() : false;
      }
   }

   public boolean onMenuOpened(int var1, Menu var2) {
      return super.onMenuOpened(var1, var2);
   }

   protected void onNightModeChanged(int var1) {
   }

   public void onPanelClosed(int var1, Menu var2) {
      super.onPanelClosed(var1, var2);
   }

   protected void onPostCreate(Bundle var1) {
      super.onPostCreate(var1);
      this.getDelegate().onPostCreate(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.getDelegate().onPostResume();
   }

   public void onPrepareSupportNavigateUpTaskStack(TaskStackBuilder var1) {
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      this.getDelegate().onSaveInstanceState(var1);
   }

   protected void onStart() {
      super.onStart();
      this.getDelegate().onStart();
   }

   protected void onStop() {
      super.onStop();
      this.getDelegate().onStop();
   }

   public void onSupportActionModeFinished(ActionMode var1) {
   }

   public void onSupportActionModeStarted(ActionMode var1) {
   }

   @Deprecated
   public void onSupportContentChanged() {
   }

   public boolean onSupportNavigateUp() {
      Intent var1 = this.getSupportParentActivityIntent();
      if (var1 != null) {
         if (this.supportShouldUpRecreateTask(var1)) {
            TaskStackBuilder var3 = TaskStackBuilder.create(this);
            this.onCreateSupportNavigateUpTaskStack(var3);
            this.onPrepareSupportNavigateUpTaskStack(var3);
            var3.startActivities();

            try {
               ActivityCompat.finishAffinity(this);
            } catch (IllegalStateException var2) {
               this.finish();
            }
         } else {
            this.supportNavigateUpTo(var1);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void onTitleChanged(CharSequence var1, int var2) {
      super.onTitleChanged(var1, var2);
      this.getDelegate().setTitle(var1);
   }

   public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1) {
      return null;
   }

   public void openOptionsMenu() {
      ActionBar var1 = this.getSupportActionBar();
      if (this.getWindow().hasFeature(0) && (var1 == null || !var1.openOptionsMenu())) {
         super.openOptionsMenu();
      }

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

   public void setSupportActionBar(Toolbar var1) {
      this.getDelegate().setSupportActionBar(var1);
   }

   @Deprecated
   public void setSupportProgress(int var1) {
   }

   @Deprecated
   public void setSupportProgressBarIndeterminate(boolean var1) {
   }

   @Deprecated
   public void setSupportProgressBarIndeterminateVisibility(boolean var1) {
   }

   @Deprecated
   public void setSupportProgressBarVisibility(boolean var1) {
   }

   public void setTheme(int var1) {
      super.setTheme(var1);
      this.getDelegate().setTheme(var1);
   }

   public ActionMode startSupportActionMode(ActionMode.Callback var1) {
      return this.getDelegate().startSupportActionMode(var1);
   }

   public void supportInvalidateOptionsMenu() {
      this.getDelegate().invalidateOptionsMenu();
   }

   public void supportNavigateUpTo(Intent var1) {
      NavUtils.navigateUpTo(this, var1);
   }

   public boolean supportRequestWindowFeature(int var1) {
      return this.getDelegate().requestWindowFeature(var1);
   }

   public boolean supportShouldUpRecreateTask(Intent var1) {
      return NavUtils.shouldUpRecreateTask(this, var1);
   }
}
