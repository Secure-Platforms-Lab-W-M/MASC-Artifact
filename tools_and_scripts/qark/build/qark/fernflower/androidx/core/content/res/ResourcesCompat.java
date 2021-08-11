package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
   private static final String TAG = "ResourcesCompat";

   private ResourcesCompat() {
   }

   public static int getColor(Resources var0, int var1, Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 23 ? var0.getColor(var1, var2) : var0.getColor(var1);
   }

   public static ColorStateList getColorStateList(Resources var0, int var1, Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 23 ? var0.getColorStateList(var1, var2) : var0.getColorStateList(var1);
   }

   public static Drawable getDrawable(Resources var0, int var1, Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 21 ? var0.getDrawable(var1, var2) : var0.getDrawable(var1);
   }

   public static Drawable getDrawableForDensity(Resources var0, int var1, int var2, Theme var3) throws NotFoundException {
      if (VERSION.SDK_INT >= 21) {
         return var0.getDrawableForDensity(var1, var2, var3);
      } else {
         return VERSION.SDK_INT >= 15 ? var0.getDrawableForDensity(var1, var2) : var0.getDrawable(var1);
      }
   }

   public static float getFloat(Resources var0, int var1) {
      TypedValue var2 = new TypedValue();
      var0.getValue(var1, var2, true);
      if (var2.type == 4) {
         return var2.getFloat();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Resource ID #0x");
         var3.append(Integer.toHexString(var1));
         var3.append(" type #0x");
         var3.append(Integer.toHexString(var2.type));
         var3.append(" is not valid");
         throw new NotFoundException(var3.toString());
      }
   }

   public static Typeface getFont(Context var0, int var1) throws NotFoundException {
      return var0.isRestricted() ? null : loadFont(var0, var1, new TypedValue(), 0, (ResourcesCompat.FontCallback)null, (Handler)null, false);
   }

   public static Typeface getFont(Context var0, int var1, TypedValue var2, int var3, ResourcesCompat.FontCallback var4) throws NotFoundException {
      return var0.isRestricted() ? null : loadFont(var0, var1, var2, var3, var4, (Handler)null, true);
   }

   public static void getFont(Context var0, int var1, ResourcesCompat.FontCallback var2, Handler var3) throws NotFoundException {
      Preconditions.checkNotNull(var2);
      if (var0.isRestricted()) {
         var2.callbackFailAsync(-4, var3);
      } else {
         loadFont(var0, var1, new TypedValue(), 0, var2, var3, false);
      }
   }

   private static Typeface loadFont(Context var0, int var1, TypedValue var2, int var3, ResourcesCompat.FontCallback var4, Handler var5, boolean var6) {
      Resources var7 = var0.getResources();
      var7.getValue(var1, var2, true);
      Typeface var8 = loadFont(var0, var7, var2, var1, var3, var4, var5, var6);
      if (var8 == null) {
         if (var4 != null) {
            return var8;
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("Font resource ID #0x");
            var9.append(Integer.toHexString(var1));
            var9.append(" could not be retrieved.");
            throw new NotFoundException(var9.toString());
         }
      } else {
         return var8;
      }
   }

   private static Typeface loadFont(Context var0, Resources var1, TypedValue var2, int var3, int var4, ResourcesCompat.FontCallback var5, Handler var6, boolean var7) {
      if (var2.string != null) {
         String var30 = var2.string.toString();
         if (!var30.startsWith("res/")) {
            if (var5 != null) {
               var5.callbackFailAsync(-3, var6);
            }

            return null;
         } else {
            Typeface var9 = TypefaceCompat.findFromCache(var1, var3, var4);
            if (var9 != null) {
               if (var5 != null) {
                  var5.callbackSuccessAsync(var9, var6);
               }

               return var9;
            } else {
               label123: {
                  IOException var27;
                  StringBuilder var29;
                  label122: {
                     XmlPullParserException var28;
                     label121: {
                        boolean var8;
                        try {
                           var8 = var30.toLowerCase().endsWith(".xml");
                        } catch (XmlPullParserException var24) {
                           var28 = var24;
                           break label121;
                        } catch (IOException var25) {
                           var27 = var25;
                           break label122;
                        }

                        IOException var10000;
                        boolean var10001;
                        Typeface var31;
                        XmlPullParserException var33;
                        if (var8) {
                           label104: {
                              FontResourcesParserCompat.FamilyResourceEntry var32;
                              try {
                                 var32 = FontResourcesParserCompat.parse(var1.getXml(var3), var1);
                              } catch (XmlPullParserException var16) {
                                 var28 = var16;
                                 break label104;
                              } catch (IOException var17) {
                                 var27 = var17;
                                 break label122;
                              }

                              if (var32 == null) {
                                 label140: {
                                    label131: {
                                       try {
                                          Log.e("ResourcesCompat", "Failed to find font-family tag");
                                       } catch (XmlPullParserException var12) {
                                          var33 = var12;
                                          var10001 = false;
                                          break label140;
                                       } catch (IOException var13) {
                                          var10000 = var13;
                                          var10001 = false;
                                          break label131;
                                       }

                                       if (var5 == null) {
                                          return null;
                                       }

                                       try {
                                          var5.callbackFailAsync(-3, var6);
                                          return null;
                                       } catch (XmlPullParserException var10) {
                                          var33 = var10;
                                          var10001 = false;
                                          break label140;
                                       } catch (IOException var11) {
                                          var10000 = var11;
                                          var10001 = false;
                                       }
                                    }

                                    var27 = var10000;
                                    break label122;
                                 }

                                 var28 = var33;
                              } else {
                                 try {
                                    var31 = TypefaceCompat.createFromResourcesFamilyXml(var0, var32, var1, var3, var4, var5, var6, var7);
                                    return var31;
                                 } catch (XmlPullParserException var14) {
                                    var28 = var14;
                                 } catch (IOException var15) {
                                    var27 = var15;
                                    break label122;
                                 }
                              }
                           }
                        } else {
                           label132: {
                              try {
                                 var31 = TypefaceCompat.createFromResourcesFontFile(var0, var1, var3, var30, var4);
                              } catch (XmlPullParserException var22) {
                                 var28 = var22;
                                 break label132;
                              } catch (IOException var23) {
                                 var27 = var23;
                                 break label122;
                              }

                              if (var5 == null) {
                                 return var31;
                              }

                              label112: {
                                 if (var31 != null) {
                                    try {
                                       var5.callbackSuccessAsync(var31, var6);
                                       return var31;
                                    } catch (XmlPullParserException var18) {
                                       var33 = var18;
                                       var10001 = false;
                                    } catch (IOException var19) {
                                       var10000 = var19;
                                       var10001 = false;
                                       break label112;
                                    }
                                 } else {
                                    try {
                                       var5.callbackFailAsync(-3, var6);
                                       return var31;
                                    } catch (XmlPullParserException var20) {
                                       var33 = var20;
                                       var10001 = false;
                                    } catch (IOException var21) {
                                       var10000 = var21;
                                       var10001 = false;
                                       break label112;
                                    }
                                 }

                                 var28 = var33;
                                 break label132;
                              }

                              var27 = var10000;
                              break label122;
                           }
                        }
                     }

                     var29 = new StringBuilder();
                     var29.append("Failed to parse xml resource ");
                     var29.append(var30);
                     Log.e("ResourcesCompat", var29.toString(), var28);
                     break label123;
                  }

                  var29 = new StringBuilder();
                  var29.append("Failed to read xml resource ");
                  var29.append(var30);
                  Log.e("ResourcesCompat", var29.toString(), var27);
               }

               if (var5 != null) {
                  var5.callbackFailAsync(-3, var6);
               }

               return null;
            }
         }
      } else {
         StringBuilder var26 = new StringBuilder();
         var26.append("Resource \"");
         var26.append(var1.getResourceName(var3));
         var26.append("\" (");
         var26.append(Integer.toHexString(var3));
         var26.append(") is not a Font: ");
         var26.append(var2);
         throw new NotFoundException(var26.toString());
      }
   }

   public abstract static class FontCallback {
      public final void callbackFailAsync(final int var1, Handler var2) {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler(Looper.getMainLooper());
         }

         var3.post(new Runnable() {
            public void run() {
               FontCallback.this.onFontRetrievalFailed(var1);
            }
         });
      }

      public final void callbackSuccessAsync(final Typeface var1, Handler var2) {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler(Looper.getMainLooper());
         }

         var3.post(new Runnable() {
            public void run() {
               FontCallback.this.onFontRetrieved(var1);
            }
         });
      }

      public abstract void onFontRetrievalFailed(int var1);

      public abstract void onFontRetrieved(Typeface var1);
   }
}
