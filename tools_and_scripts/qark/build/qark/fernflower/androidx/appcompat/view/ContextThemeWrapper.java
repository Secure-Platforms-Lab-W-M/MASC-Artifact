package androidx.appcompat.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import androidx.appcompat.R.style;

public class ContextThemeWrapper extends ContextWrapper {
   private LayoutInflater mInflater;
   private Configuration mOverrideConfiguration;
   private Resources mResources;
   private Theme mTheme;
   private int mThemeResource;

   public ContextThemeWrapper() {
      super((Context)null);
   }

   public ContextThemeWrapper(Context var1, int var2) {
      super(var1);
      this.mThemeResource = var2;
   }

   public ContextThemeWrapper(Context var1, Theme var2) {
      super(var1);
      this.mTheme = var2;
   }

   private Resources getResourcesInternal() {
      if (this.mResources == null) {
         if (this.mOverrideConfiguration == null) {
            this.mResources = super.getResources();
         } else if (VERSION.SDK_INT >= 17) {
            this.mResources = this.createConfigurationContext(this.mOverrideConfiguration).getResources();
         }
      }

      return this.mResources;
   }

   private void initializeTheme() {
      boolean var1;
      if (this.mTheme == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var1) {
         this.mTheme = this.getResources().newTheme();
         Theme var2 = this.getBaseContext().getTheme();
         if (var2 != null) {
            this.mTheme.setTo(var2);
         }
      }

      this.onApplyThemeResource(this.mTheme, this.mThemeResource, var1);
   }

   public void applyOverrideConfiguration(Configuration var1) {
      if (this.mResources == null) {
         if (this.mOverrideConfiguration == null) {
            this.mOverrideConfiguration = new Configuration(var1);
         } else {
            throw new IllegalStateException("Override configuration has already been set");
         }
      } else {
         throw new IllegalStateException("getResources() or getAssets() has already been called");
      }
   }

   protected void attachBaseContext(Context var1) {
      super.attachBaseContext(var1);
   }

   public AssetManager getAssets() {
      return this.getResources().getAssets();
   }

   public Resources getResources() {
      return this.getResourcesInternal();
   }

   public Object getSystemService(String var1) {
      if ("layout_inflater".equals(var1)) {
         if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.getBaseContext()).cloneInContext(this);
         }

         return this.mInflater;
      } else {
         return this.getBaseContext().getSystemService(var1);
      }
   }

   public Theme getTheme() {
      Theme var1 = this.mTheme;
      if (var1 != null) {
         return var1;
      } else {
         if (this.mThemeResource == 0) {
            this.mThemeResource = style.Theme_AppCompat_Light;
         }

         this.initializeTheme();
         return this.mTheme;
      }
   }

   public int getThemeResId() {
      return this.mThemeResource;
   }

   protected void onApplyThemeResource(Theme var1, int var2, boolean var3) {
      var1.applyStyle(var2, true);
   }

   public void setTheme(int var1) {
      if (this.mThemeResource != var1) {
         this.mThemeResource = var1;
         this.initializeTheme();
      }

   }
}
