package androidx.core.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ShortcutInfoCompat {
   private static final String EXTRA_LONG_LIVED = "extraLongLived";
   private static final String EXTRA_PERSON_ = "extraPerson_";
   private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
   ComponentName mActivity;
   Set mCategories;
   Context mContext;
   CharSequence mDisabledMessage;
   IconCompat mIcon;
   String mId;
   Intent[] mIntents;
   boolean mIsAlwaysBadged;
   boolean mIsLongLived;
   CharSequence mLabel;
   CharSequence mLongLabel;
   Person[] mPersons;

   ShortcutInfoCompat() {
   }

   private PersistableBundle buildExtrasBundle() {
      PersistableBundle var2 = new PersistableBundle();
      Person[] var3 = this.mPersons;
      if (var3 != null && var3.length > 0) {
         var2.putInt("extraPersonCount", var3.length);

         for(int var1 = 0; var1 < this.mPersons.length; ++var1) {
            StringBuilder var4 = new StringBuilder();
            var4.append("extraPerson_");
            var4.append(var1 + 1);
            var2.putPersistableBundle(var4.toString(), this.mPersons[var1].toPersistableBundle());
         }
      }

      var2.putBoolean("extraLongLived", this.mIsLongLived);
      return var2;
   }

   static boolean getLongLivedFromExtra(PersistableBundle var0) {
      return var0 != null && var0.containsKey("extraLongLived") ? var0.getBoolean("extraLongLived") : false;
   }

   static Person[] getPersonsFromExtra(PersistableBundle var0) {
      if (var0 != null && var0.containsKey("extraPersonCount")) {
         int var2 = var0.getInt("extraPersonCount");
         Person[] var3 = new Person[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            StringBuilder var4 = new StringBuilder();
            var4.append("extraPerson_");
            var4.append(var1 + 1);
            var3[var1] = Person.fromPersistableBundle(var0.getPersistableBundle(var4.toString()));
         }

         return var3;
      } else {
         return null;
      }
   }

   Intent addToIntent(Intent var1) {
      Intent[] var2 = this.mIntents;
      var1.putExtra("android.intent.extra.shortcut.INTENT", var2[var2.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
      if (this.mIcon != null) {
         Drawable var3 = null;
         Object var4 = null;
         if (this.mIsAlwaysBadged) {
            PackageManager var5 = this.mContext.getPackageManager();
            ComponentName var8 = this.mActivity;
            Drawable var7 = (Drawable)var4;
            if (var8 != null) {
               try {
                  var7 = var5.getActivityIcon(var8);
               } catch (NameNotFoundException var6) {
                  var7 = (Drawable)var4;
               }
            }

            var3 = var7;
            if (var7 == null) {
               var3 = this.mContext.getApplicationInfo().loadIcon(var5);
            }
         }

         this.mIcon.addToShortcutIntent(var1, var3, this.mContext);
      }

      return var1;
   }

   public ComponentName getActivity() {
      return this.mActivity;
   }

   public Set getCategories() {
      return this.mCategories;
   }

   public CharSequence getDisabledMessage() {
      return this.mDisabledMessage;
   }

   public IconCompat getIcon() {
      return this.mIcon;
   }

   public String getId() {
      return this.mId;
   }

   public Intent getIntent() {
      Intent[] var1 = this.mIntents;
      return var1[var1.length - 1];
   }

   public Intent[] getIntents() {
      Intent[] var1 = this.mIntents;
      return (Intent[])Arrays.copyOf(var1, var1.length);
   }

   public CharSequence getLongLabel() {
      return this.mLongLabel;
   }

   public CharSequence getShortLabel() {
      return this.mLabel;
   }

   public ShortcutInfo toShortcutInfo() {
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

      Set var4 = this.mCategories;
      if (var4 != null) {
         var1.setCategories(var4);
      }

      var1.setExtras(this.buildExtrasBundle());
      return var1.build();
   }

   public static class Builder {
      private final ShortcutInfoCompat mInfo;

      public Builder(Context var1, ShortcutInfo var2) {
         ShortcutInfoCompat var3 = new ShortcutInfoCompat();
         this.mInfo = var3;
         var3.mContext = var1;
         this.mInfo.mId = var2.getId();
         Intent[] var4 = var2.getIntents();
         this.mInfo.mIntents = (Intent[])Arrays.copyOf(var4, var4.length);
         this.mInfo.mActivity = var2.getActivity();
         this.mInfo.mLabel = var2.getShortLabel();
         this.mInfo.mLongLabel = var2.getLongLabel();
         this.mInfo.mDisabledMessage = var2.getDisabledMessage();
         this.mInfo.mCategories = var2.getCategories();
         this.mInfo.mPersons = ShortcutInfoCompat.getPersonsFromExtra(var2.getExtras());
      }

      public Builder(Context var1, String var2) {
         ShortcutInfoCompat var3 = new ShortcutInfoCompat();
         this.mInfo = var3;
         var3.mContext = var1;
         this.mInfo.mId = var2;
      }

      public Builder(ShortcutInfoCompat var1) {
         ShortcutInfoCompat var2 = new ShortcutInfoCompat();
         this.mInfo = var2;
         var2.mContext = var1.mContext;
         this.mInfo.mId = var1.mId;
         this.mInfo.mIntents = (Intent[])Arrays.copyOf(var1.mIntents, var1.mIntents.length);
         this.mInfo.mActivity = var1.mActivity;
         this.mInfo.mLabel = var1.mLabel;
         this.mInfo.mLongLabel = var1.mLongLabel;
         this.mInfo.mDisabledMessage = var1.mDisabledMessage;
         this.mInfo.mIcon = var1.mIcon;
         this.mInfo.mIsAlwaysBadged = var1.mIsAlwaysBadged;
         this.mInfo.mIsLongLived = var1.mIsLongLived;
         if (var1.mPersons != null) {
            this.mInfo.mPersons = (Person[])Arrays.copyOf(var1.mPersons, var1.mPersons.length);
         }

         if (var1.mCategories != null) {
            this.mInfo.mCategories = new HashSet(var1.mCategories);
         }

      }

      public ShortcutInfoCompat build() {
         if (!TextUtils.isEmpty(this.mInfo.mLabel)) {
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
               return this.mInfo;
            } else {
               throw new IllegalArgumentException("Shortcut must have an intent");
            }
         } else {
            throw new IllegalArgumentException("Shortcut must have a non-empty label");
         }
      }

      public ShortcutInfoCompat.Builder setActivity(ComponentName var1) {
         this.mInfo.mActivity = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setAlwaysBadged() {
         this.mInfo.mIsAlwaysBadged = true;
         return this;
      }

      public ShortcutInfoCompat.Builder setCategories(Set var1) {
         this.mInfo.mCategories = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setDisabledMessage(CharSequence var1) {
         this.mInfo.mDisabledMessage = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setIcon(IconCompat var1) {
         this.mInfo.mIcon = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setIntent(Intent var1) {
         return this.setIntents(new Intent[]{var1});
      }

      public ShortcutInfoCompat.Builder setIntents(Intent[] var1) {
         this.mInfo.mIntents = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setLongLabel(CharSequence var1) {
         this.mInfo.mLongLabel = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setLongLived() {
         this.mInfo.mIsLongLived = true;
         return this;
      }

      public ShortcutInfoCompat.Builder setPerson(Person var1) {
         return this.setPersons(new Person[]{var1});
      }

      public ShortcutInfoCompat.Builder setPersons(Person[] var1) {
         this.mInfo.mPersons = var1;
         return this;
      }

      public ShortcutInfoCompat.Builder setShortLabel(CharSequence var1) {
         this.mInfo.mLabel = var1;
         return this;
      }
   }
}
