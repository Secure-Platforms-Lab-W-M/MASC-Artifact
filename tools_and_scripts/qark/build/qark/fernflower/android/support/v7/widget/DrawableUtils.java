package android.support.v7.widget;

import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.Log;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableUtils {
   public static final Rect INSETS_NONE = new Rect();
   private static final String TAG = "DrawableUtils";
   private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
   private static Class sInsetsClazz;

   static {
      if (VERSION.SDK_INT >= 18) {
         try {
            sInsetsClazz = Class.forName("android.graphics.Insets");
            return;
         } catch (ClassNotFoundException var1) {
         }
      }

   }

   private DrawableUtils() {
   }

   public static boolean canSafelyMutateDrawable(@NonNull Drawable var0) {
      if (VERSION.SDK_INT < 15 && var0 instanceof InsetDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 15 && var0 instanceof GradientDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 17 && var0 instanceof LayerDrawable) {
         return false;
      } else {
         if (var0 instanceof DrawableContainer) {
            ConstantState var3 = var0.getConstantState();
            if (var3 instanceof DrawableContainerState) {
               Drawable[] var4 = ((DrawableContainerState)var3).getChildren();
               int var2 = var4.length;

               for(int var1 = 0; var1 < var2; ++var1) {
                  if (!canSafelyMutateDrawable(var4[var1])) {
                     return false;
                  }
               }
            }
         } else {
            if (var0 instanceof DrawableWrapper) {
               return canSafelyMutateDrawable(((DrawableWrapper)var0).getWrappedDrawable());
            }

            if (var0 instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
               return canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper)var0).getWrappedDrawable());
            }

            if (var0 instanceof ScaleDrawable) {
               return canSafelyMutateDrawable(((ScaleDrawable)var0).getDrawable());
            }
         }

         return true;
      }
   }

   static void fixDrawable(@NonNull Drawable var0) {
      if (VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         fixVectorDrawableTinting(var0);
      }

   }

   private static void fixVectorDrawableTinting(Drawable var0) {
      int[] var1 = var0.getState();
      if (var1 != null && var1.length != 0) {
         var0.setState(ThemeUtils.EMPTY_STATE_SET);
      } else {
         var0.setState(ThemeUtils.CHECKED_STATE_SET);
      }

      var0.setState(var1);
   }

   public static Rect getOpticalBounds(Drawable var0) {
      if (sInsetsClazz != null) {
         label134: {
            boolean var10001;
            Object var20;
            try {
               var0 = DrawableCompat.unwrap(var0);
               var20 = var0.getClass().getMethod("getOpticalInsets").invoke(var0);
            } catch (Exception var19) {
               var10001 = false;
               break label134;
            }

            if (var20 == null) {
               return INSETS_NONE;
            }

            int var3;
            Rect var4;
            Field[] var5;
            try {
               var4 = new Rect();
               var5 = sInsetsClazz.getFields();
               var3 = var5.length;
            } catch (Exception var18) {
               var10001 = false;
               break label134;
            }

            int var2 = 0;

            while(true) {
               if (var2 >= var3) {
                  return var4;
               }

               Field var6 = var5[var2];

               String var7;
               try {
                  var7 = var6.getName();
               } catch (Exception var12) {
                  var10001 = false;
                  break;
               }

               byte var1 = -1;

               label136: {
                  label137: {
                     label138: {
                        label139: {
                           try {
                              switch(var7.hashCode()) {
                              case -1383228885:
                                 break label138;
                              case 115029:
                                 break label139;
                              case 3317767:
                                 break;
                              case 108511772:
                                 break label137;
                              default:
                                 break label136;
                              }
                           } catch (Exception var17) {
                              var10001 = false;
                              break;
                           }

                           try {
                              if (!var7.equals("left")) {
                                 break label136;
                              }
                           } catch (Exception var15) {
                              var10001 = false;
                              break;
                           }

                           var1 = 0;
                           break label136;
                        }

                        try {
                           if (!var7.equals("top")) {
                              break label136;
                           }
                        } catch (Exception var14) {
                           var10001 = false;
                           break;
                        }

                        var1 = 1;
                        break label136;
                     }

                     try {
                        if (!var7.equals("bottom")) {
                           break label136;
                        }
                     } catch (Exception var13) {
                        var10001 = false;
                        break;
                     }

                     var1 = 3;
                     break label136;
                  }

                  try {
                     if (!var7.equals("right")) {
                        break label136;
                     }
                  } catch (Exception var16) {
                     var10001 = false;
                     break;
                  }

                  var1 = 2;
               }

               if (var1 != 0) {
                  if (var1 != 1) {
                     if (var1 != 2) {
                        if (var1 == 3) {
                           try {
                              var4.bottom = var6.getInt(var20);
                           } catch (Exception var11) {
                              var10001 = false;
                              break;
                           }
                        }
                     } else {
                        try {
                           var4.right = var6.getInt(var20);
                        } catch (Exception var10) {
                           var10001 = false;
                           break;
                        }
                     }
                  } else {
                     try {
                        var4.top = var6.getInt(var20);
                     } catch (Exception var9) {
                        var10001 = false;
                        break;
                     }
                  }
               } else {
                  try {
                     var4.left = var6.getInt(var20);
                  } catch (Exception var8) {
                     var10001 = false;
                     break;
                  }
               }

               ++var2;
            }
         }

         Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
      }

      return INSETS_NONE;
   }

   public static Mode parseTintMode(int var0, Mode var1) {
      if (var0 != 3) {
         if (var0 != 5) {
            if (var0 != 9) {
               switch(var0) {
               case 14:
                  return Mode.MULTIPLY;
               case 15:
                  return Mode.SCREEN;
               case 16:
                  if (VERSION.SDK_INT >= 11) {
                     return Mode.valueOf("ADD");
                  }

                  return var1;
               default:
                  return var1;
               }
            } else {
               return Mode.SRC_ATOP;
            }
         } else {
            return Mode.SRC_IN;
         }
      } else {
         return Mode.SRC_OVER;
      }
   }
}
