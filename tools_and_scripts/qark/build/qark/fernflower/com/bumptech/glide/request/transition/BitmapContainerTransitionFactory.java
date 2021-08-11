package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.DataSource;

public abstract class BitmapContainerTransitionFactory implements TransitionFactory {
   private final TransitionFactory realFactory;

   public BitmapContainerTransitionFactory(TransitionFactory var1) {
      this.realFactory = var1;
   }

   public Transition build(DataSource var1, boolean var2) {
      return new BitmapContainerTransitionFactory.BitmapGlideAnimation(this.realFactory.build(var1, var2));
   }

   protected abstract Bitmap getBitmap(Object var1);

   private final class BitmapGlideAnimation implements Transition {
      private final Transition transition;

      BitmapGlideAnimation(Transition var2) {
         this.transition = var2;
      }

      public boolean transition(Object var1, Transition.ViewAdapter var2) {
         BitmapDrawable var3 = new BitmapDrawable(var2.getView().getResources(), BitmapContainerTransitionFactory.this.getBitmap(var1));
         return this.transition.transition(var3, var2);
      }
   }
}
