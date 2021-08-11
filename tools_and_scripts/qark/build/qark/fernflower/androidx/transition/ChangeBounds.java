package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.util.Map;

public class ChangeBounds extends Transition {
   private static final Property BOTTOM_RIGHT_ONLY_PROPERTY = new Property(PointF.class, "bottomRight") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         ViewUtils.setLeftTopRightBottom(var1, var1.getLeft(), var1.getTop(), Math.round(var2.x), Math.round(var2.y));
      }
   };
   private static final Property BOTTOM_RIGHT_PROPERTY = new Property(PointF.class, "bottomRight") {
      public PointF get(ChangeBounds.ViewBounds var1) {
         return null;
      }

      public void set(ChangeBounds.ViewBounds var1, PointF var2) {
         var1.setBottomRight(var2);
      }
   };
   private static final Property DRAWABLE_ORIGIN_PROPERTY = new Property(PointF.class, "boundsOrigin") {
      private Rect mBounds = new Rect();

      public PointF get(Drawable var1) {
         var1.copyBounds(this.mBounds);
         return new PointF((float)this.mBounds.left, (float)this.mBounds.top);
      }

      public void set(Drawable var1, PointF var2) {
         var1.copyBounds(this.mBounds);
         this.mBounds.offsetTo(Math.round(var2.x), Math.round(var2.y));
         var1.setBounds(this.mBounds);
      }
   };
   private static final Property POSITION_PROPERTY = new Property(PointF.class, "position") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         int var3 = Math.round(var2.x);
         int var4 = Math.round(var2.y);
         ViewUtils.setLeftTopRightBottom(var1, var3, var4, var1.getWidth() + var3, var1.getHeight() + var4);
      }
   };
   private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
   private static final String PROPNAME_CLIP = "android:changeBounds:clip";
   private static final String PROPNAME_PARENT = "android:changeBounds:parent";
   private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
   private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
   private static final Property TOP_LEFT_ONLY_PROPERTY = new Property(PointF.class, "topLeft") {
      public PointF get(View var1) {
         return null;
      }

      public void set(View var1, PointF var2) {
         ViewUtils.setLeftTopRightBottom(var1, Math.round(var2.x), Math.round(var2.y), var1.getRight(), var1.getBottom());
      }
   };
   private static final Property TOP_LEFT_PROPERTY = new Property(PointF.class, "topLeft") {
      public PointF get(ChangeBounds.ViewBounds var1) {
         return null;
      }

      public void set(ChangeBounds.ViewBounds var1, PointF var2) {
         var1.setTopLeft(var2);
      }
   };
   private static RectEvaluator sRectEvaluator = new RectEvaluator();
   private static final String[] sTransitionProperties = new String[]{"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
   private boolean mReparent = false;
   private boolean mResizeClip = false;
   private int[] mTempLocation = new int[2];

   public ChangeBounds() {
   }

   public ChangeBounds(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var4 = var1.obtainStyledAttributes(var2, Styleable.CHANGE_BOUNDS);
      boolean var3 = TypedArrayUtils.getNamedBoolean(var4, (XmlResourceParser)var2, "resizeClip", 0, false);
      var4.recycle();
      this.setResizeClip(var3);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      if (ViewCompat.isLaidOut(var2) || var2.getWidth() != 0 || var2.getHeight() != 0) {
         var1.values.put("android:changeBounds:bounds", new Rect(var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom()));
         var1.values.put("android:changeBounds:parent", var1.view.getParent());
         if (this.mReparent) {
            var1.view.getLocationInWindow(this.mTempLocation);
            var1.values.put("android:changeBounds:windowX", this.mTempLocation[0]);
            var1.values.put("android:changeBounds:windowY", this.mTempLocation[1]);
         }

         if (this.mResizeClip) {
            var1.values.put("android:changeBounds:clip", ViewCompat.getClipBounds(var2));
         }
      }

   }

   private boolean parentMatches(View var1, View var2) {
      boolean var3 = true;
      if (this.mReparent) {
         boolean var4 = true;
         var3 = true;
         TransitionValues var5 = this.getMatchedTransitionValues(var1, true);
         if (var5 == null) {
            if (var1 != var2) {
               var3 = false;
            }

            return var3;
         }

         if (var2 == var5.view) {
            var3 = var4;
         } else {
            var3 = false;
         }
      }

      return var3;
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(final ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      if (var2 != null) {
         if (var3 == null) {
            return null;
         } else {
            Map var21 = var2.values;
            Map var20 = var3.values;
            ViewGroup var40 = (ViewGroup)var21.get("android:changeBounds:parent");
            ViewGroup var22 = (ViewGroup)var20.get("android:changeBounds:parent");
            if (var40 != null && var22 != null) {
               final View var39 = var3.view;
               int var5;
               int var6;
               int var8;
               ObjectAnimator var33;
               if (this.parentMatches(var40, var22)) {
                  final int var9;
                  int var10;
                  final int var11;
                  int var12;
                  final int var13;
                  int var14;
                  final int var15;
                  int var16;
                  int var17;
                  int var18;
                  int var19;
                  Rect var29;
                  final Rect var42;
                  label143: {
                     Rect var24 = (Rect)var2.values.get("android:changeBounds:bounds");
                     var42 = (Rect)var3.values.get("android:changeBounds:bounds");
                     var8 = var24.left;
                     var9 = var42.left;
                     var10 = var24.top;
                     var11 = var42.top;
                     var12 = var24.right;
                     var13 = var42.right;
                     var14 = var24.bottom;
                     var15 = var42.bottom;
                     var16 = var12 - var8;
                     var17 = var14 - var10;
                     var18 = var13 - var9;
                     var19 = var15 - var11;
                     var29 = (Rect)var2.values.get("android:changeBounds:clip");
                     var42 = (Rect)var3.values.get("android:changeBounds:clip");
                     byte var38 = 0;
                     var6 = 0;
                     if (var16 == 0 || var17 == 0) {
                        var5 = var38;
                        if (var18 == 0) {
                           break label143;
                        }

                        var5 = var38;
                        if (var19 == 0) {
                           break label143;
                        }
                     }

                     if (var8 != var9 || var10 != var11) {
                        var6 = 0 + 1;
                     }

                     if (var12 == var13) {
                        var5 = var6;
                        if (var14 == var15) {
                           break label143;
                        }
                     }

                     var5 = var6 + 1;
                  }

                  label146: {
                     if (var29 == null || var29.equals(var42)) {
                        var6 = var5;
                        if (var29 != null) {
                           break label146;
                        }

                        var6 = var5;
                        if (var42 == null) {
                           break label146;
                        }
                     }

                     var6 = var5 + 1;
                  }

                  if (var6 > 0) {
                     Path var25;
                     Object var27;
                     if (!this.mResizeClip) {
                        ViewUtils.setLeftTopRightBottom(var39, var8, var10, var12, var14);
                        if (var6 == 2) {
                           if (var16 == var18 && var17 == var19) {
                              var25 = this.getPathMotion().getPath((float)var8, (float)var10, (float)var9, (float)var11);
                              var27 = ObjectAnimatorUtils.ofPointF(var39, POSITION_PROPERTY, var25);
                           } else {
                              final ChangeBounds.ViewBounds var31 = new ChangeBounds.ViewBounds(var39);
                              var25 = this.getPathMotion().getPath((float)var8, (float)var10, (float)var9, (float)var11);
                              var33 = ObjectAnimatorUtils.ofPointF(var31, TOP_LEFT_PROPERTY, var25);
                              var25 = this.getPathMotion().getPath((float)var12, (float)var14, (float)var13, (float)var15);
                              ObjectAnimator var43 = ObjectAnimatorUtils.ofPointF(var31, BOTTOM_RIGHT_PROPERTY, var25);
                              var27 = new AnimatorSet();
                              ((AnimatorSet)var27).playTogether(new Animator[]{var33, var43});
                              ((AnimatorSet)var27).addListener(new AnimatorListenerAdapter() {
                                 private ChangeBounds.ViewBounds mViewBounds = var31;
                              });
                           }
                        } else if (var8 == var9 && var10 == var11) {
                           var25 = this.getPathMotion().getPath((float)var12, (float)var14, (float)var13, (float)var15);
                           var27 = ObjectAnimatorUtils.ofPointF(var39, BOTTOM_RIGHT_ONLY_PROPERTY, var25);
                        } else {
                           var25 = this.getPathMotion().getPath((float)var8, (float)var10, (float)var9, (float)var11);
                           var27 = ObjectAnimatorUtils.ofPointF(var39, TOP_LEFT_ONLY_PROPERTY, var25);
                        }
                     } else {
                        ViewUtils.setLeftTopRightBottom(var39, var8, var10, var8 + Math.max(var16, var18), var10 + Math.max(var17, var19));
                        ObjectAnimator var36;
                        if (var8 == var9 && var10 == var11) {
                           var36 = null;
                        } else {
                           var25 = this.getPathMotion().getPath((float)var8, (float)var10, (float)var9, (float)var11);
                           var36 = ObjectAnimatorUtils.ofPointF(var39, POSITION_PROPERTY, var25);
                        }

                        if (var29 == null) {
                           var29 = new Rect(0, 0, var16, var17);
                        }

                        Rect var35;
                        if (var42 == null) {
                           var35 = new Rect(0, 0, var18, var19);
                        } else {
                           var35 = var42;
                        }

                        Object var23 = null;
                        ObjectAnimator var34;
                        if (!var29.equals(var35)) {
                           ViewCompat.setClipBounds(var39, var29);
                           var34 = ObjectAnimator.ofObject(var39, "clipBounds", sRectEvaluator, new Object[]{var29, var35});
                           var34.addListener(new AnimatorListenerAdapter() {
                              private boolean mIsCanceled;

                              public void onAnimationCancel(Animator var1) {
                                 this.mIsCanceled = true;
                              }

                              public void onAnimationEnd(Animator var1) {
                                 if (!this.mIsCanceled) {
                                    ViewCompat.setClipBounds(var39, var42);
                                    ViewUtils.setLeftTopRightBottom(var39, var9, var11, var13, var15);
                                 }

                              }
                           });
                        } else {
                           var34 = (ObjectAnimator)var23;
                        }

                        var27 = TransitionUtils.mergeAnimators(var36, var34);
                     }

                     if (var39.getParent() instanceof ViewGroup) {
                        final ViewGroup var37 = (ViewGroup)var39.getParent();
                        ViewGroupUtils.suppressLayout(var37, true);
                        this.addListener(new TransitionListenerAdapter() {
                           boolean mCanceled = false;

                           public void onTransitionCancel(Transition var1) {
                              ViewGroupUtils.suppressLayout(var37, false);
                              this.mCanceled = true;
                           }

                           public void onTransitionEnd(Transition var1) {
                              if (!this.mCanceled) {
                                 ViewGroupUtils.suppressLayout(var37, false);
                              }

                              var1.removeListener(this);
                           }

                           public void onTransitionPause(Transition var1) {
                              ViewGroupUtils.suppressLayout(var37, false);
                           }

                           public void onTransitionResume(Transition var1) {
                              ViewGroupUtils.suppressLayout(var37, true);
                           }
                        });
                     }

                     return (Animator)var27;
                  }
               } else {
                  var5 = (Integer)var2.values.get("android:changeBounds:windowX");
                  var6 = (Integer)var2.values.get("android:changeBounds:windowY");
                  int var7 = (Integer)var3.values.get("android:changeBounds:windowX");
                  var8 = (Integer)var3.values.get("android:changeBounds:windowY");
                  if (var5 != var7 || var6 != var8) {
                     var1.getLocationInWindow(this.mTempLocation);
                     Bitmap var26 = Bitmap.createBitmap(var39.getWidth(), var39.getHeight(), Config.ARGB_8888);
                     var39.draw(new Canvas(var26));
                     final BitmapDrawable var28 = new BitmapDrawable(var26);
                     final float var4 = ViewUtils.getTransitionAlpha(var39);
                     ViewUtils.setTransitionAlpha(var39, 0.0F);
                     ViewUtils.getOverlay(var1).add(var28);
                     PathMotion var30 = this.getPathMotion();
                     int[] var41 = this.mTempLocation;
                     Path var32 = var30.getPath((float)(var5 - var41[0]), (float)(var6 - var41[1]), (float)(var7 - var41[0]), (float)(var8 - var41[1]));
                     var33 = ObjectAnimator.ofPropertyValuesHolder(var28, new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, var32)});
                     var33.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1x) {
                           ViewUtils.getOverlay(var1).remove(var28);
                           ViewUtils.setTransitionAlpha(var39, var4);
                        }
                     });
                     return var33;
                  }
               }

               return null;
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean getResizeClip() {
      return this.mResizeClip;
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   public void setResizeClip(boolean var1) {
      this.mResizeClip = var1;
   }

   private static class ViewBounds {
      private int mBottom;
      private int mBottomRightCalls;
      private int mLeft;
      private int mRight;
      private int mTop;
      private int mTopLeftCalls;
      private View mView;

      ViewBounds(View var1) {
         this.mView = var1;
      }

      private void setLeftTopRightBottom() {
         ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
         this.mTopLeftCalls = 0;
         this.mBottomRightCalls = 0;
      }

      void setBottomRight(PointF var1) {
         this.mRight = Math.round(var1.x);
         this.mBottom = Math.round(var1.y);
         int var2 = this.mBottomRightCalls + 1;
         this.mBottomRightCalls = var2;
         if (this.mTopLeftCalls == var2) {
            this.setLeftTopRightBottom();
         }

      }

      void setTopLeft(PointF var1) {
         this.mLeft = Math.round(var1.x);
         this.mTop = Math.round(var1.y);
         int var2 = this.mTopLeftCalls + 1;
         this.mTopLeftCalls = var2;
         if (var2 == this.mBottomRightCalls) {
            this.setLeftTopRightBottom();
         }

      }
   }
}
