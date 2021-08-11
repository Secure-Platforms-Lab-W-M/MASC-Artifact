package androidx.appcompat.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.core.graphics.drawable.WrappedDrawable;

class AppCompatProgressBarHelper {
   private static final int[] TINT_ATTRS = new int[]{16843067, 16843068};
   private Bitmap mSampleTile;
   private final ProgressBar mView;

   AppCompatProgressBarHelper(ProgressBar var1) {
      this.mView = var1;
   }

   private Shape getDrawableShape() {
      return new RoundRectShape(new float[]{5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F}, (RectF)null, (float[])null);
   }

   private Drawable tileify(Drawable var1, boolean var2) {
      if (var1 instanceof WrappedDrawable) {
         Drawable var12 = ((WrappedDrawable)var1).getWrappedDrawable();
         if (var12 != null) {
            var12 = this.tileify(var12, var2);
            ((WrappedDrawable)var1).setWrappedDrawable(var12);
         }

         return var1;
      } else if (!(var1 instanceof LayerDrawable)) {
         if (var1 instanceof BitmapDrawable) {
            BitmapDrawable var9 = (BitmapDrawable)var1;
            Bitmap var13 = var9.getBitmap();
            if (this.mSampleTile == null) {
               this.mSampleTile = var13;
            }

            ShapeDrawable var11 = new ShapeDrawable(this.getDrawableShape());
            BitmapShader var14 = new BitmapShader(var13, TileMode.REPEAT, TileMode.CLAMP);
            var11.getPaint().setShader(var14);
            var11.getPaint().setColorFilter(var9.getPaint().getColorFilter());
            return (Drawable)(var2 ? new ClipDrawable(var11, 3, 1) : var11);
         } else {
            return var1;
         }
      } else {
         LayerDrawable var8 = (LayerDrawable)var1;
         int var4 = var8.getNumberOfLayers();
         Drawable[] var6 = new Drawable[var4];

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            int var5 = var8.getId(var3);
            Drawable var7 = var8.getDrawable(var3);
            if (var5 != 16908301 && var5 != 16908303) {
               var2 = false;
            } else {
               var2 = true;
            }

            var6[var3] = this.tileify(var7, var2);
         }

         LayerDrawable var10 = new LayerDrawable(var6);

         for(var3 = 0; var3 < var4; ++var3) {
            var10.setId(var3, var8.getId(var3));
         }

         return var10;
      }
   }

   private Drawable tileifyIndeterminate(Drawable var1) {
      Object var4 = var1;
      if (var1 instanceof AnimationDrawable) {
         AnimationDrawable var6 = (AnimationDrawable)var1;
         int var3 = var6.getNumberOfFrames();
         var4 = new AnimationDrawable();
         ((AnimationDrawable)var4).setOneShot(var6.isOneShot());

         for(int var2 = 0; var2 < var3; ++var2) {
            Drawable var5 = this.tileify(var6.getFrame(var2), true);
            var5.setLevel(10000);
            ((AnimationDrawable)var4).addFrame(var5, var6.getDuration(var2));
         }

         ((AnimationDrawable)var4).setLevel(10000);
      }

      return (Drawable)var4;
   }

   Bitmap getSampleTile() {
      return this.mSampleTile;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, TINT_ATTRS, var2, 0);
      Drawable var3 = var4.getDrawableIfKnown(0);
      if (var3 != null) {
         this.mView.setIndeterminateDrawable(this.tileifyIndeterminate(var3));
      }

      var3 = var4.getDrawableIfKnown(1);
      if (var3 != null) {
         this.mView.setProgressDrawable(this.tileify(var3, false));
      }

      var4.recycle();
   }
}
