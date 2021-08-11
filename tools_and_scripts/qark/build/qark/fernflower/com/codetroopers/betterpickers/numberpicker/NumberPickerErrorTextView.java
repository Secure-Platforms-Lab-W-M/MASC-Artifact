package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class NumberPickerErrorTextView extends TextView {
   private static final long LENGTH_SHORT = 3000L;
   private Handler fadeInEndHandler = new Handler();
   private Runnable hideRunnable = new Runnable() {
      public void run() {
         NumberPickerErrorTextView.this.hide();
      }
   };

   public NumberPickerErrorTextView(Context var1) {
      super(var1);
   }

   public NumberPickerErrorTextView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public NumberPickerErrorTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public void hide() {
      this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
      Animation var1 = AnimationUtils.loadAnimation(this.getContext(), 17432577);
      var1.setAnimationListener(new AnimationListener() {
         public void onAnimationEnd(Animation var1) {
            NumberPickerErrorTextView.this.setVisibility(4);
         }

         public void onAnimationRepeat(Animation var1) {
         }

         public void onAnimationStart(Animation var1) {
         }
      });
      this.startAnimation(var1);
   }

   public void hideImmediately() {
      this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
      this.setVisibility(4);
   }

   public void show() {
      this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
      Animation var1 = AnimationUtils.loadAnimation(this.getContext(), 17432576);
      var1.setAnimationListener(new AnimationListener() {
         public void onAnimationEnd(Animation var1) {
            NumberPickerErrorTextView.this.fadeInEndHandler.postDelayed(NumberPickerErrorTextView.this.hideRunnable, 3000L);
            NumberPickerErrorTextView.this.setVisibility(0);
         }

         public void onAnimationRepeat(Animation var1) {
         }

         public void onAnimationStart(Animation var1) {
         }
      });
      this.startAnimation(var1);
   }
}
