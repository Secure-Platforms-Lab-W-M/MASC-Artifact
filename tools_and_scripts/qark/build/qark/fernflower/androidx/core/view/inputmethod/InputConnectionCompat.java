package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
   private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
   private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
   private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
   private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
   private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
   private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
   private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
   private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
   private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
   private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
   private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
   private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
   private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
   private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
   public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

   public static boolean commitContent(InputConnection var0, EditorInfo var1, InputContentInfoCompat var2, int var3, Bundle var4) {
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

      if (!var6) {
         return false;
      } else if (VERSION.SDK_INT >= 25) {
         return var0.commitContent((InputContentInfo)var2.unwrap(), var3, var4);
      } else {
         var5 = EditorInfoCompat.getProtocol(var1);
         boolean var12;
         if (var5 != 2) {
            if (var5 != 3 && var5 != 4) {
               return false;
            }

            var12 = false;
         } else {
            var12 = true;
         }

         Bundle var13 = new Bundle();
         String var11;
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
         }

         var13.putParcelable(var11, var2.getContentUri());
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
         }

         var13.putParcelable(var11, var2.getDescription());
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
         }

         var13.putParcelable(var11, var2.getLinkUri());
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
         }

         var13.putInt(var11, var3);
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
         }

         var13.putParcelable(var11, var4);
         if (var12) {
            var11 = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
         } else {
            var11 = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
         }

         return var0.performPrivateCommand(var11, var13);
      }
   }

   public static InputConnection createWrapper(InputConnection var0, EditorInfo var1, final InputConnectionCompat.OnCommitContentListener var2) {
      if (var0 != null) {
         if (var1 != null) {
            if (var2 != null) {
               if (VERSION.SDK_INT >= 25) {
                  return new InputConnectionWrapper(var0, false) {
                     public boolean commitContent(InputContentInfo var1, int var2x, Bundle var3) {
                        return var2.onCommitContent(InputContentInfoCompat.wrap(var1), var2x, var3) ? true : super.commitContent(var1, var2x, var3);
                     }
                  };
               } else {
                  return (InputConnection)(EditorInfoCompat.getContentMimeTypes(var1).length == 0 ? var0 : new InputConnectionWrapper(var0, false) {
                     public boolean performPrivateCommand(String var1, Bundle var2x) {
                        return InputConnectionCompat.handlePerformPrivateCommand(var1, var2x, var2) ? true : super.performPrivateCommand(var1, var2x);
                     }
                  });
               }
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

   static boolean handlePerformPrivateCommand(String var0, Bundle var1, InputConnectionCompat.OnCommitContentListener var2) {
      byte var4 = 0;
      if (var1 == null) {
         return false;
      } else {
         boolean var3;
         if (TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", var0)) {
            var3 = false;
         } else {
            if (!TextUtils.equals("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", var0)) {
               return false;
            }

            var3 = true;
         }

         ResultReceiver var69 = null;
         boolean var7 = false;
         String var8;
         if (var3) {
            var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
         } else {
            var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
         }

         boolean var6;
         ResultReceiver var9;
         label951: {
            Throwable var10000;
            label952: {
               boolean var10001;
               try {
                  var9 = (ResultReceiver)var1.getParcelable(var8);
               } catch (Throwable var68) {
                  var10000 = var68;
                  var10001 = false;
                  break label952;
               }

               if (var3) {
                  var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
               } else {
                  var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
               }

               var69 = var9;

               Uri var10;
               try {
                  var10 = (Uri)var1.getParcelable(var8);
               } catch (Throwable var67) {
                  var10000 = var67;
                  var10001 = false;
                  break label952;
               }

               if (var3) {
                  var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
               } else {
                  var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
               }

               var69 = var9;

               ClipDescription var11;
               try {
                  var11 = (ClipDescription)var1.getParcelable(var8);
               } catch (Throwable var66) {
                  var10000 = var66;
                  var10001 = false;
                  break label952;
               }

               if (var3) {
                  var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
               } else {
                  var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
               }

               var69 = var9;

               Uri var12;
               try {
                  var12 = (Uri)var1.getParcelable(var8);
               } catch (Throwable var65) {
                  var10000 = var65;
                  var10001 = false;
                  break label952;
               }

               if (var3) {
                  var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
               } else {
                  var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
               }

               var69 = var9;

               int var5;
               try {
                  var5 = var1.getInt(var8);
               } catch (Throwable var64) {
                  var10000 = var64;
                  var10001 = false;
                  break label952;
               }

               if (var3) {
                  var8 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
               } else {
                  var8 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
               }

               var69 = var9;

               try {
                  var1 = (Bundle)var1.getParcelable(var8);
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label952;
               }

               var6 = var7;
               if (var10 == null) {
                  break label951;
               }

               var6 = var7;
               if (var11 == null) {
                  break label951;
               }

               var69 = var9;

               label920:
               try {
                  var6 = var2.onCommitContent(new InputContentInfoCompat(var10, var11, var12), var5, var1);
                  break label951;
               } catch (Throwable var62) {
                  var10000 = var62;
                  var10001 = false;
                  break label920;
               }
            }

            Throwable var70 = var10000;
            if (var69 != null) {
               var69.send(0, (Bundle)null);
            }

            throw var70;
         }

         if (var9 != null) {
            byte var71 = var4;
            if (var6) {
               var71 = 1;
            }

            var9.send(var71, (Bundle)null);
         }

         return var6;
      }
   }

   public interface OnCommitContentListener {
      boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
   }
}
