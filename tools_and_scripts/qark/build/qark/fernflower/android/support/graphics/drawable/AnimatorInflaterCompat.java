package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
   private static final boolean DBG_ANIMATOR_INFLATER = false;
   private static final int MAX_NUM_POINTS = 100;
   private static final String TAG = "AnimatorInflater";
   private static final int TOGETHER = 0;
   private static final int VALUE_TYPE_COLOR = 3;
   private static final int VALUE_TYPE_FLOAT = 0;
   private static final int VALUE_TYPE_INT = 1;
   private static final int VALUE_TYPE_PATH = 2;
   private static final int VALUE_TYPE_UNDEFINED = 4;

   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, float var4) throws XmlPullParserException, IOException {
      return createAnimatorFromXml(var0, var1, var2, var3, Xml.asAttributeSet(var3), (AnimatorSet)null, 0, var4);
   }

   private static Animator createAnimatorFromXml(Context var0, Resources var1, Theme var2, XmlPullParser var3, AttributeSet var4, AnimatorSet var5, int var6, float var7) throws XmlPullParserException, IOException {
      int var9 = var3.getDepth();
      Object var11 = null;
      ArrayList var12 = null;

      while(true) {
         int var8 = var3.next();
         if (var8 == 3 && var3.getDepth() <= var9 || var8 == 1) {
            if (var5 != null && var12 != null) {
               Animator[] var15 = new Animator[var12.size()];
               var8 = 0;

               for(Iterator var16 = var12.iterator(); var16.hasNext(); ++var8) {
                  var15[var8] = (Animator)var16.next();
               }

               if (var6 == 0) {
                  var5.playTogether(var15);
                  return (Animator)var11;
               }

               var5.playSequentially(var15);
            }

            return (Animator)var11;
         }

         if (var8 == 2) {
            String var13 = var3.getName();
            boolean var17 = false;
            if (var13.equals("objectAnimator")) {
               var11 = loadObjectAnimator(var0, var1, var2, var4, var7, var3);
            } else if (var13.equals("animator")) {
               var11 = loadAnimator(var0, var1, var2, var4, (ValueAnimator)null, var7, var3);
            } else if (var13.equals("set")) {
               var11 = new AnimatorSet();
               TypedArray var18 = TypedArrayUtils.obtainAttributes(var1, var2, var4, AndroidResources.STYLEABLE_ANIMATOR_SET);
               int var10 = TypedArrayUtils.getNamedInt(var18, var3, "ordering", 0, 0);
               createAnimatorFromXml(var0, var1, var2, var3, var4, (AnimatorSet)var11, var10, var7);
               var18.recycle();
            } else {
               if (!var13.equals("propertyValuesHolder")) {
                  StringBuilder var14 = new StringBuilder();
                  var14.append("Unknown animator name: ");
                  var14.append(var3.getName());
                  throw new RuntimeException(var14.toString());
               }

               PropertyValuesHolder[] var19 = loadValues(var0, var1, var2, var3, Xml.asAttributeSet(var3));
               if (var19 != null && var11 != null && var11 instanceof ValueAnimator) {
                  ((ValueAnimator)var11).setValues(var19);
               }

               var17 = true;
            }

            ArrayList var20 = var12;
            if (var5 != null) {
               var20 = var12;
               if (!var17) {
                  var20 = var12;
                  if (var12 == null) {
                     var20 = new ArrayList();
                  }

                  var20.add(var11);
               }
            }

            var12 = var20;
         }
      }
   }

   private static Keyframe createNewKeyframe(Keyframe var0, float var1) {
      if (var0.getType() == Float.TYPE) {
         return Keyframe.ofFloat(var1);
      } else {
         return var0.getType() == Integer.TYPE ? Keyframe.ofInt(var1) : Keyframe.ofObject(var1);
      }
   }

   private static void distributeKeyframes(Keyframe[] var0, float var1, int var2, int var3) {
      for(var1 /= (float)(var3 - var2 + 2); var2 <= var3; ++var2) {
         var0[var2].setFraction(var0[var2 - 1].getFraction() + var1);
      }

   }

   private static void dumpKeyframes(Object[] var0, String var1) {
      if (var0 != null) {
         if (var0.length != 0) {
            Log.d("AnimatorInflater", var1);
            int var4 = var0.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               Keyframe var7 = (Keyframe)var0[var3];
               StringBuilder var6 = new StringBuilder();
               var6.append("Keyframe ");
               var6.append(var3);
               var6.append(": fraction ");
               float var2 = var7.getFraction();
               String var5 = "null";
               Object var8;
               if (var2 < 0.0F) {
                  var8 = "null";
               } else {
                  var8 = var7.getFraction();
               }

               var6.append(var8);
               var6.append(", ");
               var6.append(", value : ");
               var8 = var5;
               if (var7.hasValue()) {
                  var8 = var7.getValue();
               }

               var6.append(var8);
               Log.d("AnimatorInflater", var6.toString());
            }

         }
      }
   }

   private static PropertyValuesHolder getPVH(TypedArray var0, int var1, int var2, int var3, String var4) {
      TypedValue var12 = var0.peekValue(var2);
      boolean var7;
      if (var12 != null) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var9;
      if (var7) {
         var9 = var12.type;
      } else {
         var9 = 0;
      }

      var12 = var0.peekValue(var3);
      boolean var8;
      if (var12 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var10;
      if (var8) {
         var10 = var12.type;
      } else {
         var10 = 0;
      }

      if (var1 == 4) {
         if ((!var7 || !isColorType(var9)) && (!var8 || !isColorType(var10))) {
            var1 = 0;
         } else {
            var1 = 3;
         }
      }

      boolean var11;
      if (var1 == 0) {
         var11 = true;
      } else {
         var11 = false;
      }

      PropertyValuesHolder var17;
      if (var1 != 2) {
         ArgbEvaluator var20 = null;
         if (var1 == 3) {
            var20 = ArgbEvaluator.getInstance();
         }

         if (var11) {
            float var5;
            if (var7) {
               if (var9 == 5) {
                  var5 = var0.getDimension(var2, 0.0F);
               } else {
                  var5 = var0.getFloat(var2, 0.0F);
               }

               if (var8) {
                  float var6;
                  if (var10 == 5) {
                     var6 = var0.getDimension(var3, 0.0F);
                  } else {
                     var6 = var0.getFloat(var3, 0.0F);
                  }

                  var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var5, var6});
               } else {
                  var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var5});
               }
            } else {
               if (var10 == 5) {
                  var5 = var0.getDimension(var3, 0.0F);
               } else {
                  var5 = var0.getFloat(var3, 0.0F);
               }

               var17 = PropertyValuesHolder.ofFloat(var4, new float[]{var5});
            }
         } else if (var7) {
            if (var9 == 5) {
               var1 = (int)var0.getDimension(var2, 0.0F);
            } else if (isColorType(var9)) {
               var1 = var0.getColor(var2, 0);
            } else {
               var1 = var0.getInt(var2, 0);
            }

            if (var8) {
               if (var10 == 5) {
                  var2 = (int)var0.getDimension(var3, 0.0F);
               } else if (isColorType(var10)) {
                  var2 = var0.getColor(var3, 0);
               } else {
                  var2 = var0.getInt(var3, 0);
               }

               var17 = PropertyValuesHolder.ofInt(var4, new int[]{var1, var2});
            } else {
               var17 = PropertyValuesHolder.ofInt(var4, new int[]{var1});
            }
         } else if (var8) {
            if (var10 == 5) {
               var1 = (int)var0.getDimension(var3, 0.0F);
            } else if (isColorType(var10)) {
               var1 = var0.getColor(var3, 0);
            } else {
               var1 = var0.getInt(var3, 0);
            }

            var17 = PropertyValuesHolder.ofInt(var4, new int[]{var1});
         } else {
            var17 = null;
         }

         if (var17 != null && var20 != null) {
            var17.setEvaluator(var20);
         }

         return var17;
      } else {
         String var19 = var0.getString(var2);
         String var16 = var0.getString(var3);
         PathParser.PathDataNode[] var13 = PathParser.createNodesFromPathData(var19);
         PathParser.PathDataNode[] var14 = PathParser.createNodesFromPathData(var16);
         if (var13 != null || var14 != null) {
            if (var13 != null) {
               AnimatorInflaterCompat.PathDataEvaluator var15 = new AnimatorInflaterCompat.PathDataEvaluator();
               if (var14 != null) {
                  if (!PathParser.canMorph(var13, var14)) {
                     StringBuilder var18 = new StringBuilder();
                     var18.append(" Can't morph from ");
                     var18.append(var19);
                     var18.append(" to ");
                     var18.append(var16);
                     throw new InflateException(var18.toString());
                  }

                  var17 = PropertyValuesHolder.ofObject(var4, var15, new Object[]{var13, var14});
               } else {
                  var17 = PropertyValuesHolder.ofObject(var4, var15, new Object[]{var13});
               }

               return var17;
            }

            if (var14 != null) {
               var17 = PropertyValuesHolder.ofObject(var4, new AnimatorInflaterCompat.PathDataEvaluator(), new Object[]{var14});
               return var17;
            }
         }

         var17 = null;
         return var17;
      }
   }

   private static int inferValueTypeFromValues(TypedArray var0, int var1, int var2) {
      TypedValue var6 = var0.peekValue(var1);
      boolean var5 = true;
      int var4 = 0;
      boolean var8;
      if (var6 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var3;
      if (var8) {
         var3 = var6.type;
      } else {
         var3 = 0;
      }

      TypedValue var7 = var0.peekValue(var2);
      boolean var9;
      if (var7 != null) {
         var9 = var5;
      } else {
         var9 = false;
      }

      if (var9) {
         var4 = var7.type;
      }

      return (!var8 || !isColorType(var3)) && (!var9 || !isColorType(var4)) ? 0 : 3;
   }

   private static int inferValueTypeOfKeyframe(Resources var0, Theme var1, AttributeSet var2, XmlPullParser var3) {
      TypedArray var5 = TypedArrayUtils.obtainAttributes(var0, var1, var2, AndroidResources.STYLEABLE_KEYFRAME);
      boolean var4 = false;
      TypedValue var6 = TypedArrayUtils.peekNamedValue(var5, var3, "value", 0);
      if (var6 != null) {
         var4 = true;
      }

      byte var7;
      if (var4 && isColorType(var6.type)) {
         var7 = 3;
      } else {
         var7 = 0;
      }

      var5.recycle();
      return var7;
   }

   private static boolean isColorType(int var0) {
      return var0 >= 28 && var0 <= 31;
   }

   public static Animator loadAnimator(Context var0, @AnimatorRes int var1) throws NotFoundException {
      return VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator(var0, var1) : loadAnimator(var0, var0.getResources(), var0.getTheme(), var1);
   }

   public static Animator loadAnimator(Context var0, Resources var1, Theme var2, @AnimatorRes int var3) throws NotFoundException {
      return loadAnimator(var0, var1, var2, var3, 1.0F);
   }

   public static Animator loadAnimator(Context param0, Resources param1, Theme param2, @AnimatorRes int param3, float param4) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }

   private static ValueAnimator loadAnimator(Context var0, Resources var1, Theme var2, AttributeSet var3, ValueAnimator var4, float var5, XmlPullParser var6) throws NotFoundException {
      TypedArray var8 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_ANIMATOR);
      TypedArray var10 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
      ValueAnimator var9 = var4;
      if (var4 == null) {
         var9 = new ValueAnimator();
      }

      parseAnimatorFromTypeArray(var9, var8, var10, var5, var6);
      int var7 = TypedArrayUtils.getNamedResourceId(var8, var6, "interpolator", 0, 0);
      if (var7 > 0) {
         var9.setInterpolator(AnimationUtilsCompat.loadInterpolator(var0, var7));
      }

      var8.recycle();
      if (var10 != null) {
         var10.recycle();
      }

      return var9;
   }

   private static Keyframe loadKeyframe(Context var0, Resources var1, Theme var2, AttributeSet var3, int var4, XmlPullParser var5) throws XmlPullParserException, IOException {
      TypedArray var10 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_KEYFRAME);
      Keyframe var9 = null;
      float var6 = TypedArrayUtils.getNamedFloat(var10, var5, "fraction", 3, -1.0F);
      TypedValue var11 = TypedArrayUtils.peekNamedValue(var10, var5, "value", 0);
      boolean var8;
      if (var11 != null) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var7 = var4;
      if (var4 == 4) {
         if (var8 && isColorType(var11.type)) {
            var7 = 3;
         } else {
            var7 = 0;
         }
      }

      if (var8) {
         if (var7 != 0) {
            if (var7 == 1 || var7 == 3) {
               var9 = Keyframe.ofInt(var6, TypedArrayUtils.getNamedInt(var10, var5, "value", 0, 0));
            }
         } else {
            var9 = Keyframe.ofFloat(var6, TypedArrayUtils.getNamedFloat(var10, var5, "value", 0, 0.0F));
         }
      } else if (var7 == 0) {
         var9 = Keyframe.ofFloat(var6);
      } else {
         var9 = Keyframe.ofInt(var6);
      }

      var4 = TypedArrayUtils.getNamedResourceId(var10, var5, "interpolator", 1, 0);
      if (var4 > 0) {
         var9.setInterpolator(AnimationUtilsCompat.loadInterpolator(var0, var4));
      }

      var10.recycle();
      return var9;
   }

   private static ObjectAnimator loadObjectAnimator(Context var0, Resources var1, Theme var2, AttributeSet var3, float var4, XmlPullParser var5) throws NotFoundException {
      ObjectAnimator var6 = new ObjectAnimator();
      loadAnimator(var0, var1, var2, var3, var6, var4, var5);
      return var6;
   }

   private static PropertyValuesHolder loadPvh(Context var0, Resources var1, Theme var2, XmlPullParser var3, String var4, int var5) throws XmlPullParserException, IOException {
      boolean var13 = false;
      ArrayList var14 = null;
      int var7 = var5;

      while(true) {
         var5 = var3.next();
         int var9 = var5;
         if (var5 == 3 || var5 == 1) {
            PropertyValuesHolder var17;
            if (var14 != null) {
               var5 = var14.size();
               int var8 = var5;
               if (var5 > 0) {
                  Keyframe var18 = (Keyframe)var14.get(0);
                  Keyframe var20 = (Keyframe)var14.get(var5 - 1);
                  float var6 = var20.getFraction();
                  var5 = var5;
                  if (var6 < 1.0F) {
                     if (var6 < 0.0F) {
                        var20.setFraction(1.0F);
                        var5 = var8;
                     } else {
                        var14.add(var14.size(), createNewKeyframe(var20, 1.0F));
                        var5 = var8 + 1;
                     }
                  }

                  var6 = var18.getFraction();
                  int var10 = var5;
                  if (var6 != 0.0F) {
                     if (var6 < 0.0F) {
                        var18.setFraction(0.0F);
                        var10 = var5;
                     } else {
                        var14.add(0, createNewKeyframe(var18, 0.0F));
                        var10 = var5 + 1;
                     }
                  }

                  Keyframe[] var19 = new Keyframe[var10];
                  var14.toArray(var19);
                  var8 = 0;

                  for(var5 = var9; var8 < var10; ++var8) {
                     var20 = var19[var8];
                     if (var20.getFraction() < 0.0F) {
                        if (var8 == 0) {
                           var20.setFraction(0.0F);
                        } else if (var8 == var10 - 1) {
                           var20.setFraction(1.0F);
                        } else {
                           int var12 = var8 + 1;
                           int var11 = var8;
                           var9 = var5;

                           for(var5 = var12; var5 < var10 - 1 && var19[var5].getFraction() < 0.0F; var11 = var5++) {
                           }

                           distributeKeyframes(var19, var19[var11 + 1].getFraction() - var19[var8 - 1].getFraction(), var8, var11);
                           var5 = var9;
                        }
                     }
                  }

                  PropertyValuesHolder var21 = PropertyValuesHolder.ofKeyframe(var4, var19);
                  var17 = var21;
                  if (var7 == 3) {
                     var21.setEvaluator(ArgbEvaluator.getInstance());
                     return var21;
                  }

                  return var17;
               }
            }

            var17 = null;
            return var17;
         }

         if (var3.getName().equals("keyframe")) {
            if (var7 == 4) {
               var7 = inferValueTypeOfKeyframe(var1, var2, Xml.asAttributeSet(var3), var3);
            }

            Keyframe var16 = loadKeyframe(var0, var1, var2, Xml.asAttributeSet(var3), var7, var3);
            ArrayList var15 = var14;
            if (var16 != null) {
               var15 = var14;
               if (var14 == null) {
                  var15 = new ArrayList();
               }

               var15.add(var16);
            }

            var3.next();
            var14 = var15;
         }
      }
   }

   private static PropertyValuesHolder[] loadValues(Context var0, Resources var1, Theme var2, XmlPullParser var3, AttributeSet var4) throws XmlPullParserException, IOException {
      ArrayList var7 = null;

      while(true) {
         int var5 = var3.getEventType();
         if (var5 == 3 || var5 == 1) {
            PropertyValuesHolder[] var11 = null;
            if (var7 != null) {
               int var6 = var7.size();
               PropertyValuesHolder[] var12 = new PropertyValuesHolder[var6];
               var5 = 0;

               while(true) {
                  var11 = var12;
                  if (var5 >= var6) {
                     break;
                  }

                  var12[var5] = (PropertyValuesHolder)var7.get(var5);
                  ++var5;
               }
            }

            return var11;
         }

         if (var5 != 2) {
            var3.next();
         } else {
            if (var3.getName().equals("propertyValuesHolder")) {
               TypedArray var10 = TypedArrayUtils.obtainAttributes(var1, var2, var4, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
               String var8 = TypedArrayUtils.getNamedString(var10, var3, "propertyName", 3);
               var5 = TypedArrayUtils.getNamedInt(var10, var3, "valueType", 2, 4);
               PropertyValuesHolder var9 = loadPvh(var0, var1, var2, var3, var8, var5);
               if (var9 == null) {
                  var9 = getPVH(var10, var5, 0, 1, var8);
               }

               ArrayList var13 = var7;
               if (var9 != null) {
                  var13 = var7;
                  if (var7 == null) {
                     var13 = new ArrayList();
                  }

                  var13.add(var9);
               }

               var10.recycle();
               var7 = var13;
            }

            var3.next();
         }
      }
   }

   private static void parseAnimatorFromTypeArray(ValueAnimator var0, TypedArray var1, TypedArray var2, float var3, XmlPullParser var4) {
      long var8 = (long)TypedArrayUtils.getNamedInt(var1, var4, "duration", 1, 300);
      long var10 = (long)TypedArrayUtils.getNamedInt(var1, var4, "startOffset", 2, 0);
      int var6 = TypedArrayUtils.getNamedInt(var1, var4, "valueType", 7, 4);
      int var7 = var6;
      if (TypedArrayUtils.hasAttribute(var4, "valueFrom")) {
         var7 = var6;
         if (TypedArrayUtils.hasAttribute(var4, "valueTo")) {
            int var5 = var6;
            if (var6 == 4) {
               var5 = inferValueTypeFromValues(var1, 5, 6);
            }

            PropertyValuesHolder var12 = getPVH(var1, var5, 5, 6, "");
            var7 = var5;
            if (var12 != null) {
               var0.setValues(new PropertyValuesHolder[]{var12});
               var7 = var5;
            }
         }
      }

      var0.setDuration(var8);
      var0.setStartDelay(var10);
      var0.setRepeatCount(TypedArrayUtils.getNamedInt(var1, var4, "repeatCount", 3, 0));
      var0.setRepeatMode(TypedArrayUtils.getNamedInt(var1, var4, "repeatMode", 4, 1));
      if (var2 != null) {
         setupObjectAnimator(var0, var2, var7, var3, var4);
      }

   }

   private static void setupObjectAnimator(ValueAnimator var0, TypedArray var1, int var2, float var3, XmlPullParser var4) {
      ObjectAnimator var7 = (ObjectAnimator)var0;
      String var5 = TypedArrayUtils.getNamedString(var1, var4, "pathData", 1);
      if (var5 != null) {
         String var6 = TypedArrayUtils.getNamedString(var1, var4, "propertyXName", 2);
         String var9 = TypedArrayUtils.getNamedString(var1, var4, "propertyYName", 3);
         if (var2 != 2 && var2 == 4) {
         }

         if (var6 == null && var9 == null) {
            StringBuilder var8 = new StringBuilder();
            var8.append(var1.getPositionDescription());
            var8.append(" propertyXName or propertyYName is needed for PathData");
            throw new InflateException(var8.toString());
         } else {
            setupPathMotion(PathParser.createPathFromPathData(var5), var7, 0.5F * var3, var6, var9);
         }
      } else {
         var7.setPropertyName(TypedArrayUtils.getNamedString(var1, var4, "propertyName", 0));
      }
   }

   private static void setupPathMotion(Path var0, ObjectAnimator var1, float var2, String var3, String var4) {
      PathMeasure var12 = new PathMeasure(var0, false);
      float var5 = 0.0F;
      ArrayList var11 = new ArrayList();
      var11.add(0.0F);

      do {
         var5 += var12.getLength();
         var11.add(var5);
      } while(var12.nextContour());

      PathMeasure var15 = new PathMeasure(var0, false);
      int var10 = Math.min(100, (int)(var5 / var2) + 1);
      float[] var13 = new float[var10];
      float[] var18 = new float[var10];
      float[] var14 = new float[2];
      float var6 = var5 / (float)(var10 - 1);
      var2 = 0.0F;
      int var8 = 0;

      int var9;
      for(int var7 = 0; var7 < var10; var8 = var9) {
         var15.getPosTan(var2, var14, (float[])null);
         var15.getPosTan(var2, var14, (float[])null);
         var13[var7] = var14[0];
         var18[var7] = var14[1];
         var5 = var2 + var6;
         var2 = var5;
         var9 = var8;
         if (var8 + 1 < var11.size()) {
            var2 = var5;
            var9 = var8;
            if (var5 > (Float)var11.get(var8 + 1)) {
               var2 = var5 - (Float)var11.get(var8 + 1);
               var9 = var8 + 1;
               var15.nextContour();
            }
         }

         ++var7;
      }

      PropertyValuesHolder var16 = null;
      var11 = null;
      if (var3 != null) {
         var16 = PropertyValuesHolder.ofFloat(var3, var13);
      }

      PropertyValuesHolder var17 = var11;
      if (var4 != null) {
         var17 = PropertyValuesHolder.ofFloat(var4, var18);
      }

      if (var16 == null) {
         var1.setValues(new PropertyValuesHolder[]{var17});
      } else if (var17 == null) {
         var1.setValues(new PropertyValuesHolder[]{var16});
      } else {
         var1.setValues(new PropertyValuesHolder[]{var16, var17});
      }
   }

   private static class PathDataEvaluator implements TypeEvaluator {
      private PathParser.PathDataNode[] mNodeArray;

      private PathDataEvaluator() {
      }

      // $FF: synthetic method
      PathDataEvaluator(Object var1) {
         this();
      }

      PathDataEvaluator(PathParser.PathDataNode[] var1) {
         this.mNodeArray = var1;
      }

      public PathParser.PathDataNode[] evaluate(float var1, PathParser.PathDataNode[] var2, PathParser.PathDataNode[] var3) {
         if (!PathParser.canMorph(var2, var3)) {
            IllegalArgumentException var6 = new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            throw var6;
         } else {
            PathParser.PathDataNode[] var5 = this.mNodeArray;
            if (var5 == null || !PathParser.canMorph(var5, var2)) {
               this.mNodeArray = PathParser.deepCopyNodes(var2);
            }

            for(int var4 = 0; var4 < var2.length; ++var4) {
               this.mNodeArray[var4].interpolatePathDataNode(var2[var4], var3[var4], var1);
            }

            return this.mNodeArray;
         }
      }
   }
}
