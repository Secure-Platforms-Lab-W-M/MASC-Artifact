package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R.attr;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;

class DropDownListView extends ListView {
   public static final int INVALID_POSITION = -1;
   public static final int NO_POSITION = -1;
   private ViewPropertyAnimatorCompat mClickAnimation;
   private boolean mDrawsInPressedState;
   private boolean mHijackFocus;
   private Field mIsChildViewEnabled;
   private boolean mListSelectionHidden;
   private int mMotionPosition;
   DropDownListView.ResolveHoverRunnable mResolveHoverRunnable;
   private ListViewAutoScrollHelper mScrollHelper;
   private int mSelectionBottomPadding = 0;
   private int mSelectionLeftPadding = 0;
   private int mSelectionRightPadding = 0;
   private int mSelectionTopPadding = 0;
   private DropDownListView.GateKeeperDrawable mSelector;
   private final Rect mSelectorRect = new Rect();

   DropDownListView(Context var1, boolean var2) {
      super(var1, (AttributeSet)null, attr.dropDownListViewStyle);
      this.mHijackFocus = var2;
      this.setCacheColorHint(0);

      try {
         Field var4 = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
         this.mIsChildViewEnabled = var4;
         var4.setAccessible(true);
      } catch (NoSuchFieldException var3) {
         var3.printStackTrace();
      }
   }

   private void clearPressedItem() {
      this.mDrawsInPressedState = false;
      this.setPressed(false);
      this.drawableStateChanged();
      View var1 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
      if (var1 != null) {
         var1.setPressed(false);
      }

      ViewPropertyAnimatorCompat var2 = this.mClickAnimation;
      if (var2 != null) {
         var2.cancel();
         this.mClickAnimation = null;
      }

   }

   private void clickPressedItem(View var1, int var2) {
      this.performItemClick(var1, var2, this.getItemIdAtPosition(var2));
   }

   private void drawSelectorCompat(Canvas var1) {
      if (!this.mSelectorRect.isEmpty()) {
         Drawable var2 = this.getSelector();
         if (var2 != null) {
            var2.setBounds(this.mSelectorRect);
            var2.draw(var1);
         }
      }

   }

   private void positionSelectorCompat(int var1, View var2) {
      Rect var4 = this.mSelectorRect;
      var4.set(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom());
      var4.left -= this.mSelectionLeftPadding;
      var4.top -= this.mSelectionTopPadding;
      var4.right += this.mSelectionRightPadding;
      var4.bottom += this.mSelectionBottomPadding;

      IllegalAccessException var10000;
      label41: {
         boolean var10001;
         boolean var3;
         Field var8;
         try {
            var3 = this.mIsChildViewEnabled.getBoolean(this);
            if (var2.isEnabled() == var3) {
               return;
            }

            var8 = this.mIsChildViewEnabled;
         } catch (IllegalAccessException var7) {
            var10000 = var7;
            var10001 = false;
            break label41;
         }

         if (!var3) {
            var3 = true;
         } else {
            var3 = false;
         }

         try {
            var8.set(this, var3);
         } catch (IllegalAccessException var6) {
            var10000 = var6;
            var10001 = false;
            break label41;
         }

         if (var1 == -1) {
            return;
         }

         try {
            this.refreshDrawableState();
            return;
         } catch (IllegalAccessException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IllegalAccessException var9 = var10000;
      var9.printStackTrace();
   }

   private void positionSelectorLikeFocusCompat(int var1, View var2) {
      Drawable var7 = this.getSelector();
      boolean var6 = true;
      boolean var5;
      if (var7 != null && var1 != -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         var7.setVisible(false, false);
      }

      this.positionSelectorCompat(var1, var2);
      if (var5) {
         Rect var8 = this.mSelectorRect;
         float var3 = var8.exactCenterX();
         float var4 = var8.exactCenterY();
         if (this.getVisibility() != 0) {
            var6 = false;
         }

         var7.setVisible(var6, false);
         DrawableCompat.setHotspot(var7, var3, var4);
      }

   }

   private void positionSelectorLikeTouchCompat(int var1, View var2, float var3, float var4) {
      this.positionSelectorLikeFocusCompat(var1, var2);
      Drawable var5 = this.getSelector();
      if (var5 != null && var1 != -1) {
         DrawableCompat.setHotspot(var5, var3, var4);
      }

   }

   private void setPressedItem(View var1, int var2, float var3, float var4) {
      this.mDrawsInPressedState = true;
      if (VERSION.SDK_INT >= 21) {
         this.drawableHotspotChanged(var3, var4);
      }

      if (!this.isPressed()) {
         this.setPressed(true);
      }

      this.layoutChildren();
      int var7 = this.mMotionPosition;
      if (var7 != -1) {
         View var8 = this.getChildAt(var7 - this.getFirstVisiblePosition());
         if (var8 != null && var8 != var1 && var8.isPressed()) {
            var8.setPressed(false);
         }
      }

      this.mMotionPosition = var2;
      float var5 = (float)var1.getLeft();
      float var6 = (float)var1.getTop();
      if (VERSION.SDK_INT >= 21) {
         var1.drawableHotspotChanged(var3 - var5, var4 - var6);
      }

      if (!var1.isPressed()) {
         var1.setPressed(true);
      }

      this.positionSelectorLikeTouchCompat(var2, var1, var3, var4);
      this.setSelectorEnabled(false);
      this.refreshDrawableState();
   }

   private void setSelectorEnabled(boolean var1) {
      DropDownListView.GateKeeperDrawable var2 = this.mSelector;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

   }

   private boolean touchModeDrawsInPressedStateCompat() {
      return this.mDrawsInPressedState;
   }

   private void updateSelectorStateCompat() {
      Drawable var1 = this.getSelector();
      if (var1 != null && this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
         var1.setState(this.getDrawableState());
      }

   }

   protected void dispatchDraw(Canvas var1) {
      this.drawSelectorCompat(var1);
      super.dispatchDraw(var1);
   }

   protected void drawableStateChanged() {
      if (this.mResolveHoverRunnable == null) {
         super.drawableStateChanged();
         this.setSelectorEnabled(true);
         this.updateSelectorStateCompat();
      }
   }

   public boolean hasFocus() {
      return this.mHijackFocus || super.hasFocus();
   }

   public boolean hasWindowFocus() {
      return this.mHijackFocus || super.hasWindowFocus();
   }

   public boolean isFocused() {
      return this.mHijackFocus || super.isFocused();
   }

   public boolean isInTouchMode() {
      return this.mHijackFocus && this.mListSelectionHidden || super.isInTouchMode();
   }

   public int lookForSelectablePosition(int var1, boolean var2) {
      ListAdapter var5 = this.getAdapter();
      if (var5 == null) {
         return -1;
      } else if (this.isInTouchMode()) {
         return -1;
      } else {
         int var4 = var5.getCount();
         if (this.getAdapter().areAllItemsEnabled()) {
            if (var1 >= 0) {
               return var1 >= var4 ? -1 : var1;
            } else {
               return -1;
            }
         } else {
            int var3;
            if (var2) {
               var1 = Math.max(0, var1);

               while(true) {
                  var3 = var1;
                  if (var1 >= var4) {
                     break;
                  }

                  var3 = var1;
                  if (var5.isEnabled(var1)) {
                     break;
                  }

                  ++var1;
               }
            } else {
               var1 = Math.min(var1, var4 - 1);

               while(true) {
                  var3 = var1;
                  if (var1 < 0) {
                     break;
                  }

                  var3 = var1;
                  if (var5.isEnabled(var1)) {
                     break;
                  }

                  --var1;
               }
            }

            if (var3 >= 0) {
               return var3 >= var4 ? -1 : var3;
            } else {
               return -1;
            }
         }
      }
   }

   public int measureHeightOfChildrenCompat(int var1, int var2, int var3, int var4, int var5) {
      int var9 = this.getListPaddingTop();
      int var6 = this.getListPaddingBottom();
      this.getListPaddingLeft();
      this.getListPaddingRight();
      var3 = this.getDividerHeight();
      Drawable var14 = this.getDivider();
      ListAdapter var16 = this.getAdapter();
      if (var16 == null) {
         return var9 + var6;
      } else {
         var2 = var9 + var6;
         if (var3 <= 0 || var14 == null) {
            var3 = 0;
         }

         int var8 = 0;
         View var17 = null;
         int var11 = 0;
         int var13 = var16.getCount();

         int var10;
         for(int var7 = 0; var7 < var13; var11 = var10) {
            int var12 = var16.getItemViewType(var7);
            var10 = var11;
            if (var12 != var11) {
               var17 = null;
               var10 = var12;
            }

            View var15 = var16.getView(var7, var17, this);
            LayoutParams var18 = var15.getLayoutParams();
            if (var18 == null) {
               var18 = this.generateDefaultLayoutParams();
               var15.setLayoutParams(var18);
            }

            if (var18.height > 0) {
               var11 = MeasureSpec.makeMeasureSpec(var18.height, 1073741824);
            } else {
               var11 = MeasureSpec.makeMeasureSpec(0, 0);
            }

            var15.measure(var1, var11);
            var15.forceLayout();
            var11 = var2;
            if (var7 > 0) {
               var11 = var2 + var3;
            }

            var2 = var11 + var15.getMeasuredHeight();
            if (var2 >= var4) {
               if (var5 >= 0 && var7 > var5 && var8 > 0 && var2 != var4) {
                  return var8;
               }

               return var4;
            }

            var11 = var8;
            if (var5 >= 0) {
               var11 = var8;
               if (var7 >= var5) {
                  var11 = var2;
               }
            }

            ++var7;
            var8 = var11;
            var17 = var15;
         }

         return var2;
      }
   }

   protected void onDetachedFromWindow() {
      this.mResolveHoverRunnable = null;
      super.onDetachedFromWindow();
   }

   public boolean onForwardedEvent(MotionEvent var1, int var2) {
      boolean var7;
      boolean var11;
      label44: {
         boolean var8 = true;
         var7 = true;
         boolean var3 = false;
         int var4 = var1.getActionMasked();
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 != 3) {
                  var7 = var8;
                  var11 = var3;
               } else {
                  var7 = false;
                  var11 = var3;
               }
               break label44;
            }
         } else {
            var7 = false;
         }

         int var5 = var1.findPointerIndex(var2);
         if (var5 < 0) {
            var7 = false;
            var11 = var3;
         } else {
            var2 = (int)var1.getX(var5);
            int var6 = (int)var1.getY(var5);
            var5 = this.pointToPosition(var2, var6);
            if (var5 == -1) {
               var11 = true;
            } else {
               View var9 = this.getChildAt(var5 - this.getFirstVisiblePosition());
               this.setPressedItem(var9, var5, (float)var2, (float)var6);
               var8 = true;
               var7 = var8;
               var11 = var3;
               if (var4 == 1) {
                  this.clickPressedItem(var9, var5);
                  var11 = var3;
                  var7 = var8;
               }
            }
         }
      }

      if (!var7 || var11) {
         this.clearPressedItem();
      }

      if (var7) {
         if (this.mScrollHelper == null) {
            this.mScrollHelper = new ListViewAutoScrollHelper(this);
         }

         this.mScrollHelper.setEnabled(true);
         this.mScrollHelper.onTouch(this, var1);
         return var7;
      } else {
         ListViewAutoScrollHelper var10 = this.mScrollHelper;
         if (var10 != null) {
            var10.setEnabled(false);
         }

         return var7;
      }
   }

   public boolean onHoverEvent(MotionEvent var1) {
      if (VERSION.SDK_INT < 26) {
         return super.onHoverEvent(var1);
      } else {
         int var2 = var1.getActionMasked();
         if (var2 == 10 && this.mResolveHoverRunnable == null) {
            DropDownListView.ResolveHoverRunnable var4 = new DropDownListView.ResolveHoverRunnable();
            this.mResolveHoverRunnable = var4;
            var4.post();
         }

         boolean var3 = super.onHoverEvent(var1);
         if (var2 != 9 && var2 != 7) {
            this.setSelection(-1);
            return var3;
         } else {
            var2 = this.pointToPosition((int)var1.getX(), (int)var1.getY());
            if (var2 != -1 && var2 != this.getSelectedItemPosition()) {
               View var5 = this.getChildAt(var2 - this.getFirstVisiblePosition());
               if (var5.isEnabled()) {
                  this.setSelectionFromTop(var2, var5.getTop() - this.getTop());
               }

               this.updateSelectorStateCompat();
            }

            return var3;
         }
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getAction() == 0) {
         this.mMotionPosition = this.pointToPosition((int)var1.getX(), (int)var1.getY());
      }

      DropDownListView.ResolveHoverRunnable var2 = this.mResolveHoverRunnable;
      if (var2 != null) {
         var2.cancel();
      }

      return super.onTouchEvent(var1);
   }

   void setListSelectionHidden(boolean var1) {
      this.mListSelectionHidden = var1;
   }

   public void setSelector(Drawable var1) {
      DropDownListView.GateKeeperDrawable var2;
      if (var1 != null) {
         var2 = new DropDownListView.GateKeeperDrawable(var1);
      } else {
         var2 = null;
      }

      this.mSelector = var2;
      super.setSelector(var2);
      Rect var3 = new Rect();
      if (var1 != null) {
         var1.getPadding(var3);
      }

      this.mSelectionLeftPadding = var3.left;
      this.mSelectionTopPadding = var3.top;
      this.mSelectionRightPadding = var3.right;
      this.mSelectionBottomPadding = var3.bottom;
   }

   private static class GateKeeperDrawable extends DrawableWrapper {
      private boolean mEnabled = true;

      GateKeeperDrawable(Drawable var1) {
         super(var1);
      }

      public void draw(Canvas var1) {
         if (this.mEnabled) {
            super.draw(var1);
         }

      }

      void setEnabled(boolean var1) {
         this.mEnabled = var1;
      }

      public void setHotspot(float var1, float var2) {
         if (this.mEnabled) {
            super.setHotspot(var1, var2);
         }

      }

      public void setHotspotBounds(int var1, int var2, int var3, int var4) {
         if (this.mEnabled) {
            super.setHotspotBounds(var1, var2, var3, var4);
         }

      }

      public boolean setState(int[] var1) {
         return this.mEnabled ? super.setState(var1) : false;
      }

      public boolean setVisible(boolean var1, boolean var2) {
         return this.mEnabled ? super.setVisible(var1, var2) : false;
      }
   }

   private class ResolveHoverRunnable implements Runnable {
      ResolveHoverRunnable() {
      }

      public void cancel() {
         DropDownListView.this.mResolveHoverRunnable = null;
         DropDownListView.this.removeCallbacks(this);
      }

      public void post() {
         DropDownListView.this.post(this);
      }

      public void run() {
         DropDownListView.this.mResolveHoverRunnable = null;
         DropDownListView.this.drawableStateChanged();
      }
   }
}
