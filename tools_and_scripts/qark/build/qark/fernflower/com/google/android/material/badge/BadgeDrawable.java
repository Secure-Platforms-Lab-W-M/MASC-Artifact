package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.plurals;
import com.google.android.material.R.string;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class BadgeDrawable extends Drawable implements TextDrawableHelper.TextDrawableDelegate {
   private static final int BADGE_NUMBER_NONE = -1;
   public static final int BOTTOM_END = 8388693;
   public static final int BOTTOM_START = 8388691;
   static final String DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX = "+";
   private static final int DEFAULT_MAX_BADGE_CHARACTER_COUNT = 4;
   private static final int DEFAULT_STYLE;
   private static final int DEFAULT_THEME_ATTR;
   private static final int MAX_CIRCULAR_BADGE_NUMBER_COUNT = 9;
   public static final int TOP_END = 8388661;
   public static final int TOP_START = 8388659;
   private WeakReference anchorViewRef;
   private final Rect badgeBounds;
   private float badgeCenterX;
   private float badgeCenterY;
   private final float badgeRadius;
   private final float badgeWidePadding;
   private final float badgeWithTextRadius;
   private final WeakReference contextRef;
   private float cornerRadius;
   private WeakReference customBadgeParentRef;
   private float halfBadgeHeight;
   private float halfBadgeWidth;
   private int maxBadgeNumber;
   private final BadgeDrawable.SavedState savedState;
   private final MaterialShapeDrawable shapeDrawable;
   private final TextDrawableHelper textDrawableHelper;

   static {
      DEFAULT_STYLE = style.Widget_MaterialComponents_Badge;
      DEFAULT_THEME_ATTR = attr.badgeStyle;
   }

   private BadgeDrawable(Context var1) {
      this.contextRef = new WeakReference(var1);
      ThemeEnforcement.checkMaterialTheme(var1);
      Resources var2 = var1.getResources();
      this.badgeBounds = new Rect();
      this.shapeDrawable = new MaterialShapeDrawable();
      this.badgeRadius = (float)var2.getDimensionPixelSize(dimen.mtrl_badge_radius);
      this.badgeWidePadding = (float)var2.getDimensionPixelSize(dimen.mtrl_badge_long_text_horizontal_padding);
      this.badgeWithTextRadius = (float)var2.getDimensionPixelSize(dimen.mtrl_badge_with_text_radius);
      TextDrawableHelper var3 = new TextDrawableHelper(this);
      this.textDrawableHelper = var3;
      var3.getTextPaint().setTextAlign(Align.CENTER);
      this.savedState = new BadgeDrawable.SavedState(var1);
      this.setTextAppearanceResource(style.TextAppearance_MaterialComponents_Badge);
   }

   private void calculateCenterAndBounds(Context var1, Rect var2, View var3) {
      int var5 = this.savedState.badgeGravity;
      if (var5 != 8388691 && var5 != 8388693) {
         this.badgeCenterY = (float)var2.top;
      } else {
         this.badgeCenterY = (float)var2.bottom;
      }

      float var4;
      if (this.getNumber() <= 9) {
         if (!this.hasNumber()) {
            var4 = this.badgeRadius;
         } else {
            var4 = this.badgeWithTextRadius;
         }

         this.cornerRadius = var4;
         this.halfBadgeHeight = var4;
         this.halfBadgeWidth = var4;
      } else {
         var4 = this.badgeWithTextRadius;
         this.cornerRadius = var4;
         this.halfBadgeHeight = var4;
         String var7 = this.getBadgeText();
         this.halfBadgeWidth = this.textDrawableHelper.getTextWidth(var7) / 2.0F + this.badgeWidePadding;
      }

      Resources var8 = var1.getResources();
      if (this.hasNumber()) {
         var5 = dimen.mtrl_badge_text_horizontal_edge_offset;
      } else {
         var5 = dimen.mtrl_badge_horizontal_edge_offset;
      }

      var5 = var8.getDimensionPixelSize(var5);
      int var6 = this.savedState.badgeGravity;
      if (var6 != 8388659 && var6 != 8388691) {
         if (ViewCompat.getLayoutDirection(var3) == 0) {
            var4 = (float)var2.right + this.halfBadgeWidth - (float)var5;
         } else {
            var4 = (float)var2.left - this.halfBadgeWidth + (float)var5;
         }

         this.badgeCenterX = var4;
      } else {
         if (ViewCompat.getLayoutDirection(var3) == 0) {
            var4 = (float)var2.left - this.halfBadgeWidth + (float)var5;
         } else {
            var4 = (float)var2.right + this.halfBadgeWidth - (float)var5;
         }

         this.badgeCenterX = var4;
      }
   }

   public static BadgeDrawable create(Context var0) {
      return createFromAttributes(var0, (AttributeSet)null, DEFAULT_THEME_ATTR, DEFAULT_STYLE);
   }

   private static BadgeDrawable createFromAttributes(Context var0, AttributeSet var1, int var2, int var3) {
      BadgeDrawable var4 = new BadgeDrawable(var0);
      var4.loadDefaultStateFromAttributes(var0, var1, var2, var3);
      return var4;
   }

   public static BadgeDrawable createFromResource(Context var0, int var1) {
      AttributeSet var3 = DrawableUtils.parseDrawableXml(var0, var1, "badge");
      int var2 = var3.getStyleAttribute();
      var1 = var2;
      if (var2 == 0) {
         var1 = DEFAULT_STYLE;
      }

      return createFromAttributes(var0, var3, DEFAULT_THEME_ATTR, var1);
   }

   static BadgeDrawable createFromSavedState(Context var0, BadgeDrawable.SavedState var1) {
      BadgeDrawable var2 = new BadgeDrawable(var0);
      var2.restoreFromSavedState(var1);
      return var2;
   }

   private void drawText(Canvas var1) {
      Rect var2 = new Rect();
      String var3 = this.getBadgeText();
      this.textDrawableHelper.getTextPaint().getTextBounds(var3, 0, var3.length(), var2);
      var1.drawText(var3, this.badgeCenterX, this.badgeCenterY + (float)(var2.height() / 2), this.textDrawableHelper.getTextPaint());
   }

   private String getBadgeText() {
      if (this.getNumber() <= this.maxBadgeNumber) {
         return Integer.toString(this.getNumber());
      } else {
         Context var1 = (Context)this.contextRef.get();
         return var1 == null ? "" : var1.getString(string.mtrl_exceed_max_badge_number_suffix, new Object[]{this.maxBadgeNumber, "+"});
      }
   }

   private void loadDefaultStateFromAttributes(Context var1, AttributeSet var2, int var3, int var4) {
      TypedArray var5 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.Badge, var3, var4);
      this.setMaxCharacterCount(var5.getInt(styleable.Badge_maxCharacterCount, 4));
      if (var5.hasValue(styleable.Badge_number)) {
         this.setNumber(var5.getInt(styleable.Badge_number, 0));
      }

      this.setBackgroundColor(readColorFromAttributes(var1, var5, styleable.Badge_backgroundColor));
      if (var5.hasValue(styleable.Badge_badgeTextColor)) {
         this.setBadgeTextColor(readColorFromAttributes(var1, var5, styleable.Badge_badgeTextColor));
      }

      this.setBadgeGravity(var5.getInt(styleable.Badge_badgeGravity, 8388661));
      var5.recycle();
   }

   private static int readColorFromAttributes(Context var0, TypedArray var1, int var2) {
      return MaterialResources.getColorStateList(var0, var1, var2).getDefaultColor();
   }

   private void restoreFromSavedState(BadgeDrawable.SavedState var1) {
      this.setMaxCharacterCount(var1.maxCharacterCount);
      if (var1.number != -1) {
         this.setNumber(var1.number);
      }

      this.setBackgroundColor(var1.backgroundColor);
      this.setBadgeTextColor(var1.badgeTextColor);
      this.setBadgeGravity(var1.badgeGravity);
   }

   private void setTextAppearance(TextAppearance var1) {
      if (this.textDrawableHelper.getTextAppearance() != var1) {
         Context var2 = (Context)this.contextRef.get();
         if (var2 != null) {
            this.textDrawableHelper.setTextAppearance(var1, var2);
            this.updateCenterAndBounds();
         }
      }
   }

   private void setTextAppearanceResource(int var1) {
      Context var2 = (Context)this.contextRef.get();
      if (var2 != null) {
         this.setTextAppearance(new TextAppearance(var2, var1));
      }
   }

   private void updateCenterAndBounds() {
      Context var3 = (Context)this.contextRef.get();
      WeakReference var2 = this.anchorViewRef;
      ViewGroup var1 = null;
      View var7;
      if (var2 != null) {
         var7 = (View)var2.get();
      } else {
         var7 = null;
      }

      if (var3 != null) {
         if (var7 != null) {
            Rect var4 = new Rect();
            var4.set(this.badgeBounds);
            Rect var5 = new Rect();
            var7.getDrawingRect(var5);
            WeakReference var6 = this.customBadgeParentRef;
            if (var6 != null) {
               var1 = (ViewGroup)var6.get();
            }

            if (var1 != null || BadgeUtils.USE_COMPAT_PARENT) {
               if (var1 == null) {
                  var1 = (ViewGroup)var7.getParent();
               }

               var1.offsetDescendantRectToMyCoords(var7, var5);
            }

            this.calculateCenterAndBounds(var3, var5, var7);
            BadgeUtils.updateBadgeBounds(this.badgeBounds, this.badgeCenterX, this.badgeCenterY, this.halfBadgeWidth, this.halfBadgeHeight);
            this.shapeDrawable.setCornerSize(this.cornerRadius);
            if (!var4.equals(this.badgeBounds)) {
               this.shapeDrawable.setBounds(this.badgeBounds);
            }

         }
      }
   }

   private void updateMaxBadgeNumber() {
      this.maxBadgeNumber = (int)Math.pow(10.0D, (double)this.getMaxCharacterCount() - 1.0D) - 1;
   }

   public void clearNumber() {
      this.savedState.number = -1;
      this.invalidateSelf();
   }

   public void draw(Canvas var1) {
      if (!this.getBounds().isEmpty() && this.getAlpha() != 0) {
         if (this.isVisible()) {
            this.shapeDrawable.draw(var1);
            if (this.hasNumber()) {
               this.drawText(var1);
            }

         }
      }
   }

   public int getAlpha() {
      return this.savedState.alpha;
   }

   public int getBackgroundColor() {
      return this.shapeDrawable.getFillColor().getDefaultColor();
   }

   public int getBadgeGravity() {
      return this.savedState.badgeGravity;
   }

   public int getBadgeTextColor() {
      return this.textDrawableHelper.getTextPaint().getColor();
   }

   public CharSequence getContentDescription() {
      if (!this.isVisible()) {
         return null;
      } else if (this.hasNumber()) {
         if (this.savedState.contentDescriptionQuantityStrings > 0) {
            Context var1 = (Context)this.contextRef.get();
            return var1 == null ? null : var1.getResources().getQuantityString(this.savedState.contentDescriptionQuantityStrings, this.getNumber(), new Object[]{this.getNumber()});
         } else {
            return null;
         }
      } else {
         return this.savedState.contentDescriptionNumberless;
      }
   }

   public int getIntrinsicHeight() {
      return this.badgeBounds.height();
   }

   public int getIntrinsicWidth() {
      return this.badgeBounds.width();
   }

   public int getMaxCharacterCount() {
      return this.savedState.maxCharacterCount;
   }

   public int getNumber() {
      return !this.hasNumber() ? 0 : this.savedState.number;
   }

   public int getOpacity() {
      return -3;
   }

   public BadgeDrawable.SavedState getSavedState() {
      return this.savedState;
   }

   public boolean hasNumber() {
      return this.savedState.number != -1;
   }

   public boolean isStateful() {
      return false;
   }

   public boolean onStateChange(int[] var1) {
      return super.onStateChange(var1);
   }

   public void onTextSizeChange() {
      this.invalidateSelf();
   }

   public void setAlpha(int var1) {
      this.savedState.alpha = var1;
      this.textDrawableHelper.getTextPaint().setAlpha(var1);
      this.invalidateSelf();
   }

   public void setBackgroundColor(int var1) {
      this.savedState.backgroundColor = var1;
      ColorStateList var2 = ColorStateList.valueOf(var1);
      if (this.shapeDrawable.getFillColor() != var2) {
         this.shapeDrawable.setFillColor(var2);
         this.invalidateSelf();
      }

   }

   public void setBadgeGravity(int var1) {
      if (this.savedState.badgeGravity != var1) {
         this.savedState.badgeGravity = var1;
         WeakReference var2 = this.anchorViewRef;
         if (var2 != null && var2.get() != null) {
            View var3 = (View)this.anchorViewRef.get();
            var2 = this.customBadgeParentRef;
            ViewGroup var4;
            if (var2 != null) {
               var4 = (ViewGroup)var2.get();
            } else {
               var4 = null;
            }

            this.updateBadgeCoordinates(var3, var4);
         }
      }

   }

   public void setBadgeTextColor(int var1) {
      this.savedState.badgeTextColor = var1;
      if (this.textDrawableHelper.getTextPaint().getColor() != var1) {
         this.textDrawableHelper.getTextPaint().setColor(var1);
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setContentDescriptionNumberless(CharSequence var1) {
      this.savedState.contentDescriptionNumberless = var1;
   }

   public void setContentDescriptionQuantityStringsResource(int var1) {
      this.savedState.contentDescriptionQuantityStrings = var1;
   }

   public void setMaxCharacterCount(int var1) {
      if (this.savedState.maxCharacterCount != var1) {
         this.savedState.maxCharacterCount = var1;
         this.updateMaxBadgeNumber();
         this.textDrawableHelper.setTextWidthDirty(true);
         this.updateCenterAndBounds();
         this.invalidateSelf();
      }

   }

   public void setNumber(int var1) {
      var1 = Math.max(0, var1);
      if (this.savedState.number != var1) {
         this.savedState.number = var1;
         this.textDrawableHelper.setTextWidthDirty(true);
         this.updateCenterAndBounds();
         this.invalidateSelf();
      }

   }

   public void setVisible(boolean var1) {
      this.setVisible(var1, false);
   }

   public void updateBadgeCoordinates(View var1, ViewGroup var2) {
      this.anchorViewRef = new WeakReference(var1);
      this.customBadgeParentRef = new WeakReference(var2);
      this.updateCenterAndBounds();
      this.invalidateSelf();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface BadgeGravity {
   }

   public static final class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public BadgeDrawable.SavedState createFromParcel(Parcel var1) {
            return new BadgeDrawable.SavedState(var1);
         }

         public BadgeDrawable.SavedState[] newArray(int var1) {
            return new BadgeDrawable.SavedState[var1];
         }
      };
      private int alpha = 255;
      private int backgroundColor;
      private int badgeGravity;
      private int badgeTextColor;
      private CharSequence contentDescriptionNumberless;
      private int contentDescriptionQuantityStrings;
      private int maxCharacterCount;
      private int number = -1;

      public SavedState(Context var1) {
         this.badgeTextColor = (new TextAppearance(var1, style.TextAppearance_MaterialComponents_Badge)).textColor.getDefaultColor();
         this.contentDescriptionNumberless = var1.getString(string.mtrl_badge_numberless_content_description);
         this.contentDescriptionQuantityStrings = plurals.mtrl_badge_content_description;
      }

      protected SavedState(Parcel var1) {
         this.backgroundColor = var1.readInt();
         this.badgeTextColor = var1.readInt();
         this.alpha = var1.readInt();
         this.number = var1.readInt();
         this.maxCharacterCount = var1.readInt();
         this.contentDescriptionNumberless = var1.readString();
         this.contentDescriptionQuantityStrings = var1.readInt();
         this.badgeGravity = var1.readInt();
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.backgroundColor);
         var1.writeInt(this.badgeTextColor);
         var1.writeInt(this.alpha);
         var1.writeInt(this.number);
         var1.writeInt(this.maxCharacterCount);
         var1.writeString(this.contentDescriptionNumberless.toString());
         var1.writeInt(this.contentDescriptionQuantityStrings);
         var1.writeInt(this.badgeGravity);
      }
   }
}
