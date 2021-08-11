/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Bundle
 *  android.os.Handler
 *  android.text.format.DateUtils
 *  android.text.format.Time
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.FrameLayout
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 */
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
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import com.codetroopers.betterpickers.HapticFeedbackController;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.radialtimepicker.AmPmCirclesView;
import com.codetroopers.betterpickers.radialtimepicker.CircleView;
import com.codetroopers.betterpickers.radialtimepicker.RadialSelectorView;
import com.codetroopers.betterpickers.radialtimepicker.RadialTextsView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import java.util.List;

public class RadialPickerLayout
extends FrameLayout
implements View.OnTouchListener {
    private static final int AM = 0;
    private static final int AMPM_INDEX = 2;
    private static final int ENABLE_PICKER_INDEX = 3;
    private static final int HOUR_INDEX = 0;
    private static final int HOUR_VALUE_TO_DEGREES_STEP_SIZE = 30;
    private static final int MINUTE_INDEX = 1;
    private static final int MINUTE_VALUE_TO_DEGREES_STEP_SIZE = 6;
    private static final int PM = 1;
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
    private OnValueSelectedListener mListener;
    private RadialSelectorView mMinuteRadialSelectorView;
    private RadialTextsView mMinuteRadialTextsView;
    private int[] mSnapPrefer30sMap;
    private boolean mTimeInitialized;
    private AnimatorSet mTransition;

    public RadialPickerLayout(Context context, AttributeSet object) {
        super(context, (AttributeSet)object);
        this.setOnTouchListener((View.OnTouchListener)this);
        this.TOUCH_SLOP = ViewConfiguration.get((Context)context).getScaledTouchSlop();
        this.TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
        this.mDoingMove = false;
        this.mCircleView = object = new CircleView(context);
        this.addView((View)object);
        this.mAmPmCirclesView = object = new AmPmCirclesView(context);
        this.addView((View)object);
        this.mHourRadialTextsView = object = new RadialTextsView(context);
        this.addView((View)object);
        this.mMinuteRadialTextsView = object = new RadialTextsView(context);
        this.addView((View)object);
        this.mHourRadialSelectorView = object = new RadialSelectorView(context);
        this.addView((View)object);
        this.mMinuteRadialSelectorView = object = new RadialSelectorView(context);
        this.addView((View)object);
        this.preparePrefer30sMap();
        this.mLastValueSelected = -1;
        this.mInputEnabled = true;
        this.mGrayBox = object = new View(context);
        object.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mGrayBox.setBackgroundColor(this.getResources().getColor(R.color.bpTransparent_black));
        this.mGrayBox.setVisibility(4);
        this.addView(this.mGrayBox);
        this.mAccessibilityManager = (AccessibilityManager)context.getSystemService("accessibility");
        this.mTimeInitialized = false;
    }

    private int getCurrentlyShowingValue() {
        int n = this.getCurrentItemShowing();
        if (n == 0) {
            return this.mCurrentHoursOfDay;
        }
        if (n == 1) {
            return this.mCurrentMinutes;
        }
        return -1;
    }

    private int getDegreesFromCoords(float f, float f2, boolean bl, Boolean[] arrboolean) {
        int n = this.getCurrentItemShowing();
        if (n == 0) {
            return this.mHourRadialSelectorView.getDegreesFromCoords(f, f2, bl, arrboolean);
        }
        if (n == 1) {
            return this.mMinuteRadialSelectorView.getDegreesFromCoords(f, f2, bl, arrboolean);
        }
        return -1;
    }

    private boolean isHourInnerCircle(int n) {
        if (this.mIs24HourMode && n <= 12 && n != 0) {
            return true;
        }
        return false;
    }

    private void preparePrefer30sMap() {
        this.mSnapPrefer30sMap = new int[361];
        int n = 0;
        int n2 = 1;
        int n3 = 8;
        for (int i = 0; i < 361; ++i) {
            int n4;
            this.mSnapPrefer30sMap[i] = n;
            if (n2 == n3) {
                n2 = n + 6;
                n = n2 == 360 ? 7 : (n2 % 30 == 0 ? 14 : 4);
                n3 = 1;
                n4 = n;
            } else {
                n4 = n3;
                n3 = ++n2;
                n2 = n;
            }
            n = n2;
            n2 = n3;
            n3 = n4;
        }
    }

    private int reselectSelector(int n, boolean bl, boolean bl2, boolean bl3) {
        RadialSelectorView radialSelectorView;
        int n2;
        if (n == -1) {
            return -1;
        }
        int n3 = this.getCurrentItemShowing();
        int n4 = !bl2 && n3 == 1 ? 1 : 0;
        n4 = n4 != 0 ? this.snapPrefer30s(n) : this.snapOnly30s(n, 0);
        if (n3 == 0) {
            radialSelectorView = this.mHourRadialSelectorView;
            n2 = 30;
        } else {
            radialSelectorView = this.mMinuteRadialSelectorView;
            n2 = 6;
        }
        radialSelectorView.setSelection(n4, bl, bl3);
        radialSelectorView.invalidate();
        if (n3 == 0) {
            if (this.mIs24HourMode) {
                if (n4 == 0 && bl) {
                    n = 360;
                } else {
                    n = n4;
                    if (n4 == 360) {
                        n = n4;
                        if (!bl) {
                            n = 0;
                        }
                    }
                }
            } else {
                n = n4;
                if (n4 == 0) {
                    n = 360;
                }
            }
        } else {
            n = n4;
            if (n4 == 360) {
                n = n4;
                if (n3 == 1) {
                    n = 0;
                }
            }
        }
        n4 = n2 = n / n2;
        if (n3 == 0) {
            n4 = n2;
            if (this.mIs24HourMode) {
                n4 = n2;
                if (!bl) {
                    n4 = n2;
                    if (n != 0) {
                        n4 = n2 + 12;
                    }
                }
            }
        }
        return n4;
    }

    private void setItem(int n, int n2) {
        if (n == 0) {
            this.setValueForItem(0, n2);
            this.mHourRadialSelectorView.setSelection(n2 % 12 * 30, this.isHourInnerCircle(n2), false);
            this.mHourRadialSelectorView.invalidate();
        } else if (n == 1) {
            this.setValueForItem(1, n2);
            this.mMinuteRadialSelectorView.setSelection(n2 * 6, false, false);
            this.mMinuteRadialSelectorView.invalidate();
            return;
        }
    }

    private void setValueForItem(int n, int n2) {
        if (n == 0) {
            this.mCurrentHoursOfDay = n2;
            return;
        }
        if (n == 1) {
            this.mCurrentMinutes = n2;
            return;
        }
        if (n == 2) {
            if (n2 == 0) {
                this.mCurrentHoursOfDay %= 12;
                return;
            }
            if (n2 == 1) {
                this.mCurrentHoursOfDay = this.mCurrentHoursOfDay % 12 + 12;
            }
        }
    }

    private int snapOnly30s(int n, int n2) {
        int n3 = n / 30 * 30;
        int n4 = n3 + 30;
        if (n2 == 1) {
            return n4;
        }
        if (n2 == -1) {
            n2 = n3;
            if (n == n3) {
                n2 = n3 - 30;
            }
            return n2;
        }
        if (n - n3 < n4 - n) {
            return n3;
        }
        return n4;
    }

    private int snapPrefer30s(int n) {
        int[] arrn = this.mSnapPrefer30sMap;
        if (arrn == null) {
            return -1;
        }
        return arrn[n];
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.getText().clear();
            Object object = new Time();
            object.hour = this.getHours();
            object.minute = this.getMinutes();
            long l = object.normalize(true);
            int n = 1;
            if (this.mIs24HourMode) {
                n = 1 | 128;
            }
            object = DateUtils.formatDateTime((Context)this.getContext(), (long)l, (int)n);
            accessibilityEvent.getText().add(object);
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    public int getCurrentItemShowing() {
        int n = this.mCurrentItemShowing;
        if (n != 0 && n != 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Current item showing was unfortunately set to ");
            stringBuilder.append(this.mCurrentItemShowing);
            Log.e((String)"RadialPickerLayout", (String)stringBuilder.toString());
            return -1;
        }
        return this.mCurrentItemShowing;
    }

    public int getHours() {
        return this.mCurrentHoursOfDay;
    }

    public int getIsCurrentlyAmOrPm() {
        int n = this.mCurrentHoursOfDay;
        if (n < 12) {
            return 0;
        }
        if (n < 24) {
            return 1;
        }
        return -1;
    }

    public int getMinutes() {
        return this.mCurrentMinutes;
    }

    public void initialize(Context context, HapticFeedbackController arrstring, int n, int n2, boolean bl) {
        int n3;
        if (this.mTimeInitialized) {
            Log.e((String)"RadialPickerLayout", (String)"Time has already been initialized.");
            return;
        }
        this.mHapticFeedbackController = arrstring;
        this.mIs24HourMode = bl;
        boolean bl2 = AccessibilityManagerCompat.isTouchExplorationEnabled(this.mAccessibilityManager) ? true : this.mIs24HourMode;
        this.mHideAmPm = bl2;
        this.mCircleView.initialize(context, bl2);
        this.mCircleView.invalidate();
        if (!this.mHideAmPm) {
            arrstring = this.mAmPmCirclesView;
            n3 = n < 12 ? 0 : 1;
            arrstring.initialize(context, n3);
            this.mAmPmCirclesView.invalidate();
        }
        Resources resources = context.getResources();
        Object object = new int[12];
        int[] arrn = object;
        arrn[0] = 12;
        arrn[1] = 1;
        arrn[2] = 2;
        arrn[3] = 3;
        arrn[4] = 4;
        arrn[5] = 5;
        arrn[6] = 6;
        arrn[7] = 7;
        arrn[8] = 8;
        arrn[9] = 9;
        arrn[10] = 10;
        arrn[11] = 11;
        String[] arrstring2 = new String[12];
        String[] arrstring3 = new String[12];
        String[] arrstring4 = new String[12];
        for (n3 = 0; n3 < 12; ++n3) {
            arrstring = bl ? String.format("%02d", new int[]{0, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23}[n3]) : String.format("%d", (int)object[n3]);
            arrstring2[n3] = arrstring;
            arrstring3[n3] = String.format("%d", (int)object[n3]);
            arrstring4[n3] = String.format("%02d", new int[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55}[n3]);
        }
        object = this.mHourRadialTextsView;
        arrstring = bl ? arrstring3 : null;
        object.initialize(resources, arrstring2, arrstring, this.mHideAmPm, true);
        this.mHourRadialTextsView.invalidate();
        this.mMinuteRadialTextsView.initialize(resources, arrstring4, null, this.mHideAmPm, false);
        this.mMinuteRadialTextsView.invalidate();
        this.setValueForItem(0, n);
        this.setValueForItem(1, n2);
        this.mHourRadialSelectorView.initialize(context, this.mHideAmPm, bl, true, n % 12 * 30, this.isHourInnerCircle(n));
        this.mMinuteRadialSelectorView.initialize(context, this.mHideAmPm, false, false, n2 * 6, false);
        this.mTimeInitialized = true;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(4096);
        accessibilityNodeInfo.addAction(8192);
    }

    public void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getSize((int)n);
        n = View.MeasureSpec.getMode((int)n);
        int n4 = View.MeasureSpec.getSize((int)n2);
        n2 = View.MeasureSpec.getMode((int)n2);
        n3 = Math.min(n3, n4);
        super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)n3, (int)n), View.MeasureSpec.makeMeasureSpec((int)n3, (int)n2));
    }

    public boolean onTouch(final View arrboolean, MotionEvent motionEvent) {
        float f = motionEvent.getX();
        float f2 = motionEvent.getY();
        arrboolean = new Boolean[]{false};
        int n = motionEvent.getAction();
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    return false;
                }
                if (!this.mInputEnabled) {
                    Log.e((String)"RadialPickerLayout", (String)"Input was disabled, but received ACTION_MOVE.");
                    return true;
                }
                float f3 = Math.abs(f2 - this.mDownY);
                float f4 = Math.abs(f - this.mDownX);
                if (!this.mDoingMove && f4 <= (float)(n = this.TOUCH_SLOP) && f3 <= (float)n) {
                    return false;
                }
                n = this.mIsTouchingAmOrPm;
                if (n != 0 && n != 1) {
                    if (this.mDownDegrees == -1) {
                        return false;
                    }
                    this.mDoingMove = true;
                    this.mHandler.removeCallbacksAndMessages((Object)null);
                    n = this.getDegreesFromCoords(f, f2, true, arrboolean);
                    if (n != -1 && (n = this.reselectSelector(n, arrboolean[0], false, true)) != this.mLastValueSelected) {
                        this.mHapticFeedbackController.tryVibrate();
                        this.mLastValueSelected = n;
                        this.mListener.onValueSelected(this.getCurrentItemShowing(), n, false);
                    }
                    return true;
                }
                this.mHandler.removeCallbacksAndMessages((Object)null);
                if (this.mAmPmCirclesView.getIsTouchingAmOrPm(f, f2) != this.mIsTouchingAmOrPm) {
                    this.mAmPmCirclesView.setAmOrPmPressed(-1);
                    this.mAmPmCirclesView.invalidate();
                    this.mIsTouchingAmOrPm = -1;
                    return false;
                }
            } else {
                if (!this.mInputEnabled) {
                    Log.d((String)"RadialPickerLayout", (String)"Input was disabled, but received ACTION_UP.");
                    this.mListener.onValueSelected(3, 1, false);
                    return true;
                }
                this.mHandler.removeCallbacksAndMessages((Object)null);
                this.mDoingTouch = false;
                n = this.mIsTouchingAmOrPm;
                if (n != 0 && n != 1) {
                    if (this.mDownDegrees != -1 && (n = this.getDegreesFromCoords(f, f2, this.mDoingMove, arrboolean)) != -1) {
                        int n2;
                        n = n2 = this.reselectSelector(n, arrboolean[0], this.mDoingMove ^ true, false);
                        if (this.getCurrentItemShowing() == 0) {
                            n = n2;
                            if (!this.mIs24HourMode) {
                                int n3 = this.getIsCurrentlyAmOrPm();
                                if (n3 == 0 && n2 == 12) {
                                    n = 0;
                                } else {
                                    n = n2;
                                    if (n3 == 1) {
                                        n = n2;
                                        if (n2 != 12) {
                                            n = n2 + 12;
                                        }
                                    }
                                }
                            }
                        }
                        this.setValueForItem(this.getCurrentItemShowing(), n);
                        this.mListener.onValueSelected(this.getCurrentItemShowing(), n, true);
                    }
                    this.mDoingMove = false;
                    return true;
                }
                n = this.mAmPmCirclesView.getIsTouchingAmOrPm(f, f2);
                this.mAmPmCirclesView.setAmOrPmPressed(-1);
                this.mAmPmCirclesView.invalidate();
                if (n == this.mIsTouchingAmOrPm) {
                    this.mAmPmCirclesView.setAmOrPm(n);
                    if (this.getIsCurrentlyAmOrPm() != n) {
                        this.mListener.onValueSelected(2, this.mIsTouchingAmOrPm, false);
                        this.setValueForItem(2, n);
                    }
                }
                this.mIsTouchingAmOrPm = -1;
            }
            return false;
        }
        if (!this.mInputEnabled) {
            return true;
        }
        this.mDownX = f;
        this.mDownY = f2;
        this.mLastValueSelected = -1;
        this.mDoingMove = false;
        this.mDoingTouch = true;
        this.mIsTouchingAmOrPm = !this.mHideAmPm ? this.mAmPmCirclesView.getIsTouchingAmOrPm(f, f2) : -1;
        n = this.mIsTouchingAmOrPm;
        if (n != 0 && n != 1) {
            this.mDownDegrees = n = this.getDegreesFromCoords(f, f2, AccessibilityManagerCompat.isTouchExplorationEnabled(this.mAccessibilityManager), arrboolean);
            if (n != -1) {
                this.mHapticFeedbackController.tryVibrate();
                this.mHandler.postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        RadialPickerLayout.this.mDoingMove = true;
                        RadialPickerLayout radialPickerLayout = RadialPickerLayout.this;
                        int n = radialPickerLayout.reselectSelector(radialPickerLayout.mDownDegrees, arrboolean[0], false, true);
                        RadialPickerLayout.this.mLastValueSelected = n;
                        RadialPickerLayout.this.mListener.onValueSelected(RadialPickerLayout.this.getCurrentItemShowing(), n, false);
                    }
                }, (long)this.TAP_TIMEOUT);
                return true;
            }
        } else {
            this.mHapticFeedbackController.tryVibrate();
            this.mDownDegrees = -1;
            this.mHandler.postDelayed(new Runnable(){

                @Override
                public void run() {
                    RadialPickerLayout.this.mAmPmCirclesView.setAmOrPmPressed(RadialPickerLayout.this.mIsTouchingAmOrPm);
                    RadialPickerLayout.this.mAmPmCirclesView.invalidate();
                }
            }, (long)this.TAP_TIMEOUT);
        }
        return true;
    }

    public boolean performAccessibilityAction(int n, Bundle bundle) {
        if (super.performAccessibilityAction(n, bundle)) {
            return true;
        }
        int n2 = 0;
        if (n == 4096) {
            n2 = 1;
        } else if (n == 8192) {
            n2 = -1;
        }
        if (n2 != 0) {
            int n3;
            int n4 = this.getCurrentlyShowingValue();
            n = 0;
            int n5 = this.getCurrentItemShowing();
            if (n5 == 0) {
                n = 30;
                n3 = n4 % 12;
            } else {
                n3 = n4;
                if (n5 == 1) {
                    n = 6;
                    n3 = n4;
                }
            }
            n4 = this.snapOnly30s(n3 * n, n2) / n;
            n2 = 0;
            if (n5 == 0) {
                if (this.mIs24HourMode) {
                    n = 23;
                } else {
                    n = 12;
                    n2 = 1;
                }
            } else {
                n = 55;
            }
            if (n4 > n) {
                n3 = n2;
            } else {
                n3 = n4;
                if (n4 < n2) {
                    n3 = n;
                }
            }
            this.setItem(n5, n3);
            this.mListener.onValueSelected(n5, n3, false);
            return true;
        }
        return false;
    }

    public void setAmOrPm(int n) {
        this.mAmPmCirclesView.setAmOrPm(n);
        this.mAmPmCirclesView.invalidate();
        this.setValueForItem(2, n);
    }

    public void setCurrentItemShowing(int n, boolean bl) {
        if (n != 0 && n != 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TimePicker does not support view at index ");
            stringBuilder.append(n);
            Log.e((String)"RadialPickerLayout", (String)stringBuilder.toString());
            return;
        }
        int n2 = this.getCurrentItemShowing();
        this.mCurrentItemShowing = n;
        int n3 = 0;
        if (bl && n != n2) {
            Animator[] arranimator = new ObjectAnimator[4];
            if (n == 1) {
                arranimator[0] = this.mHourRadialTextsView.getDisappearAnimator();
                arranimator[1] = this.mHourRadialSelectorView.getDisappearAnimator();
                arranimator[2] = this.mMinuteRadialTextsView.getReappearAnimator();
                arranimator[3] = this.mMinuteRadialSelectorView.getReappearAnimator();
            } else if (n == 0) {
                arranimator[0] = this.mHourRadialTextsView.getReappearAnimator();
                arranimator[1] = this.mHourRadialSelectorView.getReappearAnimator();
                arranimator[2] = this.mMinuteRadialTextsView.getDisappearAnimator();
                arranimator[3] = this.mMinuteRadialSelectorView.getDisappearAnimator();
            }
            AnimatorSet animatorSet = this.mTransition;
            if (animatorSet != null && animatorSet.isRunning()) {
                this.mTransition.end();
            }
            this.mTransition = animatorSet = new AnimatorSet();
            animatorSet.playTogether(arranimator);
            this.mTransition.start();
            return;
        }
        n2 = n == 0 ? 255 : 0;
        if (n == 1) {
            n3 = 255;
        }
        ViewHelper.setAlpha(this.mHourRadialTextsView, n2);
        ViewHelper.setAlpha(this.mHourRadialSelectorView, n2);
        ViewHelper.setAlpha(this.mMinuteRadialTextsView, n3);
        ViewHelper.setAlpha(this.mMinuteRadialSelectorView, n3);
    }

    public void setOnValueSelectedListener(OnValueSelectedListener onValueSelectedListener) {
        this.mListener = onValueSelectedListener;
    }

    void setTheme(TypedArray typedArray) {
        this.mCircleView.setTheme(typedArray);
        this.mAmPmCirclesView.setTheme(typedArray);
        this.mHourRadialTextsView.setTheme(typedArray);
        this.mMinuteRadialTextsView.setTheme(typedArray);
        this.mHourRadialSelectorView.setTheme(typedArray);
        this.mMinuteRadialSelectorView.setTheme(typedArray);
    }

    public void setTime(int n, int n2) {
        this.setItem(0, n);
        this.setItem(1, n2);
    }

    public boolean trySettingInputEnabled(boolean bl) {
        boolean bl2 = this.mDoingTouch;
        int n = 0;
        if (bl2 && !bl) {
            return false;
        }
        this.mInputEnabled = bl;
        View view = this.mGrayBox;
        if (bl) {
            n = 4;
        }
        view.setVisibility(n);
        return true;
    }

    public static interface OnValueSelectedListener {
        public void onValueSelected(int var1, int var2, boolean var3);
    }

}

