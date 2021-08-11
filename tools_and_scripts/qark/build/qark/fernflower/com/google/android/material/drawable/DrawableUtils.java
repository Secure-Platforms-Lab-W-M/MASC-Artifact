package com.google.android.material.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableUtils {
   private DrawableUtils() {
   }

   public static AttributeSet parseDrawableXml(Context var0, int var1, CharSequence var2) {
      Object var16;
      label61: {
         XmlPullParserException var19;
         label60: {
            IOException var10000;
            label59: {
               XmlResourceParser var14;
               boolean var10001;
               try {
                  var14 = var0.getResources().getXml(var1);
               } catch (XmlPullParserException var12) {
                  var19 = var12;
                  var10001 = false;
                  break label60;
               } catch (IOException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label59;
               }

               while(true) {
                  int var3;
                  try {
                     var3 = var14.next();
                  } catch (XmlPullParserException var10) {
                     var19 = var10;
                     var10001 = false;
                     break label60;
                  } catch (IOException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }

                  if (var3 == 2 || var3 == 1) {
                     if (var3 == 2) {
                        try {
                           if (TextUtils.equals(var14.getName(), var2)) {
                              return Xml.asAttributeSet(var14);
                           }
                        } catch (XmlPullParserException var6) {
                           var19 = var6;
                           var10001 = false;
                           break label60;
                        } catch (IOException var7) {
                           var10000 = var7;
                           var10001 = false;
                           break;
                        }

                        try {
                           StringBuilder var15 = new StringBuilder();
                           var15.append("Must have a <");
                           var15.append(var2);
                           var15.append("> start tag");
                           throw new XmlPullParserException(var15.toString());
                        } catch (XmlPullParserException var4) {
                           var19 = var4;
                           var10001 = false;
                           break label60;
                        } catch (IOException var5) {
                           var10000 = var5;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           throw new XmlPullParserException("No start tag found");
                        } catch (XmlPullParserException var8) {
                           var19 = var8;
                           var10001 = false;
                           break label60;
                        } catch (IOException var9) {
                           var10000 = var9;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }
            }

            var16 = var10000;
            break label61;
         }

         var16 = var19;
      }

      StringBuilder var17 = new StringBuilder();
      var17.append("Can't load badge resource ID #0x");
      var17.append(Integer.toHexString(var1));
      NotFoundException var18 = new NotFoundException(var17.toString());
      var18.initCause((Throwable)var16);
      throw var18;
   }

   public static PorterDuffColorFilter updateTintFilter(Drawable var0, ColorStateList var1, Mode var2) {
      return var1 != null && var2 != null ? new PorterDuffColorFilter(var1.getColorForState(var0.getState(), 0), var2) : null;
   }
}
