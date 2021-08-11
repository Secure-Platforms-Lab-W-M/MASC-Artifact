package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

class CircleImageView extends ImageView {
   private static final int FILL_SHADOW_COLOR = 1023410176;
   private static final int KEY_SHADOW_COLOR = 503316480;
   private static final int SHADOW_ELEVATION = 4;
   private static final float SHADOW_RADIUS = 3.5F;
   private static final float X_OFFSET = 0.0F;
   private static final float Y_OFFSET = 1.75F;
   private AnimationListener mListener;
   int mShadowRadius;

   CircleImageView(Context var1, int var2) {
      super(var1);
      float var3 = this.getContext().getResources().getDisplayMetrics().density;
      int var4 = (int)(1.75F * var3);
      int var5 = (int)(0.0F * var3);
      this.mShadowRadius = (int)(3.5F * var3);
      ShapeDrawable var6;
      if (this.elevationSupported()) {
         var6 = new ShapeDrawable(new OvalShape());
         ViewCompat.setElevation(this, 4.0F * var3);
      } else {
         var6 = new ShapeDrawable(new CircleImageView.OvalShadow(this.mShadowRadius));
         this.setLayerType(1, var6.getPaint());
         var6.getPaint().setShadowLayer((float)this.mShadowRadius, (float)var5, (float)var4, 503316480);
         var4 = this.mShadowRadius;
         this.setPadding(var4, var4, var4, var4);
      }

      var6.getPaint().setColor(var2);
      ViewCompat.setBackground(this, var6);
   }

   private boolean elevationSupported() {
      return VERSION.SDK_INT >= 21;
   }

   public void onAnimationEnd() {
      super.onAnimationEnd();
      AnimationListener var1 = this.mListener;
      if (var1 != null) {
         var1.onAnimationEnd(this.getAnimation());
      }

   }

   public void onAnimationStart() {
      super.onAnimationStart();
      AnimationListener var1 = this.mListener;
      if (var1 != null) {
         var1.onAnimationStart(this.getAnimation());
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (!this.elevationSupported()) {
         this.setMeasuredDimension(this.getMeasuredWidth() + this.mShadowRadius * 2, this.getMeasuredHeight() + this.mShadowRadius * 2);
      }

   }

   public void setAnimationListener(AnimationListener var1) {
      this.mListener = var1;
   }

   public void setBackgroundColor(int var1) {
      if (this.getBackground() instanceof ShapeDrawable) {
         ((ShapeDrawable)this.getBackground()).getPaint().setColor(var1);
      }

   }

   public void setBackgroundColorRes(int var1) {
      this.setBackgroundColor(ContextCompat.getColor(this.getContext(), var1));
   }

   private class OvalShadow extends OvalShape {
      private RadialGradient mRadialGradient;
      private Paint mShadowPaint = new Paint();

      OvalShadow(int var2) {
         CircleImageView.this.mShadowRadius = var2;
         this.updateRadialGradient((int)this.rect().width());
      }

      private void updateRadialGradient(int var1) {
         float var2 = (float)(var1 / 2);
         float var3 = (float)(var1 / 2);
         float var4 = (float)CircleImageView.this.mShadowRadius;
         TileMode var5 = TileMode.CLAMP;
         this.mRadialGradient = new RadialGradient(var2, var3, var4, new int[]{1023410176, 0}, (float[])null, var5);
         this.mShadowPaint.setShader(this.mRadialGradient);
      }

      public void draw(Canvas var1, Paint var2) {
         int var3 = CircleImageView.this.getWidth();
         int var4 = CircleImageView.this.getHeight();
         var1.drawCircle((float)(var3 / 2), (float)(var4 / 2), (float)(var3 / 2), this.mShadowPaint);
         var1.drawCircle((float)(var3 / 2), (float)(var4 / 2), (float)(var3 / 2 - CircleImageView.this.mShadowRadius), var2);
      }

      protected void onResize(float var1, float var2) {
         super.onResize(var1, var2);
         this.updateRadialGradient((int)var1);
      }
   }
}
