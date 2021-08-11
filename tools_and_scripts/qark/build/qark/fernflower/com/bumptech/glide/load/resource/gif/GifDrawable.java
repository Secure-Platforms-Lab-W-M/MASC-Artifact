package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.view.Gravity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GifDrawable extends Drawable implements GifFrameLoader.FrameCallback, Animatable, Animatable2Compat {
   private static final int GRAVITY = 119;
   public static final int LOOP_FOREVER = -1;
   public static final int LOOP_INTRINSIC = 0;
   private List animationCallbacks;
   private boolean applyGravity;
   private Rect destRect;
   private boolean isRecycled;
   private boolean isRunning;
   private boolean isStarted;
   private boolean isVisible;
   private int loopCount;
   private int maxLoopCount;
   private Paint paint;
   private final GifDrawable.GifState state;

   public GifDrawable(Context var1, GifDecoder var2, Transformation var3, int var4, int var5, Bitmap var6) {
      this(new GifDrawable.GifState(new GifFrameLoader(Glide.get(var1), var2, var4, var5, var3, var6)));
   }

   @Deprecated
   public GifDrawable(Context var1, GifDecoder var2, BitmapPool var3, Transformation var4, int var5, int var6, Bitmap var7) {
      this(var1, var2, var4, var5, var6, var7);
   }

   GifDrawable(GifDrawable.GifState var1) {
      this.isVisible = true;
      this.maxLoopCount = -1;
      this.state = (GifDrawable.GifState)Preconditions.checkNotNull(var1);
   }

   GifDrawable(GifFrameLoader var1, Paint var2) {
      this(new GifDrawable.GifState(var1));
      this.paint = var2;
   }

   private Callback findCallback() {
      Callback var1;
      for(var1 = this.getCallback(); var1 instanceof Drawable; var1 = ((Drawable)var1).getCallback()) {
      }

      return var1;
   }

   private Rect getDestRect() {
      if (this.destRect == null) {
         this.destRect = new Rect();
      }

      return this.destRect;
   }

   private Paint getPaint() {
      if (this.paint == null) {
         this.paint = new Paint(2);
      }

      return this.paint;
   }

   private void notifyAnimationEndToListeners() {
      List var3 = this.animationCallbacks;
      if (var3 != null) {
         int var1 = 0;

         for(int var2 = var3.size(); var1 < var2; ++var1) {
            ((Animatable2Compat.AnimationCallback)this.animationCallbacks.get(var1)).onAnimationEnd(this);
         }
      }

   }

   private void resetLoopCount() {
      this.loopCount = 0;
   }

   private void startRunning() {
      Preconditions.checkArgument(this.isRecycled ^ true, "You cannot start a recycled Drawable. Ensure thatyou clear any references to the Drawable when clearing the corresponding request.");
      if (this.state.frameLoader.getFrameCount() == 1) {
         this.invalidateSelf();
      } else {
         if (!this.isRunning) {
            this.isRunning = true;
            this.state.frameLoader.subscribe(this);
            this.invalidateSelf();
         }

      }
   }

   private void stopRunning() {
      this.isRunning = false;
      this.state.frameLoader.unsubscribe(this);
   }

   public void clearAnimationCallbacks() {
      List var1 = this.animationCallbacks;
      if (var1 != null) {
         var1.clear();
      }

   }

   public void draw(Canvas var1) {
      if (!this.isRecycled) {
         if (this.applyGravity) {
            Gravity.apply(119, this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.getBounds(), this.getDestRect());
            this.applyGravity = false;
         }

         var1.drawBitmap(this.state.frameLoader.getCurrentFrame(), (Rect)null, this.getDestRect(), this.getPaint());
      }
   }

   public ByteBuffer getBuffer() {
      return this.state.frameLoader.getBuffer();
   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public Bitmap getFirstFrame() {
      return this.state.frameLoader.getFirstFrame();
   }

   public int getFrameCount() {
      return this.state.frameLoader.getFrameCount();
   }

   public int getFrameIndex() {
      return this.state.frameLoader.getCurrentIndex();
   }

   public Transformation getFrameTransformation() {
      return this.state.frameLoader.getFrameTransformation();
   }

   public int getIntrinsicHeight() {
      return this.state.frameLoader.getHeight();
   }

   public int getIntrinsicWidth() {
      return this.state.frameLoader.getWidth();
   }

   public int getOpacity() {
      return -2;
   }

   public int getSize() {
      return this.state.frameLoader.getSize();
   }

   boolean isRecycled() {
      return this.isRecycled;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.applyGravity = true;
   }

   public void onFrameReady() {
      if (this.findCallback() == null) {
         this.stop();
         this.invalidateSelf();
      } else {
         this.invalidateSelf();
         if (this.getFrameIndex() == this.getFrameCount() - 1) {
            ++this.loopCount;
         }

         int var1 = this.maxLoopCount;
         if (var1 != -1 && this.loopCount >= var1) {
            this.notifyAnimationEndToListeners();
            this.stop();
         }

      }
   }

   public void recycle() {
      this.isRecycled = true;
      this.state.frameLoader.clear();
   }

   public void registerAnimationCallback(Animatable2Compat.AnimationCallback var1) {
      if (var1 != null) {
         if (this.animationCallbacks == null) {
            this.animationCallbacks = new ArrayList();
         }

         this.animationCallbacks.add(var1);
      }
   }

   public void setAlpha(int var1) {
      this.getPaint().setAlpha(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.getPaint().setColorFilter(var1);
   }

   public void setFrameTransformation(Transformation var1, Bitmap var2) {
      this.state.frameLoader.setFrameTransformation(var1, var2);
   }

   void setIsRunning(boolean var1) {
      this.isRunning = var1;
   }

   public void setLoopCount(int var1) {
      byte var2 = -1;
      if (var1 <= 0 && var1 != -1 && var1 != 0) {
         throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
      } else if (var1 == 0) {
         var1 = this.state.frameLoader.getLoopCount();
         if (var1 == 0) {
            var1 = var2;
         }

         this.maxLoopCount = var1;
      } else {
         this.maxLoopCount = var1;
      }
   }

   public boolean setVisible(boolean var1, boolean var2) {
      Preconditions.checkArgument(this.isRecycled ^ true, "Cannot change the visibility of a recycled resource. Ensure that you unset the Drawable from your View before changing the View's visibility.");
      this.isVisible = var1;
      if (!var1) {
         this.stopRunning();
      } else if (this.isStarted) {
         this.startRunning();
      }

      return super.setVisible(var1, var2);
   }

   public void start() {
      this.isStarted = true;
      this.resetLoopCount();
      if (this.isVisible) {
         this.startRunning();
      }

   }

   public void startFromFirstFrame() {
      Preconditions.checkArgument(this.isRunning ^ true, "You cannot restart a currently running animation.");
      this.state.frameLoader.setNextStartFromFirstFrame();
      this.start();
   }

   public void stop() {
      this.isStarted = false;
      this.stopRunning();
   }

   public boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback var1) {
      List var2 = this.animationCallbacks;
      return var2 != null && var1 != null ? var2.remove(var1) : false;
   }

   static final class GifState extends ConstantState {
      final GifFrameLoader frameLoader;

      GifState(GifFrameLoader var1) {
         this.frameLoader = var1;
      }

      public int getChangingConfigurations() {
         return 0;
      }

      public Drawable newDrawable() {
         return new GifDrawable(this);
      }

      public Drawable newDrawable(Resources var1) {
         return this.newDrawable();
      }
   }
}
