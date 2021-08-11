package android.support.v7.widget;

import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityManager;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TooltipCompatHandler implements OnLongClickListener, OnHoverListener, OnAttachStateChangeListener {
   private static final long HOVER_HIDE_TIMEOUT_MS = 15000L;
   private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000L;
   private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500L;
   private static final String TAG = "TooltipCompatHandler";
   private static TooltipCompatHandler sActiveHandler;
   private final View mAnchor;
   private int mAnchorX;
   private int mAnchorY;
   private boolean mFromTouch;
   private final Runnable mHideRunnable = new Runnable() {
      public void run() {
         TooltipCompatHandler.this.hide();
      }
   };
   private TooltipPopup mPopup;
   private final Runnable mShowRunnable = new Runnable() {
      public void run() {
         TooltipCompatHandler.this.show(false);
      }
   };
   private final CharSequence mTooltipText;

   private TooltipCompatHandler(View var1, CharSequence var2) {
      this.mAnchor = var1;
      this.mTooltipText = var2;
      this.mAnchor.setOnLongClickListener(this);
      this.mAnchor.setOnHoverListener(this);
   }

   private void hide() {
      if (sActiveHandler == this) {
         sActiveHandler = null;
         TooltipPopup var1 = this.mPopup;
         if (var1 != null) {
            var1.hide();
            this.mPopup = null;
            this.mAnchor.removeOnAttachStateChangeListener(this);
         } else {
            Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
         }
      }

      this.mAnchor.removeCallbacks(this.mShowRunnable);
      this.mAnchor.removeCallbacks(this.mHideRunnable);
   }

   public static void setTooltipText(View var0, CharSequence var1) {
      if (TextUtils.isEmpty(var1)) {
         TooltipCompatHandler var2 = sActiveHandler;
         if (var2 != null && var2.mAnchor == var0) {
            var2.hide();
         }

         var0.setOnLongClickListener((OnLongClickListener)null);
         var0.setLongClickable(false);
         var0.setOnHoverListener((OnHoverListener)null);
      } else {
         new TooltipCompatHandler(var0, var1);
      }
   }

   private void show(boolean var1) {
      if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
         TooltipCompatHandler var4 = sActiveHandler;
         if (var4 != null) {
            var4.hide();
         }

         sActiveHandler = this;
         this.mFromTouch = var1;
         this.mPopup = new TooltipPopup(this.mAnchor.getContext());
         this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
         this.mAnchor.addOnAttachStateChangeListener(this);
         long var2;
         if (this.mFromTouch) {
            var2 = 2500L;
         } else if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1) {
            var2 = 3000L - (long)ViewConfiguration.getLongPressTimeout();
         } else {
            var2 = 15000L - (long)ViewConfiguration.getLongPressTimeout();
         }

         this.mAnchor.removeCallbacks(this.mHideRunnable);
         this.mAnchor.postDelayed(this.mHideRunnable, var2);
      }
   }

   public boolean onHover(View var1, MotionEvent var2) {
      if (this.mPopup != null && this.mFromTouch) {
         return false;
      } else {
         AccessibilityManager var4 = (AccessibilityManager)this.mAnchor.getContext().getSystemService("accessibility");
         if (var4.isEnabled() && var4.isTouchExplorationEnabled()) {
            return false;
         } else {
            int var3 = var2.getAction();
            if (var3 != 7) {
               if (var3 != 10) {
                  return false;
               } else {
                  this.hide();
                  return false;
               }
            } else {
               if (this.mAnchor.isEnabled() && this.mPopup == null) {
                  this.mAnchorX = (int)var2.getX();
                  this.mAnchorY = (int)var2.getY();
                  this.mAnchor.removeCallbacks(this.mShowRunnable);
                  this.mAnchor.postDelayed(this.mShowRunnable, (long)ViewConfiguration.getLongPressTimeout());
               }

               return false;
            }
         }
      }
   }

   public boolean onLongClick(View var1) {
      this.mAnchorX = var1.getWidth() / 2;
      this.mAnchorY = var1.getHeight() / 2;
      this.show(true);
      return true;
   }

   public void onViewAttachedToWindow(View var1) {
   }

   public void onViewDetachedFromWindow(View var1) {
      this.hide();
   }
}
