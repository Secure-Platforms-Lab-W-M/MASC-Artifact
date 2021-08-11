package androidx.core.provider;

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
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
   public static final String PARCEL_FONT_RESULTS = "font_results";
   static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
   static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
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
   static final Object sLock = new Object();
   static final SimpleArrayMap sPendingReplies = new SimpleArrayMap();
   static final LruCache sTypefaceCache = new LruCache(16);

   private FontsContractCompat() {
   }

   public static Typeface buildTypeface(Context var0, CancellationSignal var1, FontsContractCompat.FontInfo[] var2) {
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

   public static FontsContractCompat.FontFamilyResult fetchFonts(Context var0, CancellationSignal var1, FontRequest var2) throws NameNotFoundException {
      ProviderInfo var3 = getProvider(var0.getPackageManager(), var2, var0.getResources());
      return var3 == null ? new FontsContractCompat.FontFamilyResult(1, (FontsContractCompat.FontInfo[])null) : new FontsContractCompat.FontFamilyResult(0, getFontFromProvider(var0, var2, var3.authority, var1));
   }

   private static List getCertificates(FontRequest var0, Resources var1) {
      return var0.getCertificates() != null ? var0.getCertificates() : FontResourcesParserCompat.readCerts(var1, var0.getCertificatesArrayResId());
   }

   static FontsContractCompat.FontInfo[] getFontFromProvider(Context var0, FontRequest var1, String var2, CancellationSignal var3) {
      ArrayList var14 = new ArrayList();
      Uri var16 = (new Builder()).scheme("content").authority(var2).build();
      Uri var17 = (new Builder()).scheme("content").authority(var2).appendPath("file").build();
      Object var15 = null;
      Cursor var575 = (Cursor)var15;

      Cursor var571;
      ArrayList var574;
      label4801: {
         Throwable var10000;
         label4802: {
            boolean var10001;
            label4803: {
               ContentResolver var570;
               String var572;
               label4804: {
                  try {
                     if (VERSION.SDK_INT > 16) {
                        break label4804;
                     }
                  } catch (Throwable var569) {
                     var10000 = var569;
                     var10001 = false;
                     break label4802;
                  }

                  var575 = (Cursor)var15;

                  try {
                     var570 = var0.getContentResolver();
                  } catch (Throwable var565) {
                     var10000 = var565;
                     var10001 = false;
                     break label4802;
                  }

                  var575 = (Cursor)var15;

                  try {
                     var572 = var1.getQuery();
                  } catch (Throwable var564) {
                     var10000 = var564;
                     var10001 = false;
                     break label4802;
                  }

                  var575 = (Cursor)var15;

                  try {
                     var571 = var570.query(var16, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var572}, (String)null);
                     break label4803;
                  } catch (Throwable var563) {
                     var10000 = var563;
                     var10001 = false;
                     break label4802;
                  }
               }

               var575 = (Cursor)var15;

               try {
                  var570 = var0.getContentResolver();
               } catch (Throwable var568) {
                  var10000 = var568;
                  var10001 = false;
                  break label4802;
               }

               var575 = (Cursor)var15;

               try {
                  var572 = var1.getQuery();
               } catch (Throwable var567) {
                  var10000 = var567;
                  var10001 = false;
                  break label4802;
               }

               var575 = (Cursor)var15;

               try {
                  var571 = var570.query(var16, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var572}, (String)null, var3);
               } catch (Throwable var566) {
                  var10000 = var566;
                  var10001 = false;
                  break label4802;
               }
            }

            var574 = var14;
            if (var571 == null) {
               break label4801;
            }

            var574 = var14;
            var575 = var571;

            try {
               if (var571.getCount() <= 0) {
                  break label4801;
               }
            } catch (Throwable var562) {
               var10000 = var562;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var7;
            try {
               var7 = var571.getColumnIndex("result_code");
            } catch (Throwable var561) {
               var10000 = var561;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            ArrayList var577;
            try {
               var577 = new ArrayList();
            } catch (Throwable var560) {
               var10000 = var560;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var8;
            try {
               var8 = var571.getColumnIndex("_id");
            } catch (Throwable var559) {
               var10000 = var559;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var9;
            try {
               var9 = var571.getColumnIndex("file_id");
            } catch (Throwable var558) {
               var10000 = var558;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var10;
            try {
               var10 = var571.getColumnIndex("font_ttc_index");
            } catch (Throwable var557) {
               var10000 = var557;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var11;
            try {
               var11 = var571.getColumnIndex("font_weight");
            } catch (Throwable var556) {
               var10000 = var556;
               var10001 = false;
               break label4802;
            }

            var575 = var571;

            int var12;
            try {
               var12 = var571.getColumnIndex("font_italic");
            } catch (Throwable var555) {
               var10000 = var555;
               var10001 = false;
               break label4802;
            }

            while(true) {
               var574 = var577;
               var575 = var571;

               try {
                  if (!var571.moveToNext()) {
                     break label4801;
                  }
               } catch (Throwable var553) {
                  var10000 = var553;
                  var10001 = false;
                  break;
               }

               int var4;
               if (var7 != -1) {
                  var575 = var571;

                  try {
                     var4 = var571.getInt(var7);
                  } catch (Throwable var552) {
                     var10000 = var552;
                     var10001 = false;
                     break;
                  }
               } else {
                  var4 = 0;
               }

               int var5;
               if (var10 != -1) {
                  var575 = var571;

                  try {
                     var5 = var571.getInt(var10);
                  } catch (Throwable var551) {
                     var10000 = var551;
                     var10001 = false;
                     break;
                  }
               } else {
                  var5 = 0;
               }

               Uri var576;
               if (var9 == -1) {
                  var575 = var571;

                  try {
                     var576 = ContentUris.withAppendedId(var16, var571.getLong(var8));
                  } catch (Throwable var550) {
                     var10000 = var550;
                     var10001 = false;
                     break;
                  }
               } else {
                  var575 = var571;

                  try {
                     var576 = ContentUris.withAppendedId(var17, var571.getLong(var9));
                  } catch (Throwable var549) {
                     var10000 = var549;
                     var10001 = false;
                     break;
                  }
               }

               int var6;
               if (var11 != -1) {
                  var575 = var571;

                  try {
                     var6 = var571.getInt(var11);
                  } catch (Throwable var548) {
                     var10000 = var548;
                     var10001 = false;
                     break;
                  }
               } else {
                  var6 = 400;
               }

               boolean var13;
               label4808: {
                  if (var12 != -1) {
                     label4807: {
                        var575 = var571;

                        try {
                           if (var571.getInt(var12) != 1) {
                              break label4807;
                           }
                        } catch (Throwable var554) {
                           var10000 = var554;
                           var10001 = false;
                           break;
                        }

                        var13 = true;
                        break label4808;
                     }
                  }

                  var13 = false;
               }

               var575 = var571;

               try {
                  var577.add(new FontsContractCompat.FontInfo(var576, var5, var6, var13, var4));
               } catch (Throwable var547) {
                  var10000 = var547;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var573 = var10000;
         if (var575 != null) {
            var575.close();
         }

         throw var573;
      }

      if (var571 != null) {
         var571.close();
      }

      return (FontsContractCompat.FontInfo[])var574.toArray(new FontsContractCompat.FontInfo[0]);
   }

   static FontsContractCompat.TypefaceResult getFontInternal(Context var0, FontRequest var1, int var2) {
      FontsContractCompat.FontFamilyResult var7;
      try {
         var7 = fetchFonts(var0, (CancellationSignal)null, var1);
      } catch (NameNotFoundException var5) {
         return new FontsContractCompat.TypefaceResult((Typeface)null, -1);
      }

      int var4 = var7.getStatusCode();
      byte var3 = -3;
      if (var4 == 0) {
         Typeface var6 = TypefaceCompat.createFromFontInfo(var0, (CancellationSignal)null, var7.getFonts(), var2);
         if (var6 != null) {
            var3 = 0;
         }

         return new FontsContractCompat.TypefaceResult(var6, var3);
      } else {
         if (var7.getStatusCode() == 1) {
            var3 = -2;
         }

         return new FontsContractCompat.TypefaceResult((Typeface)null, var3);
      }
   }

   public static Typeface getFontSync(final Context var0, final FontRequest var1, final ResourcesCompat.FontCallback var2, final Handler var3, boolean var4, int var5, final int var6) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var1.getIdentifier());
      var7.append("-");
      var7.append(var6);
      final String var65 = var7.toString();
      Typeface var8 = (Typeface)sTypefaceCache.get(var65);
      if (var8 != null) {
         if (var2 != null) {
            var2.onFontRetrieved(var8);
         }

         return var8;
      } else if (var4 && var5 == -1) {
         FontsContractCompat.TypefaceResult var62 = getFontInternal(var0, var1, var6);
         if (var2 != null) {
            if (var62.mResult == 0) {
               var2.callbackSuccessAsync(var62.mTypeface, var3);
            } else {
               var2.callbackFailAsync(var62.mResult, var3);
            }
         }

         return var62.mTypeface;
      } else {
         Callable var59 = new Callable() {
            public FontsContractCompat.TypefaceResult call() throws Exception {
               FontsContractCompat.TypefaceResult var1x = FontsContractCompat.getFontInternal(var0, var1, var6);
               if (var1x.mTypeface != null) {
                  FontsContractCompat.sTypefaceCache.put(var65, var1x.mTypeface);
               }

               return var1x;
            }
         };
         if (var4) {
            try {
               Typeface var61 = ((FontsContractCompat.TypefaceResult)sBackgroundThread.postAndWait(var59, var5)).mTypeface;
               return var61;
            } catch (InterruptedException var51) {
               return null;
            }
         } else {
            SelfDestructiveThread.ReplyCallback var58;
            if (var2 == null) {
               var58 = null;
            } else {
               var58 = new SelfDestructiveThread.ReplyCallback() {
                  public void onReply(FontsContractCompat.TypefaceResult var1) {
                     if (var1 == null) {
                        var2.callbackFailAsync(1, var3);
                     } else if (var1.mResult == 0) {
                        var2.callbackSuccessAsync(var1.mTypeface, var3);
                     } else {
                        var2.callbackFailAsync(var1.mResult, var3);
                     }
                  }
               };
            }

            Object var63 = sLock;
            synchronized(var63){}

            Throwable var10000;
            boolean var10001;
            label567: {
               ArrayList var64;
               try {
                  var64 = (ArrayList)sPendingReplies.get(var65);
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label567;
               }

               if (var64 != null) {
                  label556: {
                     if (var58 != null) {
                        try {
                           var64.add(var58);
                        } catch (Throwable var54) {
                           var10000 = var54;
                           var10001 = false;
                           break label556;
                        }
                     }

                     label552:
                     try {
                        return null;
                     } catch (Throwable var53) {
                        var10000 = var53;
                        var10001 = false;
                        break label552;
                     }
                  }
               } else {
                  label577: {
                     if (var58 != null) {
                        try {
                           var64 = new ArrayList();
                           var64.add(var58);
                           sPendingReplies.put(var65, var64);
                        } catch (Throwable var56) {
                           var10000 = var56;
                           var10001 = false;
                           break label577;
                        }
                     }

                     try {
                        ;
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label577;
                     }

                     sBackgroundThread.postAndReply(var59, new SelfDestructiveThread.ReplyCallback() {
                        public void onReply(FontsContractCompat.TypefaceResult var1) {
                           Object var3 = FontsContractCompat.sLock;
                           synchronized(var3){}

                           Throwable var10000;
                           boolean var10001;
                           label222: {
                              ArrayList var4;
                              try {
                                 var4 = (ArrayList)FontsContractCompat.sPendingReplies.get(var65);
                              } catch (Throwable var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label222;
                              }

                              if (var4 == null) {
                                 label215:
                                 try {
                                    return;
                                 } catch (Throwable var22) {
                                    var10000 = var22;
                                    var10001 = false;
                                    break label215;
                                 }
                              } else {
                                 label226: {
                                    try {
                                       FontsContractCompat.sPendingReplies.remove(var65);
                                    } catch (Throwable var23) {
                                       var10000 = var23;
                                       var10001 = false;
                                       break label226;
                                    }

                                    for(int var2 = 0; var2 < var4.size(); ++var2) {
                                       ((SelfDestructiveThread.ReplyCallback)var4.get(var2)).onReply(var1);
                                    }

                                    return;
                                 }
                              }
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
                     });
                     return null;
                  }
               }
            }

            while(true) {
               Throwable var60 = var10000;

               try {
                  throw var60;
               } catch (Throwable var52) {
                  var10000 = var52;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public static ProviderInfo getProvider(PackageManager var0, FontRequest var1, Resources var2) throws NameNotFoundException {
      String var5 = var1.getProviderAuthority();
      ProviderInfo var4 = var0.resolveContentProvider(var5, 0);
      StringBuilder var6;
      if (var4 != null) {
         if (var4.packageName.equals(var1.getProviderPackage())) {
            List var7 = convertToByteArrayList(var0.getPackageInfo(var4.packageName, 64).signatures);
            Collections.sort(var7, sByteArrayComparator);
            List var8 = getCertificates(var1, var2);

            for(int var3 = 0; var3 < var8.size(); ++var3) {
               ArrayList var9 = new ArrayList((Collection)var8.get(var3));
               Collections.sort(var9, sByteArrayComparator);
               if (equalsByteArrayList(var7, var9)) {
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
         throw new NameNotFoundException(var6.toString());
      }
   }

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

   public static void requestFont(Context var0, FontRequest var1, FontsContractCompat.FontRequestCallback var2, Handler var3) {
      requestFontInternal(var0.getApplicationContext(), var1, var2, var3);
   }

   private static void requestFontInternal(final Context var0, final FontRequest var1, final FontsContractCompat.FontRequestCallback var2, Handler var3) {
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

   public static void resetCache() {
      sTypefaceCache.evictAll();
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

      public FontFamilyResult(int var1, FontsContractCompat.FontInfo[] var2) {
         this.mStatusCode = var1;
         this.mFonts = var2;
      }

      public FontsContractCompat.FontInfo[] getFonts() {
         return this.mFonts;
      }

      public int getStatusCode() {
         return this.mStatusCode;
      }
   }

   public static class FontInfo {
      private final boolean mItalic;
      private final int mResultCode;
      private final int mTtcIndex;
      private final Uri mUri;
      private final int mWeight;

      public FontInfo(Uri var1, int var2, int var3, boolean var4, int var5) {
         this.mUri = (Uri)Preconditions.checkNotNull(var1);
         this.mTtcIndex = var2;
         this.mWeight = var3;
         this.mItalic = var4;
         this.mResultCode = var5;
      }

      public int getResultCode() {
         return this.mResultCode;
      }

      public int getTtcIndex() {
         return this.mTtcIndex;
      }

      public Uri getUri() {
         return this.mUri;
      }

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
      public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
      public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
      public static final int RESULT_OK = 0;

      public void onTypefaceRequestFailed(int var1) {
      }

      public void onTypefaceRetrieved(Typeface var1) {
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface FontRequestFailReason {
      }
   }

   private static final class TypefaceResult {
      final int mResult;
      final Typeface mTypeface;

      TypefaceResult(Typeface var1, int var2) {
         this.mTypeface = var1;
         this.mResult = var2;
      }
   }
}
