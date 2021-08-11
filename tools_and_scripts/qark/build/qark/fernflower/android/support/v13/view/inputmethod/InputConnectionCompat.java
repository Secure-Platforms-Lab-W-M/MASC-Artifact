package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
   private static final InputConnectionCompat.InputConnectionCompatImpl IMPL;
   public static int INPUT_CONTENT_GRANT_READ_URI_PERMISSION;

   static {
      if (VERSION.SDK_INT >= 25) {
         IMPL = new InputConnectionCompat.InputContentInfoCompatApi25Impl();
      } else {
         IMPL = new InputConnectionCompat.InputContentInfoCompatBaseImpl();
      }

      INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
   }

   public static boolean commitContent(@NonNull InputConnection var0, @NonNull EditorInfo var1, @NonNull InputContentInfoCompat var2, int var3, @Nullable Bundle var4) {
      ClipDescription var9 = var2.getDescription();
      boolean var7 = false;
      String[] var10 = EditorInfoCompat.getContentMimeTypes(var1);
      int var8 = var10.length;
      int var5 = 0;

      boolean var6;
      while(true) {
         var6 = var7;
         if (var5 >= var8) {
            break;
         }

         if (var9.hasMimeType(var10[var5])) {
            var6 = true;
            break;
         }

         ++var5;
      }

      return !var6 ? false : IMPL.commitContent(var0, var2, var3, var4);
   }

   @NonNull
   public static InputConnection createWrapper(@NonNull InputConnection var0, @NonNull EditorInfo var1, @NonNull InputConnectionCompat.OnCommitContentListener var2) {
      if (var0 != null) {
         if (var1 != null) {
            if (var2 != null) {
               return IMPL.createWrapper(var0, var1, var2);
            } else {
               throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
         } else {
            throw new IllegalArgumentException("editorInfo must be non-null");
         }
      } else {
         throw new IllegalArgumentException("inputConnection must be non-null");
      }
   }

   private interface InputConnectionCompatImpl {
      boolean commitContent(@NonNull InputConnection var1, @NonNull InputContentInfoCompat var2, int var3, @Nullable Bundle var4);

      @NonNull
      InputConnection createWrapper(@NonNull InputConnection var1, @NonNull EditorInfo var2, @NonNull InputConnectionCompat.OnCommitContentListener var3);
   }

   @RequiresApi(25)
   private static final class InputContentInfoCompatApi25Impl implements InputConnectionCompat.InputConnectionCompatImpl {
      private InputContentInfoCompatApi25Impl() {
      }

      // $FF: synthetic method
      InputContentInfoCompatApi25Impl(Object var1) {
         this();
      }

      public boolean commitContent(@NonNull InputConnection var1, @NonNull InputContentInfoCompat var2, int var3, @Nullable Bundle var4) {
         return var1.commitContent((InputContentInfo)var2.unwrap(), var3, var4);
      }

      @Nullable
      public InputConnection createWrapper(@Nullable InputConnection var1, @NonNull EditorInfo var2, @Nullable final InputConnectionCompat.OnCommitContentListener var3) {
         return new InputConnectionWrapper(var1, false) {
            public boolean commitContent(InputContentInfo var1, int var2, Bundle var3x) {
               return var3.onCommitContent(InputContentInfoCompat.wrap(var1), var2, var3x) ? true : super.commitContent(var1, var2, var3x);
            }
         };
      }
   }

   static final class InputContentInfoCompatBaseImpl implements InputConnectionCompat.InputConnectionCompatImpl {
      private static String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
      private static String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
      private static String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
      private static String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
      private static String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
      private static String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
      private static String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";

      static boolean handlePerformPrivateCommand(@Nullable String var0, @NonNull Bundle var1, @NonNull InputConnectionCompat.OnCommitContentListener var2) {
         boolean var6 = TextUtils.equals(COMMIT_CONTENT_ACTION, var0);
         byte var4 = 0;
         byte var3 = 0;
         if (!var6) {
            return false;
         } else if (var1 == null) {
            return false;
         } else {
            ResultReceiver var67 = null;

            ResultReceiver var7;
            label671: {
               Throwable var10000;
               label672: {
                  boolean var10001;
                  try {
                     var7 = (ResultReceiver)var1.getParcelable(COMMIT_CONTENT_RESULT_RECEIVER);
                  } catch (Throwable var66) {
                     var10000 = var66;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  Uri var8;
                  try {
                     var8 = (Uri)var1.getParcelable(COMMIT_CONTENT_CONTENT_URI_KEY);
                  } catch (Throwable var65) {
                     var10000 = var65;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  ClipDescription var9;
                  try {
                     var9 = (ClipDescription)var1.getParcelable(COMMIT_CONTENT_DESCRIPTION_KEY);
                  } catch (Throwable var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  Uri var10;
                  try {
                     var10 = (Uri)var1.getParcelable(COMMIT_CONTENT_LINK_URI_KEY);
                  } catch (Throwable var63) {
                     var10000 = var63;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  int var5;
                  try {
                     var5 = var1.getInt(COMMIT_CONTENT_FLAGS_KEY);
                  } catch (Throwable var62) {
                     var10000 = var62;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  try {
                     var1 = (Bundle)var1.getParcelable(COMMIT_CONTENT_OPTS_KEY);
                  } catch (Throwable var61) {
                     var10000 = var61;
                     var10001 = false;
                     break label672;
                  }

                  var67 = var7;

                  label639:
                  try {
                     var6 = var2.onCommitContent(new InputContentInfoCompat(var8, var9, var10), var5, var1);
                     break label671;
                  } catch (Throwable var60) {
                     var10000 = var60;
                     var10001 = false;
                     break label639;
                  }
               }

               Throwable var68 = var10000;
               if (var67 != null) {
                  var3 = var4;
                  if (false) {
                     var3 = 1;
                  }

                  var67.send(var3, (Bundle)null);
               }

               throw var68;
            }

            if (var7 != null) {
               if (var6) {
                  var3 = 1;
               }

               var7.send(var3, (Bundle)null);
            }

            return var6;
         }
      }

      public boolean commitContent(@NonNull InputConnection var1, @NonNull InputContentInfoCompat var2, int var3, @Nullable Bundle var4) {
         Bundle var5 = new Bundle();
         var5.putParcelable(COMMIT_CONTENT_CONTENT_URI_KEY, var2.getContentUri());
         var5.putParcelable(COMMIT_CONTENT_DESCRIPTION_KEY, var2.getDescription());
         var5.putParcelable(COMMIT_CONTENT_LINK_URI_KEY, var2.getLinkUri());
         var5.putInt(COMMIT_CONTENT_FLAGS_KEY, var3);
         var5.putParcelable(COMMIT_CONTENT_OPTS_KEY, var4);
         return var1.performPrivateCommand(COMMIT_CONTENT_ACTION, var5);
      }

      @NonNull
      public InputConnection createWrapper(@NonNull InputConnection var1, @NonNull EditorInfo var2, @NonNull final InputConnectionCompat.OnCommitContentListener var3) {
         return (InputConnection)(EditorInfoCompat.getContentMimeTypes(var2).length == 0 ? var1 : new InputConnectionWrapper(var1, false) {
            public boolean performPrivateCommand(String var1, Bundle var2) {
               return InputConnectionCompat.InputContentInfoCompatBaseImpl.handlePerformPrivateCommand(var1, var2, var3) ? true : super.performPrivateCommand(var1, var2);
            }
         });
      }
   }

   public interface OnCommitContentListener {
      boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
   }
}
