package androidx.core.widget;

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
            ContentLoadingProgressBar.this.mPostedHide = false;
            ContentLoadingProgressBar.this.mStartTime = -1L;
            ContentLoadingProgressBar.this.setVisibility(8);
         }
      };
      this.mDelayedShow = new Runnable() {
         public void run() {
            ContentLoadingProgressBar.this.mPostedShow = false;
            if (!ContentLoadingProgressBar.this.mDismissed) {
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
      synchronized(this){}

      Throwable var10000;
      label201: {
         long var1;
         boolean var10001;
         try {
            this.mDismissed = true;
            this.removeCallbacks(this.mDelayedShow);
            this.mPostedShow = false;
            var1 = System.currentTimeMillis() - this.mStartTime;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label201;
         }

         label192: {
            if (var1 < 500L) {
               try {
                  if (this.mStartTime != -1L) {
                     break label192;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label201;
               }
            }

            try {
               this.setVisibility(8);
               return;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label201;
            }
         }

         label183:
         try {
            if (!this.mPostedHide) {
               this.postDelayed(this.mDelayedHide, 500L - var1);
               this.mPostedHide = true;
            }

            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label183;
         }
      }

      Throwable var3 = var10000;
      throw var3;
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
      synchronized(this){}

      try {
         this.mStartTime = -1L;
         this.mDismissed = false;
         this.removeCallbacks(this.mDelayedHide);
         this.mPostedHide = false;
         if (!this.mPostedShow) {
            this.postDelayed(this.mDelayedShow, 500L);
            this.mPostedShow = true;
         }
      } finally {
         ;
      }

   }
}
