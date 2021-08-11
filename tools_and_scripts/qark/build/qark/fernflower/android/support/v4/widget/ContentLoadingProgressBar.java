package android.support.v4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar {
   private static final int MIN_DELAY = 500;
   private static final int MIN_SHOW_TIME = 500;
   private final Runnable mDelayedHide;
   private final Runnable mDelayedShow;
   boolean mDismissed;
   boolean mPostedHide;
   boolean mPostedShow;
   long mStartTime;

   public ContentLoadingProgressBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ContentLoadingProgressBar(Context var1, AttributeSet var2) {
      super(var1, var2, 0);
      this.mStartTime = -1L;
      this.mPostedHide = false;
      this.mPostedShow = false;
      this.mDismissed = false;
      this.mDelayedHide = new Runnable() {
         public void run() {
            ContentLoadingProgressBar var1 = ContentLoadingProgressBar.this;
            var1.mPostedHide = false;
            var1.mStartTime = -1L;
            var1.setVisibility(8);
         }
      };
      this.mDelayedShow = new Runnable() {
         public void run() {
            ContentLoadingProgressBar var1 = ContentLoadingProgressBar.this;
            var1.mPostedShow = false;
            if (!var1.mDismissed) {
               ContentLoadingProgressBar.this.mStartTime = System.currentTimeMillis();
               ContentLoadingProgressBar.this.setVisibility(0);
            }

         }
      };
   }

   private void removeCallbacks() {
      this.removeCallbacks(this.mDelayedHide);
      this.removeCallbacks(this.mDelayedShow);
   }

   public void hide() {
      this.mDismissed = true;
      this.removeCallbacks(this.mDelayedShow);
      long var3 = System.currentTimeMillis();
      long var1 = this.mStartTime;
      var3 -= var1;
      if (var3 < 500L && var1 != -1L) {
         if (!this.mPostedHide) {
            this.postDelayed(this.mDelayedHide, 500L - var3);
            this.mPostedHide = true;
            return;
         }
      } else {
         this.setVisibility(8);
      }

   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.removeCallbacks();
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks();
   }

   public void show() {
      this.mStartTime = -1L;
      this.mDismissed = false;
      this.removeCallbacks(this.mDelayedHide);
      if (!this.mPostedShow) {
         this.postDelayed(this.mDelayedShow, 500L);
         this.mPostedShow = true;
      }

   }
}
