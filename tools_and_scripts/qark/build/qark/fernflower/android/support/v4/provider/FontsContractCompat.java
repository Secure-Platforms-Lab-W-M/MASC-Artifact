package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Build.VERSION;
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
   private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String PARCEL_FONT_RESULTS = "font_results";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
   private static final String TAG = "FontsContractCompat";
   private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
   private static final Comparator sByteArrayComparator = new Comparator() {
      public int compare(byte[] var1, byte[] var2) {
         if (var1.length != var2.length) {
            return var1.length - var2.length;
         } else {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var1[var3] != var2[var3]) {
                  return var1[var3] - var2[var3];
               }
            }

            return 0;
         }
      }
   };
   private static final Object sLock = new Object();
   @GuardedBy("sLock")
   private static final SimpleArrayMap sPendingReplies = new SimpleArrayMap();
   private static final LruCache sTypefaceCache = new LruCache(16);

   private FontsContractCompat() {
   }

   // $FF: synthetic method
   static Object access$200() {
      return sLock;
   }

   // $FF: synthetic method
   static SimpleArrayMap access$300() {
      return sPendingReplies;
   }

   public static Typeface buildTypeface(@NonNull Context var0, @Nullable CancellationSignal var1, @NonNull FontsContractCompat.FontInfo[] var2) {
      return TypefaceCompat.createFromFontInfo(var0, var1, var2, 0);
   }

   private static List convertToByteArrayList(Signature[] var0) {
      ArrayList var2 = new ArrayList();

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var2.add(var0[var1].toByteArray());
      }

      return var2;
   }

   private static boolean equalsByteArrayList(List var0, List var1) {
      if (var0.size() != var1.size()) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            if (!Arrays.equals((byte[])var0.get(var2), (byte[])var1.get(var2))) {
               return false;
            }
         }

         return true;
      }
   }

   @NonNull
   public static FontsContractCompat.FontFamilyResult fetchFonts(@NonNull Context var0, @Nullable CancellationSignal var1, @NonNull FontRequest var2) throws NameNotFoundException {
      ProviderInfo var3 = getProvider(var0.getPackageManager(), var2, var0.getResources());
      return var3 == null ? new FontsContractCompat.FontFamilyResult(1, (FontsContractCompat.FontInfo[])null) : new FontsContractCompat.FontFamilyResult(0, getFontFromProvider(var0, var2, var3.authority, var1));
   }

   private static List getCertificates(FontRequest var0, Resources var1) {
      return var0.getCertificates() != null ? var0.getCertificates() : FontResourcesParserCompat.readCerts(var1, var0.getCertificatesArrayResId());
   }

   @NonNull
   @VisibleForTesting
   static FontsContractCompat.FontInfo[] getFontFromProvider(Context var0, FontRequest var1, String var2, CancellationSignal var3) {
      ArrayList var14 = new ArrayList();
      Uri var16 = (new Builder()).scheme("content").authority(var2).build();
      Uri var17 = (new Builder()).scheme("content").authority(var2).appendPath("file").build();
      var2 = null;
      Object var15 = null;

      Cursor var360;
      ArrayList var365;
      label3035: {
         label3039: {
            Throwable var362;
            label3040: {
               int var4;
               int var7;
               Throwable var10000;
               boolean var10001;
               ArrayList var367;
               label3032: {
                  label3041: {
                     try {
                        var4 = VERSION.SDK_INT;
                     } catch (Throwable var359) {
                        var10000 = var359;
                        var10001 = false;
                        break label3041;
                     }

                     Cursor var364;
                     label3042: {
                        ContentResolver var361;
                        String var363;
                        if (var4 > 16) {
                           try {
                              var361 = var0.getContentResolver();
                              var363 = var1.getQuery();
                           } catch (Throwable var354) {
                              var10000 = var354;
                              var10001 = false;
                              break label3041;
                           }

                           var364 = (Cursor)var15;

                           try {
                              var360 = var361.query(var16, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var363}, (String)null, var3);
                           } catch (Throwable var358) {
                              var10000 = var358;
                              var10001 = false;
                              break label3042;
                           }
                        } else {
                           var364 = (Cursor)var15;

                           try {
                              var361 = var0.getContentResolver();
                           } catch (Throwable var357) {
                              var10000 = var357;
                              var10001 = false;
                              break label3042;
                           }

                           var364 = (Cursor)var15;

                           try {
                              var363 = var1.getQuery();
                           } catch (Throwable var356) {
                              var10000 = var356;
                              var10001 = false;
                              break label3042;
                           }

                           var364 = (Cursor)var15;

                           try {
                              var360 = var361.query(var16, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var363}, (String)null);
                           } catch (Throwable var355) {
                              var10000 = var355;
                              var10001 = false;
                              break label3042;
                           }
                        }

                        if (var360 == null) {
                           break label3039;
                        }

                        var364 = var360;

                        try {
                           if (var360.getCount() <= 0) {
                              break label3039;
                           }
                        } catch (Throwable var353) {
                           var10000 = var353;
                           var10001 = false;
                           break label3042;
                        }

                        var364 = var360;

                        try {
                           var7 = var360.getColumnIndex("result_code");
                        } catch (Throwable var352) {
                           var10000 = var352;
                           var10001 = false;
                           break label3042;
                        }

                        var364 = var360;

                        label3002:
                        try {
                           var367 = new ArrayList();
                           break label3032;
                        } catch (Throwable var351) {
                           var10000 = var351;
                           var10001 = false;
                           break label3002;
                        }
                     }

                     var362 = var10000;
                     var360 = var364;
                     break label3040;
                  }

                  var362 = var10000;
                  var360 = var2;
                  break label3040;
               }

               label2993: {
                  int var8;
                  int var9;
                  int var10;
                  int var11;
                  int var12;
                  try {
                     var8 = var360.getColumnIndex("_id");
                     var9 = var360.getColumnIndex("file_id");
                     var10 = var360.getColumnIndex("font_ttc_index");
                     var11 = var360.getColumnIndex("font_weight");
                     var12 = var360.getColumnIndex("font_italic");
                  } catch (Throwable var350) {
                     var10000 = var350;
                     var10001 = false;
                     break label2993;
                  }

                  while(true) {
                     var365 = var367;

                     try {
                        if (!var360.moveToNext()) {
                           break label3035;
                        }
                     } catch (Throwable var348) {
                        var10000 = var348;
                        var10001 = false;
                        break;
                     }

                     if (var7 != -1) {
                        try {
                           var4 = var360.getInt(var7);
                        } catch (Throwable var347) {
                           var10000 = var347;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var4 = 0;
                     }

                     int var5;
                     if (var10 != -1) {
                        try {
                           var5 = var360.getInt(var10);
                        } catch (Throwable var346) {
                           var10000 = var346;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var5 = 0;
                     }

                     Uri var366;
                     if (var9 == -1) {
                        try {
                           var366 = ContentUris.withAppendedId(var16, var360.getLong(var8));
                        } catch (Throwable var345) {
                           var10000 = var345;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           var366 = ContentUris.withAppendedId(var17, var360.getLong(var9));
                        } catch (Throwable var344) {
                           var10000 = var344;
                           var10001 = false;
                           break;
                        }
                     }

                     int var6;
                     if (var11 != -1) {
                        try {
                           var6 = var360.getInt(var11);
                        } catch (Throwable var343) {
                           var10000 = var343;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var6 = 400;
                     }

                     boolean var13;
                     label2987: {
                        label2986: {
                           if (var12 != -1) {
                              try {
                                 if (var360.getInt(var12) == 1) {
                                    break label2986;
                                 }
                              } catch (Throwable var349) {
                                 var10000 = var349;
                                 var10001 = false;
                                 break;
                              }
                           }

                           var13 = false;
                           break label2987;
                        }

                        var13 = true;
                     }

                     try {
                        var367.add(new FontsContractCompat.FontInfo(var366, var5, var6, var13, var4));
                     } catch (Throwable var342) {
                        var10000 = var342;
                        var10001 = false;
                        break;
                     }
                  }
               }

               var362 = var10000;
            }

            if (var360 != null) {
               var360.close();
            }

            throw var362;
         }

         var365 = var14;
      }

      if (var360 != null) {
         var360.close();
      }

      return (FontsContractCompat.FontInfo[])var365.toArray(new FontsContractCompat.FontInfo[0]);
   }

   private static Typeface getFontInternal(Context var0, FontRequest var1, int var2) {
      FontsContractCompat.FontFamilyResult var4;
      try {
         var4 = fetchFonts(var0, (CancellationSignal)null, var1);
      } catch (NameNotFoundException var3) {
         return null;
      }

      return var4.getStatusCode() == 0 ? TypefaceCompat.createFromFontInfo(var0, (CancellationSignal)null, var4.getFonts(), var2) : null;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static Typeface getFontSync(final Context var0, final FontRequest var1, @Nullable final TextView var2, int var3, int var4, final int var5) {
      StringBuilder var6 = new StringBuilder();
      var6.append(var1.getIdentifier());
      var6.append("-");
      var6.append(var5);
      final String var30 = var6.toString();
      Typeface var7 = (Typeface)sTypefaceCache.get(var30);
      if (var7 != null) {
         return var7;
      } else {
         boolean var29;
         if (var3 == 0) {
            var29 = true;
         } else {
            var29 = false;
         }

         if (var29 && var4 == -1) {
            return getFontInternal(var0, var1, var5);
         } else {
            Callable var24 = new Callable() {
               public Typeface call() throws Exception {
                  Typeface var1x = FontsContractCompat.getFontInternal(var0, var1, var5);
                  if (var1x != null) {
                     FontsContractCompat.sTypefaceCache.put(var30, var1x);
                  }

                  return var1x;
               }
            };
            if (var29) {
               try {
                  Typeface var26 = (Typeface)sBackgroundThread.postAndWait(var24, var4);
                  return var26;
               } catch (InterruptedException var20) {
                  return null;
               }
            } else {
               SelfDestructiveThread.ReplyCallback var28 = new SelfDestructiveThread.ReplyCallback(new WeakReference(var2)) {
                  // $FF: synthetic field
                  final WeakReference val$textViewWeak;

                  {
                     this.val$textViewWeak = var1;
                  }

                  public void onReply(Typeface var1) {
                     if ((TextView)this.val$textViewWeak.get() != null) {
                        var2.setTypeface(var1, var5);
                     }

                  }
               };
               Object var27 = sLock;
               synchronized(var27){}

               Throwable var10000;
               boolean var10001;
               label223: {
                  try {
                     if (sPendingReplies.containsKey(var30)) {
                        ((ArrayList)sPendingReplies.get(var30)).add(var28);
                        return null;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label223;
                  }

                  try {
                     ArrayList var31 = new ArrayList();
                     var31.add(var28);
                     sPendingReplies.put(var30, var31);
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label223;
                  }

                  sBackgroundThread.postAndReply(var24, new SelfDestructiveThread.ReplyCallback() {
                     public void onReply(Typeface param1) {
                        // $FF: Couldn't be decompiled
                     }
                  });
                  return null;
               }

               while(true) {
                  Throwable var25 = var10000;

                  try {
                     throw var25;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     continue;
                  }
               }
            }
         }
      }
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   @VisibleForTesting
   public static ProviderInfo getProvider(@NonNull PackageManager var0, @NonNull FontRequest var1, @Nullable Resources var2) throws NameNotFoundException {
      String var5 = var1.getProviderAuthority();
      ProviderInfo var4 = var0.resolveContentProvider(var5, 0);
      StringBuilder var6;
      if (var4 != null) {
         if (var4.packageName.equals(var1.getProviderPackage())) {
            List var8 = convertToByteArrayList(var0.getPackageInfo(var4.packageName, 64).signatures);
            Collections.sort(var8, sByteArrayComparator);
            List var9 = getCertificates(var1, var2);

            for(int var3 = 0; var3 < var9.size(); ++var3) {
               ArrayList var10 = new ArrayList((Collection)var9.get(var3));
               Collections.sort(var10, sByteArrayComparator);
               if (equalsByteArrayList(var8, var10)) {
                  return var4;
               }
            }

            return null;
         } else {
            var6 = new StringBuilder();
            var6.append("Found content provider ");
            var6.append(var5);
            var6.append(", but package was not ");
            var6.append(var1.getProviderPackage());
            throw new NameNotFoundException(var6.toString());
         }
      } else {
         var6 = new StringBuilder();
         var6.append("No package found for authority: ");
         var6.append(var5);
         NameNotFoundException var7 = new NameNotFoundException(var6.toString());
         throw var7;
      }
   }

   @RequiresApi(19)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static Map prepareFontData(Context var0, FontsContractCompat.FontInfo[] var1, CancellationSignal var2) {
      HashMap var5 = new HashMap();
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         FontsContractCompat.FontInfo var6 = var1[var3];
         if (var6.getResultCode() == 0) {
            Uri var7 = var6.getUri();
            if (!var5.containsKey(var7)) {
               var5.put(var7, TypefaceCompatUtil.mmap(var0, var2, var7));
            }
         }
      }

      return Collections.unmodifiableMap(var5);
   }

   public static void requestFont(@NonNull final Context var0, @NonNull final FontRequest var1, @NonNull final FontsContractCompat.FontRequestCallback var2, @NonNull Handler var3) {
      var3.post(new Runnable(new Handler()) {
         // $FF: synthetic field
         final Handler val$callerThreadHandler;

         {
            this.val$callerThreadHandler = var3;
         }

         public void run() {
            FontsContractCompat.FontFamilyResult var3;
            try {
               var3 = FontsContractCompat.fetchFonts(var0, (CancellationSignal)null, var1);
            } catch (NameNotFoundException var5) {
               this.val$callerThreadHandler.post(new Runnable() {
                  public void run() {
                     var2.onTypefaceRequestFailed(-1);
                  }
               });
               return;
            }

            final int var1x;
            if (var3.getStatusCode() != 0) {
               var1x = var3.getStatusCode();
               if (var1x != 1) {
                  if (var1x != 2) {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRequestFailed(-3);
                        }
                     });
                  } else {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRequestFailed(-3);
                        }
                     });
                  }
               } else {
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(-2);
                     }
                  });
               }
            } else {
               FontsContractCompat.FontInfo[] var6 = var3.getFonts();
               if (var6 != null && var6.length != 0) {
                  int var2x = var6.length;

                  for(var1x = 0; var1x < var2x; ++var1x) {
                     FontsContractCompat.FontInfo var4 = var6[var1x];
                     if (var4.getResultCode() != 0) {
                        var1x = var4.getResultCode();
                        if (var1x < 0) {
                           this.val$callerThreadHandler.post(new Runnable() {
                              public void run() {
                                 var2.onTypefaceRequestFailed(-3);
                              }
                           });
                           return;
                        }

                        this.val$callerThreadHandler.post(new Runnable() {
                           public void run() {
                              var2.onTypefaceRequestFailed(var1x);
                           }
                        });
                        return;
                     }
                  }

                  final Typeface var7 = FontsContractCompat.buildTypeface(var0, (CancellationSignal)null, var6);
                  if (var7 == null) {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRequestFailed(-3);
                        }
                     });
                  } else {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRetrieved(var7);
                        }
                     });
                  }
               } else {
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(1);
                     }
                  });
               }
            }
         }
      });
   }

   public static final class Columns implements BaseColumns {
      public static final String FILE_ID = "file_id";
      public static final String ITALIC = "font_italic";
      public static final String RESULT_CODE = "result_code";
      public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
      public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
      public static final int RESULT_CODE_MALFORMED_QUERY = 3;
      public static final int RESULT_CODE_OK = 0;
      public static final String TTC_INDEX = "font_ttc_index";
      public static final String VARIATION_SETTINGS = "font_variation_settings";
      public static final String WEIGHT = "font_weight";
   }

   public static class FontFamilyResult {
      public static final int STATUS_OK = 0;
      public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
      public static final int STATUS_WRONG_CERTIFICATES = 1;
      private final FontsContractCompat.FontInfo[] mFonts;
      private final int mStatusCode;

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public FontFamilyResult(int var1, @Nullable FontsContractCompat.FontInfo[] var2) {
         this.mStatusCode = var1;
         this.mFonts = var2;
      }

      public FontsContractCompat.FontInfo[] getFonts() {
         return this.mFonts;
      }

      public int getStatusCode() {
         return this.mStatusCode;
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      @interface FontResultStatus {
      }
   }

   public static class FontInfo {
      private final boolean mItalic;
      private final int mResultCode;
      private final int mTtcIndex;
      private final Uri mUri;
      private final int mWeight;

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public FontInfo(@NonNull Uri var1, @IntRange(from = 0L) int var2, @IntRange(from = 1L,to = 1000L) int var3, boolean var4, int var5) {
         this.mUri = (Uri)Preconditions.checkNotNull(var1);
         this.mTtcIndex = var2;
         this.mWeight = var3;
         this.mItalic = var4;
         this.mResultCode = var5;
      }

      public int getResultCode() {
         return this.mResultCode;
      }

      @IntRange(
         from = 0L
      )
      public int getTtcIndex() {
         return this.mTtcIndex;
      }

      @NonNull
      public Uri getUri() {
         return this.mUri;
      }

      @IntRange(
         from = 1L,
         to = 1000L
      )
      public int getWeight() {
         return this.mWeight;
      }

      public boolean isItalic() {
         return this.mItalic;
      }
   }

   public static class FontRequestCallback {
      public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
      public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
      public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
      public static final int FAIL_REASON_MALFORMED_QUERY = 3;
      public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
      public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

      public void onTypefaceRequestFailed(int var1) {
      }

      public void onTypefaceRetrieved(Typeface var1) {
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      @interface FontRequestFailReason {
      }
   }
}
