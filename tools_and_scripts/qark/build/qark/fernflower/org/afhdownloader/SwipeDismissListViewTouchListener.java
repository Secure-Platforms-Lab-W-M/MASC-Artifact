package org.afhdownloader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class SwipeDismissListViewTouchListener implements OnTouchListener {
   private long mAnimationTime;
   private SwipeDismissListViewTouchListener.DismissCallbacks mCallbacks;
   private int mDismissAnimationRefCount = 0;
   private int mDownPosition;
   private View mDownView;
   private float mDownX;
   private float mDownY;
   private ListView mListView;
   private int mMaxFlingVelocity;
   private int mMinFlingVelocity;
   private boolean mPaused;
   private List mPendingDismisses = new ArrayList();
   private int mSlop;
   private boolean mSwiping;
   private int mSwipingSlop;
   private VelocityTracker mVelocityTracker;
   private int mViewWidth = 1;

   public SwipeDismissListViewTouchListener(ListView var1, SwipeDismissListViewTouchListener.DismissCallbacks var2) {
      try {
         Log.d("cipherName-96", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      ViewConfiguration var3 = ViewConfiguration.get(var1.getContext());
      this.mSlop = var3.getScaledTouchSlop();
      this.mMinFlingVelocity = var3.getScaledMinimumFlingVelocity() * 16;
      this.mMaxFlingVelocity = var3.getScaledMaximumFlingVelocity();
      this.mAnimationTime = (long)var1.getContext().getResources().getInteger(17694720);
      this.mListView = var1;
      this.mCallbacks = var2;
   }

   // $FF: synthetic method
   static int access$106(SwipeDismissListViewTouchListener var0) {
      int var1 = var0.mDismissAnimationRefCount - 1;
      var0.mDismissAnimationRefCount = var1;
      return var1;
   }

   private void performDismiss(final View var1, int var2) {
      try {
         Log.d("cipherName-129", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      final LayoutParams var4 = var1.getLayoutParams();
      final int var3 = var1.getHeight();
      ValueAnimator var5 = ValueAnimator.ofInt(new int[]{var3, 1}).setDuration(this.mAnimationTime);
      var5.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            try {
               Log.d("cipherName-130", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var13) {
            } catch (NoSuchPaddingException var14) {
            }

            SwipeDismissListViewTouchListener.access$106(SwipeDismissListViewTouchListener.this);
            if (SwipeDismissListViewTouchListener.this.mDismissAnimationRefCount == 0) {
               try {
                  Log.d("cipherName-131", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var11) {
               } catch (NoSuchPaddingException var12) {
               }

               Collections.sort(SwipeDismissListViewTouchListener.this.mPendingDismisses);
               int[] var15 = new int[SwipeDismissListViewTouchListener.this.mPendingDismisses.size()];

               for(int var2 = SwipeDismissListViewTouchListener.this.mPendingDismisses.size() - 1; var2 >= 0; --var2) {
                  try {
                     Log.d("cipherName-132", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var9) {
                  } catch (NoSuchPaddingException var10) {
                  }

                  var15[var2] = ((SwipeDismissListViewTouchListener.PendingDismissData)SwipeDismissListViewTouchListener.this.mPendingDismisses.get(var2)).position;
               }

               SwipeDismissListViewTouchListener.this.mCallbacks.onDismiss(SwipeDismissListViewTouchListener.this.mListView, var15);
               SwipeDismissListViewTouchListener.this.mDownPosition = -1;
               Iterator var16 = SwipeDismissListViewTouchListener.this.mPendingDismisses.iterator();

               while(var16.hasNext()) {
                  SwipeDismissListViewTouchListener.PendingDismissData var5 = (SwipeDismissListViewTouchListener.PendingDismissData)var16.next();

                  try {
                     Log.d("cipherName-133", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var7) {
                  } catch (NoSuchPaddingException var8) {
                  }

                  var5.view.setAlpha(1.0F);
                  var5.view.setTranslationX(0.0F);
                  LayoutParams var6 = var5.view.getLayoutParams();
                  var6.height = var3;
                  var5.view.setLayoutParams(var6);
               }

               long var3x = SystemClock.uptimeMillis();
               MotionEvent var17 = MotionEvent.obtain(var3x, var3x, 3, 0.0F, 0.0F, 0);
               SwipeDismissListViewTouchListener.this.mListView.dispatchTouchEvent(var17);
               SwipeDismissListViewTouchListener.this.mPendingDismisses.clear();
            }

         }
      });
      var5.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1x) {
            try {
               Log.d("cipherName-134", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4x) {
            }

            var4.height = (Integer)var1x.getAnimatedValue();
            var1.setLayoutParams(var4);
         }
      });
      this.mPendingDismisses.add(new SwipeDismissListViewTouchListener.PendingDismissData(var2, var1));
      var5.start();
   }

   public OnScrollListener makeScrollListener() {
      try {
         Log.d("cipherName-98", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return new OnScrollListener() {
         public void onScroll(AbsListView var1, int var2, int var3, int var4) {
            try {
               Log.d("cipherName-100", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var5) {
            } catch (NoSuchPaddingException var6) {
            }
         }

         public void onScrollStateChanged(AbsListView var1, int var2) {
            boolean var3 = true;

            try {
               Log.d("cipherName-99", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var4) {
            } catch (NoSuchPaddingException var5) {
            }

            SwipeDismissListViewTouchListener var6 = SwipeDismissListViewTouchListener.this;
            if (var2 == 1) {
               var3 = false;
            }

            var6.setEnabled(var3);
         }
      };
   }

   @TargetApi(12)
   public boolean onTouch(final View var1, MotionEvent var2) {
      try {
         Log.d("cipherName-101", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var62) {
      } catch (NoSuchPaddingException var63) {
      }

      if (this.mViewWidth < 2) {
         try {
            Log.d("cipherName-102", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var60) {
         } catch (NoSuchPaddingException var61) {
         }

         this.mViewWidth = this.mListView.getWidth();
      }

      float var3;
      float var4;
      float var5;
      final int var69;
      int var7;
      switch(var2.getActionMasked()) {
      case 0:
         try {
            Log.d("cipherName-103", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var28) {
         } catch (NoSuchPaddingException var29) {
         }

         if (this.mPaused) {
            try {
               Log.d("cipherName-104", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var16) {
            } catch (NoSuchPaddingException var17) {
            }

            return false;
         }

         Rect var64 = new Rect();
         var69 = this.mListView.getChildCount();
         int[] var70 = new int[2];
         this.mListView.getLocationOnScreen(var70);
         int var67 = (int)var2.getRawX();
         int var68 = var70[0];
         int var11 = (int)var2.getRawY();
         int var12 = var70[1];

         for(var7 = 0; var7 < var69; ++var7) {
            try {
               Log.d("cipherName-105", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var26) {
            } catch (NoSuchPaddingException var27) {
            }

            View var71 = this.mListView.getChildAt(var7);
            var71.getHitRect(var64);
            if (var64.contains(var67 - var68, var11 - var12)) {
               try {
                  Log.d("cipherName-106", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var24) {
               } catch (NoSuchPaddingException var25) {
               }

               this.mDownView = var71;
               break;
            }
         }

         if (this.mDownView != null) {
            try {
               Log.d("cipherName-107", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var22) {
            } catch (NoSuchPaddingException var23) {
            }

            this.mDownX = var2.getRawX();
            this.mDownY = var2.getRawY();
            this.mDownPosition = this.mListView.getPositionForView(this.mDownView);
            if (this.mCallbacks.canDismiss(this.mDownPosition)) {
               try {
                  Log.d("cipherName-108", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var20) {
               } catch (NoSuchPaddingException var21) {
               }

               this.mVelocityTracker = VelocityTracker.obtain();
               this.mVelocityTracker.addMovement(var2);
            } else {
               try {
                  Log.d("cipherName-109", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var18) {
               } catch (NoSuchPaddingException var19) {
               }

               this.mDownView = null;
            }
         }

         return false;
      case 1:
         try {
            Log.d("cipherName-113", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var52) {
         } catch (NoSuchPaddingException var53) {
         }

         if (this.mVelocityTracker == null) {
            try {
               Log.d("cipherName-114", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var50) {
            } catch (NoSuchPaddingException var51) {
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
            boolean var66;
            boolean var8;
            if (Math.abs(var3) > (float)(this.mViewWidth / 2) && this.mSwiping) {
               try {
                  Log.d("cipherName-115", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var48) {
               } catch (NoSuchPaddingException var49) {
               }

               var8 = true;
               if (var3 > 0.0F) {
                  var66 = true;
               } else {
                  var66 = false;
               }
            } else {
               var8 = var9;
               var66 = var10;
               if ((float)this.mMinFlingVelocity <= var5) {
                  var8 = var9;
                  var66 = var10;
                  if (var5 <= (float)this.mMaxFlingVelocity) {
                     var8 = var9;
                     var66 = var10;
                     if (var6 < var5) {
                        var8 = var9;
                        var66 = var10;
                        if (this.mSwiping) {
                           try {
                              Log.d("cipherName-116", Cipher.getInstance("DES").getAlgorithm());
                           } catch (NoSuchAlgorithmException var46) {
                           } catch (NoSuchPaddingException var47) {
                           }

                           if (var4 < 0.0F) {
                              var66 = true;
                           } else {
                              var66 = false;
                           }

                           if (var3 < 0.0F) {
                              var8 = true;
                           } else {
                              var8 = false;
                           }

                           if (var66 == var8) {
                              var8 = true;
                           } else {
                              var8 = false;
                           }

                           if (this.mVelocityTracker.getXVelocity() > 0.0F) {
                              var66 = true;
                           } else {
                              var66 = false;
                           }
                        }
                     }
                  }
               }
            }

            if (var8 && this.mDownPosition != -1) {
               try {
                  Log.d("cipherName-117", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var44) {
               } catch (NoSuchPaddingException var45) {
               }

               var1 = this.mDownView;
               var69 = this.mDownPosition;
               ++this.mDismissAnimationRefCount;
               if (VERSION.SDK_INT >= 12) {
                  try {
                     Log.d("cipherName-118", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var42) {
                  } catch (NoSuchPaddingException var43) {
                  }

                  ViewPropertyAnimator var65 = this.mDownView.animate();
                  if (var66) {
                     var3 = (float)this.mViewWidth;
                  } else {
                     var3 = (float)(-this.mViewWidth);
                  }

                  var65.translationX(var3).alpha(0.0F).setDuration(this.mAnimationTime).setListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        try {
                           Log.d("cipherName-119", Cipher.getInstance("DES").getAlgorithm());
                        } catch (NoSuchAlgorithmException var2) {
                        } catch (NoSuchPaddingException var3) {
                        }

                        SwipeDismissListViewTouchListener.this.performDismiss(var1, var69);
                     }
                  });
               } else {
                  try {
                     Log.d("cipherName-120", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var40) {
                  } catch (NoSuchPaddingException var41) {
                  }

                  this.performDismiss(var1, var69);
               }
            } else {
               try {
                  Log.d("cipherName-121", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var38) {
               } catch (NoSuchPaddingException var39) {
               }

               if (VERSION.SDK_INT >= 12) {
                  try {
                     Log.d("cipherName-122", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var36) {
                  } catch (NoSuchPaddingException var37) {
                  }

                  this.mDownView.animate().translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener((AnimatorListener)null);
               }
            }

            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mDownX = 0.0F;
            this.mDownY = 0.0F;
            this.mDownView = null;
            this.mDownPosition = -1;
            this.mSwiping = false;
         }
         break;
      case 2:
         try {
            Log.d("cipherName-123", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var34) {
         } catch (NoSuchPaddingException var35) {
         }

         if (this.mVelocityTracker != null && !this.mPaused) {
            this.mVelocityTracker.addMovement(var2);
            var3 = var2.getRawX() - this.mDownX;
            var4 = var2.getRawY();
            var5 = this.mDownY;
            if (Math.abs(var3) > (float)this.mSlop && Math.abs(var4 - var5) < Math.abs(var3) / 2.0F) {
               try {
                  Log.d("cipherName-125", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var30) {
               } catch (NoSuchPaddingException var31) {
               }

               this.mSwiping = true;
               if (var3 > 0.0F) {
                  var7 = this.mSlop;
               } else {
                  var7 = -this.mSlop;
               }

               this.mSwipingSlop = var7;
               this.mListView.requestDisallowInterceptTouchEvent(true);
               MotionEvent var13 = MotionEvent.obtain(var2);
               var13.setAction(var2.getActionIndex() << 8 | 3);
               this.mListView.onTouchEvent(var13);
               var13.recycle();
            }

            if (this.mSwiping) {
               try {
                  Log.d("cipherName-126", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var14) {
               } catch (NoSuchPaddingException var15) {
               }

               this.mDownView.setTranslationX(var3 - (float)this.mSwipingSlop);
               this.mDownView.setAlpha(Math.max(0.0F, Math.min(1.0F, 1.0F - 2.0F * Math.abs(var3) / (float)this.mViewWidth)));
               var1.performClick();
               return true;
            }
         } else {
            try {
               Log.d("cipherName-124", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var32) {
            } catch (NoSuchPaddingException var33) {
            }
         }
         break;
      case 3:
         try {
            Log.d("cipherName-110", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var58) {
         } catch (NoSuchPaddingException var59) {
         }

         if (this.mVelocityTracker == null) {
            try {
               Log.d("cipherName-111", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var56) {
            } catch (NoSuchPaddingException var57) {
            }
         } else {
            if (this.mDownView != null && this.mSwiping) {
               try {
                  Log.d("cipherName-112", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var54) {
               } catch (NoSuchPaddingException var55) {
               }

               this.mDownView.animate().translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener((AnimatorListener)null);
            }

            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mDownX = 0.0F;
            this.mDownY = 0.0F;
            this.mDownView = null;
            this.mDownPosition = -1;
            this.mSwiping = false;
         }
      }

      return false;
   }

   public void setEnabled(boolean var1) {
      try {
         Log.d("cipherName-97", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      if (!var1) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.mPaused = var1;
   }

   public interface DismissCallbacks {
      boolean canDismiss(int var1);

      void onDismiss(ListView var1, int[] var2);
   }

   class PendingDismissData implements Comparable {
      public int position;
      public View view;

      public PendingDismissData(int var2, View var3) {
         try {
            Log.d("cipherName-127", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         this.position = var2;
         this.view = var3;
      }

      public int compareTo(SwipeDismissListViewTouchListener.PendingDismissData var1) {
         try {
            Log.d("cipherName-128", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         return var1.position - this.position;
      }
   }
}
