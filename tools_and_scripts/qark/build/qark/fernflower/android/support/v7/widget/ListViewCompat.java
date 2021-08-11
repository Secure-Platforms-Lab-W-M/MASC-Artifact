package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ListViewCompat extends ListView {
   public static final int INVALID_POSITION = -1;
   public static final int NO_POSITION = -1;
   private static final int[] STATE_SET_NOTHING = new int[]{0};
   private Field mIsChildViewEnabled;
   protected int mMotionPosition;
   int mSelectionBottomPadding;
   int mSelectionLeftPadding;
   int mSelectionRightPadding;
   int mSelectionTopPadding;
   private ListViewCompat.GateKeeperDrawable mSelector;
   final Rect mSelectorRect;

   public ListViewCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ListViewCompat(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ListViewCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSelectorRect = new Rect();
      this.mSelectionLeftPadding = 0;
      this.mSelectionTopPadding = 0;
      this.mSelectionRightPadding = 0;
      this.mSelectionBottomPadding = 0;

      try {
         this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
         this.mIsChildViewEnabled.setAccessible(true);
      } catch (NoSuchFieldException var4) {
         var4.printStackTrace();
      }
   }

   protected void dispatchDraw(Canvas var1) {
      this.drawSelectorCompat(var1);
      super.dispatchDraw(var1);
   }

   protected void drawSelectorCompat(Canvas var1) {
      if (!this.mSelectorRect.isEmpty()) {
         Drawable var2 = this.getSelector();
         if (var2 != null) {
            var2.setBounds(this.mSelectorRect);
            var2.draw(var1);
         }
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.setSelectorEnabled(true);
      this.updateSelectorStateCompat();
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
      int var8 = this.getListPaddingTop();
      int var7 = this.getListPaddingBottom();
      this.getListPaddingLeft();
      this.getListPaddingRight();
      var3 = this.getDividerHeight();
      Drawable var14 = this.getDivider();
      ListAdapter var16 = this.getAdapter();
      if (var16 == null) {
         return var8 + var7;
      } else {
         if (var3 <= 0 || var14 == null) {
            var3 = 0;
         }

         View var17 = null;
         int var13 = var16.getCount();
         int var11 = 0;
         int var6 = 0;
         var2 = var8 + var7;

         int var12;
         for(int var9 = 0; var9 < var13; var6 = var12) {
            var12 = var16.getItemViewType(var9);
            int var10 = var11;
            if (var12 != var11) {
               var17 = null;
               var10 = var12;
            }

            View var15 = var16.getView(var9, var17, this);
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
            if (var9 > 0) {
               var11 = var2 + var3;
            }

            var2 = var11 + var15.getMeasuredHeight();
            if (var2 >= var4) {
               if (var5 >= 0 && var9 > var5 && var6 > 0 && var2 != var4) {
                  return var6;
               }

               return var4;
            }

            var12 = var6;
            if (var5 >= 0) {
               var12 = var6;
               if (var9 >= var5) {
                  var12 = var2;
               }
            }

            ++var9;
            var11 = var10;
            var17 = var15;
         }

         return var2;
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1.getAction() == 0) {
         this.mMotionPosition = this.pointToPosition((int)var1.getX(), (int)var1.getY());
      }

      return super.onTouchEvent(var1);
   }

   protected void positionSelectorCompat(int var1, View var2) {
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

   protected void positionSelectorLikeFocusCompat(int var1, View var2) {
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

   protected void positionSelectorLikeTouchCompat(int var1, View var2, float var3, float var4) {
      this.positionSelectorLikeFocusCompat(var1, var2);
      Drawable var5 = this.getSelector();
      if (var5 != null && var1 != -1) {
         DrawableCompat.setHotspot(var5, var3, var4);
      }

   }

   public void setSelector(Drawable var1) {
      ListViewCompat.GateKeeperDrawable var2;
      if (var1 != null) {
         var2 = new ListViewCompat.GateKeeperDrawable(var1);
      } else {
         var2 = null;
      }

      this.mSelector = var2;
      super.setSelector(this.mSelector);
      Rect var3 = new Rect();
      if (var1 != null) {
         var1.getPadding(var3);
      }

      this.mSelectionLeftPadding = var3.left;
      this.mSelectionTopPadding = var3.top;
      this.mSelectionRightPadding = var3.right;
      this.mSelectionBottomPadding = var3.bottom;
   }

   protected void setSelectorEnabled(boolean var1) {
      ListViewCompat.GateKeeperDrawable var2 = this.mSelector;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

   }

   protected boolean shouldShowSelectorCompat() {
      return this.touchModeDrawsInPressedStateCompat() && this.isPressed();
   }

   protected boolean touchModeDrawsInPressedStateCompat() {
      return false;
   }

   protected void updateSelectorStateCompat() {
      Drawable var1 = this.getSelector();
      if (var1 != null && this.shouldShowSelectorCompat()) {
         var1.setState(this.getDrawableState());
      }

   }

   private static class GateKeeperDrawable extends DrawableWrapper {
      private boolean mEnabled = true;

      public GateKeeperDrawable(Drawable var1) {
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
}
