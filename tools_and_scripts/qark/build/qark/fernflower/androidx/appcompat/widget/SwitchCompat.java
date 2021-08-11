package androidx.appcompat.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ActionMode.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

public class SwitchCompat extends CompoundButton {
   private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int MONOSPACE = 3;
   private static final int SANS = 1;
   private static final int SERIF = 2;
   private static final int THUMB_ANIMATION_DURATION = 250;
   private static final Property THUMB_POS = new Property(Float.class, "thumbPos") {
      public Float get(SwitchCompat var1) {
         return var1.mThumbPosition;
      }

      public void set(SwitchCompat var1, Float var2) {
         var1.setThumbPosition(var2);
      }
   };
   private static final int TOUCH_MODE_DOWN = 1;
   private static final int TOUCH_MODE_DRAGGING = 2;
   private static final int TOUCH_MODE_IDLE = 0;
   private boolean mHasThumbTint;
   private boolean mHasThumbTintMode;
   private boolean mHasTrackTint;
   private boolean mHasTrackTintMode;
   private int mMinFlingVelocity;
   private Layout mOffLayout;
   private Layout mOnLayout;
   ObjectAnimator mPositionAnimator;
   private boolean mShowText;
   private boolean mSplitTrack;
   private int mSwitchBottom;
   private int mSwitchHeight;
   private int mSwitchLeft;
   private int mSwitchMinWidth;
   private int mSwitchPadding;
   private int mSwitchRight;
   private int mSwitchTop;
   private TransformationMethod mSwitchTransformationMethod;
   private int mSwitchWidth;
   private final Rect mTempRect;
   private ColorStateList mTextColors;
   private final AppCompatTextHelper mTextHelper;
   private CharSequence mTextOff;
   private CharSequence mTextOn;
   private final TextPaint mTextPaint;
   private Drawable mThumbDrawable;
   float mThumbPosition;
   private int mThumbTextPadding;
   private ColorStateList mThumbTintList;
   private Mode mThumbTintMode;
   private int mThumbWidth;
   private int mTouchMode;
   private int mTouchSlop;
   private float mTouchX;
   private float mTouchY;
   private Drawable mTrackDrawable;
   private ColorStateList mTrackTintList;
   private Mode mTrackTintMode;
   private VelocityTracker mVelocityTracker;

   public SwitchCompat(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SwitchCompat(Context var1, AttributeSet var2) {
      this(var1, var2, attr.switchStyle);
   }

   public SwitchCompat(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mThumbTintList = null;
      this.mThumbTintMode = null;
      this.mHasThumbTint = false;
      this.mHasThumbTintMode = false;
      this.mTrackTintList = null;
      this.mTrackTintMode = null;
      this.mHasTrackTint = false;
      this.mHasTrackTintMode = false;
      this.mVelocityTracker = VelocityTracker.obtain();
      this.mTempRect = new Rect();
      this.mTextPaint = new TextPaint(1);
      Resources var5 = this.getResources();
      this.mTextPaint.density = var5.getDisplayMetrics().density;
      TintTypedArray var8 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.SwitchCompat, var3, 0);
      Drawable var6 = var8.getDrawable(styleable.SwitchCompat_android_thumb);
      this.mThumbDrawable = var6;
      if (var6 != null) {
         var6.setCallback(this);
      }

      var6 = var8.getDrawable(styleable.SwitchCompat_track);
      this.mTrackDrawable = var6;
      if (var6 != null) {
         var6.setCallback(this);
      }

      this.mTextOn = var8.getText(styleable.SwitchCompat_android_textOn);
      this.mTextOff = var8.getText(styleable.SwitchCompat_android_textOff);
      this.mShowText = var8.getBoolean(styleable.SwitchCompat_showText, true);
      this.mThumbTextPadding = var8.getDimensionPixelSize(styleable.SwitchCompat_thumbTextPadding, 0);
      this.mSwitchMinWidth = var8.getDimensionPixelSize(styleable.SwitchCompat_switchMinWidth, 0);
      this.mSwitchPadding = var8.getDimensionPixelSize(styleable.SwitchCompat_switchPadding, 0);
      this.mSplitTrack = var8.getBoolean(styleable.SwitchCompat_splitTrack, false);
      ColorStateList var9 = var8.getColorStateList(styleable.SwitchCompat_thumbTint);
      if (var9 != null) {
         this.mThumbTintList = var9;
         this.mHasThumbTint = true;
      }

      Mode var10 = DrawableUtils.parseTintMode(var8.getInt(styleable.SwitchCompat_thumbTintMode, -1), (Mode)null);
      if (this.mThumbTintMode != var10) {
         this.mThumbTintMode = var10;
         this.mHasThumbTintMode = true;
      }

      if (this.mHasThumbTint || this.mHasThumbTintMode) {
         this.applyThumbTint();
      }

      var9 = var8.getColorStateList(styleable.SwitchCompat_trackTint);
      if (var9 != null) {
         this.mTrackTintList = var9;
         this.mHasTrackTint = true;
      }

      var10 = DrawableUtils.parseTintMode(var8.getInt(styleable.SwitchCompat_trackTintMode, -1), (Mode)null);
      if (this.mTrackTintMode != var10) {
         this.mTrackTintMode = var10;
         this.mHasTrackTintMode = true;
      }

      if (this.mHasTrackTint || this.mHasTrackTintMode) {
         this.applyTrackTint();
      }

      int var4 = var8.getResourceId(styleable.SwitchCompat_switchTextAppearance, 0);
      if (var4 != 0) {
         this.setSwitchTextAppearance(var1, var4);
      }

      AppCompatTextHelper var11 = new AppCompatTextHelper(this);
      this.mTextHelper = var11;
      var11.loadFromAttributes(var2, var3);
      var8.recycle();
      ViewConfiguration var7 = ViewConfiguration.get(var1);
      this.mTouchSlop = var7.getScaledTouchSlop();
      this.mMinFlingVelocity = var7.getScaledMinimumFlingVelocity();
      this.refreshDrawableState();
      this.setChecked(this.isChecked());
   }

   private void animateThumbToCheckedState(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      ObjectAnimator var3 = ObjectAnimator.ofFloat(this, THUMB_POS, new float[]{var2});
      this.mPositionAnimator = var3;
      var3.setDuration(250L);
      if (VERSION.SDK_INT >= 18) {
         this.mPositionAnimator.setAutoCancel(true);
      }

      this.mPositionAnimator.start();
   }

   private void applyThumbTint() {
      if (this.mThumbDrawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
         Drawable var1 = DrawableCompat.wrap(this.mThumbDrawable).mutate();
         this.mThumbDrawable = var1;
         if (this.mHasThumbTint) {
            DrawableCompat.setTintList(var1, this.mThumbTintList);
         }

         if (this.mHasThumbTintMode) {
            DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode);
         }

         if (this.mThumbDrawable.isStateful()) {
            this.mThumbDrawable.setState(this.getDrawableState());
         }
      }

   }

   private void applyTrackTint() {
      if (this.mTrackDrawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
         Drawable var1 = DrawableCompat.wrap(this.mTrackDrawable).mutate();
         this.mTrackDrawable = var1;
         if (this.mHasTrackTint) {
            DrawableCompat.setTintList(var1, this.mTrackTintList);
         }

         if (this.mHasTrackTintMode) {
            DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode);
         }

         if (this.mTrackDrawable.isStateful()) {
            this.mTrackDrawable.setState(this.getDrawableState());
         }
      }

   }

   private void cancelPositionAnimator() {
      ObjectAnimator var1 = this.mPositionAnimator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   private void cancelSuperTouch(MotionEvent var1) {
      var1 = MotionEvent.obtain(var1);
      var1.setAction(3);
      super.onTouchEvent(var1);
      var1.recycle();
   }

   private static float constrain(float var0, float var1, float var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   private boolean getTargetCheckedState() {
      return this.mThumbPosition > 0.5F;
   }

   private int getThumbOffset() {
      float var1;
      if (ViewUtils.isLayoutRtl(this)) {
         var1 = 1.0F - this.mThumbPosition;
      } else {
         var1 = this.mThumbPosition;
      }

      return (int)((float)this.getThumbScrollRange() * var1 + 0.5F);
   }

   private int getThumbScrollRange() {
      Drawable var1 = this.mTrackDrawable;
      if (var1 != null) {
         Rect var2 = this.mTempRect;
         var1.getPadding(var2);
         var1 = this.mThumbDrawable;
         Rect var3;
         if (var1 != null) {
            var3 = DrawableUtils.getOpticalBounds(var1);
         } else {
            var3 = DrawableUtils.INSETS_NONE;
         }

         return this.mSwitchWidth - this.mThumbWidth - var2.left - var2.right - var3.left - var3.right;
      } else {
         return 0;
      }
   }

   private boolean hitThumb(float var1, float var2) {
      Drawable var13 = this.mThumbDrawable;
      boolean var12 = false;
      if (var13 == null) {
         return false;
      } else {
         int var5 = this.getThumbOffset();
         this.mThumbDrawable.getPadding(this.mTempRect);
         int var3 = this.mSwitchTop;
         int var4 = this.mTouchSlop;
         var5 = this.mSwitchLeft + var5 - var4;
         int var6 = this.mThumbWidth;
         int var7 = this.mTempRect.left;
         int var8 = this.mTempRect.right;
         int var9 = this.mTouchSlop;
         int var10 = this.mSwitchBottom;
         boolean var11 = var12;
         if (var1 > (float)var5) {
            var11 = var12;
            if (var1 < (float)(var6 + var5 + var7 + var8 + var9)) {
               var11 = var12;
               if (var2 > (float)(var3 - var4)) {
                  var11 = var12;
                  if (var2 < (float)(var10 + var9)) {
                     var11 = true;
                  }
               }
            }
         }

         return var11;
      }
   }

   private Layout makeLayout(CharSequence var1) {
      TransformationMethod var3 = this.mSwitchTransformationMethod;
      if (var3 != null) {
         var1 = var3.getTransformation(var1, this);
      }

      TextPaint var4 = this.mTextPaint;
      int var2;
      if (var1 != null) {
         var2 = (int)Math.ceil((double)Layout.getDesiredWidth(var1, var4));
      } else {
         var2 = 0;
      }

      return new StaticLayout(var1, var4, var2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
   }

   private void setSwitchTypefaceByIndex(int var1, int var2) {
      Typeface var3 = null;
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3) {
               var3 = Typeface.MONOSPACE;
            }
         } else {
            var3 = Typeface.SERIF;
         }
      } else {
         var3 = Typeface.SANS_SERIF;
      }

      this.setSwitchTypeface(var3, var2);
   }

   private void stopDrag(MotionEvent var1) {
      this.mTouchMode = 0;
      int var3 = var1.getAction();
      boolean var4 = true;
      boolean var6;
      if (var3 == 1 && this.isEnabled()) {
         var6 = true;
      } else {
         var6 = false;
      }

      boolean var5 = this.isChecked();
      if (var6) {
         this.mVelocityTracker.computeCurrentVelocity(1000);
         float var2 = this.mVelocityTracker.getXVelocity();
         if (Math.abs(var2) > (float)this.mMinFlingVelocity) {
            label29: {
               if (ViewUtils.isLayoutRtl(this)) {
                  if (var2 < 0.0F) {
                     break label29;
                  }
               } else if (var2 > 0.0F) {
                  break label29;
               }

               var4 = false;
            }
         } else {
            var4 = this.getTargetCheckedState();
         }
      } else {
         var4 = var5;
      }

      if (var4 != var5) {
         this.playSoundEffect(0);
      }

      this.setChecked(var4);
      this.cancelSuperTouch(var1);
   }

   public void draw(Canvas var1) {
      Rect var15 = this.mTempRect;
      int var5 = this.mSwitchLeft;
      int var11 = this.mSwitchTop;
      int var3 = this.mSwitchRight;
      int var12 = this.mSwitchBottom;
      int var4 = this.getThumbOffset() + var5;
      Drawable var14 = this.mThumbDrawable;
      Rect var17;
      if (var14 != null) {
         var17 = DrawableUtils.getOpticalBounds(var14);
      } else {
         var17 = DrawableUtils.INSETS_NONE;
      }

      Drawable var16 = this.mTrackDrawable;
      int var2 = var4;
      if (var16 != null) {
         var16.getPadding(var15);
         int var13 = var4 + var15.left;
         int var7 = var5;
         int var8 = var11;
         int var9 = var3;
         int var10 = var12;
         if (var17 != null) {
            var2 = var5;
            if (var17.left > var15.left) {
               var2 = var5 + (var17.left - var15.left);
            }

            var5 = var11;
            if (var17.top > var15.top) {
               var5 = var11 + (var17.top - var15.top);
            }

            var4 = var3;
            if (var17.right > var15.right) {
               var4 = var3 - (var17.right - var15.right);
            }

            var7 = var2;
            var8 = var5;
            var9 = var4;
            var10 = var12;
            if (var17.bottom > var15.bottom) {
               var10 = var12 - (var17.bottom - var15.bottom);
               var9 = var4;
               var8 = var5;
               var7 = var2;
            }
         }

         this.mTrackDrawable.setBounds(var7, var8, var9, var10);
         var2 = var13;
      }

      var14 = this.mThumbDrawable;
      if (var14 != null) {
         var14.getPadding(var15);
         var3 = var2 - var15.left;
         var2 = this.mThumbWidth + var2 + var15.right;
         this.mThumbDrawable.setBounds(var3, var11, var2, var12);
         var14 = this.getBackground();
         if (var14 != null) {
            DrawableCompat.setHotspotBounds(var14, var3, var11, var2, var12);
         }
      }

      super.draw(var1);
   }

   public void drawableHotspotChanged(float var1, float var2) {
      if (VERSION.SDK_INT >= 21) {
         super.drawableHotspotChanged(var1, var2);
      }

      Drawable var3 = this.mThumbDrawable;
      if (var3 != null) {
         DrawableCompat.setHotspot(var3, var1, var2);
      }

      var3 = this.mTrackDrawable;
      if (var3 != null) {
         DrawableCompat.setHotspot(var3, var1, var2);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      int[] var3 = this.getDrawableState();
      boolean var2 = false;
      Drawable var4 = this.mThumbDrawable;
      boolean var1 = var2;
      if (var4 != null) {
         var1 = var2;
         if (var4.isStateful()) {
            var1 = false | var4.setState(var3);
         }
      }

      var4 = this.mTrackDrawable;
      var2 = var1;
      if (var4 != null) {
         var2 = var1;
         if (var4.isStateful()) {
            var2 = var1 | var4.setState(var3);
         }
      }

      if (var2) {
         this.invalidate();
      }

   }

   public int getCompoundPaddingLeft() {
      if (!ViewUtils.isLayoutRtl(this)) {
         return super.getCompoundPaddingLeft();
      } else {
         int var2 = super.getCompoundPaddingLeft() + this.mSwitchWidth;
         int var1 = var2;
         if (!TextUtils.isEmpty(this.getText())) {
            var1 = var2 + this.mSwitchPadding;
         }

         return var1;
      }
   }

   public int getCompoundPaddingRight() {
      if (ViewUtils.isLayoutRtl(this)) {
         return super.getCompoundPaddingRight();
      } else {
         int var2 = super.getCompoundPaddingRight() + this.mSwitchWidth;
         int var1 = var2;
         if (!TextUtils.isEmpty(this.getText())) {
            var1 = var2 + this.mSwitchPadding;
         }

         return var1;
      }
   }

   public boolean getShowText() {
      return this.mShowText;
   }

   public boolean getSplitTrack() {
      return this.mSplitTrack;
   }

   public int getSwitchMinWidth() {
      return this.mSwitchMinWidth;
   }

   public int getSwitchPadding() {
      return this.mSwitchPadding;
   }

   public CharSequence getTextOff() {
      return this.mTextOff;
   }

   public CharSequence getTextOn() {
      return this.mTextOn;
   }

   public Drawable getThumbDrawable() {
      return this.mThumbDrawable;
   }

   public int getThumbTextPadding() {
      return this.mThumbTextPadding;
   }

   public ColorStateList getThumbTintList() {
      return this.mThumbTintList;
   }

   public Mode getThumbTintMode() {
      return this.mThumbTintMode;
   }

   public Drawable getTrackDrawable() {
      return this.mTrackDrawable;
   }

   public ColorStateList getTrackTintList() {
      return this.mTrackTintList;
   }

   public Mode getTrackTintMode() {
      return this.mTrackTintMode;
   }

   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      Drawable var1 = this.mThumbDrawable;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

      var1 = this.mTrackDrawable;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

      ObjectAnimator var2 = this.mPositionAnimator;
      if (var2 != null && var2.isStarted()) {
         this.mPositionAnimator.end();
         this.mPositionAnimator = null;
      }

   }

   protected int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      if (this.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      Rect var9 = this.mTempRect;
      Drawable var11 = this.mTrackDrawable;
      if (var11 != null) {
         var11.getPadding(var9);
      } else {
         var9.setEmpty();
      }

      int var4 = this.mSwitchTop;
      int var5 = this.mSwitchBottom;
      int var6 = var9.top;
      int var7 = var9.bottom;
      Drawable var10 = this.mThumbDrawable;
      int var2;
      if (var11 != null) {
         if (this.mSplitTrack && var10 != null) {
            Rect var12 = DrawableUtils.getOpticalBounds(var10);
            var10.copyBounds(var9);
            var9.left += var12.left;
            var9.right -= var12.right;
            var2 = var1.save();
            var1.clipRect(var9, Op.DIFFERENCE);
            var11.draw(var1);
            var1.restoreToCount(var2);
         } else {
            var11.draw(var1);
         }
      }

      int var3 = var1.save();
      if (var10 != null) {
         var10.draw(var1);
      }

      Layout var13;
      if (this.getTargetCheckedState()) {
         var13 = this.mOnLayout;
      } else {
         var13 = this.mOffLayout;
      }

      if (var13 != null) {
         int[] var15 = this.getDrawableState();
         ColorStateList var16 = this.mTextColors;
         if (var16 != null) {
            this.mTextPaint.setColor(var16.getColorForState(var15, 0));
         }

         this.mTextPaint.drawableState = var15;
         if (var10 != null) {
            Rect var14 = var10.getBounds();
            var2 = var14.left + var14.right;
         } else {
            var2 = this.getWidth();
         }

         var2 /= 2;
         int var8 = var13.getWidth() / 2;
         var4 = (var6 + var4 + (var5 - var7)) / 2;
         var5 = var13.getHeight() / 2;
         var1.translate((float)(var2 - var8), (float)(var4 - var5));
         var13.draw(var1);
      }

      var1.restoreToCount(var3);
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      var1.setClassName("android.widget.Switch");
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.Switch");
      CharSequence var2;
      if (this.isChecked()) {
         var2 = this.mTextOn;
      } else {
         var2 = this.mTextOff;
      }

      if (!TextUtils.isEmpty(var2)) {
         CharSequence var3 = var1.getText();
         if (TextUtils.isEmpty(var3)) {
            var1.setText(var2);
            return;
         }

         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append(' ');
         var4.append(var2);
         var1.setText(var4);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      var2 = 0;
      var3 = 0;
      if (this.mThumbDrawable != null) {
         Rect var6 = this.mTempRect;
         Drawable var7 = this.mTrackDrawable;
         if (var7 != null) {
            var7.getPadding(var6);
         } else {
            var6.setEmpty();
         }

         Rect var8 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
         var2 = Math.max(0, var8.left - var6.left);
         var3 = Math.max(0, var8.right - var6.right);
      }

      if (ViewUtils.isLayoutRtl(this)) {
         var4 = this.getPaddingLeft() + var2;
         var5 = this.mSwitchWidth + var4 - var2 - var3;
      } else {
         var5 = this.getWidth() - this.getPaddingRight() - var3;
         var4 = var5 - this.mSwitchWidth + var2 + var3;
      }

      var2 = this.getGravity() & 112;
      if (var2 != 16) {
         if (var2 != 80) {
            var2 = this.getPaddingTop();
            var3 = this.mSwitchHeight + var2;
         } else {
            var3 = this.getHeight() - this.getPaddingBottom();
            var2 = var3 - this.mSwitchHeight;
         }
      } else {
         var2 = (this.getPaddingTop() + this.getHeight() - this.getPaddingBottom()) / 2;
         var3 = this.mSwitchHeight;
         var2 -= var3 / 2;
         var3 += var2;
      }

      this.mSwitchLeft = var4;
      this.mSwitchTop = var2;
      this.mSwitchBottom = var3;
      this.mSwitchRight = var5;
   }

   public void onMeasure(int var1, int var2) {
      if (this.mShowText) {
         if (this.mOnLayout == null) {
            this.mOnLayout = this.makeLayout(this.mTextOn);
         }

         if (this.mOffLayout == null) {
            this.mOffLayout = this.makeLayout(this.mTextOff);
         }
      }

      Rect var9 = this.mTempRect;
      Drawable var10 = this.mThumbDrawable;
      int var3;
      int var4;
      if (var10 != null) {
         var10.getPadding(var9);
         var4 = this.mThumbDrawable.getIntrinsicWidth() - var9.left - var9.right;
         var3 = this.mThumbDrawable.getIntrinsicHeight();
      } else {
         var4 = 0;
         var3 = 0;
      }

      int var5;
      if (this.mShowText) {
         var5 = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
      } else {
         var5 = 0;
      }

      this.mThumbWidth = Math.max(var5, var4);
      var10 = this.mTrackDrawable;
      if (var10 != null) {
         var10.getPadding(var9);
         var4 = this.mTrackDrawable.getIntrinsicHeight();
      } else {
         var9.setEmpty();
         var4 = 0;
      }

      int var8 = var9.left;
      int var7 = var9.right;
      Drawable var11 = this.mThumbDrawable;
      int var6 = var8;
      var5 = var7;
      if (var11 != null) {
         var9 = DrawableUtils.getOpticalBounds(var11);
         var6 = Math.max(var8, var9.left);
         var5 = Math.max(var7, var9.right);
      }

      var5 = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + var6 + var5);
      var3 = Math.max(var4, var3);
      this.mSwitchWidth = var5;
      this.mSwitchHeight = var3;
      super.onMeasure(var1, var2);
      if (this.getMeasuredHeight() < var3) {
         this.setMeasuredDimension(this.getMeasuredWidthAndState(), var3);
      }

   }

   public void onPopulateAccessibilityEvent(AccessibilityEvent var1) {
      super.onPopulateAccessibilityEvent(var1);
      CharSequence var2;
      if (this.isChecked()) {
         var2 = this.mTextOn;
      } else {
         var2 = this.mTextOff;
      }

      if (var2 != null) {
         var1.getText().add(var2);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.mVelocityTracker.addMovement(var1);
      int var5 = var1.getActionMasked();
      float var2;
      float var3;
      if (var5 != 0) {
         if (var5 != 1) {
            if (var5 == 2) {
               var5 = this.mTouchMode;
               if (var5 != 1) {
                  if (var5 == 2) {
                     float var4 = var1.getX();
                     var5 = this.getThumbScrollRange();
                     var2 = var4 - this.mTouchX;
                     if (var5 != 0) {
                        var2 /= (float)var5;
                     } else if (var2 > 0.0F) {
                        var2 = 1.0F;
                     } else {
                        var2 = -1.0F;
                     }

                     var3 = var2;
                     if (ViewUtils.isLayoutRtl(this)) {
                        var3 = -var2;
                     }

                     var2 = constrain(this.mThumbPosition + var3, 0.0F, 1.0F);
                     if (var2 != this.mThumbPosition) {
                        this.mTouchX = var4;
                        this.setThumbPosition(var2);
                     }

                     return true;
                  }
               } else {
                  var2 = var1.getX();
                  var3 = var1.getY();
                  if (Math.abs(var2 - this.mTouchX) > (float)this.mTouchSlop || Math.abs(var3 - this.mTouchY) > (float)this.mTouchSlop) {
                     this.mTouchMode = 2;
                     this.getParent().requestDisallowInterceptTouchEvent(true);
                     this.mTouchX = var2;
                     this.mTouchY = var3;
                     return true;
                  }
               }

               return super.onTouchEvent(var1);
            }

            if (var5 != 3) {
               return super.onTouchEvent(var1);
            }
         }

         if (this.mTouchMode == 2) {
            this.stopDrag(var1);
            super.onTouchEvent(var1);
            return true;
         }

         this.mTouchMode = 0;
         this.mVelocityTracker.clear();
      } else {
         var2 = var1.getX();
         var3 = var1.getY();
         if (this.isEnabled() && this.hitThumb(var2, var3)) {
            this.mTouchMode = 1;
            this.mTouchX = var2;
            this.mTouchY = var3;
         }
      }

      return super.onTouchEvent(var1);
   }

   public void setChecked(boolean var1) {
      super.setChecked(var1);
      var1 = this.isChecked();
      if (this.getWindowToken() != null && ViewCompat.isLaidOut(this)) {
         this.animateThumbToCheckedState(var1);
      } else {
         this.cancelPositionAnimator();
         float var2;
         if (var1) {
            var2 = 1.0F;
         } else {
            var2 = 0.0F;
         }

         this.setThumbPosition(var2);
      }
   }

   public void setCustomSelectionActionModeCallback(Callback var1) {
      super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, var1));
   }

   public void setShowText(boolean var1) {
      if (this.mShowText != var1) {
         this.mShowText = var1;
         this.requestLayout();
      }

   }

   public void setSplitTrack(boolean var1) {
      this.mSplitTrack = var1;
      this.invalidate();
   }

   public void setSwitchMinWidth(int var1) {
      this.mSwitchMinWidth = var1;
      this.requestLayout();
   }

   public void setSwitchPadding(int var1) {
      this.mSwitchPadding = var1;
      this.requestLayout();
   }

   public void setSwitchTextAppearance(Context var1, int var2) {
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.TextAppearance);
      ColorStateList var3 = var4.getColorStateList(styleable.TextAppearance_android_textColor);
      if (var3 != null) {
         this.mTextColors = var3;
      } else {
         this.mTextColors = this.getTextColors();
      }

      var2 = var4.getDimensionPixelSize(styleable.TextAppearance_android_textSize, 0);
      if (var2 != 0 && (float)var2 != this.mTextPaint.getTextSize()) {
         this.mTextPaint.setTextSize((float)var2);
         this.requestLayout();
      }

      this.setSwitchTypefaceByIndex(var4.getInt(styleable.TextAppearance_android_typeface, -1), var4.getInt(styleable.TextAppearance_android_textStyle, -1));
      if (var4.getBoolean(styleable.TextAppearance_textAllCaps, false)) {
         this.mSwitchTransformationMethod = new AllCapsTransformationMethod(this.getContext());
      } else {
         this.mSwitchTransformationMethod = null;
      }

      var4.recycle();
   }

   public void setSwitchTypeface(Typeface var1) {
      if (this.mTextPaint.getTypeface() != null && !this.mTextPaint.getTypeface().equals(var1) || this.mTextPaint.getTypeface() == null && var1 != null) {
         this.mTextPaint.setTypeface(var1);
         this.requestLayout();
         this.invalidate();
      }

   }

   public void setSwitchTypeface(Typeface var1, int var2) {
      float var3 = 0.0F;
      boolean var5 = false;
      if (var2 > 0) {
         if (var1 == null) {
            var1 = Typeface.defaultFromStyle(var2);
         } else {
            var1 = Typeface.create(var1, var2);
         }

         this.setSwitchTypeface(var1);
         int var4;
         if (var1 != null) {
            var4 = var1.getStyle();
         } else {
            var4 = 0;
         }

         var2 &= var4;
         TextPaint var6 = this.mTextPaint;
         if ((var2 & 1) != 0) {
            var5 = true;
         }

         var6.setFakeBoldText(var5);
         var6 = this.mTextPaint;
         if ((var2 & 2) != 0) {
            var3 = -0.25F;
         }

         var6.setTextSkewX(var3);
      } else {
         this.mTextPaint.setFakeBoldText(false);
         this.mTextPaint.setTextSkewX(0.0F);
         this.setSwitchTypeface(var1);
      }
   }

   public void setTextOff(CharSequence var1) {
      this.mTextOff = var1;
      this.requestLayout();
   }

   public void setTextOn(CharSequence var1) {
      this.mTextOn = var1;
      this.requestLayout();
   }

   public void setThumbDrawable(Drawable var1) {
      Drawable var2 = this.mThumbDrawable;
      if (var2 != null) {
         var2.setCallback((android.graphics.drawable.Drawable.Callback)null);
      }

      this.mThumbDrawable = var1;
      if (var1 != null) {
         var1.setCallback(this);
      }

      this.requestLayout();
   }

   void setThumbPosition(float var1) {
      this.mThumbPosition = var1;
      this.invalidate();
   }

   public void setThumbResource(int var1) {
      this.setThumbDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setThumbTextPadding(int var1) {
      this.mThumbTextPadding = var1;
      this.requestLayout();
   }

   public void setThumbTintList(ColorStateList var1) {
      this.mThumbTintList = var1;
      this.mHasThumbTint = true;
      this.applyThumbTint();
   }

   public void setThumbTintMode(Mode var1) {
      this.mThumbTintMode = var1;
      this.mHasThumbTintMode = true;
      this.applyThumbTint();
   }

   public void setTrackDrawable(Drawable var1) {
      Drawable var2 = this.mTrackDrawable;
      if (var2 != null) {
         var2.setCallback((android.graphics.drawable.Drawable.Callback)null);
      }

      this.mTrackDrawable = var1;
      if (var1 != null) {
         var1.setCallback(this);
      }

      this.requestLayout();
   }

   public void setTrackResource(int var1) {
      this.setTrackDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setTrackTintList(ColorStateList var1) {
      this.mTrackTintList = var1;
      this.mHasTrackTint = true;
      this.applyTrackTint();
   }

   public void setTrackTintMode(Mode var1) {
      this.mTrackTintMode = var1;
      this.mHasTrackTintMode = true;
      this.applyTrackTint();
   }

   public void toggle() {
      this.setChecked(this.isChecked() ^ true);
   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.mThumbDrawable || var1 == this.mTrackDrawable;
   }
}
