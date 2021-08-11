package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.graphics.BitmapCompat;
import androidx.core.view.GravityCompat;
import java.io.InputStream;

public final class RoundedBitmapDrawableFactory {
   private static final String TAG = "RoundedBitmapDrawableFa";

   private RoundedBitmapDrawableFactory() {
   }

   public static RoundedBitmapDrawable create(Resources var0, Bitmap var1) {
      return (RoundedBitmapDrawable)(VERSION.SDK_INT >= 21 ? new RoundedBitmapDrawable21(var0, var1) : new RoundedBitmapDrawableFactory.DefaultRoundedBitmapDrawable(var0, var1));
   }

   public static RoundedBitmapDrawable create(Resources var0, InputStream var1) {
      RoundedBitmapDrawable var3 = create(var0, BitmapFactory.decodeStream(var1));
      if (var3.getBitmap() == null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("RoundedBitmapDrawable cannot decode ");
         var2.append(var1);
         Log.w("RoundedBitmapDrawableFa", var2.toString());
      }

      return var3;
   }

   public static RoundedBitmapDrawable create(Resources var0, String var1) {
      RoundedBitmapDrawable var3 = create(var0, BitmapFactory.decodeFile(var1));
      if (var3.getBitmap() == null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("RoundedBitmapDrawable cannot decode ");
         var2.append(var1);
         Log.w("RoundedBitmapDrawableFa", var2.toString());
      }

      return var3;
   }

   private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
      DefaultRoundedBitmapDrawable(Resources var1, Bitmap var2) {
         super(var1, var2);
      }

      void gravityCompatApply(int var1, int var2, int var3, Rect var4, Rect var5) {
         GravityCompat.apply(var1, var2, var3, var4, var5, 0);
      }

      public boolean hasMipMap() {
         return this.mBitmap != null && BitmapCompat.hasMipMap(this.mBitmap);
      }

      public void setMipMap(boolean var1) {
         if (this.mBitmap != null) {
            BitmapCompat.setHasMipMap(this.mBitmap, var1);
            this.invalidateSelf();
         }

      }
   }
}
