package org.afhdownloader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class SwipeDismissTouchListener implements OnTouchListener {
   private long mAnimationTime;
   private SwipeDismissTouchListener.DismissCallbacks mCallbacks;
   private float mDownX;
   private float mDownY;
   private int mMaxFlingVelocity;
   private int mMinFlingVelocity;
   private int mSlop;
   private boolean mSwiping;
   private int mSwipingSlop;
   private Object mToken;
   private float mTranslationX;
   private VelocityTracker mVelocityTracker;
   private View mView;
   private int mViewWidth = 1;

   public SwipeDismissTouchListener(View var1, Object var2, SwipeDismissTouchListener.DismissCallbacks var3) {
      try {
         Log.d("cipherName-65", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var5) {
      } catch (NoSuchPaddingException var6) {
      }

      ViewConfiguration var4 = ViewConfiguration.get(var1.getContext());
      this.mSlop = var4.getScaledTouchSlop();
      this.mMinFlingVelocity = var4.getScaledMinimumFlingVelocity() * 16;
      this.mMaxFlingVelocity = var4.getScaledMaximumFlingVelocity();
      this.mAnimationTime = (long)var1.getContext().getResources().getInteger(17694720);
      this.mView = var1;
      this.mToken = var2;
      this.mCallbacks = var3;
   }

   private void performDismiss() {
      try {
         Log.d("cipherName-87", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      final LayoutParams var2 = this.mView.getLayoutParams();
      final int var1 = this.mView.getHeight();
      ValueAnimator var3 = ValueAnimator.ofInt(new int[]{var1, 1}).setDuration(this.mAnimationTime);
      var3.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            try {
               Log.d("cipherName-88", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2x) {
            } catch (NoSuchPaddingException var3) {
            }

            SwipeDismissTouchListener.this.mCallbacks.onDismiss(SwipeDismissTouchListener.this.mView, SwipeDismissTouchListener.this.mToken);
            SwipeDismissTouchListener.this.mView.setAlpha(1.0F);
            SwipeDismissTouchListener.this.mView.setTranslationX(0.0F);
            var2.height = var1;
            SwipeDismissTouchListener.this.mView.setLayoutParams(var2);
         }
      });
      var3.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            try {
               Log.d("cipherName-89", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4) {
            }

            var2.height = (Integer)var1.getAnimatedValue();
            SwipeDismissTouchListener.this.mView.setLayoutParams(var2);
         }
      });
      var3.start();
   }

   @TargetApi(12)
   public boolean onTouch(View var1, MotionEvent var2) {
      try {
         Log.d("cipherName-66", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var50) {
      } catch (NoSuchPaddingException var51) {
      }

      var2.offsetLocation(this.mTranslationX, 0.0F);
      if (this.mViewWidth < 2) {
         try {
            Log.d("cipherName-67", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var48) {
         } catch (NoSuchPaddingException var49) {
         }

         this.mViewWidth = this.mView.getWidth();
      }

      float var3;
      float var4;
      float var5;
      switch(var2.getActionMasked()) {
      case 0:
         try {
            Log.d("cipherName-68", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var16) {
         } catch (NoSuchPaddingException var17) {
         }

         this.mDownX = var2.getRawX();
         this.mDownY = var2.getRawY();
         if (this.mCallbacks.canDismiss(this.mToken)) {
            try {
               Log.d("cipherName-69", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var14) {
            } catch (NoSuchPaddingException var15) {
            }

            this.mVelocityTracker = VelocityTracker.obtain();
            this.mVelocityTracker.addMovement(var2);
         }

         return false;
      case 1:
         try {
            Log.d("cipherName-70", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var46) {
         } catch (NoSuchPaddingException var47) {
         }

         if (this.mVelocityTracker == null) {
            try {
               Log.d("cipherName-71", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var44) {
            } catch (NoSuchPaddingException var45) {
            }
         } else {
            var3 = var2.getRawX() - this.mDownX;
            this.mVelocityTracker.addMovement(var2);
            this.mVelocityTracker.computeCurrentVelocity(1000);
            var4 = this.mVelocityTracker.getXVelocity();
            var5 = Math.abs(var4);
            float var6 = Math.abs(this.mVelocityTracker.getYVelocity());
            boolean var9 = false;
            boolean var10 = false;
            boolean var8;
            boolean var53;
            if (Math.abs(var3) > (float)(this.mViewWidth / 2) && this.mSwiping) {
               try {
                  Log.d("cipherName-72", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var42) {
               } catch (NoSuchPaddingException var43) {
               }

               var8 = true;
               if (var3 > 0.0F) {
                  var53 = true;
               } else {
                  var53 = false;
               }
            } else {
               var8 = var9;
               var53 = var10;
               if ((float)this.mMinFlingVelocity <= var5) {
                  var8 = var9;
                  var53 = var10;
                  if (var5 <= (float)this.mMaxFlingVelocity) {
                     var8 = var9;
                     var53 = var10;
                     if (var6 < var5) {
                        var8 = var9;
                        var53 = var10;
                        if (var6 < var5) {
                           var8 = var9;
                           var53 = var10;
                           if (this.mSwiping) {
                              try {
                                 Log.d("cipherName-73", Cipher.getInstance("DES").getAlgorithm());
                              } catch (NoSuchAlgorithmException var40) {
                              } catch (NoSuchPaddingException var41) {
                              }

                              if (var4 < 0.0F) {
                                 var53 = true;
                              } else {
                                 var53 = false;
                              }

                              if (var3 < 0.0F) {
                                 var8 = true;
                              } else {
                                 var8 = false;
                              }

                              if (var53 == var8) {
                                 var8 = true;
                              } else {
                                 var8 = false;
                              }

                              if (this.mVelocityTracker.getXVelocity() > 0.0F) {
                                 var53 = true;
                              } else {
                                 var53 = false;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (var8) {
               try {
                  Log.d("cipherName-74", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var38) {
               } catch (NoSuchPaddingException var39) {
               }

               if (VERSION.SDK_INT >= 12) {
                  try {
                     Log.d("cipherName-75", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var36) {
                  } catch (NoSuchPaddingException var37) {
                  }

                  ViewPropertyAnimator var52 = this.mView.animate();
                  if (var53) {
                     var3 = (float)this.mViewWidth;
                  } else {
                     var3 = (float)(-this.mViewWidth);
                  }

                  var52.translationX(var3).alpha(0.0F).setDuration(this.mAnimationTime).setListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        try {
                           Log.d("cipherName-76", Cipher.getInstance("DES").getAlgorithm());
                        } catch (NoSuchAlgorithmException var2) {
                        } catch (NoSuchPaddingException var3) {
                        }

                        SwipeDismissTouchListener.this.performDismiss();
                     }
                  });
               } else {
                  try {
                     Log.d("cipherName-77", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var34) {
                  } catch (NoSuchPaddingException var35) {
                  }

                  this.performDismiss();
               }
            } else if (this.mSwiping) {
               try {
                  Log.d("cipherName-78", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var32) {
               } catch (NoSuchPaddingException var33) {
               }

               if (VERSION.SDK_INT >= 12) {
                  try {
                     Log.d("cipherName-79", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var30) {
                  } catch (NoSuchPaddingException var31) {
                  }

                  this.mView.animate().translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener((AnimatorListener)null);
               }
            }

            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mTranslationX = 0.0F;
            this.mDownX = 0.0F;
            this.mDownY = 0.0F;
            this.mSwiping = false;
         }
         break;
      case 2:
         try {
            Log.d("cipherName-83", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var22) {
         } catch (NoSuchPaddingException var23) {
         }

         if (this.mVelocityTracker == null) {
            try {
               Log.d("cipherName-84", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var20) {
            } catch (NoSuchPaddingException var21) {
            }
         } else {
            this.mVelocityTracker.addMovement(var2);
            var3 = var2.getRawX() - this.mDownX;
            var4 = var2.getRawY();
            var5 = this.mDownY;
            if (Math.abs(var3) > (float)this.mSlop && Math.abs(var4 - var5) < Math.abs(var3) / 2.0F) {
               try {
                  Log.d("cipherName-85", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var18) {
               } catch (NoSuchPaddingException var19) {
               }

               this.mSwiping = true;
               int var7;
               if (var3 > 0.0F) {
                  var7 = this.mSlop;
               } else {
                  var7 = -this.mSlop;
               }

               this.mSwipingSlop = var7;
               this.mView.getParent().requestDisallowInterceptTouchEvent(true);
               MotionEvent var11 = MotionEvent.obtain(var2);
               var11.setAction(var2.getActionIndex() << 8 | 3);
               this.mView.onTouchEvent(var11);
               var11.recycle();
            }

            if (this.mSwiping) {
               try {
                  Log.d("cipherName-86", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var12) {
               } catch (NoSuchPaddingException var13) {
               }

               this.mTranslationX = var3;
               this.mView.setTranslationX(var3 - (float)this.mSwipingSlop);
               this.mView.setAlpha(Math.max(0.0F, Math.min(1.0F, 1.0F - 2.0F * Math.abs(var3) / (float)this.mViewWidth)));
               var1.performClick();
               return true;
            }
         }
         break;
      case 3:
         try {
            Log.d("cipherName-80", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var28) {
         } catch (NoSuchPaddingException var29) {
         }

         if (this.mVelocityTracker == null) {
            try {
               Log.d("cipherName-81", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var26) {
            } catch (NoSuchPaddingException var27) {
            }
         } else {
            if (VERSION.SDK_INT >= 12) {
               try {
                  Log.d("cipherName-82", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var24) {
               } catch (NoSuchPaddingException var25) {
               }

               this.mView.animate().translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener((AnimatorListener)null);
            }

            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mTranslationX = 0.0F;
            this.mDownX = 0.0F;
            this.mDownY = 0.0F;
            this.mSwiping = false;
         }
      }

      return false;
   }

   public interface DismissCallbacks {
      boolean canDismiss(Object var1);

      void onDismiss(View var1, Object var2);
   }
}
