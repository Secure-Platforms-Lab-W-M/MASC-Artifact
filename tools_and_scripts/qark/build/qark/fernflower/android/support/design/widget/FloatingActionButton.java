package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R$dimen;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(FloatingActionButton.Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton {
   private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
   private static final String LOG_TAG = "FloatingActionButton";
   public static final int SIZE_AUTO = -1;
   public static final int SIZE_MINI = 1;
   public static final int SIZE_NORMAL = 0;
   private ColorStateList mBackgroundTint;
   private Mode mBackgroundTintMode;
   private int mBorderWidth;
   boolean mCompatPadding;
   private AppCompatImageHelper mImageHelper;
   int mImagePadding;
   private FloatingActionButtonImpl mImpl;
   private int mMaxImageSize;
   private int mRippleColor;
   final Rect mShadowPadding;
   private int mSize;
   private final Rect mTouchArea;

   public FloatingActionButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public FloatingActionButton(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public FloatingActionButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mShadowPadding = new Rect();
      this.mTouchArea = new Rect();
      ThemeUtils.checkAppCompatTheme(var1);
      TypedArray var6 = var1.obtainStyledAttributes(var2, R$styleable.FloatingActionButton, var3, R$style.Widget_Design_FloatingActionButton);
      this.mBackgroundTint = var6.getColorStateList(R$styleable.FloatingActionButton_backgroundTint);
      this.mBackgroundTintMode = ViewUtils.parseTintMode(var6.getInt(R$styleable.FloatingActionButton_backgroundTintMode, -1), (Mode)null);
      this.mRippleColor = var6.getColor(R$styleable.FloatingActionButton_rippleColor, 0);
      this.mSize = var6.getInt(R$styleable.FloatingActionButton_fabSize, -1);
      this.mBorderWidth = var6.getDimensionPixelSize(R$styleable.FloatingActionButton_borderWidth, 0);
      float var4 = var6.getDimension(R$styleable.FloatingActionButton_elevation, 0.0F);
      float var5 = var6.getDimension(R$styleable.FloatingActionButton_pressedTranslationZ, 0.0F);
      this.mCompatPadding = var6.getBoolean(R$styleable.FloatingActionButton_useCompatPadding, false);
      var6.recycle();
      this.mImageHelper = new AppCompatImageHelper(this);
      this.mImageHelper.loadFromAttributes(var2, var3);
      this.mMaxImageSize = (int)this.getResources().getDimension(R$dimen.design_fab_image_size);
      this.getImpl().setBackgroundDrawable(this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
      this.getImpl().setElevation(var4);
      this.getImpl().setPressedTranslationZ(var5);
   }

   private FloatingActionButtonImpl createImpl() {
      return (FloatingActionButtonImpl)(VERSION.SDK_INT >= 21 ? new FloatingActionButtonLollipop(this, new FloatingActionButton.ShadowDelegateImpl()) : new FloatingActionButtonImpl(this, new FloatingActionButton.ShadowDelegateImpl()));
   }

   private FloatingActionButtonImpl getImpl() {
      if (this.mImpl == null) {
         this.mImpl = this.createImpl();
      }

      return this.mImpl;
   }

   private int getSizeDimension(int var1) {
      Resources var2 = this.getResources();
      if (var1 != -1) {
         return var1 != 1 ? var2.getDimensionPixelSize(R$dimen.design_fab_size_normal) : var2.getDimensionPixelSize(R$dimen.design_fab_size_mini);
      } else {
         return Math.max(var2.getConfiguration().screenWidthDp, var2.getConfiguration().screenHeightDp) < 470 ? this.getSizeDimension(1) : this.getSizeDimension(0);
      }
   }

   private static int resolveAdjustedSize(int var0, int var1) {
      int var2 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      if (var2 != Integer.MIN_VALUE) {
         if (var2 != 0) {
            return var2 != 1073741824 ? var0 : var1;
         } else {
            return var0;
         }
      } else {
         return Math.min(var0, var1);
      }
   }

   @Nullable
   private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(@Nullable final FloatingActionButton.OnVisibilityChangedListener var1) {
      return var1 == null ? null : new FloatingActionButtonImpl.InternalVisibilityChangedListener() {
         public void onHidden() {
            var1.onHidden(FloatingActionButton.this);
         }

         public void onShown() {
            var1.onShown(FloatingActionButton.this);
         }
      };
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.getImpl().onDrawableStateChanged(this.getDrawableState());
   }

   @Nullable
   public ColorStateList getBackgroundTintList() {
      return this.mBackgroundTint;
   }

   @Nullable
   public Mode getBackgroundTintMode() {
      return this.mBackgroundTintMode;
   }

   public float getCompatElevation() {
      return this.getImpl().getElevation();
   }

   @NonNull
   public Drawable getContentBackground() {
      return this.getImpl().getContentBackground();
   }

   public boolean getContentRect(@NonNull Rect var1) {
      if (ViewCompat.isLaidOut(this)) {
         var1.set(0, 0, this.getWidth(), this.getHeight());
         var1.left += this.mShadowPadding.left;
         var1.top += this.mShadowPadding.top;
         var1.right -= this.mShadowPadding.right;
         var1.bottom -= this.mShadowPadding.bottom;
         return true;
      } else {
         return false;
      }
   }

   @ColorInt
   public int getRippleColor() {
      return this.mRippleColor;
   }

   public int getSize() {
      return this.mSize;
   }

   int getSizeDimension() {
      return this.getSizeDimension(this.mSize);
   }

   public boolean getUseCompatPadding() {
      return this.mCompatPadding;
   }

   public void hide() {
      this.hide((FloatingActionButton.OnVisibilityChangedListener)null);
   }

   public void hide(@Nullable FloatingActionButton.OnVisibilityChangedListener var1) {
      this.hide(var1, true);
   }

   void hide(@Nullable FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().hide(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      this.getImpl().jumpDrawableToCurrentState();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.getImpl().onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.getImpl().onDetachedFromWindow();
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.getSizeDimension();
      this.mImagePadding = (var3 - this.mMaxImageSize) / 2;
      this.getImpl().updatePadding();
      var1 = Math.min(resolveAdjustedSize(var3, var1), resolveAdjustedSize(var3, var2));
      this.setMeasuredDimension(this.mShadowPadding.left + var1 + this.mShadowPadding.right, this.mShadowPadding.top + var1 + this.mShadowPadding.bottom);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return var1.getAction() == 0 && this.getContentRect(this.mTouchArea) && !this.mTouchArea.contains((int)var1.getX(), (int)var1.getY()) ? false : super.onTouchEvent(var1);
   }

   public void setBackgroundColor(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundDrawable(Drawable var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundResource(int var1) {
      Log.i("FloatingActionButton", "Setting a custom background is not supported.");
   }

   public void setBackgroundTintList(@Nullable ColorStateList var1) {
      if (this.mBackgroundTint != var1) {
         this.mBackgroundTint = var1;
         this.getImpl().setBackgroundTintList(var1);
      }
   }

   public void setBackgroundTintMode(@Nullable Mode var1) {
      if (this.mBackgroundTintMode != var1) {
         this.mBackgroundTintMode = var1;
         this.getImpl().setBackgroundTintMode(var1);
      }
   }

   public void setCompatElevation(float var1) {
      this.getImpl().setElevation(var1);
   }

   public void setImageResource(@DrawableRes int var1) {
      this.mImageHelper.setImageResource(var1);
   }

   public void setRippleColor(@ColorInt int var1) {
      if (this.mRippleColor != var1) {
         this.mRippleColor = var1;
         this.getImpl().setRippleColor(var1);
      }
   }

   public void setSize(int var1) {
      if (var1 != this.mSize) {
         this.mSize = var1;
         this.requestLayout();
      }
   }

   public void setUseCompatPadding(boolean var1) {
      if (this.mCompatPadding != var1) {
         this.mCompatPadding = var1;
         this.getImpl().onCompatShadowChanged();
      }
   }

   public void show() {
      this.show((FloatingActionButton.OnVisibilityChangedListener)null);
   }

   public void show(@Nullable FloatingActionButton.OnVisibilityChangedListener var1) {
      this.show(var1, true);
   }

   void show(FloatingActionButton.OnVisibilityChangedListener var1, boolean var2) {
      this.getImpl().show(this.wrapOnVisibilityChangedListener(var1), var2);
   }

   public static class Behavior extends CoordinatorLayout.Behavior {
      private static final boolean AUTO_HIDE_DEFAULT = true;
      private boolean mAutoHideEnabled;
      private FloatingActionButton.OnVisibilityChangedListener mInternalAutoHideListener;
      private Rect mTmpRect;

      public Behavior() {
         this.mAutoHideEnabled = true;
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.FloatingActionButton_Behavior_Layout);
         this.mAutoHideEnabled = var3.getBoolean(R$styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
         var3.recycle();
      }

      private static boolean isBottomSheet(@NonNull View var0) {
         LayoutParams var1 = var0.getLayoutParams();
         return var1 instanceof CoordinatorLayout.LayoutParams ? ((CoordinatorLayout.LayoutParams)var1).getBehavior() instanceof BottomSheetBehavior : false;
      }

      private void offsetIfNeeded(CoordinatorLayout var1, FloatingActionButton var2) {
         Rect var5 = var2.mShadowPadding;
         if (var5 != null && var5.centerX() > 0 && var5.centerY() > 0) {
            CoordinatorLayout.LayoutParams var6 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            int var4 = 0;
            int var3 = 0;
            if (var2.getRight() >= var1.getWidth() - var6.rightMargin) {
               var3 = var5.right;
            } else if (var2.getLeft() <= var6.leftMargin) {
               var3 = -var5.left;
            }

            if (var2.getBottom() >= var1.getHeight() - var6.bottomMargin) {
               var4 = var5.bottom;
            } else if (var2.getTop() <= var6.topMargin) {
               var4 = -var5.top;
            }

            if (var4 != 0) {
               ViewCompat.offsetTopAndBottom(var2, var4);
            }

            if (var3 != 0) {
               ViewCompat.offsetLeftAndRight(var2, var3);
            }
         }
      }

      private boolean shouldUpdateVisibility(View var1, FloatingActionButton var2) {
         CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
         if (!this.mAutoHideEnabled) {
            return false;
         } else if (var3.getAnchorId() != var1.getId()) {
            return false;
         } else {
            return var2.getUserSetVisibility() == 0;
         }
      }

      private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout var1, AppBarLayout var2, FloatingActionButton var3) {
         if (!this.shouldUpdateVisibility(var2, var3)) {
            return false;
         } else {
            if (this.mTmpRect == null) {
               this.mTmpRect = new Rect();
            }

            Rect var4 = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(var1, var2, var4);
            if (var4.bottom <= var2.getMinimumHeightForVisibleOverlappingContent()) {
               var3.hide(this.mInternalAutoHideListener, false);
            } else {
               var3.show(this.mInternalAutoHideListener, false);
            }

            return true;
         }
      }

      private boolean updateFabVisibilityForBottomSheet(View var1, FloatingActionButton var2) {
         if (!this.shouldUpdateVisibility(var1, var2)) {
            return false;
         } else {
            CoordinatorLayout.LayoutParams var3 = (CoordinatorLayout.LayoutParams)var2.getLayoutParams();
            if (var1.getTop() < var2.getHeight() / 2 + var3.topMargin) {
               var2.hide(this.mInternalAutoHideListener, false);
            } else {
               var2.show(this.mInternalAutoHideListener, false);
            }

            return true;
         }
      }

      public boolean getInsetDodgeRect(@NonNull CoordinatorLayout var1, @NonNull FloatingActionButton var2, @NonNull Rect var3) {
         Rect var4 = var2.mShadowPadding;
         var3.set(var2.getLeft() + var4.left, var2.getTop() + var4.top, var2.getRight() - var4.right, var2.getBottom() - var4.bottom);
         return true;
      }

      public boolean isAutoHideEnabled() {
         return this.mAutoHideEnabled;
      }

      public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams var1) {
         if (var1.dodgeInsetEdges == 0) {
            var1.dodgeInsetEdges = 80;
         }
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, FloatingActionButton var2, View var3) {
         if (var3 instanceof AppBarLayout) {
            this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var3, var2);
         } else if (isBottomSheet(var3)) {
            this.updateFabVisibilityForBottomSheet(var3, var2);
         }

         return false;
      }

      public boolean onLayoutChild(CoordinatorLayout var1, FloatingActionButton var2, int var3) {
         List var6 = var1.getDependencies(var2);
         int var4 = 0;

         for(int var5 = var6.size(); var4 < var5; ++var4) {
            View var7 = (View)var6.get(var4);
            if (var7 instanceof AppBarLayout) {
               if (this.updateFabVisibilityForAppBarLayout(var1, (AppBarLayout)var7, var2)) {
                  break;
               }
            } else if (isBottomSheet(var7) && this.updateFabVisibilityForBottomSheet(var7, var2)) {
               break;
            }
         }

         var1.onLayoutChild(var2, var3);
         this.offsetIfNeeded(var1, var2);
         return true;
      }

      public void setAutoHideEnabled(boolean var1) {
         this.mAutoHideEnabled = var1;
      }

      @VisibleForTesting
      void setInternalAutoHideListener(FloatingActionButton.OnVisibilityChangedListener var1) {
         this.mInternalAutoHideListener = var1;
      }
   }

   public abstract static class OnVisibilityChangedListener {
      public void onHidden(FloatingActionButton var1) {
      }

      public void onShown(FloatingActionButton var1) {
      }
   }

   private class ShadowDelegateImpl implements ShadowViewDelegate {
      ShadowDelegateImpl() {
      }

      public float getRadius() {
         return (float)FloatingActionButton.this.getSizeDimension() / 2.0F;
      }

      public boolean isCompatPaddingEnabled() {
         return FloatingActionButton.this.mCompatPadding;
      }

      public void setBackgroundDrawable(Drawable var1) {
         FloatingActionButton.super.setBackgroundDrawable(var1);
      }

      public void setShadowPadding(int var1, int var2, int var3, int var4) {
         FloatingActionButton.this.mShadowPadding.set(var1, var2, var3, var4);
         FloatingActionButton var5 = FloatingActionButton.this;
         var5.setPadding(var5.mImagePadding + var1, FloatingActionButton.this.mImagePadding + var2, FloatingActionButton.this.mImagePadding + var3, FloatingActionButton.this.mImagePadding + var4);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface Size {
   }
}
