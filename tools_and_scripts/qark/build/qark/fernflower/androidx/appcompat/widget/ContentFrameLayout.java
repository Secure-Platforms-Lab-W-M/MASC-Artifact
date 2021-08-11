package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;

public class ContentFrameLayout extends FrameLayout {
   private ContentFrameLayout.OnAttachListener mAttachListener;
   private final Rect mDecorPadding;
   private TypedValue mFixedHeightMajor;
   private TypedValue mFixedHeightMinor;
   private TypedValue mFixedWidthMajor;
   private TypedValue mFixedWidthMinor;
   private TypedValue mMinWidthMajor;
   private TypedValue mMinWidthMinor;

   public ContentFrameLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ContentFrameLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ContentFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mDecorPadding = new Rect();
   }

   public void dispatchFitSystemWindows(Rect var1) {
      this.fitSystemWindows(var1);
   }

   public TypedValue getFixedHeightMajor() {
      if (this.mFixedHeightMajor == null) {
         this.mFixedHeightMajor = new TypedValue();
      }

      return this.mFixedHeightMajor;
   }

   public TypedValue getFixedHeightMinor() {
      if (this.mFixedHeightMinor == null) {
         this.mFixedHeightMinor = new TypedValue();
      }

      return this.mFixedHeightMinor;
   }

   public TypedValue getFixedWidthMajor() {
      if (this.mFixedWidthMajor == null) {
         this.mFixedWidthMajor = new TypedValue();
      }

      return this.mFixedWidthMajor;
   }

   public TypedValue getFixedWidthMinor() {
      if (this.mFixedWidthMinor == null) {
         this.mFixedWidthMinor = new TypedValue();
      }

      return this.mFixedWidthMinor;
   }

   public TypedValue getMinWidthMajor() {
      if (this.mMinWidthMajor == null) {
         this.mMinWidthMajor = new TypedValue();
      }

      return this.mMinWidthMajor;
   }

   public TypedValue getMinWidthMinor() {
      if (this.mMinWidthMinor == null) {
         this.mMinWidthMinor = new TypedValue();
      }

      return this.mMinWidthMinor;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      ContentFrameLayout.OnAttachListener var1 = this.mAttachListener;
      if (var1 != null) {
         var1.onAttachedFromWindow();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      ContentFrameLayout.OnAttachListener var1 = this.mAttachListener;
      if (var1 != null) {
         var1.onDetachedFromWindow();
      }

   }

   protected void onMeasure(int var1, int var2) {
      DisplayMetrics var11 = this.getContext().getResources().getDisplayMetrics();
      boolean var4;
      if (var11.widthPixels < var11.heightPixels) {
         var4 = true;
      } else {
         var4 = false;
      }

      int var8 = MeasureSpec.getMode(var1);
      int var9 = MeasureSpec.getMode(var2);
      boolean var7 = false;
      boolean var5 = var7;
      int var6 = var1;
      int var3;
      TypedValue var10;
      if (var8 == Integer.MIN_VALUE) {
         if (var4) {
            var10 = this.mFixedWidthMinor;
         } else {
            var10 = this.mFixedWidthMajor;
         }

         var5 = var7;
         var6 = var1;
         if (var10 != null) {
            var5 = var7;
            var6 = var1;
            if (var10.type != 0) {
               var3 = 0;
               if (var10.type == 5) {
                  var3 = (int)var10.getDimension(var11);
               } else if (var10.type == 6) {
                  var3 = (int)var10.getFraction((float)var11.widthPixels, (float)var11.widthPixels);
               }

               var5 = var7;
               var6 = var1;
               if (var3 > 0) {
                  var6 = MeasureSpec.makeMeasureSpec(Math.min(var3 - (this.mDecorPadding.left + this.mDecorPadding.right), MeasureSpec.getSize(var1)), 1073741824);
                  var5 = true;
               }
            }
         }
      }

      var3 = var2;
      if (var9 == Integer.MIN_VALUE) {
         if (var4) {
            var10 = this.mFixedHeightMajor;
         } else {
            var10 = this.mFixedHeightMinor;
         }

         var3 = var2;
         if (var10 != null) {
            var3 = var2;
            if (var10.type != 0) {
               var1 = 0;
               if (var10.type == 5) {
                  var1 = (int)var10.getDimension(var11);
               } else if (var10.type == 6) {
                  var1 = (int)var10.getFraction((float)var11.heightPixels, (float)var11.heightPixels);
               }

               var3 = var2;
               if (var1 > 0) {
                  var3 = MeasureSpec.makeMeasureSpec(Math.min(var1 - (this.mDecorPadding.top + this.mDecorPadding.bottom), MeasureSpec.getSize(var2)), 1073741824);
               }
            }
         }
      }

      super.onMeasure(var6, var3);
      var9 = this.getMeasuredWidth();
      boolean var15 = false;
      int var14 = MeasureSpec.makeMeasureSpec(var9, 1073741824);
      boolean var12 = var15;
      var1 = var14;
      if (!var5) {
         var12 = var15;
         var1 = var14;
         if (var8 == Integer.MIN_VALUE) {
            if (var4) {
               var10 = this.mMinWidthMinor;
            } else {
               var10 = this.mMinWidthMajor;
            }

            var12 = var15;
            var1 = var14;
            if (var10 != null) {
               var12 = var15;
               var1 = var14;
               if (var10.type != 0) {
                  var1 = 0;
                  if (var10.type == 5) {
                     var1 = (int)var10.getDimension(var11);
                  } else if (var10.type == 6) {
                     var1 = (int)var10.getFraction((float)var11.widthPixels, (float)var11.widthPixels);
                  }

                  int var13 = var1;
                  if (var1 > 0) {
                     var13 = var1 - (this.mDecorPadding.left + this.mDecorPadding.right);
                  }

                  var12 = var15;
                  var1 = var14;
                  if (var9 < var13) {
                     var1 = MeasureSpec.makeMeasureSpec(var13, 1073741824);
                     var12 = true;
                  }
               }
            }
         }
      }

      if (var12) {
         super.onMeasure(var1, var3);
      }

   }

   public void setAttachListener(ContentFrameLayout.OnAttachListener var1) {
      this.mAttachListener = var1;
   }

   public void setDecorPadding(int var1, int var2, int var3, int var4) {
      this.mDecorPadding.set(var1, var2, var3, var4);
      if (ViewCompat.isLaidOut(this)) {
         this.requestLayout();
      }

   }

   public interface OnAttachListener {
      void onAttachedFromWindow();

      void onDetachedFromWindow();
   }
}
