package android.support.transition;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
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
      if (this.mReparent) {
         boolean var4 = true;
         boolean var3 = true;
         TransitionValues var5 = this.getMatchedTransitionValues(var1, true);
         if (var5 == null) {
            if (var1 != var2) {
               var3 = false;
            }

            return var3;
         } else {
            if (var2 == var5.view) {
               var3 = var4;
            } else {
               var3 = false;
            }

            return var3;
         }
      } else {
         return true;
      }
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      this.captureValues(var1);
   }

   @Nullable
   public Animator createAnimator(@NonNull final ViewGroup var1, @Nullable TransitionValues var2, @Nullable TransitionValues var3) {
      if (var2 != null) {
         if (var3 == null) {
            return null;
         } else {
            Map var20 = var2.values;
            Map var19 = var3.values;
            ViewGroup var39 = (ViewGroup)var20.get("android:changeBounds:parent");
            ViewGroup var21 = (ViewGroup)var19.get("android:changeBounds:parent");
            if (var39 != null && var21 != null) {
               final View var38 = var3.view;
               int var5;
               int var7;
               final int var8;
               ObjectAnimator var32;
               if (this.parentMatches(var39, var21)) {
                  Rect var22 = (Rect)var2.values.get("android:changeBounds:bounds");
                  final Rect var41 = (Rect)var3.values.get("android:changeBounds:bounds");
                  var7 = var22.left;
                  var8 = var41.left;
                  int var9 = var22.top;
                  final int var10 = var41.top;
                  int var11 = var22.right;
                  final int var12 = var41.right;
                  int var13 = var22.bottom;
                  final int var14 = var41.bottom;
                  int var15 = var11 - var7;
                  int var16 = var13 - var9;
                  int var17 = var12 - var8;
                  int var18 = var14 - var10;
                  Rect var27 = (Rect)var2.values.get("android:changeBounds:clip");
                  var41 = (Rect)var3.values.get("android:changeBounds:clip");
                  byte var37 = 0;
                  var5 = 0;
                  if ((var15 == 0 || var16 == 0) && (var17 == 0 || var18 == 0)) {
                     var5 = var37;
                  } else {
                     if (var7 != var8 || var9 != var10) {
                        var5 = 0 + 1;
                     }

                     if (var11 != var12 || var13 != var14) {
                        ++var5;
                     }
                  }

                  if (var27 != null && !var27.equals(var41) || var27 == null && var41 != null) {
                     ++var5;
                  }

                  if (var5 > 0) {
                     Path var23;
                     Object var25;
                     if (!this.mResizeClip) {
                        ViewUtils.setLeftTopRightBottom(var38, var7, var9, var11, var13);
                        Path var31;
                        if (var5 == 2) {
                           if (var15 == var17 && var16 == var18) {
                              var31 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                              var25 = ObjectAnimatorUtils.ofPointF(var38, POSITION_PROPERTY, var31);
                           } else {
                              final ChangeBounds.ViewBounds var29 = new ChangeBounds.ViewBounds(var38);
                              var23 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                              var32 = ObjectAnimatorUtils.ofPointF(var29, TOP_LEFT_PROPERTY, var23);
                              var23 = this.getPathMotion().getPath((float)var11, (float)var13, (float)var12, (float)var14);
                              ObjectAnimator var42 = ObjectAnimatorUtils.ofPointF(var29, BOTTOM_RIGHT_PROPERTY, var23);
                              var25 = new AnimatorSet();
                              ((AnimatorSet)var25).playTogether(new Animator[]{var32, var42});
                              ((AnimatorSet)var25).addListener(new AnimatorListenerAdapter() {
                                 private ChangeBounds.ViewBounds mViewBounds = var29;
                              });
                           }
                        } else if (var7 == var8 && var9 == var10) {
                           var31 = this.getPathMotion().getPath((float)var11, (float)var13, (float)var12, (float)var14);
                           var25 = ObjectAnimatorUtils.ofPointF(var38, BOTTOM_RIGHT_ONLY_PROPERTY, var31);
                        } else {
                           var23 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                           var25 = ObjectAnimatorUtils.ofPointF(var38, TOP_LEFT_ONLY_PROPERTY, var23);
                        }
                     } else {
                        ViewUtils.setLeftTopRightBottom(var38, var7, var9, var7 + Math.max(var15, var17), var9 + Math.max(var16, var18));
                        ObjectAnimator var33;
                        if (var7 == var8 && var9 == var10) {
                           var33 = null;
                        } else {
                           var23 = this.getPathMotion().getPath((float)var7, (float)var9, (float)var8, (float)var10);
                           var33 = ObjectAnimatorUtils.ofPointF(var38, POSITION_PROPERTY, var23);
                        }

                        if (var27 == null) {
                           var27 = new Rect(0, 0, var15, var16);
                        }

                        Rect var34;
                        if (var41 == null) {
                           var34 = new Rect(0, 0, var17, var18);
                        } else {
                           var34 = var41;
                        }

                        ObjectAnimator var35;
                        if (!var27.equals(var34)) {
                           ViewCompat.setClipBounds(var38, var27);
                           var35 = ObjectAnimator.ofObject(var38, "clipBounds", sRectEvaluator, new Object[]{var27, var34});
                           var35.addListener(new AnimatorListenerAdapter() {
                              private boolean mIsCanceled;

                              public void onAnimationCancel(Animator var1) {
                                 this.mIsCanceled = true;
                              }

                              public void onAnimationEnd(Animator var1) {
                                 if (!this.mIsCanceled) {
                                    ViewCompat.setClipBounds(var38, var41);
                                    ViewUtils.setLeftTopRightBottom(var38, var8, var10, var12, var14);
                                 }
                              }
                           });
                        } else {
                           var35 = null;
                        }

                        var25 = TransitionUtils.mergeAnimators(var33, var35);
                     }

                     if (var38.getParent() instanceof ViewGroup) {
                        final ViewGroup var36 = (ViewGroup)var38.getParent();
                        ViewGroupUtils.suppressLayout(var36, true);
                        this.addListener(new TransitionListenerAdapter() {
                           boolean mCanceled = false;

                           public void onTransitionCancel(@NonNull Transition var1) {
                              ViewGroupUtils.suppressLayout(var36, false);
                              this.mCanceled = true;
                           }

                           public void onTransitionEnd(@NonNull Transition var1) {
                              if (!this.mCanceled) {
                                 ViewGroupUtils.suppressLayout(var36, false);
                              }

                              var1.removeListener(this);
                           }

                           public void onTransitionPause(@NonNull Transition var1) {
                              ViewGroupUtils.suppressLayout(var36, false);
                           }

                           public void onTransitionResume(@NonNull Transition var1) {
                              ViewGroupUtils.suppressLayout(var36, true);
                           }
                        });
                        return (Animator)var25;
                     }

                     return (Animator)var25;
                  }
               } else {
                  var5 = (Integer)var2.values.get("android:changeBounds:windowX");
                  int var6 = (Integer)var2.values.get("android:changeBounds:windowY");
                  var7 = (Integer)var3.values.get("android:changeBounds:windowX");
                  var8 = (Integer)var3.values.get("android:changeBounds:windowY");
                  if (var5 != var7 || var6 != var8) {
                     var1.getLocationInWindow(this.mTempLocation);
                     Bitmap var24 = Bitmap.createBitmap(var38.getWidth(), var38.getHeight(), Config.ARGB_8888);
                     var38.draw(new Canvas(var24));
                     final BitmapDrawable var26 = new BitmapDrawable(var24);
                     final float var4 = ViewUtils.getTransitionAlpha(var38);
                     ViewUtils.setTransitionAlpha(var38, 0.0F);
                     ViewUtils.getOverlay(var1).add(var26);
                     PathMotion var28 = this.getPathMotion();
                     int[] var40 = this.mTempLocation;
                     Path var30 = var28.getPath((float)(var5 - var40[0]), (float)(var6 - var40[1]), (float)(var7 - var40[0]), (float)(var8 - var40[1]));
                     var32 = ObjectAnimator.ofPropertyValuesHolder(var26, new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, var30)});
                     var32.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1x) {
                           ViewUtils.getOverlay(var1).remove(var26);
                           ViewUtils.setTransitionAlpha(var38, var4);
                        }
                     });
                     return var32;
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

   @Nullable
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
         ++this.mBottomRightCalls;
         if (this.mTopLeftCalls == this.mBottomRightCalls) {
            this.setLeftTopRightBottom();
         }
      }

      void setTopLeft(PointF var1) {
         this.mLeft = Math.round(var1.x);
         this.mTop = Math.round(var1.y);
         ++this.mTopLeftCalls;
         if (this.mTopLeftCalls == this.mBottomRightCalls) {
            this.setLeftTopRightBottom();
         }
      }
   }
}
