package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

class TintResources extends ResourcesWrapper {
   private final WeakReference mContextRef;

   public TintResources(@NonNull Context var1, @NonNull Resources var2) {
      super(var2);
      this.mContextRef = new WeakReference(var1);
   }

   public Drawable getDrawable(int var1) throws NotFoundException {
      Drawable var2 = super.getDrawable(var1);
      Context var3 = (Context)this.mContextRef.get();
      if (var2 != null && var3 != null) {
         AppCompatDrawableManager.get();
         AppCompatDrawableManager.tintDrawableUsingColorFilter(var3, var1, var2);
      }

      return var2;
   }
}
