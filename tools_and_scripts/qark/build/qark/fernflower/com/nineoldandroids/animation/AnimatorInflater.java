package com.nineoldandroids.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.animation.AnimationUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflater {
   private static final int[] Animator = new int[]{16843073, 16843160, 16843198, 16843199, 16843200, 16843486, 16843487, 16843488};
   private static final int[] AnimatorSet = new int[]{16843490};
   private static final int AnimatorSet_ordering = 0;
   private static final int Animator_duration = 1;
   private static final int Animator_interpolator = 0;
   private static final int Animator_repeatCount = 3;
   private static final int Animator_repeatMode = 4;
   private static final int Animator_startOffset = 2;
   private static final int Animator_valueFrom = 5;
   private static final int Animator_valueTo = 6;
   private static final int Animator_valueType = 7;
   private static final int[] PropertyAnimator = new int[]{16843489};
   private static final int PropertyAnimator_propertyName = 0;
   private static final int TOGETHER = 0;
   private static final int VALUE_TYPE_FLOAT = 0;

   private static Animator createAnimatorFromXml(Context var0, XmlPullParser var1) throws XmlPullParserException, IOException {
      return createAnimatorFromXml(var0, var1, Xml.asAttributeSet(var1), (AnimatorSet)null, 0);
   }

   private static Animator createAnimatorFromXml(Context var0, XmlPullParser var1, AttributeSet var2, AnimatorSet var3, int var4) throws XmlPullParserException, IOException {
      Object var7 = null;
      ArrayList var8 = null;
      int var6 = var1.getDepth();

      while(true) {
         int var5 = var1.next();
         if (var5 == 3 && var1.getDepth() <= var6 || var5 == 1) {
            if (var3 != null && var8 != null) {
               Animator[] var12 = new Animator[var8.size()];
               var5 = 0;

               for(Iterator var13 = var8.iterator(); var13.hasNext(); ++var5) {
                  var12[var5] = (Animator)var13.next();
               }

               if (var4 == 0) {
                  var3.playTogether(var12);
                  return (Animator)var7;
               }

               var3.playSequentially(var12);
            }

            return (Animator)var7;
         }

         if (var5 == 2) {
            String var14 = var1.getName();
            if (var14.equals("objectAnimator")) {
               var7 = loadObjectAnimator(var0, var2);
            } else if (var14.equals("animator")) {
               var7 = loadAnimator(var0, var2, (ValueAnimator)null);
            } else {
               if (!var14.equals("set")) {
                  StringBuilder var11 = new StringBuilder();
                  var11.append("Unknown animator name: ");
                  var11.append(var1.getName());
                  throw new RuntimeException(var11.toString());
               }

               var7 = new AnimatorSet();
               TypedArray var9 = var0.obtainStyledAttributes(var2, AnimatorSet);
               TypedValue var10 = new TypedValue();
               var5 = 0;
               var9.getValue(0, var10);
               if (var10.type == 16) {
                  var5 = var10.data;
               }

               createAnimatorFromXml(var0, var1, var2, (AnimatorSet)var7, var5);
               var9.recycle();
            }

            ArrayList var15 = var8;
            if (var3 != null) {
               var15 = var8;
               if (var8 == null) {
                  var15 = new ArrayList();
               }

               var15.add(var7);
            }

            var8 = var15;
         }
      }
   }

   public static Animator loadAnimator(Context param0, int param1) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }

   private static ValueAnimator loadAnimator(Context var0, AttributeSet var1, ValueAnimator var2) throws NotFoundException {
      TypedArray var15 = var0.obtainStyledAttributes(var1, Animator);
      long var11 = (long)var15.getInt(1, 0);
      long var13 = (long)var15.getInt(2, 0);
      int var5 = var15.getInt(7, 0);
      ValueAnimator var16;
      if (var2 == null) {
         var16 = new ValueAnimator();
      } else {
         var16 = var2;
      }

      boolean var18;
      if (var5 == 0) {
         var18 = true;
      } else {
         var18 = false;
      }

      TypedValue var17 = var15.peekValue(5);
      boolean var6;
      if (var17 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var8;
      if (var6) {
         var8 = var17.type;
      } else {
         var8 = 0;
      }

      var17 = var15.peekValue(6);
      boolean var7;
      if (var17 != null) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var9;
      if (var7) {
         var9 = var17.type;
      } else {
         var9 = 0;
      }

      boolean var10;
      label153: {
         if (!var6 || var8 < 28 || var8 > 31) {
            var10 = var18;
            if (!var7) {
               break label153;
            }

            var10 = var18;
            if (var9 < 28) {
               break label153;
            }

            var10 = var18;
            if (var9 > 31) {
               break label153;
            }
         }

         var10 = false;
         var16.setEvaluator(new ArgbEvaluator());
      }

      if (var10) {
         float var3;
         if (var6) {
            if (var8 == 5) {
               var3 = var15.getDimension(5, 0.0F);
            } else {
               var3 = var15.getFloat(5, 0.0F);
            }

            if (var7) {
               float var4;
               if (var9 == 5) {
                  var4 = var15.getDimension(6, 0.0F);
               } else {
                  var4 = var15.getFloat(6, 0.0F);
               }

               var16.setFloatValues(var3, var4);
            } else {
               var16.setFloatValues(var3);
            }
         } else {
            if (var9 == 5) {
               var3 = var15.getDimension(6, 0.0F);
            } else {
               var3 = var15.getFloat(6, 0.0F);
            }

            var16.setFloatValues(var3);
         }
      } else if (var6) {
         if (var8 == 5) {
            var5 = (int)var15.getDimension(5, 0.0F);
         } else if (var8 >= 28 && var8 <= 31) {
            var5 = var15.getColor(5, 0);
         } else {
            var5 = var15.getInt(5, 0);
         }

         if (var7) {
            int var19;
            if (var9 == 5) {
               var19 = (int)var15.getDimension(6, 0.0F);
            } else if (var9 >= 28 && var9 <= 31) {
               var19 = var15.getColor(6, 0);
            } else {
               var19 = var15.getInt(6, 0);
            }

            var16.setIntValues(var5, var19);
         } else {
            var16.setIntValues(var5);
         }
      } else if (var7) {
         if (var9 == 5) {
            var5 = (int)var15.getDimension(6, 0.0F);
         } else if (var9 >= 28 && var9 <= 31) {
            var5 = var15.getColor(6, 0);
         } else {
            var5 = var15.getInt(6, 0);
         }

         var16.setIntValues(var5);
      }

      var16.setDuration(var11);
      var16.setStartDelay(var13);
      if (var15.hasValue(3)) {
         var16.setRepeatCount(var15.getInt(3, 0));
      }

      if (var15.hasValue(4)) {
         var16.setRepeatMode(var15.getInt(4, 1));
      }

      var5 = var15.getResourceId(0, 0);
      if (var5 > 0) {
         var16.setInterpolator(AnimationUtils.loadInterpolator(var0, var5));
      }

      var15.recycle();
      return var16;
   }

   private static ObjectAnimator loadObjectAnimator(Context var0, AttributeSet var1) throws NotFoundException {
      ObjectAnimator var2 = new ObjectAnimator();
      loadAnimator(var0, var1, var2);
      TypedArray var3 = var0.obtainStyledAttributes(var1, PropertyAnimator);
      var2.setPropertyName(var3.getString(0));
      var3.recycle();
      return var2;
   }
}
