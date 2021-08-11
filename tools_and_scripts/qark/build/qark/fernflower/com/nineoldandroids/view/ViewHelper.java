package com.nineoldandroids.view;

import android.view.View;
import com.nineoldandroids.view.animation.AnimatorProxy;

public final class ViewHelper {
   private ViewHelper() {
   }

   public static float getAlpha(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getAlpha() : ViewHelper.Honeycomb.getAlpha(var0);
   }

   public static float getPivotX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getPivotX() : ViewHelper.Honeycomb.getPivotX(var0);
   }

   public static float getPivotY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getPivotY() : ViewHelper.Honeycomb.getPivotY(var0);
   }

   public static float getRotation(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getRotation() : ViewHelper.Honeycomb.getRotation(var0);
   }

   public static float getRotationX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getRotationX() : ViewHelper.Honeycomb.getRotationX(var0);
   }

   public static float getRotationY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getRotationY() : ViewHelper.Honeycomb.getRotationY(var0);
   }

   public static float getScaleX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getScaleX() : ViewHelper.Honeycomb.getScaleX(var0);
   }

   public static float getScaleY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getScaleY() : ViewHelper.Honeycomb.getScaleY(var0);
   }

   public static float getScrollX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? (float)AnimatorProxy.wrap(var0).getScrollX() : ViewHelper.Honeycomb.getScrollX(var0);
   }

   public static float getScrollY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? (float)AnimatorProxy.wrap(var0).getScrollY() : ViewHelper.Honeycomb.getScrollY(var0);
   }

   public static float getTranslationX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getTranslationX() : ViewHelper.Honeycomb.getTranslationX(var0);
   }

   public static float getTranslationY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getTranslationY() : ViewHelper.Honeycomb.getTranslationY(var0);
   }

   public static float getX(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getX() : ViewHelper.Honeycomb.getX(var0);
   }

   public static float getY(View var0) {
      return AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(var0).getY() : ViewHelper.Honeycomb.getY(var0);
   }

   public static void setAlpha(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setAlpha(var1);
      } else {
         ViewHelper.Honeycomb.setAlpha(var0, var1);
      }
   }

   public static void setPivotX(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setPivotX(var1);
      } else {
         ViewHelper.Honeycomb.setPivotX(var0, var1);
      }
   }

   public static void setPivotY(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setPivotY(var1);
      } else {
         ViewHelper.Honeycomb.setPivotY(var0, var1);
      }
   }

   public static void setRotation(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setRotation(var1);
      } else {
         ViewHelper.Honeycomb.setRotation(var0, var1);
      }
   }

   public static void setRotationX(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setRotationX(var1);
      } else {
         ViewHelper.Honeycomb.setRotationX(var0, var1);
      }
   }

   public static void setRotationY(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setRotationY(var1);
      } else {
         ViewHelper.Honeycomb.setRotationY(var0, var1);
      }
   }

   public static void setScaleX(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setScaleX(var1);
      } else {
         ViewHelper.Honeycomb.setScaleX(var0, var1);
      }
   }

   public static void setScaleY(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setScaleY(var1);
      } else {
         ViewHelper.Honeycomb.setScaleY(var0, var1);
      }
   }

   public static void setScrollX(View var0, int var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setScrollX(var1);
      } else {
         ViewHelper.Honeycomb.setScrollX(var0, var1);
      }
   }

   public static void setScrollY(View var0, int var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setScrollY(var1);
      } else {
         ViewHelper.Honeycomb.setScrollY(var0, var1);
      }
   }

   public static void setTranslationX(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setTranslationX(var1);
      } else {
         ViewHelper.Honeycomb.setTranslationX(var0, var1);
      }
   }

   public static void setTranslationY(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setTranslationY(var1);
      } else {
         ViewHelper.Honeycomb.setTranslationY(var0, var1);
      }
   }

   public static void setX(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setX(var1);
      } else {
         ViewHelper.Honeycomb.setX(var0, var1);
      }
   }

   public static void setY(View var0, float var1) {
      if (AnimatorProxy.NEEDS_PROXY) {
         AnimatorProxy.wrap(var0).setY(var1);
      } else {
         ViewHelper.Honeycomb.setY(var0, var1);
      }
   }

   private static final class Honeycomb {
      static float getAlpha(View var0) {
         return var0.getAlpha();
      }

      static float getPivotX(View var0) {
         return var0.getPivotX();
      }

      static float getPivotY(View var0) {
         return var0.getPivotY();
      }

      static float getRotation(View var0) {
         return var0.getRotation();
      }

      static float getRotationX(View var0) {
         return var0.getRotationX();
      }

      static float getRotationY(View var0) {
         return var0.getRotationY();
      }

      static float getScaleX(View var0) {
         return var0.getScaleX();
      }

      static float getScaleY(View var0) {
         return var0.getScaleY();
      }

      static float getScrollX(View var0) {
         return (float)var0.getScrollX();
      }

      static float getScrollY(View var0) {
         return (float)var0.getScrollY();
      }

      static float getTranslationX(View var0) {
         return var0.getTranslationX();
      }

      static float getTranslationY(View var0) {
         return var0.getTranslationY();
      }

      static float getX(View var0) {
         return var0.getX();
      }

      static float getY(View var0) {
         return var0.getY();
      }

      static void setAlpha(View var0, float var1) {
         var0.setAlpha(var1);
      }

      static void setPivotX(View var0, float var1) {
         var0.setPivotX(var1);
      }

      static void setPivotY(View var0, float var1) {
         var0.setPivotY(var1);
      }

      static void setRotation(View var0, float var1) {
         var0.setRotation(var1);
      }

      static void setRotationX(View var0, float var1) {
         var0.setRotationX(var1);
      }

      static void setRotationY(View var0, float var1) {
         var0.setRotationY(var1);
      }

      static void setScaleX(View var0, float var1) {
         var0.setScaleX(var1);
      }

      static void setScaleY(View var0, float var1) {
         var0.setScaleY(var1);
      }

      static void setScrollX(View var0, int var1) {
         var0.setScrollX(var1);
      }

      static void setScrollY(View var0, int var1) {
         var0.setScrollY(var1);
      }

      static void setTranslationX(View var0, float var1) {
         var0.setTranslationX(var1);
      }

      static void setTranslationY(View var0, float var1) {
         var0.setTranslationY(var1);
      }

      static void setX(View var0, float var1) {
         var0.setX(var1);
      }

      static void setY(View var0, float var1) {
         var0.setY(var1);
      }
   }
}
