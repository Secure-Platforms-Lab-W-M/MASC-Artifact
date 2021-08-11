package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.InputContentInfo;

public final class InputContentInfoCompat {
   private final InputContentInfoCompat.InputContentInfoCompatImpl mImpl;

   public InputContentInfoCompat(@NonNull Uri var1, @NonNull ClipDescription var2, @Nullable Uri var3) {
      if (VERSION.SDK_INT >= 25) {
         this.mImpl = new InputContentInfoCompat.InputContentInfoCompatApi25Impl(var1, var2, var3);
      } else {
         this.mImpl = new InputContentInfoCompat.InputContentInfoCompatBaseImpl(var1, var2, var3);
      }
   }

   private InputContentInfoCompat(@NonNull InputContentInfoCompat.InputContentInfoCompatImpl var1) {
      this.mImpl = var1;
   }

   @Nullable
   public static InputContentInfoCompat wrap(@Nullable Object var0) {
      if (var0 == null) {
         return null;
      } else {
         return VERSION.SDK_INT < 25 ? null : new InputContentInfoCompat(new InputContentInfoCompat.InputContentInfoCompatApi25Impl(var0));
      }
   }

   @NonNull
   public Uri getContentUri() {
      return this.mImpl.getContentUri();
   }

   @NonNull
   public ClipDescription getDescription() {
      return this.mImpl.getDescription();
   }

   @Nullable
   public Uri getLinkUri() {
      return this.mImpl.getLinkUri();
   }

   public void releasePermission() {
      this.mImpl.releasePermission();
   }

   public void requestPermission() {
      this.mImpl.requestPermission();
   }

   @Nullable
   public Object unwrap() {
      return this.mImpl.getInputContentInfo();
   }

   @RequiresApi(25)
   private static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompat.InputContentInfoCompatImpl {
      @NonNull
      final InputContentInfo mObject;

      InputContentInfoCompatApi25Impl(@NonNull Uri var1, @NonNull ClipDescription var2, @Nullable Uri var3) {
         this.mObject = new InputContentInfo(var1, var2, var3);
      }

      InputContentInfoCompatApi25Impl(@NonNull Object var1) {
         this.mObject = (InputContentInfo)var1;
      }

      @NonNull
      public Uri getContentUri() {
         return this.mObject.getContentUri();
      }

      @NonNull
      public ClipDescription getDescription() {
         return this.mObject.getDescription();
      }

      @Nullable
      public Object getInputContentInfo() {
         return this.mObject;
      }

      @Nullable
      public Uri getLinkUri() {
         return this.mObject.getLinkUri();
      }

      public void releasePermission() {
         this.mObject.releasePermission();
      }

      public void requestPermission() {
         this.mObject.requestPermission();
      }
   }

   private static final class InputContentInfoCompatBaseImpl implements InputContentInfoCompat.InputContentInfoCompatImpl {
      @NonNull
      private final Uri mContentUri;
      @NonNull
      private final ClipDescription mDescription;
      @Nullable
      private final Uri mLinkUri;

      InputContentInfoCompatBaseImpl(@NonNull Uri var1, @NonNull ClipDescription var2, @Nullable Uri var3) {
         this.mContentUri = var1;
         this.mDescription = var2;
         this.mLinkUri = var3;
      }

      @NonNull
      public Uri getContentUri() {
         return this.mContentUri;
      }

      @NonNull
      public ClipDescription getDescription() {
         return this.mDescription;
      }

      @Nullable
      public Object getInputContentInfo() {
         return null;
      }

      @Nullable
      public Uri getLinkUri() {
         return this.mLinkUri;
      }

      public void releasePermission() {
      }

      public void requestPermission() {
      }
   }

   private interface InputContentInfoCompatImpl {
      @NonNull
      Uri getContentUri();

      @NonNull
      ClipDescription getDescription();

      @Nullable
      Object getInputContentInfo();

      @Nullable
      Uri getLinkUri();

      void releasePermission();

      void requestPermission();
   }
}
