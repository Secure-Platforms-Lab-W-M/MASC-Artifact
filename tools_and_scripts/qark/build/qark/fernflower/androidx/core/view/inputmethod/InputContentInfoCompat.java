package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.inputmethod.InputContentInfo;

public final class InputContentInfoCompat {
   private final InputContentInfoCompat.InputContentInfoCompatImpl mImpl;

   public InputContentInfoCompat(Uri var1, ClipDescription var2, Uri var3) {
      if (VERSION.SDK_INT >= 25) {
         this.mImpl = new InputContentInfoCompat.InputContentInfoCompatApi25Impl(var1, var2, var3);
      } else {
         this.mImpl = new InputContentInfoCompat.InputContentInfoCompatBaseImpl(var1, var2, var3);
      }
   }

   private InputContentInfoCompat(InputContentInfoCompat.InputContentInfoCompatImpl var1) {
      this.mImpl = var1;
   }

   public static InputContentInfoCompat wrap(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         return VERSION.SDK_INT < 25 ? null : new InputContentInfoCompat(new InputContentInfoCompat.InputContentInfoCompatApi25Impl(var0));
      }
   }

   public Uri getContentUri() {
      return this.mImpl.getContentUri();
   }

   public ClipDescription getDescription() {
      return this.mImpl.getDescription();
   }

   public Uri getLinkUri() {
      return this.mImpl.getLinkUri();
   }

   public void releasePermission() {
      this.mImpl.releasePermission();
   }

   public void requestPermission() {
      this.mImpl.requestPermission();
   }

   public Object unwrap() {
      return this.mImpl.getInputContentInfo();
   }

   private static final class InputContentInfoCompatApi25Impl implements InputContentInfoCompat.InputContentInfoCompatImpl {
      final InputContentInfo mObject;

      InputContentInfoCompatApi25Impl(Uri var1, ClipDescription var2, Uri var3) {
         this.mObject = new InputContentInfo(var1, var2, var3);
      }

      InputContentInfoCompatApi25Impl(Object var1) {
         this.mObject = (InputContentInfo)var1;
      }

      public Uri getContentUri() {
         return this.mObject.getContentUri();
      }

      public ClipDescription getDescription() {
         return this.mObject.getDescription();
      }

      public Object getInputContentInfo() {
         return this.mObject;
      }

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
      private final Uri mContentUri;
      private final ClipDescription mDescription;
      private final Uri mLinkUri;

      InputContentInfoCompatBaseImpl(Uri var1, ClipDescription var2, Uri var3) {
         this.mContentUri = var1;
         this.mDescription = var2;
         this.mLinkUri = var3;
      }

      public Uri getContentUri() {
         return this.mContentUri;
      }

      public ClipDescription getDescription() {
         return this.mDescription;
      }

      public Object getInputContentInfo() {
         return null;
      }

      public Uri getLinkUri() {
         return this.mLinkUri;
      }

      public void releasePermission() {
      }

      public void requestPermission() {
      }
   }

   private interface InputContentInfoCompatImpl {
      Uri getContentUri();

      ClipDescription getDescription();

      Object getInputContentInfo();

      Uri getLinkUri();

      void releasePermission();

      void requestPermission();
   }
}
