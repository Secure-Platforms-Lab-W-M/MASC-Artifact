package android.support.v4.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.Arrays;

public class ShortcutInfoCompat {
   private ComponentName mActivity;
   private Context mContext;
   private CharSequence mDisabledMessage;
   private IconCompat mIcon;
   private String mId;
   private Intent[] mIntents;
   private CharSequence mLabel;
   private CharSequence mLongLabel;

   private ShortcutInfoCompat() {
   }

   // $FF: synthetic method
   ShortcutInfoCompat(Object var1) {
      this();
   }

   Intent addToIntent(Intent var1) {
      Intent[] var2 = this.mIntents;
      var1.putExtra("android.intent.extra.shortcut.INTENT", var2[var2.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
      IconCompat var3 = this.mIcon;
      if (var3 != null) {
         var3.addToShortcutIntent(var1);
      }

      return var1;
   }

   @Nullable
   public ComponentName getActivity() {
      return this.mActivity;
   }

   @Nullable
   public CharSequence getDisabledMessage() {
      return this.mDisabledMessage;
   }

   @NonNull
   public String getId() {
      return this.mId;
   }

   @NonNull
   public Intent getIntent() {
      Intent[] var1 = this.mIntents;
      return var1[var1.length - 1];
   }

   @NonNull
   public Intent[] getIntents() {
      Intent[] var1 = this.mIntents;
      return (Intent[])Arrays.copyOf(var1, var1.length);
   }

   @Nullable
   public CharSequence getLongLabel() {
      return this.mLongLabel;
   }

   @NonNull
   public CharSequence getShortLabel() {
      return this.mLabel;
   }

   @RequiresApi(26)
   ShortcutInfo toShortcutInfo() {
      android.content.pm.ShortcutInfo.Builder var1 = (new android.content.pm.ShortcutInfo.Builder(this.mContext, this.mId)).setShortLabel(this.mLabel).setIntents(this.mIntents);
      IconCompat var2 = this.mIcon;
      if (var2 != null) {
         var1.setIcon(var2.toIcon());
      }

      if (!TextUtils.isEmpty(this.mLongLabel)) {
         var1.setLongLabel(this.mLongLabel);
      }

      if (!TextUtils.isEmpty(this.mDisabledMessage)) {
         var1.setDisabledMessage(this.mDisabledMessage);
      }

      ComponentName var3 = this.mActivity;
      if (var3 != null) {
         var1.setActivity(var3);
      }

      return var1.build();
   }

   public static class Builder {
      private final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();

      public Builder(@NonNull Context var1, @NonNull String var2) {
         this.mInfo.mContext = var1;
         this.mInfo.mId = var2;
      }

      @NonNull
      public ShortcutInfoCompat build() {
         if (!TextUtils.isEmpty(this.mInfo.mLabel)) {
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
               return this.mInfo;
            } else {
               throw new IllegalArgumentException("Shortcut much have an intent");
            }
         } else {
            throw new IllegalArgumentException("Shortcut much have a non-empty label");
         }
      }

      @NonNull
      public ShortcutInfoCompat.Builder setActivity(@NonNull ComponentName var1) {
         this.mInfo.mActivity = var1;
         return this;
      }

      @NonNull
      public ShortcutInfoCompat.Builder setDisabledMessage(@NonNull CharSequence var1) {
         this.mInfo.mDisabledMessage = var1;
         return this;
      }

      @NonNull
      public ShortcutInfoCompat.Builder setIcon(@DrawableRes int var1) {
         return this.setIcon(IconCompat.createWithResource(this.mInfo.mContext, var1));
      }

      @NonNull
      public ShortcutInfoCompat.Builder setIcon(@NonNull Bitmap var1) {
         return this.setIcon(IconCompat.createWithBitmap(var1));
      }

      @NonNull
      public ShortcutInfoCompat.Builder setIcon(IconCompat var1) {
         this.mInfo.mIcon = var1;
         return this;
      }

      @NonNull
      public ShortcutInfoCompat.Builder setIntent(@NonNull Intent var1) {
         return this.setIntents(new Intent[]{var1});
      }

      @NonNull
      public ShortcutInfoCompat.Builder setIntents(@NonNull Intent[] var1) {
         this.mInfo.mIntents = var1;
         return this;
      }

      @NonNull
      public ShortcutInfoCompat.Builder setLongLabel(@NonNull CharSequence var1) {
         this.mInfo.mLongLabel = var1;
         return this;
      }

      @NonNull
      public ShortcutInfoCompat.Builder setShortLabel(@NonNull CharSequence var1) {
         this.mInfo.mLabel = var1;
         return this;
      }
   }
}
