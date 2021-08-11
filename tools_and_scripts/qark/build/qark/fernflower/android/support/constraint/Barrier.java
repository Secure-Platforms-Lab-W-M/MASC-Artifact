package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;

public class Barrier extends ConstraintHelper {
   public static final int BOTTOM = 3;
   public static final int END = 6;
   public static final int LEFT = 0;
   public static final int RIGHT = 1;
   public static final int START = 5;
   public static final int TOP = 2;
   private android.support.constraint.solver.widgets.Barrier mBarrier;
   private int mIndicatedType;
   private int mResolvedType;

   public Barrier(Context var1) {
      super(var1);
      super.setVisibility(8);
   }

   public Barrier(Context var1, AttributeSet var2) {
      super(var1, var2);
      super.setVisibility(8);
   }

   public Barrier(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      super.setVisibility(8);
   }

   public boolean allowsGoneWidget() {
      return this.mBarrier.allowsGoneWidget();
   }

   public int getType() {
      return this.mIndicatedType;
   }

   protected void init(AttributeSet var1) {
      super.init(var1);
      this.mBarrier = new android.support.constraint.solver.widgets.Barrier();
      if (var1 != null) {
         TypedArray var5 = this.getContext().obtainStyledAttributes(var1, R$styleable.ConstraintLayout_Layout);
         int var3 = var5.getIndexCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            int var4 = var5.getIndex(var2);
            if (var4 == R$styleable.ConstraintLayout_Layout_barrierDirection) {
               this.setType(var5.getInt(var4, 0));
            } else if (var4 == R$styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
               this.mBarrier.setAllowsGoneWidget(var5.getBoolean(var4, true));
            }
         }
      }

      this.mHelperWidget = this.mBarrier;
      this.validateParams();
   }

   public void setAllowsGoneWidget(boolean var1) {
      this.mBarrier.setAllowsGoneWidget(var1);
   }

   public void setType(int var1) {
      this.mIndicatedType = var1;
      this.mResolvedType = var1;
      if (VERSION.SDK_INT < 17) {
         var1 = this.mIndicatedType;
         if (var1 == 5) {
            this.mResolvedType = 0;
         } else if (var1 == 6) {
            this.mResolvedType = 1;
         }
      } else {
         boolean var2;
         if (1 == this.getResources().getConfiguration().getLayoutDirection()) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            var1 = this.mIndicatedType;
            if (var1 == 5) {
               this.mResolvedType = 1;
            } else if (var1 == 6) {
               this.mResolvedType = 0;
            }
         } else {
            var1 = this.mIndicatedType;
            if (var1 == 5) {
               this.mResolvedType = 0;
            } else if (var1 == 6) {
               this.mResolvedType = 1;
            }
         }
      }

      this.mBarrier.setBarrierType(this.mResolvedType);
   }
}
