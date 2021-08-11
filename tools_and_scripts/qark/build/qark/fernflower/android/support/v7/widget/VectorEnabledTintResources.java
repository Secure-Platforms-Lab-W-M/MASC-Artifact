package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AppCompatDelegate;
import java.lang.ref.WeakReference;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class VectorEnabledTintResources extends Resources {
   public static final int MAX_SDK_WHERE_REQUIRED = 20;
   private final WeakReference mContextRef;

   public VectorEnabledTintResources(@NonNull Context var1, @NonNull Resources var2) {
      super(var2.getAssets(), var2.getDisplayMetrics(), var2.getConfiguration());
      this.mContextRef = new WeakReference(var1);
   }

   public static boolean shouldBeUsed() {
      return AppCompatDelegate.isCompatVectorFromResourcesEnabled() && VERSION.SDK_INT <= 20;
   }

   public Drawable getDrawable(int var1) throws NotFoundException {
      Context var2 = (Context)this.mContextRef.get();
      return var2 != null ? AppCompatDrawableManager.get().onDrawableLoadedFromResources(var2, this, var1) : super.getDrawable(var1);
   }

   final Drawable superGetDrawable(int var1) {
      return super.getDrawable(var1);
   }
}
