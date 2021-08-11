package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

@TargetApi(14)
@RequiresApi(14)
class ChangeBoundsPort extends TransitionPort {
   private static final String LOG_TAG = "ChangeBounds";
   private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
   private static final String PROPNAME_PARENT = "android:changeBounds:parent";
   private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
   private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
   private static RectEvaluator sRectEvaluator = new RectEvaluator();
   private static final String[] sTransitionProperties = new String[]{"android:changeBounds:bounds", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
   boolean mReparent = false;
   boolean mResizeClip = false;
   int[] tempLocation = new int[2];

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      var1.values.put("android:changeBounds:bounds", new Rect(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom()));
      var1.values.put("android:changeBounds:parent", var1.view.getParent());
      var1.view.getLocationInWindow(this.tempLocation);
      var1.values.put("android:changeBounds:windowX", this.tempLocation[0]);
      var1.values.put("android:changeBounds:windowY", this.tempLocation[1]);
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(final ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      ObjectAnimator var24;
      if (var2 != null && var3 != null) {
         Map var22 = var2.values;
         Map var21 = var3.values;
         ViewGroup var37 = (ViewGroup)var22.get("android:changeBounds:parent");
         ViewGroup var23 = (ViewGroup)var21.get("android:changeBounds:parent");
         if (var37 != null && var23 != null) {
            final View var36 = var3.view;
            boolean var6;
            if (var37 != var23 && var37.getId() != var23.getId()) {
               var6 = false;
            } else {
               var6 = true;
            }

            int var7;
            int var9;
            int var34;
            int var35;
            if (this.mReparent && !var6) {
               var34 = (Integer)var2.values.get("android:changeBounds:windowX");
               var7 = (Integer)var2.values.get("android:changeBounds:windowY");
               var35 = (Integer)var3.values.get("android:changeBounds:windowX");
               var9 = (Integer)var3.values.get("android:changeBounds:windowY");
               if (var34 != var35 || var7 != var9) {
                  var1.getLocationInWindow(this.tempLocation);
                  Bitmap var30 = Bitmap.createBitmap(var36.getWidth(), var36.getHeight(), Config.ARGB_8888);
                  var36.draw(new Canvas(var30));
                  final BitmapDrawable var32 = new BitmapDrawable(var30);
                  var36.setVisibility(4);
                  ViewOverlay.createFrom(var1).add(var32);
                  Rect var31 = new Rect(var34 - this.tempLocation[0], var7 - this.tempLocation[1], var34 - this.tempLocation[0] + var36.getWidth(), var7 - this.tempLocation[1] + var36.getHeight());
                  Rect var38 = new Rect(var35 - this.tempLocation[0], var9 - this.tempLocation[1], var35 - this.tempLocation[0] + var36.getWidth(), var9 - this.tempLocation[1] + var36.getHeight());
                  ObjectAnimator var33 = ObjectAnimator.ofObject(var32, "bounds", sRectEvaluator, new Object[]{var31, var38});
                  var33.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        ViewOverlay.createFrom(var1).remove(var32);
                        var36.setVisibility(0);
                     }
                  });
                  return var33;
               }
            } else {
               Rect var25 = (Rect)var2.values.get("android:changeBounds:bounds");
               Rect var27 = (Rect)var3.values.get("android:changeBounds:bounds");
               int var19 = var25.left;
               int var20 = var27.left;
               int var17 = var25.top;
               int var18 = var27.top;
               int var15 = var25.right;
               int var16 = var27.right;
               int var13 = var25.bottom;
               int var14 = var27.bottom;
               var9 = var15 - var19;
               int var10 = var13 - var17;
               int var11 = var16 - var20;
               int var12 = var14 - var18;
               byte var8 = 0;
               var7 = 0;
               var34 = var8;
               if (var9 != 0) {
                  var34 = var8;
                  if (var10 != 0) {
                     var34 = var8;
                     if (var11 != 0) {
                        var34 = var8;
                        if (var12 != 0) {
                           if (var19 != var20) {
                              var7 = 0 + 1;
                           }

                           var34 = var7;
                           if (var17 != var18) {
                              var34 = var7 + 1;
                           }

                           var7 = var34;
                           if (var15 != var16) {
                              var7 = var34 + 1;
                           }

                           var34 = var7;
                           if (var13 != var14) {
                              var34 = var7 + 1;
                           }
                        }
                     }
                  }
               }

               if (var34 > 0) {
                  PropertyValuesHolder[] var26;
                  if (this.mResizeClip) {
                     if (var9 != var11) {
                        var36.setRight(Math.max(var9, var11) + var20);
                     }

                     if (var10 != var12) {
                        var36.setBottom(Math.max(var10, var12) + var18);
                     }

                     if (var19 != var20) {
                        var36.setTranslationX((float)(var19 - var20));
                     }

                     if (var17 != var18) {
                        var36.setTranslationY((float)(var17 - var18));
                     }

                     float var4 = (float)(var20 - var19);
                     float var5 = (float)(var18 - var17);
                     var35 = var11 - var9;
                     var13 = var12 - var10;
                     var7 = 0;
                     if (var4 != 0.0F) {
                        var7 = 0 + 1;
                     }

                     var34 = var7;
                     if (var5 != 0.0F) {
                        var34 = var7 + 1;
                     }

                     label127: {
                        if (var35 == 0) {
                           var7 = var34;
                           if (var13 == 0) {
                              break label127;
                           }
                        }

                        var7 = var34 + 1;
                     }

                     var26 = new PropertyValuesHolder[var7];
                     if (var4 != 0.0F) {
                        var34 = 0 + 1;
                        var26[0] = PropertyValuesHolder.ofFloat("translationX", new float[]{var36.getTranslationX(), 0.0F});
                     } else {
                        var34 = 0;
                     }

                     if (var5 != 0.0F) {
                        var26[var34] = PropertyValuesHolder.ofFloat("translationY", new float[]{var36.getTranslationY(), 0.0F});
                     }

                     if (var35 != 0 || var13 != 0) {
                        new Rect(0, 0, var9, var10);
                        new Rect(0, 0, var11, var12);
                     }

                     var24 = ObjectAnimator.ofPropertyValuesHolder(var36, var26);
                     if (var36.getParent() instanceof ViewGroup) {
                        ViewGroup var29 = (ViewGroup)var36.getParent();
                        this.addListener(new TransitionPort.TransitionListenerAdapter() {
                           boolean mCanceled = false;

                           public void onTransitionCancel(TransitionPort var1) {
                              this.mCanceled = true;
                           }

                           public void onTransitionEnd(TransitionPort var1) {
                              if (!this.mCanceled) {
                              }

                           }

                           public void onTransitionPause(TransitionPort var1) {
                           }

                           public void onTransitionResume(TransitionPort var1) {
                           }
                        });
                     }

                     var24.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                        }
                     });
                     return var24;
                  }

                  var26 = new PropertyValuesHolder[var34];
                  if (var19 != var20) {
                     var36.setLeft(var19);
                  }

                  if (var17 != var18) {
                     var36.setTop(var17);
                  }

                  if (var15 != var16) {
                     var36.setRight(var15);
                  }

                  if (var13 != var14) {
                     var36.setBottom(var13);
                  }

                  if (var19 != var20) {
                     var7 = 0 + 1;
                     var26[0] = PropertyValuesHolder.ofInt("left", new int[]{var19, var20});
                  } else {
                     var7 = 0;
                  }

                  var34 = var7;
                  if (var17 != var18) {
                     var26[var7] = PropertyValuesHolder.ofInt("top", new int[]{var17, var18});
                     var34 = var7 + 1;
                  }

                  var7 = var34;
                  if (var15 != var16) {
                     var26[var34] = PropertyValuesHolder.ofInt("right", new int[]{var15, var16});
                     var7 = var34 + 1;
                  }

                  if (var13 != var14) {
                     var26[var7] = PropertyValuesHolder.ofInt("bottom", new int[]{var13, var14});
                  }

                  ObjectAnimator var28 = ObjectAnimator.ofPropertyValuesHolder(var36, var26);
                  var24 = var28;
                  if (var36.getParent() instanceof ViewGroup) {
                     var1 = (ViewGroup)var36.getParent();
                     this.addListener(new TransitionPort.TransitionListenerAdapter() {
                        boolean mCanceled = false;

                        public void onTransitionCancel(TransitionPort var1) {
                           this.mCanceled = true;
                        }

                        public void onTransitionEnd(TransitionPort var1) {
                           if (!this.mCanceled) {
                           }

                        }

                        public void onTransitionPause(TransitionPort var1) {
                        }

                        public void onTransitionResume(TransitionPort var1) {
                        }
                     });
                     return var28;
                  }

                  return var24;
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         var24 = null;
         return var24;
      }
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   public void setReparent(boolean var1) {
      this.mReparent = var1;
   }

   public void setResizeClip(boolean var1) {
      this.mResizeClip = var1;
   }
}
