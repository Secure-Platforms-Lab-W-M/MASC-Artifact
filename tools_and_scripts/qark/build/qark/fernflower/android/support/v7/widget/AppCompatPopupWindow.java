package android.support.v7.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

class AppCompatPopupWindow extends PopupWindow {
   private static final boolean COMPAT_OVERLAP_ANCHOR;
   private static final String TAG = "AppCompatPopupWindow";
   private boolean mOverlapAnchor;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      COMPAT_OVERLAP_ANCHOR = var0;
   }

   public AppCompatPopupWindow(@NonNull Context var1, @Nullable AttributeSet var2, @AttrRes int var3) {
      super(var1, var2, var3);
      this.init(var1, var2, var3, 0);
   }

   public AppCompatPopupWindow(@NonNull Context var1, @Nullable AttributeSet var2, @AttrRes int var3, @StyleRes int var4) {
      super(var1, var2, var3, var4);
      this.init(var1, var2, var3, var4);
   }

   private void init(Context var1, AttributeSet var2, int var3, int var4) {
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.PopupWindow, var3, var4);
      if (var5.hasValue(R$styleable.PopupWindow_overlapAnchor)) {
         this.setSupportOverlapAnchor(var5.getBoolean(R$styleable.PopupWindow_overlapAnchor, false));
      }

      this.setBackgroundDrawable(var5.getDrawable(R$styleable.PopupWindow_android_popupBackground));
      var3 = VERSION.SDK_INT;
      if (var4 != 0 && var3 < 11 && var5.hasValue(R$styleable.PopupWindow_android_popupAnimationStyle)) {
         this.setAnimationStyle(var5.getResourceId(R$styleable.PopupWindow_android_popupAnimationStyle, -1));
      }

      var5.recycle();
      if (VERSION.SDK_INT < 14) {
         wrapOnScrollChangedListener(this);
      }

   }

   private static void wrapOnScrollChangedListener(final PopupWindow var0) {
      try {
         final Field var1 = PopupWindow.class.getDeclaredField("mAnchor");
         var1.setAccessible(true);
         Field var2 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
         var2.setAccessible(true);
         var2.set(var0, new OnScrollChangedListener((OnScrollChangedListener)var2.get(var0)) {
            // $FF: synthetic field
            final OnScrollChangedListener val$originalListener;

            {
               this.val$originalListener = var3;
            }

            public void onScrollChanged() {
               // $FF: Couldn't be decompiled
            }
         });
      } catch (Exception var3) {
         Log.d("AppCompatPopupWindow", "Exception while installing workaround OnScrollChangedListener", var3);
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean getSupportOverlapAnchor() {
      return COMPAT_OVERLAP_ANCHOR ? this.mOverlapAnchor : PopupWindowCompat.getOverlapAnchor(this);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportOverlapAnchor(boolean var1) {
      if (COMPAT_OVERLAP_ANCHOR) {
         this.mOverlapAnchor = var1;
      } else {
         PopupWindowCompat.setOverlapAnchor(this, var1);
      }
   }

   public void showAsDropDown(View var1, int var2, int var3) {
      int var4 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var4 = var3;
         if (this.mOverlapAnchor) {
            var4 = var3 - var1.getHeight();
         }
      }

      super.showAsDropDown(var1, var2, var4);
   }

   public void showAsDropDown(View var1, int var2, int var3, int var4) {
      int var5 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var5 = var3;
         if (this.mOverlapAnchor) {
            var5 = var3 - var1.getHeight();
         }
      }

      super.showAsDropDown(var1, var2, var5, var4);
   }

   public void update(View var1, int var2, int var3, int var4, int var5) {
      int var6 = var3;
      if (COMPAT_OVERLAP_ANCHOR) {
         var6 = var3;
         if (this.mOverlapAnchor) {
            var6 = var3 - var1.getHeight();
         }
      }

      super.update(var1, var2, var6, var4, var5);
   }
}
