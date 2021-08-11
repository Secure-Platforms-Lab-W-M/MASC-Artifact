package androidx.recyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
   private static final int[] ATTRS = new int[]{16843284};
   public static final int HORIZONTAL = 0;
   private static final String TAG = "DividerItem";
   public static final int VERTICAL = 1;
   private final Rect mBounds = new Rect();
   private Drawable mDivider;
   private int mOrientation;

   public DividerItemDecoration(Context var1, int var2) {
      TypedArray var4 = var1.obtainStyledAttributes(ATTRS);
      Drawable var3 = var4.getDrawable(0);
      this.mDivider = var3;
      if (var3 == null) {
         Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
      }

      var4.recycle();
      this.setOrientation(var2);
   }

   private void drawHorizontal(Canvas var1, RecyclerView var2) {
      var1.save();
      int var3;
      int var4;
      if (var2.getClipToPadding()) {
         var3 = var2.getPaddingTop();
         var4 = var2.getHeight() - var2.getPaddingBottom();
         var1.clipRect(var2.getPaddingLeft(), var3, var2.getWidth() - var2.getPaddingRight(), var4);
      } else {
         var3 = 0;
         var4 = var2.getHeight();
      }

      int var6 = var2.getChildCount();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var9 = var2.getChildAt(var5);
         var2.getLayoutManager().getDecoratedBoundsWithMargins(var9, this.mBounds);
         int var7 = this.mBounds.right + Math.round(var9.getTranslationX());
         int var8 = this.mDivider.getIntrinsicWidth();
         this.mDivider.setBounds(var7 - var8, var3, var7, var4);
         this.mDivider.draw(var1);
      }

      var1.restore();
   }

   private void drawVertical(Canvas var1, RecyclerView var2) {
      var1.save();
      int var3;
      int var4;
      if (var2.getClipToPadding()) {
         var3 = var2.getPaddingLeft();
         var4 = var2.getWidth() - var2.getPaddingRight();
         var1.clipRect(var3, var2.getPaddingTop(), var4, var2.getHeight() - var2.getPaddingBottom());
      } else {
         var3 = 0;
         var4 = var2.getWidth();
      }

      int var6 = var2.getChildCount();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var9 = var2.getChildAt(var5);
         var2.getDecoratedBoundsWithMargins(var9, this.mBounds);
         int var7 = this.mBounds.bottom + Math.round(var9.getTranslationY());
         int var8 = this.mDivider.getIntrinsicHeight();
         this.mDivider.setBounds(var3, var7 - var8, var4, var7);
         this.mDivider.draw(var1);
      }

      var1.restore();
   }

   public Drawable getDrawable() {
      return this.mDivider;
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
      Drawable var5 = this.mDivider;
      if (var5 == null) {
         var1.set(0, 0, 0, 0);
      } else if (this.mOrientation == 1) {
         var1.set(0, 0, 0, var5.getIntrinsicHeight());
      } else {
         var1.set(0, 0, var5.getIntrinsicWidth(), 0);
      }
   }

   public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      if (var2.getLayoutManager() != null) {
         if (this.mDivider != null) {
            if (this.mOrientation == 1) {
               this.drawVertical(var1, var2);
            } else {
               this.drawHorizontal(var1, var2);
            }
         }
      }
   }

   public void setDrawable(Drawable var1) {
      if (var1 != null) {
         this.mDivider = var1;
      } else {
         throw new IllegalArgumentException("Drawable cannot be null.");
      }
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
      } else {
         this.mOrientation = var1;
      }
   }
}
