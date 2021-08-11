package com.codetroopers.betterpickers.radialtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import com.codetroopers.betterpickers.HapticFeedbackController;
import com.codetroopers.betterpickers.R.color;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class RadialPickerLayout extends FrameLayout implements OnTouchListener {
   // $FF: renamed from: AM int
   private static final int field_186 = 0;
   private static final int AMPM_INDEX = 2;
   private static final int ENABLE_PICKER_INDEX = 3;
   private static final int HOUR_INDEX = 0;
   private static final int HOUR_VALUE_TO_DEGREES_STEP_SIZE = 30;
   private static final int MINUTE_INDEX = 1;
   private static final int MINUTE_VALUE_TO_DEGREES_STEP_SIZE = 6;
   // $FF: renamed from: PM int
   private static final int field_187 = 1;
   private static final String TAG = "RadialPickerLayout";
   private static final int VISIBLE_DEGREES_STEP_SIZE = 30;
   private final int TAP_TIMEOUT;
   private final int TOUCH_SLOP;
   private AccessibilityManager mAccessibilityManager;
   private AmPmCirclesView mAmPmCirclesView;
   private CircleView mCircleView;
   private int mCurrentHoursOfDay;
   private int mCurrentItemShowing;
   private int mCurrentMinutes;
   private boolean mDoingMove;
   private boolean mDoingTouch;
   private int mDownDegrees;
   private float mDownX;
   private float mDownY;
   private View mGrayBox;
   private Handler mHandler = new Handler();
   private HapticFeedbackController mHapticFeedbackController;
   private boolean mHideAmPm;
   private RadialSelectorView mHourRadialSelectorView;
   private RadialTextsView mHourRadialTextsView;
   private boolean mInputEnabled;
   private boolean mIs24HourMode;
   private int mIsTouchingAmOrPm = -1;
   private int mLastValueSelected;
   private RadialPickerLayout.OnValueSelectedListener mListener;
   private RadialSelectorView mMinuteRadialSelectorView;
   private RadialTextsView mMinuteRadialTextsView;
   private int[] mSnapPrefer30sMap;
   private boolean mTimeInitialized;
   private AnimatorSet mTransition;

   public RadialPickerLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setOnTouchListener(this);
      this.TOUCH_SLOP = ViewConfiguration.get(var1).getScaledTouchSlop();
      this.TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
      this.mDoingMove = false;
      CircleView var3 = new CircleView(var1);
      this.mCircleView = var3;
      this.addView(var3);
      AmPmCirclesView var4 = new AmPmCirclesView(var1);
      this.mAmPmCirclesView = var4;
      this.addView(var4);
      RadialTextsView var5 = new RadialTextsView(var1);
      this.mHourRadialTextsView = var5;
      this.addView(var5);
      var5 = new RadialTextsView(var1);
      this.mMinuteRadialTextsView = var5;
      this.addView(var5);
      RadialSelectorView var6 = new RadialSelectorView(var1);
      this.mHourRadialSelectorView = var6;
      this.addView(var6);
      var6 = new RadialSelectorView(var1);
      this.mMinuteRadialSelectorView = var6;
      this.addView(var6);
      this.preparePrefer30sMap();
      this.mLastValueSelected = -1;
      this.mInputEnabled = true;
      View var7 = new View(var1);
      this.mGrayBox = var7;
      var7.setLayoutParams(new LayoutParams(-1, -1));
      this.mGrayBox.setBackgroundColor(this.getResources().getColor(color.bpTransparent_black));
      this.mGrayBox.setVisibility(4);
      this.addView(this.mGrayBox);
      this.mAccessibilityManager = (AccessibilityManager)var1.getSystemService("accessibility");
      this.mTimeInitialized = false;
   }

   private int getCurrentlyShowingValue() {
      int var1 = this.getCurrentItemShowing();
      if (var1 == 0) {
         return this.mCurrentHoursOfDay;
      } else {
         return var1 == 1 ? this.mCurrentMinutes : -1;
      }
   }

   private int getDegreesFromCoords(float var1, float var2, boolean var3, Boolean[] var4) {
      int var5 = this.getCurrentItemShowing();
      if (var5 == 0) {
         return this.mHourRadialSelectorView.getDegreesFromCoords(var1, var2, var3, var4);
      } else {
         return var5 == 1 ? this.mMinuteRadialSelectorView.getDegreesFromCoords(var1, var2, var3, var4) : -1;
      }
   }

   private boolean isHourInnerCircle(int var1) {
      return this.mIs24HourMode && var1 <= 12 && var1 != 0;
   }

   private void preparePrefer30sMap() {
      this.mSnapPrefer30sMap = new int[361];
      int var1 = 0;
      int var5 = 1;
      byte var2 = 8;

      byte var4;
      for(int var3 = 0; var3 < 361; var2 = var4) {
         this.mSnapPrefer30sMap[var3] = var1;
         int var7;
         if (var5 == var2) {
            var5 = var1 + 6;
            byte var6;
            if (var5 == 360) {
               var6 = 7;
            } else if (var5 % 30 == 0) {
               var6 = 14;
            } else {
               var6 = 4;
            }

            var7 = 1;
            var4 = var6;
         } else {
            ++var5;
            var4 = var2;
            var7 = var5;
            var5 = var1;
         }

         ++var3;
         var1 = var5;
         var5 = var7;
      }

   }

   private int reselectSelector(int var1, boolean var2, boolean var3, boolean var4) {
      if (var1 == -1) {
         return -1;
      } else {
         int var7 = this.getCurrentItemShowing();
         boolean var5;
         if (!var3 && var7 == 1) {
            var5 = true;
         } else {
            var5 = false;
         }

         int var9;
         if (var5) {
            var9 = this.snapPrefer30s(var1);
         } else {
            var9 = this.snapOnly30s(var1, 0);
         }

         byte var6;
         RadialSelectorView var8;
         if (var7 == 0) {
            var8 = this.mHourRadialSelectorView;
            var6 = 30;
         } else {
            var8 = this.mMinuteRadialSelectorView;
            var6 = 6;
         }

         var8.setSelection(var9, var2, var4);
         var8.invalidate();
         if (var7 == 0) {
            if (this.mIs24HourMode) {
               if (var9 == 0 && var2) {
                  var1 = 360;
               } else {
                  var1 = var9;
                  if (var9 == 360) {
                     var1 = var9;
                     if (!var2) {
                        var1 = 0;
                     }
                  }
               }
            } else {
               var1 = var9;
               if (var9 == 0) {
                  var1 = 360;
               }
            }
         } else {
            var1 = var9;
            if (var9 == 360) {
               var1 = var9;
               if (var7 == 1) {
                  var1 = 0;
               }
            }
         }

         int var10 = var1 / var6;
         var9 = var10;
         if (var7 == 0) {
            var9 = var10;
            if (this.mIs24HourMode) {
               var9 = var10;
               if (!var2) {
                  var9 = var10;
                  if (var1 != 0) {
                     var9 = var10 + 12;
                  }
               }
            }
         }

         return var9;
      }
   }

   private void setItem(int var1, int var2) {
      if (var1 == 0) {
         this.setValueForItem(0, var2);
         this.mHourRadialSelectorView.setSelection(var2 % 12 * 30, this.isHourInnerCircle(var2), false);
         this.mHourRadialSelectorView.invalidate();
      } else if (var1 == 1) {
         this.setValueForItem(1, var2);
         this.mMinuteRadialSelectorView.setSelection(var2 * 6, false, false);
         this.mMinuteRadialSelectorView.invalidate();
         return;
      }

   }

   private void setValueForItem(int var1, int var2) {
      if (var1 == 0) {
         this.mCurrentHoursOfDay = var2;
      } else if (var1 == 1) {
         this.mCurrentMinutes = var2;
      } else {
         if (var1 == 2) {
            if (var2 == 0) {
               this.mCurrentHoursOfDay %= 12;
               return;
            }

            if (var2 == 1) {
               this.mCurrentHoursOfDay = this.mCurrentHoursOfDay % 12 + 12;
            }
         }

      }
   }

   private int snapOnly30s(int var1, int var2) {
      int var3 = var1 / 30 * 30;
      int var4 = var3 + 30;
      if (var2 == 1) {
         return var4;
      } else if (var2 == -1) {
         var2 = var3;
         if (var1 == var3) {
            var2 = var3 - 30;
         }

         return var2;
      } else {
         return var1 - var3 < var4 - var1 ? var3 : var4;
      }
   }

   private int snapPrefer30s(int var1) {
      int[] var2 = this.mSnapPrefer30sMap;
      return var2 == null ? -1 : var2[var1];
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      if (var1.getEventType() == 32) {
         var1.getText().clear();
         Time var5 = new Time();
         var5.hour = this.getHours();
         var5.minute = this.getMinutes();
         long var3 = var5.normalize(true);
         int var2 = 1;
         if (this.mIs24HourMode) {
            var2 = 1 | 128;
         }

         String var6 = DateUtils.formatDateTime(this.getContext(), var3, var2);
         var1.getText().add(var6);
         return true;
      } else {
         return super.dispatchPopulateAccessibilityEvent(var1);
      }
   }

   public int getCurrentItemShowing() {
      int var1 = this.mCurrentItemShowing;
      if (var1 != 0 && var1 != 1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Current item showing was unfortunately set to ");
         var2.append(this.mCurrentItemShowing);
         Log.e("RadialPickerLayout", var2.toString());
         return -1;
      } else {
         return this.mCurrentItemShowing;
      }
   }

   public int getHours() {
      return this.mCurrentHoursOfDay;
   }

   public int getIsCurrentlyAmOrPm() {
      int var1 = this.mCurrentHoursOfDay;
      if (var1 < 12) {
         return 0;
      } else {
         return var1 < 24 ? 1 : -1;
      }
   }

   public int getMinutes() {
      return this.mCurrentMinutes;
   }

   public void initialize(Context var1, HapticFeedbackController var2, int var3, int var4, boolean var5) {
      if (this.mTimeInitialized) {
         Log.e("RadialPickerLayout", "Time has already been initialized.");
      } else {
         this.mHapticFeedbackController = var2;
         this.mIs24HourMode = var5;
         boolean var7;
         if (AccessibilityManagerCompat.isTouchExplorationEnabled(this.mAccessibilityManager)) {
            var7 = true;
         } else {
            var7 = this.mIs24HourMode;
         }

         this.mHideAmPm = var7;
         this.mCircleView.initialize(var1, var7);
         this.mCircleView.invalidate();
         if (!this.mHideAmPm) {
            AmPmCirclesView var13 = this.mAmPmCirclesView;
            byte var6;
            if (var3 < 12) {
               var6 = 0;
            } else {
               var6 = 1;
            }

            var13.initialize(var1, var6);
            this.mAmPmCirclesView.invalidate();
         }

         Resources var9 = var1.getResources();
         int[] var12 = new int[]{12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
         String[] var10 = new String[12];
         String[] var8 = new String[12];
         String[] var11 = new String[12];

         for(int var16 = 0; var16 < 12; ++var16) {
            String var14;
            if (var5) {
               var14 = String.format("%02d", (new int[]{0, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23})[var16]);
            } else {
               var14 = String.format("%d", var12[var16]);
            }

            var10[var16] = var14;
            var8[var16] = String.format("%d", var12[var16]);
            var11[var16] = String.format("%02d", (new int[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55})[var16]);
         }

         RadialTextsView var17 = this.mHourRadialTextsView;
         String[] var15;
         if (var5) {
            var15 = var8;
         } else {
            var15 = null;
         }

         var17.initialize(var9, var10, var15, this.mHideAmPm, true);
         this.mHourRadialTextsView.invalidate();
         this.mMinuteRadialTextsView.initialize(var9, var11, (String[])null, this.mHideAmPm, false);
         this.mMinuteRadialTextsView.invalidate();
         this.setValueForItem(0, var3);
         this.setValueForItem(1, var4);
         this.mHourRadialSelectorView.initialize(var1, this.mHideAmPm, var5, true, var3 % 12 * 30, this.isHourInnerCircle(var3));
         this.mMinuteRadialSelectorView.initialize(var1, this.mHideAmPm, false, false, var4 * 6, false);
         this.mTimeInitialized = true;
      }
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.addAction(4096);
      var1.addAction(8192);
   }

   public void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      var1 = MeasureSpec.getMode(var1);
      int var4 = MeasureSpec.getSize(var2);
      var2 = MeasureSpec.getMode(var2);
      var3 = Math.min(var3, var4);
      super.onMeasure(MeasureSpec.makeMeasureSpec(var3, var1), MeasureSpec.makeMeasureSpec(var3, var2));
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      float var3 = var2.getX();
      float var4 = var2.getY();
      final Boolean[] var10 = new Boolean[]{false};
      int var7 = var2.getAction();
      if (var7 != 0) {
         if (var7 != 1) {
            if (var7 != 2) {
               return false;
            }

            if (!this.mInputEnabled) {
               Log.e("RadialPickerLayout", "Input was disabled, but received ACTION_MOVE.");
               return true;
            }

            float var5 = Math.abs(var4 - this.mDownY);
            float var6 = Math.abs(var3 - this.mDownX);
            if (!this.mDoingMove) {
               var7 = this.TOUCH_SLOP;
               if (var6 <= (float)var7 && var5 <= (float)var7) {
                  return false;
               }
            }

            var7 = this.mIsTouchingAmOrPm;
            if (var7 != 0 && var7 != 1) {
               if (this.mDownDegrees == -1) {
                  return false;
               }

               this.mDoingMove = true;
               this.mHandler.removeCallbacksAndMessages((Object)null);
               var7 = this.getDegreesFromCoords(var3, var4, true, var10);
               if (var7 != -1) {
                  var7 = this.reselectSelector(var7, var10[0], false, true);
                  if (var7 != this.mLastValueSelected) {
                     this.mHapticFeedbackController.tryVibrate();
                     this.mLastValueSelected = var7;
                     this.mListener.onValueSelected(this.getCurrentItemShowing(), var7, false);
                  }
               }

               return true;
            }

            this.mHandler.removeCallbacksAndMessages((Object)null);
            if (this.mAmPmCirclesView.getIsTouchingAmOrPm(var3, var4) != this.mIsTouchingAmOrPm) {
               this.mAmPmCirclesView.setAmOrPmPressed(-1);
               this.mAmPmCirclesView.invalidate();
               this.mIsTouchingAmOrPm = -1;
               return false;
            }
         } else {
            if (!this.mInputEnabled) {
               Log.d("RadialPickerLayout", "Input was disabled, but received ACTION_UP.");
               this.mListener.onValueSelected(3, 1, false);
               return true;
            }

            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.mDoingTouch = false;
            var7 = this.mIsTouchingAmOrPm;
            if (var7 != 0 && var7 != 1) {
               if (this.mDownDegrees != -1) {
                  var7 = this.getDegreesFromCoords(var3, var4, this.mDoingMove, var10);
                  if (var7 != -1) {
                     int var8 = this.reselectSelector(var7, var10[0], this.mDoingMove ^ true, false);
                     var7 = var8;
                     if (this.getCurrentItemShowing() == 0) {
                        var7 = var8;
                        if (!this.mIs24HourMode) {
                           int var9 = this.getIsCurrentlyAmOrPm();
                           if (var9 == 0 && var8 == 12) {
                              var7 = 0;
                           } else {
                              var7 = var8;
                              if (var9 == 1) {
                                 var7 = var8;
                                 if (var8 != 12) {
                                    var7 = var8 + 12;
                                 }
                              }
                           }
                        }
                     }

                     this.setValueForItem(this.getCurrentItemShowing(), var7);
                     this.mListener.onValueSelected(this.getCurrentItemShowing(), var7, true);
                  }
               }

               this.mDoingMove = false;
               return true;
            }

            var7 = this.mAmPmCirclesView.getIsTouchingAmOrPm(var3, var4);
            this.mAmPmCirclesView.setAmOrPmPressed(-1);
            this.mAmPmCirclesView.invalidate();
            if (var7 == this.mIsTouchingAmOrPm) {
               this.mAmPmCirclesView.setAmOrPm(var7);
               if (this.getIsCurrentlyAmOrPm() != var7) {
                  this.mListener.onValueSelected(2, this.mIsTouchingAmOrPm, false);
                  this.setValueForItem(2, var7);
               }
            }

            this.mIsTouchingAmOrPm = -1;
         }

         return false;
      } else if (!this.mInputEnabled) {
         return true;
      } else {
         this.mDownX = var3;
         this.mDownY = var4;
         this.mLastValueSelected = -1;
         this.mDoingMove = false;
         this.mDoingTouch = true;
         if (!this.mHideAmPm) {
            this.mIsTouchingAmOrPm = this.mAmPmCirclesView.getIsTouchingAmOrPm(var3, var4);
         } else {
            this.mIsTouchingAmOrPm = -1;
         }

         var7 = this.mIsTouchingAmOrPm;
         if (var7 != 0 && var7 != 1) {
            var7 = this.getDegreesFromCoords(var3, var4, AccessibilityManagerCompat.isTouchExplorationEnabled(this.mAccessibilityManager), var10);
            this.mDownDegrees = var7;
            if (var7 != -1) {
               this.mHapticFeedbackController.tryVibrate();
               this.mHandler.postDelayed(new Runnable() {
                  public void run() {
                     RadialPickerLayout.this.mDoingMove = true;
                     RadialPickerLayout var2 = RadialPickerLayout.this;
                     int var1 = var2.reselectSelector(var2.mDownDegrees, var10[0], false, true);
                     RadialPickerLayout.this.mLastValueSelected = var1;
                     RadialPickerLayout.this.mListener.onValueSelected(RadialPickerLayout.this.getCurrentItemShowing(), var1, false);
                  }
               }, (long)this.TAP_TIMEOUT);
               return true;
            }
         } else {
            this.mHapticFeedbackController.tryVibrate();
            this.mDownDegrees = -1;
            this.mHandler.postDelayed(new Runnable() {
               public void run() {
                  RadialPickerLayout.this.mAmPmCirclesView.setAmOrPmPressed(RadialPickerLayout.this.mIsTouchingAmOrPm);
                  RadialPickerLayout.this.mAmPmCirclesView.invalidate();
               }
            }, (long)this.TAP_TIMEOUT);
         }

         return true;
      }
   }

   public boolean performAccessibilityAction(int var1, Bundle var2) {
      if (super.performAccessibilityAction(var1, var2)) {
         return true;
      } else {
         byte var3 = 0;
         if (var1 == 4096) {
            var3 = 1;
         } else if (var1 == 8192) {
            var3 = -1;
         }

         if (var3 != 0) {
            int var5 = this.getCurrentlyShowingValue();
            byte var7 = 0;
            int var6 = this.getCurrentItemShowing();
            int var4;
            if (var6 == 0) {
               var7 = 30;
               var4 = var5 % 12;
            } else {
               var4 = var5;
               if (var6 == 1) {
                  var7 = 6;
                  var4 = var5;
               }
            }

            var5 = this.snapOnly30s(var4 * var7, var3) / var7;
            byte var8 = 0;
            if (var6 == 0) {
               if (this.mIs24HourMode) {
                  var7 = 23;
               } else {
                  var7 = 12;
                  var8 = 1;
               }
            } else {
               var7 = 55;
            }

            if (var5 > var7) {
               var4 = var8;
            } else {
               var4 = var5;
               if (var5 < var8) {
                  var4 = var7;
               }
            }

            this.setItem(var6, var4);
            this.mListener.onValueSelected(var6, var4, false);
            return true;
         } else {
            return false;
         }
      }
   }

   public void setAmOrPm(int var1) {
      this.mAmPmCirclesView.setAmOrPm(var1);
      this.mAmPmCirclesView.invalidate();
      this.setValueForItem(2, var1);
   }

   public void setCurrentItemShowing(int var1, boolean var2) {
      if (var1 != 0 && var1 != 1) {
         StringBuilder var8 = new StringBuilder();
         var8.append("TimePicker does not support view at index ");
         var8.append(var1);
         Log.e("RadialPickerLayout", var8.toString());
      } else {
         int var3 = this.getCurrentItemShowing();
         this.mCurrentItemShowing = var1;
         short var4 = 0;
         if (var2 && var1 != var3) {
            ObjectAnimator[] var5 = new ObjectAnimator[4];
            if (var1 == 1) {
               var5[0] = this.mHourRadialTextsView.getDisappearAnimator();
               var5[1] = this.mHourRadialSelectorView.getDisappearAnimator();
               var5[2] = this.mMinuteRadialTextsView.getReappearAnimator();
               var5[3] = this.mMinuteRadialSelectorView.getReappearAnimator();
            } else if (var1 == 0) {
               var5[0] = this.mHourRadialTextsView.getReappearAnimator();
               var5[1] = this.mHourRadialSelectorView.getReappearAnimator();
               var5[2] = this.mMinuteRadialTextsView.getDisappearAnimator();
               var5[3] = this.mMinuteRadialSelectorView.getDisappearAnimator();
            }

            AnimatorSet var6 = this.mTransition;
            if (var6 != null && var6.isRunning()) {
               this.mTransition.end();
            }

            var6 = new AnimatorSet();
            this.mTransition = var6;
            var6.playTogether((Animator[])var5);
            this.mTransition.start();
         } else {
            short var7;
            if (var1 == 0) {
               var7 = 255;
            } else {
               var7 = 0;
            }

            if (var1 == 1) {
               var4 = 255;
            }

            ViewHelper.setAlpha(this.mHourRadialTextsView, (float)var7);
            ViewHelper.setAlpha(this.mHourRadialSelectorView, (float)var7);
            ViewHelper.setAlpha(this.mMinuteRadialTextsView, (float)var4);
            ViewHelper.setAlpha(this.mMinuteRadialSelectorView, (float)var4);
         }
      }
   }

   public void setOnValueSelectedListener(RadialPickerLayout.OnValueSelectedListener var1) {
      this.mListener = var1;
   }

   void setTheme(TypedArray var1) {
      this.mCircleView.setTheme(var1);
      this.mAmPmCirclesView.setTheme(var1);
      this.mHourRadialTextsView.setTheme(var1);
      this.mMinuteRadialTextsView.setTheme(var1);
      this.mHourRadialSelectorView.setTheme(var1);
      this.mMinuteRadialSelectorView.setTheme(var1);
   }

   public void setTime(int var1, int var2) {
      this.setItem(0, var1);
      this.setItem(1, var2);
   }

   public boolean trySettingInputEnabled(boolean var1) {
      boolean var3 = this.mDoingTouch;
      byte var2 = 0;
      if (var3 && !var1) {
         return false;
      } else {
         this.mInputEnabled = var1;
         View var4 = this.mGrayBox;
         if (var1) {
            var2 = 4;
         }

         var4.setVisibility(var2);
         return true;
      }
   }

   public interface OnValueSelectedListener {
      void onValueSelected(int var1, int var2, boolean var3);
   }
}
