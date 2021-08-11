package com.bumptech.glide.request.target;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import com.bumptech.glide.util.Preconditions;

public class FixedSizeDrawable extends Drawable {
   private final RectF bounds;
   private final Matrix matrix;
   private boolean mutated;
   private FixedSizeDrawable.State state;
   private Drawable wrapped;
   private final RectF wrappedRect;

   public FixedSizeDrawable(Drawable var1, int var2, int var3) {
      this(new FixedSizeDrawable.State(var1.getConstantState(), var2, var3), var1);
   }

   FixedSizeDrawable(FixedSizeDrawable.State var1, Drawable var2) {
      this.state = (FixedSizeDrawable.State)Preconditions.checkNotNull(var1);
      this.wrapped = (Drawable)Preconditions.checkNotNull(var2);
      var2.setBounds(0, 0, var2.getIntrinsicWidth(), var2.getIntrinsicHeight());
      this.matrix = new Matrix();
      this.wrappedRect = new RectF(0.0F, 0.0F, (float)var2.getIntrinsicWidth(), (float)var2.getIntrinsicHeight());
      this.bounds = new RectF();
   }

   private void updateMatrix() {
      this.matrix.setRectToRect(this.wrappedRect, this.bounds, ScaleToFit.CENTER);
   }

   public void clearColorFilter() {
      this.wrapped.clearColorFilter();
   }

   public void draw(Canvas var1) {
      var1.save();
      var1.concat(this.matrix);
      this.wrapped.draw(var1);
      var1.restore();
   }

   public int getAlpha() {
      return this.wrapped.getAlpha();
   }

   public Callback getCallback() {
      return this.wrapped.getCallback();
   }

   public int getChangingConfigurations() {
      return this.wrapped.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public Drawable getCurrent() {
      return this.wrapped.getCurrent();
   }

   public int getIntrinsicHeight() {
      return this.state.height;
   }

   public int getIntrinsicWidth() {
      return this.state.width;
   }

   public int getMinimumHeight() {
      return this.wrapped.getMinimumHeight();
   }

   public int getMinimumWidth() {
      return this.wrapped.getMinimumWidth();
   }

   public int getOpacity() {
      return this.wrapped.getOpacity();
   }

   public boolean getPadding(Rect var1) {
      return this.wrapped.getPadding(var1);
   }

   public void invalidateSelf() {
      super.invalidateSelf();
      this.wrapped.invalidateSelf();
   }

   public Drawable mutate() {
      if (!this.mutated && super.mutate() == this) {
         this.wrapped = this.wrapped.mutate();
         this.state = new FixedSizeDrawable.State(this.state);
         this.mutated = true;
      }

      return this;
   }

   public void scheduleSelf(Runnable var1, long var2) {
      super.scheduleSelf(var1, var2);
      this.wrapped.scheduleSelf(var1, var2);
   }

   public void setAlpha(int var1) {
      this.wrapped.setAlpha(var1);
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.bounds.set((float)var1, (float)var2, (float)var3, (float)var4);
      this.updateMatrix();
   }

   public void setBounds(Rect var1) {
      super.setBounds(var1);
      this.bounds.set(var1);
      this.updateMatrix();
   }

   public void setChangingConfigurations(int var1) {
      this.wrapped.setChangingConfigurations(var1);
   }

   public void setColorFilter(int var1, Mode var2) {
      this.wrapped.setColorFilter(var1, var2);
   }

   public void setColorFilter(ColorFilter var1) {
      this.wrapped.setColorFilter(var1);
   }

   @Deprecated
   public void setDither(boolean var1) {
      this.wrapped.setDither(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.wrapped.setFilterBitmap(var1);
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return this.wrapped.setVisible(var1, var2);
   }

   public void unscheduleSelf(Runnable var1) {
      super.unscheduleSelf(var1);
      this.wrapped.unscheduleSelf(var1);
   }

   static final class State extends ConstantState {
      final int height;
      final int width;
      private final ConstantState wrapped;

      State(ConstantState var1, int var2, int var3) {
         this.wrapped = var1;
         this.width = var2;
         this.height = var3;
      }

      State(FixedSizeDrawable.State var1) {
         this(var1.wrapped, var1.width, var1.height);
      }

      public int getChangingConfigurations() {
         return 0;
      }

      public Drawable newDrawable() {
         return new FixedSizeDrawable(this, this.wrapped.newDrawable());
      }

      public Drawable newDrawable(Resources var1) {
         return new FixedSizeDrawable(this, this.wrapped.newDrawable(var1));
      }
   }
}
