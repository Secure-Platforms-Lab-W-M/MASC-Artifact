package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TintTypedArray {
   private final Context mContext;
   private TypedValue mTypedValue;
   private final TypedArray mWrapped;

   private TintTypedArray(Context var1, TypedArray var2) {
      this.mContext = var1;
      this.mWrapped = var2;
   }

   public static TintTypedArray obtainStyledAttributes(Context var0, int var1, int[] var2) {
      return new TintTypedArray(var0, var0.obtainStyledAttributes(var1, var2));
   }

   public static TintTypedArray obtainStyledAttributes(Context var0, AttributeSet var1, int[] var2) {
      return new TintTypedArray(var0, var0.obtainStyledAttributes(var1, var2));
   }

   public static TintTypedArray obtainStyledAttributes(Context var0, AttributeSet var1, int[] var2, int var3, int var4) {
      return new TintTypedArray(var0, var0.obtainStyledAttributes(var1, var2, var3, var4));
   }

   public boolean getBoolean(int var1, boolean var2) {
      return this.mWrapped.getBoolean(var1, var2);
   }

   @RequiresApi(21)
   public int getChangingConfigurations() {
      return this.mWrapped.getChangingConfigurations();
   }

   public int getColor(int var1, int var2) {
      return this.mWrapped.getColor(var1, var2);
   }

   public ColorStateList getColorStateList(int var1) {
      if (this.mWrapped.hasValue(var1)) {
         int var2 = this.mWrapped.getResourceId(var1, 0);
         if (var2 != 0) {
            ColorStateList var3 = AppCompatResources.getColorStateList(this.mContext, var2);
            if (var3 != null) {
               return var3;
            }
         }
      }

      return this.mWrapped.getColorStateList(var1);
   }

   public float getDimension(int var1, float var2) {
      return this.mWrapped.getDimension(var1, var2);
   }

   public int getDimensionPixelOffset(int var1, int var2) {
      return this.mWrapped.getDimensionPixelOffset(var1, var2);
   }

   public int getDimensionPixelSize(int var1, int var2) {
      return this.mWrapped.getDimensionPixelSize(var1, var2);
   }

   public Drawable getDrawable(int var1) {
      if (this.mWrapped.hasValue(var1)) {
         int var2 = this.mWrapped.getResourceId(var1, 0);
         if (var2 != 0) {
            return AppCompatResources.getDrawable(this.mContext, var2);
         }
      }

      return this.mWrapped.getDrawable(var1);
   }

   public Drawable getDrawableIfKnown(int var1) {
      if (this.mWrapped.hasValue(var1)) {
         var1 = this.mWrapped.getResourceId(var1, 0);
         if (var1 != 0) {
            return AppCompatDrawableManager.get().getDrawable(this.mContext, var1, true);
         }
      }

      return null;
   }

   public float getFloat(int var1, float var2) {
      return this.mWrapped.getFloat(var1, var2);
   }

   @Nullable
   public Typeface getFont(@StyleableRes int var1, int var2, @NonNull TextView var3) {
      var1 = this.mWrapped.getResourceId(var1, 0);
      if (var1 == 0) {
         return null;
      } else {
         if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
         }

         return ResourcesCompat.getFont(this.mContext, var1, this.mTypedValue, var2, var3);
      }
   }

   public float getFraction(int var1, int var2, int var3, float var4) {
      return this.mWrapped.getFraction(var1, var2, var3, var4);
   }

   public int getIndex(int var1) {
      return this.mWrapped.getIndex(var1);
   }

   public int getIndexCount() {
      return this.mWrapped.getIndexCount();
   }

   public int getInt(int var1, int var2) {
      return this.mWrapped.getInt(var1, var2);
   }

   public int getInteger(int var1, int var2) {
      return this.mWrapped.getInteger(var1, var2);
   }

   public int getLayoutDimension(int var1, int var2) {
      return this.mWrapped.getLayoutDimension(var1, var2);
   }

   public int getLayoutDimension(int var1, String var2) {
      return this.mWrapped.getLayoutDimension(var1, var2);
   }

   public String getNonResourceString(int var1) {
      return this.mWrapped.getNonResourceString(var1);
   }

   public String getPositionDescription() {
      return this.mWrapped.getPositionDescription();
   }

   public int getResourceId(int var1, int var2) {
      return this.mWrapped.getResourceId(var1, var2);
   }

   public Resources getResources() {
      return this.mWrapped.getResources();
   }

   public String getString(int var1) {
      return this.mWrapped.getString(var1);
   }

   public CharSequence getText(int var1) {
      return this.mWrapped.getText(var1);
   }

   public CharSequence[] getTextArray(int var1) {
      return this.mWrapped.getTextArray(var1);
   }

   public int getType(int var1) {
      if (VERSION.SDK_INT >= 21) {
         return this.mWrapped.getType(var1);
      } else {
         if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
         }

         this.mWrapped.getValue(var1, this.mTypedValue);
         return this.mTypedValue.type;
      }
   }

   public boolean getValue(int var1, TypedValue var2) {
      return this.mWrapped.getValue(var1, var2);
   }

   public boolean hasValue(int var1) {
      return this.mWrapped.hasValue(var1);
   }

   public int length() {
      return this.mWrapped.length();
   }

   public TypedValue peekValue(int var1) {
      return this.mWrapped.peekValue(var1);
   }

   public void recycle() {
      this.mWrapped.recycle();
   }
}
